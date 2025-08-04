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

package walkingkooka.spreadsheet.dominokit.tooltip;

import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

/**
 * Defines a public contract for a {@link TooltipComponent}.
 */
public interface TooltipComponentLike extends TreePrintable {

    /**
     * Getter that returns the text content of this tooltip.
     */
    String textContent();

    /**
     * Sets or replaces the text content of this tooltip.
     */
    TooltipComponentLike setTextContent(final String text);

    /**
     * Removes the tooltip.
     */
    void detach();

    // TreePrintable....................................................................................................

    @Override
    default void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            printer.println(
                CharSequences.quoteAndEscape(
                    this.textContent()
                )
            );
        }
        printer.outdent();
    }
}
