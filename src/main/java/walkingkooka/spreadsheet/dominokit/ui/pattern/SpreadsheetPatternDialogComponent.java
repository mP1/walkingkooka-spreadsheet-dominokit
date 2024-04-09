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

import org.dominokit.domino.ui.utils.ElementsFactory;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.net.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.NopNoResponseWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.ui.textbox.SpreadsheetTextBox;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Optional;

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
        context.addSpreadsheetDeltaWatcher(this);
        context.addSpreadsheetMetadataWatcher(this);

        this.tabs = SpreadsheetPatternComponentTabs.empty(
                this.spreadsheetPatternKinds(),
                context
        );

        this.patternTextBox = this.patternTextBox();

        this.elements = SpreadsheetPatternComponentElements.empty();

        this.appendLinks = SpreadsheetPatternComponentAppenderComponent.empty();

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

        final SpreadsheetDialogComponent dialog = SpreadsheetDialogComponent.with(
                ID,
                "Pattern", // will be replaced by refresh when "opened"
                true, // includeClose
                context
        );

        dialog.appendChild(this.tabs);

        dialog.appendChild(
                this.table
        );

        dialog.appendChild(this.elements);
        dialog.appendChild(this.appendLinks);

        final SpreadsheetTextBox patternTextBox = this.patternTextBox;
        dialog.appendChild(patternTextBox);

        dialog.appendChild(
                ElementsFactory.elements.div()
                        .appendChild(this.save)
                        .appendChild(this.undo)
                        .appendChild(this.clear)
                        .appendChild(this.close)
        );

        return dialog;
    }

    @Override
    public final SpreadsheetDialogComponent dialog() {
        return this.dialog;
    }

    private final SpreadsheetDialogComponent dialog;

    private final SpreadsheetPatternDialogComponentContext context;

    @Override
    public final String idPrefix() {
        return ID_PREFIX;
    }

    // tabs............................................................................................................

    private final SpreadsheetPatternComponentTabs tabs;

    // sample...........................................................................................................

    private final SpreadsheetPatternComponentTable table;

    // componentChips...................................................................................................

    private final SpreadsheetPatternComponentElements elements;

    // patternAppendLinks......................................................................................................

    private final SpreadsheetPatternComponentAppenderComponent appendLinks;

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
        String errorPattern = "";

        final int patternTextLength = patternText.length();
        int last = patternTextLength;

        while (last > 0) {
            final String tryingPatternText = patternText.substring(0, last);
            context.debug(this.getClass().getSimpleName() + ".onPatternTextBox trying to parse " + CharSequences.quoteAndEscape(tryingPatternText));

            // try parsing...
            try {
                pattern = patternKind.parse(tryingPatternText);
                errorPattern = patternText.substring(last);
                break; // best attempt stop

            } catch (final Exception failed) {
                if (null == errorMessage) {
                    errorMessage = failed.getMessage();
                }

                last--;
                context.debug(this.getClass().getSimpleName() + ".onPatternTextBox parsing failed " + CharSequences.quoteAndEscape(tryingPatternText), failed);
            }
        }

        context.debug(this.getClass().getSimpleName() + ".onPatternTextBox " + CharSequences.quoteAndEscape(patternText) + " errorMessage: " + errorMessage + " pattern: " + pattern);

        // clear or update the errors
        patternTextBox.setHelperText(
                Optional.ofNullable(errorMessage)
        );

        this.elements.refresh(
                pattern,
                errorPattern,
                context
        );

        this.appendLinks.refreshLinks(
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

    private void setPatternText(final String patternText) {
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
    public final void onSpreadsheetDelta(final SpreadsheetId id,
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
    public final void onSpreadsheetMetadataList(final List<SpreadsheetMetadata> metadatas,
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
        this.appendLinks.recreate(
                this::setPatternText,
                componentContext
        );

        final Optional<SpreadsheetPattern> pattern = componentContext.undo();
        this.setPatternText(pattern.map(
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

    // ids..............................................................................................................

    /**
     * The ID assigned to the container TABLE element.
     */
    private final static String ID = "pattern";

    final static String ID_PREFIX = ID + "-";

    // @VisibleForTesting
    static String spreadsheetPatternKindId(final SpreadsheetPatternKind kind) {
        return ID_PREFIX +
                CaseKind.SNAKE.change(
                        kind.name(),
                        CaseKind.KEBAB
                ).replace("-pattern", "");
    }
}
