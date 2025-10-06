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

package walkingkooka.spreadsheet.dominokit.choicelist;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.validation.ValidationChoice;
import walkingkooka.validation.ValidationChoiceList;

import java.util.Optional;

public final class ValidationChoiceListComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, Object, ValidationChoiceListComponent> {

    @Test
    public void testSetValidationChoiceList() {
        this.treePrintAndCheck(
            ValidationChoiceListComponent.empty()
                .setLabel("Label")
                .setValidationChoiceList(
                    ValidationChoiceList.EMPTY.concat(
                        ValidationChoice.with(
                            "ValueLabel1",
                            Optional.of(
                                "Value1"
                            )
                        )
                    ).concat(
                        ValidationChoice.with(
                            "ValueLabel2",
                            Optional.of(
                                "Value2"
                            )
                        )
                    ).concat(
                        ValidationChoice.with(
                            "ValueLabel3",
                            Optional.empty()
                        )
                    )
                ),
            "ValidationChoiceListComponent\n" +
                "  SelectComponent\n" +
                "    Label []\n" +
                "      ValueLabel1=Value1\n" +
                "      ValueLabel2=Value2\n" +
                "      ValueLabel3=\n"
        );
    }

    @Test
    public void testSetValidationChoiceListWithNumberValues() {
        this.treePrintAndCheck(
            ValidationChoiceListComponent.empty()
                .setValidationChoiceList(
                    ValidationChoiceList.EMPTY.concat(
                        ValidationChoice.with(
                            "ValueLabel1",
                            Optional.of(
                                11
                            )
                        )
                    ).concat(
                        ValidationChoice.with(
                            "ValueLabel2",
                            Optional.of(
                                22
                            )
                        )
                    ).concat(
                        ValidationChoice.with(
                            "ValueLabel3",
                            Optional.empty()
                        )
                    )
                ),
            "ValidationChoiceListComponent\n" +
                "  SelectComponent\n" +
                "    []\n" +
                "      ValueLabel1=11\n" +
                "      ValueLabel2=22\n" +
                "      ValueLabel3=\n"
        );
    }

    @Test
    public void testSetValidationChoiceListAndSetValue() {
        this.treePrintAndCheck(
            ValidationChoiceListComponent.empty()
                .setLabel("LabelLabel")
                .setValidationChoiceList(
                    ValidationChoiceList.EMPTY.concat(
                        ValidationChoice.with(
                            "ValueLabel1",
                            Optional.of(
                                11
                            )
                        )
                    ).concat(
                        ValidationChoice.with(
                            "ValueLabel2",
                            Optional.of(
                                22
                            )
                        )
                    ).concat(
                        ValidationChoice.with(
                            "ValueLabel3",
                            Optional.empty()
                        )
                    )
                ).setValue(
                    Optional.of(11)
                ),
            "ValidationChoiceListComponent\n" +
                "  SelectComponent\n" +
                "    LabelLabel [11]\n" +
                "      ValueLabel1=11\n" +
                "      ValueLabel2=22\n" +
                "      ValueLabel3=\n"
        );
    }

    @Test
    public void testSetValidationChoiceListSetValidationChoiceList() {
        this.treePrintAndCheck(
            ValidationChoiceListComponent.empty()
                .setLabel("LabelLabel")
                .setValidationChoiceList(
                    ValidationChoiceList.EMPTY.concat(
                        ValidationChoice.with(
                            "Label1",
                            Optional.of(
                                "Value1"
                            )
                        )
                    )
                ).setValue(
                    Optional.of("Value1")
                ).setValidationChoiceList(
                    ValidationChoiceList.EMPTY.concat(
                        ValidationChoice.with(
                            "DifferentLabel1",
                            Optional.of(
                                "DifferentValue1"
                            )
                        )
                    ).concat(
                        ValidationChoice.with(
                            "DifferentLabel2",
                            Optional.of(
                                "DifferentValue2"
                            )
                        )
                    ).concat(
                        ValidationChoice.with(
                            "DifferentLabel3",
                            Optional.empty()
                        )
                    )
                ),
            "ValidationChoiceListComponent\n" +
                "  SelectComponent\n" +
                "    LabelLabel [Value1]\n" +
                "      DifferentLabel1=DifferentValue1\n" +
                "      DifferentLabel2=DifferentValue2\n" +
                "      DifferentLabel3=\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public ValidationChoiceListComponent createComponent() {
        return ValidationChoiceListComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<ValidationChoiceListComponent> type() {
        return ValidationChoiceListComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
