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
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

public final class SpreadsheetViewportComponentThumbnailsTest implements ClassTesting<SpreadsheetViewportComponentThumbnails> {

    @Test
    public void testOnlyHomeNoCells() {
        this.computeAndCheck(
                "A1:J10",
                0, // columnCount
                0, //rowCount
                SpreadsheetViewportComponentThumbnails.with(
                        0, // left
                        1, // width
                        0, // top
                        1 // height
                )
        );
    }

    @Test
    public void testHomeSomeCells() {
        this.computeAndCheck(
                "A1:J10",
                20, // columnCount
                20, //rowCount
                SpreadsheetViewportComponentThumbnails.with(
                        0, // left
                        0.5f, // width
                        0, // top
                        0.5f // height
                )
        );
    }

    @Test
    public void testHomeSomeCells2() {
        this.computeAndCheck(
                "A1:J10",
                10, // columnCount
                20, //rowCount
                SpreadsheetViewportComponentThumbnails.with(
                        0, // left
                        1, // width
                        0, // top
                        0.5f // height
                )
        );
    }

    @Test
    public void testQuarterAcross() {
        this.computeAndCheck(
                "D1:I12",
                12, // columnCount
                12, //rowCount
                SpreadsheetViewportComponentThumbnails.with(
                        0.25f, // left
                        0.5f, // width
                        0, // top
                        1 // height
                )
        );
    }

    @Test
    public void testQuarterDown() {
        this.computeAndCheck(
                "A4:L9",
                12, // columnCount
                12, //rowCount
                SpreadsheetViewportComponentThumbnails.with(
                        0, // left
                        1, // width
                        0.25f, // top
                        0.5f // height
                )
        );
    }

    @Test
    public void testHalfAcross() {
        this.computeAndCheck(
                "g1:L12",
                12, // columnCount
                12, //rowCount
                SpreadsheetViewportComponentThumbnails.with(
                        0.5f, // left
                        0.5f, // width
                        0, // top
                        1 // height
                )
        );
    }

    @Test
    public void testHalfDown() {
        this.computeAndCheck(
                "A7:L12",
                12, // columnCount
                12, //rowCount
                SpreadsheetViewportComponentThumbnails.with(
                        0, // left
                        1, // width
                        0.5f, // top
                        0.5f // height
                )
        );
    }

    private void computeAndCheck(final String last,
                                 final int columnCount,
                                 final int rowCount,
                                 final SpreadsheetViewportComponentThumbnails expected) {
        this.computeAndCheck(
                SpreadsheetSelection.parseCellRange(last),
                columnCount,
                rowCount,
                expected
        );
    }

    private void computeAndCheck(final SpreadsheetCellRange last,
                                 final int columnCount,
                                 final int rowCount,
                                 final SpreadsheetViewportComponentThumbnails expected) {
        this.checkEquals(
                expected,
                SpreadsheetViewportComponentThumbnails.compute(
                        last,
                        columnCount,
                        rowCount
                )
        );
    }

    @Override
    public Class<SpreadsheetViewportComponentThumbnails> type() {
        return SpreadsheetViewportComponentThumbnails.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
