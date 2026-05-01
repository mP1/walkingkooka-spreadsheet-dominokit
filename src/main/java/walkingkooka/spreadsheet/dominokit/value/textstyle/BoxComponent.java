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

package walkingkooka.spreadsheet.dominokit.value.textstyle;

import elemental2.dom.HTMLDivElement;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.value.ValueComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

/**
 * A simple component that displays a box and supports coloring or other styles from any value set upon it.
 */
public interface BoxComponent<V, C extends BoxComponent<V, C>> extends ValueComponent<HTMLDivElement, V, C>,
    HtmlComponentDelegator<HTMLDivElement, C>,
    TreePrintable {

    String DEFAULT_BOX_COMPONENT_CSS_TEXT = "width: 20px; height: 20px; border-color: black; border-style: solid; border-width: 1px;";

    @Override
    default Runnable addValueWatcher(final ValueWatcher<V> watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    default boolean isDisabled() {
        return false;
    }

    @Override
    default C setDisabled(final boolean disabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    default C hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    default C removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    default C removePadding() {
        throw new UnsupportedOperationException();
    }

    @Override
    default boolean isEditing() {
        return false;
    }
    
    // TreePrintable....................................................................................................

    @Override
    default void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());

        final V valueOrNull = this.value()
            .orElse(null);
        if(null != valueOrNull) {

            printer.indent();
            {
                TreePrintable.printTreeOrToString(
                    valueOrNull,
                    printer
                );
            }
            printer.outdent();
        }
    }
}
