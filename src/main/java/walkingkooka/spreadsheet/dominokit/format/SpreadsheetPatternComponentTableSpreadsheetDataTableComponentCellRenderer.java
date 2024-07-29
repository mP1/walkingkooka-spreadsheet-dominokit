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

import walkingkooka.net.Url;
import walkingkooka.spreadsheet.dominokit.datatable.SpreadsheetDataTableComponentCellRenderer;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.ui.text.SpreadsheetTextComponent;
import walkingkooka.spreadsheet.dominokit.ui.textnode.SpreadsheetTextNodeComponent;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;

import java.util.Optional;

/**
 * Handles rendering cells within a {@link SpreadsheetPatternComponentTable}.
 */
final class SpreadsheetPatternComponentTableSpreadsheetDataTableComponentCellRenderer implements SpreadsheetDataTableComponentCellRenderer<SpreadsheetPatternComponentTableRow> {

    static SpreadsheetPatternComponentTableSpreadsheetDataTableComponentCellRenderer with(final SpreadsheetPatternDialogComponentContext context) {
        return new SpreadsheetPatternComponentTableSpreadsheetDataTableComponentCellRenderer(context);
    }

    private SpreadsheetPatternComponentTableSpreadsheetDataTableComponentCellRenderer(final SpreadsheetPatternDialogComponentContext context) {
        this.context = context;
    }

    @Override
    public HtmlElementComponent<?, ?> render(final int column,
                                             final SpreadsheetPatternComponentTableRow row) {
        HtmlElementComponent<?, ?> rendered;

        switch (column) {
            case 0:
                rendered = text(
                        row.label()
                );
                break;
            case 1:
                rendered = patternAnchor(
                        row.label(),
                        row.pattern()
                                .map(SpreadsheetPattern::text)
                                .orElse("")
                );
                break;
            case 2:
                rendered = textNode(
                        row,
                        0
                );
                break;
            case 3:
                rendered = textNode(
                        row,
                        1
                );
                break;
            case 4:
                rendered = textNode(
                        row,
                        2
                );
                break;
            default:
                throw new IllegalArgumentException("Invalid column " + column);
        }

        return rendered;
    }

    private SpreadsheetTextComponent text(final String text) {
        return SpreadsheetTextComponent.with(
                Optional.of(text)
        );
    }

    private SpreadsheetTextNodeComponent textNode(final SpreadsheetPatternComponentTableRow row,
                                                  final int index) {
        return SpreadsheetTextNodeComponent.with(
                Optional.of(
                        row.formatted()
                                .get(index)
                )
        );
    }

    /**
     * Creates an anchor which will appear in the pattern column
     */
    private HistoryTokenAnchorComponent patternAnchor(final String label,
                                                      final String patternText) {
        final SpreadsheetPatternDialogComponentContext context = this.context;

        return HistoryTokenAnchorComponent.empty()
                .setHref(
                        Url.EMPTY_RELATIVE_URL.setFragment(
                                context.historyToken()
                                        .setSave(
                                                context.savePatternText(
                                                        patternText
                                                )
                                        ).urlFragment()
                        )
                ).setTextContent(patternText)
                .setId(
                        SpreadsheetPatternDialogComponent.ID_PREFIX +
                                label.toLowerCase() +
                                SpreadsheetElementIds.LINK
                );
    }

    private final SpreadsheetPatternDialogComponentContext context;
}
