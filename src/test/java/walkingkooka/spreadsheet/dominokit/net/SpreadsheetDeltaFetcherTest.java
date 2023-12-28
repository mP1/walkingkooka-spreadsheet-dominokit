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
import walkingkooka.collect.list.Lists;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.UrlPath;
import walkingkooka.net.UrlQueryString;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetValueType;
import walkingkooka.spreadsheet.SpreadsheetViewportWindows;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangePath;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportAnchor;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportNavigation;
import walkingkooka.test.Testing;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.FunctionExpressionName;

import java.util.Optional;
import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetDeltaFetcherTest implements Testing {

    // appendSelection..................................................................................................

    @Test
    public void testAppendViewportWithNullSelectionFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetDeltaFetcher.appendViewport(
                        null,
                        UrlQueryString.EMPTY
                )
        );
    }

    @Test
    public void testAppendViewportWithNullQueryStringFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetDeltaFetcher.appendViewport(
                        SpreadsheetSelection.A1
                                .viewportRectangle(100, 200)
                                .viewport(),
                        null
                )
        );
    }

    @Test
    public void testAppendViewportCell() {
        this.appendViewportAndCheck(
                SpreadsheetSelection.parseCell("B2")
                        .viewportRectangle(
                                111,
                                222
                        ).viewport()
                        .setSelection(
                                Optional.of(
                                        SpreadsheetSelection.parseCell("B2")
                                                .setDefaultAnchor()
                                )
                        ),
                "home=B2&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=B2&selectionType=cell"
        );
    }

    @Test
    public void testAppendViewportCellRange() {
        this.appendViewportAndCheck(
                SpreadsheetSelection.A1
                        .viewportRectangle(
                                111,
                                222
                        ).viewport()
                        .setSelection(
                                Optional.of(
                                        SpreadsheetSelection.parseCellRange("B2:B3")
                                                .setAnchor(SpreadsheetViewportAnchor.TOP_RIGHT)
                                )
                        ),
                "home=A1&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=B2%3AB3&selectionType=cell-range&selectionAnchor=top-right"
        );
    }

    @Test
    public void testAppendViewportColumn() {
        this.appendViewportAndCheck(
                SpreadsheetSelection.A1
                        .viewportRectangle(
                                111,
                                222
                        ).viewport()
                        .setSelection(
                                Optional.of(
                                        SpreadsheetSelection.parseColumn("B").setDefaultAnchor()
                                )
                        ),
                "home=A1&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=B&selectionType=column"
        );
    }

    @Test
    public void testAppendViewportColumnRange() {
        this.appendViewportAndCheck(
                SpreadsheetSelection.A1
                        .viewportRectangle(
                                111,
                                222
                        ).viewport()
                        .setSelection(
                                Optional.of(
                                        SpreadsheetSelection.parseColumnRange("B:C").setAnchor(SpreadsheetViewportAnchor.LEFT)
                                )
                        ),
                "home=A1&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=B%3AC&selectionType=column-range&selectionAnchor=left"
        );
    }

    @Test
    public void testAppendViewportRow() {
        this.appendViewportAndCheck(
                SpreadsheetSelection.A1
                        .viewportRectangle(
                                111,
                                222
                        ).viewport()
                        .setSelection(
                                Optional.of(
                                        SpreadsheetSelection.parseRow("2")
                                                .setDefaultAnchor()
                                )
                        ),
                "home=A1&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=2&selectionType=row"
        );
    }

    @Test
    public void testAppendViewportRowRange() {
        this.appendViewportAndCheck(
                SpreadsheetSelection.A1
                        .viewportRectangle(
                                111,
                                222
                        ).viewport()
                        .setSelection(
                                Optional.of(
                                        SpreadsheetSelection.parseRowRange("2:3")
                                                .setAnchor(SpreadsheetViewportAnchor.TOP)
                                )
                        ),
                "home=A1&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=2%3A3&selectionType=row-range&selectionAnchor=top"
        );
    }

    @Test
    public void testAppendViewportLabel() {
        this.appendViewportAndCheck(
                SpreadsheetSelection.A1
                        .viewportRectangle(
                                111,
                                222
                        ).viewport()
                        .setSelection(
                                Optional.of(
                                        SpreadsheetSelection.labelName("Label123").setDefaultAnchor()
                                )
                        ),
                "home=A1&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=Label123&selectionType=label"
        );
    }

    @Test
    public void testAppendViewportLabel2() {
        this.appendViewportAndCheck(
                SpreadsheetSelection.A1
                        .viewportRectangle(
                                111,
                                222
                        ).viewport()
                        .setSelection(
                                Optional.of(
                                        SpreadsheetSelection.labelName("Label123")
                                                .setDefaultAnchor()
                                )
                        ),
                UrlQueryString.parse("a=1"),
                UrlQueryString.parse("a=1&home=A1&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=Label123&selectionType=label")
        );
    }

    @Test
    public void testAppendViewportColumnAndNavigationLeftColumn() {
        this.appendViewportAndCheck(
                SpreadsheetSelection.parseCell("A2")
                        .viewportRectangle(
                                111,
                                222
                        ).viewport()
                        .setSelection(
                                Optional.of(
                                        SpreadsheetSelection.parseColumn("ABC")
                                                .setDefaultAnchor()
                                )
                        ).setNavigations(
                                Lists.of(
                                        SpreadsheetViewportNavigation.leftColumn()
                                )
                        ),
                UrlQueryString.parse("a=1"),
                UrlQueryString.parse("a=1&home=A2&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=ABC&selectionType=column&navigation=left+column")
        );
    }

    @Test
    public void testAppendViewportColumnAndNavigationExtendRightColumn() {
        this.appendViewportAndCheck(
                SpreadsheetSelection.parseCell("A2")
                        .viewportRectangle(
                                111,
                                222
                        ).viewport()
                        .setSelection(
                                Optional.of(
                                        SpreadsheetSelection.parseColumn("Z")
                                                .setDefaultAnchor()
                                )
                        ).setNavigations(
                                Lists.of(
                                        SpreadsheetViewportNavigation.extendRightColumn()
                                )
                        ),
                UrlQueryString.parse("a=1"),
                UrlQueryString.parse("a=1&home=A2&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=Z&selectionType=column&navigation=extend-right+column")
        );
    }

    private void appendViewportAndCheck(final SpreadsheetViewport viewport,
                                        final String expected) {
        this.appendViewportAndCheck(
                viewport,
                UrlQueryString.parse(expected)
        );
    }

    private void appendViewportAndCheck(final SpreadsheetViewport viewport,
                                        final UrlQueryString expected) {
        this.appendViewportAndCheck(
                viewport,
                UrlQueryString.EMPTY,
                expected
        );
    }

    private void appendViewportAndCheck(final SpreadsheetViewport viewport,
                                        final UrlQueryString initial,
                                        final UrlQueryString expected) {
        this.checkEquals(
                expected,
                SpreadsheetDeltaFetcher.appendViewport(
                        viewport,
                        initial
                ),
                () -> initial + " appendViewport " + viewport
        );
    }

    // appendWindow..................................................................................................

    @Test
    public void testAppendWindowWithNullWindowFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetDeltaFetcher.appendWindow(
                        null,
                        UrlQueryString.EMPTY
                )
        );
    }

    @Test
    public void testAppendWindowWithNullQueryStringFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetDeltaFetcher.appendWindow(
                        SpreadsheetViewportWindows.EMPTY,
                        null
                )
        );
    }

    @Test
    public void testAppendWindowEmpty() {
        this.appendWindowAndCheck(
                "",
                "a=1",
                "a=1"
        );
    }

    @Test
    public void testAppendWindowNotEmpty() {
        this.appendWindowAndCheck(
                "a1:b2",
                "a=1",
                "a=1&window=A1:B2"
        );
    }

    @Test
    public void testAppendWindowNotEmpty2() {
        this.appendWindowAndCheck(
                "a1:b2,c3:d4",
                "a=1",
                "a=1&window=A1:B2,C3:D4"
        );
    }

    private void appendWindowAndCheck(final String window,
                                      final String initial,
                                      final String expected) {
        this.appendWindowAndCheck(
                SpreadsheetViewportWindows.parse(window),
                UrlQueryString.parse(initial),
                UrlQueryString.parse(expected)
        );
    }

    private void appendWindowAndCheck(final SpreadsheetViewportWindows window,
                                      final UrlQueryString initial,
                                      final UrlQueryString expected) {
        this.checkEquals(
                expected,
                SpreadsheetDeltaFetcher.appendWindow(
                        window,
                        initial
                ),
                () -> initial + " appendWindow " + window
        );
    }

    // appendViewportAndWindowAndCheck.........................................................................................

    @Test
    public void testAppendViewportAndWindowCell() {
        this.appendViewportAndWindowAndCheck(
                SpreadsheetSelection.A1
                        .viewportRectangle(111, 222)
                        .viewport()
                        .setSelection(
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
    public void testAppendViewportAndWindowCellRange() {
        this.appendViewportAndWindowAndCheck(
                SpreadsheetSelection.A1
                        .viewportRectangle(111, 222)
                        .viewport()
                        .setSelection(
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
    public void testAppendViewportAndWindowColumn() {
        this.appendViewportAndWindowAndCheck(
                SpreadsheetSelection.A1
                        .viewportRectangle(111, 222)
                        .viewport()
                        .setSelection(
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
    public void testAppendViewportAndWindowColumnRange() {
        this.appendViewportAndWindowAndCheck(
                SpreadsheetSelection.A1
                        .viewportRectangle(111, 222)
                        .viewport()
                        .setSelection(
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
    public void testAppendViewportAndWindowRow() {
        this.appendViewportAndWindowAndCheck(
                SpreadsheetSelection.A1
                        .viewportRectangle(111, 222)
                        .viewport()
                        .setSelection(
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
    public void testAppendViewportAndWindowRowRange() {
        this.appendViewportAndWindowAndCheck(
                SpreadsheetSelection.A1
                        .viewportRectangle(111, 222)
                        .viewport()
                        .setSelection(
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
    public void testAppendViewportAndWindowLabel() {
        this.appendViewportAndWindowAndCheck(
                SpreadsheetSelection.A1
                        .viewportRectangle(111, 222)
                        .viewport()
                        .setSelection(
                                Optional.of(
                                        SpreadsheetSelection.labelName("Label123")
                                                .setDefaultAnchor()
                                )
                        ),
                "A1:C3",
                "a1",
                "a1&home=A1&width=111.0&height=222.0&includeFrozenColumnsRows=true&selection=Label123&selectionType=label&window=A1%3AC3"
        );
    }

    private void appendViewportAndWindowAndCheck(final SpreadsheetViewport viewport,
                                                 final String windows,
                                                 final String expected) {
        this.appendViewportAndWindowAndCheck(
                viewport,
                windows,
                "",
                expected
        );
    }

    private void appendViewportAndWindowAndCheck(final SpreadsheetViewport viewport,
                                                 final String windows,
                                                 final String initial,
                                                 final String expected) {
        this.appendViewportAndWindowAndCheck(
                viewport,
                SpreadsheetViewportWindows.parse(windows),
                UrlQueryString.parse(initial),
                UrlQueryString.parse(expected)
        );
    }

    private void appendViewportAndWindowAndCheck(final SpreadsheetViewport viewport,
                                                 final SpreadsheetViewportWindows windows,
                                                 final UrlQueryString initial,
                                                 final UrlQueryString expected) {
        this.checkEquals(
                expected,
                SpreadsheetDeltaFetcher.appendViewportAndWindow(
                        viewport,
                        windows,
                        initial
                ),
                () -> initial + " appendViewportAndWindow " + viewport + " " + windows
        );
    }

    // findCells........................................................................................................

    private final static SpreadsheetId ID = SpreadsheetId.parse("1234");

    private final static SpreadsheetCellRange CELLS = SpreadsheetSelection.parseCellRange("A1:B2");

    private final static Optional<SpreadsheetCellRangePath> PATH = Optional.of(
            SpreadsheetCellRangePath.BULR
    );

    private final static OptionalInt OFFSET = OptionalInt.of(12);

    private final static OptionalInt MAX = OptionalInt.of(34);

    private final static Optional<String> VALUE_TYPE = Optional.of(SpreadsheetValueType.DATE);

    private final static Optional<Expression> QUERY = Optional.of(
            Expression.add(
                    Expression.value(56),
                    Expression.call(
                            Expression.namedFunction(
                                    FunctionExpressionName.with("function78")
                            ),
                            Lists.of(
                                    Expression.value(90),
                                    Expression.value(true)
                            )
                    )
            )
    );

    @Test
    public void testFindCellsWithNullIdFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetDeltaFetcher.findCellsUrl(
                        null,
                        CELLS,
                        PATH,
                        OFFSET,
                        MAX,
                        VALUE_TYPE,
                        QUERY
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
                        PATH,
                        OFFSET,
                        MAX,
                        VALUE_TYPE,
                        QUERY
                )
        );
    }

    @Test
    public void testFindCellsWithNullPathFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetDeltaFetcher.findCellsUrl(
                        ID,
                        CELLS,
                        null,
                        OFFSET,
                        MAX,
                        VALUE_TYPE,
                        QUERY
                )
        );
    }

    @Test
    public void testFindCellsWithNullOffsetFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetDeltaFetcher.findCellsUrl(
                        ID,
                        CELLS,
                        PATH,
                        null,
                        MAX,
                        VALUE_TYPE,
                        QUERY
                )
        );
    }

    @Test
    public void testFindCellsWithNullMaxFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetDeltaFetcher.findCellsUrl(
                        ID,
                        CELLS,
                        PATH,
                        OFFSET,
                        null,
                        VALUE_TYPE,
                        QUERY
                )
        );
    }

    @Test
    public void testFindCellsWithNullValueTypeFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetDeltaFetcher.findCellsUrl(
                        ID,
                        CELLS,
                        PATH,
                        OFFSET,
                        MAX,
                        null,
                        QUERY
                )
        );
    }

    @Test
    public void testFindCellsWithNullQueryFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetDeltaFetcher.findCellsUrl(
                        ID,
                        CELLS,
                        PATH,
                        OFFSET,
                        MAX,
                        VALUE_TYPE,
                        null
                )
        );
    }

    @Test
    public void testFindCellsPath() {
        this.findCellsUrlAndCheck(
                ID,
                CELLS,
                PATH,
                OptionalInt.empty(), // offset
                OptionalInt.empty(), // max
                Optional.empty(),
                Optional.empty(),
                Url.parseRelative("/api/spreadsheet/1234/cells/A1:B2/find?cell-range-path=bulr")
        );
    }

    @Test
    public void testFindCellsOffset() {
        this.findCellsUrlAndCheck(
                ID,
                CELLS,
                Optional.empty(),
                OptionalInt.of(123), // offset
                OptionalInt.empty(), // max
                Optional.empty(),
                Optional.empty(),
                Url.parseRelative("/api/spreadsheet/1234/cells/A1:B2/find?offset=123")
        );
    }

    @Test
    public void testFindCellsMax() {
        this.findCellsUrlAndCheck(
                ID,
                CELLS,
                Optional.empty(),
                OptionalInt.empty(), // offset
                OptionalInt.of(123), // max
                Optional.empty(),
                Optional.empty(),
                Url.parseRelative("/api/spreadsheet/1234/cells/A1:B2/find?max=123")
        );
    }

    @Test
    public void testFindCellsValueType() {
        this.findCellsUrlAndCheck(
                ID,
                CELLS,
                Optional.empty(),
                OptionalInt.empty(), // offset
                OptionalInt.empty(), // max
                Optional.of(SpreadsheetValueType.NUMBER),
                Optional.empty(),
                Url.parseRelative("/api/spreadsheet/1234/cells/A1:B2/find?value-type=number")
        );
    }

    @Test
    public void testFindCellsQuery() {
        this.findCellsUrlAndCheck(
                ID,
                CELLS,
                Optional.empty(), // path
                OptionalInt.empty(), // offset
                OptionalInt.empty(), // max
                Optional.empty(), // value-type
                QUERY,
                Url.parseRelative("/api/spreadsheet/1234/cells/A1:B2/find?query=56%2Bfunction78%2890%2Ctrue%29")
        );
    }

    @Test
    public void testFindCellsAllParameters() {
        this.findCellsUrlAndCheck(
                ID,
                CELLS,
                PATH,
                OFFSET,
                MAX,
                VALUE_TYPE,
                Optional.of(
                        Expression.add(
                                Expression.value(56),
                                Expression.value(78)
                        )
                ),
                Url.parseRelative("/api/spreadsheet/1234/cells/A1:B2/find?cell-range-path=bulr&max=34&offset=12&query=56%2B78&value-type=date")
        );
    }

    @Test
    public void testFindCellsAllParameters2() {
        this.findCellsUrlAndCheck(
                ID,
                CELLS,
                PATH,
                OFFSET,
                MAX,
                VALUE_TYPE,
                QUERY,
                Url.parseRelative("/api/spreadsheet/1234/cells/A1:B2/find?cell-range-path=bulr&max=34&offset=12&query=56%2Bfunction78%2890%2Ctrue%29&value-type=date")
        );
    }

    private void findCellsUrlAndCheck(final SpreadsheetId id,
                                      final SpreadsheetCellRange cells,
                                      final Optional<SpreadsheetCellRangePath> path,
                                      final OptionalInt offset,
                                      final OptionalInt max,
                                      final Optional<String> valueType,
                                      final Optional<Expression> query,
                                      final RelativeUrl expected) {
        this.checkEquals(
                expected,
                SpreadsheetDeltaFetcher.findCellsUrl(
                        id,
                        cells,
                        path,
                        offset,
                        max,
                        valueType,
                        query
                ),
                () -> "findCellsUrl " + id + " " + cells + " path=" + path + " offset=" + offset + " max=" + max + " valueType=" + valueType + " query=" + query
        );
    }

    // url..............................................................................................................

    @Test
    public void testUrlWithNullIdFails() {
        this.urlFails(
                null,
                SpreadsheetSelection.ALL_CELLS,
                Optional.empty()
        );
    }

    @Test
    public void testUrlWithNullSelectionFails() {
        this.urlFails(
                SpreadsheetId.with(1),
                null,
                Optional.empty()
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
                          final Optional<UrlPath> path) {
        final SpreadsheetDeltaFetcher fetcher = SpreadsheetDeltaFetcher.with(
                new FakeSpreadsheetDeltaFetcherWatcher(),
                new FakeAppContext() {
                    public SpreadsheetMetadataFetcher spreadsheetMetadataFetcher() {
                        return SpreadsheetMetadataFetcher.with(
                                new FakeSpreadsheetMetadataFetcherWatcher(),
                                new FakeAppContext()
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
                Optional.empty(),
                "/api/spreadsheet/1/cell/A1"
        );
    }

    @Test
    public void testUrlExtraPath() {
        this.urlAndCheck(
                2,
                "B2",
                Optional.of(UrlPath.parse("clear")),
                "/api/spreadsheet/2/cell/B2/clear"
        );
    }

    private void urlAndCheck(final long id,
                             final String cell,
                             final Optional<UrlPath> path,
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
                             final Optional<UrlPath> path,
                             final RelativeUrl url) {
        final SpreadsheetDeltaFetcher fetcher = SpreadsheetDeltaFetcher.with(
                new FakeSpreadsheetDeltaFetcherWatcher(),
                new FakeAppContext() {
                    @Override
                    public SpreadsheetMetadataFetcher spreadsheetMetadataFetcher() {
                        return SpreadsheetMetadataFetcher.with(
                                new FakeSpreadsheetMetadataFetcherWatcher(),
                                new FakeAppContext()
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
