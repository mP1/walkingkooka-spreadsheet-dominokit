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
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterSelectorEdit;

/**
 * The event payload used by {@link SpreadsheetFormatterFetcherWatchers}.
 */
final class SpreadsheetFormatterFetcherWatchersEditEvent extends FetcherWatchersEvent<SpreadsheetFormatterFetcherWatcher> {

    static SpreadsheetFormatterFetcherWatchersEditEvent with(final SpreadsheetId id,
                                                             final SpreadsheetFormatterSelectorEdit edit,
                                                             final AppContext context) {
        return new SpreadsheetFormatterFetcherWatchersEditEvent(
                id,
                edit,
                context
        );
    }

    private SpreadsheetFormatterFetcherWatchersEditEvent(final SpreadsheetId id,
                                                         final SpreadsheetFormatterSelectorEdit edit,
                                                         final AppContext context) {
        super(context);
        this.id = id;
        this.edit = edit;
    }

    @Override
    void fire(final SpreadsheetFormatterFetcherWatcher watcher) {
        watcher.onSpreadsheetFormatterSelectorEdit(
                this.id,
                this.edit,
                this.context
        );
    }

    private final SpreadsheetId id;

    private final SpreadsheetFormatterSelectorEdit edit;

    @Override
    public String toString() {
        return this.id + " " + this.edit + " " + this.context;
    }
}
