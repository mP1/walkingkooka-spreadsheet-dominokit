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
import elemental2.dom.HTMLAnchorElement;
import elemental2.dom.HTMLElement;
import elemental2.dom.KeyboardEvent;
import org.jboss.elemento.EventType;
import org.jboss.elemento.IsElement;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;

import java.util.Objects;

import static org.jboss.elemento.Key.Enter;

/**
 * Abstraction for working with a HTML anchor.
 */
public final class Anchor implements IsElement<HTMLAnchorElement> {

    /**
     * Wraps an existing {@link HTMLAnchorElement}
     */
    public static <T extends HTMLElement> Anchor with(final HTMLAnchorElement element) {
        Objects.requireNonNull(element, "element");

        return new Anchor(element);
    }

    private Anchor(final HTMLAnchorElement element) {
        this.element = element;
    }

    public Anchor addClick(final EventListener listener) {
        this.element.addEventListener(
                EventType.click.getName(),
                listener
        );
        return this;
    }

    public Anchor addKeydown(final EventListener listener) {
        this.element.addEventListener(
                EventType.keydown.getName(),
                listener
        );
        return this;
    }

    public HistoryToken historyToken() {
        return HistoryToken.parse(
                this.href().fragment()
        );
    }

    public Anchor setHistoryToken(final HistoryToken historyToken) {
        return this.setHref(
                Url.parseRelative("" + Url.FRAGMENT_START + historyToken.urlFragment())
        );
    }

    public Anchor pushHistoryToken(final HistoryTokenContext context) {
        return this.addClick(
                (e) -> {
                    e.preventDefault();
                    context.pushHistoryToken(this.historyToken());
                }
        ).addKeydown(
                (e) -> {
                    final KeyboardEvent keyboardEvent = (KeyboardEvent) e;
                    if (keyboardEvent.code.equals(Enter)) {
                        keyboardEvent.preventDefault();
                        context.pushHistoryToken(this.historyToken());
                    }
                }
        );
    }

    public AbsoluteOrRelativeUrl href() {
        return Url.parseAbsoluteOrRelative(
                this.getAttribute("href")
        );
    }

    public Anchor setHref(final Url url) {
        return this.setAttribute(
                "href",
                url.toString()
        );
    }

    public Anchor tabIndex(final int tabIndex) {
        return this.setAttribute(
                "tabindex",
                tabIndex
        );
    }

    public String getAttribute(final String name) {
        return this.element.getAttribute(name);
    }

    public Anchor setAttribute(final String name,
                               final String value) {
        this.element.setAttribute(name, value);
        return this;
    }

    public Anchor setAttribute(final String name,
                               final int value) {
        this.element.setAttribute(name, value);
        return this;
    }

    public Anchor text(final String text) {
        this.element.textContent = text;
        return this;
    }

    // isElement........................................................................................................

    @Override
    public HTMLAnchorElement element() {
        return this.element;
    }

    private final HTMLAnchorElement element;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.element.toString();
    }
}
