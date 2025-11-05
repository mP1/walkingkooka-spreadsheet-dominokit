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

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.events.EventType;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTreePrintable;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * A text box component that includes support for finding values that match the entered search text.
 */
abstract class SuggestBoxComponentLike<T> implements FormValueComponent<HTMLFieldSetElement, T, SuggestBoxComponent<T>>,
    FormValueComponentTreePrintable<HTMLFieldSetElement, SuggestBoxComponent<T>, T> {

    abstract public SuggestBoxComponent<T> setStringValue(final Optional<String> value);

    abstract public Optional<String> stringValue();

    abstract public List<T> options();

    abstract public SuggestBoxComponent<T> setOptions(final List<T> options);

    abstract public SuggestBoxComponent<T> setVerifiedOption(final T option);

    @Override
    public final SuggestBoxComponent<T> addBlurListener(final EventListener listener) {
        return this.addEventListener(
            EventType.blur,
            listener
        );
    }

    @Override
    public final SuggestBoxComponent<T> addClickListener(final EventListener listener) {
        return this.addEventListener(
            EventType.click,
            listener
        );
    }

    @Override
    public final SuggestBoxComponent<T> addContextMenuListener(final EventListener listener) {
        return this.addEventListener(
            EventType.contextmenu,
            listener
        );
    }

    @Override
    public final SuggestBoxComponent<T> addFocusListener(final EventListener listener) {
        return this.addEventListener(
            EventType.focus,
            listener
        );
    }

    @Override
    public final SuggestBoxComponent<T> addInputListener(final EventListener listener) {
        return this.addEventListener(
            EventType.input,
            listener
        );
    }

    @Override
    public final SuggestBoxComponent<T> addKeyDownListener(final EventListener listener) {
        return this.addEventListener(
            EventType.keydown,
            listener
        );
    }

    @Override
    public final SuggestBoxComponent<T> addKeyUpListener(final EventListener listener) {
        return this.addEventListener(
            EventType.keyup,
            listener
        );
    }

    abstract SuggestBoxComponent<T> addEventListener(final EventType eventType,
                                                     final EventListener listener);

    // FormValueComponentTreePrintable..................................................................................

    @Override
    public final void treePrintAlternateValues(final IndentingPrinter printer) {
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
