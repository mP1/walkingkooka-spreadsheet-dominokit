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
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;
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
                () -> SpreadsheetDeltaFetcher.appendViewportSelection(
                        null,
                        UrlQueryString.EMPTY
                )
        );
    }

    @Test
    public void testAppendSelectionWithNullQueryStringFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetDeltaFetcher.appendViewportSelection(
                        SpreadsheetSelection.ALL_CELLS.setDefaultAnchor(),
                        null
                )
        );
    }

    @Test
    public void testAppendSelectionCell() {
        this.appendSelectionAndCheck(
                SpreadsheetSelection.parseCell("B2"),
                SpreadsheetViewportSelectionAnchor.NONE,
                "selection=B2&selectionType=cell"
        );
    }

    @Test
    public void testAppendSelectionCellRange() {
        this.appendSelectionAndCheck(
                SpreadsheetSelection.parseCellRange("B2:B3"),
                SpreadsheetViewportSelectionAnchor.TOP_RIGHT,
                "selection=B2:B3&selectionType=cell-range&selectionAnchor=top-right"
        );
    }

    @Test
    public void testAppendSelectionColumn() {
        this.appendSelectionAndCheck(
                SpreadsheetSelection.parseColumn("B"),
                SpreadsheetViewportSelectionAnchor.NONE,
                "selection=B&selectionType=column"
        );
    }

    @Test
    public void testAppendSelectionColumnRange() {
        this.appendSelectionAndCheck(
                SpreadsheetSelection.parseColumnRange("B:C"),
                SpreadsheetViewportSelectionAnchor.LEFT,
                "selection=B:C&selectionType=column-range&selectionAnchor=left"
        );
    }

    @Test
    public void testAppendSelectionRow() {
        this.appendSelectionAndCheck(
                SpreadsheetSelection.parseRow("2"),
                SpreadsheetViewportSelectionAnchor.NONE,
                "selection=2&selectionType=row"
        );
    }

    @Test
    public void testAppendSelectionRowRange() {
        this.appendSelectionAndCheck(
                SpreadsheetSelection.parseRowRange("2:3"),
                SpreadsheetViewportSelectionAnchor.TOP,
                "selection=2:3&selectionType=row-range&selectionAnchor=top"
        );
    }

    @Test
    public void testAppendSelectionLabel() {
        this.appendSelectionAndCheck(
                SpreadsheetSelection.labelName("Label123"),
                SpreadsheetViewportSelectionAnchor.NONE,
                "selection=Label123&selectionType=label"
        );
    }

    @Test
    public void testAppendSelectionLabel2() {
        this.appendSelectionAndCheck(
                SpreadsheetSelection.labelName("Label123"),
                SpreadsheetViewportSelectionAnchor.NONE,
                UrlQueryString.parse("a=1"),
                UrlQueryString.parse("a=1&selection=Label123&selectionType=label")
        );
    }

    private void appendSelectionAndCheck(final SpreadsheetSelection selection,
                                         final SpreadsheetViewportSelectionAnchor anchor,
                                         final String expected) {
        this.appendSelectionAndCheck(
                selection,
                anchor,
                UrlQueryString.parse(expected)
        );
    }

    private void appendSelectionAndCheck(final SpreadsheetSelection selection,
                                         final SpreadsheetViewportSelectionAnchor anchor,
                                         final UrlQueryString expected) {
        this.appendSelectionAndCheck(
                selection,
                anchor,
                UrlQueryString.EMPTY,
                expected
        );
    }

    private void appendSelectionAndCheck(final SpreadsheetSelection selection,
                                         final SpreadsheetViewportSelectionAnchor anchor,
                                         final UrlQueryString initial,
                                         final UrlQueryString expected) {
        this.checkEquals(
                expected,
                SpreadsheetDeltaFetcher.appendViewportSelection(
                        selection.setAnchor(anchor),
                        initial
                ),
                () -> initial + " appendSelection " + selection + " " + anchor
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
                        SpreadsheetSelection.parseWindow(""),
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
                SpreadsheetSelection.parseWindow(window),
                UrlQueryString.parse(initial),
                UrlQueryString.parse(expected)
        );
    }

    private void appendWindowAndCheck(final Set<SpreadsheetCellRange> window,
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

    // appendSelectionAndWindow.........................................................................................

    @Test
    public void testAppendSelectionAndWindowCell() {
        this.appendSelectionAndWindow(
                SpreadsheetSelection.parseCell("B2"),
                SpreadsheetViewportSelectionAnchor.NONE,
                "A1:C3",
                "selection=B2&selectionType=cell&window=A1:C3"
        );
    }

    @Test
    public void testAppendSelectionAndWindowCellRange() {
        this.appendSelectionAndWindow(
                SpreadsheetSelection.parseCellRange("B2:B3"),
                SpreadsheetViewportSelectionAnchor.TOP_LEFT,
                "A1:C3",
                "selection=B2:B3&selectionType=cell-range&selectionAnchor=top-left&window=A1:C3"
        );
    }

    @Test
    public void testAppendSelectionAndWindowColumn() {
        this.appendSelectionAndWindow(
                SpreadsheetSelection.parseColumn("B"),
                SpreadsheetViewportSelectionAnchor.NONE,
                "A1:C3",
                "selection=B&selectionType=column&window=A1:C3"
        );
    }

    @Test
    public void testAppendSelectionAndWindowColumnRange() {
        this.appendSelectionAndWindow(
                SpreadsheetSelection.parseColumnRange("B:C"),
                SpreadsheetViewportSelectionAnchor.LEFT,
                "A1:C3",
                "selection=B:C&selectionType=column-range&selectionAnchor=left&window=A1:C3"
        );
    }

    @Test
    public void testAppendSelectionAndWindowRow() {
        this.appendSelectionAndWindow(
                SpreadsheetSelection.parseRow("2"),
                SpreadsheetViewportSelectionAnchor.NONE,
                "A1:C3",
                "selection=2&selectionType=row&window=A1:C3"
        );
    }

    @Test
    public void testAppendSelectionAndWindowRowRange() {
        this.appendSelectionAndWindow(
                SpreadsheetSelection.parseRowRange("2:3"),
                SpreadsheetViewportSelectionAnchor.TOP,
                "A1:C3",
                "selection=2:3&selectionType=row-range&selectionAnchor=top&window=A1:C3"
        );
    }

    @Test
    public void testAppendSelectionAndWindowLabel() {
        this.appendSelectionAndWindow(
                SpreadsheetSelection.labelName("Label123"),
                SpreadsheetViewportSelectionAnchor.NONE,
                "A1:C3",
                "selection=Label123&selectionType=label&window=A1:C3"
        );
    }

    @Test
    public void testAppendSelectionAndWindowLabel2() {
        this.appendSelectionAndWindow(
                SpreadsheetSelection.labelName("Label123"),
                SpreadsheetViewportSelectionAnchor.NONE,
                "A1:C3",
                "a1",
                "a1&selection=Label123&selectionType=label&window=A1:C3"
        );
    }

    private void appendSelectionAndWindow(final SpreadsheetSelection selection,
                                          final SpreadsheetViewportSelectionAnchor anchor,
                                          final String window,
                                          final String expected) {
        this.appendSelectionAndWindow(
                selection,
                anchor,
                window,
                "",
                expected
        );
    }

    private void appendSelectionAndWindow(final SpreadsheetSelection selection,
                                          final SpreadsheetViewportSelectionAnchor anchor,
                                          final String window,
                                          final String initial,
                                          final String expected) {
        this.appendSelectionAndWindow(
                selection,
                anchor,
                SpreadsheetSelection.parseWindow(window),
                UrlQueryString.parse(initial),
                UrlQueryString.parse(expected)
        );
    }

    private void appendSelectionAndWindow(final SpreadsheetSelection selection,
                                          final SpreadsheetViewportSelectionAnchor anchor,
                                          final Set<SpreadsheetCellRange> window,
                                          final UrlQueryString initial,
                                          final UrlQueryString expected) {
        this.checkEquals(
                expected,
                SpreadsheetDeltaFetcher.appendViewportSelectionAndWindow(
                        selection.setAnchor(anchor),
                        window,
                        initial
                ),
                () -> initial + " appendSelectionAndWindow " + selection + " " + anchor + " " + window
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
