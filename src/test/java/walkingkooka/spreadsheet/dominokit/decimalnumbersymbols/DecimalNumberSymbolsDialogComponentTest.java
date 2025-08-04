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

package walkingkooka.spreadsheet.dominokit.decimalnumbersymbols;

import org.junit.jupiter.api.Test;
import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.fetcher.DecimalNumberSymbolsFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

public final class DecimalNumberSymbolsDialogComponentTest implements SpreadsheetDialogComponentLifecycleTesting<DecimalNumberSymbolsDialogComponent>,
    HistoryTokenTesting,
    SpreadsheetMetadataTesting {

    // onHistoryToken...................................................................................................

    @Test
    public void testOnHistoryTokenWithSpreadsheetCellDecimalNumberSymbolsSelectHistoryToken() {
        final AppContext context = this.appContext(
            HistoryToken.cellDecimalNumberSymbolsSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            DECIMAL_NUMBER_SYMBOLS
        );

        this.onHistoryTokenChangeAndCheck2(
            DecimalNumberSymbolsDialogComponent.with(
                DecimalNumberSymbolsDialogComponentContexts.cell(context)
            ),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            context,
            "DecimalNumberSymbolsDialogComponent\n" +
                "  SpreadsheetDialogComponent\n" +
                "    A1: Decimal Number Symbols\n" +
                "    id=decimalNumberSymbols-Dialog includeClose=true\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          CharacterComponent\n" +
                "            ValueSpreadsheetTextBox\n" +
                "              SpreadsheetTextBox\n" +
                "                Negative sign [] id=decimalNumberSymbolsnegativeSign-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Negative sign\"\n" +
                "          CharacterComponent\n" +
                "            ValueSpreadsheetTextBox\n" +
                "              SpreadsheetTextBox\n" +
                "                Positive sign [] id=decimalNumberSymbolspositiveSign-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Positive sign\"\n" +
                "          CharacterComponent\n" +
                "            ValueSpreadsheetTextBox\n" +
                "              SpreadsheetTextBox\n" +
                "                Zero digit [] id=decimalNumberSymbolszeroDigit-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Zero digit\"\n" +
                "          SpreadsheetTextBox\n" +
                "            Currency [] id=decimalNumberSymbolsCurrencySymbol-TextBox\n" +
                "            Errors\n" +
                "              Required\n" +
                "          CharacterComponent\n" +
                "            ValueSpreadsheetTextBox\n" +
                "              SpreadsheetTextBox\n" +
                "                Decimal separator [] id=decimalNumberSymbolsdecimalSeparator-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Decimal separator\"\n" +
                "          SpreadsheetTextBox\n" +
                "            Exponent [] id=decimalNumberSymbolsExponentSymbol-TextBox\n" +
                "            Errors\n" +
                "              Required\n" +
                "          CharacterComponent\n" +
                "            ValueSpreadsheetTextBox\n" +
                "              SpreadsheetTextBox\n" +
                "                Group separator [] id=decimalNumberSymbolsgroupSeparator-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Group separator\"\n" +
                "          SpreadsheetTextBox\n" +
                "            Infinity [] id=decimalNumberSymbolsInfinitySymbol-TextBox\n" +
                "            Errors\n" +
                "              Required\n" +
                "          CharacterComponent\n" +
                "            ValueSpreadsheetTextBox\n" +
                "              SpreadsheetTextBox\n" +
                "                Monetary decimal separator [] id=decimalNumberSymbolsmonetaryDecimalSeparator-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Monetary decimal separator\"\n" +
                "          SpreadsheetTextBox\n" +
                "            Nan [] id=decimalNumberSymbolsNanSymbol-TextBox\n" +
                "            Errors\n" +
                "              Required\n" +
                "          CharacterComponent\n" +
                "            ValueSpreadsheetTextBox\n" +
                "              SpreadsheetTextBox\n" +
                "                Percent symbol [] id=decimalNumberSymbolspercentSymbol-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Percent symbol\"\n" +
                "          CharacterComponent\n" +
                "            ValueSpreadsheetTextBox\n" +
                "              SpreadsheetTextBox\n" +
                "                Permill symbol [] id=decimalNumberSymbolspermillSymbol-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Permill symbol\"\n" +
                "          DecimalNumberSymbolsComponent\n" +
                "            ValueSpreadsheetTextBox\n" +
                "              SpreadsheetTextBox\n" +
                "                Date Time Symbols []\n" +
                "                Errors\n" +
                "                  Expected 12 tokens but got 0\n" +
                "      SpreadsheetLocaleComponent\n" +
                "        SpreadsheetSuggestBoxComponent\n" +
                "          Load from Locale []\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" DISABLED id=decimalNumberSymbols-save-Link\n" +
                "            \"Clear\" [#/1/SpreadsheetName1/cell/A1/decimalNumberSymbols/save/] id=decimalNumberSymbols-clear-Link\n" +
                "            \"Undo\" [#/1/SpreadsheetName1/cell/A1/decimalNumberSymbols/save/] id=decimalNumberSymbols-undo-Link\n" +
                "            \"Copy Defaults\" [#/1/SpreadsheetName1/cell/A1/decimalNumberSymbols/save/-,+,0,$,.,e,%22,%22,%E2%88%9E,.,NaN,%25,%E2%80%B0] id=decimalNumberSymbols-copyDefaults-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/cell/A1] id=decimalNumberSymbols-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenWithSpreadsheetMetadataPropertySelectHistoryTokenDecimalNumberSymbols() {
        final AppContext context = this.appContext(
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.DECIMAL_NUMBER_SYMBOLS
            ),
            DECIMAL_NUMBER_SYMBOLS
        );

        this.onHistoryTokenChangeAndCheck2(
            DecimalNumberSymbolsDialogComponent.with(
                DecimalNumberSymbolsDialogComponentContexts.metadata(context)
            ),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            context,
            "DecimalNumberSymbolsDialogComponent\n" +
                "  SpreadsheetDialogComponent\n" +
                "    Spreadsheet: Decimal Number Symbols (decimalNumberSymbols)\n" +
                "    id=decimalNumberSymbols-Dialog includeClose=true\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          CharacterComponent\n" +
                "            ValueSpreadsheetTextBox\n" +
                "              SpreadsheetTextBox\n" +
                "                Negative sign [-] id=decimalNumberSymbolsnegativeSign-TextBox\n" +
                "          CharacterComponent\n" +
                "            ValueSpreadsheetTextBox\n" +
                "              SpreadsheetTextBox\n" +
                "                Positive sign [+] id=decimalNumberSymbolspositiveSign-TextBox\n" +
                "          CharacterComponent\n" +
                "            ValueSpreadsheetTextBox\n" +
                "              SpreadsheetTextBox\n" +
                "                Zero digit [0] id=decimalNumberSymbolszeroDigit-TextBox\n" +
                "          SpreadsheetTextBox\n" +
                "            Currency [$] id=decimalNumberSymbolsCurrencySymbol-TextBox\n" +
                "          CharacterComponent\n" +
                "            ValueSpreadsheetTextBox\n" +
                "              SpreadsheetTextBox\n" +
                "                Decimal separator [.] id=decimalNumberSymbolsdecimalSeparator-TextBox\n" +
                "          SpreadsheetTextBox\n" +
                "            Exponent [e] id=decimalNumberSymbolsExponentSymbol-TextBox\n" +
                "          CharacterComponent\n" +
                "            ValueSpreadsheetTextBox\n" +
                "              SpreadsheetTextBox\n" +
                "                Group separator [,] id=decimalNumberSymbolsgroupSeparator-TextBox\n" +
                "          SpreadsheetTextBox\n" +
                "            Infinity [∞] id=decimalNumberSymbolsInfinitySymbol-TextBox\n" +
                "          CharacterComponent\n" +
                "            ValueSpreadsheetTextBox\n" +
                "              SpreadsheetTextBox\n" +
                "                Monetary decimal separator [.] id=decimalNumberSymbolsmonetaryDecimalSeparator-TextBox\n" +
                "          SpreadsheetTextBox\n" +
                "            Nan [NaN] id=decimalNumberSymbolsNanSymbol-TextBox\n" +
                "          CharacterComponent\n" +
                "            ValueSpreadsheetTextBox\n" +
                "              SpreadsheetTextBox\n" +
                "                Percent symbol [%] id=decimalNumberSymbolspercentSymbol-TextBox\n" +
                "          CharacterComponent\n" +
                "            ValueSpreadsheetTextBox\n" +
                "              SpreadsheetTextBox\n" +
                "                Permill symbol [‰] id=decimalNumberSymbolspermillSymbol-TextBox\n" +
                "          DecimalNumberSymbolsComponent\n" +
                "            ValueSpreadsheetTextBox\n" +
                "              SpreadsheetTextBox\n" +
                "                Date Time Symbols [-,+,0,$,.,e,\",\",∞,.,NaN,%,‰]\n" +
                "      SpreadsheetLocaleComponent\n" +
                "        SpreadsheetSuggestBoxComponent\n" +
                "          Load from Locale []\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/SpreadsheetName1/spreadsheet/decimalNumberSymbols/save/-,+,0,$,.,e,%22,%22,%E2%88%9E,.,NaN,%25,%E2%80%B0] id=decimalNumberSymbols-save-Link\n" +
                "            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/decimalNumberSymbols/save/] id=decimalNumberSymbols-clear-Link\n" +
                "            \"Undo\" [#/1/SpreadsheetName1/spreadsheet/decimalNumberSymbols/save/-,+,0,$,.,e,%22,%22,%E2%88%9E,.,NaN,%25,%E2%80%B0] id=decimalNumberSymbols-undo-Link\n" +
                "            \"Copy Defaults\" DISABLED id=decimalNumberSymbols-copyDefaults-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/spreadsheet] id=decimalNumberSymbols-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenWithSpreadsheetMetadataPropertySelectHistoryTokenMissingDecimalNumberSymbols() {
        final AppContext context = this.appContext(
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.DECIMAL_NUMBER_SYMBOLS
            ),
            null // missing
        );

        this.onHistoryTokenChangeAndCheck2(
            DecimalNumberSymbolsDialogComponent.with(
                DecimalNumberSymbolsDialogComponentContexts.metadata(context)
            ),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            context,
            "DecimalNumberSymbolsDialogComponent\n" +
                "  SpreadsheetDialogComponent\n" +
                "    Spreadsheet: Decimal Number Symbols (decimalNumberSymbols)\n" +
                "    id=decimalNumberSymbols-Dialog includeClose=true\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          CharacterComponent\n" +
                "            ValueSpreadsheetTextBox\n" +
                "              SpreadsheetTextBox\n" +
                "                Negative sign [] id=decimalNumberSymbolsnegativeSign-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Negative sign\"\n" +
                "          CharacterComponent\n" +
                "            ValueSpreadsheetTextBox\n" +
                "              SpreadsheetTextBox\n" +
                "                Positive sign [] id=decimalNumberSymbolspositiveSign-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Positive sign\"\n" +
                "          CharacterComponent\n" +
                "            ValueSpreadsheetTextBox\n" +
                "              SpreadsheetTextBox\n" +
                "                Zero digit [] id=decimalNumberSymbolszeroDigit-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Zero digit\"\n" +
                "          SpreadsheetTextBox\n" +
                "            Currency [] id=decimalNumberSymbolsCurrencySymbol-TextBox\n" +
                "            Errors\n" +
                "              Required\n" +
                "          CharacterComponent\n" +
                "            ValueSpreadsheetTextBox\n" +
                "              SpreadsheetTextBox\n" +
                "                Decimal separator [] id=decimalNumberSymbolsdecimalSeparator-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Decimal separator\"\n" +
                "          SpreadsheetTextBox\n" +
                "            Exponent [] id=decimalNumberSymbolsExponentSymbol-TextBox\n" +
                "            Errors\n" +
                "              Required\n" +
                "          CharacterComponent\n" +
                "            ValueSpreadsheetTextBox\n" +
                "              SpreadsheetTextBox\n" +
                "                Group separator [] id=decimalNumberSymbolsgroupSeparator-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Group separator\"\n" +
                "          SpreadsheetTextBox\n" +
                "            Infinity [] id=decimalNumberSymbolsInfinitySymbol-TextBox\n" +
                "            Errors\n" +
                "              Required\n" +
                "          CharacterComponent\n" +
                "            ValueSpreadsheetTextBox\n" +
                "              SpreadsheetTextBox\n" +
                "                Monetary decimal separator [] id=decimalNumberSymbolsmonetaryDecimalSeparator-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Monetary decimal separator\"\n" +
                "          SpreadsheetTextBox\n" +
                "            Nan [] id=decimalNumberSymbolsNanSymbol-TextBox\n" +
                "            Errors\n" +
                "              Required\n" +
                "          CharacterComponent\n" +
                "            ValueSpreadsheetTextBox\n" +
                "              SpreadsheetTextBox\n" +
                "                Percent symbol [] id=decimalNumberSymbolspercentSymbol-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Percent symbol\"\n" +
                "          CharacterComponent\n" +
                "            ValueSpreadsheetTextBox\n" +
                "              SpreadsheetTextBox\n" +
                "                Permill symbol [] id=decimalNumberSymbolspermillSymbol-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Permill symbol\"\n" +
                "          DecimalNumberSymbolsComponent\n" +
                "            ValueSpreadsheetTextBox\n" +
                "              SpreadsheetTextBox\n" +
                "                Date Time Symbols []\n" +
                "                Errors\n" +
                "                  Expected 12 tokens but got 0\n" +
                "      SpreadsheetLocaleComponent\n" +
                "        SpreadsheetSuggestBoxComponent\n" +
                "          Load from Locale []\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" DISABLED id=decimalNumberSymbols-save-Link\n" +
                "            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/decimalNumberSymbols/save/] id=decimalNumberSymbols-clear-Link\n" +
                "            \"Undo\" [#/1/SpreadsheetName1/spreadsheet/decimalNumberSymbols/save/] id=decimalNumberSymbols-undo-Link\n" +
                "            \"Copy Defaults\" DISABLED id=decimalNumberSymbols-copyDefaults-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/spreadsheet] id=decimalNumberSymbols-close-Link\n"
        );
    }

    @Override
    public DecimalNumberSymbolsDialogComponent createSpreadsheetDialogComponentLifecycle(final HistoryToken historyToken) {
        return DecimalNumberSymbolsDialogComponent.with(
            DecimalNumberSymbolsDialogComponentContexts.metadata(
                this.appContext(
                    historyToken,
                    null
                )
            )
        );
    }

    private AppContext appContext(final HistoryToken historyToken,
                                  final DecimalNumberSymbols decimalNumberSymbols) {
        return new FakeAppContext() {

            @Override
            public Runnable addDecimalNumberSymbolsFetcherWatcher(final DecimalNumberSymbolsFetcherWatcher watcher) {
                return null;
            }

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
                return SpreadsheetViewportCache.empty(this);
            }

            @Override
            public SpreadsheetMetadata spreadsheetMetadata() {
                return SpreadsheetMetadataTesting.METADATA_EN_AU.set(
                    SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
                    SPREADSHEET_ID
                ).setOrRemove(
                    SpreadsheetMetadataPropertyName.DECIMAL_NUMBER_SYMBOLS,
                    decimalNumberSymbols
                );
            }

            @Override
            public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
                return null;
            }
        };
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<DecimalNumberSymbolsDialogComponent> type() {
        return DecimalNumberSymbolsDialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
