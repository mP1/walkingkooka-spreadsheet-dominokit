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
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

public final class SpreadsheetSortDialogComponentTest implements SpreadsheetDialogComponentLifecycleTesting<SpreadsheetSortDialogComponent> {

    private final static SpreadsheetId ID = SpreadsheetId.with(1);

    private final static SpreadsheetName NAME = SpreadsheetName.with("spreadsheetName123");

    @Test
    public void testCellOpenAndRefresh() {
        final AppContext context = new FakeAppContext() {

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return null;
            }

            @Override
            public HistoryToken historyToken() {
                return HistoryToken.cellSortEdit(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseCellRange("B2:C3")
                                .setDefaultAnchor(),
                        "B=text"
                );
            }
        };

        final SpreadsheetSortDialogComponent dialog = SpreadsheetSortDialogComponent.with(
                new FakeSpreadsheetSortDialogComponentContext() {

                    @Override
                    public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                        return context.addHistoryTokenWatcher(watcher);
                    }

                    @Override
                    public HistoryToken historyToken() {
                        return context.historyToken();
                    }
                }
        );

        this.onHistoryTokenChangeAndCheck(
                dialog,
                context,
                "SpreadsheetSortDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Sort\n" +
                        "    id=sort includeClose=true\n" +
                        "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                        "        ParserSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [B=text] id=sort-comparatorNamesList\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        \"Sort\" [#/1/spreadsheetName123/cell/B2:C3/bottom-right/sort/save/B%3Dtext] id=sort-sort-Link\n" +
                        "        \"Close\" [#/1/spreadsheetName123/cell/B2:C3/bottom-right] id=sort-close-Link\n"
        );
    }

    @Test
    public void testColumnOpenAndRefresh() {
        final AppContext context = new FakeAppContext() {

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return null;
            }

            @Override
            public HistoryToken historyToken() {
                return HistoryToken.columnSortEdit(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseColumnRange("B:C")
                                .setDefaultAnchor(),
                        "B=text"
                );
            }
        };

        final SpreadsheetSortDialogComponent dialog = SpreadsheetSortDialogComponent.with(
                new FakeSpreadsheetSortDialogComponentContext() {

                    @Override
                    public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                        return context.addHistoryTokenWatcher(watcher);
                    }

                    @Override
                    public HistoryToken historyToken() {
                        return context.historyToken();
                    }
                }
        );

        this.onHistoryTokenChangeAndCheck(
                dialog,
                context,
                "SpreadsheetSortDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Sort\n" +
                        "    id=sort includeClose=true\n" +
                        "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                        "        ParserSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [B=text] id=sort-comparatorNamesList\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        \"Sort\" [#/1/spreadsheetName123/column/B:C/right/sort/save/B%3Dtext] id=sort-sort-Link\n" +
                        "        \"Close\" [#/1/spreadsheetName123/column/B:C/right] id=sort-close-Link\n"
        );
    }

    @Test
    public void testRowOpenAndRefresh() {
        final AppContext context = new FakeAppContext() {

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return null;
            }

            @Override
            public HistoryToken historyToken() {
                return HistoryToken.rowSortEdit(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseRowRange("3:4")
                                .setDefaultAnchor(),
                        "3=text"
                );
            }
        };

        final SpreadsheetSortDialogComponent dialog = SpreadsheetSortDialogComponent.with(
                new FakeSpreadsheetSortDialogComponentContext() {

                    @Override
                    public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                        return context.addHistoryTokenWatcher(watcher);
                    }

                    @Override
                    public HistoryToken historyToken() {
                        return context.historyToken();
                    }
                }
        );

        this.onHistoryTokenChangeAndCheck(
                dialog,
                context,
                "SpreadsheetSortDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Sort\n" +
                        "    id=sort includeClose=true\n" +
                        "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                        "        ParserSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [3=text] id=sort-comparatorNamesList\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        \"Sort\" [#/1/spreadsheetName123/row/3:4/bottom/sort/save/3%3Dtext] id=sort-sort-Link\n" +
                        "        \"Close\" [#/1/spreadsheetName123/row/3:4/bottom] id=sort-close-Link\n"
        );
    }

    @Override
    public Class<SpreadsheetSortDialogComponent> type() {
        return SpreadsheetSortDialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
