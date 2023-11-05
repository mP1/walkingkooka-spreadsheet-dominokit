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
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;

import java.util.Objects;
import java.util.Optional;

/**
 * When the {@link SpreadsheetDeltaFetcherWatcher#onSpreadsheetDelta(SpreadsheetDelta, AppContext)} updates the history token with {@link AnchoredSpreadsheetSelection}
 */
final class PushHistoryTokenSelectionSpreadsheetDeltaFetcherWatcher implements SpreadsheetDeltaFetcherWatcher,
        NopFetcherWatcher {

    /**
     * Factory that creates a new {@link PushHistoryTokenSelectionSpreadsheetDeltaFetcherWatcher}.
     */
    static PushHistoryTokenSelectionSpreadsheetDeltaFetcherWatcher with(final Optional<AnchoredSpreadsheetSelection> selection) {
        Objects.requireNonNull(selection, "selection");

        return new PushHistoryTokenSelectionSpreadsheetDeltaFetcherWatcher(selection);
    }

    private PushHistoryTokenSelectionSpreadsheetDeltaFetcherWatcher(final Optional<AnchoredSpreadsheetSelection> selection) {
        this.selection = selection;
    }

    @Override
    public void onSpreadsheetDelta(final SpreadsheetDelta delta,
                                   final AppContext context) {
        final HistoryToken historyToken = context.historyToken();

        // if a selection is already present copy from the metadata
        if (historyToken instanceof SpreadsheetSelectionHistoryToken) {
            final Optional<AnchoredSpreadsheetSelection> current = historyToken.selectionOrEmpty();
            final Optional<AnchoredSpreadsheetSelection> selection = this.selection;

            if (false == current.equals(selection)) {
                context.debug("PushHistoryTokenSelectionSpreadsheetDeltaFetcherWatcher.onSpreadsheetDelta replaced " + historyToken + " with " + selection);
                context.pushHistoryToken(
                        historyToken.setSelection(selection)
                );
            }
        }
    }

    private final Optional<AnchoredSpreadsheetSelection> selection;

    @Override
    public String toString() {
        return this.selection.toString();
    }
}
