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

import elemental2.dom.HTMLAnchorElement;
import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.menu.direction.DropDirection;
import org.junit.jupiter.api.Test;
import walkingkooka.net.Url;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuItem;
import walkingkooka.spreadsheet.dominokit.tooltip.TooltipComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;
import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HistoryTokenAnchorComponentTest implements HtmlComponentTesting<HistoryTokenAnchorComponent, HTMLAnchorElement> {

    // setCount.........................................................................................................

    private final static Optional<HistoryToken> CELL_REFERENCES_HISTORY_TOKEN = Optional.of(
        HistoryToken.cellReferences(
            SpreadsheetId.with(1),
            SpreadsheetName.with("Spreadsheet222"),
            SpreadsheetSelection.A1.setDefaultAnchor(),
            HistoryTokenOffsetAndCount.EMPTY
        )
    );

    @Test
    public void testSetCountWithEmpty() {
        this.treePrintAndCheck(
            HistoryTokenAnchorComponent.empty()
                .setHistoryToken(CELL_REFERENCES_HISTORY_TOKEN)
                .setCount(OptionalInt.empty()),
            "[#/1/Spreadsheet222/cell/A1/references]"
        );
    }

    @Test
    public void testSetCountWithZero() {
        this.treePrintAndCheck(
            HistoryTokenAnchorComponent.empty()
                .setHistoryToken(CELL_REFERENCES_HISTORY_TOKEN)
                .setCount(OptionalInt.of(0)),
            "[#/1/Spreadsheet222/cell/A1/references] (0)"
        );
    }

    @Test
    public void testSetCountWithNonZero() {
        this.treePrintAndCheck(
            HistoryTokenAnchorComponent.empty()
                .setHistoryToken(CELL_REFERENCES_HISTORY_TOKEN)
                .setCount(OptionalInt.of(123)),
            "[#/1/Spreadsheet222/cell/A1/references] (123)"
        );
    }

    // historyToken.....................................................................................................

    @Test
    public void testSetHistoryTokenEmpty() {
        this.treePrintAndCheck(
            HistoryTokenAnchorComponent.empty(),
            "DISABLED"
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

    // id...............................................................................................................

    @Test
    public void testSetIdWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> HistoryTokenAnchorComponent.empty()
                .setId(null)
        );
    }

    @Test
    public void testSetIdWithEmptyails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> HistoryTokenAnchorComponent.empty()
                .setId("")
        );
    }

    // setFlag..........................................................................................................

    @Test
    public void testSetFlagWithEmpty() {
        this.treePrintAndCheck(
            HistoryTokenAnchorComponent.empty()
                .setFlag("")
                .setHref(
                    Url.parseAbsoluteOrRelative("#/1/SpreadsheetName234/cell/A1")
                ),
            "[#/1/SpreadsheetName234/cell/A1]"
        );
    }

    @Test
    public void testSetFlagWithInvalid() {
        this.treePrintAndCheck(
            HistoryTokenAnchorComponent.empty()
                .setFlag("AUS")
                .setHref(
                    Url.parseAbsoluteOrRelative("#/1/SpreadsheetName234/cell/A1")
                ),
            "[#/1/SpreadsheetName234/cell/A1] [AUS]"
        );
    }

    @Test
    public void testSetFlag() {
        this.treePrintAndCheck(
            HistoryTokenAnchorComponent.empty()
                .setFlag("AU")
                .setHref(
                    Url.parseAbsoluteOrRelative("#/1/SpreadsheetName234/cell/A1")
                ),
            "[#/1/SpreadsheetName234/cell/A1] [AU]"
        );
    }

    @Test
    public void testSetFlagAndText() {
        this.treePrintAndCheck(
            HistoryTokenAnchorComponent.empty()
                .setFlag("AU")
                .setTextContent("Hello")
                .setHref(
                    Url.parseAbsoluteOrRelative("#/1/SpreadsheetName234/cell/A1")
                ),
            "\"Hello\" [#/1/SpreadsheetName234/cell/A1] [AU]"
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

    // badge............................................................................................................

    @Test
    public void testSetBadge() {
        this.treePrintAndCheck(
            HistoryTokenAnchorComponent.empty()
                .setTextContent(
                    "Text123"
                ).setHref(
                    Url.parseAbsoluteOrRelative("#/1/SpreadsheetName234/cell/A1")
                ).setBadge("Badge456"),
            "\"Text123\" [#/1/SpreadsheetName234/cell/A1] (Badge456)"
        );
    }

    // TreePrintable....................................................................................................

    @Test
    public void testPrintTreeAll() {
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
                ).setBadge("Badge555")
                .setHref(
                    Url.parseAbsoluteOrRelative("#/1/SpreadsheetName234/cell/A1")
                ).setId("id789"),
            "IconBefore456 \"Text789\" [#/1/SpreadsheetName234/cell/A1] (Badge555) CHECKED IconAfter123 id=id789"
        );
    }

    @Test
    public void testPrintTreeAllDisabled() {
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
            "IconBefore456 \"Text789\" DISABLED CHECKED IconAfter123"
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
            HistoryContexts.fake()
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
        final TooltipComponent tooltip = TooltipComponent.attach(
            anchor,
            "Tooltip123",
            DropDirection.BOTTOM_MIDDLE
        );

        this.treePrintAndCheck(
            anchor,
            "\"Hello\" [#/1/SpreadsheetName234/cell/A1]\n" +
                "  TooltipComponent\n" +
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
