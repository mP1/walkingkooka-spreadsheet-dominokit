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

package walkingkooka.spreadsheet.dominokit.viewport.menu;

import org.dominokit.domino.ui.menu.Menu;
import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuFactory;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.validation.ValueType;

import java.util.Locale;
import java.util.Optional;

public final class SpreadsheetSelectionMenuValuesValueTest extends SpreadsheetSelectionMenuValuesTestCase<SpreadsheetSelectionMenuValuesValue, Object> {

    @Test
    public void testBuild() {
        this.buildAndCheck(
            HistoryToken.cellValueSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL,
                ValueType.TEXT
            ),
            Optional.empty(), // summary
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"Value\" id=test-Value-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName111/cell/A1/value/Boolean] id=test-Value-Boolean-MenuItem\n" +
                "    \"Currency\" [/1/SpreadsheetName111/cell/A1/value/Currency] id=test-Value-currency/Currency-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName111/cell/A1/value/Date] id=test-Value-date-time/Date-MenuItem\n" +
                "    \"Datetime\" [/1/SpreadsheetName111/cell/A1/value/DateTime] id=test-Value-date-time/DateTime-MenuItem\n" +
                "    \"Emailaddress\" [/1/SpreadsheetName111/cell/A1/value/EmailAddress] id=test-Value-email/EmailAddress-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName111/cell/A1/value/Number] id=test-Value-number/Number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName111/cell/A1/value/Text] id=test-Value-text/Text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName111/cell/A1/value/Time] id=test-Value-date-time/Time-MenuItem\n" +
                "    \"Url\" [/1/SpreadsheetName111/cell/A1/value/url] id=test-Value-url-MenuItem\n" +
                "    \"Wholenumber\" [/1/SpreadsheetName111/cell/A1/value/wholeNumber] id=test-Value-number/wholeNumber-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName111/cell/A1/value/Text/save/] id=test-Value-clear-MenuItem\n"
        );
    }

    @Test
    public void testBuildWithSpreadsheetCellFormulaHistoryToken() {
        this.buildAndCheck(
            HistoryToken.cellFormula(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL
            ),
            Optional.empty(), // summary
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"Value\" id=test-Value-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName111/cell/A1/value/Boolean] id=test-Value-Boolean-MenuItem\n" +
                "    \"Currency\" [/1/SpreadsheetName111/cell/A1/value/Currency] id=test-Value-currency/Currency-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName111/cell/A1/value/Date] id=test-Value-date-time/Date-MenuItem\n" +
                "    \"Datetime\" [/1/SpreadsheetName111/cell/A1/value/DateTime] id=test-Value-date-time/DateTime-MenuItem\n" +
                "    \"Emailaddress\" [/1/SpreadsheetName111/cell/A1/value/EmailAddress] id=test-Value-email/EmailAddress-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName111/cell/A1/value/Number] id=test-Value-number/Number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName111/cell/A1/value/Text] id=test-Value-text/Text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName111/cell/A1/value/Time] id=test-Value-date-time/Time-MenuItem\n" +
                "    \"Url\" [/1/SpreadsheetName111/cell/A1/value/url] id=test-Value-url-MenuItem\n" +
                "    \"Wholenumber\" [/1/SpreadsheetName111/cell/A1/value/wholeNumber] id=test-Value-number/wholeNumber-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName111/cell/A1/formula/save/] id=test-Value-clear-MenuItem\n"
        );
    }

    @Test
    public void testBuildWithChecked() {
        this.buildAndCheck(
            HistoryToken.cellValueSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL,
                ValueType.TEXT
            ),
            Optional.of(
                SpreadsheetSelection.A1.setFormula(
                    SpreadsheetFormula.EMPTY.setValue(
                        Optional.of("Hello")
                    )
                )
            ),
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"Value\" id=test-Value-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName111/cell/A1/value/Boolean] id=test-Value-Boolean-MenuItem\n" +
                "    \"Currency\" [/1/SpreadsheetName111/cell/A1/value/Currency] id=test-Value-currency/Currency-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName111/cell/A1/value/Date] id=test-Value-date-time/Date-MenuItem\n" +
                "    \"Datetime\" [/1/SpreadsheetName111/cell/A1/value/DateTime] id=test-Value-date-time/DateTime-MenuItem\n" +
                "    \"Emailaddress\" [/1/SpreadsheetName111/cell/A1/value/EmailAddress] id=test-Value-email/EmailAddress-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName111/cell/A1/value/Number] id=test-Value-number/Number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName111/cell/A1/value/Text] CHECKED id=test-Value-text/Text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName111/cell/A1/value/Time] id=test-Value-date-time/Time-MenuItem\n" +
                "    \"Url\" [/1/SpreadsheetName111/cell/A1/value/url] id=test-Value-url-MenuItem\n" +
                "    \"Wholenumber\" [/1/SpreadsheetName111/cell/A1/value/wholeNumber] id=test-Value-number/wholeNumber-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName111/cell/A1/value/Text/save/] id=test-Value-clear-MenuItem\n"
        );
    }

    private void buildAndCheck(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                               final Optional<SpreadsheetCell> summary,
                               final String expected) {
        final SpreadsheetSelectionMenuContext context = this.context(
            historyToken,
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

        SpreadsheetSelectionMenuValuesValue.with(
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
            public Optional<String> localeText(final Locale locale) {
                return Optional.of(
                    locale.getDisplayName()
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
    public Class<SpreadsheetSelectionMenuValuesValue> type() {
        return SpreadsheetSelectionMenuValuesValue.class;
    }
}
