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

    private final static TextStyle VALUE_TEXT_STYLE = TextStyle.parse("text-align: CENTER; vertical-align: MIDDLE");

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
                "                      \"Left to Right\" [#/1/SpreadsheetName1/cell/A1/style/direction/save/LTR] id=TextStyle-direction-LTR-Link\n" +
                "                      \"Right to Left\" [#/1/SpreadsheetName1/cell/A1/style/direction/save/RTL] id=TextStyle-direction-RTL-Link\n" +
                "          FontKerningComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Font Kerning\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/font-kerning/save/] id=TextStyle-fontKerning-Link\n" +
                "                      \"Auto\" [#/1/SpreadsheetName1/cell/A1/style/font-kerning/save/AUTO] id=TextStyle-fontKerning-AUTO-Link\n" +
                "                      \"None\" [#/1/SpreadsheetName1/cell/A1/style/font-kerning/save/NONE] id=TextStyle-fontKerning-NONE-Link\n" +
                "                      \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/font-kerning/save/NORMAL] id=TextStyle-fontKerning-NORMAL-Link\n" +
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
                "                      \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-line/save/] CHECKED id=TextStyle-textDecorationLine-Link\n" +
                "                      mdi-format-clear \"None\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-line/save/NONE] id=TextStyle-textDecorationLine-NONE-Link\n" +
                "                      mdi-format-strikethrough \"Strikethrough\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-line/save/LINE_THROUGH] id=TextStyle-textDecorationLine-LINE_THROUGH-Link\n" +
                "                      \"Overline\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-line/save/OVERLINE] id=TextStyle-textDecorationLine-OVERLINE-Link\n" +
                "                      mdi-format-underline \"Underline\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-line/save/UNDERLINE] id=TextStyle-textDecorationLine-UNDERLINE-Link\n" +
                "          TextDecorationStyleComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Text Decoration Style\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-style/save/] CHECKED id=TextStyle-textDecorationStyle-Link\n" +
                "                      \"Solid\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-style/save/SOLID] id=TextStyle-textDecorationStyle-SOLID-Link\n" +
                "                      \"Double\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-style/save/DOUBLE] id=TextStyle-textDecorationStyle-DOUBLE-Link\n" +
                "                      \"Dashed\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-style/save/DASHED] id=TextStyle-textDecorationStyle-DASHED-Link\n" +
                "                      \"Dotted\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-style/save/DOTTED] id=TextStyle-textDecorationStyle-DOTTED-Link\n" +
                "                      \"Wavy\" [#/1/SpreadsheetName1/cell/A1/style/text-decoration-style/save/WAVY] id=TextStyle-textDecorationStyle-WAVY-Link\n" +
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
                "          TextStyleComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Style [text-align: center; vertical-align: middle;]\n" +
                "      DialogAnchorListComponent\n" +
                "        AnchorListComponent\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              \"Save\" [#/1/SpreadsheetName1/cell/A1/style/*/save/text-align:%20center;%20vertical-align:%20middle;] id=TextStyle-save-Link\n" +
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
                "                      \"Left to Right\" [#/1/SpreadsheetName1/spreadsheet/style/direction/save/LTR] id=TextStyle-direction-LTR-Link\n" +
                "                      \"Right to Left\" [#/1/SpreadsheetName1/spreadsheet/style/direction/save/RTL] id=TextStyle-direction-RTL-Link\n" +
                "          FontKerningComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Font Kerning\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/font-kerning/save/] id=TextStyle-fontKerning-Link\n" +
                "                      \"Auto\" [#/1/SpreadsheetName1/spreadsheet/style/font-kerning/save/AUTO] id=TextStyle-fontKerning-AUTO-Link\n" +
                "                      \"None\" [#/1/SpreadsheetName1/spreadsheet/style/font-kerning/save/NONE] id=TextStyle-fontKerning-NONE-Link\n" +
                "                      \"Normal\" [#/1/SpreadsheetName1/spreadsheet/style/font-kerning/save/NORMAL] id=TextStyle-fontKerning-NORMAL-Link\n" +
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
                "                      \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-line/save/] CHECKED id=TextStyle-textDecorationLine-Link\n" +
                "                      mdi-format-clear \"None\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-line/save/NONE] id=TextStyle-textDecorationLine-NONE-Link\n" +
                "                      mdi-format-strikethrough \"Strikethrough\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-line/save/LINE_THROUGH] id=TextStyle-textDecorationLine-LINE_THROUGH-Link\n" +
                "                      \"Overline\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-line/save/OVERLINE] id=TextStyle-textDecorationLine-OVERLINE-Link\n" +
                "                      mdi-format-underline \"Underline\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-line/save/UNDERLINE] id=TextStyle-textDecorationLine-UNDERLINE-Link\n" +
                "          TextDecorationStyleComponent\n" +
                "            TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "              Text Decoration Style\n" +
                "                AnchorListComponent\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-style/save/] CHECKED id=TextStyle-textDecorationStyle-Link\n" +
                "                      \"Solid\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-style/save/SOLID] id=TextStyle-textDecorationStyle-SOLID-Link\n" +
                "                      \"Double\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-style/save/DOUBLE] id=TextStyle-textDecorationStyle-DOUBLE-Link\n" +
                "                      \"Dashed\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-style/save/DASHED] id=TextStyle-textDecorationStyle-DASHED-Link\n" +
                "                      \"Dotted\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-style/save/DOTTED] id=TextStyle-textDecorationStyle-DOTTED-Link\n" +
                "                      \"Wavy\" [#/1/SpreadsheetName1/spreadsheet/style/text-decoration-style/save/WAVY] id=TextStyle-textDecorationStyle-WAVY-Link\n" +
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
                "          TextStyleComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Style [text-align: center; vertical-align: middle;]\n" +
                "      DialogAnchorListComponent\n" +
                "        AnchorListComponent\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              \"Save\" [#/1/SpreadsheetName1/spreadsheet/style/*/save/text-align:%20center;%20vertical-align:%20middle;] id=TextStyle-save-Link\n" +
                "              \"Clear\" [#/1/SpreadsheetName1/spreadsheet/style/*/save/] id=TextStyle-clear-Link\n" +
                "              \"Undo\" [#/1/SpreadsheetName1/spreadsheet/style/*/save/text-align:%20left;] id=TextStyle-undo-Link\n" +
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
