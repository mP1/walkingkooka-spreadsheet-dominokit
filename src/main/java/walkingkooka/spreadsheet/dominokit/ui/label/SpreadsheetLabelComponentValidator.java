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

package walkingkooka.spreadsheet.dominokit.ui.label;

import org.dominokit.domino.ui.elements.SpanElement;
import org.dominokit.domino.ui.forms.suggest.SuggestBox;
import org.dominokit.domino.ui.forms.suggest.SuggestOption;
import org.dominokit.domino.ui.forms.validations.ValidationResult;
import org.dominokit.domino.ui.utils.HasValidation.Validator;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.CharSequences;

/**
 * Reads the text of the input for the {@link SuggestBox} and attempts to parse the text. Any caught {@link Exception#getMessage()} becomes the validation failure message.
 */
final class SpreadsheetLabelComponentValidator implements Validator<SuggestBox<String, SpanElement, SuggestOption<String>>> {

    /**
     * Factory
     */
    static SpreadsheetLabelComponentValidator with(final SpreadsheetLabelComponent component) {
        return new SpreadsheetLabelComponentValidator(component);
    }

    private SpreadsheetLabelComponentValidator(final SpreadsheetLabelComponent component) {
        super();
        this.component = component;
    }

    @Override
    public ValidationResult isValid(final SuggestBox<String, SpanElement, SuggestOption<String>> component) {
        ValidationResult result;

        final String text = component.getInputElement()
                .element()
                .value;

        if (CharSequences.isNullOrEmpty(text) && false == this.component.required) {
            result = ValidationResult.valid();
        } else {
            try {
                SpreadsheetSelection.labelName(text);
                result = ValidationResult.valid();
            } catch (final Exception fail) {
                result = ValidationResult.invalid(fail.getMessage());
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return this.component.toString();
    }

    private final SpreadsheetLabelComponent component;
}
