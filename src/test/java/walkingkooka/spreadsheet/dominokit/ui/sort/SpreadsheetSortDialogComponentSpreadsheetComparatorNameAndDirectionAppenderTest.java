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
import walkingkooka.collect.set.Sets;
import walkingkooka.net.Url;
import walkingkooka.net.UrlFragment;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNames;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorInfo;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorNameAndDirection;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppenderTest implements ClassTesting<SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender>,
        TreePrintableTesting {

    // cell.............................................................................................................

    @Test
    public void testCellMissingColumnOrRow() {
        this.refreshAndCheck(
                0, // index within namesList
                "", // columnOrRow
                "", // namesList
                "/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit/", // historyToken
                ""
        );
    }

    @Test
    public void testCell2() {
        this.refreshAndCheck(
                0, // index within namesList
                "A", // columnOrRow
                "", // namesList
                "/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit/", // historyToken
                (names) -> HistoryToken.parse(
                        UrlFragment.parse("/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit/B=text," + names)
                ),
                "SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Append comparator(s)\n" +
                        "        \"comparator-1\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit/B=text,A=comparator-1] id=sort-comparator-0-append-0-Link\n" +
                        "        \"comparator-2\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit/B=text,A=comparator-2] id=sort-comparator-0-append-1-Link\n" +
                        "        \"comparator-3\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/edit/B=text,A=comparator-3] id=sort-comparator-0-append-2-Link\n"
        );
    }

    // column...........................................................................................................

    @Test
    public void testColumn() {
        this.refreshAndCheck(
                1, // index within namesList
                "C", // columnOrRow
                "comparator-3", // namesList
                "/1/spreadsheetName23/column/B:C/right/sort/edit/", // historyToken
                "SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Append comparator(s)\n" +
                        "        \"comparator-1\" [#/1/spreadsheetName23/column/B:C/right/sort/edit/C=comparator-3,comparator-1] id=sort-comparator-1-append-0-Link\n" +
                        "        \"comparator-2\" [#/1/spreadsheetName23/column/B:C/right/sort/edit/C=comparator-3,comparator-2] id=sort-comparator-1-append-1-Link\n"
        );
    }

    // row///...........................................................................................................

    @Test
    public void testRow() {
        this.refreshAndCheck(
                1, // index within namesList
                "3", // columnOrRow
                "comparator-3", // namesList
                "/1/spreadsheetName23/row/2:3/bottom/sort/edit/", // historyToken
                "SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Append comparator(s)\n" +
                        "        \"comparator-1\" [#/1/spreadsheetName23/row/2:3/bottom/sort/edit/3=comparator-3,comparator-1] id=sort-comparator-1-append-0-Link\n" +
                        "        \"comparator-2\" [#/1/spreadsheetName23/row/2:3/bottom/sort/edit/3=comparator-3,comparator-2] id=sort-comparator-1-append-1-Link\n"
        );
    }

    // helper...........................................................................................................

    private void refreshAndCheck(final int index,
                                 final String columnOrRow,
                                 final String setter,
                                 final String historyToken,
                                 final String expected) {
        this.refreshAndCheck(
                index,
                columnOrRow,
                setter,
                historyToken,
                (names) -> HistoryToken.parse(
                        UrlFragment.parse(historyToken + names)
                ),
                expected
        );
    }

    private void refreshAndCheck(final int index,
                                 final String columnOrRow,
                                 final String setter,
                                 final String historyToken,
                                 final Function<SpreadsheetColumnOrRowSpreadsheetComparatorNames, HistoryToken> columnOrRowSpreadsheetComparatorNamesToHistoryToken,
                                 final String expected) {
        this.refreshAndCheck(
                index,
                columnOrRow.isEmpty() ?
                        Optional.empty() :
                        Optional.of(
                                SpreadsheetSelection.parseColumnOrRow(columnOrRow)
                        ),
                setter.isEmpty() ?
                        Lists.empty() :
                        Arrays.stream(setter.split(","))
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

    private void refreshAndCheck(final int index,
                                 final Optional<SpreadsheetColumnOrRowReference> columnOrRow,
                                 final List<SpreadsheetComparatorNameAndDirection> setter,
                                 final Function<SpreadsheetColumnOrRowSpreadsheetComparatorNames, HistoryToken> columnOrRowSpreadsheetComparatorNamesToHistoryToken,
                                 final SpreadsheetSortDialogComponentContext context,
                                 final String expected) {
        final SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender appender = SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender.empty(
                "sort-comparator-" + index + "-",
                columnOrRowSpreadsheetComparatorNamesToHistoryToken
        );

        appender.refresh(
                columnOrRow,
                setter,
                context
        );

        this.treePrintAndCheck(
                appender,
                expected
        );
    }

    @Override
    public Class<SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender> type() {
        return SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
