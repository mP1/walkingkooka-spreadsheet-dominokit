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
import elemental2.dom.HTMLAnchorElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentLikeDelegate;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * A ValueComponent that wraps a {@link HistoryTokenAnchorComponent}, adding additional support for setting a value via a function.
 * Decorations such as icon for the anchor must be set on the {@link HistoryTokenAnchorComponent} itself as no delegating methods are available.
 */
public final class ValueHistoryTokenAnchorComponent<T> implements ValueComponent<HTMLAnchorElement, T, ValueHistoryTokenAnchorComponent<T>>,
        AnchorComponentLikeDelegate<ValueHistoryTokenAnchorComponent<T>> {

    public static <T> ValueHistoryTokenAnchorComponent<T> with(final HistoryTokenAnchorComponent anchor,
                                                               final Function<HistoryTokenAnchorComponent, Optional<T>> getter,
                                                               final BiConsumer<Optional<T>, HistoryTokenAnchorComponent> setter) {
        return new ValueHistoryTokenAnchorComponent<>(
                Objects.requireNonNull(anchor, "anchor"),
                Objects.requireNonNull(getter, "getter"),
                Objects.requireNonNull(setter, "setter")
        );
    }

    private ValueHistoryTokenAnchorComponent(final HistoryTokenAnchorComponent anchor,
                                             final Function<HistoryTokenAnchorComponent, Optional<T>> getter,
                                             final BiConsumer<Optional<T>, HistoryTokenAnchorComponent> setter) {
        this.anchor = anchor;
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public ValueHistoryTokenAnchorComponent<T> setId(String id) {
        this.anchor.setId(id);
        return this;
    }

    @Override
    public String id() {
        return this.anchor.id();
    }

    @Override
    public String label() {
        return "";
    }

    @Override
    public ValueHistoryTokenAnchorComponent<T> setLabel(final String label) {
        throw new UnsupportedOperationException();
    }


    @Override
    public Optional<T> value() {
        return this.getter.apply(this.anchor);
    }

    private final Function<HistoryTokenAnchorComponent, Optional<T>> getter;

    @Override
    public ValueHistoryTokenAnchorComponent<T> setValue(final Optional<T> value) {
        this.setter.accept(
                value,
                this.anchor
        );
        return this;
    }

    private final BiConsumer<Optional<T>, HistoryTokenAnchorComponent> setter;

    @Override
    public ValueHistoryTokenAnchorComponent<T> validate() {
        return this;
    }

    @Override
    public ValueHistoryTokenAnchorComponent<T> optional() {
        return this;
    }

    @Override
    public ValueHistoryTokenAnchorComponent<T> required() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public ValueHistoryTokenAnchorComponent<T> setDisabled(final boolean disabled) {
        this.anchor.setDisabled(disabled);
        return this;
    }

    @Override
    public ValueHistoryTokenAnchorComponent<T> alwaysShowHelperText() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<String> helperText() {
        return Optional.empty();
    }

    @Override
    public ValueHistoryTokenAnchorComponent<T> setHelperText(final Optional<String> text) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ValueHistoryTokenAnchorComponent<T> addChangeListener(final ChangeListener<Optional<T>> listener) {
        return this;
    }

    @Override
    public ValueHistoryTokenAnchorComponent<T> addFocusListener(final EventListener listener) {
        this.anchor.addFocusListener(listener);
        return this;
    }

    @Override
    public ValueHistoryTokenAnchorComponent<T> addKeydownListener(final EventListener listener) {
        this.anchor.addKeydownListener(listener);
        return this;
    }

    @Override
    public ValueHistoryTokenAnchorComponent<T> addKeyupListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ValueHistoryTokenAnchorComponent<T> hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ValueHistoryTokenAnchorComponent<T> removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> errors() {
        return List.of();
    }

    @Override
    public ValueHistoryTokenAnchorComponent<T> setErrors(final List<String> errors) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ValueHistoryTokenAnchorComponent<T> focus() {
        this.anchor.focus();
        return this;
    }

    // AnchorComponentLikeDelegate......................................................................................

    @Override
    public HistoryTokenAnchorComponent anchorComponentLike() {
        return this.anchor;
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        // continuing the anchor tradition, delegate to the anchor#printTree and do not print this#class#simpleName
        this.anchor.printTree(printer);
    }

    private final HistoryTokenAnchorComponent anchor;
}
