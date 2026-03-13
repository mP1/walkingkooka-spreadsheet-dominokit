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

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentLikeTesting;
import walkingkooka.validation.form.FormName;

import java.util.Optional;

public final class FormNameComponentTest implements ValueTextBoxComponentLikeTesting<FormNameComponent, FormName> {

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            FormNameComponent.empty()
                .setStringValue(
                    Optional.of(
                        "Form123"
                    )
                ),
            "FormNameComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [Form123]\n"
        );
    }

    @Test
    public void testSetValueWithInvalid() {
        this.treePrintAndCheck(
            FormNameComponent.empty()
                .clearValue(),
            "FormNameComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      []\n" +
                "      Errors\n" +
                "        Empty \"name\"\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public FormNameComponent createComponent() {
        return FormNameComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<FormNameComponent> type() {
        return FormNameComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
