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

package walkingkooka.spreadsheet.dominokit.locale;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.dominokit.value.SpreadsheetSelectComponent;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * A drop down that supports picking an optional {@link Locale}.
 * TODO Display flags for each locale
 */
final class SpreadsheetLocaleComponent implements FormValueComponent<HTMLFieldSetElement, Locale, SpreadsheetLocaleComponent>,
    TreePrintable {

    /**
     * A {@link SpreadsheetLocaleComponent} which is initially empty, possible options to select must be added after
     * creation.
     */
    static SpreadsheetLocaleComponent empty() {
        return new SpreadsheetLocaleComponent();
    }

    private SpreadsheetLocaleComponent() {
        this.select = SpreadsheetSelectComponent.empty();
    }

    @Override
    public SpreadsheetLocaleComponent setId(final String id) {
        this.select.setId(id);
        return this;
    }

    @Override
    public String id() {
        return this.select.id();
    }

    @Override
    public SpreadsheetLocaleComponent setLabel(final String label) {
        this.select.setLabel(label);
        return this;
    }

    @Override
    public String label() {
        return this.select.label();
    }

    @Override
    public SpreadsheetLocaleComponent focus() {
        this.select.focus();
        return this;
    }

    @Override
    public SpreadsheetLocaleComponent alwaysShowHelperText() {
        this.select.alwaysShowHelperText();
        return this;
    }

    @Override
    public SpreadsheetLocaleComponent setHelperText(final Optional<String> text) {
        this.select.setHelperText(text);
        return this;
    }

    @Override
    public Optional<String> helperText() {
        return this.select.helperText();
    }

    @Override
    public List<String> errors() {
        return this.select.errors();
    }

    @Override
    public SpreadsheetLocaleComponent setErrors(final List<String> errors) {
        this.select.setErrors(errors);
        return this;
    }

    @Override
    public SpreadsheetLocaleComponent hideMarginBottom() {
        this.select.hideMarginBottom();
        return this;
    }

    @Override
    public SpreadsheetLocaleComponent removeBorders() {
        this.select.removeBorders();
        return this;
    }

    @Override
    public boolean isDisabled() {
        return this.select.isDisabled();
    }

    @Override
    public SpreadsheetLocaleComponent setDisabled(final boolean disabled) {
        this.select.setDisabled(disabled);
        return this;
    }

    @Override
    public SpreadsheetLocaleComponent required() {
        this.select.required();
        return this;
    }

    @Override
    public boolean isRequired() {
        return this.select.isRequired();
    }

    @Override
    public SpreadsheetLocaleComponent optional() {
        this.select.optional();
        return this;
    }

    @Override
    public SpreadsheetLocaleComponent validate() {
        this.select.validate();
        return this;
    }

    @Override
    public SpreadsheetLocaleComponent addChangeListener(final ChangeListener<Optional<Locale>> listener) {
        this.select.addChangeListener(listener);
        return this;
    }

    @Override
    public SpreadsheetLocaleComponent addClickListener(final EventListener listener) {
        this.select.addClickListener(
            listener
        );
        return this;
    }
    
    @Override
    public SpreadsheetLocaleComponent addFocusListener(final EventListener listener) {
        this.select.addFocusListener(
            listener
        );
        return this;
    }

    @Override
    public SpreadsheetLocaleComponent addKeydownListener(final EventListener listener) {
        this.select.addKeydownListener(listener);
        return this;
    }

    @Override
    public SpreadsheetLocaleComponent addKeyupListener(final EventListener listener) {
        this.select.addKeyupListener(listener);
        return this;
    }

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetLocaleComponent setCssText(final String css) {
        this.select.setCssText(css);
        return this;
    }

    // setCssProperty...................................................................................................

    @Override
    public SpreadsheetLocaleComponent setCssProperty(final String name,
                                                     final String value) {
        this.select.setCssProperty(
            name,
            value
        );
        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        return this.select.element();
    }

    // Value............................................................................................................

    @Override
    public SpreadsheetLocaleComponent setValue(final Optional<Locale> locale) {
        Objects.requireNonNull(locale, "locale");

        this.select.setValue(locale);
        return this;
    }

    @Override //
    public Optional<Locale> value() {
        return this.select.value();
    }

    private final SpreadsheetSelectComponent<Locale> select;

    /**
     * Adds a {@link Locale}.
     */
    public SpreadsheetLocaleComponent addLocale(final String label,
                                                final Locale locale) {
        Objects.requireNonNull(label, "label");
        Objects.requireNonNull(locale, "locale");

        this.select.appendValue(
            label,
            locale
        );
        return this;
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.select.toString();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.select.printTree(printer);
        }
        printer.outdent();
    }
}