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

package walkingkooka.spreadsheet.dominokit.ui.card;

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.cards.Card;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.ui.ComponentWithChildren;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;
import java.util.Objects;

/**
 * A {@link Card} that auto hides when empty.
 */
public final class SpreadsheetCard implements HtmlElementComponent<HTMLDivElement, SpreadsheetCard>,
        TreePrintable,
        ComponentWithChildren<SpreadsheetCard, HTMLDivElement> {

    public static SpreadsheetCard empty() {
        return new SpreadsheetCard();
    }

    private SpreadsheetCard() {
        this.card = Card.create();
        this.children = Lists.array();
    }

    public SpreadsheetCard setTitle(final String title) {
        Objects.requireNonNull(title, "title");

        this.card.setTitle(title);
        return this;
    }

    @Override
    public SpreadsheetCard appendChild(final IsElement<?> child) {
        this.card.appendChild(child);
        this.card.setDisplay("");
        this.children.add(child);
        return this;
    }

    /**
     * Removes an existing child.
     */
    @Override
    public SpreadsheetCard removeChild(final int index) {
        final IsElement<?> child = this.children.remove(index);
        this.card.getAppendTarget()
                .removeChild(child.element());
        return this;
    }

    /**
     * Getter that returns all children.
     */
    @Override
    public List<IsElement<?>> children() {
        return Lists.immutable(
                this.children
        );
    }

    /**
     * Holds all added child components.
     */
    private List<IsElement<?>> children;

    // Component........................................................................................................

    @Override
    public HTMLDivElement element() {
        return this.card.element();
    }

    private final Card card;

    // TreePrintable....................................................................................................

    // SpreadsheetCard
    //   title
    //     this.card
    //       XXX
    //
    // Spreadsheet
    //   this.card
    //     XXX
    @Override
    public void printTree(final IndentingPrinter printer) {
        if (false == this.isEmpty()) {
            printer.println("SpreadsheetCard");

            printer.indent();
            {
                TreePrintable.printTreeOrToString(
                        this.card,
                        printer
                );
            }
            printer.outdent();
        }
    }
}
