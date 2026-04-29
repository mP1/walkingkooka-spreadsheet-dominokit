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

import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.color.Color;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentDelegator;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponent;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.TextStylePropertyName;

/**
 * A general purpose {@link walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponent}, that accepts
 * a {@link TextStylePropertyName} for a {@link Color}.
 */
public final class TextStylePropertyColorComponent implements TextStylePropertyComponent<HTMLFieldSetElement, Color, TextStylePropertyColorComponent>,
    FormValueComponentDelegator<HTMLFieldSetElement, Color, TextStylePropertyColorComponent> {

    public static TextStylePropertyColorComponent with(final String idPrefix,
                                                       final TextStylePropertyName<Color> propertyName,
                                                       final TextStylePropertyColorComponentContext context) {
        return new TextStylePropertyColorComponent(
            idPrefix,
            propertyName,
            context
        );
    }

    private TextStylePropertyColorComponent(final String idPrefix,
                                            final TextStylePropertyName<Color> propertyName,
                                            final TextStylePropertyColorComponentContext context) {
        super();

        this.propertyName = propertyName;
        this.component = ColorComponent.with(
            ColorPaletteComponent.with(
                idPrefix,
                ColorPaletteComponent.historyTokenPreparer(propertyName),
                context // ColorPaletteComponentContext
            )
        );

        this.setIdPrefix(
            idPrefix,
            SpreadsheetElementIds.TEXT_BOX
        );
    }

    @Override
    public TextStylePropertyName<Color> name() {
        return this.propertyName;
    }

    private final TextStylePropertyName<Color> propertyName;

    @Override
    public FormValueComponent<HTMLFieldSetElement, Color, ?> formValueComponent() {
        return this.component;
    }

    private final ColorComponent component;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.component.printTree(printer);
        }
        printer.outdent();
    }
}
