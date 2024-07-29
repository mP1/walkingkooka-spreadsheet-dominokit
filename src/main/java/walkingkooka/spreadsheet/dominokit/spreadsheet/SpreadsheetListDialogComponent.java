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

package walkingkooka.spreadsheet.dominokit.spreadsheet;

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetListDeleteHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetListHistoryToken;
import walkingkooka.spreadsheet.dominokit.net.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.NopNoResponseWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIcons;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

/**
 * A dialog that displays a table with a listing of spreadsheets. Controls are present to open, delete and create
 * spreadsheets.
 */
public final class SpreadsheetListDialogComponent implements SpreadsheetDialogComponentLifecycle,
        SpreadsheetMetadataFetcherWatcher,
        NopFetcherWatcher,
        NopNoResponseWatcher {

    public static SpreadsheetListDialogComponent with(final SpreadsheetListComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetListDialogComponent(context);
    }

    private SpreadsheetListDialogComponent(final SpreadsheetListComponentContext context) {
        this.context = context;

        context.addHistoryTokenWatcher(this);
        context.addSpreadsheetMetadataFetcherWatcher(this);

        this.reload = this.reload();

        this.count10 = this.count10();
        this.count20 = this.count20();

        this.table = this.table();
        this.dialog = this.dialogCreate(context);
    }

    // reload............................................................................................................

    private HistoryTokenAnchorComponent reload() {
        return this.context.historyToken()
                .link(ID_PREFIX + "reload")
                .setTextContent("reload")
                .setIconAfter(
                        Optional.of(
                                SpreadsheetIcons.spreadsheetListReload()
                        )
                );
    }

    private final HistoryTokenAnchorComponent reload;

    // count10............................................................................................................

    private HistoryTokenAnchorComponent count10() {
        return this.count(10);
    }

    private final HistoryTokenAnchorComponent count10;

    // count20............................................................................................................

    private HistoryTokenAnchorComponent count20() {
        return this.count(20);
    }

    private final HistoryTokenAnchorComponent count20;

    private HistoryTokenAnchorComponent count(final int count) {
        return this.context.historyToken()
                .link(ID_PREFIX + "count-" + count + "-rows")
                .setTextContent(count + " Rows");
    }

    // table............................................................................................................

    private SpreadsheetListComponentSpreadsheetDataTable table() {
        return SpreadsheetListComponentSpreadsheetDataTable.empty(this.context);
    }

    private final SpreadsheetListComponentSpreadsheetDataTable table;

    // SpreadsheetDialogComponentLifecycle..............................................................................

    private SpreadsheetDialogComponent dialogCreate(final SpreadsheetListComponentContext context) {
        return SpreadsheetDialogComponent.with(
                        ID, // id
                        "Spreadsheet List", // title
                        false, // includeClose
                        context
                ).appendChild(this.table)
                .appendChild(
                        SpreadsheetFlexLayout.row()
                                .appendChild(
                                        HistoryToken.spreadsheetCreate()
                                                .link(ID_PREFIX + "create")
                                                .setTextContent("Create")
                                ).appendChild(this.reload)
                                .appendChild(this.count10)
                                .appendChild(this.count20)
                );
    }

    @Override
    public SpreadsheetDialogComponent dialog() {
        return this.dialog;
    }

    private final SpreadsheetDialogComponent dialog;

    private final SpreadsheetListComponentContext context;

    // id...............................................................................................................

    @Override
    public String idPrefix() {
        return ID_PREFIX;
    }

    final static String ID = "spreadsheet-list";

    final static String ID_PREFIX = ID + '-';

    // SpreadsheetMetadataFetcherWatcher................................................................................

    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        // ignore
    }

    @Override
    public void onSpreadsheetMetadataSet(final Set<SpreadsheetMetadata> metadatas,
                                         final AppContext context) {
        if (this.isOpen()) {
            this.table.setMetadata(
                    new ArrayList<>(metadatas)
            );
            this.table.refresh(
                    context.historyToken()
                            .cast(SpreadsheetListHistoryToken.class)
            );
        }
    }

    // SpreadsheetDialogComponentLifecycle..............................................................................

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetListDeleteHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetListHistoryToken;
    }

    @Override
    public void openGiveFocus(final AppContext context) {
        // NOP
    }

    @Override
    public void refresh(final AppContext context) {
        final SpreadsheetListHistoryToken historyToken = context.historyToken()
                .cast(SpreadsheetListHistoryToken.class);

        // refresh reload, history token might have changed from or count etc.
        this.reload.setHistoryToken(
                Optional.of(historyToken.setReload())
        );

        this.count10.setHistoryToken(
                Optional.of(
                        historyToken.setCount(
                                OptionalInt.of(10)
                        )
                )
        );

        this.count20.setHistoryToken(
                Optional.of(
                        historyToken.setCount(
                                OptionalInt.of(20)
                        )
                )
        );

        this.table.refresh(historyToken);
    }
}
