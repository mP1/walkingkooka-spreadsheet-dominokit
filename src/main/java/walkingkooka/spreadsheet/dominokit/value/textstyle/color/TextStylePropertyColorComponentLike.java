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
import walkingkooka.Cast;
import walkingkooka.ToStringBuilder;
import walkingkooka.color.Color;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;

abstract class TextStylePropertyColorComponentLike implements TextStylePropertyComponent<HTMLFieldSetElement, Color, TextStylePropertyColorComponent> {

    TextStylePropertyColorComponentLike(final String idPrefix,
                                        final TextStylePropertyName<Color> propertyName,
                                        final TextStylePropertyColorComponentContext context) {
        super();

        CharSequences.failIfNullOrEmpty(idPrefix, "idPrefix");
        Objects.requireNonNull(propertyName, "stylePropertyName");
        Objects.requireNonNull(context, "context");

        this.name = propertyName;

        this.textStylePropertyColorComponentMenu = TextStylePropertyColorComponentMenu.with(
            ColorComponent.with(
            idPrefix,
            ColorComponent.historyTokenPreparer(propertyName),
            context // ColorComponentContext
        )
        );
    }

    @Override
    public final boolean isEditing() {
        return this.textStylePropertyColorComponentMenu.isEditing();
    }

    @Override
    public final Optional<Color> value() {
        return this.textStylePropertyColorComponentMenu.value();
    }

    @Override
    public final TextStylePropertyColorComponent setValue(final Optional<Color> value) {
        this.textStylePropertyColorComponentMenu.setValue(value);
        return (TextStylePropertyColorComponent) this;
    }

    @Override
    public final Runnable addValueWatcher(final ValueWatcher<Color> watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean isDisabled() {
        return false;
    }

    @Override
    public final TextStylePropertyColorComponent setDisabled(final boolean disabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final TextStylePropertyColorComponent alwaysShowHelperText() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean isRequired() {
        return false;
    }

    @Override
    public final TextStylePropertyColorComponent optional() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final TextStylePropertyColorComponent required() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final TextStylePropertyColorComponent validate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final TextStylePropertyColorComponent hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final TextStylePropertyColorComponent removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final TextStylePropertyColorComponent removePadding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final TextStylePropertyColorComponent focus() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final TextStylePropertyColorComponent blur() {
        throw new UnsupportedOperationException();
    }

    /**
     * Getter that returns a {@link ValueWatcher} which will cause this component to be update its value, sourced from a
     * {@link TextStyle} value change.
     */
    public final ValueWatcher<TextStyle> textStyleValueWatcher() {
        return new ValueWatcher<TextStyle>() {
            @Override
            public void onValue(final Optional<TextStyle> value) {
                TextStylePropertyColorComponentLike.this.setValue(
                    value.flatMap(
                        (final TextStyle textStyle) -> textStyle.get(
                            TextStylePropertyColorComponentLike.this.name
                        )
                    )
                );
            }
        };
    }

    final TextStylePropertyColorComponentMenu textStylePropertyColorComponentMenu;

    // Object...........................................................................................................

    @Override
    public final int hashCode() {
        return this.textStylePropertyColorComponentMenu.hashCode();
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
            other instanceof TextStylePropertyColorComponentLike &&
                this.equals0(Cast.to(other));
    }

    private boolean equals0(final TextStylePropertyColorComponentLike other) {
        return this.textStylePropertyColorComponentMenu.equals(other.textStylePropertyColorComponentMenu);
    }

    @Override
    public final String toString() {
        return ToStringBuilder.empty()
            .value(this.textStylePropertyColorComponentMenu)
            .build();
    }

    // HasName..........................................................................................................

    @Override
    public final TextStylePropertyName<Color> name() {
        return this.name;
    }

    final TextStylePropertyName<Color> name;

    // TreePrintable....................................................................................................

    @Override
    public final void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            final String label = this.label();
            final boolean missingLabel = CharSequences.isNullOrEmpty(label);

            if (false == missingLabel) {
                printer.println(label);
                printer.indent();
            }

            this.textStylePropertyColorComponentMenu.printTree(printer);

            if (false == missingLabel) {
                printer.outdent();
            }
        }
        printer.outdent();
    }
}
