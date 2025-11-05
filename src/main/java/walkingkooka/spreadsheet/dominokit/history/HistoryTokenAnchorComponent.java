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

package walkingkooka.spreadsheet.dominokit.history;

import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.Event;
import elemental2.dom.EventListener;
import elemental2.dom.HTMLAnchorElement;
import elemental2.dom.Text;
import org.dominokit.domino.ui.badges.Badge;
import org.dominokit.domino.ui.elements.AnchorElement;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.utils.ElementsFactory;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import org.dominokit.domino.ui.utils.PostfixAddOn;
import walkingkooka.net.Url;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.color.SpreadsheetDominoKitColor;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.tooltip.TooltipComponent;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

import static org.dominokit.domino.ui.style.SpacingCss.dui_rounded_xl;

/**
 * Abstraction for working with a HTML anchor.
 */
public final class HistoryTokenAnchorComponent extends HistoryTokenAnchorComponentLike {

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

        this.text = ElementsFactory.elements.text();

        this.element = element.appendChild(this.text);
        this.setCssText("margin: 5px; font-family: \"Inter\"; font-size: 15px; font-weight: 400; text-wrap: nowrap;");
        this.setDisabled(true);

        this.tooltip = Optional.empty();
    }

    // HtmlComponent....................................................................................................

    @Override
    public int width() {
        return this.element.element()
            .offsetWidth;
    }

    @Override
    public int height() {
        return this.element.element()
            .offsetHeight;
    }

    @Override
    public HistoryTokenAnchorComponent setCssText(final String css) {
        Objects.requireNonNull(css, "css");

        this.element.style().cssText(css);
        return this;
    }

    @Override
    public HistoryTokenAnchorComponent setCssProperty(final String name,
                                                      final String value) {
        this.element.setCssProperty(
            name,
            value
        );
        return this;
    }

    @Override
    public HistoryTokenAnchorComponent removeCssProperty(final String name) {
        this.element.removeCssProperty(name);
        return this;
    }

    // disabled.........................................................................................................

    @Override
    public HistoryTokenAnchorComponent setDisabled(final boolean disabled) {
        this.element.setAttribute("aria-disabled", disabled);

        final HTMLAnchorElement element = this.element();

        // if disabled remove href and tabIndex
        if (disabled) {
            element.removeAttribute("href"); // cant assign null, because href will still be present and isDisabled() wll be confused and report false
            element.removeAttribute("tabindex");
        } else {
            element.tabIndex = this.tabIndex;
        }

        final CSSStyleDeclaration style = element.style;

        // disabled is black, without underline and not-allowed pointer
        // enabled is blue, underlined and pointer=pointer
        style.textDecoration = disabled ? "none" : "underline";

        style.cursor = disabled ?
            "not-allowed !important" :
            "pointer !important";
        style.color = disabled ?
            "var(--dui-hyperlink-disabled-color)" :
            "var(--dui-hyperlink-color)";

        this.iconBefore = null;
        this.iconAfter = null;

        return this;
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
        CharSequences.failIfNullOrEmpty(id, "id");

        this.element.setId(id);
        return this;
    }

    // tabIndex.........................................................................................................

    @Override
    public int tabIndex() {
        return this.tabIndex;
    }

    @Override
    public HistoryTokenAnchorComponent setTabIndex(final int tabIndex) {
        this.element.setTabIndex(tabIndex);
        this.tabIndex = tabIndex;
        return this;
    }

    private int tabIndex;

    // target.........................................................................................................

    @Override
    public String target() {
        return this.element().getAttribute(TARGET);
    }

    /**
     * Sets the target attribute of an ANCHOR.<br>
     * Typical values include:
     * <li>
     * <li>_self</li>
     * <li>_blank</li>
     * <li>_parent</li>
     * <li>_top</li>
     * <li>_unfencedTop</li>
     * </li>
     * https://developer.mozilla.org/en-US/docs/Web/HTML/Element/a#target
     */
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
        return this.text.textContent;
    }

    @Override
    public HistoryTokenAnchorComponent setTextContent(final String text) {
        this.text.textContent = text;
        return this;
    }

    /**
     * This is the {@link Text} that receives text.
     * Element#setTextContent cannot be used as it will replace any badges etc.
     */
    private final Text text;

    // badge............................................................................................................

    @Override
    public String badge() {
        final Badge badge = this.badge;
        return null != badge ?
            badge.getTextContent() :
            "";
    }

    @Override
    public HistoryTokenAnchorComponent setBadge(final String badgeText) {
        Objects.requireNonNull(badgeText, "badgeText");

        Badge badge = this.badge;
        if (badgeText.isEmpty()) {
            if (null != badge) {
                this.element.removeChild(
                    badge.parent()
                );
                this.badge = null;
            }
        } else {
            if (null == badge) {
                badge = Badge.create(badgeText)
                    .addCss(
                        dui_rounded_xl
                    );
                this.element.appendChild(
                    PostfixAddOn.of(badge)
                );
                this.badge = badge;
            }
            badge.setTextContent(badgeText);

            final boolean zero = badgeText.equals("0");
            this.badge.addCss(
                zero ?
                    SpreadsheetDominoKitColor.BADGE_ZERO_COLOR :
                    SpreadsheetDominoKitColor.BADGE_NON_ZERO_COLOR
            ).removeCss(
                zero ?
                    SpreadsheetDominoKitColor.BADGE_NON_ZERO_COLOR :
                    SpreadsheetDominoKitColor.BADGE_ZERO_COLOR
            );
        }

        return this;
    }

    private Badge badge;

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

    // addXXXListener...................................................................................................

    @Override
    public HistoryTokenAnchorComponent addChangeListener(final ChangeListener<Optional<HistoryToken>> listener) {
        Objects.requireNonNull(listener, "listener");
        throw new UnsupportedOperationException();
    }

    /**
     * If this anchor is disabled calls {@link Event#preventDefault()} and skips calling the given {@link EventListener}.
     */
    @Override
    HistoryTokenAnchorComponent addEventListener(final EventType eventType,
                                                 final EventListener listener) {
        Objects.requireNonNull(listener, "listener");

        this.element.addEventListener(
            eventType,
            (e) -> {
                if (this.isDisabled()) {
                    e.preventDefault();
                } else {
                    listener.handleEvent(e);
                }
            }
        );
        return this;
    }

    // focus............................................................................................................

    @Override
    public HistoryTokenAnchorComponent focus() {
        this.element().focus();
        return this;
    }

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return HtmlComponent.hasFocus(this.element());
    }

    // isElement........................................................................................................

    @Override
    public HTMLAnchorElement element() {
        return this.element.element();
    }

    final AnchorElement element;

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

    // TooltipComponentTarget................................................................................

    @Override
    public void tooltipAttached(final TooltipComponent tooltip) {
        Objects.requireNonNull(tooltip, "tooltip");

        this.tooltip = Optional.of(tooltip);
    }

    @Override
    public void tooltipDetached() {
        this.tooltip = Optional.empty();
    }

    @Override
    public Optional<TooltipComponent> spreadsheetTooltipComponent() {
        return this.tooltip;
    }

    private Optional<TooltipComponent> tooltip;
}
