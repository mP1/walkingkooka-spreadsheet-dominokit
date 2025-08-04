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

package walkingkooka.spreadsheet.dominokit.suggestbox;

import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.spreadsheet.dominokit.text.FormValueComponentTreePrintable;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * A text box component that includes support for finding values that match the entered search text.
 */
public interface SuggestBoxComponentLike<T> extends FormValueComponent<HTMLFieldSetElement, T, SuggestBoxComponent<T>>,
    FormValueComponentTreePrintable<SuggestBoxComponent<T>, T> {

    SuggestBoxComponent<T> setStringValue(final Optional<String> value);

    Optional<String> stringValue();

    List<T> options();

    SuggestBoxComponent<T> setOptions(final List<T> options);

    SuggestBoxComponent<T> setVerifiedOption(final T option);

    // FormValueComponentTreePrintable..................................................................................

    @Override
    default void treePrintAlternateValues(final IndentingPrinter printer) {
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
