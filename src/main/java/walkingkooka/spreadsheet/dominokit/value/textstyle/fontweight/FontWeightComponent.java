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

package walkingkooka.spreadsheet.dominokit.value.textstyle.fontweight;

import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentDelegator;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.FontWeight;

/**
 * A text box that accepts a {@link FontWeight}.
 */
public final class FontWeightComponent implements ValueTextBoxComponentDelegator<FontWeightComponent, FontWeight> {

    public static FontWeightComponent empty() {
        return new FontWeightComponent();
    }

    private FontWeightComponent() {
        this.textBox = ValueTextBoxComponent.with(
            FontWeight::parse,
            FontWeight::toString
        );
    }

    // ValueTextBoxComponentDelegator...................................................................................

    @Override
    public ValueTextBoxComponent<FontWeight> valueTextBoxComponent() {
        return this.textBox;
    }

    private final ValueTextBoxComponent<FontWeight> textBox;

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            printer.indent();
            {
                this.valueTextBoxComponent()
                    .printTree(printer);
            }
            printer.outdent();
        }
        printer.outdent();
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}