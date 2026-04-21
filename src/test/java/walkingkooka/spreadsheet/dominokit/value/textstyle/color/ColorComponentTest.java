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

package walkingkooka.spreadsheet.dominokit.value.textstyle.color;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.color.Color;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

import static walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting.METADATA_EN_AU;

public final class ColorComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, Color, ColorComponent>,
    ClassTesting<ColorComponent> {

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "ColorComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] icons=mdi-close-circle, mdi-palette REQUIRED\n" +
                "      Errors\n" +
                "        Empty \"text\"\n" +
                "    ColorPaletteComponent\n" +
                "      TABLE\n" +
                "        id=\"TestIdPrefix123-Table\" className=dui dui-menu-item\n" +
                "          TBODY\n" +
                "            TR\n" +
                "              TD\n" +
                "                style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  \"Black\" [#/1/SpreadsheetName1/cell/A1/style/color/save/black] id=TestIdPrefix123-color-1-Link\n" +
                "              TD\n" +
                "                style=\"background-color: white; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  \"White\" [#/1/SpreadsheetName1/cell/A1/style/color/save/white] id=TestIdPrefix123-color-2-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-3-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-4-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-5-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-6-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-7-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-8-Link\n" +
                "            TR\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-9-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-10-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-11-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-12-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-13-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-14-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-15-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-16-Link\n" +
                "            TR\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-17-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-18-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-19-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-20-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-21-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-22-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-23-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-24-Link\n" +
                "            TR\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-25-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-26-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-27-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-28-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-29-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-30-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-31-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-32-Link\n" +
                "            TR\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-33-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-34-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-35-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-36-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-37-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-38-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-39-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-40-Link\n" +
                "            TR\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-41-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-42-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-43-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-44-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-45-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-46-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-47-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-48-Link\n" +
                "            TR\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-49-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-50-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-51-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-52-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-53-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-54-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-55-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-56-Link\n" +
                "            TR\n" +
                "              TD\n" +
                "                colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                  \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/color/save/] id=TestIdPrefix123-color-clear-Link\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(Color.BLACK)
                ),
            "ColorComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [black] icons=mdi-close-circle, mdi-palette REQUIRED\n" +
                "    ColorPaletteComponent\n" +
                "      TABLE\n" +
                "        id=\"TestIdPrefix123-Table\" className=dui dui-menu-item\n" +
                "          TBODY\n" +
                "            TR\n" +
                "              TD\n" +
                "                style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  \"Black\" DISABLED id=TestIdPrefix123-color-1-Link\n" +
                "              TD\n" +
                "                style=\"background-color: white; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  \"White\" [#/1/SpreadsheetName1/cell/A1/style/color/save/white] id=TestIdPrefix123-color-2-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-3-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-4-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-5-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-6-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-7-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-8-Link\n" +
                "            TR\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-9-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-10-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-11-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-12-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-13-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-14-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-15-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-16-Link\n" +
                "            TR\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-17-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-18-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-19-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-20-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-21-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-22-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-23-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-24-Link\n" +
                "            TR\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-25-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-26-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-27-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-28-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-29-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-30-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-31-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-32-Link\n" +
                "            TR\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-33-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-34-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-35-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-36-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-37-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-38-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-39-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-40-Link\n" +
                "            TR\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-41-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-42-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-43-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-44-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-45-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-46-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-47-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-48-Link\n" +
                "            TR\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-49-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-50-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-51-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-52-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-53-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-54-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-55-Link\n" +
                "              TD\n" +
                "                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                  DISABLED id=TestIdPrefix123-color-56-Link\n" +
                "            TR\n" +
                "              TD\n" +
                "                colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                  \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/color/save/] id=TestIdPrefix123-color-clear-Link\n"
        );
    }

    @Test
    public void testAddValueWatcher() {
        final ColorComponent component = this.createComponent();

        component.addValueWatcher(
            value -> {
                ColorComponentTest.this.fired = value.orElse(null);
            }
        );

        this.fired = null;

        final Color color = Color.BLACK;
        component.setValue(
            Optional.of(color)
        );

        this.checkEquals(
            color,
            this.fired
        );
    }

    private Color fired;

    @Override
    public ColorComponent createComponent() {
        return ColorComponent.with(
            ColorPaletteComponent.with(
                "TestIdPrefix123-",
                (h) -> Optional.of(
                    h.setStylePropertyName(
                        Optional.of(TextStylePropertyName.COLOR)
                    )
                ),
                new FakeColorPaletteComponentContext() {
                    @Override
                    public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                        return null;
                    }

                    @Override
                    public HistoryToken historyToken() {
                        return HistoryToken.cellStyle(
                            SpreadsheetId.with(1),
                            SpreadsheetName.with("SpreadsheetName1"),
                            SpreadsheetSelection.A1.setDefaultAnchor(),
                            Optional.of(TextStylePropertyName.COLOR)
                        );
                    }

                    @Override
                    public SpreadsheetMetadata spreadsheetMetadata() {
                        return METADATA_EN_AU;
                    }
                }
            )
        );
    }

    // class............................................................................................................

    @Override
    public Class<ColorComponent> type() {
        return ColorComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public void testAllMethodsVisibility() {
        throw new UnsupportedOperationException();
    }
}
