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

package walkingkooka.spreadsheet.dominokit.ui.formula;

import org.dominokit.domino.ui.forms.validations.ValidationResult;
import org.dominokit.domino.ui.utils.HasValidation.Validator;
import walkingkooka.spreadsheet.SpreadsheetError;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.function.Function;

/**
 * A {@link Validator} that uses the function to parse new text. If the parser throws an exception or returns a {@link SpreadsheetFormula#error()},
 * the message will be used as the validation text.
 */
final class SpreadsheetFormulaComponentValidator implements Validator<String> {

    /**
     * Factory
     */
    static SpreadsheetFormulaComponentValidator with(final Function<String, SpreadsheetFormula> parser) {
        Objects.requireNonNull(parser, "parser");

        return new SpreadsheetFormulaComponentValidator(parser);
    }

    private SpreadsheetFormulaComponentValidator(final Function<String, SpreadsheetFormula> parser) {
        super();
        this.parser = parser;
    }

    @Override
    public ValidationResult isValid(final String value) {
        String message;

        try {
            final SpreadsheetFormula formula = this.parser.apply(value);
            message = formula.error()
                    .map(SpreadsheetError::message)
                    .orElse(
                            null
                    );
        } catch (final Exception fail) {
            message = fail.getMessage();
        }
        return CharSequences.isNullOrEmpty(message) ?
                ValidationResult.valid() :
                ValidationResult.invalid(message);
    }

    private final Function<String, SpreadsheetFormula> parser;

    @Override
    public String toString() {
        return this.parser.toString();
    }
}
