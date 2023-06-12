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

import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.DomGlobal;
import elemental2.dom.EventListener;
import elemental2.dom.HTMLAnchorElement;
import elemental2.dom.KeyboardEvent;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;

import java.util.Objects;

import static org.jboss.elemento.Key.Enter;

/**
 * Abstraction for working with a HTML anchor.
 */
public final class Anchor extends Element<HTMLAnchorElement> {

    /**
     * Creates a new un-attached ANCHOR.
     */
    public static Anchor empty() {
        return new Anchor(
                (HTMLAnchorElement)
                        DomGlobal.document.createElement("a")
        );
    }

    /**
     * Wraps an existing {@link HTMLAnchorElement}
     */
    public static Anchor with(final HTMLAnchorElement element) {
        Objects.requireNonNull(element, "element");

        return new Anchor(element);
    }

    private Anchor(final HTMLAnchorElement element) {
        super(element);
        element.style.set("margin", "5px");
    }

    // disabled.........................................................................................................

    public Anchor setDisabled(final boolean disabled) {
        this.setAttribute("aria-disabled", disabled);

        final CSSStyleDeclaration style = this.element.style;

        style.cursor = disabled ? "not-allowed" : "pointer";
        style.textDecoration = disabled ? "none" : "underline";

        return this;
    }

    // historyToken....................................................................................................

    public HistoryToken historyToken() {
        return HistoryToken.parse(
                this.href().fragment()
        );
    }

    public Anchor setHistoryToken(final HistoryToken historyToken) {
        return this.setHref(
                null != historyToken ?
                        Url.parseRelative("" + Url.FRAGMENT_START + historyToken.urlFragment()) :
                        null
        );
    }

    public Anchor addPushHistoryToken(final HistoryTokenContext context) {
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

    // href.............................................................................................................

    public AbsoluteOrRelativeUrl href() {
        return Url.parseAbsoluteOrRelative(
                this.element.href
        );
    }

    public Anchor setHref(final Url url) {
        this.element.href =
                null != url ?
                        url.toString() :
                        "";
        return this.setDisabled(null == url);
    }

    // id...............................................................................................................

    @Override
    public Anchor setId(final String id) {
        this.setId0(id);
        return this;
    }

    // tabIndex.........................................................................................................

    @Override
    public Anchor setTabIndex(final int tabIndex) {
        this.setTabIndex0(tabIndex);
        return this;
    }

    // textContent......................................................................................................

    @Override
    public Anchor setTextContent(final String text) {
        this.setTextContent0(text);
        return this;
    }

    // events..........................................................................................................
    @Override
    public Anchor addClick(final EventListener listener) {
        this.addClick0(listener);
        return this;
    }

    @Override
    public Anchor addKeydown(final EventListener listener) {
        this.addKeydown0(listener);
        return this;
    }

    // children.........................................................................................................

    @Override
    public Anchor removeAllChildren() {
        this.removeAllChildren0();
        return this;
    }
}
