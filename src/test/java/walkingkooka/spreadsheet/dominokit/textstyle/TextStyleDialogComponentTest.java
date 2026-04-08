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

    private final static TextStyle TEXT_STYLE = TextStyle.parse("text-align: LEFT;");

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

        component.textStyle.setValue(
            Optional.of(TEXT_STYLE)
        );

        this.treePrintAndCheck(
            component,
            "TextStyleDialogComponent\n" +
                "  DialogComponent\n" +
                "    id=TextStyle-Dialog includeClose=true CLOSED\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          TextStyleComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                [text-align: left;]\n" +
                "      DialogAnchorListComponent\n" +
                "        AnchorListComponent\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              \"Save\" [#/1/SpreadsheetName1/cell/A1/style/*/save/text-align:%20left;] id=TextStyle-save-Link\n" +
                "              \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/*/save/] id=TextStyle-clear-Link\n" +
                "              \"Undo\" [#/1/SpreadsheetName1/cell/A1/style/*/save/] id=TextStyle-undo-Link\n" +
                "              \"Close\" [#/1/SpreadsheetName1/cell/A1] id=TextStyle-close-Link\n"
        );
    }

    @Test
    public void testSetValueWithMetadataStyleWithoutTextStylePropertyName() {
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

        component.textStyle.setValue(
            Optional.of(TEXT_STYLE)
        );

        this.treePrintAndCheck(
            component,
            "TextStyleDialogComponent\n" +
                "  DialogComponent\n" +
                "    id=TextStyle-Dialog includeClose=true CLOSED\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          TextStyleComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                [text-align: left;]\n" +
                "      DialogAnchorListComponent\n" +
                "        AnchorListComponent\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              \"Save\" [#/1/SpreadsheetName1/cell/A1/style/*/save/text-align:%20left;] id=TextStyle-save-Link\n" +
                "              \"Clear\" [#/1/SpreadsheetName1/cell/A1/style/*/save/] id=TextStyle-clear-Link\n" +
                "              \"Undo\" [#/1/SpreadsheetName1/cell/A1/style/*/save/text-align:%20left;] id=TextStyle-undo-Link\n" +
                "              \"Close\" [#/1/SpreadsheetName1/cell/A1] id=TextStyle-close-Link\n"
        );
    }

    @Override
    public TextStyleDialogComponent createSpreadsheetDialogComponentLifecycle(final HistoryToken historyToken) {
        return TextStyleDialogComponent.with(
            TextStyleDialogComponentContexts.appContextMetadataStyle(
                this.appContext(
                    historyToken,
                    Optional.of(
                        TextStyle.parse("text-align: CENTER;")
                    )
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
                    TEXT_STYLE
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
