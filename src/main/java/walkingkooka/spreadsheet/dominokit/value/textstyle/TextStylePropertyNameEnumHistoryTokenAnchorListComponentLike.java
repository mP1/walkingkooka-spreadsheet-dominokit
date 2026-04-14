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

import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.Cast;
import walkingkooka.ToStringBuilder;
import walkingkooka.naming.HasName;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorListComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

abstract class TextStylePropertyNameEnumHistoryTokenAnchorListComponentLike<T extends Enum<T>> implements FormValueComponent<HTMLFieldSetElement, T, TextStylePropertyNameEnumHistoryTokenAnchorListComponent<T>>,
    HasName<TextStylePropertyName<T>> {

    static String propertyNameToLabel(final TextStylePropertyName<?> propertyName) {
        return CaseKind.KEBAB.change(
            propertyName.value(),
            CaseKind.TITLE
        );
    }

    TextStylePropertyNameEnumHistoryTokenAnchorListComponentLike(final String idPrefix,
                                                                 final TextStylePropertyName<T> propertyName,
                                                                 final List<T> values,
                                                                 final Function<Optional<T>, String> valueToText,
                                                                 final Function<Optional<T>, Optional<Icon<?>>> valueToIcon,
                                                                 final TextStylePropertyNameEnumHistoryTokenAnchorListComponentContext context) {
        super();

        CharSequences.failIfNullOrEmpty(idPrefix, "idPrefix");
        Objects.requireNonNull(propertyName, "stylePropertyName");
        Objects.requireNonNull(values, "values");
        Objects.requireNonNull(valueToText, "valueToText");
        Objects.requireNonNull(valueToIcon, "valueToIcon");
        Objects.requireNonNull(context, "context");

        this.name = propertyName;

        final AnchorListComponent list = AnchorListComponent.empty();

        for (final T value : values) {
            final Optional<T> optionalValue = Optional.ofNullable(value);

            list.appendChild(
                TextStylePropertyHistoryTokenAnchorComponent.with(
                    idPrefix,
                    propertyName,
                    optionalValue,
                    valueToIcon.apply(optionalValue),
                    context
                ).setTextContent(
                    valueToText.apply(optionalValue)
                )
            );
        }

        this.list = list;
    }

    @Override
    public final boolean isEditing() {
        return this.list.isEditing();
    }

    @Override
    public final Optional<T> value() {
        return this.children()
            .stream()
            .filter(TextStylePropertyHistoryTokenAnchorComponent::isChecked)
            .findFirst()
            .map(TextStylePropertyHistoryTokenAnchorComponent::value)
            .orElse(
                Optional.empty()
            );
    }

    @Override
    public final TextStylePropertyNameEnumHistoryTokenAnchorListComponent<T> setValue(final Optional<T> value) {
        Objects.requireNonNull(value, "value");

        for (final TextStylePropertyHistoryTokenAnchorComponent<T> child : this.children()) {
            child.setChecked(
                child.value()
                    .equals(value)
            );
        }

        return (TextStylePropertyNameEnumHistoryTokenAnchorListComponent<T>) this;
    }

    private List<TextStylePropertyHistoryTokenAnchorComponent<T>> children() {
        return Cast.to(
            this.list.children()
        );
    }

    @Override
    public final Runnable addValueWatcher(final ValueWatcher<T> watcher) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final boolean isDisabled() {
        return false;
    }

    @Override
    public final TextStylePropertyNameEnumHistoryTokenAnchorListComponent<T> setDisabled(final boolean disabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final TextStylePropertyNameEnumHistoryTokenAnchorListComponent<T> alwaysShowHelperText() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean isRequired() {
        return false;
    }

    @Override
    public final TextStylePropertyNameEnumHistoryTokenAnchorListComponent<T> optional() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final TextStylePropertyNameEnumHistoryTokenAnchorListComponent<T> required() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final TextStylePropertyNameEnumHistoryTokenAnchorListComponent<T> validate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final TextStylePropertyNameEnumHistoryTokenAnchorListComponent<T> hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final TextStylePropertyNameEnumHistoryTokenAnchorListComponent<T> removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final TextStylePropertyNameEnumHistoryTokenAnchorListComponent<T> removePadding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final TextStylePropertyNameEnumHistoryTokenAnchorListComponent<T> focus() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final TextStylePropertyNameEnumHistoryTokenAnchorListComponent<T> blur() {
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
                TextStylePropertyNameEnumHistoryTokenAnchorListComponentLike.this.setValue(
                    value.flatMap(
                        (final TextStyle textStyle) -> textStyle.get(
                            TextStylePropertyNameEnumHistoryTokenAnchorListComponentLike.this.name
                        )
                    )
                );
            }
        };
    }

    final AnchorListComponent list;

    // Object...........................................................................................................

    @Override
    public final int hashCode() {
        return this.list.hashCode();
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
            other instanceof TextStylePropertyNameEnumHistoryTokenAnchorListComponent &&
                this.equals0(Cast.to(other));
    }

    private boolean equals0(final TextStylePropertyNameEnumHistoryTokenAnchorListComponent<?> other) {
        return this.list.equals(other.list);
    }

    @Override
    public final String toString() {
        return ToStringBuilder.empty()
            .value(this.list)
            .build();
    }

    // HasName..........................................................................................................

    @Override
    public final TextStylePropertyName<T> name() {
        return this.name;
    }

    final TextStylePropertyName<T> name;

    // TreePrintable....................................................................................................

    @Override
    public final void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            final String label = this.label();
            final boolean missingLabel = CharSequences.isNullOrEmpty(label);

            if(false == missingLabel) {
                printer.println(label);
                printer.indent();
            }

            {
                this.list.printTree(printer);

                printer.indent();
                {
                    final String helperText = this.helperText()
                        .orElse(null);
                    if (null != helperText) {
                        printer.println(helperText);
                    }

                    final List<String> errors = this.errors();
                    if (null != errors && false == errors.isEmpty()) {
                        printer.println("Error(s)");
                        printer.indent();
                        {
                            for (final String error : errors) {
                                printer.println(error);
                            }
                        }
                        printer.outdent();
                    }
                }
                printer.outdent();
            }

            if(false == missingLabel) {
                printer.outdent();
            }
        }
        printer.outdent();
    }
}
