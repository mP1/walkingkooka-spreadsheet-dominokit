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

package walkingkooka.spreadsheet.dominokit.pluginaliassetlike;

import walkingkooka.currency.CurrencyContexts;
import walkingkooka.naming.Name;
import walkingkooka.plugin.PluginAliasLike;
import walkingkooka.plugin.PluginAliasSetLike;
import walkingkooka.plugin.PluginInfoLike;
import walkingkooka.plugin.PluginInfoSetLike;
import walkingkooka.plugin.PluginSelectorLike;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContextDelegator;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySelectHistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Objects;

/**
 * Base class that captures a lot of common functionality for a {@link PluginAliasSetLikeDialogComponentContext}.
 */
abstract class AppContextPluginAliasSetLikeDialogComponentContext<N extends Name & Comparable<N>,
    I extends PluginInfoLike<I, N>,
    IS extends PluginInfoSetLike<N, I, IS, S, A, AS>,
    S extends PluginSelectorLike<N>,
    A extends PluginAliasLike<N, S, A>,
    AS extends PluginAliasSetLike<N, I, IS, S, A, AS>> implements PluginAliasSetLikeDialogComponentContext<N, I, IS, S, A, AS>,
    DialogComponentContextDelegator {

    AppContextPluginAliasSetLikeDialogComponentContext(final AppContext context) {
        this.context = Objects.requireNonNull(context, "context");
    }

    // ComponentLifecycleMatcher........................................................................................

    @Override
    public final boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetMetadataPropertySaveHistoryToken;
    }

    // PluginAliasSetLikeDialogComponentContext..............................................................................

    @Override
    public final String dialogTitle() {
        return this.spreadsheetMetadataPropertyNameDialogTitle(this.metadataPropertyName());
    }

    @Override
    public final boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetMetadataPropertySelectHistoryToken &&
            this.metadataPropertyName()
                .equals(
                    token.metadataPropertyName().orElse(null)
                );
    }

    @Override
    public final Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
        return this.context.addSpreadsheetMetadataFetcherWatcher(watcher);
    }

    @Override
    public final AS parseAliasSetLike(final String text) {
        return this.metadataPropertyName()
            .parseUrlFragmentSaveValue(
                text,
                CurrencyContexts.fake()
                    .setLocaleContext(this.context)
            );
    }

    @Override
    public final AS metadataAliasSetLike() {
        return this.context.spreadsheetMetadata()
            .getIgnoringDefaults(this.metadataPropertyName())
            .orElse(this.emptyAliasSetLike());
    }

    abstract SpreadsheetMetadataPropertyName<AS> metadataPropertyName();

    // focus............................................................................................................

    @Override
    public final void giveFocus(final Runnable focus) {
        this.context.giveFocus(focus);
    }

    // DialogComponentContext...........................................................................................

    @Override
    public final DialogComponentContext dialogComponentContext() {
        return DialogComponentContexts.basic(this.context);
    }

    final AppContext context;

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return this.context.toString();
    }
}
