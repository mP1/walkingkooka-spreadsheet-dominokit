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

import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.forms.validations.ValidationResult;
import org.dominokit.domino.ui.utils.HasValidation.Validator;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.ui.viewport.ValueComponentTesting;

import java.util.Optional;

public final class SpreadsheetTextBoxTest implements ValueComponentTesting<HTMLFieldSetElement, String, SpreadsheetTextBox> {

    @Test
    public void testValidationPass() {
        this.treePrintAndCheck(
                SpreadsheetTextBox.empty()
                        .setLabel("Label123")
                        .setValidator(
                                new Validator<Optional<String>>() {
                                    @Override
                                    public ValidationResult isValid(final Optional<String> component) {
                                        return ValidationResult.valid();
                                    }
                                }
                        ).setValue(Optional.of("Value456")),
                "SpreadsheetTextBox\n" +
                        "  Label123 [Value456]\n"
        );
    }

    @Test
    public void testValidationFailure() {
        this.treePrintAndCheck(
                SpreadsheetTextBox.empty()
                        .setLabel("Label123")
                        .setValidator(
                                new Validator<Optional<String>>() {
                                    @Override
                                    public ValidationResult isValid(final Optional<String> component) {
                                        return ValidationResult.invalid("Error message 123");
                                    }
                                }
                        ).setValue(Optional.of("Value456")),
                "SpreadsheetTextBox\n" +
                        "  Label123 [Value456]\n" +
                        "  Errors\n" +
                        "    Error message 123\n"
        );
    }

    @Test
    public void testLabelAndValueWithoutValidator() {
        this.treePrintAndCheck(
                SpreadsheetTextBox.empty()
                        .setLabel("Label123")
                        .setValue(Optional.of("Value456")),
                "SpreadsheetTextBox\n" +
                        "  Label123 [Value456]\n"
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetTextBox> type() {
        return SpreadsheetTextBox.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
