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

package walkingkooka.spreadsheet.dominokit.currency;

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

import java.util.Arrays;
import java.util.Currency;
import java.util.Optional;
import java.util.Set;

public final class CurrencyDialogComponentTest implements DialogComponentLifecycleTesting<CurrencyDialogComponent>,
    HistoryTokenTesting,
    SpreadsheetMetadataTesting {

    private final static SpreadsheetMetadata METADATA = METADATA_EN_AU.set(
        SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
        SPREADSHEET_ID
    );

    private final static Currency AUD = Currency.getInstance("AUD");
    private final static Currency NZD = Currency.getInstance("NZD");
    private final static Currency EURO = Currency.getInstance("EUR");

    // onHistoryToken...................................................................................................

    @Test
    public void testOnHistoryTokenWithSpreadsheetCellCurrencySelectHistoryTokenMissingCurrency() {
        final SpreadsheetCellReference cell = SpreadsheetSelection.A1;

        final AppContext context = this.appContext(
            HistoryToken.cellCurrencySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                cell.setDefaultAnchor()
            ),
            Optional.empty() // no currency
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
                        cell.setFormula(SpreadsheetFormula.EMPTY) // no currency
                    )
                )
            );

        this.onHistoryTokenChangeAndCheck2(
            CurrencyDialogComponent.with(
                CurrencyDialogComponentContexts.appContextCellCurrency(context)
            ),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            context,
            "CurrencyDialogComponent\n" +
                "  DialogComponent\n" +
                "    A1: Currency\n" +
                "    id=Currency-Dialog includeClose=true\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          CurrencyComponent\n" +
                "            SuggestBoxComponent\n" +
                "              []\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/SpreadsheetName1/cell/A1/currency/save/] id=Currency-save-Link\n" +
                "            \"Clear\" [#/1/SpreadsheetName1/cell/A1/currency/save/] id=Currency-clear-Link\n" +
                "            \"Undo\" [#/1/SpreadsheetName1/cell/A1/currency/save/] id=Currency-undo-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/cell/A1] id=Currency-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenWithSpreadsheetCellCurrencySelectHistoryTokenWithCurrency() {
        final SpreadsheetCellReference cell = SpreadsheetSelection.A1;

        final AppContext context = this.appContext(
            HistoryToken.cellCurrencySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                cell.setDefaultAnchor()
            ),
            Optional.of(CURRENCY)
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
                        cell.setFormula(SpreadsheetFormula.EMPTY)
                            .setCurrency(
                                Optional.of(CURRENCY)
                            )
                    )
                )
            );

        this.onHistoryTokenChangeAndCheck2(
            CurrencyDialogComponent.with(
                CurrencyDialogComponentContexts.appContextCellCurrency(context)
            ),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                cell.setDefaultAnchor()
            ),
            context,
            "CurrencyDialogComponent\n" +
                "  DialogComponent\n" +
                "    A1: Currency\n" +
                "    id=Currency-Dialog includeClose=true\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          CurrencyComponent\n" +
                "            SuggestBoxComponent\n" +
                "              [Australian Dollar]\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/SpreadsheetName1/cell/A1/currency/save/AUD] id=Currency-save-Link\n" +
                "            \"Clear\" [#/1/SpreadsheetName1/cell/A1/currency/save/] id=Currency-clear-Link\n" +
                "            \"Undo\" [#/1/SpreadsheetName1/cell/A1/currency/save/AUD] id=Currency-undo-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/cell/A1] id=Currency-close-Link\n"
        );
    }

    @Override
    public CurrencyDialogComponent createSpreadsheetDialogComponentLifecycle(final HistoryToken historyToken) {
        return CurrencyDialogComponent.with(
            CurrencyDialogComponentContexts.appContextMetadataCurrency(
                this.appContext(
                    historyToken,
                    null
                )
            )
        );
    }

    private AppContext appContext(final HistoryToken historyToken,
                                  final Optional<Currency> currency) {
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
                    SpreadsheetMetadataPropertyName.CURRENCY,
                    currency.orElse(EURO)
                );
            }

            @Override
            public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
                return null;
            }

            @Override
            public Set<Currency> availableCurrencies() {
                return Sets.of(
                    AUD,
                    NZD,
                    EURO
                );
            }

            @Override
            public Optional<String> currencyText(final Currency currency) {
                if (currency.equals(AUD)) {
                    return Optional.of("Australian Dollar");
                }
                if (currency.equals(NZD)) {
                    return Optional.of("New Zealand Dollar");
                }
                if (currency.equals(EURO)) {
                    return Optional.of("Euro");
                }
                return Optional.empty();
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
    public Class<CurrencyDialogComponent> type() {
        return CurrencyDialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
