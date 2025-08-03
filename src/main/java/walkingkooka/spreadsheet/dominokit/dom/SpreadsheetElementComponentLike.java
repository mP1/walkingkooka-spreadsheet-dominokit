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
import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;

/**
 * Base class for an element {@link HtmlElementComponent}
 */
abstract class SpreadsheetElementComponentLike<E extends HTMLElement, C extends SpreadsheetElementComponentLike<E, C>> implements HtmlElementComponent<E, C> {

    SpreadsheetElementComponentLike() {
        super();
    }

    public abstract C setId(final String id);

    public abstract C setTabIndex(final int tabIndex);

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

    public final C setOverflow(final String overflow) {
        return this.setCssProperty(
            "overflow",
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

    @Override
    public abstract C setCssProperty(final String name,
                                     final String value);

    @Override
    public abstract C setCssText(final String cssText);

    public abstract C clear();

    public abstract C appendChild(final Node child);

    public abstract C appendChild(final IsElement<?> child);

    public abstract C addEventListener(final String type,
                                       final EventListener listener);
}
