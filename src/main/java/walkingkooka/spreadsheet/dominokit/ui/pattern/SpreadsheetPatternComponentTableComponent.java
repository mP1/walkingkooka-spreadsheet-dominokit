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
import walkingkooka.spreadsheet.dominokit.ui.Anchor;
import walkingkooka.spreadsheet.dominokit.ui.Component;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIds;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.tree.text.TextAlign;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A table that holds available patterns used to format values.
 */
final class SpreadsheetPatternComponentTableComponent implements Component<HTMLDivElement> {

    /**
     * Creates an empty {@link SpreadsheetPatternComponentTableComponent}.
     */
    static SpreadsheetPatternComponentTableComponent empty(final Consumer<String> setPatternText,
                                                           final SpreadsheetPatternComponentContext context) {
        return new SpreadsheetPatternComponentTableComponent(
                setPatternText,
                context
        );
    }

    private SpreadsheetPatternComponentTableComponent(final Consumer<String> setPatternText,
                                                      final SpreadsheetPatternComponentContext context) {
        final LocalListDataStore<SpreadsheetPatternComponentTableComponentRow> localListDataStore = new LocalListDataStore<>();
        this.table = new DataTable<>(
                this.tableConfig(
                        setPatternText,
                        context
                ),
                localListDataStore
        );
        this.dataStore = localListDataStore;
        this.table.headerElement().hide();

        this.card = Card.create()
                .appendChild(this.table);
    }

    private TableConfig<SpreadsheetPatternComponentTableComponentRow> tableConfig(final Consumer<String> setPatternText,
                                                                                  final SpreadsheetPatternComponentContext context) {
        return new TableConfig<SpreadsheetPatternComponentTableComponentRow>()
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
                                d -> this.patternAnchor(
                                        d.label(),
                                        d.pattern()
                                                .map(SpreadsheetPattern::text)
                                                .orElse(""),
                                        setPatternText,
                                        context
                                )
                        )
                ).addColumn(
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

    private static ColumnConfig<SpreadsheetPatternComponentTableComponentRow> columnConfig(final String columnName,
                                                                                           final TextAlign textAlign,
                                                                                           final Function<SpreadsheetPatternComponentTableComponentRow, Node> nodeMapper) {
        return ColumnConfig.<SpreadsheetPatternComponentTableComponentRow>create(columnName)
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
     * Creates an anchor which will appear in the pattern column, which when clicked updates the pattern text box.
     * The history token is not updated.
     */
    private HTMLAnchorElement patternAnchor(final String label,
                                            final String patternText,
                                            final Consumer<String> setPatternText,
                                            final SpreadsheetPatternComponentContext context) {
        return Anchor.empty()
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
                ).addClickAndKeydownEnterListener(
                        e ->
                        {
                            e.preventDefault();
                            setPatternText.accept(patternText);
                        }).element();
    }

    void refresh(final String patternText,
                 final SpreadsheetPatternComponentContext context) {
        final SpreadsheetPatternKind patternKind = context.patternKind();

        this.dataStore.setData(
                SpreadsheetPatternComponentTableComponentRowProvider.spreadsheetPatternKind(patternKind)
                        .apply(
                                patternText,
                                SpreadsheetPatternComponentTableComponentRowProviderContexts.basic(
                                        patternKind,
                                        context.spreadsheetFormatterContext(),
                                        context
                                )
                        )
        );
        this.table.load();
    }

    private final DataTable<SpreadsheetPatternComponentTableComponentRow> table;

    private final LocalListDataStore<SpreadsheetPatternComponentTableComponentRow> dataStore;

    @Override
    public HTMLDivElement element() {
        return this.card.element();
    }

    private final Card card;
}
