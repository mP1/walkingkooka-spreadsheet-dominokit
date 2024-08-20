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

package walkingkooka.spreadsheet.dominokit.reference;

import org.dominokit.domino.ui.menu.Menu;
import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportAnchor;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterSelectorMenu;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.List;
import java.util.Optional;

public final class SpreadsheetSelectionMenuFormatterTest implements TreePrintableTesting,
        SpreadsheetMetadataTesting,
        ClassTesting<SpreadsheetSelectionMenuFormatter> {

    @Test
    public void testBuildRecents() {
        final SpreadsheetAnchoredSelectionHistoryToken historyToken = HistoryToken.selection(
                SpreadsheetId.with(1),
                SpreadsheetName.with("Spreadsheet123"),
                AnchoredSpreadsheetSelection.with(
                        SpreadsheetSelection.A1,
                        SpreadsheetViewportAnchor.NONE
                )
        );

        final SpreadsheetSelectionMenuContext context = this.context(
                historyToken,
                Lists.of(
                        SpreadsheetFormatterSelector.parse("date-format-pattern recent-1A"),
                        SpreadsheetFormatterSelector.parse("date-format-pattern recent-2B")
                ),
                Lists.empty()
        );

        final SpreadsheetContextMenu menu = SpreadsheetContextMenuFactory.with(
                Menu.create(
                        "Cell-MenuId",
                        "Cell A1 Menu",
                        Optional.empty(), // no icon
                        Optional.empty() // no badge
                ),
                context
        );

        SpreadsheetSelectionMenuFormatter.build(
                historyToken,
                menu,
                context
        );

        this.treePrintAndCheck(
                menu,
                "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                        "  \"Edit...\" [/1/Spreadsheet123/cell/A1/formatter] id=test-formatter-edit-MenuItem\n" +
                        "  -----\n" +
                        "  \"Date Format Pattern recent-1A\" [/1/Spreadsheet123/cell/A1/formatter] id=test-formatter-recent-0-MenuItem\n" +
                        "  \"Date Format Pattern recent-2B\" [/1/Spreadsheet123/cell/A1/formatter] id=test-formatter-recent-1-MenuItem\n"
        );
    }

    @Test
    public void testBuildMenus() {
        final SpreadsheetAnchoredSelectionHistoryToken historyToken = HistoryToken.selection(
                SpreadsheetId.with(1),
                SpreadsheetName.with("Spreadsheet123"),
                AnchoredSpreadsheetSelection.with(
                        SpreadsheetSelection.A1,
                        SpreadsheetViewportAnchor.NONE
                )
        );

        final SpreadsheetSelectionMenuContext context = this.context(
                historyToken,
                Lists.empty(),
                Lists.of(
                        SpreadsheetFormatterSelectorMenu.with(
                                "Short",
                                SpreadsheetPattern.parseDateFormatPattern("yy/mm/dd")
                                        .spreadsheetFormatterSelector()
                        ),
                        SpreadsheetFormatterSelectorMenu.with(
                                "Medium",
                                SpreadsheetPattern.parseDateFormatPattern("yyyy/mm/ddd")
                                        .spreadsheetFormatterSelector()
                        ),
                        SpreadsheetFormatterSelectorMenu.with(
                                "Default text",
                                SpreadsheetFormatterSelector.DEFAULT_TEXT_FORMAT
                        )
                )
        );

        final SpreadsheetContextMenu menu = SpreadsheetContextMenuFactory.with(
                Menu.create(
                        "Cell-MenuId",
                        "Cell A1 Menu",
                        Optional.empty(), // no icon
                        Optional.empty() // no badge
                ),
                context
        );

        SpreadsheetSelectionMenuFormatter.build(
                historyToken,
                menu,
                context
        );

        this.treePrintAndCheck(
                menu,
                "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                        "  \"Date Format Pattern\" id=test-formatter-date-format-pattern-SubMenu\n" +
                        "    \"Short\" [/1/Spreadsheet123/cell/A1/formatter/save/date-format-pattern%20yy/mm/dd] id=test-formatter-date-format-pattern-MenuItem\n" +
                        "    \"Medium\" [/1/Spreadsheet123/cell/A1/formatter/save/date-format-pattern%20yyyy/mm/ddd] id=test-formatter-date-format-pattern-MenuItem\n" +
                        "  \"Text Format Pattern\" id=test-formatter-text-format-pattern-SubMenu\n" +
                        "    \"Default text\" [/1/Spreadsheet123/cell/A1/formatter/save/text-format-pattern%20@] id=test-formatter-text-format-pattern-MenuItem\n" +
                        "  -----\n" +
                        "  \"Edit...\" [/1/Spreadsheet123/cell/A1/formatter] id=test-formatter-edit-MenuItem\n"
        );
    }

    @Test
    public void testBuildRecentsAndMenus() {
        final SpreadsheetAnchoredSelectionHistoryToken historyToken = HistoryToken.selection(
                SpreadsheetId.with(1),
                SpreadsheetName.with("Spreadsheet123"),
                AnchoredSpreadsheetSelection.with(
                        SpreadsheetSelection.A1,
                        SpreadsheetViewportAnchor.NONE
                )
        );

        final SpreadsheetSelectionMenuContext context = this.context(
                historyToken,
                Lists.of(
                        SpreadsheetFormatterSelector.parse("date-format-pattern recent-1A"),
                        SpreadsheetFormatterSelector.parse("date-format-pattern recent-2B")
                ),
                Lists.of(
                        SpreadsheetFormatterSelectorMenu.with(
                                "Short",
                                SpreadsheetPattern.parseDateFormatPattern("yy/mm/dd")
                                        .spreadsheetFormatterSelector()
                        ),
                        SpreadsheetFormatterSelectorMenu.with(
                                "Medium",
                                SpreadsheetPattern.parseDateFormatPattern("yyyy/mm/ddd")
                                        .spreadsheetFormatterSelector()
                        ),
                        SpreadsheetFormatterSelectorMenu.with(
                                "Default text",
                                SpreadsheetFormatterSelector.DEFAULT_TEXT_FORMAT
                        ),
                        SpreadsheetFormatterSelectorMenu.with(
                                "Short",
                                SpreadsheetPattern.parseDateTimeFormatPattern("yy/mm/dd")
                                        .spreadsheetFormatterSelector()
                        ),
                        SpreadsheetFormatterSelectorMenu.with(
                                "Medium",
                                SpreadsheetPattern.parseDateTimeFormatPattern("yyyy/mm/ddd")
                                        .spreadsheetFormatterSelector()
                        ),
                        SpreadsheetFormatterSelectorMenu.with(
                                "Long",
                                SpreadsheetPattern.parseDateTimeFormatPattern("yyyy/mmm/dddd")
                                        .spreadsheetFormatterSelector()
                        )
                )
        );

        final SpreadsheetContextMenu menu = SpreadsheetContextMenuFactory.with(
                Menu.create(
                        "Cell-MenuId",
                        "Cell A1 Menu",
                        Optional.empty(), // no icon
                        Optional.empty() // no badge
                ),
                context
        );

        SpreadsheetSelectionMenuFormatter.build(
                historyToken,
                menu,
                context
        );

        this.treePrintAndCheck(
                menu,
                "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                        "  \"Date Format Pattern\" id=test-formatter-date-format-pattern-SubMenu\n" +
                        "    \"Short\" [/1/Spreadsheet123/cell/A1/formatter/save/date-format-pattern%20yy/mm/dd] id=test-formatter-date-format-pattern-MenuItem\n" +
                        "    \"Medium\" [/1/Spreadsheet123/cell/A1/formatter/save/date-format-pattern%20yyyy/mm/ddd] id=test-formatter-date-format-pattern-MenuItem\n" +
                        "  \"Date Time Format Pattern\" id=test-formatter-date-time-format-pattern-SubMenu\n" +
                        "    \"Short\" [/1/Spreadsheet123/cell/A1/formatter/save/date-time-format-pattern%20yy/mm/dd] id=test-formatter-date-time-format-pattern-MenuItem\n" +
                        "    \"Medium\" [/1/Spreadsheet123/cell/A1/formatter/save/date-time-format-pattern%20yyyy/mm/ddd] id=test-formatter-date-time-format-pattern-MenuItem\n" +
                        "    \"Long\" [/1/Spreadsheet123/cell/A1/formatter/save/date-time-format-pattern%20yyyy/mmm/dddd] id=test-formatter-date-time-format-pattern-MenuItem\n" +
                        "  \"Text Format Pattern\" id=test-formatter-text-format-pattern-SubMenu\n" +
                        "    \"Default text\" [/1/Spreadsheet123/cell/A1/formatter/save/text-format-pattern%20@] id=test-formatter-text-format-pattern-MenuItem\n" +
                        "  -----\n" +
                        "  \"Edit...\" [/1/Spreadsheet123/cell/A1/formatter] id=test-formatter-edit-MenuItem\n" +
                        "  -----\n" +
                        "  \"Date Format Pattern recent-1A\" [/1/Spreadsheet123/cell/A1/formatter] id=test-formatter-recent-0-MenuItem\n" +
                        "  \"Date Format Pattern recent-2B\" [/1/Spreadsheet123/cell/A1/formatter] id=test-formatter-recent-1-MenuItem\n"
        );
    }

    private SpreadsheetSelectionMenuContext context(final HistoryToken historyToken,
                                                    final List<SpreadsheetFormatterSelector> recentFormatters,
                                                    final List<SpreadsheetFormatterSelectorMenu> menus) {
        return new FakeSpreadsheetSelectionMenuContext() {

            @Override
            public HistoryToken historyToken() {
                return historyToken;
            }

            @Override
            public String idPrefix() {
                return "test-";
            }

            @Override
            public List<SpreadsheetFormatterSelector> recentSpreadsheetFormatterSelectors() {
                return recentFormatters;
            }

            @Override
            public List<SpreadsheetFormatterSelectorMenu> spreadsheetFormatterSelectorsMenus() {
                return menus;
            }
        };
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetSelectionMenuFormatter> type() {
        return SpreadsheetSelectionMenuFormatter.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
