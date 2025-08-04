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

import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcher;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherDelegator;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.delta.SpreadsheetDeltaLabelsTableComponent;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenOffsetAndCount;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetIdHistoryToken;
import walkingkooka.spreadsheet.dominokit.link.AnchorListComponent;

import java.util.Objects;
import java.util.Optional;

/**
 * A modal dialog that displays a list of {@link walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping}.
 */
public final class SpreadsheetLabelMappingListDialogComponent implements SpreadsheetDialogComponentLifecycle,
    LoadedSpreadsheetMetadataRequired,
    ComponentLifecycleMatcherDelegator {

    /**
     * Creates a new {@link SpreadsheetLabelMappingListDialogComponent}.
     */
    public static SpreadsheetLabelMappingListDialogComponent with(final SpreadsheetLabelMappingListDialogComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetLabelMappingListDialogComponent(context);
    }

    private SpreadsheetLabelMappingListDialogComponent(final SpreadsheetLabelMappingListDialogComponentContext context) {
        this.context = context;

        this.create = SpreadsheetLabelCreateAnchorComponent.with(
            HistoryTokenAnchorComponent.empty(),
            ID_PREFIX + "create" + SpreadsheetElementIds.LINK, // id,
            context // HistoryTokenContext
        );
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
        final SpreadsheetLabelMappingListDialogComponentContext context = this.context;

        return SpreadsheetDialogComponent.largeList(
                ID + SpreadsheetElementIds.DIALOG,
                SpreadsheetDialogComponent.INCLUDE_CLOSE,
                context
            ).appendChild(this.table)
            .appendChild(
                AnchorListComponent.empty()
                    .setCssProperty("margin-top", "5px")
                    .setCssProperty("margin-left", "-5px")
                    .appendChild(this.create)
                    .appendChild(this.close)
            );
    }

    private final SpreadsheetDialogComponent dialog;

    private final SpreadsheetLabelMappingListDialogComponentContext context;

    // table............................................................................................................

    private void refreshTable(final HistoryToken token) {
        this.table.refresh(token);
    }

    // @VisibleForTesting.
    final SpreadsheetDeltaLabelsTableComponent table;

    // create...........................................................................................................

    private void refreshCreate() {
        this.create.clearValue()
            .setTextContent("Create");
    }

    private final SpreadsheetLabelCreateAnchorComponent create;

    // close............................................................................................................

    private void refreshClose(final HistoryToken token) {
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
    public ComponentLifecycleMatcher componentLifecycleMatcher() {
        return this.context;
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
        this.context.refreshDialogTitle(this);

        final HistoryToken historyToken = context.historyToken();

        this.refreshCreate();
        this.refreshClose(historyToken);

        this.refreshTable(historyToken);

        this.loadLabelMappings(
            historyToken.offsetAndCount()
        );
    }

    private void loadLabelMappings(final HistoryTokenOffsetAndCount offsetAndCount) {
        this.context.loadLabelMappings(
            this.context.historyToken()
                .cast(SpreadsheetIdHistoryToken.class)
                .id(),
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
