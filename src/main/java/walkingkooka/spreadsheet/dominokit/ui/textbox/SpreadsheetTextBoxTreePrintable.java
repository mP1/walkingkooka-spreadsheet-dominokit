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

package walkingkooka.spreadsheet.dominokit.ui.textbox;

import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.ui.ValueComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Provides a default {@link TreePrintable#printTree(IndentingPrinter)}.
 */
public interface SpreadsheetTextBoxTreePrintable<V extends ValueComponent<HTMLFieldSetElement, T, V>, T> extends ValueComponent<HTMLFieldSetElement, T, V> {

    // TreePrintable....................................................................................................

    @Override
    default void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            final List<String> components = Lists.array();

            final String label = this.label();
            if (null != label) {
                components.add(label);
            }

            components.add(
                    "[" +
                            this.value()
                                    .map(Object::toString)
                                    .orElse("") +
                            "]"
            );

            final String id = this.id();
            if (null != id) {
                components.add("id=" + id);
            }

            final Optional<String> helperText = this.helperText();
            if (helperText.isPresent()) {
                components.add("helperText=" + CharSequences.quoteAndEscape(helperText.get()));
            }

            if (this.isDisabled()) {
                components.add("DISABLED");
            }

            if (this.isRequired()) {
                components.add("REQUIRED");
            }

            printer.println(
                    components.stream()
                            .collect(Collectors.joining(" "))
            );

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
     * Provides an opportunity for {@link walkingkooka.spreadsheet.dominokit.ui.select.SpreadsheetSelectComponent} to print
     * drop down values.
     */
    void treePrintAlternateValues(final IndentingPrinter printer);
}
