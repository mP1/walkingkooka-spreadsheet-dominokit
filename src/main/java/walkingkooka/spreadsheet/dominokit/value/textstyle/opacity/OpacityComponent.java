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

package walkingkooka.spreadsheet.dominokit.value.textstyle.opacity;

import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentDelegator;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.Opacity;
import walkingkooka.tree.text.TextStylePropertyName;

/**
 * A text box that accepts text entry and validates it as a {@link Opacity}.
 */
public final class OpacityComponent implements TextStylePropertyComponent<HTMLFieldSetElement, Opacity, OpacityComponent>,
    ValueTextBoxComponentDelegator<OpacityComponent, Opacity> {

    public static OpacityComponent with(final String idPrefix) {
        return new OpacityComponent(
            CharSequences.failIfNullOrEmpty(idPrefix, "idPrefix"
            )
        );
    }

    private OpacityComponent(final String idPrefix) {
        this.textBox = ValueTextBoxComponent.with(
            Opacity::parse,
            Opacity::text
        );
        this.setId(idPrefix + this.name() + SpreadsheetElementIds.TEXT_BOX);
        this.setLabelFromPropertyName();
    }

    // HasName..........................................................................................................

    @Override
    public TextStylePropertyName<Opacity> name() {
        return TextStylePropertyName.OPACITY;
    }

    // ValueTextBoxComponentDelegator...................................................................................

    @Override
    public ValueTextBoxComponent<Opacity> valueTextBoxComponent() {
        return this.textBox;
    }

    private final ValueTextBoxComponent<Opacity> textBox;

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