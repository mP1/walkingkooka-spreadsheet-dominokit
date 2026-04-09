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

package walkingkooka.spreadsheet.dominokit.textstyle;

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
                "          TextStyleComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                [text-align: center; vertical-align: middle;]\n" +
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
                "          TextStyleComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                [text-align: center; vertical-align: middle;]\n" +
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
                return null;
            }

            @Override
            public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
                return null;
            }

            @Override
            public void giveFocus(final Runnable focus) {
                // ignore
            }

            @Override
            public HistoryToken historyToken() {
                return historyToken;
            }

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
