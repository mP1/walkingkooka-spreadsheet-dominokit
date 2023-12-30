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

package walkingkooka.spreadsheet.dominokit.ui.textbox;

import org.dominokit.domino.ui.forms.validations.ValidationResult;
import org.dominokit.domino.ui.utils.HasValidation.Validator;
import walkingkooka.text.CharSequences;

import java.util.Objects;

/**
 * A {@link Validator} that only sends non empty {@link String} to a wrapped {@link Validator}.
 * This has the effect of making string values optional.
 */
final class SpreadsheetTextBoxOptionalValidator implements Validator<String> {

    /**
     * Factory
     */
    static SpreadsheetTextBoxOptionalValidator with(final Validator<String> validator) {
        Objects.requireNonNull(validator, "validator");

        return new SpreadsheetTextBoxOptionalValidator(validator);
    }

    private SpreadsheetTextBoxOptionalValidator(final Validator<String> validator) {
        super();
        this.validator = validator;
    }

    @Override
    public ValidationResult isValid(final String value) {
        return CharSequences.isNullOrEmpty(value) ?
                ValidationResult.valid() :
                this.validator.isValid(value);
    }

    private final Validator<String> validator;

    @Override
    public String toString() {
        return this.validator.toString();
    }
}
