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
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuFactory;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public final class SpreadsheetSelectionMenuLocaleTest implements TreePrintableTesting,
    SpreadsheetMetadataTesting,
    ClassTesting<SpreadsheetSelectionMenuLocale> {

    @Test
    public void testBuild() {
        final SpreadsheetAnchoredSelectionHistoryToken historyToken = HistoryToken.selection(
            SpreadsheetId.with(1),
            SpreadsheetName.with("Spreadsheet123"),
            SpreadsheetSelection.A1.setDefaultAnchor()
        );

        final SpreadsheetSelectionMenuContext context = this.context(
            historyToken,
            Lists.of(
                Locale.forLanguageTag("en-AU"),
                Locale.forLanguageTag("en-NZ")
            ) // recent
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

        SpreadsheetSelectionMenuLocale.build(
            historyToken,
            menu,
            context
        );

        this.treePrintAndCheck(
            menu,
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  (mdi-close) \"Clear...\" [/1/Spreadsheet123/cell/A1/locale/save/und] id=test-locale-clear-MenuItem\n" +
                "  -----\n" +
                "  \"Edit...\" [/1/Spreadsheet123/cell/A1/locale] id=test-locale-edit-MenuItem\n" +
                "  -----\n" +
                "  \"English (Australia)\" [/1/Spreadsheet123/cell/A1/locale/save/en-AU] id=test-locale-recent-0-MenuItem\n" +
                "  \"English (New Zealand)\" [/1/Spreadsheet123/cell/A1/locale/save/en-NZ] id=test-locale-recent-1-MenuItem\n"
        );
    }

    private SpreadsheetSelectionMenuContext context(final HistoryToken historyToken,
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
                return Optional.of(
                    SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                        .setLocale(
                            Optional.of(
                                Locale.forLanguageTag("en-AU")
                            )
                        )
                );
            }
        };
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetSelectionMenuLocale> type() {
        return SpreadsheetSelectionMenuLocale.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
