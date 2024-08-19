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
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterSelectorMenu;

/**
 * The event payload used by {@link SpreadsheetFormatterFetcherWatchers}.
 */
final class SpreadsheetFormatterFetcherWatchersMenuEvent extends FetcherWatchersEvent<SpreadsheetFormatterFetcherWatcher> {

    static SpreadsheetFormatterFetcherWatchersMenuEvent with(final SpreadsheetId id,
                                                             final SpreadsheetFormatterSelectorMenu menu,
                                                             final AppContext context) {
        return new SpreadsheetFormatterFetcherWatchersMenuEvent(
                id,
                menu,
                context
        );
    }

    private SpreadsheetFormatterFetcherWatchersMenuEvent(final SpreadsheetId id,
                                                         final SpreadsheetFormatterSelectorMenu menu,
                                                         final AppContext context) {
        super(context);
        this.id = id;
        this.menu = menu;
    }

    @Override
    void fire(final SpreadsheetFormatterFetcherWatcher watcher) {
        watcher.onSpreadsheetFormatterSelectorMenu(
                this.id,
                this.menu,
                this.context
        );
    }

    private final SpreadsheetId id;

    private final SpreadsheetFormatterSelectorMenu menu;

    @Override
    public String toString() {
        return this.id + " " + this.menu + " " + this.context;
    }
}
