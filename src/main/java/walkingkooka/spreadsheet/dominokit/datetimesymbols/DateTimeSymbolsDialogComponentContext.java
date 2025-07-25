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

package walkingkooka.spreadsheet.dominokit.datetimesymbols;

import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcher;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContext;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.label.SpreadsheetLabelMappingDialogComponent;

import java.util.Optional;

/**
 * The {@link walkingkooka.Context} accompanying a {@link SpreadsheetLabelMappingDialogComponent}.
 */
public interface DateTimeSymbolsDialogComponentContext extends
    SpreadsheetDialogComponentContext,
    ComponentLifecycleMatcher {

    /**
     * A label which is used for the title of the dialog box.
     */
    String dialogTitle();

    /**
     * Used by "Copy Default" link as the source value.
     */
    Optional<DateTimeSymbols> copyDateTimeSymbols();

    /**
     * Gets the current {@link DateTimeSymbols}
     */
    Optional<DateTimeSymbols> loadDateTimeSymbols();

    /**
     * Saves the given value, preparing the {@link walkingkooka.spreadsheet.dominokit.history.HistoryToken}
     */
    void save(final Optional<DateTimeSymbols> symbols);

    /**
     * Adds a {@link SpreadsheetDeltaFetcherWatcher}.
     */
    Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher);

    /**
     * Adds a {@link SpreadsheetMetadataFetcherWatcher}.
     */
    Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher);
}
