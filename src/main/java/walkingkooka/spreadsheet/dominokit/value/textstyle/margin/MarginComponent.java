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

package walkingkooka.spreadsheet.dominokit.value.textstyle.margin;

import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentDelegator;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponent;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.Margin;
import walkingkooka.tree.text.TextStylePropertyName;

/**
 * A text box that accepts text entry and validates it as a {@link Margin}.
 */
public final class MarginComponent implements TextStylePropertyComponent<HTMLFieldSetElement, Margin, MarginComponent>,
    ValueTextBoxComponentDelegator<MarginComponent, Margin> {

    public static MarginComponent with(final String idPrefix) {
        return new MarginComponent(idPrefix);
    }

    private MarginComponent(final String idPrefix) {
        this.textBox = ValueTextBoxComponent.with(
            Margin::parse,
            Margin::text
        );
        this.setIdPrefix(idPrefix);
    }

    // HasName..........................................................................................................

    @Override
    public TextStylePropertyName<Margin> name() {
        return TextStylePropertyName.MARGIN;
    }

    // ValueTextBoxComponentDelegator...................................................................................

    @Override
    public ValueTextBoxComponent<Margin> valueTextBoxComponent() {
        return this.textBox;
    }

    private final ValueTextBoxComponent<Margin> textBox;

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.valueTextBoxComponent()
                .printTree(printer);
        }
        printer.outdent();
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}