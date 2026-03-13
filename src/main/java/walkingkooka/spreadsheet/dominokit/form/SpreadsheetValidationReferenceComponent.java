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

package walkingkooka.spreadsheet.dominokit.form;

import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentDelegator;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.validation.SpreadsheetValidationReference;
import walkingkooka.text.HasText;

/**
 * A text box that accepts entry and validates entry of a {@link SpreadsheetValidationReference}.
 */
public final class SpreadsheetValidationReferenceComponent implements ValueTextBoxComponentDelegator<SpreadsheetValidationReferenceComponent, SpreadsheetValidationReference> {

    public static SpreadsheetValidationReferenceComponent empty() {
        return new SpreadsheetValidationReferenceComponent();
    }

    private SpreadsheetValidationReferenceComponent() {
        this.textBox = ValueTextBoxComponent.with(
            SpreadsheetSelection::parseValidationReference,
            HasText::text
        );
    }

    // ValueTextBoxComponentDelegator..................................................................................

    @Override
    public ValueTextBoxComponent<SpreadsheetValidationReference> valueTextBoxComponent() {
        return this.textBox;
    }

    private final ValueTextBoxComponent<SpreadsheetValidationReference> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}