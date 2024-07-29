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

import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.menu.direction.DropDirection;
import org.junit.jupiter.api.Test;
import walkingkooka.net.Url;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContexts;
import walkingkooka.spreadsheet.dominokit.reference.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.reference.SpreadsheetContextMenuItem;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.ui.tooltip.SpreadsheetTooltipComponent;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Optional;

public final class HistoryTokenAnchorComponentTest implements TreePrintableTesting,
        ClassTesting<HistoryTokenAnchorComponent> {

    // historyToken.....................................................................................................

    @Test
    public void testSetHistoryTokenEmpty() {
        this.treePrintAndCheck(
                HistoryTokenAnchorComponent.empty(),
                ""
        );
    }

    @Test
    public void testSetHistoryToken() {
        final String href = "/1/SpreadsheetName234/cell/A1";

        this.treePrintAndCheck(
                HistoryTokenAnchorComponent.empty()
                        .setHistoryToken(
                                Optional.of(
                                        HistoryToken.parseString(href)
                                )
                        ),
                "[#/1/SpreadsheetName234/cell/A1]"
        );
    }

    // href.............................................................................................................

    @Test
    public void testSetHrefNull() {
        this.treePrintAndCheck(
                HistoryTokenAnchorComponent.empty()
                        .setHref(null),
                "DISABLED"
        );
    }

    @Test
    public void testSetHref() {
        final String href = "#/1/SpreadsheetName234/cell/A1";

        this.treePrintAndCheck(
                HistoryTokenAnchorComponent.empty()
                        .setHref(
                                Url.parseAbsoluteOrRelative(href)
                        ),
                "[" + href + "]"
        );
    }

    // iconAfter.......................................................................................................

    @Test
    public void testSetIconAfter() {
        this.treePrintAndCheck(
                HistoryTokenAnchorComponent.empty()
                        .setIconAfter(
                                Optional.of(
                                        new Icon<>("Icon123")
                                )
                        )
                        .setHref(
                                Url.parseAbsoluteOrRelative("#/1/SpreadsheetName234/cell/A1")
                        ),
                "[#/1/SpreadsheetName234/cell/A1] Icon123"
        );
    }

    // iconBefore.......................................................................................................

    @Test
    public void testSetIconBefore() {
        this.treePrintAndCheck(
                HistoryTokenAnchorComponent.empty()
                        .setIconBefore(
                                Optional.of(
                                        new Icon<>("Icon123")
                                )
                        )
                        .setHref(
                                Url.parseAbsoluteOrRelative("#/1/SpreadsheetName234/cell/A1")
                        ),
                "Icon123 [#/1/SpreadsheetName234/cell/A1]"
        );
    }

    // textContent.......................................................................................................

    @Test
    public void testSetTextContent() {
        this.treePrintAndCheck(
                HistoryTokenAnchorComponent.empty()
                        .setTextContent(
                                "Text123"
                        )
                        .setHref(
                                Url.parseAbsoluteOrRelative("#/1/SpreadsheetName234/cell/A1")
                        ),
                "\"Text123\" [#/1/SpreadsheetName234/cell/A1]"
        );
    }

    // all..............................................................................................................

    @Test
    public void testAll() {
        this.treePrintAndCheck(
                HistoryTokenAnchorComponent.empty()
                        .setChecked(true)
                        .setIconAfter(
                                Optional.of(
                                        new Icon<>("IconAfter123")
                                )
                        ).setIconBefore(
                                Optional.of(
                                        new Icon<>("IconBefore456")
                                )
                        ).setTextContent(
                                "Text789"
                        ).setHref(
                                Url.parseAbsoluteOrRelative("#/1/SpreadsheetName234/cell/A1")
                        ).setId("id789"),
                "IconBefore456 \"Text789\" [#/1/SpreadsheetName234/cell/A1] CHECKED IconAfter123 id=id789"
        );
    }

    @Test
    public void testAllDisabled() {
        this.treePrintAndCheck(
                HistoryTokenAnchorComponent.empty()
                        .setChecked(true)
                        .setIconAfter(
                                Optional.of(
                                        new Icon<>("IconAfter123")
                                )
                        ).setIconBefore(
                                Optional.of(
                                        new Icon<>("IconBefore456")
                                )
                        ).setTextContent(
                                "Text789"
                        )
                        .setHref(
                                Url.parseAbsoluteOrRelative("#/1/SpreadsheetName234/cell/A1")
                        ).setDisabled(true),
                "IconBefore456 \"Text789\" DISABLED [#/1/SpreadsheetName234/cell/A1] CHECKED IconAfter123"
        );
    }

    // setSpreadsheetContextMenu........................................................................................

    @Test
    public void testSetSpreadsheetContextMenu() {
        final HistoryTokenAnchorComponent anchor = HistoryTokenAnchorComponent.empty()
                .setTextContent("Hello")
                .setHref(
                        Url.parseAbsoluteOrRelative("#/1/SpreadsheetName234/cell/A1")
                );
        final SpreadsheetContextMenu menu = SpreadsheetContextMenu.wrap(
                anchor,
                HistoryTokenContexts.fake()
        ).item(
                SpreadsheetContextMenuItem.with(
                        "menu-item-1" + SpreadsheetElementIds.MENU_ITEM,
                        "Item 1"
                )
        ).item(
                SpreadsheetContextMenuItem.with(
                        "menu-item-2" + SpreadsheetElementIds.MENU_ITEM,
                        "Item 2"
                )
        ).item(
                SpreadsheetContextMenuItem.with(
                        "menu-item-3" + SpreadsheetElementIds.MENU_ITEM,
                        "Item 3"
                )
        );

        anchor.setSpreadsheetContextMenu(menu);

        this.treePrintAndCheck(
                anchor,
                "\"Hello\" [#/1/SpreadsheetName234/cell/A1]\n" +
                        "    \"Item 1\" id=menu-item-1-MenuItem\n" +
                        "    \"Item 2\" id=menu-item-2-MenuItem\n" +
                        "    \"Item 3\" id=menu-item-3-MenuItem\n"
        );
    }

    // Tooltip..........................................................................................................

    @Test
    public void testSpreadsheetTooltipComponent() {
        final HistoryTokenAnchorComponent anchor = HistoryTokenAnchorComponent.empty()
                .setTextContent("Hello")
                .setHref(
                        Url.parseAbsoluteOrRelative("#/1/SpreadsheetName234/cell/A1")
                );
        final SpreadsheetTooltipComponent tooltip = SpreadsheetTooltipComponent.attach(
                anchor,
                "Tooltip123",
                DropDirection.BOTTOM_MIDDLE
        );

        this.treePrintAndCheck(
                anchor,
                "\"Hello\" [#/1/SpreadsheetName234/cell/A1]\n" +
                        "  SpreadsheetTooltipComponent\n" +
                        "    \"Tooltip123\"\n"
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<HistoryTokenAnchorComponent> type() {
        return HistoryTokenAnchorComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
