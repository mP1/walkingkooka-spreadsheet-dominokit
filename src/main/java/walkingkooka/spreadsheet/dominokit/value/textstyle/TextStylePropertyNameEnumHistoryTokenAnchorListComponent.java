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

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.Cast;
import walkingkooka.ToStringBuilder;
import walkingkooka.naming.HasName;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorListComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueComponent;
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

public final class TextStylePropertyNameEnumHistoryTokenAnchorListComponent<T extends Enum<T>> implements ValueComponent<HTMLDivElement, T, TextStylePropertyNameEnumHistoryTokenAnchorListComponent<T>>,
    HtmlComponentDelegator<HTMLDivElement, TextStylePropertyNameEnumHistoryTokenAnchorListComponent<T>>,
    HasName<TextStylePropertyName<T>> {

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

    public static <T extends Enum<T>> TextStylePropertyNameEnumHistoryTokenAnchorListComponent<T> with(final String idPrefix,
                                                                                                       final TextStylePropertyName<T> propertyName,
                                                                                                       final List<T> values,
                                                                                                       final Function<Optional<T>, String> valueToText,
                                                                                                       final Function<Optional<T>, Optional<Icon<?>>> valueToIcon,
                                                                                                       final TextStylePropertyNameEnumHistoryTokenAnchorListComponentContext context) {
        return new TextStylePropertyNameEnumHistoryTokenAnchorListComponent<>(
            CharSequences.failIfNullOrEmpty(idPrefix, "idPrefix"),
            Objects.requireNonNull(propertyName, "stylePropertyName"),
            Objects.requireNonNull(values, "values"),
            Objects.requireNonNull(valueToText, "valueToText"),
            Objects.requireNonNull(valueToIcon, "valueToIcon"),
            Objects.requireNonNull(context, "context")
        );
    }

    private TextStylePropertyNameEnumHistoryTokenAnchorListComponent(final String idPrefix,
                                                                     final TextStylePropertyName<T> propertyName,
                                                                     final List<T> values,
                                                                     final Function<Optional<T>, String> valueToText,
                                                                     final Function<Optional<T>, Optional<Icon<?>>> valueToIcon,
                                                                     final TextStylePropertyNameEnumHistoryTokenAnchorListComponentContext context) {
        super();

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
    public boolean isEditing() {
        return this.list.isEditing();
    }

    @Override
    public Optional<T> value() {
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
    public TextStylePropertyNameEnumHistoryTokenAnchorListComponent<T> setValue(final Optional<T> value) {
        Objects.requireNonNull(value, "value");

        for (final TextStylePropertyHistoryTokenAnchorComponent<T> child : this.children()) {
            child.setChecked(
                child.value()
                    .equals(value)
            );
        }

        return this;
    }

    private List<TextStylePropertyHistoryTokenAnchorComponent<T>> children() {
        return Cast.to(
            this.list.children()
        );
    }

    @Override
    public Runnable addValueWatcher(final ValueWatcher<T> watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public TextStylePropertyNameEnumHistoryTokenAnchorListComponent<T> setDisabled(final boolean disabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStylePropertyNameEnumHistoryTokenAnchorListComponent<T> hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStylePropertyNameEnumHistoryTokenAnchorListComponent<T> removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStylePropertyNameEnumHistoryTokenAnchorListComponent<T> removePadding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStylePropertyNameEnumHistoryTokenAnchorListComponent<T> focus() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStylePropertyNameEnumHistoryTokenAnchorListComponent<T> blur() {
        throw new UnsupportedOperationException();
    }

    /**
     * Getter that returns a {@link ValueWatcher} which will cause this component to be update its value, sourced from a
     * {@link TextStyle} value change.
     */
    public ValueWatcher<TextStyle> textStyleValueWatcher() {
        return new ValueWatcher<TextStyle>() {
            @Override
            public void onValue(final Optional<TextStyle> value) {
                TextStylePropertyNameEnumHistoryTokenAnchorListComponent.this.setValue(
                    value.flatMap(
                        (final TextStyle textStyle) -> textStyle.get(
                            TextStylePropertyNameEnumHistoryTokenAnchorListComponent.this.name
                        )
                    )
                );
            }
        };
    }

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLDivElement, ?> htmlComponent() {
        return this.list;
    }

    private final AnchorListComponent list;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.list.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof TextStylePropertyNameEnumHistoryTokenAnchorListComponent &&
                this.equals0(Cast.to(other));
    }

    private boolean equals0(final TextStylePropertyNameEnumHistoryTokenAnchorListComponent<?> other) {
        return this.list.equals(other.list);
    }

    @Override
    public String toString() {
        return ToStringBuilder.empty()
            .value(this.list)
            .build();
    }

    // HasName..........................................................................................................

    @Override
    public TextStylePropertyName<T> name() {
        return this.name;
    }

    private final TextStylePropertyName<T> name;

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.list.printTree(printer);
        }
        printer.outdent();
    }
}
