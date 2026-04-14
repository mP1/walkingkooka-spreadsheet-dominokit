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
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextStyle;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public final class TextStyleDialogComponentTest implements DialogComponentLifecycleTesting<TextStyleDialogComponent>,
    HistoryTokenTesting,
    SpreadsheetMetadataTesting {

    private final static SpreadsheetMetadata METADATA = METADATA_EN_AU.set(
        SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
        SPREADSHEET_ID
    );

    private final static TextStyle UNDO_TEXT_STYLE = TextStyle.parse("text-align: LEFT;");

    private final static TextStyle VALUE_TEXT_STYLE = TextStyle.parse("direction: LTR; font-kerning: AUTO; font-stretch: NORMAL; font-style: ITALIC; font-variant: SMALL_CAPS; hanging-punctuation: FIRST; hyphens: AUTO; overflow-x: VISIBLE; overflow-y: HIDDEN; overflow-wrap: BREAK_WORD; text-align: CENTER; text-decoration-line: OVERLINE; text-decoration-style: DOTTED; text-justify: INTER_WORD; text-transform: UPPERCASE; text-wrapping: WRAP; vertical-align: MIDDLE; visibility: VISIBLE; white-space: NOWRAP; word-break: BREAK_WORD; word-wrap: BREAK_WORD; writing-mode: HORIZONTAL_TB;");

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
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          SpreadsheetExpressionReferenceComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Selection [A1] id=TextStyle-selection-TextBox REQUIRED\n" +
                "          DirectionComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Direction\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/direction/save/] id=TextStyle-direction-Link\n" +
                "                      \"Left to Right\" [#/1/SpreadsheetName1/cell/A1/style/direction/save/LTR] CHECKED id=TextStyle-direction-LTR-Link\n" +
                "                      \"Right to Left\" [#/1/SpreadsheetName1/cell/A1/style/direction/save/RTL] id=TextStyle-direction-RTL-Link\n" +
                "          FontKerningComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Font Kerning\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/font-kerning/save/] id=TextStyle-fontKerning-Link\n" +
                "                      \"Auto\" [#/1/SpreadsheetName1/cell/A1/style/font-kerning/save/AUTO] CHECKED id=TextStyle-fontKerning-AUTO-Link\n" +
                "                      \"None\" [#/1/SpreadsheetName1/cell/A1/style/font-kerning/save/NONE] id=TextStyle-fontKerning-NONE-Link\n" +
                "                      \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/font-kerning/save/NORMAL] id=TextStyle-fontKerning-NORMAL-Link\n" +
                "          FontStretchComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Font Stretch\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/] id=TextStyle-fontStretch-Link\n" +
                "                      \"Ultra Condensed\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/ULTRA_CONDENSED] id=TextStyle-fontStretch-ULTRA_CONDENSED-Link\n" +
                "                      \"Extra Condensed\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/EXTRA_CONDENSED] id=TextStyle-fontStretch-EXTRA_CONDENSED-Link\n" +
                "                      \"Condensed\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/CONDENSED] id=TextStyle-fontStretch-CONDENSED-Link\n" +
                "                      \"Semi Condensed\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/SEMI_CONDENSED] id=TextStyle-fontStretch-SEMI_CONDENSED-Link\n" +
                "                      \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/NORMAL] CHECKED id=TextStyle-fontStretch-NORMAL-Link\n" +
                "                      \"Expanded\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/EXPANDED] id=TextStyle-fontStretch-EXPANDED-Link\n" +
                "                      \"Extra Expanded\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/EXTRA_EXPANDED] id=TextStyle-fontStretch-EXTRA_EXPANDED-Link\n" +
                "                      \"Ultra Expanded\" [#/1/SpreadsheetName1/cell/A1/style/font-stretch/save/ULTRA_EXPANDED] id=TextStyle-fontStretch-ULTRA_EXPANDED-Link\n" +
                "          FontStyleComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Font Style\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/font-style/save/] id=TextStyle-fontStyle-Link\n" +
                "                      \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/font-style/save/NORMAL] id=TextStyle-fontStyle-NORMAL-Link\n" +
                "                      mdi-format-italic \"Italic\" [#/1/SpreadsheetName1/cell/A1/style/font-style/save/ITALIC] CHECKED id=TextStyle-fontStyle-ITALIC-Link\n" +
                "                      \"Oblique\" [#/1/SpreadsheetName1/cell/A1/style/font-style/save/OBLIQUE] id=TextStyle-fontStyle-OBLIQUE-Link\n" +
                "          FontVariantComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Font Variant\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/font-variant/save/] id=TextStyle-fontVariant-Link\n" +
                "                      \"Initial\" [#/1/SpreadsheetName1/cell/A1/style/font-variant/save/INITIAL] id=TextStyle-fontVariant-INITIAL-Link\n" +
                "                      \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/font-variant/save/NORMAL] id=TextStyle-fontVariant-NORMAL-Link\n" +
                "                      \"Small Caps\" [#/1/SpreadsheetName1/cell/A1/style/font-variant/save/SMALL_CAPS] CHECKED id=TextStyle-fontVariant-SMALL_CAPS-Link\n" +
                "          HangingPunctuationComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Hanging Punctuation\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/hanging-punctuation/save/] id=TextStyle-hangingPunctuation-Link\n" +
                "                      \"None\" [#/1/SpreadsheetName1/cell/A1/style/hanging-punctuation/save/NONE] id=TextStyle-hangingPunctuation-NONE-Link\n" +
                "                      \"First\" [#/1/SpreadsheetName1/cell/A1/style/hanging-punctuation/save/FIRST] CHECKED id=TextStyle-hangingPunctuation-FIRST-Link\n" +
                "                      \"Last\" [#/1/SpreadsheetName1/cell/A1/style/hanging-punctuation/save/LAST] id=TextStyle-hangingPunctuation-LAST-Link\n" +
                "                      \"Allow End\" [#/1/SpreadsheetName1/cell/A1/style/hanging-punctuation/save/ALLOW_END] id=TextStyle-hangingPunctuation-ALLOW_END-Link\n" +
                "                      \"Force End\" [#/1/SpreadsheetName1/cell/A1/style/hanging-punctuation/save/FORCE_END] id=TextStyle-hangingPunctuation-FORCE_END-Link\n" +
                "          HyphensComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Hyphens\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/hyphens/save/] id=TextStyle-hyphens-Link\n" +
                "                      \"None\" [#/1/SpreadsheetName1/cell/A1/style/hyphens/save/NONE] id=TextStyle-hyphens-NONE-Link\n" +
                "                      \"Manual\" [#/1/SpreadsheetName1/cell/A1/style/hyphens/save/MANUAL] id=TextStyle-hyphens-MANUAL-Link\n" +
                "                      \"Auto\" [#/1/SpreadsheetName1/cell/A1/style/hyphens/save/AUTO] CHECKED id=TextStyle-hyphens-AUTO-Link\n" +
                "          OverflowComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Overflow X\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/overflow-x/save/] id=TextStyle-overflowX-Link\n" +
                "                      \"Visible\" [#/1/SpreadsheetName1/cell/A1/style/overflow-x/save/VISIBLE] CHECKED id=TextStyle-overflowX-VISIBLE-Link\n" +
                "                      \"Hidden\" [#/1/SpreadsheetName1/cell/A1/style/overflow-x/save/HIDDEN] id=TextStyle-overflowX-HIDDEN-Link\n" +
                "                      \"Scroll\" [#/1/SpreadsheetName1/cell/A1/style/overflow-x/save/SCROLL] id=TextStyle-overflowX-SCROLL-Link\n" +
                "                      \"Auto\" [#/1/SpreadsheetName1/cell/A1/style/overflow-x/save/AUTO] id=TextStyle-overflowX-AUTO-Link\n" +
                "          OverflowComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Overflow Y\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/overflow-y/save/] id=TextStyle-overflowY-Link\n" +
                "                      \"Visible\" [#/1/SpreadsheetName1/cell/A1/style/overflow-y/save/VISIBLE] id=TextStyle-overflowY-VISIBLE-Link\n" +
                "                      \"Hidden\" [#/1/SpreadsheetName1/cell/A1/style/overflow-y/save/HIDDEN] CHECKED id=TextStyle-overflowY-HIDDEN-Link\n" +
                "                      \"Scroll\" [#/1/SpreadsheetName1/cell/A1/style/overflow-y/save/SCROLL] id=TextStyle-overflowY-SCROLL-Link\n" +
                "                      \"Auto\" [#/1/SpreadsheetName1/cell/A1/style/overflow-y/save/AUTO] id=TextStyle-overflowY-AUTO-Link\n" +
                "          OverflowWrapComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Overflow Wrap\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/overflow-wrap/save/] id=TextStyle-overflowWrap-Link\n" +
                "                      \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/overflow-wrap/save/NORMAL] id=TextStyle-overflowWrap-NORMAL-Link\n" +
                "                      \"Anywhere\" [#/1/SpreadsheetName1/cell/A1/style/overflow-wrap/save/ANYWHERE] id=TextStyle-overflowWrap-ANYWHERE-Link\n" +
                "                      \"Break Word\" [#/1/SpreadsheetName1/cell/A1/style/overflow-wrap/save/BREAK_WORD] CHECKED id=TextStyle-overflowWrap-BREAK_WORD-Link\n" +
                "          TextAlignComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Text Align\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      mdi-format-clear \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/text-align/save/] id=TextStyle-textAlign-Link\n" +
                "                      mdi-format-align-left \"Left\" [#/1/SpreadsheetName1/cell/A1/style/text-align/save/LEFT] id=TextStyle-textAlign-LEFT-Link\n" +
                "                      mdi-format-align-center \"Center\" [#/1/SpreadsheetName1/cell/A1/style/text-align/save/CENTER] CHECKED id=TextStyle-textAlign-CENTER-Link\n" +
                "                      mdi-format-align-right \"Right\" [#/1/SpreadsheetName1/cell/A1/style/text-align/save/RIGHT] id=TextStyle-textAlign-RIGHT-Link\n" +
                "                      mdi-format-align-justify \"Justify\" [#/1/SpreadsheetName1/cell/A1/style/text-align/save/JUSTIFY] id=TextStyle-textAlign-JUSTIFY-Link\n" +
                "          TextDecorationLineComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Text Decoration Line\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-line/save/] id=TextStyle-textDecorationLine-Link\n" +
                "                      mdi-format-clear \"None\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-line/save/NONE] id=TextStyle-textDecorationLine-NONE-Link\n" +
                "                      mdi-format-strikethrough \"Strikethrough\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-line/save/LINE_THROUGH] id=TextStyle-textDecorationLine-LINE_THROUGH-Link\n" +
                "                      \"Overline\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-line/save/OVERLINE] CHECKED id=TextStyle-textDecorationLine-OVERLINE-Link\n" +
                "                      mdi-format-underline \"Underline\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-line/save/UNDERLINE] id=TextStyle-textDecorationLine-UNDERLINE-Link\n" +
                "          TextDecorationStyleComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Text Decoration Style\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-style/save/] id=TextStyle-textDecorationStyle-Link\n" +
                "                      \"Solid\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-style/save/SOLID] id=TextStyle-textDecorationStyle-SOLID-Link\n" +
                "                      \"Double\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-style/save/DOUBLE] id=TextStyle-textDecorationStyle-DOUBLE-Link\n" +
                "                      \"Dashed\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-style/save/DASHED] id=TextStyle-textDecorationStyle-DASHED-Link\n" +
                "                      \"Dotted\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-style/save/DOTTED] CHECKED id=TextStyle-textDecorationStyle-DOTTED-Link\n" +
                "                      \"Wavy\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-style/save/WAVY] id=TextStyle-textDecorationStyle-WAVY-Link\n" +
                "          TextJustifyComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Text Justify\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/text-justify/save/] id=TextStyle-textJustify-Link\n" +
                "                      \"Auto\" [#/1/SpreadsheetName1/cell/A1/style/text-justify/save/AUTO] id=TextStyle-textJustify-AUTO-Link\n" +
                "                      \"Inter Word\" [#/1/SpreadsheetName1/cell/A1/style/text-justify/save/INTER_WORD] CHECKED id=TextStyle-textJustify-INTER_WORD-Link\n" +
                "                      \"Inter Character\" [#/1/SpreadsheetName1/cell/A1/style/text-justify/save/INTER_CHARACTER] id=TextStyle-textJustify-INTER_CHARACTER-Link\n" +
                "          TextTransformComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Text Transform\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/text-transform/save/] id=TextStyle-textTransform-Link\n" +
                "                      \"None\" [#/1/SpreadsheetName1/cell/A1/style/text-transform/save/NONE] id=TextStyle-textTransform-NONE-Link\n" +
                "                      mdi-format-letter-case \"Capitalize\" [#/1/SpreadsheetName1/cell/A1/style/text-transform/save/CAPITALIZE] id=TextStyle-textTransform-CAPITALIZE-Link\n" +
                "                      mdi-format-letter-case-upper \"Uppercase\" [#/1/SpreadsheetName1/cell/A1/style/text-transform/save/UPPERCASE] CHECKED id=TextStyle-textTransform-UPPERCASE-Link\n" +
                "                      mdi-format-letter-case-lower \"Lowercase\" [#/1/SpreadsheetName1/cell/A1/style/text-transform/save/LOWERCASE] id=TextStyle-textTransform-LOWERCASE-Link\n" +
                "          TextWrappingComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Text Wrapping\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/text-wrapping/save/] id=TextStyle-textWrapping-Link\n" +
                "                      \"Overflow\" [#/1/SpreadsheetName1/cell/A1/style/text-wrapping/save/OVERFLOW] id=TextStyle-textWrapping-OVERFLOW-Link\n" +
                "                      \"Wrap\" [#/1/SpreadsheetName1/cell/A1/style/text-wrapping/save/WRAP] CHECKED id=TextStyle-textWrapping-WRAP-Link\n" +
                "                      \"Clip\" [#/1/SpreadsheetName1/cell/A1/style/text-wrapping/save/CLIP] id=TextStyle-textWrapping-CLIP-Link\n" +
                "          VerticalAlignComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Vertical Align\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      mdi-format-clear \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/vertical-align/save/] id=TextStyle-verticalAlign-Link\n" +
                "                      mdi-format-align-top \"Top\" [#/1/SpreadsheetName1/cell/A1/style/vertical-align/save/TOP] id=TextStyle-verticalAlign-TOP-Link\n" +
                "                      mdi-format-align-middle \"Middle\" [#/1/SpreadsheetName1/cell/A1/style/vertical-align/save/MIDDLE] CHECKED id=TextStyle-verticalAlign-MIDDLE-Link\n" +
                "                      mdi-format-align-bottom \"Bottom\" [#/1/SpreadsheetName1/cell/A1/style/vertical-align/save/BOTTOM] id=TextStyle-verticalAlign-BOTTOM-Link\n" +
                "          VisibilityComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Visibility\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/visibility/save/] id=TextStyle-visibility-Link\n" +
                "                      \"Visible\" [#/1/SpreadsheetName1/cell/A1/style/visibility/save/VISIBLE] CHECKED id=TextStyle-visibility-VISIBLE-Link\n" +
                "                      \"Hidden\" [#/1/SpreadsheetName1/cell/A1/style/visibility/save/HIDDEN] id=TextStyle-visibility-HIDDEN-Link\n" +
                "                      \"Collapse\" [#/1/SpreadsheetName1/cell/A1/style/visibility/save/COLLAPSE] id=TextStyle-visibility-COLLAPSE-Link\n" +
                "          WhitespaceComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              White Space\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/white-space/save/] id=TextStyle-whiteSpace-Link\n" +
                "                      \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/white-space/save/NORMAL] id=TextStyle-whiteSpace-NORMAL-Link\n" +
                "                      \"Nowrap\" [#/1/SpreadsheetName1/cell/A1/style/white-space/save/NOWRAP] CHECKED id=TextStyle-whiteSpace-NOWRAP-Link\n" +
                "                      \"Pre\" [#/1/SpreadsheetName1/cell/A1/style/white-space/save/PRE] id=TextStyle-whiteSpace-PRE-Link\n" +
                "                      \"Pre Line\" [#/1/SpreadsheetName1/cell/A1/style/white-space/save/PRE_LINE] id=TextStyle-whiteSpace-PRE_LINE-Link\n" +
                "                      \"Pre Wrap\" [#/1/SpreadsheetName1/cell/A1/style/white-space/save/PRE_WRAP] id=TextStyle-whiteSpace-PRE_WRAP-Link\n" +
                "          WordBreakComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Word Break\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/word-break/save/] id=TextStyle-wordBreak-Link\n" +
                "                      \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/word-break/save/NORMAL] id=TextStyle-wordBreak-NORMAL-Link\n" +
                "                      \"Break All\" [#/1/SpreadsheetName1/cell/A1/style/word-break/save/BREAK_ALL] id=TextStyle-wordBreak-BREAK_ALL-Link\n" +
                "                      \"Keep All\" [#/1/SpreadsheetName1/cell/A1/style/word-break/save/KEEP_ALL] id=TextStyle-wordBreak-KEEP_ALL-Link\n" +
                "                      \"Break Word\" [#/1/SpreadsheetName1/cell/A1/style/word-break/save/BREAK_WORD] CHECKED id=TextStyle-wordBreak-BREAK_WORD-Link\n" +
                "          WordWrapComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Word Wrap\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/word-wrap/save/] id=TextStyle-wordWrap-Link\n" +
                "                      \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/word-wrap/save/NORMAL] id=TextStyle-wordWrap-NORMAL-Link\n" +
                "                      \"Break Word\" [#/1/SpreadsheetName1/cell/A1/style/word-wrap/save/BREAK_WORD] CHECKED id=TextStyle-wordWrap-BREAK_WORD-Link\n" +
                "          WritingModeComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Writing Mode\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/writing-mode/save/] id=TextStyle-writingMode-Link\n" +
                "                      \"Horizontal Tb\" [#/1/SpreadsheetName1/cell/A1/style/writing-mode/save/HORIZONTAL_TB] CHECKED id=TextStyle-writingMode-HORIZONTAL_TB-Link\n" +
                "                      \"Vertical Lr\" [#/1/SpreadsheetName1/cell/A1/style/writing-mode/save/VERTICAL_LR] id=TextStyle-writingMode-VERTICAL_LR-Link\n" +
                "                      \"Vertical Rl\" [#/1/SpreadsheetName1/cell/A1/style/writing-mode/save/VERTICAL_RL] id=TextStyle-writingMode-VERTICAL_RL-Link\n" +
                "          TextStyleComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Style [direction: LTR; font-kerning: AUTO; font-stretch: NORMAL; font-style: ITALIC; font-variant: SMALL_CAPS; hanging-punctuation: FIRST; hyphens: AUTO; overflow-wrap: BREAK_WORD; overflow-x: VISIBLE; overflow-y: HIDDEN; text-align: CENTER; text-decoration-line: OVERLINE; text-decoration-style: DOTTED; text-justify: INTER_WORD; text-transform: UPPERCASE; text-wrapping: WRAP; vertical-align: MIDDLE; visibility: VISIBLE; white-space: NOWRAP; word-break: BREAK_WORD; word-wrap: BREAK_WORD; writing-mode: HORIZONTAL_TB;]\n" +
                "      DialogAnchorListComponent\n" +
                "        AnchorListComponent\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              \"Save\" [#/1/SpreadsheetName1/cell/A1/style/*/save/direction:%20LTR;%20font-kerning:%20AUTO;%20font-stretch:%20NORMAL;%20font-style:%20ITALIC;%20font-variant:%20SMALL_CAPS;%20hanging-punctuation:%20FIRST;%20hyphens:%20AUTO;%20overflow-wrap:%20BREAK_WORD;%20overflow-x:%20VISIBLE;%20overflow-y:%20HIDDEN;%20text-align:%20CENTER;%20text-decoration-line:%20OVERLINE;%20text-decoration-style:%20DOTTED;%20text-justify:%20INTER_WORD;%20text-transform:%20UPPERCASE;%20text-wrapping:%20WRAP;%20vertical-align:%20MIDDLE;%20visibility:%20VISIBLE;%20white-space:%20NOWRAP;%20word-break:%20BREAK_WORD;%20word-wrap:%20BREAK_WORD;%20writing-mode:%20HORIZONTAL_TB;] id=TextStyle-save-Link\n" +
                "              \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/*/save/] id=TextStyle-clear-Link\n" +
                "              \"Undo\" [#/1/SpreadsheetName1/cell/A1/style/*/save/] id=TextStyle-undo-Link\n" +
                "              \"Close\" [#/1/SpreadsheetName1/cell/A1] id=TextStyle-close-Link\n"
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
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          SpreadsheetExpressionReferenceComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Selection [] id=TextStyle-selection-TextBox REQUIRED\n" +
                "                Errors\n" +
                "                  Empty \"text\"\n" +
                "          DirectionComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Direction\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/direction/save/] id=TextStyle-direction-Link\n" +
                "                      \"Left to Right\" [#/1/SpreadsheetName1/spreadsheet/style/direction/save/LTR] CHECKED id=TextStyle-direction-LTR-Link\n" +
                "                      \"Right to Left\" [#/1/SpreadsheetName1/spreadsheet/style/direction/save/RTL] id=TextStyle-direction-RTL-Link\n" +
                "          FontKerningComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Font Kerning\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/font-kerning/save/] id=TextStyle-fontKerning-Link\n" +
                "                      \"Auto\" [#/1/SpreadsheetName1/spreadsheet/style/font-kerning/save/AUTO] CHECKED id=TextStyle-fontKerning-AUTO-Link\n" +
                "                      \"None\" [#/1/SpreadsheetName1/spreadsheet/style/font-kerning/save/NONE] id=TextStyle-fontKerning-NONE-Link\n" +
                "                      \"Normal\" [#/1/SpreadsheetName1/spreadsheet/style/font-kerning/save/NORMAL] id=TextStyle-fontKerning-NORMAL-Link\n" +
                "          FontStretchComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Font Stretch\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/font-stretch/save/] id=TextStyle-fontStretch-Link\n" +
                "                      \"Ultra Condensed\" [#/1/SpreadsheetName1/spreadsheet/style/font-stretch/save/ULTRA_CONDENSED] id=TextStyle-fontStretch-ULTRA_CONDENSED-Link\n" +
                "                      \"Extra Condensed\" [#/1/SpreadsheetName1/spreadsheet/style/font-stretch/save/EXTRA_CONDENSED] id=TextStyle-fontStretch-EXTRA_CONDENSED-Link\n" +
                "                      \"Condensed\" [#/1/SpreadsheetName1/spreadsheet/style/font-stretch/save/CONDENSED] id=TextStyle-fontStretch-CONDENSED-Link\n" +
                "                      \"Semi Condensed\" [#/1/SpreadsheetName1/spreadsheet/style/font-stretch/save/SEMI_CONDENSED] id=TextStyle-fontStretch-SEMI_CONDENSED-Link\n" +
                "                      \"Normal\" [#/1/SpreadsheetName1/spreadsheet/style/font-stretch/save/NORMAL] CHECKED id=TextStyle-fontStretch-NORMAL-Link\n" +
                "                      \"Expanded\" [#/1/SpreadsheetName1/spreadsheet/style/font-stretch/save/EXPANDED] id=TextStyle-fontStretch-EXPANDED-Link\n" +
                "                      \"Extra Expanded\" [#/1/SpreadsheetName1/spreadsheet/style/font-stretch/save/EXTRA_EXPANDED] id=TextStyle-fontStretch-EXTRA_EXPANDED-Link\n" +
                "                      \"Ultra Expanded\" [#/1/SpreadsheetName1/spreadsheet/style/font-stretch/save/ULTRA_EXPANDED] id=TextStyle-fontStretch-ULTRA_EXPANDED-Link\n" +
                "          FontStyleComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Font Style\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/font-style/save/] id=TextStyle-fontStyle-Link\n" +
                "                      \"Normal\" [#/1/SpreadsheetName1/spreadsheet/style/font-style/save/NORMAL] id=TextStyle-fontStyle-NORMAL-Link\n" +
                "                      mdi-format-italic \"Italic\" [#/1/SpreadsheetName1/spreadsheet/style/font-style/save/ITALIC] CHECKED id=TextStyle-fontStyle-ITALIC-Link\n" +
                "                      \"Oblique\" [#/1/SpreadsheetName1/spreadsheet/style/font-style/save/OBLIQUE] id=TextStyle-fontStyle-OBLIQUE-Link\n" +
                "          FontVariantComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Font Variant\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/font-variant/save/] id=TextStyle-fontVariant-Link\n" +
                "                      \"Initial\" [#/1/SpreadsheetName1/spreadsheet/style/font-variant/save/INITIAL] id=TextStyle-fontVariant-INITIAL-Link\n" +
                "                      \"Normal\" [#/1/SpreadsheetName1/spreadsheet/style/font-variant/save/NORMAL] id=TextStyle-fontVariant-NORMAL-Link\n" +
                "                      \"Small Caps\" [#/1/SpreadsheetName1/spreadsheet/style/font-variant/save/SMALL_CAPS] CHECKED id=TextStyle-fontVariant-SMALL_CAPS-Link\n" +
                "          HangingPunctuationComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Hanging Punctuation\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/hanging-punctuation/save/] id=TextStyle-hangingPunctuation-Link\n" +
                "                      \"None\" [#/1/SpreadsheetName1/spreadsheet/style/hanging-punctuation/save/NONE] id=TextStyle-hangingPunctuation-NONE-Link\n" +
                "                      \"First\" [#/1/SpreadsheetName1/spreadsheet/style/hanging-punctuation/save/FIRST] CHECKED id=TextStyle-hangingPunctuation-FIRST-Link\n" +
                "                      \"Last\" [#/1/SpreadsheetName1/spreadsheet/style/hanging-punctuation/save/LAST] id=TextStyle-hangingPunctuation-LAST-Link\n" +
                "                      \"Allow End\" [#/1/SpreadsheetName1/spreadsheet/style/hanging-punctuation/save/ALLOW_END] id=TextStyle-hangingPunctuation-ALLOW_END-Link\n" +
                "                      \"Force End\" [#/1/SpreadsheetName1/spreadsheet/style/hanging-punctuation/save/FORCE_END] id=TextStyle-hangingPunctuation-FORCE_END-Link\n" +
                "          HyphensComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Hyphens\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/hyphens/save/] id=TextStyle-hyphens-Link\n" +
                "                      \"None\" [#/1/SpreadsheetName1/spreadsheet/style/hyphens/save/NONE] id=TextStyle-hyphens-NONE-Link\n" +
                "                      \"Manual\" [#/1/SpreadsheetName1/spreadsheet/style/hyphens/save/MANUAL] id=TextStyle-hyphens-MANUAL-Link\n" +
                "                      \"Auto\" [#/1/SpreadsheetName1/spreadsheet/style/hyphens/save/AUTO] CHECKED id=TextStyle-hyphens-AUTO-Link\n" +
                "          OverflowComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Overflow X\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-x/save/] id=TextStyle-overflowX-Link\n" +
                "                      \"Visible\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-x/save/VISIBLE] CHECKED id=TextStyle-overflowX-VISIBLE-Link\n" +
                "                      \"Hidden\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-x/save/HIDDEN] id=TextStyle-overflowX-HIDDEN-Link\n" +
                "                      \"Scroll\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-x/save/SCROLL] id=TextStyle-overflowX-SCROLL-Link\n" +
                "                      \"Auto\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-x/save/AUTO] id=TextStyle-overflowX-AUTO-Link\n" +
                "          OverflowComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Overflow Y\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-y/save/] id=TextStyle-overflowY-Link\n" +
                "                      \"Visible\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-y/save/VISIBLE] id=TextStyle-overflowY-VISIBLE-Link\n" +
                "                      \"Hidden\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-y/save/HIDDEN] CHECKED id=TextStyle-overflowY-HIDDEN-Link\n" +
                "                      \"Scroll\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-y/save/SCROLL] id=TextStyle-overflowY-SCROLL-Link\n" +
                "                      \"Auto\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-y/save/AUTO] id=TextStyle-overflowY-AUTO-Link\n" +
                "          OverflowWrapComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Overflow Wrap\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-wrap/save/] id=TextStyle-overflowWrap-Link\n" +
                "                      \"Normal\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-wrap/save/NORMAL] id=TextStyle-overflowWrap-NORMAL-Link\n" +
                "                      \"Anywhere\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-wrap/save/ANYWHERE] id=TextStyle-overflowWrap-ANYWHERE-Link\n" +
                "                      \"Break Word\" [#/1/SpreadsheetName1/spreadsheet/style/overflow-wrap/save/BREAK_WORD] CHECKED id=TextStyle-overflowWrap-BREAK_WORD-Link\n" +
                "          TextAlignComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Text Align\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      mdi-format-clear \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/] id=TextStyle-textAlign-Link\n" +
                "                      mdi-format-align-left \"Left\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/LEFT] id=TextStyle-textAlign-LEFT-Link\n" +
                "                      mdi-format-align-center \"Center\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/CENTER] CHECKED id=TextStyle-textAlign-CENTER-Link\n" +
                "                      mdi-format-align-right \"Right\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/RIGHT] id=TextStyle-textAlign-RIGHT-Link\n" +
                "                      mdi-format-align-justify \"Justify\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/JUSTIFY] id=TextStyle-textAlign-JUSTIFY-Link\n" +
                "          TextDecorationLineComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Text Decoration Line\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-line/save/] id=TextStyle-textDecorationLine-Link\n" +
                "                      mdi-format-clear \"None\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-line/save/NONE] id=TextStyle-textDecorationLine-NONE-Link\n" +
                "                      mdi-format-strikethrough \"Strikethrough\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-line/save/LINE_THROUGH] id=TextStyle-textDecorationLine-LINE_THROUGH-Link\n" +
                "                      \"Overline\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-line/save/OVERLINE] CHECKED id=TextStyle-textDecorationLine-OVERLINE-Link\n" +
                "                      mdi-format-underline \"Underline\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-line/save/UNDERLINE] id=TextStyle-textDecorationLine-UNDERLINE-Link\n" +
                "          TextDecorationStyleComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Text Decoration Style\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-style/save/] id=TextStyle-textDecorationStyle-Link\n" +
                "                      \"Solid\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-style/save/SOLID] id=TextStyle-textDecorationStyle-SOLID-Link\n" +
                "                      \"Double\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-style/save/DOUBLE] id=TextStyle-textDecorationStyle-DOUBLE-Link\n" +
                "                      \"Dashed\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-style/save/DASHED] id=TextStyle-textDecorationStyle-DASHED-Link\n" +
                "                      \"Dotted\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-style/save/DOTTED] CHECKED id=TextStyle-textDecorationStyle-DOTTED-Link\n" +
                "                      \"Wavy\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-style/save/WAVY] id=TextStyle-textDecorationStyle-WAVY-Link\n" +
                "          TextJustifyComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Text Justify\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/text-justify/save/] id=TextStyle-textJustify-Link\n" +
                "                      \"Auto\" [#/1/SpreadsheetName1/spreadsheet/style/text-justify/save/AUTO] id=TextStyle-textJustify-AUTO-Link\n" +
                "                      \"Inter Word\" [#/1/SpreadsheetName1/spreadsheet/style/text-justify/save/INTER_WORD] CHECKED id=TextStyle-textJustify-INTER_WORD-Link\n" +
                "                      \"Inter Character\" [#/1/SpreadsheetName1/spreadsheet/style/text-justify/save/INTER_CHARACTER] id=TextStyle-textJustify-INTER_CHARACTER-Link\n" +
                "          TextTransformComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Text Transform\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/text-transform/save/] id=TextStyle-textTransform-Link\n" +
                "                      \"None\" [#/1/SpreadsheetName1/spreadsheet/style/text-transform/save/NONE] id=TextStyle-textTransform-NONE-Link\n" +
                "                      mdi-format-letter-case \"Capitalize\" [#/1/SpreadsheetName1/spreadsheet/style/text-transform/save/CAPITALIZE] id=TextStyle-textTransform-CAPITALIZE-Link\n" +
                "                      mdi-format-letter-case-upper \"Uppercase\" [#/1/SpreadsheetName1/spreadsheet/style/text-transform/save/UPPERCASE] CHECKED id=TextStyle-textTransform-UPPERCASE-Link\n" +
                "                      mdi-format-letter-case-lower \"Lowercase\" [#/1/SpreadsheetName1/spreadsheet/style/text-transform/save/LOWERCASE] id=TextStyle-textTransform-LOWERCASE-Link\n" +
                "          TextWrappingComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Text Wrapping\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/text-wrapping/save/] id=TextStyle-textWrapping-Link\n" +
                "                      \"Overflow\" [#/1/SpreadsheetName1/spreadsheet/style/text-wrapping/save/OVERFLOW] id=TextStyle-textWrapping-OVERFLOW-Link\n" +
                "                      \"Wrap\" [#/1/SpreadsheetName1/spreadsheet/style/text-wrapping/save/WRAP] CHECKED id=TextStyle-textWrapping-WRAP-Link\n" +
                "                      \"Clip\" [#/1/SpreadsheetName1/spreadsheet/style/text-wrapping/save/CLIP] id=TextStyle-textWrapping-CLIP-Link\n" +
                "          VerticalAlignComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Vertical Align\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      mdi-format-clear \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/vertical-align/save/] id=TextStyle-verticalAlign-Link\n" +
                "                      mdi-format-align-top \"Top\" [#/1/SpreadsheetName1/spreadsheet/style/vertical-align/save/TOP] id=TextStyle-verticalAlign-TOP-Link\n" +
                "                      mdi-format-align-middle \"Middle\" [#/1/SpreadsheetName1/spreadsheet/style/vertical-align/save/MIDDLE] CHECKED id=TextStyle-verticalAlign-MIDDLE-Link\n" +
                "                      mdi-format-align-bottom \"Bottom\" [#/1/SpreadsheetName1/spreadsheet/style/vertical-align/save/BOTTOM] id=TextStyle-verticalAlign-BOTTOM-Link\n" +
                "          VisibilityComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Visibility\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/visibility/save/] id=TextStyle-visibility-Link\n" +
                "                      \"Visible\" [#/1/SpreadsheetName1/spreadsheet/style/visibility/save/VISIBLE] CHECKED id=TextStyle-visibility-VISIBLE-Link\n" +
                "                      \"Hidden\" [#/1/SpreadsheetName1/spreadsheet/style/visibility/save/HIDDEN] id=TextStyle-visibility-HIDDEN-Link\n" +
                "                      \"Collapse\" [#/1/SpreadsheetName1/spreadsheet/style/visibility/save/COLLAPSE] id=TextStyle-visibility-COLLAPSE-Link\n" +
                "          WhitespaceComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              White Space\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/white-space/save/] id=TextStyle-whiteSpace-Link\n" +
                "                      \"Normal\" [#/1/SpreadsheetName1/spreadsheet/style/white-space/save/NORMAL] id=TextStyle-whiteSpace-NORMAL-Link\n" +
                "                      \"Nowrap\" [#/1/SpreadsheetName1/spreadsheet/style/white-space/save/NOWRAP] CHECKED id=TextStyle-whiteSpace-NOWRAP-Link\n" +
                "                      \"Pre\" [#/1/SpreadsheetName1/spreadsheet/style/white-space/save/PRE] id=TextStyle-whiteSpace-PRE-Link\n" +
                "                      \"Pre Line\" [#/1/SpreadsheetName1/spreadsheet/style/white-space/save/PRE_LINE] id=TextStyle-whiteSpace-PRE_LINE-Link\n" +
                "                      \"Pre Wrap\" [#/1/SpreadsheetName1/spreadsheet/style/white-space/save/PRE_WRAP] id=TextStyle-whiteSpace-PRE_WRAP-Link\n" +
                "          WordBreakComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Word Break\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/word-break/save/] id=TextStyle-wordBreak-Link\n" +
                "                      \"Normal\" [#/1/SpreadsheetName1/spreadsheet/style/word-break/save/NORMAL] id=TextStyle-wordBreak-NORMAL-Link\n" +
                "                      \"Break All\" [#/1/SpreadsheetName1/spreadsheet/style/word-break/save/BREAK_ALL] id=TextStyle-wordBreak-BREAK_ALL-Link\n" +
                "                      \"Keep All\" [#/1/SpreadsheetName1/spreadsheet/style/word-break/save/KEEP_ALL] id=TextStyle-wordBreak-KEEP_ALL-Link\n" +
                "                      \"Break Word\" [#/1/SpreadsheetName1/spreadsheet/style/word-break/save/BREAK_WORD] CHECKED id=TextStyle-wordBreak-BREAK_WORD-Link\n" +
                "          WordWrapComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Word Wrap\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/word-wrap/save/] id=TextStyle-wordWrap-Link\n" +
                "                      \"Normal\" [#/1/SpreadsheetName1/spreadsheet/style/word-wrap/save/NORMAL] id=TextStyle-wordWrap-NORMAL-Link\n" +
                "                      \"Break Word\" [#/1/SpreadsheetName1/spreadsheet/style/word-wrap/save/BREAK_WORD] CHECKED id=TextStyle-wordWrap-BREAK_WORD-Link\n" +
                "          WritingModeComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Writing Mode\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/writing-mode/save/] id=TextStyle-writingMode-Link\n" +
                "                      \"Horizontal Tb\" [#/1/SpreadsheetName1/spreadsheet/style/writing-mode/save/HORIZONTAL_TB] CHECKED id=TextStyle-writingMode-HORIZONTAL_TB-Link\n" +
                "                      \"Vertical Lr\" [#/1/SpreadsheetName1/spreadsheet/style/writing-mode/save/VERTICAL_LR] id=TextStyle-writingMode-VERTICAL_LR-Link\n" +
                "                      \"Vertical Rl\" [#/1/SpreadsheetName1/spreadsheet/style/writing-mode/save/VERTICAL_RL] id=TextStyle-writingMode-VERTICAL_RL-Link\n" +
                "          TextStyleComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Style [direction: LTR; font-kerning: AUTO; font-stretch: NORMAL; font-style: ITALIC; font-variant: SMALL_CAPS; hanging-punctuation: FIRST; hyphens: AUTO; overflow-wrap: BREAK_WORD; overflow-x: VISIBLE; overflow-y: HIDDEN; text-align: CENTER; text-decoration-line: OVERLINE; text-decoration-style: DOTTED; text-justify: INTER_WORD; text-transform: UPPERCASE; text-wrapping: WRAP; vertical-align: MIDDLE; visibility: VISIBLE; white-space: NOWRAP; word-break: BREAK_WORD; word-wrap: BREAK_WORD; writing-mode: HORIZONTAL_TB;]\n" +
                "      DialogAnchorListComponent\n" +
                "        AnchorListComponent\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              \"Save\" [#/1/SpreadsheetName1/spreadsheet/style/*/save/direction:%20LTR;%20font-kerning:%20AUTO;%20font-stretch:%20NORMAL;%20font-style:%20ITALIC;%20font-variant:%20SMALL_CAPS;%20hanging-punctuation:%20FIRST;%20hyphens:%20AUTO;%20overflow-wrap:%20BREAK_WORD;%20overflow-x:%20VISIBLE;%20overflow-y:%20HIDDEN;%20text-align:%20CENTER;%20text-decoration-line:%20OVERLINE;%20text-decoration-style:%20DOTTED;%20text-justify:%20INTER_WORD;%20text-transform:%20UPPERCASE;%20text-wrapping:%20WRAP;%20vertical-align:%20MIDDLE;%20visibility:%20VISIBLE;%20white-space:%20NOWRAP;%20word-break:%20BREAK_WORD;%20word-wrap:%20BREAK_WORD;%20writing-mode:%20HORIZONTAL_TB;] id=TextStyle-save-Link\n" +
                "              \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/*/save/] id=TextStyle-clear-Link\n" +
                "              \"Undo\" [#/1/SpreadsheetName1/spreadsheet/style/*/save/text-align:%20LEFT;] id=TextStyle-undo-Link\n" +
                "              \"Close\" [#/1/SpreadsheetName1] id=TextStyle-close-Link\n"
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
                return METADATA.setOrRemove(
                    SpreadsheetMetadataPropertyName.STYLE,
                    textStyle.orElse(null)
                );
            }

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
