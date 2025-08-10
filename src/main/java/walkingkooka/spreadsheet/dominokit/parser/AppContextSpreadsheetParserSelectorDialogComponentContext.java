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

package walkingkooka.spreadsheet.dominokit.parser;

import walkingkooka.InvalidCharacterException;
import walkingkooka.convert.CanConvert;
import walkingkooka.environment.EnvironmentValueName;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.plugin.ProviderContextDelegator;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContextDelegator;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetParserFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetParserFetcherWatchersDelegator;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetParserFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetIdHistoryToken;
import walkingkooka.spreadsheet.dominokit.util.Throttler;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContextDelegator;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterProvider;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterProviderDelegator;
import walkingkooka.spreadsheet.parser.SpreadsheetParserProvider;
import walkingkooka.spreadsheet.parser.SpreadsheetParserProviderDelegator;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContextPreProcessor;

import java.time.LocalDateTime;

/**
 * A mostly complete {@link SpreadsheetParserSelectorDialogComponent}.
 */
abstract class AppContextSpreadsheetParserSelectorDialogComponentContext implements SpreadsheetParserSelectorDialogComponentContext,
    DialogComponentContextDelegator,
    SpreadsheetFormatterContextDelegator,
    SpreadsheetFormatterProviderDelegator,
    SpreadsheetParserProviderDelegator,
    HasSpreadsheetParserFetcherWatchersDelegator,
    ProviderContextDelegator {

    AppContextSpreadsheetParserSelectorDialogComponentContext(final AppContext context) {
        super();

        this.throttler = Throttler.empty(Throttler.KEYBOARD_DELAY);
        this.context = context;
    }

    // SpreadsheetFormatterContext......................................................................................

    @Override
    public final SpreadsheetFormatterContext spreadsheetFormatterContext() {
        return this.context;
    }

    @Override
    public SpreadsheetParserSelectorDialogComponentContext setPreProcessor(final JsonNodeUnmarshallContextPreProcessor processor) {
        throw new UnsupportedOperationException();
    }

    // SpreadsheetParserContext.........................................................................................

    @Override
    public InvalidCharacterException invalidCharacterException(final Parser<?> parser,
                                                               final TextCursor cursor) {
        return this.context.invalidCharacterException(
            parser,
            cursor
        );
    }

    @Override
    public char valueSeparator() {
        return this.context.valueSeparator();
    }

    // SpreadsheetFormatterProvider........................................................................................

    @Override
    public final SpreadsheetFormatterProvider spreadsheetFormatterProvider() {
        return this.context;
    }

    // SpreadsheetParserProvider........................................................................................

    @Override
    public final SpreadsheetParserProvider spreadsheetParserProvider() {
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
    public final Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
        return this.context.addSpreadsheetMetadataFetcherWatcher(watcher);
    }

    // SpreadsheetParserFetcher.........................................................................................

    @Override
    public final Runnable addSpreadsheetParserFetcherWatcher(final SpreadsheetParserFetcherWatcher watcher) {
        return this.context.addSpreadsheetParserFetcherWatcher(watcher);
    }

    @Override
    public final Runnable addSpreadsheetParserFetcherWatcherOnce(final SpreadsheetParserFetcherWatcher watcher) {
        return this.context.addSpreadsheetParserFetcherWatcherOnce(watcher);
    }

    @Override
    public final HasSpreadsheetParserFetcherWatchers hasSpreadsheetParserFetcherWatchers() {
        return this.context;
    }

    @Override
    public final void loadSpreadsheetParsersEdit(final String text) {
        this.throttler.add(
            () -> this.context.spreadsheetParserFetcher()
                .getEdit(
                    this.context.historyToken()
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

    // ProviderContext..................................................................................................

    @Override
    public final CanConvert canConvert() {
        return this.context;
    }

    @Override
    public AppContextSpreadsheetParserSelectorDialogComponentContext removeEnvironmentValue(final EnvironmentValueName<?> name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final ProviderContext providerContext() {
        return this.context;
    }

    @Override
    public final LocalDateTime now() {
        return this.context.now();
    }

    // DialogComponentContext...........................................................................................

    @Override
    public final DialogComponentContext dialogComponentContext() {
        return DialogComponentContexts.basic(
            this.context,
            this.context
        );
    }

    // Object..........................................................................................................

    @Override
    public final String toString() {
        return this.context.toString();
    }

    final AppContext context;
}
