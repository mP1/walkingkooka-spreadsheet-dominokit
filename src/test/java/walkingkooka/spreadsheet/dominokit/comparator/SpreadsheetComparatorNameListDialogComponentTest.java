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

package walkingkooka.spreadsheet.dominokit.comparator;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;

public final class SpreadsheetComparatorNameListDialogComponentTest implements SpreadsheetDialogComponentLifecycleTesting<SpreadsheetComparatorNameListDialogComponent>,
        SpreadsheetMetadataTesting {

    @Test
    public void testRefresh() {
        final AppContext context = this.appContext(
                "/1/Spreadsheet123/metadata/sort-comparators"
        );

        final SpreadsheetComparatorNameListDialogComponent dialog = SpreadsheetComparatorNameListDialogComponent.with(
                new FakeSpreadsheetComparatorNameListDialogComponentContext() {
                    @Override
                    public HistoryToken historyToken() {
                        return context.historyToken();
                    }

                    @Override
                    public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                        return () -> {
                        };
                    }

                    @Override
                    public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
                        return () -> {
                        };
                    }

                    @Override
                    public String dialogTitle() {
                        return "Sort Comparators Title123";
                    }

                    @Override
                    public String undo() {
                        return "day-of-month";
                    }
                }
        );

        dialog.refresh(context);

        this.treePrintAndCheck(
                dialog,
                "SpreadsheetComparatorNameListDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Sort Comparators Title123\n" +
                        "    id=SpreadsheetComparatorNameListDialogComponent includeClose=true CLOSED\n" +
                        "      SpreadsheetComparatorNameListComponent\n" +
                        "        ValueSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [day-of-month] id=SpreadsheetComparatorNameListDialogComponent-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/metadata/sort-comparators/save/day-of-month] id=SpreadsheetComparatorNameListDialogComponent-save-Link\n" +
                        "          \"Undo\" [#/1/Spreadsheet123/metadata/sort-comparators/save/day-of-month] id=SpreadsheetComparatorNameListDialogComponent-undo-Link\n" +
                        "          \"Clear\" [#/1/Spreadsheet123/metadata/sort-comparators/save/] id=SpreadsheetComparatorNameListDialogComponent-clear-Link\n" +
                        "          \"Close\" [#/1/Spreadsheet123/metadata] id=SpreadsheetComparatorNameListDialogComponent-close-Link\n"
        );
    }

    private AppContext appContext(final String historyToken) {
        return new FakeAppContext() {
            @Override
            public HistoryToken historyToken() {
                return HistoryToken.parseString(historyToken);
            }
        };
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetComparatorNameListDialogComponent> type() {
        return SpreadsheetComparatorNameListDialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
