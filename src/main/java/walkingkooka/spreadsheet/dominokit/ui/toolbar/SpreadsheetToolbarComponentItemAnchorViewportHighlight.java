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
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellHighlightHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellHighlightSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.NopComponentLifecycleOpenGiveFocus;
import walkingkooka.spreadsheet.dominokit.ui.NopComponentLifecycleRefresh;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.ui.VisibleComponentLifecycle;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Objects;
import java.util.Optional;

final class SpreadsheetToolbarComponentItemAnchorViewportHighlight extends SpreadsheetToolbarComponentItemAnchor<SpreadsheetToolbarComponentItemAnchorViewportHighlight>
        implements NopComponentLifecycleOpenGiveFocus,
        NopComponentLifecycleRefresh,
        VisibleComponentLifecycle<HTMLElement, SpreadsheetToolbarComponentItemAnchorViewportHighlight> {

    static SpreadsheetToolbarComponentItemAnchorViewportHighlight with(final AppContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetToolbarComponentItemAnchorViewportHighlight(
                context
        );
    }

    private SpreadsheetToolbarComponentItemAnchorViewportHighlight(final AppContext context) {
        super(
                SpreadsheetToolbarComponent.highlightId(),
                SpreadsheetIcons.highlight(),
                "Highlight",
                "",
                context
        );
        this.context = context;

        this.refresh(context);
    }

    private final AppContext context;

    // SpreadsheetToolbarComponentItemLink............................................................................

    @Override //
    void onFocus(final Event event) {
        // do nothing, do not update history token.
    }

    // ComponentLifecycle...............................................................................................

    @Override
    public void refresh(final AppContext context) {
        final boolean enabled = context.isViewportHighlightEnabled();

        this.setTooltipText(
                enabled ?
                        "Disable cell highlighting" :
                        "Enable cell highlighting"
        );

        final boolean enable = false == context.isViewportHighlightEnabled();

        final HistoryToken historyToken = context.historyToken();

        // default to "*" if no current cells present.
        this.anchor.setChecked(enabled)
                .setHistoryToken(
                Optional.of(
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
                )
        );
    }

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
