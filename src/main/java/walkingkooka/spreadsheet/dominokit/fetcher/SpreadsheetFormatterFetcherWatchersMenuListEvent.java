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
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterSelectorMenuList;

/**
 * The event payload used by {@link SpreadsheetFormatterFetcherWatchers} when it receives a {@link SpreadsheetFormatterSelectorMenuList}.
 */
final class SpreadsheetFormatterFetcherWatchersMenuListEvent extends FetcherWatchersEvent<SpreadsheetFormatterFetcherWatcher> {

    static SpreadsheetFormatterFetcherWatchersMenuListEvent with(final SpreadsheetId id,
                                                                 final SpreadsheetExpressionReference cellOrLabel,
                                                                 final SpreadsheetFormatterSelectorMenuList menuList,
                                                                 final AppContext context) {
        return new SpreadsheetFormatterFetcherWatchersMenuListEvent(
            id,
            cellOrLabel,
            menuList,
            context
        );
    }

    private SpreadsheetFormatterFetcherWatchersMenuListEvent(final SpreadsheetId id,
                                                             final SpreadsheetExpressionReference cellOrLabel,
                                                             final SpreadsheetFormatterSelectorMenuList menuList,
                                                             final AppContext context) {
        super(context);
        this.id = id;
        this.cellOrLabel = cellOrLabel;
        this.menuList = menuList;
    }

    @Override
    void fire(final SpreadsheetFormatterFetcherWatcher watcher) {
        watcher.onSpreadsheetFormatterSelectorMenuList(
            this.id,
            this.cellOrLabel,
            this.menuList,
            this.context
        );
    }

    private final SpreadsheetId id;

    private final SpreadsheetExpressionReference cellOrLabel;

    private final SpreadsheetFormatterSelectorMenuList menuList;

    @Override
    public String toString() {
        return this.id + " " + this.cellOrLabel + " " + this.menuList;
    }
}
