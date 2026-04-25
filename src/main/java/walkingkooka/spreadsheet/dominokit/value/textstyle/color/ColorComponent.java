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

import org.dominokit.domino.ui.icons.MdiIcon;
import org.dominokit.domino.ui.menu.Menu;
import org.dominokit.domino.ui.utils.DominoWrapper;
import org.gwtproject.core.shared.GWT;
import walkingkooka.color.Color;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentDelegator;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Objects;

/**
 * Wraps a {@link ColorComponent} a palette icon which when clicked displays {@link ColorPaletteComponent}.
 */
public final class ColorComponent implements ValueTextBoxComponentDelegator<ColorComponent, Color> {

    public static ColorComponent with(final ColorPaletteComponent colorPaletteComponent) {
        return new ColorComponent(
            Objects.requireNonNull(colorPaletteComponent, "colorPaletteComponent")
        );
    }

    private ColorComponent(final ColorPaletteComponent colorPaletteComponent) {
        super();

        final ValueTextBoxComponent<Color> textBox = ValueTextBoxComponent.with(
            Color::parseRgb,
            Color::text
        );

        this.colorBox = ColorBoxComponent.empty()
            .setCssProperty(
                "width",
                "20px"
            ).setCssProperty(
                "height",
                "20px"
            );
        textBox.setInnerRight(this.colorBox);

        textBox.clearIcon();

        final MdiIcon paletteIcon = SpreadsheetIcons.palette();

        textBox.setIcon(paletteIcon);

        this.textBox = textBox;

        this.colorPaletteComponent = colorPaletteComponent.addValueWatcher2(this::setValue);

        textBox.addValueWatcher2(
            this.colorPaletteComponent::setValue
        );
        textBox.addValueWatcher2(this.colorBox::setValue);

        if (GWT.isScript()) {
            DominoWrapper.wrap(
                paletteIcon.element()
            ).setDropMenu(
                Menu.create()
                    .appendChild(colorPaletteComponent)
            );
        }
    }

    // ValueTextBoxComponentDelegator...................................................................................

    @Override
    public ValueTextBoxComponent<Color> valueTextBoxComponent() {
        return this.textBox;
    }

    /**
     * A textBox that contains a {@link Color}.
     */
    private final ValueTextBoxComponent<Color> textBox;

    private final ColorBoxComponent colorBox;

    private final ColorPaletteComponent colorPaletteComponent;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox + " " + this.colorBox + " " + this.colorPaletteComponent;
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.textBox.printTree(printer);
            printer.indent();
            {
                this.colorPaletteComponent.printTree(printer);
            }
        }
        printer.outdent();
    }
}
