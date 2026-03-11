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
import walkingkooka.validation.form.FormField;

/**
 * A text box that accepts entry and validates it as a {@link FormField#setLabel(String)}.
 */
public final class FormFieldLabelComponent implements ValueTextBoxComponentDelegator<FormFieldLabelComponent, String> {

    public static FormFieldLabelComponent empty() {
        return new FormFieldLabelComponent();
    }

    private FormFieldLabelComponent() {
        this.textBox = ValueTextBoxComponent.with(
            FormFieldLabelComponent::formFieldSetLabel,
            String::toString
        );
    }

    private static String formFieldSetLabel(final String label) {
        FormField.with(SpreadsheetSelection.A1)
            .setLabel(label);
        return label;
    }

    // ValueTextBoxComponentDelegator..................................................................................

    @Override
    public ValueTextBoxComponent<String> valueTextBoxComponent() {
        return this.textBox;
    }

    private final ValueTextBoxComponent<String> textBox;

    // Object...........................................................................................................
    @Override
    public String toString() {
        return this.textBox.toString();
    }
}
