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

package walkingkooka.spreadsheet.dominokit.textstyle;

import elemental2.dom.HTMLDivElement;
import walkingkooka.Cast;
import walkingkooka.ToStringBuilder;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorListComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public final class TextStylePropertyNameEnumHistoryTokenAnchorListComponent<T> implements HtmlComponentDelegator<HTMLDivElement, TextStylePropertyNameEnumHistoryTokenAnchorListComponent<T>> {

    public static <T> TextStylePropertyNameEnumHistoryTokenAnchorListComponent<T> with(final String idPrefix,
                                                                                       final TextStylePropertyName<T> propertyName,
                                                                                       final List<T> values,
                                                                                       final Function<Optional<T>, String> valueToText,
                                                                                       final TextStylePropertyNameEnumHistoryTokenAnchorListComponentContext context) {
        return new TextStylePropertyNameEnumHistoryTokenAnchorListComponent<>(
            CharSequences.failIfNullOrEmpty(idPrefix, "idPrefix"),
            Objects.requireNonNull(propertyName, "propertyName"),
            Objects.requireNonNull(values, "values"),
            Objects.requireNonNull(valueToText, "valueToText"),
            Objects.requireNonNull(context, "context")
        );
    }

    private TextStylePropertyNameEnumHistoryTokenAnchorListComponent(final String idPrefix,
                                                                     final TextStylePropertyName<T> propertyName,
                                                                     final List<T> values,
                                                                     final Function<Optional<T>, String> valueToText,
                                                                     final TextStylePropertyNameEnumHistoryTokenAnchorListComponentContext context) {
        super();

        final AnchorListComponent list = AnchorListComponent.empty();

        for (final T value : values) {
            final Optional<T> optionalValue = Optional.ofNullable(value);

            list.appendChild(
                TextStylePropertyHistoryTokenAnchorComponent.with(
                    idPrefix,
                    propertyName,
                    optionalValue,
                    TextStylePropertyHistoryTokenAnchorComponent.NO_ICON,
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
