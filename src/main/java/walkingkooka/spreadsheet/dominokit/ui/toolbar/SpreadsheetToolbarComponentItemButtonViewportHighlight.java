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

package walkingkooka.spreadsheet.dominokit.ui.toolbar;

import elemental2.dom.Event;
import elemental2.dom.HTMLElement;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellHighlightHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellHighlightSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.NopRefreshComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetDominoKitColor;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.ui.VisibleComponentLifecycle;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Objects;
import java.util.Optional;

final class SpreadsheetToolbarComponentItemButtonViewportHighlight extends SpreadsheetToolbarComponentItemButton
        implements NopRefreshComponentLifecycle,
        VisibleComponentLifecycle<HTMLElement> {

    static SpreadsheetToolbarComponentItemButtonViewportHighlight with(final AppContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetToolbarComponentItemButtonViewportHighlight(
                context
        );
    }

    private SpreadsheetToolbarComponentItemButtonViewportHighlight(final AppContext context) {
        super(
                SpreadsheetToolbarComponent.highlightId(),
                SpreadsheetIcons.highlight(),
                "",
                context
        );
        this.context = context;

        this.refreshButton(context);
    }

    private final AppContext context;

    // SpreadsheetToolbarComponentItemButton............................................................................

    @Override
    void onClick(final Event event) {
        final AppContext context = this.context;

        final boolean enable = false == context.isViewportHighlightEnabled();

        final HistoryToken historyToken = context.historyToken();

        // default to "*" if no current cells present.
        context.pushHistoryToken(
                historyToken.setAnchoredSelection(
                                Optional.of(
                                        historyToken.anchoredSelectionOrEmpty()
                                                .orElse(
                                                        SpreadsheetSelection.ALL_CELLS.setDefaultAnchor()
                                                )
                                )
                        ).setHighlight()
                        .setSave(
                                // https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/1556
                                enable ?
                                        SpreadsheetCellHighlightHistoryToken.ENABLE :
                                        SpreadsheetCellHighlightHistoryToken.DISABLE
                        )
        );
        context.debug(this.getClass().getSimpleName() + ".onClick highlight save " + enable + " " + historyToken.setAnchoredSelection(
                                Optional.of(
                                        historyToken.anchoredSelectionOrEmpty()
                                                .orElse(
                                                        SpreadsheetSelection.ALL_CELLS.setDefaultAnchor()
                                                )
                                )
                        ).setHighlight()
                        .setSave(
                                // https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/1556
                                enable ?
                                        SpreadsheetCellHighlightSaveHistoryToken.ENABLE :
                                        SpreadsheetCellHighlightSaveHistoryToken.DISABLE
                        )
        );
    }

    @Override //
    void onFocus(final Event event) {
        // do nothing, do not update history token.
    }

    @Override //
    void onToolbarRefreshBegin() {
        // dont care
    }

    @Override //
    void onToolbarRefreshSelectedCell(final SpreadsheetCell cell,
                                      final AppContext context) {
        // dont care
    }

    @Override //
    void onToolbarRefreshEnd(final int cellPresentCount,
                             final AppContext context) {
        this.refreshButton(context);
    }

    private void refreshButton(final AppContext context) {
        final boolean enabled = context.isViewportHighlightEnabled();

        this.setButtonSelected(
                enabled, // selected
                SpreadsheetDominoKitColor.HIGHLIGHT_COLOR
        );
        this.setTooltipText(
                enabled ?
                        "Disable cell highlighting" :
                        "Enable cell highlighting"
        );
    }

    // ComponentLifecycle...............................................................................................

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        boolean ignore = false;

        if(token instanceof SpreadsheetCellHighlightSaveHistoryToken) {
            final SpreadsheetCellHighlightSaveHistoryToken save = token.cast(SpreadsheetCellHighlightSaveHistoryToken.class);
            ignore = this.context.isViewportHighlightEnabled() == save.value();
        }

        return ignore;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return true; // always show
    }
}
