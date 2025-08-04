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
import walkingkooka.environment.AuditInfo;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetListHistoryToken;
import walkingkooka.spreadsheet.dominokit.value.TableComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public final class SpreadsheetListTableComponentTest implements TableComponentTesting<HTMLDivElement, List<SpreadsheetMetadata>, SpreadsheetListTableComponent>,
    SpreadsheetMetadataTesting {

    private final static String ID = "Table123-";

    @Test
    public void testPrintTreeWhenEmpty() {
        this.refreshAndCheck(
            SpreadsheetListTableComponent.empty(
                ID,
                context("/")
            ),
            "/",
            "SpreadsheetListTableComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      DataTableComponent\n" +
                "        id=Table123-Table\n" +
                "        COLUMN(S)\n" +
                "          Name\n" +
                "          Created by\n" +
                "          Created timestamp\n" +
                "          Last modified timestamp\n" +
                "          Last modified\n" +
                "          Links\n" +
                "        CHILDREN\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              mdi-arrow-left \"previous\" DISABLED id=Table123-previous-Link\n" +
                "              \"next\" DISABLED mdi-arrow-right id=Table123-next-Link\n" +
                "        PLUGINS\n" +
                "          EmptyStatePlugin (mdi-gauge-empty) \"No spreadsheets\"\n"
        );
    }

    @Test
    public void testTableThreeColumnsThreeRows() {
        this.refreshAndCheck(
            SpreadsheetListTableComponent.empty(
                ID,
                context("/")
            ).setValue(
                Optional.of(
                    Lists.of(
                        spreadsheetMetadata(1, "Spreadsheet111"),
                        spreadsheetMetadata(2, "Spreadsheet222"),
                        spreadsheetMetadata(3, "Spreadsheet333")
                    )
                )
            ),
            "/",
            "SpreadsheetListTableComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      DataTableComponent\n" +
                "        id=Table123-Table\n" +
                "        COLUMN(S)\n" +
                "          Name\n" +
                "          Created by\n" +
                "          Created timestamp\n" +
                "          Last modified timestamp\n" +
                "          Last modified\n" +
                "          Links\n" +
                "        ROW(S)\n" +
                "          ROW 0\n" +
                "            \"Spreadsheet111\" [#/1] id=Table123-1-Link\n" +
                "            TextComponent\n" +
                "              \"creator@example.com\"\n" +
                "            TextComponent\n" +
                "              \"31/12/99, 12:01 pm\"\n" +
                "            TextComponent\n" +
                "              \"modifier@example.com\"\n" +
                "            TextComponent\n" +
                "              \"31/1/00, 12:58 pm\"\n" +
                "            FlexLayoutComponent\n" +
                "              ROW\n" +
                "                \"Rename\" [#/rename/1] id=Table123-1-rename-Link\n" +
                "                \"Delete\" [#/delete/1] id=Table123-1-delete-Link\n" +
                "          ROW 1\n" +
                "            \"Spreadsheet222\" [#/2] id=Table123-2-Link\n" +
                "            TextComponent\n" +
                "              \"creator@example.com\"\n" +
                "            TextComponent\n" +
                "              \"31/12/99, 12:01 pm\"\n" +
                "            TextComponent\n" +
                "              \"modifier@example.com\"\n" +
                "            TextComponent\n" +
                "              \"31/1/00, 12:58 pm\"\n" +
                "            FlexLayoutComponent\n" +
                "              ROW\n" +
                "                \"Rename\" [#/rename/2] id=Table123-2-rename-Link\n" +
                "                \"Delete\" [#/delete/2] id=Table123-2-delete-Link\n" +
                "          ROW 2\n" +
                "            \"Spreadsheet333\" [#/3] id=Table123-3-Link\n" +
                "            TextComponent\n" +
                "              \"creator@example.com\"\n" +
                "            TextComponent\n" +
                "              \"31/12/99, 12:01 pm\"\n" +
                "            TextComponent\n" +
                "              \"modifier@example.com\"\n" +
                "            TextComponent\n" +
                "              \"31/1/00, 12:58 pm\"\n" +
                "            FlexLayoutComponent\n" +
                "              ROW\n" +
                "                \"Rename\" [#/rename/3] id=Table123-3-rename-Link\n" +
                "                \"Delete\" [#/delete/3] id=Table123-3-delete-Link\n" +
                "        CHILDREN\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              mdi-arrow-left \"previous\" DISABLED id=Table123-previous-Link\n" +
                "              \"next\" DISABLED mdi-arrow-right id=Table123-next-Link\n" +
                "        PLUGINS\n" +
                "          EmptyStatePlugin (mdi-gauge-empty) \"No spreadsheets\"\n"
        );
    }

    @Test
    public void testTableThreeColumnsThreeRowsAndPrevious() {
        this.refreshAndCheck(
            SpreadsheetListTableComponent.empty(
                ID,
                context("/offset/1/count/2")
            ).setValue(
                Optional.of(
                    Lists.of(
                        spreadsheetMetadata(1, "Spreadsheet111"),
                        spreadsheetMetadata(2, "Spreadsheet222"),
                        spreadsheetMetadata(3, "Spreadsheet333")
                    )
                )
            ),
            "/*/offset/1/count/2",
            "SpreadsheetListTableComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      DataTableComponent\n" +
                "        id=Table123-Table\n" +
                "        COLUMN(S)\n" +
                "          Name\n" +
                "          Created by\n" +
                "          Created timestamp\n" +
                "          Last modified timestamp\n" +
                "          Last modified\n" +
                "          Links\n" +
                "        ROW(S)\n" +
                "          ROW 0\n" +
                "            \"Spreadsheet111\" [#/1] id=Table123-1-Link\n" +
                "            TextComponent\n" +
                "              \"creator@example.com\"\n" +
                "            TextComponent\n" +
                "              \"31/12/99, 12:01 pm\"\n" +
                "            TextComponent\n" +
                "              \"modifier@example.com\"\n" +
                "            TextComponent\n" +
                "              \"31/1/00, 12:58 pm\"\n" +
                "            FlexLayoutComponent\n" +
                "              ROW\n" +
                "                \"Rename\" [#/rename/1] id=Table123-1-rename-Link\n" +
                "                \"Delete\" [#/delete/1] id=Table123-1-delete-Link\n" +
                "          ROW 1\n" +
                "            \"Spreadsheet222\" [#/2] id=Table123-2-Link\n" +
                "            TextComponent\n" +
                "              \"creator@example.com\"\n" +
                "            TextComponent\n" +
                "              \"31/12/99, 12:01 pm\"\n" +
                "            TextComponent\n" +
                "              \"modifier@example.com\"\n" +
                "            TextComponent\n" +
                "              \"31/1/00, 12:58 pm\"\n" +
                "            FlexLayoutComponent\n" +
                "              ROW\n" +
                "                \"Rename\" [#/rename/2] id=Table123-2-rename-Link\n" +
                "                \"Delete\" [#/delete/2] id=Table123-2-delete-Link\n" +
                "          ROW 2\n" +
                "            \"Spreadsheet333\" [#/3] id=Table123-3-Link\n" +
                "            TextComponent\n" +
                "              \"creator@example.com\"\n" +
                "            TextComponent\n" +
                "              \"31/12/99, 12:01 pm\"\n" +
                "            TextComponent\n" +
                "              \"modifier@example.com\"\n" +
                "            TextComponent\n" +
                "              \"31/1/00, 12:58 pm\"\n" +
                "            FlexLayoutComponent\n" +
                "              ROW\n" +
                "                \"Rename\" [#/rename/3] id=Table123-3-rename-Link\n" +
                "                \"Delete\" [#/delete/3] id=Table123-3-delete-Link\n" +
                "        CHILDREN\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              mdi-arrow-left \"previous\" [#/*/offset/0/count/2] id=Table123-previous-Link\n" +
                "              \"next\" [#/*/offset/2/count/2] mdi-arrow-right id=Table123-next-Link\n" +
                "        PLUGINS\n" +
                "          EmptyStatePlugin (mdi-gauge-empty) \"No spreadsheets\"\n"
        );
    }

    @Test
    public void testTableThreeColumnsThreeRowsAndNext() {
        this.refreshAndCheck(
            SpreadsheetListTableComponent.empty(
                ID,
                context("/")
            ).setValue(
                Optional.of(
                    Lists.of(
                        spreadsheetMetadata(1, "Spreadsheet111"),
                        spreadsheetMetadata(2, "Spreadsheet222"),
                        spreadsheetMetadata(3, "Spreadsheet333")
                    )
                )
            ),
            "/*/count/2",
            "SpreadsheetListTableComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      DataTableComponent\n" +
                "        id=Table123-Table\n" +
                "        COLUMN(S)\n" +
                "          Name\n" +
                "          Created by\n" +
                "          Created timestamp\n" +
                "          Last modified timestamp\n" +
                "          Last modified\n" +
                "          Links\n" +
                "        ROW(S)\n" +
                "          ROW 0\n" +
                "            \"Spreadsheet111\" [#/1] id=Table123-1-Link\n" +
                "            TextComponent\n" +
                "              \"creator@example.com\"\n" +
                "            TextComponent\n" +
                "              \"31/12/99, 12:01 pm\"\n" +
                "            TextComponent\n" +
                "              \"modifier@example.com\"\n" +
                "            TextComponent\n" +
                "              \"31/1/00, 12:58 pm\"\n" +
                "            FlexLayoutComponent\n" +
                "              ROW\n" +
                "                \"Rename\" [#/rename/1] id=Table123-1-rename-Link\n" +
                "                \"Delete\" [#/delete/1] id=Table123-1-delete-Link\n" +
                "          ROW 1\n" +
                "            \"Spreadsheet222\" [#/2] id=Table123-2-Link\n" +
                "            TextComponent\n" +
                "              \"creator@example.com\"\n" +
                "            TextComponent\n" +
                "              \"31/12/99, 12:01 pm\"\n" +
                "            TextComponent\n" +
                "              \"modifier@example.com\"\n" +
                "            TextComponent\n" +
                "              \"31/1/00, 12:58 pm\"\n" +
                "            FlexLayoutComponent\n" +
                "              ROW\n" +
                "                \"Rename\" [#/rename/2] id=Table123-2-rename-Link\n" +
                "                \"Delete\" [#/delete/2] id=Table123-2-delete-Link\n" +
                "          ROW 2\n" +
                "            \"Spreadsheet333\" [#/3] id=Table123-3-Link\n" +
                "            TextComponent\n" +
                "              \"creator@example.com\"\n" +
                "            TextComponent\n" +
                "              \"31/12/99, 12:01 pm\"\n" +
                "            TextComponent\n" +
                "              \"modifier@example.com\"\n" +
                "            TextComponent\n" +
                "              \"31/1/00, 12:58 pm\"\n" +
                "            FlexLayoutComponent\n" +
                "              ROW\n" +
                "                \"Rename\" [#/rename/3] id=Table123-3-rename-Link\n" +
                "                \"Delete\" [#/delete/3] id=Table123-3-delete-Link\n" +
                "        CHILDREN\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              mdi-arrow-left \"previous\" DISABLED id=Table123-previous-Link\n" +
                "              \"next\" [#/*/offset/1/count/2] mdi-arrow-right id=Table123-next-Link\n" +
                "        PLUGINS\n" +
                "          EmptyStatePlugin (mdi-gauge-empty) \"No spreadsheets\"\n"
        );
    }

    @Override
    public SpreadsheetListTableComponent createComponent() {
        return SpreadsheetListTableComponent.empty(
            ID,
            context("/")
        );
    }

    private static FakeSpreadsheetListDialogComponentContext context(final String historyToken) {
        return new FakeSpreadsheetListDialogComponentContext() {
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
        ).set(SpreadsheetMetadataPropertyName.AUDIT_INFO,
            AuditInfo.with(
                EmailAddress.parse("creator@example.com"),
                LocalDateTime.of(1999, 12, 31, 12, 1, 2),
                EmailAddress.parse("modifier@example.com"),
                LocalDateTime.of(2000, 1, 31, 12, 58, 59)
            )
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetListTableComponent> type() {
        return SpreadsheetListTableComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
