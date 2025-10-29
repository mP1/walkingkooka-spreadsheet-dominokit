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

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;

public final class SpreadsheetNameDialogComponentTest implements DialogComponentLifecycleTesting<SpreadsheetNameDialogComponent>,
    SpreadsheetMetadataTesting {

    @Test
    public void testOnHistoryTokenChangeOpenUsesHistoryTokenName() {
        final AppContext context = new FakeAppContext() {

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return null;
            }

            @Override
            public HistoryToken historyToken() {
                return HistoryToken.spreadsheetRenameSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME
                );
            }
        };

        final SpreadsheetNameDialogComponent dialog = SpreadsheetNameDialogComponent.with(
            SpreadsheetNameDialogComponentContexts.spreadsheetRename(context)
        );

        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "SpreadsheetNameDialogComponent\n" +
                "  DialogComponent\n" +
                "    Spreadsheet: Name\n" +
                "    id=SpreadsheetName-Dialog includeClose=true\n" +
                "      SpreadsheetNameComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [SpreadsheetName1] id=SpreadsheetName-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/SpreadsheetName1/rename/save/SpreadsheetName1] id=SpreadsheetName-save-Link\n" +
                "            \"Undo\" [#/1/SpreadsheetName1/rename/save/SpreadsheetName1] id=SpreadsheetName-undo-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1] id=SpreadsheetName-close-Link\n"
        );
    }

    @Override
    public SpreadsheetNameDialogComponent createSpreadsheetDialogComponentLifecycle(final HistoryToken historyToken) {
        return SpreadsheetNameDialogComponent.with(
            SpreadsheetNameDialogComponentContexts.spreadsheetRename(
                new FakeAppContext() {

                    @Override
                    public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                        return null;
                    }

                    @Override
                    public HistoryToken historyToken() {
                        return historyToken;
                    }
                }
            )
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetNameDialogComponent> type() {
        return SpreadsheetNameDialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}

