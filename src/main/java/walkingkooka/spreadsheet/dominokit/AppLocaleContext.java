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

package walkingkooka.spreadsheet.dominokit;

import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.ImmutableSortedSet;
import walkingkooka.collect.set.Sets;
import walkingkooka.locale.LocaleContext;
import walkingkooka.locale.LocaleContextDelegator;
import walkingkooka.locale.LocaleContexts;
import walkingkooka.spreadsheet.dominokit.fetcher.HasLocaleFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.LocaleFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.locale.LocaleComponent;
import walkingkooka.spreadsheet.server.locale.LocaleHateosResource;
import walkingkooka.spreadsheet.server.locale.LocaleHateosResourceSet;
import walkingkooka.spreadsheet.server.locale.LocaleLanguageTag;

import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

final class AppLocaleContext implements LocaleContextDelegator,
    NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher,
    LocaleFetcherWatcher {

    static AppLocaleContext with(final HasLocaleFetcherWatchers hasLocaleFetcherWatchers,
                                 final LocaleContext localeContext) {
        return new AppLocaleContext(
            Objects.requireNonNull(hasLocaleFetcherWatchers, "hasLocaleFetcherWatchers"),
            Objects.requireNonNull(localeContext, "localeContext")
        );
    }

    private AppLocaleContext(final HasLocaleFetcherWatchers watchers,
                             final LocaleContext localeContext) {
        super();
        watchers.addLocaleFetcherWatcher(this);

        this.localeContext = localeContext;
    }

    // LocaleContext....................................................................................................

    @Override
    public Set<Locale> findByLocaleText(final String text,
                                        final int offset,
                                        final int count) {
        Objects.requireNonNull(text, "text");
        if (offset < 0) {
            throw new IllegalArgumentException("Invalid offset < 0");
        }
        if (count < 0) {
            throw new IllegalArgumentException("Invalid count < 0");
        }

        return this.localeToText.entrySet()
            .stream()
            .filter(localeAndText -> {
                final String localeText = localeAndText.getValue();
                return false == localeText.isEmpty() &&
                    (LocaleContexts.CASE_SENSITIVITY.equals(text, localeText) || LocaleContexts.CASE_SENSITIVITY.startsWith(localeText, text));
            }).skip(offset)
            .limit(count)
            .map(Entry::getKey)
            .collect(
                ImmutableSortedSet.collector(LocaleContexts.LANGUAGE_TAG_COMPARATOR)
            );
    }

    @Override
    public Optional<String> localeText(final Locale locale) {
        Objects.requireNonNull(locale, "locale");

        return Optional.ofNullable(
            this.localeToText.get(locale)
        );
    }

    /**
     * This may be updated externally.
     */
    private Map<Locale, String> localeToText = Maps.empty();

    // LocaleContext....................................................................................................

    @Override
    public LocaleContext localeContext() {
        return this.localeContext;
    }

    private LocaleContext localeContext;

    // LocaleFetcherWatcher.............................................................................................

    @Override
    public void onLocaleHateosResource(final LocaleLanguageTag id,
                                       final LocaleHateosResource locale) {
        // NOP
    }

    /**
     * Save the loaded locales. These will appear in the {@link LocaleComponent}.
     */
    @Override
    public void onLocaleHateosResourceSet(final LocaleHateosResourceSet locales) {
        final Set<Locale> availableLocales = Sets.hash();
        final Map<Locale, String> localeToText = Maps.hash();

        for (final LocaleHateosResource localeHateosResource : locales) {
            final Locale locale = Locale.forLanguageTag(
                localeHateosResource.value()
                    .value()
            );

            availableLocales.add(locale);

            localeToText.put(
                locale,
                localeHateosResource.text()
            );
        }

        this.localeToText = localeToText;
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.localeContext.toString();
    }
}
