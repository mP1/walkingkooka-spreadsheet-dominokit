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
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellLocaleSelectHistoryToken;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.SpreadsheetCell;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public final class SpreadsheetSelectionMenuValuesLocaleTest extends SpreadsheetSelectionMenuValuesTestCase<SpreadsheetSelectionMenuValuesLocale, Locale> {

    @Test
    public void testBuild() {
        this.buildAndCheck(
            HistoryToken.cellLocaleSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL
            ),
            Optional.empty(), // summary
            Lists.empty(),
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"Locale\" id=test-Locale-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName111/cell/A1/locale/save/] id=test-Locale-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName111/cell/A1/locale] id=test-Locale-edit-MenuItem\n"
        );
    }

    @Test
    public void testBuildWithSpreadsheetCellFormulaHistoryToken() {
        this.buildAndCheck(
            HistoryToken.cellFormula(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL
            ).locale()
                .cast(SpreadsheetCellLocaleSelectHistoryToken.class),
            Optional.empty(), // summary
            Lists.empty(), // recents
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"Locale\" id=test-Locale-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName111/cell/A1/locale/save/] id=test-Locale-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName111/cell/A1/locale] id=test-Locale-edit-MenuItem\n"
        );
    }

    @Test
    public void testBuildWithChecked() {
        this.buildAndCheck(
            HistoryToken.cellLocaleSelect(
                SpreadsheetId.with(1),
                SpreadsheetName.with("SpreadsheetName111"),
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            Optional.of(
                SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                    .setLocale(
                        Optional.of(
                            Locale.forLanguageTag("en-AU")
                        )
                    )
            ),
            Lists.empty(), // recents
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"Locale\" id=test-Locale-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName111/cell/A1/locale/save/] id=test-Locale-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName111/cell/A1/locale] id=test-Locale-edit-MenuItem\n"
        );
    }

    @Test
    public void testBuildWithRecents() {
        this.buildAndCheck(
            HistoryToken.cellLocaleSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL
            ),
            Optional.empty(), // summary
            Lists.of(
                Locale.forLanguageTag("en-AU"),
                Locale.forLanguageTag("en-NZ")
            ), // recent,
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"Locale\" id=test-Locale-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName111/cell/A1/locale/save/] id=test-Locale-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName111/cell/A1/locale] id=test-Locale-edit-MenuItem\n" +
                "    -----\n" +
                "    \"English (Australia)\" [/1/SpreadsheetName111/cell/A1/locale/save/en-AU] id=test-Locale-recent-0-MenuItem\n" +
                "    \"English (New Zealand)\" [/1/SpreadsheetName111/cell/A1/locale/save/en-NZ] id=test-Locale-recent-1-MenuItem\n"
        );
    }

    private void buildAndCheck(final SpreadsheetCellLocaleSelectHistoryToken historyToken,
                               final Optional<SpreadsheetCell> summary,
                               final List<Locale> recents,
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

        SpreadsheetSelectionMenuValuesLocale.with(
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
                                                    final List<Locale> recentLocales) {
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
            public List<Locale> recentLocales() {
                return recentLocales;
            }

            @Override
            public Optional<SpreadsheetCell> selectionSummary() {
                return summary;
            }
        };
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetSelectionMenuValuesLocale> type() {
        return SpreadsheetSelectionMenuValuesLocale.class;
    }
}
