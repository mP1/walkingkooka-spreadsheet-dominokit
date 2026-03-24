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
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuFactory;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellDateTimeSymbolsSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellHistoryToken;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.SpreadsheetCell;

import java.text.DateFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public final class SpreadsheetSelectionMenuValuesDateTimeSymbolsTest extends SpreadsheetSelectionMenuValuesTestCase<SpreadsheetSelectionMenuValuesDateTimeSymbols, DateTimeSymbols> {

    private final static DateTimeSymbols DATE_TIME_SYMBOLS_AU = DateTimeSymbols.fromDateFormatSymbols(
        new DateFormatSymbols(
            Locale.forLanguageTag("en-AU")
        )
    );

    private final static DateTimeSymbols DATE_TIME_SYMBOLS_NZ = DateTimeSymbols.fromDateFormatSymbols(
        new DateFormatSymbols(
            Locale.forLanguageTag("en-NZ")
        )
    );

    @Test
    public void testBuild() {
        this.buildAndCheck(
            HistoryToken.cellDateTimeSymbolsSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            Lists.empty(), // recentDateTimeSymbols
            Optional.empty(), // summary
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"DateTimeSymbols\" id=test-DateTimeSymbols-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName111/cell/A1/dateTimeSymbols/save/] id=test-DateTimeSymbols-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName111/cell/A1/dateTimeSymbols] id=test-DateTimeSymbols-edit-MenuItem\n"
        );
    }

    @Test
    public void testBuildWithSpreadsheetCellFormulaHistoryToken() {
        this.buildAndCheck(
            HistoryToken.cellFormula(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    SpreadsheetSelection.A1.setDefaultAnchor()
                ).dateTimeSymbols()
                .cast(SpreadsheetCellDateTimeSymbolsSelectHistoryToken.class),
            Lists.empty(), // recentDateTimeSymbols
            Optional.empty(), // summary
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"DateTimeSymbols\" id=test-DateTimeSymbols-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName111/cell/A1/dateTimeSymbols/save/] id=test-DateTimeSymbols-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName111/cell/A1/dateTimeSymbols] id=test-DateTimeSymbols-edit-MenuItem\n"
        );
    }

    @Test
    public void testBuildWithChecked() {
        this.buildAndCheck(
            HistoryToken.cellDateTimeSymbolsSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            Lists.empty(), // recentDateTimeSymbols
            Optional.of(
                SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                    .setDateTimeSymbols(
                        Optional.of(DATE_TIME_SYMBOLS_AU)
                    )
            ),
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"DateTimeSymbols\" id=test-DateTimeSymbols-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName111/cell/A1/dateTimeSymbols/save/] id=test-DateTimeSymbols-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName111/cell/A1/dateTimeSymbols] id=test-DateTimeSymbols-edit-MenuItem\n"
        );
    }

    @Test
    public void testBuildWithRecentDateTimeSymbols() {
        this.buildAndCheck(
            HistoryToken.cellDateTimeSymbolsSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            Lists.of(
                DATE_TIME_SYMBOLS_AU,
                DATE_TIME_SYMBOLS_NZ
            ), // recentDateTimeSymbols
            Optional.empty(),
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"DateTimeSymbols\" id=test-DateTimeSymbols-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName111/cell/A1/dateTimeSymbols/save/] id=test-DateTimeSymbols-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName111/cell/A1/dateTimeSymbols] id=test-DateTimeSymbols-edit-MenuItem\n" +
                "    -----\n" +
                "    \"am,pm\\\",\\\"January,February,March,April,May,June,July,August,September,October,November,December\\\",\\\"Jan.,Feb.,Mar.,Apr.,May,Jun.,Jul.,Aug.,Sep.,Oct.,Nov.,Dec.\\\",\\\"Sunday,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday\\\",\\\"Sun.,Mon.,Tue.,Wed.,Thu.,Fri.,Sat.\" [/1/SpreadsheetName111/cell/A1/dateTimeSymbols/save/%22am,pm%22,%22January,February,March,April,May,June,July,August,September,October,November,December%22,%22Jan.,Feb.,Mar.,Apr.,May,Jun.,Jul.,Aug.,Sep.,Oct.,Nov.,Dec.%22,%22Sunday,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday%22,%22Sun.,Mon.,Tue.,Wed.,Thu.,Fri.,Sat.%22] id=test-DateTimeSymbols-recent-0-MenuItem\n" +
                "    \"AM,PM\\\",\\\"January,February,March,April,May,June,July,August,September,October,November,December\\\",\\\"Jan,Feb,Mar,Apr,May,Jun,Jul,Aug,Sep,Oct,Nov,Dec\\\",\\\"Sunday,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday\\\",\\\"Sun,Mon,Tue,Wed,Thu,Fri,Sat\" [/1/SpreadsheetName111/cell/A1/dateTimeSymbols/save/%22AM,PM%22,%22January,February,March,April,May,June,July,August,September,October,November,December%22,%22Jan,Feb,Mar,Apr,May,Jun,Jul,Aug,Sep,Oct,Nov,Dec%22,%22Sunday,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday%22,%22Sun,Mon,Tue,Wed,Thu,Fri,Sat%22] id=test-DateTimeSymbols-recent-1-MenuItem\n"
        );
    }

    private void buildAndCheck(final SpreadsheetCellHistoryToken historyToken,
                               final List<DateTimeSymbols> recentDateTimeSymbols,
                               final Optional<SpreadsheetCell> summary,
                               final String expected) {
        final SpreadsheetSelectionMenuContext context = this.context(
            historyToken,
            recentDateTimeSymbols,
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

        SpreadsheetSelectionMenuValuesDateTimeSymbols.with(
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
                                                    final List<DateTimeSymbols> recentDateTimeSymbols,
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
            public List<DateTimeSymbols> recentDateTimeSymbols() {
                return recentDateTimeSymbols;
            }

            @Override
            public Optional<SpreadsheetCell> selectionSummary() {
                return summary;
            }
        };
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetSelectionMenuValuesDateTimeSymbols> type() {
        return SpreadsheetSelectionMenuValuesDateTimeSymbols.class;
    }
}
