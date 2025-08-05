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
import walkingkooka.spreadsheet.dominokit.NopComponentLifecycleOpenGiveFocus;
import walkingkooka.spreadsheet.dominokit.NopComponentLifecycleRefresh;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetNameHistoryToken;
import walkingkooka.spreadsheet.engine.SpreadsheetCellFindQuery;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

import java.util.Optional;

final class SpreadsheetToolbarComponentItemAnchorCellFind extends SpreadsheetToolbarComponentItemAnchor<SpreadsheetToolbarComponentItemAnchorCellFind>
    implements NopComponentLifecycleOpenGiveFocus,
    NopComponentLifecycleRefresh {

    static SpreadsheetToolbarComponentItemAnchorCellFind with(final SpreadsheetToolbarComponentContext context) {
        return new SpreadsheetToolbarComponentItemAnchorCellFind(context);
    }

    private SpreadsheetToolbarComponentItemAnchorCellFind(final SpreadsheetToolbarComponentContext context) {
        super(
            SpreadsheetToolbarComponent.findCellsId(),
            Optional.of(
                SpreadsheetIcons.cellsFind()
            ),
            "Find",
            "Find cells...",
            context
        );
    }

    // SpreadsheetToolbarComponentItemLink............................................................................

    @Override //
    void onFocus(final Event event) {
        // do nothing, do not update history token.
    }

    // HistoryTokenAwareComponentLifecycle..............................................................................

    @Override
    public void refresh(final RefreshContext context) {
        final HistoryToken historyToken = context.historyToken();

        AnchoredSpreadsheetSelection anchoredSpreadsheetSelection = historyToken.anchoredSelectionOrEmpty()
            .orElse(null);
        if (null != anchoredSpreadsheetSelection) {
            final SpreadsheetSelection selection = anchoredSpreadsheetSelection.selection();
            if (false == selection.isCellOrCellRange()) {
                anchoredSpreadsheetSelection = null;
            }
        }

        if (null == anchoredSpreadsheetSelection) {
            anchoredSpreadsheetSelection = SpreadsheetSelection.ALL_CELLS.setDefaultAnchor();
        }

        this.anchor.setHistoryToken(
            Optional.of(
                historyToken.setAnchoredSelection(
                    Optional.of(anchoredSpreadsheetSelection)
                ).setQuery(
                    SpreadsheetCellFindQuery.empty()
                )
            )
        );
    }

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return false;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetNameHistoryToken;
    }
}
