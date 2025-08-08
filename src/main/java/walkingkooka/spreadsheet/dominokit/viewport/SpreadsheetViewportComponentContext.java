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

package walkingkooka.spreadsheet.dominokit.viewport;

import walkingkooka.spreadsheet.compare.SpreadsheetComparatorProvider;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.cell.SpreadsheetCellLinksComponentContext;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetFormatterFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetFormatterFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetMetadataFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.recent.RecentValueSavesContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.meta.HasSpreadsheetMetadata;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelNameResolver;

public interface SpreadsheetViewportComponentContext extends HistoryContext,
    LoggingContext,
    HasSpreadsheetDeltaFetcher,
    HasSpreadsheetFormatterFetcher,
    HasSpreadsheetFormatterFetcherWatchers,
    HasSpreadsheetMetadata,
    HasSpreadsheetMetadataFetcherWatchers,
    HasSpreadsheetViewportCache,
    RefreshContext,
    SpreadsheetComparatorProvider,
    SpreadsheetLabelNameResolver,
    SpreadsheetViewportFormulaComponentContext,
    SpreadsheetCellLinksComponentContext,
    RecentValueSavesContext {
}
