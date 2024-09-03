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

package walkingkooka.spreadsheet.dominokit.flex;

import elemental2.dom.HTMLDivElement;
import elemental2.dom.Node;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.elements.DivElement;
import org.dominokit.domino.ui.style.CssClass;
import org.dominokit.domino.ui.style.SpacingCss;
import org.dominokit.domino.ui.utils.ElementsFactory;
import walkingkooka.collect.list.Lists;

import java.util.List;
import java.util.Objects;

/**
 * A very basic attempt at re-creating the old DominoUI 1.x FlexLayout.
 */
public class SpreadsheetFlexLayout implements SpreadsheetFlexLayoutLike {

    private final static CssClass GAP = SpacingCss.dui_gap_1;

    public static SpreadsheetFlexLayout column() {
        final SpreadsheetFlexLayout flex = new SpreadsheetFlexLayout(true);
        flex.div.addCss(
                SpacingCss.dui_flex_col,
                //SpacingCss.dui_v_full,
                SpacingCss.dui_items_start,
                GAP
        );
        return flex;
    }

    public static SpreadsheetFlexLayout row() {
        final SpreadsheetFlexLayout flex = new SpreadsheetFlexLayout(false);
        flex.div.style()
                .cssText("display:flex; flex-wrap: wrap;");
        flex.div.addCss(
                SpacingCss.dui_flex_row,
                SpacingCss.dui_h_full,
                SpacingCss.dui_items_start,
                GAP
        );
        return flex;
    }

    private SpreadsheetFlexLayout(final boolean column) {
        super();
        this.children = Lists.array();
        this.column = column;
    }

    @Override
    public boolean isColumn() {
        return this.column;
    }

    private final boolean column;

    // displayBlock.....................................................................................................

    @Override
    public SpreadsheetFlexLayout displayBlock() {
        this.element()
                .style
                .display = "block";
        return this;
    }

    // id...............................................................................................................

    @Override
    public SpreadsheetFlexLayout setId(final String id) {
        this.div.setId(id);
        return this;
    }

    @Override
    public String id() {
        return this.div.getId();
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
    private final List<IsElement<?>> children;

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetFlexLayout setCssText(final String css) {
        Objects.requireNonNull(css, "css");

        this.div.cssText(css);
        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLDivElement element() {
        return this.div.element();
    }

    // node.............................................................................................................

    @Override
    public Node node() {
        return this.element();
    }

    private final DivElement div = ElementsFactory.elements.div();
}
