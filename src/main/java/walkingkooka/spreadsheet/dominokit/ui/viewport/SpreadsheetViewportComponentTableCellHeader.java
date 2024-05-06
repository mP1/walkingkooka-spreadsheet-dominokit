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
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetDominoKitColor;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Base class for any of the header UI components belonging to the {@link SpreadsheetViewportComponent} TABLE.
 */
abstract class SpreadsheetViewportComponentTableCellHeader<T extends SpreadsheetSelection> extends SpreadsheetViewportComponentTableCell
        implements IsElement<HTMLTableCellElement> {

    SpreadsheetViewportComponentTableCellHeader(final String id,
                                                final String css,
                                                final T selection,
                                                final String text,
                                                final HistoryTokenContext context) {
        super();

        final HistoryTokenAnchorComponent anchor = context.historyToken()
                .setAnchoredSelection(
                        Optional.of(
                                selection.setDefaultAnchor()
                        )
                ).link(id)
                .setTabIndex(0)
                .addPushHistoryToken(context)
                .setTextContent(text);
        this.anchor = anchor;

        final THElement element = ElementsFactory.elements.th()
                .id(id)
                .style(css);

        element.appendChild(
                anchor.element()
        );
        this.element = element;

        this.selection = selection;
        this.extended = null;
    }

    @Override //
    final void setIdAndName(final SpreadsheetId id,
                            final SpreadsheetName name) {
        final HistoryTokenAnchorComponent anchor = this.anchor;
        anchor.setHistoryToken(
                anchor.historyToken()
                        .map(t -> t.setIdAndName(id, name))
        );
    }

    final void setAnchoredSpreadsheetSelection(final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection) {
        this.anchor.historyToken()
                .ifPresent(
                        h -> this.setHistoryToken(
                                h.setAnchoredSelection(
                                        Optional.of(anchoredSpreadsheetSelection)
                                )
                        )
                );
    }

    final void setHistoryToken(final HistoryToken historyToken) {
        this.anchor.setHistoryToken(
                Optional.of(historyToken)
        );
    }

    private final HistoryTokenAnchorComponent anchor;

    @Override //
    final void refresh(final Predicate<SpreadsheetSelection> selected,
                       final SpreadsheetViewportComponentTableContext context) {
        this.element.setBackgroundColor(
                selected.test(this.selection) ?
                        SpreadsheetDominoKitColor.VIEWPORT_HEADER_SELECTED_BACKGROUND_COLOR.toString() :
                        SpreadsheetDominoKitColor.VIEWPORT_HEADER_UNSELECTED_BACKGROUND_COLOR.toString()
        );

        final Boolean extended = context.isShiftKeyDown();
        if (false == extended.equals(this.extended)) {
            if (extended) {
                this.refreshExtendLink(context);
            } else {
                this.refreshNonExtendLink(context);
            }
            this.extended = extended;
        }
    }

    /**
     * Sub-classes should refresh their links now because the extended flag became true.
     */
    abstract void refreshNonExtendLink(final SpreadsheetViewportComponentTableContext context);

    /**
     * Sub-classes should refresh their links now because the extended flag became true.
     */
    abstract void refreshExtendLink(final SpreadsheetViewportComponentTableContext context);

    /**
     * The {@link SpreadsheetSelection} for this TD.s
     */
    final T selection;

    /**
     * When true indicates that the extend link is shown.
     */
    Boolean extended;

    // IsElement........................................................................................................

    @Override
    public final HTMLTableCellElement element() {
        return this.element.element();
    }

    private final THElement element;


    // Object...........................................................................................................

    @Override
    public final String toString() {
        return this.anchor.toString();
    }
}
