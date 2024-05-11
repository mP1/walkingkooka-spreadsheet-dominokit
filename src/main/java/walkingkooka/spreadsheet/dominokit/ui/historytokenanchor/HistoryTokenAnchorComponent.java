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
import org.dominokit.domino.ui.elements.AnchorElement;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.menu.direction.DropDirection;
import org.dominokit.domino.ui.utils.ElementsFactory;
import walkingkooka.net.Url;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.ui.contextmenu.SpreadsheetContextMenu;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

/**
 * Abstraction for working with a HTML anchor.
 */
public final class HistoryTokenAnchorComponent implements HistoryTokenAnchorComponentLike {

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

        // CSS-STYLE
        final CSSStyleDeclaration style = element.element()
                .style;
        style.set("font-family", "Inter");
        style.set("font-size", "15px");
        style.set("font-weight", "400");
        style.set("text-wrap", "nowrap");

        this.element = element;
    }

    // disabled.........................................................................................................

    @Override
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
     * The {@link #historyToken()} will be pushed if this anchor is clicked or ENTER key downed.
     */
    @Override
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

    @Override
    public boolean isChecked() {
        return this.checked;
    }

    @Override
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

    @Override
    public Url href() {
        final String href = this.element().href;
        return CharSequences.isNullOrEmpty(href) ?
                null :
                Url.parseAbsoluteOrRelative(href);
    }

    @Override
    public HistoryTokenAnchorComponent setHref(final Url url) {
        this.element().href =
                null == url ?
                        "" :
                        url.toString();
        return this.setDisabled(null == url);
    }

    // id...............................................................................................................

    @Override
    public String id() {
        return this.element.getId();
    }

    @Override
    public HistoryTokenAnchorComponent setId(final String id) {
        this.element.setId(id);
        return this;
    }

    // tabIndex.........................................................................................................

    @Override
    public int tabIndex() {
        return this.element().tabIndex;
    }

    @Override
    public HistoryTokenAnchorComponent setTabIndex(final int tabIndex) {
        this.element.setTabIndex(tabIndex);
        return this;
    }

    // target.........................................................................................................

    @Override
    public String target() {
        return this.element().getAttribute(TARGET);
    }

    @Override
    public HistoryTokenAnchorComponent setTarget(final String target) {
        this.element.setAttribute(
                TARGET,
                target
        );
        return this;
    }

    private final static String TARGET = "target";

    // textContent......................................................................................................

    @Override
    public String textContent() {
        return this.element.getTextContent();
    }

    @Override
    public HistoryTokenAnchorComponent setTextContent(final String text) {
        this.element.setTextContent(text);
        return this;
    }

    // iconBefore | text Content | iconAfter

    // iconBefore......................................................................................................

    @Override
    public Optional<Icon<?>> iconBefore() {
        return Optional.ofNullable(this.iconBefore);
    }

    @Override
    public HistoryTokenAnchorComponent setIconBefore(final Optional<Icon<?>> icon) {
        Objects.requireNonNull(icon, "icon");

        final AnchorElement anchorElement = this.element;

        final Icon<?> oldBeforeIcon = this.iconBefore;
        if (null != oldBeforeIcon) {
            anchorElement.removeChild(
                    oldBeforeIcon.element()
            );
        }

        if (icon.isPresent()) {
            final Icon<?> newIcon = icon.get();
            anchorElement.insertFirst(
                    newIcon.element()
            );
            this.iconBefore = newIcon;

        } else {
            this.iconBefore = null;
        }

        return this;
    }

    private Icon<?> iconBefore;

    // iconAfter......................................................................................................

    @Override
    public Optional<Icon<?>> iconAfter() {
        return Optional.ofNullable(this.iconAfter);
    }

    @Override
    public HistoryTokenAnchorComponent setIconAfter(final Optional<Icon<?>> icon) {
        Objects.requireNonNull(icon, "icon");

        final AnchorElement anchorElement = this.element;

        final Icon<?> oldIconAfter = this.iconAfter;
        if (null != oldIconAfter) {
            anchorElement.removeChild(
                    oldIconAfter.element()
            );
        }

        if (icon.isPresent()) {
            final Icon<?> newIcon = icon.get();
            anchorElement.appendChild(
                    newIcon.element()
            );
            this.iconBefore = newIcon;

        } else {
            this.iconAfter = null;
        }

        return this;
    }

    private Icon<?> iconAfter;

    // tooltip..........................................................................................................

    @Override
    public HistoryTokenAnchorComponent setTooltip(final String text,
                                                  final DropDirection dropDirection) {
        this.element.setTooltip(
                text,
                dropDirection
        );
        return this;
    }

    // events..........................................................................................................

    @Override
    public HistoryTokenAnchorComponent addClickListener(final EventListener listener) {
        this.element.addEventListener(
                EventType.click.getName(),
                this.disabledAwareEventListener(listener)
        );
        return this;
    }

    @Override
    public HistoryTokenAnchorComponent addFocusListener(final EventListener listener) {
        this.element.addEventListener(
                EventType.focus.getName(),
                this.disabledAwareEventListener(listener)
        );
        return this;
    }

    @Override
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
    @Override
    public HistoryTokenAnchorComponent addClickAndKeydownEnterListener(final EventListener listener) {
        this.element.onKeyPress(e -> e.onEnter(listener));

        return this.addClickListener(listener);
    }

    // focus............................................................................................................

    @Override
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
        return HistoryTokenAnchorComponentToString.toString(this);
    }

    // SpreadsheetContextMenuTarget.....................................................................................

    @Override
    public void setSpreadsheetContextMenu(final SpreadsheetContextMenu menu) {
        Objects.requireNonNull(menu, "menu");

        this.menu = menu;
    }

    @Override
    public Optional<SpreadsheetContextMenu> spreadsheetContextMenu() {
        return Optional.ofNullable(this.menu);
    }

    private SpreadsheetContextMenu menu;
}
