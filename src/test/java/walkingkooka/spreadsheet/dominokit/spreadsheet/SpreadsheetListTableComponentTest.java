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

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponentTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetListHistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;

import java.time.LocalDateTime;
import java.util.Locale;

public final class SpreadsheetListTableComponentTest implements HtmlElementComponentTesting<SpreadsheetListTableComponent, HTMLDivElement>,
        SpreadsheetMetadataTesting {

    @Test
    public void testPrintTreeWhenEmpty() {
        this.refreshAndCheck(
                SpreadsheetListTableComponent.empty(
                        context("/")
                ),
                "/",
                "SpreadsheetListTableComponent\n"
        );
    }

    @Test
    public void testTableThreeColumnsThreeRows() {
        this.refreshAndCheck(
                SpreadsheetListTableComponent.empty(
                        context("/")
                ).setMetadata(
                        Lists.of(
                                spreadsheetMetadata(1, "Spreadsheet111"),
                                spreadsheetMetadata(2, "Spreadsheet222"),
                                spreadsheetMetadata(3, "Spreadsheet333")
                        )
                ),
                "/",
                "SpreadsheetListTableComponent\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      SpreadsheetDataTableComponent\n" +
                        "        id=spreadsheetList-Table\n" +
                        "        COLUMN(S)\n" +
                        "          Name\n" +
                        "          Created by\n" +
                        "          Created\n" +
                        "          Last modified by\n" +
                        "          Last modified\n" +
                        "          Links\n" +
                        "        ROW(S)\n" +
                        "          ROW 0\n" +
                        "            \"Spreadsheet111\" [#/1] id=spreadsheetList-1-Link\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"user@example.com\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31/12/99, 12:01 pm\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"user@example.com\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31/1/00, 12:58 pm\"\n" +
                        "            SpreadsheetFlexLayout\n" +
                        "              ROW\n" +
                        "                \"Rename\" [#/rename/1] id=spreadsheetList-1-rename-Link\n" +
                        "                \"Delete\" [#/delete/1] id=spreadsheetList-1-delete-Link\n" +
                        "          ROW 1\n" +
                        "            \"Spreadsheet222\" [#/2] id=spreadsheetList-2-Link\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"user@example.com\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31/12/99, 12:01 pm\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"user@example.com\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31/1/00, 12:58 pm\"\n" +
                        "            SpreadsheetFlexLayout\n" +
                        "              ROW\n" +
                        "                \"Rename\" [#/rename/2] id=spreadsheetList-2-rename-Link\n" +
                        "                \"Delete\" [#/delete/2] id=spreadsheetList-2-delete-Link\n" +
                        "          ROW 2\n" +
                        "            \"Spreadsheet333\" [#/3] id=spreadsheetList-3-Link\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"user@example.com\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31/12/99, 12:01 pm\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"user@example.com\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31/1/00, 12:58 pm\"\n" +
                        "            SpreadsheetFlexLayout\n" +
                        "              ROW\n" +
                        "                \"Rename\" [#/rename/3] id=spreadsheetList-3-rename-Link\n" +
                        "                \"Delete\" [#/delete/3] id=spreadsheetList-3-delete-Link\n" +
                        "        CHILDREN\n" +
                        "          SpreadsheetFlexLayout\n" +
                        "            ROW\n" +
                        "              mdi-arrow-left \"previous\" DISABLED id=spreadsheetList-previous-Link\n" +
                        "              \"next\" DISABLED mdi-arrow-right id=spreadsheetList-next-Link\n" +
                        "        PLUGINS\n" +
                        "          EmptyStatePlugin (mdi-gauge-empty) \"No spreadsheets\"\n"
        );
    }

    @Test
    public void testTableThreeColumnsThreeRowsAndPrevious() {
        this.refreshAndCheck(
                SpreadsheetListTableComponent.empty(
                        context("/offset/1/count/2")
                ).setMetadata(
                        Lists.of(
                                spreadsheetMetadata(1, "Spreadsheet111"),
                                spreadsheetMetadata(2, "Spreadsheet222"),
                                spreadsheetMetadata(3, "Spreadsheet333")
                        )
                ),
                "/*/offset/1/count/2",
                "SpreadsheetListTableComponent\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      SpreadsheetDataTableComponent\n" +
                        "        id=spreadsheetList-Table\n" +
                        "        COLUMN(S)\n" +
                        "          Name\n" +
                        "          Created by\n" +
                        "          Created\n" +
                        "          Last modified by\n" +
                        "          Last modified\n" +
                        "          Links\n" +
                        "        ROW(S)\n" +
                        "          ROW 0\n" +
                        "            \"Spreadsheet111\" [#/1] id=spreadsheetList-1-Link\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"user@example.com\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31/12/99, 12:01 pm\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"user@example.com\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31/1/00, 12:58 pm\"\n" +
                        "            SpreadsheetFlexLayout\n" +
                        "              ROW\n" +
                        "                \"Rename\" [#/rename/1] id=spreadsheetList-1-rename-Link\n" +
                        "                \"Delete\" [#/delete/1] id=spreadsheetList-1-delete-Link\n" +
                        "          ROW 1\n" +
                        "            \"Spreadsheet222\" [#/2] id=spreadsheetList-2-Link\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"user@example.com\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31/12/99, 12:01 pm\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"user@example.com\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31/1/00, 12:58 pm\"\n" +
                        "            SpreadsheetFlexLayout\n" +
                        "              ROW\n" +
                        "                \"Rename\" [#/rename/2] id=spreadsheetList-2-rename-Link\n" +
                        "                \"Delete\" [#/delete/2] id=spreadsheetList-2-delete-Link\n" +
                        "          ROW 2\n" +
                        "            \"Spreadsheet333\" [#/3] id=spreadsheetList-3-Link\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"user@example.com\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31/12/99, 12:01 pm\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"user@example.com\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31/1/00, 12:58 pm\"\n" +
                        "            SpreadsheetFlexLayout\n" +
                        "              ROW\n" +
                        "                \"Rename\" [#/rename/3] id=spreadsheetList-3-rename-Link\n" +
                        "                \"Delete\" [#/delete/3] id=spreadsheetList-3-delete-Link\n" +
                        "        CHILDREN\n" +
                        "          SpreadsheetFlexLayout\n" +
                        "            ROW\n" +
                        "              mdi-arrow-left \"previous\" [#/*/offset/0/count/2] id=spreadsheetList-previous-Link\n" +
                        "              \"next\" [#/*/offset/2/count/2] mdi-arrow-right id=spreadsheetList-next-Link\n" +
                        "        PLUGINS\n" +
                        "          EmptyStatePlugin (mdi-gauge-empty) \"No spreadsheets\"\n"
        );
    }

    @Test
    public void testTableThreeColumnsThreeRowsAndNext() {
        this.refreshAndCheck(
                SpreadsheetListTableComponent.empty(
                        context("/")
                ).setMetadata(
                        Lists.of(
                                spreadsheetMetadata(1, "Spreadsheet111"),
                                spreadsheetMetadata(2, "Spreadsheet222"),
                                spreadsheetMetadata(3, "Spreadsheet333")
                        )
                ),
                "/*/count/2",
                "SpreadsheetListTableComponent\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      SpreadsheetDataTableComponent\n" +
                        "        id=spreadsheetList-Table\n" +
                        "        COLUMN(S)\n" +
                        "          Name\n" +
                        "          Created by\n" +
                        "          Created\n" +
                        "          Last modified by\n" +
                        "          Last modified\n" +
                        "          Links\n" +
                        "        ROW(S)\n" +
                        "          ROW 0\n" +
                        "            \"Spreadsheet111\" [#/1] id=spreadsheetList-1-Link\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"user@example.com\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31/12/99, 12:01 pm\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"user@example.com\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31/1/00, 12:58 pm\"\n" +
                        "            SpreadsheetFlexLayout\n" +
                        "              ROW\n" +
                        "                \"Rename\" [#/rename/1] id=spreadsheetList-1-rename-Link\n" +
                        "                \"Delete\" [#/delete/1] id=spreadsheetList-1-delete-Link\n" +
                        "          ROW 1\n" +
                        "            \"Spreadsheet222\" [#/2] id=spreadsheetList-2-Link\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"user@example.com\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31/12/99, 12:01 pm\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"user@example.com\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31/1/00, 12:58 pm\"\n" +
                        "            SpreadsheetFlexLayout\n" +
                        "              ROW\n" +
                        "                \"Rename\" [#/rename/2] id=spreadsheetList-2-rename-Link\n" +
                        "                \"Delete\" [#/delete/2] id=spreadsheetList-2-delete-Link\n" +
                        "          ROW 2\n" +
                        "            \"Spreadsheet333\" [#/3] id=spreadsheetList-3-Link\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"user@example.com\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31/12/99, 12:01 pm\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"user@example.com\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31/1/00, 12:58 pm\"\n" +
                        "            SpreadsheetFlexLayout\n" +
                        "              ROW\n" +
                        "                \"Rename\" [#/rename/3] id=spreadsheetList-3-rename-Link\n" +
                        "                \"Delete\" [#/delete/3] id=spreadsheetList-3-delete-Link\n" +
                        "        CHILDREN\n" +
                        "          SpreadsheetFlexLayout\n" +
                        "            ROW\n" +
                        "              mdi-arrow-left \"previous\" DISABLED id=spreadsheetList-previous-Link\n" +
                        "              \"next\" [#/*/offset/1/count/2] mdi-arrow-right id=spreadsheetList-next-Link\n" +
                        "        PLUGINS\n" +
                        "          EmptyStatePlugin (mdi-gauge-empty) \"No spreadsheets\"\n"
        );
    }

    private static FakeSpreadsheetListComponentContext context(final String historyToken) {
        return new FakeSpreadsheetListComponentContext() {
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

    private void refreshAndCheck(final SpreadsheetListTableComponent table,
                                 final String historyToken,
                                 final String expected) {
        this.refreshAndCheck(
                table,
                HistoryToken.parseString(historyToken)
                        .cast(SpreadsheetListHistoryToken.class),
                expected
        );
    }

    private void refreshAndCheck(final SpreadsheetListTableComponent table,
                                 final SpreadsheetListHistoryToken historyToken,
                                 final String expected) {
        table.refresh(historyToken);

        this.treePrintAndCheck(
                table,
                expected
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

    @Override
    public Class<SpreadsheetListTableComponent> type() {
        return SpreadsheetListTableComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
