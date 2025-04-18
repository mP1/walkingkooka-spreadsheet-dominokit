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

import elemental2.dom.HTMLElement;
import elemental2.dom.Node;
import org.dominokit.domino.ui.IsElement;

/**
 * A {@link Component} that adds a few helpers relating to {@link HTMLElement} and {@link Node}.
 */
public interface HtmlElementComponent<E extends HTMLElement, C extends HtmlElementComponent<E, C>> extends Component,
    IsElement<E> {

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
