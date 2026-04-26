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
import walkingkooka.Cast;
import walkingkooka.EmptyTextException;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.color.Color;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.color.SpreadsheetColors;
import walkingkooka.spreadsheet.dominokit.AppContexts;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextStylePropertyColorComponentTest implements TextStylePropertyComponentTesting<HTMLFieldSetElement, Color, TextStylePropertyColorComponent>,
    ComponentLifecycleMatcherTesting,
    ToStringTesting<TextStylePropertyColorComponent>,
    SpreadsheetMetadataTesting {

    private final static String ID_PREFIX = "TestId123-";

    private final static TextStylePropertyName<Color> PROPERTY_NAME = TextStylePropertyName.BACKGROUND_COLOR;

    static {
        SpreadsheetMetadata spreadsheetMetadata = SpreadsheetMetadataTesting.METADATA_EN_AU;

        for(int i = SpreadsheetColors.MIN; i < SpreadsheetColors.MAX; i++) {
            spreadsheetMetadata = spreadsheetMetadata.set(
                SpreadsheetMetadataPropertyName.numberedColor(i),
                Color.fromRgb(i)
            );
        }

        COLOR1 = spreadsheetMetadata.getOrFail(
            SpreadsheetMetadataPropertyName.numberedColor(
                SpreadsheetColors.MIN
            )
        );
        COLOR2 = spreadsheetMetadata.getOrFail(
            SpreadsheetMetadataPropertyName.numberedColor(
                SpreadsheetColors.MIN + 1
            )
        );
        SPREADSHEET_METADATA = spreadsheetMetadata;
    }

    private final static Color COLOR1;
    private final static Color COLOR2;

    private final static SpreadsheetMetadata SPREADSHEET_METADATA;

    private final static TextStylePropertyColorComponentContext CONTEXT = new FakeTextStylePropertyColorComponentContext();

    // with.............................................................................................................

    @Test
    public void testWithNullIdPrefixFails() {
        assertThrows(
            NullPointerException.class,
            () -> TextStylePropertyColorComponent.with(
                null,
                PROPERTY_NAME,
                CONTEXT
            )
        );
    }

    @Test
    public void testWithEmptyIdPrefixFails() {
        assertThrows(
            EmptyTextException.class,
            () -> TextStylePropertyColorComponent.with(
                "",
                PROPERTY_NAME,
                CONTEXT
            )
        );
    }

    @Test
    public void testWithNullPropertyNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> TextStylePropertyColorComponent.with(
                ID_PREFIX,
                null,
                CONTEXT
            )
        );
    }

    @Test
    public void testWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> TextStylePropertyColorComponent.with(
                ID_PREFIX,
                PROPERTY_NAME,
                null
            )
        );
    }

    // treePrint........................................................................................................

    @Test
    public void testTreePrintWhenCellSelectHistoryToken() {
        this.treePrintAndCheck(
            this.createComponent(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    SpreadsheetSelection.A1.setDefaultAnchor()
                )
            ),
            "TextStylePropertyColorComponent\n" +
                "  ColorComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [] icons=mdi-close-circle, mdi-palette id=TestId123-backgroundColor-TextBox REQUIRED\n" +
                "          innerRight\n" +
                "            ColorBoxComponent\n" +
                "        Errors\n" +
                "          Empty \"text\"\n" +
                "      ColorPaletteComponent\n" +
                "        TABLE\n" +
                "          id=\"TestId123-Table\" className=dui dui-menu-item\n" +
                "            TBODY\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000001; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"Black\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000001] id=TestId123-color-1-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000002; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"White\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000002] id=TestId123-color-2-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000003; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 3\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000003] id=TestId123-color-3-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000004; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 4\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000004] id=TestId123-color-4-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000005; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 5\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000005] id=TestId123-color-5-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000006; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 6\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000006] id=TestId123-color-6-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000007; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 7\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000007] id=TestId123-color-7-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000008; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 8\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000008] id=TestId123-color-8-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000009; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 9\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000009] id=TestId123-color-9-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 10\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%2300000a] id=TestId123-color-10-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 11\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%2300000b] id=TestId123-color-11-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 12\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%2300000c] id=TestId123-color-12-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 13\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%2300000d] id=TestId123-color-13-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 14\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%2300000e] id=TestId123-color-14-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 15\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%2300000f] id=TestId123-color-15-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000010; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 16\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000010] id=TestId123-color-16-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000011; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 17\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23001] id=TestId123-color-17-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000012; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 18\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000012] id=TestId123-color-18-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000013; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 19\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000013] id=TestId123-color-19-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000014; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 20\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000014] id=TestId123-color-20-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000015; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 21\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000015] id=TestId123-color-21-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000016; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 22\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000016] id=TestId123-color-22-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000017; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 23\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000017] id=TestId123-color-23-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000018; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 24\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000018] id=TestId123-color-24-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000019; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 25\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000019] id=TestId123-color-25-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 26\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%2300001a] id=TestId123-color-26-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 27\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%2300001b] id=TestId123-color-27-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 28\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%2300001c] id=TestId123-color-28-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 29\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%2300001d] id=TestId123-color-29-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 30\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%2300001e] id=TestId123-color-30-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 31\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%2300001f] id=TestId123-color-31-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000020; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 32\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000020] id=TestId123-color-32-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000021; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 33\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000021] id=TestId123-color-33-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000022; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 34\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23002] id=TestId123-color-34-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000023; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 35\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000023] id=TestId123-color-35-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000024; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 36\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000024] id=TestId123-color-36-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000025; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 37\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000025] id=TestId123-color-37-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000026; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 38\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000026] id=TestId123-color-38-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000027; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 39\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000027] id=TestId123-color-39-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000028; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 40\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000028] id=TestId123-color-40-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000029; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 41\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000029] id=TestId123-color-41-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 42\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%2300002a] id=TestId123-color-42-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 43\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%2300002b] id=TestId123-color-43-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 44\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%2300002c] id=TestId123-color-44-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 45\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%2300002d] id=TestId123-color-45-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 46\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%2300002e] id=TestId123-color-46-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 47\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%2300002f] id=TestId123-color-47-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000030; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 48\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000030] id=TestId123-color-48-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000031; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 49\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000031] id=TestId123-color-49-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000032; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 50\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000032] id=TestId123-color-50-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000033; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 51\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23003] id=TestId123-color-51-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000034; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 52\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000034] id=TestId123-color-52-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000035; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 53\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000035] id=TestId123-color-53-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000036; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 54\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000036] id=TestId123-color-54-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000037; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 55\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/%23000037] id=TestId123-color-55-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=TestId123-color-56-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                    \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/] id=TestId123-color-clear-Link\n"
        );
    }

    @Test
    public void testTreePrintWhenColumnSelectHistoryToken() {
        this.treePrintAndCheck(
            this.createComponent(
                HistoryToken.columnSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    SpreadsheetSelection.A1.column()
                        .setDefaultAnchor()
                )
            ),
            "TextStylePropertyColorComponent\n" +
                "  ColorComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [] icons=mdi-close-circle, mdi-palette id=TestId123-backgroundColor-TextBox REQUIRED\n" +
                "          innerRight\n" +
                "            ColorBoxComponent\n" +
                "        Errors\n" +
                "          Empty \"text\"\n" +
                "      ColorPaletteComponent\n" +
                "        TABLE\n" +
                "          id=\"TestId123-Table\" className=dui dui-menu-item\n" +
                "            TBODY\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000001; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"Black\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-1-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000002; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"White\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-2-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000003; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 3\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-3-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000004; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 4\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-4-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000005; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 5\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-5-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000006; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 6\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-6-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000007; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 7\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-7-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000008; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 8\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-8-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000009; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 9\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-9-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 10\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-10-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 11\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-11-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 12\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-12-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 13\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-13-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 14\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-14-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 15\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-15-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000010; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 16\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-16-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000011; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 17\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-17-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000012; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 18\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-18-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000013; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 19\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-19-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000014; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 20\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-20-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000015; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 21\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-21-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000016; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 22\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-22-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000017; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 23\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-23-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000018; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 24\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-24-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000019; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 25\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-25-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 26\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-26-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 27\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-27-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 28\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-28-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 29\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-29-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 30\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-30-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 31\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-31-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000020; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 32\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-32-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000021; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 33\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-33-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000022; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 34\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-34-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000023; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 35\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-35-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000024; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 36\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-36-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000025; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 37\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-37-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000026; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 38\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-38-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000027; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 39\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-39-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000028; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 40\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-40-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000029; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 41\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-41-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 42\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-42-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 43\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-43-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 44\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-44-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 45\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-45-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 46\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-46-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 47\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-47-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000030; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 48\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-48-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000031; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 49\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-49-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000032; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 50\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-50-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000033; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 51\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-51-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000034; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 52\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-52-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000035; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 53\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-53-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000036; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 54\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-54-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000037; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 55\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-55-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=TestId123-color-56-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                    \"Clear\" [#/1/SpreadsheetName1/column/A] id=TestId123-color-clear-Link\n"
        );
    }

    @Test
    public void testTreePrintWhenSpreadsheetSelectHistoryToken() {
        this.treePrintAndCheck(
            this.createComponent(
                HistoryToken.spreadsheetSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME
                )
            ),
            "TextStylePropertyColorComponent\n" +
                "  ColorComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [] icons=mdi-close-circle, mdi-palette id=TestId123-backgroundColor-TextBox REQUIRED\n" +
                "          innerRight\n" +
                "            ColorBoxComponent\n" +
                "        Errors\n" +
                "          Empty \"text\"\n" +
                "      ColorPaletteComponent\n" +
                "        TABLE\n" +
                "          id=\"TestId123-Table\" className=dui dui-menu-item\n" +
                "            TBODY\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000001; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"Black\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000001] id=TestId123-color-1-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000002; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"White\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000002] id=TestId123-color-2-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000003; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 3\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000003] id=TestId123-color-3-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000004; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 4\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000004] id=TestId123-color-4-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000005; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 5\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000005] id=TestId123-color-5-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000006; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 6\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000006] id=TestId123-color-6-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000007; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 7\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000007] id=TestId123-color-7-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000008; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 8\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000008] id=TestId123-color-8-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000009; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 9\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000009] id=TestId123-color-9-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 10\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000a] id=TestId123-color-10-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 11\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000b] id=TestId123-color-11-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 12\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000c] id=TestId123-color-12-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 13\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000d] id=TestId123-color-13-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 14\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000e] id=TestId123-color-14-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 15\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000f] id=TestId123-color-15-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000010; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 16\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000010] id=TestId123-color-16-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000011; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 17\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23001] id=TestId123-color-17-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000012; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 18\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000012] id=TestId123-color-18-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000013; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 19\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000013] id=TestId123-color-19-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000014; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 20\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000014] id=TestId123-color-20-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000015; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 21\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000015] id=TestId123-color-21-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000016; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 22\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000016] id=TestId123-color-22-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000017; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 23\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000017] id=TestId123-color-23-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000018; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 24\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000018] id=TestId123-color-24-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000019; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 25\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000019] id=TestId123-color-25-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 26\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001a] id=TestId123-color-26-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 27\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001b] id=TestId123-color-27-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 28\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001c] id=TestId123-color-28-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 29\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001d] id=TestId123-color-29-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 30\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001e] id=TestId123-color-30-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 31\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001f] id=TestId123-color-31-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000020; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 32\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000020] id=TestId123-color-32-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000021; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 33\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000021] id=TestId123-color-33-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000022; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 34\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23002] id=TestId123-color-34-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000023; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 35\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000023] id=TestId123-color-35-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000024; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 36\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000024] id=TestId123-color-36-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000025; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 37\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000025] id=TestId123-color-37-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000026; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 38\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000026] id=TestId123-color-38-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000027; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 39\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000027] id=TestId123-color-39-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000028; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 40\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000028] id=TestId123-color-40-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000029; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 41\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000029] id=TestId123-color-41-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 42\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002a] id=TestId123-color-42-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 43\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002b] id=TestId123-color-43-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 44\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002c] id=TestId123-color-44-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 45\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002d] id=TestId123-color-45-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 46\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002e] id=TestId123-color-46-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 47\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002f] id=TestId123-color-47-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000030; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 48\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000030] id=TestId123-color-48-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000031; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 49\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000031] id=TestId123-color-49-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000032; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 50\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000032] id=TestId123-color-50-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000033; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 51\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23003] id=TestId123-color-51-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000034; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 52\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000034] id=TestId123-color-52-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000035; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 53\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000035] id=TestId123-color-53-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000036; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 54\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000036] id=TestId123-color-54-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000037; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 55\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000037] id=TestId123-color-55-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=TestId123-color-56-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                    \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/] id=TestId123-color-clear-Link\n"
        );
    }

    @Test
    public void testTreePrintWhenCellSelectHistoryTokenChange() {
        final TextStylePropertyColorComponentContext context = this.createContext(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );

        final TextStylePropertyColorComponent anchor = this.createComponent(context);

        context.pushHistoryToken(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.parseCell("B2")
                    .setDefaultAnchor()
            )
        );

        this.treePrintAndCheck(
            anchor,
            "TextStylePropertyColorComponent\n" +
                "  ColorComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [] icons=mdi-close-circle, mdi-palette id=TestId123-backgroundColor-TextBox REQUIRED\n" +
                "          innerRight\n" +
                "            ColorBoxComponent\n" +
                "        Errors\n" +
                "          Empty \"text\"\n" +
                "      ColorPaletteComponent\n" +
                "        TABLE\n" +
                "          id=\"TestId123-Table\" className=dui dui-menu-item\n" +
                "            TBODY\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000001; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"Black\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000001] id=TestId123-color-1-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000002; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"White\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000002] id=TestId123-color-2-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000003; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 3\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000003] id=TestId123-color-3-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000004; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 4\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000004] id=TestId123-color-4-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000005; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 5\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000005] id=TestId123-color-5-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000006; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 6\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000006] id=TestId123-color-6-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000007; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 7\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000007] id=TestId123-color-7-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000008; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 8\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000008] id=TestId123-color-8-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000009; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 9\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000009] id=TestId123-color-9-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 10\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300000a] id=TestId123-color-10-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 11\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300000b] id=TestId123-color-11-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 12\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300000c] id=TestId123-color-12-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 13\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300000d] id=TestId123-color-13-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 14\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300000e] id=TestId123-color-14-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 15\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300000f] id=TestId123-color-15-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000010; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 16\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000010] id=TestId123-color-16-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000011; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 17\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23001] id=TestId123-color-17-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000012; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 18\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000012] id=TestId123-color-18-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000013; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 19\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000013] id=TestId123-color-19-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000014; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 20\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000014] id=TestId123-color-20-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000015; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 21\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000015] id=TestId123-color-21-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000016; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 22\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000016] id=TestId123-color-22-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000017; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 23\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000017] id=TestId123-color-23-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000018; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 24\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000018] id=TestId123-color-24-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000019; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 25\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000019] id=TestId123-color-25-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 26\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300001a] id=TestId123-color-26-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 27\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300001b] id=TestId123-color-27-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 28\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300001c] id=TestId123-color-28-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 29\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300001d] id=TestId123-color-29-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 30\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300001e] id=TestId123-color-30-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 31\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300001f] id=TestId123-color-31-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000020; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 32\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000020] id=TestId123-color-32-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000021; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 33\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000021] id=TestId123-color-33-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000022; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 34\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23002] id=TestId123-color-34-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000023; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 35\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000023] id=TestId123-color-35-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000024; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 36\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000024] id=TestId123-color-36-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000025; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 37\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000025] id=TestId123-color-37-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000026; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 38\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000026] id=TestId123-color-38-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000027; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 39\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000027] id=TestId123-color-39-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000028; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 40\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000028] id=TestId123-color-40-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000029; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 41\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000029] id=TestId123-color-41-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 42\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300002a] id=TestId123-color-42-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 43\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300002b] id=TestId123-color-43-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 44\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300002c] id=TestId123-color-44-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 45\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300002d] id=TestId123-color-45-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 46\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300002e] id=TestId123-color-46-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 47\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300002f] id=TestId123-color-47-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000030; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 48\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000030] id=TestId123-color-48-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000031; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 49\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000031] id=TestId123-color-49-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000032; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 50\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000032] id=TestId123-color-50-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000033; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 51\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23003] id=TestId123-color-51-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000034; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 52\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000034] id=TestId123-color-52-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000035; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 53\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000035] id=TestId123-color-53-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000036; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 54\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000036] id=TestId123-color-54-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000037; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 55\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000037] id=TestId123-color-55-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=TestId123-color-56-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                    \"Clear\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/] id=TestId123-color-clear-Link\n"
        );
    }

    // setValue.........................................................................................................

    @Test
    public void testSetValue() {
        final TextStylePropertyColorComponentContext context = this.createContext(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );

        final TextStylePropertyColorComponent component = this.createComponent(context);

        context.pushHistoryToken(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.parseCell("B2")
                    .setDefaultAnchor()
            )
        );

        component.setValue(
            Optional.of(COLOR1)
        );

        this.treePrintAndCheck(
            component,
            "TextStylePropertyColorComponent\n" +
                "  ColorComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [#000001] icons=mdi-close-circle, mdi-palette id=TestId123-backgroundColor-TextBox REQUIRED\n" +
                "          innerRight\n" +
                "            ColorBoxComponent\n" +
                "              #000001\n" +
                "      ColorPaletteComponent\n" +
                "        TABLE\n" +
                "          id=\"TestId123-Table\" className=dui dui-menu-item\n" +
                "            TBODY\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000001; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"Black\" DISABLED id=TestId123-color-1-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000002; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"White\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000002] id=TestId123-color-2-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000003; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 3\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000003] id=TestId123-color-3-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000004; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 4\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000004] id=TestId123-color-4-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000005; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 5\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000005] id=TestId123-color-5-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000006; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 6\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000006] id=TestId123-color-6-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000007; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 7\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000007] id=TestId123-color-7-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000008; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 8\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000008] id=TestId123-color-8-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000009; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 9\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000009] id=TestId123-color-9-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 10\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300000a] id=TestId123-color-10-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 11\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300000b] id=TestId123-color-11-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 12\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300000c] id=TestId123-color-12-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 13\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300000d] id=TestId123-color-13-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 14\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300000e] id=TestId123-color-14-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 15\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300000f] id=TestId123-color-15-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000010; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 16\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000010] id=TestId123-color-16-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000011; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 17\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23001] id=TestId123-color-17-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000012; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 18\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000012] id=TestId123-color-18-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000013; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 19\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000013] id=TestId123-color-19-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000014; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 20\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000014] id=TestId123-color-20-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000015; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 21\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000015] id=TestId123-color-21-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000016; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 22\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000016] id=TestId123-color-22-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000017; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 23\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000017] id=TestId123-color-23-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000018; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 24\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000018] id=TestId123-color-24-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000019; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 25\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000019] id=TestId123-color-25-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 26\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300001a] id=TestId123-color-26-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 27\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300001b] id=TestId123-color-27-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 28\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300001c] id=TestId123-color-28-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 29\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300001d] id=TestId123-color-29-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 30\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300001e] id=TestId123-color-30-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 31\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300001f] id=TestId123-color-31-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000020; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 32\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000020] id=TestId123-color-32-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000021; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 33\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000021] id=TestId123-color-33-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000022; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 34\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23002] id=TestId123-color-34-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000023; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 35\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000023] id=TestId123-color-35-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000024; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 36\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000024] id=TestId123-color-36-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000025; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 37\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000025] id=TestId123-color-37-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000026; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 38\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000026] id=TestId123-color-38-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000027; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 39\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000027] id=TestId123-color-39-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000028; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 40\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000028] id=TestId123-color-40-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000029; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 41\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000029] id=TestId123-color-41-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 42\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300002a] id=TestId123-color-42-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 43\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300002b] id=TestId123-color-43-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 44\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300002c] id=TestId123-color-44-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 45\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300002d] id=TestId123-color-45-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 46\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300002e] id=TestId123-color-46-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 47\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300002f] id=TestId123-color-47-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000030; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 48\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000030] id=TestId123-color-48-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000031; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 49\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000031] id=TestId123-color-49-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000032; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 50\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000032] id=TestId123-color-50-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000033; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 51\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23003] id=TestId123-color-51-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000034; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 52\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000034] id=TestId123-color-52-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000035; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 53\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000035] id=TestId123-color-53-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000036; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 54\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000036] id=TestId123-color-54-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000037; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 55\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000037] id=TestId123-color-55-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=TestId123-color-56-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                    \"Clear\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/] id=TestId123-color-clear-Link\n"
        );
    }

    @Test
    public void testSetValueTwice() {
        final TextStylePropertyColorComponentContext context = this.createContext(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );

        final TextStylePropertyColorComponent component = this.createComponent(context);

        context.pushHistoryToken(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.parseCell("B2")
                    .setDefaultAnchor()
            )
        );

        component.setValue(
            Optional.of(COLOR1)
        );

        component.setValue(
            Optional.of(COLOR2)
        );

        this.treePrintAndCheck(
            component,
            "TextStylePropertyColorComponent\n" +
                "  ColorComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [#000002] icons=mdi-close-circle, mdi-palette id=TestId123-backgroundColor-TextBox REQUIRED\n" +
                "          innerRight\n" +
                "            ColorBoxComponent\n" +
                "              #000002\n" +
                "      ColorPaletteComponent\n" +
                "        TABLE\n" +
                "          id=\"TestId123-Table\" className=dui dui-menu-item\n" +
                "            TBODY\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000001; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"Black\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000001] id=TestId123-color-1-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000002; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"White\" DISABLED id=TestId123-color-2-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000003; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 3\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000003] id=TestId123-color-3-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000004; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 4\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000004] id=TestId123-color-4-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000005; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 5\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000005] id=TestId123-color-5-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000006; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 6\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000006] id=TestId123-color-6-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000007; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 7\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000007] id=TestId123-color-7-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000008; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 8\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000008] id=TestId123-color-8-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000009; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 9\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000009] id=TestId123-color-9-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 10\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300000a] id=TestId123-color-10-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 11\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300000b] id=TestId123-color-11-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 12\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300000c] id=TestId123-color-12-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 13\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300000d] id=TestId123-color-13-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 14\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300000e] id=TestId123-color-14-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 15\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300000f] id=TestId123-color-15-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000010; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 16\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000010] id=TestId123-color-16-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000011; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 17\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23001] id=TestId123-color-17-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000012; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 18\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000012] id=TestId123-color-18-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000013; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 19\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000013] id=TestId123-color-19-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000014; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 20\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000014] id=TestId123-color-20-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000015; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 21\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000015] id=TestId123-color-21-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000016; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 22\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000016] id=TestId123-color-22-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000017; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 23\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000017] id=TestId123-color-23-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000018; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 24\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000018] id=TestId123-color-24-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000019; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 25\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000019] id=TestId123-color-25-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 26\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300001a] id=TestId123-color-26-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 27\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300001b] id=TestId123-color-27-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 28\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300001c] id=TestId123-color-28-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 29\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300001d] id=TestId123-color-29-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 30\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300001e] id=TestId123-color-30-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 31\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300001f] id=TestId123-color-31-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000020; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 32\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000020] id=TestId123-color-32-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000021; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 33\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000021] id=TestId123-color-33-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000022; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 34\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23002] id=TestId123-color-34-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000023; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 35\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000023] id=TestId123-color-35-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000024; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 36\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000024] id=TestId123-color-36-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000025; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 37\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000025] id=TestId123-color-37-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000026; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 38\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000026] id=TestId123-color-38-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000027; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 39\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000027] id=TestId123-color-39-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000028; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 40\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000028] id=TestId123-color-40-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000029; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 41\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000029] id=TestId123-color-41-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 42\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300002a] id=TestId123-color-42-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 43\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300002b] id=TestId123-color-43-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 44\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300002c] id=TestId123-color-44-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 45\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300002d] id=TestId123-color-45-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 46\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300002e] id=TestId123-color-46-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 47\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300002f] id=TestId123-color-47-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000030; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 48\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000030] id=TestId123-color-48-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000031; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 49\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000031] id=TestId123-color-49-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000032; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 50\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000032] id=TestId123-color-50-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000033; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 51\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23003] id=TestId123-color-51-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000034; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 52\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000034] id=TestId123-color-52-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000035; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 53\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000035] id=TestId123-color-53-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000036; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 54\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000036] id=TestId123-color-54-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000037; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 55\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000037] id=TestId123-color-55-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=TestId123-color-56-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                    \"Clear\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/] id=TestId123-color-clear-Link\n"
        );
    }

    // addValueWatcher..................................................................................................

    @Test
    public void testAddValueWatcher() {
        final TextStylePropertyColorComponentContext context = this.createContext(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );

        final TextStylePropertyColorComponent component = this.createComponent(context);

        this.fired = null;
        component.addValueWatcher(
            new ValueWatcher<>() {
                @Override
                public void onValue(final Optional<Color> value) {
                    TextStylePropertyColorComponentTest.this.fired = value.orElse(null);
                }
            }
        );

        component.setValue(
            Optional.of(COLOR1)
        );

        this.checkEquals(
            COLOR1,
            this.fired,
            "fired"
        );
    }

    private Color fired;

    // HasName..........................................................................................................

    @Test
    public void testHasName() {
        this.nameAndCheck(
            this.createComponent(),
            PROPERTY_NAME
        );
    }

    // textStyleValueWatcher............................................................................................

    @Test
    public void testTextStyleValueWatcherOnValueChange() {
        final TextStylePropertyColorComponentContext context = this.createContext(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );

        final TextStylePropertyColorComponent component = this.createComponent(context);

        context.pushHistoryToken(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.parseCell("B2")
                    .setDefaultAnchor()
            )
        );

        component.setValue(
            Optional.of(COLOR1)
        );

        component.textStyleValueWatcher()
            .onValue(
                Optional.of(
                    TextStyle.EMPTY.set(
                        PROPERTY_NAME,
                        COLOR2
                    )
                )
            );

        this.treePrintAndCheck(
            component,
            "TextStylePropertyColorComponent\n" +
                "  ColorComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [#000002] icons=mdi-close-circle, mdi-palette id=TestId123-backgroundColor-TextBox REQUIRED\n" +
                "          innerRight\n" +
                "            ColorBoxComponent\n" +
                "              #000002\n" +
                "      ColorPaletteComponent\n" +
                "        TABLE\n" +
                "          id=\"TestId123-Table\" className=dui dui-menu-item\n" +
                "            TBODY\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000001; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"Black\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000001] id=TestId123-color-1-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000002; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"White\" DISABLED id=TestId123-color-2-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000003; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 3\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000003] id=TestId123-color-3-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000004; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 4\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000004] id=TestId123-color-4-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000005; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 5\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000005] id=TestId123-color-5-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000006; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 6\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000006] id=TestId123-color-6-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000007; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 7\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000007] id=TestId123-color-7-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000008; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 8\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000008] id=TestId123-color-8-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000009; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 9\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000009] id=TestId123-color-9-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 10\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300000a] id=TestId123-color-10-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 11\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300000b] id=TestId123-color-11-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 12\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300000c] id=TestId123-color-12-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 13\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300000d] id=TestId123-color-13-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 14\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300000e] id=TestId123-color-14-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 15\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300000f] id=TestId123-color-15-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000010; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 16\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000010] id=TestId123-color-16-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000011; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 17\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23001] id=TestId123-color-17-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000012; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 18\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000012] id=TestId123-color-18-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000013; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 19\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000013] id=TestId123-color-19-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000014; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 20\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000014] id=TestId123-color-20-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000015; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 21\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000015] id=TestId123-color-21-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000016; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 22\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000016] id=TestId123-color-22-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000017; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 23\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000017] id=TestId123-color-23-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000018; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 24\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000018] id=TestId123-color-24-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000019; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 25\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000019] id=TestId123-color-25-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 26\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300001a] id=TestId123-color-26-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 27\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300001b] id=TestId123-color-27-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 28\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300001c] id=TestId123-color-28-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 29\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300001d] id=TestId123-color-29-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 30\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300001e] id=TestId123-color-30-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 31\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300001f] id=TestId123-color-31-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000020; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 32\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000020] id=TestId123-color-32-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000021; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 33\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000021] id=TestId123-color-33-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000022; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 34\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23002] id=TestId123-color-34-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000023; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 35\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000023] id=TestId123-color-35-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000024; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 36\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000024] id=TestId123-color-36-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000025; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 37\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000025] id=TestId123-color-37-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000026; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 38\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000026] id=TestId123-color-38-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000027; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 39\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000027] id=TestId123-color-39-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000028; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 40\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000028] id=TestId123-color-40-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000029; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 41\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000029] id=TestId123-color-41-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 42\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300002a] id=TestId123-color-42-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 43\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300002b] id=TestId123-color-43-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 44\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300002c] id=TestId123-color-44-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 45\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300002d] id=TestId123-color-45-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 46\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300002e] id=TestId123-color-46-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 47\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300002f] id=TestId123-color-47-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000030; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 48\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000030] id=TestId123-color-48-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000031; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 49\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000031] id=TestId123-color-49-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000032; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 50\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000032] id=TestId123-color-50-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000033; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 51\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23003] id=TestId123-color-51-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000034; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 52\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000034] id=TestId123-color-52-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000035; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 53\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000035] id=TestId123-color-53-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000036; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 54\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000036] id=TestId123-color-54-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000037; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 55\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000037] id=TestId123-color-55-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=TestId123-color-56-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                    \"Clear\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/] id=TestId123-color-clear-Link\n"
        );
    }

    @Test
    public void testTextStyleValueWatcherOnValueChangeCleared() {
        final TextStylePropertyColorComponentContext context = this.createContext(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );

        final TextStylePropertyColorComponent component = this.createComponent(context);

        context.pushHistoryToken(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.parseCell("B2")
                    .setDefaultAnchor()
            )
        );

        component.setValue(
            Optional.of(COLOR1)
        );

        component.textStyleValueWatcher()
            .onValue(
                Optional.of(TextStyle.EMPTY)
            );

        this.treePrintAndCheck(
            component,
            "TextStylePropertyColorComponent\n" +
                "  ColorComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [] icons=mdi-close-circle, mdi-palette id=TestId123-backgroundColor-TextBox REQUIRED\n" +
                "          innerRight\n" +
                "            ColorBoxComponent\n" +
                "        Errors\n" +
                "          Empty \"text\"\n" +
                "      ColorPaletteComponent\n" +
                "        TABLE\n" +
                "          id=\"TestId123-Table\" className=dui dui-menu-item\n" +
                "            TBODY\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000001; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"Black\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000001] id=TestId123-color-1-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000002; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"White\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000002] id=TestId123-color-2-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000003; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 3\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000003] id=TestId123-color-3-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000004; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 4\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000004] id=TestId123-color-4-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000005; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 5\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000005] id=TestId123-color-5-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000006; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 6\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000006] id=TestId123-color-6-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000007; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 7\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000007] id=TestId123-color-7-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000008; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 8\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000008] id=TestId123-color-8-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000009; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 9\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000009] id=TestId123-color-9-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 10\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300000a] id=TestId123-color-10-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 11\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300000b] id=TestId123-color-11-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 12\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300000c] id=TestId123-color-12-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 13\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300000d] id=TestId123-color-13-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 14\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300000e] id=TestId123-color-14-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 15\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300000f] id=TestId123-color-15-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000010; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 16\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000010] id=TestId123-color-16-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000011; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 17\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23001] id=TestId123-color-17-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000012; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 18\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000012] id=TestId123-color-18-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000013; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 19\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000013] id=TestId123-color-19-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000014; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 20\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000014] id=TestId123-color-20-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000015; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 21\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000015] id=TestId123-color-21-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000016; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 22\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000016] id=TestId123-color-22-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000017; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 23\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000017] id=TestId123-color-23-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000018; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 24\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000018] id=TestId123-color-24-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000019; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 25\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000019] id=TestId123-color-25-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 26\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300001a] id=TestId123-color-26-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 27\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300001b] id=TestId123-color-27-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 28\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300001c] id=TestId123-color-28-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 29\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300001d] id=TestId123-color-29-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 30\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300001e] id=TestId123-color-30-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 31\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300001f] id=TestId123-color-31-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000020; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 32\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000020] id=TestId123-color-32-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000021; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 33\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000021] id=TestId123-color-33-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000022; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 34\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23002] id=TestId123-color-34-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000023; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 35\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000023] id=TestId123-color-35-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000024; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 36\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000024] id=TestId123-color-36-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000025; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 37\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000025] id=TestId123-color-37-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000026; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 38\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000026] id=TestId123-color-38-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000027; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 39\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000027] id=TestId123-color-39-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000028; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 40\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000028] id=TestId123-color-40-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000029; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 41\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000029] id=TestId123-color-41-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 42\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300002a] id=TestId123-color-42-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 43\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300002b] id=TestId123-color-43-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 44\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300002c] id=TestId123-color-44-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 45\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300002d] id=TestId123-color-45-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 46\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300002e] id=TestId123-color-46-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 47\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%2300002f] id=TestId123-color-47-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000030; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 48\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000030] id=TestId123-color-48-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000031; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 49\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000031] id=TestId123-color-49-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000032; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 50\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000032] id=TestId123-color-50-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000033; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 51\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23003] id=TestId123-color-51-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000034; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 52\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000034] id=TestId123-color-52-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000035; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 53\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000035] id=TestId123-color-53-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000036; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 54\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000036] id=TestId123-color-54-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000037; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 55\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/%23000037] id=TestId123-color-55-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=TestId123-color-56-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                    \"Clear\" [#/1/SpreadsheetName1/cell/B2/style/background-color/save/] id=TestId123-color-clear-Link\n"
        );
    }

    @Override
    public TextStylePropertyColorComponent createComponent() {
        return this.createComponent(
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            )
        );
    }

    private TextStylePropertyColorComponent createComponent(final HistoryToken historyToken) {
        return this.createComponent(
            this.createContext(historyToken)
        );
    }

    private TextStylePropertyColorComponent createComponent(final TextStylePropertyColorComponentContext context) {
        return TextStylePropertyColorComponent.with(
            ID_PREFIX,
            PROPERTY_NAME,
            context
        );
    }

    private TextStylePropertyColorComponentContext createContext(final HistoryToken historyToken) {
        return new FakeTextStylePropertyColorComponentContext() {

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

    // printTree........................................................................................................

    @Test
    public void testPrintTree() {
        this.treePrintAndCheck(
            this.createComponent(),
            "TextStylePropertyColorComponent\n" +
                "  ColorComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [] icons=mdi-close-circle, mdi-palette id=TestId123-backgroundColor-TextBox REQUIRED\n" +
                "          innerRight\n" +
                "            ColorBoxComponent\n" +
                "        Errors\n" +
                "          Empty \"text\"\n" +
                "      ColorPaletteComponent\n" +
                "        TABLE\n" +
                "          id=\"TestId123-Table\" className=dui dui-menu-item\n" +
                "            TBODY\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000001; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"Black\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000001] id=TestId123-color-1-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000002; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"White\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000002] id=TestId123-color-2-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000003; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 3\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000003] id=TestId123-color-3-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000004; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 4\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000004] id=TestId123-color-4-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000005; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 5\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000005] id=TestId123-color-5-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000006; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 6\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000006] id=TestId123-color-6-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000007; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 7\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000007] id=TestId123-color-7-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000008; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 8\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000008] id=TestId123-color-8-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000009; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 9\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000009] id=TestId123-color-9-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 10\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000a] id=TestId123-color-10-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 11\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000b] id=TestId123-color-11-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 12\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000c] id=TestId123-color-12-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 13\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000d] id=TestId123-color-13-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 14\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000e] id=TestId123-color-14-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 15\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000f] id=TestId123-color-15-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000010; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 16\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000010] id=TestId123-color-16-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000011; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 17\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23001] id=TestId123-color-17-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000012; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 18\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000012] id=TestId123-color-18-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000013; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 19\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000013] id=TestId123-color-19-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000014; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 20\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000014] id=TestId123-color-20-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000015; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 21\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000015] id=TestId123-color-21-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000016; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 22\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000016] id=TestId123-color-22-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000017; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 23\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000017] id=TestId123-color-23-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000018; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 24\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000018] id=TestId123-color-24-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000019; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 25\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000019] id=TestId123-color-25-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 26\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001a] id=TestId123-color-26-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 27\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001b] id=TestId123-color-27-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 28\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001c] id=TestId123-color-28-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 29\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001d] id=TestId123-color-29-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 30\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001e] id=TestId123-color-30-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 31\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001f] id=TestId123-color-31-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000020; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 32\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000020] id=TestId123-color-32-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000021; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 33\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000021] id=TestId123-color-33-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000022; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 34\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23002] id=TestId123-color-34-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000023; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 35\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000023] id=TestId123-color-35-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000024; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 36\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000024] id=TestId123-color-36-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000025; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 37\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000025] id=TestId123-color-37-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000026; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 38\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000026] id=TestId123-color-38-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000027; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 39\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000027] id=TestId123-color-39-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000028; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 40\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000028] id=TestId123-color-40-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000029; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 41\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000029] id=TestId123-color-41-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 42\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002a] id=TestId123-color-42-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 43\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002b] id=TestId123-color-43-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 44\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002c] id=TestId123-color-44-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 45\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002d] id=TestId123-color-45-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 46\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002e] id=TestId123-color-46-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 47\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002f] id=TestId123-color-47-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000030; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 48\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000030] id=TestId123-color-48-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000031; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 49\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000031] id=TestId123-color-49-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000032; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 50\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000032] id=TestId123-color-50-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000033; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 51\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23003] id=TestId123-color-51-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000034; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 52\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000034] id=TestId123-color-52-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000035; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 53\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000035] id=TestId123-color-53-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000036; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 54\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000036] id=TestId123-color-54-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000037; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 55\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000037] id=TestId123-color-55-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=TestId123-color-56-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                    \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/] id=TestId123-color-clear-Link\n"
        );
    }

    @Test
    public void testSetLabelFromPropertyName() {
        this.treePrintAndCheck(
            this.createComponent()
                .setLabelFromPropertyName(),
            "TextStylePropertyColorComponent\n" +
                "  ColorComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        Background Color [] icons=mdi-close-circle, mdi-palette id=TestId123-backgroundColor-TextBox REQUIRED\n" +
                "          innerRight\n" +
                "            ColorBoxComponent\n" +
                "        Errors\n" +
                "          Empty \"text\"\n" +
                "      ColorPaletteComponent\n" +
                "        TABLE\n" +
                "          id=\"TestId123-Table\" className=dui dui-menu-item\n" +
                "            TBODY\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000001; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"Black\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000001] id=TestId123-color-1-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000002; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"White\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000002] id=TestId123-color-2-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000003; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 3\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000003] id=TestId123-color-3-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000004; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 4\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000004] id=TestId123-color-4-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000005; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 5\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000005] id=TestId123-color-5-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000006; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 6\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000006] id=TestId123-color-6-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000007; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 7\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000007] id=TestId123-color-7-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000008; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 8\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000008] id=TestId123-color-8-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000009; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 9\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000009] id=TestId123-color-9-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 10\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000a] id=TestId123-color-10-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 11\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000b] id=TestId123-color-11-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 12\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000c] id=TestId123-color-12-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 13\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000d] id=TestId123-color-13-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 14\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000e] id=TestId123-color-14-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 15\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000f] id=TestId123-color-15-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000010; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 16\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000010] id=TestId123-color-16-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000011; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 17\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23001] id=TestId123-color-17-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000012; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 18\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000012] id=TestId123-color-18-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000013; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 19\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000013] id=TestId123-color-19-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000014; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 20\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000014] id=TestId123-color-20-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000015; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 21\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000015] id=TestId123-color-21-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000016; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 22\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000016] id=TestId123-color-22-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000017; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 23\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000017] id=TestId123-color-23-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000018; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 24\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000018] id=TestId123-color-24-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000019; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 25\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000019] id=TestId123-color-25-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 26\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001a] id=TestId123-color-26-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 27\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001b] id=TestId123-color-27-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 28\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001c] id=TestId123-color-28-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 29\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001d] id=TestId123-color-29-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 30\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001e] id=TestId123-color-30-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 31\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001f] id=TestId123-color-31-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000020; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 32\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000020] id=TestId123-color-32-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000021; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 33\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000021] id=TestId123-color-33-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000022; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 34\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23002] id=TestId123-color-34-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000023; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 35\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000023] id=TestId123-color-35-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000024; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 36\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000024] id=TestId123-color-36-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000025; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 37\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000025] id=TestId123-color-37-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000026; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 38\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000026] id=TestId123-color-38-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000027; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 39\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000027] id=TestId123-color-39-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000028; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 40\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000028] id=TestId123-color-40-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000029; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 41\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000029] id=TestId123-color-41-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 42\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002a] id=TestId123-color-42-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 43\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002b] id=TestId123-color-43-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 44\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002c] id=TestId123-color-44-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 45\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002d] id=TestId123-color-45-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 46\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002e] id=TestId123-color-46-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 47\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002f] id=TestId123-color-47-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000030; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 48\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000030] id=TestId123-color-48-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000031; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 49\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000031] id=TestId123-color-49-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000032; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 50\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000032] id=TestId123-color-50-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000033; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 51\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23003] id=TestId123-color-51-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000034; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 52\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000034] id=TestId123-color-52-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000035; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 53\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000035] id=TestId123-color-53-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000036; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 54\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000036] id=TestId123-color-54-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000037; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 55\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000037] id=TestId123-color-55-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=TestId123-color-56-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                    \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/] id=TestId123-color-clear-Link\n"
        );
    }

    @Test
    public void testSetHelperText() {
        this.treePrintAndCheck(
            this.createComponent()
                .setHelperText(
                    Optional.of("HelperText 123")
                ),
            "TextStylePropertyColorComponent\n" +
                "  ColorComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [] icons=mdi-close-circle, mdi-palette id=TestId123-backgroundColor-TextBox helperText=\"HelperText 123\" REQUIRED\n" +
                "          innerRight\n" +
                "            ColorBoxComponent\n" +
                "        Errors\n" +
                "          Empty \"text\"\n" +
                "      ColorPaletteComponent\n" +
                "        TABLE\n" +
                "          id=\"TestId123-Table\" className=dui dui-menu-item\n" +
                "            TBODY\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000001; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"Black\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000001] id=TestId123-color-1-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000002; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"White\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000002] id=TestId123-color-2-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000003; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 3\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000003] id=TestId123-color-3-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000004; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 4\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000004] id=TestId123-color-4-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000005; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 5\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000005] id=TestId123-color-5-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000006; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 6\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000006] id=TestId123-color-6-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000007; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 7\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000007] id=TestId123-color-7-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000008; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 8\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000008] id=TestId123-color-8-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000009; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 9\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000009] id=TestId123-color-9-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 10\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000a] id=TestId123-color-10-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 11\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000b] id=TestId123-color-11-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 12\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000c] id=TestId123-color-12-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 13\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000d] id=TestId123-color-13-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 14\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000e] id=TestId123-color-14-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 15\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000f] id=TestId123-color-15-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000010; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 16\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000010] id=TestId123-color-16-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000011; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 17\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23001] id=TestId123-color-17-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000012; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 18\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000012] id=TestId123-color-18-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000013; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 19\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000013] id=TestId123-color-19-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000014; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 20\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000014] id=TestId123-color-20-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000015; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 21\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000015] id=TestId123-color-21-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000016; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 22\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000016] id=TestId123-color-22-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000017; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 23\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000017] id=TestId123-color-23-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000018; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 24\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000018] id=TestId123-color-24-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000019; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 25\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000019] id=TestId123-color-25-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 26\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001a] id=TestId123-color-26-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 27\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001b] id=TestId123-color-27-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 28\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001c] id=TestId123-color-28-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 29\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001d] id=TestId123-color-29-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 30\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001e] id=TestId123-color-30-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 31\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001f] id=TestId123-color-31-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000020; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 32\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000020] id=TestId123-color-32-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000021; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 33\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000021] id=TestId123-color-33-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000022; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 34\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23002] id=TestId123-color-34-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000023; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 35\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000023] id=TestId123-color-35-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000024; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 36\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000024] id=TestId123-color-36-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000025; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 37\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000025] id=TestId123-color-37-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000026; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 38\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000026] id=TestId123-color-38-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000027; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 39\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000027] id=TestId123-color-39-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000028; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 40\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000028] id=TestId123-color-40-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000029; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 41\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000029] id=TestId123-color-41-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 42\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002a] id=TestId123-color-42-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 43\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002b] id=TestId123-color-43-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 44\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002c] id=TestId123-color-44-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 45\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002d] id=TestId123-color-45-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 46\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002e] id=TestId123-color-46-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 47\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002f] id=TestId123-color-47-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000030; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 48\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000030] id=TestId123-color-48-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000031; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 49\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000031] id=TestId123-color-49-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000032; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 50\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000032] id=TestId123-color-50-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000033; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 51\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23003] id=TestId123-color-51-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000034; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 52\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000034] id=TestId123-color-52-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000035; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 53\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000035] id=TestId123-color-53-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000036; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 54\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000036] id=TestId123-color-54-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000037; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 55\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000037] id=TestId123-color-55-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=TestId123-color-56-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                    \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/] id=TestId123-color-clear-Link\n"
        );
    }

    @Test
    public void testSetErrors() {
        this.treePrintAndCheck(
            this.createComponent()
                .setErrors(
                    Lists.of(
                        "Error111",
                        "Error222",
                        "Error333"
                    )
                ),
            "TextStylePropertyColorComponent\n" +
                "  ColorComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [] icons=mdi-close-circle, mdi-palette id=TestId123-backgroundColor-TextBox REQUIRED\n" +
                "          innerRight\n" +
                "            ColorBoxComponent\n" +
                "        Errors\n" +
                "          Error111\n" +
                "          Error222\n" +
                "          Error333\n" +
                "      ColorPaletteComponent\n" +
                "        TABLE\n" +
                "          id=\"TestId123-Table\" className=dui dui-menu-item\n" +
                "            TBODY\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000001; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"Black\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000001] id=TestId123-color-1-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000002; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"White\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000002] id=TestId123-color-2-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000003; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 3\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000003] id=TestId123-color-3-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000004; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 4\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000004] id=TestId123-color-4-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000005; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 5\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000005] id=TestId123-color-5-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000006; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 6\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000006] id=TestId123-color-6-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000007; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 7\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000007] id=TestId123-color-7-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000008; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 8\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000008] id=TestId123-color-8-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000009; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 9\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000009] id=TestId123-color-9-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 10\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000a] id=TestId123-color-10-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 11\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000b] id=TestId123-color-11-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 12\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000c] id=TestId123-color-12-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 13\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000d] id=TestId123-color-13-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 14\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000e] id=TestId123-color-14-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 15\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000f] id=TestId123-color-15-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000010; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 16\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000010] id=TestId123-color-16-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000011; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 17\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23001] id=TestId123-color-17-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000012; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 18\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000012] id=TestId123-color-18-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000013; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 19\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000013] id=TestId123-color-19-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000014; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 20\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000014] id=TestId123-color-20-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000015; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 21\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000015] id=TestId123-color-21-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000016; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 22\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000016] id=TestId123-color-22-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000017; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 23\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000017] id=TestId123-color-23-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000018; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 24\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000018] id=TestId123-color-24-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000019; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 25\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000019] id=TestId123-color-25-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 26\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001a] id=TestId123-color-26-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 27\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001b] id=TestId123-color-27-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 28\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001c] id=TestId123-color-28-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 29\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001d] id=TestId123-color-29-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 30\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001e] id=TestId123-color-30-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 31\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001f] id=TestId123-color-31-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000020; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 32\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000020] id=TestId123-color-32-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000021; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 33\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000021] id=TestId123-color-33-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000022; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 34\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23002] id=TestId123-color-34-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000023; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 35\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000023] id=TestId123-color-35-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000024; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 36\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000024] id=TestId123-color-36-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000025; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 37\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000025] id=TestId123-color-37-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000026; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 38\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000026] id=TestId123-color-38-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000027; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 39\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000027] id=TestId123-color-39-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000028; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 40\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000028] id=TestId123-color-40-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000029; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 41\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000029] id=TestId123-color-41-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 42\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002a] id=TestId123-color-42-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 43\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002b] id=TestId123-color-43-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 44\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002c] id=TestId123-color-44-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 45\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002d] id=TestId123-color-45-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 46\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002e] id=TestId123-color-46-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 47\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002f] id=TestId123-color-47-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000030; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 48\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000030] id=TestId123-color-48-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000031; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 49\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000031] id=TestId123-color-49-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000032; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 50\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000032] id=TestId123-color-50-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000033; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 51\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23003] id=TestId123-color-51-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000034; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 52\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000034] id=TestId123-color-52-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000035; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 53\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000035] id=TestId123-color-53-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000036; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 54\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000036] id=TestId123-color-54-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000037; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 55\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000037] id=TestId123-color-55-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=TestId123-color-56-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                    \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/] id=TestId123-color-clear-Link\n"
        );
    }

    @Test
    public void testSetHelperTextSetErrors() {
        this.treePrintAndCheck(
            this.createComponent()
                .setHelperText(
                    Optional.of("HelperText 123")
                ).setErrors(
                    Lists.of(
                        "Error111",
                        "Error222",
                        "Error333"
                    )
                ),
            "TextStylePropertyColorComponent\n" +
                "  ColorComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [] icons=mdi-close-circle, mdi-palette id=TestId123-backgroundColor-TextBox helperText=\"HelperText 123\" REQUIRED\n" +
                "          innerRight\n" +
                "            ColorBoxComponent\n" +
                "        Errors\n" +
                "          Error111\n" +
                "          Error222\n" +
                "          Error333\n" +
                "      ColorPaletteComponent\n" +
                "        TABLE\n" +
                "          id=\"TestId123-Table\" className=dui dui-menu-item\n" +
                "            TBODY\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000001; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"Black\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000001] id=TestId123-color-1-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000002; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"White\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000002] id=TestId123-color-2-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000003; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 3\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000003] id=TestId123-color-3-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000004; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 4\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000004] id=TestId123-color-4-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000005; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 5\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000005] id=TestId123-color-5-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000006; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 6\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000006] id=TestId123-color-6-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000007; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 7\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000007] id=TestId123-color-7-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000008; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 8\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000008] id=TestId123-color-8-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000009; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 9\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000009] id=TestId123-color-9-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 10\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000a] id=TestId123-color-10-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 11\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000b] id=TestId123-color-11-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 12\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000c] id=TestId123-color-12-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 13\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000d] id=TestId123-color-13-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 14\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000e] id=TestId123-color-14-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00000f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 15\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000f] id=TestId123-color-15-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000010; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 16\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000010] id=TestId123-color-16-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000011; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 17\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23001] id=TestId123-color-17-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000012; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 18\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000012] id=TestId123-color-18-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000013; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 19\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000013] id=TestId123-color-19-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000014; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 20\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000014] id=TestId123-color-20-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000015; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 21\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000015] id=TestId123-color-21-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000016; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 22\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000016] id=TestId123-color-22-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000017; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 23\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000017] id=TestId123-color-23-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000018; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 24\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000018] id=TestId123-color-24-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000019; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 25\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000019] id=TestId123-color-25-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 26\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001a] id=TestId123-color-26-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 27\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001b] id=TestId123-color-27-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 28\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001c] id=TestId123-color-28-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 29\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001d] id=TestId123-color-29-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 30\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001e] id=TestId123-color-30-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00001f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 31\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001f] id=TestId123-color-31-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000020; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 32\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000020] id=TestId123-color-32-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000021; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 33\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000021] id=TestId123-color-33-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000022; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 34\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23002] id=TestId123-color-34-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000023; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 35\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000023] id=TestId123-color-35-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000024; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 36\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000024] id=TestId123-color-36-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000025; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 37\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000025] id=TestId123-color-37-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000026; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 38\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000026] id=TestId123-color-38-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000027; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 39\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000027] id=TestId123-color-39-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000028; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 40\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000028] id=TestId123-color-40-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000029; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 41\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000029] id=TestId123-color-41-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002a; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 42\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002a] id=TestId123-color-42-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002b; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 43\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002b] id=TestId123-color-43-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002c; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 44\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002c] id=TestId123-color-44-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002d; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 45\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002d] id=TestId123-color-45-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002e; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 46\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002e] id=TestId123-color-46-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #00002f; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 47\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002f] id=TestId123-color-47-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000030; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 48\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000030] id=TestId123-color-48-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  style=\"background-color: #000031; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 49\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000031] id=TestId123-color-49-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000032; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 50\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000032] id=TestId123-color-50-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000033; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 51\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23003] id=TestId123-color-51-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000034; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 52\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000034] id=TestId123-color-52-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000035; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 53\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000035] id=TestId123-color-53-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000036; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 54\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000036] id=TestId123-color-54-Link\n" +
                "                TD\n" +
                "                  style=\"background-color: #000037; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    \"color 55\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000037] id=TestId123-color-55-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=TestId123-color-56-Link\n" +
                "              TR\n" +
                "                TD\n" +
                "                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                    \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/] id=TestId123-color-clear-Link\n"
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createComponent(),
            "[] id=TestId123-backgroundColor-TextBox REQUIRED Errors=\"Empty \"text\"\"  TABLE [TBODY [TR [TD [\"Black\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000001] id=TestId123-color-1-Link], TD [\"White\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000002] id=TestId123-color-2-Link], TD [\"color 3\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000003] id=TestId123-color-3-Link], TD [\"color 4\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000004] id=TestId123-color-4-Link], TD [\"color 5\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000005] id=TestId123-color-5-Link], TD [\"color 6\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000006] id=TestId123-color-6-Link], TD [\"color 7\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000007] id=TestId123-color-7-Link], TD [\"color 8\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000008] id=TestId123-color-8-Link]], TR [TD [\"color 9\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000009] id=TestId123-color-9-Link], TD [\"color 10\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000a] id=TestId123-color-10-Link], TD [\"color 11\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000b] id=TestId123-color-11-Link], TD [\"color 12\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000c] id=TestId123-color-12-Link], TD [\"color 13\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000d] id=TestId123-color-13-Link], TD [\"color 14\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000e] id=TestId123-color-14-Link], TD [\"color 15\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300000f] id=TestId123-color-15-Link], TD [\"color 16\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000010] id=TestId123-color-16-Link]], TR [TD [\"color 17\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23001] id=TestId123-color-17-Link], TD [\"color 18\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000012] id=TestId123-color-18-Link], TD [\"color 19\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000013] id=TestId123-color-19-Link], TD [\"color 20\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000014] id=TestId123-color-20-Link], TD [\"color 21\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000015] id=TestId123-color-21-Link], TD [\"color 22\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000016] id=TestId123-color-22-Link], TD [\"color 23\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000017] id=TestId123-color-23-Link], TD [\"color 24\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000018] id=TestId123-color-24-Link]], TR [TD [\"color 25\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000019] id=TestId123-color-25-Link], TD [\"color 26\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001a] id=TestId123-color-26-Link], TD [\"color 27\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001b] id=TestId123-color-27-Link], TD [\"color 28\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001c] id=TestId123-color-28-Link], TD [\"color 29\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001d] id=TestId123-color-29-Link], TD [\"color 30\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001e] id=TestId123-color-30-Link], TD [\"color 31\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300001f] id=TestId123-color-31-Link], TD [\"color 32\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000020] id=TestId123-color-32-Link]], TR [TD [\"color 33\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000021] id=TestId123-color-33-Link], TD [\"color 34\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23002] id=TestId123-color-34-Link], TD [\"color 35\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000023] id=TestId123-color-35-Link], TD [\"color 36\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000024] id=TestId123-color-36-Link], TD [\"color 37\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000025] id=TestId123-color-37-Link], TD [\"color 38\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000026] id=TestId123-color-38-Link], TD [\"color 39\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000027] id=TestId123-color-39-Link], TD [\"color 40\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000028] id=TestId123-color-40-Link]], TR [TD [\"color 41\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000029] id=TestId123-color-41-Link], TD [\"color 42\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002a] id=TestId123-color-42-Link], TD [\"color 43\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002b] id=TestId123-color-43-Link], TD [\"color 44\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002c] id=TestId123-color-44-Link], TD [\"color 45\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002d] id=TestId123-color-45-Link], TD [\"color 46\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002e] id=TestId123-color-46-Link], TD [\"color 47\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%2300002f] id=TestId123-color-47-Link], TD [\"color 48\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000030] id=TestId123-color-48-Link]], TR [TD [\"color 49\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000031] id=TestId123-color-49-Link], TD [\"color 50\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000032] id=TestId123-color-50-Link], TD [\"color 51\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23003] id=TestId123-color-51-Link], TD [\"color 52\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000034] id=TestId123-color-52-Link], TD [\"color 53\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000035] id=TestId123-color-53-Link], TD [\"color 54\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000036] id=TestId123-color-54-Link], TD [\"color 55\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/%23000037] id=TestId123-color-55-Link], TD [DISABLED id=TestId123-color-56-Link]], TR [TD [\"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/] id=TestId123-color-clear-Link]]]]"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TextStylePropertyColorComponent> type() {
        return Cast.to(TextStylePropertyColorComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
