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
import elemental2.dom.Event;
import elemental2.dom.EventListener;
import elemental2.dom.HTMLAnchorElement;
import elemental2.dom.KeyboardEvent;
import elemental2.dom.Node;
import org.jboss.elemento.IsElement;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.text.CharSequences;

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

    /**
     * An {@link Anchor} is considered disabled when it has no href.
     */
    public boolean isDisabled() {
        return null == this.href();
    }

    public Anchor setDisabled(final boolean disabled) {
        this.setAttribute("aria-disabled", disabled);

        final HTMLAnchorElement element = this.element;

        if (disabled) {
            element.removeAttribute("href"); // cant assign null, because href will still be present and isDisabled() wll be confused and report false
        }

        final CSSStyleDeclaration style = element.style;

        style.textDecoration = disabled ? "none" : "underline";

        // DominoKit includes a cursor: pointer !important in one of its styles.
        //
        // element.cursor with !important is ignored, the only form that works is appending to cssText
        style.cssText = style.cssText +
                (disabled ? ";cursor: not-allowed !important" : ";cursor: pointer !important");

        return this;
    }

    // historyToken....................................................................................................

    public HistoryToken historyToken() {
        final AbsoluteOrRelativeUrl url = this.href();

        return null == url ?
                null :
                HistoryToken.parse(
                        url.fragment()
                );
    }

    public Anchor setHistoryToken(final HistoryToken historyToken) {
        return this.setHref(
                null == historyToken ?
                        null :
                        Url.parseRelative(
                                "" + Url.FRAGMENT_START + historyToken.urlFragment()
                        )
        );
    }

    public Anchor addPushHistoryToken(final HistoryTokenContext context) {
        return this.addClickListener(
                (e) -> {
                    e.preventDefault();
                    context.pushHistoryToken(this.historyToken());
                }
        ).addKeydownListener(
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
        final String href = this.element.href;
        return CharSequences.isNullOrEmpty(href) ?
                null :
                Url.parseAbsoluteOrRelative(href);
    }

    public Anchor setHref(final Url url) {
        this.element.href =
                null == url ?
                       "" :
                        url.toString();
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
    public Anchor addClickListener(final EventListener listener) {
        this.addClickListener0(
                this.disabledAwareEventListener(listener)
        );
        return this;
    }

    @Override
    public Anchor addKeydownListener(final EventListener listener) {
        this.addKeydownListener0(
                this.disabledAwareEventListener(listener)
        );
        return this;
    }

    /**
     * If this anchor is disabled calls {@link Event#preventDefault()} and skips calling the given {@link EventListener}.
     */
    private EventListener disabledAwareEventListener(final EventListener listener) {
        return (e) -> {
            if (this.isDisabled()) {
                e.preventDefault();
            } else {
                listener.handleEvent(e);
            }
        };
    }

    // children.........................................................................................................

    @Override
    public Anchor append(final Node node) {
        this.append0(node);
        return this;
    }

    @Override
    public Anchor append(final IsElement<?> element) {
        this.append0(element);
        return this;
    }

    @Override
    public Anchor removeAllChildren() {
        this.removeAllChildren0();
        return this;
    }
}
