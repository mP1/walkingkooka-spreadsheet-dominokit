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
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetDominoKitColor;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;
import java.util.function.Predicate;

abstract class SpreadsheetViewportComponentTableCellHeader<T extends SpreadsheetSelection> extends SpreadsheetViewportComponentTableCell
        implements IsElement<HTMLTableCellElement> {

    SpreadsheetViewportComponentTableCellHeader(final String id,
                                                final String css,
                                                final T selection,
                                                final String text,
                                                final HistoryTokenContext context) {
        super();

        this.element = ElementsFactory.elements.th()
                .id(id)
                .style(css);

        this.element.appendChild(
                context.historyToken()
                        .clearAction()
                        .setAnchoredSelection(
                                Optional.of(
                                        selection.setDefaultAnchor()
                                )
                        ).link(id)
                        .setTabIndex(0)
                        .addPushHistoryToken(context)
                        .setTextContent(text)
                        .element()
        );

        this.selection = selection;
    }

    @Override //
    final void refresh(final Predicate<SpreadsheetSelection> selected,
                       final SpreadsheetViewportComponentTableContext context) {
        this.element.setBackgroundColor(
                selected.test(this.selection) ?
                        SpreadsheetDominoKitColor.VIEWPORT_HEADER_SELECTED_BACKGROUND_COLOR.toString() :
                        SpreadsheetDominoKitColor.VIEWPORT_HEADER_UNSELECTED_BACKGROUND_COLOR.toString()
        );
    }

    T selection;

    // IsElement........................................................................................................

    @Override
    public final HTMLTableCellElement element() {
        return this.element.element();
    }

    private final THElement element;
}
