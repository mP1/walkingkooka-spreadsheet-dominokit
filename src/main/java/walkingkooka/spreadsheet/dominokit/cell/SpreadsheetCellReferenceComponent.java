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

package walkingkooka.spreadsheet.dominokit.cell;

import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentDelegator;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.HasText;

/**
 * A text box that accepts entry and validates it as a {@link SpreadsheetCellReference}.
 */
public final class SpreadsheetCellReferenceComponent implements ValueTextBoxComponentDelegator<SpreadsheetCellReferenceComponent, SpreadsheetCellReference> {

    public static SpreadsheetCellReferenceComponent with(final String id) {
        return new SpreadsheetCellReferenceComponent(id);
    }

    private SpreadsheetCellReferenceComponent(final String id) {
        this.textBox = ValueTextBoxComponent.with(
            SpreadsheetSelection::parseCell,
            HasText::text
        );
        this.setId(id);
    }

    // ValueTextBoxComponentDelegator..................................................................................

    @Override
    public ValueTextBoxComponent<SpreadsheetCellReference> valueTextBoxComponent() {
        return this.textBox;
    }

    private final ValueTextBoxComponent<SpreadsheetCellReference> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}