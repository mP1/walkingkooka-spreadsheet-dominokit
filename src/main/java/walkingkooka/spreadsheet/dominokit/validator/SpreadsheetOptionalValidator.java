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

package walkingkooka.spreadsheet.dominokit.validator;

import org.dominokit.domino.ui.forms.validations.ValidationResult;
import org.dominokit.domino.ui.utils.HasValidation.Validator;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link Validator} that skips the wrapped {@link Validator} if the value is missing.
 */
final class SpreadsheetOptionalValidator<T> implements Validator<Optional<T>> {

    /**
     * Factory
     */
    static <T> SpreadsheetOptionalValidator<T> with(final Validator<Optional<T>> validator) {
        Objects.requireNonNull(validator, "validator");

        return new SpreadsheetOptionalValidator<>(validator);
    }

    private SpreadsheetOptionalValidator(final Validator<Optional<T>> validator) {
        super();
        this.validator = validator;
    }

    @Override
    public ValidationResult isValid(final Optional<T> value) {
        return false == value.isPresent() ?
            ValidationResult.valid() :
            this.validator.isValid(value);
    }

    private final Validator<Optional<T>> validator;

    @Override
    public String toString() {
        return "Optional " + this.validator.toString();
    }
}
