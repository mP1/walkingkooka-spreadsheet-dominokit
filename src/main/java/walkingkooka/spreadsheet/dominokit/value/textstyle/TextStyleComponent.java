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

package walkingkooka.spreadsheet.dominokit.value.textstyle;

import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentDelegator;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStyleProperty;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;

/**
 * A text box that accepts entry as text parsing it as a {@link TextStyle}.
 */
public final class TextStyleComponent implements ValueTextBoxComponentDelegator<TextStyleComponent, TextStyle> {

    public static TextStyleComponent empty() {
        return new TextStyleComponent();
    }

    private TextStyleComponent() {
        this.textBox = ValueTextBoxComponent.with(
            TextStyle::parse,
            TextStyle::text
        );
    }

    /**
     * Helper intended to be part of a {@link walkingkooka.spreadsheet.dominokit.value.ValueWatcher} for individual
     * {@link TextStyleProperty properties} that do not push {@link walkingkooka.spreadsheet.dominokit.history.HistoryToken},
     * themselves.
     */
    public void pushHistoryTokenIfNecessary(final TextStyleProperty<?> property,
                                            final HistoryContext context) {
        Objects.requireNonNull(property, "property");
        Objects.requireNonNull(context, "context");

        final TextStyle textStyle = this.value()
            .orElse(TextStyle.EMPTY);
        final TextStylePropertyName<?> propertyName = property.name();
        final Object propertyValue = property.value()
            .orElse(null);

        if (false == Objects.equals(propertyValue, textStyle.get(propertyName).orElse(null))) {
            context.pushHistoryToken(
                context.historyToken()
                    .setStyleProperty(property)
            );
        }
    }

    // ValueTextBoxComponentDelegator..................................................................................

    @Override
    public ValueTextBoxComponent<TextStyle> valueTextBoxComponent() {
        return this.textBox;
    }

    private final ValueTextBoxComponent<TextStyle> textBox;

    // Object...........................................................................................................
    @Override
    public String toString() {
        return this.textBox.toString();
    }
}
