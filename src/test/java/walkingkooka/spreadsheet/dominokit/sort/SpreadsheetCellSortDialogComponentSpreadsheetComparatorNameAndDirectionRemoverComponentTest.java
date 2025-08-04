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

package walkingkooka.spreadsheet.dominokit.sort;

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNames;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorNameAndDirection;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReferenceOrRange;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionRemoverComponentTest implements HtmlComponentTesting<SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionRemoverComponent, HTMLDivElement> {

    // cell.............................................................................................................

    @Test
    public void testCellMissingColumnORow() {
        this.refreshAndCheck(
            "", // columnOrRow
            "ignored-comparator-1", // namesList
            "/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit/", // historyToken
            ""
        );
    }

    @Test
    public void testCellEmpty() {
        this.refreshAndCheck(
            "A", // columnOrRow
            "", // namesList
            "/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit/", // historyToken
            ""
        );
    }

    @Test
    public void testCellOnly() {
        this.refreshAndCheck(
            "A", // columnOrRow
            "comparator-1", // namesList
            "/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit/", // historyToken
            "SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionRemoverComponent\n" +
                "  SpreadsheetCard\n" +
                "    Card\n" +
                "      Remove comparator(s)\n" +
                "        \"comparator-1\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit] id=sort-comparator-0-remove-0-Link\n"
        );
    }

    @Test
    public void testCellNotEmpty() {
        this.refreshAndCheck(
            "A", // columnOrRow
            "comparator-1,comparator-2,comparator-3", // namesList
            "/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit/", // historyToken
            "SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionRemoverComponent\n" +
                "  SpreadsheetCard\n" +
                "    Card\n" +
                "      Remove comparator(s)\n" +
                "        \"comparator-1\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit/A=comparator-2,comparator-3] id=sort-comparator-0-remove-0-Link\n" +
                "        \"comparator-2\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit/A=comparator-1,comparator-3] id=sort-comparator-0-remove-1-Link\n" +
                "        \"comparator-3\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit/A=comparator-1,comparator-2] id=sort-comparator-0-remove-2-Link\n"
        );
    }

    @Test
    public void testCellNotEmpty2() {
        this.refreshAndCheck(
            "A", // columnOrRow
            "comparator-1,comparator-2,comparator-3", // namesList
            "/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit/", // historyToken
            (names) -> HistoryToken.parseString(
                "/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit/B=text," +
                    names.map(SpreadsheetColumnOrRowSpreadsheetComparatorNames::text)
                        .orElse("")
            ),
            "SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionRemoverComponent\n" +
                "  SpreadsheetCard\n" +
                "    Card\n" +
                "      Remove comparator(s)\n" +
                "        \"comparator-1\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit/B=text,A=comparator-2,comparator-3] id=sort-comparator-0-remove-0-Link\n" +
                "        \"comparator-2\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit/B=text,A=comparator-1,comparator-3] id=sort-comparator-0-remove-1-Link\n" +
                "        \"comparator-3\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit/B=text,A=comparator-1,comparator-2] id=sort-comparator-0-remove-2-Link\n"
        );
    }

    // column...........................................................................................................

    @Test
    public void testColumn() {
        this.refreshAndCheck(
            "B", // columnOrRow
            "comparator-1,comparator-2,comparator-3", // namesList
            "/1/spreadsheetName23/column/B:C/right/sort/edit/", // historyToken
            "SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionRemoverComponent\n" +
                "  SpreadsheetCard\n" +
                "    Card\n" +
                "      Remove comparator(s)\n" +
                "        \"comparator-1\" [#/1/spreadsheetName23/column/B:C/right/sort/edit/B=comparator-2,comparator-3] id=sort-comparator-0-remove-0-Link\n" +
                "        \"comparator-2\" [#/1/spreadsheetName23/column/B:C/right/sort/edit/B=comparator-1,comparator-3] id=sort-comparator-0-remove-1-Link\n" +
                "        \"comparator-3\" [#/1/spreadsheetName23/column/B:C/right/sort/edit/B=comparator-1,comparator-2] id=sort-comparator-0-remove-2-Link\n"
        );
    }

    // row///...........................................................................................................

    @Test
    public void testRow() {
        this.refreshAndCheck(
            "3", // columnOrRow
            "comparator-1,comparator-2,comparator-3", // namesList
            "/1/spreadsheetName23/row/3:4/top/sort/edit/", // historyToken
            "SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionRemoverComponent\n" +
                "  SpreadsheetCard\n" +
                "    Card\n" +
                "      Remove comparator(s)\n" +
                "        \"comparator-1\" [#/1/spreadsheetName23/row/3:4/top/sort/edit/3=comparator-2,comparator-3] id=sort-comparator-0-remove-0-Link\n" +
                "        \"comparator-2\" [#/1/spreadsheetName23/row/3:4/top/sort/edit/3=comparator-1,comparator-3] id=sort-comparator-0-remove-1-Link\n" +
                "        \"comparator-3\" [#/1/spreadsheetName23/row/3:4/top/sort/edit/3=comparator-1,comparator-2] id=sort-comparator-0-remove-2-Link\n"
        );
    }

    // helper...........................................................................................................

    private void refreshAndCheck(final String columnOrRow,
                                 final String spreadsheetComparatorNameAndDirections,
                                 final String historyToken,
                                 final String expected) {
        this.refreshAndCheck(
            columnOrRow,
            spreadsheetComparatorNameAndDirections,
            historyToken,
            (names) -> HistoryToken.parseString(
                historyToken +
                    names.map(SpreadsheetColumnOrRowSpreadsheetComparatorNames::text)
                        .orElse("")
            ),
            expected
        );
    }

    private void refreshAndCheck(final String columnOrRow,
                                 final String spreadsheetComparatorNameAndDirections,
                                 final String historyToken,
                                 final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, HistoryToken> setter,
                                 final String expected) {
        this.refreshAndCheck(
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
            setter,
            new FakeSpreadsheetCellSortDialogComponentContext() {

                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.parseString(historyToken);
                }
            },
            expected
        );
    }

    private void refreshAndCheck(final Optional<SpreadsheetColumnOrRowReferenceOrRange> columnOrRow,
                                 final List<SpreadsheetComparatorNameAndDirection> spreadsheetComparatorNameAndDirections,
                                 final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, HistoryToken> setter,
                                 final SpreadsheetCellSortDialogComponentContext context,
                                 final String expected) {
        final SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionRemoverComponent remover = SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionRemoverComponent.empty(
            "sort-comparator-0-",
            setter
        );

        remover.refresh(
            columnOrRow,
            spreadsheetComparatorNameAndDirections,
            context
        );

        this.treePrintAndCheck(
            remover,
            expected
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionRemoverComponent> type() {
        return SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionRemoverComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
