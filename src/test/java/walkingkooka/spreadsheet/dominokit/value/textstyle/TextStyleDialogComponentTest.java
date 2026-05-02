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

package walkingkooka.spreadsheet.dominokit.value.textstyle;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentLike;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public final class TextStyleDialogComponentTest implements DialogComponentLifecycleTesting<TextStyleDialogComponent>,
    HistoryTokenTesting,
    SpreadsheetMetadataTesting {

    private final static SpreadsheetMetadata METADATA = METADATA_EN_AU.set(
        SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
        SPREADSHEET_ID
    );

    private final static TextStyle UNDO_TEXT_STYLE = TextStyle.parse("text-align: LEFT;");

    private final static TextStyle VALUE_TEXT_STYLE = TextStyle.parse("background-color: BLACK; color: BLACK; direction: LTR; font-family: Courier; font-kerning: AUTO; font-stretch: NORMAL; font-size: 99; font-style: ITALIC; font-variant: SMALL_CAPS; font-weight: BOLD; hanging-punctuation: FIRST; height: 55px; hyphens: AUTO; letter-spacing: 33px; line-height: 44px; margin-top: 1px; margin-right: 2px; margin-bottom: 3px; margin-left: 4px; opacity: 50%; overflow-x: VISIBLE; overflow-y: HIDDEN; overflow-wrap: BREAK_WORD; padding-top: 1px; padding-right: 2px; padding-bottom: 3px; padding-left: 4px; text-align: CENTER; text-decoration-line: OVERLINE; text-decoration-style: DOTTED; text-decoration-thickness: 123px; text-indent: 123px; text-justify: INTER_WORD; text-transform: UPPERCASE; text-wrapping: WRAP; vertical-align: MIDDLE; visibility: VISIBLE; white-space: NOWRAP; width: 44px; word-break: BREAK_WORD; word-wrap: BREAK_WORD; writing-mode: HORIZONTAL_TB;");

    // setStringValue...................................................................................................

    @Test
    public void testTextStylePropertyComponentSetStringValueWithInvalid() {
        final SpreadsheetCellReference cell = SpreadsheetSelection.A1;

        final AppContext context = this.appContext(
            HistoryToken.cellStyle(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                cell.setDefaultAnchor(),
                Optional.empty()
            ),
            Optional.empty() // no textStyle
        );

        context.spreadsheetViewportCache()
            .onSpreadsheetMetadata(
                METADATA
            );

        context.spreadsheetViewportCache()
            .onSpreadsheetDelta(
                HttpMethod.GET,
                Url.parseRelative("/api/spreadsheet/1/cell"),
                SpreadsheetDelta.EMPTY.setCells(
                    Sets.of(
                        cell.setFormula(SpreadsheetFormula.EMPTY) // no textStyle
                    )
                )
            );

        final TextStyleDialogComponent component = TextStyleDialogComponent.with(
            TextStyleDialogComponentContexts.appContextCellStyle(context)
        );

        component.onHistoryTokenChange(
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            ),
            context
        );

        component.textStyle.setValue(
            Optional.of(VALUE_TEXT_STYLE)
        );

        int i = 0;

        for(final TextStylePropertyComponent<?, ?, ?> c : component.components) {
            // only TextBoxes can have invalid string values.
            if(c instanceof ValueTextBoxComponentLike) {
                final ValueTextBoxComponentLike<?, ?> valueTextBoxComponent = (ValueTextBoxComponentLike<?, ?>) c;

                System.out.println(c);

                valueTextBoxComponent.setStringValue(
                    Optional.of("!Invalid")
                );

                this.checkNotEquals(
                    Lists.empty(),
                    c.errors(),
                    () -> " Expected errors after setting \"!Invalid\""
                );

                this.checkEquals(
                    Optional.of(VALUE_TEXT_STYLE),
                    component.textStyle.value(),
                    () -> "textStyle should not have changed " + c
                );

                i++;
            }
        }

        this.checkNotEquals(
            0,
            i
        );

        this.treePrintAndCheck(
            component,
            "TextStyleDialogComponent\n" +
                "  DialogComponent\n" +
                "    A1: *\n" +
                "    id=TextStyle-Dialog includeClose=true\n" +
                "      SpreadsheetExpressionReferenceComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Selection [A1] icons=mdi-close-circle id=TextStyle-selection-TextBox REQUIRED\n" +
                "      TextStyleSampleComponent\n" +
                "        DIV\n" +
                "          style=\"background-color: black; color: black; direction: LTR; font-family: Courier; font-kerning: AUTO; font-size: 99; font-stretch: NORMAL; font-style: ITALIC; font-variant: SMALL_CAPS; font-weight: BOLD; hanging-punctuation: FIRST; height: 55px; hyphens: AUTO; letter-spacing: 33px; line-height: 44px; margin-bottom: 3px; margin-left: 4px; margin-right: 2px; margin-top: 1px; opacity: 50%; overflow-wrap: BREAK_WORD; overflow-x: VISIBLE; overflow-y: HIDDEN; padding-bottom: 3px; padding-left: 4px; padding-right: 2px; padding-top: 1px; text-align: CENTER; text-decoration-line: OVERLINE; text-decoration-style: DOTTED; text-decoration-thickness: 123px; text-indent: 123px; text-justify: INTER_WORD; text-transform: UPPERCASE; text-wrapping: WRAP; vertical-align: MIDDLE; visibility: VISIBLE; white-space: NOWRAP; width: 44px; word-break: BREAK_WORD; word-wrap: BREAK_WORD; writing-mode: HORIZONTAL_TB;\"\n" +
                "            \"The quick brown fox jumps over the lazy dog\"\n" +
                "      ThreeColumnComponent\n" +
                "        DIV\n" +
                "          style=\"display: grid; gap: 5px; grid-template-columns: 33% 33% 33%;\"\n" +
                "            BackgroundColorComponent\n" +
                "              TextStylePropertyColorComponent\n" +
                "                ColorComponent\n" +
                "                  ValueTextBoxComponent\n" +
                "                    TextBoxComponent\n" +
                "                      Background Color [black] icons=mdi-close-circle, mdi-palette id=TextStyle-backgroundColor-TextBox\n" +
                "                        innerRight\n" +
                "                          ColorBoxComponent\n" +
                "                            black\n" +
                "                    ColorPaletteComponent\n" +
                "                      TABLE\n" +
                "                        id=\"TextStyle-Table\" className=dui dui-menu-item\n" +
                "                          TBODY\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  \"Black\" DISABLED id=TextStyle-color-1-Link\n" +
                "                              TD\n" +
                "                                style=\"background-color: white; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  \"White\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/white] id=TextStyle-color-2-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-3-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-4-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-5-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-6-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-7-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-8-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-9-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-10-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-11-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-12-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-13-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-14-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-15-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-16-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-17-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-18-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-19-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-20-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-21-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-22-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-23-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-24-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-25-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-26-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-27-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-28-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-29-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-30-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-31-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-32-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-33-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-34-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-35-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-36-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-37-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-38-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-39-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-40-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-41-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-42-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-43-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-44-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-45-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-46-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-47-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-48-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-49-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-50-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-51-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-52-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-53-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-54-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-55-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-56-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                                  \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/] id=TextStyle-color-clear-Link\n" +
                "              TextStyleColorComponent\n" +
                "                TextStylePropertyColorComponent\n" +
                "                  ColorComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Color [black] icons=mdi-close-circle, mdi-palette id=TextStyle-color-TextBox\n" +
                "                          innerRight\n" +
                "                            ColorBoxComponent\n" +
                "                              black\n" +
                "                      ColorPaletteComponent\n" +
                "                        TABLE\n" +
                "                          id=\"TextStyle-Table\" className=dui dui-menu-item\n" +
                "                            TBODY\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    \"Black\" DISABLED id=TextStyle-color-1-Link\n" +
                "                                TD\n" +
                "                                  style=\"background-color: white; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    \"White\" [#/1/SpreadsheetName1/cell/A1/style/color/save/white] id=TextStyle-color-2-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-3-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-4-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-5-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-6-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-7-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-8-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-9-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-10-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-11-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-12-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-13-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-14-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-15-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-16-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-17-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-18-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-19-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-20-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-21-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-22-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-23-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-24-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-25-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-26-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-27-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-28-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-29-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-30-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-31-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-32-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-33-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-34-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-35-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-36-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-37-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-38-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-39-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-40-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-41-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-42-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-43-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-44-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-45-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-46-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-47-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-48-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-49-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-50-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-51-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-52-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-53-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-54-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-55-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-56-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                                    \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/color/save/] id=TextStyle-color-clear-Link\n" +
                "                DirectionComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Direction\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/direction/save/] id=TextStyle-direction-Link\n" +
                "                            \"Left to Right\" [#/1/SpreadsheetName1/cell/A1/style/direction/save/LTR] CHECKED id=TextStyle-direction-LTR-Link\n" +
                "                            \"Right to Left\" [#/1/SpreadsheetName1/cell/A1/style/direction/save/RTL] id=TextStyle-direction-RTL-Link\n" +
                "                FontFamilyComponent\n" +
                "                  SelectComponent\n" +
                "                    Font Family [Courier] id=TextStyle-Select\n" +
                "                      \"Courier\" DISABLED id=TextStyle-courier-Option\n" +
                "                      \"Sans Serif\" DISABLED id=TextStyle-sansSerif-Option\n" +
                "                      \"Times New Roman\" DISABLED id=TextStyle-timesNewRoman-Option\n" +
                "                FontKerningComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Font Kerning\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/font-kerning/save/] id=TextStyle-fontKerning-Link\n" +
                "                            \"Auto\" [#/1/SpreadsheetName1/cell/A1/style/font-kerning/save/AUTO] CHECKED id=TextStyle-fontKerning-AUTO-Link\n" +
                "                            \"None\" [#/1/SpreadsheetName1/cell/A1/style/font-kerning/save/NONE] id=TextStyle-fontKerning-NONE-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/font-kerning/save/NORMAL] id=TextStyle-fontKerning-NORMAL-Link\n" +
                "                FontSizeComponent\n" +
                "                  SuggestBoxComponent\n" +
                "                    Font Size [99]\n" +
                "                FontStretchComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Font Stretch\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/] id=TextStyle-fontStretch-Link\n" +
                "                            \"Ultra Condensed\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/ULTRA_CONDENSED] id=TextStyle-fontStretch-ULTRA_CONDENSED-Link\n" +
                "                            \"Extra Condensed\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/EXTRA_CONDENSED] id=TextStyle-fontStretch-EXTRA_CONDENSED-Link\n" +
                "                            \"Condensed\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/CONDENSED] id=TextStyle-fontStretch-CONDENSED-Link\n" +
                "                            \"Semi Condensed\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/SEMI_CONDENSED] id=TextStyle-fontStretch-SEMI_CONDENSED-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/NORMAL] CHECKED id=TextStyle-fontStretch-NORMAL-Link\n" +
                "                            \"Expanded\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/EXPANDED] id=TextStyle-fontStretch-EXPANDED-Link\n" +
                "                            \"Extra Expanded\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/EXTRA_EXPANDED] id=TextStyle-fontStretch-EXTRA_EXPANDED-Link\n" +
                "                            \"Ultra Expanded\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/ULTRA_EXPANDED] id=TextStyle-fontStretch-ULTRA_EXPANDED-Link\n" +
                "                FontStyleComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Font Style\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/font-style/save/] id=TextStyle-fontStyle-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/font-style/save/NORMAL] id=TextStyle-fontStyle-NORMAL-Link\n" +
                "                            mdi-format-italic \"Italic\" [#/1/SpreadsheetName1/cell/A1/style/font-style/save/ITALIC] CHECKED id=TextStyle-fontStyle-ITALIC-Link\n" +
                "                            \"Oblique\" [#/1/SpreadsheetName1/cell/A1/style/font-style/save/OBLIQUE] id=TextStyle-fontStyle-OBLIQUE-Link\n" +
                "                FontVariantComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Font Variant\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/font-variant/save/] id=TextStyle-fontVariant-Link\n" +
                "                            \"Initial\" [#/1/SpreadsheetName1/cell/A1/style/font-variant/save/INITIAL] id=TextStyle-fontVariant-INITIAL-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/font-variant/save/NORMAL] id=TextStyle-fontVariant-NORMAL-Link\n" +
                "                            \"Small Caps\" [#/1/SpreadsheetName1/cell/A1/style/font-variant/save/SMALL_CAPS] CHECKED id=TextStyle-fontVariant-SMALL_CAPS-Link\n" +
                "                FontWeightComponent\n" +
                "                  ValueTextBoxComponent\n" +
                "                    TextBoxComponent\n" +
                "                      Font Weight [!Invalid] icons=mdi-close-circle id=TextStyle-fontWeight-TextBox\n" +
                "                      Errors\n" +
                "                        Invalid character '!' at 0\n" +
                "                HangingPunctuationComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Hanging Punctuation\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/hanging-punctuation/save/] id=TextStyle-hangingPunctuation-Link\n" +
                "                            \"None\" [#/1/SpreadsheetName1/cell/A1/style/hanging-punctuation/save/NONE] id=TextStyle-hangingPunctuation-NONE-Link\n" +
                "                            \"First\" [#/1/SpreadsheetName1/cell/A1/style/hanging-punctuation/save/FIRST] CHECKED id=TextStyle-hangingPunctuation-FIRST-Link\n" +
                "                            \"Last\" [#/1/SpreadsheetName1/cell/A1/style/hanging-punctuation/save/LAST] id=TextStyle-hangingPunctuation-LAST-Link\n" +
                "                            \"Allow End\" [#/1/SpreadsheetName1/cell/A1/style/hanging-punctuation/save/ALLOW_END] id=TextStyle-hangingPunctuation-ALLOW_END-Link\n" +
                "                            \"Force End\" [#/1/SpreadsheetName1/cell/A1/style/hanging-punctuation/save/FORCE_END] id=TextStyle-hangingPunctuation-FORCE_END-Link\n" +
                "                HeightComponent\n" +
                "                  LengthComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Height [!Invalid] icons=mdi-close-circle id=TextStyle-height-TextBox\n" +
                "                        Errors\n" +
                "                          Invalid character '!' at 0\n" +
                "                HyphensComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Hyphens\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/hyphens/save/] id=TextStyle-hyphens-Link\n" +
                "                            \"None\" [#/1/SpreadsheetName1/cell/A1/style/hyphens/save/NONE] id=TextStyle-hyphens-NONE-Link\n" +
                "                            \"Manual\" [#/1/SpreadsheetName1/cell/A1/style/hyphens/save/MANUAL] id=TextStyle-hyphens-MANUAL-Link\n" +
                "                            \"Auto\" [#/1/SpreadsheetName1/cell/A1/style/hyphens/save/AUTO] CHECKED id=TextStyle-hyphens-AUTO-Link\n" +
                "                LetterSpacingComponent\n" +
                "                  LengthComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Letter Spacing [!Invalid] icons=mdi-close-circle id=TextStyle-letterSpacing-TextBox\n" +
                "                        Errors\n" +
                "                          Invalid character '!' at 0\n" +
                "                LineHeightComponent\n" +
                "                  LengthComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Line Height [!Invalid] icons=mdi-close-circle id=TextStyle-lineHeight-TextBox\n" +
                "                        Errors\n" +
                "                          Invalid character '!' at 0\n" +
                "                BigMarginComponent\n" +
                "                  FormElementComponent\n" +
                "                    label\n" +
                "                      Margin\n" +
                "                    FlexLayoutComponent\n" +
                "                      ROW\n" +
                "                        MarginTopComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Top [1px] icons=mdi-close-circle id=TextStyle-marginTop-TextBox\n" +
                "                        MarginRightComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Right [2px] icons=mdi-close-circle id=TextStyle-marginRight-TextBox\n" +
                "                        MarginBottomComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Bottom [3px] icons=mdi-close-circle id=TextStyle-marginBottom-TextBox\n" +
                "                        MarginLeftComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Left [4px] icons=mdi-close-circle id=TextStyle-marginLeft-TextBox\n" +
                "                        MarginComponent\n" +
                "                          ValueTextBoxComponent\n" +
                "                            TextBoxComponent\n" +
                "                              All [1px 2px 3px 4px] icons=mdi-close-circle id=TextStyle-margin-TextBox\n" +
                "                                innerRight\n" +
                "                                  MarginBoxComponent\n" +
                "                                    Margin\n" +
                "                                      ALL\n" +
                "                                        TextStyle\n" +
                "                                          margin-bottom=3px\n" +
                "                                          margin-left=4px\n" +
                "                                          margin-right=2px\n" +
                "                                          margin-top=1px\n" +
                "                OpacityComponent\n" +
                "                  ValueTextBoxComponent\n" +
                "                    TextBoxComponent\n" +
                "                      Opacity [!Invalid] icons=mdi-close-circle id=TextStyle-opacity-TextBox\n" +
                "                      Errors\n" +
                "                        Invalid character '!' at 0\n" +
                "                OverflowComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Overflow X\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/overflow-x/save/] id=TextStyle-overflowX-Link\n" +
                "                            \"Visible\" [#/1/SpreadsheetName1/cell/A1/style/overflow-x/save/VISIBLE] CHECKED id=TextStyle-overflowX-VISIBLE-Link\n" +
                "                            \"Hidden\" [#/1/SpreadsheetName1/cell/A1/style/overflow-x/save/HIDDEN] id=TextStyle-overflowX-HIDDEN-Link\n" +
                "                            \"Scroll\" [#/1/SpreadsheetName1/cell/A1/style/overflow-x/save/SCROLL] id=TextStyle-overflowX-SCROLL-Link\n" +
                "                            \"Auto\" [#/1/SpreadsheetName1/cell/A1/style/overflow-x/save/AUTO] id=TextStyle-overflowX-AUTO-Link\n" +
                "                OverflowComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Overflow Y\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/overflow-y/save/] id=TextStyle-overflowY-Link\n" +
                "                            \"Visible\" [#/1/SpreadsheetName1/cell/A1/style/overflow-y/save/VISIBLE] id=TextStyle-overflowY-VISIBLE-Link\n" +
                "                            \"Hidden\" [#/1/SpreadsheetName1/cell/A1/style/overflow-y/save/HIDDEN] CHECKED id=TextStyle-overflowY-HIDDEN-Link\n" +
                "                            \"Scroll\" [#/1/SpreadsheetName1/cell/A1/style/overflow-y/save/SCROLL] id=TextStyle-overflowY-SCROLL-Link\n" +
                "                            \"Auto\" [#/1/SpreadsheetName1/cell/A1/style/overflow-y/save/AUTO] id=TextStyle-overflowY-AUTO-Link\n" +
                "                OverflowWrapComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Overflow Wrap\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/overflow-wrap/save/] id=TextStyle-overflowWrap-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/overflow-wrap/save/NORMAL] id=TextStyle-overflowWrap-NORMAL-Link\n" +
                "                            \"Anywhere\" [#/1/SpreadsheetName1/cell/A1/style/overflow-wrap/save/ANYWHERE] id=TextStyle-overflowWrap-ANYWHERE-Link\n" +
                "                            \"Break Word\" [#/1/SpreadsheetName1/cell/A1/style/overflow-wrap/save/BREAK_WORD] CHECKED id=TextStyle-overflowWrap-BREAK_WORD-Link\n" +
                "                BigPaddingComponent\n" +
                "                  FormElementComponent\n" +
                "                    label\n" +
                "                      Padding\n" +
                "                    FlexLayoutComponent\n" +
                "                      ROW\n" +
                "                        PaddingTopComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Top [1px] icons=mdi-close-circle id=TextStyle-paddingTop-TextBox\n" +
                "                        PaddingRightComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Right [2px] icons=mdi-close-circle id=TextStyle-paddingRight-TextBox\n" +
                "                        PaddingBottomComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Bottom [3px] icons=mdi-close-circle id=TextStyle-paddingBottom-TextBox\n" +
                "                        PaddingLeftComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Left [4px] icons=mdi-close-circle id=TextStyle-paddingLeft-TextBox\n" +
                "                        PaddingComponent\n" +
                "                          ValueTextBoxComponent\n" +
                "                            TextBoxComponent\n" +
                "                              All [1px 2px 3px 4px] icons=mdi-close-circle id=TextStyle-padding-TextBox\n" +
                "                                innerRight\n" +
                "                                  PaddingBoxComponent\n" +
                "                                    Padding\n" +
                "                                      ALL\n" +
                "                                        TextStyle\n" +
                "                                          padding-bottom=3px\n" +
                "                                          padding-left=4px\n" +
                "                                          padding-right=2px\n" +
                "                                          padding-top=1px\n" +
                "                TextAlignComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Text Align\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            mdi-format-clear \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/text-align/save/] id=TextStyle-textAlign-Link\n" +
                "                            mdi-format-align-left \"Left\" [#/1/SpreadsheetName1/cell/A1/style/text-align/save/LEFT] id=TextStyle-textAlign-LEFT-Link\n" +
                "                            mdi-format-align-center \"Center\" [#/1/SpreadsheetName1/cell/A1/style/text-align/save/CENTER] CHECKED id=TextStyle-textAlign-CENTER-Link\n" +
                "                            mdi-format-align-right \"Right\" [#/1/SpreadsheetName1/cell/A1/style/text-align/save/RIGHT] id=TextStyle-textAlign-RIGHT-Link\n" +
                "                            mdi-format-align-justify \"Justify\" [#/1/SpreadsheetName1/cell/A1/style/text-align/save/JUSTIFY] id=TextStyle-textAlign-JUSTIFY-Link\n" +
                "                TextDecorationLineComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Text Decoration Line\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-line/save/] id=TextStyle-textDecorationLine-Link\n" +
                "                            mdi-format-clear \"None\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-line/save/NONE] id=TextStyle-textDecorationLine-NONE-Link\n" +
                "                            mdi-format-strikethrough \"Strikethrough\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-line/save/LINE_THROUGH] id=TextStyle-textDecorationLine-LINE_THROUGH-Link\n" +
                "                            \"Overline\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-line/save/OVERLINE] CHECKED id=TextStyle-textDecorationLine-OVERLINE-Link\n" +
                "                            mdi-format-underline \"Underline\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-line/save/UNDERLINE] id=TextStyle-textDecorationLine-UNDERLINE-Link\n" +
                "                TextDecorationStyleComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Text Decoration Style\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-style/save/] id=TextStyle-textDecorationStyle-Link\n" +
                "                            \"Solid\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-style/save/SOLID] id=TextStyle-textDecorationStyle-SOLID-Link\n" +
                "                            \"Double\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-style/save/DOUBLE] id=TextStyle-textDecorationStyle-DOUBLE-Link\n" +
                "                            \"Dashed\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-style/save/DASHED] id=TextStyle-textDecorationStyle-DASHED-Link\n" +
                "                            \"Dotted\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-style/save/DOTTED] CHECKED id=TextStyle-textDecorationStyle-DOTTED-Link\n" +
                "                            \"Wavy\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-style/save/WAVY] id=TextStyle-textDecorationStyle-WAVY-Link\n" +
                "                TextDecorationThicknessComponent\n" +
                "                  LengthComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Text Decoration Thickness [!Invalid] icons=mdi-close-circle id=TextStyle-textDecorationThickness-TextBox\n" +
                "                        Errors\n" +
                "                          Invalid character '!' at 0\n" +
                "                TextIndentComponent\n" +
                "                  LengthComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Text Indent [!Invalid] icons=mdi-close-circle id=TextStyle-textIndent-TextBox\n" +
                "                        Errors\n" +
                "                          Invalid character '!' at 0\n" +
                "                TextJustifyComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Text Justify\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/text-justify/save/] id=TextStyle-textJustify-Link\n" +
                "                            \"Auto\" [#/1/SpreadsheetName1/cell/A1/style/text-justify/save/AUTO] id=TextStyle-textJustify-AUTO-Link\n" +
                "                            \"Inter Word\" [#/1/SpreadsheetName1/cell/A1/style/text-justify/save/INTER_WORD] CHECKED id=TextStyle-textJustify-INTER_WORD-Link\n" +
                "                            \"Inter Character\" [#/1/SpreadsheetName1/cell/A1/style/text-justify/save/INTER_CHARACTER] id=TextStyle-textJustify-INTER_CHARACTER-Link\n" +
                "                TextTransformComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Text Transform\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/text-transform/save/] id=TextStyle-textTransform-Link\n" +
                "                            \"None\" [#/1/SpreadsheetName1/cell/A1/style/text-transform/save/NONE] id=TextStyle-textTransform-NONE-Link\n" +
                "                            mdi-format-letter-case \"Capitalize\" [#/1/SpreadsheetName1/cell/A1/style/text-transform/save/CAPITALIZE] id=TextStyle-textTransform-CAPITALIZE-Link\n" +
                "                            mdi-format-letter-case-upper \"Uppercase\" [#/1/SpreadsheetName1/cell/A1/style/text-transform/save/UPPERCASE] CHECKED id=TextStyle-textTransform-UPPERCASE-Link\n" +
                "                            mdi-format-letter-case-lower \"Lowercase\" [#/1/SpreadsheetName1/cell/A1/style/text-transform/save/LOWERCASE] id=TextStyle-textTransform-LOWERCASE-Link\n" +
                "                TextWrappingComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Text Wrapping\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/text-wrapping/save/] id=TextStyle-textWrapping-Link\n" +
                "                            \"Overflow\" [#/1/SpreadsheetName1/cell/A1/style/text-wrapping/save/OVERFLOW] id=TextStyle-textWrapping-OVERFLOW-Link\n" +
                "                            \"Wrap\" [#/1/SpreadsheetName1/cell/A1/style/text-wrapping/save/WRAP] CHECKED id=TextStyle-textWrapping-WRAP-Link\n" +
                "                            \"Clip\" [#/1/SpreadsheetName1/cell/A1/style/text-wrapping/save/CLIP] id=TextStyle-textWrapping-CLIP-Link\n" +
                "                VerticalAlignComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Vertical Align\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            mdi-format-clear \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/vertical-align/save/] id=TextStyle-verticalAlign-Link\n" +
                "                            mdi-format-align-top \"Top\" [#/1/SpreadsheetName1/cell/A1/style/vertical-align/save/TOP] id=TextStyle-verticalAlign-TOP-Link\n" +
                "                            mdi-format-align-middle \"Middle\" [#/1/SpreadsheetName1/cell/A1/style/vertical-align/save/MIDDLE] CHECKED id=TextStyle-verticalAlign-MIDDLE-Link\n" +
                "                            mdi-format-align-bottom \"Bottom\" [#/1/SpreadsheetName1/cell/A1/style/vertical-align/save/BOTTOM] id=TextStyle-verticalAlign-BOTTOM-Link\n" +
                "                VisibilityComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Visibility\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/visibility/save/] id=TextStyle-visibility-Link\n" +
                "                            \"Visible\" [#/1/SpreadsheetName1/cell/A1/style/visibility/save/VISIBLE] CHECKED id=TextStyle-visibility-VISIBLE-Link\n" +
                "                            \"Hidden\" [#/1/SpreadsheetName1/cell/A1/style/visibility/save/HIDDEN] id=TextStyle-visibility-HIDDEN-Link\n" +
                "                            \"Collapse\" [#/1/SpreadsheetName1/cell/A1/style/visibility/save/COLLAPSE] id=TextStyle-visibility-COLLAPSE-Link\n" +
                "                WhitespaceComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    White Space\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/white-space/save/] id=TextStyle-whiteSpace-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/white-space/save/NORMAL] id=TextStyle-whiteSpace-NORMAL-Link\n" +
                "                            \"Nowrap\" [#/1/SpreadsheetName1/cell/A1/style/white-space/save/NOWRAP] CHECKED id=TextStyle-whiteSpace-NOWRAP-Link\n" +
                "                            \"Pre\" [#/1/SpreadsheetName1/cell/A1/style/white-space/save/PRE] id=TextStyle-whiteSpace-PRE-Link\n" +
                "                            \"Pre Line\" [#/1/SpreadsheetName1/cell/A1/style/white-space/save/PRE_LINE] id=TextStyle-whiteSpace-PRE_LINE-Link\n" +
                "                            \"Pre Wrap\" [#/1/SpreadsheetName1/cell/A1/style/white-space/save/PRE_WRAP] id=TextStyle-whiteSpace-PRE_WRAP-Link\n" +
                "                WidthComponent\n" +
                "                  LengthComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Width [!Invalid] icons=mdi-close-circle id=TextStyle-width-TextBox\n" +
                "                        Errors\n" +
                "                          Invalid character '!' at 0\n" +
                "                WordBreakComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Word Break\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/word-break/save/] id=TextStyle-wordBreak-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/word-break/save/NORMAL] id=TextStyle-wordBreak-NORMAL-Link\n" +
                "                            \"Break All\" [#/1/SpreadsheetName1/cell/A1/style/word-break/save/BREAK_ALL] id=TextStyle-wordBreak-BREAK_ALL-Link\n" +
                "                            \"Keep All\" [#/1/SpreadsheetName1/cell/A1/style/word-break/save/KEEP_ALL] id=TextStyle-wordBreak-KEEP_ALL-Link\n" +
                "                            \"Break Word\" [#/1/SpreadsheetName1/cell/A1/style/word-break/save/BREAK_WORD] CHECKED id=TextStyle-wordBreak-BREAK_WORD-Link\n" +
                "                WordWrapComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Word Wrap\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/word-wrap/save/] id=TextStyle-wordWrap-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/word-wrap/save/NORMAL] id=TextStyle-wordWrap-NORMAL-Link\n" +
                "                            \"Break Word\" [#/1/SpreadsheetName1/cell/A1/style/word-wrap/save/BREAK_WORD] CHECKED id=TextStyle-wordWrap-BREAK_WORD-Link\n" +
                "                WritingModeComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Writing Mode\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/writing-mode/save/] id=TextStyle-writingMode-Link\n" +
                "                            \"Horizontal Tb\" [#/1/SpreadsheetName1/cell/A1/style/writing-mode/save/HORIZONTAL_TB] CHECKED id=TextStyle-writingMode-HORIZONTAL_TB-Link\n" +
                "                            \"Vertical Lr\" [#/1/SpreadsheetName1/cell/A1/style/writing-mode/save/VERTICAL_LR] id=TextStyle-writingMode-VERTICAL_LR-Link\n" +
                "                            \"Vertical Rl\" [#/1/SpreadsheetName1/cell/A1/style/writing-mode/save/VERTICAL_RL] id=TextStyle-writingMode-VERTICAL_RL-Link\n" +
                "          TextStyleComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Style [background-color: black; color: black; direction: LTR; font-family: Courier; font-kerning: AUTO; font-size: 99; font-stretch: NORMAL; font-style: ITALIC; font-variant: SMALL_CAPS; font-weight: BOLD; hanging-punctuation: FIRST; height: 55px; hyphens: AUTO; letter-spacing: 33px; line-height: 44px; margin-bottom: 3px; margin-left: 4px; margin-right: 2px; margin-top: 1px; opacity: 50%; overflow-wrap: BREAK_WORD; overflow-x: VISIBLE; overflow-y: HIDDEN; padding-bottom: 3px; padding-left: 4px; padding-right: 2px; padding-top: 1px; text-align: CENTER; text-decoration-line: OVERLINE; text-decoration-style: DOTTED; text-decoration-thickness: 123px; text-indent: 123px; text-justify: INTER_WORD; text-transform: UPPERCASE; text-wrapping: WRAP; vertical-align: MIDDLE; visibility: VISIBLE; white-space: NOWRAP; width: 44px; word-break: BREAK_WORD; word-wrap: BREAK_WORD; writing-mode: HORIZONTAL_TB;] icons=mdi-close-circle\n" +
                "          DialogAnchorListComponent\n" +
                "            AnchorListComponent\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Save\" [#/1/SpreadsheetName1/cell/A1/style/*/save/background-color:%20black;%20color:%20black;%20direction:%20LTR;%20font-family:%20Courier;%20font-kerning:%20AUTO;%20font-size:%2099;%20font-stretch:%20NORMAL;%20font-style:%20ITALIC;%20font-variant:%20SMALL_CAPS;%20font-weight:%20BOLD;%20hanging-punctuation:%20FIRST;%20height:%2055px;%20hyphens:%20AUTO;%20letter-spacing:%2033px;%20line-height:%2044px;%20margin-bottom:%203px;%20margin-left:%204px;%20margin-right:%202px;%20margin-top:%201px;%20opacity:%2050%25;%20overflow-wrap:%20BREAK_WORD;%20overflow-x:%20VISIBLE;%20overflow-y:%20HIDDEN;%20padding-bottom:%203px;%20padding-left:%204px;%20padding-right:%202px;%20padding-top:%201px;%20text-align:%20CENTER;%20text-decoration-line:%20OVERLINE;%20text-decoration-style:%20DOTTED;%20text-decoration-thickness:%20123px;%20text-indent:%20123px;%20text-justify:%20INTER_WORD;%20text-transform:%20UPPERCASE; id=TextStyle-save-Link\n" +
                "                  \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/*/save/] id=TextStyle-clear-Link\n" +
                "                  \"Undo\" [#/1/SpreadsheetName1/cell/A1/style/*/save/] id=TextStyle-undo-Link\n" +
                "                  \"Close\" [#/1/SpreadsheetName1/cell/A1] id=TextStyle-close-Link\n"
        );
    }

    @Test
    public void testTextStyleSetStringValueWithInvalid() {
        final SpreadsheetCellReference cell = SpreadsheetSelection.A1;

        final AppContext context = this.appContext(
            HistoryToken.cellStyle(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                cell.setDefaultAnchor(),
                Optional.empty()
            ),
            Optional.empty() // no textStyle
        );

        context.spreadsheetViewportCache()
            .onSpreadsheetMetadata(
                METADATA
            );

        context.spreadsheetViewportCache()
            .onSpreadsheetDelta(
                HttpMethod.GET,
                Url.parseRelative("/api/spreadsheet/1/cell"),
                SpreadsheetDelta.EMPTY.setCells(
                    Sets.of(
                        cell.setFormula(SpreadsheetFormula.EMPTY) // no textStyle
                    )
                )
            );

        final TextStyleDialogComponent component = TextStyleDialogComponent.with(
            TextStyleDialogComponentContexts.appContextCellStyle(context)
        );

        component.onHistoryTokenChange(
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            ),
            context
        );

        component.textStyle.setValue(
            Optional.of(VALUE_TEXT_STYLE)
        );

        component.textStyle.setStringValue(
            Optional.of("!invalid")
        );

        final Set<TextStylePropertyComponent<?, ?, ?>> empty = Sets.ordered();

        for(final TextStylePropertyComponent<?, ?, ?> c : component.components) {
            if(c.value().isEmpty()) {
                empty.add(c);
            }
        }

        this.checkEquals(
            Sets.empty(),
            empty,
            "Expected 0 TextStylePropertyComponents with no values."
        );

        this.treePrintAndCheck(
            component,
            "TextStyleDialogComponent\n" +
                "  DialogComponent\n" +
                "    A1: *\n" +
                "    id=TextStyle-Dialog includeClose=true\n" +
                "      SpreadsheetExpressionReferenceComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Selection [A1] icons=mdi-close-circle id=TextStyle-selection-TextBox REQUIRED\n" +
                "      TextStyleSampleComponent\n" +
                "        DIV\n" +
                "          style=\"background-color: black; color: black; direction: LTR; font-family: Courier; font-kerning: AUTO; font-size: 99; font-stretch: NORMAL; font-style: ITALIC; font-variant: SMALL_CAPS; font-weight: BOLD; hanging-punctuation: FIRST; height: 55px; hyphens: AUTO; letter-spacing: 33px; line-height: 44px; margin-bottom: 3px; margin-left: 4px; margin-right: 2px; margin-top: 1px; opacity: 50%; overflow-wrap: BREAK_WORD; overflow-x: VISIBLE; overflow-y: HIDDEN; padding-bottom: 3px; padding-left: 4px; padding-right: 2px; padding-top: 1px; text-align: CENTER; text-decoration-line: OVERLINE; text-decoration-style: DOTTED; text-decoration-thickness: 123px; text-indent: 123px; text-justify: INTER_WORD; text-transform: UPPERCASE; text-wrapping: WRAP; vertical-align: MIDDLE; visibility: VISIBLE; white-space: NOWRAP; width: 44px; word-break: BREAK_WORD; word-wrap: BREAK_WORD; writing-mode: HORIZONTAL_TB;\"\n" +
                "            \"The quick brown fox jumps over the lazy dog\"\n" +
                "      ThreeColumnComponent\n" +
                "        DIV\n" +
                "          style=\"display: grid; gap: 5px; grid-template-columns: 33% 33% 33%;\"\n" +
                "            BackgroundColorComponent\n" +
                "              TextStylePropertyColorComponent\n" +
                "                ColorComponent\n" +
                "                  ValueTextBoxComponent\n" +
                "                    TextBoxComponent\n" +
                "                      Background Color [black] icons=mdi-close-circle, mdi-palette id=TextStyle-backgroundColor-TextBox\n" +
                "                        innerRight\n" +
                "                          ColorBoxComponent\n" +
                "                            black\n" +
                "                    ColorPaletteComponent\n" +
                "                      TABLE\n" +
                "                        id=\"TextStyle-Table\" className=dui dui-menu-item\n" +
                "                          TBODY\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  \"Black\" DISABLED id=TextStyle-color-1-Link\n" +
                "                              TD\n" +
                "                                style=\"background-color: white; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  \"White\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/white] id=TextStyle-color-2-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-3-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-4-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-5-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-6-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-7-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-8-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-9-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-10-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-11-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-12-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-13-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-14-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-15-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-16-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-17-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-18-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-19-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-20-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-21-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-22-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-23-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-24-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-25-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-26-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-27-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-28-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-29-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-30-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-31-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-32-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-33-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-34-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-35-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-36-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-37-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-38-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-39-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-40-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-41-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-42-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-43-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-44-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-45-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-46-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-47-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-48-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-49-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-50-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-51-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-52-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-53-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-54-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-55-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-56-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                                  \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/] id=TextStyle-color-clear-Link\n" +
                "              TextStyleColorComponent\n" +
                "                TextStylePropertyColorComponent\n" +
                "                  ColorComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Color [black] icons=mdi-close-circle, mdi-palette id=TextStyle-color-TextBox\n" +
                "                          innerRight\n" +
                "                            ColorBoxComponent\n" +
                "                              black\n" +
                "                      ColorPaletteComponent\n" +
                "                        TABLE\n" +
                "                          id=\"TextStyle-Table\" className=dui dui-menu-item\n" +
                "                            TBODY\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    \"Black\" DISABLED id=TextStyle-color-1-Link\n" +
                "                                TD\n" +
                "                                  style=\"background-color: white; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    \"White\" [#/1/SpreadsheetName1/cell/A1/style/color/save/white] id=TextStyle-color-2-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-3-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-4-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-5-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-6-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-7-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-8-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-9-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-10-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-11-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-12-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-13-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-14-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-15-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-16-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-17-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-18-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-19-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-20-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-21-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-22-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-23-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-24-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-25-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-26-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-27-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-28-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-29-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-30-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-31-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-32-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-33-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-34-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-35-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-36-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-37-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-38-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-39-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-40-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-41-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-42-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-43-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-44-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-45-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-46-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-47-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-48-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-49-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-50-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-51-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-52-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-53-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-54-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-55-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-56-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                                    \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/color/save/] id=TextStyle-color-clear-Link\n" +
                "                DirectionComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Direction\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/direction/save/] id=TextStyle-direction-Link\n" +
                "                            \"Left to Right\" [#/1/SpreadsheetName1/cell/A1/style/direction/save/LTR] CHECKED id=TextStyle-direction-LTR-Link\n" +
                "                            \"Right to Left\" [#/1/SpreadsheetName1/cell/A1/style/direction/save/RTL] id=TextStyle-direction-RTL-Link\n" +
                "                FontFamilyComponent\n" +
                "                  SelectComponent\n" +
                "                    Font Family [Courier] id=TextStyle-Select\n" +
                "                      \"Courier\" DISABLED id=TextStyle-courier-Option\n" +
                "                      \"Sans Serif\" DISABLED id=TextStyle-sansSerif-Option\n" +
                "                      \"Times New Roman\" DISABLED id=TextStyle-timesNewRoman-Option\n" +
                "                FontKerningComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Font Kerning\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/font-kerning/save/] id=TextStyle-fontKerning-Link\n" +
                "                            \"Auto\" [#/1/SpreadsheetName1/cell/A1/style/font-kerning/save/AUTO] CHECKED id=TextStyle-fontKerning-AUTO-Link\n" +
                "                            \"None\" [#/1/SpreadsheetName1/cell/A1/style/font-kerning/save/NONE] id=TextStyle-fontKerning-NONE-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/font-kerning/save/NORMAL] id=TextStyle-fontKerning-NORMAL-Link\n" +
                "                FontSizeComponent\n" +
                "                  SuggestBoxComponent\n" +
                "                    Font Size [99]\n" +
                "                FontStretchComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Font Stretch\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/] id=TextStyle-fontStretch-Link\n" +
                "                            \"Ultra Condensed\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/ULTRA_CONDENSED] id=TextStyle-fontStretch-ULTRA_CONDENSED-Link\n" +
                "                            \"Extra Condensed\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/EXTRA_CONDENSED] id=TextStyle-fontStretch-EXTRA_CONDENSED-Link\n" +
                "                            \"Condensed\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/CONDENSED] id=TextStyle-fontStretch-CONDENSED-Link\n" +
                "                            \"Semi Condensed\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/SEMI_CONDENSED] id=TextStyle-fontStretch-SEMI_CONDENSED-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/NORMAL] CHECKED id=TextStyle-fontStretch-NORMAL-Link\n" +
                "                            \"Expanded\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/EXPANDED] id=TextStyle-fontStretch-EXPANDED-Link\n" +
                "                            \"Extra Expanded\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/EXTRA_EXPANDED] id=TextStyle-fontStretch-EXTRA_EXPANDED-Link\n" +
                "                            \"Ultra Expanded\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/ULTRA_EXPANDED] id=TextStyle-fontStretch-ULTRA_EXPANDED-Link\n" +
                "                FontStyleComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Font Style\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/font-style/save/] id=TextStyle-fontStyle-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/font-style/save/NORMAL] id=TextStyle-fontStyle-NORMAL-Link\n" +
                "                            mdi-format-italic \"Italic\" [#/1/SpreadsheetName1/cell/A1/style/font-style/save/ITALIC] CHECKED id=TextStyle-fontStyle-ITALIC-Link\n" +
                "                            \"Oblique\" [#/1/SpreadsheetName1/cell/A1/style/font-style/save/OBLIQUE] id=TextStyle-fontStyle-OBLIQUE-Link\n" +
                "                FontVariantComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Font Variant\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/font-variant/save/] id=TextStyle-fontVariant-Link\n" +
                "                            \"Initial\" [#/1/SpreadsheetName1/cell/A1/style/font-variant/save/INITIAL] id=TextStyle-fontVariant-INITIAL-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/font-variant/save/NORMAL] id=TextStyle-fontVariant-NORMAL-Link\n" +
                "                            \"Small Caps\" [#/1/SpreadsheetName1/cell/A1/style/font-variant/save/SMALL_CAPS] CHECKED id=TextStyle-fontVariant-SMALL_CAPS-Link\n" +
                "                FontWeightComponent\n" +
                "                  ValueTextBoxComponent\n" +
                "                    TextBoxComponent\n" +
                "                      Font Weight [BOLD] icons=mdi-close-circle id=TextStyle-fontWeight-TextBox\n" +
                "                HangingPunctuationComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Hanging Punctuation\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/hanging-punctuation/save/] id=TextStyle-hangingPunctuation-Link\n" +
                "                            \"None\" [#/1/SpreadsheetName1/cell/A1/style/hanging-punctuation/save/NONE] id=TextStyle-hangingPunctuation-NONE-Link\n" +
                "                            \"First\" [#/1/SpreadsheetName1/cell/A1/style/hanging-punctuation/save/FIRST] CHECKED id=TextStyle-hangingPunctuation-FIRST-Link\n" +
                "                            \"Last\" [#/1/SpreadsheetName1/cell/A1/style/hanging-punctuation/save/LAST] id=TextStyle-hangingPunctuation-LAST-Link\n" +
                "                            \"Allow End\" [#/1/SpreadsheetName1/cell/A1/style/hanging-punctuation/save/ALLOW_END] id=TextStyle-hangingPunctuation-ALLOW_END-Link\n" +
                "                            \"Force End\" [#/1/SpreadsheetName1/cell/A1/style/hanging-punctuation/save/FORCE_END] id=TextStyle-hangingPunctuation-FORCE_END-Link\n" +
                "                HeightComponent\n" +
                "                  LengthComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Height [55px] icons=mdi-close-circle id=TextStyle-height-TextBox\n" +
                "                HyphensComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Hyphens\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/hyphens/save/] id=TextStyle-hyphens-Link\n" +
                "                            \"None\" [#/1/SpreadsheetName1/cell/A1/style/hyphens/save/NONE] id=TextStyle-hyphens-NONE-Link\n" +
                "                            \"Manual\" [#/1/SpreadsheetName1/cell/A1/style/hyphens/save/MANUAL] id=TextStyle-hyphens-MANUAL-Link\n" +
                "                            \"Auto\" [#/1/SpreadsheetName1/cell/A1/style/hyphens/save/AUTO] CHECKED id=TextStyle-hyphens-AUTO-Link\n" +
                "                LetterSpacingComponent\n" +
                "                  LengthComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Letter Spacing [33px] icons=mdi-close-circle id=TextStyle-letterSpacing-TextBox\n" +
                "                LineHeightComponent\n" +
                "                  LengthComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Line Height [44px] icons=mdi-close-circle id=TextStyle-lineHeight-TextBox\n" +
                "                BigMarginComponent\n" +
                "                  FormElementComponent\n" +
                "                    label\n" +
                "                      Margin\n" +
                "                    FlexLayoutComponent\n" +
                "                      ROW\n" +
                "                        MarginTopComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Top [1px] icons=mdi-close-circle id=TextStyle-marginTop-TextBox\n" +
                "                        MarginRightComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Right [2px] icons=mdi-close-circle id=TextStyle-marginRight-TextBox\n" +
                "                        MarginBottomComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Bottom [3px] icons=mdi-close-circle id=TextStyle-marginBottom-TextBox\n" +
                "                        MarginLeftComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Left [4px] icons=mdi-close-circle id=TextStyle-marginLeft-TextBox\n" +
                "                        MarginComponent\n" +
                "                          ValueTextBoxComponent\n" +
                "                            TextBoxComponent\n" +
                "                              All [1px 2px 3px 4px] icons=mdi-close-circle id=TextStyle-margin-TextBox\n" +
                "                                innerRight\n" +
                "                                  MarginBoxComponent\n" +
                "                                    Margin\n" +
                "                                      ALL\n" +
                "                                        TextStyle\n" +
                "                                          margin-bottom=3px\n" +
                "                                          margin-left=4px\n" +
                "                                          margin-right=2px\n" +
                "                                          margin-top=1px\n" +
                "                OpacityComponent\n" +
                "                  ValueTextBoxComponent\n" +
                "                    TextBoxComponent\n" +
                "                      Opacity [0.5] icons=mdi-close-circle id=TextStyle-opacity-TextBox\n" +
                "                OverflowComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Overflow X\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/overflow-x/save/] id=TextStyle-overflowX-Link\n" +
                "                            \"Visible\" [#/1/SpreadsheetName1/cell/A1/style/overflow-x/save/VISIBLE] CHECKED id=TextStyle-overflowX-VISIBLE-Link\n" +
                "                            \"Hidden\" [#/1/SpreadsheetName1/cell/A1/style/overflow-x/save/HIDDEN] id=TextStyle-overflowX-HIDDEN-Link\n" +
                "                            \"Scroll\" [#/1/SpreadsheetName1/cell/A1/style/overflow-x/save/SCROLL] id=TextStyle-overflowX-SCROLL-Link\n" +
                "                            \"Auto\" [#/1/SpreadsheetName1/cell/A1/style/overflow-x/save/AUTO] id=TextStyle-overflowX-AUTO-Link\n" +
                "                OverflowComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Overflow Y\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/overflow-y/save/] id=TextStyle-overflowY-Link\n" +
                "                            \"Visible\" [#/1/SpreadsheetName1/cell/A1/style/overflow-y/save/VISIBLE] id=TextStyle-overflowY-VISIBLE-Link\n" +
                "                            \"Hidden\" [#/1/SpreadsheetName1/cell/A1/style/overflow-y/save/HIDDEN] CHECKED id=TextStyle-overflowY-HIDDEN-Link\n" +
                "                            \"Scroll\" [#/1/SpreadsheetName1/cell/A1/style/overflow-y/save/SCROLL] id=TextStyle-overflowY-SCROLL-Link\n" +
                "                            \"Auto\" [#/1/SpreadsheetName1/cell/A1/style/overflow-y/save/AUTO] id=TextStyle-overflowY-AUTO-Link\n" +
                "                OverflowWrapComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Overflow Wrap\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/overflow-wrap/save/] id=TextStyle-overflowWrap-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/overflow-wrap/save/NORMAL] id=TextStyle-overflowWrap-NORMAL-Link\n" +
                "                            \"Anywhere\" [#/1/SpreadsheetName1/cell/A1/style/overflow-wrap/save/ANYWHERE] id=TextStyle-overflowWrap-ANYWHERE-Link\n" +
                "                            \"Break Word\" [#/1/SpreadsheetName1/cell/A1/style/overflow-wrap/save/BREAK_WORD] CHECKED id=TextStyle-overflowWrap-BREAK_WORD-Link\n" +
                "                BigPaddingComponent\n" +
                "                  FormElementComponent\n" +
                "                    label\n" +
                "                      Padding\n" +
                "                    FlexLayoutComponent\n" +
                "                      ROW\n" +
                "                        PaddingTopComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Top [1px] icons=mdi-close-circle id=TextStyle-paddingTop-TextBox\n" +
                "                        PaddingRightComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Right [2px] icons=mdi-close-circle id=TextStyle-paddingRight-TextBox\n" +
                "                        PaddingBottomComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Bottom [3px] icons=mdi-close-circle id=TextStyle-paddingBottom-TextBox\n" +
                "                        PaddingLeftComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Left [4px] icons=mdi-close-circle id=TextStyle-paddingLeft-TextBox\n" +
                "                        PaddingComponent\n" +
                "                          ValueTextBoxComponent\n" +
                "                            TextBoxComponent\n" +
                "                              All [1px 2px 3px 4px] icons=mdi-close-circle id=TextStyle-padding-TextBox\n" +
                "                                innerRight\n" +
                "                                  PaddingBoxComponent\n" +
                "                                    Padding\n" +
                "                                      ALL\n" +
                "                                        TextStyle\n" +
                "                                          padding-bottom=3px\n" +
                "                                          padding-left=4px\n" +
                "                                          padding-right=2px\n" +
                "                                          padding-top=1px\n" +
                "                TextAlignComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Text Align\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            mdi-format-clear \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/text-align/save/] id=TextStyle-textAlign-Link\n" +
                "                            mdi-format-align-left \"Left\" [#/1/SpreadsheetName1/cell/A1/style/text-align/save/LEFT] id=TextStyle-textAlign-LEFT-Link\n" +
                "                            mdi-format-align-center \"Center\" [#/1/SpreadsheetName1/cell/A1/style/text-align/save/CENTER] CHECKED id=TextStyle-textAlign-CENTER-Link\n" +
                "                            mdi-format-align-right \"Right\" [#/1/SpreadsheetName1/cell/A1/style/text-align/save/RIGHT] id=TextStyle-textAlign-RIGHT-Link\n" +
                "                            mdi-format-align-justify \"Justify\" [#/1/SpreadsheetName1/cell/A1/style/text-align/save/JUSTIFY] id=TextStyle-textAlign-JUSTIFY-Link\n" +
                "                TextDecorationLineComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Text Decoration Line\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-line/save/] id=TextStyle-textDecorationLine-Link\n" +
                "                            mdi-format-clear \"None\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-line/save/NONE] id=TextStyle-textDecorationLine-NONE-Link\n" +
                "                            mdi-format-strikethrough \"Strikethrough\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-line/save/LINE_THROUGH] id=TextStyle-textDecorationLine-LINE_THROUGH-Link\n" +
                "                            \"Overline\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-line/save/OVERLINE] CHECKED id=TextStyle-textDecorationLine-OVERLINE-Link\n" +
                "                            mdi-format-underline \"Underline\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-line/save/UNDERLINE] id=TextStyle-textDecorationLine-UNDERLINE-Link\n" +
                "                TextDecorationStyleComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Text Decoration Style\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-style/save/] id=TextStyle-textDecorationStyle-Link\n" +
                "                            \"Solid\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-style/save/SOLID] id=TextStyle-textDecorationStyle-SOLID-Link\n" +
                "                            \"Double\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-style/save/DOUBLE] id=TextStyle-textDecorationStyle-DOUBLE-Link\n" +
                "                            \"Dashed\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-style/save/DASHED] id=TextStyle-textDecorationStyle-DASHED-Link\n" +
                "                            \"Dotted\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-style/save/DOTTED] CHECKED id=TextStyle-textDecorationStyle-DOTTED-Link\n" +
                "                            \"Wavy\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-style/save/WAVY] id=TextStyle-textDecorationStyle-WAVY-Link\n" +
                "                TextDecorationThicknessComponent\n" +
                "                  LengthComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Text Decoration Thickness [123px] icons=mdi-close-circle id=TextStyle-textDecorationThickness-TextBox\n" +
                "                TextIndentComponent\n" +
                "                  LengthComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Text Indent [123px] icons=mdi-close-circle id=TextStyle-textIndent-TextBox\n" +
                "                TextJustifyComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Text Justify\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/text-justify/save/] id=TextStyle-textJustify-Link\n" +
                "                            \"Auto\" [#/1/SpreadsheetName1/cell/A1/style/text-justify/save/AUTO] id=TextStyle-textJustify-AUTO-Link\n" +
                "                            \"Inter Word\" [#/1/SpreadsheetName1/cell/A1/style/text-justify/save/INTER_WORD] CHECKED id=TextStyle-textJustify-INTER_WORD-Link\n" +
                "                            \"Inter Character\" [#/1/SpreadsheetName1/cell/A1/style/text-justify/save/INTER_CHARACTER] id=TextStyle-textJustify-INTER_CHARACTER-Link\n" +
                "                TextTransformComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Text Transform\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/text-transform/save/] id=TextStyle-textTransform-Link\n" +
                "                            \"None\" [#/1/SpreadsheetName1/cell/A1/style/text-transform/save/NONE] id=TextStyle-textTransform-NONE-Link\n" +
                "                            mdi-format-letter-case \"Capitalize\" [#/1/SpreadsheetName1/cell/A1/style/text-transform/save/CAPITALIZE] id=TextStyle-textTransform-CAPITALIZE-Link\n" +
                "                            mdi-format-letter-case-upper \"Uppercase\" [#/1/SpreadsheetName1/cell/A1/style/text-transform/save/UPPERCASE] CHECKED id=TextStyle-textTransform-UPPERCASE-Link\n" +
                "                            mdi-format-letter-case-lower \"Lowercase\" [#/1/SpreadsheetName1/cell/A1/style/text-transform/save/LOWERCASE] id=TextStyle-textTransform-LOWERCASE-Link\n" +
                "                TextWrappingComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Text Wrapping\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/text-wrapping/save/] id=TextStyle-textWrapping-Link\n" +
                "                            \"Overflow\" [#/1/SpreadsheetName1/cell/A1/style/text-wrapping/save/OVERFLOW] id=TextStyle-textWrapping-OVERFLOW-Link\n" +
                "                            \"Wrap\" [#/1/SpreadsheetName1/cell/A1/style/text-wrapping/save/WRAP] CHECKED id=TextStyle-textWrapping-WRAP-Link\n" +
                "                            \"Clip\" [#/1/SpreadsheetName1/cell/A1/style/text-wrapping/save/CLIP] id=TextStyle-textWrapping-CLIP-Link\n" +
                "                VerticalAlignComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Vertical Align\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            mdi-format-clear \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/vertical-align/save/] id=TextStyle-verticalAlign-Link\n" +
                "                            mdi-format-align-top \"Top\" [#/1/SpreadsheetName1/cell/A1/style/vertical-align/save/TOP] id=TextStyle-verticalAlign-TOP-Link\n" +
                "                            mdi-format-align-middle \"Middle\" [#/1/SpreadsheetName1/cell/A1/style/vertical-align/save/MIDDLE] CHECKED id=TextStyle-verticalAlign-MIDDLE-Link\n" +
                "                            mdi-format-align-bottom \"Bottom\" [#/1/SpreadsheetName1/cell/A1/style/vertical-align/save/BOTTOM] id=TextStyle-verticalAlign-BOTTOM-Link\n" +
                "                VisibilityComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Visibility\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/visibility/save/] id=TextStyle-visibility-Link\n" +
                "                            \"Visible\" [#/1/SpreadsheetName1/cell/A1/style/visibility/save/VISIBLE] CHECKED id=TextStyle-visibility-VISIBLE-Link\n" +
                "                            \"Hidden\" [#/1/SpreadsheetName1/cell/A1/style/visibility/save/HIDDEN] id=TextStyle-visibility-HIDDEN-Link\n" +
                "                            \"Collapse\" [#/1/SpreadsheetName1/cell/A1/style/visibility/save/COLLAPSE] id=TextStyle-visibility-COLLAPSE-Link\n" +
                "                WhitespaceComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    White Space\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/white-space/save/] id=TextStyle-whiteSpace-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/white-space/save/NORMAL] id=TextStyle-whiteSpace-NORMAL-Link\n" +
                "                            \"Nowrap\" [#/1/SpreadsheetName1/cell/A1/style/white-space/save/NOWRAP] CHECKED id=TextStyle-whiteSpace-NOWRAP-Link\n" +
                "                            \"Pre\" [#/1/SpreadsheetName1/cell/A1/style/white-space/save/PRE] id=TextStyle-whiteSpace-PRE-Link\n" +
                "                            \"Pre Line\" [#/1/SpreadsheetName1/cell/A1/style/white-space/save/PRE_LINE] id=TextStyle-whiteSpace-PRE_LINE-Link\n" +
                "                            \"Pre Wrap\" [#/1/SpreadsheetName1/cell/A1/style/white-space/save/PRE_WRAP] id=TextStyle-whiteSpace-PRE_WRAP-Link\n" +
                "                WidthComponent\n" +
                "                  LengthComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Width [44px] icons=mdi-close-circle id=TextStyle-width-TextBox\n" +
                "                WordBreakComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Word Break\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/word-break/save/] id=TextStyle-wordBreak-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/word-break/save/NORMAL] id=TextStyle-wordBreak-NORMAL-Link\n" +
                "                            \"Break All\" [#/1/SpreadsheetName1/cell/A1/style/word-break/save/BREAK_ALL] id=TextStyle-wordBreak-BREAK_ALL-Link\n" +
                "                            \"Keep All\" [#/1/SpreadsheetName1/cell/A1/style/word-break/save/KEEP_ALL] id=TextStyle-wordBreak-KEEP_ALL-Link\n" +
                "                            \"Break Word\" [#/1/SpreadsheetName1/cell/A1/style/word-break/save/BREAK_WORD] CHECKED id=TextStyle-wordBreak-BREAK_WORD-Link\n" +
                "                WordWrapComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Word Wrap\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/word-wrap/save/] id=TextStyle-wordWrap-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/word-wrap/save/NORMAL] id=TextStyle-wordWrap-NORMAL-Link\n" +
                "                            \"Break Word\" [#/1/SpreadsheetName1/cell/A1/style/word-wrap/save/BREAK_WORD] CHECKED id=TextStyle-wordWrap-BREAK_WORD-Link\n" +
                "                WritingModeComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Writing Mode\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/writing-mode/save/] id=TextStyle-writingMode-Link\n" +
                "                            \"Horizontal Tb\" [#/1/SpreadsheetName1/cell/A1/style/writing-mode/save/HORIZONTAL_TB] CHECKED id=TextStyle-writingMode-HORIZONTAL_TB-Link\n" +
                "                            \"Vertical Lr\" [#/1/SpreadsheetName1/cell/A1/style/writing-mode/save/VERTICAL_LR] id=TextStyle-writingMode-VERTICAL_LR-Link\n" +
                "                            \"Vertical Rl\" [#/1/SpreadsheetName1/cell/A1/style/writing-mode/save/VERTICAL_RL] id=TextStyle-writingMode-VERTICAL_RL-Link\n" +
                "          TextStyleComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Style [!invalid] icons=mdi-close-circle\n" +
                "                Errors\n" +
                "                  Invalid character '!' at 0\n" +
                "          DialogAnchorListComponent\n" +
                "            AnchorListComponent\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Save\" DISABLED id=TextStyle-save-Link\n" +
                "                  \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/*/save/] id=TextStyle-clear-Link\n" +
                "                  \"Undo\" [#/1/SpreadsheetName1/cell/A1/style/*/save/] id=TextStyle-undo-Link\n" +
                "                  \"Close\" [#/1/SpreadsheetName1/cell/A1] id=TextStyle-close-Link\n"
        );
    }

    // setValue.........................................................................................................

    @Test
    public void testSetValueWithCellStyleWithoutTextStylePropertyName() {
        final SpreadsheetCellReference cell = SpreadsheetSelection.A1;

        final AppContext context = this.appContext(
            HistoryToken.cellStyle(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                cell.setDefaultAnchor(),
                Optional.empty()
            ),
            Optional.empty() // no textStyle
        );

        context.spreadsheetViewportCache()
            .onSpreadsheetMetadata(
                METADATA
            );

        context.spreadsheetViewportCache()
            .onSpreadsheetDelta(
                HttpMethod.GET,
                Url.parseRelative("/api/spreadsheet/1/cell"),
                SpreadsheetDelta.EMPTY.setCells(
                    Sets.of(
                        cell.setFormula(SpreadsheetFormula.EMPTY) // no textStyle
                    )
                )
            );

        final TextStyleDialogComponent component = TextStyleDialogComponent.with(
            TextStyleDialogComponentContexts.appContextCellStyle(context)
        );

        component.onHistoryTokenChange(
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            ),
            context
        );

        component.textStyle.setValue(
            Optional.of(VALUE_TEXT_STYLE)
        );

        this.treePrintAndCheck(
            component,
            "TextStyleDialogComponent\n" +
                "  DialogComponent\n" +
                "    A1: *\n" +
                "    id=TextStyle-Dialog includeClose=true\n" +
                "      SpreadsheetExpressionReferenceComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Selection [A1] icons=mdi-close-circle id=TextStyle-selection-TextBox REQUIRED\n" +
                "      TextStyleSampleComponent\n" +
                "        DIV\n" +
                "          style=\"background-color: black; color: black; direction: LTR; font-family: Courier; font-kerning: AUTO; font-size: 99; font-stretch: NORMAL; font-style: ITALIC; font-variant: SMALL_CAPS; font-weight: BOLD; hanging-punctuation: FIRST; height: 55px; hyphens: AUTO; letter-spacing: 33px; line-height: 44px; margin-bottom: 3px; margin-left: 4px; margin-right: 2px; margin-top: 1px; opacity: 50%; overflow-wrap: BREAK_WORD; overflow-x: VISIBLE; overflow-y: HIDDEN; padding-bottom: 3px; padding-left: 4px; padding-right: 2px; padding-top: 1px; text-align: CENTER; text-decoration-line: OVERLINE; text-decoration-style: DOTTED; text-decoration-thickness: 123px; text-indent: 123px; text-justify: INTER_WORD; text-transform: UPPERCASE; text-wrapping: WRAP; vertical-align: MIDDLE; visibility: VISIBLE; white-space: NOWRAP; width: 44px; word-break: BREAK_WORD; word-wrap: BREAK_WORD; writing-mode: HORIZONTAL_TB;\"\n" +
                "            \"The quick brown fox jumps over the lazy dog\"\n" +
                "      ThreeColumnComponent\n" +
                "        DIV\n" +
                "          style=\"display: grid; gap: 5px; grid-template-columns: 33% 33% 33%;\"\n" +
                "            BackgroundColorComponent\n" +
                "              TextStylePropertyColorComponent\n" +
                "                ColorComponent\n" +
                "                  ValueTextBoxComponent\n" +
                "                    TextBoxComponent\n" +
                "                      Background Color [black] icons=mdi-close-circle, mdi-palette id=TextStyle-backgroundColor-TextBox\n" +
                "                        innerRight\n" +
                "                          ColorBoxComponent\n" +
                "                            black\n" +
                "                    ColorPaletteComponent\n" +
                "                      TABLE\n" +
                "                        id=\"TextStyle-Table\" className=dui dui-menu-item\n" +
                "                          TBODY\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  \"Black\" DISABLED id=TextStyle-color-1-Link\n" +
                "                              TD\n" +
                "                                style=\"background-color: white; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  \"White\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/white] id=TextStyle-color-2-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-3-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-4-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-5-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-6-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-7-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-8-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-9-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-10-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-11-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-12-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-13-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-14-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-15-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-16-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-17-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-18-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-19-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-20-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-21-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-22-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-23-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-24-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-25-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-26-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-27-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-28-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-29-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-30-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-31-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-32-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-33-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-34-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-35-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-36-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-37-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-38-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-39-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-40-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-41-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-42-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-43-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-44-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-45-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-46-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-47-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-48-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-49-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-50-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-51-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-52-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-53-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-54-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-55-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-56-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                                  \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/background-color/save/] id=TextStyle-color-clear-Link\n" +
                "              TextStyleColorComponent\n" +
                "                TextStylePropertyColorComponent\n" +
                "                  ColorComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Color [black] icons=mdi-close-circle, mdi-palette id=TextStyle-color-TextBox\n" +
                "                          innerRight\n" +
                "                            ColorBoxComponent\n" +
                "                              black\n" +
                "                      ColorPaletteComponent\n" +
                "                        TABLE\n" +
                "                          id=\"TextStyle-Table\" className=dui dui-menu-item\n" +
                "                            TBODY\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    \"Black\" DISABLED id=TextStyle-color-1-Link\n" +
                "                                TD\n" +
                "                                  style=\"background-color: white; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    \"White\" [#/1/SpreadsheetName1/cell/A1/style/color/save/white] id=TextStyle-color-2-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-3-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-4-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-5-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-6-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-7-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-8-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-9-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-10-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-11-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-12-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-13-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-14-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-15-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-16-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-17-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-18-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-19-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-20-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-21-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-22-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-23-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-24-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-25-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-26-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-27-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-28-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-29-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-30-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-31-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-32-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-33-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-34-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-35-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-36-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-37-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-38-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-39-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-40-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-41-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-42-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-43-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-44-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-45-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-46-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-47-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-48-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-49-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-50-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-51-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-52-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-53-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-54-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-55-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-56-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                                    \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/color/save/] id=TextStyle-color-clear-Link\n" +
                "                DirectionComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Direction\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/direction/save/] id=TextStyle-direction-Link\n" +
                "                            \"Left to Right\" [#/1/SpreadsheetName1/cell/A1/style/direction/save/LTR] CHECKED id=TextStyle-direction-LTR-Link\n" +
                "                            \"Right to Left\" [#/1/SpreadsheetName1/cell/A1/style/direction/save/RTL] id=TextStyle-direction-RTL-Link\n" +
                "                FontFamilyComponent\n" +
                "                  SelectComponent\n" +
                "                    Font Family [Courier] id=TextStyle-Select\n" +
                "                      \"Courier\" DISABLED id=TextStyle-courier-Option\n" +
                "                      \"Sans Serif\" DISABLED id=TextStyle-sansSerif-Option\n" +
                "                      \"Times New Roman\" DISABLED id=TextStyle-timesNewRoman-Option\n" +
                "                FontKerningComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Font Kerning\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/font-kerning/save/] id=TextStyle-fontKerning-Link\n" +
                "                            \"Auto\" [#/1/SpreadsheetName1/cell/A1/style/font-kerning/save/AUTO] CHECKED id=TextStyle-fontKerning-AUTO-Link\n" +
                "                            \"None\" [#/1/SpreadsheetName1/cell/A1/style/font-kerning/save/NONE] id=TextStyle-fontKerning-NONE-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/font-kerning/save/NORMAL] id=TextStyle-fontKerning-NORMAL-Link\n" +
                "                FontSizeComponent\n" +
                "                  SuggestBoxComponent\n" +
                "                    Font Size [99]\n" +
                "                FontStretchComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Font Stretch\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/] id=TextStyle-fontStretch-Link\n" +
                "                            \"Ultra Condensed\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/ULTRA_CONDENSED] id=TextStyle-fontStretch-ULTRA_CONDENSED-Link\n" +
                "                            \"Extra Condensed\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/EXTRA_CONDENSED] id=TextStyle-fontStretch-EXTRA_CONDENSED-Link\n" +
                "                            \"Condensed\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/CONDENSED] id=TextStyle-fontStretch-CONDENSED-Link\n" +
                "                            \"Semi Condensed\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/SEMI_CONDENSED] id=TextStyle-fontStretch-SEMI_CONDENSED-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/NORMAL] CHECKED id=TextStyle-fontStretch-NORMAL-Link\n" +
                "                            \"Expanded\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/EXPANDED] id=TextStyle-fontStretch-EXPANDED-Link\n" +
                "                            \"Extra Expanded\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/EXTRA_EXPANDED] id=TextStyle-fontStretch-EXTRA_EXPANDED-Link\n" +
                "                            \"Ultra Expanded\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/ULTRA_EXPANDED] id=TextStyle-fontStretch-ULTRA_EXPANDED-Link\n" +
                "                FontStyleComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Font Style\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/font-style/save/] id=TextStyle-fontStyle-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/font-style/save/NORMAL] id=TextStyle-fontStyle-NORMAL-Link\n" +
                "                            mdi-format-italic \"Italic\" [#/1/SpreadsheetName1/cell/A1/style/font-style/save/ITALIC] CHECKED id=TextStyle-fontStyle-ITALIC-Link\n" +
                "                            \"Oblique\" [#/1/SpreadsheetName1/cell/A1/style/font-style/save/OBLIQUE] id=TextStyle-fontStyle-OBLIQUE-Link\n" +
                "                FontVariantComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Font Variant\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/font-variant/save/] id=TextStyle-fontVariant-Link\n" +
                "                            \"Initial\" [#/1/SpreadsheetName1/cell/A1/style/font-variant/save/INITIAL] id=TextStyle-fontVariant-INITIAL-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/font-variant/save/NORMAL] id=TextStyle-fontVariant-NORMAL-Link\n" +
                "                            \"Small Caps\" [#/1/SpreadsheetName1/cell/A1/style/font-variant/save/SMALL_CAPS] CHECKED id=TextStyle-fontVariant-SMALL_CAPS-Link\n" +
                "                FontWeightComponent\n" +
                "                  ValueTextBoxComponent\n" +
                "                    TextBoxComponent\n" +
                "                      Font Weight [BOLD] icons=mdi-close-circle id=TextStyle-fontWeight-TextBox\n" +
                "                HangingPunctuationComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Hanging Punctuation\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/hanging-punctuation/save/] id=TextStyle-hangingPunctuation-Link\n" +
                "                            \"None\" [#/1/SpreadsheetName1/cell/A1/style/hanging-punctuation/save/NONE] id=TextStyle-hangingPunctuation-NONE-Link\n" +
                "                            \"First\" [#/1/SpreadsheetName1/cell/A1/style/hanging-punctuation/save/FIRST] CHECKED id=TextStyle-hangingPunctuation-FIRST-Link\n" +
                "                            \"Last\" [#/1/SpreadsheetName1/cell/A1/style/hanging-punctuation/save/LAST] id=TextStyle-hangingPunctuation-LAST-Link\n" +
                "                            \"Allow End\" [#/1/SpreadsheetName1/cell/A1/style/hanging-punctuation/save/ALLOW_END] id=TextStyle-hangingPunctuation-ALLOW_END-Link\n" +
                "                            \"Force End\" [#/1/SpreadsheetName1/cell/A1/style/hanging-punctuation/save/FORCE_END] id=TextStyle-hangingPunctuation-FORCE_END-Link\n" +
                "                HeightComponent\n" +
                "                  LengthComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Height [55px] icons=mdi-close-circle id=TextStyle-height-TextBox\n" +
                "                HyphensComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Hyphens\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/hyphens/save/] id=TextStyle-hyphens-Link\n" +
                "                            \"None\" [#/1/SpreadsheetName1/cell/A1/style/hyphens/save/NONE] id=TextStyle-hyphens-NONE-Link\n" +
                "                            \"Manual\" [#/1/SpreadsheetName1/cell/A1/style/hyphens/save/MANUAL] id=TextStyle-hyphens-MANUAL-Link\n" +
                "                            \"Auto\" [#/1/SpreadsheetName1/cell/A1/style/hyphens/save/AUTO] CHECKED id=TextStyle-hyphens-AUTO-Link\n" +
                "                LetterSpacingComponent\n" +
                "                  LengthComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Letter Spacing [33px] icons=mdi-close-circle id=TextStyle-letterSpacing-TextBox\n" +
                "                LineHeightComponent\n" +
                "                  LengthComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Line Height [44px] icons=mdi-close-circle id=TextStyle-lineHeight-TextBox\n" +
                "                BigMarginComponent\n" +
                "                  FormElementComponent\n" +
                "                    label\n" +
                "                      Margin\n" +
                "                    FlexLayoutComponent\n" +
                "                      ROW\n" +
                "                        MarginTopComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Top [1px] icons=mdi-close-circle id=TextStyle-marginTop-TextBox\n" +
                "                        MarginRightComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Right [2px] icons=mdi-close-circle id=TextStyle-marginRight-TextBox\n" +
                "                        MarginBottomComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Bottom [3px] icons=mdi-close-circle id=TextStyle-marginBottom-TextBox\n" +
                "                        MarginLeftComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Left [4px] icons=mdi-close-circle id=TextStyle-marginLeft-TextBox\n" +
                "                        MarginComponent\n" +
                "                          ValueTextBoxComponent\n" +
                "                            TextBoxComponent\n" +
                "                              All [1px 2px 3px 4px] icons=mdi-close-circle id=TextStyle-margin-TextBox\n" +
                "                                innerRight\n" +
                "                                  MarginBoxComponent\n" +
                "                                    Margin\n" +
                "                                      ALL\n" +
                "                                        TextStyle\n" +
                "                                          margin-bottom=3px\n" +
                "                                          margin-left=4px\n" +
                "                                          margin-right=2px\n" +
                "                                          margin-top=1px\n" +
                "                OpacityComponent\n" +
                "                  ValueTextBoxComponent\n" +
                "                    TextBoxComponent\n" +
                "                      Opacity [0.5] icons=mdi-close-circle id=TextStyle-opacity-TextBox\n" +
                "                OverflowComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Overflow X\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/overflow-x/save/] id=TextStyle-overflowX-Link\n" +
                "                            \"Visible\" [#/1/SpreadsheetName1/cell/A1/style/overflow-x/save/VISIBLE] CHECKED id=TextStyle-overflowX-VISIBLE-Link\n" +
                "                            \"Hidden\" [#/1/SpreadsheetName1/cell/A1/style/overflow-x/save/HIDDEN] id=TextStyle-overflowX-HIDDEN-Link\n" +
                "                            \"Scroll\" [#/1/SpreadsheetName1/cell/A1/style/overflow-x/save/SCROLL] id=TextStyle-overflowX-SCROLL-Link\n" +
                "                            \"Auto\" [#/1/SpreadsheetName1/cell/A1/style/overflow-x/save/AUTO] id=TextStyle-overflowX-AUTO-Link\n" +
                "                OverflowComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Overflow Y\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/overflow-y/save/] id=TextStyle-overflowY-Link\n" +
                "                            \"Visible\" [#/1/SpreadsheetName1/cell/A1/style/overflow-y/save/VISIBLE] id=TextStyle-overflowY-VISIBLE-Link\n" +
                "                            \"Hidden\" [#/1/SpreadsheetName1/cell/A1/style/overflow-y/save/HIDDEN] CHECKED id=TextStyle-overflowY-HIDDEN-Link\n" +
                "                            \"Scroll\" [#/1/SpreadsheetName1/cell/A1/style/overflow-y/save/SCROLL] id=TextStyle-overflowY-SCROLL-Link\n" +
                "                            \"Auto\" [#/1/SpreadsheetName1/cell/A1/style/overflow-y/save/AUTO] id=TextStyle-overflowY-AUTO-Link\n" +
                "                OverflowWrapComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Overflow Wrap\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/overflow-wrap/save/] id=TextStyle-overflowWrap-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/overflow-wrap/save/NORMAL] id=TextStyle-overflowWrap-NORMAL-Link\n" +
                "                            \"Anywhere\" [#/1/SpreadsheetName1/cell/A1/style/overflow-wrap/save/ANYWHERE] id=TextStyle-overflowWrap-ANYWHERE-Link\n" +
                "                            \"Break Word\" [#/1/SpreadsheetName1/cell/A1/style/overflow-wrap/save/BREAK_WORD] CHECKED id=TextStyle-overflowWrap-BREAK_WORD-Link\n" +
                "                BigPaddingComponent\n" +
                "                  FormElementComponent\n" +
                "                    label\n" +
                "                      Padding\n" +
                "                    FlexLayoutComponent\n" +
                "                      ROW\n" +
                "                        PaddingTopComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Top [1px] icons=mdi-close-circle id=TextStyle-paddingTop-TextBox\n" +
                "                        PaddingRightComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Right [2px] icons=mdi-close-circle id=TextStyle-paddingRight-TextBox\n" +
                "                        PaddingBottomComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Bottom [3px] icons=mdi-close-circle id=TextStyle-paddingBottom-TextBox\n" +
                "                        PaddingLeftComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Left [4px] icons=mdi-close-circle id=TextStyle-paddingLeft-TextBox\n" +
                "                        PaddingComponent\n" +
                "                          ValueTextBoxComponent\n" +
                "                            TextBoxComponent\n" +
                "                              All [1px 2px 3px 4px] icons=mdi-close-circle id=TextStyle-padding-TextBox\n" +
                "                                innerRight\n" +
                "                                  PaddingBoxComponent\n" +
                "                                    Padding\n" +
                "                                      ALL\n" +
                "                                        TextStyle\n" +
                "                                          padding-bottom=3px\n" +
                "                                          padding-left=4px\n" +
                "                                          padding-right=2px\n" +
                "                                          padding-top=1px\n" +
                "                TextAlignComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Text Align\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            mdi-format-clear \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/text-align/save/] id=TextStyle-textAlign-Link\n" +
                "                            mdi-format-align-left \"Left\" [#/1/SpreadsheetName1/cell/A1/style/text-align/save/LEFT] id=TextStyle-textAlign-LEFT-Link\n" +
                "                            mdi-format-align-center \"Center\" [#/1/SpreadsheetName1/cell/A1/style/text-align/save/CENTER] CHECKED id=TextStyle-textAlign-CENTER-Link\n" +
                "                            mdi-format-align-right \"Right\" [#/1/SpreadsheetName1/cell/A1/style/text-align/save/RIGHT] id=TextStyle-textAlign-RIGHT-Link\n" +
                "                            mdi-format-align-justify \"Justify\" [#/1/SpreadsheetName1/cell/A1/style/text-align/save/JUSTIFY] id=TextStyle-textAlign-JUSTIFY-Link\n" +
                "                TextDecorationLineComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Text Decoration Line\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-line/save/] id=TextStyle-textDecorationLine-Link\n" +
                "                            mdi-format-clear \"None\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-line/save/NONE] id=TextStyle-textDecorationLine-NONE-Link\n" +
                "                            mdi-format-strikethrough \"Strikethrough\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-line/save/LINE_THROUGH] id=TextStyle-textDecorationLine-LINE_THROUGH-Link\n" +
                "                            \"Overline\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-line/save/OVERLINE] CHECKED id=TextStyle-textDecorationLine-OVERLINE-Link\n" +
                "                            mdi-format-underline \"Underline\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-line/save/UNDERLINE] id=TextStyle-textDecorationLine-UNDERLINE-Link\n" +
                "                TextDecorationStyleComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Text Decoration Style\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-style/save/] id=TextStyle-textDecorationStyle-Link\n" +
                "                            \"Solid\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-style/save/SOLID] id=TextStyle-textDecorationStyle-SOLID-Link\n" +
                "                            \"Double\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-style/save/DOUBLE] id=TextStyle-textDecorationStyle-DOUBLE-Link\n" +
                "                            \"Dashed\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-style/save/DASHED] id=TextStyle-textDecorationStyle-DASHED-Link\n" +
                "                            \"Dotted\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-style/save/DOTTED] CHECKED id=TextStyle-textDecorationStyle-DOTTED-Link\n" +
                "                            \"Wavy\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-style/save/WAVY] id=TextStyle-textDecorationStyle-WAVY-Link\n" +
                "                TextDecorationThicknessComponent\n" +
                "                  LengthComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Text Decoration Thickness [123px] icons=mdi-close-circle id=TextStyle-textDecorationThickness-TextBox\n" +
                "                TextIndentComponent\n" +
                "                  LengthComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Text Indent [123px] icons=mdi-close-circle id=TextStyle-textIndent-TextBox\n" +
                "                TextJustifyComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Text Justify\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/text-justify/save/] id=TextStyle-textJustify-Link\n" +
                "                            \"Auto\" [#/1/SpreadsheetName1/cell/A1/style/text-justify/save/AUTO] id=TextStyle-textJustify-AUTO-Link\n" +
                "                            \"Inter Word\" [#/1/SpreadsheetName1/cell/A1/style/text-justify/save/INTER_WORD] CHECKED id=TextStyle-textJustify-INTER_WORD-Link\n" +
                "                            \"Inter Character\" [#/1/SpreadsheetName1/cell/A1/style/text-justify/save/INTER_CHARACTER] id=TextStyle-textJustify-INTER_CHARACTER-Link\n" +
                "                TextTransformComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Text Transform\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/text-transform/save/] id=TextStyle-textTransform-Link\n" +
                "                            \"None\" [#/1/SpreadsheetName1/cell/A1/style/text-transform/save/NONE] id=TextStyle-textTransform-NONE-Link\n" +
                "                            mdi-format-letter-case \"Capitalize\" [#/1/SpreadsheetName1/cell/A1/style/text-transform/save/CAPITALIZE] id=TextStyle-textTransform-CAPITALIZE-Link\n" +
                "                            mdi-format-letter-case-upper \"Uppercase\" [#/1/SpreadsheetName1/cell/A1/style/text-transform/save/UPPERCASE] CHECKED id=TextStyle-textTransform-UPPERCASE-Link\n" +
                "                            mdi-format-letter-case-lower \"Lowercase\" [#/1/SpreadsheetName1/cell/A1/style/text-transform/save/LOWERCASE] id=TextStyle-textTransform-LOWERCASE-Link\n" +
                "                TextWrappingComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Text Wrapping\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/text-wrapping/save/] id=TextStyle-textWrapping-Link\n" +
                "                            \"Overflow\" [#/1/SpreadsheetName1/cell/A1/style/text-wrapping/save/OVERFLOW] id=TextStyle-textWrapping-OVERFLOW-Link\n" +
                "                            \"Wrap\" [#/1/SpreadsheetName1/cell/A1/style/text-wrapping/save/WRAP] CHECKED id=TextStyle-textWrapping-WRAP-Link\n" +
                "                            \"Clip\" [#/1/SpreadsheetName1/cell/A1/style/text-wrapping/save/CLIP] id=TextStyle-textWrapping-CLIP-Link\n" +
                "                VerticalAlignComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Vertical Align\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            mdi-format-clear \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/vertical-align/save/] id=TextStyle-verticalAlign-Link\n" +
                "                            mdi-format-align-top \"Top\" [#/1/SpreadsheetName1/cell/A1/style/vertical-align/save/TOP] id=TextStyle-verticalAlign-TOP-Link\n" +
                "                            mdi-format-align-middle \"Middle\" [#/1/SpreadsheetName1/cell/A1/style/vertical-align/save/MIDDLE] CHECKED id=TextStyle-verticalAlign-MIDDLE-Link\n" +
                "                            mdi-format-align-bottom \"Bottom\" [#/1/SpreadsheetName1/cell/A1/style/vertical-align/save/BOTTOM] id=TextStyle-verticalAlign-BOTTOM-Link\n" +
                "                VisibilityComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Visibility\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/visibility/save/] id=TextStyle-visibility-Link\n" +
                "                            \"Visible\" [#/1/SpreadsheetName1/cell/A1/style/visibility/save/VISIBLE] CHECKED id=TextStyle-visibility-VISIBLE-Link\n" +
                "                            \"Hidden\" [#/1/SpreadsheetName1/cell/A1/style/visibility/save/HIDDEN] id=TextStyle-visibility-HIDDEN-Link\n" +
                "                            \"Collapse\" [#/1/SpreadsheetName1/cell/A1/style/visibility/save/COLLAPSE] id=TextStyle-visibility-COLLAPSE-Link\n" +
                "                WhitespaceComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    White Space\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/white-space/save/] id=TextStyle-whiteSpace-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/white-space/save/NORMAL] id=TextStyle-whiteSpace-NORMAL-Link\n" +
                "                            \"Nowrap\" [#/1/SpreadsheetName1/cell/A1/style/white-space/save/NOWRAP] CHECKED id=TextStyle-whiteSpace-NOWRAP-Link\n" +
                "                            \"Pre\" [#/1/SpreadsheetName1/cell/A1/style/white-space/save/PRE] id=TextStyle-whiteSpace-PRE-Link\n" +
                "                            \"Pre Line\" [#/1/SpreadsheetName1/cell/A1/style/white-space/save/PRE_LINE] id=TextStyle-whiteSpace-PRE_LINE-Link\n" +
                "                            \"Pre Wrap\" [#/1/SpreadsheetName1/cell/A1/style/white-space/save/PRE_WRAP] id=TextStyle-whiteSpace-PRE_WRAP-Link\n" +
                "                WidthComponent\n" +
                "                  LengthComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Width [44px] icons=mdi-close-circle id=TextStyle-width-TextBox\n" +
                "                WordBreakComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Word Break\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/word-break/save/] id=TextStyle-wordBreak-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/word-break/save/NORMAL] id=TextStyle-wordBreak-NORMAL-Link\n" +
                "                            \"Break All\" [#/1/SpreadsheetName1/cell/A1/style/word-break/save/BREAK_ALL] id=TextStyle-wordBreak-BREAK_ALL-Link\n" +
                "                            \"Keep All\" [#/1/SpreadsheetName1/cell/A1/style/word-break/save/KEEP_ALL] id=TextStyle-wordBreak-KEEP_ALL-Link\n" +
                "                            \"Break Word\" [#/1/SpreadsheetName1/cell/A1/style/word-break/save/BREAK_WORD] CHECKED id=TextStyle-wordBreak-BREAK_WORD-Link\n" +
                "                WordWrapComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Word Wrap\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/word-wrap/save/] id=TextStyle-wordWrap-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/word-wrap/save/NORMAL] id=TextStyle-wordWrap-NORMAL-Link\n" +
                "                            \"Break Word\" [#/1/SpreadsheetName1/cell/A1/style/word-wrap/save/BREAK_WORD] CHECKED id=TextStyle-wordWrap-BREAK_WORD-Link\n" +
                "                WritingModeComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Writing Mode\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/writing-mode/save/] id=TextStyle-writingMode-Link\n" +
                "                            \"Horizontal Tb\" [#/1/SpreadsheetName1/cell/A1/style/writing-mode/save/HORIZONTAL_TB] CHECKED id=TextStyle-writingMode-HORIZONTAL_TB-Link\n" +
                "                            \"Vertical Lr\" [#/1/SpreadsheetName1/cell/A1/style/writing-mode/save/VERTICAL_LR] id=TextStyle-writingMode-VERTICAL_LR-Link\n" +
                "                            \"Vertical Rl\" [#/1/SpreadsheetName1/cell/A1/style/writing-mode/save/VERTICAL_RL] id=TextStyle-writingMode-VERTICAL_RL-Link\n" +
                "          TextStyleComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Style [background-color: black; color: black; direction: LTR; font-family: Courier; font-kerning: AUTO; font-size: 99; font-stretch: NORMAL; font-style: ITALIC; font-variant: SMALL_CAPS; font-weight: BOLD; hanging-punctuation: FIRST; height: 55px; hyphens: AUTO; letter-spacing: 33px; line-height: 44px; margin-bottom: 3px; margin-left: 4px; margin-right: 2px; margin-top: 1px; opacity: 50%; overflow-wrap: BREAK_WORD; overflow-x: VISIBLE; overflow-y: HIDDEN; padding-bottom: 3px; padding-left: 4px; padding-right: 2px; padding-top: 1px; text-align: CENTER; text-decoration-line: OVERLINE; text-decoration-style: DOTTED; text-decoration-thickness: 123px; text-indent: 123px; text-justify: INTER_WORD; text-transform: UPPERCASE; text-wrapping: WRAP; vertical-align: MIDDLE; visibility: VISIBLE; white-space: NOWRAP; width: 44px; word-break: BREAK_WORD; word-wrap: BREAK_WORD; writing-mode: HORIZONTAL_TB;] icons=mdi-close-circle\n" +
                "          DialogAnchorListComponent\n" +
                "            AnchorListComponent\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Save\" [#/1/SpreadsheetName1/cell/A1/style/*/save/background-color:%20black;%20color:%20black;%20direction:%20LTR;%20font-family:%20Courier;%20font-kerning:%20AUTO;%20font-size:%2099;%20font-stretch:%20NORMAL;%20font-style:%20ITALIC;%20font-variant:%20SMALL_CAPS;%20font-weight:%20BOLD;%20hanging-punctuation:%20FIRST;%20height:%2055px;%20hyphens:%20AUTO;%20letter-spacing:%2033px;%20line-height:%2044px;%20margin-bottom:%203px;%20margin-left:%204px;%20margin-right:%202px;%20margin-top:%201px;%20opacity:%2050%25;%20overflow-wrap:%20BREAK_WORD;%20overflow-x:%20VISIBLE;%20overflow-y:%20HIDDEN;%20padding-bottom:%203px;%20padding-left:%204px;%20padding-right:%202px;%20padding-top:%201px;%20text-align:%20CENTER;%20text-decoration-line:%20OVERLINE;%20text-decoration-style:%20DOTTED;%20text-decoration-thickness:%20123px;%20text-indent:%20123px;%20text-justify:%20INTER_WORD;%20text-transform:%20UPPERCASE; id=TextStyle-save-Link\n" +
                "                  \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/*/save/] id=TextStyle-clear-Link\n" +
                "                  \"Undo\" [#/1/SpreadsheetName1/cell/A1/style/*/save/] id=TextStyle-undo-Link\n" +
                "                  \"Close\" [#/1/SpreadsheetName1/cell/A1] id=TextStyle-close-Link\n"
        );
    }

    @Test
    public void testSetValueWithMetadataStyleWithoutTextStylePropertyName() {
        final SpreadsheetCellReference cell = SpreadsheetSelection.A1;

        final AppContext context = this.appContext(
            HistoryToken.metadataPropertyStyle(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                Optional.empty()
            ),
            Optional.of(UNDO_TEXT_STYLE)
        );

        context.spreadsheetViewportCache()
            .onSpreadsheetMetadata(METADATA);

        context.spreadsheetViewportCache()
            .onSpreadsheetDelta(
                HttpMethod.GET,
                Url.parseRelative("/api/spreadsheet/1/cell"),
                SpreadsheetDelta.EMPTY.setCells(
                    Sets.of(
                        cell.setFormula(SpreadsheetFormula.EMPTY) // no textStyle
                    )
                )
            );

        final TextStyleDialogComponent component = TextStyleDialogComponent.with(
            TextStyleDialogComponentContexts.appContextMetadataStyle(context)
        );

        component.onHistoryTokenChange(
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            ),
            context
        );

        component.textStyle.setValue(
            Optional.of(VALUE_TEXT_STYLE)
        );

        this.treePrintAndCheck(
            component,
            "TextStyleDialogComponent\n" +
                "  DialogComponent\n" +
                "    Spreadsheet: Style (style)\n" +
                "    id=TextStyle-Dialog includeClose=true\n" +
                "      SpreadsheetExpressionReferenceComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Selection [] icons=mdi-close-circle id=TextStyle-selection-TextBox REQUIRED\n" +
                "            Errors\n" +
                "              Empty \"text\"\n" +
                "      TextStyleSampleComponent\n" +
                "        DIV\n" +
                "          style=\"background-color: black; color: black; direction: LTR; font-family: Courier; font-kerning: AUTO; font-size: 99; font-stretch: NORMAL; font-style: ITALIC; font-variant: SMALL_CAPS; font-weight: BOLD; hanging-punctuation: FIRST; height: 55px; hyphens: AUTO; letter-spacing: 33px; line-height: 44px; margin-bottom: 3px; margin-left: 4px; margin-right: 2px; margin-top: 1px; opacity: 50%; overflow-wrap: BREAK_WORD; overflow-x: VISIBLE; overflow-y: HIDDEN; padding-bottom: 3px; padding-left: 4px; padding-right: 2px; padding-top: 1px; text-align: CENTER; text-decoration-line: OVERLINE; text-decoration-style: DOTTED; text-decoration-thickness: 123px; text-indent: 123px; text-justify: INTER_WORD; text-transform: UPPERCASE; text-wrapping: WRAP; vertical-align: MIDDLE; visibility: VISIBLE; white-space: NOWRAP; width: 44px; word-break: BREAK_WORD; word-wrap: BREAK_WORD; writing-mode: HORIZONTAL_TB;\"\n" +
                "            \"The quick brown fox jumps over the lazy dog\"\n" +
                "      ThreeColumnComponent\n" +
                "        DIV\n" +
                "          style=\"display: grid; gap: 5px; grid-template-columns: 33% 33% 33%;\"\n" +
                "            BackgroundColorComponent\n" +
                "              TextStylePropertyColorComponent\n" +
                "                ColorComponent\n" +
                "                  ValueTextBoxComponent\n" +
                "                    TextBoxComponent\n" +
                "                      Background Color [black] icons=mdi-close-circle, mdi-palette id=TextStyle-backgroundColor-TextBox\n" +
                "                        innerRight\n" +
                "                          ColorBoxComponent\n" +
                "                            black\n" +
                "                    ColorPaletteComponent\n" +
                "                      TABLE\n" +
                "                        id=\"TextStyle-Table\" className=dui dui-menu-item\n" +
                "                          TBODY\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  \"Black\" DISABLED id=TextStyle-color-1-Link\n" +
                "                              TD\n" +
                "                                style=\"background-color: white; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  \"White\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/white] id=TextStyle-color-2-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-3-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-4-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-5-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-6-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-7-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-8-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-9-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-10-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-11-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-12-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-13-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-14-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-15-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-16-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-17-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-18-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-19-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-20-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-21-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-22-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-23-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-24-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-25-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-26-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-27-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-28-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-29-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-30-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-31-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-32-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-33-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-34-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-35-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-36-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-37-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-38-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-39-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-40-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-41-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-42-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-43-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-44-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-45-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-46-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-47-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-48-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-49-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-50-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-51-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-52-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-53-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-54-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-55-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-56-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                                  \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/] id=TextStyle-color-clear-Link\n" +
                "              TextStyleColorComponent\n" +
                "                TextStylePropertyColorComponent\n" +
                "                  ColorComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Color [black] icons=mdi-close-circle, mdi-palette id=TextStyle-color-TextBox\n" +
                "                          innerRight\n" +
                "                            ColorBoxComponent\n" +
                "                              black\n" +
                "                      ColorPaletteComponent\n" +
                "                        TABLE\n" +
                "                          id=\"TextStyle-Table\" className=dui dui-menu-item\n" +
                "                            TBODY\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    \"Black\" DISABLED id=TextStyle-color-1-Link\n" +
                "                                TD\n" +
                "                                  style=\"background-color: white; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    \"White\" [#/1/SpreadsheetName1/spreadsheet/style/color/save/white] id=TextStyle-color-2-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-3-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-4-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-5-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-6-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-7-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-8-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-9-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-10-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-11-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-12-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-13-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-14-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-15-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-16-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-17-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-18-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-19-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-20-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-21-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-22-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-23-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-24-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-25-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-26-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-27-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-28-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-29-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-30-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-31-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-32-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-33-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-34-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-35-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-36-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-37-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-38-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-39-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-40-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-41-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-42-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-43-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-44-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-45-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-46-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-47-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-48-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-49-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-50-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-51-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-52-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-53-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-54-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-55-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-56-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                                    \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/color/save/] id=TextStyle-color-clear-Link\n" +
                "                DirectionComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Direction\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/direction/save/] id=TextStyle-direction-Link\n" +
                "                            \"Left to Right\" [#/1/SpreadsheetName1/spreadsheet/style/direction/save/LTR] CHECKED id=TextStyle-direction-LTR-Link\n" +
                "                            \"Right to Left\" [#/1/SpreadsheetName1/spreadsheet/style/direction/save/RTL] id=TextStyle-direction-RTL-Link\n" +
                "                FontFamilyComponent\n" +
                "                  SelectComponent\n" +
                "                    Font Family [Courier] id=TextStyle-Select\n" +
                "                      \"Courier\" DISABLED id=TextStyle-courier-Option\n" +
                "                      \"Sans Serif\" DISABLED id=TextStyle-sansSerif-Option\n" +
                "                      \"Times New Roman\" DISABLED id=TextStyle-timesNewRoman-Option\n" +
                "                FontKerningComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Font Kerning\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/font-kerning/save/] id=TextStyle-fontKerning-Link\n" +
                "                            \"Auto\" [#/1/SpreadsheetName1/spreadsheet/style/font-kerning/save/AUTO] CHECKED id=TextStyle-fontKerning-AUTO-Link\n" +
                "                            \"None\" [#/1/SpreadsheetName1/spreadsheet/style/font-kerning/save/NONE] id=TextStyle-fontKerning-NONE-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/spreadsheet/style/font-kerning/save/NORMAL] id=TextStyle-fontKerning-NORMAL-Link\n" +
                "                FontSizeComponent\n" +
                "                  SuggestBoxComponent\n" +
                "                    Font Size [99]\n" +
                "                FontStretchComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Font Stretch\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/font-stretch/save/] id=TextStyle-fontStretch-Link\n" +
                "                            \"Ultra Condensed\" [#/1/SpreadsheetName1/spreadsheet/style/font-stretch/save/ULTRA_CONDENSED] id=TextStyle-fontStretch-ULTRA_CONDENSED-Link\n" +
                "                            \"Extra Condensed\" [#/1/SpreadsheetName1/spreadsheet/style/font-stretch/save/EXTRA_CONDENSED] id=TextStyle-fontStretch-EXTRA_CONDENSED-Link\n" +
                "                            \"Condensed\" [#/1/SpreadsheetName1/spreadsheet/style/font-stretch/save/CONDENSED] id=TextStyle-fontStretch-CONDENSED-Link\n" +
                "                            \"Semi Condensed\" [#/1/SpreadsheetName1/spreadsheet/style/font-stretch/save/SEMI_CONDENSED] id=TextStyle-fontStretch-SEMI_CONDENSED-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/spreadsheet/style/font-stretch/save/NORMAL] CHECKED id=TextStyle-fontStretch-NORMAL-Link\n" +
                "                            \"Expanded\" [#/1/SpreadsheetName1/spreadsheet/style/font-stretch/save/EXPANDED] id=TextStyle-fontStretch-EXPANDED-Link\n" +
                "                            \"Extra Expanded\" [#/1/SpreadsheetName1/spreadsheet/style/font-stretch/save/EXTRA_EXPANDED] id=TextStyle-fontStretch-EXTRA_EXPANDED-Link\n" +
                "                            \"Ultra Expanded\" [#/1/SpreadsheetName1/spreadsheet/style/font-stretch/save/ULTRA_EXPANDED] id=TextStyle-fontStretch-ULTRA_EXPANDED-Link\n" +
                "                FontStyleComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Font Style\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/font-style/save/] id=TextStyle-fontStyle-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/spreadsheet/style/font-style/save/NORMAL] id=TextStyle-fontStyle-NORMAL-Link\n" +
                "                            mdi-format-italic \"Italic\" [#/1/SpreadsheetName1/spreadsheet/style/font-style/save/ITALIC] CHECKED id=TextStyle-fontStyle-ITALIC-Link\n" +
                "                            \"Oblique\" [#/1/SpreadsheetName1/spreadsheet/style/font-style/save/OBLIQUE] id=TextStyle-fontStyle-OBLIQUE-Link\n" +
                "                FontVariantComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Font Variant\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/font-variant/save/] id=TextStyle-fontVariant-Link\n" +
                "                            \"Initial\" [#/1/SpreadsheetName1/spreadsheet/style/font-variant/save/INITIAL] id=TextStyle-fontVariant-INITIAL-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/spreadsheet/style/font-variant/save/NORMAL] id=TextStyle-fontVariant-NORMAL-Link\n" +
                "                            \"Small Caps\" [#/1/SpreadsheetName1/spreadsheet/style/font-variant/save/SMALL_CAPS] CHECKED id=TextStyle-fontVariant-SMALL_CAPS-Link\n" +
                "                FontWeightComponent\n" +
                "                  ValueTextBoxComponent\n" +
                "                    TextBoxComponent\n" +
                "                      Font Weight [BOLD] icons=mdi-close-circle id=TextStyle-fontWeight-TextBox\n" +
                "                HangingPunctuationComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Hanging Punctuation\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/hanging-punctuation/save/] id=TextStyle-hangingPunctuation-Link\n" +
                "                            \"None\" [#/1/SpreadsheetName1/spreadsheet/style/hanging-punctuation/save/NONE] id=TextStyle-hangingPunctuation-NONE-Link\n" +
                "                            \"First\" [#/1/SpreadsheetName1/spreadsheet/style/hanging-punctuation/save/FIRST] CHECKED id=TextStyle-hangingPunctuation-FIRST-Link\n" +
                "                            \"Last\" [#/1/SpreadsheetName1/spreadsheet/style/hanging-punctuation/save/LAST] id=TextStyle-hangingPunctuation-LAST-Link\n" +
                "                            \"Allow End\" [#/1/SpreadsheetName1/spreadsheet/style/hanging-punctuation/save/ALLOW_END] id=TextStyle-hangingPunctuation-ALLOW_END-Link\n" +
                "                            \"Force End\" [#/1/SpreadsheetName1/spreadsheet/style/hanging-punctuation/save/FORCE_END] id=TextStyle-hangingPunctuation-FORCE_END-Link\n" +
                "                HeightComponent\n" +
                "                  LengthComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Height [55px] icons=mdi-close-circle id=TextStyle-height-TextBox\n" +
                "                HyphensComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Hyphens\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/hyphens/save/] id=TextStyle-hyphens-Link\n" +
                "                            \"None\" [#/1/SpreadsheetName1/spreadsheet/style/hyphens/save/NONE] id=TextStyle-hyphens-NONE-Link\n" +
                "                            \"Manual\" [#/1/SpreadsheetName1/spreadsheet/style/hyphens/save/MANUAL] id=TextStyle-hyphens-MANUAL-Link\n" +
                "                            \"Auto\" [#/1/SpreadsheetName1/spreadsheet/style/hyphens/save/AUTO] CHECKED id=TextStyle-hyphens-AUTO-Link\n" +
                "                LetterSpacingComponent\n" +
                "                  LengthComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Letter Spacing [33px] icons=mdi-close-circle id=TextStyle-letterSpacing-TextBox\n" +
                "                LineHeightComponent\n" +
                "                  LengthComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Line Height [44px] icons=mdi-close-circle id=TextStyle-lineHeight-TextBox\n" +
                "                BigMarginComponent\n" +
                "                  FormElementComponent\n" +
                "                    label\n" +
                "                      Margin\n" +
                "                    FlexLayoutComponent\n" +
                "                      ROW\n" +
                "                        MarginTopComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Top [1px] icons=mdi-close-circle id=TextStyle-marginTop-TextBox\n" +
                "                        MarginRightComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Right [2px] icons=mdi-close-circle id=TextStyle-marginRight-TextBox\n" +
                "                        MarginBottomComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Bottom [3px] icons=mdi-close-circle id=TextStyle-marginBottom-TextBox\n" +
                "                        MarginLeftComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Left [4px] icons=mdi-close-circle id=TextStyle-marginLeft-TextBox\n" +
                "                        MarginComponent\n" +
                "                          ValueTextBoxComponent\n" +
                "                            TextBoxComponent\n" +
                "                              All [1px 2px 3px 4px] icons=mdi-close-circle id=TextStyle-margin-TextBox\n" +
                "                                innerRight\n" +
                "                                  MarginBoxComponent\n" +
                "                                    Margin\n" +
                "                                      ALL\n" +
                "                                        TextStyle\n" +
                "                                          margin-bottom=3px\n" +
                "                                          margin-left=4px\n" +
                "                                          margin-right=2px\n" +
                "                                          margin-top=1px\n" +
                "                OpacityComponent\n" +
                "                  ValueTextBoxComponent\n" +
                "                    TextBoxComponent\n" +
                "                      Opacity [0.5] icons=mdi-close-circle id=TextStyle-opacity-TextBox\n" +
                "                OverflowComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Overflow X\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-x/save/] id=TextStyle-overflowX-Link\n" +
                "                            \"Visible\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-x/save/VISIBLE] CHECKED id=TextStyle-overflowX-VISIBLE-Link\n" +
                "                            \"Hidden\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-x/save/HIDDEN] id=TextStyle-overflowX-HIDDEN-Link\n" +
                "                            \"Scroll\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-x/save/SCROLL] id=TextStyle-overflowX-SCROLL-Link\n" +
                "                            \"Auto\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-x/save/AUTO] id=TextStyle-overflowX-AUTO-Link\n" +
                "                OverflowComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Overflow Y\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-y/save/] id=TextStyle-overflowY-Link\n" +
                "                            \"Visible\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-y/save/VISIBLE] id=TextStyle-overflowY-VISIBLE-Link\n" +
                "                            \"Hidden\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-y/save/HIDDEN] CHECKED id=TextStyle-overflowY-HIDDEN-Link\n" +
                "                            \"Scroll\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-y/save/SCROLL] id=TextStyle-overflowY-SCROLL-Link\n" +
                "                            \"Auto\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-y/save/AUTO] id=TextStyle-overflowY-AUTO-Link\n" +
                "                OverflowWrapComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Overflow Wrap\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-wrap/save/] id=TextStyle-overflowWrap-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-wrap/save/NORMAL] id=TextStyle-overflowWrap-NORMAL-Link\n" +
                "                            \"Anywhere\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-wrap/save/ANYWHERE] id=TextStyle-overflowWrap-ANYWHERE-Link\n" +
                "                            \"Break Word\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-wrap/save/BREAK_WORD] CHECKED id=TextStyle-overflowWrap-BREAK_WORD-Link\n" +
                "                BigPaddingComponent\n" +
                "                  FormElementComponent\n" +
                "                    label\n" +
                "                      Padding\n" +
                "                    FlexLayoutComponent\n" +
                "                      ROW\n" +
                "                        PaddingTopComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Top [1px] icons=mdi-close-circle id=TextStyle-paddingTop-TextBox\n" +
                "                        PaddingRightComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Right [2px] icons=mdi-close-circle id=TextStyle-paddingRight-TextBox\n" +
                "                        PaddingBottomComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Bottom [3px] icons=mdi-close-circle id=TextStyle-paddingBottom-TextBox\n" +
                "                        PaddingLeftComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Left [4px] icons=mdi-close-circle id=TextStyle-paddingLeft-TextBox\n" +
                "                        PaddingComponent\n" +
                "                          ValueTextBoxComponent\n" +
                "                            TextBoxComponent\n" +
                "                              All [1px 2px 3px 4px] icons=mdi-close-circle id=TextStyle-padding-TextBox\n" +
                "                                innerRight\n" +
                "                                  PaddingBoxComponent\n" +
                "                                    Padding\n" +
                "                                      ALL\n" +
                "                                        TextStyle\n" +
                "                                          padding-bottom=3px\n" +
                "                                          padding-left=4px\n" +
                "                                          padding-right=2px\n" +
                "                                          padding-top=1px\n" +
                "                TextAlignComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Text Align\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            mdi-format-clear \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/] id=TextStyle-textAlign-Link\n" +
                "                            mdi-format-align-left \"Left\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/LEFT] id=TextStyle-textAlign-LEFT-Link\n" +
                "                            mdi-format-align-center \"Center\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/CENTER] CHECKED id=TextStyle-textAlign-CENTER-Link\n" +
                "                            mdi-format-align-right \"Right\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/RIGHT] id=TextStyle-textAlign-RIGHT-Link\n" +
                "                            mdi-format-align-justify \"Justify\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/JUSTIFY] id=TextStyle-textAlign-JUSTIFY-Link\n" +
                "                TextDecorationLineComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Text Decoration Line\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-line/save/] id=TextStyle-textDecorationLine-Link\n" +
                "                            mdi-format-clear \"None\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-line/save/NONE] id=TextStyle-textDecorationLine-NONE-Link\n" +
                "                            mdi-format-strikethrough \"Strikethrough\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-line/save/LINE_THROUGH] id=TextStyle-textDecorationLine-LINE_THROUGH-Link\n" +
                "                            \"Overline\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-line/save/OVERLINE] CHECKED id=TextStyle-textDecorationLine-OVERLINE-Link\n" +
                "                            mdi-format-underline \"Underline\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-line/save/UNDERLINE] id=TextStyle-textDecorationLine-UNDERLINE-Link\n" +
                "                TextDecorationStyleComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Text Decoration Style\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-style/save/] id=TextStyle-textDecorationStyle-Link\n" +
                "                            \"Solid\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-style/save/SOLID] id=TextStyle-textDecorationStyle-SOLID-Link\n" +
                "                            \"Double\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-style/save/DOUBLE] id=TextStyle-textDecorationStyle-DOUBLE-Link\n" +
                "                            \"Dashed\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-style/save/DASHED] id=TextStyle-textDecorationStyle-DASHED-Link\n" +
                "                            \"Dotted\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-style/save/DOTTED] CHECKED id=TextStyle-textDecorationStyle-DOTTED-Link\n" +
                "                            \"Wavy\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-style/save/WAVY] id=TextStyle-textDecorationStyle-WAVY-Link\n" +
                "                TextDecorationThicknessComponent\n" +
                "                  LengthComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Text Decoration Thickness [123px] icons=mdi-close-circle id=TextStyle-textDecorationThickness-TextBox\n" +
                "                TextIndentComponent\n" +
                "                  LengthComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Text Indent [123px] icons=mdi-close-circle id=TextStyle-textIndent-TextBox\n" +
                "                TextJustifyComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Text Justify\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/text-justify/save/] id=TextStyle-textJustify-Link\n" +
                "                            \"Auto\" [#/1/SpreadsheetName1/spreadsheet/style/text-justify/save/AUTO] id=TextStyle-textJustify-AUTO-Link\n" +
                "                            \"Inter Word\" [#/1/SpreadsheetName1/spreadsheet/style/text-justify/save/INTER_WORD] CHECKED id=TextStyle-textJustify-INTER_WORD-Link\n" +
                "                            \"Inter Character\" [#/1/SpreadsheetName1/spreadsheet/style/text-justify/save/INTER_CHARACTER] id=TextStyle-textJustify-INTER_CHARACTER-Link\n" +
                "                TextTransformComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Text Transform\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/text-transform/save/] id=TextStyle-textTransform-Link\n" +
                "                            \"None\" [#/1/SpreadsheetName1/spreadsheet/style/text-transform/save/NONE] id=TextStyle-textTransform-NONE-Link\n" +
                "                            mdi-format-letter-case \"Capitalize\" [#/1/SpreadsheetName1/spreadsheet/style/text-transform/save/CAPITALIZE] id=TextStyle-textTransform-CAPITALIZE-Link\n" +
                "                            mdi-format-letter-case-upper \"Uppercase\" [#/1/SpreadsheetName1/spreadsheet/style/text-transform/save/UPPERCASE] CHECKED id=TextStyle-textTransform-UPPERCASE-Link\n" +
                "                            mdi-format-letter-case-lower \"Lowercase\" [#/1/SpreadsheetName1/spreadsheet/style/text-transform/save/LOWERCASE] id=TextStyle-textTransform-LOWERCASE-Link\n" +
                "                TextWrappingComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Text Wrapping\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/text-wrapping/save/] id=TextStyle-textWrapping-Link\n" +
                "                            \"Overflow\" [#/1/SpreadsheetName1/spreadsheet/style/text-wrapping/save/OVERFLOW] id=TextStyle-textWrapping-OVERFLOW-Link\n" +
                "                            \"Wrap\" [#/1/SpreadsheetName1/spreadsheet/style/text-wrapping/save/WRAP] CHECKED id=TextStyle-textWrapping-WRAP-Link\n" +
                "                            \"Clip\" [#/1/SpreadsheetName1/spreadsheet/style/text-wrapping/save/CLIP] id=TextStyle-textWrapping-CLIP-Link\n" +
                "                VerticalAlignComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Vertical Align\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            mdi-format-clear \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/vertical-align/save/] id=TextStyle-verticalAlign-Link\n" +
                "                            mdi-format-align-top \"Top\" [#/1/SpreadsheetName1/spreadsheet/style/vertical-align/save/TOP] id=TextStyle-verticalAlign-TOP-Link\n" +
                "                            mdi-format-align-middle \"Middle\" [#/1/SpreadsheetName1/spreadsheet/style/vertical-align/save/MIDDLE] CHECKED id=TextStyle-verticalAlign-MIDDLE-Link\n" +
                "                            mdi-format-align-bottom \"Bottom\" [#/1/SpreadsheetName1/spreadsheet/style/vertical-align/save/BOTTOM] id=TextStyle-verticalAlign-BOTTOM-Link\n" +
                "                VisibilityComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Visibility\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/visibility/save/] id=TextStyle-visibility-Link\n" +
                "                            \"Visible\" [#/1/SpreadsheetName1/spreadsheet/style/visibility/save/VISIBLE] CHECKED id=TextStyle-visibility-VISIBLE-Link\n" +
                "                            \"Hidden\" [#/1/SpreadsheetName1/spreadsheet/style/visibility/save/HIDDEN] id=TextStyle-visibility-HIDDEN-Link\n" +
                "                            \"Collapse\" [#/1/SpreadsheetName1/spreadsheet/style/visibility/save/COLLAPSE] id=TextStyle-visibility-COLLAPSE-Link\n" +
                "                WhitespaceComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    White Space\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/white-space/save/] id=TextStyle-whiteSpace-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/spreadsheet/style/white-space/save/NORMAL] id=TextStyle-whiteSpace-NORMAL-Link\n" +
                "                            \"Nowrap\" [#/1/SpreadsheetName1/spreadsheet/style/white-space/save/NOWRAP] CHECKED id=TextStyle-whiteSpace-NOWRAP-Link\n" +
                "                            \"Pre\" [#/1/SpreadsheetName1/spreadsheet/style/white-space/save/PRE] id=TextStyle-whiteSpace-PRE-Link\n" +
                "                            \"Pre Line\" [#/1/SpreadsheetName1/spreadsheet/style/white-space/save/PRE_LINE] id=TextStyle-whiteSpace-PRE_LINE-Link\n" +
                "                            \"Pre Wrap\" [#/1/SpreadsheetName1/spreadsheet/style/white-space/save/PRE_WRAP] id=TextStyle-whiteSpace-PRE_WRAP-Link\n" +
                "                WidthComponent\n" +
                "                  LengthComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Width [44px] icons=mdi-close-circle id=TextStyle-width-TextBox\n" +
                "                WordBreakComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Word Break\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/word-break/save/] id=TextStyle-wordBreak-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/spreadsheet/style/word-break/save/NORMAL] id=TextStyle-wordBreak-NORMAL-Link\n" +
                "                            \"Break All\" [#/1/SpreadsheetName1/spreadsheet/style/word-break/save/BREAK_ALL] id=TextStyle-wordBreak-BREAK_ALL-Link\n" +
                "                            \"Keep All\" [#/1/SpreadsheetName1/spreadsheet/style/word-break/save/KEEP_ALL] id=TextStyle-wordBreak-KEEP_ALL-Link\n" +
                "                            \"Break Word\" [#/1/SpreadsheetName1/spreadsheet/style/word-break/save/BREAK_WORD] CHECKED id=TextStyle-wordBreak-BREAK_WORD-Link\n" +
                "                WordWrapComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Word Wrap\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/word-wrap/save/] id=TextStyle-wordWrap-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/spreadsheet/style/word-wrap/save/NORMAL] id=TextStyle-wordWrap-NORMAL-Link\n" +
                "                            \"Break Word\" [#/1/SpreadsheetName1/spreadsheet/style/word-wrap/save/BREAK_WORD] CHECKED id=TextStyle-wordWrap-BREAK_WORD-Link\n" +
                "                WritingModeComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Writing Mode\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/writing-mode/save/] id=TextStyle-writingMode-Link\n" +
                "                            \"Horizontal Tb\" [#/1/SpreadsheetName1/spreadsheet/style/writing-mode/save/HORIZONTAL_TB] CHECKED id=TextStyle-writingMode-HORIZONTAL_TB-Link\n" +
                "                            \"Vertical Lr\" [#/1/SpreadsheetName1/spreadsheet/style/writing-mode/save/VERTICAL_LR] id=TextStyle-writingMode-VERTICAL_LR-Link\n" +
                "                            \"Vertical Rl\" [#/1/SpreadsheetName1/spreadsheet/style/writing-mode/save/VERTICAL_RL] id=TextStyle-writingMode-VERTICAL_RL-Link\n" +
                "          TextStyleComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Style [background-color: black; color: black; direction: LTR; font-family: Courier; font-kerning: AUTO; font-size: 99; font-stretch: NORMAL; font-style: ITALIC; font-variant: SMALL_CAPS; font-weight: BOLD; hanging-punctuation: FIRST; height: 55px; hyphens: AUTO; letter-spacing: 33px; line-height: 44px; margin-bottom: 3px; margin-left: 4px; margin-right: 2px; margin-top: 1px; opacity: 50%; overflow-wrap: BREAK_WORD; overflow-x: VISIBLE; overflow-y: HIDDEN; padding-bottom: 3px; padding-left: 4px; padding-right: 2px; padding-top: 1px; text-align: CENTER; text-decoration-line: OVERLINE; text-decoration-style: DOTTED; text-decoration-thickness: 123px; text-indent: 123px; text-justify: INTER_WORD; text-transform: UPPERCASE; text-wrapping: WRAP; vertical-align: MIDDLE; visibility: VISIBLE; white-space: NOWRAP; width: 44px; word-break: BREAK_WORD; word-wrap: BREAK_WORD; writing-mode: HORIZONTAL_TB;] icons=mdi-close-circle\n" +
                "          DialogAnchorListComponent\n" +
                "            AnchorListComponent\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Save\" [#/1/SpreadsheetName1/spreadsheet/style/*/save/background-color:%20black;%20color:%20black;%20direction:%20LTR;%20font-family:%20Courier;%20font-kerning:%20AUTO;%20font-size:%2099;%20font-stretch:%20NORMAL;%20font-style:%20ITALIC;%20font-variant:%20SMALL_CAPS;%20font-weight:%20BOLD;%20hanging-punctuation:%20FIRST;%20height:%2055px;%20hyphens:%20AUTO;%20letter-spacing:%2033px;%20line-height:%2044px;%20margin-bottom:%203px;%20margin-left:%204px;%20margin-right:%202px;%20margin-top:%201px;%20opacity:%2050%25;%20overflow-wrap:%20BREAK_WORD;%20overflow-x:%20VISIBLE;%20overflow-y:%20HIDDEN;%20padding-bottom:%203px;%20padding-left:%204px;%20padding-right:%202px;%20padding-top:%201px;%20text-align:%20CENTER;%20text-decoration-line:%20OVERLINE;%20text-decoration-style:%20DOTTED;%20text-decoration-thickness:%20123px;%20text-indent:%20123px;%20text-justify:%20INTER_WORD;%20text-transform:%20UPPERC id=TextStyle-save-Link\n" +
                "                  \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/*/save/] id=TextStyle-clear-Link\n" +
                "                  \"Undo\" [#/1/SpreadsheetName1/spreadsheet/style/*/save/text-align:%20LEFT;] id=TextStyle-undo-Link\n" +
                "                  \"Close\" [#/1/SpreadsheetName1] id=TextStyle-close-Link\n"
        );
    }


    @Test
    public void testTextStyleComponentValueWatcher() {
        final SpreadsheetCellReference cell = SpreadsheetSelection.A1;

        final AppContext context = this.appContext(
            HistoryToken.metadataPropertyStyle(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                Optional.empty()
            ),
            Optional.of(UNDO_TEXT_STYLE)
        );

        context.spreadsheetViewportCache()
            .onSpreadsheetMetadata(METADATA);

        context.spreadsheetViewportCache()
            .onSpreadsheetDelta(
                HttpMethod.GET,
                Url.parseRelative("/api/spreadsheet/1/cell"),
                SpreadsheetDelta.EMPTY.setCells(
                    Sets.of(
                        cell.setFormula(SpreadsheetFormula.EMPTY) // no textStyle
                    )
                )
            );

        final TextStyleDialogComponent dialogComponent = TextStyleDialogComponent.with(
            TextStyleDialogComponentContexts.appContextMetadataStyle(context)
        );

        for (TextStylePropertyComponent<?, ?, ?> component : dialogComponent.components) {
            component.setValue(
                Cast.to(
                    VALUE_TEXT_STYLE.get(
                        component.name()
                    )
                )
            );
        }

        context.pushHistoryToken(
            HistoryToken.metadataPropertyStyle(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                Optional.empty()
            )
        );

        this.checkEquals(
            VALUE_TEXT_STYLE,
            dialogComponent.textStyle.value()
                .orElse(null)
        );

        this.treePrintAndCheck(
            dialogComponent,
            "TextStyleDialogComponent\n" +
                "  DialogComponent\n" +
                "    Spreadsheet: Style (style)\n" +
                "    id=TextStyle-Dialog includeClose=true\n" +
                "      SpreadsheetExpressionReferenceComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Selection [] icons=mdi-close-circle id=TextStyle-selection-TextBox REQUIRED\n" +
                "            Errors\n" +
                "              Empty \"text\"\n" +
                "      TextStyleSampleComponent\n" +
                "        DIV\n" +
                "          style=\"background-color: black; color: black; direction: LTR; font-family: Courier; font-kerning: AUTO; font-size: 99; font-stretch: NORMAL; font-style: ITALIC; font-variant: SMALL_CAPS; font-weight: BOLD; hanging-punctuation: FIRST; height: 55px; hyphens: AUTO; letter-spacing: 33px; line-height: 44px; margin-bottom: 3px; margin-left: 4px; margin-right: 2px; margin-top: 1px; opacity: 50%; overflow-wrap: BREAK_WORD; overflow-x: VISIBLE; overflow-y: HIDDEN; padding-bottom: 3px; padding-left: 4px; padding-right: 2px; padding-top: 1px; text-align: CENTER; text-decoration-line: OVERLINE; text-decoration-style: DOTTED; text-decoration-thickness: 123px; text-indent: 123px; text-justify: INTER_WORD; text-transform: UPPERCASE; text-wrapping: WRAP; vertical-align: MIDDLE; visibility: VISIBLE; white-space: NOWRAP; width: 44px; word-break: BREAK_WORD; word-wrap: BREAK_WORD; writing-mode: HORIZONTAL_TB;\"\n" +
                "            \"The quick brown fox jumps over the lazy dog\"\n" +
                "      ThreeColumnComponent\n" +
                "        DIV\n" +
                "          style=\"display: grid; gap: 5px; grid-template-columns: 33% 33% 33%;\"\n" +
                "            BackgroundColorComponent\n" +
                "              TextStylePropertyColorComponent\n" +
                "                ColorComponent\n" +
                "                  ValueTextBoxComponent\n" +
                "                    TextBoxComponent\n" +
                "                      Background Color [black] icons=mdi-close-circle, mdi-palette id=TextStyle-backgroundColor-TextBox\n" +
                "                        innerRight\n" +
                "                          ColorBoxComponent\n" +
                "                            black\n" +
                "                    ColorPaletteComponent\n" +
                "                      TABLE\n" +
                "                        id=\"TextStyle-Table\" className=dui dui-menu-item\n" +
                "                          TBODY\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  \"Black\" DISABLED id=TextStyle-color-1-Link\n" +
                "                              TD\n" +
                "                                style=\"background-color: white; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  \"White\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/white] id=TextStyle-color-2-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-3-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-4-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-5-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-6-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-7-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-8-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-9-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-10-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-11-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-12-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-13-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-14-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-15-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-16-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-17-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-18-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-19-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-20-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-21-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-22-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-23-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-24-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-25-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-26-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-27-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-28-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-29-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-30-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-31-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-32-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-33-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-34-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-35-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-36-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-37-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-38-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-39-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-40-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-41-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-42-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-43-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-44-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-45-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-46-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-47-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-48-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-49-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-50-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-51-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-52-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-53-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-54-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-55-Link\n" +
                "                              TD\n" +
                "                                style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                  DISABLED id=TextStyle-color-56-Link\n" +
                "                            TR\n" +
                "                              TD\n" +
                "                                colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                                  \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/background-color/save/] id=TextStyle-color-clear-Link\n" +
                "              TextStyleColorComponent\n" +
                "                TextStylePropertyColorComponent\n" +
                "                  ColorComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Color [black] icons=mdi-close-circle, mdi-palette id=TextStyle-color-TextBox\n" +
                "                          innerRight\n" +
                "                            ColorBoxComponent\n" +
                "                              black\n" +
                "                      ColorPaletteComponent\n" +
                "                        TABLE\n" +
                "                          id=\"TextStyle-Table\" className=dui dui-menu-item\n" +
                "                            TBODY\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"background-color: black; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    \"Black\" DISABLED id=TextStyle-color-1-Link\n" +
                "                                TD\n" +
                "                                  style=\"background-color: white; border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    \"White\" [#/1/SpreadsheetName1/spreadsheet/style/color/save/white] id=TextStyle-color-2-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-3-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-4-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-5-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-6-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-7-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-8-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-9-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-10-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-11-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-12-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-13-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-14-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-15-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-16-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-17-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-18-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-19-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-20-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-21-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-22-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-23-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-24-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-25-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-26-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-27-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-28-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-29-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-30-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-31-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-32-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-33-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-34-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-35-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-36-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-37-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-38-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-39-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-40-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-41-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-42-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-43-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-44-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-45-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-46-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-47-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-48-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-49-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-50-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-51-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-52-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-53-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-54-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-55-Link\n" +
                "                                TD\n" +
                "                                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                                    DISABLED id=TextStyle-color-56-Link\n" +
                "                              TR\n" +
                "                                TD\n" +
                "                                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                                    \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/color/save/] id=TextStyle-color-clear-Link\n" +
                "                DirectionComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Direction\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/direction/save/] id=TextStyle-direction-Link\n" +
                "                            \"Left to Right\" [#/1/SpreadsheetName1/spreadsheet/style/direction/save/LTR] CHECKED id=TextStyle-direction-LTR-Link\n" +
                "                            \"Right to Left\" [#/1/SpreadsheetName1/spreadsheet/style/direction/save/RTL] id=TextStyle-direction-RTL-Link\n" +
                "                FontFamilyComponent\n" +
                "                  SelectComponent\n" +
                "                    Font Family [Courier] id=TextStyle-Select\n" +
                "                      \"Courier\" DISABLED id=TextStyle-courier-Option\n" +
                "                      \"Sans Serif\" DISABLED id=TextStyle-sansSerif-Option\n" +
                "                      \"Times New Roman\" DISABLED id=TextStyle-timesNewRoman-Option\n" +
                "                FontKerningComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Font Kerning\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/font-kerning/save/] id=TextStyle-fontKerning-Link\n" +
                "                            \"Auto\" [#/1/SpreadsheetName1/spreadsheet/style/font-kerning/save/AUTO] CHECKED id=TextStyle-fontKerning-AUTO-Link\n" +
                "                            \"None\" [#/1/SpreadsheetName1/spreadsheet/style/font-kerning/save/NONE] id=TextStyle-fontKerning-NONE-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/spreadsheet/style/font-kerning/save/NORMAL] id=TextStyle-fontKerning-NORMAL-Link\n" +
                "                FontSizeComponent\n" +
                "                  SuggestBoxComponent\n" +
                "                    Font Size [99]\n" +
                "                FontStretchComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Font Stretch\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/font-stretch/save/] id=TextStyle-fontStretch-Link\n" +
                "                            \"Ultra Condensed\" [#/1/SpreadsheetName1/spreadsheet/style/font-stretch/save/ULTRA_CONDENSED] id=TextStyle-fontStretch-ULTRA_CONDENSED-Link\n" +
                "                            \"Extra Condensed\" [#/1/SpreadsheetName1/spreadsheet/style/font-stretch/save/EXTRA_CONDENSED] id=TextStyle-fontStretch-EXTRA_CONDENSED-Link\n" +
                "                            \"Condensed\" [#/1/SpreadsheetName1/spreadsheet/style/font-stretch/save/CONDENSED] id=TextStyle-fontStretch-CONDENSED-Link\n" +
                "                            \"Semi Condensed\" [#/1/SpreadsheetName1/spreadsheet/style/font-stretch/save/SEMI_CONDENSED] id=TextStyle-fontStretch-SEMI_CONDENSED-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/spreadsheet/style/font-stretch/save/NORMAL] CHECKED id=TextStyle-fontStretch-NORMAL-Link\n" +
                "                            \"Expanded\" [#/1/SpreadsheetName1/spreadsheet/style/font-stretch/save/EXPANDED] id=TextStyle-fontStretch-EXPANDED-Link\n" +
                "                            \"Extra Expanded\" [#/1/SpreadsheetName1/spreadsheet/style/font-stretch/save/EXTRA_EXPANDED] id=TextStyle-fontStretch-EXTRA_EXPANDED-Link\n" +
                "                            \"Ultra Expanded\" [#/1/SpreadsheetName1/spreadsheet/style/font-stretch/save/ULTRA_EXPANDED] id=TextStyle-fontStretch-ULTRA_EXPANDED-Link\n" +
                "                FontStyleComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Font Style\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/font-style/save/] id=TextStyle-fontStyle-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/spreadsheet/style/font-style/save/NORMAL] id=TextStyle-fontStyle-NORMAL-Link\n" +
                "                            mdi-format-italic \"Italic\" [#/1/SpreadsheetName1/spreadsheet/style/font-style/save/ITALIC] CHECKED id=TextStyle-fontStyle-ITALIC-Link\n" +
                "                            \"Oblique\" [#/1/SpreadsheetName1/spreadsheet/style/font-style/save/OBLIQUE] id=TextStyle-fontStyle-OBLIQUE-Link\n" +
                "                FontVariantComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Font Variant\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/font-variant/save/] id=TextStyle-fontVariant-Link\n" +
                "                            \"Initial\" [#/1/SpreadsheetName1/spreadsheet/style/font-variant/save/INITIAL] id=TextStyle-fontVariant-INITIAL-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/spreadsheet/style/font-variant/save/NORMAL] id=TextStyle-fontVariant-NORMAL-Link\n" +
                "                            \"Small Caps\" [#/1/SpreadsheetName1/spreadsheet/style/font-variant/save/SMALL_CAPS] CHECKED id=TextStyle-fontVariant-SMALL_CAPS-Link\n" +
                "                FontWeightComponent\n" +
                "                  ValueTextBoxComponent\n" +
                "                    TextBoxComponent\n" +
                "                      Font Weight [BOLD] icons=mdi-close-circle id=TextStyle-fontWeight-TextBox\n" +
                "                HangingPunctuationComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Hanging Punctuation\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/hanging-punctuation/save/] id=TextStyle-hangingPunctuation-Link\n" +
                "                            \"None\" [#/1/SpreadsheetName1/spreadsheet/style/hanging-punctuation/save/NONE] id=TextStyle-hangingPunctuation-NONE-Link\n" +
                "                            \"First\" [#/1/SpreadsheetName1/spreadsheet/style/hanging-punctuation/save/FIRST] CHECKED id=TextStyle-hangingPunctuation-FIRST-Link\n" +
                "                            \"Last\" [#/1/SpreadsheetName1/spreadsheet/style/hanging-punctuation/save/LAST] id=TextStyle-hangingPunctuation-LAST-Link\n" +
                "                            \"Allow End\" [#/1/SpreadsheetName1/spreadsheet/style/hanging-punctuation/save/ALLOW_END] id=TextStyle-hangingPunctuation-ALLOW_END-Link\n" +
                "                            \"Force End\" [#/1/SpreadsheetName1/spreadsheet/style/hanging-punctuation/save/FORCE_END] id=TextStyle-hangingPunctuation-FORCE_END-Link\n" +
                "                HeightComponent\n" +
                "                  LengthComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Height [55px] icons=mdi-close-circle id=TextStyle-height-TextBox\n" +
                "                HyphensComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Hyphens\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/hyphens/save/] id=TextStyle-hyphens-Link\n" +
                "                            \"None\" [#/1/SpreadsheetName1/spreadsheet/style/hyphens/save/NONE] id=TextStyle-hyphens-NONE-Link\n" +
                "                            \"Manual\" [#/1/SpreadsheetName1/spreadsheet/style/hyphens/save/MANUAL] id=TextStyle-hyphens-MANUAL-Link\n" +
                "                            \"Auto\" [#/1/SpreadsheetName1/spreadsheet/style/hyphens/save/AUTO] CHECKED id=TextStyle-hyphens-AUTO-Link\n" +
                "                LetterSpacingComponent\n" +
                "                  LengthComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Letter Spacing [33px] icons=mdi-close-circle id=TextStyle-letterSpacing-TextBox\n" +
                "                LineHeightComponent\n" +
                "                  LengthComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Line Height [44px] icons=mdi-close-circle id=TextStyle-lineHeight-TextBox\n" +
                "                BigMarginComponent\n" +
                "                  FormElementComponent\n" +
                "                    label\n" +
                "                      Margin\n" +
                "                    FlexLayoutComponent\n" +
                "                      ROW\n" +
                "                        MarginTopComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Top [1px] icons=mdi-close-circle id=TextStyle-marginTop-TextBox\n" +
                "                        MarginRightComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Right [2px] icons=mdi-close-circle id=TextStyle-marginRight-TextBox\n" +
                "                        MarginBottomComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Bottom [3px] icons=mdi-close-circle id=TextStyle-marginBottom-TextBox\n" +
                "                        MarginLeftComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Left [4px] icons=mdi-close-circle id=TextStyle-marginLeft-TextBox\n" +
                "                        MarginComponent\n" +
                "                          ValueTextBoxComponent\n" +
                "                            TextBoxComponent\n" +
                "                              All [1px 2px 3px 4px] icons=mdi-close-circle id=TextStyle-margin-TextBox\n" +
                "                                innerRight\n" +
                "                                  MarginBoxComponent\n" +
                "                                    Margin\n" +
                "                                      ALL\n" +
                "                                        TextStyle\n" +
                "                                          margin-bottom=3px\n" +
                "                                          margin-left=4px\n" +
                "                                          margin-right=2px\n" +
                "                                          margin-top=1px\n" +
                "                OpacityComponent\n" +
                "                  ValueTextBoxComponent\n" +
                "                    TextBoxComponent\n" +
                "                      Opacity [0.5] icons=mdi-close-circle id=TextStyle-opacity-TextBox\n" +
                "                OverflowComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Overflow X\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-x/save/] id=TextStyle-overflowX-Link\n" +
                "                            \"Visible\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-x/save/VISIBLE] CHECKED id=TextStyle-overflowX-VISIBLE-Link\n" +
                "                            \"Hidden\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-x/save/HIDDEN] id=TextStyle-overflowX-HIDDEN-Link\n" +
                "                            \"Scroll\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-x/save/SCROLL] id=TextStyle-overflowX-SCROLL-Link\n" +
                "                            \"Auto\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-x/save/AUTO] id=TextStyle-overflowX-AUTO-Link\n" +
                "                OverflowComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Overflow Y\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-y/save/] id=TextStyle-overflowY-Link\n" +
                "                            \"Visible\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-y/save/VISIBLE] id=TextStyle-overflowY-VISIBLE-Link\n" +
                "                            \"Hidden\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-y/save/HIDDEN] CHECKED id=TextStyle-overflowY-HIDDEN-Link\n" +
                "                            \"Scroll\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-y/save/SCROLL] id=TextStyle-overflowY-SCROLL-Link\n" +
                "                            \"Auto\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-y/save/AUTO] id=TextStyle-overflowY-AUTO-Link\n" +
                "                OverflowWrapComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Overflow Wrap\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-wrap/save/] id=TextStyle-overflowWrap-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-wrap/save/NORMAL] id=TextStyle-overflowWrap-NORMAL-Link\n" +
                "                            \"Anywhere\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-wrap/save/ANYWHERE] id=TextStyle-overflowWrap-ANYWHERE-Link\n" +
                "                            \"Break Word\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-wrap/save/BREAK_WORD] CHECKED id=TextStyle-overflowWrap-BREAK_WORD-Link\n" +
                "                BigPaddingComponent\n" +
                "                  FormElementComponent\n" +
                "                    label\n" +
                "                      Padding\n" +
                "                    FlexLayoutComponent\n" +
                "                      ROW\n" +
                "                        PaddingTopComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Top [1px] icons=mdi-close-circle id=TextStyle-paddingTop-TextBox\n" +
                "                        PaddingRightComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Right [2px] icons=mdi-close-circle id=TextStyle-paddingRight-TextBox\n" +
                "                        PaddingBottomComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Bottom [3px] icons=mdi-close-circle id=TextStyle-paddingBottom-TextBox\n" +
                "                        PaddingLeftComponent\n" +
                "                          LengthComponent\n" +
                "                            ValueTextBoxComponent\n" +
                "                              TextBoxComponent\n" +
                "                                Left [4px] icons=mdi-close-circle id=TextStyle-paddingLeft-TextBox\n" +
                "                        PaddingComponent\n" +
                "                          ValueTextBoxComponent\n" +
                "                            TextBoxComponent\n" +
                "                              All [1px 2px 3px 4px] icons=mdi-close-circle id=TextStyle-padding-TextBox\n" +
                "                                innerRight\n" +
                "                                  PaddingBoxComponent\n" +
                "                                    Padding\n" +
                "                                      ALL\n" +
                "                                        TextStyle\n" +
                "                                          padding-bottom=3px\n" +
                "                                          padding-left=4px\n" +
                "                                          padding-right=2px\n" +
                "                                          padding-top=1px\n" +
                "                TextAlignComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Text Align\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            mdi-format-clear \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/] id=TextStyle-textAlign-Link\n" +
                "                            mdi-format-align-left \"Left\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/LEFT] id=TextStyle-textAlign-LEFT-Link\n" +
                "                            mdi-format-align-center \"Center\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/CENTER] CHECKED id=TextStyle-textAlign-CENTER-Link\n" +
                "                            mdi-format-align-right \"Right\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/RIGHT] id=TextStyle-textAlign-RIGHT-Link\n" +
                "                            mdi-format-align-justify \"Justify\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/JUSTIFY] id=TextStyle-textAlign-JUSTIFY-Link\n" +
                "                TextDecorationLineComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Text Decoration Line\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-line/save/] id=TextStyle-textDecorationLine-Link\n" +
                "                            mdi-format-clear \"None\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-line/save/NONE] id=TextStyle-textDecorationLine-NONE-Link\n" +
                "                            mdi-format-strikethrough \"Strikethrough\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-line/save/LINE_THROUGH] id=TextStyle-textDecorationLine-LINE_THROUGH-Link\n" +
                "                            \"Overline\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-line/save/OVERLINE] CHECKED id=TextStyle-textDecorationLine-OVERLINE-Link\n" +
                "                            mdi-format-underline \"Underline\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-line/save/UNDERLINE] id=TextStyle-textDecorationLine-UNDERLINE-Link\n" +
                "                TextDecorationStyleComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Text Decoration Style\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-style/save/] id=TextStyle-textDecorationStyle-Link\n" +
                "                            \"Solid\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-style/save/SOLID] id=TextStyle-textDecorationStyle-SOLID-Link\n" +
                "                            \"Double\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-style/save/DOUBLE] id=TextStyle-textDecorationStyle-DOUBLE-Link\n" +
                "                            \"Dashed\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-style/save/DASHED] id=TextStyle-textDecorationStyle-DASHED-Link\n" +
                "                            \"Dotted\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-style/save/DOTTED] CHECKED id=TextStyle-textDecorationStyle-DOTTED-Link\n" +
                "                            \"Wavy\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-style/save/WAVY] id=TextStyle-textDecorationStyle-WAVY-Link\n" +
                "                TextDecorationThicknessComponent\n" +
                "                  LengthComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Text Decoration Thickness [123px] icons=mdi-close-circle id=TextStyle-textDecorationThickness-TextBox\n" +
                "                TextIndentComponent\n" +
                "                  LengthComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Text Indent [123px] icons=mdi-close-circle id=TextStyle-textIndent-TextBox\n" +
                "                TextJustifyComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Text Justify\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/text-justify/save/] id=TextStyle-textJustify-Link\n" +
                "                            \"Auto\" [#/1/SpreadsheetName1/spreadsheet/style/text-justify/save/AUTO] id=TextStyle-textJustify-AUTO-Link\n" +
                "                            \"Inter Word\" [#/1/SpreadsheetName1/spreadsheet/style/text-justify/save/INTER_WORD] CHECKED id=TextStyle-textJustify-INTER_WORD-Link\n" +
                "                            \"Inter Character\" [#/1/SpreadsheetName1/spreadsheet/style/text-justify/save/INTER_CHARACTER] id=TextStyle-textJustify-INTER_CHARACTER-Link\n" +
                "                TextTransformComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Text Transform\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/text-transform/save/] id=TextStyle-textTransform-Link\n" +
                "                            \"None\" [#/1/SpreadsheetName1/spreadsheet/style/text-transform/save/NONE] id=TextStyle-textTransform-NONE-Link\n" +
                "                            mdi-format-letter-case \"Capitalize\" [#/1/SpreadsheetName1/spreadsheet/style/text-transform/save/CAPITALIZE] id=TextStyle-textTransform-CAPITALIZE-Link\n" +
                "                            mdi-format-letter-case-upper \"Uppercase\" [#/1/SpreadsheetName1/spreadsheet/style/text-transform/save/UPPERCASE] CHECKED id=TextStyle-textTransform-UPPERCASE-Link\n" +
                "                            mdi-format-letter-case-lower \"Lowercase\" [#/1/SpreadsheetName1/spreadsheet/style/text-transform/save/LOWERCASE] id=TextStyle-textTransform-LOWERCASE-Link\n" +
                "                TextWrappingComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Text Wrapping\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/text-wrapping/save/] id=TextStyle-textWrapping-Link\n" +
                "                            \"Overflow\" [#/1/SpreadsheetName1/spreadsheet/style/text-wrapping/save/OVERFLOW] id=TextStyle-textWrapping-OVERFLOW-Link\n" +
                "                            \"Wrap\" [#/1/SpreadsheetName1/spreadsheet/style/text-wrapping/save/WRAP] CHECKED id=TextStyle-textWrapping-WRAP-Link\n" +
                "                            \"Clip\" [#/1/SpreadsheetName1/spreadsheet/style/text-wrapping/save/CLIP] id=TextStyle-textWrapping-CLIP-Link\n" +
                "                VerticalAlignComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Vertical Align\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            mdi-format-clear \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/vertical-align/save/] id=TextStyle-verticalAlign-Link\n" +
                "                            mdi-format-align-top \"Top\" [#/1/SpreadsheetName1/spreadsheet/style/vertical-align/save/TOP] id=TextStyle-verticalAlign-TOP-Link\n" +
                "                            mdi-format-align-middle \"Middle\" [#/1/SpreadsheetName1/spreadsheet/style/vertical-align/save/MIDDLE] CHECKED id=TextStyle-verticalAlign-MIDDLE-Link\n" +
                "                            mdi-format-align-bottom \"Bottom\" [#/1/SpreadsheetName1/spreadsheet/style/vertical-align/save/BOTTOM] id=TextStyle-verticalAlign-BOTTOM-Link\n" +
                "                VisibilityComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Visibility\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/visibility/save/] id=TextStyle-visibility-Link\n" +
                "                            \"Visible\" [#/1/SpreadsheetName1/spreadsheet/style/visibility/save/VISIBLE] CHECKED id=TextStyle-visibility-VISIBLE-Link\n" +
                "                            \"Hidden\" [#/1/SpreadsheetName1/spreadsheet/style/visibility/save/HIDDEN] id=TextStyle-visibility-HIDDEN-Link\n" +
                "                            \"Collapse\" [#/1/SpreadsheetName1/spreadsheet/style/visibility/save/COLLAPSE] id=TextStyle-visibility-COLLAPSE-Link\n" +
                "                WhitespaceComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    White Space\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/white-space/save/] id=TextStyle-whiteSpace-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/spreadsheet/style/white-space/save/NORMAL] id=TextStyle-whiteSpace-NORMAL-Link\n" +
                "                            \"Nowrap\" [#/1/SpreadsheetName1/spreadsheet/style/white-space/save/NOWRAP] CHECKED id=TextStyle-whiteSpace-NOWRAP-Link\n" +
                "                            \"Pre\" [#/1/SpreadsheetName1/spreadsheet/style/white-space/save/PRE] id=TextStyle-whiteSpace-PRE-Link\n" +
                "                            \"Pre Line\" [#/1/SpreadsheetName1/spreadsheet/style/white-space/save/PRE_LINE] id=TextStyle-whiteSpace-PRE_LINE-Link\n" +
                "                            \"Pre Wrap\" [#/1/SpreadsheetName1/spreadsheet/style/white-space/save/PRE_WRAP] id=TextStyle-whiteSpace-PRE_WRAP-Link\n" +
                "                WidthComponent\n" +
                "                  LengthComponent\n" +
                "                    ValueTextBoxComponent\n" +
                "                      TextBoxComponent\n" +
                "                        Width [44px] icons=mdi-close-circle id=TextStyle-width-TextBox\n" +
                "                WordBreakComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Word Break\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/word-break/save/] id=TextStyle-wordBreak-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/spreadsheet/style/word-break/save/NORMAL] id=TextStyle-wordBreak-NORMAL-Link\n" +
                "                            \"Break All\" [#/1/SpreadsheetName1/spreadsheet/style/word-break/save/BREAK_ALL] id=TextStyle-wordBreak-BREAK_ALL-Link\n" +
                "                            \"Keep All\" [#/1/SpreadsheetName1/spreadsheet/style/word-break/save/KEEP_ALL] id=TextStyle-wordBreak-KEEP_ALL-Link\n" +
                "                            \"Break Word\" [#/1/SpreadsheetName1/spreadsheet/style/word-break/save/BREAK_WORD] CHECKED id=TextStyle-wordBreak-BREAK_WORD-Link\n" +
                "                WordWrapComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Word Wrap\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/word-wrap/save/] id=TextStyle-wordWrap-Link\n" +
                "                            \"Normal\" [#/1/SpreadsheetName1/spreadsheet/style/word-wrap/save/NORMAL] id=TextStyle-wordWrap-NORMAL-Link\n" +
                "                            \"Break Word\" [#/1/SpreadsheetName1/spreadsheet/style/word-wrap/save/BREAK_WORD] CHECKED id=TextStyle-wordWrap-BREAK_WORD-Link\n" +
                "                WritingModeComponent\n" +
                "                  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "                    Writing Mode\n" +
                "                      AnchorListComponent\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/writing-mode/save/] id=TextStyle-writingMode-Link\n" +
                "                            \"Horizontal Tb\" [#/1/SpreadsheetName1/spreadsheet/style/writing-mode/save/HORIZONTAL_TB] CHECKED id=TextStyle-writingMode-HORIZONTAL_TB-Link\n" +
                "                            \"Vertical Lr\" [#/1/SpreadsheetName1/spreadsheet/style/writing-mode/save/VERTICAL_LR] id=TextStyle-writingMode-VERTICAL_LR-Link\n" +
                "                            \"Vertical Rl\" [#/1/SpreadsheetName1/spreadsheet/style/writing-mode/save/VERTICAL_RL] id=TextStyle-writingMode-VERTICAL_RL-Link\n" +
                "          TextStyleComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Style [background-color: black; color: black; direction: LTR; font-family: Courier; font-kerning: AUTO; font-size: 99; font-stretch: NORMAL; font-style: ITALIC; font-variant: SMALL_CAPS; font-weight: BOLD; hanging-punctuation: FIRST; height: 55px; hyphens: AUTO; letter-spacing: 33px; line-height: 44px; margin-bottom: 3px; margin-left: 4px; margin-right: 2px; margin-top: 1px; opacity: 50%; overflow-wrap: BREAK_WORD; overflow-x: VISIBLE; overflow-y: HIDDEN; padding-bottom: 3px; padding-left: 4px; padding-right: 2px; padding-top: 1px; text-align: CENTER; text-decoration-line: OVERLINE; text-decoration-style: DOTTED; text-decoration-thickness: 123px; text-indent: 123px; text-justify: INTER_WORD; text-transform: UPPERCASE; text-wrapping: WRAP; vertical-align: MIDDLE; visibility: VISIBLE; white-space: NOWRAP; width: 44px; word-break: BREAK_WORD; word-wrap: BREAK_WORD; writing-mode: HORIZONTAL_TB;] icons=mdi-close-circle\n" +
                "          DialogAnchorListComponent\n" +
                "            AnchorListComponent\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Save\" [#/1/SpreadsheetName1/spreadsheet/style/*/save/background-color:%20black;%20color:%20black;%20direction:%20LTR;%20font-family:%20Courier;%20font-kerning:%20AUTO;%20font-size:%2099;%20font-stretch:%20NORMAL;%20font-style:%20ITALIC;%20font-variant:%20SMALL_CAPS;%20font-weight:%20BOLD;%20hanging-punctuation:%20FIRST;%20height:%2055px;%20hyphens:%20AUTO;%20letter-spacing:%2033px;%20line-height:%2044px;%20margin-bottom:%203px;%20margin-left:%204px;%20margin-right:%202px;%20margin-top:%201px;%20opacity:%2050%25;%20overflow-wrap:%20BREAK_WORD;%20overflow-x:%20VISIBLE;%20overflow-y:%20HIDDEN;%20padding-bottom:%203px;%20padding-left:%204px;%20padding-right:%202px;%20padding-top:%201px;%20text-align:%20CENTER;%20text-decoration-line:%20OVERLINE;%20text-decoration-style:%20DOTTED;%20text-decoration-thickness:%20123px;%20text-indent:%20123px;%20text-justify:%20INTER_WORD;%20text-transform:%20UPPERC id=TextStyle-save-Link\n" +
                "                  \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/*/save/] id=TextStyle-clear-Link\n" +
                "                  \"Undo\" [#/1/SpreadsheetName1/spreadsheet/style/*/save/background-color:%20black;%20color:%20black;%20direction:%20LTR;%20font-family:%20Courier;%20font-kerning:%20AUTO;%20font-size:%2099;%20font-stretch:%20NORMAL;%20font-style:%20ITALIC;%20font-variant:%20SMALL_CAPS;%20font-weight:%20BOLD;%20hanging-punctuation:%20FIRST;%20height:%2055px;%20hyphens:%20AUTO;%20letter-spacing:%2033px;%20line-height:%2044px;%20margin-bottom:%203px;%20margin-left:%204px;%20margin-right:%202px;%20margin-top:%201px;%20opacity:%2050%25;%20overflow-wrap:%20BREAK_WORD;%20overflow-x:%20VISIBLE;%20overflow-y:%20HIDDEN;%20padding-bottom:%203px;%20padding-left:%204px;%20padding-right:%202px;%20padding-top:%201px;%20text-align:%20CENTER;%20text-decoration-line:%20OVERLINE;%20text-decoration-style:%20DOTTED;%20text-decoration-thickness:%20123px;%20text-indent:%20123px;%20text-justify:%20INTER_WORD;%20text-transform:%20UPPERC id=TextStyle-undo-Link\n" +
                "                  \"Close\" [#/1/SpreadsheetName1] id=TextStyle-close-Link\n"
        );
    }

    @Override
    public TextStyleDialogComponent createSpreadsheetDialogComponentLifecycle(final HistoryToken historyToken) {
        return TextStyleDialogComponent.with(
            TextStyleDialogComponentContexts.appContextMetadataStyle(
                this.appContext(
                    historyToken,
                    Optional.of(UNDO_TEXT_STYLE)
                )
            )
        );
    }

    private AppContext appContext(final HistoryToken historyToken,
                                  final Optional<TextStyle> textStyle) {
        return new FakeAppContext() {

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return this.historyTokenWatchers.add(watcher);
            }

            private final HistoryTokenWatchers historyTokenWatchers = HistoryTokenWatchers.empty();

            @Override
            public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
                return null;
            }

            @Override
            public void giveFocus(final Runnable focus) {
                // ignore
            }

            @Override
            public void pushHistoryToken(final HistoryToken token) {
                Objects.requireNonNull(token, "token");

                final HistoryToken previous = this.currentHistoryToken;
                this.currentHistoryToken = token;
                this.historyTokenWatchers.onHistoryTokenChange(
                    previous,
                    this // AppContext
                );

                if(token.isSave()) {
                    final TextStylePropertyName<?> propertyName = token.stylePropertyName()
                        .orElse(null);
                    final Object saveValue = token.saveValue()
                        .orElse(null);

                    this.spreadsheetMetadata = this.spreadsheetMetadata.set(
                        SpreadsheetMetadataPropertyName.STYLE,
                        this.spreadsheetMetadata.getOrFail(
                            SpreadsheetMetadataPropertyName.STYLE
                        ).setOrRemove(
                            propertyName,
                            Cast.to(saveValue)
                        )
                    );
                }
            }

            @Override
            public HistoryToken historyToken() {
                return currentHistoryToken;
            }

            private HistoryToken currentHistoryToken = historyToken;

            @Override
            public SpreadsheetViewportCache spreadsheetViewportCache() {
                return this.spreadsheetViewportCache;
            }

            private final SpreadsheetViewportCache spreadsheetViewportCache = SpreadsheetViewportCache.empty(this);

            @Override
            public SpreadsheetMetadata spreadsheetMetadata() {
                return this.spreadsheetMetadata;
            }

            private SpreadsheetMetadata spreadsheetMetadata = METADATA.setOrRemove(
                SpreadsheetMetadataPropertyName.STYLE,
                textStyle.orElse(null)
            );

            @Override
            public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
                return null;
            }

            @Override
            public void debug(final Object... values) {
                System.out.println(
                    Arrays.toString(values)
                );
            }
        };
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<TextStyleDialogComponent> type() {
        return TextStyleDialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
