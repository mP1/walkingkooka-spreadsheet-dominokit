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

package walkingkooka.spreadsheet.dominokit.value.plugin.currencyexchangerater;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.currency.provider.CurrencyExchangeRaterAliasSet;
import walkingkooka.currency.provider.CurrencyExchangeRaterName;
import walkingkooka.currency.provider.CurrencyExchangeRaterSelector;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryWatcher;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.SpreadsheetError;
import walkingkooka.spreadsheet.value.SpreadsheetErrorKind;

import java.util.Arrays;
import java.util.Optional;

public final class CurrencyExchangeRaterSelectorDialogComponentTest implements DialogComponentLifecycleTesting<CurrencyExchangeRaterSelectorDialogComponent>,
    SpreadsheetMetadataTesting {

    @Test
    public void testSetStringValue() {
        final TestAppContext context = new TestAppContext(
            HistoryToken.cellCurrencyExchangeRaterSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );

        final CurrencyExchangeRaterSelectorDialogComponent dialog = CurrencyExchangeRaterSelectorDialogComponent.with(
            new TestCurrencyExchangeRaterSelectorDialogComponentContext(context) {
                @Override
                public HistoryToken historyToken() {
                    return context.historyToken();
                }

                @Override
                public Optional<CurrencyExchangeRaterSelector> undo() {
                    return Optional.of(
                        CurrencyExchangeRaterSelector.parse("hello-currency-exchange-rater-111")
                    );
                }

                @Override
                public SpreadsheetViewportCache spreadsheetViewportCache() {
                    return context.spreadsheetViewportCache();
                }
            }
        );

        dialog.selector.setStringValue(
            Optional.of("new-currency-exchange-rater-222")
        );

        this.treePrintAndCheck(
            dialog,
            "CurrencyExchangeRaterSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    id=CurrencyExchangeRaterSelector-Dialog includeClose=true CLOSED\n" +
                "      CurrencyExchangeRaterNameAnchorListComponent\n" +
                "        PluginNameAnchorListComponent\n" +
                "          AnchorListComponent\n" +
                "            FlexLayoutComponent\n" +
                "              ROW\n" +
                "                id=CurrencyExchangeRaterSelector-links\n" +
                "      CurrencyExchangeRaterSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [new-currency-exchange-rater-222] icons=mdi-close-circle id=CurrencyExchangeRaterSelector-selector-TextBox REQUIRED\n" +
                "      DialogAnchorListComponent\n" +
                "        AnchorListComponent\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              \"Save\" [#/123/SpreadsheetName456/cell/A1/currencyExchangeRater/save/new-currency-exchange-rater-222] id=CurrencyExchangeRaterSelector-save-Link\n" +
                "              \"Clear\" [#/123/SpreadsheetName456/cell/A1/currencyExchangeRater/save/] id=CurrencyExchangeRaterSelector-clear-Link\n" +
                "              \"Undo\" [#/123/SpreadsheetName456/cell/A1/currencyExchangeRater/save/hello-currency-exchange-rater-111] id=CurrencyExchangeRaterSelector-undo-Link\n" +
                "              \"Close\" [#/123/SpreadsheetName456/cell/A1] id=CurrencyExchangeRaterSelector-close-Link\n"
        );
    }

    @Test
    public void testSetStringValueWithSelectorSyntaxError() {
        final TestAppContext context = new TestAppContext(
            HistoryToken.cellCurrencyExchangeRaterSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );

        final CurrencyExchangeRaterSelectorDialogComponent dialog = CurrencyExchangeRaterSelectorDialogComponent.with(
            new TestCurrencyExchangeRaterSelectorDialogComponentContext(context) {
                @Override
                public HistoryToken historyToken() {
                    return context.historyToken();
                }

                @Override
                public Optional<CurrencyExchangeRaterSelector> undo() {
                    return Optional.of(
                        CurrencyExchangeRaterSelector.parse("hello-currency-exchange-rater-111")
                    );
                }

                @Override
                public SpreadsheetViewportCache spreadsheetViewportCache() {
                    return context.spreadsheetViewportCache();
                }
            }
        );

        dialog.selector.setStringValue(
            Optional.of("new-currency-exchange-rater-222!")
        );

        this.treePrintAndCheck(
            dialog,
            "CurrencyExchangeRaterSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    id=CurrencyExchangeRaterSelector-Dialog includeClose=true CLOSED\n" +
                "      CurrencyExchangeRaterNameAnchorListComponent\n" +
                "        PluginNameAnchorListComponent\n" +
                "          AnchorListComponent\n" +
                "            FlexLayoutComponent\n" +
                "              ROW\n" +
                "                id=CurrencyExchangeRaterSelector-links\n" +
                "      CurrencyExchangeRaterSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [new-currency-exchange-rater-222!] icons=mdi-close-circle id=CurrencyExchangeRaterSelector-selector-TextBox REQUIRED\n" +
                "            Errors\n" +
                "              Invalid character '!' at 31\n" +
                "      DialogAnchorListComponent\n" +
                "        AnchorListComponent\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              \"Save\" DISABLED id=CurrencyExchangeRaterSelector-save-Link\n" +
                "              \"Clear\" [#/123/SpreadsheetName456/cell/A1/currencyExchangeRater/save/] id=CurrencyExchangeRaterSelector-clear-Link\n" +
                "              \"Undo\" [#/123/SpreadsheetName456/cell/A1/currencyExchangeRater/save/hello-currency-exchange-rater-111] id=CurrencyExchangeRaterSelector-undo-Link\n" +
                "              \"Close\" [#/123/SpreadsheetName456/cell/A1] id=CurrencyExchangeRaterSelector-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenChange() {
        final TestAppContext context = new TestAppContext(
            HistoryToken.cellCurrencyExchangeRaterSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );

        final CurrencyExchangeRaterSelectorDialogComponent dialog = CurrencyExchangeRaterSelectorDialogComponent.with(
            new TestCurrencyExchangeRaterSelectorDialogComponentContext(context) {
                @Override
                public HistoryToken historyToken() {
                    return context.historyToken();
                }

                @Override
                public Optional<CurrencyExchangeRaterSelector> undo() {
                    return Optional.of(
                        CurrencyExchangeRaterSelector.parse("hello-currency-exchange-rater")
                    );
                }

                @Override
                public SpreadsheetViewportCache spreadsheetViewportCache() {
                    return context.spreadsheetViewportCache();
                }
            }
        );

        dialog.onHistoryTokenChange(
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            ),
            context
        );
        dialog.refresh(context);

        this.treePrintAndCheck(
            dialog,
            "CurrencyExchangeRaterSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    CurrencyExchangeRater Title123\n" +
                "    id=CurrencyExchangeRaterSelector-Dialog includeClose=true\n" +
                "      CurrencyExchangeRaterNameAnchorListComponent\n" +
                "        PluginNameAnchorListComponent\n" +
                "          AnchorListComponent\n" +
                "            FlexLayoutComponent\n" +
                "              ROW\n" +
                "                id=CurrencyExchangeRaterSelector-links\n" +
                "                  \"Currency Exchange Rater 1\" [#/123/SpreadsheetName456/cell/A1/currencyExchangeRater/save/currency-exchange-rater-1] id=CurrencyExchangeRaterSelector-currency-exchange-rater-1-Link\n" +
                "                  \"Currency Exchange Rater 2\" [#/123/SpreadsheetName456/cell/A1/currencyExchangeRater/save/currency-exchange-rater-2] id=CurrencyExchangeRaterSelector-currency-exchange-rater-2-Link\n" +
                "                  \"Currency Exchange Rater 3\" [#/123/SpreadsheetName456/cell/A1/currencyExchangeRater/save/currency-exchange-rater-3] id=CurrencyExchangeRaterSelector-currency-exchange-rater-3-Link\n" +
                "      CurrencyExchangeRaterSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [hello-currency-exchange-rater] icons=mdi-close-circle id=CurrencyExchangeRaterSelector-selector-TextBox REQUIRED\n" +
                "      DialogAnchorListComponent\n" +
                "        AnchorListComponent\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              \"Save\" [#/123/SpreadsheetName456/cell/A1/currencyExchangeRater/save/hello-currency-exchange-rater] id=CurrencyExchangeRaterSelector-save-Link\n" +
                "              \"Clear\" [#/123/SpreadsheetName456/cell/A1/currencyExchangeRater/save/] id=CurrencyExchangeRaterSelector-clear-Link\n" +
                "              \"Undo\" [#/123/SpreadsheetName456/cell/A1/currencyExchangeRater/save/hello-currency-exchange-rater] id=CurrencyExchangeRaterSelector-undo-Link\n" +
                "              \"Close\" [#/123/SpreadsheetName456/cell/A1] id=CurrencyExchangeRaterSelector-close-Link\n"
        );
    }

    @Test
    public void testOnSpreadsheetDeltaWhenCellWithCurrencyExchangeError() {
        final TestAppContext context = new TestAppContext(
            HistoryToken.cellCurrencyExchangeRaterSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );

        context.metadataWatchers.onSpreadsheetMetadata(
            context.spreadsheetMetadata()
        );

        final CurrencyExchangeRaterSelectorDialogComponent dialog = CurrencyExchangeRaterSelectorDialogComponent.with(
            new TestCurrencyExchangeRaterSelectorDialogComponentContext(context) {
                @Override
                public HistoryToken historyToken() {
                    return context.historyToken();
                }

                @Override
                public Optional<CurrencyExchangeRaterSelector> undo() {
                    return Optional.of(
                        CurrencyExchangeRaterSelector.parse("hello-currency-exchange-rater")
                    );
                }

                @Override
                public SpreadsheetViewportCache spreadsheetViewportCache() {
                    return context.spreadsheetViewportCache();
                }
            }
        );

        dialog.onHistoryTokenChange(
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            ),
            context
        );
        dialog.refresh(context);

        context.deltaWatchers.onSpreadsheetDelta(
            HttpMethod.GET,
            Url.parseRelative("/api/spreadsheet/123/cell/A1"),
            SpreadsheetDelta.EMPTY.setCells(
                Sets.of(
                    SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY.setError(
                            Optional.of(
                                SpreadsheetError.currencyExchangeRaterNotFound(
                                    CurrencyExchangeRaterName.with("hello-currency-exchange-rater")
                                )
                            )
                        )
                    )
                )
            )
        );

        this.treePrintAndCheck(
            dialog,
            "CurrencyExchangeRaterSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    CurrencyExchangeRater Title123\n" +
                "    id=CurrencyExchangeRaterSelector-Dialog includeClose=true\n" +
                "      CurrencyExchangeRaterNameAnchorListComponent\n" +
                "        PluginNameAnchorListComponent\n" +
                "          AnchorListComponent\n" +
                "            FlexLayoutComponent\n" +
                "              ROW\n" +
                "                id=CurrencyExchangeRaterSelector-links\n" +
                "                  \"Currency Exchange Rater 1\" [#/123/SpreadsheetName456/cell/A1/currencyExchangeRater/save/currency-exchange-rater-1] id=CurrencyExchangeRaterSelector-currency-exchange-rater-1-Link\n" +
                "                  \"Currency Exchange Rater 2\" [#/123/SpreadsheetName456/cell/A1/currencyExchangeRater/save/currency-exchange-rater-2] id=CurrencyExchangeRaterSelector-currency-exchange-rater-2-Link\n" +
                "                  \"Currency Exchange Rater 3\" [#/123/SpreadsheetName456/cell/A1/currencyExchangeRater/save/currency-exchange-rater-3] id=CurrencyExchangeRaterSelector-currency-exchange-rater-3-Link\n" +
                "      CurrencyExchangeRaterSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [hello-currency-exchange-rater] icons=mdi-close-circle id=CurrencyExchangeRaterSelector-selector-TextBox REQUIRED\n" +
                "      DialogAnchorListComponent\n" +
                "        AnchorListComponent\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              \"Save\" [#/123/SpreadsheetName456/cell/A1/currencyExchangeRater/save/hello-currency-exchange-rater] id=CurrencyExchangeRaterSelector-save-Link\n" +
                "              \"Clear\" [#/123/SpreadsheetName456/cell/A1/currencyExchangeRater/save/] id=CurrencyExchangeRaterSelector-clear-Link\n" +
                "              \"Undo\" [#/123/SpreadsheetName456/cell/A1/currencyExchangeRater/save/hello-currency-exchange-rater] id=CurrencyExchangeRaterSelector-undo-Link\n" +
                "              \"Close\" [#/123/SpreadsheetName456/cell/A1] id=CurrencyExchangeRaterSelector-close-Link\n"
        );
    }

    @Test
    public void testOnSpreadsheetDeltaWhenCellWithCurrencyExchangeRaterSelectorError() {
        final TestAppContext context = new TestAppContext(
            HistoryToken.cellCurrencyExchangeRaterSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );

        context.metadataWatchers.onSpreadsheetMetadata(
            context.spreadsheetMetadata()
        );

        final CurrencyExchangeRaterSelectorDialogComponent dialog = CurrencyExchangeRaterSelectorDialogComponent.with(
            new TestCurrencyExchangeRaterSelectorDialogComponentContext(context) {
                @Override
                public HistoryToken historyToken() {
                    return context.historyToken();
                }

                @Override
                public Optional<CurrencyExchangeRaterSelector> undo() {
                    return Optional.of(
                        CurrencyExchangeRaterSelector.parse("hello-currency-exchange-rater")
                    );
                }

                @Override
                public SpreadsheetViewportCache spreadsheetViewportCache() {
                    return context.spreadsheetViewportCache();
                }
            }
        );

        dialog.onHistoryTokenChange(
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            ),
            context
        );
        dialog.refresh(context);

        context.deltaWatchers.onSpreadsheetDelta(
            HttpMethod.GET,
            Url.parseRelative("/api/spreadsheet/123/cell/A1"),
            SpreadsheetDelta.EMPTY.setCells(
                Sets.of(
                    SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY.setError(
                            Optional.of(
                                SpreadsheetErrorKind.CURRENCY_EXCHANGE.setMessage("Bad CurrencyExchangeRater")
                            )
                        )
                    )
                )
            )
        );

        this.treePrintAndCheck(
            dialog,
            "CurrencyExchangeRaterSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    CurrencyExchangeRater Title123\n" +
                "    id=CurrencyExchangeRaterSelector-Dialog includeClose=true\n" +
                "      CurrencyExchangeRaterNameAnchorListComponent\n" +
                "        PluginNameAnchorListComponent\n" +
                "          AnchorListComponent\n" +
                "            FlexLayoutComponent\n" +
                "              ROW\n" +
                "                id=CurrencyExchangeRaterSelector-links\n" +
                "                  \"Currency Exchange Rater 1\" [#/123/SpreadsheetName456/cell/A1/currencyExchangeRater/save/currency-exchange-rater-1] id=CurrencyExchangeRaterSelector-currency-exchange-rater-1-Link\n" +
                "                  \"Currency Exchange Rater 2\" [#/123/SpreadsheetName456/cell/A1/currencyExchangeRater/save/currency-exchange-rater-2] id=CurrencyExchangeRaterSelector-currency-exchange-rater-2-Link\n" +
                "                  \"Currency Exchange Rater 3\" [#/123/SpreadsheetName456/cell/A1/currencyExchangeRater/save/currency-exchange-rater-3] id=CurrencyExchangeRaterSelector-currency-exchange-rater-3-Link\n" +
                "      CurrencyExchangeRaterSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [hello-currency-exchange-rater] icons=mdi-close-circle id=CurrencyExchangeRaterSelector-selector-TextBox REQUIRED\n" +
                "            Errors\n" +
                "              Bad CurrencyExchangeRater\n" +
                "      DialogAnchorListComponent\n" +
                "        AnchorListComponent\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              \"Save\" DISABLED id=CurrencyExchangeRaterSelector-save-Link\n" +
                "              \"Clear\" [#/123/SpreadsheetName456/cell/A1/currencyExchangeRater/save/] id=CurrencyExchangeRaterSelector-clear-Link\n" +
                "              \"Undo\" [#/123/SpreadsheetName456/cell/A1/currencyExchangeRater/save/hello-currency-exchange-rater] id=CurrencyExchangeRaterSelector-undo-Link\n" +
                "              \"Close\" [#/123/SpreadsheetName456/cell/A1] id=CurrencyExchangeRaterSelector-close-Link\n"
        );
    }

    private static class TestCurrencyExchangeRaterSelectorDialogComponentContext extends FakeCurrencyExchangeRaterSelectorDialogComponentContext {

        TestCurrencyExchangeRaterSelectorDialogComponentContext(final AppContext context) {
            this.context = context;
        }

        @Override
        public Runnable addHistoryWatcher(final HistoryWatcher watcher) {
            return () -> {
            };
        }

        @Override
        public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
            return this.context.addSpreadsheetDeltaFetcherWatcher(watcher);
        }

        @Override
        public SpreadsheetMetadata spreadsheetMetadata() {
            return this.context.spreadsheetMetadata();
        }

        private final AppContext context;

        @Override
        public String dialogTitle() {
            return "CurrencyExchangeRater Title123";
        }
    }

    final static class TestAppContext extends FakeAppContext {

        TestAppContext(final HistoryToken historyToken) {
            this.historyToken = historyToken;
        }

        @Override
        public HistoryToken historyToken() {
            return this.historyToken;
        }

        private final HistoryToken historyToken;

        @Override
        public Runnable addHistoryWatcher(final HistoryWatcher watcher) {
            return null;
        }

        @Override
        public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
            return this.deltaWatchers.addSpreadsheetDeltaFetcherWatcher(watcher);
        }

        final SpreadsheetDeltaFetcherWatchers deltaWatchers = SpreadsheetDeltaFetcherWatchers.empty();

        @Override
        public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
            return this.metadataWatchers.addSpreadsheetMetadataFetcherWatcher(watcher);
        }

        final SpreadsheetMetadataFetcherWatchers metadataWatchers = SpreadsheetMetadataFetcherWatchers.empty();

        @Override
        public SpreadsheetMetadata spreadsheetMetadata() {
            return METADATA_EN_AU.set(
                SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
                CurrencyExchangeRaterSelectorDialogComponentTest.SPREADSHEET_ID
            ).set(
                SpreadsheetMetadataPropertyName.CURRENCY_EXCHANGE_RATERS,
                CurrencyExchangeRaterAliasSet.parse("currency-exchange-rater-1, currency-exchange-rater-2, currency-exchange-rater-3")
            );
        }

        @Override
        public void giveFocus(final Runnable focus) {
            // NOP
        }

        @Override
        public SpreadsheetViewportCache spreadsheetViewportCache() {
            return this.cache;
        }

        private final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty(this);

        @Override
        public void debug(final Object... values) {
            System.out.println("DEBUG: " + Arrays.toString(values));
        }

        @Override
        public void error(final Object... values) {
            System.out.println("ERROR: " + Arrays.toString(values));
        }
    }

    @Override
    public CurrencyExchangeRaterSelectorDialogComponent createSpreadsheetDialogComponentLifecycle(final HistoryToken historyToken) {
        return CurrencyExchangeRaterSelectorDialogComponent.with(
            CurrencyExchangeRaterSelectorDialogComponentContexts.appContext(
                new TestAppContext(historyToken)
            )
        );
    }

    // class............................................................................................................

    @Override
    public Class<CurrencyExchangeRaterSelectorDialogComponent> type() {
        return CurrencyExchangeRaterSelectorDialogComponent.class;
    }
}
