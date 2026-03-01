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

import org.junit.jupiter.api.Test;
import walkingkooka.locale.LocaleContextTesting2;
import walkingkooka.locale.LocaleContexts;
import walkingkooka.spreadsheet.dominokit.fetcher.HasLocaleFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.LocaleFetcherWatcher;
import walkingkooka.spreadsheet.server.locale.LocaleHateosResource;
import walkingkooka.spreadsheet.server.locale.LocaleHateosResourceSet;
import walkingkooka.spreadsheet.server.locale.LocaleLanguageTag;

import java.util.Locale;

import static org.junit.Assert.assertThrows;

public final class AppLocaleContextTest implements LocaleContextTesting2<AppLocaleContext> {

    private final static Locale LOCALE = Locale.forLanguageTag("en-AU");

    private final static HasLocaleFetcherWatchers HAS_LOCALE_FETCHER_WATCHERS = new HasLocaleFetcherWatchers() {
        @Override
        public Runnable addLocaleFetcherWatcher(final LocaleFetcherWatcher watcher) {
            return () -> {};
        }

        @Override
        public Runnable addLocaleFetcherWatcherOnce(final LocaleFetcherWatcher watcher) {
            throw new UnsupportedOperationException();
        }
    };

    // with.............................................................................................................

    @Test
    public void testWithNullHasLocaleFetcherWatchersFails() {
        assertThrows(
            NullPointerException.class,
            () -> AppLocaleContext.with(
                null,
                LocaleContexts.fake()
            )
        );
    }

    @Test
    public void testWithNullLocaleContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> AppLocaleContext.with(
                HAS_LOCALE_FETCHER_WATCHERS,
                null
            )
        );
    }

    @Test
    public void testLocale() {
        this.localeAndCheck(
            this.createContext(),
            LOCALE
        );
    }

    @Test
    public void testSetLocale() {
        final Locale locale = Locale.forLanguageTag("en-NZ");
        this.checkNotEquals(
            locale,
            LOCALE
        );

        this.setLocaleAndCheck(
            this.createContext(),
            locale
        );
    }

    @Test
    public void testLocaleTextBeforeOnLocaleHateosResourceSet() {
        this.localeTextAndCheck(
            this.createContext(),
            LOCALE
        );
    }

    @Test
    public void testLocaleTextAfterOnLocaleHateosResourceSet() {
        final String text = "Custom Australia Locale";

        final AppLocaleContext context = this.createContext();
        context.onLocaleHateosResourceSet(
            LocaleHateosResourceSet.EMPTY.concat(
                LocaleHateosResource.with(
                    LocaleLanguageTag.fromLocale(LOCALE),
                    text
                )
            )
        );

        this.localeTextAndCheck(
            context,
            LOCALE,
            text
        );
    }

    @Test
    public void testFindTextAfterOnLocaleHateosResourceSet() {
        final AppLocaleContext context = this.createContext();
        context.onLocaleHateosResourceSet(
            LocaleHateosResourceSet.EMPTY.concat(
                LocaleHateosResource.with(
                    LocaleLanguageTag.fromLocale(
                        Locale.forLanguageTag("en-AF")
                    ),
                    "Custom Antartica Locale"
                )
            ).concat(
                LocaleHateosResource.with(
                    LocaleLanguageTag.fromLocale(LOCALE),
                    "Custom Australia Locale"
                )
            ).concat(
                LocaleHateosResource.with(
                    LocaleLanguageTag.fromLocale(
                        Locale.forLanguageTag("en-NZ")
                    ),
                    "Custom New Zealand"
                )
            )
        );

        this.findByLocaleTextAndCheck(
            context,
            "Custom",
            1,
            1,
            LOCALE
        );
    }

    @Override
    public AppLocaleContext createContext() {
        return AppLocaleContext.with(
            HAS_LOCALE_FETCHER_WATCHERS,
            LocaleContexts.jre(LOCALE)
        );
    }

    // class............................................................................................................

    @Override
    public Class<AppLocaleContext> type() {
        return AppLocaleContext.class;
    }
}
