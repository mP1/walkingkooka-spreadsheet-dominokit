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

import elemental2.dom.EventListener;
import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.Cast;
import walkingkooka.ToStringBuilder;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorListComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.spreadsheet.dominokit.value.ValueWatchers;
import walkingkooka.spreadsheet.dominokit.value.textstyle.filter.TextStylePropertyFilterKind;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

abstract class TextStylePropertyEnumComponentShared<T extends Enum<T>> implements TextStylePropertyEnumComponentLike<T, TextStylePropertyEnumComponent<T>> {

    /**
     * A function that converts {@link Enum#name()} to Title Case.
     * <pre>
     * UPPER_CASE -> Upper Case
     * </pre>
     */
    public static <T extends Enum<T>> Function<Optional<T>, String> valueToText() {
        return (value) -> value.map(
            (T v) -> CaseKind.SNAKE.change(
                v.name(),
                CaseKind.TITLE
            )
        ).orElse("Clear");
    }

    /**
     * A {@link Function} which will always return no icons for any value
     */
    public static <T extends Enum<T>> Function<Optional<T>, Optional<Icon<?>>> noIcons() {
        return (value) -> Optional.empty();
    }

    TextStylePropertyEnumComponentShared(final String idPrefix,
                                         final TextStylePropertyName<T> propertyName,
                                         final List<T> values,
                                         final Function<Optional<T>, String> valueToText,
                                         final Function<Optional<T>, Optional<Icon<?>>> valueToIcon,
                                         final Set<TextStylePropertyFilterKind> textStylePropertyFilterKinds,
                                         final TextStylePropertyEnumComponentContext context) {
        super();

        CharSequences.failIfNullOrEmpty(idPrefix, "idPrefix");
        Objects.requireNonNull(propertyName, "stylePropertyName");
        Objects.requireNonNull(values, "values");
        Objects.requireNonNull(valueToText, "valueToText");
        Objects.requireNonNull(valueToIcon, "valueToIcon");
        Objects.requireNonNull(textStylePropertyFilterKinds, "textStylePropertyFilterKinds");
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

        this.values = values;

        this.textStylePropertyFilterKinds = textStylePropertyFilterKinds;
    }

    final List<T> values;

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
    public final TextStylePropertyEnumComponent<T> setValue(final Optional<T> value) {
        Objects.requireNonNull(value, "value");

        // only refresh and fire value change event if the value is different.
        if(false == this.value().equals(value)) {
            for (final TextStylePropertyHistoryTokenAnchorComponent<T> child : this.children()) {
                child.setChecked(
                    child.value()
                        .equals(value)
                );
            }

            this.valueWatchers.onValue(value);
        }

        return (TextStylePropertyEnumComponent<T>) this;
    }

    private List<TextStylePropertyHistoryTokenAnchorComponent<T>> children() {
        return Cast.to(
            this.list.children()
        );
    }

    @Override
    public final Runnable addValueWatcher(final ValueWatcher<T> watcher) {
        return this.valueWatchers.add(watcher); // NOP
    }

    private final ValueWatchers<T> valueWatchers = ValueWatchers.empty();
    
    @Override
    public final boolean isDisabled() {
        return false;
    }

    @Override
    public final TextStylePropertyEnumComponent<T> setDisabled(final boolean disabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final TextStylePropertyEnumComponent<T> alwaysShowHelperText() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean isRequired() {
        return false;
    }

    @Override
    public final TextStylePropertyEnumComponent<T> optional() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final TextStylePropertyEnumComponent<T> required() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final TextStylePropertyEnumComponent<T> validate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final TextStylePropertyEnumComponent<T> hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final TextStylePropertyEnumComponent<T> removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final TextStylePropertyEnumComponent<T> removePadding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final TextStylePropertyEnumComponent<T> focus() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final TextStylePropertyEnumComponent<T> blur() {
        throw new UnsupportedOperationException();
    }

    final AnchorListComponent list;

    // HasFocusBlurEventListener........................................................................................

    @Override
    public final TextStylePropertyEnumComponent<T> addBlurListener(final EventListener listener) {
        this.list.addBlurListener(listener);
        return (TextStylePropertyEnumComponent<T>) this;
    }

    @Override
    public final TextStylePropertyEnumComponent<T> addFocusListener(final EventListener listener) {
        this.list.addFocusListener(listener);
        return (TextStylePropertyEnumComponent<T>) this;
    }

    @Override
    public final TextStylePropertyEnumComponent<T> addFocusInListener(final EventListener listener) {
        this.list.addFocusInListener(listener);
        return (TextStylePropertyEnumComponent<T>) this;
    }

    @Override
    public final TextStylePropertyEnumComponent<T> addFocusOutListener(final EventListener listener) {
        this.list.addFocusOutListener(listener);
        return (TextStylePropertyEnumComponent<T>) this;
    }

    // Object...........................................................................................................

    @Override
    public final int hashCode() {
        return this.list.hashCode();
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
            other instanceof TextStylePropertyEnumComponent &&
                this.equals0(Cast.to(other));
    }

    private boolean equals0(final TextStylePropertyEnumComponent<?> other) {
        return this.list.equals(other.list);
    }

    @Override
    public final String toString() {
        return ToStringBuilder.empty()
            .value(this.list)
            .build();
    }

    // TextStylePropertyComponent.......................................................................................

    @Override
    public final Set<TextStylePropertyFilterKind> textStylePropertyFilterKinds() {
        return this.textStylePropertyFilterKinds;
    }

    private final Set<TextStylePropertyFilterKind> textStylePropertyFilterKinds;

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
