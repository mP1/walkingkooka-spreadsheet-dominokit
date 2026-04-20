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

package walkingkooka.spreadsheet.dominokit.grid;

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.IsElement;
import walkingkooka.ToStringBuilder;
import walkingkooka.ToStringBuilderOption;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.ComponentWithChildren;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.dom.DivComponent;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;

/**
 * A container component with 4 columns, using a CSS grid.
 */
public final class FourColumnComponent implements HtmlComponentDelegator<HTMLDivElement, FourColumnComponent>,
    ComponentWithChildren<FourColumnComponent, HTMLDivElement> {

    public static FourColumnComponent empty() {
        return new FourColumnComponent();
    }

    private FourColumnComponent() {
        super();
        this.div = DivComponent.div()
            .setCssText("display: grid; grid-template-columns: 25% 25% 25% 25%; gap: 5px;");
        this.children = Lists.array();
    }

    // id...............................................................................................................

    @Override
    public FourColumnComponent setId(final String id) {
        this.div.setId(id);
        return this;
    }

    @Override
    public String id() {
        return this.div.id();
    }

    // children.........................................................................................................

    @Override
    public FourColumnComponent appendChild(final IsElement<?> child) {
        this.div.appendChild(child);
        this.children.add(child);
        return this;
    }

    /**
     * Removes an existing child.
     */
    @Override
    public FourColumnComponent removeChild(final int index) {
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

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return false;
    }

    // HtmlElementDelegator.............................................................................................

    @Override
    public HtmlComponent<HTMLDivElement, ?> htmlComponent() {
        return this.div;
    }

    private final DivComponent div;

    // CanBeEmpty.......................................................................................................

    @Override
    public boolean isEmpty() {
        return this.isEmptyIfChildrenAreEmpty();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.div.printTree(printer);
        }
        printer.outdent();
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return ToStringBuilder.empty()
            .disable(ToStringBuilderOption.QUOTE)
            .value(this.getClass().getSimpleName())
            .value(this.div)
            .build();
    }
}
