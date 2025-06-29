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
import walkingkooka.spreadsheet.server.locale.LocaleHateosResourceSet;

/**
 * The event payload used by {@link LocaleFetcherWatchers}.
 */
final class LocaleFetcherWatchersLocaleHateosResourceSetEvent extends FetcherWatchersEvent<LocaleFetcherWatcher> {

    static LocaleFetcherWatchersLocaleHateosResourceSetEvent with(final LocaleHateosResourceSet locales,
                                                                  final AppContext context) {
        return new LocaleFetcherWatchersLocaleHateosResourceSetEvent(
            locales,
            context
        );
    }

    private LocaleFetcherWatchersLocaleHateosResourceSetEvent(final LocaleHateosResourceSet locales,
                                                              final AppContext context) {
        super(context);
        this.locales = locales;
    }

    @Override
    void fire(final LocaleFetcherWatcher watcher) {
        watcher.onLocaleHateosResourceSet(
            this.locales,
            this.context
        );
    }

    private final LocaleHateosResourceSet locales;

    @Override
    public String toString() {
        return this.locales.toString();
    }
}
