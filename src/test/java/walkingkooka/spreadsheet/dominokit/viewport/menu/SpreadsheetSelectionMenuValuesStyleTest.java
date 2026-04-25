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

package walkingkooka.spreadsheet.dominokit.viewport.menu;

import org.dominokit.domino.ui.menu.Menu;
import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.color.Color;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuFactory;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.format.SpreadsheetColorName;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStyleProperty;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public final class SpreadsheetSelectionMenuValuesStyleTest extends SpreadsheetSelectionMenuValuesTestCase<SpreadsheetSelectionMenuValuesStyle, TextStyleProperty<?>> {

    @Test
    public void testBuild() {
        this.buildAndCheck(
            HistoryToken.cellStyle(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL,
                Optional.empty()
            ),
            Optional.empty(), // summary
            Lists.empty(),
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"Style\" id=test-Style-SubMenu\n" +
                "    \"Alignment\" id=test-Style-alignment-SubMenu\n" +
                "      (mdi-format-align-left) \"Left\" [/1/SpreadsheetName111/cell/A1/style/text-align/save/LEFT] id=test-Style-alignment-left-MenuItem\n" +
                "      (mdi-format-align-center) \"Center\" [/1/SpreadsheetName111/cell/A1/style/text-align/save/CENTER] id=test-Style-alignment-center-MenuItem\n" +
                "      (mdi-format-align-right) \"Right\" [/1/SpreadsheetName111/cell/A1/style/text-align/save/RIGHT] id=test-Style-alignment-right-MenuItem\n" +
                "      (mdi-format-align-justify) \"Justify\" [/1/SpreadsheetName111/cell/A1/style/text-align/save/JUSTIFY] id=test-Style-alignment-justify-MenuItem\n" +
                "    \"Vertical Alignment\" id=test-Style-verticalAlignment-SubMenu\n" +
                "      (mdi-format-align-top) \"Top\" [/1/SpreadsheetName111/cell/A1/style/vertical-align/save/TOP] id=test-Style-verticalAlignment-top-MenuItem\n" +
                "      (mdi-format-align-middle) \"Middle\" [/1/SpreadsheetName111/cell/A1/style/vertical-align/save/MIDDLE] id=test-Style-verticalAlignment-middle-MenuItem\n" +
                "      (mdi-format-align-bottom) \"Bottom\" [/1/SpreadsheetName111/cell/A1/style/vertical-align/save/BOTTOM] id=test-Style-verticalAlignment-bottom-MenuItem\n" +
                "    (mdi-palette) \"Color\" id=test-Style-color-SubMenu\n" +
                "      ColorPaletteComponent\n" +
                "        TABLE\n" +
                "          id=\"test-Style-color-Table\" className=dui dui-menu-item\n" +
                "            TBODY\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"Black\" [#/1/SpreadsheetName111/cell/A1/style/color/save/black] id=test-Style-color-color-1-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-2-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-3-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-4-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-5-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-6-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-7-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-8-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-9-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-10-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-11-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-12-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-13-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-14-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-15-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-16-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-17-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-18-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-19-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-20-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-21-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-22-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-23-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-24-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-25-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-26-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-27-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-28-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-29-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-30-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-31-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-32-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-33-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-34-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-35-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-36-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-37-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-38-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-39-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-40-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-41-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-42-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-43-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-44-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-45-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-46-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-47-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-48-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-49-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-50-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-51-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-52-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-53-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-54-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-55-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-56-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                    \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/color/save/] id=test-Style-color-color-clear-Link\n" +
                "    (mdi-palette) \"Background Color\" id=test-Style-backgroundColor-SubMenu\n" +
                "      ColorPaletteComponent\n" +
                "        TABLE\n" +
                "          id=\"test-Style-backgroundColor-Table\" className=dui dui-menu-item\n" +
                "            TBODY\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"Black\" [#/1/SpreadsheetName111/cell/A1/style/background-color/save/black] id=test-Style-backgroundColor-color-1-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-2-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-3-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-4-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-5-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-6-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-7-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-8-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-9-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-10-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-11-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-12-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-13-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-14-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-15-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-16-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-17-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-18-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-19-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-20-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-21-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-22-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-23-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-24-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-25-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-26-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-27-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-28-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-29-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-30-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-31-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-32-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-33-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-34-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-35-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-36-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-37-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-38-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-39-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-40-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-41-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-42-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-43-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-44-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-45-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-46-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-47-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-48-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-49-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-50-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-51-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-52-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-53-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-54-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-55-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-56-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                    \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/background-color/save/] id=test-Style-backgroundColor-color-clear-Link\n" +
                "    (mdi-format-bold) \"Bold\" [/1/SpreadsheetName111/cell/A1/style/font-weight/save/BOLD] id=test-Style-bold-MenuItem\n" +
                "    (mdi-format-italic) \"Italics\" [/1/SpreadsheetName111/cell/A1/style/font-style/save/ITALIC] id=test-Style-italics-MenuItem\n" +
                "    (mdi-format-strikethrough) \"Strike-thru\" [/1/SpreadsheetName111/cell/A1/style/text-decoration-line/save/LINE_THROUGH] id=test-strikeThru-MenuItem\n" +
                "    (mdi-format-underline) \"Underline\" [/1/SpreadsheetName111/cell/A1/style/text-decoration-line/save/UNDERLINE] id=test-underline-MenuItem\n" +
                "    \"Text case\" id=test-Style-textCase-SubMenu\n" +
                "      (mdi-format-letter-case-upper) \"Normal\" [/1/SpreadsheetName111/cell/A1/style/text-transform/save/] id=test-Style-textCase-normal-MenuItem\n" +
                "      (mdi-format-letter-case) \"Capitalize\" [/1/SpreadsheetName111/cell/A1/style/text-transform/save/CAPITALIZE] id=test-Style-textCase-capitalize-MenuItem\n" +
                "      (mdi-format-letter-case-lower) \"Lower case\" [/1/SpreadsheetName111/cell/A1/style/text-transform/save/LOWERCASE] id=test-Style-textCase-lower-MenuItem\n" +
                "      (mdi-format-letter-case-upper) \"Upper case\" [/1/SpreadsheetName111/cell/A1/style/text-transform/save/UPPERCASE] id=test-Style-textCase-upper-MenuItem\n" +
                "    \"Wrapping\" id=test-Style-textWrapping-SubMenu\n" +
                "      (mdi-format-text-wrapping-clip) \"Clip\" [/1/SpreadsheetName111/cell/A1/style/overflow-wrap/save/NORMAL] id=test-Style-textWrapping-clip-MenuItem\n" +
                "      (mdi-format-text-wrapping-overflow) \"Overflow\" [/1/SpreadsheetName111/cell/A1/style/overflow-x/save/VISIBLE] id=test-Style-textWrapping-overflow-MenuItem\n" +
                "      (mdi-format-text-wrapping-wrap) \"Wrap\" [/1/SpreadsheetName111/cell/A1/style/overflow-x/save/HIDDEN] id=test-Style-textWrapping-wrap-MenuItem\n" +
                "    \"Border\" id=test-Style-border-SubMenu\n" +
                "      (mdi-border-top-variant) \"Top\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Top Color\" id=test-Style-borderTopColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderTopColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"Black\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/black] id=test-Style-borderTopColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/] id=test-Style-borderTopColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderTopStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/NONE] id=test-Style-borderTopStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/HIDDEN] id=test-Style-borderTopStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/DOTTED] id=test-Style-borderTopStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/DASHED] id=test-Style-borderTopStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/SOLID] id=test-Style-borderTopStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/DOUBLE] id=test-Style-borderTopStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/GROOVE] id=test-Style-borderTopStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/RIDGE] id=test-Style-borderTopStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/INSET] id=test-Style-borderTopStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/OUTSET] id=test-Style-borderTopStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/] id=test-Style-borderTopStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderTopWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName111/cell/A1/style/border-top-width/save/0px] id=test-Style-borderTopWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName111/cell/A1/style/border-top-width/save/1px] id=test-Style-borderTopWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName111/cell/A1/style/border-top-width/save/2px] id=test-Style-borderTopWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName111/cell/A1/style/border-top-width/save/3px] id=test-Style-borderTopWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName111/cell/A1/style/border-top-width/save/4px] id=test-Style-borderTopWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName111/cell/A1/style/border-top-width/save/] id=test-Style-borderTopWidth-clear-MenuItem\n" +
                "      (mdi-border-left-variant) \"Left\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Left Color\" id=test-Style-borderLeftColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderLeftColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"Black\" [#/1/SpreadsheetName111/cell/A1/style/border-left-color/save/black] id=test-Style-borderLeftColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/border-left-color/save/] id=test-Style-borderLeftColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderLeftStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/NONE] id=test-Style-borderLeftStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/HIDDEN] id=test-Style-borderLeftStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/DOTTED] id=test-Style-borderLeftStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/DASHED] id=test-Style-borderLeftStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/SOLID] id=test-Style-borderLeftStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/DOUBLE] id=test-Style-borderLeftStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/GROOVE] id=test-Style-borderLeftStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/RIDGE] id=test-Style-borderLeftStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/INSET] id=test-Style-borderLeftStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/OUTSET] id=test-Style-borderLeftStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/] id=test-Style-borderLeftStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderLeftWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName111/cell/A1/style/border-left-width/save/0px] id=test-Style-borderLeftWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName111/cell/A1/style/border-left-width/save/1px] id=test-Style-borderLeftWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName111/cell/A1/style/border-left-width/save/2px] id=test-Style-borderLeftWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName111/cell/A1/style/border-left-width/save/3px] id=test-Style-borderLeftWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName111/cell/A1/style/border-left-width/save/4px] id=test-Style-borderLeftWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName111/cell/A1/style/border-left-width/save/] id=test-Style-borderLeftWidth-clear-MenuItem\n" +
                "      (mdi-border-right-variant) \"Right\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Right Color\" id=test-Style-borderRightColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderRightColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"Black\" [#/1/SpreadsheetName111/cell/A1/style/border-right-color/save/black] id=test-Style-borderRightColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/border-right-color/save/] id=test-Style-borderRightColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderRightStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/NONE] id=test-Style-borderRightStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/HIDDEN] id=test-Style-borderRightStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/DOTTED] id=test-Style-borderRightStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/DASHED] id=test-Style-borderRightStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/SOLID] id=test-Style-borderRightStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/DOUBLE] id=test-Style-borderRightStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/GROOVE] id=test-Style-borderRightStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/RIDGE] id=test-Style-borderRightStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/INSET] id=test-Style-borderRightStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/OUTSET] id=test-Style-borderRightStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/] id=test-Style-borderRightStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderRightWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName111/cell/A1/style/border-right-width/save/0px] id=test-Style-borderRightWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName111/cell/A1/style/border-right-width/save/1px] id=test-Style-borderRightWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName111/cell/A1/style/border-right-width/save/2px] id=test-Style-borderRightWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName111/cell/A1/style/border-right-width/save/3px] id=test-Style-borderRightWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName111/cell/A1/style/border-right-width/save/4px] id=test-Style-borderRightWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName111/cell/A1/style/border-right-width/save/] id=test-Style-borderRightWidth-clear-MenuItem\n" +
                "      (mdi-border-bottom-variant) \"Bottom\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Bottom Color\" id=test-Style-borderBottomColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderBottomColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"Black\" [#/1/SpreadsheetName111/cell/A1/style/border-bottom-color/save/black] id=test-Style-borderBottomColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/border-bottom-color/save/] id=test-Style-borderBottomColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderBottomStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/NONE] id=test-Style-borderBottomStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/HIDDEN] id=test-Style-borderBottomStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/DOTTED] id=test-Style-borderBottomStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/DASHED] id=test-Style-borderBottomStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/SOLID] id=test-Style-borderBottomStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/DOUBLE] id=test-Style-borderBottomStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/GROOVE] id=test-Style-borderBottomStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/RIDGE] id=test-Style-borderBottomStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/INSET] id=test-Style-borderBottomStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/OUTSET] id=test-Style-borderBottomStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/] id=test-Style-borderBottomStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderBottomWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-width/save/0px] id=test-Style-borderBottomWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-width/save/1px] id=test-Style-borderBottomWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-width/save/2px] id=test-Style-borderBottomWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-width/save/3px] id=test-Style-borderBottomWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-width/save/4px] id=test-Style-borderBottomWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-width/save/] id=test-Style-borderBottomWidth-clear-MenuItem\n" +
                "      (mdi-border-all-variant) \"All\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Color\" id=test-Style-borderColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"Black\" [#/1/SpreadsheetName111/cell/A1/style/border-color/save/black] id=test-Style-borderColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/border-color/save/] id=test-Style-borderColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderAllStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/NONE] id=test-Style-borderAllStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/HIDDEN] id=test-Style-borderAllStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/DOTTED] id=test-Style-borderAllStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/DASHED] id=test-Style-borderAllStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/SOLID] id=test-Style-borderAllStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/DOUBLE] id=test-Style-borderAllStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/GROOVE] id=test-Style-borderAllStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/RIDGE] id=test-Style-borderAllStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/INSET] id=test-Style-borderAllStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/OUTSET] id=test-Style-borderAllStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/] id=test-Style-borderAllStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderAllWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName111/cell/A1/style/border-width/save/0px] id=test-Style-borderAllWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName111/cell/A1/style/border-width/save/1px] id=test-Style-borderAllWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName111/cell/A1/style/border-width/save/2px] id=test-Style-borderAllWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName111/cell/A1/style/border-width/save/3px] id=test-Style-borderAllWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName111/cell/A1/style/border-width/save/4px] id=test-Style-borderAllWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName111/cell/A1/style/border-width/save/] id=test-Style-borderAllWidth-clear-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear...\" [/1/SpreadsheetName111/cell/A1/style/*/save/] id=test-Style-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName111/cell/A1/style] id=test-Style-edit-MenuItem\n"
        );
    }

    @Test
    public void testBuildWithChecked() {
        this.buildAndCheck(
            HistoryToken.cellStyle(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL,
                Optional.empty()
            ),
            Optional.of(
                SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                    .setStyle(
                        TextStyle.EMPTY.set(
                            TextStylePropertyName.TEXT_ALIGN,
                            TextAlign.LEFT
                        )
                    )
            ),
            Lists.empty(),
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"Style\" id=test-Style-SubMenu\n" +
                "    \"Alignment\" id=test-Style-alignment-SubMenu\n" +
                "      (mdi-format-align-left) \"Left\" [/1/SpreadsheetName111/cell/A1/style/text-align/save/] CHECKED id=test-Style-alignment-left-MenuItem\n" +
                "      (mdi-format-align-center) \"Center\" [/1/SpreadsheetName111/cell/A1/style/text-align/save/CENTER] id=test-Style-alignment-center-MenuItem\n" +
                "      (mdi-format-align-right) \"Right\" [/1/SpreadsheetName111/cell/A1/style/text-align/save/RIGHT] id=test-Style-alignment-right-MenuItem\n" +
                "      (mdi-format-align-justify) \"Justify\" [/1/SpreadsheetName111/cell/A1/style/text-align/save/JUSTIFY] id=test-Style-alignment-justify-MenuItem\n" +
                "    \"Vertical Alignment\" id=test-Style-verticalAlignment-SubMenu\n" +
                "      (mdi-format-align-top) \"Top\" [/1/SpreadsheetName111/cell/A1/style/vertical-align/save/TOP] id=test-Style-verticalAlignment-top-MenuItem\n" +
                "      (mdi-format-align-middle) \"Middle\" [/1/SpreadsheetName111/cell/A1/style/vertical-align/save/MIDDLE] id=test-Style-verticalAlignment-middle-MenuItem\n" +
                "      (mdi-format-align-bottom) \"Bottom\" [/1/SpreadsheetName111/cell/A1/style/vertical-align/save/BOTTOM] id=test-Style-verticalAlignment-bottom-MenuItem\n" +
                "    (mdi-palette) \"Color\" id=test-Style-color-SubMenu\n" +
                "      ColorPaletteComponent\n" +
                "        TABLE\n" +
                "          id=\"test-Style-color-Table\" className=dui dui-menu-item\n" +
                "            TBODY\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"Black\" [#/1/SpreadsheetName111/cell/A1/style/color/save/black] id=test-Style-color-color-1-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-2-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-3-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-4-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-5-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-6-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-7-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-8-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-9-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-10-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-11-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-12-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-13-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-14-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-15-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-16-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-17-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-18-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-19-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-20-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-21-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-22-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-23-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-24-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-25-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-26-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-27-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-28-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-29-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-30-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-31-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-32-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-33-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-34-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-35-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-36-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-37-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-38-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-39-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-40-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-41-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-42-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-43-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-44-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-45-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-46-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-47-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-48-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-49-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-50-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-51-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-52-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-53-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-54-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-55-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-56-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                    \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/color/save/] id=test-Style-color-color-clear-Link\n" +
                "    (mdi-palette) \"Background Color\" id=test-Style-backgroundColor-SubMenu\n" +
                "      ColorPaletteComponent\n" +
                "        TABLE\n" +
                "          id=\"test-Style-backgroundColor-Table\" className=dui dui-menu-item\n" +
                "            TBODY\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"Black\" [#/1/SpreadsheetName111/cell/A1/style/background-color/save/black] id=test-Style-backgroundColor-color-1-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-2-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-3-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-4-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-5-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-6-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-7-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-8-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-9-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-10-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-11-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-12-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-13-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-14-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-15-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-16-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-17-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-18-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-19-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-20-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-21-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-22-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-23-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-24-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-25-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-26-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-27-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-28-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-29-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-30-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-31-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-32-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-33-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-34-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-35-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-36-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-37-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-38-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-39-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-40-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-41-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-42-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-43-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-44-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-45-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-46-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-47-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-48-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-49-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-50-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-51-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-52-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-53-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-54-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-55-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-56-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                    \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/background-color/save/] id=test-Style-backgroundColor-color-clear-Link\n" +
                "    (mdi-format-bold) \"Bold\" [/1/SpreadsheetName111/cell/A1/style/font-weight/save/BOLD] id=test-Style-bold-MenuItem\n" +
                "    (mdi-format-italic) \"Italics\" [/1/SpreadsheetName111/cell/A1/style/font-style/save/ITALIC] id=test-Style-italics-MenuItem\n" +
                "    (mdi-format-strikethrough) \"Strike-thru\" [/1/SpreadsheetName111/cell/A1/style/text-decoration-line/save/LINE_THROUGH] id=test-strikeThru-MenuItem\n" +
                "    (mdi-format-underline) \"Underline\" [/1/SpreadsheetName111/cell/A1/style/text-decoration-line/save/UNDERLINE] id=test-underline-MenuItem\n" +
                "    \"Text case\" id=test-Style-textCase-SubMenu\n" +
                "      (mdi-format-letter-case-upper) \"Normal\" [/1/SpreadsheetName111/cell/A1/style/text-transform/save/] id=test-Style-textCase-normal-MenuItem\n" +
                "      (mdi-format-letter-case) \"Capitalize\" [/1/SpreadsheetName111/cell/A1/style/text-transform/save/CAPITALIZE] id=test-Style-textCase-capitalize-MenuItem\n" +
                "      (mdi-format-letter-case-lower) \"Lower case\" [/1/SpreadsheetName111/cell/A1/style/text-transform/save/LOWERCASE] id=test-Style-textCase-lower-MenuItem\n" +
                "      (mdi-format-letter-case-upper) \"Upper case\" [/1/SpreadsheetName111/cell/A1/style/text-transform/save/UPPERCASE] id=test-Style-textCase-upper-MenuItem\n" +
                "    \"Wrapping\" id=test-Style-textWrapping-SubMenu\n" +
                "      (mdi-format-text-wrapping-clip) \"Clip\" [/1/SpreadsheetName111/cell/A1/style/overflow-wrap/save/NORMAL] id=test-Style-textWrapping-clip-MenuItem\n" +
                "      (mdi-format-text-wrapping-overflow) \"Overflow\" [/1/SpreadsheetName111/cell/A1/style/overflow-x/save/VISIBLE] id=test-Style-textWrapping-overflow-MenuItem\n" +
                "      (mdi-format-text-wrapping-wrap) \"Wrap\" [/1/SpreadsheetName111/cell/A1/style/overflow-x/save/HIDDEN] id=test-Style-textWrapping-wrap-MenuItem\n" +
                "    \"Border\" id=test-Style-border-SubMenu\n" +
                "      (mdi-border-top-variant) \"Top\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Top Color\" id=test-Style-borderTopColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderTopColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"Black\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/black] id=test-Style-borderTopColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/] id=test-Style-borderTopColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderTopStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/NONE] id=test-Style-borderTopStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/HIDDEN] id=test-Style-borderTopStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/DOTTED] id=test-Style-borderTopStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/DASHED] id=test-Style-borderTopStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/SOLID] id=test-Style-borderTopStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/DOUBLE] id=test-Style-borderTopStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/GROOVE] id=test-Style-borderTopStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/RIDGE] id=test-Style-borderTopStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/INSET] id=test-Style-borderTopStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/OUTSET] id=test-Style-borderTopStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/] id=test-Style-borderTopStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderTopWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName111/cell/A1/style/border-top-width/save/0px] id=test-Style-borderTopWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName111/cell/A1/style/border-top-width/save/1px] id=test-Style-borderTopWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName111/cell/A1/style/border-top-width/save/2px] id=test-Style-borderTopWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName111/cell/A1/style/border-top-width/save/3px] id=test-Style-borderTopWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName111/cell/A1/style/border-top-width/save/4px] id=test-Style-borderTopWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName111/cell/A1/style/border-top-width/save/] id=test-Style-borderTopWidth-clear-MenuItem\n" +
                "      (mdi-border-left-variant) \"Left\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Left Color\" id=test-Style-borderLeftColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderLeftColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"Black\" [#/1/SpreadsheetName111/cell/A1/style/border-left-color/save/black] id=test-Style-borderLeftColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/border-left-color/save/] id=test-Style-borderLeftColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderLeftStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/NONE] id=test-Style-borderLeftStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/HIDDEN] id=test-Style-borderLeftStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/DOTTED] id=test-Style-borderLeftStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/DASHED] id=test-Style-borderLeftStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/SOLID] id=test-Style-borderLeftStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/DOUBLE] id=test-Style-borderLeftStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/GROOVE] id=test-Style-borderLeftStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/RIDGE] id=test-Style-borderLeftStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/INSET] id=test-Style-borderLeftStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/OUTSET] id=test-Style-borderLeftStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/] id=test-Style-borderLeftStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderLeftWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName111/cell/A1/style/border-left-width/save/0px] id=test-Style-borderLeftWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName111/cell/A1/style/border-left-width/save/1px] id=test-Style-borderLeftWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName111/cell/A1/style/border-left-width/save/2px] id=test-Style-borderLeftWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName111/cell/A1/style/border-left-width/save/3px] id=test-Style-borderLeftWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName111/cell/A1/style/border-left-width/save/4px] id=test-Style-borderLeftWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName111/cell/A1/style/border-left-width/save/] id=test-Style-borderLeftWidth-clear-MenuItem\n" +
                "      (mdi-border-right-variant) \"Right\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Right Color\" id=test-Style-borderRightColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderRightColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"Black\" [#/1/SpreadsheetName111/cell/A1/style/border-right-color/save/black] id=test-Style-borderRightColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/border-right-color/save/] id=test-Style-borderRightColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderRightStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/NONE] id=test-Style-borderRightStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/HIDDEN] id=test-Style-borderRightStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/DOTTED] id=test-Style-borderRightStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/DASHED] id=test-Style-borderRightStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/SOLID] id=test-Style-borderRightStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/DOUBLE] id=test-Style-borderRightStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/GROOVE] id=test-Style-borderRightStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/RIDGE] id=test-Style-borderRightStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/INSET] id=test-Style-borderRightStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/OUTSET] id=test-Style-borderRightStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/] id=test-Style-borderRightStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderRightWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName111/cell/A1/style/border-right-width/save/0px] id=test-Style-borderRightWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName111/cell/A1/style/border-right-width/save/1px] id=test-Style-borderRightWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName111/cell/A1/style/border-right-width/save/2px] id=test-Style-borderRightWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName111/cell/A1/style/border-right-width/save/3px] id=test-Style-borderRightWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName111/cell/A1/style/border-right-width/save/4px] id=test-Style-borderRightWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName111/cell/A1/style/border-right-width/save/] id=test-Style-borderRightWidth-clear-MenuItem\n" +
                "      (mdi-border-bottom-variant) \"Bottom\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Bottom Color\" id=test-Style-borderBottomColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderBottomColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"Black\" [#/1/SpreadsheetName111/cell/A1/style/border-bottom-color/save/black] id=test-Style-borderBottomColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/border-bottom-color/save/] id=test-Style-borderBottomColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderBottomStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/NONE] id=test-Style-borderBottomStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/HIDDEN] id=test-Style-borderBottomStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/DOTTED] id=test-Style-borderBottomStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/DASHED] id=test-Style-borderBottomStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/SOLID] id=test-Style-borderBottomStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/DOUBLE] id=test-Style-borderBottomStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/GROOVE] id=test-Style-borderBottomStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/RIDGE] id=test-Style-borderBottomStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/INSET] id=test-Style-borderBottomStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/OUTSET] id=test-Style-borderBottomStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/] id=test-Style-borderBottomStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderBottomWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-width/save/0px] id=test-Style-borderBottomWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-width/save/1px] id=test-Style-borderBottomWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-width/save/2px] id=test-Style-borderBottomWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-width/save/3px] id=test-Style-borderBottomWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-width/save/4px] id=test-Style-borderBottomWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-width/save/] id=test-Style-borderBottomWidth-clear-MenuItem\n" +
                "      (mdi-border-all-variant) \"All\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Color\" id=test-Style-borderColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"Black\" [#/1/SpreadsheetName111/cell/A1/style/border-color/save/black] id=test-Style-borderColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/border-color/save/] id=test-Style-borderColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderAllStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/NONE] id=test-Style-borderAllStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/HIDDEN] id=test-Style-borderAllStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/DOTTED] id=test-Style-borderAllStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/DASHED] id=test-Style-borderAllStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/SOLID] id=test-Style-borderAllStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/DOUBLE] id=test-Style-borderAllStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/GROOVE] id=test-Style-borderAllStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/RIDGE] id=test-Style-borderAllStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/INSET] id=test-Style-borderAllStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/OUTSET] id=test-Style-borderAllStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/] id=test-Style-borderAllStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderAllWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName111/cell/A1/style/border-width/save/0px] id=test-Style-borderAllWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName111/cell/A1/style/border-width/save/1px] id=test-Style-borderAllWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName111/cell/A1/style/border-width/save/2px] id=test-Style-borderAllWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName111/cell/A1/style/border-width/save/3px] id=test-Style-borderAllWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName111/cell/A1/style/border-width/save/4px] id=test-Style-borderAllWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName111/cell/A1/style/border-width/save/] id=test-Style-borderAllWidth-clear-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear...\" [/1/SpreadsheetName111/cell/A1/style/*/save/] id=test-Style-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName111/cell/A1/style] id=test-Style-edit-MenuItem\n"
        );
    }

    @Test
    public void testBuildWithRecents() {
        this.buildAndCheck(
            HistoryToken.cellStyle(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL,
                Optional.empty()
            ),
            Optional.empty(), // summary
            Lists.of(
                TextStyleProperty.with(
                    TextStylePropertyName.COLOR,
                    Optional.of(
                        Color.BLACK
                    )
                )
            ), // recent,
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"Style\" id=test-Style-SubMenu\n" +
                "    \"Alignment\" id=test-Style-alignment-SubMenu\n" +
                "      (mdi-format-align-left) \"Left\" [/1/SpreadsheetName111/cell/A1/style/text-align/save/LEFT] id=test-Style-alignment-left-MenuItem\n" +
                "      (mdi-format-align-center) \"Center\" [/1/SpreadsheetName111/cell/A1/style/text-align/save/CENTER] id=test-Style-alignment-center-MenuItem\n" +
                "      (mdi-format-align-right) \"Right\" [/1/SpreadsheetName111/cell/A1/style/text-align/save/RIGHT] id=test-Style-alignment-right-MenuItem\n" +
                "      (mdi-format-align-justify) \"Justify\" [/1/SpreadsheetName111/cell/A1/style/text-align/save/JUSTIFY] id=test-Style-alignment-justify-MenuItem\n" +
                "    \"Vertical Alignment\" id=test-Style-verticalAlignment-SubMenu\n" +
                "      (mdi-format-align-top) \"Top\" [/1/SpreadsheetName111/cell/A1/style/vertical-align/save/TOP] id=test-Style-verticalAlignment-top-MenuItem\n" +
                "      (mdi-format-align-middle) \"Middle\" [/1/SpreadsheetName111/cell/A1/style/vertical-align/save/MIDDLE] id=test-Style-verticalAlignment-middle-MenuItem\n" +
                "      (mdi-format-align-bottom) \"Bottom\" [/1/SpreadsheetName111/cell/A1/style/vertical-align/save/BOTTOM] id=test-Style-verticalAlignment-bottom-MenuItem\n" +
                "    (mdi-palette) \"Color\" id=test-Style-color-SubMenu\n" +
                "      ColorPaletteComponent\n" +
                "        TABLE\n" +
                "          id=\"test-Style-color-Table\" className=dui dui-menu-item\n" +
                "            TBODY\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"Black\" [#/1/SpreadsheetName111/cell/A1/style/color/save/black] id=test-Style-color-color-1-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-2-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-3-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-4-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-5-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-6-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-7-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-8-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-9-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-10-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-11-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-12-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-13-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-14-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-15-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-16-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-17-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-18-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-19-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-20-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-21-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-22-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-23-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-24-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-25-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-26-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-27-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-28-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-29-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-30-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-31-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-32-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-33-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-34-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-35-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-36-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-37-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-38-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-39-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-40-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-41-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-42-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-43-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-44-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-45-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-46-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-47-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-48-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-49-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-50-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-51-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-52-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-53-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-54-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-55-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-56-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                    \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/color/save/] id=test-Style-color-color-clear-Link\n" +
                "    (mdi-palette) \"Background Color\" id=test-Style-backgroundColor-SubMenu\n" +
                "      ColorPaletteComponent\n" +
                "        TABLE\n" +
                "          id=\"test-Style-backgroundColor-Table\" className=dui dui-menu-item\n" +
                "            TBODY\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"Black\" [#/1/SpreadsheetName111/cell/A1/style/background-color/save/black] id=test-Style-backgroundColor-color-1-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-2-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-3-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-4-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-5-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-6-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-7-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-8-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-9-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-10-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-11-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-12-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-13-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-14-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-15-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-16-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-17-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-18-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-19-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-20-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-21-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-22-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-23-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-24-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-25-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-26-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-27-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-28-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-29-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-30-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-31-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-32-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-33-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-34-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-35-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-36-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-37-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-38-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-39-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-40-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-41-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-42-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-43-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-44-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-45-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-46-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-47-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-48-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-49-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-50-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-51-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-52-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-53-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-54-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-55-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-56-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                    \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/background-color/save/] id=test-Style-backgroundColor-color-clear-Link\n" +
                "    (mdi-format-bold) \"Bold\" [/1/SpreadsheetName111/cell/A1/style/font-weight/save/BOLD] id=test-Style-bold-MenuItem\n" +
                "    (mdi-format-italic) \"Italics\" [/1/SpreadsheetName111/cell/A1/style/font-style/save/ITALIC] id=test-Style-italics-MenuItem\n" +
                "    (mdi-format-strikethrough) \"Strike-thru\" [/1/SpreadsheetName111/cell/A1/style/text-decoration-line/save/LINE_THROUGH] id=test-strikeThru-MenuItem\n" +
                "    (mdi-format-underline) \"Underline\" [/1/SpreadsheetName111/cell/A1/style/text-decoration-line/save/UNDERLINE] id=test-underline-MenuItem\n" +
                "    \"Text case\" id=test-Style-textCase-SubMenu\n" +
                "      (mdi-format-letter-case-upper) \"Normal\" [/1/SpreadsheetName111/cell/A1/style/text-transform/save/] id=test-Style-textCase-normal-MenuItem\n" +
                "      (mdi-format-letter-case) \"Capitalize\" [/1/SpreadsheetName111/cell/A1/style/text-transform/save/CAPITALIZE] id=test-Style-textCase-capitalize-MenuItem\n" +
                "      (mdi-format-letter-case-lower) \"Lower case\" [/1/SpreadsheetName111/cell/A1/style/text-transform/save/LOWERCASE] id=test-Style-textCase-lower-MenuItem\n" +
                "      (mdi-format-letter-case-upper) \"Upper case\" [/1/SpreadsheetName111/cell/A1/style/text-transform/save/UPPERCASE] id=test-Style-textCase-upper-MenuItem\n" +
                "    \"Wrapping\" id=test-Style-textWrapping-SubMenu\n" +
                "      (mdi-format-text-wrapping-clip) \"Clip\" [/1/SpreadsheetName111/cell/A1/style/overflow-wrap/save/NORMAL] id=test-Style-textWrapping-clip-MenuItem\n" +
                "      (mdi-format-text-wrapping-overflow) \"Overflow\" [/1/SpreadsheetName111/cell/A1/style/overflow-x/save/VISIBLE] id=test-Style-textWrapping-overflow-MenuItem\n" +
                "      (mdi-format-text-wrapping-wrap) \"Wrap\" [/1/SpreadsheetName111/cell/A1/style/overflow-x/save/HIDDEN] id=test-Style-textWrapping-wrap-MenuItem\n" +
                "    \"Border\" id=test-Style-border-SubMenu\n" +
                "      (mdi-border-top-variant) \"Top\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Top Color\" id=test-Style-borderTopColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderTopColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"Black\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/black] id=test-Style-borderTopColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/] id=test-Style-borderTopColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderTopStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/NONE] id=test-Style-borderTopStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/HIDDEN] id=test-Style-borderTopStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/DOTTED] id=test-Style-borderTopStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/DASHED] id=test-Style-borderTopStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/SOLID] id=test-Style-borderTopStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/DOUBLE] id=test-Style-borderTopStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/GROOVE] id=test-Style-borderTopStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/RIDGE] id=test-Style-borderTopStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/INSET] id=test-Style-borderTopStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/OUTSET] id=test-Style-borderTopStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName111/cell/A1/style/border-top-style/save/] id=test-Style-borderTopStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderTopWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName111/cell/A1/style/border-top-width/save/0px] id=test-Style-borderTopWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName111/cell/A1/style/border-top-width/save/1px] id=test-Style-borderTopWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName111/cell/A1/style/border-top-width/save/2px] id=test-Style-borderTopWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName111/cell/A1/style/border-top-width/save/3px] id=test-Style-borderTopWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName111/cell/A1/style/border-top-width/save/4px] id=test-Style-borderTopWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName111/cell/A1/style/border-top-width/save/] id=test-Style-borderTopWidth-clear-MenuItem\n" +
                "      (mdi-border-left-variant) \"Left\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Left Color\" id=test-Style-borderLeftColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderLeftColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"Black\" [#/1/SpreadsheetName111/cell/A1/style/border-left-color/save/black] id=test-Style-borderLeftColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/border-left-color/save/] id=test-Style-borderLeftColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderLeftStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/NONE] id=test-Style-borderLeftStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/HIDDEN] id=test-Style-borderLeftStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/DOTTED] id=test-Style-borderLeftStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/DASHED] id=test-Style-borderLeftStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/SOLID] id=test-Style-borderLeftStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/DOUBLE] id=test-Style-borderLeftStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/GROOVE] id=test-Style-borderLeftStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/RIDGE] id=test-Style-borderLeftStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/INSET] id=test-Style-borderLeftStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/OUTSET] id=test-Style-borderLeftStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName111/cell/A1/style/border-left-style/save/] id=test-Style-borderLeftStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderLeftWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName111/cell/A1/style/border-left-width/save/0px] id=test-Style-borderLeftWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName111/cell/A1/style/border-left-width/save/1px] id=test-Style-borderLeftWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName111/cell/A1/style/border-left-width/save/2px] id=test-Style-borderLeftWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName111/cell/A1/style/border-left-width/save/3px] id=test-Style-borderLeftWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName111/cell/A1/style/border-left-width/save/4px] id=test-Style-borderLeftWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName111/cell/A1/style/border-left-width/save/] id=test-Style-borderLeftWidth-clear-MenuItem\n" +
                "      (mdi-border-right-variant) \"Right\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Right Color\" id=test-Style-borderRightColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderRightColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"Black\" [#/1/SpreadsheetName111/cell/A1/style/border-right-color/save/black] id=test-Style-borderRightColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/border-right-color/save/] id=test-Style-borderRightColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderRightStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/NONE] id=test-Style-borderRightStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/HIDDEN] id=test-Style-borderRightStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/DOTTED] id=test-Style-borderRightStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/DASHED] id=test-Style-borderRightStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/SOLID] id=test-Style-borderRightStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/DOUBLE] id=test-Style-borderRightStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/GROOVE] id=test-Style-borderRightStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/RIDGE] id=test-Style-borderRightStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/INSET] id=test-Style-borderRightStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/OUTSET] id=test-Style-borderRightStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName111/cell/A1/style/border-right-style/save/] id=test-Style-borderRightStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderRightWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName111/cell/A1/style/border-right-width/save/0px] id=test-Style-borderRightWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName111/cell/A1/style/border-right-width/save/1px] id=test-Style-borderRightWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName111/cell/A1/style/border-right-width/save/2px] id=test-Style-borderRightWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName111/cell/A1/style/border-right-width/save/3px] id=test-Style-borderRightWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName111/cell/A1/style/border-right-width/save/4px] id=test-Style-borderRightWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName111/cell/A1/style/border-right-width/save/] id=test-Style-borderRightWidth-clear-MenuItem\n" +
                "      (mdi-border-bottom-variant) \"Bottom\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Bottom Color\" id=test-Style-borderBottomColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderBottomColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"Black\" [#/1/SpreadsheetName111/cell/A1/style/border-bottom-color/save/black] id=test-Style-borderBottomColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/border-bottom-color/save/] id=test-Style-borderBottomColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderBottomStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/NONE] id=test-Style-borderBottomStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/HIDDEN] id=test-Style-borderBottomStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/DOTTED] id=test-Style-borderBottomStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/DASHED] id=test-Style-borderBottomStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/SOLID] id=test-Style-borderBottomStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/DOUBLE] id=test-Style-borderBottomStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/GROOVE] id=test-Style-borderBottomStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/RIDGE] id=test-Style-borderBottomStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/INSET] id=test-Style-borderBottomStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/OUTSET] id=test-Style-borderBottomStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-style/save/] id=test-Style-borderBottomStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderBottomWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-width/save/0px] id=test-Style-borderBottomWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-width/save/1px] id=test-Style-borderBottomWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-width/save/2px] id=test-Style-borderBottomWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-width/save/3px] id=test-Style-borderBottomWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-width/save/4px] id=test-Style-borderBottomWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName111/cell/A1/style/border-bottom-width/save/] id=test-Style-borderBottomWidth-clear-MenuItem\n" +
                "      (mdi-border-all-variant) \"All\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Color\" id=test-Style-borderColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"Black\" [#/1/SpreadsheetName111/cell/A1/style/border-color/save/black] id=test-Style-borderColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/border-color/save/] id=test-Style-borderColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderAllStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/NONE] id=test-Style-borderAllStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/HIDDEN] id=test-Style-borderAllStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/DOTTED] id=test-Style-borderAllStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/DASHED] id=test-Style-borderAllStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/SOLID] id=test-Style-borderAllStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/DOUBLE] id=test-Style-borderAllStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/GROOVE] id=test-Style-borderAllStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/RIDGE] id=test-Style-borderAllStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/INSET] id=test-Style-borderAllStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/OUTSET] id=test-Style-borderAllStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName111/cell/A1/style/border-style/save/] id=test-Style-borderAllStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderAllWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName111/cell/A1/style/border-width/save/0px] id=test-Style-borderAllWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName111/cell/A1/style/border-width/save/1px] id=test-Style-borderAllWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName111/cell/A1/style/border-width/save/2px] id=test-Style-borderAllWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName111/cell/A1/style/border-width/save/3px] id=test-Style-borderAllWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName111/cell/A1/style/border-width/save/4px] id=test-Style-borderAllWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName111/cell/A1/style/border-width/save/] id=test-Style-borderAllWidth-clear-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear...\" [/1/SpreadsheetName111/cell/A1/style/*/save/] id=test-Style-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName111/cell/A1/style] id=test-Style-edit-MenuItem\n" +
                "    -----\n" +
                "    \"Color\" [/1/SpreadsheetName111/cell/A1/style/color/save/black] id=test-Style-recent-0-MenuItem\n"
        );
    }

    private void buildAndCheck(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                               final Optional<SpreadsheetCell> summary,
                               final List<TextStyleProperty<?>> recentTextStyleProperties,
                               final String... expected) {
        final SpreadsheetSelectionMenuContext context = this.context(
            historyToken,
            summary,
            recentTextStyleProperties
        );

        final SpreadsheetContextMenu menu = SpreadsheetContextMenuFactory.with(
            Menu.create(
                "Cell-MenuId",
                "Cell A1 Menu",
                Optional.empty(), // no icon
                Optional.empty() // no badge
            ),
            context
        );

        SpreadsheetSelectionMenuValuesStyle.with(
            historyToken,
            menu,
            context
        ).build();

        this.treePrintAndCheck(
            menu,
            Arrays.stream(expected)
                .collect(Collectors.joining())
        );
    }

    private SpreadsheetSelectionMenuContext context(final HistoryToken historyToken,
                                                    final Optional<SpreadsheetCell> summary,
                                                    final List<TextStyleProperty<?>> recentTextStyleProperties) {
        return new FakeSpreadsheetSelectionMenuContext() {

            @Override
            public HistoryToken historyToken() {
                return historyToken;
            }

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return () -> {};
            }

            @Override
            public String idPrefix() {
                return "test-";
            }

            @Override
            public Optional<String> localeText(final Locale locale) {
                return Optional.of(
                    locale.getDisplayName()
                );
            }

            @Override
            public List<TextStyleProperty<?>> recentTextStyleProperties() {
                return recentTextStyleProperties;
            }

            @Override
            public Optional<SpreadsheetCell> selectionSummary() {
                return summary;
            }

            @Override
            public SpreadsheetMetadata spreadsheetMetadata() {
                return SpreadsheetMetadata.EMPTY.set(
                    SpreadsheetMetadataPropertyName.LOCALE,
                    LOCALE
                ).loadFromLocale(CURRENCY_LOCALE_CONTEXT)
                    .set(
                        SpreadsheetMetadataPropertyName.namedColor(SpreadsheetColorName.BLACK),
                        1
                    ).set(
                        SpreadsheetMetadataPropertyName.numberedColor(1),
                        Color.BLACK
                    );
            }
        };
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetSelectionMenuValuesStyle> type() {
        return SpreadsheetSelectionMenuValuesStyle.class;
    }
}
