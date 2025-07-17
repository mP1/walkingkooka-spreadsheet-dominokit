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
import walkingkooka.spreadsheet.server.decimalnumbersymbols.DecimalNumberSymbolsHateosResource;
import walkingkooka.spreadsheet.server.decimalnumbersymbols.DecimalNumberSymbolsHateosResourceSet;
import walkingkooka.spreadsheet.server.locale.LocaleTag;

public final class DecimalNumberSymbolsFetcherWatchers extends FetcherWatchers<DecimalNumberSymbolsFetcherWatcher>
    implements DecimalNumberSymbolsFetcherWatcher {

    public static DecimalNumberSymbolsFetcherWatchers empty() {
        return new DecimalNumberSymbolsFetcherWatchers();
    }

    private DecimalNumberSymbolsFetcherWatchers() {
        super();
    }

    @Override
    public void onDecimalNumberSymbolsHateosResource(final LocaleTag id,
                                                     final DecimalNumberSymbolsHateosResource symbols,
                                                     final AppContext context) {
        this.fire(
            DecimalNumberSymbolsFetcherWatchersDecimalNumberSymbolsHateosResourceEvent.with(
                id,
                symbols,
                context
            )
        );
    }

    @Override
    public void onDecimalNumberSymbolsHateosResourceSet(final String localeStartsWith,
                                                        final DecimalNumberSymbolsHateosResourceSet symbols,
                                                        final AppContext context) {
        this.fire(
            DecimalNumberSymbolsFetcherWatchersDecimalNumberSymbolsHateosResourceSetEvent.with(
                localeStartsWith,
                symbols,
                context
            )
        );
    }
}
