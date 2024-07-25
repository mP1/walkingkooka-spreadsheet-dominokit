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

package walkingkooka.spreadsheet.dominokit.ui.find;

import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ui.datatable.SpreadsheetDataTableComponentCellRenderer;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.ui.text.SpreadsheetTextComponent;
import walkingkooka.spreadsheet.dominokit.ui.textnode.SpreadsheetTextNodeComponent;

import java.util.Optional;

final class SpreadsheetFindDialogComponentSpreadsheetDataTableComponentCellRenderer implements SpreadsheetDataTableComponentCellRenderer<SpreadsheetCell> {

    static SpreadsheetFindDialogComponentSpreadsheetDataTableComponentCellRenderer with(final SpreadsheetFindDialogComponentContext context) {
        return new SpreadsheetFindDialogComponentSpreadsheetDataTableComponentCellRenderer(context);
    }

    private SpreadsheetFindDialogComponentSpreadsheetDataTableComponentCellRenderer(final SpreadsheetFindDialogComponentContext context) {
        this.context = context;
    }

    @Override
    public HtmlElementComponent<?, ?> render(final int column,
                                             final SpreadsheetCell cell) {
        final HtmlElementComponent<?, ?> component;

        switch (column) {
            case 0: // cell
                component = renderCellReference(cell);
                break;
            case 1: // formula
                component = renderCellFormula(cell);
                break;
            case 2: // cell formatted value
                component = renderCellFormattedValue(cell);
                break;
            case 3: // value
                component = renderCellValue(cell);
                break;
            default:
                throw new IllegalArgumentException("Unknown column " + column);
        }

        return component;
    }

    private HistoryTokenAnchorComponent renderCellReference(final SpreadsheetCell cell) {
        final HistoryToken historyToken = this.context.historyToken();

        return HistoryTokenAnchorComponent.empty()
                .setTextContent(cell.reference().text())
                .setHistoryToken(
                        Optional.of(
                                historyToken.clearSelection()
                                        .setAnchoredSelection(
                                                Optional.of(
                                                        cell.reference()
                                                                .setDefaultAnchor()
                                                )
                                        )
                        )
                );
    }

    private HistoryTokenAnchorComponent renderCellFormula(final SpreadsheetCell cell) {
        final HistoryToken historyToken = this.context.historyToken();

        return HistoryTokenAnchorComponent.empty()
                .setTextContent(cell.formula().text())
                .setHistoryToken(
                        Optional.of(
                                historyToken.clearSelection()
                                        .setAnchoredSelection(
                                                Optional.of(
                                                        cell.reference()
                                                                .setDefaultAnchor()
                                                )
                                        ).setFormula()
                        )
                );
    }

    private SpreadsheetTextNodeComponent renderCellFormattedValue(final SpreadsheetCell cell) {
        return SpreadsheetTextNodeComponent.with(
                cell.formattedValue()
        );
    }

    private SpreadsheetTextComponent renderCellValue(final SpreadsheetCell cell) {
        return SpreadsheetTextComponent.with(
                cell.formula()
                        .value()
                        .map(Object::toString)
        );
    }

    private final SpreadsheetFindDialogComponentContext context;
}
