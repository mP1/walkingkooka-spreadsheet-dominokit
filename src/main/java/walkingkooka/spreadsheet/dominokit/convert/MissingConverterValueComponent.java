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

package walkingkooka.spreadsheet.dominokit.convert;

import elemental2.dom.HTMLDivElement;
import walkingkooka.spreadsheet.SpreadsheetValueType;
import walkingkooka.spreadsheet.convert.provider.MissingConverterValue;
import walkingkooka.spreadsheet.dominokit.dom.DivComponent;
import walkingkooka.spreadsheet.dominokit.dom.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.spreadsheet.dominokit.valuetype.ValueTypeViewComponent;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Objects;
import java.util.Optional;

/**
 * A component that displays a {@link MissingConverterValue}.
 */
final class MissingConverterValueComponent implements ValueComponent<HTMLDivElement, MissingConverterValue, MissingConverterValueComponent> {

    static MissingConverterValueComponent empty(final MissingConverterValue value) {
        return new MissingConverterValueComponent(
            Objects.requireNonNull(value, "value")
        );
    }

    private MissingConverterValueComponent(final MissingConverterValue value) {
        this.div = HtmlElementComponent.div()
            .setCssText("margin: 8px");

        this.setValue(
            Optional.of(value)
        );
    }

    // HtmlComponent....................................................................................................

    @Override
    public String id() {
        return this.div.id();
    }

    @Override
    public MissingConverterValueComponent setId(final String id) {
        this.div.setId(id);
        return this;
    }

    @Override
    public int width() {
        return this.div.width();
    }

    @Override
    public int height() {
        return this.div.height();
    }

    @Override
    public boolean isEditing() {
        return false;
    }

    @Override
    public MissingConverterValueComponent setCssText(final String css) {
        this.div.setCssText(css);
        return this;
    }

    @Override
    public MissingConverterValueComponent setCssProperty(final String name,
                                                         final String value) {
        this.div.setCssProperty(
            name,
            value
        );
        return this;
    }

    @Override
    public MissingConverterValueComponent removeCssProperty(final String name) {
        this.div.removeCssProperty(name);
        return this;
    }

    @Override
    public HTMLDivElement element() {
        return this.div.element();
    }

    private final DivComponent div;

    // ValueComponent...................................................................................................

    @Override
    public MissingConverterValueComponent setValue(final Optional<MissingConverterValue> value) {
        Objects.requireNonNull(value, "value");

        this.value = value;

        final DivComponent component = this.div;
        component.clear();

        final MissingConverterValue missingConverterValue = value.orElse(null);
        if (null != missingConverterValue) {
            component.appendText(
                String.valueOf(missingConverterValue.value())
            );
            component.appendChild(
                ValueTypeViewComponent.empty()
                    .setValue(
                        Optional.of(
                            SpreadsheetValueType.fromClassName(
                                missingConverterValue.type()
                            )
                        )
                    )
            );
        }

        return this;
    }

    @Override
    public Optional<MissingConverterValue> value() {
        return this.value;
    }

    private Optional<MissingConverterValue> value;

    @Override
    public Runnable addValueWatcher(final ValueWatcher<MissingConverterValue> watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public MissingConverterValueComponent setDisabled(final boolean disabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MissingConverterValueComponent hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MissingConverterValueComponent removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MissingConverterValueComponent removePadding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MissingConverterValueComponent focus() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MissingConverterValueComponent blur() {
        throw new UnsupportedOperationException();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());

        final MissingConverterValue value = this.value.orElse(null);
        if (null != value) {
            printer.indent();
            {
                value.printTree(printer);
            }
            printer.outdent();
        }
    }
}
