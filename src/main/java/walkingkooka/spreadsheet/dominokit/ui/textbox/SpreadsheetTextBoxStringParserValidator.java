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

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * A {@link Validator} that invokes {@link Consumer#accept(Object)}, catching any exceptions and using that as the fail message.
 */
final class SpreadsheetTextBoxStringParserValidator implements Validator<Optional<String>> {

    /**
     * Factory
     */
    static SpreadsheetTextBoxStringParserValidator with(final Consumer<String> parser) {
        Objects.requireNonNull(parser, "parser");

        return new SpreadsheetTextBoxStringParserValidator(parser);
    }

    private SpreadsheetTextBoxStringParserValidator(final Consumer<String> parser) {
        super();
        this.parser = parser;
    }

    @Override
    public ValidationResult isValid(final Optional<String> value) {
        ValidationResult result;

        try {
            this.parser.accept(
                    value.orElse("")
            );
            result = ValidationResult.valid();
        } catch (final Exception fail) {
            result = ValidationResult.invalid(fail.getMessage());
        }
        return result;
    }

    private final Consumer<String> parser;

    @Override
    public String toString() {
        return this.parser.toString();
    }
}
