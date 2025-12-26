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
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.UrlPath;
import walkingkooka.net.UrlQueryString;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.dominokit.AppContexts;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.engine.SpreadsheetCellFindQuery;
import walkingkooka.spreadsheet.engine.SpreadsheetCellQuery;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReferencePath;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.SpreadsheetValueType;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewport;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportAnchor;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportNavigation;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportNavigationList;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportWindows;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonString;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContexts;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContexts;
import walkingkooka.validation.ValueType;
import walkingkooka.validation.form.FormName;

import java.math.MathContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetDeltaFetcherTest implements SpreadsheetMetadataTesting {

    // isGetAllCells....................................................................................................

    @Test
    public void testIsGetAllCellsPOST() {
        this.isGetAllCellsAndCheck(
            HttpMethod.POST,
            "https://server/api/spreadsheet/1/cells/*",
            false
        );
    }

    @Test
    public void testIsGetAllCellsGetNotSpreadsheet() {
        this.isGetAllCellsAndCheck(
            HttpMethod.GET,
            "https://server/api/not/1/cells",
            false
        );
    }

    @Test
    public void testIsGetAllCellsGetNotAllCells() {
        this.isGetAllCellsAndCheck(
            HttpMethod.GET,
            "https://server/api/spreadsheet/1/cells",
            false
        );
    }

    @Test
    public void testIsGetAllCellsGetAllCells() {
        this.isGetAllCellsAndCheck(
            HttpMethod.GET,
            "https://server/api/spreadsheet/1/cells/*",
            true
        );
    }

    @Test
    public void testIsGetAllCellsGetAllCellsExtraPath() {
        this.isGetAllCellsAndCheck(
            HttpMethod.GET,
            "https://server/api/spreadsheet/1/cells/*/extra",
            false
        );
    }

    private void isGetAllCellsAndCheck(final HttpMethod method,
                                       final String url,
                                       final boolean expected) {
        this.isGetAllCellsAndCheck(
            method,
            Url.parseAbsoluteOrRelative(url),
            expected
        );
    }

    private void isGetAllCellsAndCheck(final HttpMethod method,
                                       final AbsoluteOrRelativeUrl url,
                                       final boolean expected) {
        this.checkEquals(
            expected,
            SpreadsheetDeltaFetcher.isGetAllCells(
                method,
                url
            ),
            () -> method + " " + url
        );
    }

    // isGetLabelMapping................................................................................................

    @Test
    public void testIsGetLabelMappingsFindByNameMissingFindByName() {
        this.isGetLabelMappingsFindByNameAndCheck(
            HttpMethod.GET,
            UrlPath.parse("/api/spreadsheet/1/label/*?offset=0&count=1"),
            false
        );
    }

    @Test
    public void testIsGetLabelMappingsFindByNameMissingQuery() {
        this.isGetLabelMappingsFindByNameAndCheck(
            HttpMethod.GET,
            UrlPath.parse("/api/spreadsheet/1/label/*/findByName"),
            true
        );
    }

    @Test
    public void testIsGetLabelMappingsFindByNameWithQuery() {
        this.isGetLabelMappingsFindByNameAndCheck(
            HttpMethod.GET,
            UrlPath.parse("/api/spreadsheet/1/label/*/findByName/query"),
            true
        );
    }

    public void isGetLabelMappingsFindByNameAndCheck(final HttpMethod method,
                                                     final UrlPath path,
                                                     final boolean expected) {
        this.checkEquals(
            expected,
            SpreadsheetDeltaFetcher.isGetLabelMappingsFindByName(
                method,
                path
            ),
            () -> "isGetLabelMapping " + method + " " + path
        );
    }

    // patchValuePatch..................................................................................................

    @Test
    public void testPatchValueWithBooleanTrue() {
        this.patchValuePatchAndCheck(
            SpreadsheetValueType.BOOLEAN_STRING,
            true,
            "{\n" +
                "  \"formula\": {\n" +
                "    \"value\": true\n" +
                "  }\n" +
                "}"
        );
    }

    @Test
    public void testPatchValueWithBooleanFalse() {
        this.patchValuePatchAndCheck(
            SpreadsheetValueType.BOOLEAN_STRING,
            false,
            "{\n" +
                "  \"formula\": {\n" +
                "    \"value\": false\n" +
                "  }\n" +
                "}"
        );
    }

    @Test
    public void testPatchValueWithCell() {
        this.patchValuePatchAndCheck(
            SpreadsheetValueType.CELL_STRING,
            SpreadsheetSelection.A1,
            "{\n" +
                "  \"formula\": {\n" +
                "    \"value\": {\n" +
                "      \"type\": \"spreadsheet-cell-reference\",\n" +
                "      \"value\": \"A1\"\n" +
                "    }\n" +
                "  }\n" +
                "}"
        );
    }

    @Test
    public void testPatchValueWithCellRange() {
        this.patchValuePatchAndCheck(
            SpreadsheetValueType.CELL_RANGE_STRING,
            SpreadsheetSelection.A1.toCellRange(),
            "{\n" +
                "  \"formula\": {\n" +
                "    \"value\": {\n" +
                "      \"type\": \"spreadsheet-cell-range-reference\",\n" +
                "      \"value\": \"A1\"\n" +
                "    }\n" +
                "  }\n" +
                "}"
        );
    }

    @Test
    public void testPatchValueWithColumn() {
        this.patchValuePatchAndCheck(
            SpreadsheetValueType.COLUMN_STRING,
            SpreadsheetSelection.parseColumn("AB"),
            "{\n" +
                "  \"formula\": {\n" +
                "    \"value\": {\n" +
                "      \"type\": \"spreadsheet-column-reference\",\n" +
                "      \"value\": \"AB\"\n" +
                "    }\n" +
                "  }\n" +
                "}"
        );
    }

    @Test
    public void testPatchValueWithColumnRange() {
        this.patchValuePatchAndCheck(
            SpreadsheetValueType.COLUMN_RANGE_STRING,
            SpreadsheetSelection.parseColumnRange("C:D"),
            "{\n" +
                "  \"formula\": {\n" +
                "    \"value\": {\n" +
                "      \"type\": \"spreadsheet-column-range-reference\",\n" +
                "      \"value\": \"C:D\"\n" +
                "    }\n" +
                "  }\n" +
                "}"
        );
    }

    @Test
    public void testPatchValueWithDate() {
        this.patchValuePatchAndCheck(
            SpreadsheetValueType.DATE_STRING,
            LocalDate.of(1999, 12, 31),
            "{\n" +
                "  \"formula\": {\n" +
                "    \"value\": {\n" +
                "      \"type\": \"local-date\",\n" +
                "      \"value\": \"1999-12-31\"\n" +
                "    }\n" +
                "  }\n" +
                "}"
        );
    }

    @Test
    public void testPatchValueWithDateTime() {
        this.patchValuePatchAndCheck(
            SpreadsheetValueType.DATE_TIME_STRING,
            LocalDateTime.of(1999, 12, 31, 12, 58, 59),
            "{\n" +
                "  \"formula\": {\n" +
                "    \"value\": {\n" +
                "      \"type\": \"local-date-time\",\n" +
                "      \"value\": \"1999-12-31T12:58:59\"\n" +
                "    }\n" +
                "  }\n" +
                "}"
        );
    }

    @Test
    public void testPatchValueWithLabel() {
        this.patchValuePatchAndCheck(
            SpreadsheetValueType.LABEL_STRING,
            SpreadsheetSelection.labelName("HelloLabel"),
            "{\n" +
                "  \"formula\": {\n" +
                "    \"value\": {\n" +
                "      \"type\": \"spreadsheet-label-name\",\n" +
                "      \"value\": \"HelloLabel\"\n" +
                "    }\n" +
                "  }\n" +
                "}"
        );
    }

    @Test
    public void testPatchValueWithNumber() {
        this.patchValuePatchAndCheck(
            SpreadsheetValueType.NUMBER_STRING,
            ExpressionNumberKind.BIG_DECIMAL.create(123),
            "{\n" +
                "  \"formula\": {\n" +
                "    \"value\": {\n" +
                "      \"type\": \"expression-number\",\n" +
                "      \"value\": \"123\"\n" +
                "    }\n" +
                "  }\n" +
                "}"
        );
    }

    @Test
    public void testPatchValueWithRow() {
        this.patchValuePatchAndCheck(
            SpreadsheetValueType.ROW_STRING,
            SpreadsheetSelection.parseRow("1"),
            "{\n" +
                "  \"formula\": {\n" +
                "    \"value\": {\n" +
                "      \"type\": \"spreadsheet-row-reference\",\n" +
                "      \"value\": \"1\"\n" +
                "    }\n" +
                "  }\n" +
                "}"
        );
    }

    @Test
    public void testPatchValueWithRowRange() {
        this.patchValuePatchAndCheck(
            SpreadsheetValueType.ROW_RANGE_STRING,
            SpreadsheetSelection.parseRowRange("2:3"),
            "{\n" +
                "  \"formula\": {\n" +
                "    \"value\": {\n" +
                "      \"type\": \"spreadsheet-row-range-reference\",\n" +
                "      \"value\": \"2:3\"\n" +
                "    }\n" +
                "  }\n" +
                "}"
        );
    }

    @Test
    public void testPatchValueWithText() {
        this.patchValuePatchAndCheck(
            SpreadsheetValueType.TEXT_STRING,
            "HelloText",
            "{\n" +
                "  \"formula\": {\n" +
                "    \"value\": \"HelloText\"\n" +
                "  }\n" +
                "}"
        );
    }

    @Test
    public void testPatchValueWithTime() {
        this.patchValuePatchAndCheck(
            SpreadsheetValueType.TIME_STRING,
            LocalTime.of(12, 58, 59),
            "{\n" +
                "  \"formula\": {\n" +
                "    \"value\": {\n" +
                "      \"type\": \"local-time\",\n" +
                "      \"value\": \"12:58:59\"\n" +
                "    }\n" +
                "  }\n" +
                "}"
        );
    }

    private void patchValuePatchAndCheck(final String valueType,
                                         final Object value,
                                         final String expected) {
        this.patchValuePatchAndCheck2(
            valueType,
            Optional.of(value),
            expected
        );
    }

    private void patchValuePatchAndCheck2(final String valueType,
                                          final Optional<Object> value,
                                          final String expected) {
        this.patchValuePatchAndCheck2(
            ValueType.with(valueType),
            value,
            JsonNode.parse(expected)
        );
    }

    private void patchValuePatchAndCheck2(final ValueType valueType,
                                          final Optional<?> value,
                                          final JsonNode expected) {
        this.checkEquals(
            expected,
            SpreadsheetDeltaFetcher.with(
                SpreadsheetDeltaFetcherWatchers.empty(),
                new FakeAppContext() {

                    @Override
                    public Optional<JsonString> typeName(final Class<?> type) {
                        return this.jsonNodeUnmarshallContext()
                            .typeName(type);
                    }

                    @Override
                    public JsonNodeMarshallContext jsonNodeMarshallContext() {
                        return JsonNodeMarshallContexts.basic();
                    }

                    @Override
                    public JsonNodeUnmarshallContext jsonNodeUnmarshallContext() {
                        return JsonNodeUnmarshallContexts.basic(
                            ExpressionNumberKind.BIG_DECIMAL,
                            MathContext.UNLIMITED
                        );
                    }
                }
            ).patchValuePatch(
                valueType,
                value
            )
        );
    }

    // viewportQueryString..............................................................................................

    @Test
    public void testViewportQueryStringWithNullSelectionFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetDeltaFetcher.viewportQueryString(
                null
            )
        );
    }

    @Test
    public void testViewportQueryStringCell() {
        this.viewportQueryStringAndCheck(
            SpreadsheetSelection.parseCell("B2")
                .viewportRectangle(
                    111,
                    222
                ).viewport()
                .setAnchoredSelection(
                    Optional.of(
                        SpreadsheetSelection.parseCell("B2")
                            .setDefaultAnchor()
                    )
                ),
            "home=B2&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=B2&selectionType=cell"
        );
    }

    @Test
    public void testViewportQueryStringCellRange() {
        this.viewportQueryStringAndCheck(
            SpreadsheetSelection.A1
                .viewportRectangle(
                    111,
                    222
                ).viewport()
                .setAnchoredSelection(
                    Optional.of(
                        SpreadsheetSelection.parseCellRange("B2:B3")
                            .setAnchor(SpreadsheetViewportAnchor.TOP_RIGHT)
                    )
                ),
            "home=A1&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=B2%3AB3&selectionType=cell-range&selectionAnchor=top-right"
        );
    }

    @Test
    public void testViewportQueryStringCellRangeAll() {
        this.viewportQueryStringAndCheck(
            SpreadsheetSelection.A1
                .viewportRectangle(
                    111,
                    222
                ).viewport()
                .setAnchoredSelection(
                    Optional.of(
                        SpreadsheetSelection.ALL_CELLS
                            .setAnchor(SpreadsheetViewportAnchor.TOP_RIGHT)
                    )
                ),
            "home=A1&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=*&selectionType=cell-range&selectionAnchor=top-right"
        );
    }

    @Test
    public void testViewportQueryStringColumn() {
        this.viewportQueryStringAndCheck(
            SpreadsheetSelection.A1
                .viewportRectangle(
                    111,
                    222
                ).viewport()
                .setAnchoredSelection(
                    Optional.of(
                        SpreadsheetSelection.parseColumn("B").setDefaultAnchor()
                    )
                ),
            "home=A1&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=B&selectionType=column"
        );
    }

    @Test
    public void testViewportQueryStringColumnRange() {
        this.viewportQueryStringAndCheck(
            SpreadsheetSelection.A1
                .viewportRectangle(
                    111,
                    222
                ).viewport()
                .setAnchoredSelection(
                    Optional.of(
                        SpreadsheetSelection.parseColumnRange("B:C").setAnchor(SpreadsheetViewportAnchor.LEFT)
                    )
                ),
            "home=A1&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=B%3AC&selectionType=column-range&selectionAnchor=left"
        );
    }

    @Test
    public void testViewportQueryStringRow() {
        this.viewportQueryStringAndCheck(
            SpreadsheetSelection.A1
                .viewportRectangle(
                    111,
                    222
                ).viewport()
                .setAnchoredSelection(
                    Optional.of(
                        SpreadsheetSelection.parseRow("2")
                            .setDefaultAnchor()
                    )
                ),
            "home=A1&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=2&selectionType=row"
        );
    }

    @Test
    public void testViewportQueryStringRowRange() {
        this.viewportQueryStringAndCheck(
            SpreadsheetSelection.A1
                .viewportRectangle(
                    111,
                    222
                ).viewport()
                .setAnchoredSelection(
                    Optional.of(
                        SpreadsheetSelection.parseRowRange("2:3")
                            .setAnchor(SpreadsheetViewportAnchor.TOP)
                    )
                ),
            "home=A1&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=2%3A3&selectionType=row-range&selectionAnchor=top"
        );
    }

    @Test
    public void testViewportQueryStringLabel() {
        this.viewportQueryStringAndCheck(
            SpreadsheetSelection.A1
                .viewportRectangle(
                    111,
                    222
                ).viewport()
                .setAnchoredSelection(
                    Optional.of(
                        SpreadsheetSelection.labelName("Label123").setDefaultAnchor()
                    )
                ),
            "home=A1&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=Label123&selectionType=label"
        );
    }

    @Test
    public void testViewportQueryStringLabel2() {
        this.viewportQueryStringAndCheck(
            SpreadsheetSelection.A1
                .viewportRectangle(
                    111,
                    222
                ).viewport()
                .setAnchoredSelection(
                    Optional.of(
                        SpreadsheetSelection.labelName("Label123")
                            .setDefaultAnchor()
                    )
                ),
            "home=A1&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=Label123&selectionType=label"
        );
    }

    @Test
    public void testViewportQueryStringColumnAndNavigationMoveLeft() {
        this.viewportQueryStringAndCheck(
            SpreadsheetSelection.parseCell("A2")
                .viewportRectangle(
                    111,
                    222
                ).viewport()
                .setAnchoredSelection(
                    Optional.of(
                        SpreadsheetSelection.parseColumn("ABC")
                            .setDefaultAnchor()
                    )
                ).setNavigations(
                    SpreadsheetViewportNavigationList.EMPTY.concat(
                        SpreadsheetViewportNavigation.moveLeft()
                    )
                ),
            "home=A2&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=ABC&selectionType=column&navigation=move+left+column"
        );
    }

    @Test
    public void testViewportQueryStringColumnAndNavigationExtendRightColumn() {
        this.viewportQueryStringAndCheck(
            SpreadsheetSelection.parseCell("A2")
                .viewportRectangle(
                    111,
                    222
                ).viewport()
                .setAnchoredSelection(
                    Optional.of(
                        SpreadsheetSelection.parseColumn("Z")
                            .setDefaultAnchor()
                    )
                ).setNavigations(
                    SpreadsheetViewportNavigationList.EMPTY.concat(
                        SpreadsheetViewportNavigation.extendMoveRight()
                    )
                ),
            "home=A2&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=Z&selectionType=column&navigation=move%26extend+right+column"
        );
    }

    private void viewportQueryStringAndCheck(final SpreadsheetViewport viewport,
                                             final String expected) {
        this.viewportQueryStringAndCheck(
            viewport,
            UrlQueryString.parse(expected)
        );
    }

    private void viewportQueryStringAndCheck(final SpreadsheetViewport viewport,
                                             final UrlQueryString expected) {
        this.checkEquals(
            expected,
            SpreadsheetDeltaFetcher.viewportQueryString(
                viewport
            ),
            viewport::toString
        );
    }

    // windowQueryString..................................................................................................

    @Test
    public void testWindowQueryStringWithNullWindowFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetDeltaFetcher.windowQueryString(
                null
            )
        );
    }

    @Test
    public void testWindowQueryStringEmpty() {
        this.windowQueryStringAndCheck(
            "",
            ""
        );
    }

    @Test
    public void testWindowQueryStringNotEmpty() {
        this.windowQueryStringAndCheck(
            "a1:b2",
            "window=A1:B2"
        );
    }

    @Test
    public void testWindowQueryStringAllCells() {
        this.windowQueryStringAndCheck(
            "*",
            "window=*"
        );
    }

    @Test
    public void testWindowQueryStringNotEmpty2() {
        this.windowQueryStringAndCheck(
            "a1:b2,c3:d4",
            "window=A1:B2,C3:D4"
        );
    }

    private void windowQueryStringAndCheck(final String window,
                                           final String expected) {
        this.windowQueryStringAndCheck(
            SpreadsheetViewportWindows.parse(window),
            UrlQueryString.parse(expected)
        );
    }

    private void windowQueryStringAndCheck(final SpreadsheetViewportWindows window,
                                           final UrlQueryString expected) {
        this.checkEquals(
            expected,
            SpreadsheetDeltaFetcher.windowQueryString(
                window
            ),
            window::toString
        );
    }

    // viewportAndWindowQueryStringAndCheck.........................................................................................

    @Test
    public void testViewportWindowQueryStringCell() {
        this.viewportAndWindowQueryStringAndCheck(
            SpreadsheetSelection.A1
                .viewportRectangle(111, 222)
                .viewport()
                .setAnchoredSelection(
                    Optional.of(
                        SpreadsheetSelection.parseCell("B2")
                            .setDefaultAnchor()
                    )
                ),
            "A1:C3",
            "home=A1&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=B2&selectionType=cell&window=A1%3AC3"
        );
    }

    @Test
    public void testViewportWindowQueryStringCellRange() {
        this.viewportAndWindowQueryStringAndCheck(
            SpreadsheetSelection.A1
                .viewportRectangle(111, 222)
                .viewport()
                .setAnchoredSelection(
                    Optional.of(
                        SpreadsheetSelection.parseCellRange("B2:B3")
                            .setAnchor(SpreadsheetViewportAnchor.TOP_LEFT)
                    )
                )
            ,
            "A1:C3",
            "home=A1&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=B2%3AB3&selectionType=cell-range&selectionAnchor=top-left&window=A1%3AC3"
        );
    }

    @Test
    public void testViewportWindowQueryStringColumn() {
        this.viewportAndWindowQueryStringAndCheck(
            SpreadsheetSelection.A1
                .viewportRectangle(111, 222)
                .viewport()
                .setAnchoredSelection(
                    Optional.of(
                        SpreadsheetSelection.parseColumn("B")
                            .setDefaultAnchor()
                    )
                )
            ,
            "A1:C3",
            "home=A1&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=B&selectionType=column&window=A1%3AC3"
        );
    }

    @Test
    public void testViewportWindowQueryStringColumnRange() {
        this.viewportAndWindowQueryStringAndCheck(
            SpreadsheetSelection.A1
                .viewportRectangle(111, 222)
                .viewport()
                .setAnchoredSelection(
                    Optional.of(
                        SpreadsheetSelection.parseColumnRange("B:C")
                            .setAnchor(SpreadsheetViewportAnchor.LEFT)
                    )
                ),
            "A1:C3",
            "home=A1&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=B%3AC&selectionType=column-range&selectionAnchor=left&window=A1%3AC3"
        );
    }

    @Test
    public void testViewportWindowQueryStringRow() {
        this.viewportAndWindowQueryStringAndCheck(
            SpreadsheetSelection.A1
                .viewportRectangle(111, 222)
                .viewport()
                .setAnchoredSelection(
                    Optional.of(
                        SpreadsheetSelection.parseRow("2")
                            .setDefaultAnchor()
                    )
                ),
            "A1:C3",
            "home=A1&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=2&selectionType=row&window=A1%3AC3"
        );
    }

    @Test
    public void testViewportWindowQueryStringRowRange() {
        this.viewportAndWindowQueryStringAndCheck(
            SpreadsheetSelection.A1
                .viewportRectangle(111, 222)
                .viewport()
                .setAnchoredSelection(
                    Optional.of(
                        SpreadsheetSelection.parseRowRange("2:3")
                            .setAnchor(SpreadsheetViewportAnchor.TOP)
                    )
                ),
            "A1:C3",
            "home=A1&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=2%3A3&selectionType=row-range&selectionAnchor=top&window=A1%3AC3"
        );
    }

    @Test
    public void testViewportWindowQueryStringLabel() {
        this.viewportAndWindowQueryStringAndCheck(
            SpreadsheetSelection.A1
                .viewportRectangle(111, 222)
                .viewport()
                .setAnchoredSelection(
                    Optional.of(
                        SpreadsheetSelection.labelName("Label123")
                            .setDefaultAnchor()
                    )
                ),
            "A1:C3",
            "home=A1&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=Label123&selectionType=label&window=A1%3AC3"
        );
    }

    private void viewportAndWindowQueryStringAndCheck(final SpreadsheetViewport viewport,
                                                      final String windows,
                                                      final String expected) {
        this.viewportAndWindowQueryStringAndCheck(
            viewport,
            SpreadsheetViewportWindows.parse(windows),
            UrlQueryString.parse(expected)
        );
    }

    private void viewportAndWindowQueryStringAndCheck(final SpreadsheetViewport viewport,
                                                      final SpreadsheetViewportWindows windows,
                                                      final UrlQueryString expected) {
        this.checkEquals(
            expected,
            SpreadsheetDeltaFetcher.viewportAndWindowQueryString(
                viewport,
                windows
            ),
            () -> viewport + " " + windows
        );
    }

    // getFindCells........................................................................................................

    private final static SpreadsheetId ID = SpreadsheetId.parse("1234");

    private final static SpreadsheetCellRangeReference CELLS = SpreadsheetSelection.parseCellRange("A1:B2");

    private final static Optional<SpreadsheetCellRangeReferencePath> PATH = Optional.of(
        SpreadsheetCellRangeReferencePath.BULR
    );

    private final static OptionalInt OFFSET = OptionalInt.of(12);

    private final static OptionalInt COUNT = OptionalInt.of(34);

    private final static Optional<ValueType> VALUE_TYPE = Optional.of(
        SpreadsheetValueType.DATE
    );

    private final static Optional<SpreadsheetCellQuery> QUERY = Optional.of(
        SpreadsheetCellQuery.parse("query789()")
    );

    @Test
    public void testGetFindCellsWithNullIdFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetDeltaFetcher.findCellsUrl(
                null,
                CELLS,
                SpreadsheetCellFindQuery.empty()
            )
        );
    }

    @Test
    public void testGetFindCellsWithNullCellsFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetDeltaFetcher.findCellsUrl(
                ID,
                null,
                SpreadsheetCellFindQuery.empty()
            )
        );
    }

    @Test
    public void testGetFindCellsWithNullFindFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetDeltaFetcher.findCellsUrl(
                ID,
                CELLS,
                null
            )
        );
    }

    @Test
    public void testGetFindCellsPath() {
        this.getFindCellsUrlAndCheck(
            ID,
            CELLS,
            SpreadsheetCellFindQuery.empty()
                .setPath(PATH),
            Url.parseRelative("/api/spreadsheet/1234/cell/A1:B2/find?cell-range-path=BULR")
        );
    }

    @Test
    public void testGetFindCellsOffset() {
        this.getFindCellsUrlAndCheck(
            ID,
            CELLS,
            SpreadsheetCellFindQuery.empty()
                .setOffset(OFFSET),
            Url.parseRelative("/api/spreadsheet/1234/cell/A1:B2/find?offset=12")
        );
    }

    @Test
    public void testGetFindCellsCount() {
        this.getFindCellsUrlAndCheck(
            ID,
            CELLS,
            SpreadsheetCellFindQuery.empty()
                .setCount(COUNT),
            Url.parseRelative("/api/spreadsheet/1234/cell/A1:B2/find?count=34")
        );
    }

    @Test
    public void testGetFindCellsValueType() {
        this.getFindCellsUrlAndCheck(
            ID,
            CELLS,
            SpreadsheetCellFindQuery.empty()
                .setValueType(
                    Optional.of(
                        SpreadsheetValueType.NUMBER)
                ),
            Url.parseRelative("/api/spreadsheet/1234/cell/A1:B2/find?value-type=number")
        );
    }

    @Test
    public void testGetFindCellsQuery() {
        this.getFindCellsUrlAndCheck(
            ID,
            CELLS,
            SpreadsheetCellFindQuery.empty()
                .setQuery(QUERY),
            Url.parseRelative("/api/spreadsheet/1234/cell/A1:B2/find?query=query789()")
        );
    }

    @Test
    public void testGetFindCellsAllParameters() {
        this.getFindCellsUrlAndCheck(
            ID,
            CELLS,
            SpreadsheetCellFindQuery.empty()
                .setPath(PATH)
                .setOffset(OFFSET)
                .setCount(COUNT)
                .setValueType(VALUE_TYPE)
                .setQuery(QUERY),
            Url.parseRelative("/api/spreadsheet/1234/cell/A1:B2/find?cell-range-path=BULR&count=34&offset=12&query=query789()&value-type=date")
        );
    }

    private void getFindCellsUrlAndCheck(final SpreadsheetId id,
                                         final SpreadsheetCellRangeReference cells,
                                         final SpreadsheetCellFindQuery find,
                                         final RelativeUrl expected) {
        this.checkEquals(
            expected,
            SpreadsheetDeltaFetcher.findCellsUrl(
                id,
                cells,
                find
            ),
            () -> "findCellsUrl " + id + " " + cells + " find=" + find
        );
    }

    // url..............................................................................................................

    @Test
    public void testUrlWithNullIdFails() {
        this.urlFails(
            null,
            SpreadsheetSelection.ALL_CELLS,
            UrlPath.EMPTY
        );
    }

    @Test
    public void testUrlWithNullSelectionFails() {
        this.urlFails(
            SpreadsheetId.with(1),
            null,
            UrlPath.EMPTY
        );
    }

    @Test
    public void testUrlWithNullPathFails() {
        this.urlFails(
            SpreadsheetId.with(1),
            SpreadsheetSelection.ALL_CELLS,
            null
        );
    }

    private void urlFails(final SpreadsheetId id,
                          final SpreadsheetSelection selection,
                          final UrlPath path) {
        final SpreadsheetDeltaFetcher fetcher = SpreadsheetDeltaFetcher.with(
            new FakeSpreadsheetDeltaFetcherWatcher(),
            new FakeAppContext() {

                @Override public SpreadsheetMetadataFetcher spreadsheetMetadataFetcher() {
                    return SpreadsheetMetadataFetcher.with(
                        new FakeSpreadsheetMetadataFetcherWatcher(),
                        AppContexts.fake()
                    );
                }
            }
        );

        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetDeltaFetcher.url(
                id,
                selection,
                path
            )
        );
    }

    @Test
    public void testUrl() {
        this.urlAndCheck(
            1,
            "A1",
            UrlPath.EMPTY,
            "/api/spreadsheet/1/cell/A1"
        );
    }

    @Test
    public void testUrlExtraPath() {
        this.urlAndCheck(
            2,
            "B2",
            UrlPath.parse("clear"),
            "/api/spreadsheet/2/cell/B2/clear"
        );
    }

    private void urlAndCheck(final long id,
                             final String cell,
                             final UrlPath path,
                             final String url) {
        this.urlAndCheck(
            SpreadsheetId.with(id),
            SpreadsheetSelection.parseCell(cell),
            path,
            Url.parseRelative(url)
        );
    }

    private void urlAndCheck(final SpreadsheetId id,
                             final SpreadsheetSelection selection,
                             final UrlPath path,
                             final RelativeUrl url) {
        final SpreadsheetDeltaFetcher fetcher = SpreadsheetDeltaFetcher.with(
            new FakeSpreadsheetDeltaFetcherWatcher(),
            new FakeAppContext() {
                @Override
                public SpreadsheetMetadataFetcher spreadsheetMetadataFetcher() {
                    return SpreadsheetMetadataFetcher.with(
                        new FakeSpreadsheetMetadataFetcherWatcher(),
                        AppContexts.fake()
                    );
                }
            }
        );

        this.checkEquals(
            url,
            SpreadsheetDeltaFetcher.url(
                id,
                selection,
                path
            ),
            "formatter " + id + ", " + selection + ", " + path
        );
    }

    // formUrl..........................................................................................................

    @Test
    public void testFormUrlMissingFormName() {
        this.formUrlAndCheck(
            ID,
            Url.parseRelative("/api/spreadsheet/1234/form/*")
        );
    }

    @Test
    public void testFormUrlWithFormName() {
        this.formUrlAndCheck(
            ID,
            FormName.with("Form123"),
            Url.parseRelative("/api/spreadsheet/1234/form/Form123")
        );
    }

    private void formUrlAndCheck(final SpreadsheetId id,
                                 final RelativeUrl expected) {
        this.formUrlAndCheck(
            id,
            Optional.empty(),
            expected
        );
    }

    private void formUrlAndCheck(final SpreadsheetId id,
                                 final FormName formName,
                                 final RelativeUrl expected) {
        this.formUrlAndCheck(
            id,
            Optional.of(formName),
            expected
        );
    }

    private void formUrlAndCheck(final SpreadsheetId id,
                                 final Optional<FormName> formName,
                                 final RelativeUrl expected) {
        this.checkEquals(
            expected,
            SpreadsheetDeltaFetcher.formUrl(
                id,
                formName
            )
        );
    }

    // patchValuePatch..................................................................................................

    private final static LocalDateTime NOW = LocalDateTime.of(
        1999,
        12,
        31,
        12,
        58,
        59
    );

    @Test
    public void testPatchValuePatchWithTextAndEmptyValue() {
        this.patchValuePatchAndCheck(
            ValueType.TEXT,
            Optional.empty(),
            "{\n" +
                "  \"formula\": {\n" +
                "    \"value\": null\n" +
                "  }\n" +
                "}"
        );
    }

    @Test
    public void testPatchValuePatchWithTextAndNotEmptyString() {
        this.patchValuePatchAndCheck(
            ValueType.TEXT,
            "Hello",
            "{\n" +
                "  \"formula\": {\n" +
                "    \"value\": \"Hello\"\n" +
                "  }\n" +
                "}"
        );
    }

    @Test
    public void testPatchValuePatchWithDateAndToday() {
        this.patchValuePatchAndCheck(
            ValueType.DATE,
            "today",
            "{\n" +
                "  \"formula\": {\n" +
                "    \"value\": {\n" +
                "      \"type\": \"local-date\",\n" +
                "      \"value\": \"1999-12-31\"\n" +
                "    }\n" +
                "  }\n" +
                "}"
        );
    }

    @Test
    public void testPatchValuePatchWithDate() {
        this.patchValuePatchAndCheck(
            ValueType.DATE,
            LocalDate.of(1999, 12, 31),
            "{\n" +
                "  \"formula\": {\n" +
                "    \"value\": {\n" +
                "      \"type\": \"local-date\",\n" +
                "      \"value\": \"1999-12-31\"\n" +
                "    }\n" +
                "  }\n" +
                "}"
        );
    }

    @Test
    public void testPatchValuePatchWithDateTimeAndNow() {
        this.patchValuePatchAndCheck(
            ValueType.DATE_TIME,
            "now",
            "{\n" +
                "  \"formula\": {\n" +
                "    \"value\": {\n" +
                "      \"type\": \"local-date-time\",\n" +
                "      \"value\": \"1999-12-31T12:58:59\"\n" +
                "    }\n" +
                "  }\n" +
                "}"
        );
    }

    @Test
    public void testPatchValuePatchWithDateTime() {
        this.patchValuePatchAndCheck(
            ValueType.DATE_TIME,
            LocalDateTime.of(1999, 12, 31, 12, 58, 59),
            "{\n" +
                "  \"formula\": {\n" +
                "    \"value\": {\n" +
                "      \"type\": \"local-date-time\",\n" +
                "      \"value\": \"1999-12-31T12:58:59\"\n" +
                "    }\n" +
                "  }\n" +
                "}"
        );
    }

    @Test
    public void testPatchValuePatchWithTimeAndNow() {
        this.patchValuePatchAndCheck(
            ValueType.TIME,
            "now",
            "{\n" +
                "  \"formula\": {\n" +
                "    \"value\": {\n" +
                "      \"type\": \"local-time\",\n" +
                "      \"value\": \"12:58:59\"\n" +
                "    }\n" +
                "  }\n" +
                "}"
        );
    }

    @Test
    public void testPatchValuePatchWithTime() {
        this.patchValuePatchAndCheck(
            ValueType.TIME,
            LocalTime.of(12, 58, 59),
            "{\n" +
                "  \"formula\": {\n" +
                "    \"value\": {\n" +
                "      \"type\": \"local-time\",\n" +
                "      \"value\": \"12:58:59\"\n" +
                "    }\n" +
                "  }\n" +
                "}"
        );
    }

    private void patchValuePatchAndCheck(final ValueType valueType,
                                         final Object value,
                                         final String expected) {
        this.patchValuePatchAndCheck(
            valueType,
            Optional.of(value),
            expected
        );
    }

    private void patchValuePatchAndCheck(final ValueType valueType,
                                         final Optional<?> value,
                                         final String expected) {
        this.patchValuePatchAndCheck(
            valueType,
            value,
            JsonNode.parse(expected)
        );
    }

    private void patchValuePatchAndCheck(final ValueType valueType,
                                         final Optional<?> value,
                                         final JsonNode expected) {
        this.checkEquals(
            expected,
            SpreadsheetDeltaFetcher.with(
                SpreadsheetDeltaFetcherWatchers.empty(),
                new FakeAppContext() {

                    @Override
                    public LocalDateTime now() {
                        return NOW;
                    }


                    @Override
                    public JsonNodeMarshallContext jsonNodeMarshallContext() {
                        return JSON_NODE_MARSHALL_CONTEXT;
                    }

                    @Override
                    public JsonNodeUnmarshallContext jsonNodeUnmarshallContext() {
                        return JSON_NODE_UNMARSHALL_CONTEXT;
                    }
                }
            ).patchValuePatch(
                valueType,
                value
            ),
            () -> "patchValuePatch " + valueType + " " + value.map(CharSequences::quoteIfChars).orElse(null)
        );
    }
}
