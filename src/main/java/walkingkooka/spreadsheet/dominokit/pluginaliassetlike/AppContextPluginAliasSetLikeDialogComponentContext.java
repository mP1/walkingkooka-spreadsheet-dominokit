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
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContextDelegator;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetNameHistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.text.CaseKind;

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
        SpreadsheetDialogComponentContextDelegator {

    AppContextPluginAliasSetLikeDialogComponentContext(final AppContext context) {
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
    public final void loadProviderInfoSetLike() {
        this.loadPluginInfoSetLike0(
                this.historyToken()
                        .cast(SpreadsheetNameHistoryToken.class)
                        .id()
        );
    }

    abstract void loadPluginInfoSetLike0(final SpreadsheetId id);

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

    // focus............................................................................................................

    @Override
    public final void giveFocus(final Runnable focus) {
        this.context.giveFocus(focus);
    }

    // SpreadsheetDialogComponentContext................................................................................

    @Override
    public final SpreadsheetDialogComponentContext spreadsheetDialogComponentContext() {
        return this.context;
    }

    final AppContext context;

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return this.context.toString();
    }
}
