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

package walkingkooka.spreadsheet.dominokit.value.textstyle.filter;

import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentDelegator;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Optional;

/**
 * A {@link walkingkooka.spreadsheet.dominokit.HtmlComponent} that supports entry of a filter, which will then update the
 * displayed {@link TextStylePropertyComponent} within a {@link walkingkooka.spreadsheet.dominokit.value.textstyle.TextStyleDialogComponent}
 */
public final class TextStylePropertyFilterComponent implements ValueTextBoxComponentDelegator<TextStylePropertyFilterComponent, TextStylePropertyFilter> {

    public static TextStylePropertyFilterComponent with(final String idPrefix) {
        return new TextStylePropertyFilterComponent(
            CharSequences.failIfNullOrEmpty(idPrefix, "idPrefix")
        );
    }

    private TextStylePropertyFilterComponent(final String idPrefix) {
        super();

        this.filter = ValueTextBoxComponent.with(
                TextStylePropertyFilter::with,
                TextStylePropertyFilter::toString
            ).setId(idPrefix + "filter" + SpreadsheetElementIds.TEXT_BOX);
    }

    // FormValueComponent...............................................................................................

    @Override
    public TextStylePropertyFilterComponent alwaysShowHelperText() {
        this.filter.alwaysShowHelperText();
        return this;
    }

    @Override
    public TextStylePropertyFilterComponent optional() {
        this.filter.optional();
        return this;
    }

    @Override
    public TextStylePropertyFilterComponent required() {
        this.filter.required();
        return this;
    }

    @Override
    public boolean isRequired() {
        this.filter.isRequired();
        return false;
    }

    @Override
    public Optional<TextStylePropertyFilter> value() {
        return this.filter.value();
    }

    @Override
    public TextStylePropertyFilterComponent setValue(final Optional<TextStylePropertyFilter> value) {
        this.filter.setValue(value);

        return this;
    }

    @Override
    public Runnable addValueWatcher(final ValueWatcher<TextStylePropertyFilter> watcher) {
        return this.filter.addValueWatcher(watcher);
    }

    @Override
    public boolean isDisabled() {
        return this.filter.isDisabled();
    }

    @Override
    public TextStylePropertyFilterComponent setDisabled(final boolean disabled) {
        this.filter.setDisabled(disabled);
        return this;
    }

    @Override
    public TextStylePropertyFilterComponent validate() {
        return this;
    }

    @Override
    public TextStylePropertyFilterComponent hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStylePropertyFilterComponent removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStylePropertyFilterComponent removePadding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStylePropertyFilterComponent focus() {
        this.filter.focus();
        return this;
    }

    @Override
    public TextStylePropertyFilterComponent blur() {
        this.filter.blur();
        return this;
    }

    @Override
    public boolean isEditing() {
        return this.filter.isEditing();
    }

    @Override
    public HTMLFieldSetElement element() {
        return this.filter.element();
    }

    // ValueTextBoxComponentDelegator...................................................................................

    @Override
    public ValueTextBoxComponent<TextStylePropertyFilter> valueTextBoxComponent() {
        return this.filter;
    }

    // @VisibleForTesting
    final ValueTextBoxComponent<TextStylePropertyFilter> filter;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.filter.toString();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.filter.printTree(printer);
        }
        printer.outdent();
    }
}
