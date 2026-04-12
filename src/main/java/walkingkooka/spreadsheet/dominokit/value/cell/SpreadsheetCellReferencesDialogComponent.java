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

package walkingkooka.spreadsheet.dominokit.value.cell;

import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.delta.SpreadsheetDeltaCellsTableComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogAnchorListComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenOffsetAndCount;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellReferenceListHistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.value.SpreadsheetCell;

import java.util.Objects;

/**
 * A modal dialog that displays the refreshReferences for the selected cells.
 */
public final class SpreadsheetCellReferencesDialogComponent implements DialogComponentLifecycle,
    LoadedSpreadsheetMetadataRequired {

    /**
     * Creates a new {@link SpreadsheetCellReferencesDialogComponent}.
     */
    public static SpreadsheetCellReferencesDialogComponent with(final SpreadsheetCellReferencesDialogComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetCellReferencesDialogComponent(context);
    }

    private SpreadsheetCellReferencesDialogComponent(final SpreadsheetCellReferencesDialogComponentContext context) {
        this.context = context;

        this.links = this.links();

        this.table = SpreadsheetDeltaCellsTableComponent.with(
            ID_PREFIX,
            context // SpreadsheetDeltaCellsTableComponentContext
        );

        this.dialog = this.dialogCreate();

        context.addHistoryTokenWatcher(this);
    }

    // dialog...........................................................................................................

    /**
     * Creates the modal dialog, with a table showing the refreshReferences and the links such as CLOSE.
     */
    private DialogComponent dialogCreate() {
        final SpreadsheetCellReferencesDialogComponentContext context = this.context;

        return DialogComponent.largeList(
                ID + SpreadsheetElementIds.DIALOG,
                DialogComponent.INCLUDE_CLOSE,
                context
            ).appendChild(this.table)
            .appendChild(this.links);
    }

    private final DialogComponent dialog;

    private final SpreadsheetCellReferencesDialogComponentContext context;

    // table............................................................................................................

    private void refreshTable(final HistoryToken historyToken) {
        this.table.refresh(historyToken);
    }

    // @VisibleForTesting.
    final SpreadsheetDeltaCellsTableComponent table;

    // links............................................................................................................

    private DialogAnchorListComponent<?> links() {
        return this.dialogAnchorListComponent(this.context)
            .close();
    }

    private final DialogAnchorListComponent<?> links;

    // DialogComponentLifecycle.........................................................................................

    @Override
    public DialogComponent dialog() {
        return this.dialog;
    }

    @Override
    public String idPrefix() {
        return ID_PREFIX;
    }

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return false;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetCellReferenceListHistoryToken;
    }

    @Override
    public void dialogReset() {
        this.table.clear();
    }

    @Override
    public void openGiveFocus(final RefreshContext context) {
        // NOP
    }

    /**
     * A change in history token probably means the selected cells, offset or count changed so refresh links and fetch
     * the data for the table.
     */
    @Override
    public void refresh(final RefreshContext context) {
        this.context.refreshDialogTitle(this);

        final HistoryToken historyToken = context.historyToken();

        this.refreshTable(historyToken);

        this.refreshReferences(historyToken.offsetAndCount());
    }

    private void refreshReferences(final HistoryTokenOffsetAndCount offsetAndCount) {
        final SpreadsheetCellReferencesDialogComponentContext context = this.context;

        final SpreadsheetCellReferenceListHistoryToken historyToken = context.historyToken()
            .cast(SpreadsheetCellReferenceListHistoryToken.class);

        final SpreadsheetId id = historyToken.spreadsheetId();
        final SpreadsheetCellRangeReference cells = historyToken.anchoredSelection()
            .selection()
            .toCellRange();

        context.loadCellReferences(
            id,
            cells,
            offsetAndCount
        );
    }

    @Override
    public boolean shouldLogLifecycleChanges() {
        return SPREADSHEET_CELL_REFERENCES_DIALOG_COMPONENT;
    }

    // UI...............................................................................................................

    private final static String ID = SpreadsheetCell.class.getSimpleName() + "References";

    private final static String ID_PREFIX = ID + "-";

    // Object..........................................................................................................

    @Override
    public String toString() {
        return this.dialog.toString();
    }
}
