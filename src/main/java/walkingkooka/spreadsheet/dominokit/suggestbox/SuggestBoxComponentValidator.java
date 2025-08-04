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

package walkingkooka.spreadsheet.dominokit.suggestbox;

import org.dominokit.domino.ui.elements.SpanElement;
import org.dominokit.domino.ui.forms.suggest.SuggestBox;
import org.dominokit.domino.ui.forms.suggest.SuggestOption;
import org.dominokit.domino.ui.forms.validations.ValidationResult;
import org.dominokit.domino.ui.utils.HasValidation.Validator;

import java.util.Optional;

/**
 * Reads the text of the input for the {@link SuggestBox} and attempts to validate the text, using the provided Validator.
 * Any caught {@link Exception#getMessage()} becomes the validation failure message.
 */
final class SuggestBoxComponentValidator<T> implements Validator<SuggestBox<T, SpanElement, SuggestOption<T>>> {

    /**
     * Factory
     */
    static <T> SuggestBoxComponentValidator<T> with(final Validator<Optional<T>> validator) {
        return new SuggestBoxComponentValidator<>(validator);
    }

    private SuggestBoxComponentValidator(final Validator<Optional<T>> validator) {
        super();
        this.validator = validator;
    }

    @Override
    public ValidationResult isValid(final SuggestBox<T, SpanElement, SuggestOption<T>> component) {
        return this.validator.isValid(
            Optional.ofNullable(
                component.isEmpty() ?
                null :
                    component.getValue()
            )
        );
    }

    @Override
    public String toString() {
        return this.validator.toString();
    }

    private final Validator<Optional<T>> validator;
}
