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

package walkingkooka.spreadsheet.dominokit.ui.select;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.spreadsheet.dominokit.ui.ValueComponent;
import walkingkooka.spreadsheet.dominokit.ui.textbox.SpreadsheetTextBoxTreePrintable;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

/**
 * A select component with a few helpers to assist with build and working with values.
 */
public final class SpreadsheetSelectComponent<T> implements ValueComponent<HTMLFieldSetElement, T, SpreadsheetSelectComponent<T>>,
        SpreadsheetTextBoxTreePrintable<SpreadsheetSelectComponent<T>, T> {

    public static <T> SpreadsheetSelectComponent<T> empty() {
        return new SpreadsheetSelectComponent<>();
    }

    private SpreadsheetSelectComponent() {
    }

    @Override
    public SpreadsheetSelectComponent<T> setId(final String id) {
        this.id = id;
        return this;
    }

    @Override
    public String id() {
        return this.id;
    }

    private String id;

    @Override
    public SpreadsheetSelectComponent<T> setLabel(final String label) {
        this.label = label;
        return this;
    }

    @Override
    public String label() {
        return this.label;
    }

    private String label;

    @Override
    public SpreadsheetSelectComponent<T> optional() {
        this.required = false;
        return this;
    }

    @Override
    public SpreadsheetSelectComponent<T> required() {
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
    public SpreadsheetSelectComponent<T> setDisabled(final boolean disabled) {
        this.disabled = true;
        return this;
    }

    private boolean disabled;

    @Override
    public SpreadsheetSelectComponent<T> validate() {
        // TODO enable later somehow ?
        return this;
    }

    @Override
    public List<String> errors() {
        return Lists.empty();
    }

    @Override
    public SpreadsheetSelectComponent<T> focus() {
        return this;
    }

    @Override
    public SpreadsheetSelectComponent<T> alwaysShowHelperText() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetSelectComponent<T> setHelperText(final Optional<String> text) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<String> helperText() {
        return Optional.empty();
    }

    @Override
    public SpreadsheetSelectComponent<T> hideMarginBottom() {
        return this;
    }

    @Override
    public SpreadsheetSelectComponent<T> removeBorders() {
        return this;
    }

    public SpreadsheetSelectComponent<T> addChangeListener(final ChangeListener<Optional<T>> listener) {
        Objects.requireNonNull(listener, "listener");

        return this;
    }

    @Override
    public SpreadsheetSelectComponent<T> addFocusListener(final EventListener listener) {
        Objects.requireNonNull(listener, "listener");
        return this;
    }

    @Override
    public SpreadsheetSelectComponent<T> addKeydownListener(final EventListener listener) {
        Objects.requireNonNull(listener, "listener");

        return this;
    }

    @Override
    public SpreadsheetSelectComponent<T> addKeyupListener(final EventListener listener) {
        Objects.requireNonNull(listener, "listener");
        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        throw new UnsupportedOperationException();
    }

    // Value............................................................................................................

    /**
     * Appends a new value to the drop down.
     */
    public SpreadsheetSelectComponent<T> appendValue(final String text,
                                                     final T value) {
        checkText(text);
        checkValue(value);

        return this.appendValue(
                text,
                Optional.of(value)
        );
    }

    /**
     * Appends a new value to the drop down.
     */
    public SpreadsheetSelectComponent<T> appendValue(final String text,
                                                     final Optional<T> value) {
        checkText(text);
        checkValue(value);

        this.textToValue.put(
                text,
                value
        );
        return this;
    }

    // order is important!
    private final Map<String, Optional<T>> textToValue = Maps.ordered();

    @Override
    public SpreadsheetSelectComponent<T> setValue(final Optional<T> value) {
        checkValue(value);

        this.value = value;
        return this;
    }

    @Override //
    public Optional<T> value() {
        return this.value;
    }

    private Optional<T> value = Optional.empty();

    private static String checkText(final String text) {
        return Objects.requireNonNull(text, "text");
    }

    private static <T> T checkValue(final T value) {
        return Objects.requireNonNull(value, "value");
    }

    // SpreadsheetTextBoxTreePrintable..................................................................................

    @Override
    public void treePrintAlternateValues(final IndentingPrinter printer) {
        printer.indent();
        {
            for (final Entry<String, Optional<T>> textAndValue : this.textToValue.entrySet()) {
                printer.println(
                        textAndValue.getKey() +
                                "=" +
                                textAndValue.getValue()
                                        .map(Object::toString)
                                        .orElse("")
                );
            }
        }
        printer.outdent();
    }
}
