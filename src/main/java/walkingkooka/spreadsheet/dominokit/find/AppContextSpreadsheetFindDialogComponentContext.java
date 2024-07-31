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

package walkingkooka.spreadsheet.dominokit.find;

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContextDelegator;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.parser.SpreadsheetParser;
import walkingkooka.spreadsheet.parser.SpreadsheetParserInfo;
import walkingkooka.spreadsheet.parser.SpreadsheetParserName;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelectorTextComponent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

final class AppContextSpreadsheetFindDialogComponentContext implements SpreadsheetFindDialogComponentContext,
        LoggingContextDelegator {

    static AppContextSpreadsheetFindDialogComponentContext with(final AppContext context) {
        return new AppContextSpreadsheetFindDialogComponentContext(context);
    }

    private AppContextSpreadsheetFindDialogComponentContext(final AppContext context) {
        this.context = context;
    }

    @Override
    public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
        return this.context.addHistoryTokenWatcher(watcher);
    }

    @Override
    public Runnable addHistoryTokenWatcherOnce(final HistoryTokenWatcher watcher) {
        return this.context.addHistoryTokenWatcherOnce(watcher);
    }

    @Override
    public HistoryToken historyToken() {
        return this.context.historyToken();
    }

    @Override
    public void pushHistoryToken(final HistoryToken token) {
        this.context.pushHistoryToken(token);
    }

    @Override
    public void fireCurrentHistoryToken() {
        this.context.fireCurrentHistoryToken();
    }

    // HasNow...........................................................................................................

    @Override
    public LocalDateTime now() {
        return this.context.now();
    }

    // HasSpreadsheetDeltaWatcher.......................................................................................

    @Override
    public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.context.addSpreadsheetDeltaFetcherWatcher(watcher);
    }

    @Override
    public Runnable addSpreadsheetDeltaFetcherWatcherOnce(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.context.addSpreadsheetDeltaFetcherWatcherOnce(watcher);
    }

    @Override
    public SpreadsheetDeltaFetcher spreadsheetDeltaFetcher() {
        return this.context.spreadsheetDeltaFetcher();
    }

    // HasSpreadsheetMetadata...........................................................................................

    @Override
    public SpreadsheetMetadata spreadsheetMetadata() {
        return this.context.spreadsheetMetadata();
    }

    // SpreadsheetParserProvider........................................................................................

    @Override
    public SpreadsheetParser spreadsheetParser(final SpreadsheetParserSelector selector) {
        return this.context.spreadsheetParser(selector);
    }

    @Override
    public SpreadsheetParser spreadsheetParser(final SpreadsheetParserName name,
                                               final List<?> values) {
        return this.context.spreadsheetParser(
                name,
                values
        );
    }

    @Override
    public Optional<SpreadsheetParserSelectorTextComponent> spreadsheetParserNextTextComponent(final SpreadsheetParserSelector selector) {
        return this.context.spreadsheetParserNextTextComponent(selector);
    }

    @Override
    public Optional<SpreadsheetFormatterSelector> spreadsheetFormatterSelector(final SpreadsheetParserSelector selector) {
        return this.context.spreadsheetFormatterSelector(selector);
    }

    @Override
    public Set<SpreadsheetParserInfo> spreadsheetParserInfos() {
        return this.context.spreadsheetParserInfos();
    }

    // LoggingContext...................................................................................................

    @Override
    public LoggingContext loggingContext() {
        return this.context;
    }

    private final AppContext context;

    @Override
    public String toString() {
        return this.context.toString();
    }
}
