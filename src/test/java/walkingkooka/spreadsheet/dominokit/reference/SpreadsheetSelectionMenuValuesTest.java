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

import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.menu.Menu;
import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.plugin.PluginSelectorLike;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuFactory;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellLocaleSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.reference.SpreadsheetSelectionMenuValuesTest.TestSpreadsheetSelectionMenuValues;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.validation.provider.ValidatorSelector;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public final class SpreadsheetSelectionMenuValuesTest extends SpreadsheetSelectionMenuValuesTestCase<TestSpreadsheetSelectionMenuValues, Locale> {

    private final static String ID_PREFIX = "Test123-";

    @Test
    public void testBuild() {
        final SpreadsheetCellLocaleSelectHistoryToken historyToken = HistoryToken.cellLocaleSelect(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            SpreadsheetSelection.A1.setDefaultAnchor()
        );

        final FakeSpreadsheetSelectionMenuContext menuContext = new FakeSpreadsheetSelectionMenuContext() {

            @Override
            public String idPrefix() {
                return ID_PREFIX;
            }

            @Override
            public Optional<SpreadsheetCell> selectionSummary() {
                return Optional.of(
                    SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                );
            }
        };

        final SpreadsheetSelectionMenuValues<Locale> menuValues = new TestSpreadsheetSelectionMenuValues(
            historyToken,
            SpreadsheetContextMenuFactory.with(
                Menu.create(
                    "Locale",
                    "Locale(s)",
                    Optional.empty(), // no icon
                    Optional.empty() // no badge
                ),
                menuContext
            ),
            menuContext
        ) {

        };
        menuValues.build();

        this.treePrintAndCheck(
            menuValues,
            "  \"Locale\" id=Test123-Locale-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName111/cell/A1/locale/save/] id=Test123-Locale-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName111/cell/A1/locale] id=Test123-Locale-edit-MenuItem\n"
        );
    }

    @Test
    public void testBuildWithRecents() {
        final SpreadsheetCellLocaleSelectHistoryToken historyToken = HistoryToken.cellLocaleSelect(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            SpreadsheetSelection.A1.setDefaultAnchor()
        );

        final FakeSpreadsheetSelectionMenuContext menuContext = new FakeSpreadsheetSelectionMenuContext() {

            @Override
            public String idPrefix() {
                return ID_PREFIX;
            }

            @Override
            public Optional<SpreadsheetCell> selectionSummary() {
                return Optional.of(
                    SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                );
            }
        };

        final SpreadsheetSelectionMenuValues<Locale> menuValues = new TestSpreadsheetSelectionMenuValues(
            historyToken,
            SpreadsheetContextMenuFactory.with(
                Menu.create(
                    "Locale",
                    "Locale(s)",
                    Optional.empty(), // no icon
                    Optional.empty() // no badge
                ),
                menuContext
            ),
            menuContext,
            Locale.forLanguageTag("en-AU"),
            Locale.forLanguageTag("en-CA")
        ) {

        };
        menuValues.build();

        this.treePrintAndCheck(
            menuValues,
            "  \"Locale\" id=Test123-Locale-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName111/cell/A1/locale/save/] id=Test123-Locale-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName111/cell/A1/locale] id=Test123-Locale-edit-MenuItem\n" +
                "    -----\n" +
                "    \"English (Australia)\" [/1/SpreadsheetName111/cell/A1/locale/save/en-AU] CHECKED id=Test123-Locale-recent-0-MenuItem\n" +
                "    \"English (Canada)\" [/1/SpreadsheetName111/cell/A1/locale/save/en-CA] id=Test123-Locale-recent-1-MenuItem\n"
        );
    }

    static abstract class TestSpreadsheetSelectionMenuValues extends SpreadsheetSelectionMenuValues<Locale> {

        TestSpreadsheetSelectionMenuValues(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                                           final SpreadsheetContextMenu menu,
                                           final SpreadsheetSelectionMenuContext context,
                                           final Locale ... recentValues) {
            super(
                historyToken,
                menu,
                context
            );

            this.recentValues = Lists.of(recentValues);
        }

        @Override
        void values() {
            // no values
        }

        @Override
        Optional<Icon<?>> clearIcon() {
            return Optional.of(
                SpreadsheetIcons.localeRemove() // "close"
            );
        }

        @Override
        Collection<Locale> recentValues() {
            return this.recentValues;
        }

        private final List<Locale> recentValues;

        @Override
        String recentText(final Locale value) {
            return value.getDisplayName();
        }

        @Override
        Class<Locale> type() {
            return Locale.class;
        }

        @Override
        Optional<Locale> spreadsheetCellValue(final SpreadsheetCell cell) {
            return Optional.ofNullable(
                this.recentValues.isEmpty() ?
                    null :
                    this.recentValues.get(0)
            );
        }
    }

    // selectorToMenuItemText...........................................................................................

    @Test
    public void testSelectorToMenuItemTextWithSpreadsheetFormatterSelector() {
        this.selectorToMenuItemTextAndCheck(
            SpreadsheetPattern.parseDateFormatPattern("dd/mm/yyyy")
                .spreadsheetFormatterSelector(),
            "Date"
        );
    }

    @Test
    public void testSelectorToMenuItemTextWithValidatorSelector() {
        this.selectorToMenuItemTextAndCheck(
            ValidatorSelector.parse("hello-validator"),
            "Hello Validator"
        );
    }

    @Test
    public void testSelectorToMenuItemTextWithValidatorSelectorAndParametersIgnored() {
        this.selectorToMenuItemTextAndCheck(
            ValidatorSelector.parse("hello-validator \"abc123\""),
            "Hello Validator"
        );
    }

    private void selectorToMenuItemTextAndCheck(final PluginSelectorLike<?> selector,
                                                final String expected) {
        this.checkEquals(
            expected,
            SpreadsheetSelectionMenuValues.selectorToMenuItemText(selector),
            selector::toString
        );
    }

    // class............................................................................................................

    @Override
    public void testTestNaming() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<TestSpreadsheetSelectionMenuValues> type() {
        return TestSpreadsheetSelectionMenuValues.class;
    }
}
