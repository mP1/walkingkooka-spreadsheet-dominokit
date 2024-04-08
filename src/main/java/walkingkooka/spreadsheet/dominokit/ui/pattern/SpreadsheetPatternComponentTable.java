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

import elemental2.dom.HTMLAnchorElement;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.Node;
import org.dominokit.domino.ui.cards.Card;
import org.dominokit.domino.ui.datatable.CellTextAlign;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import org.dominokit.domino.ui.datatable.DataTable;
import org.dominokit.domino.ui.datatable.TableConfig;
import org.dominokit.domino.ui.datatable.store.LocalListDataStore;
import walkingkooka.net.Url;
import walkingkooka.spreadsheet.dominokit.dom.Doms;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIds;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.tree.text.TextAlign;

import java.util.function.Function;

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
        this.card = Card.create();
    }

    void refresh(final String patternText,
                 final SpreadsheetPatternComponentContext context) {
        final SpreadsheetPatternKind patternKind = context.patternKind();

        // recreate table if pattern kind different
        if (false == patternKind.equals(this.patternKind)) {
            this.card.clearElement();

            this.patternKind = patternKind;

            final LocalListDataStore<SpreadsheetPatternComponentTableRow> localListDataStore = new LocalListDataStore<>();
            this.table = new DataTable<>(
                    tableConfig(
                            patternKind,
                            context
                    ),
                    localListDataStore
            );
            this.dataStore = localListDataStore;
            this.table.headerElement().hide();

            this.card.appendChild(this.table);
        }

        // load dataStore and table with new rows...
        this.dataStore.setData(
                SpreadsheetPatternComponentTableRowProvider.spreadsheetPatternKind(patternKind)
                        .apply(
                                patternText,
                                SpreadsheetPatternComponentTableRowProviderContexts.basic(
                                        patternKind,
                                        context.spreadsheetFormatterContext(),
                                        context
                                )
                        )
        );
        this.table.load();
    }

    private SpreadsheetPatternKind patternKind;

    private DataTable<SpreadsheetPatternComponentTableRow> table;

    private LocalListDataStore<SpreadsheetPatternComponentTableRow> dataStore;

    private static TableConfig<SpreadsheetPatternComponentTableRow> tableConfig(final SpreadsheetPatternKind kind,
                                                                                final SpreadsheetPatternComponentContext context) {
        final TableConfig<SpreadsheetPatternComponentTableRow> tableConfig = new TableConfig<SpreadsheetPatternComponentTableRow>()
                .addColumn(
                        columnConfig(
                                "label",
                                TextAlign.LEFT,
                                d -> Doms.textNode(d.label())
                        )
                ).addColumn(
                        columnConfig(
                                "pattern-text",
                                TextAlign.CENTER,
                                d -> patternAnchor(
                                        d.label(),
                                        d.pattern()
                                                .map(SpreadsheetPattern::text)
                                                .orElse(""),
                                        context
                                )
                        )
                );

        switch (kind) {
            case NUMBER_FORMAT_PATTERN:
            case NUMBER_PARSE_PATTERN:
                for (int i = 0; i < 3; i++) {
                    final int j = i;
                    tableConfig.addColumn(
                            columnConfig(
                                    "formatted-" + j,
                                    TextAlign.CENTER,
                                    d -> Doms.node(
                                            d.formatted()
                                                    .get(j)
                                    )
                            )
                    );
                }

                break;
            default:
                tableConfig.addColumn(
                        columnConfig(
                                "formatted",
                                TextAlign.CENTER,
                                d -> Doms.node(
                                        d.formatted()
                                                .get(0)
                                )
                        )
                );
        }

        return tableConfig;
    }

    private static ColumnConfig<SpreadsheetPatternComponentTableRow> columnConfig(final String columnName,
                                                                                  final TextAlign textAlign,
                                                                                  final Function<SpreadsheetPatternComponentTableRow, Node> nodeMapper) {
        return ColumnConfig.<SpreadsheetPatternComponentTableRow>create(columnName)
                .setFixed(true)
                .minWidth("25%")
                .setTextAlign(
                        CellTextAlign.valueOf(
                                textAlign.name()
                        )
                )
                .setCellRenderer(cell -> nodeMapper.apply(
                                cell.getTableRow()
                                        .getRecord()
                        )
                );
    }

    /**
     * Creates an anchor which will appear in the pattern column
     */
    private static HTMLAnchorElement patternAnchor(final String label,
                                                   final String patternText,
                                                   final SpreadsheetPatternComponentContext context) {
        return HistoryTokenAnchorComponent.empty()
                .setHref(
                        Url.EMPTY_RELATIVE_URL.setFragment(
                                context.historyToken()
                                        .setSave(patternText)
                                        .urlFragment()
                        )
                ).setTextContent(patternText)
                .setId(
                        SpreadsheetPatternComponent.ID_PREFIX +
                                label.toLowerCase() +
                                SpreadsheetIds.LINK
                ).element();
    }

    @Override
    public HTMLDivElement element() {
        return this.card.element();
    }

    private final Card card;
}
