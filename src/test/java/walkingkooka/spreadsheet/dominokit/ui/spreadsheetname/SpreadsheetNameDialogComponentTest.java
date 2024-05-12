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

package walkingkooka.spreadsheet.dominokit.ui.spreadsheetname;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;

public final class SpreadsheetNameDialogComponentTest implements SpreadsheetDialogComponentLifecycleTesting<SpreadsheetNameDialogComponent>,
        SpreadsheetMetadataTesting {

    private final static SpreadsheetId ID = SpreadsheetId.with(1);

    private final static SpreadsheetName NAME = SpreadsheetName.with("spreadsheetName123");

    @Test
    public void testOpenUsesHistoryTokenName() {
        final AppContext context = new FakeAppContext() {

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return null;
            }

            @Override
            public HistoryToken historyToken() {
                return HistoryToken.spreadsheetRenameSelect(
                        ID,
                        NAME
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
                        "  SpreadsheetDialogComponent\n" +
                        "    Spreadsheet Name\n" +
                        "    id=name includeClose=true\n" +
                        "      SpreadsheetNameComponent\n" +
                        "        ParserSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [spreadsheetName123] id=name-name\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        \"Save\" [#/1/spreadsheetName123/rename/save/spreadsheetName123] id=name-save-Link\n" +
                        "        \"Undo\" DISABLED id=name-undo-Link\n" +
                        "        \"Close\" [#/1/spreadsheetName123] id=name-close-Link\n"
        );
    }

    @Override
    public Class<SpreadsheetNameDialogComponent> type() {
        return SpreadsheetNameDialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}

