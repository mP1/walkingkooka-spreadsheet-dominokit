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

package walkingkooka.spreadsheet.dominokit.datetime;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.Node;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Abstract base class for date/datetime/time pickers.
 */
abstract class SpreadsheetPickerComponent<T, C extends SpreadsheetPickerComponent<T, C>> implements FormValueComponent<HTMLDivElement, T, C> {
    
    SpreadsheetPickerComponent(final Supplier<T> clearValue) {
        this.clearValue = Objects.requireNonNull(clearValue, "clearValue");
    }

    @Override
    public final C setId(final String id) {
        this.element().id = id;
        return (C)this;
    }

    @Override
    public final String id() {
        return this.element().id;
    }

    @Override
    public final C setLabel(final String label) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final String label() {
        return "";
    }

    @Override
    public final boolean isDisabled() {
        return false;
    }

    @Override
    public final C setDisabled(final boolean disabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final C alwaysShowHelperText() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final Optional<String> helperText() {
        return Optional.empty();
    }

    @Override
    public final C setHelperText(final Optional<String> text) {
        throw new UnsupportedOperationException();
    }
    
    final Supplier<T> clearValue;

    @Override
    public final C validate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final C optional() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final C required() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRequired() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> errors() {
        return Lists.empty();
    }

    @Override
    public final C setErrors(final List<String> errors) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final C addFocusListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final C addKeydownListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final C addKeyupListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final C hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final C removeBorders() {
        throw new UnsupportedOperationException();
    }
    
    // node.............................................................................................................

    @Override
    public final Node node() {
        return this.element();
    }

    // TreePrintable....................................................................................................

    @Override
    public final void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            printer.println(
                this.toString()
            );
        }
        printer.outdent();
    }

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return this.value()
            .map(Object::toString)
            .orElse("");
    }
}
