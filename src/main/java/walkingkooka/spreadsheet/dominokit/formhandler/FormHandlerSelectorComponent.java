
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

package walkingkooka.spreadsheet.dominokit.formhandler;

import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBox;
import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBoxWrapper;
import walkingkooka.text.HasText;
import walkingkooka.validation.form.provider.FormHandlerSelector;

/**
 * A text box that accepts entry and validates it as a {@link FormHandlerSelector}.
 */
public final class FormHandlerSelectorComponent implements ValueSpreadsheetTextBoxWrapper<FormHandlerSelectorComponent, FormHandlerSelector> {

    public static FormHandlerSelectorComponent empty() {
        return new FormHandlerSelectorComponent();
    }

    private FormHandlerSelectorComponent() {
        this.textBox = ValueSpreadsheetTextBox.with(
            FormHandlerSelector::parse,
            HasText::text
        );
    }

    // ValueSpreadsheetTextBoxWrapper..................................................................................

    @Override
    public ValueSpreadsheetTextBox<FormHandlerSelector> valueSpreadsheetTextBox() {
        return this.textBox;
    }

    private final ValueSpreadsheetTextBox<FormHandlerSelector> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}