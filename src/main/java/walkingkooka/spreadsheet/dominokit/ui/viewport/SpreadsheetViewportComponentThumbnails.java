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

package walkingkooka.spreadsheet.dominokit.ui.viewport;

import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;

import java.util.Objects;

/**
 * Given various details about the active scrollbar window and the spreadsheet dimensions compute the relative position and length of both scrollbar thumb controls.
 */
final class SpreadsheetViewportComponentThumbnails {

    static SpreadsheetViewportComponentThumbnails compute(final SpreadsheetCellRangeReference last,
                                                          final int columnCount,
                                                          final int rowCount) {
        final SpreadsheetCellReference topLeft = last.begin();

        final float left = topLeft.column()
                .value();
        final float top = topLeft.row()
                .value();

        final SpreadsheetCellReference bottomRight = last.end();
        final float width = bottomRight.column()
                .value() -
                left +
                1;
        final float right = Math.max(
                left + width,
                columnCount
        );

        final float height = bottomRight.row()
                .value() -
                top +
                1;
        final float bottom = Math.max(
                top + height,
                rowCount
        );

        final float horizontalMultiplier = 100 / right;
        final float verticalMultiplier = 100 / bottom;

        return with(
                left * horizontalMultiplier,// left
                width * horizontalMultiplier,// width
                top * verticalMultiplier,// top
                height * verticalMultiplier// height
        );
    }

    static SpreadsheetViewportComponentThumbnails with(final float left,
                                                       final float width,
                                                       final float top,
                                                       final float height) {
        return new SpreadsheetViewportComponentThumbnails(
                left,
                width,
                top,
                height
        );
    }

    private SpreadsheetViewportComponentThumbnails(final float left,
                                                   final float width,
                                                   final float top,
                                                   final float height) {
        this.left = left;
        this.width = width;
        this.top = top;
        this.height = height;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.left,
                this.width,
                this.top,
                this.height
        );
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof SpreadsheetViewportComponentThumbnails && this.equals0((SpreadsheetViewportComponentThumbnails) other);
    }

    private boolean equals0(final SpreadsheetViewportComponentThumbnails other) {
        return this.left == other.left &&
                this.width == other.width &&
                this.top == other.top &&
                this.height == other.height;
    }

    final float left;

    final float width;

    final float top;

    final float height;

    @Override
    public String toString() {
        return "left: " + this.left + " width: " + this.width + " top: " + this.top + " height: " + this.height;
    }
}
