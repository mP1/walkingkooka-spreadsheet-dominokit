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
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetNameHistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.NopRefreshComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetCellFind;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.ui.VisibleComponentLifecycle;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Objects;
import java.util.Optional;

final class SpreadsheetToolbarComponentItemButtonReload extends SpreadsheetToolbarComponentItemButton
        implements NopRefreshComponentLifecycle,
        VisibleComponentLifecycle<HTMLElement> {

    static SpreadsheetToolbarComponentItemButtonReload with(final HistoryTokenContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetToolbarComponentItemButtonReload(
                context
        );
    }

    private SpreadsheetToolbarComponentItemButtonReload(final HistoryTokenContext context) {
        super(
                SpreadsheetToolbarComponent.reloadId(),
                SpreadsheetIcons.reload(),
                "Reload",
                context
        );
    }

    // SpreadsheetToolbarComponentItemButton............................................................................

    @Override
    void onClick(final Event event) {
        final HistoryTokenContext context = this.context;

        final HistoryToken historyToken = context.historyToken();

        context.pushHistoryToken(
                historyToken.setReload()
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
        // dont care
    }

    // ComponentLifecycle...............................................................................................

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return false;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetNameHistoryToken;
    }
}
