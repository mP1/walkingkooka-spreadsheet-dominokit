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

import org.junit.jupiter.api.Test;
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

public final class AppContextDateTimeSymbolsDialogComponentContextSpreadsheetMetadataPropertyTest extends AppContextDateTimeSymbolsDialogComponentContextTestCase<AppContextDateTimeSymbolsDialogComponentContextSpreadsheetMetadataProperty> {

    @Test
    public void testIsMatchWhenSpreadsheetCellDateTimeSymbolsSaveHistoryToken() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.cellDateTimeSymbolsSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                Optional.empty()
            ),
            false
        );
    }

    @Test
    public void testIsMatchWhenSpreadsheetCellDateTimeSymbolsSelectHistoryToken() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.cellDateTimeSymbolsSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            false
        );
    }

    @Test
    public void testIsMatchWhenSpreadsheetCellSelectHistoryToken() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            false
        );
    }

    @Test
    public void testIsMatchWhenSpreadsheetMetadataPropertySelectHistoryTokenDateTimeSymbols() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.DATE_TIME_SYMBOLS
            ),
            true
        );
    }

    @Test
    public void testIsMatchWhenSpreadsheetMetadataPropertySelectHistoryTokenDecimalNumberSymbols() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.DECIMAL_NUMBER_SYMBOLS
            ),
            false
        );
    }

    // copyDateTimeSymbols..............................................................................................

    @Test
    public void testCopyDateTimeSymbols() {
        final AppContext appContext = new FakeAppContext() {

            @Override
            public SpreadsheetMetadata spreadsheetMetadata() {
                return METADATA_EN_AU.set(SpreadsheetMetadataPropertyName.SPREADSHEET_ID, SPREADSHEET_ID)
                    .set(SpreadsheetMetadataPropertyName.DATE_TIME_SYMBOLS, DATE_TIME_SYMBOLS);
            }
        };

        this.copyDateTimeSymbolsAndCheck(
            AppContextDateTimeSymbolsDialogComponentContextSpreadsheetMetadataProperty.with(appContext),
            Optional.empty()
        );
    }

    // load.............................................................................................................

    @Test
    public void testLoadDateTimeSymbols() {
        final Optional<DateTimeSymbols> dateTimeSymbols = Optional.of(DATE_TIME_SYMBOLS);

        final AppContext appContext = new FakeAppContext() {
            @Override
            public SpreadsheetViewportCache spreadsheetViewportCache() {
                return cache;
            }

            final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty(this);

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return null;
            }

            @Override
            public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
                return null;
            }

            @Override
            public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
                return null;
            }

            @Override
            public HistoryToken historyToken() {
                return HistoryToken.metadataPropertySelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    SpreadsheetMetadataPropertyName.DATE_TIME_SYMBOLS
                );
            }

            @Override
            public SpreadsheetMetadata spreadsheetMetadata() {
                return METADATA_EN_AU.set(SpreadsheetMetadataPropertyName.SPREADSHEET_ID, SPREADSHEET_ID)
                    .set(SpreadsheetMetadataPropertyName.DATE_TIME_SYMBOLS, DATE_TIME_SYMBOLS);
            }

            @Override
            public void debug(final Object... values) {
                // NOP
            }
        };

        appContext.spreadsheetViewportCache()
            .onSpreadsheetMetadata(
                METADATA_EN_AU.set(SpreadsheetMetadataPropertyName.SPREADSHEET_ID, SPREADSHEET_ID)
                    .set(SpreadsheetMetadataPropertyName.DATE_TIME_SYMBOLS, DATE_TIME_SYMBOLS)
            );

        this.loadDateTimeSymbolsAndCheck(
            AppContextDateTimeSymbolsDialogComponentContextSpreadsheetMetadataProperty.with(appContext),
            dateTimeSymbols
        );
    }

    @Override
    public AppContextDateTimeSymbolsDialogComponentContextSpreadsheetMetadataProperty createContext() {
        return AppContextDateTimeSymbolsDialogComponentContextSpreadsheetMetadataProperty.with(
            new FakeAppContext() {

            }
        );
    }

    // class............................................................................................................

    @Override
    public Class<AppContextDateTimeSymbolsDialogComponentContextSpreadsheetMetadataProperty> type() {
        return AppContextDateTimeSymbolsDialogComponentContextSpreadsheetMetadataProperty.class;
    }
}
