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

package walkingkooka.spreadsheet.dominokit.key;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherTesting;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.insert.SpreadsheetColumnRowInsertCountDialogComponentTest;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportComponentKeyBindings;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportComponentKeyBindingses;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

public final class SpreadsheetKeyboardDialogComponentTest implements DialogComponentLifecycleTesting<SpreadsheetKeyboardDialogComponent>,
    SpreadsheetMetadataTesting {

    @Test
    public void testOnHistoryTokenSpreadsheet() {
        this.refreshAndCheck(
            "Cell A1",
            HistoryToken.spreadsheetKeyboard(
                ComponentLifecycleMatcherTesting.SPREADSHEET_ID,
                SPREADSHEET_NAME
            ),
            "SpreadsheetKeyboardDialogComponent\n" +
                "  DialogComponent\n" +
                "    Cell A1\n" +
                "    id=keyboard-Dialog includeClose=true\n" +
                "      KeyBindingTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=keyboard-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Select\n" +
                "                  TextNodeComponent\n" +
                "                    Enter \n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Select all\n" +
                "                  TextNodeComponent\n" +
                "                    a control\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Up\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowUp \n" +
                "                ROW 3\n" +
                "                  TextNodeComponent\n" +
                "                    Right\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowRight \n" +
                "                ROW 4\n" +
                "                  TextNodeComponent\n" +
                "                    Down\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowDown \n" +
                "                ROW 5\n" +
                "                  TextNodeComponent\n" +
                "                    Left\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowLeft \n" +
                "                ROW 6\n" +
                "                  TextNodeComponent\n" +
                "                    Delete\n" +
                "                  TextNodeComponent\n" +
                "                    Backspace \n" +
                "                ROW 7\n" +
                "                  TextNodeComponent\n" +
                "                    Extend up\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowUp shift\n" +
                "                ROW 8\n" +
                "                  TextNodeComponent\n" +
                "                    Extend right\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowRight shift\n" +
                "                ROW 9\n" +
                "                  TextNodeComponent\n" +
                "                    Extend down\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowDown shift\n" +
                "                ROW 10\n" +
                "                  TextNodeComponent\n" +
                "                    Extend left\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowLeft shift\n" +
                "                ROW 11\n" +
                "                  TextNodeComponent\n" +
                "                    Extend screen up\n" +
                "                  TextNodeComponent\n" +
                "                    PageUp shift\n" +
                "                ROW 12\n" +
                "                  TextNodeComponent\n" +
                "                    Extend screen right\n" +
                "                  TextNodeComponent\n" +
                "                    End shift\n" +
                "                ROW 13\n" +
                "                  TextNodeComponent\n" +
                "                    Extend screen down\n" +
                "                  TextNodeComponent\n" +
                "                    PageDown shift\n" +
                "                ROW 14\n" +
                "                  TextNodeComponent\n" +
                "                    Extend screen left\n" +
                "                  TextNodeComponent\n" +
                "                    Home shift\n" +
                "                ROW 15\n" +
                "                  TextNodeComponent\n" +
                "                    Screen up\n" +
                "                  TextNodeComponent\n" +
                "                    PageUp \n" +
                "                ROW 16\n" +
                "                  TextNodeComponent\n" +
                "                    Screen right\n" +
                "                  TextNodeComponent\n" +
                "                    End \n" +
                "                ROW 17\n" +
                "                  TextNodeComponent\n" +
                "                    Screen down\n" +
                "                  TextNodeComponent\n" +
                "                    PageDown \n" +
                "                ROW 18\n" +
                "                  TextNodeComponent\n" +
                "                    Screen left\n" +
                "                  TextNodeComponent\n" +
                "                    Home \n" +
                "                ROW 19\n" +
                "                  TextNodeComponent\n" +
                "                    Exit\n" +
                "                  TextNodeComponent\n" +
                "                    Escape \n" +
                "                ROW 20\n" +
                "                  TextNodeComponent\n" +
                "                    Text Align: left\n" +
                "                  TextNodeComponent\n" +
                "                    l control\n" +
                "                ROW 21\n" +
                "                  TextNodeComponent\n" +
                "                    Text Align: center\n" +
                "                  TextNodeComponent\n" +
                "                    c control\n" +
                "                ROW 22\n" +
                "                  TextNodeComponent\n" +
                "                    Text Align: right\n" +
                "                  TextNodeComponent\n" +
                "                    r control\n" +
                "                ROW 23\n" +
                "                  TextNodeComponent\n" +
                "                    Text Align: justify\n" +
                "                  TextNodeComponent\n" +
                "                    j control\n" +
                "                ROW 24\n" +
                "                  TextNodeComponent\n" +
                "                    Vertical Align: top\n" +
                "                  TextNodeComponent\n" +
                "                    T control+shift\n" +
                "                ROW 25\n" +
                "                  TextNodeComponent\n" +
                "                    Vertical Align: middle\n" +
                "                  TextNodeComponent\n" +
                "                    M control+shift\n" +
                "                ROW 26\n" +
                "                  TextNodeComponent\n" +
                "                    Vertical Align: bottom\n" +
                "                  TextNodeComponent\n" +
                "                    B control+shift\n" +
                "                ROW 27\n" +
                "                  TextNodeComponent\n" +
                "                    Currency format\n" +
                "                  TextNodeComponent\n" +
                "                    4 control+shift\n" +
                "                ROW 28\n" +
                "                  TextNodeComponent\n" +
                "                    General format\n" +
                "                  TextNodeComponent\n" +
                "                    7 control+shift\n" +
                "                ROW 29\n" +
                "                  TextNodeComponent\n" +
                "                    Date format\n" +
                "                  TextNodeComponent\n" +
                "                    3 control+shift\n" +
                "                ROW 30\n" +
                "                  TextNodeComponent\n" +
                "                    Number format\n" +
                "                  TextNodeComponent\n" +
                "                    1 control+shift\n" +
                "                ROW 31\n" +
                "                  TextNodeComponent\n" +
                "                    Percent format\n" +
                "                  TextNodeComponent\n" +
                "                    5 control+shift\n" +
                "                ROW 32\n" +
                "                  TextNodeComponent\n" +
                "                    Scientific format\n" +
                "                  TextNodeComponent\n" +
                "                    6 control+shift\n" +
                "                ROW 33\n" +
                "                  TextNodeComponent\n" +
                "                    Text format\n" +
                "                  TextNodeComponent\n" +
                "                    8 control+shift\n" +
                "                ROW 34\n" +
                "                  TextNodeComponent\n" +
                "                    Time format\n" +
                "                  TextNodeComponent\n" +
                "                    2 control+shift\n" +
                "                ROW 35\n" +
                "                  TextNodeComponent\n" +
                "                    Bold\n" +
                "                  TextNodeComponent\n" +
                "                    b control\n" +
                "                ROW 36\n" +
                "                  TextNodeComponent\n" +
                "                    Bold\n" +
                "                  TextNodeComponent\n" +
                "                    2 control\n" +
                "                ROW 37\n" +
                "                  TextNodeComponent\n" +
                "                    Italics\n" +
                "                  TextNodeComponent\n" +
                "                    i control\n" +
                "                ROW 38\n" +
                "                  TextNodeComponent\n" +
                "                    Italics\n" +
                "                  TextNodeComponent\n" +
                "                    3 control\n" +
                "                ROW 39\n" +
                "                  TextNodeComponent\n" +
                "                    Normal text\n" +
                "                  TextNodeComponent\n" +
                "                    N control+shift\n" +
                "                ROW 40\n" +
                "                  TextNodeComponent\n" +
                "                    Strikethru\n" +
                "                  TextNodeComponent\n" +
                "                    5 control\n" +
                "                ROW 41\n" +
                "                  TextNodeComponent\n" +
                "                    Underline\n" +
                "                  TextNodeComponent\n" +
                "                    u control\n" +
                "                ROW 42\n" +
                "                  TextNodeComponent\n" +
                "                    Underline\n" +
                "                  TextNodeComponent\n" +
                "                    4 control\n" +
                "                ROW 43\n" +
                "                  TextNodeComponent\n" +
                "                    Capitalize\n" +
                "                  TextNodeComponent\n" +
                "                    C control+shift\n" +
                "                ROW 44\n" +
                "                  TextNodeComponent\n" +
                "                    Lowercase\n" +
                "                  TextNodeComponent\n" +
                "                    L control+shift\n" +
                "                ROW 45\n" +
                "                  TextNodeComponent\n" +
                "                    Uppercase\n" +
                "                  TextNodeComponent\n" +
                "                    U control+shift\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Close\" [#/1/SpreadsheetName1] id=keyboard-close-Link\n"
        );
    }


    @Test
    public void testOnHistoryTokenCell() {
        this.refreshAndCheck(
            "Cell A1",
            HistoryToken.cellKeyboard(
                ComponentLifecycleMatcherTesting.SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            "SpreadsheetKeyboardDialogComponent\n" +
                "  DialogComponent\n" +
                "    Cell A1\n" +
                "    id=keyboard-Dialog includeClose=true\n" +
                "      KeyBindingTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=keyboard-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Select\n" +
                "                  TextNodeComponent\n" +
                "                    Enter \n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Select all\n" +
                "                  TextNodeComponent\n" +
                "                    a control\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Up\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowUp \n" +
                "                ROW 3\n" +
                "                  TextNodeComponent\n" +
                "                    Right\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowRight \n" +
                "                ROW 4\n" +
                "                  TextNodeComponent\n" +
                "                    Down\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowDown \n" +
                "                ROW 5\n" +
                "                  TextNodeComponent\n" +
                "                    Left\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowLeft \n" +
                "                ROW 6\n" +
                "                  TextNodeComponent\n" +
                "                    Delete\n" +
                "                  TextNodeComponent\n" +
                "                    Backspace \n" +
                "                ROW 7\n" +
                "                  TextNodeComponent\n" +
                "                    Extend up\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowUp shift\n" +
                "                ROW 8\n" +
                "                  TextNodeComponent\n" +
                "                    Extend right\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowRight shift\n" +
                "                ROW 9\n" +
                "                  TextNodeComponent\n" +
                "                    Extend down\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowDown shift\n" +
                "                ROW 10\n" +
                "                  TextNodeComponent\n" +
                "                    Extend left\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowLeft shift\n" +
                "                ROW 11\n" +
                "                  TextNodeComponent\n" +
                "                    Extend screen up\n" +
                "                  TextNodeComponent\n" +
                "                    PageUp shift\n" +
                "                ROW 12\n" +
                "                  TextNodeComponent\n" +
                "                    Extend screen right\n" +
                "                  TextNodeComponent\n" +
                "                    End shift\n" +
                "                ROW 13\n" +
                "                  TextNodeComponent\n" +
                "                    Extend screen down\n" +
                "                  TextNodeComponent\n" +
                "                    PageDown shift\n" +
                "                ROW 14\n" +
                "                  TextNodeComponent\n" +
                "                    Extend screen left\n" +
                "                  TextNodeComponent\n" +
                "                    Home shift\n" +
                "                ROW 15\n" +
                "                  TextNodeComponent\n" +
                "                    Screen up\n" +
                "                  TextNodeComponent\n" +
                "                    PageUp \n" +
                "                ROW 16\n" +
                "                  TextNodeComponent\n" +
                "                    Screen right\n" +
                "                  TextNodeComponent\n" +
                "                    End \n" +
                "                ROW 17\n" +
                "                  TextNodeComponent\n" +
                "                    Screen down\n" +
                "                  TextNodeComponent\n" +
                "                    PageDown \n" +
                "                ROW 18\n" +
                "                  TextNodeComponent\n" +
                "                    Screen left\n" +
                "                  TextNodeComponent\n" +
                "                    Home \n" +
                "                ROW 19\n" +
                "                  TextNodeComponent\n" +
                "                    Exit\n" +
                "                  TextNodeComponent\n" +
                "                    Escape \n" +
                "                ROW 20\n" +
                "                  TextNodeComponent\n" +
                "                    Text Align: left\n" +
                "                  TextNodeComponent\n" +
                "                    l control\n" +
                "                ROW 21\n" +
                "                  TextNodeComponent\n" +
                "                    Text Align: center\n" +
                "                  TextNodeComponent\n" +
                "                    c control\n" +
                "                ROW 22\n" +
                "                  TextNodeComponent\n" +
                "                    Text Align: right\n" +
                "                  TextNodeComponent\n" +
                "                    r control\n" +
                "                ROW 23\n" +
                "                  TextNodeComponent\n" +
                "                    Text Align: justify\n" +
                "                  TextNodeComponent\n" +
                "                    j control\n" +
                "                ROW 24\n" +
                "                  TextNodeComponent\n" +
                "                    Vertical Align: top\n" +
                "                  TextNodeComponent\n" +
                "                    T control+shift\n" +
                "                ROW 25\n" +
                "                  TextNodeComponent\n" +
                "                    Vertical Align: middle\n" +
                "                  TextNodeComponent\n" +
                "                    M control+shift\n" +
                "                ROW 26\n" +
                "                  TextNodeComponent\n" +
                "                    Vertical Align: bottom\n" +
                "                  TextNodeComponent\n" +
                "                    B control+shift\n" +
                "                ROW 27\n" +
                "                  TextNodeComponent\n" +
                "                    Currency format\n" +
                "                  TextNodeComponent\n" +
                "                    4 control+shift\n" +
                "                ROW 28\n" +
                "                  TextNodeComponent\n" +
                "                    General format\n" +
                "                  TextNodeComponent\n" +
                "                    7 control+shift\n" +
                "                ROW 29\n" +
                "                  TextNodeComponent\n" +
                "                    Date format\n" +
                "                  TextNodeComponent\n" +
                "                    3 control+shift\n" +
                "                ROW 30\n" +
                "                  TextNodeComponent\n" +
                "                    Number format\n" +
                "                  TextNodeComponent\n" +
                "                    1 control+shift\n" +
                "                ROW 31\n" +
                "                  TextNodeComponent\n" +
                "                    Percent format\n" +
                "                  TextNodeComponent\n" +
                "                    5 control+shift\n" +
                "                ROW 32\n" +
                "                  TextNodeComponent\n" +
                "                    Scientific format\n" +
                "                  TextNodeComponent\n" +
                "                    6 control+shift\n" +
                "                ROW 33\n" +
                "                  TextNodeComponent\n" +
                "                    Text format\n" +
                "                  TextNodeComponent\n" +
                "                    8 control+shift\n" +
                "                ROW 34\n" +
                "                  TextNodeComponent\n" +
                "                    Time format\n" +
                "                  TextNodeComponent\n" +
                "                    2 control+shift\n" +
                "                ROW 35\n" +
                "                  TextNodeComponent\n" +
                "                    Bold\n" +
                "                  TextNodeComponent\n" +
                "                    b control\n" +
                "                ROW 36\n" +
                "                  TextNodeComponent\n" +
                "                    Bold\n" +
                "                  TextNodeComponent\n" +
                "                    2 control\n" +
                "                ROW 37\n" +
                "                  TextNodeComponent\n" +
                "                    Italics\n" +
                "                  TextNodeComponent\n" +
                "                    i control\n" +
                "                ROW 38\n" +
                "                  TextNodeComponent\n" +
                "                    Italics\n" +
                "                  TextNodeComponent\n" +
                "                    3 control\n" +
                "                ROW 39\n" +
                "                  TextNodeComponent\n" +
                "                    Normal text\n" +
                "                  TextNodeComponent\n" +
                "                    N control+shift\n" +
                "                ROW 40\n" +
                "                  TextNodeComponent\n" +
                "                    Strikethru\n" +
                "                  TextNodeComponent\n" +
                "                    5 control\n" +
                "                ROW 41\n" +
                "                  TextNodeComponent\n" +
                "                    Underline\n" +
                "                  TextNodeComponent\n" +
                "                    u control\n" +
                "                ROW 42\n" +
                "                  TextNodeComponent\n" +
                "                    Underline\n" +
                "                  TextNodeComponent\n" +
                "                    4 control\n" +
                "                ROW 43\n" +
                "                  TextNodeComponent\n" +
                "                    Capitalize\n" +
                "                  TextNodeComponent\n" +
                "                    C control+shift\n" +
                "                ROW 44\n" +
                "                  TextNodeComponent\n" +
                "                    Lowercase\n" +
                "                  TextNodeComponent\n" +
                "                    L control+shift\n" +
                "                ROW 45\n" +
                "                  TextNodeComponent\n" +
                "                    Uppercase\n" +
                "                  TextNodeComponent\n" +
                "                    U control+shift\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Close\" [#/1/SpreadsheetName1/cell/A1] id=keyboard-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenColumn() {
        this.refreshAndCheck(
            "Column A",
            HistoryToken.columnKeyboard(
                ComponentLifecycleMatcherTesting.SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.column()
                    .setDefaultAnchor()
            ),
            "SpreadsheetKeyboardDialogComponent\n" +
                "  DialogComponent\n" +
                "    Column A\n" +
                "    id=keyboard-Dialog includeClose=true\n" +
                "      KeyBindingTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=keyboard-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Select\n" +
                "                  TextNodeComponent\n" +
                "                    Enter \n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Select all\n" +
                "                  TextNodeComponent\n" +
                "                    a control\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Up\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowUp \n" +
                "                ROW 3\n" +
                "                  TextNodeComponent\n" +
                "                    Right\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowRight \n" +
                "                ROW 4\n" +
                "                  TextNodeComponent\n" +
                "                    Down\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowDown \n" +
                "                ROW 5\n" +
                "                  TextNodeComponent\n" +
                "                    Left\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowLeft \n" +
                "                ROW 6\n" +
                "                  TextNodeComponent\n" +
                "                    Delete\n" +
                "                  TextNodeComponent\n" +
                "                    Backspace \n" +
                "                ROW 7\n" +
                "                  TextNodeComponent\n" +
                "                    Extend up\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowUp shift\n" +
                "                ROW 8\n" +
                "                  TextNodeComponent\n" +
                "                    Extend right\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowRight shift\n" +
                "                ROW 9\n" +
                "                  TextNodeComponent\n" +
                "                    Extend down\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowDown shift\n" +
                "                ROW 10\n" +
                "                  TextNodeComponent\n" +
                "                    Extend left\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowLeft shift\n" +
                "                ROW 11\n" +
                "                  TextNodeComponent\n" +
                "                    Extend screen up\n" +
                "                  TextNodeComponent\n" +
                "                    PageUp shift\n" +
                "                ROW 12\n" +
                "                  TextNodeComponent\n" +
                "                    Extend screen right\n" +
                "                  TextNodeComponent\n" +
                "                    End shift\n" +
                "                ROW 13\n" +
                "                  TextNodeComponent\n" +
                "                    Extend screen down\n" +
                "                  TextNodeComponent\n" +
                "                    PageDown shift\n" +
                "                ROW 14\n" +
                "                  TextNodeComponent\n" +
                "                    Extend screen left\n" +
                "                  TextNodeComponent\n" +
                "                    Home shift\n" +
                "                ROW 15\n" +
                "                  TextNodeComponent\n" +
                "                    Screen up\n" +
                "                  TextNodeComponent\n" +
                "                    PageUp \n" +
                "                ROW 16\n" +
                "                  TextNodeComponent\n" +
                "                    Screen right\n" +
                "                  TextNodeComponent\n" +
                "                    End \n" +
                "                ROW 17\n" +
                "                  TextNodeComponent\n" +
                "                    Screen down\n" +
                "                  TextNodeComponent\n" +
                "                    PageDown \n" +
                "                ROW 18\n" +
                "                  TextNodeComponent\n" +
                "                    Screen left\n" +
                "                  TextNodeComponent\n" +
                "                    Home \n" +
                "                ROW 19\n" +
                "                  TextNodeComponent\n" +
                "                    Exit\n" +
                "                  TextNodeComponent\n" +
                "                    Escape \n" +
                "                ROW 20\n" +
                "                  TextNodeComponent\n" +
                "                    Text Align: left\n" +
                "                  TextNodeComponent\n" +
                "                    l control\n" +
                "                ROW 21\n" +
                "                  TextNodeComponent\n" +
                "                    Text Align: center\n" +
                "                  TextNodeComponent\n" +
                "                    c control\n" +
                "                ROW 22\n" +
                "                  TextNodeComponent\n" +
                "                    Text Align: right\n" +
                "                  TextNodeComponent\n" +
                "                    r control\n" +
                "                ROW 23\n" +
                "                  TextNodeComponent\n" +
                "                    Text Align: justify\n" +
                "                  TextNodeComponent\n" +
                "                    j control\n" +
                "                ROW 24\n" +
                "                  TextNodeComponent\n" +
                "                    Vertical Align: top\n" +
                "                  TextNodeComponent\n" +
                "                    T control+shift\n" +
                "                ROW 25\n" +
                "                  TextNodeComponent\n" +
                "                    Vertical Align: middle\n" +
                "                  TextNodeComponent\n" +
                "                    M control+shift\n" +
                "                ROW 26\n" +
                "                  TextNodeComponent\n" +
                "                    Vertical Align: bottom\n" +
                "                  TextNodeComponent\n" +
                "                    B control+shift\n" +
                "                ROW 27\n" +
                "                  TextNodeComponent\n" +
                "                    Currency format\n" +
                "                  TextNodeComponent\n" +
                "                    4 control+shift\n" +
                "                ROW 28\n" +
                "                  TextNodeComponent\n" +
                "                    General format\n" +
                "                  TextNodeComponent\n" +
                "                    7 control+shift\n" +
                "                ROW 29\n" +
                "                  TextNodeComponent\n" +
                "                    Date format\n" +
                "                  TextNodeComponent\n" +
                "                    3 control+shift\n" +
                "                ROW 30\n" +
                "                  TextNodeComponent\n" +
                "                    Number format\n" +
                "                  TextNodeComponent\n" +
                "                    1 control+shift\n" +
                "                ROW 31\n" +
                "                  TextNodeComponent\n" +
                "                    Percent format\n" +
                "                  TextNodeComponent\n" +
                "                    5 control+shift\n" +
                "                ROW 32\n" +
                "                  TextNodeComponent\n" +
                "                    Scientific format\n" +
                "                  TextNodeComponent\n" +
                "                    6 control+shift\n" +
                "                ROW 33\n" +
                "                  TextNodeComponent\n" +
                "                    Text format\n" +
                "                  TextNodeComponent\n" +
                "                    8 control+shift\n" +
                "                ROW 34\n" +
                "                  TextNodeComponent\n" +
                "                    Time format\n" +
                "                  TextNodeComponent\n" +
                "                    2 control+shift\n" +
                "                ROW 35\n" +
                "                  TextNodeComponent\n" +
                "                    Bold\n" +
                "                  TextNodeComponent\n" +
                "                    b control\n" +
                "                ROW 36\n" +
                "                  TextNodeComponent\n" +
                "                    Bold\n" +
                "                  TextNodeComponent\n" +
                "                    2 control\n" +
                "                ROW 37\n" +
                "                  TextNodeComponent\n" +
                "                    Italics\n" +
                "                  TextNodeComponent\n" +
                "                    i control\n" +
                "                ROW 38\n" +
                "                  TextNodeComponent\n" +
                "                    Italics\n" +
                "                  TextNodeComponent\n" +
                "                    3 control\n" +
                "                ROW 39\n" +
                "                  TextNodeComponent\n" +
                "                    Normal text\n" +
                "                  TextNodeComponent\n" +
                "                    N control+shift\n" +
                "                ROW 40\n" +
                "                  TextNodeComponent\n" +
                "                    Strikethru\n" +
                "                  TextNodeComponent\n" +
                "                    5 control\n" +
                "                ROW 41\n" +
                "                  TextNodeComponent\n" +
                "                    Underline\n" +
                "                  TextNodeComponent\n" +
                "                    u control\n" +
                "                ROW 42\n" +
                "                  TextNodeComponent\n" +
                "                    Underline\n" +
                "                  TextNodeComponent\n" +
                "                    4 control\n" +
                "                ROW 43\n" +
                "                  TextNodeComponent\n" +
                "                    Capitalize\n" +
                "                  TextNodeComponent\n" +
                "                    C control+shift\n" +
                "                ROW 44\n" +
                "                  TextNodeComponent\n" +
                "                    Lowercase\n" +
                "                  TextNodeComponent\n" +
                "                    L control+shift\n" +
                "                ROW 45\n" +
                "                  TextNodeComponent\n" +
                "                    Uppercase\n" +
                "                  TextNodeComponent\n" +
                "                    U control+shift\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Close\" [#/1/SpreadsheetName1/column/A] id=keyboard-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenRow() {
        this.refreshAndCheck(
            "Row 1",
            HistoryToken.rowKeyboard(
                ComponentLifecycleMatcherTesting.SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.row()
                    .setDefaultAnchor()
            ),
            "SpreadsheetKeyboardDialogComponent\n" +
                "  DialogComponent\n" +
                "    Row 1\n" +
                "    id=keyboard-Dialog includeClose=true\n" +
                "      KeyBindingTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=keyboard-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Select\n" +
                "                  TextNodeComponent\n" +
                "                    Enter \n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Select all\n" +
                "                  TextNodeComponent\n" +
                "                    a control\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Up\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowUp \n" +
                "                ROW 3\n" +
                "                  TextNodeComponent\n" +
                "                    Right\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowRight \n" +
                "                ROW 4\n" +
                "                  TextNodeComponent\n" +
                "                    Down\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowDown \n" +
                "                ROW 5\n" +
                "                  TextNodeComponent\n" +
                "                    Left\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowLeft \n" +
                "                ROW 6\n" +
                "                  TextNodeComponent\n" +
                "                    Delete\n" +
                "                  TextNodeComponent\n" +
                "                    Backspace \n" +
                "                ROW 7\n" +
                "                  TextNodeComponent\n" +
                "                    Extend up\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowUp shift\n" +
                "                ROW 8\n" +
                "                  TextNodeComponent\n" +
                "                    Extend right\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowRight shift\n" +
                "                ROW 9\n" +
                "                  TextNodeComponent\n" +
                "                    Extend down\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowDown shift\n" +
                "                ROW 10\n" +
                "                  TextNodeComponent\n" +
                "                    Extend left\n" +
                "                  TextNodeComponent\n" +
                "                    ArrowLeft shift\n" +
                "                ROW 11\n" +
                "                  TextNodeComponent\n" +
                "                    Extend screen up\n" +
                "                  TextNodeComponent\n" +
                "                    PageUp shift\n" +
                "                ROW 12\n" +
                "                  TextNodeComponent\n" +
                "                    Extend screen right\n" +
                "                  TextNodeComponent\n" +
                "                    End shift\n" +
                "                ROW 13\n" +
                "                  TextNodeComponent\n" +
                "                    Extend screen down\n" +
                "                  TextNodeComponent\n" +
                "                    PageDown shift\n" +
                "                ROW 14\n" +
                "                  TextNodeComponent\n" +
                "                    Extend screen left\n" +
                "                  TextNodeComponent\n" +
                "                    Home shift\n" +
                "                ROW 15\n" +
                "                  TextNodeComponent\n" +
                "                    Screen up\n" +
                "                  TextNodeComponent\n" +
                "                    PageUp \n" +
                "                ROW 16\n" +
                "                  TextNodeComponent\n" +
                "                    Screen right\n" +
                "                  TextNodeComponent\n" +
                "                    End \n" +
                "                ROW 17\n" +
                "                  TextNodeComponent\n" +
                "                    Screen down\n" +
                "                  TextNodeComponent\n" +
                "                    PageDown \n" +
                "                ROW 18\n" +
                "                  TextNodeComponent\n" +
                "                    Screen left\n" +
                "                  TextNodeComponent\n" +
                "                    Home \n" +
                "                ROW 19\n" +
                "                  TextNodeComponent\n" +
                "                    Exit\n" +
                "                  TextNodeComponent\n" +
                "                    Escape \n" +
                "                ROW 20\n" +
                "                  TextNodeComponent\n" +
                "                    Text Align: left\n" +
                "                  TextNodeComponent\n" +
                "                    l control\n" +
                "                ROW 21\n" +
                "                  TextNodeComponent\n" +
                "                    Text Align: center\n" +
                "                  TextNodeComponent\n" +
                "                    c control\n" +
                "                ROW 22\n" +
                "                  TextNodeComponent\n" +
                "                    Text Align: right\n" +
                "                  TextNodeComponent\n" +
                "                    r control\n" +
                "                ROW 23\n" +
                "                  TextNodeComponent\n" +
                "                    Text Align: justify\n" +
                "                  TextNodeComponent\n" +
                "                    j control\n" +
                "                ROW 24\n" +
                "                  TextNodeComponent\n" +
                "                    Vertical Align: top\n" +
                "                  TextNodeComponent\n" +
                "                    T control+shift\n" +
                "                ROW 25\n" +
                "                  TextNodeComponent\n" +
                "                    Vertical Align: middle\n" +
                "                  TextNodeComponent\n" +
                "                    M control+shift\n" +
                "                ROW 26\n" +
                "                  TextNodeComponent\n" +
                "                    Vertical Align: bottom\n" +
                "                  TextNodeComponent\n" +
                "                    B control+shift\n" +
                "                ROW 27\n" +
                "                  TextNodeComponent\n" +
                "                    Currency format\n" +
                "                  TextNodeComponent\n" +
                "                    4 control+shift\n" +
                "                ROW 28\n" +
                "                  TextNodeComponent\n" +
                "                    General format\n" +
                "                  TextNodeComponent\n" +
                "                    7 control+shift\n" +
                "                ROW 29\n" +
                "                  TextNodeComponent\n" +
                "                    Date format\n" +
                "                  TextNodeComponent\n" +
                "                    3 control+shift\n" +
                "                ROW 30\n" +
                "                  TextNodeComponent\n" +
                "                    Number format\n" +
                "                  TextNodeComponent\n" +
                "                    1 control+shift\n" +
                "                ROW 31\n" +
                "                  TextNodeComponent\n" +
                "                    Percent format\n" +
                "                  TextNodeComponent\n" +
                "                    5 control+shift\n" +
                "                ROW 32\n" +
                "                  TextNodeComponent\n" +
                "                    Scientific format\n" +
                "                  TextNodeComponent\n" +
                "                    6 control+shift\n" +
                "                ROW 33\n" +
                "                  TextNodeComponent\n" +
                "                    Text format\n" +
                "                  TextNodeComponent\n" +
                "                    8 control+shift\n" +
                "                ROW 34\n" +
                "                  TextNodeComponent\n" +
                "                    Time format\n" +
                "                  TextNodeComponent\n" +
                "                    2 control+shift\n" +
                "                ROW 35\n" +
                "                  TextNodeComponent\n" +
                "                    Bold\n" +
                "                  TextNodeComponent\n" +
                "                    b control\n" +
                "                ROW 36\n" +
                "                  TextNodeComponent\n" +
                "                    Bold\n" +
                "                  TextNodeComponent\n" +
                "                    2 control\n" +
                "                ROW 37\n" +
                "                  TextNodeComponent\n" +
                "                    Italics\n" +
                "                  TextNodeComponent\n" +
                "                    i control\n" +
                "                ROW 38\n" +
                "                  TextNodeComponent\n" +
                "                    Italics\n" +
                "                  TextNodeComponent\n" +
                "                    3 control\n" +
                "                ROW 39\n" +
                "                  TextNodeComponent\n" +
                "                    Normal text\n" +
                "                  TextNodeComponent\n" +
                "                    N control+shift\n" +
                "                ROW 40\n" +
                "                  TextNodeComponent\n" +
                "                    Strikethru\n" +
                "                  TextNodeComponent\n" +
                "                    5 control\n" +
                "                ROW 41\n" +
                "                  TextNodeComponent\n" +
                "                    Underline\n" +
                "                  TextNodeComponent\n" +
                "                    u control\n" +
                "                ROW 42\n" +
                "                  TextNodeComponent\n" +
                "                    Underline\n" +
                "                  TextNodeComponent\n" +
                "                    4 control\n" +
                "                ROW 43\n" +
                "                  TextNodeComponent\n" +
                "                    Capitalize\n" +
                "                  TextNodeComponent\n" +
                "                    C control+shift\n" +
                "                ROW 44\n" +
                "                  TextNodeComponent\n" +
                "                    Lowercase\n" +
                "                  TextNodeComponent\n" +
                "                    L control+shift\n" +
                "                ROW 45\n" +
                "                  TextNodeComponent\n" +
                "                    Uppercase\n" +
                "                  TextNodeComponent\n" +
                "                    U control+shift\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Close\" [#/1/SpreadsheetName1/row/1] id=keyboard-close-Link\n"
        );
    }

    private void refreshAndCheck(final String dialogTitle,
                                 final HistoryToken historyToken,
                                 final String expected) {
        final AppContext context = new FakeAppContext() {

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return null;
            }

            @Override
            public HistoryToken historyToken() {
                return historyToken;
            }

            @Override
            public SpreadsheetMetadata spreadsheetMetadata() {
                return SpreadsheetMetadataTesting.METADATA_EN_AU.set(
                    SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
                    SpreadsheetColumnRowInsertCountDialogComponentTest.SPREADSHEET_ID
                ).set(
                    SpreadsheetMetadataPropertyName.SPREADSHEET_NAME,
                    SPREADSHEET_NAME
                );
            }

            @Override
            public void giveFocus(final Runnable runnable) {
                // nop
            }
        };

        final SpreadsheetKeyboardDialogComponent dialog = SpreadsheetKeyboardDialogComponent.with(
            new FakeSpreadsheetKeyboardDialogComponentContext() {

                @Override
                public String dialogTitle() {
                    return dialogTitle;
                }

                @Override
                public HistoryToken historyToken() {
                    return historyToken;
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
                    return token.equals(this.historyToken());
                }
            }
        );

        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            expected
        );
    }

    @Override
    public SpreadsheetKeyboardDialogComponent createSpreadsheetDialogComponentLifecycle(final HistoryToken historyToken) {
        return SpreadsheetKeyboardDialogComponent.with(
            SpreadsheetKeyboardDialogComponentContexts.cell(
                new FakeAppContext() {

                    @Override
                    public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                        return null;
                    }

                    @Override
                    public HistoryToken historyToken() {
                        return historyToken;
                    }

                    @Override
                    public SpreadsheetViewportComponentKeyBindings spreadsheetViewportComponentKeyBindings() {
                        return SpreadsheetViewportComponentKeyBindingses.basic();
                    }
                }
            )
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetKeyboardDialogComponent> type() {
        return SpreadsheetKeyboardDialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}

