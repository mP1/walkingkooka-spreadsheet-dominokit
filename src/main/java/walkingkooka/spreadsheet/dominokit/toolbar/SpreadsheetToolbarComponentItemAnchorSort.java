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

package walkingkooka.spreadsheet.dominokit.toolbar;

import elemental2.dom.Event;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.NopComponentLifecycleOpenGiveFocus;
import walkingkooka.spreadsheet.dominokit.ui.NopComponentLifecycleRefresh;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIcons;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Objects;
import java.util.Optional;

/**
 * Displays SORT in the toolbar. This will only be enabled when more than one cell, one column or one row is selected.
 */
final class SpreadsheetToolbarComponentItemAnchorSort extends SpreadsheetToolbarComponentItemAnchor<SpreadsheetToolbarComponentItemAnchorSort>
        implements NopComponentLifecycleOpenGiveFocus,
        NopComponentLifecycleRefresh {

    static SpreadsheetToolbarComponentItemAnchorSort with(final HistoryTokenContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetToolbarComponentItemAnchorSort(
                context
        );
    }

    private SpreadsheetToolbarComponentItemAnchorSort(final HistoryTokenContext context) {
        super(
                SpreadsheetToolbarComponent.findCellsId(),
                Optional.of(
                        SpreadsheetIcons.cellsFind()
                ),
                "Sort",
                "Sort cell(s), column(s), row(s)...",
                context
        );
    }

    // SpreadsheetToolbarComponentItemLink............................................................................

    @Override //
    void onFocus(final Event event) {
        // do nothing, do not update history token.
    }

    // ComponentLifecycle...............................................................................................

    // only match selections of more than one cell/column/row
    @Override
    public boolean isMatch(final HistoryToken token) {
        boolean match = false;

        if (token instanceof SpreadsheetAnchoredSelectionHistoryToken) {
            final SpreadsheetAnchoredSelectionHistoryToken anchored = token.cast(SpreadsheetAnchoredSelectionHistoryToken.class);
            final SpreadsheetSelection selection = anchored.anchoredSelection()
                    .selection();
            if (selection.count() > 1) {
                match = true;
            }
        }

        return match;
    }

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return false;
    }

    @Override
    public void refresh(final AppContext context) {
        final HistoryToken historyToken = context.historyToken();

        HistoryToken sortEdit = historyToken.setSortEdit("");
        if (historyToken.equals(sortEdit)) {
            sortEdit = null;
        }

        this.anchor.setHistoryToken(
                Optional.ofNullable(sortEdit)
        );
    }
}
