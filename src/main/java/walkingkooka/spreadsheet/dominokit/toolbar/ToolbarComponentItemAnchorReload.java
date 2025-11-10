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

import java.util.Optional;

final class ToolbarComponentItemAnchorReload extends ToolbarComponentItemAnchor<ToolbarComponentItemAnchorReload>
    implements NopComponentLifecycleOpenGiveFocus,
    NopComponentLifecycleRefresh {

    static ToolbarComponentItemAnchorReload with(final ToolbarComponentContext context) {
        return new ToolbarComponentItemAnchorReload(context);
    }

    private ToolbarComponentItemAnchorReload(final ToolbarComponentContext context) {
        super(
            ToolbarComponent.RELOAD_ID,
            Optional.of(
                SpreadsheetIcons.reload()
            ),
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

    // HistoryTokenAwareComponentLifecycle..............................................................................

    @Override
    public void refresh(final RefreshContext context) {
        this.anchor.setHistoryToken(
            Optional.of(
                context.historyToken()
                    .reload()
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
