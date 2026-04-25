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

package walkingkooka.spreadsheet.dominokit.value.textstyle.color;

import elemental2.dom.HTMLDivElement;
import walkingkooka.color.Color;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.dom.DivComponent;
import walkingkooka.spreadsheet.dominokit.dom.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;

/**
 * A simple component that displays a box and fills the background with the given {@link Color}, showing if a value is present,
 * hiding if absent.
 */
public final class ColorBoxComponent implements ValueComponent<HTMLDivElement, Color, ColorBoxComponent>,
    HtmlComponentDelegator<HTMLDivElement, ColorBoxComponent> {

    public static ColorBoxComponent empty() {
        return new ColorBoxComponent();
    }

    private ColorBoxComponent() {
        super();
        this.component = HtmlElementComponent.div()
            .setCssText("border-color: black; border-style: solid; border-width: 1px;");
        this.setValue(Optional.empty());
    }

    // ValueComponent...................................................................................................

    @Override
    public Optional<Color> value() {
        return this.color;
    }

    @Override
    public ColorBoxComponent setValue(final Optional<Color> value) {
        this.color = Objects.requireNonNull(value, "value");

        final DivComponent component = this.component;
        final Color colorOrNull = value.orElse(null);

        component.setDisplay(
            null != colorOrNull ?
                "" :
                "none"
        );

        if(null != colorOrNull) {
            component.setBackgroundColor(
                colorOrNull.toString()
            );
        } else {
            component.removeCssProperty(
                TextStylePropertyName.BACKGROUND_COLOR.toString()
            );
        }
        return this;
    }

    private Optional<Color> color;

    @Override
    public Runnable addValueWatcher(final ValueWatcher<Color> watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public ColorBoxComponent setDisabled(final boolean disabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ColorBoxComponent hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ColorBoxComponent removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ColorBoxComponent removePadding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEditing() {
        return false;
    }

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLDivElement, ?> htmlComponent() {
        return this.component;
    }

    private final DivComponent component;

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());

        final Color colorOrNull = this.color.orElse(null);
        if(null != colorOrNull) {

            printer.indent();
            {
                colorOrNull.printTree(printer);
            }
            printer.outdent();
        }
    }
}
