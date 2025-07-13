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

import java.util.Optional;

/**
 * A {@link Validator} that fails if the text is empty or missing.
 */
final class SpreadsheetRequiredStringValidator<T> implements Validator<Optional<T>> {

    static <T> SpreadsheetRequiredStringValidator<T> instance() {
        return INSTANCE;
    }

    /**
     * Singleton
     */
    private final static SpreadsheetRequiredStringValidator INSTANCE = new SpreadsheetRequiredStringValidator();

    private SpreadsheetRequiredStringValidator() {
        super();
    }

    @Override
    public ValidationResult isValid(final Optional<T> value) {
        return false == value.isPresent() ?
            ValidationResult.invalid("Required") :
            ValidationResult.valid();
    }

    @Override
    public String toString() {
        return "Required";
    }
}
