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

package walkingkooka.spreadsheet.dominokit.viewport;

import elemental2.dom.HTMLTableCellElement;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.dom.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.dom.ThComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Base class for any of the header UI components belonging to the {@link SpreadsheetViewportComponent} TABLE.
 */
abstract class SpreadsheetViewportComponentTableCellHeader<S extends SpreadsheetSelection, C extends SpreadsheetViewportComponentTableCellHeader<S, C>> extends SpreadsheetViewportComponentTableCell<HTMLTableCellElement, C> {

    SpreadsheetViewportComponentTableCellHeader(final String id,
                                                final S selection,
                                                final String text,
                                                final SpreadsheetViewportComponentTableContext context) {
        super();

        final HistoryTokenAnchorComponent anchor = context.historyToken()
            .setSelection(
                Optional.of(selection)
            ).link(id)
            .setTabIndex(0)
            .addPushHistoryToken(context)
            .setTextContent(text);
        this.anchor = anchor;

        this.th = HtmlElementComponent.th()
            .setId(id)
            .appendChild(anchor);

        this.selection = selection;
        this.extended = null;

        this.refresh(context);
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
    final void refresh(final Predicate<SpreadsheetSelection> selectionTester,
                       final SpreadsheetViewportComponentTableContext context) {
        if(context.shouldShowHeaders()) {
            final SpreadsheetSelection selection = this.selection;

            this.th.setCssText(
                setWidthAndHeight(
                    (SpreadsheetSelection.ALL_CELLS.equals(selection) ?
                        selectionTester.equals(selection) :
                        selectionTester.test(selection)) ?
                        this.selectedTextStyle(context) :
                        this.unselectedTextStyle(context),
                    context
                )
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
    final S selection;

    /**
     * When true indicates that the extend link is shown.
     */
    Boolean extended;

    // HtmlComponentDelegator...........................................................................................

    @Override
    public final HtmlElementComponent<HTMLTableCellElement, ?> htmlComponent() {
        return this.th;
    }

    private final ThComponent th;

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return this.anchor.toString();
    }
}
