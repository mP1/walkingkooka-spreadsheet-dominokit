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

package walkingkooka.spreadsheet.dominokit.card;

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.cards.Card;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import org.dominokit.domino.ui.utils.PostfixAddOn;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.ComponentWithChildren;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.text.SpreadsheetTextBox;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link Card} that auto hides when empty.
 */
public final class SpreadsheetCard implements HtmlElementComponent<HTMLDivElement, SpreadsheetCard>,
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

        // TODO need to discover how to remove a title because setTitle(null) still shows an empty header
        if (false == CharSequences.isNullOrEmpty(title)) {
            this.card.setTitle(title);
        }
        return this;
    }

    public SpreadsheetCard show() {
        this.card.show();
        return this;
    }

    public SpreadsheetCard hide() {
        this.card.hide();
        return this;
    }

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetCard setCssText(final String css) {
        Objects.requireNonNull(css, "css");

        this.card.style()
            .cssText(css);
        return this;
    }

    // setCssProperty...................................................................................................

    @Override
    public SpreadsheetCard setCssProperty(final String name,
                                          final String value) {
        this.card.style()
            .setCssProperty(
                name,
                value
            );
        return this;
    }

    // filter...........................................................................................................

    /**
     * Adds a {@link ChangeListener}, automatically adding a filter text box lazily. The textbox will occupy the
     * right third of the card header.
     */
    public SpreadsheetCard setFilterValueChangeListener(final ChangeListener<Optional<String>> changeListener) {
        Objects.requireNonNull(changeListener, "changeListener");

        if (null == this.filter) {
            this.filter = SpreadsheetTextBox.empty()
                .clearIcon();
            this.card.withHeader(
                (card, header) -> header.appendChild(
                    PostfixAddOn.of(
                        this.filter.setCssText("display: inline-block; width: 33%; background-color: "))
                )
            );
        }
        this.filter.addChangeListener(changeListener);

        return this;
    }

    /**
     * Getter that returns the current filter text.
     */
    public Optional<String> filterValue() {
        return this.filter.value();
    }

    public SpreadsheetCard setFilterValue(final Optional<String> value) {
        this.filter.setValue(value);
        return this;
    }

    private SpreadsheetTextBox filter;

    // ComponentWithChildren............................................................................................

    @Override
    public SpreadsheetCard appendChild(final IsElement<?> child) {
        this.card.appendChild(child);
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
    private final List<IsElement<?>> children;

    // CanBeEmpty.......................................................................................................

    @Override
    public boolean isEmpty() {
        return this.isEmptyIfChildrenAreEmpty();
    }

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
        if (this.isNotEmpty()) {
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
