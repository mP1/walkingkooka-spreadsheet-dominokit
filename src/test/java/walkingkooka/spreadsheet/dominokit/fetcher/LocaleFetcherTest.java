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

import org.junit.jupiter.api.Test;
import walkingkooka.net.UrlPath;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.server.locale.LocaleLanguageTag;

public final class LocaleFetcherTest implements ClassTesting<LocaleFetcher> {

    @Test
    public void testLocaleLanguageTagGet() {
        this.localeLanguageTagAndCheck(
            UrlPath.parse("/api/locale/EN-AU"),
            LocaleLanguageTag.parse("EN-AU")
        );
    }

    private void localeLanguageTagAndCheck(final UrlPath path,
                                           final LocaleLanguageTag expected) {
        this.checkEquals(
            expected,
            LocaleFetcher.LOCALE_TAG_TEMPLATE.localeTag(path)
        );
    }

    // class............................................................................................................

    @Override
    public Class<LocaleFetcher> type() {
        return LocaleFetcher.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
