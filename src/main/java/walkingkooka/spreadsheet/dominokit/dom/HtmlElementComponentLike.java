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
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.style.CssClass;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.text.HasText;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStylePropertyName;

/**
 * Base class for an element {@link HtmlComponent}
 */
abstract class HtmlElementComponentLike<E extends HTMLElement, C extends HtmlElementComponentLike<E, C>> implements HtmlComponent<E, C>,
    HasText {

    HtmlElementComponentLike() {
        super();
    }

    public abstract C setId(final String id);

    public abstract String id();

    public abstract C setTabIndex(final int tabIndex);

    public final C setAttribute(final String name,
                                final int value) {
        return this.setAttribute(
            name,
            String.valueOf(value)
        );
    }

    public abstract C setAttribute(final String name,
                                   final String value);

    public final C setBackgroundColor(final String color) {
        return this.setCssProperty(
            "background-color",
            color
        );
    }

    public final C setColor(final String color) {
        return this.setCssProperty(
            "color",
            color
        );
    }
    
    public final C setDisplay(final String display) {
        return this.setCssProperty(
            "display",
            display
        );
    }

    public final C setHeight(final String height) {
        return this.setCssProperty(
            "height",
            height
        );
    }
    
    public final C setLeft(final String left) {
        return this.setCssProperty(
            "left",
            left
        );
    }

    public final C setMargin(final String margin) {
        return this.setCssProperty(
            TextStylePropertyName.MARGIN_LEFT,
            margin
        ).setCssProperty(
            TextStylePropertyName.MARGIN_RIGHT,
            margin
        ).setCssProperty(
            TextStylePropertyName.MARGIN_TOP,
            margin
        ).setCssProperty(
            TextStylePropertyName.MARGIN_BOTTOM,
            margin
        );
    }

    public final C setMarginBottom(final String margin) {
        return this.setCssProperty(
            TextStylePropertyName.MARGIN_BOTTOM,
            margin
        );
    }

    public final C setMarginLeft(final String margin) {
        return this.setCssProperty(
            TextStylePropertyName.MARGIN_LEFT,
            margin
        );
    }

    public final C setMarginRight(final String margin) {
        return this.setCssProperty(
            TextStylePropertyName.MARGIN_RIGHT,
            margin
        );
    }

    public final C setMarginTop(final String overflow) {
        return this.setCssProperty(
            TextStylePropertyName.MARGIN_TOP,
            overflow
        );
    }
    
    public final C setOverflow(final String overflow) {
        return this.setCssProperty(
            "overflow",
            overflow
        );
    }

    public final C setPadding(final String padding) {
        return this.setCssProperty(
            TextStylePropertyName.PADDING_LEFT,
            padding
        ).setCssProperty(
            TextStylePropertyName.PADDING_RIGHT,
            padding
        ).setCssProperty(
            TextStylePropertyName.PADDING_TOP,
            padding
        ).setCssProperty(
            TextStylePropertyName.PADDING_BOTTOM,
            padding
        );
    }

    public final C setPaddingBottom(final String padding) {
        return this.setCssProperty(
            TextStylePropertyName.PADDING_BOTTOM,
            padding
        );
    }

    public final C setPaddingLeft(final String padding) {
        return this.setCssProperty(
            TextStylePropertyName.PADDING_LEFT,
            padding
        );
    }

    public final C setPaddingRight(final String padding) {
        return this.setCssProperty(
            TextStylePropertyName.PADDING_RIGHT,
            padding
        );
    }

    public final C setPaddingTop(final String overflow) {
        return this.setCssProperty(
            TextStylePropertyName.PADDING_TOP,
            overflow
        );
    }

    public final C setTop(final String top) {
        return this.setCssProperty(
            "top",
            top
        );
    }

    public final C setWidth(final String width) {
        return this.setCssProperty(
            "width",
            width
        );
    }

    final C setCssProperty(final TextStylePropertyName<?> name,
                           final String value) {
        return this.setCssProperty(
            name.toString(),
            value
        );
    }

    @Override
    public abstract C setCssProperty(final String name,
                                     final String value);

    @Override
    public abstract C setCssText(final String cssText);

    public abstract C addCssClasses(final CssClass...cssClass);

    public abstract C removeCssClasses(final CssClass...cssClass);

    public abstract C clear();

    public abstract C appendChild(final TextNode textNode);

    public abstract C appendChild(final Node child);

    public abstract C appendChild(final IsElement<?> child);

    public abstract C appendText(final String text);

    public abstract C removeChild(final Node child);

    public abstract C removeChild(final IsElement<?> child);

    public abstract C setText(final String text);

    public final C addBlurListener(final EventListener listener) {
        return this.addEventListener(
            EventType.blur.getName(),
            listener
        );
    }

    public final C addChangeListener(final EventListener listener) {
        return this.addEventListener(
            EventType.change.getName(),
            listener
        );
    }

    public final C addClickListener(final EventListener listener) {
        return this.addEventListener(
            EventType.click.getName(),
            listener
        );
    }

    public final C addContextMenuListener(final EventListener listener) {
        return this.addEventListener(
            EventType.contextmenu.getName(),
            listener
        );
    }

    public final C addDoubleClickListener(final EventListener listener) {
        return this.addEventListener(
            EventType.dblclick.getName(),
            listener
        );
    }

    public final C addFocusListener(final EventListener listener) {
        return this.addEventListener(
            EventType.focus.getName(),
            listener
        );
    }

    public final C addFocusInListener(final EventListener listener) {
        return this.addEventListener(
            "focusin",
            listener
        );
    }

    public final C addFocusOutListener(final EventListener listener) {
        return this.addEventListener(
            "focusout",
            listener
        );
    }

    public final C addKeyDownListener(final EventListener listener) {
        return this.addEventListener(
            EventType.keydown.getName(),
            listener
        );
    }

    public final C addKeyPressListener(final EventListener listener) {
        return this.addEventListener(
            EventType.keypress.getName(),
            listener
        );
    }

    public final C addKeyUpListener(final EventListener listener) {
        return this.addEventListener(
            EventType.keyup.getName(),
            listener
        );
    }

    public final C addMouseDownListener(final EventListener listener) {
        return this.addEventListener(
            EventType.mousedown.getName(),
            listener
        );
    }

    public final C addMouseEnterListener(final EventListener listener) {
        return this.addEventListener(
            EventType.mouseenter.getName(),
            listener
        );
    }

    public final C addMouseLeaveListener(final EventListener listener) {
        return this.addEventListener(
            EventType.mouseleave.getName(),
            listener
        );
    }

    public final C addMouseOverListener(final EventListener listener) {
        return this.addEventListener(
            EventType.mouseup.getName(),
            listener
        );
    }

    public final C addMouseOutListener(final EventListener listener) {
        return this.addEventListener(
            EventType.mouseout.getName(),
            listener
        );
    }

    public final C addMouseUpListener(final EventListener listener) {
        return this.addEventListener(
            EventType.mouseup.getName(),
            listener
        );
    }

    public abstract C addEventListener(final String type,
                                       final EventListener listener);

    // TreePrintable....................................................................................................

    public abstract void printTreeChildren(final IndentingPrinter printer);
}
