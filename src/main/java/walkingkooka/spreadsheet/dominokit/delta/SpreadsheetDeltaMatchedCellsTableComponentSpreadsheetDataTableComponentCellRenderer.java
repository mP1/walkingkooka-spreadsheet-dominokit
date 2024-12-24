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

package walkingkooka.spreadsheet.dominokit.delta;

import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.Component;
import walkingkooka.spreadsheet.dominokit.datatable.SpreadsheetDataTableComponentCellRenderer;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.value.SpreadsheetTextComponent;
import walkingkooka.spreadsheet.dominokit.value.SpreadsheetTextNodeComponent;

import java.util.Optional;

final class SpreadsheetDeltaMatchedCellsTableComponentSpreadsheetDataTableComponentCellRenderer implements SpreadsheetDataTableComponentCellRenderer<SpreadsheetCell> {

    static SpreadsheetDeltaMatchedCellsTableComponentSpreadsheetDataTableComponentCellRenderer with(final SpreadsheetDeltaMatchedCellsTableComponentContext context) {
        return new SpreadsheetDeltaMatchedCellsTableComponentSpreadsheetDataTableComponentCellRenderer(context);
    }

    private SpreadsheetDeltaMatchedCellsTableComponentSpreadsheetDataTableComponentCellRenderer(final SpreadsheetDeltaMatchedCellsTableComponentContext context) {
        this.context = context;
    }

    @Override
    public Component<?> render(final int column,
                               final SpreadsheetCell cell) {
        final Component<?> component;

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
                                        ).formula()
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

    private final SpreadsheetDeltaMatchedCellsTableComponentContext context;
}
