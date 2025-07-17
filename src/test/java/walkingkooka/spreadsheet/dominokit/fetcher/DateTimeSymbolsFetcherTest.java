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
import walkingkooka.spreadsheet.server.locale.LocaleTag;
import walkingkooka.text.printer.TreePrintableTesting;

public final class DateTimeSymbolsFetcherTest implements TreePrintableTesting,
    ClassTesting<DateTimeSymbolsFetcher> {

    @Test
    public void testParseLocaleTag() {
        this.parseLocaleTagAndCheck(
            UrlPath.parse("/api/dateTimeSymbols/EN-AU"),
            LocaleTag.parse("EN-AU")
        );
    }

    private void parseLocaleTagAndCheck(final UrlPath path,
                                        final LocaleTag expected) {
        this.checkEquals(
            expected,
            DateTimeSymbolsFetcher.parseLocaleTag(path)
        );
    }

    // getDateTimeSymbolsLocaleStartsWith...............................................................................

    @Test
    public void testDateTimeSymbolsLocaleStartsWithUrlWithEmptyStartsWith() {
        this.dateTimeSymbolsLocaleStartsWithUrlAndCheck(
            "",
            "/api/dateTimeSymbols/*/localeStartsWith"
        );
    }

    @Test
    public void testDateTimeSymbolsLocaleStartsWithUrlWithNotEmpty() {
        this.dateTimeSymbolsLocaleStartsWithUrlAndCheck(
            "English",
            "/api/dateTimeSymbols/*/localeStartsWith/English"
        );
    }

    private void dateTimeSymbolsLocaleStartsWithUrlAndCheck(final String startsWith,
                                                            final String expected) {
        this.dateTimeSymbolsLocaleStartsWithUrlAndCheck(
            startsWith,
            Url.parse(expected)
        );
    }

    private void dateTimeSymbolsLocaleStartsWithUrlAndCheck(final String startsWith,
                                                            final Url expected) {
        this.checkEquals(
            expected,
            DateTimeSymbolsFetcher.dateTimeSymbolsLocaleStartsWithUrl(startsWith)
        );
    }

    // localeStartsWith.................................................................................................

    @Test
    public void testLocaleStartsWithWithEmptyStartsWith() {
        this.localeStartsWithAndCheck(
            UrlPath.parse("/api/dateTimeSymbols/*/localeStartsWith"),
            ""
        );
    }

    @Test
    public void testLocaleStartsWithWithNotEmpty() {
        this.localeStartsWithAndCheck(
            UrlPath.parse("/api/dateTimeSymbols/*/localeStartsWith/English"),
            "English"
        );
    }

    private void localeStartsWithAndCheck(final UrlPath path,
                                          final String expected) {
        this.checkEquals(
            expected,
            DateTimeSymbolsFetcher.localeStartsWith(path)
        );
    }

    // class............................................................................................................

    @Override
    public Class<DateTimeSymbolsFetcher> type() {
        return DateTimeSymbolsFetcher.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
