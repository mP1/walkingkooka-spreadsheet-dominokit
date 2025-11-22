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
import org.dominokit.domino.ui.IsElement;
import walkingkooka.spreadsheet.convert.provider.MissingConverter;
import walkingkooka.spreadsheet.convert.provider.MissingConverterSet;
import walkingkooka.spreadsheet.dominokit.ComponentWithChildren;
import walkingkooka.spreadsheet.dominokit.flex.FlexLayoutComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A component that displays a {@link MissingConverterSet}.
 */
final class MissingConverterSetComponent implements ValueComponent<HTMLDivElement, MissingConverterSet, MissingConverterSetComponent>,
    ComponentWithChildren<MissingConverterSetComponent, HTMLDivElement> {

    static MissingConverterSetComponent empty(final MissingConverterSet value) {
        return new MissingConverterSetComponent(
            Objects.requireNonNull(value, "value")
        );
    }

    private MissingConverterSetComponent(final MissingConverterSet value) {
        this.flex = FlexLayoutComponent.column();
        this.setValue(
            Optional.of(value)
        );
    }

    // HtmlComponent....................................................................................................

    @Override
    public String id() {
        return this.flex.id();
    }

    @Override
    public MissingConverterSetComponent setId(final String id) {
        this.flex.setId(id);
        return this;
    }

    @Override
    public int width() {
        return this.flex.width();
    }

    @Override
    public int height() {
        return this.flex.height();
    }

    @Override
    public boolean isEditing() {
        return false;
    }

    @Override
    public MissingConverterSetComponent setCssText(final String css) {
        this.flex.setCssText(css);
        return this;
    }

    @Override
    public MissingConverterSetComponent setCssProperty(final String name,
                                                       final String value) {
        this.flex.setCssProperty(
            name,
            value
        );
        return this;
    }

    @Override
    public MissingConverterSetComponent removeCssProperty(final String name) {
        this.flex.removeCssProperty(name);
        return this;
    }

    @Override
    public HTMLDivElement element() {
        return this.flex.element();
    }

    private final FlexLayoutComponent flex;

    // ValueComponent...................................................................................................

    @Override
    public MissingConverterSetComponent setValue(final Optional<MissingConverterSet> value) {
        Objects.requireNonNull(value, "value");

        this.value = value;

        final FlexLayoutComponent flex = this.flex;
        flex.removeAllChildren();

        final MissingConverterSet missingConverterSet = value.orElse(null);
        if (null != missingConverterSet) {
            for (final MissingConverter missingConverter : missingConverterSet) {
                this.appendChild(
                    MissingConverterComponent.empty(missingConverter)
                );
            }
        }

        return this;
    }

    @Override
    public Optional<MissingConverterSet> value() {
        return this.value;
    }

    private Optional<MissingConverterSet> value;

    @Override
    public Runnable addValueWatcher(final ValueWatcher<MissingConverterSet> watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public MissingConverterSetComponent setDisabled(final boolean disabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MissingConverterSetComponent hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MissingConverterSetComponent removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MissingConverterSetComponent removePadding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MissingConverterSetComponent focus() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MissingConverterSetComponent blur() {
        throw new UnsupportedOperationException();
    }

    // ComponentWithChildren............................................................................................

    @Override
    public MissingConverterSetComponent appendChild(final IsElement<?> child) {
        Objects.requireNonNull(child, "child");

        this.flex.appendChild(child);

        return this;
    }

    @Override
    public MissingConverterSetComponent removeChild(final int index) {
        this.flex.removeChild(index);

        return this;
    }

    @Override
    public List<IsElement<?>> children() {
        return this.flex.children();
    }

    @Override
    public boolean isEmpty() {
        return this.flex.isEmpty();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());

        final MissingConverterSet value = this.value.orElse(null);
        if (null != value) {
            printer.indent();
            {
                value.printTree(printer);
            }
            printer.outdent();
        }
    }
}
