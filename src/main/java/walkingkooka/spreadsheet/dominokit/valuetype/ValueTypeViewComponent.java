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

package walkingkooka.spreadsheet.dominokit.valuetype;

import elemental2.dom.HTMLElement;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.label.LabelComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.validation.ValueTypeName;

import java.util.Objects;
import java.util.Optional;

public final class ValueTypeViewComponent implements ValueComponent<HTMLElement, ValueTypeName, ValueTypeViewComponent>,
    HtmlComponentDelegator<HTMLElement, ValueTypeViewComponent> {

    public static ValueTypeViewComponent empty() {
        return new ValueTypeViewComponent();
    }

    private ValueTypeViewComponent() {
        super();
        this.label = LabelComponent.empty();
    }

    @Override
    public ValueTypeViewComponent setValue(final Optional<ValueTypeName> value) {
        Objects.requireNonNull(value, "value");

        this.label.setValue(value.map(ValueTypeName::value));
        return this;
    }

    @Override
    public Optional<ValueTypeName> value() {
        return this.label.value()
            .map(ValueTypeName::with);
    }

    @Override
    public boolean isEditing() {
        return false;
    }

    @Override
    public Runnable addValueWatcher(final ValueWatcher<ValueTypeName> watcher) {
        throw new UnsupportedOperationException();
    }

    // HtmlElementComponentDelegator....................................................................................

    @Override
    public HtmlComponent<HTMLElement, ?> htmlComponent() {
        return this.label;
    }

    private final LabelComponent label;

    @Override
    public boolean isDisabled() {
        return true;
    }

    @Override
    public ValueTypeViewComponent setDisabled(final boolean disabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ValueTypeViewComponent hideMarginBottom() {
        return this;
    }

    @Override
    public ValueTypeViewComponent removeBorders() {
        return this;
    }

    @Override
    public ValueTypeViewComponent removePadding() {
        return this;
    }

    @Override
    public ValueTypeViewComponent focus() {
        return this;
    }

    @Override
    public ValueTypeViewComponent blur() {
        return this;
    }

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.label.printTree(printer);
        }
        printer.outdent();
    }
}
