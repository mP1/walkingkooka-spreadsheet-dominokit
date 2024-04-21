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
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetNameHistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.NopComponentLifecycleOpenGiveFocus;
import walkingkooka.spreadsheet.dominokit.ui.NopComponentLifecycleRefresh;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.ui.VisibleComponentLifecycle;

import java.util.Objects;
import java.util.Optional;

final class SpreadsheetToolbarComponentItemAnchorReload extends SpreadsheetToolbarComponentItemAnchor<SpreadsheetToolbarComponentItemAnchorReload>
        implements NopComponentLifecycleOpenGiveFocus,
        NopComponentLifecycleRefresh,
        VisibleComponentLifecycle<HTMLElement, SpreadsheetToolbarComponentItemAnchorReload> {

    static SpreadsheetToolbarComponentItemAnchorReload with(final HistoryTokenContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetToolbarComponentItemAnchorReload(
                context
        );
    }

    private SpreadsheetToolbarComponentItemAnchorReload(final HistoryTokenContext context) {
        super(
                SpreadsheetToolbarComponent.reloadId(),
                SpreadsheetIcons.reload(),
                "Reload",
                "Reload",
                context
        );
    }

    // SpreadsheetToolbarComponentItemLink............................................................................

    @Override //
    void onFocus(final Event event) {
        // do nothing, do not update history token.
    }

    // ComponentLifecycle...............................................................................................

    @Override
    public void refresh(final AppContext context) {
        this.anchor.setHistoryToken(
                Optional.of(
                        context.historyToken().setReload()
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
