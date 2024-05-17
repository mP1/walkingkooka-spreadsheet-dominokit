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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;

import java.time.LocalDateTime;
import java.util.Locale;

public final class SpreadsheetListDialogComponentTest implements SpreadsheetDialogComponentLifecycleTesting<SpreadsheetListDialogComponent> {

    @Test
    public void testEmpty() {
        this.onHistoryTokenChangeAndCheck(
                "/",
                "SpreadsheetListDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Spreadsheet List\n" +
                        "    id=spreadsheet-list includeClose=false\n" +
                        "      SpreadsheetListComponentTable\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              COLUMN(S)\n" +
                        "                Name\n" +
                        "                Created by\n" +
                        "                Created\n" +
                        "                Last modified by\n" +
                        "                Last modified\n" +
                        "                Links\n" +
                        "              CHILDREN\n" +
                        "                SpreadsheetFlexLayout\n" +
                        "                  ROW\n" +
                        "                    mdi-arrow-left \"previous\" DISABLED [#/] id=spreadsheet-list-datatable-previous-Link\n" +
                        "                    \"next\" DISABLED [#/] mdi-arrow-right id=spreadsheet-list-datatable-next-Link\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Create\" [#/create] id=spreadsheet-list-create-Link\n" +
                        "          \"reload\" [#/reload/] mdi-reload id=spreadsheet-list-reload-Link\n" +
                        "          \"10 Rows\" [#/count/10] id=spreadsheet-list-count-10-rows-Link\n" +
                        "          \"20 Rows\" [#/count/20] id=spreadsheet-list-count-20-rows-Link\n"
        );
    }

    @Test
    public void testSeveralRows() {
        final AppContext context = appContext("/from/1/count/3");

        final SpreadsheetListDialogComponent dialog = this.dialog(
                spreadsheetListComponentContext(context)
        );

        // initially empty
        this.onHistoryTokenChangeAndCheck(
                dialog,
                context,
                "SpreadsheetListDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Spreadsheet List\n" +
                        "    id=spreadsheet-list includeClose=false\n" +
                        "      SpreadsheetListComponentTable\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              COLUMN(S)\n" +
                        "                Name\n" +
                        "                Created by\n" +
                        "                Created\n" +
                        "                Last modified by\n" +
                        "                Last modified\n" +
                        "                Links\n" +
                        "              CHILDREN\n" +
                        "                SpreadsheetFlexLayout\n" +
                        "                  ROW\n" +
                        "                    mdi-arrow-left \"previous\" [#/from/0/count/3] id=spreadsheet-list-datatable-previous-Link\n" +
                        "                    \"next\" DISABLED [#/from/1/count/3] mdi-arrow-right id=spreadsheet-list-datatable-next-Link\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Create\" [#/create] id=spreadsheet-list-create-Link\n" +
                        "          \"reload\" [#/reload/from/1/count/3] mdi-reload id=spreadsheet-list-reload-Link\n" +
                        "          \"10 Rows\" [#/from/1/count/10] id=spreadsheet-list-count-10-rows-Link\n" +
                        "          \"20 Rows\" [#/from/1/count/20] id=spreadsheet-list-count-20-rows-Link\n"
        );

        // previous history token opens the diloag, otherwise the metadata's below will be ignored.
        dialog.onSpreadsheetMetadataList(
                Lists.of(
                        this.spreadsheetMetadata(1, "SpreadsheetName111"),
                        this.spreadsheetMetadata(2, "SpreadsheetName222"),
                        this.spreadsheetMetadata(3, "SpreadsheetName333")
                ),
                context
        );

        // refresh again !
        this.onHistoryTokenChangeAndCheck(
                dialog,
                context,
                "SpreadsheetListDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Spreadsheet List\n" +
                        "    id=spreadsheet-list includeClose=false\n" +
                        "      SpreadsheetListComponentTable\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              COLUMN(S)\n" +
                        "                Name\n" +
                        "                Created by\n" +
                        "                Created\n" +
                        "                Last modified by\n" +
                        "                Last modified\n" +
                        "                Links\n" +
                        "              ROW(S)\n" +
                        "                ROW 0\n" +
                        "                  \"SpreadsheetName111\" [#/1] id=spreadsheet-list-1-Link\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"user@example.com\"\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31/12/99, 12:01 pm\"\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"user@example.com\"\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31/1/00, 12:58 pm\"\n" +
                        "                  SpreadsheetFlexLayout\n" +
                        "                    ROW\n" +
                        "                      \"Rename\" [#/rename/1] id=spreadsheet-list-1-rename-Link\n" +
                        "                      \"Delete\" [#/delete/1] id=spreadsheet-list-1-delete-Link\n" +
                        "                ROW 1\n" +
                        "                  \"SpreadsheetName222\" [#/2] id=spreadsheet-list-2-Link\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"user@example.com\"\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31/12/99, 12:01 pm\"\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"user@example.com\"\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31/1/00, 12:58 pm\"\n" +
                        "                  SpreadsheetFlexLayout\n" +
                        "                    ROW\n" +
                        "                      \"Rename\" [#/rename/2] id=spreadsheet-list-2-rename-Link\n" +
                        "                      \"Delete\" [#/delete/2] id=spreadsheet-list-2-delete-Link\n" +
                        "                ROW 2\n" +
                        "                  \"SpreadsheetName333\" [#/3] id=spreadsheet-list-3-Link\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"user@example.com\"\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31/12/99, 12:01 pm\"\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"user@example.com\"\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31/1/00, 12:58 pm\"\n" +
                        "                  SpreadsheetFlexLayout\n" +
                        "                    ROW\n" +
                        "                      \"Rename\" [#/rename/3] id=spreadsheet-list-3-rename-Link\n" +
                        "                      \"Delete\" [#/delete/3] id=spreadsheet-list-3-delete-Link\n" +
                        "              CHILDREN\n" +
                        "                SpreadsheetFlexLayout\n" +
                        "                  ROW\n" +
                        "                    mdi-arrow-left \"previous\" [#/from/0/count/3] id=spreadsheet-list-datatable-previous-Link\n" +
                        "                    \"next\" [#/from/3/count/3] mdi-arrow-right id=spreadsheet-list-datatable-next-Link\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Create\" [#/create] id=spreadsheet-list-create-Link\n" +
                        "          \"reload\" [#/reload/from/1/count/3] mdi-reload id=spreadsheet-list-reload-Link\n" +
                        "          \"10 Rows\" [#/from/1/count/10] id=spreadsheet-list-count-10-rows-Link\n" +
                        "          \"20 Rows\" [#/from/1/count/20] id=spreadsheet-list-count-20-rows-Link\n"
        );
    }

