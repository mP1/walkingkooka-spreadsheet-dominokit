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

package walkingkooka.spreadsheet.dominokit.tagbox;

import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A text box component that includes support for finding values that match the entered search text.
 */
abstract class TagBoxComponentLike<T> implements FormValueComponent<HTMLFieldSetElement, List<T>, TagBoxComponent<T>> {

    abstract public TagBoxComponent<T> setStringValue(final Optional<String> value);

    abstract public Optional<String> stringValue();

    abstract public List<T> options();

    abstract public TagBoxComponent<T> setOptions(final List<T> options);

    abstract public TagBoxComponent<T> setVerifiedOption(final T option);

    // TreePrintable....................................................................................................

    @Override
    public final void printTree(final IndentingPrinter printer) {
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
                        .map(
                            v -> v.stream()
                                .map(Object::toString)
                                .collect(Collectors.joining(","))
                        )
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

            this.treeOptions(printer);

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

    private void treeOptions(final IndentingPrinter printer) {
        final Collection<T> options = this.options();
        if (false == options.isEmpty()) {
            printer.println("options");

            printer.indent();
            {
                for (final T option : options) {
                    TreePrintable.printTreeOrToString(
                        option,
                        printer
                    );
                }
            }
            printer.outdent();
        }
    }
}
