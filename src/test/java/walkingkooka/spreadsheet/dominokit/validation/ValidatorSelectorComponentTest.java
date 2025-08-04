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

package walkingkooka.spreadsheet.dominokit.validation;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.validation.provider.ValidatorSelector;

import java.util.Optional;

public final class ValidatorSelectorComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, ValidatorSelector, ValidatorSelectorComponent> {

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            ValidatorSelectorComponent.empty()
                .setStringValue(
                    Optional.of(
                        "Hello"
                    )
                ),
            "ValidatorSelectorComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [Hello]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            ValidatorSelectorComponent.empty()
                .setStringValue(
                    Optional.of(
                        "Invalid123!"
                    )
                ),
            "ValidatorSelectorComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [Invalid123!]\n" +
                "      Errors\n" +
                "        Invalid character '!' at 10\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public ValidatorSelectorComponent createComponent() {
        return ValidatorSelectorComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<ValidatorSelectorComponent> type() {
        return ValidatorSelectorComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
