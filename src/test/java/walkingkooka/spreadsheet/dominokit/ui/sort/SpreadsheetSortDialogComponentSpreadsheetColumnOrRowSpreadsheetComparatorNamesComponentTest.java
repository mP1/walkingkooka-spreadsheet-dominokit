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

package walkingkooka.spreadsheet.dominokit.ui.sort;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.Url;
import walkingkooka.net.UrlFragment;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNames;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorInfo;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public final class SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponentTest implements ClassTesting<SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent>,
        TreePrintableTesting {

    @Test
    public void testCellEmptyColumnOrRowSpreadsheetComparatorNames() {
        this.refreshAndCheck(
                "",
                "SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "  SpreadsheetFlexLayout\n" +
                        "    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "      ParserSpreadsheetTextBox\n" +
                        "        SpreadsheetTextBox\n" +
                        "          [] id=sort-comparator-1-TextBox\n"
        );
    }

    @Test
    public void testCellOnlyColumn() {
        this.refreshAndCheck(
                "A",
                "SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "  SpreadsheetFlexLayout\n" +
                        "    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "      ParserSpreadsheetTextBox\n" +
                        "        SpreadsheetTextBox\n" +
                        "          [A] id=sort-comparator-1-TextBox\n" +
                        "    SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender\n" +
                        "      SpreadsheetCard\n" +
                        "        Card\n" +
                        "          Append comparator(s)\n" +
                        "            \"comparator-1\" [#/1/SpreadsheetName123/cell/A1:C3/top-right] id=sort-1-append-0-Link\n" +
                        "            \"comparator-2\" [#/1/SpreadsheetName123/cell/A1:C3/top-right] id=sort-1-append-1-Link\n" +
                        "            \"comparator-3\" [#/1/SpreadsheetName123/cell/A1:C3/top-right] id=sort-1-append-2-Link\n"
        );
    }

    @Test
    public void testCellOnlyColumnEqualsSign() {
        this.refreshAndCheck(
                "A=",
                "SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "  SpreadsheetFlexLayout\n" +
                        "    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "      ParserSpreadsheetTextBox\n" +
                        "        SpreadsheetTextBox\n" +
                        "          [A=] id=sort-comparator-1-TextBox\n" +
                        "    SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender\n" +
                        "      SpreadsheetCard\n" +
                        "        Card\n" +
                        "          Append comparator(s)\n" +
                        "            \"comparator-1\" [#/1/SpreadsheetName123/cell/A1:C3/top-right] id=sort-1-append-0-Link\n" +
                        "            \"comparator-2\" [#/1/SpreadsheetName123/cell/A1:C3/top-right] id=sort-1-append-1-Link\n" +
                        "            \"comparator-3\" [#/1/SpreadsheetName123/cell/A1:C3/top-right] id=sort-1-append-2-Link\n"
        );
    }

    @Test
    public void testCellOnlyRowEqualsSign() {
        this.refreshAndCheck(
                "12=",
                "SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "  SpreadsheetFlexLayout\n" +
                        "    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "      ParserSpreadsheetTextBox\n" +
                        "        SpreadsheetTextBox\n" +
                        "          [12=] id=sort-comparator-1-TextBox\n" +
                        "    SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender\n" +
                        "      SpreadsheetCard\n" +
                        "        Card\n" +
                        "          Append comparator(s)\n" +
                        "            \"comparator-1\" [#/1/SpreadsheetName123/cell/A1:C3/top-right] id=sort-1-append-0-Link\n" +
                        "            \"comparator-2\" [#/1/SpreadsheetName123/cell/A1:C3/top-right] id=sort-1-append-1-Link\n" +
                        "            \"comparator-3\" [#/1/SpreadsheetName123/cell/A1:C3/top-right] id=sort-1-append-2-Link\n"
        );
    }

    @Test
    public void testCellComparatorName() {
        this.refreshAndCheck(
                "A=comparator-1",
                "SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "  SpreadsheetFlexLayout\n" +
                        "    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "      ParserSpreadsheetTextBox\n" +
                        "        SpreadsheetTextBox\n" +
                        "          [A=comparator-1] id=sort-comparator-1-TextBox\n" +
                        "    SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender\n" +
                        "      SpreadsheetCard\n" +
                        "        Card\n" +
                        "          Append comparator(s)\n" +
                        "            \"comparator-2\" [#/1/SpreadsheetName123/cell/A1:C3/top-right] id=sort-1-append-0-Link\n" +
                        "            \"comparator-3\" [#/1/SpreadsheetName123/cell/A1:C3/top-right] id=sort-1-append-1-Link\n" +
                        "    SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover\n" +
                        "      SpreadsheetCard\n" +
                        "        Card\n" +
                        "          Remove comparator(s)\n" +
                        "            \"comparator-1\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit] id=sort-1--remove-0-Link\n"
        );
    }

    @Test
    public void testCellSeveralComparatorName() {
        this.refreshAndCheck(
                "A=comparator-1,comparator-2",
                "SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "  SpreadsheetFlexLayout\n" +
                        "    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "      ParserSpreadsheetTextBox\n" +
                        "        SpreadsheetTextBox\n" +
                        "          [A=comparator-1,comparator-2] id=sort-comparator-1-TextBox\n" +
                        "    SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender\n" +
                        "      SpreadsheetCard\n" +
                        "        Card\n" +
                        "          Append comparator(s)\n" +
                        "            \"comparator-3\" [#/1/SpreadsheetName123/cell/A1:C3/top-right] id=sort-1-append-0-Link\n" +
                        "    SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover\n" +
                        "      SpreadsheetCard\n" +
                        "        Card\n" +
                        "          Remove comparator(s)\n" +
                        "            \"comparator-1\" [#/1/SpreadsheetName123/cell/A1:C3/top-right] id=sort-1--remove-0-Link\n" +
                        "            \"comparator-2\" [#/1/SpreadsheetName123/cell/A1:C3/top-right] id=sort-1--remove-1-Link\n"
        );
    }

    @Test
    public void testCellAllComparatorNames() {
        this.refreshAndCheck(
                "A=comparator-1,comparator-2,comparator-3",
                "SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "  SpreadsheetFlexLayout\n" +
                        "    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "      ParserSpreadsheetTextBox\n" +
                        "        SpreadsheetTextBox\n" +
                        "          [A=comparator-1,comparator-2,comparator-3] id=sort-comparator-1-TextBox\n" +
                        "    SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover\n" +
                        "      SpreadsheetCard\n" +
                        "        Card\n" +
                        "          Remove comparator(s)\n" +
                        "            \"comparator-1\" [#/1/SpreadsheetName123/cell/A1:C3/top-right] id=sort-1--remove-0-Link\n" +
                        "            \"comparator-2\" [#/1/SpreadsheetName123/cell/A1:C3/top-right] id=sort-1--remove-1-Link\n" +
                        "            \"comparator-3\" [#/1/SpreadsheetName123/cell/A1:C3/top-right] id=sort-1--remove-2-Link\n"
        );
    }

    void refreshAndCheck(final String columnOrRowSpreadsheetComparatorNames,
                         final String expected) {
        final String historyToken = "/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit";

        this.refreshAndCheck(
                columnOrRowSpreadsheetComparatorNames,
                historyToken,
                (names) -> HistoryToken.parse(
                        UrlFragment.parse(
                                historyToken +
                                        names.map(SpreadsheetColumnOrRowSpreadsheetComparatorNames::text)
                                                .orElse("")
                        )
                ),
                expected
        );
    }

    void refreshAndCheck(final String columnOrRowSpreadsheetComparatorNames,
                         final String historyToken,
                         final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, HistoryToken> columnOrRowSpreadsheetComparatorNamesToHistoryToken,
                         final String expected) {
        this.refreshAndCheck(
                columnOrRowSpreadsheetComparatorNames,
                columnOrRowSpreadsheetComparatorNamesToHistoryToken,
                new FakeSpreadsheetSortDialogComponentContext() {
                    @Override
                    public HistoryToken historyToken() {
                        return HistoryToken.parse(
                                UrlFragment.parse(historyToken)
                        );
                    }

                    @Override
                    public Set<SpreadsheetComparatorInfo> spreadsheetComparatorInfos() {
                        return Sets.of(
                                SpreadsheetComparatorInfo.with(
                                        Url.parseAbsolute("https://example.com/comparator-1"),
                                        SpreadsheetComparatorName.with("comparator-1")
                                ),
                                SpreadsheetComparatorInfo.with(
                                        Url.parseAbsolute("https://example.com/comparator-2"),
                                        SpreadsheetComparatorName.with("comparator-2")
                                ),
                                SpreadsheetComparatorInfo.with(
                                        Url.parseAbsolute("https://example.com/comparator-3"),
                                        SpreadsheetComparatorName.with("comparator-3")
                                )
                        );
                    }
                },
                expected
        );
    }

    void refreshAndCheck(final String columnOrRowSpreadsheetComparatorNames,
                         final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, HistoryToken> columnOrRowSpreadsheetComparatorNamesToHistoryToken,
                         final SpreadsheetSortDialogComponentContext context,
                         final String expected) {
        final SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent component = SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent.with(1);
        component.refresh(
                columnOrRowSpreadsheetComparatorNames,
                columnOrRowSpreadsheetComparatorNamesToHistoryToken,
                context
        );
        this.treePrintAndCheck(
                component,
                expected
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent> type() {
        return SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
