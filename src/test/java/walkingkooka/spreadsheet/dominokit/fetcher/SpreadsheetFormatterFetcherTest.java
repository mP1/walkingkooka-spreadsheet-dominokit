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
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetFormatterFetcherTest implements ClassTesting<SpreadsheetFormatterFetcher> {

    @Test
    public void testUrlWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetFormatterFetcher.url(null)
        );
    }

    @Test
    public void testUrl() {
        this.checkEquals(
            Url.parseRelative("/api/spreadsheet/123/formatter"),
            SpreadsheetFormatterFetcher.url(
                SpreadsheetId.with(0x123)
            )
        );
    }

    // editUrl..........................................................................................................

    @Test
    public void testEditUrlWithEmptyFormatterSelector() {
        this.editUrlAndCheck(
            SpreadsheetId.with(1),
            "",
            "/api/spreadsheet/1/formatter/*/edit"
        );
    }

    @Test
    public void testEditUrlWith() {
        this.editUrlAndCheck(
            SpreadsheetId.with(1),
            "hello-formatter 123",
            "/api/spreadsheet/1/formatter/*/edit/hello-formatter%20123"
        );
    }

    private void editUrlAndCheck(final SpreadsheetId id,
                                 final String selector,
                                 final String expected) {
        this.editUrlAndCheck(
            id,
            selector,
            Url.parseAbsoluteOrRelative(expected)
        );
    }

    private void editUrlAndCheck(final SpreadsheetId id,
                                 final String selector,
                                 final AbsoluteOrRelativeUrl expected) {
        this.checkEquals(
            expected,
            SpreadsheetFormatterFetcher.editUrl(
                id,
                selector
            )
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetFormatterFetcher> type() {
        return SpreadsheetFormatterFetcher.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
