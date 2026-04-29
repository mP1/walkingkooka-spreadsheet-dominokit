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

package walkingkooka.spreadsheet.dominokit.value.textstyle.margin;

import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.spreadsheet.dominokit.flex.FlexLayoutComponent;
import walkingkooka.spreadsheet.dominokit.value.FormElementComponent;
import walkingkooka.spreadsheet.dominokit.value.FormElementComponentDelegator;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyLengthComponentLike;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.BoxEdge;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.Margin;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link TextStylePropertyComponent} that supports editing individual margin {@link Length} or the entire {@link Margin}
 * as text.
 */
public final class BigMarginComponent implements TextStylePropertyComponent<HTMLFieldSetElement, Margin, BigMarginComponent>,
    FormElementComponentDelegator<Margin, BigMarginComponent> {

    public static BigMarginComponent with(final String idPrefix) {
        return new BigMarginComponent(idPrefix);
    }

    private BigMarginComponent(final String idPrefix) {
        super();

        this.margin = MarginComponent.with(idPrefix)
            .optional()
            .setLabel("All");

        this.top = MarginTopComponent.with(idPrefix)
            .setLabel("Top")
            .optional();
        this.right = MarginRightComponent.with(idPrefix)
            .setLabel("Right")
            .optional();
        this.bottom = MarginBottomComponent.with(idPrefix)
            .setLabel("Bottom")
            .optional();
        this.left = MarginLeftComponent.with(idPrefix)
            .setLabel("Left")
            .optional();

        this.top.addValueWatcherSkipIfErrors2(
            this.lengthToMarginValueWatcher(this.top)
        );
        this.right.addValueWatcherSkipIfErrors2(
            this.lengthToMarginValueWatcher(this.right)
        );
        this.bottom.addValueWatcherSkipIfErrors2(
            this.lengthToMarginValueWatcher(this.bottom)
        );
        this.left.addValueWatcherSkipIfErrors2(
            this.lengthToMarginValueWatcher(this.left)
        );

        // ignore new Margin if margin has errors, otherwise other components top/right/bottom/left being cleared.
        this.margin.addValueWatcherSkipIfErrors2(
            (Optional<Margin> value) -> {
                this.top.setValue(
                    value.flatMap(
                        (Margin margin) -> margin.getProperty(TextStylePropertyName.MARGIN_TOP)
                    )
                );
                this.right.setValue(
                    value.flatMap(
                        (Margin margin) -> margin.getProperty(TextStylePropertyName.MARGIN_RIGHT)
                    )
                );
                this.bottom.setValue(
                    value.flatMap(
                        (Margin margin) -> margin.getProperty(TextStylePropertyName.MARGIN_BOTTOM)
                    )
                );
                this.left.setValue(
                    value.flatMap(
                        (Margin margin) -> margin.getProperty(TextStylePropertyName.MARGIN_LEFT)
                    )
                );
            }
        );

        final String width = "calc(25% - 5px)";

        this.formElementComponent = FormElementComponent.with(
            FlexLayoutComponent.row()
                .appendChild(this.top.setCssProperty("width", width))
                .appendChild(this.right.setCssProperty("width", width))
                .appendChild(this.bottom.setCssProperty("width", width))
                .appendChild(this.left.setCssProperty("width", width))
                .appendChild(this.margin)
                .setCssProperty("justify-content", "space-between")
        );

        this.setIdPrefix(
            idPrefix,
            "" // no suffix
        );
        this.setLabelFromPropertyName();
    }

    /**
     * Accepts a new {@link Length} ignoring it when errors are present patches the current {@link Margin} and updates the {@link MarginComponent}
     */
    private ValueWatcher<Length<?>> lengthToMarginValueWatcher(final TextStylePropertyLengthComponentLike<?> length) {
        return new ValueWatcher<Length<?>>() {
            @Override
            public void onValue(final Optional<Length<?>> value) {
                // only copy value if no errors
                final TextStylePropertyName<Length<?>> propertyName = length.name();

                final Margin margin = BigMarginComponent.this.value()
                    .orElse(EMPTY_MARGIN)
                    .setOrRemoveProperty(
                        propertyName,
                        value
                    );

                BigMarginComponent.this.setValue(
                    Optional.ofNullable(margin)
                );
            }
        };
    }

    // @VisibleForTesting
    final MarginTopComponent top;

    // @VisibleForTesting
    final MarginRightComponent right;

    // @VisibleForTesting
    final MarginBottomComponent bottom;

    // @VisibleForTesting
    final MarginLeftComponent left;

    // @VisibleForTesting
    final MarginComponent margin;

    // HasName..........................................................................................................

    @Override
    public TextStylePropertyName<Margin> name() {
        return TextStylePropertyName.MARGIN;
    }

    @Override
    public BigMarginComponent alwaysShowHelperText() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Margin> value() {
        return this.margin.value();
    }

    @Override
    public BigMarginComponent setValue(final Optional<Margin> value) {
        Objects.requireNonNull(value, "value");

        final Margin before = this.margin.value()
            .orElse(EMPTY_MARGIN);

        final Margin valueOrEmpty = value.orElse(EMPTY_MARGIN);

        final BoxEdge edge = valueOrEmpty.edge();

        Margin after = before;

        switch (edge) {
            case TOP:
            case RIGHT:
            case BOTTOM:
            case LEFT:
                final TextStylePropertyName<Length<?>> textStylePropertyName = edge.marginPropertyName();
                after = before.setOrRemoveProperty(
                    textStylePropertyName,
                    valueOrEmpty.getProperty(
                        textStylePropertyName
                    )
                );
                break;
            case ALL:
                after = valueOrEmpty;
                break;
        }

        this.margin.setValue(
            Optional.ofNullable(after)
        );
        return this;
    }

    private final static Margin EMPTY_MARGIN = BoxEdge.ALL.margin(TextStyle.EMPTY);

    @Override
    public Runnable addValueWatcher(final ValueWatcher<Margin> watcher) {
        return this.margin.addValueWatcher(watcher);
    }

    @Override
    public boolean isDisabled() {
        return this.margin.isDisabled();
    }

    @Override
    public BigMarginComponent setDisabled(final boolean disabled) {
        this.margin.setDisabled(disabled);
        return this;
    }

    @Override
    public BigMarginComponent optional() {
        this.margin.optional();
        return this;
    }

    @Override
    public BigMarginComponent required() {
        this.margin.required();
        return this;
    }

    @Override
    public boolean isRequired() {
        return this.margin.isRequired();
    }

    @Override
    public BigMarginComponent validate() {
        return null;
    }

    @Override
    public BigMarginComponent hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BigMarginComponent removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BigMarginComponent removePadding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEditing() {
        return this.margin.isEditing();
    }

    // FormElementComponentDelegator....................................................................................

    @Override
    public FormElementComponent<Margin, ?, ?> formElementComponent() {
        return this.formElementComponent;
    }

    @Override
    public BigMarginComponent focus() {
        this.margin.focus();
        return this;
    }

    @Override
    public BigMarginComponent blur() {
        this.margin.blur();
        return this;
    }

    private final FormElementComponent<Margin, HTMLDivElement, ?> formElementComponent;

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.formElementComponent.printTree(printer);
        }
        printer.outdent();
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.formElementComponent.toString();
    }
}
