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
import org.dominokit.domino.ui.style.CssClass;
import org.dominokit.domino.ui.style.Style;
import org.dominokit.domino.ui.utils.ElementUtil;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.TextNode;

import java.util.Objects;

/**
 * Base class for an element {@link HtmlComponent}
 */
public abstract class HtmlElementComponent<E extends HTMLElement, C extends HtmlElementComponent<E, C>> extends HtmlElementComponentLike<E, C> {

    public static DivComponent div() {
        return new DivComponent();
    }

    public static LiComponent li() {
        return new LiComponent();
    }

    public static TableComponent table() {
        return new TableComponent();
    }

    public static TBodyComponent tbody() {
        return new TBodyComponent();
    }

    public static TdComponent td() {
        return new TdComponent();
    }

    public static ThComponent th() {
        return new ThComponent();
    }

    public static THeadComponent thead() {
        return new THeadComponent();
    }

    public static TrComponent tr() {
        return new TrComponent();
    }

    public static UlComponent ul() {
        return new UlComponent();
    }

    HtmlElementComponent(final String tag) {
        this(
            Js.<E>cast(
                DomGlobal.document.createElement(tag)
            )
        );
    }

    HtmlElementComponent(final E element) {
        super();

        this.element = element;
    }

    // width............................................................................................................

    @Override
    public int width() {
        return this.element.offsetWidth;
    }

    // height...........................................................................................................

    @Override
    public int height() {
        return this.element.offsetHeight;
    }

    // id...............................................................................................................

    @Override
    public final C setId(final String id) {
        this.element.id = id;
        return (C) this;
    }

    @Override
    public final String id() {
        return this.element.id;
    }

    @Override
    public final C setTabIndex(final int tabIndex) {
        this.element.tabIndex = tabIndex;
        return (C) this;
    }

    @Override
    public final C setAttribute(final String name,
                                final String value) {
        this.element.setAttribute(name, value);
        return (C) this;
    }

    @Override
    public final C setCssProperty(final String name,
                                  final String value) {
        this.element.style.setProperty(name, value);
        return (C) this;
    }

    @Override
    public final C removeCssProperty(final String name) {
        this.element.style.removeProperty(name);
        return (C) this;
    }

    @Override
    public final C setCssText(final String cssText) {
        this.element.style.cssText = cssText;
        return (C) this;
    }

    @Override
    public final C addCssClasses(final CssClass...cssClass) {
        this.style().addCss(cssClass);
        return (C) this;
    }

    @Override
    public final C removeCssClasses(final CssClass...cssClass) {
        this.style().removeCss(cssClass);
        return (C) this;
    }

    private Style style() {
        return Style.of(this.element);
    }

    @Override
    public final C clear() {
        ElementUtil.clear(this.element);
        return (C) this;
    }

    @Override
    public boolean contains(final IsElement<?> element) {
        Objects.requireNonNull(element, "element");

        return this.element.contains(element.element());
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
    public C appendText(final String text) {
        return this.appendChild(
            Doms.textNode(text)
        );
    }

    @Override
    public C removeChild(final Node child) {
        this.element.removeChild(child);
        return (C) this;
    }

    @Override
    public C removeChild(final IsElement<?> child) {
        this.element.removeChild(child.element());
        return (C) this;
    }

    // text.............................................................................................................

    @Override
    public final String text() {
        return this.element()
            .textContent;
    }

    @Override
    public final C setText(final String text) {
        Objects.requireNonNull(text, "text");

        this.element()
            .textContent = text;
        return (C) this;
    }

    // IsElement........................................................................................................

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
