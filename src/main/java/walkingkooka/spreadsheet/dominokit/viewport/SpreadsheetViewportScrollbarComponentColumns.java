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

import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.flex.FlexLayoutComponent;
import walkingkooka.spreadsheet.dominokit.slider.SliderComponent;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReferenceKind;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

final class SpreadsheetViewportScrollbarComponentColumns extends SpreadsheetViewportScrollbarComponent<SpreadsheetColumnReference> {

    static SpreadsheetViewportScrollbarComponentColumns with(final SpreadsheetViewportScrollbarComponentContext context) {
        return new SpreadsheetViewportScrollbarComponentColumns(context);
    }

    private SpreadsheetViewportScrollbarComponentColumns(final SpreadsheetViewportScrollbarComponentContext context) {
        super(
            FlexLayoutComponent.row(),
            context
        );

        this.before.setIconBefore(
            Optional.of(
                SpreadsheetIcons.arrowLeft()
            )
        );

        this.after.setIconAfter(
            Optional.of(
                SpreadsheetIcons.arrowRight()
            )
        );
    }

    @Override
    SliderComponent createSlider() {
        return SliderComponent.horizontal(
            SpreadsheetSelection.MIN_COLUMN,
            SpreadsheetSelection.MAX_COLUMN
        );
    }

    @Override
    public SpreadsheetColumnOrRowReferenceKind referenceKind() {
        return SpreadsheetColumnOrRowReferenceKind.COLUMN;
    }
}
