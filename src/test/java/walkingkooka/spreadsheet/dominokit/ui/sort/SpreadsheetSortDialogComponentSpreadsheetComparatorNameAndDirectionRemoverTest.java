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
import walkingkooka.collect.list.Lists;
import walkingkooka.net.UrlFragment;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNames;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorNameAndDirection;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemoverTest implements ClassTesting<SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover>,
        TreePrintableTesting {

    // cell.............................................................................................................

    @Test
    public void testCellMissingColumnORow() {
        this.refreshAndCheck(
                0, // index within namesList
                "", // columnOrRow
                "ignored-comparator-1", // namesList
                "/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit/", // historyToken
                ""
        );
    }

    @Test
    public void testCellEmpty() {
        this.refreshAndCheck(
                0, // index within namesList
                "A", // columnOrRow
                "", // namesList
                "/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit/", // historyToken
                ""
        );
    }

    @Test
    public void testCellOnly() {
        this.refreshAndCheck(
                0, // index within namesList
                "A", // columnOrRow
                "comparator-1", // namesList
                "/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit/", // historyToken
                "SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove comparator(s)\n" +
                        "        \"comparator-1\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit] id=sort-0--remove-0-Link\n"
        );
    }

    @Test
    public void testCellNotEmpty() {
        this.refreshAndCheck(
                0, // index within namesList
                "A", // columnOrRow
                "comparator-1,comparator-2,comparator-3", // namesList
                "/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit/", // historyToken
                "SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove comparator(s)\n" +
                        "        \"comparator-1\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit/A%3Dcomparator-2%2Ccomparator-3] id=sort-0--remove-0-Link\n" +
                        "        \"comparator-2\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit/A%3Dcomparator-1%2Ccomparator-3] id=sort-0--remove-1-Link\n" +
                        "        \"comparator-3\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit/A%3Dcomparator-1%2Ccomparator-2] id=sort-0--remove-2-Link\n"
        );
    }

    @Test
    public void testCellNotEmpty2() {
        this.refreshAndCheck(
                0, // index within namesList
                "A", // columnOrRow
                "comparator-1,comparator-2,comparator-3", // namesList
                "/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit/", // historyToken
                (names) -> HistoryToken.parse(
                        UrlFragment.parse(
                                "/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit/B=text," +
                                        names.map(SpreadsheetColumnOrRowSpreadsheetComparatorNames::text)
                                                .orElse("")
                        )
                ),
                "SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove comparator(s)\n" +
                        "        \"comparator-1\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit/B%3Dtext%2CA%3Dcomparator-2%2Ccomparator-3] id=sort-0--remove-0-Link\n" +
                        "        \"comparator-2\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit/B%3Dtext%2CA%3Dcomparator-1%2Ccomparator-3] id=sort-0--remove-1-Link\n" +
                        "        \"comparator-3\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit/B%3Dtext%2CA%3Dcomparator-1%2Ccomparator-2] id=sort-0--remove-2-Link\n"
        );
    }

    // column...........................................................................................................

    @Test
    public void testColumn() {
        this.refreshAndCheck(
                0, // index within namesList
                "B", // columnOrRow
                "comparator-1,comparator-2,comparator-3", // namesList
                "/1/spreadsheetName23/column/B:C/right/sort/edit/", // historyToken
                "SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove comparator(s)\n" +
                        "        \"comparator-1\" [#/1/spreadsheetName23/column/B:C/right/sort/edit/B%3Dcomparator-2%2Ccomparator-3] id=sort-0--remove-0-Link\n" +
                        "        \"comparator-2\" [#/1/spreadsheetName23/column/B:C/right/sort/edit/B%3Dcomparator-1%2Ccomparator-3] id=sort-0--remove-1-Link\n" +
                        "        \"comparator-3\" [#/1/spreadsheetName23/column/B:C/right/sort/edit/B%3Dcomparator-1%2Ccomparator-2] id=sort-0--remove-2-Link\n"
        );
    }

    // row///...........................................................................................................

    @Test
    public void testRow() {
        this.refreshAndCheck(
                0, // index within namesList
                "3", // columnOrRow
                "comparator-1,comparator-2,comparator-3", // namesList
                "/1/spreadsheetName23/row/3:4/top/sort/edit/", // historyToken
                "SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove comparator(s)\n" +
                        "        \"comparator-1\" [#/1/spreadsheetName23/row/3:4/top/sort/edit/3%3Dcomparator-2%2Ccomparator-3] id=sort-0--remove-0-Link\n" +
                        "        \"comparator-2\" [#/1/spreadsheetName23/row/3:4/top/sort/edit/3%3Dcomparator-1%2Ccomparator-3] id=sort-0--remove-1-Link\n" +
                        "        \"comparator-3\" [#/1/spreadsheetName23/row/3:4/top/sort/edit/3%3Dcomparator-1%2Ccomparator-2] id=sort-0--remove-2-Link\n"
        );
    }

    // helper...........................................................................................................

    private void refreshAndCheck(final int index,
                                 final String columnOrRow,
                                 final String spreadsheetComparatorNameAndDirections,
                                 final String historyToken,
                                 final String expected) {
        this.refreshAndCheck(
                index,
                columnOrRow,
                spreadsheetComparatorNameAndDirections,
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

    private void refreshAndCheck(final int index,
                                 final String columnOrRow,
                                 final String spreadsheetComparatorNameAndDirections,
                                 final String historyToken,
                                 final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, HistoryToken> columnOrRowSpreadsheetComparatorNamesToHistoryToken,
                                 final String expected) {
        this.refreshAndCheck(
                index,
                columnOrRow.isEmpty() ?
                        Optional.empty() :
                        Optional.of(
                                SpreadsheetSelection.parseColumnOrRow(columnOrRow)
                        ),
                spreadsheetComparatorNameAndDirections.isEmpty() ?
                        Lists.empty() :
                        Arrays.stream(spreadsheetComparatorNameAndDirections.split(","))
                                .map(SpreadsheetComparatorNameAndDirection::parse)
                                .collect(Collectors.toList()),
                columnOrRowSpreadsheetComparatorNamesToHistoryToken,
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
                                 final Optional<SpreadsheetColumnOrRowReference> columnOrRow,
                                 final List<SpreadsheetComparatorNameAndDirection> spreadsheetComparatorNameAndDirections,
                                 final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, HistoryToken> columnOrRowSpreadsheetComparatorNamesToHistoryToken,
                                 final SpreadsheetSortDialogComponentContext context,
                                 final String expected) {
        final SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover remover = SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover.empty(index);

        remover.refresh(
                columnOrRow,
                spreadsheetComparatorNameAndDirections,
                columnOrRowSpreadsheetComparatorNamesToHistoryToken,
                context
        );

        this.treePrintAndCheck(
                remover,
                expected
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover> type() {
        return SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
