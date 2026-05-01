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

package walkingkooka.spreadsheet.dominokit;

import elemental2.dom.DomGlobal;
import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import elemental2.dom.Node;
import org.dominokit.domino.ui.IsElement;
import walkingkooka.spreadsheet.dominokit.dom.Doms;
import walkingkooka.text.HasText;
import walkingkooka.tree.text.Border;
import walkingkooka.tree.text.BoxEdge;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.Margin;
import walkingkooka.tree.text.Padding;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link Component} that adds a few helpers relating to {@link HTMLElement} and {@link Node}.
 */
public interface HtmlComponent<E extends HTMLElement, C extends HtmlComponent<E, C>> extends Component,
    IsElement<E> {

    String id();

    C setId(final String id);

    /**
     * The width of this component, mostly delegates to offsetWidth
     */
    int width();

    /**
     * The height of this component, mostly delegates to offsetHeight
     */
    int height();

    C focus();

    C blur();

    /**
     * Helper that may be used mostly by implementations to test if an element has focus.
     */
    static boolean hasFocus(final Element element) {
        boolean focused = false;

        final Element active = DomGlobal.document.activeElement;
        if (null != active) {
            // verify active element belongs to the same selection. if it does it must have focus so no need to focus again
            focused = Doms.isOrHasChild(
                element,
                active
            );
        }

        return focused;
    }

    default boolean isNotEditing() {
        return false == this.isEditing();
    }

    /**
     * Returns true indicating that user has focus on an element or something similar.
     * This is useful to test if a component should NOT have its value replaced because an event introduces a new value,
     * such as a history token change.
     */
    boolean isEditing();

    // setCssText.......................................................................................................

    /**
     * Sets the CSS text on the element returned by {@link org.dominokit.domino.ui.IsElement}.
     */
    C setCssText(final String css);

    /**
     * Sets a CSS property for this component.
     */
    C setCssProperty(final String name,
                     final String value);

    /**
     * A type safe way to set a {@link TextStylePropertyName} and value. Note that not all CSS properties
     * have a {@link TextStylePropertyName} or value, in which case {@link #setCssProperty(String, String) must be used.}
     */
    default <T> C setStyleProperty(final TextStylePropertyName<T> name,
                                   final T value) {
        Objects.requireNonNull(name, "name");
        name.checkValue(value);

        final String nameString = name.value();
        switch (nameString) {
            case "border":
                final Border border = (Border) value;

                for (final BoxEdge boxEdge : BoxEdge.topRightBottomLeft()) {
                    final TextStylePropertyName<Border> propertyName = boxEdge.borderPropertyName();

                    this.setOrRemoveStyleProperty(
                        propertyName,
                        border.getProperty(propertyName)
                    );
                }
                break;
            case "border-top":
                final Border borderTop = (Border) value;

                this.setOrRemoveStyleProperty(
                    TextStylePropertyName.BORDER_TOP_COLOR,
                    borderTop.getProperty(TextStylePropertyName.BORDER_TOP_COLOR)
                );
                this.setOrRemoveStyleProperty(
                    TextStylePropertyName.BORDER_TOP_STYLE,
                    borderTop.getProperty(TextStylePropertyName.BORDER_TOP_STYLE)
                );
                this.setOrRemoveStyleProperty(
                    TextStylePropertyName.BORDER_TOP_WIDTH,
                    borderTop.getProperty(TextStylePropertyName.BORDER_TOP_WIDTH)
                );
                break;
            case "border-right":
                final Border borderRight = (Border) value;

                this.setOrRemoveStyleProperty(
                    TextStylePropertyName.BORDER_RIGHT_COLOR,
                    borderRight.getProperty(TextStylePropertyName.BORDER_RIGHT_COLOR)
                );
                this.setOrRemoveStyleProperty(
                    TextStylePropertyName.BORDER_RIGHT_STYLE,
                    borderRight.getProperty(TextStylePropertyName.BORDER_RIGHT_STYLE)
                );
                this.setOrRemoveStyleProperty(
                    TextStylePropertyName.BORDER_RIGHT_WIDTH,
                    borderRight.getProperty(TextStylePropertyName.BORDER_RIGHT_WIDTH)
                );
                break;
            case "border-bottom":
                final Border borderBottom = (Border) value;

                this.setOrRemoveStyleProperty(
                    TextStylePropertyName.BORDER_BOTTOM_COLOR,
                    borderBottom.getProperty(TextStylePropertyName.BORDER_BOTTOM_COLOR)
                );
                this.setOrRemoveStyleProperty(
                    TextStylePropertyName.BORDER_BOTTOM_STYLE,
                    borderBottom.getProperty(TextStylePropertyName.BORDER_BOTTOM_STYLE)
                );
                this.setOrRemoveStyleProperty(
                    TextStylePropertyName.BORDER_BOTTOM_WIDTH,
                    borderBottom.getProperty(TextStylePropertyName.BORDER_BOTTOM_WIDTH)
                );
                break;
            case "border-left":
                final Border borderLeft = (Border) value;

                this.setOrRemoveStyleProperty(
                    TextStylePropertyName.BORDER_LEFT_COLOR,
                    borderLeft.getProperty(TextStylePropertyName.BORDER_LEFT_COLOR)
                );
                this.setOrRemoveStyleProperty(
                    TextStylePropertyName.BORDER_LEFT_STYLE,
                    borderLeft.getProperty(TextStylePropertyName.BORDER_LEFT_STYLE)
                );
                this.setOrRemoveStyleProperty(
                    TextStylePropertyName.BORDER_LEFT_WIDTH,
                    borderLeft.getProperty(TextStylePropertyName.BORDER_LEFT_WIDTH)
                );
                break;
            case "margin":
                final Margin margin = (Margin) value;

                for (final BoxEdge boxEdge : BoxEdge.topRightBottomLeft()) {
                    final TextStylePropertyName<Length<?>> propertyName = boxEdge.marginPropertyName();

                    this.setOrRemoveStyleProperty(
                        propertyName,
                        margin.getProperty(propertyName)
                    );
                }
                break;
            case "padding":
                final Padding padding = (Padding) value;

                for (final BoxEdge boxEdge : BoxEdge.topRightBottomLeft()) {
                    final TextStylePropertyName<Length<?>> propertyName = boxEdge.paddingPropertyName();

                    this.setOrRemoveStyleProperty(
                        propertyName,
                        padding.getProperty(propertyName)
                    );
                }
                break;
            default:
                if (value instanceof HasText) {
                    this.setCssProperty(
                        nameString,
                        ((HasText) value)
                            .text()
                    );
                } else {
                    this.setCssProperty(
                        nameString,
                        value.toString()
                    );
                }
                break;
        }

        return (C) this;
    }

    default <T> C setOrRemoveStyleProperty(final TextStylePropertyName<T> name,
                                           final Optional<T> value) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(value, "value");

        final T valueOrNull = value.orElse(null);
        return null == valueOrNull ?
            this.removeStyleProperty(
                name
            ) :
            this.setStyleProperty(
                name,
                valueOrNull
            );
    }

    /**
     * Removes a CSS property from this component.
     */
    C removeCssProperty(final String name);

    default C removeStyleProperty(final TextStylePropertyName<?> propertyName) {
        Objects.requireNonNull(propertyName, "propertyName");

        return this.removeCssProperty(
            propertyName.value()
        );
    }

    String VISIBILITY = "visibility";
    String HIDDEN = "hidden";
    String VISIBLE = "visible";

    default C setVisibility(final boolean visibility) {
        return this.setCssProperty(
            VISIBILITY,
            visibility ?
                VISIBLE :
                HIDDEN
        );
    }

    // node.............................................................................................................

    @Override
    default Node node() {
        return this.element();
    }
}
