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

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetSelectionHistoryToken;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;

import java.util.Optional;

/**
 * If a viewport selection is present then copy the received selection even if its now gone.
 */
final class PushHistoryTokenResponseSpreadsheetDeltaSelectionSpreadsheetDeltaFetcherWatcher implements SpreadsheetDeltaFetcherWatcher,
        NopFetcherWatcher {

    /**
     * Singleton
     */
    static PushHistoryTokenResponseSpreadsheetDeltaSelectionSpreadsheetDeltaFetcherWatcher INSTANCE = new PushHistoryTokenResponseSpreadsheetDeltaSelectionSpreadsheetDeltaFetcherWatcher();

    private PushHistoryTokenResponseSpreadsheetDeltaSelectionSpreadsheetDeltaFetcherWatcher() {
    }

    @Override
    public void onSpreadsheetDelta(final SpreadsheetDelta delta,
                                   final AppContext context) {
        final HistoryToken historyToken = context.historyToken();

        // if a selection is already present copy from the metadata
        if (historyToken instanceof SpreadsheetSelectionHistoryToken) {
            final Optional<SpreadsheetViewport> viewport = delta.viewport();
            if (viewport.isPresent()) {
                final HistoryToken withSelection = historyToken.setSelection(
                        viewport.get()
                                .selection()
                );

                if (false == historyToken.equals(withSelection)) {
                    context.debug("PushHistoryTokenResponseSpreadsheetDeltaSelectionSpreadsheetDeltaFetcherWatcher.onSpreadsheetDelta selection active, updating " + withSelection, delta);
                    context.pushHistoryToken(withSelection);
                }
            }
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName().toString();
    }
}