    private SpreadsheetMetadata spreadsheetMetadata(final long id,
                                                    final String name) {
        return SpreadsheetMetadataTesting.METADATA_EN_AU.set(
                SpreadsheetMetadataPropertyName.SPREADSHEET_ID, SpreadsheetId.with(id)
        ).set(
                SpreadsheetMetadataPropertyName.SPREADSHEET_NAME, SpreadsheetName.with(name)
        ).set(
                SpreadsheetMetadataPropertyName.CREATE_DATE_TIME,
                LocalDateTime.of(1999, 12, 31, 12, 1, 2)
        ).set(
                SpreadsheetMetadataPropertyName.MODIFIED_DATE_TIME,
                LocalDateTime.of(2000, 1, 31, 12, 58, 59)
        );
    }

    private void onHistoryTokenChangeAndCheck(final String historyToken,
                                              final String expected) {
        this.onHistoryTokenChangeAndCheck(
                appContext(historyToken),
                expected
        );
    }

    private static FakeAppContext appContext(final String historyToken) {
        return new FakeAppContext() {

            @Override
            public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
                return null;
            }

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return null;
            }

            @Override
            public HistoryToken historyToken() {
                return HistoryToken.parseString(historyToken);
            }

            @Override
            public Locale locale() {
                return Locale.forLanguageTag("EN-AU");
            }
        };
    }

    private void onHistoryTokenChangeAndCheck(final AppContext context,
                                              final String expected) {
        this.onHistoryTokenChangeAndCheck(
                this.dialog(
                        this.spreadsheetListComponentContext(context)
                ),
                context,
                expected
        );
    }

    private SpreadsheetListComponentContext spreadsheetListComponentContext(final AppContext context) {
        final SpreadsheetMetadataFetcherWatchers watchers = SpreadsheetMetadataFetcherWatchers.empty();

        return SpreadsheetListComponentContexts.basic(
                context, // historyToken
                SpreadsheetMetadataFetcher.with(
                        watchers,
                        context
                ),
                watchers,
                context // HasLocale
        );
    }

    private SpreadsheetListDialogComponent dialog(final SpreadsheetListComponentContext context) {
        return SpreadsheetListDialogComponent.with(context);
    }

    // ClassTesting.....................................................................................................

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public Class<SpreadsheetListDialogComponent> type() {
        return SpreadsheetListDialogComponent.class;
    }
}
