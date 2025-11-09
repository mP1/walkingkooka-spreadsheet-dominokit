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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.environment.AuditInfo;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;

import java.time.LocalDateTime;
import java.util.Locale;

public final class SpreadsheetListDialogComponentTest implements DialogComponentLifecycleTesting<SpreadsheetListDialogComponent> {

    @Test
    public void testEmpty() {
        this.onHistoryTokenChangeAndCheck(
            "/",
            "SpreadsheetListDialogComponent\n" +
                "  DialogComponent\n" +
                "    Spreadsheet List\n" +
                "    id=spreadsheetList-Dialog includeClose=false\n" +
                "      SpreadsheetListTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=spreadsheetList-Table\n" +
                "              COLUMN(S)\n" +
                "                Name\n" +
                "                Created by\n" +
                "                Created timestamp\n" +
                "                Last modified timestamp\n" +
                "                Last modified\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=spreadsheetList-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=spreadsheetList-next-Link\n" +
                "              PLUGINS\n" +
                "                EmptyStatePlugin (mdi-gauge-empty) \"No spreadsheets\"\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Create\" [#/create] id=spreadsheetList-create-Link\n" +
                "            \"Reload\" [#/*/reload] mdi-reload id=spreadsheetList-reload-Link\n" +
                "            \"10 Rows\" [#/*/count/10] id=spreadsheetList-count-10-rows-Link\n" +
                "            \"20 Rows\" [#/*/count/20] id=spreadsheetList-count-20-rows-Link\n"
        );
    }

