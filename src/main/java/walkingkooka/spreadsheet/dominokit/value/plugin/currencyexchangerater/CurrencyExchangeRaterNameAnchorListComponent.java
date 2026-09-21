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

import elemental2.dom.HTMLDivElement;
import walkingkooka.currency.provider.CurrencyExchangeRaterAlias;
import walkingkooka.currency.provider.CurrencyExchangeRaterAliasSet;
import walkingkooka.currency.provider.CurrencyExchangeRaterInfo;
import walkingkooka.currency.provider.CurrencyExchangeRaterInfoSet;
import walkingkooka.currency.provider.CurrencyExchangeRaterName;
import walkingkooka.currency.provider.CurrencyExchangeRaterSelector;
import walkingkooka.spreadsheet.dominokit.value.ValueComponent;
import walkingkooka.spreadsheet.dominokit.value.plugin.PluginNameAnchorListComponent;
import walkingkooka.spreadsheet.dominokit.value.plugin.PluginNameAnchorListComponentContexts;
import walkingkooka.spreadsheet.dominokit.value.plugin.PluginNameAnchorListComponentDelegator;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.text.CharSequences;

import java.util.Objects;

/**
 * Holds a list of anchors for each given {@link CurrencyExchangeRaterName}.
 */
public final class CurrencyExchangeRaterNameAnchorListComponent implements ValueComponent<HTMLDivElement, CurrencyExchangeRaterName, CurrencyExchangeRaterNameAnchorListComponent>,
    PluginNameAnchorListComponentDelegator<CurrencyExchangeRaterNameAnchorListComponent, CurrencyExchangeRaterName, CurrencyExchangeRaterInfo, CurrencyExchangeRaterInfoSet, CurrencyExchangeRaterSelector, CurrencyExchangeRaterAlias, CurrencyExchangeRaterAliasSet> {

    public static CurrencyExchangeRaterNameAnchorListComponent with(final String idPrefix,
                                                                    final CurrencyExchangeRaterNameAnchorListComponentContext context) {
        return new CurrencyExchangeRaterNameAnchorListComponent(
            CharSequences.failIfNullOrEmpty(idPrefix, "idPrefix"),
            Objects.requireNonNull(context, "context")
        );
    }

    private CurrencyExchangeRaterNameAnchorListComponent(final String idPrefix,
                                                         final CurrencyExchangeRaterNameAnchorListComponentContext context) {
        super();

        this.valueComponent = PluginNameAnchorListComponent.with(
            idPrefix,
            PluginNameAnchorListComponentContexts.basic(
                SpreadsheetMetadataPropertyName.CURRENCY_EXCHANGE_RATERS,
                context, // HasSpreadsheetMetadata
                context // HistoryContext
            )
        );
    }

    // PluginNameAnchorListComponentDelegator...........................................................................

    @Override
    public PluginNameAnchorListComponent<CurrencyExchangeRaterName, CurrencyExchangeRaterInfo, CurrencyExchangeRaterInfoSet, CurrencyExchangeRaterSelector, CurrencyExchangeRaterAlias, CurrencyExchangeRaterAliasSet> valueComponent() {
        return this.valueComponent;
    }

    private final PluginNameAnchorListComponent<CurrencyExchangeRaterName, CurrencyExchangeRaterInfo, CurrencyExchangeRaterInfoSet, CurrencyExchangeRaterSelector, CurrencyExchangeRaterAlias, CurrencyExchangeRaterAliasSet> valueComponent;
}
