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

import walkingkooka.spreadsheet.server.locale.LocaleHateosResource;
import walkingkooka.spreadsheet.server.locale.LocaleLanguageTag;

/**
 * The event payload used by {@link LocaleFetcherWatchers}.
 */
final class LocaleFetcherWatchersLocaleHateosResourceEvent extends FetcherWatchersEvent<LocaleFetcherWatcher> {

    static LocaleFetcherWatchersLocaleHateosResourceEvent with(final LocaleLanguageTag id,
                                                               final LocaleHateosResource locale) {
        return new LocaleFetcherWatchersLocaleHateosResourceEvent(
            id,
            locale
        );
    }

    private LocaleFetcherWatchersLocaleHateosResourceEvent(final LocaleLanguageTag id,
                                                           final LocaleHateosResource locale) {
        super();
        this.id = id;
        this.locale = locale;
    }

    @Override
    void fire(final LocaleFetcherWatcher watcher) {
        watcher.onLocaleHateosResource(
            this.id,
            this.locale
        );
    }

    private final LocaleLanguageTag id;

    private final LocaleHateosResource locale;

    @Override
    public String toString() {
        return this.id + " " + this.locale;
    }
}
