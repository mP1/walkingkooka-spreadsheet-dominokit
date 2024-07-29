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

import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.datatable.SpreadsheetDataTableComponentCellRenderer;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.value.SpreadsheetTextNodeComponent;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSample;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.tree.text.TextNode;

import java.util.Optional;

final class SpreadsheetFormatterTableComponentSpreadsheetDataTableComponentCellRenderer implements SpreadsheetDataTableComponentCellRenderer<SpreadsheetFormatterSample<TextNode>> {

    static SpreadsheetFormatterTableComponentSpreadsheetDataTableComponentCellRenderer with(final String id,
                                                                                            final SpreadsheetFormatterTableComponentContext context) {
        return new SpreadsheetFormatterTableComponentSpreadsheetDataTableComponentCellRenderer(
                id,
                context
        );
    }

    private SpreadsheetFormatterTableComponentSpreadsheetDataTableComponentCellRenderer(final String id,
                                                                                        final SpreadsheetFormatterTableComponentContext context) {
        this.id = id;
        this.context = context;
    }

    @Override
    public HtmlElementComponent<?, ?> render(final int column,
                                             final SpreadsheetFormatterSample<TextNode> sample) {
        final HtmlElementComponent<?, ?> rendered;

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

    private HtmlElementComponent<?, ?> label(final String label) {
        return SpreadsheetTextNodeComponent.with(
                Optional.of(
                        TextNode.text(label)
                )
        );
    }

    private HtmlElementComponent<?, ?> selector(final String id,
                                                final SpreadsheetFormatterSelector selector) {
        final HistoryToken historyToken = this.context.historyToken();

        return historyToken.link(id)
                .setTextContent(selector.text())
                .setHistoryToken(
                        Optional.of(
                                historyToken.setSave(
                                        this.context.saveText(selector.toString())
                                )
                        )
                );
    }

    private HtmlElementComponent<?, ?> formatted(final TextNode formatted) {
        return SpreadsheetTextNodeComponent.with(
                Optional.of(
                        formatted
                )
        );
    }

    private final String id;

    private final SpreadsheetFormatterTableComponentContext context;
}
