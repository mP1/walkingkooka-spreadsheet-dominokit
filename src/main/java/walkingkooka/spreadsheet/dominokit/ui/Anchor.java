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

package walkingkooka.spreadsheet.dominokit.ui;

import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.Event;
import elemental2.dom.EventListener;
import elemental2.dom.HTMLAnchorElement;
import elemental2.dom.Node;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.elements.AnchorElement;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.menu.direction.DropDirection;
import org.dominokit.domino.ui.utils.ElementsFactory;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

/**
 * Abstraction for working with a HTML anchor.
 */
public final class Anchor implements IsElement<HTMLAnchorElement> {

    /**
     * Creates a new un-attached ANCHOR.
     */
    public static Anchor empty() {
        return new Anchor(ElementsFactory.elements.a());
    }

    /**
     * Wraps an existing {@link HTMLAnchorElement}
     */
    public static Anchor with(final HTMLAnchorElement element) {
        Objects.requireNonNull(element, "element");

        return new Anchor(
                AnchorElement.of(
                        element
                )
        );
    }

    private Anchor(final AnchorElement element) {
        super();
        element.setMargin("5px");
        element.element()
                .style.set("text-wrap", "nowrap");

        this.element = element;
    }

    // disabled.........................................................................................................

    /**
     * An {@link Anchor} is considered disabled when it has no href.
     */
    public boolean isDisabled() {
        return null == this.href();
    }

    public Anchor setDisabled(final boolean disabled) {
        this.element.setAttribute("aria-disabled", disabled);

        final HTMLAnchorElement element = this.element();

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

    public Optional<HistoryToken> historyToken() {
        final AbsoluteOrRelativeUrl url = this.href();

        return Optional.ofNullable(
                null == url ?
                        null :
                        HistoryToken.parse(
                                url.fragment()
                        )
        );
    }

    public Anchor setHistoryToken(final Optional<HistoryToken> historyToken) {
        final HistoryToken historyTokenOrNull = historyToken.orElse(null);

        return this.setHref(
                null == historyTokenOrNull ?
                        null :
                        Url.parseRelative(
                                "" + Url.FRAGMENT_START + historyTokenOrNull.urlFragment()
                        )
        );
    }

    /**
     * The {@link #historyToken()} will be pushed if this anchor is clicked or ENTER key downed.
     */
    public Anchor addPushHistoryToken(final HistoryTokenContext context) {
        return this.addClickAndKeydownEnterListener(
                (e) -> {
                    e.preventDefault();

                    this.historyToken()
                            .ifPresent(
                                    context::pushHistoryToken
                            );
                }
        );
    }

    // href.............................................................................................................

    public AbsoluteOrRelativeUrl href() {
        final String href = this.element().href;
        return CharSequences.isNullOrEmpty(href) ?
                null :
                Url.parseAbsoluteOrRelative(href);
    }

    public Anchor setHref(final Url url) {
        this.element().href =
                null == url ?
                        "" :
                        url.toString();
        return this.setDisabled(null == url);
    }

    // id...............................................................................................................

    public String id() {
        return this.element.getId();
    }

    public Anchor setId(final String id) {
        this.element.setId(id);
        return this;
    }

    // tabIndex.........................................................................................................

    public int tabIndex() {
        return this.element().tabIndex;
    }

    public Anchor setTabIndex(final int tabIndex) {
        this.element.setTabIndex(tabIndex);
        return this;
    }

    // textContent......................................................................................................

    public String textContent() {
        return this.element.getTextContent();
    }

    public Anchor setTextContent(final String text) {
        this.element.setTextContent(text);
        return this;
    }

    // tooltip..........................................................................................................

    public Anchor setTooltip(final String text,
                             final DropDirection dropDirection) {
        this.element.setTooltip(
                text,
                dropDirection
        );
        return this;
    }

    // events..........................................................................................................
    public Anchor addClickListener(final EventListener listener) {
        this.element.addEventListener(
                EventType.click.getName(),
                this.disabledAwareEventListener(listener)
        );
        return this;
    }

    public Anchor addKeydownListener(final EventListener listener) {
        this.element.addEventListener(
                EventType.keydown.getName(),
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

    /**
     * Adds a {@link EventListener} that receives click and keydown with ENTER events.
     */
    public Anchor addClickAndKeydownEnterListener(final EventListener listener) {
        this.element.onKeyPress(e -> e.onEnter(listener));

        return this.addClickListener(listener);
    }

    // children.........................................................................................................

    public Anchor append(final Node node) {
        this.element.appendChild(node);
        return this;
    }

    public Anchor append(final IsElement<?> element) {
        this.element.appendChild(element);
        return this;
    }

    public Anchor removeAllChildren() {
        this.element.clearElement();
        return this;
    }

    // focus............................................................................................................

    public void focus() {
        this.element().focus();
    }

    // isElement........................................................................................................

    @Override
    public HTMLAnchorElement element() {
        return this.element.element();
    }

    final AnchorElement element;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.element.toString();
    }
}
