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

import elemental2.dom.Element;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.icons.MdiIcon;
import org.dominokit.domino.ui.menu.Menu;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContexts;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIcons;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Optional;

public final class SpreadsheetContextMenuTest implements ClassTesting<SpreadsheetContextMenu>,
        TreePrintableTesting {

    @Test
    public void testItemSpreadsheetContextMenuItem() {
        SpreadsheetContextMenu.with(
                topLevelMenu(),
                HistoryTokenContexts.fake()
        ).item(
                SpreadsheetContextMenuItem.with(
                        "id-MenuItem",
                        "SubMenuText"
                )
        );
    }

    @Test
    public void testItemIsElement() {
        SpreadsheetContextMenu.with(
                        topLevelMenu(),
                        HistoryTokenContexts.fake()
                ).separator()
                .item(
                        new IsElement<>() {
                            @Override
                            public Element element() {
                                throw new UnsupportedOperationException();
                            }
                        }
                );
    }

    @Test
    public void testSeparatorThenItem() {
        SpreadsheetContextMenu.with(
                        topLevelMenu(),
                        HistoryTokenContexts.fake()
                ).separator()
                .item(
                        SpreadsheetContextMenuItem.with(
                                "id-MenuItem",
                                "SubMenuText"
                        )
                );
    }

    @Test
    public void testSubMenuIdText() {
        SpreadsheetContextMenu.with(
                topLevelMenu(),
                HistoryTokenContexts.fake()
        ).subMenu(
                "id-SubMenu",
                "SubMenu"
        );
    }

    @Test
    public void testSubMenuIdTextBadge() {
        SpreadsheetContextMenu.with(
                topLevelMenu(),
                HistoryTokenContexts.fake()
        ).subMenu(
                "id-SubMenu",
                "SubMenu",
                "Badge-text-123"
        );
    }

    @Test
    public void testSubMenuIdTextIcon() {
        SpreadsheetContextMenu.with(
                topLevelMenu(),
                HistoryTokenContexts.fake()
        ).subMenu(
                "id-SubMenu",
                "SubMenu",
                MdiIcon.create("Icon-123")
        );
    }

    @Test
    public void testSubMenuIdTextIconBadge() {
        SpreadsheetContextMenu.with(
                topLevelMenu(),
                HistoryTokenContexts.fake()
        ).subMenu(
                "id-SubMenu",
                "SubMenu",
                Optional.of(
                        MdiIcon.create("Icon-123")
                ),
                Optional.of("Badge-text-123")
        );
    }

    @Test
    public void testSubMenuIdTextThenItem() {
        SpreadsheetContextMenu.with(
                topLevelMenu(),
                HistoryTokenContexts.fake()
        ).subMenu(
                "id-SubMenu",
                "SubMenu"
        ).item(
                SpreadsheetContextMenuItem.with(
                        "id-MenuItem",
                        "item-text-123"
                )
        );
    }

    // TreePrintable....................................................................................................

    @Test
    public void testTreePrintMenuWithItems() {
        final SpreadsheetContextMenu menu = SpreadsheetContextMenu.with(
                topLevelMenu(),
                HistoryTokenContexts.fake()
        ).item(
                SpreadsheetContextMenuItem.with(
                        "id-MenuItem",
                        "item-text-111"
                )
        ).item(
                SpreadsheetContextMenuItem.with(
                        "id-MenuItem",
                        "item-text-222"
                )
        );

        this.treePrintAndCheck(
                menu,
                "id-top-Menu \"Top Menu!\"\n" +
                        "  id-MenuItem \"item-text-111\"\n" +
                        "  id-MenuItem \"item-text-222\"\n"
        );
    }

    @Test
    public void testTreePrintMenuWithItemsIncludingSeparator() {
        final SpreadsheetContextMenu menu = SpreadsheetContextMenu.with(
                        topLevelMenu(),
                        HistoryTokenContexts.fake()
                ).item(
                        SpreadsheetContextMenuItem.with(
                                "id-MenuItem",
                                "item-text-111"
                        )
                ).separator()
                .item(
                        SpreadsheetContextMenuItem.with(
                                "id-MenuItem",
                                "item-text-222"
                        )
                );

        this.treePrintAndCheck(
                menu,
                "id-top-Menu \"Top Menu!\"\n" +
                        "  id-MenuItem \"item-text-111\"\n" +
                        "  -----\n" +
                        "  id-MenuItem \"item-text-222\"\n"
        );
    }

    @Test
    public void testTreePrintMenuWithItemsWithIconAndBadge() {
        final SpreadsheetContextMenu menu = SpreadsheetContextMenu.with(
                topLevelMenu(),
                HistoryTokenContexts.fake()
        ).item(
                SpreadsheetContextMenuItem.with(
                        "id-MenuItem",
                        "item-text-111"
                ).icon(
                        Optional.of(
                                SpreadsheetIcons.reload()
                        )
                ).badge(
                        Optional.of(
                                "Badge-text-111"
                        )
                )
        ).item(
                SpreadsheetContextMenuItem.with(
                        "id-MenuItem",
                        "item-text-222"
                ).badge(
                        Optional.of(
                                "Badge-text-222"
                        )
                )
        ).item(
                SpreadsheetContextMenuItem.with(
                        "id-MenuItem",
                        "item-text-333"
                ).icon(
                        Optional.of(
                                SpreadsheetIcons.alignLeft()
                        )
                )
        );

        this.treePrintAndCheck(
                menu,
                "id-top-Menu \"Top Menu!\"\n" +
                        "  (mdi-reload) id-MenuItem \"item-text-111\" [Badge-text-111]\n" +
                        "  id-MenuItem \"item-text-222\" [Badge-text-222]\n" +
                        "  (mdi-format-align-left) id-MenuItem \"item-text-333\"\n"
        );
    }

    @Test
    public void testTreePrintMenuWithWithCheckedItem() {
        final SpreadsheetContextMenu menu = SpreadsheetContextMenu.with(
                topLevelMenu(),
                HistoryTokenContexts.fake()
        ).item(
                SpreadsheetContextMenuItem.with(
                        "id-MenuItem",
                        "item-text-111-checked"
                ).checked(true)
        ).item(
                SpreadsheetContextMenuItem.with(
                        "id-MenuItem",
                        "item-text-222"
                )
        );

        this.treePrintAndCheck(
                menu,
                "id-top-Menu \"Top Menu!\"\n" +
                        "  id-MenuItem \"item-text-111-checked\" CHECKED\n" +
                        "  id-MenuItem \"item-text-222\"\n"
        );
    }

    @Test
    public void testTreePrintMenuWithItemsWithHistoryToken() {
        final SpreadsheetContextMenu menu = SpreadsheetContextMenu.with(
                topLevelMenu(),
                HistoryTokenContexts.fake()
        ).item(
                SpreadsheetContextMenuItem.with(
                        "id-MenuItem",
                        "item-text-111"
                ).historyToken(
                        Optional.of(
                                HistoryToken.spreadsheetReload(
                                        SpreadsheetId.with(1),
                                        SpreadsheetName.with("Spreadsheet-Name-111")
                                )
                        )
                )
        ).item(
                SpreadsheetContextMenuItem.with(
                        "id-MenuItem",
                        "item-text-222"
                ).historyToken(
                        Optional.of(
                                HistoryToken.cellDelete(
                                        SpreadsheetId.with(1),
                                        SpreadsheetName.with("Spreadsheet-Name-222"),
                                        SpreadsheetSelection.A1.setDefaultAnchor()
                                )
                        )
                )
        ).item(
                SpreadsheetContextMenuItem.with(
                        "id-MenuItem",
                        "item-text-333"
                )
        );

        this.treePrintAndCheck(
                menu,
                "id-top-Menu \"Top Menu!\"\n" +
                        "  id-MenuItem \"item-text-111\" [/1/Spreadsheet-Name-111/reload]\n" +
                        "  id-MenuItem \"item-text-222\" [/1/Spreadsheet-Name-222/cell/A1/delete]\n" +
                        "  id-MenuItem \"item-text-333\"\n"
        );
    }

    @Test
    public void testTreePrintWithSubMenu() {
        SpreadsheetContextMenu menu = SpreadsheetContextMenu.with(
                topLevelMenu(),
                HistoryTokenContexts.fake()
        ).item(
                SpreadsheetContextMenuItem.with(
                        "id-1-MenuItem",
                        "item-text-111"
                )
        ).item(
                SpreadsheetContextMenuItem.with(
                        "id-2-MenuItem",
                        "item-text-222"
                )
        );

        final SpreadsheetContextMenu subMenu = menu.subMenu(
                "id-3-SubMenu",
                "sub-menu-item-text-333"
        ).item(
                SpreadsheetContextMenuItem.with(
                        "id-4-MenuItem",
                        "item-text-444"
                )
        );

        this.treePrintAndCheck(
                menu,
                "id-top-Menu \"Top Menu!\"\n" +
                        "  id-1-MenuItem \"item-text-111\"\n" +
                        "  id-2-MenuItem \"item-text-222\"\n" +
                        "  id-3-SubMenu \"sub-menu-item-text-333\"\n" +
                        "    id-4-MenuItem \"item-text-444\"\n"
        );
    }

    @Test
    public void testTreePrintWithTree() {
        SpreadsheetContextMenu menu = SpreadsheetContextMenu.with(
                topLevelMenu(),
                HistoryTokenContexts.fake()
        ).item(
                SpreadsheetContextMenuItem.with(
                        "id-1-MenuItem",
                        "item-text-111"
                )
        ).item(
                SpreadsheetContextMenuItem.with(
                        "id-2-MenuItem",
                        "item-text-222"
                )
        );

        SpreadsheetContextMenu subMenu = menu.subMenu(
                "id-3-SubMenu",
                "sub-menu-item-text-333"
        ).item(
                SpreadsheetContextMenuItem.with(
                        "id-4-MenuItem",
                        "item-text-444"
                )
        );

        final SpreadsheetContextMenu subSubMenu = menu.subMenu(
                "id-5-SubMenu",
                "sub-sub-menu-item-text-555"
        ).item(
                SpreadsheetContextMenuItem.with(
                        "id-6-MenuItem",
                        "item-text-666"
                )
        );

        subMenu.item(
                SpreadsheetContextMenuItem.with(
                        "id-7-MenuItem",
                        "item-text-777"
                )
        );

        this.treePrintAndCheck(
                menu,
                "id-top-Menu \"Top Menu!\"\n" +
                        "  id-1-MenuItem \"item-text-111\"\n" +
                        "  id-2-MenuItem \"item-text-222\"\n" +
                        "  id-3-SubMenu \"sub-menu-item-text-333\"\n" +
                        "    id-4-MenuItem \"item-text-444\"\n" +
                        "    id-7-MenuItem \"item-text-777\"\n" +
                        "  id-5-SubMenu \"sub-sub-menu-item-text-555\"\n" +
                        "    id-6-MenuItem \"item-text-666\"\n"
        );
    }

    private static Menu<Void> topLevelMenu() {
        return Menu.create(
                "id-top-Menu",
                "Top Menu!",
                Optional.empty(), // no icon
                Optional.empty() // no badge
        );
    }

    // ClassTesting...................................................................................................,,

    @Override
    public Class<SpreadsheetContextMenu> type() {
        return SpreadsheetContextMenu.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
