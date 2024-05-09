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
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNamesList;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorInfo;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Optional;
import java.util.Set;

public final class SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppenderTest implements ClassTesting<SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender>,
        TreePrintableTesting {

    // cell.............................................................................................................

    @Test
    public void testCellEmpty() {
        this.refreshAndCheck(
                0, // index within namesList
                "", // namesList
                "A", // columnOrRow
                "/1/spreadsheetName23/cell/A1:B2/sort/edit/IGNORED", // historyToken
                "SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      \"comparator-1\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/save/A%3Dcomparator-1]\n" +
                        "      \"comparator-2\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/save/A%3Dcomparator-2]\n" +
                        "      \"comparator-3\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/save/A%3Dcomparator-3]\n"
        );
    }

    @Test
    public void testCellLastEmpty() {
        this.refreshAndCheck(
                1, // index within namesList
                "A=text", // namesList
                "B", // columnOrRow
                "/1/spreadsheetName23/cell/A1:B2/sort/edit/IGNORED", // historyToken
                "SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      \"comparator-1\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/save/A%3Dtext%3BB%3Dcomparator-1]\n" +
                        "      \"comparator-2\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/save/A%3Dtext%3BB%3Dcomparator-2]\n" +
                        "      \"comparator-3\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/save/A%3Dtext%3BB%3Dcomparator-3]\n"
        );
    }

    @Test
    public void testCellLastNotEmpty() {
        this.refreshAndCheck(
                1, // index within namesList
                "A=text;B=unknown", // namesList
                "B", // columnOrRow
                "/1/spreadsheetName23/cell/A1:B2/sort/edit/IGNORED", // historyToken
                "SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      \"comparator-1\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/save/A%3Dtext%3BB%3Dunknown%2Ccomparator-1]\n" +
                        "      \"comparator-2\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/save/A%3Dtext%3BB%3Dunknown%2Ccomparator-2]\n" +
                        "      \"comparator-3\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/save/A%3Dtext%3BB%3Dunknown%2Ccomparator-3]\n"
        );
    }

    @Test
    public void testCellLastNotEmptySomeSkipped() {
        this.refreshAndCheck(
                1, // index within namesList
                "A=text;B=comparator-3", // namesList
                "B", // columnOrRow
                "/1/spreadsheetName23/cell/A1:B2/sort/edit/IGNORED", // historyToken
                "SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      \"comparator-1\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/save/A%3Dtext%3BB%3Dcomparator-3%2Ccomparator-1]\n" +
                        "      \"comparator-2\" [#/1/spreadsheetName23/cell/A1:B2/bottom-right/sort/save/A%3Dtext%3BB%3Dcomparator-3%2Ccomparator-2]\n"
        );
    }

    @Test
    public void testCellLastNotEmptyNoAppend() {
        this.refreshAndCheck(
                1, // index within namesList
                "A=text;B=comparator-1,comparator-2,comparator-3", // namesList
                "B", // columnOrRow
                "/1/spreadsheetName23/cell/A1:B2/sort/edit/IGNORED", // historyToken
                "SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n"
        );
    }

    private void refreshAndCheck(final int index,
                                 final String namesList,
                                 final String columnOrRow,
                                 final String historyToken,
                                 final String expected) {
        this.refreshAndCheck(
                index,
                Optional.ofNullable(
                        namesList.isEmpty() ?
                                null :
                                SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse(namesList)
                ),
                SpreadsheetSelection.parseColumnOrRow(columnOrRow),
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
                                 final Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNamesList> namesList,
                                 final SpreadsheetColumnOrRowReference columnOrRow,
                                 final SpreadsheetSortDialogComponentContext context,
                                 final String expected) {
        final SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender appender = SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender.empty(index);

        appender.refresh(
                namesList,
                columnOrRow,
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
