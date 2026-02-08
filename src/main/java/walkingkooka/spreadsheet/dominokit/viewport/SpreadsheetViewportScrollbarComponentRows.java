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
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

final class SpreadsheetViewportScrollbarComponentRows extends SpreadsheetViewportScrollbarComponent<SpreadsheetRowReference> {

    static SpreadsheetViewportScrollbarComponentRows with(final SpreadsheetViewportScrollbarComponentContext context) {
        return new SpreadsheetViewportScrollbarComponentRows(context);
    }

    private SpreadsheetViewportScrollbarComponentRows(final SpreadsheetViewportScrollbarComponentContext context) {
        super(
            FlexLayoutComponent.column(),
            context
        );

        this.before.setIconBefore(
            Optional.of(
                SpreadsheetIcons.arrowUp()
            )
        );

        this.after.setIconAfter(
            Optional.of(
                SpreadsheetIcons.arrowDown()
            )
        );
    }

    @Override
    SliderComponent createSlider() {
        return SliderComponent.vertical(
            SpreadsheetSelection.MIN_ROW,
            SpreadsheetSelection.MAX_ROW
        );
    }

    @Override
    public SpreadsheetColumnOrRowReferenceKind referenceKind() {
        return SpreadsheetColumnOrRowReferenceKind.ROW;
    }
}
