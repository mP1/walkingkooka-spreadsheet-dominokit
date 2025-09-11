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
import walkingkooka.validation.provider.ValidatorInfoSet;
import walkingkooka.validation.provider.ValidatorProviders;

import java.util.Optional;

public final class ValidatorInfoSetComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, ValidatorInfoSet, ValidatorInfoSetComponent> {

    @Test
    public void testParseAndText() {
        final ValidatorInfoSet infos = ValidatorProviders.validators()
            .validatorInfos();

        this.checkEquals(
            infos,
            ValidatorInfoSet.parse(infos.text())
        );
    }

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            ValidatorInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        ValidatorProviders.validators()
                            .validatorInfos()
                            .text()
                    )
                ),
            "ValidatorInfoSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [https://github.com/mP1/walkingkooka-validation/Validator/collection collection,https://github.com/mP1/walkingkooka-validation/Validator/email-address email-address,https://github.com/mP1/walkingkooka-validation/Validator/expression expression,https://github.com/mP1/walkingkooka-validation/Validator/non-null non-null,https://github.com/mP1/walkingkooka-validation/Validator/text-length text-length]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            ValidatorInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "https://www.example.com/Hello !"
                    )
                ),
            "ValidatorInfoSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [https://www.example.com/Hello !]\n" +
                "      Errors\n" +
                "        Invalid character '!' at 30\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalidSecondUrl() {
        this.treePrintAndCheck(
            ValidatorInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "https://www.example.com/Hello Hello, bad://example.com"
                    )
                ),
            "ValidatorInfoSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [https://www.example.com/Hello Hello, bad://example.com]\n" +
                "      Errors\n" +
                "        unknown protocol: bad\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalidSecondValidatorName() {
        this.treePrintAndCheck(
            ValidatorInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "https://www.example.com/1 Good, https://example.com/2 Bad!"
                    )
                ),
            "ValidatorInfoSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [https://www.example.com/1 Good, https://example.com/2 Bad!]\n" +
                "      Errors\n" +
                "        Invalid character '!' at 57\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public ValidatorInfoSetComponent createComponent() {
        return ValidatorInfoSetComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<ValidatorInfoSetComponent> type() {
        return ValidatorInfoSetComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
