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
import walkingkooka.spreadsheet.server.locale.LocaleHateosResourceSet;
import walkingkooka.spreadsheet.server.locale.LocaleLanguageTag;

public final class LocaleFetcherWatchers extends FetcherWatchers<LocaleFetcherWatcher>
    implements LocaleFetcherWatcher {

    public static LocaleFetcherWatchers empty() {
        return new LocaleFetcherWatchers();
    }

    private LocaleFetcherWatchers() {
        super();
    }

    @Override
    public void onLocaleHateosResource(final LocaleLanguageTag id,
                                       final LocaleHateosResource locale) {
        this.fire(
            LocaleFetcherWatchersLocaleHateosResourceEvent.with(
                id,
                locale
            )
        );
    }

    @Override
    public void onLocaleHateosResourceSet(final LocaleHateosResourceSet locales) {
        this.fire(
            LocaleFetcherWatchersLocaleHateosResourceSetEvent.with(locales)
        );
    }
}
