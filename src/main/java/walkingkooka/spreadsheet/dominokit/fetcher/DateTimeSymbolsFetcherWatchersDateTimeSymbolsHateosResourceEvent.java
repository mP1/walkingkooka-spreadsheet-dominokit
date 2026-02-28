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

import walkingkooka.spreadsheet.server.datetimesymbols.DateTimeSymbolsHateosResource;
import walkingkooka.spreadsheet.server.locale.LocaleLanguageTag;

/**
 * The event payload used by {@link DateTimeSymbolsFetcherWatchers}.
 */
final class DateTimeSymbolsFetcherWatchersDateTimeSymbolsHateosResourceEvent extends FetcherWatchersEvent<DateTimeSymbolsFetcherWatcher> {

    static DateTimeSymbolsFetcherWatchersDateTimeSymbolsHateosResourceEvent with(final LocaleLanguageTag id,
                                                                                 final DateTimeSymbolsHateosResource symbols) {
        return new DateTimeSymbolsFetcherWatchersDateTimeSymbolsHateosResourceEvent(
            id,
            symbols
        );
    }

    private DateTimeSymbolsFetcherWatchersDateTimeSymbolsHateosResourceEvent(final LocaleLanguageTag id,
                                                                             final DateTimeSymbolsHateosResource symbols) {
        super();
        this.id = id;
        this.symbols = symbols;
    }

    @Override
    void fire(final DateTimeSymbolsFetcherWatcher watcher) {
        watcher.onDateTimeSymbolsHateosResource(
            this.id,
            this.symbols
        );
    }

    private final LocaleLanguageTag id;

    private final DateTimeSymbolsHateosResource symbols;

    @Override
    public String toString() {
        return this.id + " " + this.symbols;
    }
}
