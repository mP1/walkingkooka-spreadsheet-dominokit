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

package walkingkooka.spreadsheet.dominokit.ui.flexlayout;

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.elements.DivElement;
import org.dominokit.domino.ui.style.SpacingCss;
import org.dominokit.domino.ui.utils.ElementsFactory;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;

/**
 * A very basic attempt at re-creating the old DominoUI 1.x FlexLayout.
 */
public class SpreadsheetFlexLayout implements SpreadsheetFlexLayoutLike {

    public static SpreadsheetFlexLayout emptyColumn() {
        return new SpreadsheetFlexLayout()
                .column();
    }

    public static SpreadsheetFlexLayout emptyRow() {
        return new SpreadsheetFlexLayout()
                .row();
    }

    private SpreadsheetFlexLayout() {
        this.children = Lists.array();
    }

    private SpreadsheetFlexLayout column() {
        this.div.addCss(
                SpacingCss.dui_flex_col,
                //SpacingCss.dui_v_full,
                SpacingCss.dui_items_start,
                SpacingCss.dui_gap_4);
        return this;
    }

    private SpreadsheetFlexLayout row() {
        this.div.addCss(
                SpacingCss.dui_flex_row,
                SpacingCss.dui_h_full,
                SpacingCss.dui_items_start,
                SpacingCss.dui_gap_4);
        return this;
    }

    // children.........................................................................................................

    @Override
    public SpreadsheetFlexLayout appendChild(final IsElement<?> child) {
        this.div.element()
                .appendChild(
                        child.element()
                );
        this.children.add(child);
        return this;
    }

    /**
     * Removes an existing child.
     */
    @Override
    public SpreadsheetFlexLayout removeChild(final int index) {
        final IsElement<?> child = this.children.remove(index);
        this.div.element()
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

    // IsElement........................................................................................................

    @Override
    public HTMLDivElement element() {
        return this.div.element();
    }

    private final DivElement div = ElementsFactory.elements.div();

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            for (final IsElement<?> child : this.children) {
                TreePrintable.printTreeOrToString(
                        child,
                        printer
                );
            }
        }
        printer.outdent();
    }
}
