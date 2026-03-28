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

package walkingkooka.spreadsheet.dominokit.border;

import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentDelegator;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.Border;
import walkingkooka.tree.text.BoxEdge;

import java.util.Objects;

/**
 * A text box that accepts text entry and validates it as a {@link Border} limited to the given {@link BoxEdge}.
 */
public final class BorderBoxEdgeComponent implements ValueTextBoxComponentDelegator<BorderBoxEdgeComponent, Border> {

    public static BorderBoxEdgeComponent empty(final BoxEdge boxEdge) {
        return new BorderBoxEdgeComponent(
            Objects.requireNonNull(boxEdge, "boxEdge")
        );
    }

    private BorderBoxEdgeComponent(final BoxEdge boxEdge) {
        this.textBox = ValueTextBoxComponent.with(
            boxEdge::parseBorder,
            Border::valuesAsText
        );

        this.boxEdge = boxEdge;
    }

    // ValueTextBoxComponentDelegator...................................................................................

    @Override
    public ValueTextBoxComponent<Border> valueTextBoxComponent() {
        return this.textBox;
    }

    private final ValueTextBoxComponent<Border> textBox;

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            printer.println(this.boxEdge.name());

            printer.indent();
            {
                this.valueTextBoxComponent()
                    .printTree(printer);
            }
            printer.outdent();
        }
        printer.outdent();
    }

    private final BoxEdge boxEdge;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}