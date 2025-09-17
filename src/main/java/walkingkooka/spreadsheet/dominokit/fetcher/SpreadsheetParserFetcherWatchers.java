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
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserInfoSet;
import walkingkooka.spreadsheet.server.parser.SpreadsheetParserSelectorEdit;

public final class SpreadsheetParserFetcherWatchers extends FetcherWatchers<SpreadsheetParserFetcherWatcher>
    implements SpreadsheetParserFetcherWatcher {

    private SpreadsheetParserFetcherWatchers() {
        super();
    }

    public static SpreadsheetParserFetcherWatchers empty() {
        return new SpreadsheetParserFetcherWatchers();
    }

    @Override
    public void onSpreadsheetParserInfoSet(final SpreadsheetParserInfoSet infos,
                                           final AppContext context) {
        this.fire(
            SpreadsheetParserFetcherWatchersInfoSetEvent.with(
                infos,
                context
            )
        );
    }

    @Override
    public void onSpreadsheetParserSelectorEdit(final SpreadsheetId id,
                                                final SpreadsheetParserSelectorEdit edit,
                                                final AppContext context) {
        this.fire(
            SpreadsheetParserFetcherWatchersEvent.with(
                id,
                edit,
                context
            )
        );
    }
}
