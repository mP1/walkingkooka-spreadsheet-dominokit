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
import walkingkooka.net.UrlPath;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

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

    // cellFormatterEditUrl.............................................................................................

    @Test
    public void testCellFormatterEditUrlWithEmptyFormatterSelector() {
        this.cellFormatterEditUrlAndCheck(
            SpreadsheetId.with(1),
            SpreadsheetSelection.A1,
            "",
            "/api/spreadsheet/1/cell/A1/formatter-edit"
        );
    }

    @Test
    public void testCellFormatterEditUrlWith() {
        this.cellFormatterEditUrlAndCheck(
            SpreadsheetId.with(1),
            SpreadsheetSelection.parseCell("A2"),
            "hello-formatter 123",
            "/api/spreadsheet/1/cell/A2/formatter-edit/hello-formatter%20123"
        );
    }

    private void cellFormatterEditUrlAndCheck(final SpreadsheetId id,
                                              final SpreadsheetExpressionReference cellOrLabel,
                                              final String selector,
                                              final String expected) {
        this.cellFormatterEditUrlAndCheck(
            id,
            cellOrLabel,
            selector,
            Url.parseAbsoluteOrRelative(expected)
        );
    }

    private void cellFormatterEditUrlAndCheck(final SpreadsheetId id,
                                              final SpreadsheetExpressionReference cellOrLabel,
                                              final String selector,
                                              final AbsoluteOrRelativeUrl expected) {
        this.checkEquals(
            expected,
            SpreadsheetFormatterFetcher.cellFormatterEditUrl(
                id,
                cellOrLabel,
                selector
            )
        );
    }

    // metadataFormatterEditUrl.........................................................................................

    @Test
    public void testMetadataFormatterEditUrlWithEmptyFormatterSelector() {
        this.metadataFormatterEditUrlAndCheck(
            SpreadsheetId.with(1),
            SpreadsheetMetadataPropertyName.DATE_FORMATTER,
            "",
            "/api/spreadsheet/1/metadata/dateFormatter/edit"
        );
    }

    @Test
    public void testMetadataFormatterEditUrlWith() {
        this.metadataFormatterEditUrlAndCheck(
            SpreadsheetId.with(1),
            SpreadsheetMetadataPropertyName.TEXT_FORMATTER,
            "hello-formatter 123",
            "/api/spreadsheet/1/metadata/textFormatter/edit/hello-formatter%20123"
        );
    }

    private void metadataFormatterEditUrlAndCheck(final SpreadsheetId id,
                                                  final SpreadsheetMetadataPropertyName<SpreadsheetFormatterSelector> propertyName,
                                                  final String selector,
                                                  final String expected) {
        this.metadataFormatterEditUrlAndCheck(
            id,
            propertyName,
            selector,
            Url.parseAbsoluteOrRelative(expected)
        );
    }

    private void metadataFormatterEditUrlAndCheck(final SpreadsheetId id,
                                                  final SpreadsheetMetadataPropertyName<SpreadsheetFormatterSelector> propertyName,
                                                  final String selector,
                                                  final AbsoluteOrRelativeUrl expected) {
        this.checkEquals(
            expected,
            SpreadsheetFormatterFetcher.metadataFormatterEditUrl(
                id,
                propertyName,
                selector
            ),
            () -> "spreadsheetId=" + id + " propertyName=" + propertyName + " selector=" + selector
        );
    }

    // cellFormatterMenuUrl.............................................................................................

    @Test
    public void testCellFormatterMenuUrlWithCell() {
        this.cellFormatterMenuUrlAndCheck(
            SpreadsheetId.with(1),
            SpreadsheetSelection.A1.toString(),
            "/api/spreadsheet/1/cell/A1/formatter-menu"
        );
    }

    @Test
    public void testCellFormatterMenuUrlWithLabel() {
        this.cellFormatterMenuUrlAndCheck(
            SpreadsheetId.with(1),
            "Label123",
            "/api/spreadsheet/1/cell/Label123/formatter-menu"
        );
    }

    private void cellFormatterMenuUrlAndCheck(final SpreadsheetId id,
                                              final String cellOrLabel,
                                              final String expected) {
        this.cellFormatterMenuUrlAndCheck(
            id,
            SpreadsheetSelection.parseExpressionReference(cellOrLabel),
            Url.parseAbsoluteOrRelative(expected)
        );
    }

    private void cellFormatterMenuUrlAndCheck(final SpreadsheetId id,
                                              final SpreadsheetExpressionReference cellOrLabel,
                                              final AbsoluteOrRelativeUrl expected) {
        this.checkEquals(
            expected,
            SpreadsheetFormatterFetcher.cellFormatterMenuUrl(
                id,
                cellOrLabel
            ),
            () -> "spreadsheetId=" + id + " cellOrLabel=" + cellOrLabel
        );
    }

    // extractCellOrLabel...............................................................................................

    @Test
    public void testExtractCellOrLabelWithCell() {
        this.extractCellOrLabelAndCheck(
            "/api/spreadsheet/1/cell/A1/formatter-menu",
            "A1"
        );
    }

    @Test
    public void testExtractCellOrLabelWithLabel() {
        this.extractCellOrLabelAndCheck(
            "/api/spreadsheet/1/cell/Label123/formatter-menu",
            "Label123"
        );
    }

    private void extractCellOrLabelAndCheck(final String path,
                                            final String expected) {
        this.extractCellOrLabelAndCheck(
            UrlPath.parse(path),
            SpreadsheetSelection.parseExpressionReference(expected)
        );
    }

    private void extractCellOrLabelAndCheck(final UrlPath path,
                                            final SpreadsheetExpressionReference expected) {
        this.checkEquals(
            expected,
            SpreadsheetFormatterFetcher.extractCellOrLabel(path),
            () -> "path=" + path
        );
    }

    // extractOptionalCellOrLabel.......................................................................................

    @Test
    public void testExtractOptionalCellOrLabelMissing() {
        this.extractOptionalCellOrLabelAndCheck(
            "/api/spreadsheet/1/metadata/findConverter/verify"
        );
    }

    @Test
    public void testExtractOptionalCellOrLabelWithCell() {
        this.extractOptionalCellOrLabelAndCheck(
            "/api/spreadsheet/1/cell/A1/formatter-menu",
            "A1"
        );
    }

    @Test
    public void testExtractOptionalCellOrLabelWithLabel() {
        this.extractOptionalCellOrLabelAndCheck(
            "/api/spreadsheet/1/cell/Label123/formatter-menu",
            "Label123"
        );
    }

    private void extractOptionalCellOrLabelAndCheck(final String path) {
        this.extractOptionalCellOrLabelAndCheck(
            path,
            Optional.empty()
        );
    }

    private void extractOptionalCellOrLabelAndCheck(final String path,
                                                    final String expected) {
        this.extractOptionalCellOrLabelAndCheck(
            path,
            Optional.of(expected)
        );
    }

    private void extractOptionalCellOrLabelAndCheck(final String path,
                                                    final Optional<String> expected) {
        this.extractOptionalCellOrLabelAndCheck(
            UrlPath.parse(path),
            expected.map(SpreadsheetSelection::parseExpressionReference)
        );
    }

    private void extractOptionalCellOrLabelAndCheck(final UrlPath path,
                                                    final Optional<SpreadsheetExpressionReference> expected) {
        this.checkEquals(
            expected,
            SpreadsheetFormatterFetcher.extractOptionalCellOrLabel(path),
            () -> "path=" + path
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
