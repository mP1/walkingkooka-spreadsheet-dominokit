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

package walkingkooka.spreadsheet.dominokit.value;

import elemental2.dom.HTMLElement;
import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.spreadsheet.dominokit.select.SelectComponent;
import walkingkooka.spreadsheet.dominokit.value.text.TextBoxComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;
import java.util.Optional;

/**
 * Provides a default {@link TreePrintable#printTree(IndentingPrinter)}.
 */
public interface FormValueComponentTreePrintable<E extends HTMLElement, C extends FormValueComponent<E, V, C>, V> extends FormValueComponent<E, V, C> {

    // TreePrintable....................................................................................................

    @Override
    default void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            final String label = this.label();
            if (null != label) {
                printer.print(label);
                printer.print(" ");
            }

            // [$VALUE]
            printer.print("[");
            printer.print(
                this.value()
                    .map(Object::toString)
                    .orElse("")
            );
            printer.print("]");

            String separator = " ";

            if (this instanceof TextBoxComponent) {
                final TextBoxComponent textBoxComponent = (TextBoxComponent) this;

                final Icon<?> icon = textBoxComponent.icon()
                    .orElse(null);
                if (null != icon) {
                    printer.print(separator);
                    printer.print("icon=");
                    printer.print(icon.getName());
                    separator = " ";
                }
            }

            final String id = this.id();
            if (null != id) {
                printer.print(separator);
                printer.print("id=");
                printer.print(id);
                separator = " ";
            }

            final Optional<String> helperText = this.helperText();
            if (helperText.isPresent()) {
                printer.print(separator);
                printer.print("helperText=");
                printer.print(CharSequences.quoteAndEscape(helperText.get()));
                separator = " ";
            }

            if (this.isDisabled()) {
                printer.print(separator);
                printer.print("DISABLED");
                separator = " ";
            }

            if (this.isRequired()) {
                printer.print(separator);
                printer.print("REQUIRED");
            }
            printer.println();

            this.treePrintAlternateValues(printer);

            final List<String> errors = this.errors();
            if (false == errors.isEmpty()) {
                printer.println("Errors");
                printer.indent();
                {
                    for (final String error : errors) {
                        printer.println(error);
                    }
                }
                printer.outdent();
            }
        }
        printer.outdent();
    }

    /**
     * Provides an opportunity for {@link SelectComponent} to print
     * drop down values.
     */
    void treePrintAlternateValues(final IndentingPrinter printer);
}
