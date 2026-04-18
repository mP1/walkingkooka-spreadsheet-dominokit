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

package walkingkooka.spreadsheet.dominokit.value.textstyle.border;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.dominokit.AppContexts;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class BorderTopColorComponentTest extends BorderColorSharedComponentTestCase<BorderTopColorComponent> {

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(COLOR)
                ),
            "BorderTopColorComponent\n" +
                "  TextStylePropertyColorComponent\n" +
                "    Border Top Color\n" +
                "      ColorComponent\n" +
                "        DIV\n" +
                "          style=\"display: block;\"\n" +
                "            DIV\n" +
                "              style=\"color: black; display: inline-block;\"\n" +
                "                \"#000001\"\n" +
                "            DIV\n" +
                "              style=\"background-color: #000001; border: 1px solid black; display: inline-block; height: 20px; margin-bottom: -5px; margin-left: 5px; visibility: visible; width: 20px;\"\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"TestIdPrefix-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000001; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"Black\" DISABLED id=TestIdPrefix-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000002; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"White\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000002] id=TestIdPrefix-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000003; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 3\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000003] id=TestIdPrefix-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000004; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 4\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000004] id=TestIdPrefix-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000005; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 5\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000005] id=TestIdPrefix-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000006; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 6\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000006] id=TestIdPrefix-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000007; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 7\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000007] id=TestIdPrefix-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000008; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 8\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000008] id=TestIdPrefix-color-8-Link\n" +
                "                  TR\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000009; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 9\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000009] id=TestIdPrefix-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00000a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 10\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%2300000a] id=TestIdPrefix-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00000b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 11\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%2300000b] id=TestIdPrefix-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00000c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 12\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%2300000c] id=TestIdPrefix-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00000d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 13\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%2300000d] id=TestIdPrefix-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00000e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 14\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%2300000e] id=TestIdPrefix-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00000f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 15\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%2300000f] id=TestIdPrefix-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000010; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 16\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000010] id=TestIdPrefix-color-16-Link\n" +
                "                  TR\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000011; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 17\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23001] id=TestIdPrefix-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000012; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 18\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000012] id=TestIdPrefix-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000013; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 19\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000013] id=TestIdPrefix-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000014; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 20\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000014] id=TestIdPrefix-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000015; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 21\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000015] id=TestIdPrefix-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000016; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 22\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000016] id=TestIdPrefix-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000017; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 23\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000017] id=TestIdPrefix-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000018; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 24\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000018] id=TestIdPrefix-color-24-Link\n" +
                "                  TR\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000019; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 25\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000019] id=TestIdPrefix-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00001a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 26\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%2300001a] id=TestIdPrefix-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00001b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 27\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%2300001b] id=TestIdPrefix-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00001c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 28\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%2300001c] id=TestIdPrefix-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00001d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 29\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%2300001d] id=TestIdPrefix-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00001e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 30\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%2300001e] id=TestIdPrefix-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00001f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 31\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%2300001f] id=TestIdPrefix-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000020; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 32\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000020] id=TestIdPrefix-color-32-Link\n" +
                "                  TR\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000021; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 33\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000021] id=TestIdPrefix-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000022; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 34\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23002] id=TestIdPrefix-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000023; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 35\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000023] id=TestIdPrefix-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000024; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 36\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000024] id=TestIdPrefix-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000025; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 37\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000025] id=TestIdPrefix-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000026; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 38\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000026] id=TestIdPrefix-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000027; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 39\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000027] id=TestIdPrefix-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000028; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 40\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000028] id=TestIdPrefix-color-40-Link\n" +
                "                  TR\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000029; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 41\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000029] id=TestIdPrefix-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00002a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 42\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%2300002a] id=TestIdPrefix-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00002b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 43\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%2300002b] id=TestIdPrefix-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00002c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 44\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%2300002c] id=TestIdPrefix-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00002d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 45\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%2300002d] id=TestIdPrefix-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00002e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 46\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%2300002e] id=TestIdPrefix-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00002f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 47\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%2300002f] id=TestIdPrefix-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000030; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 48\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000030] id=TestIdPrefix-color-48-Link\n" +
                "                  TR\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000031; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 49\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000031] id=TestIdPrefix-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000032; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 50\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000032] id=TestIdPrefix-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000033; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 51\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23003] id=TestIdPrefix-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000034; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 52\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000034] id=TestIdPrefix-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000035; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 53\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000035] id=TestIdPrefix-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000036; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 54\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000036] id=TestIdPrefix-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000037; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 55\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/%23000037] id=TestIdPrefix-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=TestIdPrefix-color-56-Link\n" +
                "                  TR\n" +
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/border-top-color/save/] id=TestIdPrefix-color-clear-Link\n"
        );
    }

    @Test
    public void testTextStyleValueWatcherOnValueChange() {
        final BorderTopColorComponent component = BorderTopColorComponent.with(
            "Test123-",
            this.createContext(
                HistoryToken.cellStyle(
                    SpreadsheetId.with(1),
                    SpreadsheetName.with("SpreadsheetName111"),
                    SpreadsheetSelection.A1.setDefaultAnchor(),
                    Optional.of(
                        TextStylePropertyName.BORDER_TOP_COLOR
                    )
                )
            )
        );

        component.textStyleValueWatcher()
            .onValue(
                Optional.of(
                    TextStyle.EMPTY.set(
                        TextStylePropertyName.BORDER_TOP_COLOR,
                        COLOR
                    ).set(
                        TextStylePropertyName.TEXT_ALIGN,
                        TextAlign.LEFT
                    )
                )
            );

        this.treePrintAndCheck(
            component,
            "BorderTopColorComponent\n" +
                "  TextStylePropertyColorComponent\n" +
                "    Border Top Color\n" +
                "      ColorComponent\n" +
                "        DIV\n" +
                "          style=\"display: block;\"\n" +
                "            DIV\n" +
                "              style=\"color: black; display: inline-block;\"\n" +
                "                \"#000001\"\n" +
                "            DIV\n" +
                "              style=\"background-color: #000001; border: 1px solid black; display: inline-block; height: 20px; margin-bottom: -5px; margin-left: 5px; visibility: visible; width: 20px;\"\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"Test123-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000001; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"Black\" DISABLED id=Test123-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000002; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"White\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000002] id=Test123-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000003; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 3\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000003] id=Test123-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000004; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 4\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000004] id=Test123-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000005; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 5\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000005] id=Test123-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000006; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 6\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000006] id=Test123-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000007; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 7\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000007] id=Test123-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000008; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 8\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000008] id=Test123-color-8-Link\n" +
                "                  TR\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000009; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 9\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000009] id=Test123-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00000a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 10\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%2300000a] id=Test123-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00000b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 11\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%2300000b] id=Test123-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00000c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 12\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%2300000c] id=Test123-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00000d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 13\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%2300000d] id=Test123-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00000e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 14\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%2300000e] id=Test123-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00000f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 15\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%2300000f] id=Test123-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000010; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 16\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000010] id=Test123-color-16-Link\n" +
                "                  TR\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000011; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 17\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23001] id=Test123-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000012; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 18\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000012] id=Test123-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000013; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 19\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000013] id=Test123-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000014; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 20\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000014] id=Test123-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000015; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 21\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000015] id=Test123-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000016; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 22\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000016] id=Test123-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000017; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 23\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000017] id=Test123-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000018; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 24\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000018] id=Test123-color-24-Link\n" +
                "                  TR\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000019; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 25\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000019] id=Test123-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00001a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 26\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%2300001a] id=Test123-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00001b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 27\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%2300001b] id=Test123-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00001c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 28\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%2300001c] id=Test123-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00001d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 29\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%2300001d] id=Test123-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00001e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 30\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%2300001e] id=Test123-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00001f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 31\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%2300001f] id=Test123-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000020; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 32\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000020] id=Test123-color-32-Link\n" +
                "                  TR\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000021; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 33\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000021] id=Test123-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000022; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 34\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23002] id=Test123-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000023; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 35\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000023] id=Test123-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000024; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 36\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000024] id=Test123-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000025; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 37\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000025] id=Test123-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000026; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 38\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000026] id=Test123-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000027; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 39\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000027] id=Test123-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000028; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 40\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000028] id=Test123-color-40-Link\n" +
                "                  TR\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000029; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 41\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000029] id=Test123-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00002a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 42\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%2300002a] id=Test123-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00002b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 43\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%2300002b] id=Test123-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00002c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 44\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%2300002c] id=Test123-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00002d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 45\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%2300002d] id=Test123-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00002e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 46\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%2300002e] id=Test123-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #00002f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 47\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%2300002f] id=Test123-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000030; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 48\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000030] id=Test123-color-48-Link\n" +
                "                  TR\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000031; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 49\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000031] id=Test123-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000032; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 50\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000032] id=Test123-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000033; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 51\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23003] id=Test123-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000034; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 52\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000034] id=Test123-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000035; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 53\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000035] id=Test123-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000036; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 54\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000036] id=Test123-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"background-color: #000037; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        \"color 55\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/%23000037] id=Test123-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=Test123-color-56-Link\n" +
                "                  TR\n" +
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/border-top-color/save/] id=Test123-color-clear-Link\n"
        );
    }

    @Override
    public BorderTopColorComponent createComponent() {
        return BorderTopColorComponent.with(
            "TestIdPrefix-",
            this.createContext(
                HistoryToken.spreadsheetSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME
                )
            )
        );
    }

    private BorderTopColorComponentContext createContext(final HistoryToken historyToken) {
        return new FakeBorderTopColorComponentContext() {

            @Override
            public HistoryToken historyToken() {
                return this.current;
            }

            private HistoryToken current = historyToken;

            @Override
            public void pushHistoryToken(final HistoryToken token) {
                final HistoryToken previous = this.current;
                this.current = token;
                this.watchers.onHistoryTokenChange(
                    previous,
                    AppContexts.fake()
                );
            }

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return this.watchers.add(watcher);
            }

            private final HistoryTokenWatchers watchers = HistoryTokenWatchers.empty();

            @Override
            public SpreadsheetMetadata spreadsheetMetadata() {
                return SPREADSHEET_METADATA;
            }
        };
    }

    @Override
    public Class<BorderTopColorComponent> type() {
        return BorderTopColorComponent.class;
    }
}
