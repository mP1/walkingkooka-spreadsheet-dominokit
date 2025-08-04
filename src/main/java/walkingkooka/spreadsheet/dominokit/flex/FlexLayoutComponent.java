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
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.style.CssClass;
import org.dominokit.domino.ui.style.SpacingCss;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.ComponentWithChildren;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.dom.DivComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Objects;

/**
 * A very basic attempt at re-creating the old DominoUI 1.x FlexLayout.
 */
public class FlexLayoutComponent implements HtmlComponent<HTMLDivElement, FlexLayoutComponent>,
    ComponentWithChildren<FlexLayoutComponent, HTMLDivElement> {

    private final static CssClass GAP = SpacingCss.dui_gap_1;

    public static FlexLayoutComponent column() {
        final FlexLayoutComponent flex = new FlexLayoutComponent(true);
        flex.div.addCssClasses(
            SpacingCss.dui_flex_col,
            //SpacingCss.dui_v_full,
            SpacingCss.dui_items_start,
            GAP
        );
        return flex;
    }

    public static FlexLayoutComponent row() {
        final FlexLayoutComponent flex = new FlexLayoutComponent(false);
        flex.div.setCssText("display:flex; flex-wrap: wrap;");
        flex.div.addCssClasses(
            SpacingCss.dui_flex_row,
            SpacingCss.dui_h_full,
            SpacingCss.dui_items_start,
            GAP
        );
        return flex;
    }

    private FlexLayoutComponent(final boolean column) {
        super();
        this.children = Lists.array();
        this.column = column;
    }

    public boolean isColumn() {
        return this.column;
    }

    private final boolean column;

    // displayBlock.....................................................................................................

    public FlexLayoutComponent displayBlock() {
        this.div.setDisplay("block");
        return this;
    }

    // id...............................................................................................................

    public FlexLayoutComponent setId(final String id) {
        this.div.setId(id);
        return this;
    }

    public String id() {
        return this.div.id();
    }

    // children.........................................................................................................

    @Override
    public FlexLayoutComponent appendChild(final IsElement<?> child) {
        this.div.appendChild(child);
        this.children.add(child);
        return this;
    }

    /**
     * Removes an existing child.
     */
    @Override
    public FlexLayoutComponent removeChild(final int index) {
        final IsElement<?> child = this.children.remove(index);
        this.div.removeChild(child);
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
    public FlexLayoutComponent setCssText(final String css) {
        Objects.requireNonNull(css, "css");

        this.div.setCssText(css);
        return this;
    }

    // setCssProperty...................................................................................................

    @Override
    public FlexLayoutComponent setCssProperty(final String name,
                                              final String value) {
        this.div.setCssProperty(
            name,
            value
        );
        return this;
    }

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return false;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLDivElement element() {
        return this.div.element();
    }

    // CanBeEmpty.......................................................................................................

    @Override
    public final boolean isEmpty() {
        return this.isEmptyIfChildrenAreEmpty();
    }

    // TreePrintable....................................................................................................

    // FlexLayoutComponent
    //  ROW
    //    id=Id123
    //      TextBoxComponent
    //        [Value111]
    //      TextBoxComponent
    //        [Value222]
    @Override
    public final void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            printer.println(this.isColumn() ? "COLUMN" : "ROW");
            {
                final String id = this.id();
                if (false == CharSequences.isNullOrEmpty(id)) {
                    printer.indent();
                    printer.println("id=" + id);
                }
                printer.indent();
                {
                    this.div.printTreeChildren(printer);
                }
                printer.outdent();
            }
        }
        printer.outdent();
    }

    private final DivComponent div = DivComponent.div();
}
