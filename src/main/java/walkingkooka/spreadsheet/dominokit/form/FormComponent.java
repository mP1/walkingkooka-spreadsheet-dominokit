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

import walkingkooka.currency.CurrencyContexts;
import walkingkooka.locale.LocaleContexts;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentDelegator;
import walkingkooka.spreadsheet.validation.SpreadsheetValidationReference;
import walkingkooka.spreadsheet.validation.form.SpreadsheetForms;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContexts;
import walkingkooka.tree.json.marshall.JsonNodeMarshallUnmarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallUnmarshallContexts;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContexts;
import walkingkooka.validation.form.Form;

import java.math.MathContext;
import java.util.Locale;

/**
 * A text box that supports viewing and editing a {@link Form} as JSON.
 */
public final class FormComponent implements ValueTextBoxComponentDelegator<FormComponent, Form<SpreadsheetValidationReference>> {

    public static FormComponent empty() {
        return new FormComponent();
    }

    private FormComponent() {
        this.textBox = ValueTextBoxComponent.with(
            FormComponent::parseJson,
            FormComponent::toJson
        );
    }

    private static Form<SpreadsheetValidationReference> parseJson(final String text) {
        return CONTEXT.unmarshall(
            JsonNode.parse(text),
            SpreadsheetForms.FORM_CLASS
        );
    }

    /**
     * Removes any errors from the given JSON if any were present.
     */
    private static String toJson(final Form<SpreadsheetValidationReference> form) {
        return CONTEXT.marshall(
            form.clearErrors()
        ).toString();
    }

    // @VisibleForTesting
    final static JsonNodeMarshallUnmarshallContext CONTEXT = JsonNodeMarshallUnmarshallContexts.basic(
        JsonNodeMarshallContexts.basic(),
        JsonNodeUnmarshallContexts.basic(
            ExpressionNumberKind.DEFAULT,
            CurrencyContexts.fake()
                .setLocaleContext(
                    LocaleContexts.jre(
                        Locale.forLanguageTag("en-AU")
                    )
                ),
            MathContext.UNLIMITED
        )
    );

    // ValueTextBoxComponentDelegator...................................................................................

    @Override
    public ValueTextBoxComponent<Form<SpreadsheetValidationReference>> valueTextBoxComponent() {
        return this.textBox;
    }

    private final ValueTextBoxComponent<Form<SpreadsheetValidationReference>> textBox;

    // Object...........................................................................................................
    @Override
    public String toString() {
        return this.textBox.toString();
    }
}
