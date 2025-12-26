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

import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterInfoSet;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterMenuList;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterSelectorEdit;

import java.util.Optional;

public final class SpreadsheetFormatterFetcherWatchers extends FetcherWatchers<SpreadsheetFormatterFetcherWatcher>
    implements SpreadsheetFormatterFetcherWatcher {

    public static SpreadsheetFormatterFetcherWatchers empty() {
        return new SpreadsheetFormatterFetcherWatchers();
    }

    private SpreadsheetFormatterFetcherWatchers() {
        super();
    }

    @Override
    public void onSpreadsheetFormatterInfoSet(final SpreadsheetFormatterInfoSet infos) {
        this.fire(
            SpreadsheetFormatterFetcherWatchersInfoSetEvent.with(infos)
        );
    }

    @Override
    public void onSpreadsheetFormatterSelectorEdit(final SpreadsheetId id,
                                                   final Optional<SpreadsheetExpressionReference> cellOrLabel,
                                                   final SpreadsheetFormatterSelectorEdit edit) {
        this.fire(
            SpreadsheetFormatterFetcherWatchersEditEvent.with(
                id,
                cellOrLabel,
                edit
            )
        );
    }

    @Override
    public void onSpreadsheetFormatterMenuList(final SpreadsheetId id,
                                               final SpreadsheetExpressionReference cellOrLabel,
                                               final SpreadsheetFormatterMenuList menu) {
        this.fire(
            SpreadsheetFormatterFetcherWatchersMenuListEvent.with(
                id,
                cellOrLabel,
                menu
            )
        );
    }
}
