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

package walkingkooka.spreadsheet.dominokit.viewport;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.test.ParseStringTesting;

import java.util.Optional;

public final class SpreadsheetViewportComponentTest implements ClassTesting<SpreadsheetViewportComponent>, ParseStringTesting<Optional<SpreadsheetSelection>> {

    // id...............................................................................................................

    @Test
    public void testIdWithCell() {
        this.idAndCheck(
                SpreadsheetSelection.A1,
                "viewport-cell-A1"
        );
    }

    @Test
    public void testIdWithColumn() {
        this.idAndCheck(
                SpreadsheetSelection.parseColumn("B"),
                "viewport-column-B"
        );
    }

    @Test
    public void testIdWithRow() {
        this.idAndCheck(
                SpreadsheetSelection.parseRow("3"),
                "viewport-row-3"
        );
    }

    private void idAndCheck(final SpreadsheetSelection selection,
                            final String id) {
        this.checkEquals(
                id,
                SpreadsheetViewportComponent.id(selection),
                () -> selection + " id"
        );
    }

    // parseId.........................................................................................................

    @Override
    public void testParseStringEmptyFails() {
        throw new UnsupportedOperationException(); // shouldnt be invoked
    }

    @Test
    public void testParseIdWithEmpty() {
        this.parseStringAndCheck(
                ""
        );
    }

    @Test
    public void testParseIdWithMissingViewportIdPrefix() {
        this.parseStringAndCheck(
                "xyz"
        );
    }

    @Test
    public void testParseIdWithInvalidSelectionType() {
        this.parseStringAndCheck(
                "viewport-invalid"
        );
    }

    @Test
    public void testParseIdWithInvalidSelectionType2() {
        this.parseStringAndCheck(
                "viewport-invalid-A1"
        );
    }

    @Test
    public void testParseIdWithCellRangeFails() {
        this.parseStringAndCheck(
                "viewport-cell-A1:A2"
        );
    }

    @Test
    public void testParseIdWithColumnRangeFails() {
        this.parseStringAndCheck(
                "viewport-column-B:C"
        );
    }

    @Test
    public void testParseIdWithLabelFails() {
        this.parseStringAndCheck(
                "viewport-cell-Label123"
        );
    }

    @Test
    public void testParseIdWithRowRangeFails() {
        this.parseStringAndCheck(
                "viewport-row-4:5"
        );
    }

    private void parseStringAndCheck(final String id) {
        this.parseStringAndCheck(
                id,
                Optional.empty()
        );
    }

    @Test
    public void testParseIdWithCell() {
        this.parseStringAndCheck(
                "viewport-cell-A1",
                SpreadsheetSelection.A1
        );
    }

    @Test
    public void testParseIdWithColumn() {
        this.parseStringAndCheck(
                "viewport-column-B",
                SpreadsheetSelection.parseColumn("B")
        );
    }

    @Test
    public void testParseIdWithRow() {
        this.parseStringAndCheck(
                "viewport-row-3",
                SpreadsheetSelection.parseRow("3")
        );
    }

    private void parseStringAndCheck(final String id,
                                     final SpreadsheetSelection selection) {
        this.parseStringAndCheck(
                id,
                Optional.of(selection)
        );

        this.idAndCheck(
                selection,
                id
        );
    }

    // ParseStringTesting...............................................................................................

    @Override
    public Optional<SpreadsheetSelection> parseString(final String id) {
        return SpreadsheetViewportComponent.parseId(id);
    }

    @Override
    public Class<? extends RuntimeException> parseStringFailedExpected(final Class<? extends RuntimeException> thrown) {
        return thrown;
    }

    @Override
    public RuntimeException parseStringFailedExpected(final RuntimeException thrown) {
        return thrown;
    }

    // ClassTesting....................................................................................................

    @Override
    public Class<SpreadsheetViewportComponent> type() {
        return SpreadsheetViewportComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
