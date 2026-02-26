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

import java.util.Optional;

public final class SpreadsheetSelectionMenuInsertTest implements TreePrintableTesting,
    SpreadsheetMetadataTesting,
    ClassTesting<SpreadsheetSelectionMenuInsert> {

    @Test
    public void testBuildWithCell() {
        this.buildAndCheck(
            HistoryToken.selection(
                SpreadsheetId.with(1),
                SpreadsheetName.with("Spreadsheet123"),
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  (mdi-table-column-plus-before) \"Insert before column\" id=test-column-insert-before-SubMenu\n" +
                "    \"1\" [/1/Spreadsheet123/column/A/insertBefore/1] id=test-column-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/Spreadsheet123/column/A/insertBefore/2] id=test-column-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/Spreadsheet123/column/A/insertBefore/3] id=test-column-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/Spreadsheet123/column/A/insertBefore] id=test-column-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-column-plus-after) \"Insert after column\" id=test-column-insert-after-SubMenu\n" +
                "    \"1\" [/1/Spreadsheet123/column/A/insertAfter/1] id=test-column-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/Spreadsheet123/column/A/insertAfter/2] id=test-column-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/Spreadsheet123/column/A/insertAfter/3] id=test-column-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/Spreadsheet123/column/A/insertAfter] id=test-column-insert-after-prompt-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-row-plus-before) \"Insert before row\" id=test-row-insert-before-SubMenu\n" +
                "    \"1\" [/1/Spreadsheet123/row/1/insertBefore/1] id=test-row-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/Spreadsheet123/row/1/insertBefore/2] id=test-row-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/Spreadsheet123/row/1/insertBefore/3] id=test-row-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/Spreadsheet123/row/1/insertBefore] id=test-row-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-row-plus-after) \"Insert after row\" id=test-row-insert-after-SubMenu\n" +
                "    \"1\" [/1/Spreadsheet123/row/1/insertAfter/1] id=test-row-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/Spreadsheet123/row/1/insertAfter/2] id=test-row-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/Spreadsheet123/row/1/insertAfter/3] id=test-row-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/Spreadsheet123/row/1/insertAfter] id=test-row-insert-after-prompt-MenuItem\n"
        );
    }

    @Test
    public void testBuildWithColumn() {
        this.buildAndCheck(
            HistoryToken.selection(
                SpreadsheetId.with(1),
                SpreadsheetName.with("Spreadsheet123"),
                SpreadsheetSelection.A1.column()
                    .setDefaultAnchor()
            ),
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  (mdi-table-column-plus-before) \"Insert before column\" id=test-column-insert-before-SubMenu\n" +
                "    \"1\" [/1/Spreadsheet123/column/A/insertBefore/1] id=test-column-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/Spreadsheet123/column/A/insertBefore/2] id=test-column-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/Spreadsheet123/column/A/insertBefore/3] id=test-column-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/Spreadsheet123/column/A/insertBefore] id=test-column-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-column-plus-after) \"Insert after column\" id=test-column-insert-after-SubMenu\n" +
                "    \"1\" [/1/Spreadsheet123/column/A/insertAfter/1] id=test-column-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/Spreadsheet123/column/A/insertAfter/2] id=test-column-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/Spreadsheet123/column/A/insertAfter/3] id=test-column-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/Spreadsheet123/column/A/insertAfter] id=test-column-insert-after-prompt-MenuItem\n"
        );
    }

    @Test
    public void testBuildWithRow() {
        this.buildAndCheck(
            HistoryToken.selection(
                SpreadsheetId.with(1),
                SpreadsheetName.with("Spreadsheet123"),
                SpreadsheetSelection.A1.row()
                    .setDefaultAnchor()
            ),
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  (mdi-table-row-plus-before) \"Insert before row\" id=test-row-insert-before-SubMenu\n" +
                "    \"1\" [/1/Spreadsheet123/row/1/insertBefore/1] id=test-row-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/Spreadsheet123/row/1/insertBefore/2] id=test-row-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/Spreadsheet123/row/1/insertBefore/3] id=test-row-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/Spreadsheet123/row/1/insertBefore] id=test-row-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-row-plus-after) \"Insert after row\" id=test-row-insert-after-SubMenu\n" +
                "    \"1\" [/1/Spreadsheet123/row/1/insertAfter/1] id=test-row-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/Spreadsheet123/row/1/insertAfter/2] id=test-row-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/Spreadsheet123/row/1/insertAfter/3] id=test-row-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/Spreadsheet123/row/1/insertAfter] id=test-row-insert-after-prompt-MenuItem\n"
        );
    }

    private void buildAndCheck(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                               final String expected) {
        final SpreadsheetSelectionMenuContext context = this.context(
            historyToken
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

        SpreadsheetSelectionMenuInsert.build(
            historyToken,
            menu,
            context
        );

        this.treePrintAndCheck(
            menu,
            expected
        );
    }

    private SpreadsheetSelectionMenuContext context(final HistoryToken historyToken) {
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
            public Optional<SpreadsheetCell> selectionSummary() {
                return Optional.of(
                    SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                );
            }
        };
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetSelectionMenuInsert> type() {
        return SpreadsheetSelectionMenuInsert.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
