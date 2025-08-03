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

package walkingkooka.spreadsheet.dominokit.dom;

import elemental2.dom.DomGlobal;
import elemental2.dom.EventListener;
import elemental2.dom.HTMLElement;
import elemental2.dom.Node;
import jsinterop.base.Js;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.utils.ElementUtil;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.TextNode;

/**
 * Base class for an element {@link HtmlElementComponent}
 */
public abstract class SpreadsheetElementComponent<E extends HTMLElement, C extends SpreadsheetElementComponent<E, C>> extends SpreadsheetElementComponentLike<E, C> {

    public static SpreadsheetDivComponent div() {
        return new SpreadsheetDivComponent();
    }

    public static SpreadsheetTableComponent table() {
        return new SpreadsheetTableComponent();
    }

    public static SpreadsheetTBodyComponent tbody() {
        return new SpreadsheetTBodyComponent();
    }

    public static SpreadsheetTdComponent td() {
        return new SpreadsheetTdComponent();
    }

    public static SpreadsheetThComponent th() {
        return new SpreadsheetThComponent();
    }

    public static SpreadsheetTHeadComponent thead() {
        return new SpreadsheetTHeadComponent();
    }

    public static SpreadsheetTrComponent tr() {
        return new SpreadsheetTrComponent();
    }

    SpreadsheetElementComponent(final String tag) {
        this(
            Js.<E>cast(
                DomGlobal.document.createElement(tag)
            )
        );
    }

    SpreadsheetElementComponent(final E element) {
        super();

        this.element = element;
    }

    @Override
    public final C setId(final String id) {
        this.element.id = id;
        return (C) this;
    }

    @Override
    public final C setTabIndex(final int tabIndex) {
        this.element.tabIndex = tabIndex;
        return (C) this;
    }

    @Override
    public final C setCssProperty(final String name,
                                  final String value) {
        this.element.style.setProperty(name, value);
        return (C) this;
    }

    @Override
    public final C setCssText(final String cssText) {
        this.element.style.cssText = cssText;
        return (C) this;
    }

    @Override
    public final C clear() {
        ElementUtil.clear(this.element);
        return (C) this;
    }

    @Override
    public C appendChild(final TextNode textNode) {
        this.element.appendChild(
            Doms.node(textNode)
        );
        return (C) this;
    }

    @Override
    public C appendChild(final Node child) {
        this.element.appendChild(child);
        return (C) this;
    }

    @Override
    public C appendChild(final IsElement<?> child) {
        this.element.appendChild(child.element());
        return (C) this;
    }

    @Override
    public C addEventListener(final String type,
                              final EventListener listener) {
        this.element.addEventListener(type, listener);
        return (C) this;
    }

    // IsElement........................................................................................................

    @Override
    public E element() {
        return this.element;
    }

    private final E element;

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.printTreeChildren(printer);
        }
        printer.outdent();
    }

    @Override
    public void printTreeChildren(final IndentingPrinter printer) {
        printer.println(this.element.outerHTML);
    }
}
