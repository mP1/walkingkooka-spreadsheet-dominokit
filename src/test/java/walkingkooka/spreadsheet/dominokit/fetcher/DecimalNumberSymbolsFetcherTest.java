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
import walkingkooka.net.Url;
import walkingkooka.net.UrlPath;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.server.locale.LocaleLanguageTag;

public final class DecimalNumberSymbolsFetcherTest implements ClassTesting<DecimalNumberSymbolsFetcher> {

    @Test
    public void testLocaleLanguageTag() {
        this.localeLanguageTagAndCheck(
            UrlPath.parse("/api/decimalNumberSymbols/EN-AU"),
            LocaleLanguageTag.parse("EN-AU")
        );
    }

    private void localeLanguageTagAndCheck(final UrlPath path,
                                           final LocaleLanguageTag expected) {
        this.checkEquals(
            expected,
            DecimalNumberSymbolsFetcher.LOCALE_TAG_TEMPLATE.localeLanguageTag(path)
        );
    }

    // decimalNumberSymbolsLocaleStartsWith.............................................................................

    @Test
    public void testDecimalNumberSymbolsLocaleStartsWithUrlWithEmptyStartsWith() {
        this.decimalNumberSymbolsLocaleStartsWithUrlAndCheck(
            "",
            "/api/decimalNumberSymbols/*/localeStartsWith/"
        );
    }

    @Test
    public void testDecimalNumberSymbolsLocaleStartsWithUrlWithNotEmpty() {
        this.decimalNumberSymbolsLocaleStartsWithUrlAndCheck(
            "English",
            "/api/decimalNumberSymbols/*/localeStartsWith/English"
        );
    }

    private void decimalNumberSymbolsLocaleStartsWithUrlAndCheck(final String startsWith,
                                                                 final String expected) {
        this.decimalNumberSymbolsLocaleStartsWithUrlAndCheck(
            startsWith,
            Url.parse(expected)
        );
    }

    private void decimalNumberSymbolsLocaleStartsWithUrlAndCheck(final String startsWith,
                                                                 final Url expected) {
        this.checkEquals(
            expected,
            DecimalNumberSymbolsFetcher.decimalNumberSymbolsLocaleStartsWithUrl(startsWith)
        );
    }

    // extractLocaleStartsWith..........................................................................................

    @Test
    public void testExtractLocaleStartsWithWithEmptyStartsWith() {
        this.extractLocaleStartsWithAndCheck(
            UrlPath.parse("/api/decimalNumberSymbols/*/localeStartsWith"),
            ""
        );
    }

    @Test
    public void testExtractLocaleStartsWithWithNotEmpty() {
        this.extractLocaleStartsWithAndCheck(
            UrlPath.parse("/api/decimalNumberSymbols/*/localeStartsWith/English"),
            "English"
        );
    }

    private void extractLocaleStartsWithAndCheck(final UrlPath path,
                                                 final String expected) {
        this.checkEquals(
            expected,
            DecimalNumberSymbolsFetcher.extractLocaleStartsWith(path)
        );
    }

    // class............................................................................................................

    @Override
    public Class<DecimalNumberSymbolsFetcher> type() {
        return DecimalNumberSymbolsFetcher.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
