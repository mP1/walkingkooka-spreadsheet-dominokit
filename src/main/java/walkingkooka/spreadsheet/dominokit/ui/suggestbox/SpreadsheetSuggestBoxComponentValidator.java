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

package walkingkooka.spreadsheet.dominokit.ui.suggestbox;

import org.dominokit.domino.ui.elements.SpanElement;
import org.dominokit.domino.ui.forms.suggest.SuggestBox;
import org.dominokit.domino.ui.forms.suggest.SuggestOption;
import org.dominokit.domino.ui.forms.validations.ValidationResult;
import org.dominokit.domino.ui.utils.HasValidation.Validator;
import walkingkooka.text.CharSequences;

import java.util.Optional;

/**
 * Reads the text of the input for the {@link SuggestBox} and attempts to parse the text. Any caught {@link Exception#getMessage()} becomes the validation failure message.
 */
final class SpreadsheetSuggestBoxComponentValidator implements Validator<SuggestBox<String, SpanElement, SuggestOption<String>>> {

    /**
     * Factory
     */
    static SpreadsheetSuggestBoxComponentValidator with(final Validator<Optional<String>> validator) {
        return new SpreadsheetSuggestBoxComponentValidator(validator);
    }

    private SpreadsheetSuggestBoxComponentValidator(final Validator<Optional<String>> validator) {
        super();
        this.validator = validator;
    }

    @Override
    public ValidationResult isValid(final SuggestBox<String, SpanElement, SuggestOption<String>> component) {
        final String text = component.getInputElement()
                .element()
                .value;

        return this.validator.isValid(
                Optional.ofNullable(
                        CharSequences.isNullOrEmpty(text) ?
                                null :
                                text
                )
        );
    }

    @Override
    public String toString() {
        return this.validator.toString();
    }

    private final Validator<Optional<String>> validator;
}
