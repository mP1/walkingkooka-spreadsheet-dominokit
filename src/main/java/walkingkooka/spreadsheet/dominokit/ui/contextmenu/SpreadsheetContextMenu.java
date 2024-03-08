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

package walkingkooka.spreadsheet.dominokit.ui.contextmenu;

import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.badges.Badge;
import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.icons.MdiIcon;
import org.dominokit.domino.ui.menu.AbstractMenuItem;
import org.dominokit.domino.ui.menu.Menu;
import org.dominokit.domino.ui.menu.MenuItem;
import org.dominokit.domino.ui.menu.direction.MouseBestFitDirection;
import org.dominokit.domino.ui.utils.DominoElement;
import org.dominokit.domino.ui.utils.PostfixAddOn;
import org.dominokit.domino.ui.utils.PrefixAddOn;
import org.dominokit.domino.ui.utils.Separator;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.ui.selectionmenu.SpreadsheetSelectionMenuContext;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;

import static org.dominokit.domino.ui.style.ColorsCss.dui_bg_orange;
import static org.dominokit.domino.ui.style.SpacingCss.dui_font_size_5;
import static org.dominokit.domino.ui.style.SpacingCss.dui_rounded_full;

/**
 * Abstraction for building a context menu.
 */
public class SpreadsheetContextMenu {

    public static SpreadsheetContextMenu empty(final DominoElement<?> element,
                                               final HistoryTokenContext context) {
        Objects.requireNonNull(element, "element");
        Objects.requireNonNull(context, "context");

        final Menu<Void> menu = Menu.<Void>create()
                .setContextMenu(true)
                .setDropDirection(new MouseBestFitDirection())
                .setTargetElement(element);
        element.setDropMenu(menu);

        return new SpreadsheetContextMenu(
                menu,
                context
        );
    }

    private SpreadsheetContextMenu(final Menu<Void> menu,
                                   final HistoryTokenContext context) {
        this.menu = menu;
        this.context = context;
        this.allowSeparator = false;
    }

    /**
     * Creates an empty sub menu returning the {@link SpreadsheetContextMenu} which may be used to add items.
     */
    public SpreadsheetContextMenu subMenu(final String id,
                                          final String text) {
        return this.subMenu(
                id,
                text,
                Optional.empty(), // icon
                Optional.empty() // badge
        );
    }

    /**
     * Creates an empty sub menu returning the {@link SpreadsheetContextMenu} which may be used to add items.
     */
    public SpreadsheetContextMenu subMenu(final String id,
                                          final String text,
                                          final MdiIcon icon) {
        return this.subMenu(
                id,
                text,
                Optional.of(icon),
                Optional.empty()
        );
    }

    /**
     * Creates an empty sub menu returning the {@link SpreadsheetContextMenu} which may be used to add items.
     */
    public SpreadsheetContextMenu subMenu(final String id,
                                          final String text,
                                          final String badge) {
        return this.subMenu(
                id,
                text,
                Optional.empty(),
                Optional.of(badge)
        );
    }

    /**
     * Creates an empty sub menu returning the {@link SpreadsheetContextMenu} which may be used to add items.
     */
    public SpreadsheetContextMenu subMenu(final String id,
                                          final String text,
                                          final Optional<MdiIcon> icon,
                                          final Optional<String> badge) {
        CharSequences.failIfNullOrEmpty(id, "id");
        if (false == id.endsWith(SUB_MENU_ID_SUFFIX)) {
            throw new IllegalArgumentException(
                    "Invalid subMenu id " +
                            CharSequences.quote(id) +
                            " missing " +
                            CharSequences.quote(SUB_MENU_ID_SUFFIX)
            );
        }

        checkText(text);

        this.addSeparatorIfNecessary();

        final Menu<Void> subMenu = Menu.create();

        AbstractMenuItem<Void> menuItem = MenuItem.<Void>create(text)
                .setId(id);
        if (icon.isPresent()) {
            menuItem = menuItem.appendChild(
                    PrefixAddOn.of(
                            icon.get()
                                    .addCss(dui_font_size_5)
                    )
            );
        }

        if (badge.isPresent()) {
            menuItem.appendChild(
                    PostfixAddOn.of(
                            Badge.create(
                                    badge.get()
                            ).addCss(
                                    dui_bg_orange,
                                    dui_rounded_full
                            )
                    )
            );
        }

        this.menu.appendChild(
                menuItem.setMenu(subMenu)
        );
        this.allowSeparator = true;

        return new SpreadsheetContextMenu(
                subMenu,
                this.context
        );
    }

