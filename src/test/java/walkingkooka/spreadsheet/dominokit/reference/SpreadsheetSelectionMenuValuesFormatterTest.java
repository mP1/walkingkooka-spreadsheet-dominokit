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
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuFactory;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellFormatterSelectHistoryToken;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterMenu;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportAnchor;

import java.util.List;
import java.util.Optional;

public final class SpreadsheetSelectionMenuValuesFormatterTest extends SpreadsheetSelectionMenuValuesTestCase<SpreadsheetSelectionMenuValuesFormatter, SpreadsheetFormatterSelector> {

    @Test
    public void testBuildRecents() {
        final SpreadsheetCellFormatterSelectHistoryToken historyToken = HistoryToken.cellFormatterSelect(
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
                SpreadsheetFormatterSelector.parse("date recent-1A"),
                SpreadsheetFormatterSelector.parse("date recent-2B")
            ),
            Lists.empty(),
            Optional.empty() // selectionSummary
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

        SpreadsheetSelectionMenuValuesFormatter.with(
            historyToken,
            menu,
            context
        ).build();

        this.treePrintAndCheck(
            menu,
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"Formatter\" id=test-Formatter-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/Spreadsheet123/cell/A1/formatter/save/] id=test-Formatter-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/Spreadsheet123/cell/A1/formatter] id=test-Formatter-edit-MenuItem\n" +
                "    -----\n" +
                "    \"Date (date recent-1A)\" [/1/Spreadsheet123/cell/A1/formatter/save/date%20recent-1A] id=test-Formatter-recent-0-MenuItem\n" +
                "    \"Date (date recent-2B)\" [/1/Spreadsheet123/cell/A1/formatter/save/date%20recent-2B] id=test-Formatter-recent-1-MenuItem\n"
        );
    }

