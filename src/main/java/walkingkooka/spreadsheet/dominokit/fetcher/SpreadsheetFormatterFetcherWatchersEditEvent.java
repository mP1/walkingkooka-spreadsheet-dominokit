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

package walkingkooka.spreadsheet.dominokit.fetcher;

import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterSelectorEdit;

import java.util.Optional;

/**
 * The event payload used by {@link SpreadsheetFormatterFetcherWatchers}.
 */
final class SpreadsheetFormatterFetcherWatchersEditEvent extends FetcherWatchersEvent<SpreadsheetFormatterFetcherWatcher> {

    static SpreadsheetFormatterFetcherWatchersEditEvent with(final SpreadsheetId id,
                                                             final Optional<SpreadsheetExpressionReference> cellOrLabel,
                                                             final SpreadsheetFormatterSelectorEdit edit) {
        return new SpreadsheetFormatterFetcherWatchersEditEvent(
            id,
            cellOrLabel,
            edit
        );
    }

    private SpreadsheetFormatterFetcherWatchersEditEvent(final SpreadsheetId id,
                                                         final Optional<SpreadsheetExpressionReference> cellOrLabel,
                                                         final SpreadsheetFormatterSelectorEdit edit) {
        super();
        this.id = id;
        this.cellOrLabel = cellOrLabel;
        this.edit = edit;
    }

    @Override
    void fire(final SpreadsheetFormatterFetcherWatcher watcher) {
        watcher.onSpreadsheetFormatterSelectorEdit(
            this.id,
            this.cellOrLabel,
            this.edit
        );
    }

    private final SpreadsheetId id;

    private final Optional<SpreadsheetExpressionReference> cellOrLabel;

    private final SpreadsheetFormatterSelectorEdit edit;

    @Override
    public String toString() {
        return this.id + " " + this.cellOrLabel + " " + this.edit;
    }
}
