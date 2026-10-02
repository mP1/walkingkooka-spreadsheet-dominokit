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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.validation.ValueType;

import java.util.Locale;
import java.util.Optional;

public final class SpreadsheetSelectionMenuValuesValueTypeTest extends SpreadsheetSelectionMenuValuesTestCase<SpreadsheetSelectionMenuValuesValueType, ValueType> {

    @Test
    public void testBuild() {
        this.buildAndCheck(
            HistoryToken.cellValueTypeSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL
            ),
            Optional.empty(), // summary
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"Value Type\" id=test-ValueType-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName111/cell/A1/valueType/save/Boolean] id=test-ValueTypes-Boolean-MenuItem\n" +
                "    \"Currency\" [/1/SpreadsheetName111/cell/A1/valueType/save/Currency] id=test-ValueTypes-currency/Currency-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName111/cell/A1/valueType/save/Date] id=test-ValueTypes-date-time/Date-MenuItem\n" +
                "    \"Datetime\" [/1/SpreadsheetName111/cell/A1/valueType/save/DateTime] id=test-ValueTypes-date-time/DateTime-MenuItem\n" +
                "    \"Email\" [/1/SpreadsheetName111/cell/A1/valueType/save/Email] id=test-ValueTypes-email/Email-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName111/cell/A1/valueType/save/Number] id=test-ValueTypes-number/Number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName111/cell/A1/valueType/save/Text] id=test-ValueTypes-text/Text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName111/cell/A1/valueType/save/Time] id=test-ValueTypes-date-time/Time-MenuItem\n" +
                "    \"Url\" [/1/SpreadsheetName111/cell/A1/valueType/save/url] id=test-ValueTypes-url-MenuItem\n" +
                "    \"Wholenumber\" [/1/SpreadsheetName111/cell/A1/valueType/save/wholeNumber] id=test-ValueTypes-number/wholeNumber-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName111/cell/A1/valueType/save/] id=test-ValueType-clear-MenuItem\n"
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
                "  \"Value Type\" id=test-ValueType-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName111/cell/A1/valueType/save/Boolean] id=test-ValueTypes-Boolean-MenuItem\n" +
                "    \"Currency\" [/1/SpreadsheetName111/cell/A1/valueType/save/Currency] id=test-ValueTypes-currency/Currency-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName111/cell/A1/valueType/save/Date] id=test-ValueTypes-date-time/Date-MenuItem\n" +
                "    \"Datetime\" [/1/SpreadsheetName111/cell/A1/valueType/save/DateTime] id=test-ValueTypes-date-time/DateTime-MenuItem\n" +
                "    \"Email\" [/1/SpreadsheetName111/cell/A1/valueType/save/Email] id=test-ValueTypes-email/Email-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName111/cell/A1/valueType/save/Number] id=test-ValueTypes-number/Number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName111/cell/A1/valueType/save/Text] id=test-ValueTypes-text/Text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName111/cell/A1/valueType/save/Time] id=test-ValueTypes-date-time/Time-MenuItem\n" +
                "    \"Url\" [/1/SpreadsheetName111/cell/A1/valueType/save/url] id=test-ValueTypes-url-MenuItem\n" +
                "    \"Wholenumber\" [/1/SpreadsheetName111/cell/A1/valueType/save/wholeNumber] id=test-ValueTypes-number/wholeNumber-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName111/cell/A1/formula/save/] id=test-ValueType-clear-MenuItem\n"
        );
    }

    @Test
    public void testBuildWithChecked() {
        this.buildAndCheck(
            HistoryToken.cellValueTypeSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL
            ),
            Optional.of(
                SpreadsheetSelection.A1.setFormula(
                    SpreadsheetFormula.EMPTY.setValueType(
                        Optional.of(ValueType.TEXT)
                    )
                )
            ),
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"Value Type\" id=test-ValueType-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName111/cell/A1/valueType/save/Boolean] id=test-ValueTypes-Boolean-MenuItem\n" +
                "    \"Currency\" [/1/SpreadsheetName111/cell/A1/valueType/save/Currency] id=test-ValueTypes-currency/Currency-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName111/cell/A1/valueType/save/Date] id=test-ValueTypes-date-time/Date-MenuItem\n" +
                "    \"Datetime\" [/1/SpreadsheetName111/cell/A1/valueType/save/DateTime] id=test-ValueTypes-date-time/DateTime-MenuItem\n" +
                "    \"Email\" [/1/SpreadsheetName111/cell/A1/valueType/save/Email] id=test-ValueTypes-email/Email-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName111/cell/A1/valueType/save/Number] id=test-ValueTypes-number/Number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName111/cell/A1/valueType/save/Text] CHECKED id=test-ValueTypes-text/Text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName111/cell/A1/valueType/save/Time] id=test-ValueTypes-date-time/Time-MenuItem\n" +
                "    \"Url\" [/1/SpreadsheetName111/cell/A1/valueType/save/url] id=test-ValueTypes-url-MenuItem\n" +
                "    \"Wholenumber\" [/1/SpreadsheetName111/cell/A1/valueType/save/wholeNumber] id=test-ValueTypes-number/wholeNumber-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName111/cell/A1/valueType/save/] id=test-ValueType-clear-MenuItem\n"
        );
    }

    @Test
    public void testBuildWithRecents() {
        this.buildAndCheck(
            HistoryToken.cellValueTypeSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL
            ),
            Optional.empty(), // summary
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"Value Type\" id=test-ValueType-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName111/cell/A1/valueType/save/Boolean] id=test-ValueTypes-Boolean-MenuItem\n" +
                "    \"Currency\" [/1/SpreadsheetName111/cell/A1/valueType/save/Currency] id=test-ValueTypes-currency/Currency-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName111/cell/A1/valueType/save/Date] id=test-ValueTypes-date-time/Date-MenuItem\n" +
                "    \"Datetime\" [/1/SpreadsheetName111/cell/A1/valueType/save/DateTime] id=test-ValueTypes-date-time/DateTime-MenuItem\n" +
                "    \"Email\" [/1/SpreadsheetName111/cell/A1/valueType/save/Email] id=test-ValueTypes-email/Email-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName111/cell/A1/valueType/save/Number] id=test-ValueTypes-number/Number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName111/cell/A1/valueType/save/Text] id=test-ValueTypes-text/Text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName111/cell/A1/valueType/save/Time] id=test-ValueTypes-date-time/Time-MenuItem\n" +
                "    \"Url\" [/1/SpreadsheetName111/cell/A1/valueType/save/url] id=test-ValueTypes-url-MenuItem\n" +
                "    \"Wholenumber\" [/1/SpreadsheetName111/cell/A1/valueType/save/wholeNumber] id=test-ValueTypes-number/wholeNumber-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName111/cell/A1/valueType/save/] id=test-ValueType-clear-MenuItem\n"
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

        SpreadsheetSelectionMenuValuesValueType.with(
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

            @Override
            public SpreadsheetMetadata spreadsheetMetadata() {
                return SpreadsheetMetadata.EMPTY.set(
                    SpreadsheetMetadataPropertyName.LOCALE,
                    LOCALE
                ).loadFromLocale(CURRENCY_LOCALE_CONTEXT);
            }
        };
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetSelectionMenuValuesValueType> type() {
        return SpreadsheetSelectionMenuValuesValueType.class;
    }
}
