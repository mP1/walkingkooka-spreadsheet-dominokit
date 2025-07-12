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

package walkingkooka.spreadsheet.dominokit.label;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.Context;
import walkingkooka.spreadsheet.dominokit.suggestbox.SpreadsheetSuggestBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.HasText;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A text box component that includes support for finding a label.
 */
public final class SpreadsheetLabelComponent implements FormValueComponent<HTMLFieldSetElement, SpreadsheetLabelName, SpreadsheetLabelComponent> {

    public static SpreadsheetLabelComponent with(final Context context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetLabelComponent(context);
    }

    private SpreadsheetLabelComponent(final Context context) {
        this.suggestBox = SpreadsheetSuggestBoxComponent.with(
            SpreadsheetSelection::labelName,
            SpreadsheetLabelComponentSuggestStore.with(context)
        );

        this.required();
        this.validate();
    }

    // id...............................................................................................................

    @Override
    public SpreadsheetLabelComponent setId(final String id) {
        this.suggestBox.setId(id);
        return this;
    }

    @Override
    public String id() {
        return this.suggestBox.id();
    }

    // label............................................................................................................

    @Override
    public SpreadsheetLabelComponent setLabel(final String label) {
        this.suggestBox.setLabel(label);
        return this;
    }

    @Override
    public String label() {
        return this.suggestBox.label();
    }

    // helperText.......................................................................................................

    @Override
    public SpreadsheetLabelComponent alwaysShowHelperText() {
        this.suggestBox.alwaysShowHelperText();
        return this;
    }

    @Override
    public SpreadsheetLabelComponent setHelperText(final Optional<String> text) {
        this.suggestBox.setHelperText(text);
        return this;
    }

    @Override
    public Optional<String> helperText() {
        return this.suggestBox.helperText();
    }

    // StringValue......................................................................................................

    public SpreadsheetLabelComponent setStringValue(final Optional<String> value) {
        this.suggestBox.setStringValue(value);
        return this;
    }

    public Optional<String> stringValue() {
        return this.suggestBox.stringValue();
    }

    // Value............................................................................................................

    @Override
    public SpreadsheetLabelComponent setValue(final Optional<SpreadsheetLabelName> label) {
        this.suggestBox.setValue(label);
        return this;
    }

    @Override //
    public Optional<SpreadsheetLabelName> value() {
        return this.suggestBox.value();
    }

    // isDisabled.......................................................................................................

    @Override
    public boolean isDisabled() {
        return this.suggestBox.isDisabled();
    }

    @Override
    public SpreadsheetLabelComponent setDisabled(final boolean disabled) {
        this.suggestBox.setDisabled(disabled);
        return this;
    }

    // validation.......................................................................................................

    @Override
    public SpreadsheetLabelComponent optional() {
        this.suggestBox.required();
        return this;
    }

    @Override
    public SpreadsheetLabelComponent required() {
        this.suggestBox.required();
        return this;
    }

    @Override
    public boolean isRequired() {
        return this.suggestBox.isRequired();
    }

    @Override
    public SpreadsheetLabelComponent validate() {
        this.suggestBox.validate();
        return this;
    }

    @Override
    public List<String> errors() {
        return this.suggestBox.errors();
    }

    @Override
    public SpreadsheetLabelComponent setErrors(final List<String> errors) {
        this.suggestBox.setErrors(errors);
        return this;
    }

    // events...........................................................................................................

    @Override
    public SpreadsheetLabelComponent addChangeListener(final ChangeListener<Optional<SpreadsheetLabelName>> listener) {
        this.suggestBox.addChangeListener(listener);
        return this;
    }

    @Override
    public SpreadsheetLabelComponent addClickListener(final EventListener listener) {
        this.suggestBox.addClickListener(listener);
        return this;
    }

    @Override
    public SpreadsheetLabelComponent addFocusListener(final EventListener listener) {
        this.suggestBox.addFocusListener(listener);
        return this;
    }

    @Override
    public SpreadsheetLabelComponent addKeydownListener(final EventListener listener) {
        this.suggestBox.addKeydownListener(listener);
        return this;
    }

    @Override
    public SpreadsheetLabelComponent addKeyupListener(final EventListener listener) {
        this.suggestBox.addKeyupListener(listener);
        return this;
    }

    // focus............................................................................................................

    @Override
    public SpreadsheetLabelComponent focus() {
        this.suggestBox.focus();
        return this;
    }

    // styling..........................................................................................................

    @Override
    public SpreadsheetLabelComponent hideMarginBottom() {
        this.suggestBox.hideMarginBottom();
        return this;
    }

    @Override
    public SpreadsheetLabelComponent removeBorders() {
        this.suggestBox.removeBorders();
        return this;
    }

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetLabelComponent setCssText(final String css) {
        this.suggestBox.setCssText(css);
        return this;
    }

    // setCssProperty...................................................................................................

    @Override
    public SpreadsheetLabelComponent setCssProperty(final String name,
                                                    final String value) {
        this.suggestBox.setCssProperty(
            name,
            value
        );
        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        return this.suggestBox.element();
    }

    private final SpreadsheetSuggestBoxComponent<SpreadsheetLabelName> suggestBox;

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.suggestBox.printTree(printer);
        }
        printer.outdent();
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.value()
            .map(HasText::text)
            .orElse("");
    }
}
