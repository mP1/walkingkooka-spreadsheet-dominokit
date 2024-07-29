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

package walkingkooka.spreadsheet.dominokit.format;

import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.net.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.NopNoResponseWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.ui.textbox.SpreadsheetTextBox;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelector;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A modal dialog with a text box that allows user entry of a {@link SpreadsheetPattern pattern}.
 * Buttons are available along the bottom that support SAVE, UNDO and CLOSE.
 */
public final class SpreadsheetPatternDialogComponent implements SpreadsheetDialogComponentLifecycle,
        LoadedSpreadsheetMetadataRequired,
        NopFetcherWatcher,
        NopNoResponseWatcher,
        SpreadsheetDeltaFetcherWatcher,
        SpreadsheetMetadataFetcherWatcher {

    /**
     * Creates a new {@link SpreadsheetPatternDialogComponent}.
     */
    public static SpreadsheetPatternDialogComponent with(final SpreadsheetPatternDialogComponentContext context) {
        return new SpreadsheetPatternDialogComponent(
                Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetPatternDialogComponent(final SpreadsheetPatternDialogComponentContext context) {
        this.context = context;
        context.addHistoryTokenWatcher(this);
        context.addSpreadsheetDeltaFetcherWatcher(this);
        context.addSpreadsheetMetadataFetcherWatcher(this);

        this.tabs = SpreadsheetPatternComponentTabs.empty(
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

        // patternKind might not always be present so default to empty title when absent. title will be non empty when it is actually shown.
        return SpreadsheetDialogComponent.with(
                        ID,
                        context.historyToken()
                                .patternKind()
                                .map(SpreadsheetPatternDialogComponent::title)
                                .orElse(""),
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
    public SpreadsheetDialogComponent dialog() {
        return this.dialog;
    }

    private final SpreadsheetDialogComponent dialog;

    private final SpreadsheetPatternDialogComponentContext context;

    // ids..............................................................................................................

    @Override
    public String idPrefix() {
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
    private void onPatternTextBox(final String text) {
        final SpreadsheetPatternDialogComponentContext context = this.context;
        final SpreadsheetPatternKind patternKind = context.patternKind();
        final SpreadsheetTextBox patternTextBox = this.patternTextBox;
        context.debug(this.getClass().getSimpleName() + ".onPatternTextBox " + CharSequences.quoteAndEscape(text));

        String patternText = "";
        SpreadsheetPattern pattern = null;
        String errorMessage = null;
        String errorPattern = ""; // the pattern text after the bestParse, so for "@@@\"Hello" pattern will be "@@@" and errorPattern=\"Hello"

        try {
            if (patternKind.isFormatPattern()) {
                final SpreadsheetFormatterSelector selector = SpreadsheetFormatterSelector.parse(text);
                patternText = selector.text();
            } else {
                final SpreadsheetParserSelector selector = SpreadsheetParserSelector.parse(text);
                patternText = selector.text();
            }

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
    public void onSpreadsheetDelta(final HttpMethod method,
                                   final AbsoluteOrRelativeUrl url,
                                   final SpreadsheetDelta delta,
                                   final AppContext context) {
        this.refreshIfOpen(context);
    }

    // SpreadsheetMetadataFetcherWatcher..........................................................................................

    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        this.refreshIfOpen(context);
    }

    @Override
    public void onSpreadsheetMetadataSet(final Set<SpreadsheetMetadata> metadatas,
                                         final AppContext context) {
        // Ignore many
    }

    // ComponentLifecycle...............................................................................................

    // save should not open or close the dialog.
    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return this.context.shouldIgnore(token);
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return this.context.isMatch(token);
    }

    @Override
    public void openGiveFocus(final AppContext context) {
        context.giveFocus(
                this.patternTextBox::focus
        );
    }

    /**
     * Refreshes the widget, typically done when the {@link SpreadsheetPatternKind} changes etc.
     */
    @Override
    public void refresh(final AppContext context) {
        final SpreadsheetPatternDialogComponentContext componentContext = this.context;

        this.dialog.setTitle(
                title(
                        componentContext.patternKind()
                )
        );
        this.tabs.refresh(
                componentContext
        );
        this.elementAppender.refresh(
                this::setPatternText,
                componentContext
        );

        final String undo = componentContext.undo();
        this.setPatternText(undo);

        final HistoryToken historyToken = context.historyToken();

        this.undo.setHistoryToken(
                Optional.of(
                        historyToken.setSave(undo)
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

    // Date/Time format
    // Text format
    static String title(final SpreadsheetPatternKind kind) {
        return CaseKind.SNAKE.change(
                kind.name(),
                CaseKind.TITLE
                ).replace("Pattern", "")
                .replace("Format", "formatter")
                .replace("Parse", "parser")
                .trim();
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
