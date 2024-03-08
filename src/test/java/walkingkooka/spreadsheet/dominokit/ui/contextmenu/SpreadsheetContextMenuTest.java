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
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContexts;

import java.util.Optional;

public final class SpreadsheetContextMenuTest implements ClassTesting<SpreadsheetContextMenu> {

    @Test
    public void testItemSpreadsheetContextMenuItem() {
        SpreadsheetContextMenu.with(
                new Menu<>(),
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
                        new Menu<>(),
                        HistoryTokenContexts.fake()
                ).separator()
                .item(
                        new IsElement<Element>() {
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
                        new Menu<>(),
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
                new Menu<>(),
                HistoryTokenContexts.fake()
        ).subMenu(
                "id-SubMenu",
                "SubMenu"
        );
    }

    @Test
    public void testSubMenuIdTextBadge() {
        SpreadsheetContextMenu.with(
                new Menu<>(),
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
                new Menu<>(),
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
                new Menu<>(),
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
                new Menu<>(),
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
