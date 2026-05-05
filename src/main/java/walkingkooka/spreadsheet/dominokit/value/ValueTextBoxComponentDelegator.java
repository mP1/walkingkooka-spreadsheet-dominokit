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

package walkingkooka.spreadsheet.dominokit.value;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.dom.HasEventListener;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Optional;

/**
 * A helper interface that implements delegator methods for a wrapped {@link ValueTextBoxComponent}.
 */
public interface ValueTextBoxComponentDelegator<C extends ValueTextBoxComponentDelegator<C, V>, V>
    extends ValueTextBoxComponentLike<C, V>,
    FormValueComponentDelegator<HTMLFieldSetElement, V, C>,
    HasEventListener<V, C> {

    @Override
    default Optional<String> stringValue() {
        return this.valueTextBoxComponent()
            .stringValue();
    }

    @Override
    default C setStringValue(final Optional<String> stringValue) {
        this.valueTextBoxComponent()
            .setStringValue(stringValue);
        return (C) this;
    }

    @Override
    default C setInnerRight(final HtmlComponent<?, ?> innerRight) {
        this.valueTextBoxComponent()
            .setInnerRight(innerRight);
        return (C) this;
    }

    @Override
    default C clearIcon() {
        this.valueTextBoxComponent()
            .clearIcon();
        return (C) this;
    }

    @Override
    default C setIcon(final Icon<?> icon) {
        this.valueTextBoxComponent()
            .setIcon(icon);
        return (C) this;
    }

    @Override
    default C addBlurListener(final EventListener listener) {
        this.valueTextBoxComponent()
            .addBlurListener(listener);
        return (C) this;
    }

    @Override
    default C addClickListener(final EventListener listener) {
        this.valueTextBoxComponent()
            .addClickListener(listener);
        return (C) this;
    }

    @Override
    default C addChangeListener(final ChangeListener<Optional<V>> listener) {
        this.valueTextBoxComponent()
            .addChangeListener(listener);
        return (C) this;
    }

    @Override
    default C addContextMenuListener(final EventListener listener) {
        this.valueTextBoxComponent()
            .addContextMenuListener(listener);
        return (C) this;
    }

    @Override
    default C addFocusListener(final EventListener listener) {
        this.valueTextBoxComponent()
            .addFocusListener(listener);
        return (C) this;
    }

    @Override
    default C addInputListener(final EventListener listener) {
        this.valueTextBoxComponent()
            .addInputListener(listener);
        return (C) this;
    }

    @Override
    default C addKeyDownListener(final EventListener listener) {
        this.valueTextBoxComponent()
            .addKeyDownListener(listener);
        return (C) this;
    }

    @Override
    default C addKeyUpListener(final EventListener listener) {
        this.valueTextBoxComponent()
            .addKeyUpListener(listener);
        return (C) this;
    }

    /**
     * The wrapped {@link ValueTextBoxComponent}, which is the target of all delegated methods.
     */
    ValueTextBoxComponent<V> valueTextBoxComponent();

    // FormValueComponentDelegator......................................................................................

    @Override
    default FormValueComponent<HTMLFieldSetElement, V, ?> formValueComponent() {
        return this.valueTextBoxComponent();
    }

    // TreePrintable....................................................................................................

    @Override
    default void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.valueTextBoxComponent()
                .printTree(printer);
        }
        printer.outdent();
    }
}
