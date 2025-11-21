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
import walkingkooka.spreadsheet.convert.provider.MissingConverter;
import walkingkooka.spreadsheet.convert.provider.MissingConverterValue;
import walkingkooka.spreadsheet.dominokit.dom.DivComponent;
import walkingkooka.spreadsheet.dominokit.dom.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Objects;
import java.util.Optional;

/**
 * A component that displays a {@link MissingConverter}.
 */
final class MissingConverterComponent implements ValueComponent<HTMLDivElement, MissingConverter, MissingConverterComponent> {

    static MissingConverterComponent empty(final MissingConverter value) {
        return new MissingConverterComponent(
            Objects.requireNonNull(value, "value")
        );
    }

    private MissingConverterComponent(final MissingConverter value) {
        this.component = HtmlElementComponent.div();
        this.setValue(
            Optional.of(value)
        );
    }

    // HtmlComponent....................................................................................................

    @Override
    public String id() {
        return this.component.id();
    }

    @Override
    public MissingConverterComponent setId(final String id) {
        this.component.setId(id);
        return this;
    }

    @Override
    public int width() {
        return this.component.width();
    }

    @Override
    public int height() {
        return this.component.height();
    }

    @Override
    public boolean isEditing() {
        return false;
    }

    @Override
    public MissingConverterComponent setCssText(final String css) {
        this.component.setCssText(css);
        return this;
    }

    @Override
    public MissingConverterComponent setCssProperty(final String name,
                                                    final String value) {
        this.component.setCssProperty(
            name,
            value
        );
        return this;
    }

    @Override
    public MissingConverterComponent removeCssProperty(final String name) {
        this.component.removeCssProperty(name);
        return this;
    }

    @Override
    public HTMLDivElement element() {
        return this.component.element();
    }

    private final DivComponent component;

    // ValueComponent...................................................................................................

    @Override
    public MissingConverterComponent setValue(final Optional<MissingConverter> value) {
        Objects.requireNonNull(value, "value");

        this.value = value;

        final DivComponent component = this.component;
        component.clear();

        final MissingConverter missingConverter = value.orElse(null);
        if (null != missingConverter) {
            component.appendText(
                missingConverter.name()
                    .value()
            );

            for (final MissingConverterValue missingConverterValue : missingConverter.values()) {
                component.appendChild(
                    MissingConverterValueComponent.empty(missingConverterValue)
                );
            }
        }

        return this;
    }

    @Override
    public Optional<MissingConverter> value() {
        return this.value;
    }

    private Optional<MissingConverter> value;

    @Override
    public Runnable addValueWatcher(final ValueWatcher<MissingConverter> watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public MissingConverterComponent setDisabled(final boolean disabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MissingConverterComponent hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MissingConverterComponent removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MissingConverterComponent removePadding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MissingConverterComponent focus() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MissingConverterComponent blur() {
        throw new UnsupportedOperationException();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());

        final MissingConverter value = this.value.orElse(null);
        if (null != value) {
            printer.indent();
            {
                value.printTree(printer);
            }
            printer.outdent();
        }
    }
}
