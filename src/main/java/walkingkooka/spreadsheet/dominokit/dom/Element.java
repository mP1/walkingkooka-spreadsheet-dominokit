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

import java.util.Objects;

public abstract class Element<T extends HTMLElement> implements IsElement<T> {

    Element(final T element) {
        this.element = element;
    }

    // id...............................................................................................................

    public final String id() {
        return this.element.id;
    }

    public abstract Element<T> setId(final String id);

    final void setId0(final String id) {
        this.element.id = id;
    }

    // tabIndex........................................................................................................

    public final int tabIndex() {
        return this.element.tabIndex;
    }

    public abstract Element<T> setTabIndex(final int tabIndex);

    final void setTabIndex0(final int tabIndex) {
        this.element.tabIndex = tabIndex;
    }

    // attributes.......................................................................................................

    final String getAttribute(final String name) {
        return this.element.getAttribute(name);
    }

    final void setAttribute(final String name,
                            final boolean value) {
        this.element.setAttribute(name, value);
    }

    final void setAttribute(final String name,
                            final int value) {
        this.element.setAttribute(name, value);
    }

    final void setAttribute(final String name,
                            final String value) {
        this.element.setAttribute(name, value);
    }

    // TextContent......................................................................................................

    public final String textContent() {
        return this.element.textContent;
    }

    public abstract Element<T> setTextContent(final String text);

    final void setTextContent0(final String text) {
        this.element.textContent = text;
    }

    // Events...........................................................................................................

    /**
     * Adds a click {@link EventListener} to this element.
     */
    public abstract Element<T> addClickListener(final EventListener listener);

    final void addClickListener0(final EventListener listener) {
        this.element.addEventListener(
                EventType.click.getName(),
                listener
        );
    }

    /**
     * Adds a key down {@link EventListener} to this element.
     */
    public abstract Element<T> addKeydownListener(final EventListener listener);

    final void addKeydownListener0(final EventListener listener) {
        this.element.addEventListener(
                EventType.keydown.getName(),
                listener
        );
    }

    // children.........................................................................................................

    public abstract Element<T> append(final Node node);

    final void append0(final Node node) {
        Objects.requireNonNull(node, "node");
        this.element.append(node);
    }

    public abstract Element<T> append(final IsElement<?> element);

    final void append0(final IsElement<?> element) {
        Objects.requireNonNull(element, "element");
        this.append0(element.element());
    }

    /**
     * Removes all child nodes.
     */
    public abstract Element<T> removeAllChildren();

    /**
     * Removes all child nodes.
     */
    final void removeAllChildren0() {
        final T element = this.element;
        for (; ; ) {
            final Node last = element.lastChild;
            if (null == last) {
                break;
            }
            element.removeChild(last);
        }
    }

    // isElement........................................................................................................

    @Override
    public final T element() {
        return this.element;
    }

    final T element;

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return this.element.toString();
    }
}
