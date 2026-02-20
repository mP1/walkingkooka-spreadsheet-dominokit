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

package walkingkooka.spreadsheet.dominokit.tagbox;

import org.dominokit.domino.ui.forms.suggest.TagBox;
import org.dominokit.domino.ui.forms.validations.ValidationResult;
import org.dominokit.domino.ui.utils.HasValidation.Validator;

import java.util.List;
import java.util.Optional;

/**
 * Reads the text of the input for the {@link TagBox} and attempts to validate the text, using the provided Validator.
 * Any caught {@link Exception#getMessage()} becomes the validation failure message.
 */
final class TagBoxComponentValidator<T> implements Validator<TagBox<T>> {

    /**
     * Factory
     */
    static <T> TagBoxComponentValidator<T> with(final Validator<Optional<List<T>>> validator) {
        return new TagBoxComponentValidator<>(validator);
    }

    private TagBoxComponentValidator(final Validator<Optional<List<T>>> validator) {
        super();
        this.validator = validator;
    }

    // Validator........................................................................................................

    @Override
    public ValidationResult isValid(final TagBox<T> component) {
        return this.validator.isValid(
            Optional.ofNullable(
                component.isEmpty() ?
                    null :
                    component.getValue()
            )
        );
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.validator.toString();
    }

    private final Validator<Optional<List<T>>> validator;
}
