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

package walkingkooka.spreadsheet.dominokit.ui.viewport;

import elemental2.dom.HTMLTableCellElement;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.elements.THElement;
import org.dominokit.domino.ui.utils.ElementsFactory;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * A TH holding a single COLUMN header.
 */
final class SpreadsheetViewportComponentTableCellSpreadsheetColumnHeader extends SpreadsheetViewportComponentTableCell implements IsElement<HTMLTableCellElement> {

    static SpreadsheetViewportComponentTableCellSpreadsheetColumnHeader empty(final SpreadsheetColumnReference column,
                                                                              final SpreadsheetViewportComponentTableContext context) {
        return new SpreadsheetViewportComponentTableCellSpreadsheetColumnHeader(
                column,
                context
        );
    }

    private SpreadsheetViewportComponentTableCellSpreadsheetColumnHeader(final SpreadsheetColumnReference column,
                                                                         final SpreadsheetViewportComponentTableContext context) {
        super();
        this.column = column;

        final String id = SpreadsheetViewportComponent.id(column);

        this.element = ElementsFactory.elements.th()
                .id(id)
                .style(
                        css(
                                SpreadsheetViewportComponentTableCell.HEADER_STYLE,
                                context.viewportCache()
                                        .columnWidth(column),
                                SpreadsheetViewportComponent.COLUMN_HEIGHT
                        )
                );

        this.element.appendChild(
                context.historyToken()
                        .clearAction()
                        .setAnchoredSelection(
                                Optional.of(
                                        column.setDefaultAnchor()
                                )
                        ).link(id)
                        .setTabIndex(0)
                        .addPushHistoryToken(
                                context
                        ).setTextContent(
                                column.toString()
                                        .toUpperCase()
                        ).element()
        );
    }

    @Override
    void refresh(final Predicate<SpreadsheetSelection> selected,
                 final SpreadsheetViewportComponentTableContext context) {
        this.element.setBackgroundColor(
                this.backgroundColor(
                        selected.test(this.column)
                )
        );
    }

    private final SpreadsheetColumnReference column;

    // IsElement........................................................................................................

    @Override
    public HTMLTableCellElement element() {
        return this.element.element();
    }

    private final THElement element;
}
