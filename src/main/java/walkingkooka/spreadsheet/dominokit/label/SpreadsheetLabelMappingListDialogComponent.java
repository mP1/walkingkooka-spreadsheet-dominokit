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

package walkingkooka.spreadsheet.dominokit.label;

import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.delta.SpreadsheetDeltaLabelsTableComponent;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenOffsetAndCount;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetLabelMappingListHistoryToken;
import walkingkooka.spreadsheet.dominokit.link.SpreadsheetLinkListComponent;

import java.util.Objects;
import java.util.Optional;

/**
 * A modal dialog that displays a list of {@link walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping}.
 */
public final class SpreadsheetLabelMappingListDialogComponent implements SpreadsheetDialogComponentLifecycle,
    LoadedSpreadsheetMetadataRequired {

    /**
     * Creates a new {@link SpreadsheetLabelMappingListDialogComponent}.
     */
    public static SpreadsheetLabelMappingListDialogComponent with(final SpreadsheetLabelMappingListDialogComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetLabelMappingListDialogComponent(context);
    }

    private SpreadsheetLabelMappingListDialogComponent(final SpreadsheetLabelMappingListDialogComponentContext context) {
        this.context = context;

        this.close = this.closeAnchor();

        this.table = SpreadsheetDeltaLabelsTableComponent.with(
            ID_PREFIX,
            context // SpreadsheetDeltaLabelsTableComponentContext
        );

        this.dialog = this.dialogCreate();

        context.addHistoryTokenWatcher(this);
    }

    // dialog...........................................................................................................

    /**
     * Creates the modal dialog, with a table showing the label mappings and the links such as CLOSE.
     */
    private SpreadsheetDialogComponent dialogCreate() {
        final HistoryContext context = this.context;

        return SpreadsheetDialogComponent.largeList(
                ID + SpreadsheetElementIds.DIALOG,
                "Labels",
                true, // includeClose
                context
            ).appendChild(this.table)
            .appendChild(
                SpreadsheetLinkListComponent.empty()
                    .setCssProperty("margin-top", "5px")
                    .setCssProperty("margin-left", "-5px")
                    .appendChild(this.close)
            );
    }

    private final SpreadsheetDialogComponent dialog;

    private final SpreadsheetLabelMappingListDialogComponentContext context;

    // table............................................................................................................

    private void refreshTable(final SpreadsheetLabelMappingListHistoryToken token) {
        this.table.refresh(token);
    }

    // @VisibleForTesting.
    final SpreadsheetDeltaLabelsTableComponent table;

    // close............................................................................................................

    private void refreshClose(final SpreadsheetLabelMappingListHistoryToken token) {
        this.close.setHistoryToken(
            Optional.of(token.close())
        );
    }

    private final HistoryTokenAnchorComponent close;

    // SpreadsheetDialogComponentLifecycle..............................................................................

    @Override
    public SpreadsheetDialogComponent dialog() {
        return this.dialog;
    }

    @Override
    public String idPrefix() {
        return ID_PREFIX;
    }

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
        this.table.clear();
    }

    @Override
    public void openGiveFocus(final RefreshContext context) {
        // NOP
    }

    /**
     * A change in history token probably means the offset or count changed so refresh links and fetch.
     */
    @Override
    public void refresh(final RefreshContext context) {
        final SpreadsheetLabelMappingListHistoryToken historyToken = context.historyToken()
            .cast(SpreadsheetLabelMappingListHistoryToken.class);

        this.refreshClose(historyToken);

        this.refreshTable(historyToken);

        this.loadLabelMappings(
            HistoryTokenOffsetAndCount.with(
                historyToken.offset(),
                historyToken.count()
            )
        );
    }

    private void loadLabelMappings(final HistoryTokenOffsetAndCount offsetAndCount) {
        final SpreadsheetLabelMappingListHistoryToken historyToken = this.context.historyToken()
            .cast(SpreadsheetLabelMappingListHistoryToken.class);

        this.context.loadLabelMappings(
            historyToken.id(),
            offsetAndCount
        );
    }

    // UI...............................................................................................................

    private final static String ID = "labels";

    private final static String ID_PREFIX = ID + "-";

    // Object..........................................................................................................

    @Override
    public String toString() {
        return this.dialog.toString();
    }
}
