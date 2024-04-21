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

/**
 * A toolbar link which when clicked open the create label dialog.
 */
final class SpreadsheetToolbarComponentItemAnchorLabelCreate extends SpreadsheetToolbarComponentItemAnchor<SpreadsheetToolbarComponentItemAnchorLabelCreate>
        implements NopComponentLifecycleOpenGiveFocus,
        NopComponentLifecycleRefresh,
        VisibleComponentLifecycle<HTMLElement, SpreadsheetToolbarComponentItemAnchorLabelCreate> {

    static SpreadsheetToolbarComponentItemAnchorLabelCreate with(final HistoryTokenContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetToolbarComponentItemAnchorLabelCreate(
                context
        );
    }

    private SpreadsheetToolbarComponentItemAnchorLabelCreate(final HistoryTokenContext context) {
        super(
                SpreadsheetToolbarComponent.labelCreateId(),
                Optional.of(
                        SpreadsheetIcons.labelAdd()
                ),
                "Create Label",
                "Create Label",
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
        final HistoryToken historyToken = context.historyToken();

        this.anchor.setHistoryToken(
                Optional.of(
                        historyToken.setLabelName(
                                Optional.empty()
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
