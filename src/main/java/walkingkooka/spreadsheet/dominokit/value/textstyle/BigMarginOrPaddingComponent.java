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
import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.NeverError;
import walkingkooka.spreadsheet.dominokit.flex.FlexLayoutComponent;
import walkingkooka.spreadsheet.dominokit.value.FormElementComponent;
import walkingkooka.spreadsheet.dominokit.value.FormElementComponentDelegator;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.spreadsheet.dominokit.value.textstyle.filter.TextStylePropertyFilter;
import walkingkooka.spreadsheet.dominokit.value.textstyle.filter.TextStylePropertyFilterKind;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.BoxEdge;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.MarginOrPadding;
import walkingkooka.tree.text.MarginOrPaddingKind;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public interface BigMarginOrPaddingComponent<V extends MarginOrPadding, C extends BigMarginOrPaddingComponent<V, C>> extends TextStylePropertyComponent<HTMLFieldSetElement, V, C>,
    FormElementComponentDelegator<V, C> {

    MarginOrPaddingKind marginOrPaddingKind();

    /**
     * Creates the {@link FormElementComponent} that will contain the TOP, RIGHT, BOTTOM, LEFT and ALL length components,
     * setting {@link ValueWatcher}, labels and style properties.
     */
    default FormElementComponent<V, HTMLDivElement, ?> createFormElementComponent(final TextStylePropertyLengthComponentLike<?> top,
                                                                                  final TextStylePropertyLengthComponentLike<?> right,
                                                                                  final TextStylePropertyLengthComponentLike<?> bottom,
                                                                                  final TextStylePropertyLengthComponentLike<?> left) {
        final FormValueComponent<?, V, ?> all = this.all()
            .optional()
            .setLabel("All");

        top.setLabel("Top")
            .optional();

        right.setLabel("Right")
            .optional();

        bottom.setLabel("Bottom")
            .optional();

        left.setLabel("Left")
            .optional();

        top.addValueWatcherSkipIfErrors2(
            this.lengthToMarginValueWatcher(top)
        );
        right.addValueWatcherSkipIfErrors2(
            this.lengthToMarginValueWatcher(right)
        );
        bottom.addValueWatcherSkipIfErrors2(
            this.lengthToMarginValueWatcher(bottom)
        );
        left.addValueWatcherSkipIfErrors2(
            this.lengthToMarginValueWatcher(left)
        );

        // ignore new Margin if margin has errors, otherwise other components top/right/bottom/left being cleared.
        all.addValueWatcherSkipIfErrors2(
            (Optional<V> marginOrPadding) -> {
                top.setValue(
                    marginOrPadding.flatMap(MarginOrPadding::top)
                );
                right.setValue(
                    marginOrPadding.flatMap(MarginOrPadding::right)
                );
                bottom.setValue(
                    marginOrPadding.flatMap(MarginOrPadding::bottom)
                );
                left.setValue(
                    marginOrPadding.flatMap(MarginOrPadding::left)
                );
            }
        );

        final String width = "calc(25% - 5px)";

        return FormElementComponent.with(
            FlexLayoutComponent.row()
                .appendChild(top.setCssProperty("width", width))
                .appendChild(right.setCssProperty("width", width))
                .appendChild(bottom.setCssProperty("width", width))
                .appendChild(left.setCssProperty("width", width))
                .appendChild(all)
                .setCssProperty("justify-content", "space-between")
        );
    }

    /**
     * Accepts a new {@link Length} ignoring it when errors are present patches the current {@link MarginOrPadding} and
     * updates the {@link BigMarginOrPaddingComponent}
     */
    default ValueWatcher<Length<?>> lengthToMarginValueWatcher(final TextStylePropertyLengthComponentLike<?> component) {
        return new ValueWatcher<>() {
            @Override
            public void onValue(final Optional<Length<?>> length) {

                final V marginOrPadding = (V)
                    BigMarginOrPaddingComponent.this.value()
                        .orElse(
                            (V)
                                BigMarginOrPaddingComponent.this.marginOrPaddingKind()
                                    .empty()
                        ).setOrRemoveProperty(
                            component.name(),
                            length
                        );

                BigMarginOrPaddingComponent.this.setValue(
                    Optional.of(marginOrPadding)
                );
            }
        };
    }

    // HasName..........................................................................................................

    @Override
    default TextStylePropertyName<V> name() {
        return (TextStylePropertyName<V>)
            this.marginOrPaddingKind()
                .marginOrPadding();
    }

    // TextStylePropertyComponent.......................................................................................

    @Override
    default boolean filterTest(final TextStylePropertyFilter filter) {
        Objects.requireNonNull(filter, "filter");

        return filter.testComponent(this) ||
            this.all()
                .filterTest(filter) ||
            this.top()
                .filterTest(filter) ||
            this.right()
                .filterTest(filter) ||
            this.bottom()
                .filterTest(filter) ||
            this.left()
                .filterTest(filter);
    }

    @Override
    default Set<TextStylePropertyFilterKind> textStylePropertyFilterKinds() {
        return TEXT_STYLE_PROPERTY_FILTER_KINDS_BORDER_BOX;
    }

    // FormValueComponent...............................................................................................

    @Override
    default C alwaysShowHelperText() {
        throw new UnsupportedOperationException();
    }

    @Override
    default Optional<V> value() {
        return this.all()
            .value();
    }

    @Override
    default C setValue(final Optional<V> value) {
        Objects.requireNonNull(value, "value");

        final V after;

        final V valueOrEmpty = value.orElse(null);
        if (null != valueOrEmpty) {
            final BoxEdge edge = valueOrEmpty.edge();

            switch (edge) {
                case TOP:
                case RIGHT:
                case BOTTOM:
                case LEFT:
                    final TextStylePropertyName<Length<?>> textStylePropertyName = edge.marginPropertyName();

                    final V before = this.all()
                        .value()
                        .orElse(
                            (V)
                            this.marginOrPaddingKind()
                                .empty());

                    after = (V)
                        before.setOrRemoveProperty(
                        textStylePropertyName,
                        before.getProperty(
                            textStylePropertyName
                        )
                    );
                    break;
                case ALL:
                    after = valueOrEmpty;
                    break;
                default:
                    after = NeverError.unhandledEnum(
                        edge,
                        BoxEdge.values()
                    );
                    break;
            }
        } else {
            after = null;
        }

        this.all()
            .setValue(
                Optional.ofNullable(after)
            );
        return (C) this;
    }

    @Override
    default Runnable addValueWatcher(final ValueWatcher<V> watcher) {
        return this.all()
            .addValueWatcher(watcher);
    }

    @Override
    default boolean isDisabled() {
        return this.all()
            .isDisabled();
    }

    @Override
    default C setDisabled(final boolean disabled) {
        this.all()
            .setDisabled(disabled);
        return (C) this;
    }

    @Override
    default C optional() {
        this.all()
            .optional();
        return (C) this;
    }

    @Override
    default C required() {
        this.all()
            .required();
        return (C) this;
    }

    @Override
    default boolean isRequired() {
        return this.all()
            .isRequired();
    }

    @Override
    default C validate() {
        throw new UnsupportedOperationException();
    }

    @Override
    default C hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    default C removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    default C removePadding() {
        throw new UnsupportedOperationException();
    }

    @Override
    default boolean isEditing() {
        return this.all().isEditing();
    }

    TextStylePropertyValueTextBoxComponentLike<?, V> all();

    TextStylePropertyLengthComponentLike<?> top();

    TextStylePropertyLengthComponentLike<?> right();

    TextStylePropertyLengthComponentLike<?> bottom();

    TextStylePropertyLengthComponentLike<?> left();

    // FormElementComponentDelegator....................................................................................

    @Override
    default C focus() {
        this.top()
            .focus();
        return (C) this;
    }

    @Override
    default C blur() {
        this.all()
            .blur();
        return (C) this;
    }

    // TreePrintable....................................................................................................

    @Override
    default void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.formElementComponent()
                .printTree(printer);
        }
        printer.outdent();
    }
}
