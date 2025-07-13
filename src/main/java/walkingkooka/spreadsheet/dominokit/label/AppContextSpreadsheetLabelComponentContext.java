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

package walkingkooka.spreadsheet.dominokit.label;

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchersDelegator;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetIdHistoryToken;

import java.util.Objects;
import java.util.OptionalInt;

final class AppContextSpreadsheetLabelComponentContext implements SpreadsheetLabelComponentContext,
    HasSpreadsheetDeltaFetcherWatchersDelegator {

    static AppContextSpreadsheetLabelComponentContext with(final AppContext context) {
        return new AppContextSpreadsheetLabelComponentContext(
            Objects.requireNonNull(context, "context")
        );
    }

    private AppContextSpreadsheetLabelComponentContext(final AppContext context) {
        this.context = context;
    }

    @Override
    public void findLabelByName(final String text,
                                final OptionalInt offset,
                                final OptionalInt count) {
        final AppContext context = this.context;

        context.spreadsheetDeltaFetcher()
            .getLabelMappingsFindByName(
                context.historyToken()
                    .cast(SpreadsheetIdHistoryToken.class)
                    .id(),
                text,
                offset,
                count
            );
    }

    // HasSpreadsheetDeltaFetcherWatchersDelegator......................................................................

    @Override
    public HasSpreadsheetDeltaFetcherWatchers hasSpreadsheetDeltaFetcherWatchers() {
        return this.context;
    }

    private final AppContext context;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.context.toString();
    }
}
