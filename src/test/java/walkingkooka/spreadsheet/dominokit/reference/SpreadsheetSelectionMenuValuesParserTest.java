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
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellParserSelectHistoryToken;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.SpreadsheetCell;

import java.util.List;
import java.util.Optional;

public final class SpreadsheetSelectionMenuValuesParserTest extends SpreadsheetSelectionMenuValuesTestCase<SpreadsheetSelectionMenuValuesParser, SpreadsheetParserSelector> {

    private final static SpreadsheetParserSelector PARSER1 = SpreadsheetParserSelector.parse("test-parser-111 111");
    private final static SpreadsheetParserSelector PARSER2 = SpreadsheetParserSelector.parse("test-parser-222");

    @Test
    public void testBuild() {
        this.buildAndCheck(
            HistoryToken.cellParserSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL
            ),
            Optional.empty(), // summary
            Lists.empty(),
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"Parser\" id=test-Parser-SubMenu\n" +
                "    \"Test Parser 111 (test-parser-111 111)\" [/1/SpreadsheetName111/cell/A1/parser/save/test-parser-111%20111] id=test-Parser-test-parser-111-MenuItem\n" +
                "    \"Test Parser 222\" [/1/SpreadsheetName111/cell/A1/parser/save/test-parser-222] id=test-Parser-test-parser-222-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear...\" [/1/SpreadsheetName111/cell/A1/parser/save/] id=test-Parser-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName111/cell/A1/parser] id=test-Parser-edit-MenuItem\n"
        );
    }

    @Test
    public void testBuildWithChecked() {
        this.buildAndCheck(
            HistoryToken.cellParserSelect(
                SpreadsheetId.with(1),
                SpreadsheetName.with("SpreadsheetName111"),
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            Optional.of(
                SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                    .setParser(
                        Optional.of(PARSER1)
                    )
            ),
            Lists.empty(), // recents
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"Parser\" id=test-Parser-SubMenu\n" +
                "    \"Test Parser 111 (test-parser-111 111)\" [/1/SpreadsheetName111/cell/A1/parser/save/test-parser-111%20111] CHECKED id=test-Parser-test-parser-111-MenuItem\n" +
                "    \"Test Parser 222\" [/1/SpreadsheetName111/cell/A1/parser/save/test-parser-222] id=test-Parser-test-parser-222-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear...\" [/1/SpreadsheetName111/cell/A1/parser/save/] id=test-Parser-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName111/cell/A1/parser] id=test-Parser-edit-MenuItem\n"
        );
    }

    @Test
    public void testBuildWithRecents() {
        this.buildAndCheck(
            HistoryToken.cellParserSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL
            ),
            Optional.empty(), // summary
            Lists.of(
                PARSER1
            ), // recent,
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"Parser\" id=test-Parser-SubMenu\n" +
                "    \"Test Parser 111 (test-parser-111 111)\" [/1/SpreadsheetName111/cell/A1/parser/save/test-parser-111%20111] id=test-Parser-test-parser-111-MenuItem\n" +
                "    \"Test Parser 222\" [/1/SpreadsheetName111/cell/A1/parser/save/test-parser-222] id=test-Parser-test-parser-222-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear...\" [/1/SpreadsheetName111/cell/A1/parser/save/] id=test-Parser-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName111/cell/A1/parser] id=test-Parser-edit-MenuItem\n" +
                "    -----\n" +
                "    \"Test Parser 111 (test-parser-111 111)\" [/1/SpreadsheetName111/cell/A1/parser/save/test-parser-111%20111] id=test-Parser-recent-0-MenuItem\n"
        );
    }

    @Test
    public void testBuildWithCheckedRecents() {
        this.buildAndCheck(
            HistoryToken.cellParserSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL
            ),
            Optional.of(
                SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                    .setParser(
                        Optional.of(PARSER1)
                    )
            ), // summary
            Lists.of(
                PARSER1,
                PARSER2
            ), // recent,
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"Parser\" id=test-Parser-SubMenu\n" +
                "    \"Test Parser 111 (test-parser-111 111)\" [/1/SpreadsheetName111/cell/A1/parser/save/test-parser-111%20111] CHECKED id=test-Parser-test-parser-111-MenuItem\n" +
                "    \"Test Parser 222\" [/1/SpreadsheetName111/cell/A1/parser/save/test-parser-222] id=test-Parser-test-parser-222-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear...\" [/1/SpreadsheetName111/cell/A1/parser/save/] id=test-Parser-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName111/cell/A1/parser] id=test-Parser-edit-MenuItem\n" +
                "    -----\n" +
                "    \"Test Parser 111 (test-parser-111 111)\" [/1/SpreadsheetName111/cell/A1/parser/save/test-parser-111%20111] CHECKED id=test-Parser-recent-0-MenuItem\n" +
                "    \"Test Parser 222\" [/1/SpreadsheetName111/cell/A1/parser/save/test-parser-222] id=test-Parser-recent-1-MenuItem\n"
        );
    }

    private void buildAndCheck(final SpreadsheetCellParserSelectHistoryToken historyToken,
                               final Optional<SpreadsheetCell> summary,
                               final List<SpreadsheetParserSelector> recents,
                               final String expected) {
        final SpreadsheetSelectionMenuContext context = this.context(
            historyToken,
            summary,
            recents
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

        SpreadsheetSelectionMenuValuesParser.with(
            historyToken,
            menu,
            context
        ).build();

        this.treePrintAndCheck(
            menu,
            expected
        );
    }

    private SpreadsheetSelectionMenuContext context(final HistoryToken historyToken,
                                                    final Optional<SpreadsheetCell> summary,
                                                    final List<SpreadsheetParserSelector> recentParsers) {
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
            public List<SpreadsheetParserSelector> recentSpreadsheetParserSelectors() {
                return recentParsers;
            }

            public List<SpreadsheetParserSelector> spreadsheetParserSelectors() {
                return Lists.of(
                    PARSER1,
                    PARSER2
                );
            }

            @Override
            public Optional<SpreadsheetCell> selectionSummary() {
                return summary;
            }
        };
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetSelectionMenuValuesParser> type() {
        return SpreadsheetSelectionMenuValuesParser.class;
    }
}
