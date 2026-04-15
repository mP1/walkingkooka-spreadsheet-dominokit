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

package walkingkooka.spreadsheet.dominokit.value.textstyle.color;

import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.collect.list.Lists;
import walkingkooka.color.Color;
import walkingkooka.spreadsheet.dominokit.TestHtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.value.FormElementDelegator;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class TextStylePropertyColorComponent extends TextStylePropertyColorComponentLike
    implements TestHtmlElementComponent<HTMLFieldSetElement, TextStylePropertyColorComponent> {

    public static TextStylePropertyColorComponent with(final String idPrefix,
                                                       final TextStylePropertyName<Color> propertyName,
                                                       final TextStylePropertyColorComponentContext context) {
        return new TextStylePropertyColorComponent(
            idPrefix,
            propertyName,
            context
        );
    }

    private TextStylePropertyColorComponent(final String idPrefix,
                                            final TextStylePropertyName<Color> propertyName,
                                            final TextStylePropertyColorComponentContext context) {
        super(
            idPrefix,
            propertyName,
            context
        );

        this.setLabel(
            FormElementDelegator.propertyNameToLabel(propertyName)
        );
        this.setHelperText(
            Optional.empty()
        );
        this.setErrors(
            Lists.empty()
        );
    }

    @Override
    public String label() {
        return this.label;
    }

    @Override
    public TextStylePropertyColorComponent setLabel(final String label) {
        this.label = Objects.requireNonNull(label, "label");
        return this;
    }

    private String label;

    @Override
    public Optional<String> helperText() {
        return this.helperText;
    }

    @Override
    public TextStylePropertyColorComponent setHelperText(final Optional<String> helperText) {
        Objects.requireNonNull(helperText, "helperText");

        this.helperText = helperText;
        return this;
    }

    private Optional<String> helperText;

    @Override
    public List<String> errors() {
        return this.errors;
    }

    @Override
    public TextStylePropertyColorComponent setErrors(final List<String> errors) {
        this.errors = Lists.immutable(
            Objects.requireNonNull(errors, "errors")
        );
        return this;
    }

    private List<String> errors;
}
