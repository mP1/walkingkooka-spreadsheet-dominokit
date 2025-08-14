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
import walkingkooka.CanBeEmpty;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.color.Color;
import walkingkooka.color.RgbColor;
import walkingkooka.color.WebColorName;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.TestHtmlElementComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;
import walkingkooka.tree.text.TextNode;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Base class for an element {@link HtmlComponent}
 */
public abstract class HtmlElementComponent<E extends HTMLElement, C extends HtmlElementComponent<E, C>> extends HtmlElementComponentLike<E, C>
    implements TestHtmlElementComponent<E, C> {

    public static DivComponent div() {
        return new DivComponent();
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

    HtmlElementComponent(final String tag) {
        this.tag = tag;
        this.attributes = Maps.sorted(String.CASE_INSENSITIVE_ORDER);
        this.children = Lists.array();
        this.style = Maps.sorted();
        this.text = "";
    }

    @Override
    public final C setId(final String id) {
        CharSequences.failIfNullOrEmpty(id, "id");

        return this.setAttribute(
            "id",
            id
        );
    }

    @Override
    public final String id() {
        return this.attributes.get("id");
    }

    @Override
    public final C setTabIndex(final int tabIndex) {
        return this.setAttribute(
            "tabIndex",
            String.valueOf(tabIndex)
        );
    }

    private Integer tabIndex;

    @Override
    public final C setAttribute(final String name,
                                final String value) {
        this.attributes.put(
            name,
            value
        );
        return (C) this;
    }

    private Map<String, String> attributes;

    @Override
    public final C setCssProperty(final String name,
                                  final String value) {
        CharSequences.failIfNullOrEmpty(name, "name");
        Objects.requireNonNull(value, "value");

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
        Objects.requireNonNull(cssClass, "cssClass");

        this.classes.addAll(
            Lists.of(cssClass)
        );
        return (C) this;
    }

    @Override
    public final C removeCssClasses(final CssClass...cssClass) {
        Objects.requireNonNull(cssClass, "cssClass");

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
        Objects.requireNonNull(child, "child");

        this.children.add(child);
        return (C) this;
    }

    @Override
    public final C appendChild(final IsElement<?> child) {
        Objects.requireNonNull(child, "child");

        this.children.add(child);
        return (C) this;
    }

    @Override
    public C appendText(final String text) {
        Objects.requireNonNull(text, "text");

        this.children.add(text);
        return (C) this;
    }

    @Override
    public final C removeChild(final Node child) {
        Objects.requireNonNull(child, "child");

        this.children.remove(child);
        return (C) this;
    }

    @Override
    public final C removeChild(final IsElement<?> child) {
        Objects.requireNonNull(child, "child");

        this.children.remove(child);
        return (C) this;
    }

    @Override
    public final String text() {
        return this.text;
    }

    @Override
    public final C setText(final String text) {
        Objects.requireNonNull(text, "text");

        this.text = text;
        this.children.clear();
        return (C) this;
    }

    private String text;

    // listeners........................................................................................................

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
            final List<String> namesAndValues = Lists.array();

            {
                final Map<String, String> attributes = this.attributes;
                {
                    final String id = attributes.get("id");
                    if (null != id) {
                        namesAndValues.add(
                            "id=\"" + id + "\""
                        );
                    }
                }

                for(final Entry<String, String> attributeNameAndValue : attributes.entrySet()) {
                    final String name = attributeNameAndValue.getKey();
                    switch(name.toLowerCase()) {
                        case "id":
                            break;
                        default:
                            namesAndValues.add(
                                name + "=" + attributeNameAndValue.getValue()
                            );
                            break;
                    }
                }
            }

            {
                final Set<CssClass> cssClasses = this.classes;
                if (false == cssClasses.isEmpty()) {
                    namesAndValues.add(
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
                    namesAndValues.add(
                        "style=\"" +
                            style.entrySet()
                                .stream()
                                .map(entry -> entry.getKey() + ": " + tryRgbFunctionToHash(entry.getValue()))
                                .collect(Collectors.joining("; "))
                            + ";\""
                    );
                }
            }

            if (false == namesAndValues.isEmpty()) {
                printer.println(
                    namesAndValues.stream()
                        .collect(Collectors.joining(" "))
                );
                printer.indent();
            }

            {
                final String text = this.text;
                if(false == CharSequences.isNullOrEmpty(text)) {
                    printer.println(
                        CharSequences.quoteAndEscape(text)
                    );
                }
            }

            this.printTreeChildren(printer);

            if (false == namesAndValues.isEmpty()) {
                printer.outdent();
            }
        }
        printer.outdent();
    }

    // converts rgb(111,222,333) -> #112233
    private static String tryRgbFunctionToHash(final String value) {
        String out;

        try {
            final RgbColor color = Color.parseRgb(value);

            final WebColorName webColorName = color.toWebColorName()
                .orElse(null);
            if (null != webColorName) {
                out = webColorName.toString();
            } else {
                out = color.toHexString();
            }
        } catch (final IllegalArgumentException iae) {
            out = value;
        }

        return out;
    }

    @Override
    public final void printTreeChildren(final IndentingPrinter printer) {
        for (final Object child : this.children) {
            if (child instanceof CanBeEmpty) {
                final CanBeEmpty canBeEmpty = (CanBeEmpty) child;
                if (canBeEmpty.isEmpty()) {
                    continue;
                }
            }
            TreePrintable.printTreeOrToString(
                child,
                printer
            );
            printer.lineStart();
        }
    }

    private final String tag;

    private final List<Object> children;
}
