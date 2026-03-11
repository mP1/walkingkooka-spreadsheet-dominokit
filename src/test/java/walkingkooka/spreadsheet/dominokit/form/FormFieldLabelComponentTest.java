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

package walkingkooka.spreadsheet.dominokit.form;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;

import java.util.Optional;

public final class FormFieldLabelComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, String, FormFieldLabelComponent> {

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            FormFieldLabelComponent.empty()
                .setStringValue(
                    Optional.of(
                        "Label123"
                    )
                ),
            "FormFieldLabelComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [Label123]\n"
        );
    }

    @Test
    public void testSetValueWithEmpty() {
        this.treePrintAndCheck(
            FormFieldLabelComponent.empty()
                .clearValue(),
            "FormFieldLabelComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      []\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public FormFieldLabelComponent createComponent() {
        return FormFieldLabelComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<FormFieldLabelComponent> type() {
        return FormFieldLabelComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
