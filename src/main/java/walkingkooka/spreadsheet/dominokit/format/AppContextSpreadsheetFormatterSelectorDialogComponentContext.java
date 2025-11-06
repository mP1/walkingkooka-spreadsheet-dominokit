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

import walkingkooka.plugin.ProviderContext;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContextDelegator;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetFormatterFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.util.Throttler;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContextDelegator;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterProvider;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterProviderDelegator;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContextObjectPostProcessor;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContextPreProcessor;

import java.util.Locale;

/**
 * An base class capturing most of the requirements for {@link SpreadsheetFormatterSelectorDialogComponentContext}
 */
abstract class AppContextSpreadsheetFormatterSelectorDialogComponentContext implements SpreadsheetFormatterSelectorDialogComponentContext,
    DialogComponentContextDelegator,
    SpreadsheetFormatterContextDelegator,
    SpreadsheetFormatterProviderDelegator {

    AppContextSpreadsheetFormatterSelectorDialogComponentContext(final AppContext context) {
        super();

        this.throttler = Throttler.empty(Throttler.KEYBOARD_DELAY);
        this.context = context;
    }

    @Override
    public final String formatterTableHistoryTokenSave(final SpreadsheetFormatterSelector selector) {
        return selector.text();
    }

    // SpreadsheetFormatterContext......................................................................................

    @Override
    public final SpreadsheetFormatterContext spreadsheetFormatterContext() {
        return this.context;
    }

    @Override
    public Locale locale() {
        return this.context.locale();
    }

    @Override
    public SpreadsheetFormatterSelectorDialogComponentContext setObjectPostProcessor(final JsonNodeMarshallContextObjectPostProcessor processor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetFormatterSelectorDialogComponentContext setPreProcessor(final JsonNodeUnmarshallContextPreProcessor processor) {
        throw new UnsupportedOperationException();
    }

    // SpreadsheetFormatterProvider.....................................................................................

    @Override
    public final SpreadsheetFormatterProvider spreadsheetFormatterProvider() {
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

    /**
     * Used to throttle calls to /formatter/STAR/edit
     */
    final Throttler throttler;

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

    // DialogComponentContext...........................................................................................

    @Override
    public final DialogComponentContext dialogComponentContext() {
        return DialogComponentContexts.basic(this.context);
    }

    // HasProviderContext...............................................................................................

    @Override
    public final ProviderContext providerContext() {
        return this.context;
    }

    // Object..........................................................................................................

    @Override
    public final String toString() {
        return this.context.toString();
    }

    final AppContext context;
}
