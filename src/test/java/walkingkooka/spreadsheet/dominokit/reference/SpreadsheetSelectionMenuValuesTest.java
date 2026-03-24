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
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuFactory;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellLocaleSelectHistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public final class SpreadsheetSelectionMenuValuesTest implements TreePrintableTesting,
    ClassTesting<SpreadsheetSelectionMenuValues<Locale>> {

    private final static String ID_PREFIX = "Test123-";

    @Test
    public void testBuild() {
        final SpreadsheetCellLocaleSelectHistoryToken historyToken = HistoryToken.cellLocaleSelect(
            SpreadsheetId.with(1),
            SpreadsheetName.with("SpreadsheetName111"),
            SpreadsheetSelection.A1.setDefaultAnchor()
        );

        final FakeSpreadsheetSelectionMenuContext menuContext = new FakeSpreadsheetSelectionMenuContext() {

            @Override
            public String idPrefix() {
                return ID_PREFIX;
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
            "Locale\"Locale(s)\" id=Locale\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName111/cell/A1/locale/save/] id=Test123-Locale-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName111/cell/A1/locale] id=Test123-Locale-edit-MenuItem\n"
        );
    }

    @Test
    public void testBuildWithRecents() {
        final SpreadsheetCellLocaleSelectHistoryToken historyToken = HistoryToken.cellLocaleSelect(
            SpreadsheetId.with(1),
            SpreadsheetName.with("SpreadsheetName111"),
            SpreadsheetSelection.A1.setDefaultAnchor()
        );

        final FakeSpreadsheetSelectionMenuContext menuContext = new FakeSpreadsheetSelectionMenuContext() {

            @Override
            public String idPrefix() {
                return ID_PREFIX;
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
            "Locale\"Locale(s)\" id=Locale\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName111/cell/A1/locale/save/] id=Test123-Locale-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName111/cell/A1/locale] id=Test123-Locale-edit-MenuItem\n" +
                "    -----\n" +
                "    \"English (Australia)\" [/1/SpreadsheetName111/cell/A1/locale/save/en-AU] id=Test123-Locale-recent-0-MenuItem\n" +
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
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetSelectionMenuValues<Locale>> type() {
        return Cast.to(SpreadsheetSelectionMenuValues.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
