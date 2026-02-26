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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.validation.ValueType;

import java.util.Locale;
import java.util.Optional;

public final class SpreadsheetSelectionMenuValueTypeTest implements TreePrintableTesting,
    SpreadsheetMetadataTesting,
    ClassTesting<SpreadsheetSelectionMenuValueType> {

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
                "  \"Boolean\" [/1/Spreadsheet123/cell/A1/valueType/save/boolean] id=test-valueTypes-boolean-MenuItem\n" +
                "  \"Date\" [/1/Spreadsheet123/cell/A1/valueType/save/date] id=test-valueTypes-date-MenuItem\n" +
                "  \"Date Time\" [/1/Spreadsheet123/cell/A1/valueType/save/date-time] id=test-valueTypes-date-time-MenuItem\n" +
                "  \"Email\" [/1/Spreadsheet123/cell/A1/valueType/save/email] id=test-valueTypes-email-MenuItem\n" +
                "  \"Number\" [/1/Spreadsheet123/cell/A1/valueType/save/number] id=test-valueTypes-number-MenuItem\n" +
                "  \"Text\" [/1/Spreadsheet123/cell/A1/valueType/save/text] id=test-valueTypes-text-MenuItem\n" +
                "  \"Time\" [/1/Spreadsheet123/cell/A1/valueType/save/time] id=test-valueTypes-time-MenuItem\n" +
                "  \"Url\" [/1/Spreadsheet123/cell/A1/valueType/save/url] id=test-valueTypes-url-MenuItem\n" +
                "  \"Whole Number\" [/1/Spreadsheet123/cell/A1/valueType/save/whole-number] id=test-valueTypes-whole-number-MenuItem\n"
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
                "  \"Boolean\" [/1/Spreadsheet123/cell/A1/valueType/save/boolean] id=test-valueTypes-boolean-MenuItem\n" +
                "  \"Date\" [/1/Spreadsheet123/cell/A1/valueType/save/date] id=test-valueTypes-date-MenuItem\n" +
                "  \"Date Time\" [/1/Spreadsheet123/cell/A1/valueType/save/date-time] id=test-valueTypes-date-time-MenuItem\n" +
                "  \"Email\" [/1/Spreadsheet123/cell/A1/valueType/save/email] id=test-valueTypes-email-MenuItem\n" +
                "  \"Number\" [/1/Spreadsheet123/cell/A1/valueType/save/number] id=test-valueTypes-number-MenuItem\n" +
                "  \"Text\" [/1/Spreadsheet123/cell/A1/valueType/save/text] id=test-valueTypes-text-MenuItem\n" +
                "  \"Time\" [/1/Spreadsheet123/cell/A1/valueType/save/time] id=test-valueTypes-time-MenuItem\n" +
                "  \"Url\" [/1/Spreadsheet123/cell/A1/valueType/save/url] id=test-valueTypes-url-MenuItem\n" +
                "  \"Whole Number\" [/1/Spreadsheet123/cell/A1/valueType/save/whole-number] id=test-valueTypes-whole-number-MenuItem\n"
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
                    SpreadsheetFormula.EMPTY.setValueType(
                        Optional.of(ValueType.TEXT)
                    )
                )
            ),
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"Boolean\" [/1/Spreadsheet123/cell/A1/valueType/save/boolean] id=test-valueTypes-boolean-MenuItem\n" +
                "  \"Date\" [/1/Spreadsheet123/cell/A1/valueType/save/date] id=test-valueTypes-date-MenuItem\n" +
                "  \"Date Time\" [/1/Spreadsheet123/cell/A1/valueType/save/date-time] id=test-valueTypes-date-time-MenuItem\n" +
                "  \"Email\" [/1/Spreadsheet123/cell/A1/valueType/save/email] id=test-valueTypes-email-MenuItem\n" +
                "  \"Number\" [/1/Spreadsheet123/cell/A1/valueType/save/number] id=test-valueTypes-number-MenuItem\n" +
                "  \"Text\" [/1/Spreadsheet123/cell/A1/valueType/save/text] CHECKED id=test-valueTypes-text-MenuItem\n" +
                "  \"Time\" [/1/Spreadsheet123/cell/A1/valueType/save/time] id=test-valueTypes-time-MenuItem\n" +
                "  \"Url\" [/1/Spreadsheet123/cell/A1/valueType/save/url] id=test-valueTypes-url-MenuItem\n" +
                "  \"Whole Number\" [/1/Spreadsheet123/cell/A1/valueType/save/whole-number] id=test-valueTypes-whole-number-MenuItem\n"
        );
    }

    @Test
    public void testBuildWithRecents() {
        this.buildAndCheck(
            HistoryToken.selection(
                SpreadsheetId.with(1),
                SpreadsheetName.with("Spreadsheet123"),
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            Optional.empty(), // summary
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  \"Boolean\" [/1/Spreadsheet123/cell/A1/valueType/save/boolean] id=test-valueTypes-boolean-MenuItem\n" +
                "  \"Date\" [/1/Spreadsheet123/cell/A1/valueType/save/date] id=test-valueTypes-date-MenuItem\n" +
                "  \"Date Time\" [/1/Spreadsheet123/cell/A1/valueType/save/date-time] id=test-valueTypes-date-time-MenuItem\n" +
                "  \"Email\" [/1/Spreadsheet123/cell/A1/valueType/save/email] id=test-valueTypes-email-MenuItem\n" +
                "  \"Number\" [/1/Spreadsheet123/cell/A1/valueType/save/number] id=test-valueTypes-number-MenuItem\n" +
                "  \"Text\" [/1/Spreadsheet123/cell/A1/valueType/save/text] id=test-valueTypes-text-MenuItem\n" +
                "  \"Time\" [/1/Spreadsheet123/cell/A1/valueType/save/time] id=test-valueTypes-time-MenuItem\n" +
                "  \"Url\" [/1/Spreadsheet123/cell/A1/valueType/save/url] id=test-valueTypes-url-MenuItem\n" +
                "  \"Whole Number\" [/1/Spreadsheet123/cell/A1/valueType/save/whole-number] id=test-valueTypes-whole-number-MenuItem\n"
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

        SpreadsheetSelectionMenuValueType.build(
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
    public Class<SpreadsheetSelectionMenuValueType> type() {
        return SpreadsheetSelectionMenuValueType.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
