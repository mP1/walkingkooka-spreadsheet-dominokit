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
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.UrlPath;
import walkingkooka.net.UrlQueryString;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.test.Testing;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetDeltaFetcherTest implements Testing {

    // appendSelection..................................................................................................

    @Test
    public void testAppendSelectionWithNullSelectionFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetDeltaFetcher.appendSelection(
                        null,
                        UrlQueryString.EMPTY
                )
        );
    }

    @Test
    public void testAppendSelectionWithNullQueryStringFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetDeltaFetcher.appendSelection(
                        SpreadsheetSelection.ALL_CELLS,
                        null
                )
        );
    }

    @Test
    public void testAppendSelectionCell() {
        this.appendSelectionAndCheck(
                SpreadsheetSelection.parseCell("B2"),
                "selection=B2&selectionType=cell"
        );
    }

    @Test
    public void testAppendSelectionCellRange() {
        this.appendSelectionAndCheck(
                SpreadsheetSelection.parseCellRange("B2:B3"),
                "selection=B2:B3&selectionType=cell-range"
        );
    }

    @Test
    public void testAppendSelectionColumn() {
        this.appendSelectionAndCheck(
                SpreadsheetSelection.parseColumn("B"),
                "selection=B&selectionType=column"
        );
    }

    @Test
    public void testAppendSelectionColumnRange() {
        this.appendSelectionAndCheck(
                SpreadsheetSelection.parseColumnRange("B:C"),
                "selection=B:C&selectionType=column-range"
        );
    }

    @Test
    public void testAppendSelectionRow() {
        this.appendSelectionAndCheck(
                SpreadsheetSelection.parseRow("2"),
                "selection=2&selectionType=row"
        );
    }

    @Test
    public void testAppendSelectionRowRange() {
        this.appendSelectionAndCheck(
                SpreadsheetSelection.parseRowRange("2:3"),
                "selection=2:3&selectionType=row-range"
        );
    }

    @Test
    public void testAppendSelectionLabel() {
        this.appendSelectionAndCheck(
                SpreadsheetSelection.labelName("Label123"),
                "selection=Label123&selectionType=label"
        );
    }

    @Test
    public void testAppendSelectionLabel2() {
        this.appendSelectionAndCheck(
                SpreadsheetSelection.labelName("Label123"),
                UrlQueryString.parse("a=1"),
                UrlQueryString.parse("a=1&selection=Label123&selectionType=label")
        );
    }

    private void appendSelectionAndCheck(final SpreadsheetSelection selection,
                                         final String expected) {
        this.appendSelectionAndCheck(
                selection,
                UrlQueryString.parse(expected)
        );
    }

    private void appendSelectionAndCheck(final SpreadsheetSelection selection,
                                         final UrlQueryString expected) {
        this.appendSelectionAndCheck(
                selection,
                UrlQueryString.EMPTY,
                expected
        );
    }

    private void appendSelectionAndCheck(final SpreadsheetSelection selection,
                                         final UrlQueryString initial,
                                         final UrlQueryString expected) {
        this.checkEquals(
                expected,
                SpreadsheetDeltaFetcher.appendSelection(
                        selection,
                        initial
                ),
                () -> "appendSelection " + selection + " " + initial
        );
    }

    // urlQueryString...................................................................................................
    
    @Test
    public void testUrlQueryWithNullSelectionFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetDeltaFetcher.urlQueryString(
                        null,
                        SpreadsheetDelta.NO_WINDOW
                )
        );
    }

    @Test
    public void testUrlQueryWithNullWindowFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetDeltaFetcher.urlQueryString(
                        SpreadsheetSelection.ALL_CELLS,
                        null
                )
        );
    }

    @Test
    public void testUrlQueryStringCell() {
        this.urlQueryStringAndCheck(
                SpreadsheetSelection.parseCell("B2"),
                "A1:C3",
                "selection=B2&selectionType=cell&window=A1:C3"
        );
    }

    @Test
    public void testUrlQueryStringCellRange() {
        this.urlQueryStringAndCheck(
                SpreadsheetSelection.parseCellRange("B2:B3"),
                "A1:C3",
                "selection=B2:B3&selectionType=cell-range&window=A1:C3"
        );
    }

    @Test
    public void testUrlQueryStringColumn() {
        this.urlQueryStringAndCheck(
                SpreadsheetSelection.parseColumn("B"),
                "A1:C3",
                "selection=B&selectionType=column&window=A1:C3"
        );
    }

    @Test
    public void testUrlQueryStringColumnRange() {
        this.urlQueryStringAndCheck(
                SpreadsheetSelection.parseColumnRange("B:C"),
                "A1:C3",
                "selection=B:C&selectionType=column-range&window=A1:C3"
        );
    }

    @Test
    public void testUrlQueryStringRow() {
        this.urlQueryStringAndCheck(
                SpreadsheetSelection.parseRow("2"),
                "A1:C3",
                "selection=2&selectionType=row&window=A1:C3"
        );
    }

    @Test
    public void testUrlQueryStringRowRange() {
        this.urlQueryStringAndCheck(
                SpreadsheetSelection.parseRowRange("2:3"),
                "A1:C3",
                "selection=2:3&selectionType=row-range&window=A1:C3"
        );
    }

    @Test
    public void testUrlQueryStringLabel() {
        this.urlQueryStringAndCheck(
                SpreadsheetSelection.labelName("Label123"),
                "A1:C3",
                "selection=Label123&selectionType=label&window=A1:C3"
        );
    }

    private void urlQueryStringAndCheck(final SpreadsheetSelection selection,
                                        final String window,
                                        final String expected) {
        this.urlQueryStringAndCheck(
                selection,
                SpreadsheetSelection.parseWindow(window),
                UrlQueryString.parse(expected)
        );
    }

    private void urlQueryStringAndCheck(final SpreadsheetSelection selection,
                                        final Set<SpreadsheetCellRange> window,
                                        final UrlQueryString expected) {
        this.checkEquals(
                expected,
                SpreadsheetDeltaFetcher.urlQueryString(
                        selection,
                        window
                ),
                () -> "urlQueryString " + selection + " " + window
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
                new FakeSpreadsheetDeltaWatcher(),
                new FakeAppContext() {
                    public SpreadsheetMetadataFetcher spreadsheetMetadataFetcher() {
                        return SpreadsheetMetadataFetcher.with(
                                new FakeSpreadsheetMetadataWatcher(),
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
                new FakeSpreadsheetDeltaWatcher(),
                new FakeAppContext() {
                    @Override
                    public SpreadsheetMetadataFetcher spreadsheetMetadataFetcher() {
                        return SpreadsheetMetadataFetcher.with(
                                new FakeSpreadsheetMetadataWatcher(),
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
