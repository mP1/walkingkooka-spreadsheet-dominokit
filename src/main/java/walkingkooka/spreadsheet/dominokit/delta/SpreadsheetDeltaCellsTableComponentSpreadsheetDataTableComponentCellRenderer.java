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
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.cell.SpreadsheetCellLinksComponent;
import walkingkooka.spreadsheet.dominokit.datatable.SpreadsheetDataTableComponentCellRenderer;
import walkingkooka.spreadsheet.dominokit.formula.SpreadsheetFormulaSelectAnchorComponent;
import walkingkooka.spreadsheet.dominokit.spreadsheetexpressionreference.SpreadsheetExpressionReferenceSelectAnchorComponent;
import walkingkooka.spreadsheet.dominokit.text.TextComponent;
import walkingkooka.spreadsheet.dominokit.text.TextNodeComponent;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;

import java.util.Optional;

final class SpreadsheetDeltaCellsTableComponentSpreadsheetDataTableComponentCellRenderer implements SpreadsheetDataTableComponentCellRenderer<SpreadsheetCell> {

    static SpreadsheetDeltaCellsTableComponentSpreadsheetDataTableComponentCellRenderer with(final String idPrefix,
                                                                                             final SpreadsheetDeltaCellsTableComponentContext context) {
        return new SpreadsheetDeltaCellsTableComponentSpreadsheetDataTableComponentCellRenderer(
            idPrefix,
            context
        );
    }

    private SpreadsheetDeltaCellsTableComponentSpreadsheetDataTableComponentCellRenderer(final String idPrefix,
                                                                                         final SpreadsheetDeltaCellsTableComponentContext context) {
        this.idPrefix = idPrefix;
        this.context = context;
    }

    @Override
    public Component render(final int column,
                            final SpreadsheetCell cell) {
        final Component component;

        switch (column) {
            case 0: // cell
                component = renderCellReference(cell);
                break;
            case 1: // formula
                component = renderCellFormula(cell);
                break;
            case 2: // value
                component = renderCellValue(cell);
                break;
            case 3: // cell formatted value
                component = renderCellFormattedValue(cell);
                break;
            case 4: // links
                component = renderCellLinks(cell);
                break;
            default:
                throw new IllegalArgumentException("Unknown column " + column);
        }

        return component;
    }

    private Component renderCellReference(final SpreadsheetCell cell) {
        final SpreadsheetCellReference reference = cell.reference();

        return SpreadsheetExpressionReferenceSelectAnchorComponent.with(
            this.idPrefix + reference + SpreadsheetElementIds.LINK,
            this.context
        ).setValue(
            Optional.of(reference)
        );
    }

    private Component renderCellFormula(final SpreadsheetCell cell) {
        final SpreadsheetCellReference reference = cell.reference();

        return SpreadsheetFormulaSelectAnchorComponent.with(
                this.idPrefix + reference + "-formula" + SpreadsheetElementIds.LINK,
                this.context
            ).showFormulaText()
            .setValue(
                Optional.of(reference)
            );
    }

    private TextNodeComponent renderCellFormattedValue(final SpreadsheetCell cell) {
        return TextNodeComponent.with(
            cell.formattedValue()
        );
    }

    private TextComponent renderCellValue(final SpreadsheetCell cell) {
        return TextComponent.with(
            cell.formula()
                .errorOrValue()
                .map(Object::toString)
        );
    }

    private SpreadsheetCellLinksComponent renderCellLinks(final SpreadsheetCell cell) {
        final SpreadsheetCellReference cellReference = cell.reference();

        return SpreadsheetCellLinksComponent.empty(
            this.idPrefix + cellReference + "-",
            this.context
        ).setValue(
            Optional.of(cellReference)
        );
    }

    private final String idPrefix;

    private final SpreadsheetDeltaCellsTableComponentContext context;
}
