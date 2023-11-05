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
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;

import java.util.Objects;

/**
 * Unconditionally pushes the given {@link HistoryToken}.
 */
final class PushHistoryTokenSpreadsheetDeltaFetcherWatcher implements SpreadsheetDeltaFetcherWatcher,
        NopFetcherWatcher {

    /**
     * Factory that creates a new {@link PushHistoryTokenSpreadsheetDeltaFetcherWatcher}.
     */
    static PushHistoryTokenSpreadsheetDeltaFetcherWatcher with(final HistoryToken historyToken) {
        Objects.requireNonNull(historyToken, "historyToken");

        return new PushHistoryTokenSpreadsheetDeltaFetcherWatcher(historyToken);
    }

    private PushHistoryTokenSpreadsheetDeltaFetcherWatcher(final HistoryToken historyToken) {
        this.historyToken = historyToken;
    }

    @Override
    public void onSpreadsheetDelta(final SpreadsheetDelta delta,
                                   final AppContext context) {
        final HistoryToken historyToken = this.historyToken;
        context.debug("PushHistoryTokenSelectionSpreadsheetDeltaFetcherWatcher.onSpreadsheetDelta pushing " + historyToken);
        context.pushHistoryToken(historyToken);
    }

    private final HistoryToken historyToken;

    @Override
    public String toString() {
        return this.historyToken.toString();
    }
}
