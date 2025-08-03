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

import elemental2.dom.EventListener;
import elemental2.dom.HTMLElement;
import elemental2.dom.Node;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.style.CssClass;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.color.Color;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.TestHtmlElementComponent;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;
import walkingkooka.tree.text.TextNode;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Base class for an element {@link HtmlElementComponent}
 */
public abstract class SpreadsheetElementComponent<E extends HTMLElement, C extends SpreadsheetElementComponent<E, C>> extends SpreadsheetElementComponentLike<E, C>
    implements TestHtmlElementComponent<E, C> {

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
        this.tag = tag;
        this.children = Lists.array();
        this.style = Maps.sorted();
    }

    @Override
    public final C setId(final String id) {
        this.id = id;
        return (C) this;
    }

    private String id;

    @Override
    public final C setTabIndex(final int tabIndex) {
        this.tabIndex = tabIndex;
        return (C) this;
    }

    private Integer tabIndex;

    @Override
    public final C setCssProperty(final String name,
                                  final String value) {
        this.style.put(
            name,
            value
        );
        return (C) this;
    }

    @Override
    public final C setCssText(final String cssText) {
        Objects.requireNonNull(cssText, "cssText");

        for(final String nameAndValue : cssText.split(";")) {
            if(nameAndValue.trim().isEmpty()) {
                continue;
            }
            final int separatorIndex = nameAndValue.lastIndexOf(':');
            final String name = nameAndValue.substring(0, separatorIndex)
                .trim();
            final String value = nameAndValue.substring(separatorIndex + 1)
                .trim();

            this.style.put(
                name,
                value
            );
        }
        return (C) this;
    }

    private Map<String, String> style;

    @Override
    public final C addCssClasses(final CssClass...cssClass) {
        this.classes.addAll(
            Lists.of(cssClass)
        );
        return (C) this;
    }

    @Override
    public final C removeCssClasses(final CssClass...cssClass) {
        this.classes.removeAll(
            Lists.of(cssClass)
        );
        return (C) this;
    }

    private final Set<CssClass> classes = Sets.ordered();

    @Override
    public final C clear() {
        this.children.clear();
        return (C) this;
    }

    @Override
    public final C appendChild(final TextNode textNode) {
        Objects.requireNonNull(textNode, "textNode");

        this.children.add(textNode);
        return (C) this;
    }

    @Override
    public final C appendChild(final Node child) {
        this.children.add(child);
        return (C) this;
    }

    @Override
    public final C appendChild(final IsElement<?> child) {
        this.children.add(child);
        return (C) this;
    }

    @Override
    public final C removeChild(final Node child) {
        this.children.remove(child);
        return (C) this;
    }

    @Override
    public final C removeChild(final IsElement<?> child) {
        this.children.remove(child);
        return (C) this;
    }

    @Override
    public C addEventListener(final String type,
                              final EventListener listener) {
        return (C) this;
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.tag);
        printer.indent();
        {
            final List<String> attributes = Lists.array();

            {
                final String id = this.id;
                if (null != id) {
                    attributes.add(
                        "id=\"" + id + "\""
                    );
                }
            }

            {
                final Integer tabIndex = this.tabIndex;
                if (null != tabIndex) {
                    attributes.add(
                        "tabIndex=" + tabIndex
                    );
                }
            }

            {
                final Set<CssClass> cssClasses = this.classes;
                if (false == cssClasses.isEmpty()) {
                    attributes.add(
                        "class=\"" +
                            cssClasses.stream()
                                .map(CssClass::getCssClass)
                                .collect(Collectors.joining(" "))
                            + "\""
                    );
                }
            }

            {
                final Map<String, String> style = this.style;
                if (false == style.isEmpty()) {
                    attributes.add(
                        "style=\"" +
                            style.entrySet()
                                .stream()
                                .map(entry -> entry.getKey() + ": " + tryRgbFunctionToHash(entry.getValue()))
                                .collect(Collectors.joining("; "))
                            + ";\""
                    );
                }
            }

            if (false == attributes.isEmpty()) {
                printer.println(
                    attributes.stream()
                        .collect(Collectors.joining(" "))
                );
                printer.indent();
            }

            this.printTreeChildren(printer);

            if (false == attributes.isEmpty()) {
                printer.outdent();
            }
        }
        printer.outdent();
    }

    // converts rgb(111,222,333) -> #112233
    private static String tryRgbFunctionToHash(final String value) {
        String out;

        try {
            out = Color.parseRgb(value)
                .toHexString();
        } catch (final IllegalArgumentException iae) {
            out = value;
        }

        return out;
    }

    @Override
    public final void printTreeChildren(final IndentingPrinter printer) {
        for(final Object child : this.children) {
            TreePrintable.printTreeOrToString(
                child,
                printer
            );
        }
    }

    private final String tag;

    private final List<Object> children;
}