    @Test
    public void testBuildRecentsChecked() {
        final SpreadsheetCellFormatterSelectHistoryToken historyToken = HistoryToken.cellFormatterSelect(
            SpreadsheetId.with(1),
            SpreadsheetName.with("Spreadsheet123"),
            AnchoredSpreadsheetSelection.with(
                SpreadsheetSelection.A1,
                SpreadsheetViewportAnchor.NONE
            )
        );

        final SpreadsheetFormatterSelector matchedSelector = SpreadsheetFormatterSelector.parse("date recent-1A");

        final SpreadsheetSelectionMenuContext context = this.context(
            historyToken,
            Lists.of(
                matchedSelector,
                SpreadsheetFormatterSelector.parse("date recent-2B")
            ), // recent
            Lists.empty(), // menu
            Optional.of(
                SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                    .setFormatter(
                        Optional.of(matchedSelector)
                    )
            ) // selectionSummary
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

        SpreadsheetSelectionMenuValuesFormatter.with(
            historyToken,
            menu,
            context
        ).build();

        this.treePrintAndCheck(
            menu,
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"Formatter\" id=test-Formatter-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/Spreadsheet123/cell/A1/formatter/save/] id=test-Formatter-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/Spreadsheet123/cell/A1/formatter] id=test-Formatter-edit-MenuItem\n" +
                "    -----\n" +
                "    \"Date (date recent-1A)\" [/1/Spreadsheet123/cell/A1/formatter/save/date%20recent-1A] CHECKED id=test-Formatter-recent-0-MenuItem\n" +
                "    \"Date (date recent-2B)\" [/1/Spreadsheet123/cell/A1/formatter/save/date%20recent-2B] id=test-Formatter-recent-1-MenuItem\n"
        );
    }

    @Test
    public void testBuildMenus() {
        final SpreadsheetCellFormatterSelectHistoryToken historyToken = HistoryToken.cellFormatterSelect(
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
                SpreadsheetFormatterMenu.with(
                    "Short",
                    SpreadsheetPattern.parseDateFormatPattern("yy/mm/dd")
                        .spreadsheetFormatterSelector()
                ),
                SpreadsheetFormatterMenu.with(
                    "Medium",
                    SpreadsheetPattern.parseDateFormatPattern("yyyy/mm/ddd")
                        .spreadsheetFormatterSelector()
                ),
                SpreadsheetFormatterMenu.with(
                    "Default text",
                    SpreadsheetFormatterSelector.DEFAULT_TEXT_FORMAT
                )
            ),
            Optional.empty() // selectionSummary
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

        SpreadsheetSelectionMenuValuesFormatter.with(
            historyToken,
            menu,
            context
        ).build();

        this.treePrintAndCheck(
            menu,
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"Formatter\" id=test-Formatter-SubMenu\n" +
                "    \"Date\" id=test-Formatter-date-SubMenu\n" +
                "      \"Short\" [/1/Spreadsheet123/cell/A1/formatter/save/date%20yy/mm/dd] id=test-Formatter-date-MenuItem\n" +
                "      \"Medium\" [/1/Spreadsheet123/cell/A1/formatter/save/date%20yyyy/mm/ddd] id=test-Formatter-date-MenuItem\n" +
                "    \"Text\" id=test-Formatter-text-SubMenu\n" +
                "      \"Default text\" [/1/Spreadsheet123/cell/A1/formatter/save/text%20@] id=test-Formatter-text-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/Spreadsheet123/cell/A1/formatter/save/] id=test-Formatter-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/Spreadsheet123/cell/A1/formatter] id=test-Formatter-edit-MenuItem\n"
        );
    }

    @Test
    public void testBuildMenusChecked() {
        final SpreadsheetCellFormatterSelectHistoryToken historyToken = HistoryToken.cellFormatterSelect(
            SpreadsheetId.with(1),
            SpreadsheetName.with("Spreadsheet123"),
            AnchoredSpreadsheetSelection.with(
                SpreadsheetSelection.A1,
                SpreadsheetViewportAnchor.NONE
            )
        );

        final SpreadsheetFormatterSelector selected = SpreadsheetPattern.parseDateFormatPattern("yy/mm/dd")
            .spreadsheetFormatterSelector();

        final SpreadsheetSelectionMenuContext context = this.context(
            historyToken,
            Lists.empty(),
            Lists.of(
                SpreadsheetFormatterMenu.with(
                    "Short",
                    selected
                ),
                SpreadsheetFormatterMenu.with(
                    "Medium",
                    SpreadsheetPattern.parseDateFormatPattern("yyyy/mm/ddd")
                        .spreadsheetFormatterSelector()
                ),
                SpreadsheetFormatterMenu.with(
                    "Default text",
                    SpreadsheetFormatterSelector.DEFAULT_TEXT_FORMAT
                )
            ),
            Optional.of(
                SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                    .setFormatter(
                        Optional.of(selected)
                    )
            ) // selectionSummary
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

        SpreadsheetSelectionMenuValuesFormatter.with(
            historyToken,
            menu,
            context
        ).build();

        this.treePrintAndCheck(
            menu,
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"Formatter\" id=test-Formatter-SubMenu\n" +
                "    \"Date\" id=test-Formatter-date-SubMenu\n" +
                "      \"Short\" [/1/Spreadsheet123/cell/A1/formatter/save/date%20yy/mm/dd] CHECKED id=test-Formatter-date-MenuItem\n" +
                "      \"Medium\" [/1/Spreadsheet123/cell/A1/formatter/save/date%20yyyy/mm/ddd] id=test-Formatter-date-MenuItem\n" +
                "    \"Text\" id=test-Formatter-text-SubMenu\n" +
                "      \"Default text\" [/1/Spreadsheet123/cell/A1/formatter/save/text%20@] id=test-Formatter-text-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/Spreadsheet123/cell/A1/formatter/save/] id=test-Formatter-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/Spreadsheet123/cell/A1/formatter] id=test-Formatter-edit-MenuItem\n"
        );
    }

    @Test
    public void testBuildRecentsAndMenus() {
        final SpreadsheetCellFormatterSelectHistoryToken historyToken = HistoryToken.cellFormatterSelect(
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
                SpreadsheetFormatterSelector.parse("date recent-1A"),
                SpreadsheetFormatterSelector.parse("date recent-2B")
            ),
            Lists.of(
                SpreadsheetFormatterMenu.with(
                    "Short",
                    SpreadsheetPattern.parseDateFormatPattern("yy/mm/dd")
                        .spreadsheetFormatterSelector()
                ),
                SpreadsheetFormatterMenu.with(
                    "Medium",
                    SpreadsheetPattern.parseDateFormatPattern("yyyy/mm/ddd")
                        .spreadsheetFormatterSelector()
                ),
                SpreadsheetFormatterMenu.with(
                    "Default text",
                    SpreadsheetFormatterSelector.DEFAULT_TEXT_FORMAT
                ),
                SpreadsheetFormatterMenu.with(
                    "Short",
                    SpreadsheetPattern.parseDateTimeFormatPattern("yy/mm/dd")
                        .spreadsheetFormatterSelector()
                ),
                SpreadsheetFormatterMenu.with(
                    "Medium",
                    SpreadsheetPattern.parseDateTimeFormatPattern("yyyy/mm/ddd")
                        .spreadsheetFormatterSelector()
                ),
                SpreadsheetFormatterMenu.with(
                    "Long",
                    SpreadsheetPattern.parseDateTimeFormatPattern("yyyy/mmm/dddd")
                        .spreadsheetFormatterSelector()
                )
            ),
            Optional.empty() // selectionSummary
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

        SpreadsheetSelectionMenuValuesFormatter.with(
            historyToken,
            menu,
            context
        ).build();

        this.treePrintAndCheck(
            menu,
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"Formatter\" id=test-Formatter-SubMenu\n" +
                "    \"Date\" id=test-Formatter-date-SubMenu\n" +
                "      \"Short\" [/1/Spreadsheet123/cell/A1/formatter/save/date%20yy/mm/dd] id=test-Formatter-date-MenuItem\n" +
                "      \"Medium\" [/1/Spreadsheet123/cell/A1/formatter/save/date%20yyyy/mm/ddd] id=test-Formatter-date-MenuItem\n" +
                "    \"Date Time\" id=test-Formatter-date-time-SubMenu\n" +
                "      \"Short\" [/1/Spreadsheet123/cell/A1/formatter/save/date-time%20yy/mm/dd] id=test-Formatter-date-time-MenuItem\n" +
                "      \"Medium\" [/1/Spreadsheet123/cell/A1/formatter/save/date-time%20yyyy/mm/ddd] id=test-Formatter-date-time-MenuItem\n" +
                "      \"Long\" [/1/Spreadsheet123/cell/A1/formatter/save/date-time%20yyyy/mmm/dddd] id=test-Formatter-date-time-MenuItem\n" +
                "    \"Text\" id=test-Formatter-text-SubMenu\n" +
                "      \"Default text\" [/1/Spreadsheet123/cell/A1/formatter/save/text%20@] id=test-Formatter-text-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/Spreadsheet123/cell/A1/formatter/save/] id=test-Formatter-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/Spreadsheet123/cell/A1/formatter] id=test-Formatter-edit-MenuItem\n" +
                "    -----\n" +
                "    \"Date (date recent-1A)\" [/1/Spreadsheet123/cell/A1/formatter/save/date%20recent-1A] id=test-Formatter-recent-0-MenuItem\n" +
                "    \"Date (date recent-2B)\" [/1/Spreadsheet123/cell/A1/formatter/save/date%20recent-2B] id=test-Formatter-recent-1-MenuItem\n"
        );
    }

    private SpreadsheetSelectionMenuContext context(final SpreadsheetCellFormatterSelectHistoryToken historyToken,
                                                    final List<SpreadsheetFormatterSelector> recentFormatters,
                                                    final List<SpreadsheetFormatterMenu> menus,
                                                    final Optional<SpreadsheetCell> selectionSummary) {
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
            public List<SpreadsheetFormatterMenu> spreadsheetFormatterMenus() {
                return menus;
            }

            @Override
            public Optional<SpreadsheetCell> selectionSummary() {
                return selectionSummary;
            }
        };
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetSelectionMenuValuesFormatter> type() {
        return SpreadsheetSelectionMenuValuesFormatter.class;
    }
}
