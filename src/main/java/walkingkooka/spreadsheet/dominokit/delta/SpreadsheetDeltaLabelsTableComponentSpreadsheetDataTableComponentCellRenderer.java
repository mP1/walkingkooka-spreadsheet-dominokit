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
import walkingkooka.spreadsheet.dominokit.datatable.SpreadsheetDataTableComponentCellRenderer;
import walkingkooka.spreadsheet.dominokit.formula.SpreadsheetFormulaSelectAnchorComponent;
import walkingkooka.spreadsheet.dominokit.label.SpreadsheetLabelLinksComponent;
import walkingkooka.spreadsheet.dominokit.label.SpreadsheetLabelSelectAnchorComponent;
import walkingkooka.spreadsheet.dominokit.text.SpreadsheetTextNodeComponent;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

final class SpreadsheetDeltaLabelsTableComponentSpreadsheetDataTableComponentCellRenderer implements SpreadsheetDataTableComponentCellRenderer<SpreadsheetDeltaLabelsTableComponentRow> {

    static SpreadsheetDeltaLabelsTableComponentSpreadsheetDataTableComponentCellRenderer with(final String idPrefix,
                                                                                              final SpreadsheetDeltaLabelsTableComponentContext context) {
        return new SpreadsheetDeltaLabelsTableComponentSpreadsheetDataTableComponentCellRenderer(
            idPrefix,
            context
        );
    }

    private SpreadsheetDeltaLabelsTableComponentSpreadsheetDataTableComponentCellRenderer(final String idPrefix,
                                                                                          final SpreadsheetDeltaLabelsTableComponentContext context) {
        this.idPrefix = idPrefix;
        this.context = context;
    }

    @Override
    public Component render(final int column,
                            final SpreadsheetDeltaLabelsTableComponentRow row) {
        final Component component;

        switch (column) {
            case 0: // cell
                component = this.renderLabel(row);
                break;
            case 1: // formula
                component = this.renderCellFormula(row);
                break;
            case 2: // cell formatted value
                component = this.renderCellFormattedValue(row);
                break;
            case 3: // links
                component = this.renderLabelLinks(row);
                break;
            default:
                throw new IllegalArgumentException("Unknown column " + column);
        }

        return component;
    }

    private Component renderLabel(final SpreadsheetDeltaLabelsTableComponentRow row) {
        final SpreadsheetLabelName labelName = row.mapping.label();

        return SpreadsheetLabelSelectAnchorComponent.with(
            this.idPrefix + labelName + SpreadsheetElementIds.LINK,
            this.context
        ).setValue(
            Optional.of(labelName)
        );
    }

    private Component renderCellFormula(final SpreadsheetDeltaLabelsTableComponentRow row) {
        final SpreadsheetLabelName labelName = row.mapping.label();

        final SpreadsheetFormulaSelectAnchorComponent component = SpreadsheetFormulaSelectAnchorComponent.with(
                this.idPrefix + labelName + "-formula" + SpreadsheetElementIds.LINK,
                this.context
            ).showFormulaText()
            .setValue(
                Optional.of(labelName)
            );

        // https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/4799
        // Prefer to show target reference (cell etc) than the label, if formula is shown leave as is.
        if (component.textContent().equalsIgnoreCase(labelName.text())) {
            final SpreadsheetSelection notLabel = this.context.resolveLabel(labelName)
                .orElse(null);
            if (null != notLabel) {
                component.setTextContent(notLabel.text());
            }
        }

        return component;
    }

    private SpreadsheetTextNodeComponent renderCellFormattedValue(final SpreadsheetDeltaLabelsTableComponentRow row) {
        return
            SpreadsheetTextNodeComponent.with(
                row.cell.flatMap(SpreadsheetCell::formattedValue)
            );
    }

    private SpreadsheetLabelLinksComponent renderLabelLinks(final SpreadsheetDeltaLabelsTableComponentRow row) {
        final SpreadsheetLabelName labelName = row.mapping.label();

        return SpreadsheetLabelLinksComponent.empty(
            this.idPrefix + labelName + "-",
            this.context
        ).setValue(
            Optional.of(labelName)
        );
    }

    private final String idPrefix;

    private final SpreadsheetDeltaLabelsTableComponentContext context;
}
