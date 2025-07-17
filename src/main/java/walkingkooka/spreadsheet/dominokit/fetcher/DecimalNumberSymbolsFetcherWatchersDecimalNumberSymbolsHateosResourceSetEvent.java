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

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.server.decimalnumbersymbols.DecimalNumberSymbolsHateosResourceSet;

/**
 * The event payload used by {@link DecimalNumberSymbolsFetcherWatchers}.
 */
final class DecimalNumberSymbolsFetcherWatchersDecimalNumberSymbolsHateosResourceSetEvent extends FetcherWatchersEvent<DecimalNumberSymbolsFetcherWatcher> {

    static DecimalNumberSymbolsFetcherWatchersDecimalNumberSymbolsHateosResourceSetEvent with(final String localeStartsWith,
                                                                                              final DecimalNumberSymbolsHateosResourceSet symbols,
                                                                                              final AppContext context) {
        return new DecimalNumberSymbolsFetcherWatchersDecimalNumberSymbolsHateosResourceSetEvent(
            localeStartsWith,
            symbols,
            context
        );
    }

    private DecimalNumberSymbolsFetcherWatchersDecimalNumberSymbolsHateosResourceSetEvent(final String localeStartsWith,
                                                                                          final DecimalNumberSymbolsHateosResourceSet symbols,
                                                                                          final AppContext context) {
        super(context);
        this.localeStartsWith = localeStartsWith;
        this.symbols = symbols;
    }

    @Override
    void fire(final DecimalNumberSymbolsFetcherWatcher watcher) {
        watcher.onDecimalNumberSymbolsHateosResourceSet(
            this.localeStartsWith,
            this.symbols,
            this.context
        );
    }

    private final String localeStartsWith;

    private final DecimalNumberSymbolsHateosResourceSet symbols;

    @Override
    public String toString() {
        return this.localeStartsWith + " " + this.symbols;
    }
}
