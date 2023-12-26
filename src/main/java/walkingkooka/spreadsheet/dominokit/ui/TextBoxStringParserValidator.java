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

package walkingkooka.spreadsheet.dominokit.ui;

import org.dominokit.domino.ui.forms.TextBox;
import org.dominokit.domino.ui.forms.validations.ValidationResult;
import org.dominokit.domino.ui.utils.HasValidation.Validator;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * A {@link Validator} that invokes {@link Consumer#accept(Object)}, catching any exceptions and using that as the fail message.
 */
public class TextBoxStringParserValidator implements Validator<TextBox> {

    /**
     * Factory
     */
    static TextBoxStringParserValidator with(final Consumer<String> parser) {
        Objects.requireNonNull(parser, "parser");

        return new TextBoxStringParserValidator(parser);
    }

    private TextBoxStringParserValidator(final Consumer<String> parser) {
        super();
        this.parser = parser;
    }

    @Override
    public ValidationResult isValid(final TextBox component) {
        ValidationResult result;

        try {
            this.parser.accept(
                    component.getValue()
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
