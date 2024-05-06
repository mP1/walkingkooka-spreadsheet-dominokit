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

package walkingkooka.spreadsheet.dominokit.net;

import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;

import java.util.Optional;

/**
 * Pushes the viewport selection from a {@link SpreadsheetDelta} response if one is present.
 */
final class PushHistoryTokenViewportSelectionSpreadsheetDeltaFetcherWatcher implements SpreadsheetDeltaFetcherWatcher,
        NopFetcherWatcher,
        NopNoResponseWatcher {

    /**
     * Singleton
     */
    static final PushHistoryTokenViewportSelectionSpreadsheetDeltaFetcherWatcher INSTANCE = new PushHistoryTokenViewportSelectionSpreadsheetDeltaFetcherWatcher();

    private PushHistoryTokenViewportSelectionSpreadsheetDeltaFetcherWatcher() {
    }

    @Override
    public void onSpreadsheetDelta(final SpreadsheetId id,
                                   final SpreadsheetDelta delta,
                                   final AppContext context) {
        final Optional<SpreadsheetViewport> viewport = delta.viewport();
        if (viewport.isPresent()) {
            final HistoryToken historyToken = context.historyToken();

            final HistoryToken withSelection = historyToken.setAnchoredSelection(
                    viewport.get()
                            .anchoredSelection()
            );

            if (false == historyToken.equals(withSelection)) {
                context.debug(
                        this.getClass().getSimpleName() +
                                ".onSpreadsheetDelta selection active, updating " +
                                withSelection,
                        delta
                );
                context.pushHistoryToken(withSelection);
            }
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
