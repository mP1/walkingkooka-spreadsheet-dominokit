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

package walkingkooka.spreadsheet.dominokit.component;

import elemental2.dom.EventListener;
import elemental2.dom.Node;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.elements.BaseElement;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.menu.direction.DropDirection;
import org.dominokit.domino.ui.popover.Tooltip;

import java.util.Objects;

public abstract class Element<D extends BaseElement<E, D>, E extends elemental2.dom.HTMLElement> implements IsElement<E> {

    //public abstract class Element<D extends BaseElement<E, T>, E extends elemental2.dom.Element, T extends BaseElement<E, T>> {

    Element(final D element) {
        this.element = element;
    }

    // id...............................................................................................................

    public final String id() {
        return this.element().id;
    }

    public abstract Element<D, E> setId(final String id);

    final void setId0(final String id) {
        this.element().id = id;
    }

    // tabIndex........................................................................................................

    public final int tabIndex() {
        return this.element().tabIndex;
    }

    public abstract Element<D, E> setTabIndex(final int tabIndex);

    final void setTabIndex0(final int tabIndex) {
        this.element().tabIndex = tabIndex;
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
        return this.element().textContent;
    }

    public abstract Element<D, E> setTextContent(final String text);

    final void setTextContent0(final String text) {
        this.element().textContent = text;
    }

    // tooltip..........................................................................................................

    public final Tooltip tooltip() {
        return this.element.getTooltip();
    }

    public abstract Element<D, E> setTooltip(final String text,
                                             final DropDirection dropDirection);

    final void setTooltip0(final String text,
                           final DropDirection dropDirection) {
        this.element.setTooltip(
                text,
                dropDirection
        );
    }

    // Events...........................................................................................................

    /**
     * Adds a click {@link EventListener} to this element.
     */
    public abstract Element<D, E> addClickListener(final EventListener listener);

    final void addClickListener0(final EventListener listener) {
        this.element.addEventListener(
                EventType.click.getName(),
                listener
        );
    }

    /**
     * Adds a key down {@link EventListener} to this element.
     */
    public abstract Element<D, E> addKeydownListener(final EventListener listener);

    final void addKeydownListener0(final EventListener listener) {
        this.element.addEventListener(
                EventType.keydown.getName(),
                listener
        );
    }

    // children.........................................................................................................

    public abstract Element<D, E> append(final Node node);

    final void append0(final Node node) {
        Objects.requireNonNull(node, "node");
        this.element.appendChild(node);
    }

    public abstract Element<D, E> append(final IsElement<?> element);

    final void append0(final IsElement<?> element) {
        Objects.requireNonNull(element, "element");
        this.append0(element.element());
    }

    /**
     * Removes all child nodes.
     */
    public abstract Element<D, E> removeAllChildren();

    // isElement........................................................................................................

    @Override
    public final E element() {
        return this.element.element();
    }

    final D element;

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return this.element.toString();
    }
}
