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

package walkingkooka.spreadsheet.dominokit.spreadsheet;

import walkingkooka.locale.HasLocale;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;

public final class SpreadsheetListComponentContexts implements PublicStaticHelper {

    /**
     * {@see BasicSpreadsheetListComponentContext}
     */
    public static SpreadsheetListComponentContext basic(final HistoryTokenContext historyTokenContext,
                                                        final SpreadsheetMetadataFetcherWatchers metadataFetcherWatchers,
                                                        final HasLocale hasLocale) {
        return BasicSpreadsheetListComponentContext.with(
                historyTokenContext,
                metadataFetcherWatchers,
                hasLocale
        );
    }

    /**
     * {@see FakeSpreadsheetListComponentContext}
     */
    public static SpreadsheetListComponentContext fake() {
        return new FakeSpreadsheetListComponentContext();
    }

    /**
     * Stop creation
     */
    private SpreadsheetListComponentContexts() {
        throw new UnsupportedOperationException();
    }
}
