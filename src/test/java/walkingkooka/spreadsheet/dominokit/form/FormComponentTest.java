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
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetStartup;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentLikeTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.validation.SpreadsheetValidationReference;
import walkingkooka.spreadsheet.validation.form.SpreadsheetForms;
import walkingkooka.validation.form.Form;
import walkingkooka.validation.form.FormField;
import walkingkooka.validation.form.FormName;

import java.util.Optional;

public final class FormComponentTest implements ValueTextBoxComponentLikeTesting<FormComponent, Form<SpreadsheetValidationReference>> {

    static {
        SpreadsheetStartup.init();
    }

    @Test
    public void testSetStringValueInvalidJson() {
        this.treePrintAndCheck(
            FormComponent.empty()
                .setStringValue(
                    Optional.of(
                        "{\n" +
                            "        \"name\": \"Form123\",\n" +
                            "        \"fields\": [\n" +
                            "          {\n" +
                            "            \"reference\": {\n"
                    )
                ),
            "FormComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [{\n" +
                "              \"name\": \"Form123\",\n" +
                "              \"fields\": [\n" +
                "                {\n" +
                "                  \"reference\": {\n" +
                "      ]\n" +
                "      Errors\n" +
                "        Invalid character '\\n' at 87 expected [OBJECT_PROPERTY, {[WHITESPACE], \",\", OBJECT_PROPERTY_REQUIRED}], [WHITESPACE], \"}\"\n"
        );
    }

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            FormComponent.empty()
                .setStringValue(
                    Optional.of(
                        "{\n" +
                            "        \"name\": \"Form123\",\n" +
                            "        \"fields\": [\n" +
                            "          {\n" +
                            "            \"reference\": {\n" +
                            "              \"type\": \"spreadsheet-cell-reference\",\n" +
                            "              \"value\": \"A1\"\n" +
                            "            }\n" +
                            "          }\n" +
                            "        ]\n" +
                            "      }"
                    )
                ),
            "FormComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [{\n" +
                "              \"name\": \"Form123\",\n" +
                "              \"fields\": [\n" +
                "                {\n" +
                "                  \"reference\": {\n" +
                "                    \"type\": \"spreadsheet-cell-reference\",\n" +
                "                    \"value\": \"A1\"\n" +
                "                  }\n" +
                "                }\n" +
                "              ]\n" +
                "            }]\n"
        );
    }

    @Test
    public void testSetStringValueEquivalentJson() {
        final String text = "{\n" +
            "\"name\": \"Form123\",\n" +
            "\"fields\": [\n" +
            "{\n" +
            "\"reference\": {\n" +
            "\"type\": \"spreadsheet-cell-reference\",\n" +
            "\"value\": \"A1\"\n" +
            "}\n" +
            "}\n" +
            "]\n" +
            "}";

        final FormComponent component = FormComponent.empty()
            .setStringValue(
                Optional.of(text)
            );

        this.stringValueAndCheck(
            component,
            text
        );

        // json shouldnt be reformatted or pretty formatted
        this.treePrintAndCheck(
            component,
            "FormComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [{\n" +
                "      \"name\": \"Form123\",\n" +
                "      \"fields\": [\n" +
                "      {\n" +
                "      \"reference\": {\n" +
                "      \"type\": \"spreadsheet-cell-reference\",\n" +
                "      \"value\": \"A1\"\n" +
                "      }\n" +
                "      }\n" +
                "      ]\n" +
                "      }]\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            FormComponent.empty()
                .setValue(
                    Optional.of(
                        SpreadsheetForms.form(
                            FormName.with("Form123")
                        ).setFields(
                            Lists.of(
                                FormField.with(SpreadsheetSelection.A1)
                            )
                        )
                    )
                ),
            "FormComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [{\n" +
                "        \"name\": \"Form123\",\n" +
                "        \"fields\": [\n" +
                "          {\n" +
                "            \"reference\": {\n" +
                "              \"type\": \"spreadsheet-cell-reference\",\n" +
                "              \"value\": \"A1\"\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      }]\n"
        );
    }

    @Test
    public void testSetValueFormWithErrors() {
        this.treePrintAndCheck(
            FormComponent.empty()
                .setValue(
                    Optional.of(
                        SpreadsheetForms.form(
                            FormName.with("Form123")
                        ).setFields(
                            Lists.of(
                                FormField.with(SpreadsheetSelection.A1)
                            )
                        ).setErrors(
                            Lists.of(
                                SpreadsheetForms.error(SpreadsheetSelection.A1)
                                    .setMessage("Error message 1")
                            )
                        )
                    )
                ),
            "FormComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [{\n" +
                "        \"name\": \"Form123\",\n" +
                "        \"fields\": [\n" +
                "          {\n" +
                "            \"reference\": {\n" +
                "              \"type\": \"spreadsheet-cell-reference\",\n" +
                "              \"value\": \"A1\"\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      }]\n"
        );
    }

    @Test
    public void testSetValueWithInvalid() {
        this.treePrintAndCheck(
            FormComponent.empty()
                .clearValue(),
            "FormComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      []\n" +
                "      Errors\n" +
                "        Empty \"text\"\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public FormComponent createComponent() {
        return FormComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<FormComponent> type() {
        return FormComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
