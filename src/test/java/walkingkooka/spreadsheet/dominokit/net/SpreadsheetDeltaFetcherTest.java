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

package walkingkooka.spreadsheet.dominokit.net;

import org.junit.jupiter.api.Test;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.UrlPath;
import walkingkooka.net.UrlQueryString;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetValueType;
import walkingkooka.spreadsheet.SpreadsheetViewportWindows;
import walkingkooka.spreadsheet.dominokit.AppContexts;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.ui.find.SpreadsheetCellFind;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReferencePath;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportAnchor;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportNavigation;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportNavigationList;
import walkingkooka.test.Testing;

import java.util.Optional;
import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetDeltaFetcherTest implements Testing {

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

    // cellFindQueryString..................................................................................................

    @Test
    public void testCellFindQueryStringWithNullCellFindFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetDeltaFetcher.cellFindQueryString(
                        null
                )
        );
    }

    @Test
    public void testCellFindQueryStringPath() {
        this.cellFindQueryStringAndCheck(
                SpreadsheetCellFind.empty()
                        .setPath(PATH),
                "cell-range-path=bulr"
        );
    }

    @Test
    public void testCellFindQueryStringOffset() {
        this.cellFindQueryStringAndCheck(
                SpreadsheetCellFind.empty()
                        .setOffset(OFFSET),
                "offset=12"
        );
    }

    @Test
    public void testCellFindQueryStringMax() {
        this.cellFindQueryStringAndCheck(
                SpreadsheetCellFind.empty()
                        .setMax(MAX),
                "max=34"
        );
    }

    @Test
    public void testCellFindQueryStringValueType() {
        this.cellFindQueryStringAndCheck(
                SpreadsheetCellFind.empty()
                        .setValueType(
                                Optional.of(
                                        SpreadsheetValueType.NUMBER)
                        ),
                "value-type=number"
        );
    }

    @Test
    public void testCellFindQueryStringQuery() {
        this.cellFindQueryStringAndCheck(
                SpreadsheetCellFind.empty()
                        .setQuery(QUERY),
                "query=query789"
        );
    }

    @Test
    public void testCellFindQueryStringAllParameters() {
        this.cellFindQueryStringAndCheck(
                SpreadsheetCellFind.empty()
                        .setPath(PATH)
                        .setOffset(OFFSET)
                        .setMax(MAX)
                        .setValueType(VALUE_TYPE)
                        .setQuery(QUERY),
                "cell-range-path=bulr&max=34&offset=12&query=query789&value-type=date"
        );
    }

    private void cellFindQueryStringAndCheck(final SpreadsheetCellFind find,
                                        final String expected) {
        this.checkEquals(
                UrlQueryString.parse(expected),
                SpreadsheetDeltaFetcher.cellFindQueryString(
                        find
                ),
                () -> "cellFindQueryString " + find
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
    public void testViewportQueryStringColumnAndNavigationLeftColumn() {
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
                                        SpreadsheetViewportNavigation.leftColumn()
                                )
                        ),
                "home=A2&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=ABC&selectionType=column&navigation=left+column"
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
                                        SpreadsheetViewportNavigation.extendRightColumn()
                                )
                        ),
                "home=A2&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=Z&selectionType=column&navigation=extend-right+column"
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

    // findCells........................................................................................................

    private final static SpreadsheetId ID = SpreadsheetId.parse("1234");

    private final static SpreadsheetCellRangeReference CELLS = SpreadsheetSelection.parseCellRange("A1:B2");

    private final static Optional<SpreadsheetCellRangeReferencePath> PATH = Optional.of(
            SpreadsheetCellRangeReferencePath.BULR
    );

    private final static OptionalInt OFFSET = OptionalInt.of(12);

    private final static OptionalInt MAX = OptionalInt.of(34);

    private final static Optional<String> VALUE_TYPE = Optional.of(SpreadsheetValueType.DATE);

    private final static Optional<String> QUERY = Optional.of(
            "query789"
    );

    @Test
    public void testFindCellsWithNullIdFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetDeltaFetcher.findCellsUrl(
                        null,
                        CELLS,
                        SpreadsheetCellFind.empty()
                )
        );
    }

    @Test
    public void testFindCellsWithNullCellsFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetDeltaFetcher.findCellsUrl(
                        ID,
                        null,
                        SpreadsheetCellFind.empty()
                )
        );
    }

    @Test
    public void testFindCellsWithNullFindFails() {
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
    public void testFindCellsPath() {
        this.findCellsUrlAndCheck(
                ID,
                CELLS,
                SpreadsheetCellFind.empty()
                        .setPath(PATH),
                Url.parseRelative("/api/spreadsheet/1234/cell/A1:B2/find?cell-range-path=bulr")
        );
    }

    @Test
    public void testFindCellsOffset() {
        this.findCellsUrlAndCheck(
                ID,
                CELLS,
                SpreadsheetCellFind.empty()
                        .setOffset(OFFSET),
                Url.parseRelative("/api/spreadsheet/1234/cell/A1:B2/find?offset=12")
        );
    }

    @Test
    public void testFindCellsMax() {
        this.findCellsUrlAndCheck(
                ID,
                CELLS,
                SpreadsheetCellFind.empty()
                        .setMax(MAX),
                Url.parseRelative("/api/spreadsheet/1234/cell/A1:B2/find?max=34")
        );
    }

    @Test
    public void testFindCellsValueType() {
        this.findCellsUrlAndCheck(
                ID,
                CELLS,
                SpreadsheetCellFind.empty()
                        .setValueType(
                                Optional.of(
                                        SpreadsheetValueType.NUMBER)
                        ),
                Url.parseRelative("/api/spreadsheet/1234/cell/A1:B2/find?value-type=number")
        );
    }

    @Test
    public void testFindCellsQuery() {
        this.findCellsUrlAndCheck(
                ID,
                CELLS,
                SpreadsheetCellFind.empty()
                        .setQuery(QUERY),
                Url.parseRelative("/api/spreadsheet/1234/cell/A1:B2/find?query=query789")
        );
    }

    @Test
    public void testFindCellsAllParameters() {
        this.findCellsUrlAndCheck(
                ID,
                CELLS,
                SpreadsheetCellFind.empty()
                        .setPath(PATH)
                        .setOffset(OFFSET)
                        .setMax(MAX)
                        .setValueType(VALUE_TYPE)
                        .setQuery(QUERY),
                Url.parseRelative("/api/spreadsheet/1234/cell/A1:B2/find?cell-range-path=bulr&max=34&offset=12&query=query789&value-type=date")
        );
    }

    private void findCellsUrlAndCheck(final SpreadsheetId id,
                                      final SpreadsheetCellRangeReference cells,
                                      final SpreadsheetCellFind find,
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

                    public SpreadsheetMetadataFetcher spreadsheetMetadataFetcher() {
                        return SpreadsheetMetadataFetcher.with(
                                new FakeSpreadsheetMetadataFetcherWatcher(),
                                AppContexts.fake()
                        );
                    }
                }
        );

        assertThrows(
                NullPointerException.class,
                () -> fetcher.url(
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
                fetcher.url(
                        id,
                        selection,
                        path
                ),
                "url " + id + ", " + selection + ", " + path
        );
    }
}
