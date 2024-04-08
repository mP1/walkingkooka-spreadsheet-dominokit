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

package walkingkooka.spreadsheet.dominokit.ui.spreadsheetlist;

import org.dominokit.domino.ui.utils.ElementsFactory;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetDeleteHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetListHistoryToken;
import walkingkooka.spreadsheet.dominokit.net.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIds;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;

import java.util.List;
import java.util.Objects;

/**
 * A dialog that displays a table with a listing of spreadsheets. Controls are present to open, delete and create
 * spreadsheets.
 */
public final class SpreadsheetListComponent implements SpreadsheetDialogComponentLifecycle,
        NopFetcherWatcher,
        SpreadsheetMetadataFetcherWatcher {

    private final static String ID = "spreadsheet-list";

    private final static String ID_PREFIX = ID + '-';

    public static SpreadsheetListComponent with(final SpreadsheetListComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetListComponent(context);
    }

    private SpreadsheetListComponent(final SpreadsheetListComponentContext context) {
        this.context = context;

        context.addHistoryTokenWatcher(this);
        context.addSpreadsheetMetadataWatcher(this);

        this.dialog = dialogCreate(context);
    }

    // SpreadsheetDialogComponentLifecycle.............................................................................

    private static SpreadsheetDialogComponent dialogCreate(final SpreadsheetListComponentContext context) {
        final SpreadsheetDialogComponent dialog = SpreadsheetDialogComponent.with(
                ID, // id
                "Spreadsheet Browser", // title
                false, // includeClose
                context
        );

        dialog.appendChild(
                ElementsFactory.elements.div()
                        .appendChild(
                                HistoryToken.spreadsheetCreate()
                                        .link(ID_PREFIX + "create" + SpreadsheetIds.LINK)
                                        .setTextContent("Create")
                        )
        );
        return dialog;
    }

    @Override
    public SpreadsheetDialogComponent dialog() {
        return this.dialog;
    }

    private final SpreadsheetDialogComponent dialog;

    private final SpreadsheetListComponentContext context;

    @Override
    public String idPrefix() {
        return "list-";
    }

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetDeleteHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetListHistoryToken;
    }

    @Override
    public void openGiveFocus(final AppContext context) {
        // Nop
    }

    @Override
    public void refresh(final AppContext context) {

    }

    // SpreadsheetMetadataFetcherWatcher...............................................................................

    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        // ignore
    }

    @Override
    public void onSpreadsheetMetadataList(final List<SpreadsheetMetadata> metadatas,
                                          final AppContext context) {
        // TODO refresh table
    }
}
