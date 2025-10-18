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
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.validation.ValidationChoice;
import walkingkooka.validation.ValidationChoiceList;

import java.util.Optional;

public final class ValidationChoiceListComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, Object, ValidationChoiceListComponent> {

    @Test
    public void testSetValidationChoiceList() {
        this.treePrintAndCheck(
            this.createComponent()
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
                "    Label [] id=ChoiceList123\n" +
                "      ValueLabel1\n" +
                "        \"Value1\"\n" +
                "      ValueLabel2\n" +
                "        \"Value2\"\n" +
                "      ValueLabel3\n"
        );
    }

    @Test
    public void testSetValidationChoiceListWithNumberValues() {
        this.treePrintAndCheck(
            this.createComponent()
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
                "    [] id=ChoiceList123\n" +
                "      ValueLabel1\n" +
                "        11\n" +
                "      ValueLabel2\n" +
                "        22\n" +
                "      ValueLabel3\n"
        );
    }

    @Test
    public void testSetValidationChoiceListAndSetValue() {
        this.treePrintAndCheck(
            this.createComponent()
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
                "    LabelLabel [ValueLabel1=11] id=ChoiceList123\n" +
                "      ValueLabel1\n" +
                "        11\n" +
                "      ValueLabel2\n" +
                "        22\n" +
                "      ValueLabel3\n"
        );
    }

    @Test
    public void testSetValidationChoiceListSetValidationChoiceList() {
        this.treePrintAndCheck(
            this.createComponent()
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
                "    LabelLabel [Label1=\"Value1\"] id=ChoiceList123\n" +
                "      DifferentLabel1\n" +
                "        \"DifferentValue1\"\n" +
                "      DifferentLabel2\n" +
                "        \"DifferentValue2\"\n" +
                "      DifferentLabel3\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public ValidationChoiceListComponent createComponent() {
        return ValidationChoiceListComponent.empty(
            "ChoiceList123",
            new FakeValidationChoiceListComponentContext() {

                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.parseString("/1/SpreadsheetName1/cell/A1");
                }
            }
        );
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
