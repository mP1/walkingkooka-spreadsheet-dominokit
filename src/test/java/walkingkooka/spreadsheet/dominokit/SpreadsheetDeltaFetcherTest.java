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
import walkingkooka.net.UrlQueryString;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.test.Testing;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetDeltaFetcherTest implements Testing {

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
}
