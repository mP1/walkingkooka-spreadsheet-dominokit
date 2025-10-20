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

package walkingkooka.spreadsheet.dominokit.checkbox;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.validation.ValidationCheckbox;

import java.util.Optional;

public final class ValidationCheckboxComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, Object, ValidationCheckboxComponent> {

    private final static Optional<Object> TRUE = Optional.of("111");

    private final static Optional<Object> FALSE = Optional.of("222");

    @Test
    public void testSetValidationCheckbox() {
        this.treePrintAndCheck(
            this.createComponent()
                .setLabel("Label")
                .setValidationCheckbox(
                    ValidationCheckbox.with(
                        TRUE,
                        FALSE
                    )
                ),
            "ValidationCheckboxComponent\n" +
                "  CheckboxComponent\n" +
                "    Label [] id=Checkbox\n"
        );
    }

    @Test
    public void testSetValidationCheckboxAndValue() {
        this.valueAndCheck(
            this.createComponent()
                .setLabel("Label")
                .setValidationCheckbox(
                    ValidationCheckbox.with(
                        TRUE,
                        FALSE
                    )
                ),
            FALSE
        );
    }

    @Test
    public void testSetValidationCheckboxSetValueFalseAndValue() {
        this.valueAndCheck(
            this.createComponent()
                .setLabel("Label")
                .setValidationCheckbox(
                    ValidationCheckbox.with(
                        TRUE,
                        FALSE
                    )
                ).setValue(FALSE),
            FALSE
        );
    }

    @Test
    public void testSetValidationCheckboxSetValueUnknownAndValue() {
        this.valueAndCheck(
            this.createComponent()
                .setLabel("Label")
                .setValidationCheckbox(
                    ValidationCheckbox.with(
                        TRUE,
                        FALSE
                    )
                ).setValue(
                    Optional.of("Unknown???")
                ),
            FALSE
        );
    }

    @Test
    public void testSetValidationCheckboxSetValueTrueAndValue() {
        this.valueAndCheck(
            this.createComponent()
                .setLabel("Label")
                .setValidationCheckbox(
                    ValidationCheckbox.with(
                        TRUE,
                        FALSE
                    )
                ).setValue(TRUE),
            TRUE
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public ValidationCheckboxComponent createComponent() {
        return ValidationCheckboxComponent.empty(
            "Checkbox",
            new FakeValidationCheckboxComponentContext() {

                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.parseString("/1/SpreadsheetName1/cell/A1");
                }
            }
        );
    }

    // class............................................................................................................

    @Override
    public Class<ValidationCheckboxComponent> type() {
        return ValidationCheckboxComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
