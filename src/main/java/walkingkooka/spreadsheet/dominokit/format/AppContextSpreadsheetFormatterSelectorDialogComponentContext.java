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

import walkingkooka.convert.CanConvert;
import walkingkooka.environment.EnvironmentValueName;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.plugin.ProviderContextDelegator;
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
import walkingkooka.spreadsheet.format.SpreadsheetFormatterProvider;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterProviderDelegator;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContextPreProcessor;

import java.time.LocalDateTime;
import java.util.Locale;

/**
 * An base class capturing most of the requirements for {@link SpreadsheetFormatterSelectorDialogComponentContext}
 */
abstract class AppContextSpreadsheetFormatterSelectorDialogComponentContext implements SpreadsheetFormatterSelectorDialogComponentContext,
    DialogComponentContextDelegator,
    SpreadsheetFormatterContextDelegator,
    SpreadsheetFormatterProviderDelegator,
    ProviderContextDelegator {

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
    public SpreadsheetFormatterSelectorDialogComponentContext setPreProcessor(final JsonNodeUnmarshallContextPreProcessor processor) {
        throw new UnsupportedOperationException();
    }

    // SpreadsheetFormatterProvider.....................................................................................

    @Override
    public final SpreadsheetFormatterProvider spreadsheetFormatterProvider() {
        return this.context;
    }

    // ProviderContext..................................................................................................

    @Override
    public final CanConvert canConvert() {
        return this.context;
    }

    @Override
    public final ProviderContext providerContext() {
        return this.context;
    }

    @Override
    public final LocalDateTime now() {
        return this.context.now();
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
        return DialogComponentContexts.basic(
            this.context,
            this.context
        );
    }

    // EnvironmentContext...............................................................................................

    @Override
    public final <T> SpreadsheetFormatterSelectorDialogComponentContext setEnvironmentValue(final EnvironmentValueName<T> name,
                                                                                            final T value) {
        this.context.setEnvironmentValue(
            name,
            value
        );
        return this;
    }

    @Override
    public final SpreadsheetFormatterSelectorDialogComponentContext removeEnvironmentValue(final EnvironmentValueName<?> name) {
        this.context.removeEnvironmentValue(name);
        return this;
    }

    // Object..........................................................................................................

    @Override
    public final String toString() {
        return this.context.toString();
    }

    final AppContext context;
}
