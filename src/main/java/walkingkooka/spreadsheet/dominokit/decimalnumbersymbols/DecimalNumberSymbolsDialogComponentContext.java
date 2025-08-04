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

package walkingkooka.spreadsheet.dominokit.decimalnumbersymbols;

import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcher;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContext;
import walkingkooka.spreadsheet.dominokit.fetcher.DecimalNumberSymbolsFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;

import java.util.Optional;

/**
 * The {@link walkingkooka.Context} accompanying a {@link DecimalNumberSymbolsDialogComponent}.
 */
public interface DecimalNumberSymbolsDialogComponentContext extends
    DialogComponentContext,
    ComponentLifecycleMatcher {

    /**
     * An alternative {@link DecimalNumberSymbols} to copy to the dialog being edited.
     */
    Optional<DecimalNumberSymbols> copyDecimalNumberSymbols();

    /**
     * Calls the server to find {@link DecimalNumberSymbols} for the locale startsWith parameter
     */
    void findDecimalNumberSymbolsWithLocaleStartsWith(final String startsWith);

    /**
     * Gets the current {@link DecimalNumberSymbols}
     */
    Optional<DecimalNumberSymbols> loadDecimalNumberSymbols();

    /**
     * Saves the given value, preparing the {@link walkingkooka.spreadsheet.dominokit.history.HistoryToken}
     */
    void save(final Optional<DecimalNumberSymbols> symbols);

    /**
     * Adds a {@link DecimalNumberSymbolsFetcherWatcher}
     */
    Runnable addDecimalNumberSymbolsFetcherWatcher(final DecimalNumberSymbolsFetcherWatcher watcher);

    /**
     * Adds a {@link SpreadsheetDeltaFetcherWatcher}.
     */
    Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher);

    /**
     * Adds a {@link SpreadsheetMetadataFetcherWatcher}.
     */
    Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher);
}
