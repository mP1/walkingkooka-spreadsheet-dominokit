/*
 * Copyright 2023 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.spreadsheet.dominokit.ui.pattern;

import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.net.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.NopNoResponseWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.ui.flexlayout.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.ui.textbox.SpreadsheetTextBox;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CharSequences;

import java.util.Optional;
import java.util.Set;

/**
 * A modal dialog with a text box that allows user entry of a {@link SpreadsheetPattern pattern}.
 * Buttons are available along the bottom that support SAVE, UNDO and CLOSE.
 */
public abstract class SpreadsheetPatternDialogComponent implements SpreadsheetDialogComponentLifecycle,
        LoadedSpreadsheetMetadataRequired,
        NopFetcherWatcher,
        NopNoResponseWatcher,
        SpreadsheetDeltaFetcherWatcher,
        SpreadsheetMetadataFetcherWatcher {

    /**
     * Creates a new {@link SpreadsheetPatternDialogComponentFormat}.
     */
    public static SpreadsheetPatternDialogComponent format(final SpreadsheetPatternDialogComponentContext context) {
        return SpreadsheetPatternDialogComponentFormat.with(context);
    }

    /**
     * Creates a new {@link SpreadsheetPatternDialogComponentParse}.
     */
    public static SpreadsheetPatternDialogComponent parse(final SpreadsheetPatternDialogComponentContext context) {
        return SpreadsheetPatternDialogComponentParse.with(context);
    }

    SpreadsheetPatternDialogComponent(final SpreadsheetPatternDialogComponentContext context) {
        this.context = context;
        context.addHistoryTokenWatcher(this);
        context.addSpreadsheetDeltaFetcherWatcher(this);
        context.addSpreadsheetMetadataFetcherWatcher(this);

        this.tabs = SpreadsheetPatternComponentTabs.empty(
                this.spreadsheetPatternKinds(),
                context
        );

        this.patternTextBox = this.patternTextBox();

        this.elementRemover = SpreadsheetPatternComponentElementRemover.empty();

        this.elementAppender = SpreadsheetPatternComponentElementAppender.empty();

        this.table = SpreadsheetPatternComponentTable.empty();

        this.save = this.anchor("Save")
                .setDisabled(true);
        this.undo = this.anchor("Undo")
                .setDisabled(true);
        this.clear = this.anchor("Clear")
                .setDisabled(true);
        this.close = this.anchor("Close")
                .setDisabled(true);

        this.dialog = this.dialogCreate();
    }

    // dialog...........................................................................................................

    /**
     * Creates the modal dialog, loaded with the pattern textbox and some buttons.
     */
    private SpreadsheetDialogComponent dialogCreate() {
        final SpreadsheetPatternDialogComponentContext context = this.context;

        return SpreadsheetDialogComponent.with(
                        ID,
                        "Pattern", // will be replaced by refresh when "opened"
                        true, // includeClose
                        context
                ).appendChild(this.tabs)
                .appendChild(this.table)
                .appendChild(this.elementAppender)
                .appendChild(this.elementRemover)
                .appendChild(this.patternTextBox)
                .appendChild(
                        SpreadsheetFlexLayout.row()
                                .appendChild(this.save)
                                .appendChild(this.undo)
                                .appendChild(this.clear)
                                .appendChild(this.close)
                );
    }

    @Override
    public final SpreadsheetDialogComponent dialog() {
        return this.dialog;
    }

    private final SpreadsheetDialogComponent dialog;

    private final SpreadsheetPatternDialogComponentContext context;

    // ids..............................................................................................................

    @Override
    public final String idPrefix() {
        return ID_PREFIX;
    }

    /**
     * The ID assigned to the container TABLE element.
     */
    private final static String ID = "pattern";

    final static String ID_PREFIX = ID + "-";

    // tabs............................................................................................................

    private final SpreadsheetPatternComponentTabs tabs;

    // sample...........................................................................................................

    private final SpreadsheetPatternComponentTable table;

    // componentChips...................................................................................................

    private final SpreadsheetPatternComponentElementRemover elementRemover;

    // patternAppendLinks......................................................................................................

    private final SpreadsheetPatternComponentElementAppender elementAppender;

    // patternTextBox...................................................................................................

    /**
     * Creates the pattern text box and installs a value change listener.
     */
    private SpreadsheetTextBox patternTextBox() {
        return SpreadsheetTextBox.empty()
                .setId(ID_PREFIX + "TextBox")
                .disableSpellcheck()
                .clearIcon()
                .addKeyupListener(
                        (e) -> this.onPatternTextBox(this.patternText())
                ).addChangeListener(
                        (oldValue, newValue) -> this.onPatternTextBox(this.patternText())
                );
    }

    /**
     * Tries to parse the text box text into a {@link SpreadsheetPattern}.
     * If that fails an error message will be displayed and the SAVE button disabled.
     */
    private void onPatternTextBox(final String patternText) {
        final SpreadsheetPatternDialogComponentContext context = this.context;
        final SpreadsheetPatternKind patternKind = context.patternKind();
        final SpreadsheetTextBox patternTextBox = this.patternTextBox;
        context.debug(this.getClass().getSimpleName() + ".onPatternTextBox " + CharSequences.quoteAndEscape(patternText));

        SpreadsheetPattern pattern = null;
        String errorMessage = null;
        String errorPattern = ""; // the pattern text after the bestParse, so for "@@@\"Hello" pattern will be "@@@" and errorPattern=\"Hello"

        try {
            pattern = patternKind.parse(patternText);
        } catch (final IllegalArgumentException invalid) {
            errorMessage = invalid.getMessage();

            errorPattern = patternText.substring(
                    CharSequences.bestParse(
                            patternText,
                            patternKind::parse
                    )
            );

            try {
                pattern = patternKind.parse(errorPattern);
            } catch (final IllegalArgumentException bestInvalid) {
                errorPattern = patternText;
            }
        }

        // clear or update the errors
        patternTextBox.setHelperText(
                Optional.ofNullable(errorMessage)
        );

        this.elementRemover.refresh(
                pattern,
                errorPattern,
                context
        );

        this.elementAppender.refreshLinks(
                patternText,
                CharSequences.nullToEmpty(
                        errorMessage
                ).length() > 0 ?
                        null :
                        pattern,
                context
        );

        this.table.refresh(
                patternText,
                context
        );

        this.save.setHistoryToken(
                Optional.of(
                        context.historyToken()
                                .setSave(
                                        Optional.ofNullable(pattern)
                        )
                )
        );
    }

    /**
     * Retrieves the current pattern.
     */
    private String patternText() {
        return this.patternTextBox.value()
                .orElse("");
    }

    // @VisibleForTesting
    void setPatternText(final String patternText) {
        this.patternTextBox.setValue(
                Optional.of(patternText)
        );
        this.onPatternTextBox(patternText);
    }

    /**
     * The {@link SpreadsheetTextBox} that holds the pattern in text form.
     */
    private final SpreadsheetTextBox patternTextBox;

    /**
     * A SAVE link which will be updated each time the pattern box is also updated.
     */
    private final HistoryTokenAnchorComponent save;

    /**
     * A UNDO link which will be updated each time the pattern is saved.
     */
    private final HistoryTokenAnchorComponent undo;

    /**
     * A CLEAR link which will save an empty pattern.
     */
    private final HistoryTokenAnchorComponent clear;

    /**
     * A CLOSE link which will close the dialog.
     */
    private final HistoryTokenAnchorComponent close;

    // SpreadsheetDeltaFetcherWatcher..........................................................................................

    @Override
    public final void onSpreadsheetDelta(final HttpMethod method,
                                         final AbsoluteOrRelativeUrl url,
                                         final SpreadsheetDelta delta,
                                         final AppContext context) {
        this.refreshIfOpen(context);
    }

    // SpreadsheetMetadataFetcherWatcher..........................................................................................

    @Override
    public final void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                            final AppContext context) {
        this.refreshIfOpen(context);
    }

    @Override
    public final void onSpreadsheetMetadataSet(final Set<SpreadsheetMetadata> metadatas,
                                               final AppContext context) {
        // Ignore many
    }

    // ComponentLifecycle...............................................................................................

    // save should not open or close the dialog.
    @Override
    public final boolean shouldIgnore(final HistoryToken token) {
        return this.context.shouldIgnore(token);
    }

    @Override
    public final boolean isMatch(final HistoryToken token) {
        return this.context.isMatch(token);
    }

    @Override
    public final void openGiveFocus(final AppContext context) {
        context.giveFocus(
                this.patternTextBox::focus
        );
    }

    /**
     * Refreshes the widget, typically done when the {@link SpreadsheetPatternKind} changes etc.
     */
    @Override
    public final void refresh(final AppContext context) {
        final SpreadsheetPatternDialogComponentContext componentContext = this.context;

        this.dialog.setTitle(
                title(
                        componentContext.patternKind()
                )
        );
        this.tabs.refresh(
                this.spreadsheetPatternKinds(),
                componentContext
        );
        this.elementAppender.refresh(
                this::setPatternText,
                componentContext
        );

        final Optional<SpreadsheetPattern> pattern = componentContext.undo();
        this.setPatternText(
                pattern.map(
                        SpreadsheetPattern::text)
                .orElse("")
        );

        final HistoryToken historyToken = context.historyToken();

        this.undo.setHistoryToken(
                Optional.of(
                        historyToken.setSave(pattern)
                )
        );

        this.clear.setHistoryToken(
                Optional.of(
                        historyToken.clearSave()
                )
        );

        this.close.setHistoryToken(
                Optional.of(
                        historyToken.close()
                )
        );
    }

    abstract SpreadsheetPatternKind[] spreadsheetPatternKinds();

    // Date/Time format
    // Text format
    static String title(final SpreadsheetPatternKind kind) {
        return CaseKind.SNAKE.change(
                kind.name(),
                CaseKind.TITLE
        ).replace("Pattern", "pattern");
    }

    // @VisibleForTesting
    static String spreadsheetPatternKindId(final SpreadsheetPatternKind kind) {
        return ID_PREFIX +
                CaseKind.SNAKE.change(
                        kind.name(),
                        CaseKind.KEBAB
                ).replace("-pattern", "");
    }
}
