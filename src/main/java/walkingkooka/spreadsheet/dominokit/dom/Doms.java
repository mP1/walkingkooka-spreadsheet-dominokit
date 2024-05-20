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
import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.tree.text.TextNode;

import java.util.Objects;

public final class Doms implements PublicStaticHelper {

    /**
     * Tests if the 2nd parameter is the parent element or a child.
     */
    public static boolean isOrHasChild(final Element parent,
                                       final Element child) {
        return toOldElement(parent)
                .isOrHasChild(
                        toOldElement(child)
                );
    }

    private static com.google.gwt.dom.client.Element toOldElement(final Element element) {
        return Js.cast(element);
    }

    /**
     * Creates a {@link elemental2.dom.Node} either a TextNode or Element depending on the provided {@link TextNode}.
     */
    public static elemental2.dom.Node node(final TextNode node) {
        return DomsNodeTextNodeVisitor.toNode(node);
    }

    /**
     * Creates a {@link elemental2.dom.Text}
     */
    public static elemental2.dom.Text textNode(final String text) {
        return DomGlobal.document.createTextNode(text);
    }

    // visibility.......................................................................................................

    private final static String VISIBILITY = "visibility";
    private final static String HIDDEN = "hidden";
    private final static String VISIBLE = "visible";

    public static boolean isVisibilityHidden(final HTMLElement element) {
        Objects.requireNonNull(element, "element");

        return HIDDEN.equals(
                element.style
                        .getPropertyValue(VISIBILITY)
        );
    }

    public static void setVisibility(final HTMLElement element,
                                     final boolean visibility) {
        Objects.requireNonNull(element, "element");

        element.style.set(
                VISIBILITY,
                visibility ?
                        VISIBLE :
                        HIDDEN
        );
    }

    /**
     * Stop creation
     */
    private Doms() {
        throw new UnsupportedOperationException();
    }
}
