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
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.dom.HasEventListeners;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Optional;

/**
 * A helper interface that implements delegator methods for a wrapped {@link ValueTextBoxComponent}.
 */
public interface ValueTextBoxComponentDelegator<C extends ValueTextBoxComponentDelegator<C, V>, V>
    extends FormValueComponent<HTMLFieldSetElement, V, C>,
    HtmlComponentDelegator<HTMLFieldSetElement, C>,
    HasEventListeners<V, C>,
    HasValueWatchers<HTMLFieldSetElement, V, C> {

    @Override
    default C setId(final String id) {
        this.valueTextBoxComponent()
            .setId(id);
        return (C) this;
    }

    @Override
    default String id() {
        return this.valueTextBoxComponent()
            .id();
    }

    @Override
    default C setLabel(final String label) {
        this.valueTextBoxComponent()
            .setLabel(label);
        return (C) this;
    }

    @Override
    default String label() {
        return this.valueTextBoxComponent()
            .label();
    }

    @Override
    default C setValue(final Optional<V> value) {
        this.valueTextBoxComponent()
            .setValue(value);
        return (C) this;
    }

    @Override
    default C focus() {
        this.valueTextBoxComponent()
            .focus();
        return (C) this;
    }

    @Override
    default boolean isEditing() {
        return this.valueTextBoxComponent()
            .isEditing();
    }

    @Override
    default C optional() {
        this.valueTextBoxComponent()
            .optional();
        return (C) this;
    }

    @Override
    default C required() {
        this.valueTextBoxComponent()
            .required();
        return (C) this;
    }

    @Override
    default boolean isRequired() {
        return this.valueTextBoxComponent()
            .isRequired();
    }

    @Override
    default C validate() {
        this.valueTextBoxComponent()
            .validate();
        return (C) this;
    }

    @Override
    default List<String> errors() {
        return this.valueTextBoxComponent()
            .errors();
    }

    @Override
    default C setErrors(final List<String> errors) {
        this.valueTextBoxComponent()
            .setErrors(errors);
        return (C) this;
    }

    @Override
    default boolean isDisabled() {
        return this.valueTextBoxComponent()
            .isDisabled();
    }

    @Override
    default C setDisabled(final boolean disabled) {
        this.valueTextBoxComponent()
            .setDisabled(disabled);
        return (C) this;
    }

    @Override
    default C addBlurListener(final EventListener listener) {
        this.valueTextBoxComponent()
            .addBlurListener(listener);
        return (C) this;
    }
    
    @Override
    default C addChangeListener(final ChangeListener<Optional<V>> listener) {
        this.valueTextBoxComponent()
            .addChangeListener(listener);
        return (C) this;
    }

    @Override
    default C addClickListener(final EventListener listener) {
        this.valueTextBoxComponent()
            .addClickListener(listener);
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

    @Override
    default Runnable addValueWatcher(final ValueWatcher<V> watcher) {
        return this.valueTextBoxComponent()
            .addValueWatcher(watcher);
    }

    default C addValueWatcher2(final ValueWatcher<V> watcher) {
        this.valueTextBoxComponent()
            .addValueWatcher2(watcher);
        return (C) this;
    }

    @Override
    default C alwaysShowHelperText() {
        this.valueTextBoxComponent()
            .alwaysShowHelperText();
        return (C) this;
    }

    @Override
    default C setHelperText(final Optional<String> text) {
        this.valueTextBoxComponent()
            .setHelperText(text);
        return (C) this;
    }

    @Override
    default Optional<String> helperText() {
        return this.valueTextBoxComponent()
            .helperText();
    }

    @Override
    default C hideMarginBottom() {
        this.valueTextBoxComponent()
            .hideMarginBottom();
        return (C) this;
    }

    @Override
    default C removeBorders() {
        this.valueTextBoxComponent()
            .removeBorders();
        return (C) this;
    }

    @Override
    default C removePadding() {
        this.valueTextBoxComponent()
            .removePadding();
        return (C) this;
    }

    @Override
    default Optional<V> value() {
        return this.valueTextBoxComponent()
            .value();
    }

    default Optional<String> stringValue() {
        return this.valueTextBoxComponent()
            .stringValue();
    }

    default C setStringValue(final Optional<String> stringValue) {
        this.valueTextBoxComponent()
            .setStringValue(stringValue);
        return (C) this;
    }

    /**
     * The wrapped {@link ValueTextBoxComponent}, which is the target of all delegated methods.
     */
    ValueTextBoxComponent<V> valueTextBoxComponent();

    // HtmlComponentDelegator...........................................................................................

    @Override
    default HtmlComponent<HTMLFieldSetElement, ?> htmlComponent() {
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
