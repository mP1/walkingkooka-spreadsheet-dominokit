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
import walkingkooka.net.UrlFragment;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNamesList;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.text.printer.TreePrintableTesting;

public final class SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemoverTest implements ClassTesting<SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover>,
        TreePrintableTesting {

    // cell.............................................................................................................

    @Test
    public void testCellOneNameOnly() {
        this.refreshAndCheck(
                0,
                SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse("A=text"),
                "/1/spreadsheetName23/cell/A1:B2/sort/edit/A=text",
                "SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n"
        );
    }

    @Test
    public void testCellTwoNameFirstOnly() {
        this.refreshAndCheck(
                0,
                SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse("A=text"),
                "/1/spreadsheetName23/cell/A1:B2/sort/edit/IGNORED",
                "SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n"
        );
    }

    @Test
    public void testCellTwoNameLastOnly() {
        this.refreshAndCheck(
                0,
                SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse("A=text"),
                "/1/spreadsheetName23/cell/A1:B2/sort/edit/IGNORED",
                "SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n"
        );
    }

    @Test
    public void testCellTwoNameFirstSeveral() {
        this.refreshAndCheck(
                0,
                SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse("A=text1,text2;B=ignored1,ignored2"),
                "/1/spreadsheetName23/cell/A1:B2/sort/edit/IGNORED",
                "SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      \"text1\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/save/A%3Dtext2%3BB%3Dignored1%2Cignored2]\n" +
                        "      \"text2\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/save/A%3Dtext1%3BB%3Dignored1%2Cignored2]\n"
        );
    }

    @Test
    public void testCellTwoNameLastSeveral() {
        this.refreshAndCheck(
                1,
                SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse("A=ignored1,ignored2;B=text1,text2"),
                "/1/spreadsheetName23/cell/A1:B2/sort/edit/IGNORED",
                "SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      \"text1\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/save/A%3Dignored1%2Cignored2%3BB%3Dtext2]\n" +
                        "      \"text2\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/save/A%3Dignored1%2Cignored2%3BB%3Dtext1]\n"
        );
    }

    @Test
    public void testCellTwoNameLastSeveral2() {
        this.refreshAndCheck(
                2,
                SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse("A=day-of-month;B=month-of-year;C=text1,text2,text3"),
                "/1/spreadsheetName23/cell/A1:C3/sort/edit/IGNORED",
                "SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      \"text1\" [#/1/spreadsheetName23/cell/A1:C3/bottom-right/sort/save/A%3Dday-of-month%3BB%3Dmonth-of-year%3BC%3Dtext2%2Ctext3]\n" +
                        "      \"text2\" [#/1/spreadsheetName23/cell/A1:C3/bottom-right/sort/save/A%3Dday-of-month%3BB%3Dmonth-of-year%3BC%3Dtext1%2Ctext3]\n" +
                        "      \"text3\" [#/1/spreadsheetName23/cell/A1:C3/bottom-right/sort/save/A%3Dday-of-month%3BB%3Dmonth-of-year%3BC%3Dtext1%2Ctext2]\n"
        );
    }

    // column...........................................................................................................

    @Test
    public void testColumnTwoNameFirstSeveral() {
        this.refreshAndCheck(
                0,
                SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse("A=text1,text2;B=ignored1,ignored2"),
                "/1/spreadsheetName23/column/A:B/sort/edit/IGNORED",
                "SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      \"text1\" [#/1/spreadsheetName23/column/A:B/right/sort/save/A%3Dtext2%3BB%3Dignored1%2Cignored2]\n" +
                        "      \"text2\" [#/1/spreadsheetName23/column/A:B/right/sort/save/A%3Dtext1%3BB%3Dignored1%2Cignored2]\n"
        );
    }

    // row...........................................................................................................

    @Test
    public void testRowTwoNameFirstSeveral() {
        this.refreshAndCheck(
                0,
                SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse("A=text1,text2;B=ignored1,ignored2"),
                "/1/spreadsheetName23/row/1:2/sort/edit/IGNORED",
                "SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      \"text1\" [#/1/spreadsheetName23/row/1:2/bottom/sort/save/A%3Dtext2%3BB%3Dignored1%2Cignored2]\n" +
                        "      \"text2\" [#/1/spreadsheetName23/row/1:2/bottom/sort/save/A%3Dtext1%3BB%3Dignored1%2Cignored2]\n"
        );
    }

    // helper...........................................................................................................

    private void refreshAndCheck(final int index,
                                 final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList namesList,
                                 final String historyToken,
                                 final String expected) {
        this.refreshAndCheck(
                index,
                namesList,
                new FakeSpreadsheetSortDialogComponentContext() {

                    @Override
                    public HistoryToken historyToken() {
                        return HistoryToken.parse(
                                UrlFragment.parse(historyToken)
                        );
                    }
                },
                expected
        );
    }

    private void refreshAndCheck(final int index,
                                 final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList namesList,
                                 final SpreadsheetSortDialogComponentContext context,
                                 final String expected) {
        final SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover remover = SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover.empty(index);

        remover.refresh(
                namesList,
                context
        );

        this.treePrintAndCheck(
                remover,
                expected
        );
    }

    @Override
    public Class<SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover> type() {
        return SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
