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
import org.dominokit.domino.ui.icons.MdiIcon;
import org.dominokit.domino.ui.menu.AbstractMenuItem;
import org.dominokit.domino.ui.menu.Menu;
import org.dominokit.domino.ui.menu.MenuItem;
import org.dominokit.domino.ui.menu.direction.MouseBestFitDirection;
import org.dominokit.domino.ui.utils.DominoElement;
import org.dominokit.domino.ui.utils.PostfixAddOn;
import org.dominokit.domino.ui.utils.PrefixAddOn;
import org.dominokit.domino.ui.utils.Separator;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIcons;

import java.util.Objects;
import java.util.Optional;

import static org.dominokit.domino.ui.style.ColorsCss.dui_bg_orange;
import static org.dominokit.domino.ui.style.SpacingCss.dui_font_size_5;
import static org.dominokit.domino.ui.style.SpacingCss.dui_rounded_full;

/**
 * This is a bridge that performs actual real operations to create or add items to a domino {@link Menu}.
 * Note the test source also contains a Menu class which is basically empty and only aggregates all added items.
 * This should allow writing of unit tests to verify a menu without browser native objects failing to function.
 */
public final class SpreadsheetContextMenuNative {

    /**
     * Factory that builds a {@link SpreadsheetContextMenu}.
     */
    public static SpreadsheetContextMenu empty(final DominoElement<?> element,
                                               final HistoryTokenContext context) {
        Objects.requireNonNull(element, "element");
        Objects.requireNonNull(context, "context");

        final Menu<Void> menu = Menu.<Void>create()
                .setContextMenu(true)
                .setDropDirection(new MouseBestFitDirection())
                .setTargetElement(element);
        element.setDropMenu(menu);

        return SpreadsheetContextMenu.with(
                menu,
                context
        );
    }

    static Menu<Void> addSubMenu(final String id,
                                 final String text,
                                 final Optional<MdiIcon> icon,
                                 final Optional<String> badge,
                                 final SpreadsheetContextMenu menu) {
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

        menu.menu.appendChild(
                menuItem.setMenu(subMenu)
        );

        return subMenu;
    }

    static void menuAppendChildSpreadsheetContextMenuItem(final SpreadsheetContextMenuItem item,
                                                          final SpreadsheetContextMenu menu) {
        AbstractMenuItem<Void> menuItem = menu.context.menuItem(
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
        if (false == item.enabled) {
            // TODO https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/2019 anchor.setDisabled(true)
        }

        menu.menu.appendChild(menuItem);
    }

    static void menuAppendChildIsElement(final IsElement<?> isElement,
                                         final SpreadsheetContextMenu menu) {
        menu.menu.appendChild(
                isElement.element()
        );
    }

    static void menuAppendChildSeparator(final SpreadsheetContextMenu menu) {
        menu.menu.appendChild(new Separator());
    }
}
