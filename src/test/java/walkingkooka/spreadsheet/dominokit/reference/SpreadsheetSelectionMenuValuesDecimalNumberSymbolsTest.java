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
import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuFactory;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellDecimalNumberSymbolsSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellHistoryToken;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.SpreadsheetCell;

import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public final class SpreadsheetSelectionMenuValuesDecimalNumberSymbolsTest extends SpreadsheetSelectionMenuValuesTestCase<SpreadsheetSelectionMenuValuesDecimalNumberSymbols, DecimalNumberSymbols> {

    private final static DecimalNumberSymbols DECIMAL_NUMBER_SYMBOLS_AU = DecimalNumberSymbols.fromDecimalFormatSymbols(
        '+',
        new DecimalFormatSymbols(
            Locale.forLanguageTag("en-AU")
        )
    );

    private final static DecimalNumberSymbols DECIMAL_NUMBER_SYMBOLS_NZ = DecimalNumberSymbols.fromDecimalFormatSymbols(
        '+',
        new DecimalFormatSymbols(
            Locale.forLanguageTag("en-NZ")
        )
    );

    @Test
    public void testBuild() {
        this.buildAndCheck(
            HistoryToken.cellDecimalNumberSymbolsSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            Lists.empty(), // recentDecimalNumberSymbols
            Optional.empty(), // summary
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"DecimalNumberSymbols\" id=test-DecimalNumberSymbols-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName111/cell/A1/decimalNumberSymbols/save/] id=test-DecimalNumberSymbols-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName111/cell/A1/decimalNumberSymbols] id=test-DecimalNumberSymbols-edit-MenuItem\n"
        );
    }

    @Test
    public void testBuildWithSpreadsheetCellFormulaHistoryToken() {
        this.buildAndCheck(
            HistoryToken.cellFormula(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    SpreadsheetSelection.A1.setDefaultAnchor()
                ).decimalNumberSymbols()
                .cast(SpreadsheetCellDecimalNumberSymbolsSelectHistoryToken.class),
            Lists.empty(), // recentDecimalNumberSymbols
            Optional.empty(), // summary
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"DecimalNumberSymbols\" id=test-DecimalNumberSymbols-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName111/cell/A1/decimalNumberSymbols/save/] id=test-DecimalNumberSymbols-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName111/cell/A1/decimalNumberSymbols] id=test-DecimalNumberSymbols-edit-MenuItem\n"
        );
    }

    @Test
    public void testBuildWithChecked() {
        this.buildAndCheck(
            HistoryToken.cellDecimalNumberSymbolsSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            Lists.empty(), // recentDecimalNumberSymbols
            Optional.of(
                SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                    .setDecimalNumberSymbols(
                        Optional.of(DECIMAL_NUMBER_SYMBOLS_AU)
                    )
            ),
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"DecimalNumberSymbols\" id=test-DecimalNumberSymbols-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName111/cell/A1/decimalNumberSymbols/save/] id=test-DecimalNumberSymbols-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName111/cell/A1/decimalNumberSymbols] id=test-DecimalNumberSymbols-edit-MenuItem\n"
        );
    }

    @Test
    public void testBuildWithRecentDecimalNumberSymbols() {
        this.buildAndCheck(
            HistoryToken.cellDecimalNumberSymbolsSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            Lists.of(
                DECIMAL_NUMBER_SYMBOLS_AU,
                DECIMAL_NUMBER_SYMBOLS_NZ
            ), // recentDecimalNumberSymbols
            Optional.empty(),
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"DecimalNumberSymbols\" id=test-DecimalNumberSymbols-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName111/cell/A1/decimalNumberSymbols/save/] id=test-DecimalNumberSymbols-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName111/cell/A1/decimalNumberSymbols] id=test-DecimalNumberSymbols-edit-MenuItem\n" +
                "    -----\n" +
                "    \"-,+,0,$,.,e,\\\",\\\",∞,.,NaN,%,‰\" [/1/SpreadsheetName111/cell/A1/decimalNumberSymbols/save/-,+,0,$,.,e,%22,%22,%E2%88%9E,.,NaN,%25,%E2%80%B0] id=test-DecimalNumberSymbols-recent-0-MenuItem\n" +
                "    \"-,+,0,$,.,E,\\\",\\\",∞,.,NaN,%,‰\" [/1/SpreadsheetName111/cell/A1/decimalNumberSymbols/save/-,+,0,$,.,E,%22,%22,%E2%88%9E,.,NaN,%25,%E2%80%B0] id=test-DecimalNumberSymbols-recent-1-MenuItem\n"
        );
    }

    private void buildAndCheck(final SpreadsheetCellHistoryToken historyToken,
                               final List<DecimalNumberSymbols> recentDecimalNumberSymbols,
                               final Optional<SpreadsheetCell> summary,
                               final String expected) {
        final SpreadsheetSelectionMenuContext context = this.context(
            historyToken,
            recentDecimalNumberSymbols,
            summary
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

        SpreadsheetSelectionMenuValuesDecimalNumberSymbols.with(
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
                                                    final List<DecimalNumberSymbols> recentDecimalNumberSymbols,
                                                    final Optional<SpreadsheetCell> summary) {
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
            public List<DecimalNumberSymbols> recentDecimalNumberSymbols() {
                return recentDecimalNumberSymbols;
            }

            @Override
            public Optional<SpreadsheetCell> selectionSummary() {
                return summary;
            }
        };
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetSelectionMenuValuesDecimalNumberSymbols> type() {
        return SpreadsheetSelectionMenuValuesDecimalNumberSymbols.class;
    }
}