    private final static String SUB_MENU_ID_SUFFIX = "-SubMenu";

    /**
     * Adds a checked menu item, conditionally setting the check mark and conditional clearing/saving the value.
     */
    public <T> SpreadsheetContextMenu checkedItem(final String id,
                                                  final String text,
                                                  final Optional<Icon<?>> icon,
                                                  final TextStylePropertyName<T> stylePropertyName,
                                                  final T stylePropertyValue,
                                                  final SpreadsheetSelectionMenuContext context) {
        final boolean set = context.isChecked(
                stylePropertyName,
                stylePropertyValue
        );
        return this.item(
                this.context.historyToken()
                        .setStyle(stylePropertyName)
                        .setSave(
                                Optional.ofNullable(
                                        set ?
                                                null :
                                                stylePropertyValue
                                )
                        ).contextMenuItem(
                                id,
                                text
                        ).icon(icon)
                        .checked(set)
        );
    }

    /**
     * Adds the given {@link SpreadsheetContextMenuItem} to this menu.
     */
    public SpreadsheetContextMenu item(final SpreadsheetContextMenuItem item) {
        Objects.requireNonNull(item, "item");

        this.addSeparatorIfNecessary();

        AbstractMenuItem<Void> menuItem = this.context.menuItem(
                item.id,
                item.text,
                item.historyToken
        );
        if (item.icon.isPresent()) {
            menuItem = menuItem.appendChild(
                    PrefixAddOn.of(
                            item.icon.get()
                                    .addCss(dui_font_size_5)
                    )
            );
        }

        if (item.badge.isPresent()) {
            menuItem.appendChild(
                    PostfixAddOn.of(
                            Badge.create(
                                    item.badge.get()
                            )
                    )
            );
        }

        if (item.checked) {
            menuItem.appendChild(
                    PostfixAddOn.of(
                            SpreadsheetIcons.checked()
                    )
            );
        }

        this.menu.appendChild(menuItem);
        this.allowSeparator = true;

        return this;
    }

    /**
     * Adds a {@link IsElement} which is responsible for providing the content of the menu item.
     */
    public SpreadsheetContextMenu item(final IsElement<?> component) {
        Objects.requireNonNull(component, "component");

        this.addSeparatorIfNecessary();

        this.menu.appendChild(
                component.element()
        );
        this.allowSeparator = true;
        return this;
    }

    private String checkText(final String text) {
        return CharSequences.failIfNullOrEmpty(text, "text");
    }

    /**
     * This method is used to lazily add a separator if one was added recently. This includes smarts so multiple
     * separators are not added in series or a separator with no items afterwards is also not possible.
     */
    private void addSeparatorIfNecessary() {
        if (this.addSeparator) {
            this.menu.appendChild(new Separator());
            this.addSeparator = false;
            this.allowSeparator = false;
        }
    }

    /**
     * Adds a separator before the next item is added.
     */
    public SpreadsheetContextMenu separator() {
        if (this.allowSeparator) {
            this.addSeparator = true;
        }
        return this;
    }

    private boolean addSeparator;

    /**
     * Used to prevent adding a separator as the first item, and to prevent adding to a separator immediately after another.
     */
    private boolean allowSeparator;

    /**
     * Gives focus to the menu
     */
    public SpreadsheetContextMenu focus() {
        this.menu.open(true);
        this.context.addHistoryTokenWatcherOnce(
                (final HistoryToken previous,
                 final AppContext context) ->
                        SpreadsheetContextMenu.this.close()
        );
        return this;
    }

    /**
     * Closes the menu if it is open.
     */
    public void close() {
        this.menu.close();
    }

    private final Menu<Void> menu;

    private final HistoryTokenContext context;

    @Override
    public String toString() {
        return this.menu.toString();
    }
}
