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

import walkingkooka.naming.Name;
import walkingkooka.plugin.PluginAliasLike;
import walkingkooka.plugin.PluginAliasSetLike;
import walkingkooka.plugin.PluginInfoLike;
import walkingkooka.plugin.PluginInfoSetLike;
import walkingkooka.plugin.PluginSelectorLike;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContextDelegator;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetNameHistoryToken;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContextDelegator;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.provider.SpreadsheetProvider;
import walkingkooka.text.CaseKind;

import java.util.Objects;

/**
 * Base class that captures a lot of common functionality for a {@link PluginAliasSetLikeDialogComponentContext}.
 */
abstract class PluginAliasSetLikeDialogComponentContextBasic<N extends Name & Comparable<N>,
        I extends PluginInfoLike<I, N>,
        IS extends PluginInfoSetLike<N, I, IS, S, A, AS>,
        S extends PluginSelectorLike<N>,
        A extends PluginAliasLike<N, S, A>,
        AS extends PluginAliasSetLike<N, I, IS, S, A, AS>> implements PluginAliasSetLikeDialogComponentContext<N, I, IS, S, A, AS>,
        HistoryTokenContextDelegator,
        LoggingContextDelegator {

    /**
     * {@see PluginAliasSetLikeDialogComponentContextBasicSpreadsheetExpressionFunctions}
     */
    static PluginAliasSetLikeDialogComponentContextBasicSpreadsheetExpressionFunctions expressionFunctions(final AppContext context) {
        return PluginAliasSetLikeDialogComponentContextBasicSpreadsheetExpressionFunctions.with(context);
    }

    PluginAliasSetLikeDialogComponentContextBasic(final AppContext context) {
        this.context = Objects.requireNonNull(context, "context");
    }

    // PluginAliasSetLikeDialogComponentContext..............................................................................

    @Override
    public final String dialogTitle() {
        return CaseKind.kebabToTitle(
                this.metadataPropertyName()
                        .text()
        );
    }

    @Override
    public final boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetMetadataPropertySelectHistoryToken &&
                token.cast(SpreadsheetMetadataPropertySelectHistoryToken.class)
                        .propertyName()
                        .equals(this.metadataPropertyName());
    }

    @Override
    public final Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
        return this.context.addSpreadsheetMetadataFetcherWatcher(watcher);
    }

    @Override
    public final void loadProviderAliasSetLike() {
        this.loadPluginAliasSetLike0(
                this.historyToken()
                        .cast(SpreadsheetNameHistoryToken.class)
                        .id()
        );
    }

    abstract void loadPluginAliasSetLike0(final SpreadsheetId id);

    @Override
    public final AS parseAliasSetLike(final String text) {
        return this.metadataPropertyName()
                .parseUrlFragmentSaveValue(text);
    }

    @Override
    public final AS metadataAliasSetLike() {
        return this.context.spreadsheetMetadata()
                .getIgnoringDefaults(this.metadataPropertyName())
                .orElse(this.emptyAliasSetLike());
    }

    abstract SpreadsheetMetadataPropertyName<AS> metadataPropertyName();

    @Override
    public final AS providerAliasSetLike() {
        return this.providerAliasSetLike0(
                this.context.systemSpreadsheetProvider()
        );
    }

    abstract AS providerAliasSetLike0(final SpreadsheetProvider spreadsheetProvider);

    // focus............................................................................................................

    @Override
    public final void giveFocus(final Runnable focus) {
        this.context.giveFocus(focus);
    }

    // HistoryTokenContext..............................................................................................

    @Override
    public final HistoryTokenContext historyTokenContext() {
        return this.context;
    }

    // LoggingContext...................................................................................................

    @Override
    public final LoggingContext loggingContext() {
        return this.context;
    }

    final AppContext context;
}
