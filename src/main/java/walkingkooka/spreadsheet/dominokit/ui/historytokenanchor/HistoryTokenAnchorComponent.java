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

package walkingkooka.spreadsheet.dominokit.ui.historytokenanchor;

import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.Event;
import elemental2.dom.EventListener;
import elemental2.dom.HTMLAnchorElement;
import elemental2.dom.Node;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.elements.AnchorElement;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.icons.MdiIcon;
import org.dominokit.domino.ui.menu.direction.DropDirection;
import org.dominokit.domino.ui.utils.ElementsFactory;
import walkingkooka.net.HasUrlFragment;
import walkingkooka.net.Url;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

/**
 * Abstraction for working with a HTML anchor.
 */
public final class HistoryTokenAnchorComponent implements HtmlElementComponent<HTMLAnchorElement, HistoryTokenAnchorComponent> {

    /**
     * Creates a new un-attached ANCHOR.
     */
    public static HistoryTokenAnchorComponent empty() {
        return new HistoryTokenAnchorComponent(ElementsFactory.elements.a());
    }

    /**
     * Wraps an existing {@link HTMLAnchorElement}
     */
    public static HistoryTokenAnchorComponent with(final HTMLAnchorElement element) {
        Objects.requireNonNull(element, "element");

        return new HistoryTokenAnchorComponent(
                AnchorElement.of(
                        element
                )
        );
    }

    private HistoryTokenAnchorComponent(final AnchorElement element) {
        super();
        element.setMargin("5px");
        element.element()
                .style.set("text-wrap", "nowrap");

        this.element = element;
    }

    // disabled.........................................................................................................

    /**
     * An {@link HistoryTokenAnchorComponent} is considered disabled when it has no href.
     */
    public boolean isDisabled() {
        return null == this.href();
    }

    public HistoryTokenAnchorComponent setDisabled(final boolean disabled) {
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

        this.iconBefore = null;
        this.iconAfter = null;

        return this;
    }

    // historyToken....................................................................................................

    /**
     * If the HREF contains a url with a fragment it will be parsed into a {@link HistoryToken} otherwise
     * an {@link Optional#empty()} will be returned.
     */
    public Optional<HistoryToken> historyToken() {
        final Url url = this.href();

        return Optional.ofNullable(
                url instanceof HasUrlFragment ?
                        HistoryToken.parse(
                                ((HasUrlFragment) url).urlFragment()
                        ) :
                        null
        );
    }

    public HistoryTokenAnchorComponent setHistoryToken(final Optional<HistoryToken> historyToken) {
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
    public HistoryTokenAnchorComponent addPushHistoryToken(final HistoryTokenContext context) {
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

    // checked.........................................................................................................

    public boolean isChecked() {
        return this.checked;
    }

    public HistoryTokenAnchorComponent setChecked(final boolean checked) {
        final AnchorElement element = this.element;
        if (checked) {
            element.setCssProperty(FONT_WEIGHT, "bold");
        } else {
            element.removeCssProperty(FONT_WEIGHT);
        }

        this.checked = checked;
        return this;
    }

    private boolean checked;

    private final static String FONT_WEIGHT = "font-weight";

    // href.............................................................................................................

    public Url href() {
        final String href = this.element().href;
        return CharSequences.isNullOrEmpty(href) ?
                null :
                Url.parseAbsoluteOrRelative(href);
    }

    public HistoryTokenAnchorComponent setHref(final Url url) {
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

    public HistoryTokenAnchorComponent setId(final String id) {
        this.element.setId(id);
        return this;
    }

    // tabIndex.........................................................................................................

    public int tabIndex() {
        return this.element().tabIndex;
    }

    public HistoryTokenAnchorComponent setTabIndex(final int tabIndex) {
        this.element.setTabIndex(tabIndex);
        return this;
    }

    // textContent......................................................................................................

    public String textContent() {
        return this.element.getTextContent();
    }

    public HistoryTokenAnchorComponent setTextContent(final String text) {
        this.element.setTextContent(text);
        return this;
    }

    // iconBefore | text Content | iconAfter

    // iconBefore......................................................................................................

    public Optional<MdiIcon> iconBefore() {
        return Optional.ofNullable(this.iconBefore);
    }

    public HistoryTokenAnchorComponent setIconBefore(final Optional<MdiIcon> icon) {
        Objects.requireNonNull(icon, "icon");

        final AnchorElement anchorElement = this.element;

        final MdiIcon oldBeforeIcon = this.iconBefore;
        if (null != oldBeforeIcon) {
            anchorElement.removeChild(
                    oldBeforeIcon.element()
            );
        }

        if (icon.isPresent()) {
            final MdiIcon newIcon = icon.get();
            anchorElement.insertFirst(
                    newIcon.element()
            );
            this.iconBefore = newIcon;

        } else {
            this.iconBefore = null;
        }

        return this;
    }

    private MdiIcon iconBefore;

    // iconAfter......................................................................................................

    public Optional<MdiIcon> iconAfter() {
        return Optional.ofNullable(this.iconAfter);
    }

    public HistoryTokenAnchorComponent setIconAfter(final Optional<MdiIcon> icon) {
        Objects.requireNonNull(icon, "icon");

        final AnchorElement anchorElement = this.element;

        final MdiIcon oldIconAfter = this.iconAfter;
        if (null != oldIconAfter) {
            anchorElement.removeChild(
                    oldIconAfter.element()
            );
        }

        if (icon.isPresent()) {
            final MdiIcon newIcon = icon.get();
            anchorElement.appendChild(
                    newIcon.element()
            );
            this.iconBefore = newIcon;

        } else {
            this.iconAfter = null;
        }

        return this;
    }

    private MdiIcon iconAfter;

    // tooltip..........................................................................................................

    public HistoryTokenAnchorComponent setTooltip(final String text,
                                                  final DropDirection dropDirection) {
        this.element.setTooltip(
                text,
                dropDirection
        );
        return this;
    }

    // events..........................................................................................................
    public HistoryTokenAnchorComponent addClickListener(final EventListener listener) {
        this.element.addEventListener(
                EventType.click.getName(),
                this.disabledAwareEventListener(listener)
        );
        return this;
    }

    public HistoryTokenAnchorComponent addKeydownListener(final EventListener listener) {
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
    public HistoryTokenAnchorComponent addClickAndKeydownEnterListener(final EventListener listener) {
        this.element.onKeyPress(e -> e.onEnter(listener));

        return this.addClickListener(listener);
    }

    // children.........................................................................................................

    public HistoryTokenAnchorComponent append(final Node node) {
        this.element.appendChild(node);
        return this;
    }

    public HistoryTokenAnchorComponent append(final IsElement<?> element) {
        this.element.appendChild(element);
        return this;
    }

    public HistoryTokenAnchorComponent removeAllChildren() {
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
