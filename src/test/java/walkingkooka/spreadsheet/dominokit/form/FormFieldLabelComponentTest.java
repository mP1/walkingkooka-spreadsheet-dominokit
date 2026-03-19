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
import walkingkooka.text.CharSequences;
import walkingkooka.validation.form.FormField;

import java.util.Optional;

public final class FormFieldLabelComponentTest implements ValueTextBoxComponentLikeTesting<FormFieldLabelComponent, String> {

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
                "      [Label123] REQUIRED\n"
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
                "      [] REQUIRED\n"
        );
    }

    @Test
    public void testSetValueWithLengthGreaterThanMax() {
        this.treePrintAndCheck(
            FormFieldLabelComponent.empty()
                .setValue(
                    Optional.of(
                        CharSequences.repeating('a', FormField.MAX_LABEL_LENGTH + 1)
                            .toString()
                    )
                ),
            "FormFieldLabelComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa] REQUIRED\n" +
                "      Errors\n" +
                "        Length 51 of \"label\" not between 0..50 = \"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\"\n"
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
