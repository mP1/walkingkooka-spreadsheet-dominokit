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
import org.dominokit.domino.ui.menu.Menu;
import org.dominokit.domino.ui.utils.DominoWrapper;
import org.gwtproject.core.shared.GWT;
import walkingkooka.color.Color;
import walkingkooka.spreadsheet.dominokit.dom.DivComponent;
import walkingkooka.spreadsheet.dominokit.dom.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;

/**
 * Wraps a {@link walkingkooka.color.ColorComponent} adding a drop down menu when a button is clicked. The button
 * shows the {@link Color#text()} and a small box with its background-color filled.
 */
public final class ColorComponent implements ValueComponent<HTMLDivElement, Color, ColorComponent> {

    public static ColorComponent with(final ColorPaletteComponent colorPaletteComponent) {
        return new ColorComponent(
            Objects.requireNonNull(colorPaletteComponent, "colorPaletteComponent")
        );
    }

    private ColorComponent(final ColorPaletteComponent colorPaletteComponent) {
        super();

        this.name = HtmlElementComponent.div()
            .setCssProperty(
                TextStylePropertyName.COLOR,
                Color.BLACK.text()
            ).setDisplay("inline-block")
            .setText("None");

        this.filledBox = HtmlElementComponent.div()
            .setCssText(
                "display: inline-block; margin-left: 5px; margin-bottom: -5px; width: 20px; height: 20px; border: 1px solid black;"
            ).setVisibility(false); // initially hidden.

        this.root = HtmlElementComponent.div()
            .setDisplay("block")
            .appendChild(this.name)
            .appendChild(this.filledBox);

        this.colorPaletteComponent = colorPaletteComponent.addValueWatcher2(
            (Optional<Color> color) -> {
                this.name.setText(
                    color.map(Color::toString)
                        .orElse("None")
                );
                this.filledBox.setOrRemoveCssProperty(
                    TextStylePropertyName.BACKGROUND_COLOR.text(),
                    color.map(Color::toString)
                );
                this.filledBox.setVisibility(color.isPresent());
            }
        );

        if (GWT.isScript()) {
            DominoWrapper.wrap(
                    this.root.element()
                ).setDropMenu(
                    Menu.create()
                        .appendChild(colorPaletteComponent)
                );
        }
    }

    // ValueComponent...................................................................................................

    @Override
    public String id() {
        return this.root.id();
    }

    @Override
    public ColorComponent setId(final String id) {
        this.root.setId(id);
        return this;
    }

    @Override
    public Optional<Color> value() {
        return this.colorPaletteComponent.value();
    }

    @Override
    public ColorComponent setValue(final Optional<Color> value) {
        this.colorPaletteComponent.setValue(value);
        return this;
    }

    @Override
    public Runnable addValueWatcher(final ValueWatcher<Color> watcher) {
        return this.colorPaletteComponent.addValueWatcher(watcher);
    }

    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public ColorComponent setDisabled(final boolean disabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ColorComponent hideMarginBottom() {
        return this;
    }

    @Override
    public ColorComponent removeBorders() {
        return this;
    }

    @Override
    public ColorComponent removePadding() {
        return this;
    }

    @Override
    public ColorComponent focus() {
        this.colorPaletteComponent.focus();
        return this;
    }

    @Override
    public ColorComponent blur() {
        this.colorPaletteComponent.blur();
        return this;
    }

    @Override
    public boolean isEditing() {
        return this.colorPaletteComponent.isEditing();
    }

    @Override
    public int width() {
        return this.root.width();
    }

    @Override
    public int height() {
        return this.root.height();
    }

    @Override
    public ColorComponent setCssText(final String css) {
        this.root.setCssText(css);
        return this;
    }

    @Override
    public ColorComponent setCssProperty(final String name,
                                         final String value) {
        this.root.setCssProperty(name, value);
        return this;
    }

    @Override
    public ColorComponent removeCssProperty(final String name) {
        this.root.removeCssProperty(name);
        return this;
    }

    private final ColorPaletteComponent colorPaletteComponent;

    @Override
    public HTMLDivElement element() {
        return this.root.element();
    }

    /**
     * The menu button which when clicked opens a menu with all colors. When closed the color as text and actual color
     * is displayed.
     */
    private DivComponent root;

    /**
     * This DIV will hold the {@link Color} name, RGB etc as text.
     */
    private DivComponent name;

    /**
     * A small DIV with black borders and filled with the selected {@link Color}.
     */
    private DivComponent filledBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.colorPaletteComponent.toString();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.root.printTree(printer);
            printer.indent();
            {
                this.colorPaletteComponent.printTree(printer);
            }
        }
        printer.outdent();
    }
}
