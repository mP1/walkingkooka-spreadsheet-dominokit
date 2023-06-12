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

import java.util.Objects;

/**
 * Abstraction for working with a HTML anchor.
 */
public final class Span extends Element<HTMLElement> {

    /**
     * Creates a new un-attached ANCHOR.
     */
    public static Span empty() {
        return new Span(
                (HTMLElement)
                        DomGlobal.document.createElement("span")
        );
    }

    /**
     * Wraps an existing {@link HTMLElement}
     */
    public static Span with(final HTMLElement element) {
        Objects.requireNonNull(element, "element");

        return new Span(element);
    }

    private Span(final HTMLElement element) {
        super(element);
    }

    // id...............................................................................................................

    @Override
    public Span setId(final String id) {
        this.setId0(id);
        return this;
    }

    // tabIndex.........................................................................................................

    @Override
    public Span setTabIndex(final int tabIndex) {
        this.setTabIndex0(tabIndex);
        return this;
    }

    // textContent......................................................................................................

    @Override
    public Span setTextContent(final String text) {
        this.setTextContent0(text);
        return this;
    }

    // events..........................................................................................................
    @Override
    public Span addClick(final EventListener listener) {
        this.addClick0(listener);
        return this;
    }

    @Override
    public Span addKeydown(final EventListener listener) {
        this.addKeydown0(listener);
        return this;
    }

    // children.........................................................................................................

    @Override
    public Span removeAllChildren() {
        this.removeAllChildren0();
        return this;
    }
}
