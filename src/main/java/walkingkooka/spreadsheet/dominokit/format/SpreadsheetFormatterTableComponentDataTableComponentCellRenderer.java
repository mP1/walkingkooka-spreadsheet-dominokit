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

package walkingkooka.spreadsheet.dominokit.format;

import walkingkooka.spreadsheet.dominokit.Component;
import walkingkooka.spreadsheet.dominokit.datatable.DataTableComponentCellRenderer;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.text.TextNodeComponent;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSample;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.tree.text.TextNode;

import java.util.Optional;

final class SpreadsheetFormatterTableComponentDataTableComponentCellRenderer implements DataTableComponentCellRenderer<SpreadsheetFormatterSample> {

    static SpreadsheetFormatterTableComponentDataTableComponentCellRenderer with(final String id) {
        return new SpreadsheetFormatterTableComponentDataTableComponentCellRenderer(id);
    }

    private SpreadsheetFormatterTableComponentDataTableComponentCellRenderer(final String id) {
        this.id = id;
    }

    @Override
    public Component render(final int column,
                            final SpreadsheetFormatterSample sample) {
        final Component rendered;

        switch (column) {
            case 0:
                rendered = this.label(sample.label());
                break;
            case 1:
                rendered = this.selector(
                    this.id + sample.label(),
                    sample.selector()
                );
                break;
            case 2:
                rendered = this.formatted(
                    sample.value()
                );
                break;
            default:
                throw new IllegalArgumentException("Invalid column " + column);
        }

        return rendered;
    }

    private Component label(final String label) {
        return TextNodeComponent.with(
            Optional.of(
                TextNode.text(label)
            )
        );
    }

    private Component selector(final String id,
                               final SpreadsheetFormatterSelector selector) {
        final HistoryToken historyToken = this.context.historyToken();

        return historyToken.saveLink(
            id,
            selector.valueText(),
            this.context.formatterTableHistoryTokenSave(selector)
        );
    }

    private Component formatted(final TextNode formatted) {
        return TextNodeComponent.with(
            Optional.of(
                formatted
            )
        );
    }

    private final String id;

    SpreadsheetFormatterTableComponentContext context;
}
