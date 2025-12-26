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
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetMetadataFetcherTest implements ClassTesting<SpreadsheetMetadataFetcher> {

    @Test
    public void testUrlWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetMetadataFetcher.url(null)
        );
    }

    @Test
    public void testUrl() {
        this.checkEquals(
            Url.parseRelative("/api/spreadsheet/123"),
            SpreadsheetMetadataFetcher.url(
                SpreadsheetId.with(0x123)
            )
        );
    }

    // propertyUrl......................................................................................................

    @Test
    public void testPropertyUrlWithConverter() {
        this.propertyUrlAndCheck(
            SpreadsheetId.with(1),
            SpreadsheetMetadataPropertyName.FORMATTING_CONVERTER,
            Url.parseRelative("/api/spreadsheet/1/metadata/formattingConverter")
        );
    }

    @Test
    public void testPropertyUrlWithDateFormatter() {
        this.propertyUrlAndCheck(
            SpreadsheetId.with(1),
            SpreadsheetMetadataPropertyName.DATE_FORMATTER,
            Url.parseRelative("/api/spreadsheet/1/metadata/dateFormatter")
        );
    }

    @Test
    public void testPropertyUrlWithDateTimeFormatter() {
        this.propertyUrlAndCheck(
            SpreadsheetId.with(1),
            SpreadsheetMetadataPropertyName.DATE_TIME_FORMATTER,
            Url.parseRelative("/api/spreadsheet/1/metadata/dateTimeFormatter")
        );
    }

    private void propertyUrlAndCheck(final SpreadsheetId id,
                                     final SpreadsheetMetadataPropertyName<?> propertyName,
                                     final RelativeUrl expected) {
        this.checkEquals(
            expected,
            SpreadsheetMetadataFetcher.propertyUrl(
                id,
                propertyName
            ),
            () -> "spreadsheetId=" + id + " propertyName=" + propertyName
        );
    }

    // extractSpreadsheetId.............................................................................................

    @Test
    public void testExtractSpreadsheetIdMissing() {
        this.extractSpreadsheetIdAndCheck(
            "/api/spreadsheet",
            null
        );
    }

    @Test
    public void testExtractSpreadsheetIdInvalidFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> SpreadsheetMetadataFetcher.extractSpreadsheetId(
                Url.parseRelative("/api/spreadsheet/!invalid")
            )
        );
    }

    @Test
    public void testExtractSpreadsheetId() {
        this.extractSpreadsheetIdAndCheck(
            "/api/spreadsheet/123/",
            SpreadsheetId.parse("123")
        );
    }

    @Test
    public void testExtractSpreadsheetIdCell() {
        this.extractSpreadsheetIdAndCheck(
            "/api/spreadsheet/456/cell/A1",
            SpreadsheetId.parse("456")
        );
    }

    private void extractSpreadsheetIdAndCheck(final String url,
                                              final SpreadsheetId expected) {
        this.checkEquals(
            Optional.ofNullable(expected),
            SpreadsheetMetadataFetcher.extractSpreadsheetId(
                Url.parseRelative(url)
            )
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetMetadataFetcher> type() {
        return SpreadsheetMetadataFetcher.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
