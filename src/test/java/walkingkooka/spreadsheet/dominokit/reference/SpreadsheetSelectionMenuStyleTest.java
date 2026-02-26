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

package walkingkooka.spreadsheet.dominokit.reference;

import org.dominokit.domino.ui.menu.Menu;
import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.color.Color;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuFactory;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStyleProperty;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public final class SpreadsheetSelectionMenuStyleTest implements TreePrintableTesting,
    SpreadsheetMetadataTesting,
    ClassTesting<SpreadsheetSelectionMenuStyle> {

    @Test
    public void testBuild() {
        this.buildAndCheck(
            HistoryToken.selection(
                SpreadsheetId.with(1),
                SpreadsheetName.with("Spreadsheet123"),
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            Optional.empty(), // summary
            Lists.empty(),
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"Alignment\" id=test-alignment-SubMenu\n" +
                "    (mdi-format-align-left) \"Left\" [/1/Spreadsheet123/cell/A1/style/text-align/save/LEFT] id=test-left-MenuItem\n" +
                "    (mdi-format-align-center) \"Center\" [/1/Spreadsheet123/cell/A1/style/text-align/save/CENTER] id=test-center-MenuItem\n" +
                "    (mdi-format-align-right) \"Right\" [/1/Spreadsheet123/cell/A1/style/text-align/save/RIGHT] id=test-right-MenuItem\n" +
                "    (mdi-format-align-justify) \"Justify\" [/1/Spreadsheet123/cell/A1/style/text-align/save/JUSTIFY] id=test-justify-MenuItem\n" +
                "  \"Vertical Alignment\" id=test-vertical-alignment-SubMenu\n" +
                "    (mdi-format-align-top) \"Top\" [/1/Spreadsheet123/cell/A1/style/vertical-align/save/TOP] id=test-top-MenuItem\n" +
                "    (mdi-format-align-middle) \"Middle\" [/1/Spreadsheet123/cell/A1/style/vertical-align/save/MIDDLE] id=test-middle-MenuItem\n" +
                "    (mdi-format-align-bottom) \"Bottom\" [/1/Spreadsheet123/cell/A1/style/vertical-align/save/BOTTOM] id=test-bottom-MenuItem\n" +
                "  (mdi-palette) \"Color\" id=test-color-SubMenu\n" +
                "    ColorComponent\n" +
                "      TABLE\n" +
                "        id=\"test-color-Table\" className=dui dui-menu-item\n" +
                "          TBODY\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-1-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-2-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-3-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-4-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-5-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-6-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-7-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-8-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-9-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-10-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-11-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-12-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-13-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-14-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-15-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-16-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-17-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-18-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-19-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-20-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-21-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-22-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-23-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-24-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-25-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-26-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-27-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-28-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-29-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-30-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-31-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-32-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-33-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-34-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-35-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-36-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-37-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-38-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-39-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-40-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-41-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-42-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-43-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-44-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-45-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-46-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-47-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-48-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-49-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-50-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-51-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-52-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-53-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-54-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-55-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-56-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                  \"Clear\" [#/1/Spreadsheet123/cell/A1/style/color/save/] id=test-color-color-clear-Link\n" +
                "  (mdi-palette) \"Background Color\" id=test-background-color-SubMenu\n" +
                "    ColorComponent\n" +
                "      TABLE\n" +
                "        id=\"test-background-color-Table\" className=dui dui-menu-item\n" +
                "          TBODY\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-1-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-2-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-3-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-4-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-5-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-6-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-7-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-8-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-9-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-10-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-11-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-12-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-13-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-14-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-15-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-16-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-17-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-18-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-19-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-20-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-21-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-22-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-23-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-24-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-25-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-26-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-27-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-28-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-29-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-30-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-31-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-32-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-33-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-34-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-35-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-36-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-37-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-38-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-39-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-40-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-41-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-42-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-43-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-44-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-45-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-46-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-47-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-48-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-49-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-50-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-51-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-52-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-53-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-54-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-55-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-56-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                  \"Clear\" [#/1/Spreadsheet123/cell/A1/style/background-color/save/] id=test-background-color-color-clear-Link\n" +
                "  (mdi-format-bold) \"Bold\" [/1/Spreadsheet123/cell/A1/style/font-weight/save/bold] id=test-bold-MenuItem\n" +
                "  (mdi-format-italic) \"Italics\" [/1/Spreadsheet123/cell/A1/style/font-style/save/ITALIC] id=test-italics-MenuItem\n" +
                "  (mdi-format-strikethrough) \"Strike-thru\" [/1/Spreadsheet123/cell/A1/style/text-decoration-line/save/LINE_THROUGH] id=test-strike-thru-MenuItem\n" +
                "  (mdi-format-underline) \"Underline\" [/1/Spreadsheet123/cell/A1/style/text-decoration-line/save/UNDERLINE] id=test-underline-MenuItem\n" +
                "  \"Text case\" id=test-text-case-SubMenu\n" +
                "    (mdi-format-letter-case-upper) \"Normal\" [/1/Spreadsheet123/cell/A1/style/text-transform/save/] id=test-normal-MenuItem\n" +
                "    (mdi-format-letter-case) \"Capitalize\" [/1/Spreadsheet123/cell/A1/style/text-transform/save/CAPITALIZE] id=test-capitalize-MenuItem\n" +
                "    (mdi-format-letter-case-lower) \"Lower case\" [/1/Spreadsheet123/cell/A1/style/text-transform/save/LOWERCASE] id=test-lower-MenuItem\n" +
                "    (mdi-format-letter-case-upper) \"Upper case\" [/1/Spreadsheet123/cell/A1/style/text-transform/save/UPPERCASE] id=test-upper-MenuItem\n" +
                "  \"Wrapping\" id=test-text-wrapping-SubMenu\n" +
                "    (mdi-format-text-wrapping-clip) \"Clip\" [/1/Spreadsheet123/cell/A1/style/overflow-wrap/save/NORMAL] id=test-clip-MenuItem\n" +
                "    (mdi-format-text-wrapping-overflow) \"Overflow\" [/1/Spreadsheet123/cell/A1/style/overflow-x/save/VISIBLE] id=test-overflow-MenuItem\n" +
                "    (mdi-format-text-wrapping-wrap) \"Wrap\" [/1/Spreadsheet123/cell/A1/style/overflow-x/save/HIDDEN] id=test-wrap-MenuItem\n" +
                "  \"Border\" id=test-border-SubMenu\n" +
                "    (mdi-border-top-variant) \"Top\" id=test-border-top-SubMenu\n" +
                "      (mdi-palette) \"Border Top Color\" id=test-border-top-color-SubMenu\n" +
                "        ColorComponent\n" +
                "          TABLE\n" +
                "            id=\"test-border-top-color-Table\" className=dui dui-menu-item\n" +
                "              TBODY\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-1-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-2-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-3-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-4-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-5-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-6-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-7-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-8-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-9-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-10-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-11-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-12-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-13-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-14-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-15-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-16-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-17-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-18-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-19-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-20-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-21-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-22-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-23-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-24-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-25-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-26-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-27-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-28-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-29-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-30-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-31-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-32-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-33-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-34-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-35-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-36-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-37-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-38-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-39-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-40-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-41-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-42-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-43-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-44-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-45-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-46-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-47-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-48-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-49-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-50-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-51-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-52-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-53-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-54-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-55-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-56-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                      \"Clear\" [#/1/Spreadsheet123/cell/A1/style/border-top-color/save/] id=test-border-top-color-color-clear-Link\n" +
                "      \"Style\" id=test-border-top-style-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-top-style/save/NONE] id=test-border-top-style-none-MenuItem\n" +
                "        \"Dashed\" [/1/Spreadsheet123/cell/A1/style/border-top-style/save/DASHED] id=test-border-top-style-dashed-MenuItem\n" +
                "        \"Dotted\" [/1/Spreadsheet123/cell/A1/style/border-top-style/save/DOTTED] id=test-border-top-style-dotted-MenuItem\n" +
                "        \"Solid\" [/1/Spreadsheet123/cell/A1/style/border-top-style/save/SOLID] id=test-border-top-style-solid-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-top-style/save/] id=test-border-top-style-clear-MenuItem\n" +
                "      \"Width\" id=test-border-top-width-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-top-width/save/0px] id=test-border-top-width-0-MenuItem\n" +
                "        \"1\" [/1/Spreadsheet123/cell/A1/style/border-top-width/save/1px] id=test-border-top-width-1-MenuItem\n" +
                "        \"2\" [/1/Spreadsheet123/cell/A1/style/border-top-width/save/2px] id=test-border-top-width-2-MenuItem\n" +
                "        \"3\" [/1/Spreadsheet123/cell/A1/style/border-top-width/save/3px] id=test-border-top-width-3-MenuItem\n" +
                "        \"4\" [/1/Spreadsheet123/cell/A1/style/border-top-width/save/4px] id=test-border-top-width-4-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-top-width/save/] id=test-border-top-width-clear-MenuItem\n" +
                "    (mdi-border-left-variant) \"Left\" id=test-border-left-SubMenu\n" +
                "      (mdi-palette) \"Border Left Color\" id=test-border-left-color-SubMenu\n" +
                "        ColorComponent\n" +
                "          TABLE\n" +
                "            id=\"test-border-left-color-Table\" className=dui dui-menu-item\n" +
                "              TBODY\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-1-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-2-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-3-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-4-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-5-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-6-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-7-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-8-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-9-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-10-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-11-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-12-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-13-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-14-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-15-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-16-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-17-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-18-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-19-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-20-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-21-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-22-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-23-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-24-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-25-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-26-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-27-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-28-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-29-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-30-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-31-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-32-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-33-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-34-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-35-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-36-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-37-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-38-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-39-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-40-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-41-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-42-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-43-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-44-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-45-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-46-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-47-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-48-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-49-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-50-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-51-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-52-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-53-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-54-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-55-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-56-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                      \"Clear\" [#/1/Spreadsheet123/cell/A1/style/border-left-color/save/] id=test-border-left-color-color-clear-Link\n" +
                "      \"Style\" id=test-border-left-style-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-left-style/save/NONE] id=test-border-left-style-none-MenuItem\n" +
                "        \"Dashed\" [/1/Spreadsheet123/cell/A1/style/border-left-style/save/DASHED] id=test-border-left-style-dashed-MenuItem\n" +
                "        \"Dotted\" [/1/Spreadsheet123/cell/A1/style/border-left-style/save/DOTTED] id=test-border-left-style-dotted-MenuItem\n" +
                "        \"Solid\" [/1/Spreadsheet123/cell/A1/style/border-left-style/save/SOLID] id=test-border-left-style-solid-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-left-style/save/] id=test-border-left-style-clear-MenuItem\n" +
                "      \"Width\" id=test-border-left-width-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-left-width/save/0px] id=test-border-left-width-0-MenuItem\n" +
                "        \"1\" [/1/Spreadsheet123/cell/A1/style/border-left-width/save/1px] id=test-border-left-width-1-MenuItem\n" +
                "        \"2\" [/1/Spreadsheet123/cell/A1/style/border-left-width/save/2px] id=test-border-left-width-2-MenuItem\n" +
                "        \"3\" [/1/Spreadsheet123/cell/A1/style/border-left-width/save/3px] id=test-border-left-width-3-MenuItem\n" +
                "        \"4\" [/1/Spreadsheet123/cell/A1/style/border-left-width/save/4px] id=test-border-left-width-4-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-left-width/save/] id=test-border-left-width-clear-MenuItem\n" +
                "    (mdi-border-right-variant) \"Right\" id=test-border-right-SubMenu\n" +
                "      (mdi-palette) \"Border Right Color\" id=test-border-right-color-SubMenu\n" +
                "        ColorComponent\n" +
                "          TABLE\n" +
                "            id=\"test-border-right-color-Table\" className=dui dui-menu-item\n" +
                "              TBODY\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-1-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-2-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-3-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-4-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-5-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-6-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-7-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-8-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-9-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-10-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-11-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-12-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-13-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-14-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-15-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-16-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-17-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-18-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-19-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-20-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-21-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-22-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-23-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-24-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-25-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-26-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-27-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-28-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-29-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-30-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-31-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-32-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-33-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-34-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-35-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-36-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-37-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-38-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-39-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-40-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-41-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-42-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-43-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-44-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-45-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-46-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-47-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-48-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-49-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-50-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-51-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-52-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-53-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-54-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-55-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-56-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                      \"Clear\" [#/1/Spreadsheet123/cell/A1/style/border-right-color/save/] id=test-border-right-color-color-clear-Link\n" +
                "      \"Style\" id=test-border-right-style-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-right-style/save/NONE] id=test-border-right-style-none-MenuItem\n" +
                "        \"Dashed\" [/1/Spreadsheet123/cell/A1/style/border-right-style/save/DASHED] id=test-border-right-style-dashed-MenuItem\n" +
                "        \"Dotted\" [/1/Spreadsheet123/cell/A1/style/border-right-style/save/DOTTED] id=test-border-right-style-dotted-MenuItem\n" +
                "        \"Solid\" [/1/Spreadsheet123/cell/A1/style/border-right-style/save/SOLID] id=test-border-right-style-solid-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-right-style/save/] id=test-border-right-style-clear-MenuItem\n" +
                "      \"Width\" id=test-border-right-width-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-right-width/save/0px] id=test-border-right-width-0-MenuItem\n" +
                "        \"1\" [/1/Spreadsheet123/cell/A1/style/border-right-width/save/1px] id=test-border-right-width-1-MenuItem\n" +
                "        \"2\" [/1/Spreadsheet123/cell/A1/style/border-right-width/save/2px] id=test-border-right-width-2-MenuItem\n" +
                "        \"3\" [/1/Spreadsheet123/cell/A1/style/border-right-width/save/3px] id=test-border-right-width-3-MenuItem\n" +
                "        \"4\" [/1/Spreadsheet123/cell/A1/style/border-right-width/save/4px] id=test-border-right-width-4-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-right-width/save/] id=test-border-right-width-clear-MenuItem\n" +
                "    (mdi-border-bottom-variant) \"Bottom\" id=test-border-bottom-SubMenu\n" +
                "      (mdi-palette) \"Border Bottom Color\" id=test-border-bottom-color-SubMenu\n" +
                "        ColorComponent\n" +
                "          TABLE\n" +
                "            id=\"test-border-bottom-color-Table\" className=dui dui-menu-item\n" +
                "              TBODY\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-1-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-2-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-3-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-4-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-5-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-6-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-7-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-8-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-9-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-10-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-11-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-12-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-13-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-14-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-15-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-16-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-17-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-18-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-19-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-20-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-21-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-22-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-23-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-24-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-25-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-26-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-27-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-28-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-29-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-30-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-31-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-32-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-33-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-34-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-35-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-36-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-37-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-38-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-39-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-40-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-41-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-42-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-43-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-44-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-45-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-46-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-47-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-48-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-49-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-50-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-51-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-52-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-53-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-54-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-55-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-56-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                      \"Clear\" [#/1/Spreadsheet123/cell/A1/style/border-bottom-color/save/] id=test-border-bottom-color-color-clear-Link\n" +
                "      \"Style\" id=test-border-bottom-style-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-bottom-style/save/NONE] id=test-border-bottom-style-none-MenuItem\n" +
                "        \"Dashed\" [/1/Spreadsheet123/cell/A1/style/border-bottom-style/save/DASHED] id=test-border-bottom-style-dashed-MenuItem\n" +
                "        \"Dotted\" [/1/Spreadsheet123/cell/A1/style/border-bottom-style/save/DOTTED] id=test-border-bottom-style-dotted-MenuItem\n" +
                "        \"Solid\" [/1/Spreadsheet123/cell/A1/style/border-bottom-style/save/SOLID] id=test-border-bottom-style-solid-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-bottom-style/save/] id=test-border-bottom-style-clear-MenuItem\n" +
                "      \"Width\" id=test-border-bottom-width-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-bottom-width/save/0px] id=test-border-bottom-width-0-MenuItem\n" +
                "        \"1\" [/1/Spreadsheet123/cell/A1/style/border-bottom-width/save/1px] id=test-border-bottom-width-1-MenuItem\n" +
                "        \"2\" [/1/Spreadsheet123/cell/A1/style/border-bottom-width/save/2px] id=test-border-bottom-width-2-MenuItem\n" +
                "        \"3\" [/1/Spreadsheet123/cell/A1/style/border-bottom-width/save/3px] id=test-border-bottom-width-3-MenuItem\n" +
                "        \"4\" [/1/Spreadsheet123/cell/A1/style/border-bottom-width/save/4px] id=test-border-bottom-width-4-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-bottom-width/save/] id=test-border-bottom-width-clear-MenuItem\n" +
                "    (mdi-border-all-variant) \"All\" id=test-border-all-SubMenu\n" +
                "      (mdi-palette) \"Border Color\" id=test-border-color-SubMenu\n" +
                "        ColorComponent\n" +
                "          TABLE\n" +
                "            id=\"test-border-color-Table\" className=dui dui-menu-item\n" +
                "              TBODY\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-1-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-2-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-3-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-4-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-5-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-6-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-7-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-8-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-9-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-10-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-11-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-12-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-13-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-14-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-15-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-16-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-17-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-18-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-19-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-20-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-21-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-22-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-23-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-24-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-25-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-26-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-27-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-28-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-29-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-30-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-31-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-32-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-33-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-34-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-35-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-36-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-37-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-38-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-39-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-40-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-41-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-42-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-43-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-44-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-45-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-46-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-47-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-48-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-49-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-50-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-51-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-52-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-53-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-54-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-55-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-56-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                      \"Clear\" [#/1/Spreadsheet123/cell/A1/style/border-color/save/] id=test-border-color-color-clear-Link\n" +
                "      \"Style\" id=test-border-all-style-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-style/save/NONE] id=test-border-all-style-none-MenuItem\n" +
                "        \"Dashed\" [/1/Spreadsheet123/cell/A1/style/border-style/save/DASHED] id=test-border-all-style-dashed-MenuItem\n" +
                "        \"Dotted\" [/1/Spreadsheet123/cell/A1/style/border-style/save/DOTTED] id=test-border-all-style-dotted-MenuItem\n" +
                "        \"Solid\" [/1/Spreadsheet123/cell/A1/style/border-style/save/SOLID] id=test-border-all-style-solid-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-style/save/] id=test-border-all-style-clear-MenuItem\n" +
                "      \"Width\" id=test-border-all-width-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-width/save/0px] id=test-border-all-width-0-MenuItem\n" +
                "        \"1\" [/1/Spreadsheet123/cell/A1/style/border-width/save/1px] id=test-border-all-width-1-MenuItem\n" +
                "        \"2\" [/1/Spreadsheet123/cell/A1/style/border-width/save/2px] id=test-border-all-width-2-MenuItem\n" +
                "        \"3\" [/1/Spreadsheet123/cell/A1/style/border-width/save/3px] id=test-border-all-width-3-MenuItem\n" +
                "        \"4\" [/1/Spreadsheet123/cell/A1/style/border-width/save/4px] id=test-border-all-width-4-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-width/save/] id=test-border-all-width-clear-MenuItem\n" +
                "  -----\n" +
                "  (mdi-format-clear) \"Clear style\" [/1/Spreadsheet123/cell/A1/style/*/save/] id=test-clear-style-MenuItem\n"
        );
    }

    @Test
    public void testBuildWithSpreadsheetCellFormulaHistoryToken() {
        this.buildAndCheck(
            HistoryToken.cellFormula(
                SpreadsheetId.with(1),
                SpreadsheetName.with("Spreadsheet123"),
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            Optional.empty(), // summary
            Lists.empty(), // recents
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"Alignment\" id=test-alignment-SubMenu\n" +
                "    (mdi-format-align-left) \"Left\" [/1/Spreadsheet123/cell/A1/style/text-align/save/LEFT] id=test-left-MenuItem\n" +
                "    (mdi-format-align-center) \"Center\" [/1/Spreadsheet123/cell/A1/style/text-align/save/CENTER] id=test-center-MenuItem\n" +
                "    (mdi-format-align-right) \"Right\" [/1/Spreadsheet123/cell/A1/style/text-align/save/RIGHT] id=test-right-MenuItem\n" +
                "    (mdi-format-align-justify) \"Justify\" [/1/Spreadsheet123/cell/A1/style/text-align/save/JUSTIFY] id=test-justify-MenuItem\n" +
                "  \"Vertical Alignment\" id=test-vertical-alignment-SubMenu\n" +
                "    (mdi-format-align-top) \"Top\" [/1/Spreadsheet123/cell/A1/style/vertical-align/save/TOP] id=test-top-MenuItem\n" +
                "    (mdi-format-align-middle) \"Middle\" [/1/Spreadsheet123/cell/A1/style/vertical-align/save/MIDDLE] id=test-middle-MenuItem\n" +
                "    (mdi-format-align-bottom) \"Bottom\" [/1/Spreadsheet123/cell/A1/style/vertical-align/save/BOTTOM] id=test-bottom-MenuItem\n" +
                "  (mdi-palette) \"Color\" id=test-color-SubMenu\n" +
                "    ColorComponent\n" +
                "      TABLE\n" +
                "        id=\"test-color-Table\" className=dui dui-menu-item\n" +
                "          TBODY\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-1-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-2-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-3-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-4-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-5-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-6-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-7-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-8-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-9-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-10-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-11-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-12-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-13-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-14-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-15-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-16-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-17-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-18-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-19-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-20-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-21-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-22-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-23-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-24-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-25-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-26-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-27-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-28-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-29-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-30-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-31-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-32-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-33-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-34-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-35-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-36-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-37-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-38-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-39-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-40-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-41-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-42-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-43-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-44-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-45-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-46-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-47-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-48-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-49-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-50-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-51-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-52-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-53-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-54-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-55-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-56-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                  \"Clear\" [#/1/Spreadsheet123/cell/A1/style/color/save/] id=test-color-color-clear-Link\n" +
                "  (mdi-palette) \"Background Color\" id=test-background-color-SubMenu\n" +
                "    ColorComponent\n" +
                "      TABLE\n" +
                "        id=\"test-background-color-Table\" className=dui dui-menu-item\n" +
                "          TBODY\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-1-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-2-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-3-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-4-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-5-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-6-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-7-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-8-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-9-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-10-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-11-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-12-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-13-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-14-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-15-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-16-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-17-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-18-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-19-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-20-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-21-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-22-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-23-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-24-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-25-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-26-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-27-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-28-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-29-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-30-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-31-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-32-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-33-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-34-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-35-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-36-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-37-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-38-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-39-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-40-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-41-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-42-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-43-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-44-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-45-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-46-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-47-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-48-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-49-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-50-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-51-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-52-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-53-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-54-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-55-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-56-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                  \"Clear\" [#/1/Spreadsheet123/cell/A1/style/background-color/save/] id=test-background-color-color-clear-Link\n" +
                "  (mdi-format-bold) \"Bold\" [/1/Spreadsheet123/cell/A1/style/font-weight/save/bold] id=test-bold-MenuItem\n" +
                "  (mdi-format-italic) \"Italics\" [/1/Spreadsheet123/cell/A1/style/font-style/save/ITALIC] id=test-italics-MenuItem\n" +
                "  (mdi-format-strikethrough) \"Strike-thru\" [/1/Spreadsheet123/cell/A1/style/text-decoration-line/save/LINE_THROUGH] id=test-strike-thru-MenuItem\n" +
                "  (mdi-format-underline) \"Underline\" [/1/Spreadsheet123/cell/A1/style/text-decoration-line/save/UNDERLINE] id=test-underline-MenuItem\n" +
                "  \"Text case\" id=test-text-case-SubMenu\n" +
                "    (mdi-format-letter-case-upper) \"Normal\" [/1/Spreadsheet123/cell/A1/style/text-transform/save/] id=test-normal-MenuItem\n" +
                "    (mdi-format-letter-case) \"Capitalize\" [/1/Spreadsheet123/cell/A1/style/text-transform/save/CAPITALIZE] id=test-capitalize-MenuItem\n" +
                "    (mdi-format-letter-case-lower) \"Lower case\" [/1/Spreadsheet123/cell/A1/style/text-transform/save/LOWERCASE] id=test-lower-MenuItem\n" +
                "    (mdi-format-letter-case-upper) \"Upper case\" [/1/Spreadsheet123/cell/A1/style/text-transform/save/UPPERCASE] id=test-upper-MenuItem\n" +
                "  \"Wrapping\" id=test-text-wrapping-SubMenu\n" +
                "    (mdi-format-text-wrapping-clip) \"Clip\" [/1/Spreadsheet123/cell/A1/style/overflow-wrap/save/NORMAL] id=test-clip-MenuItem\n" +
                "    (mdi-format-text-wrapping-overflow) \"Overflow\" [/1/Spreadsheet123/cell/A1/style/overflow-x/save/VISIBLE] id=test-overflow-MenuItem\n" +
                "    (mdi-format-text-wrapping-wrap) \"Wrap\" [/1/Spreadsheet123/cell/A1/style/overflow-x/save/HIDDEN] id=test-wrap-MenuItem\n" +
                "  \"Border\" id=test-border-SubMenu\n" +
                "    (mdi-border-top-variant) \"Top\" id=test-border-top-SubMenu\n" +
                "      (mdi-palette) \"Border Top Color\" id=test-border-top-color-SubMenu\n" +
                "        ColorComponent\n" +
                "          TABLE\n" +
                "            id=\"test-border-top-color-Table\" className=dui dui-menu-item\n" +
                "              TBODY\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-1-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-2-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-3-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-4-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-5-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-6-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-7-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-8-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-9-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-10-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-11-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-12-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-13-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-14-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-15-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-16-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-17-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-18-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-19-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-20-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-21-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-22-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-23-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-24-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-25-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-26-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-27-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-28-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-29-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-30-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-31-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-32-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-33-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-34-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-35-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-36-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-37-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-38-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-39-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-40-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-41-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-42-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-43-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-44-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-45-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-46-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-47-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-48-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-49-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-50-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-51-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-52-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-53-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-54-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-55-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-56-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                      \"Clear\" [#/1/Spreadsheet123/cell/A1/style/border-top-color/save/] id=test-border-top-color-color-clear-Link\n" +
                "      \"Style\" id=test-border-top-style-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-top-style/save/NONE] id=test-border-top-style-none-MenuItem\n" +
                "        \"Dashed\" [/1/Spreadsheet123/cell/A1/style/border-top-style/save/DASHED] id=test-border-top-style-dashed-MenuItem\n" +
                "        \"Dotted\" [/1/Spreadsheet123/cell/A1/style/border-top-style/save/DOTTED] id=test-border-top-style-dotted-MenuItem\n" +
                "        \"Solid\" [/1/Spreadsheet123/cell/A1/style/border-top-style/save/SOLID] id=test-border-top-style-solid-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-top-style/save/] id=test-border-top-style-clear-MenuItem\n" +
                "      \"Width\" id=test-border-top-width-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-top-width/save/0px] id=test-border-top-width-0-MenuItem\n" +
                "        \"1\" [/1/Spreadsheet123/cell/A1/style/border-top-width/save/1px] id=test-border-top-width-1-MenuItem\n" +
                "        \"2\" [/1/Spreadsheet123/cell/A1/style/border-top-width/save/2px] id=test-border-top-width-2-MenuItem\n" +
                "        \"3\" [/1/Spreadsheet123/cell/A1/style/border-top-width/save/3px] id=test-border-top-width-3-MenuItem\n" +
                "        \"4\" [/1/Spreadsheet123/cell/A1/style/border-top-width/save/4px] id=test-border-top-width-4-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-top-width/save/] id=test-border-top-width-clear-MenuItem\n" +
                "    (mdi-border-left-variant) \"Left\" id=test-border-left-SubMenu\n" +
                "      (mdi-palette) \"Border Left Color\" id=test-border-left-color-SubMenu\n" +
                "        ColorComponent\n" +
                "          TABLE\n" +
                "            id=\"test-border-left-color-Table\" className=dui dui-menu-item\n" +
                "              TBODY\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-1-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-2-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-3-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-4-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-5-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-6-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-7-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-8-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-9-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-10-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-11-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-12-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-13-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-14-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-15-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-16-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-17-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-18-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-19-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-20-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-21-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-22-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-23-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-24-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-25-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-26-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-27-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-28-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-29-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-30-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-31-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-32-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-33-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-34-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-35-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-36-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-37-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-38-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-39-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-40-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-41-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-42-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-43-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-44-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-45-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-46-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-47-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-48-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-49-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-50-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-51-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-52-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-53-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-54-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-55-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-56-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                      \"Clear\" [#/1/Spreadsheet123/cell/A1/style/border-left-color/save/] id=test-border-left-color-color-clear-Link\n" +
                "      \"Style\" id=test-border-left-style-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-left-style/save/NONE] id=test-border-left-style-none-MenuItem\n" +
                "        \"Dashed\" [/1/Spreadsheet123/cell/A1/style/border-left-style/save/DASHED] id=test-border-left-style-dashed-MenuItem\n" +
                "        \"Dotted\" [/1/Spreadsheet123/cell/A1/style/border-left-style/save/DOTTED] id=test-border-left-style-dotted-MenuItem\n" +
                "        \"Solid\" [/1/Spreadsheet123/cell/A1/style/border-left-style/save/SOLID] id=test-border-left-style-solid-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-left-style/save/] id=test-border-left-style-clear-MenuItem\n" +
                "      \"Width\" id=test-border-left-width-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-left-width/save/0px] id=test-border-left-width-0-MenuItem\n" +
                "        \"1\" [/1/Spreadsheet123/cell/A1/style/border-left-width/save/1px] id=test-border-left-width-1-MenuItem\n" +
                "        \"2\" [/1/Spreadsheet123/cell/A1/style/border-left-width/save/2px] id=test-border-left-width-2-MenuItem\n" +
                "        \"3\" [/1/Spreadsheet123/cell/A1/style/border-left-width/save/3px] id=test-border-left-width-3-MenuItem\n" +
                "        \"4\" [/1/Spreadsheet123/cell/A1/style/border-left-width/save/4px] id=test-border-left-width-4-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-left-width/save/] id=test-border-left-width-clear-MenuItem\n" +
                "    (mdi-border-right-variant) \"Right\" id=test-border-right-SubMenu\n" +
                "      (mdi-palette) \"Border Right Color\" id=test-border-right-color-SubMenu\n" +
                "        ColorComponent\n" +
                "          TABLE\n" +
                "            id=\"test-border-right-color-Table\" className=dui dui-menu-item\n" +
                "              TBODY\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-1-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-2-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-3-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-4-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-5-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-6-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-7-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-8-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-9-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-10-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-11-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-12-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-13-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-14-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-15-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-16-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-17-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-18-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-19-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-20-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-21-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-22-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-23-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-24-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-25-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-26-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-27-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-28-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-29-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-30-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-31-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-32-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-33-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-34-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-35-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-36-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-37-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-38-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-39-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-40-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-41-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-42-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-43-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-44-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-45-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-46-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-47-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-48-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-49-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-50-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-51-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-52-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-53-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-54-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-55-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-56-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                      \"Clear\" [#/1/Spreadsheet123/cell/A1/style/border-right-color/save/] id=test-border-right-color-color-clear-Link\n" +
                "      \"Style\" id=test-border-right-style-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-right-style/save/NONE] id=test-border-right-style-none-MenuItem\n" +
                "        \"Dashed\" [/1/Spreadsheet123/cell/A1/style/border-right-style/save/DASHED] id=test-border-right-style-dashed-MenuItem\n" +
                "        \"Dotted\" [/1/Spreadsheet123/cell/A1/style/border-right-style/save/DOTTED] id=test-border-right-style-dotted-MenuItem\n" +
                "        \"Solid\" [/1/Spreadsheet123/cell/A1/style/border-right-style/save/SOLID] id=test-border-right-style-solid-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-right-style/save/] id=test-border-right-style-clear-MenuItem\n" +
                "      \"Width\" id=test-border-right-width-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-right-width/save/0px] id=test-border-right-width-0-MenuItem\n" +
                "        \"1\" [/1/Spreadsheet123/cell/A1/style/border-right-width/save/1px] id=test-border-right-width-1-MenuItem\n" +
                "        \"2\" [/1/Spreadsheet123/cell/A1/style/border-right-width/save/2px] id=test-border-right-width-2-MenuItem\n" +
                "        \"3\" [/1/Spreadsheet123/cell/A1/style/border-right-width/save/3px] id=test-border-right-width-3-MenuItem\n" +
                "        \"4\" [/1/Spreadsheet123/cell/A1/style/border-right-width/save/4px] id=test-border-right-width-4-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-right-width/save/] id=test-border-right-width-clear-MenuItem\n" +
                "    (mdi-border-bottom-variant) \"Bottom\" id=test-border-bottom-SubMenu\n" +
                "      (mdi-palette) \"Border Bottom Color\" id=test-border-bottom-color-SubMenu\n" +
                "        ColorComponent\n" +
                "          TABLE\n" +
                "            id=\"test-border-bottom-color-Table\" className=dui dui-menu-item\n" +
                "              TBODY\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-1-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-2-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-3-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-4-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-5-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-6-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-7-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-8-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-9-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-10-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-11-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-12-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-13-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-14-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-15-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-16-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-17-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-18-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-19-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-20-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-21-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-22-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-23-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-24-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-25-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-26-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-27-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-28-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-29-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-30-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-31-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-32-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-33-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-34-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-35-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-36-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-37-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-38-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-39-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-40-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-41-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-42-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-43-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-44-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-45-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-46-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-47-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-48-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-49-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-50-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-51-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-52-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-53-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-54-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-55-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-56-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                      \"Clear\" [#/1/Spreadsheet123/cell/A1/style/border-bottom-color/save/] id=test-border-bottom-color-color-clear-Link\n" +
                "      \"Style\" id=test-border-bottom-style-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-bottom-style/save/NONE] id=test-border-bottom-style-none-MenuItem\n" +
                "        \"Dashed\" [/1/Spreadsheet123/cell/A1/style/border-bottom-style/save/DASHED] id=test-border-bottom-style-dashed-MenuItem\n" +
                "        \"Dotted\" [/1/Spreadsheet123/cell/A1/style/border-bottom-style/save/DOTTED] id=test-border-bottom-style-dotted-MenuItem\n" +
                "        \"Solid\" [/1/Spreadsheet123/cell/A1/style/border-bottom-style/save/SOLID] id=test-border-bottom-style-solid-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-bottom-style/save/] id=test-border-bottom-style-clear-MenuItem\n" +
                "      \"Width\" id=test-border-bottom-width-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-bottom-width/save/0px] id=test-border-bottom-width-0-MenuItem\n" +
                "        \"1\" [/1/Spreadsheet123/cell/A1/style/border-bottom-width/save/1px] id=test-border-bottom-width-1-MenuItem\n" +
                "        \"2\" [/1/Spreadsheet123/cell/A1/style/border-bottom-width/save/2px] id=test-border-bottom-width-2-MenuItem\n" +
                "        \"3\" [/1/Spreadsheet123/cell/A1/style/border-bottom-width/save/3px] id=test-border-bottom-width-3-MenuItem\n" +
                "        \"4\" [/1/Spreadsheet123/cell/A1/style/border-bottom-width/save/4px] id=test-border-bottom-width-4-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-bottom-width/save/] id=test-border-bottom-width-clear-MenuItem\n" +
                "    (mdi-border-all-variant) \"All\" id=test-border-all-SubMenu\n" +
                "      (mdi-palette) \"Border Color\" id=test-border-color-SubMenu\n" +
                "        ColorComponent\n" +
                "          TABLE\n" +
                "            id=\"test-border-color-Table\" className=dui dui-menu-item\n" +
                "              TBODY\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-1-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-2-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-3-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-4-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-5-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-6-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-7-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-8-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-9-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-10-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-11-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-12-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-13-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-14-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-15-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-16-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-17-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-18-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-19-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-20-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-21-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-22-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-23-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-24-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-25-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-26-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-27-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-28-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-29-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-30-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-31-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-32-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-33-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-34-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-35-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-36-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-37-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-38-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-39-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-40-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-41-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-42-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-43-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-44-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-45-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-46-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-47-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-48-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-49-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-50-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-51-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-52-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-53-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-54-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-55-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-56-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                      \"Clear\" [#/1/Spreadsheet123/cell/A1/style/border-color/save/] id=test-border-color-color-clear-Link\n" +
                "      \"Style\" id=test-border-all-style-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-style/save/NONE] id=test-border-all-style-none-MenuItem\n" +
                "        \"Dashed\" [/1/Spreadsheet123/cell/A1/style/border-style/save/DASHED] id=test-border-all-style-dashed-MenuItem\n" +
                "        \"Dotted\" [/1/Spreadsheet123/cell/A1/style/border-style/save/DOTTED] id=test-border-all-style-dotted-MenuItem\n" +
                "        \"Solid\" [/1/Spreadsheet123/cell/A1/style/border-style/save/SOLID] id=test-border-all-style-solid-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-style/save/] id=test-border-all-style-clear-MenuItem\n" +
                "      \"Width\" id=test-border-all-width-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-width/save/0px] id=test-border-all-width-0-MenuItem\n" +
                "        \"1\" [/1/Spreadsheet123/cell/A1/style/border-width/save/1px] id=test-border-all-width-1-MenuItem\n" +
                "        \"2\" [/1/Spreadsheet123/cell/A1/style/border-width/save/2px] id=test-border-all-width-2-MenuItem\n" +
                "        \"3\" [/1/Spreadsheet123/cell/A1/style/border-width/save/3px] id=test-border-all-width-3-MenuItem\n" +
                "        \"4\" [/1/Spreadsheet123/cell/A1/style/border-width/save/4px] id=test-border-all-width-4-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-width/save/] id=test-border-all-width-clear-MenuItem\n" +
                "  -----\n" +
                "  (mdi-format-clear) \"Clear style\" [/1/Spreadsheet123/cell/A1/style/*/save/] id=test-clear-style-MenuItem\n"
        );
    }

    @Test
    public void testBuildWithChecked() {
        this.buildAndCheck(
            HistoryToken.selection(
                SpreadsheetId.with(1),
                SpreadsheetName.with("Spreadsheet123"),
                SpreadsheetSelection.A1.setDefaultAnchor()
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
                "  \"Alignment\" id=test-alignment-SubMenu\n" +
                "    (mdi-format-align-left) \"Left\" [/1/Spreadsheet123/cell/A1/style/text-align/save/] CHECKED id=test-left-MenuItem\n" +
                "    (mdi-format-align-center) \"Center\" [/1/Spreadsheet123/cell/A1/style/text-align/save/CENTER] id=test-center-MenuItem\n" +
                "    (mdi-format-align-right) \"Right\" [/1/Spreadsheet123/cell/A1/style/text-align/save/RIGHT] id=test-right-MenuItem\n" +
                "    (mdi-format-align-justify) \"Justify\" [/1/Spreadsheet123/cell/A1/style/text-align/save/JUSTIFY] id=test-justify-MenuItem\n" +
                "  \"Vertical Alignment\" id=test-vertical-alignment-SubMenu\n" +
                "    (mdi-format-align-top) \"Top\" [/1/Spreadsheet123/cell/A1/style/vertical-align/save/TOP] id=test-top-MenuItem\n" +
                "    (mdi-format-align-middle) \"Middle\" [/1/Spreadsheet123/cell/A1/style/vertical-align/save/MIDDLE] id=test-middle-MenuItem\n" +
                "    (mdi-format-align-bottom) \"Bottom\" [/1/Spreadsheet123/cell/A1/style/vertical-align/save/BOTTOM] id=test-bottom-MenuItem\n" +
                "  (mdi-palette) \"Color\" id=test-color-SubMenu\n" +
                "    ColorComponent\n" +
                "      TABLE\n" +
                "        id=\"test-color-Table\" className=dui dui-menu-item\n" +
                "          TBODY\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-1-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-2-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-3-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-4-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-5-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-6-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-7-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-8-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-9-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-10-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-11-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-12-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-13-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-14-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-15-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-16-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-17-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-18-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-19-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-20-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-21-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-22-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-23-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-24-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-25-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-26-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-27-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-28-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-29-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-30-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-31-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-32-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-33-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-34-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-35-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-36-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-37-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-38-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-39-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-40-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-41-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-42-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-43-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-44-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-45-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-46-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-47-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-48-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-49-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-50-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-51-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-52-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-53-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-54-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-55-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-56-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                  \"Clear\" [#/1/Spreadsheet123/cell/A1/style/color/save/] id=test-color-color-clear-Link\n" +
                "  (mdi-palette) \"Background Color\" id=test-background-color-SubMenu\n" +
                "    ColorComponent\n" +
                "      TABLE\n" +
                "        id=\"test-background-color-Table\" className=dui dui-menu-item\n" +
                "          TBODY\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-1-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-2-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-3-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-4-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-5-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-6-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-7-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-8-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-9-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-10-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-11-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-12-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-13-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-14-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-15-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-16-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-17-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-18-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-19-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-20-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-21-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-22-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-23-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-24-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-25-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-26-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-27-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-28-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-29-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-30-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-31-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-32-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-33-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-34-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-35-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-36-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-37-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-38-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-39-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-40-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-41-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-42-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-43-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-44-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-45-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-46-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-47-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-48-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-49-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-50-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-51-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-52-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-53-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-54-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-55-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-56-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                  \"Clear\" [#/1/Spreadsheet123/cell/A1/style/background-color/save/] id=test-background-color-color-clear-Link\n" +
                "  (mdi-format-bold) \"Bold\" [/1/Spreadsheet123/cell/A1/style/font-weight/save/bold] id=test-bold-MenuItem\n" +
                "  (mdi-format-italic) \"Italics\" [/1/Spreadsheet123/cell/A1/style/font-style/save/ITALIC] id=test-italics-MenuItem\n" +
                "  (mdi-format-strikethrough) \"Strike-thru\" [/1/Spreadsheet123/cell/A1/style/text-decoration-line/save/LINE_THROUGH] id=test-strike-thru-MenuItem\n" +
                "  (mdi-format-underline) \"Underline\" [/1/Spreadsheet123/cell/A1/style/text-decoration-line/save/UNDERLINE] id=test-underline-MenuItem\n" +
                "  \"Text case\" id=test-text-case-SubMenu\n" +
                "    (mdi-format-letter-case-upper) \"Normal\" [/1/Spreadsheet123/cell/A1/style/text-transform/save/] id=test-normal-MenuItem\n" +
                "    (mdi-format-letter-case) \"Capitalize\" [/1/Spreadsheet123/cell/A1/style/text-transform/save/CAPITALIZE] id=test-capitalize-MenuItem\n" +
                "    (mdi-format-letter-case-lower) \"Lower case\" [/1/Spreadsheet123/cell/A1/style/text-transform/save/LOWERCASE] id=test-lower-MenuItem\n" +
                "    (mdi-format-letter-case-upper) \"Upper case\" [/1/Spreadsheet123/cell/A1/style/text-transform/save/UPPERCASE] id=test-upper-MenuItem\n" +
                "  \"Wrapping\" id=test-text-wrapping-SubMenu\n" +
                "    (mdi-format-text-wrapping-clip) \"Clip\" [/1/Spreadsheet123/cell/A1/style/overflow-wrap/save/NORMAL] id=test-clip-MenuItem\n" +
                "    (mdi-format-text-wrapping-overflow) \"Overflow\" [/1/Spreadsheet123/cell/A1/style/overflow-x/save/VISIBLE] id=test-overflow-MenuItem\n" +
                "    (mdi-format-text-wrapping-wrap) \"Wrap\" [/1/Spreadsheet123/cell/A1/style/overflow-x/save/HIDDEN] id=test-wrap-MenuItem\n" +
                "  \"Border\" id=test-border-SubMenu\n" +
                "    (mdi-border-top-variant) \"Top\" id=test-border-top-SubMenu\n" +
                "      (mdi-palette) \"Border Top Color\" id=test-border-top-color-SubMenu\n" +
                "        ColorComponent\n" +
                "          TABLE\n" +
                "            id=\"test-border-top-color-Table\" className=dui dui-menu-item\n" +
                "              TBODY\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-1-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-2-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-3-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-4-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-5-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-6-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-7-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-8-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-9-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-10-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-11-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-12-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-13-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-14-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-15-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-16-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-17-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-18-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-19-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-20-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-21-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-22-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-23-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-24-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-25-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-26-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-27-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-28-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-29-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-30-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-31-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-32-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-33-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-34-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-35-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-36-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-37-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-38-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-39-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-40-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-41-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-42-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-43-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-44-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-45-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-46-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-47-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-48-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-49-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-50-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-51-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-52-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-53-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-54-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-55-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-56-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                      \"Clear\" [#/1/Spreadsheet123/cell/A1/style/border-top-color/save/] id=test-border-top-color-color-clear-Link\n" +
                "      \"Style\" id=test-border-top-style-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-top-style/save/NONE] id=test-border-top-style-none-MenuItem\n" +
                "        \"Dashed\" [/1/Spreadsheet123/cell/A1/style/border-top-style/save/DASHED] id=test-border-top-style-dashed-MenuItem\n" +
                "        \"Dotted\" [/1/Spreadsheet123/cell/A1/style/border-top-style/save/DOTTED] id=test-border-top-style-dotted-MenuItem\n" +
                "        \"Solid\" [/1/Spreadsheet123/cell/A1/style/border-top-style/save/SOLID] id=test-border-top-style-solid-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-top-style/save/] id=test-border-top-style-clear-MenuItem\n" +
                "      \"Width\" id=test-border-top-width-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-top-width/save/0px] id=test-border-top-width-0-MenuItem\n" +
                "        \"1\" [/1/Spreadsheet123/cell/A1/style/border-top-width/save/1px] id=test-border-top-width-1-MenuItem\n" +
                "        \"2\" [/1/Spreadsheet123/cell/A1/style/border-top-width/save/2px] id=test-border-top-width-2-MenuItem\n" +
                "        \"3\" [/1/Spreadsheet123/cell/A1/style/border-top-width/save/3px] id=test-border-top-width-3-MenuItem\n" +
                "        \"4\" [/1/Spreadsheet123/cell/A1/style/border-top-width/save/4px] id=test-border-top-width-4-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-top-width/save/] id=test-border-top-width-clear-MenuItem\n" +
                "    (mdi-border-left-variant) \"Left\" id=test-border-left-SubMenu\n" +
                "      (mdi-palette) \"Border Left Color\" id=test-border-left-color-SubMenu\n" +
                "        ColorComponent\n" +
                "          TABLE\n" +
                "            id=\"test-border-left-color-Table\" className=dui dui-menu-item\n" +
                "              TBODY\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-1-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-2-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-3-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-4-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-5-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-6-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-7-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-8-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-9-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-10-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-11-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-12-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-13-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-14-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-15-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-16-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-17-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-18-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-19-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-20-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-21-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-22-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-23-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-24-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-25-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-26-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-27-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-28-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-29-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-30-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-31-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-32-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-33-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-34-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-35-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-36-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-37-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-38-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-39-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-40-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-41-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-42-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-43-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-44-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-45-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-46-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-47-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-48-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-49-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-50-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-51-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-52-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-53-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-54-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-55-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-56-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                      \"Clear\" [#/1/Spreadsheet123/cell/A1/style/border-left-color/save/] id=test-border-left-color-color-clear-Link\n" +
                "      \"Style\" id=test-border-left-style-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-left-style/save/NONE] id=test-border-left-style-none-MenuItem\n" +
                "        \"Dashed\" [/1/Spreadsheet123/cell/A1/style/border-left-style/save/DASHED] id=test-border-left-style-dashed-MenuItem\n" +
                "        \"Dotted\" [/1/Spreadsheet123/cell/A1/style/border-left-style/save/DOTTED] id=test-border-left-style-dotted-MenuItem\n" +
                "        \"Solid\" [/1/Spreadsheet123/cell/A1/style/border-left-style/save/SOLID] id=test-border-left-style-solid-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-left-style/save/] id=test-border-left-style-clear-MenuItem\n" +
                "      \"Width\" id=test-border-left-width-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-left-width/save/0px] id=test-border-left-width-0-MenuItem\n" +
                "        \"1\" [/1/Spreadsheet123/cell/A1/style/border-left-width/save/1px] id=test-border-left-width-1-MenuItem\n" +
                "        \"2\" [/1/Spreadsheet123/cell/A1/style/border-left-width/save/2px] id=test-border-left-width-2-MenuItem\n" +
                "        \"3\" [/1/Spreadsheet123/cell/A1/style/border-left-width/save/3px] id=test-border-left-width-3-MenuItem\n" +
                "        \"4\" [/1/Spreadsheet123/cell/A1/style/border-left-width/save/4px] id=test-border-left-width-4-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-left-width/save/] id=test-border-left-width-clear-MenuItem\n" +
                "    (mdi-border-right-variant) \"Right\" id=test-border-right-SubMenu\n" +
                "      (mdi-palette) \"Border Right Color\" id=test-border-right-color-SubMenu\n" +
                "        ColorComponent\n" +
                "          TABLE\n" +
                "            id=\"test-border-right-color-Table\" className=dui dui-menu-item\n" +
                "              TBODY\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-1-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-2-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-3-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-4-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-5-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-6-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-7-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-8-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-9-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-10-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-11-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-12-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-13-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-14-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-15-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-16-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-17-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-18-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-19-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-20-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-21-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-22-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-23-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-24-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-25-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-26-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-27-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-28-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-29-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-30-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-31-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-32-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-33-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-34-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-35-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-36-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-37-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-38-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-39-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-40-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-41-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-42-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-43-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-44-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-45-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-46-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-47-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-48-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-49-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-50-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-51-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-52-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-53-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-54-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-55-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-56-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                      \"Clear\" [#/1/Spreadsheet123/cell/A1/style/border-right-color/save/] id=test-border-right-color-color-clear-Link\n" +
                "      \"Style\" id=test-border-right-style-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-right-style/save/NONE] id=test-border-right-style-none-MenuItem\n" +
                "        \"Dashed\" [/1/Spreadsheet123/cell/A1/style/border-right-style/save/DASHED] id=test-border-right-style-dashed-MenuItem\n" +
                "        \"Dotted\" [/1/Spreadsheet123/cell/A1/style/border-right-style/save/DOTTED] id=test-border-right-style-dotted-MenuItem\n" +
                "        \"Solid\" [/1/Spreadsheet123/cell/A1/style/border-right-style/save/SOLID] id=test-border-right-style-solid-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-right-style/save/] id=test-border-right-style-clear-MenuItem\n" +
                "      \"Width\" id=test-border-right-width-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-right-width/save/0px] id=test-border-right-width-0-MenuItem\n" +
                "        \"1\" [/1/Spreadsheet123/cell/A1/style/border-right-width/save/1px] id=test-border-right-width-1-MenuItem\n" +
                "        \"2\" [/1/Spreadsheet123/cell/A1/style/border-right-width/save/2px] id=test-border-right-width-2-MenuItem\n" +
                "        \"3\" [/1/Spreadsheet123/cell/A1/style/border-right-width/save/3px] id=test-border-right-width-3-MenuItem\n" +
                "        \"4\" [/1/Spreadsheet123/cell/A1/style/border-right-width/save/4px] id=test-border-right-width-4-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-right-width/save/] id=test-border-right-width-clear-MenuItem\n" +
                "    (mdi-border-bottom-variant) \"Bottom\" id=test-border-bottom-SubMenu\n" +
                "      (mdi-palette) \"Border Bottom Color\" id=test-border-bottom-color-SubMenu\n" +
                "        ColorComponent\n" +
                "          TABLE\n" +
                "            id=\"test-border-bottom-color-Table\" className=dui dui-menu-item\n" +
                "              TBODY\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-1-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-2-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-3-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-4-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-5-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-6-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-7-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-8-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-9-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-10-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-11-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-12-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-13-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-14-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-15-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-16-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-17-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-18-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-19-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-20-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-21-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-22-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-23-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-24-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-25-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-26-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-27-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-28-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-29-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-30-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-31-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-32-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-33-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-34-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-35-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-36-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-37-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-38-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-39-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-40-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-41-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-42-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-43-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-44-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-45-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-46-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-47-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-48-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-49-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-50-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-51-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-52-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-53-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-54-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-55-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-56-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                      \"Clear\" [#/1/Spreadsheet123/cell/A1/style/border-bottom-color/save/] id=test-border-bottom-color-color-clear-Link\n" +
                "      \"Style\" id=test-border-bottom-style-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-bottom-style/save/NONE] id=test-border-bottom-style-none-MenuItem\n" +
                "        \"Dashed\" [/1/Spreadsheet123/cell/A1/style/border-bottom-style/save/DASHED] id=test-border-bottom-style-dashed-MenuItem\n" +
                "        \"Dotted\" [/1/Spreadsheet123/cell/A1/style/border-bottom-style/save/DOTTED] id=test-border-bottom-style-dotted-MenuItem\n" +
                "        \"Solid\" [/1/Spreadsheet123/cell/A1/style/border-bottom-style/save/SOLID] id=test-border-bottom-style-solid-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-bottom-style/save/] id=test-border-bottom-style-clear-MenuItem\n" +
                "      \"Width\" id=test-border-bottom-width-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-bottom-width/save/0px] id=test-border-bottom-width-0-MenuItem\n" +
                "        \"1\" [/1/Spreadsheet123/cell/A1/style/border-bottom-width/save/1px] id=test-border-bottom-width-1-MenuItem\n" +
                "        \"2\" [/1/Spreadsheet123/cell/A1/style/border-bottom-width/save/2px] id=test-border-bottom-width-2-MenuItem\n" +
                "        \"3\" [/1/Spreadsheet123/cell/A1/style/border-bottom-width/save/3px] id=test-border-bottom-width-3-MenuItem\n" +
                "        \"4\" [/1/Spreadsheet123/cell/A1/style/border-bottom-width/save/4px] id=test-border-bottom-width-4-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-bottom-width/save/] id=test-border-bottom-width-clear-MenuItem\n" +
                "    (mdi-border-all-variant) \"All\" id=test-border-all-SubMenu\n" +
                "      (mdi-palette) \"Border Color\" id=test-border-color-SubMenu\n" +
                "        ColorComponent\n" +
                "          TABLE\n" +
                "            id=\"test-border-color-Table\" className=dui dui-menu-item\n" +
                "              TBODY\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-1-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-2-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-3-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-4-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-5-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-6-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-7-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-8-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-9-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-10-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-11-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-12-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-13-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-14-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-15-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-16-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-17-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-18-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-19-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-20-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-21-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-22-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-23-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-24-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-25-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-26-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-27-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-28-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-29-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-30-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-31-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-32-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-33-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-34-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-35-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-36-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-37-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-38-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-39-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-40-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-41-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-42-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-43-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-44-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-45-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-46-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-47-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-48-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-49-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-50-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-51-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-52-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-53-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-54-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-55-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-56-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                      \"Clear\" [#/1/Spreadsheet123/cell/A1/style/border-color/save/] id=test-border-color-color-clear-Link\n" +
                "      \"Style\" id=test-border-all-style-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-style/save/NONE] id=test-border-all-style-none-MenuItem\n" +
                "        \"Dashed\" [/1/Spreadsheet123/cell/A1/style/border-style/save/DASHED] id=test-border-all-style-dashed-MenuItem\n" +
                "        \"Dotted\" [/1/Spreadsheet123/cell/A1/style/border-style/save/DOTTED] id=test-border-all-style-dotted-MenuItem\n" +
                "        \"Solid\" [/1/Spreadsheet123/cell/A1/style/border-style/save/SOLID] id=test-border-all-style-solid-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-style/save/] id=test-border-all-style-clear-MenuItem\n" +
                "      \"Width\" id=test-border-all-width-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-width/save/0px] id=test-border-all-width-0-MenuItem\n" +
                "        \"1\" [/1/Spreadsheet123/cell/A1/style/border-width/save/1px] id=test-border-all-width-1-MenuItem\n" +
                "        \"2\" [/1/Spreadsheet123/cell/A1/style/border-width/save/2px] id=test-border-all-width-2-MenuItem\n" +
                "        \"3\" [/1/Spreadsheet123/cell/A1/style/border-width/save/3px] id=test-border-all-width-3-MenuItem\n" +
                "        \"4\" [/1/Spreadsheet123/cell/A1/style/border-width/save/4px] id=test-border-all-width-4-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-width/save/] id=test-border-all-width-clear-MenuItem\n" +
                "  -----\n" +
                "  (mdi-format-clear) \"Clear style\" [/1/Spreadsheet123/cell/A1/style/*/save/] id=test-clear-style-MenuItem\n"
        );
    }

    @Test
    public void testBuildWithRecents() {
        this.buildAndCheck(
            HistoryToken.selection(
                SpreadsheetId.with(1),
                SpreadsheetName.with("Spreadsheet123"),
                SpreadsheetSelection.A1.setDefaultAnchor()
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
                "  \"Alignment\" id=test-alignment-SubMenu\n" +
                "    (mdi-format-align-left) \"Left\" [/1/Spreadsheet123/cell/A1/style/text-align/save/LEFT] id=test-left-MenuItem\n" +
                "    (mdi-format-align-center) \"Center\" [/1/Spreadsheet123/cell/A1/style/text-align/save/CENTER] id=test-center-MenuItem\n" +
                "    (mdi-format-align-right) \"Right\" [/1/Spreadsheet123/cell/A1/style/text-align/save/RIGHT] id=test-right-MenuItem\n" +
                "    (mdi-format-align-justify) \"Justify\" [/1/Spreadsheet123/cell/A1/style/text-align/save/JUSTIFY] id=test-justify-MenuItem\n" +
                "  \"Vertical Alignment\" id=test-vertical-alignment-SubMenu\n" +
                "    (mdi-format-align-top) \"Top\" [/1/Spreadsheet123/cell/A1/style/vertical-align/save/TOP] id=test-top-MenuItem\n" +
                "    (mdi-format-align-middle) \"Middle\" [/1/Spreadsheet123/cell/A1/style/vertical-align/save/MIDDLE] id=test-middle-MenuItem\n" +
                "    (mdi-format-align-bottom) \"Bottom\" [/1/Spreadsheet123/cell/A1/style/vertical-align/save/BOTTOM] id=test-bottom-MenuItem\n" +
                "  (mdi-palette) \"Color\" id=test-color-SubMenu\n" +
                "    ColorComponent\n" +
                "      TABLE\n" +
                "        id=\"test-color-Table\" className=dui dui-menu-item\n" +
                "          TBODY\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-1-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-2-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-3-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-4-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-5-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-6-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-7-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-8-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-9-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-10-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-11-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-12-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-13-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-14-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-15-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-16-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-17-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-18-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-19-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-20-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-21-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-22-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-23-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-24-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-25-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-26-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-27-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-28-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-29-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-30-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-31-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-32-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-33-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-34-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-35-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-36-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-37-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-38-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-39-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-40-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-41-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-42-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-43-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-44-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-45-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-46-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-47-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-48-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-49-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-50-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-51-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-52-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-53-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-54-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-55-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-color-color-56-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                  \"Clear\" [#/1/Spreadsheet123/cell/A1/style/color/save/] id=test-color-color-clear-Link\n" +
                "  (mdi-palette) \"Background Color\" id=test-background-color-SubMenu\n" +
                "    ColorComponent\n" +
                "      TABLE\n" +
                "        id=\"test-background-color-Table\" className=dui dui-menu-item\n" +
                "          TBODY\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-1-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-2-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-3-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-4-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-5-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-6-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-7-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-8-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-9-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-10-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-11-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-12-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-13-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-14-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-15-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-16-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-17-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-18-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-19-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-20-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-21-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-22-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-23-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-24-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-25-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-26-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-27-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-28-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-29-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-30-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-31-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-32-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-33-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-34-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-35-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-36-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-37-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-38-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-39-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-40-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-41-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-42-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-43-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-44-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-45-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-46-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-47-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-48-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-49-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-50-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-51-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-52-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-53-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-54-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-55-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=test-background-color-color-56-Link\n" +
                "            TR\n",
            "              TD\n" +
                "                colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                  \"Clear\" [#/1/Spreadsheet123/cell/A1/style/background-color/save/] id=test-background-color-color-clear-Link\n" +
                "  (mdi-format-bold) \"Bold\" [/1/Spreadsheet123/cell/A1/style/font-weight/save/bold] id=test-bold-MenuItem\n" +
                "  (mdi-format-italic) \"Italics\" [/1/Spreadsheet123/cell/A1/style/font-style/save/ITALIC] id=test-italics-MenuItem\n" +
                "  (mdi-format-strikethrough) \"Strike-thru\" [/1/Spreadsheet123/cell/A1/style/text-decoration-line/save/LINE_THROUGH] id=test-strike-thru-MenuItem\n" +
                "  (mdi-format-underline) \"Underline\" [/1/Spreadsheet123/cell/A1/style/text-decoration-line/save/UNDERLINE] id=test-underline-MenuItem\n" +
                "  \"Text case\" id=test-text-case-SubMenu\n" +
                "    (mdi-format-letter-case-upper) \"Normal\" [/1/Spreadsheet123/cell/A1/style/text-transform/save/] id=test-normal-MenuItem\n" +
                "    (mdi-format-letter-case) \"Capitalize\" [/1/Spreadsheet123/cell/A1/style/text-transform/save/CAPITALIZE] id=test-capitalize-MenuItem\n" +
                "    (mdi-format-letter-case-lower) \"Lower case\" [/1/Spreadsheet123/cell/A1/style/text-transform/save/LOWERCASE] id=test-lower-MenuItem\n" +
                "    (mdi-format-letter-case-upper) \"Upper case\" [/1/Spreadsheet123/cell/A1/style/text-transform/save/UPPERCASE] id=test-upper-MenuItem\n" +
                "  \"Wrapping\" id=test-text-wrapping-SubMenu\n" +
                "    (mdi-format-text-wrapping-clip) \"Clip\" [/1/Spreadsheet123/cell/A1/style/overflow-wrap/save/NORMAL] id=test-clip-MenuItem\n" +
                "    (mdi-format-text-wrapping-overflow) \"Overflow\" [/1/Spreadsheet123/cell/A1/style/overflow-x/save/VISIBLE] id=test-overflow-MenuItem\n" +
                "    (mdi-format-text-wrapping-wrap) \"Wrap\" [/1/Spreadsheet123/cell/A1/style/overflow-x/save/HIDDEN] id=test-wrap-MenuItem\n" +
                "  \"Border\" id=test-border-SubMenu\n" +
                "    (mdi-border-top-variant) \"Top\" id=test-border-top-SubMenu\n" +
                "      (mdi-palette) \"Border Top Color\" id=test-border-top-color-SubMenu\n" +
                "        ColorComponent\n" +
                "          TABLE\n" +
                "            id=\"test-border-top-color-Table\" className=dui dui-menu-item\n" +
                "              TBODY\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-1-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-2-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-3-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-4-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-5-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-6-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-7-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-8-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-9-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-10-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-11-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-12-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-13-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-14-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-15-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-16-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-17-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-18-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-19-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-20-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-21-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-22-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-23-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-24-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-25-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-26-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-27-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-28-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-29-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-30-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-31-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-32-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-33-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-34-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-35-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-36-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-37-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-38-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-39-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-40-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-41-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-42-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-43-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-44-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-45-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-46-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-47-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-48-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-49-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-50-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-51-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-52-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-53-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-54-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-55-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-top-color-color-56-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                      \"Clear\" [#/1/Spreadsheet123/cell/A1/style/border-top-color/save/] id=test-border-top-color-color-clear-Link\n" +
                "      \"Style\" id=test-border-top-style-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-top-style/save/NONE] id=test-border-top-style-none-MenuItem\n" +
                "        \"Dashed\" [/1/Spreadsheet123/cell/A1/style/border-top-style/save/DASHED] id=test-border-top-style-dashed-MenuItem\n" +
                "        \"Dotted\" [/1/Spreadsheet123/cell/A1/style/border-top-style/save/DOTTED] id=test-border-top-style-dotted-MenuItem\n" +
                "        \"Solid\" [/1/Spreadsheet123/cell/A1/style/border-top-style/save/SOLID] id=test-border-top-style-solid-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-top-style/save/] id=test-border-top-style-clear-MenuItem\n" +
                "      \"Width\" id=test-border-top-width-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-top-width/save/0px] id=test-border-top-width-0-MenuItem\n" +
                "        \"1\" [/1/Spreadsheet123/cell/A1/style/border-top-width/save/1px] id=test-border-top-width-1-MenuItem\n" +
                "        \"2\" [/1/Spreadsheet123/cell/A1/style/border-top-width/save/2px] id=test-border-top-width-2-MenuItem\n" +
                "        \"3\" [/1/Spreadsheet123/cell/A1/style/border-top-width/save/3px] id=test-border-top-width-3-MenuItem\n" +
                "        \"4\" [/1/Spreadsheet123/cell/A1/style/border-top-width/save/4px] id=test-border-top-width-4-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-top-width/save/] id=test-border-top-width-clear-MenuItem\n" +
                "    (mdi-border-left-variant) \"Left\" id=test-border-left-SubMenu\n" +
                "      (mdi-palette) \"Border Left Color\" id=test-border-left-color-SubMenu\n" +
                "        ColorComponent\n" +
                "          TABLE\n" +
                "            id=\"test-border-left-color-Table\" className=dui dui-menu-item\n" +
                "              TBODY\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-1-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-2-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-3-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-4-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-5-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-6-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-7-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-8-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-9-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-10-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-11-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-12-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-13-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-14-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-15-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-16-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-17-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-18-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-19-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-20-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-21-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-22-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-23-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-24-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-25-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-26-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-27-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-28-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-29-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-30-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-31-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-32-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-33-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-34-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-35-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-36-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-37-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-38-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-39-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-40-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-41-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-42-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-43-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-44-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-45-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-46-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-47-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-48-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-49-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-50-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-51-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-52-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-53-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-54-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-55-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-left-color-color-56-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                      \"Clear\" [#/1/Spreadsheet123/cell/A1/style/border-left-color/save/] id=test-border-left-color-color-clear-Link\n" +
                "      \"Style\" id=test-border-left-style-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-left-style/save/NONE] id=test-border-left-style-none-MenuItem\n" +
                "        \"Dashed\" [/1/Spreadsheet123/cell/A1/style/border-left-style/save/DASHED] id=test-border-left-style-dashed-MenuItem\n" +
                "        \"Dotted\" [/1/Spreadsheet123/cell/A1/style/border-left-style/save/DOTTED] id=test-border-left-style-dotted-MenuItem\n" +
                "        \"Solid\" [/1/Spreadsheet123/cell/A1/style/border-left-style/save/SOLID] id=test-border-left-style-solid-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-left-style/save/] id=test-border-left-style-clear-MenuItem\n" +
                "      \"Width\" id=test-border-left-width-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-left-width/save/0px] id=test-border-left-width-0-MenuItem\n" +
                "        \"1\" [/1/Spreadsheet123/cell/A1/style/border-left-width/save/1px] id=test-border-left-width-1-MenuItem\n" +
                "        \"2\" [/1/Spreadsheet123/cell/A1/style/border-left-width/save/2px] id=test-border-left-width-2-MenuItem\n" +
                "        \"3\" [/1/Spreadsheet123/cell/A1/style/border-left-width/save/3px] id=test-border-left-width-3-MenuItem\n" +
                "        \"4\" [/1/Spreadsheet123/cell/A1/style/border-left-width/save/4px] id=test-border-left-width-4-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-left-width/save/] id=test-border-left-width-clear-MenuItem\n" +
                "    (mdi-border-right-variant) \"Right\" id=test-border-right-SubMenu\n" +
                "      (mdi-palette) \"Border Right Color\" id=test-border-right-color-SubMenu\n" +
                "        ColorComponent\n" +
                "          TABLE\n" +
                "            id=\"test-border-right-color-Table\" className=dui dui-menu-item\n" +
                "              TBODY\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-1-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-2-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-3-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-4-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-5-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-6-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-7-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-8-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-9-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-10-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-11-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-12-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-13-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-14-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-15-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-16-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-17-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-18-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-19-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-20-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-21-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-22-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-23-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-24-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-25-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-26-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-27-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-28-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-29-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-30-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-31-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-32-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-33-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-34-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-35-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-36-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-37-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-38-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-39-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-40-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-41-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-42-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-43-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-44-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-45-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-46-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-47-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-48-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-49-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-50-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-51-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-52-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-53-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-54-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-55-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-right-color-color-56-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                      \"Clear\" [#/1/Spreadsheet123/cell/A1/style/border-right-color/save/] id=test-border-right-color-color-clear-Link\n" +
                "      \"Style\" id=test-border-right-style-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-right-style/save/NONE] id=test-border-right-style-none-MenuItem\n" +
                "        \"Dashed\" [/1/Spreadsheet123/cell/A1/style/border-right-style/save/DASHED] id=test-border-right-style-dashed-MenuItem\n" +
                "        \"Dotted\" [/1/Spreadsheet123/cell/A1/style/border-right-style/save/DOTTED] id=test-border-right-style-dotted-MenuItem\n" +
                "        \"Solid\" [/1/Spreadsheet123/cell/A1/style/border-right-style/save/SOLID] id=test-border-right-style-solid-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-right-style/save/] id=test-border-right-style-clear-MenuItem\n" +
                "      \"Width\" id=test-border-right-width-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-right-width/save/0px] id=test-border-right-width-0-MenuItem\n" +
                "        \"1\" [/1/Spreadsheet123/cell/A1/style/border-right-width/save/1px] id=test-border-right-width-1-MenuItem\n" +
                "        \"2\" [/1/Spreadsheet123/cell/A1/style/border-right-width/save/2px] id=test-border-right-width-2-MenuItem\n" +
                "        \"3\" [/1/Spreadsheet123/cell/A1/style/border-right-width/save/3px] id=test-border-right-width-3-MenuItem\n" +
                "        \"4\" [/1/Spreadsheet123/cell/A1/style/border-right-width/save/4px] id=test-border-right-width-4-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-right-width/save/] id=test-border-right-width-clear-MenuItem\n" +
                "    (mdi-border-bottom-variant) \"Bottom\" id=test-border-bottom-SubMenu\n" +
                "      (mdi-palette) \"Border Bottom Color\" id=test-border-bottom-color-SubMenu\n" +
                "        ColorComponent\n" +
                "          TABLE\n" +
                "            id=\"test-border-bottom-color-Table\" className=dui dui-menu-item\n" +
                "              TBODY\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-1-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-2-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-3-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-4-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-5-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-6-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-7-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-8-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-9-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-10-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-11-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-12-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-13-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-14-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-15-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-16-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-17-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-18-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-19-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-20-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-21-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-22-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-23-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-24-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-25-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-26-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-27-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-28-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-29-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-30-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-31-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-32-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-33-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-34-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-35-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-36-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-37-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-38-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-39-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-40-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-41-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-42-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-43-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-44-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-45-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-46-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-47-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-48-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-49-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-50-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-51-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-52-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-53-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-54-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-55-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-bottom-color-color-56-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                      \"Clear\" [#/1/Spreadsheet123/cell/A1/style/border-bottom-color/save/] id=test-border-bottom-color-color-clear-Link\n" +
                "      \"Style\" id=test-border-bottom-style-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-bottom-style/save/NONE] id=test-border-bottom-style-none-MenuItem\n" +
                "        \"Dashed\" [/1/Spreadsheet123/cell/A1/style/border-bottom-style/save/DASHED] id=test-border-bottom-style-dashed-MenuItem\n" +
                "        \"Dotted\" [/1/Spreadsheet123/cell/A1/style/border-bottom-style/save/DOTTED] id=test-border-bottom-style-dotted-MenuItem\n" +
                "        \"Solid\" [/1/Spreadsheet123/cell/A1/style/border-bottom-style/save/SOLID] id=test-border-bottom-style-solid-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-bottom-style/save/] id=test-border-bottom-style-clear-MenuItem\n" +
                "      \"Width\" id=test-border-bottom-width-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-bottom-width/save/0px] id=test-border-bottom-width-0-MenuItem\n" +
                "        \"1\" [/1/Spreadsheet123/cell/A1/style/border-bottom-width/save/1px] id=test-border-bottom-width-1-MenuItem\n" +
                "        \"2\" [/1/Spreadsheet123/cell/A1/style/border-bottom-width/save/2px] id=test-border-bottom-width-2-MenuItem\n" +
                "        \"3\" [/1/Spreadsheet123/cell/A1/style/border-bottom-width/save/3px] id=test-border-bottom-width-3-MenuItem\n" +
                "        \"4\" [/1/Spreadsheet123/cell/A1/style/border-bottom-width/save/4px] id=test-border-bottom-width-4-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-bottom-width/save/] id=test-border-bottom-width-clear-MenuItem\n" +
                "    (mdi-border-all-variant) \"All\" id=test-border-all-SubMenu\n" +
                "      (mdi-palette) \"Border Color\" id=test-border-color-SubMenu\n" +
                "        ColorComponent\n" +
                "          TABLE\n" +
                "            id=\"test-border-color-Table\" className=dui dui-menu-item\n" +
                "              TBODY\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-1-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-2-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-3-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-4-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-5-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-6-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-7-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-8-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-9-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-10-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-11-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-12-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-13-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-14-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-15-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-16-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-17-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-18-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-19-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-20-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-21-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-22-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-23-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-24-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-25-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-26-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-27-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-28-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-29-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-30-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-31-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-32-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-33-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-34-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-35-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-36-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-37-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-38-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-39-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-40-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-41-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-42-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-43-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-44-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-45-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-46-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-47-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-48-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-49-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-50-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-51-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-52-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-53-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-54-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-55-Link\n" +
                "                  TD\n" +
                "                    style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                      DISABLED id=test-border-color-color-56-Link\n" +
                "                TR\n",
            "                  TD\n" +
                "                    colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                      \"Clear\" [#/1/Spreadsheet123/cell/A1/style/border-color/save/] id=test-border-color-color-clear-Link\n" +
                "      \"Style\" id=test-border-all-style-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-style/save/NONE] id=test-border-all-style-none-MenuItem\n" +
                "        \"Dashed\" [/1/Spreadsheet123/cell/A1/style/border-style/save/DASHED] id=test-border-all-style-dashed-MenuItem\n" +
                "        \"Dotted\" [/1/Spreadsheet123/cell/A1/style/border-style/save/DOTTED] id=test-border-all-style-dotted-MenuItem\n" +
                "        \"Solid\" [/1/Spreadsheet123/cell/A1/style/border-style/save/SOLID] id=test-border-all-style-solid-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-style/save/] id=test-border-all-style-clear-MenuItem\n" +
                "      \"Width\" id=test-border-all-width-SubMenu\n" +
                "        \"None\" [/1/Spreadsheet123/cell/A1/style/border-width/save/0px] id=test-border-all-width-0-MenuItem\n" +
                "        \"1\" [/1/Spreadsheet123/cell/A1/style/border-width/save/1px] id=test-border-all-width-1-MenuItem\n" +
                "        \"2\" [/1/Spreadsheet123/cell/A1/style/border-width/save/2px] id=test-border-all-width-2-MenuItem\n" +
                "        \"3\" [/1/Spreadsheet123/cell/A1/style/border-width/save/3px] id=test-border-all-width-3-MenuItem\n" +
                "        \"4\" [/1/Spreadsheet123/cell/A1/style/border-width/save/4px] id=test-border-all-width-4-MenuItem\n" +
                "        (mdi-format-clear) \"Clear\" [/1/Spreadsheet123/cell/A1/style/border-width/save/] id=test-border-all-width-clear-MenuItem\n" +
                "  -----\n" +
                "  (mdi-format-clear) \"Clear style\" [/1/Spreadsheet123/cell/A1/style/*/save/] id=test-clear-style-MenuItem\n" +
                "  -----\n" +
                "  \"Set Color black\" [/1/Spreadsheet123/cell/A1/style/color/save/black] id=test-recent-style-0-MenuItem\n"
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

        SpreadsheetSelectionMenuStyle.build(
            historyToken,
            menu,
            context
        );

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
                ).loadFromLocale(CURRENCY_LOCALE_CONTEXT);
            }
        };
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetSelectionMenuStyle> type() {
        return SpreadsheetSelectionMenuStyle.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
