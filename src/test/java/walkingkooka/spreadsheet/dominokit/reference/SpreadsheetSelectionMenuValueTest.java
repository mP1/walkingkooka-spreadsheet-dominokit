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

import java.util.Locale;
import java.util.Optional;

public final class SpreadsheetSelectionMenuValueTest implements TreePrintableTesting,
    SpreadsheetMetadataTesting,
    ClassTesting<SpreadsheetSelectionMenuValue> {

    @Test
    public void testBuild() {
        this.buildAndCheck(
            HistoryToken.selection(
                SpreadsheetId.with(1),
                SpreadsheetName.with("Spreadsheet123"),
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            Optional.empty(), // summary
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"Boolean\" [/1/Spreadsheet123/cell/A1/value/boolean] id=test-value-boolean-MenuItem\n" +
                "  \"Date\" [/1/Spreadsheet123/cell/A1/value/date] id=test-value-date-MenuItem\n" +
                "  \"Date Time\" [/1/Spreadsheet123/cell/A1/value/date-time] id=test-value-date-time-MenuItem\n" +
                "  \"Email\" [/1/Spreadsheet123/cell/A1/value/email] id=test-value-email-MenuItem\n" +
                "  \"Number\" [/1/Spreadsheet123/cell/A1/value/number] id=test-value-number-MenuItem\n" +
                "  \"Text\" [/1/Spreadsheet123/cell/A1/value/text] id=test-value-text-MenuItem\n" +
                "  \"Time\" [/1/Spreadsheet123/cell/A1/value/time] id=test-value-time-MenuItem\n" +
                "  \"Url\" [/1/Spreadsheet123/cell/A1/value/url] id=test-value-url-MenuItem\n" +
                "  \"Whole Number\" [/1/Spreadsheet123/cell/A1/value/whole-number] id=test-value-whole-number-MenuItem\n"
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
            Optional.empty(), // summary
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"Boolean\" [/1/Spreadsheet123/cell/A1/value/boolean] id=test-value-boolean-MenuItem\n" +
                "  \"Date\" [/1/Spreadsheet123/cell/A1/value/date] id=test-value-date-MenuItem\n" +
                "  \"Date Time\" [/1/Spreadsheet123/cell/A1/value/date-time] id=test-value-date-time-MenuItem\n" +
                "  \"Email\" [/1/Spreadsheet123/cell/A1/value/email] id=test-value-email-MenuItem\n" +
                "  \"Number\" [/1/Spreadsheet123/cell/A1/value/number] id=test-value-number-MenuItem\n" +
                "  \"Text\" [/1/Spreadsheet123/cell/A1/value/text] id=test-value-text-MenuItem\n" +
                "  \"Time\" [/1/Spreadsheet123/cell/A1/value/time] id=test-value-time-MenuItem\n" +
                "  \"Url\" [/1/Spreadsheet123/cell/A1/value/url] id=test-value-url-MenuItem\n" +
                "  \"Whole Number\" [/1/Spreadsheet123/cell/A1/value/whole-number] id=test-value-whole-number-MenuItem\n"
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
            Optional.of(
                SpreadsheetSelection.A1.setFormula(
                    SpreadsheetFormula.EMPTY.setValue(
                        Optional.of("Hello")
                    )
                )
            ),
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"Boolean\" [/1/Spreadsheet123/cell/A1/value/boolean] id=test-value-boolean-MenuItem\n" +
                "  \"Date\" [/1/Spreadsheet123/cell/A1/value/date] id=test-value-date-MenuItem\n" +
                "  \"Date Time\" [/1/Spreadsheet123/cell/A1/value/date-time] id=test-value-date-time-MenuItem\n" +
                "  \"Email\" [/1/Spreadsheet123/cell/A1/value/email] id=test-value-email-MenuItem\n" +
                "  \"Number\" [/1/Spreadsheet123/cell/A1/value/number] id=test-value-number-MenuItem\n" +
                "  \"Text\" [/1/Spreadsheet123/cell/A1/value/text] CHECKED id=test-value-text-MenuItem\n" +
                "  \"Time\" [/1/Spreadsheet123/cell/A1/value/time] id=test-value-time-MenuItem\n" +
                "  \"Url\" [/1/Spreadsheet123/cell/A1/value/url] id=test-value-url-MenuItem\n" +
                "  \"Whole Number\" [/1/Spreadsheet123/cell/A1/value/whole-number] id=test-value-whole-number-MenuItem\n"
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

        SpreadsheetSelectionMenuValue.build(
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
    public Class<SpreadsheetSelectionMenuValue> type() {
        return SpreadsheetSelectionMenuValue.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
