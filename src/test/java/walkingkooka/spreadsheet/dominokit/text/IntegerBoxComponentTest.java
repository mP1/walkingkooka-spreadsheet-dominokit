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

package walkingkooka.spreadsheet.dominokit.text;

import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.forms.validations.ValidationResult;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.validator.FakeValidator;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;

import java.util.Optional;

public final class IntegerBoxComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, Integer, IntegerBoxComponent> {

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            IntegerBoxComponent.empty()
                .setId("id123")
                .setValue(
                    Optional.of(
                        123
                    )
                ),
            "IntegerBoxComponent\n" +
                "  [123] id=id123\n"
        );
    }

    @Test
    public void testSetValueValidatorPass() {
        this.treePrintAndCheck(
            IntegerBoxComponent.empty()
                .setId("id123")
                .setValidator(
                    Optional.of(
                        new FakeValidator<Optional<Integer>>() {
                            @Override
                            public ValidationResult isValid(final Optional<Integer> component) {
                                return ValidationResult.valid();
                            }
                        }
                    )
                )
                .setValue(
                    Optional.of(
                        123
                    )
                ),
            "IntegerBoxComponent\n" +
                "  [123] id=id123\n"
        );
    }

    @Test
    public void testSetValueValidatorInvalid() {
        this.treePrintAndCheck(
            IntegerBoxComponent.empty()
                .setId("id123")
                .setValidator(
                    Optional.of(
                        new FakeValidator<Optional<Integer>>() {
                            @Override
                            public ValidationResult isValid(final Optional<Integer> component) {
                                return ValidationResult.invalid("Invalid value 123");
                            }
                        }
                    )
                )
                .setValue(
                    Optional.of(
                        123
                    )
                ),
            "IntegerBoxComponent\n" +
                "  [123] id=id123\n" +
                "  Errors\n" +
                "    Invalid value 123\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public IntegerBoxComponent createComponent() {
        return IntegerBoxComponent.empty();
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<IntegerBoxComponent> type() {
        return IntegerBoxComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
