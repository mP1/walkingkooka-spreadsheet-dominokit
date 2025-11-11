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

package walkingkooka.spreadsheet.dominokit.select;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.forms.suggest.SelectOption;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.TestHtmlElementComponent;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A select component with a few helpers to assist with build and working with values.
 */
public final class SelectComponent<T> extends SelectComponentLike<T>
    implements TestHtmlElementComponent<HTMLFieldSetElement, SelectComponent<T>> {

    public static <T> SelectComponent<T> empty(final Function<Optional<T>, SelectOption<T>> selectCreator) {
        Objects.requireNonNull(selectCreator, "selectCreator");

        return new SelectComponent<>();
    }

    private SelectComponent() {
    }

    /**
     * Appends a new value to the drop down.
     */
    @Override
    public SelectComponent<T> appendOption(final Optional<T> value) {

        this.values.add(
            Objects.requireNonNull(value, "value")
                .orElse(null)
        );
        return this;
    }

    @Override
    public SelectComponent<T> clearOptions() {
        this.values.clear();
        return this;
    }

    // order is important!
    private final List<T> values = Lists.array();


    @Override
    public SelectComponent<T> setId(final String id) {
        this.id = id;
        return this;
    }

    @Override
    public String id() {
        return this.id;
    }

    private String id;

    @Override
    public SelectComponent<T> setLabel(final String label) {
        this.label = label;
        return this;
    }

    @Override
    public String label() {
        return this.label;
    }

    private String label;

    @Override
    public SelectComponent<T> optional() {
        this.required = false;
        return this;
    }

    @Override
    public SelectComponent<T> required() {
        this.required = true;
        return this;
    }

    @Override
    public boolean isRequired() {
        return this.required;
    }

    boolean required;

    @Override
    public boolean isDisabled() {
        return this.disabled;
    }

    @Override
    public SelectComponent<T> setDisabled(final boolean disabled) {
        this.disabled = true;
        return this;
    }

    private boolean disabled;

    @Override
    public SelectComponent<T> validate() {
        // TODO enable later somehow ?
        return this;
    }

    @Override
    public List<String> errors() {
        return Lists.immutable(this.errors);
    }

    @Override
    public SelectComponent<T> setErrors(final List<String> errors) {
        Objects.requireNonNull(errors, "errors");

        this.errors = Lists.immutable(errors);
        return this;
    }

    private List<String> errors = Lists.empty();

    @Override
    public SelectComponent<T> focus() {
        return this;
    }

    @Override
    public SelectComponent<T> blur() {
        return this;
    }

    @Override
    public SelectComponent<T> hideMarginBottom() {
        return this;
    }

    @Override
    public SelectComponent<T> removeBorders() {
        return this;
    }

    @Override
    public SelectComponent<T> removePadding() {
        return this;
    }

    // addXXXListener...................................................................................................

    @Override
    public SelectComponent<T> addChangeListener(final ChangeListener<Optional<T>> listener) {
        Objects.requireNonNull(listener, "listener");

        return this;
    }

    @Override
    SelectComponent<T> removeChangeListener(final ChangeListener<Optional<T>> listener) {
        Objects.requireNonNull(listener, "listener");

        return this;
    }

    @Override
    SelectComponent<T> addEventListener(final EventType type,
                                        final EventListener listener) {
        return this;
    }

    // Value............................................................................................................

    @Override
    public SelectComponent<T> setValue(final Optional<T> value) {
        this.value = Objects.requireNonNull(value, "value");
        return this;
    }

    @Override //
    public Optional<T> value() {
        return this.value;
    }

    private Optional<T> value = Optional.empty();

    // FormValueComponentTreePrintable..................................................................................

    @Override
    public void treePrintAlternateValues(final IndentingPrinter printer) {
        printer.indent();
        {
            for (final T value : this.values) {
                printer.lineStart();

                TreePrintable.printTreeOrToString(
                    value,
                    printer
                );
            }
        }
        printer.outdent();
    }
}
