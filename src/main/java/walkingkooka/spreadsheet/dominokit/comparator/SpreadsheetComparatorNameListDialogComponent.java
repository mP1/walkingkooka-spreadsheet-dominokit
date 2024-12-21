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

package walkingkooka.spreadsheet.dominokit.comparator;

import walkingkooka.spreadsheet.compare.SpreadsheetComparatorNameList;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.flex.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A modal dialog that supports editing a {@link SpreadsheetComparatorNameList}.
 */
public final class SpreadsheetComparatorNameListDialogComponent implements SpreadsheetDialogComponentLifecycle,
        LoadedSpreadsheetMetadataRequired,
        NopFetcherWatcher,
        NopEmptyResponseFetcherWatcher,
        SpreadsheetMetadataFetcherWatcher {

    /**
     * Creates a new {@link SpreadsheetComparatorNameListDialogComponent}.
     */
    public static SpreadsheetComparatorNameListDialogComponent with(final SpreadsheetComparatorNameListDialogComponentContext context) {
        return new SpreadsheetComparatorNameListDialogComponent(
                Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetComparatorNameListDialogComponent(final SpreadsheetComparatorNameListDialogComponentContext context) {
        this.context = context;
        context.addHistoryTokenWatcher(this);

        context.addSpreadsheetMetadataFetcherWatcher(this);

        this.textBox = this.textBox();

        this.save = this.anchor("Save");
        this.undo = this.anchor("Undo");
        this.clear = this.anchor("Clear");
        this.close = this.closeAnchor();

        this.dialog = this.dialogCreate();
    }

    // ids..............................................................................................................

    @Override
    public String idPrefix() {
        return ID + "-";
    }

    private final static String ID = "spreadsheetComparatorNameList";

    // dialog...........................................................................................................

    /**
     * Creates the modal dialog, loaded with the {@link SpreadsheetComparatorNameList} textbox and some links.
     */
    private SpreadsheetDialogComponent dialogCreate() {
        final SpreadsheetComparatorNameListDialogComponentContext context = this.context;

        SpreadsheetDialogComponent dialog = SpreadsheetDialogComponent.with(
                ID + SpreadsheetElementIds.DIALOG,
                context.dialogTitle(),
                true, // includeClose
                context
        );

        return dialog.appendChild(this.textBox)
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

    private final SpreadsheetComparatorNameListDialogComponentContext context;

    // textBox..........................................................................................................

    /**
     * Creates a text box to edit the {@link SpreadsheetComparatorNameList} and installs a few value change type listeners
     */
    private SpreadsheetComparatorNameListComponent textBox() {
        return SpreadsheetComparatorNameListComponent.empty()
                .setId(ID + SpreadsheetElementIds.TEXT_BOX)
                .addKeyupListener(
                        (e) -> this.onTextBox(this.text())
                ).addChangeListener(
                        (oldValue, newValue) -> this.onTextBox(this.text())
                );
    }

    /**
     * Handles updates to the {@link SpreadsheetComparatorNameListComponent}
     */
    private void onTextBox(final String text) {
        this.save.setHistoryToken(
                Optional.of(
                        this.context.historyToken()
                                .save(text)
                )
        );
    }

    /**
     * Retrieves the current {@link SpreadsheetComparatorNameList}.
     */
    private String text() {
        return this.textBox.stringValue()
                .orElse("");
    }

    // @VisibleForTesting
    void setText(final String text) {
        this.textBox.setStringValue(
                Optional.of(text)
        );
        this.onTextBox(text);
    }

    /**
     * The {@link SpreadsheetComparatorNameListComponent} that holds the {@link SpreadsheetComparatorNameList} in text form.
     */
    private final SpreadsheetComparatorNameListComponent textBox;

    // dialog links.....................................................................................................

    /**
     * A SAVE link which will be updated each time the {@link #textBox} is also updated.
     */
    private final HistoryTokenAnchorComponent save;

    /**
     * A UNDO link which will be updated each time the {@link SpreadsheetComparatorNameList} is saved.
     */
    private final HistoryTokenAnchorComponent undo;

    /**
     * A CLEAR link which will save an empty {@link SpreadsheetComparatorNameList}.
     */
    private final HistoryTokenAnchorComponent clear;

    /**
     * A CLOSE link which will close the dialog.
     */
    private final HistoryTokenAnchorComponent close;

    // SpreadsheetMetadataFetcherWatcher................................................................................
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

    // HistoryTokenAwareComponentLifecycle..............................................................................

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
    public void dialogReset() {
        // NOP
    }

    @Override
    public void openGiveFocus(final AppContext context) {
        context.giveFocus(
                this.textBox::focus
        );
    }

    /**
     * Refreshes the widget, typically done when the {@link SpreadsheetPatternKind} changes etc.
     */
    @Override
    public void refresh(final AppContext context) {
        final String undo = this.context.undo();
        this.setText(undo);

        this.undo.setHistoryToken(
                Optional.of(
                        context.historyToken()
                                .save(undo)
                )
        );

        this.refreshTitleAndLinks();
    }

    private void refreshTitleAndLinks() {
        final SpreadsheetComparatorNameListDialogComponentContext context = this.context;

        final HistoryToken historyToken = context.historyToken();

        this.dialog.setTitle(
                context.dialogTitle()
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
}
