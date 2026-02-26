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
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuFactory;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Currency;
import java.util.List;
import java.util.Optional;

public final class SpreadsheetSelectionMenuCurrencyTest implements TreePrintableTesting,
    SpreadsheetMetadataTesting,
    ClassTesting<SpreadsheetSelectionMenuCurrency> {

    @Test
    public void testBuild() {
        this.buildAndCheck(
            HistoryToken.selection(
                SpreadsheetId.with(1),
                SpreadsheetName.with("Spreadsheet123"),
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            Lists.empty(), // recentCurrencies
            Optional.empty(), // summary
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  (mdi-close) \"Clear...\" [/1/Spreadsheet123/cell/A1/currency/save/] id=test-currency-clear-MenuItem\n" +
                "  -----\n" +
                "  \"Edit...\" [/1/Spreadsheet123/cell/A1/currency] id=test-currency-edit-MenuItem\n"
        );
    }

    @Test
    public void testBuildWithSpreadsheetCellFormulaHistoryToken() {
        this.buildAndCheck(
            HistoryToken.cellFormula(
                SpreadsheetId.with(1),
                SpreadsheetName.with("Spreadsheet123"),
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            Lists.empty(), // recentCurrencies
            Optional.empty(), // summary
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  (mdi-close) \"Clear...\" [/1/Spreadsheet123/cell/A1/currency/save/] id=test-currency-clear-MenuItem\n" +
                "  -----\n" +
                "  \"Edit...\" [/1/Spreadsheet123/cell/A1/currency] id=test-currency-edit-MenuItem\n"
        );
    }

    @Test
    public void testBuildWithChecked() {
        this.buildAndCheck(
            HistoryToken.selection(
                SpreadsheetId.with(1),
                SpreadsheetName.with("Spreadsheet123"),
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            Lists.empty(), // recentCurrencies
            Optional.of(
                SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                    .setCurrency(
                        Optional.of(
                            Currency.getInstance("AUD")
                        )
                    )
            ),
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  (mdi-close) \"Clear...\" [/1/Spreadsheet123/cell/A1/currency/save/] id=test-currency-clear-MenuItem\n" +
                "  -----\n" +
                "  \"Edit...\" [/1/Spreadsheet123/cell/A1/currency] id=test-currency-edit-MenuItem\n"
        );
    }

    @Test
    public void testBuildWithRecentCurrencies() {
        this.buildAndCheck(
            HistoryToken.selection(
                SpreadsheetId.with(1),
                SpreadsheetName.with("Spreadsheet123"),
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            Lists.of(
                Currency.getInstance("AUD"),
                Currency.getInstance("NZD")
            ), // recentCurrencies
            Optional.empty(),
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  (mdi-close) \"Clear...\" [/1/Spreadsheet123/cell/A1/currency/save/] id=test-currency-clear-MenuItem\n" +
                "  -----\n" +
                "  \"Edit...\" [/1/Spreadsheet123/cell/A1/currency] id=test-currency-edit-MenuItem\n" +
                "  -----\n" +
                "  \"Australian Dollar\" [/1/Spreadsheet123/cell/A1/currency/save/AUD] id=test-currency-recent-0-MenuItem\n" +
                "  \"New Zealand Dollar\" [/1/Spreadsheet123/cell/A1/currency/save/NZD] id=test-currency-recent-1-MenuItem\n"
        );
    }

    private void buildAndCheck(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                               final List<Currency> recentCurrencies,
                               final Optional<SpreadsheetCell> summary,
                               final String expected) {
        final SpreadsheetSelectionMenuContext context = this.context(
            historyToken,
            recentCurrencies,
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

        SpreadsheetSelectionMenuCurrency.build(
            historyToken,
            menu,
            context
        );

        this.treePrintAndCheck(
            menu,
            expected
        );
    }

    private SpreadsheetSelectionMenuContext context(final HistoryToken historyToken,
                                                    final List<Currency> recentCurrencies,
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
            public Optional<String> currencyText(final Currency currency) {
                return Optional.of(
                    currency.getDisplayName()
                );
            }

            @Override
            public List<Currency> recentCurrencies() {
                return recentCurrencies;
            }

            @Override
            public Optional<SpreadsheetCell> selectionSummary() {
                return summary;
            }
        };
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetSelectionMenuCurrency> type() {
        return SpreadsheetSelectionMenuCurrency.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
