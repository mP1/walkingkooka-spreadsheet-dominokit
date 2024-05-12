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

package walkingkooka.spreadsheet.dominokit.ui.columnrowinsert;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.OptionalInt;

public final class SpreadsheetColumnRowInsertCountDialogComponentTest implements SpreadsheetDialogComponentLifecycleTesting<SpreadsheetColumnRowInsertCountDialogComponent>,
        SpreadsheetMetadataTesting {

    private final static SpreadsheetId ID = SpreadsheetId.with(1);

    private final static SpreadsheetName NAME = SpreadsheetName.with("spreadsheetName123");

    private final static HistoryToken COLUMN_HISTORY_TOKEN = HistoryToken.columnInsertAfter(
            ID,
            NAME,
            SpreadsheetSelection.parseColumnRange("A:B")
                    .setDefaultAnchor(),
            OptionalInt.empty()
    );

    private final static HistoryToken ROW_HISTORY_TOKEN = HistoryToken.rowInsertAfter(
            ID,
            NAME,
            SpreadsheetSelection.parseRowRange("1:23")
                    .setDefaultAnchor(),
            OptionalInt.empty()
    );

    @Test
    public void testOpenAndRefreshColumn() {
        final AppContext context = new FakeAppContext() {

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return null;
            }

            @Override
            public HistoryToken historyToken() {
                return COLUMN_HISTORY_TOKEN;
            }

            @Override
            public SpreadsheetMetadata spreadsheetMetadata() {
                return SpreadsheetMetadataTesting.METADATA_EN_AU.set(SpreadsheetMetadataPropertyName.SPREADSHEET_ID, ID)
                        .set(SpreadsheetMetadataPropertyName.SPREADSHEET_NAME, NAME);
            }

            @Override
            public void giveFocus(final Runnable runnable) {
                // nop
            }
        };

        final SpreadsheetColumnRowInsertCountDialogComponent dialog = SpreadsheetColumnRowInsertCountDialogComponent.with(
                new FakeSpreadsheetColumnRowInsertCountDialogComponentContext() {

                    @Override
                    public String dialogTitle() {
                        return "ColumnTitle1234";
                    }

                    @Override
                    public HistoryToken historyToken() {
                        return COLUMN_HISTORY_TOKEN;
                    }

                    @Override
                    public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                        return null;
                    }

                    @Override
                    public boolean shouldIgnore(final HistoryToken token) {
                        return false;
                    }

                    @Override
                    public boolean isMatch(final HistoryToken token) {
                        return token.equals(COLUMN_HISTORY_TOKEN);
                    }
                }
        );

        this.onHistoryTokenChangeAndCheck(
                dialog,
                context,
                "SpreadsheetColumnRowInsertCountDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    ColumnTitle1234\n" +
                        "    id=columnRowInsert includeClose=true\n" +
                        "      SpreadsheetIntegerBox\n" +
                        "        Count [] id=columnRowInsert-count-TextBox REQUIRED\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        \"Go\" id=columnRowInsert-go-Link\n" +
                        "        \"Close\" [#/1/spreadsheetName123/column/A:B/right] id=columnRowInsert-close-Link\n"
        );
    }

    @Test
    public void testOpenAndRefreshRow() {
        final AppContext context = new FakeAppContext() {

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return null;
            }

            @Override
            public HistoryToken historyToken() {
                return ROW_HISTORY_TOKEN;
            }

            @Override
            public SpreadsheetMetadata spreadsheetMetadata() {
                return SpreadsheetMetadataTesting.METADATA_EN_AU.set(SpreadsheetMetadataPropertyName.SPREADSHEET_ID, ID)
                        .set(SpreadsheetMetadataPropertyName.SPREADSHEET_NAME, NAME);
            }

            @Override
            public void giveFocus(final Runnable runnable) {
                // nop
            }
        };

        final SpreadsheetColumnRowInsertCountDialogComponent dialog = SpreadsheetColumnRowInsertCountDialogComponent.with(
                new FakeSpreadsheetColumnRowInsertCountDialogComponentContext() {

                    @Override
                    public String dialogTitle() {
                        return "RowTitle1234";
                    }

                    @Override
                    public HistoryToken historyToken() {
                        return ROW_HISTORY_TOKEN;
                    }

                    @Override
                    public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                        return null;
                    }

                    @Override
                    public boolean shouldIgnore(final HistoryToken token) {
                        return false;
                    }

                    @Override
                    public boolean isMatch(final HistoryToken token) {
                        return token.equals(ROW_HISTORY_TOKEN);
                    }
                }
        );

        this.onHistoryTokenChangeAndCheck(
                dialog,
                context,
                "SpreadsheetColumnRowInsertCountDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    RowTitle1234\n" +
                        "    id=columnRowInsert includeClose=true\n" +
                        "      SpreadsheetIntegerBox\n" +
                        "        Count [] id=columnRowInsert-count-TextBox REQUIRED\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        \"Go\" id=columnRowInsert-go-Link\n" +
                        "        \"Close\" [#/1/spreadsheetName123/row/1:23/bottom] id=columnRowInsert-close-Link\n"
        );
    }

    @Override
    public Class<SpreadsheetColumnRowInsertCountDialogComponent> type() {
        return SpreadsheetColumnRowInsertCountDialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}