    @Test
    public void testSeveralRows() {
        final AppContext context = appContext("/*/offset/1/count/3");

        final SpreadsheetListDialogComponent dialog = this.dialog(
            spreadsheetListComponentContext(context)
        );

        // initially empty
        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "SpreadsheetListDialogComponent\n" +
                "  DialogComponent\n" +
                "    Spreadsheet List\n" +
                "    id=spreadsheetList-Dialog includeClose=false\n" +
                "      SpreadsheetListTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=spreadsheetList-Table\n" +
                "              COLUMN(S)\n" +
                "                Name\n" +
                "                Created by\n" +
                "                Created timestamp\n" +
                "                Last modified timestamp\n" +
                "                Last modified\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" [#/*/offset/0/count/3] id=spreadsheetList-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=spreadsheetList-next-Link\n" +
                "              PLUGINS\n" +
                "                EmptyStatePlugin (mdi-gauge-empty) \"No spreadsheets\"\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Create\" [#/create] id=spreadsheetList-create-Link\n" +
                "            \"Reload\" [#/*/offset/1/count/3/reload] mdi-reload id=spreadsheetList-reload-Link\n" +
                "            \"10 Rows\" [#/*/offset/1/count/10] id=spreadsheetList-count-10-rows-Link\n" +
                "            \"20 Rows\" [#/*/offset/1/count/20] id=spreadsheetList-count-20-rows-Link\n"
        );

        // previous history token opens the diloag, otherwise the metadata's below will be ignored.
        dialog.onSpreadsheetMetadataSet(
            Sets.of(
                this.spreadsheetMetadata(1, "SpreadsheetName111"),
                this.spreadsheetMetadata(2, "SpreadsheetName222"),
                this.spreadsheetMetadata(3, "SpreadsheetName333")
            )
        );

        // refresh again !
        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "SpreadsheetListDialogComponent\n" +
                "  DialogComponent\n" +
                "    Spreadsheet List\n" +
                "    id=spreadsheetList-Dialog includeClose=false\n" +
                "      SpreadsheetListTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=spreadsheetList-Table\n" +
                "              COLUMN(S)\n" +
                "                Name\n" +
                "                Created by\n" +
                "                Created timestamp\n" +
                "                Last modified timestamp\n" +
                "                Last modified\n" +
                "                Links\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  \"SpreadsheetName111\" [#/1] id=spreadsheetList-1-Link\n" +
                "                  TextComponent\n" +
                "                    \"creator@example.com\"\n" +
                "                  TextComponent\n" +
                "                    \"31/12/99, 12:01 pm\"\n" +
                "                  TextComponent\n" +
                "                    \"modifier@example.com\"\n" +
                "                  TextComponent\n" +
                "                    \"31/1/00, 12:58 pm\"\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Rename\" [#/rename/1] id=spreadsheetList-1-rename-Link\n" +
                "                      \"Delete\" [#/delete/1] id=spreadsheetList-1-delete-Link\n" +
                "                ROW 1\n" +
                "                  \"SpreadsheetName222\" [#/2] id=spreadsheetList-2-Link\n" +
                "                  TextComponent\n" +
                "                    \"creator@example.com\"\n" +
                "                  TextComponent\n" +
                "                    \"31/12/99, 12:01 pm\"\n" +
                "                  TextComponent\n" +
                "                    \"modifier@example.com\"\n" +
                "                  TextComponent\n" +
                "                    \"31/1/00, 12:58 pm\"\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Rename\" [#/rename/2] id=spreadsheetList-2-rename-Link\n" +
                "                      \"Delete\" [#/delete/2] id=spreadsheetList-2-delete-Link\n" +
                "                ROW 2\n" +
                "                  \"SpreadsheetName333\" [#/3] id=spreadsheetList-3-Link\n" +
                "                  TextComponent\n" +
                "                    \"creator@example.com\"\n" +
                "                  TextComponent\n" +
                "                    \"31/12/99, 12:01 pm\"\n" +
                "                  TextComponent\n" +
                "                    \"modifier@example.com\"\n" +
                "                  TextComponent\n" +
                "                    \"31/1/00, 12:58 pm\"\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Rename\" [#/rename/3] id=spreadsheetList-3-rename-Link\n" +
                "                      \"Delete\" [#/delete/3] id=spreadsheetList-3-delete-Link\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" [#/*/offset/0/count/3] id=spreadsheetList-previous-Link\n" +
                "                    \"next\" [#/*/offset/3/count/3] mdi-arrow-right id=spreadsheetList-next-Link\n" +
                "              PLUGINS\n" +
                "                EmptyStatePlugin (mdi-gauge-empty) \"No spreadsheets\"\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Create\" [#/create] id=spreadsheetList-create-Link\n" +
                "            \"Reload\" [#/*/offset/1/count/3/reload] mdi-reload id=spreadsheetList-reload-Link\n" +
                "            \"10 Rows\" [#/*/offset/1/count/10] id=spreadsheetList-count-10-rows-Link\n" +
                "            \"20 Rows\" [#/*/offset/1/count/20] id=spreadsheetList-count-20-rows-Link\n"
        );
    }

    private SpreadsheetMetadata spreadsheetMetadata(final long id,
                                                    final String name) {
        return SpreadsheetMetadataTesting.METADATA_EN_AU.set(
            SpreadsheetMetadataPropertyName.SPREADSHEET_ID, SpreadsheetId.with(id)
        ).set(
            SpreadsheetMetadataPropertyName.SPREADSHEET_NAME, SpreadsheetName.with(name)
        ).set(
            SpreadsheetMetadataPropertyName.AUDIT_INFO,
            AuditInfo.with(
                EmailAddress.parse("creator@example.com"),
                LocalDateTime.of(1999, 12, 31, 12, 1, 2),
                EmailAddress.parse("modifier@example.com"),
                LocalDateTime.of(2000, 1, 31, 12, 58, 59)
            )
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

    private SpreadsheetListDialogComponentContext spreadsheetListComponentContext(final AppContext context) {
        return SpreadsheetListDialogComponentContexts.appContext(context);
    }

    private SpreadsheetListDialogComponent dialog(final SpreadsheetListDialogComponentContext context) {
        return SpreadsheetListDialogComponent.with(context);
    }

    @Override
    public SpreadsheetListDialogComponent createSpreadsheetDialogComponentLifecycle(final HistoryToken historyToken) {
        return SpreadsheetListDialogComponent.with(
            this.spreadsheetListComponentContext(
                appContext(historyToken.toString())
            )
        );
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
