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

package walkingkooka.spreadsheet.dominokit.value.text;

import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.forms.validations.ValidationResult;
import org.dominokit.domino.ui.utils.HasValidation.Validator;
import org.junit.jupiter.api.Test;
import walkingkooka.InvalidCharacterException;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.dominokit.value.validator.SpreadsheetValidators;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextBoxComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, String, TextBoxComponent>,
    ToStringTesting<TextBoxComponent> {

    // setId............................................................................................................

    @Test
    public void testSetIdWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createComponent()
                .setId(null)
        );
    }

    @Test
    public void testSetIdWithEmptyFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createComponent()
                .setId("")
        );
    }

    // setDisabled......................................................................................................

    @Test
    public void testSetDisabledFalse() {
        final TextBoxComponent box = this.createComponent();
        box.setDisabled(false);
        this.isDisabledAndCheck(
            box,
            false
        );
    }

    @Test
    public void testSetDisabledTrue() {
        final TextBoxComponent box = this.createComponent();
        box.setDisabled(true);
        this.isDisabledAndCheck(
            box,
            true
        );
    }

    // clearValue.......................................................................................................

    @Test
    public void testClearValue() {
        final TextBoxComponent box = this.createComponent()
            .setValue(
                Optional.of("123")
            ).clearValue();
        this.valueAndCheck(
            box,
            Optional.empty()
        );
    }

    // setValue.........................................................................................................

    @Test
    public void testSetValueWithEmpty() {
        final Optional<String> value = Optional.of("");

        final TextBoxComponent box = this.createComponent()
            .setValue(value);
        this.valueAndCheck(
            box,
            Optional.empty()
        );
        this.treePrintAndCheck(
            box,
            "TextBoxComponent\n" +
                "  [] REQUIRED\n"
        );
    }

    @Test
    public void testSetValueWithEmptyRequired() {
        final Optional<String> value = Optional.of("");

        final TextBoxComponent box = this.createComponent()
            .required()
            .setValue(value);
        this.valueAndCheck(
            box,
            Optional.empty()
        );
        this.treePrintAndCheck(
            box,
            "TextBoxComponent\n" +
                "  [] REQUIRED\n"
        );
    }

    @Test
    public void testSetValueWithEmptyRequired2() {
        final Optional<String> value = Optional.of("");

        final TextBoxComponent box = this.createComponent()
            .required()
            .setValue(
                Optional.of("not empty no errors")
            ).setValue(value);
        this.valueAndCheck(
            box,
            Optional.empty()
        );
        this.treePrintAndCheck(
            box,
            "TextBoxComponent\n" +
                "  [] REQUIRED\n"
        );
    }

    @Test
    public void testSetValueWithNotEmpty() {
        final Optional<String> value = Optional.of("value123");

        final TextBoxComponent box = this.createComponent()
            .setValue(value);
        this.valueAndCheck(box, value);

        this.treePrintAndCheck(
            box,
            "TextBoxComponent\n" +
                "  [value123] REQUIRED\n"
        );
    }

    @Test
    public void testSetValueSameWithValueWatcher() {
        final Optional<String> value = Optional.of("value123");

        final TextBoxComponent box = this.createComponent()
            .setValue(value)
            .addValueWatcher2(
                v -> {
                    throw new UnsupportedOperationException();
                }
            ).setValue(value);
        this.valueAndCheck(box, value);
    }

    @Test
    public void testSetValueDifferentWithValueWatcher() {
        this.firedValue = null;

        final Optional<String> value = Optional.of("value123");

        final TextBoxComponent box = this.createComponent()
            .addValueWatcher2(
                v -> {
                    this.firedValue = v;
                }
            ).setValue(value);
        this.valueAndCheck(box, value);

        this.checkEquals(
            value,
            this.firedValue,
            "firedValue"
        );
    }

    private Optional<String> firedValue;

    // validator........................................................................................................

    @Test
    public void testOptionalValidationPass() {
        this.treePrintAndCheck(
            TextBoxComponent.empty()
                .setLabel("Label123")
                .setValidator(
                    Optional.of(
                        SpreadsheetValidators.optional(
                            SpreadsheetValidators.fake()
                        )
                    )
                ).optional(),
            "TextBoxComponent\n" +
                "  Label123 []\n"
        );
    }

    @Test
    public void testOptionalValidationPass2() {
        this.treePrintAndCheck(
            TextBoxComponent.empty()
                .setLabel("Label123")
                .optional()
                .setValidator(
                    Optional.of(
                        SpreadsheetValidators.fake()
                    )
                ),
            "TextBoxComponent\n" +
                "  Label123 []\n"
        );
    }

    @Test
    public void testOptionalEmptyValueValidationPass() {
        this.treePrintAndCheck(
            TextBoxComponent.empty()
                .setLabel("Label123")
                .setValue(
                    Optional.of("!")
                ).setValidator(
                    Optional.of(
                        SpreadsheetValidators.optional(
                            (Optional<String> value) ->
                                    ValidationResult.invalid("Optional validator error 123")
                        )
                    )
                ).setValue(Optional.empty())
                .optional(),
            "TextBoxComponent\n" +
                "  Label123 []\n"
        );
    }

    @Test
    public void testRequiredValidationPass() {
        final String value = "PassValue111";

        this.treePrintAndCheck(
            TextBoxComponent.empty()
                .setLabel("Label123")
                .setValidator(
                    Optional.of(
                        (Optional<String> v) ->
                            value.equals(v.orElse(null)) ?
                                ValidationResult.valid() :
                                ValidationResult.invalid("Expected " + value + " but got " + v.orElse(null))
                    )
                ).setValue(
                    Optional.of(value)
                ).required(),
            "TextBoxComponent\n" +
                "  Label123 [PassValue111] REQUIRED\n"
        );
    }

    @Test
    public void testRequiredClearValueValidationPass() {
        final String value = "";

        this.treePrintAndCheck(
            TextBoxComponent.empty()
                .setLabel("Label123")
                .setValidator(
                    Optional.of(
                        (Optional<String> v) ->
                            v.isEmpty() ?
                                ValidationResult.valid() :
                                ValidationResult.invalid("Expected empty but got " + v.orElse(null))
                    )
                ).setValue(
                    Optional.of(value)
                ).required(),
            "TextBoxComponent\n" +
                "  Label123 [] REQUIRED\n"
        );
    }

    @Test
    public void testValidationPass() {
        this.treePrintAndCheck(
            TextBoxComponent.empty()
                .setLabel("Label123")
                .setValidator(
                    Optional.of(
                        new Validator<>() {
                            @Override
                            public ValidationResult isValid(final Optional<String> component) {
                                return ValidationResult.valid();
                            }
                        }
                    )
                ).setValue(Optional.of("Value456")),
            "TextBoxComponent\n" +
                "  Label123 [Value456] REQUIRED\n"
        );
    }

    @Test
    public void testValidationFailureInvalidCharacterException() {
        this.treePrintAndCheck(
            TextBoxComponent.empty()
                .setLabel("Label123")
                .setValidator(
                    Optional.of(
                        SpreadsheetValidators.tryCatch(
                            (s) -> {
                                throw new InvalidCharacterException(s, 2);
                            }
                        )
                    )
                ).setValue(Optional.of("Value456")),
            "TextBoxComponent\n" +
                "  Label123 [Value456] REQUIRED\n" +
                "  Errors\n" +
                "    Invalid character 'l' at 2\n"
        );
    }

    @Test
    public void testValidationFailure() {
        this.treePrintAndCheck(
            TextBoxComponent.empty()
                .setLabel("Label123")
                .setValidator(
                    Optional.of(
                        new Validator<>() {
                            @Override
                            public ValidationResult isValid(final Optional<String> component) {
                                return ValidationResult.invalid("Error message 123");
                            }
                        }
                    )
                ).setValue(Optional.of("Value456")),
            "TextBoxComponent\n" +
                "  Label123 [Value456] REQUIRED\n" +
                "  Errors\n" +
                "    Error message 123\n"
        );
    }

    @Test
    public void testLabelAndValueWithoutValidator() {
        this.treePrintAndCheck(
            TextBoxComponent.empty()
                .setLabel("Label123")
                .setValue(Optional.of("Value456")),
            "TextBoxComponent\n" +
                "  Label123 [Value456] REQUIRED\n"
        );
    }

    @Test
    public void testMagnifyingGlass() {
        this.treePrintAndCheck(
            TextBoxComponent.empty()
                .magnifyingGlassIcon(),
            "TextBoxComponent\n" +
                "  [] REQUIRED\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public TextBoxComponent createComponent() {
        return TextBoxComponent.empty();
    }

    // toString.........................................................................................................

    @Test
    public void testToStringEmptyValue() {
        this.toStringAndCheck(
            this.createComponent(),
            "[] REQUIRED"
        );
    }

    @Test
    public void testToStringValue() {
        this.toStringAndCheck(
            TextBoxComponent.empty()
                .setValue(
                    Optional.of("Value333")
                ),
            "[Value333] REQUIRED"
        );
    }

    @Test
    public void testToStringOptional() {
        this.toStringAndCheck(
            this.createComponent()
                .optional(),
            "[]"
        );
    }

    @Test
    public void testToStringDisabledValue() {
        this.toStringAndCheck(
            this.createComponent()
                .disabled()
                .setValue(
                    Optional.of("Value111")
                ),
            "[Value111] DISABLED REQUIRED"
        );
    }

    @Test
    public void testToStringIdLabelRequiredValue() {
        this.toStringAndCheck(
            TextBoxComponent.empty()
                .setId("id111")
                .setLabel("Label222")
                .required()
                .setValue(
                    Optional.of("Value333")
                ),
            "Label222 [Value333] id=id111 REQUIRED"
        );
    }

    @Test
    public void testToStringValueAndErrors() {
        this.toStringAndCheck(
            TextBoxComponent.empty()
                .setValue(
                    Optional.of("Value333")
                ).setErrors(
                    Lists.of(
                        "Error111",
                        "Error222"
                    )
                ),
            "[Value333] REQUIRED Errors=\"Error111\", \"Error222\""
        );
    }

    @Test
    public void testToStringHelperText() {
        this.toStringAndCheck(
            TextBoxComponent.empty()
                .setHelperText(
                    Optional.of("Helper111")
                ).setValue(
                    Optional.of("Value222")
                ),
            "[Value222] helperText=\"Helper111\" REQUIRED"
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<TextBoxComponent> type() {
        return TextBoxComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
