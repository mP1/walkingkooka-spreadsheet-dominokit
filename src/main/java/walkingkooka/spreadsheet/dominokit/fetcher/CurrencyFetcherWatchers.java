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

package walkingkooka.spreadsheet.dominokit.fetcher;

import walkingkooka.spreadsheet.server.currency.CurrencyCode;
import walkingkooka.spreadsheet.server.currency.CurrencyHateosResource;
import walkingkooka.spreadsheet.server.currency.CurrencyHateosResourceSet;

public final class CurrencyFetcherWatchers extends FetcherWatchers<CurrencyFetcherWatcher>
    implements CurrencyFetcherWatcher {

    public static CurrencyFetcherWatchers empty() {
        return new CurrencyFetcherWatchers();
    }

    private CurrencyFetcherWatchers() {
        super();
    }

    @Override
    public void onCurrencyHateosResource(final CurrencyCode id,
                                         final CurrencyHateosResource currency) {
        this.fire(
            CurrencyFetcherWatchersCurrencyHateosResourceEvent.with(
                id,
                currency
            )
        );
    }

    @Override
    public void onCurrencyHateosResourceSet(final CurrencyHateosResourceSet currencies) {
        this.fire(
            CurrencyFetcherWatchersCurrencyHateosResourceSetEvent.with(currencies)
        );
    }
}
