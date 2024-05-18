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

package walkingkooka.spreadsheet.dominokit.ui.pattern;

import elemental2.dom.HTMLDivElement;
import elemental2.dom.Node;
import org.dominokit.domino.ui.datatable.CellTextAlign;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.Url;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIds;
import walkingkooka.spreadsheet.dominokit.ui.card.SpreadsheetCard;
import walkingkooka.spreadsheet.dominokit.ui.datatable.SpreadsheetDataTableComponent;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.ui.text.SpreadsheetTextComponent;
import walkingkooka.spreadsheet.dominokit.ui.textnode.SpreadsheetTextNodeComponent;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.tree.text.TextAlign;

import java.util.List;
import java.util.Optional;

/**
 * A table that holds available patterns used to format values.
 */
final class SpreadsheetPatternComponentTable implements HtmlElementComponent<HTMLDivElement, SpreadsheetPatternComponentTable> {

    /**
     * Creates an empty {@link SpreadsheetPatternComponentTable}.
     */
    static SpreadsheetPatternComponentTable empty() {
        return new SpreadsheetPatternComponentTable();
    }

    private SpreadsheetPatternComponentTable() {
        this.card = SpreadsheetCard.empty();
    }

    void refresh(final String patternText,
                 final SpreadsheetPatternDialogComponentContext context) {
        final SpreadsheetPatternKind patternKind = context.patternKind();

        // recreate table if pattern kind different
        if (false == patternKind.equals(this.patternKind)) {
            this.card.removeAllChildren();

            this.patternKind = patternKind;
            this.dataTable = SpreadsheetDataTableComponent.with(
                    SpreadsheetPatternDialogComponent.ID_PREFIX + SpreadsheetIds.TABLE, // id
                    columnConfigs(patternKind), // configs
                    (column, row) -> {
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
                                                .orElse(""),
                                        context
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
            ).hideHeaders();
            this.card.appendChild(this.dataTable);
        }

        // load dataStore and table with new rows...
        this.dataTable.setValue(
                Optional.of(
                        SpreadsheetPatternComponentTableRowProvider.spreadsheetPatternKind(patternKind)
                                .apply(
                                        patternText,
                                        SpreadsheetPatternComponentTableRowProviderContexts.basic(
                                                patternKind,
                                                context.spreadsheetFormatterContext(),
                                                context
                                        )
                                )
                )
        );
    }

    private SpreadsheetPatternKind patternKind;

    private SpreadsheetDataTableComponent<SpreadsheetPatternComponentTableRow> dataTable;

    /**
     * Prepares the {@link ColumnConfig} for each of the columns that will appear in the table.
     */
    private static List<ColumnConfig<SpreadsheetPatternComponentTableRow>> columnConfigs(final SpreadsheetPatternKind kind) {
        final List<ColumnConfig<SpreadsheetPatternComponentTableRow>> columns = Lists.array();

        columns.add(
                columnConfig(
                        "label",
                        TextAlign.LEFT
                )
        );

        columns.add(
                columnConfig(
                        "pattern-text",
                        TextAlign.CENTER
                )
        );

        switch (kind) {
            case NUMBER_FORMAT_PATTERN:
            case NUMBER_PARSE_PATTERN:
                for (int i = 0; i < 3; i++) {
                    final int j = i;
                    columns.add(
                            columnConfig(
                                    "formatted-" + j,
                                    TextAlign.CENTER
                            )
                    );
                }

                break;
            default:
                columns.add(
                        columnConfig(
                                "formatted",
                                TextAlign.CENTER
                        )
                );
        }

        return columns;
    }

    private static ColumnConfig<SpreadsheetPatternComponentTableRow> columnConfig(final String columnName,
                                                                                  final TextAlign textAlign) {
        return ColumnConfig.<SpreadsheetPatternComponentTableRow>create(columnName)
                .setFixed(true)
                .minWidth("25%")
                .setTextAlign(
                        CellTextAlign.valueOf(
                                textAlign.name()
                        )
                );
    }

    private static SpreadsheetTextComponent text(final String text) {
        return SpreadsheetTextComponent.with(
                Optional.of(text)
        );
    }

    private static SpreadsheetTextNodeComponent textNode(final SpreadsheetPatternComponentTableRow row,
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
    private static HistoryTokenAnchorComponent patternAnchor(final String label,
                                                             final String patternText,
                                                             final SpreadsheetPatternDialogComponentContext context) {
        return HistoryTokenAnchorComponent.empty()
                .setHref(
                        Url.EMPTY_RELATIVE_URL.setFragment(
                                context.historyToken()
                                        .setSave(patternText)
                                        .urlFragment()
                        )
                ).setTextContent(patternText)
                .setId(
                        SpreadsheetPatternDialogComponent.ID_PREFIX +
                                label.toLowerCase() +
                                SpreadsheetIds.LINK
                );
    }

    // IsElement........................................................................................................

    @Override
    public HTMLDivElement element() {
        return this.card.element();
    }

    // node.............................................................................................................

    @Override
    public Node node() {
        return this.element();
    }

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetPatternComponentTable setCssText(final String css) {
        this.card.setCssText(css);
        return this;
    }

    private final SpreadsheetCard card;
}
