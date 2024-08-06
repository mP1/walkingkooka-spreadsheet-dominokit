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

package walkingkooka.spreadsheet.dominokit.format;

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContextDelegator;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetIdHistoryToken;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContextDelegator;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetFormatterFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetFormatterFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.util.Throttler;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContextDelegator;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterProvider;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterProviderDelegator;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;

/**
 * An base class capturing most of the requirements for {@link SpreadsheetFormatterSelectorDialogComponentContext}
 */
abstract class SpreadsheetFormatterSelectorDialogComponentContextBasic implements SpreadsheetFormatterSelectorDialogComponentContext,
        HistoryTokenContextDelegator,
        LoggingContextDelegator,
        SpreadsheetFormatterContextDelegator,
        SpreadsheetFormatterProviderDelegator {

    SpreadsheetFormatterSelectorDialogComponentContextBasic(final AppContext context) {
        super();

        this.throttler = Throttler.empty(2000);
        this.context = context;
    }

    @Override
    public final String formatterTableHistoryTokenSave(final SpreadsheetFormatterSelector selector) {
        return selector.toString();
    }

    // SpreadsheetFormatterContext......................................................................................

    @Override
    public final SpreadsheetFormatterContext spreadsheetFormatterContext() {
        return this.context;
    }

    // SpreadsheetFormatterProvider.....................................................................................

    @Override
    public final SpreadsheetFormatterProvider spreadsheetFormatterProvider() {
        return this.context;
    }

    // HistoryTokenContext..............................................................................................

    @Override
    public final HistoryTokenContext historyTokenContext() {
        return this.context;
    }

    // misc.............................................................................................................

    @Override
    public final void giveFocus(final Runnable focus) {
        this.context.giveFocus(focus);
    }

    @Override
    public final Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.context.addSpreadsheetDeltaFetcherWatcher(watcher);
    }

    @Override
    public final void spreadsheetFormattersEdit(final String text) {
        this.throttler.add(
                () -> this.spreadsheetFormatterFetcher()
                        .edit(
                                context.historyToken()
                                        .cast(SpreadsheetIdHistoryToken.class)
                                        .id(), // id
                                text
                        )
        );
    }

    /**
     * Used to throttle calls to /formatter/STAR/edit
     */
    private final Throttler throttler;

    @Override
    public final SpreadsheetFormatterFetcher spreadsheetFormatterFetcher() {
        return this.context.spreadsheetFormatterFetcher();
    }

    @Override
    public final Runnable addSpreadsheetFormatterFetcherWatcher(final SpreadsheetFormatterFetcherWatcher watcher) {
        return this.context.addSpreadsheetFormatterFetcherWatcher(watcher);
    }

    @Override
    public final Runnable addSpreadsheetFormatterFetcherWatcherOnce(final SpreadsheetFormatterFetcherWatcher watcher) {
        return this.context.addSpreadsheetFormatterFetcherWatcherOnce(watcher);
    }

    @Override
    public final Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
        return this.context.addSpreadsheetMetadataFetcherWatcher(watcher);
    }

    // log..............................................................................................................

    @Override
    public LoggingContext loggingContext() {
        return this.context;
    }

    // Object..........................................................................................................

    @Override
    public final String toString() {
        return this.context.toString();
    }

    final AppContext context;
}
