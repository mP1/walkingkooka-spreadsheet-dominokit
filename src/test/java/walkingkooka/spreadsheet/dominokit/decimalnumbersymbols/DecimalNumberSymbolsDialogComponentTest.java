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
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycleTesting;
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

public final class DecimalNumberSymbolsDialogComponentTest implements DialogComponentLifecycleTesting<DecimalNumberSymbolsDialogComponent>,
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
                "  DialogComponent\n" +
                "    A1: Decimal Number Symbols\n" +
                "    id=DecimalNumberSymbols-Dialog includeClose=true\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          CharacterComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Negative sign [] id=DecimalNumberSymbols-negativeSign-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Negative sign\"\n" +
                "          CharacterComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Positive sign [] id=DecimalNumberSymbols-positiveSign-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Positive sign\"\n" +
                "          CharacterComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Zero digit [] id=DecimalNumberSymbols-zeroDigit-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Zero digit\"\n" +
                "          TextBoxComponent\n" +
                "            Currency [] id=DecimalNumberSymbols-CurrencySymbol-TextBox\n" +
                "            Errors\n" +
                "              Required\n" +
                "          CharacterComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Decimal separator [] id=DecimalNumberSymbols-decimalSeparator-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Decimal separator\"\n" +
                "          TextBoxComponent\n" +
                "            Exponent [] id=DecimalNumberSymbols-ExponentSymbol-TextBox\n" +
                "            Errors\n" +
                "              Required\n" +
                "          CharacterComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Group separator [] id=DecimalNumberSymbols-groupSeparator-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Group separator\"\n" +
                "          TextBoxComponent\n" +
                "            Infinity [] id=DecimalNumberSymbols-InfinitySymbol-TextBox\n" +
                "            Errors\n" +
                "              Required\n" +
                "          CharacterComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Monetary decimal separator [] id=DecimalNumberSymbols-monetaryDecimalSeparator-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Monetary decimal separator\"\n" +
                "          TextBoxComponent\n" +
                "            Nan [] id=DecimalNumberSymbols-NanSymbol-TextBox\n" +
                "            Errors\n" +
                "              Required\n" +
                "          CharacterComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Percent symbol [] id=DecimalNumberSymbols-percentSymbol-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Percent symbol\"\n" +
                "          CharacterComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Permill symbol [] id=DecimalNumberSymbols-permillSymbol-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Permill symbol\"\n" +
                "          DecimalNumberSymbolsComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Date Time Symbols []\n" +
                "                Errors\n" +
                "                  Expected 12 tokens but got 0\n" +
                "      LocaleComponent\n" +
                "        SuggestBoxComponent\n" +
                "          Load from Locale []\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" DISABLED id=DecimalNumberSymbols-save-Link\n" +
                "            \"Clear\" [#/1/SpreadsheetName1/cell/A1/decimalNumberSymbols/save/] id=DecimalNumberSymbols-clear-Link\n" +
                "            \"Undo\" [#/1/SpreadsheetName1/cell/A1/decimalNumberSymbols/save/] id=DecimalNumberSymbols-undo-Link\n" +
                "            \"Copy Defaults\" [#/1/SpreadsheetName1/cell/A1/decimalNumberSymbols/save/-,+,0,$,.,e,%22,%22,%E2%88%9E,.,NaN,%25,%E2%80%B0] id=DecimalNumberSymbols-copyDefaults-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/cell/A1] id=DecimalNumberSymbols-close-Link\n"
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
                "  DialogComponent\n" +
                "    Spreadsheet: Decimal Number Symbols (decimalNumberSymbols)\n" +
                "    id=DecimalNumberSymbols-Dialog includeClose=true\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          CharacterComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Negative sign [-] id=DecimalNumberSymbols-negativeSign-TextBox\n" +
                "          CharacterComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Positive sign [+] id=DecimalNumberSymbols-positiveSign-TextBox\n" +
                "          CharacterComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Zero digit [0] id=DecimalNumberSymbols-zeroDigit-TextBox\n" +
                "          TextBoxComponent\n" +
                "            Currency [$] id=DecimalNumberSymbols-CurrencySymbol-TextBox\n" +
                "          CharacterComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Decimal separator [.] id=DecimalNumberSymbols-decimalSeparator-TextBox\n" +
                "          TextBoxComponent\n" +
                "            Exponent [e] id=DecimalNumberSymbols-ExponentSymbol-TextBox\n" +
                "          CharacterComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Group separator [,] id=DecimalNumberSymbols-groupSeparator-TextBox\n" +
                "          TextBoxComponent\n" +
                "            Infinity [∞] id=DecimalNumberSymbols-InfinitySymbol-TextBox\n" +
                "          CharacterComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Monetary decimal separator [.] id=DecimalNumberSymbols-monetaryDecimalSeparator-TextBox\n" +
                "          TextBoxComponent\n" +
                "            Nan [NaN] id=DecimalNumberSymbols-NanSymbol-TextBox\n" +
                "          CharacterComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Percent symbol [%] id=DecimalNumberSymbols-percentSymbol-TextBox\n" +
                "          CharacterComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Permill symbol [‰] id=DecimalNumberSymbols-permillSymbol-TextBox\n" +
                "          DecimalNumberSymbolsComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Date Time Symbols [-,+,0,$,.,e,\",\",∞,.,NaN,%,‰]\n" +
                "      LocaleComponent\n" +
                "        SuggestBoxComponent\n" +
                "          Load from Locale []\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/SpreadsheetName1/spreadsheet/decimalNumberSymbols/save/-,+,0,$,.,e,%22,%22,%E2%88%9E,.,NaN,%25,%E2%80%B0] id=DecimalNumberSymbols-save-Link\n" +
                "            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/decimalNumberSymbols/save/] id=DecimalNumberSymbols-clear-Link\n" +
                "            \"Undo\" [#/1/SpreadsheetName1/spreadsheet/decimalNumberSymbols/save/-,+,0,$,.,e,%22,%22,%E2%88%9E,.,NaN,%25,%E2%80%B0] id=DecimalNumberSymbols-undo-Link\n" +
                "            \"Copy Defaults\" DISABLED id=DecimalNumberSymbols-copyDefaults-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/spreadsheet] id=DecimalNumberSymbols-close-Link\n"
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
                "  DialogComponent\n" +
                "    Spreadsheet: Decimal Number Symbols (decimalNumberSymbols)\n" +
                "    id=DecimalNumberSymbols-Dialog includeClose=true\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          CharacterComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Negative sign [] id=DecimalNumberSymbols-negativeSign-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Negative sign\"\n" +
                "          CharacterComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Positive sign [] id=DecimalNumberSymbols-positiveSign-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Positive sign\"\n" +
                "          CharacterComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Zero digit [] id=DecimalNumberSymbols-zeroDigit-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Zero digit\"\n" +
                "          TextBoxComponent\n" +
                "            Currency [] id=DecimalNumberSymbols-CurrencySymbol-TextBox\n" +
                "            Errors\n" +
                "              Required\n" +
                "          CharacterComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Decimal separator [] id=DecimalNumberSymbols-decimalSeparator-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Decimal separator\"\n" +
                "          TextBoxComponent\n" +
                "            Exponent [] id=DecimalNumberSymbols-ExponentSymbol-TextBox\n" +
                "            Errors\n" +
                "              Required\n" +
                "          CharacterComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Group separator [] id=DecimalNumberSymbols-groupSeparator-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Group separator\"\n" +
                "          TextBoxComponent\n" +
                "            Infinity [] id=DecimalNumberSymbols-InfinitySymbol-TextBox\n" +
                "            Errors\n" +
                "              Required\n" +
                "          CharacterComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Monetary decimal separator [] id=DecimalNumberSymbols-monetaryDecimalSeparator-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Monetary decimal separator\"\n" +
                "          TextBoxComponent\n" +
                "            Nan [] id=DecimalNumberSymbols-NanSymbol-TextBox\n" +
                "            Errors\n" +
                "              Required\n" +
                "          CharacterComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Percent symbol [] id=DecimalNumberSymbols-percentSymbol-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Percent symbol\"\n" +
                "          CharacterComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Permill symbol [] id=DecimalNumberSymbols-permillSymbol-TextBox\n" +
                "                Errors\n" +
                "                  Empty \"Permill symbol\"\n" +
                "          DecimalNumberSymbolsComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Date Time Symbols []\n" +
                "                Errors\n" +
                "                  Expected 12 tokens but got 0\n" +
                "      LocaleComponent\n" +
                "        SuggestBoxComponent\n" +
                "          Load from Locale []\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" DISABLED id=DecimalNumberSymbols-save-Link\n" +
                "            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/decimalNumberSymbols/save/] id=DecimalNumberSymbols-clear-Link\n" +
                "            \"Undo\" [#/1/SpreadsheetName1/spreadsheet/decimalNumberSymbols/save/] id=DecimalNumberSymbols-undo-Link\n" +
                "            \"Copy Defaults\" DISABLED id=DecimalNumberSymbols-copyDefaults-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/spreadsheet] id=DecimalNumberSymbols-close-Link\n"
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
                    DecimalNumberSymbolsDialogComponentTest.SPREADSHEET_ID
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
