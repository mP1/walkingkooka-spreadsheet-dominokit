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
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcher;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContext;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.focus.CanGiveFocus;
import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBoxWrapper;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;

import java.util.function.Consumer;

/**
 * A {@link walkingkooka.Context} tht accompanies a {@link PluginAliasSetLikeDialogComponent} providing custom values and functionality.
 */
public interface PluginAliasSetLikeDialogComponentContext<N extends Name & Comparable<N>,
    I extends PluginInfoLike<I, N>,
    IS extends PluginInfoSetLike<N, I, IS, S, A, AS>,
    S extends PluginSelectorLike<N>,
    A extends PluginAliasLike<N, S, A>,
    AS extends PluginAliasSetLike<N, I, IS, S, A, AS>>
    extends
    ComponentLifecycleMatcher,
    CanGiveFocus,
    SpreadsheetDialogComponentContext,
    AddPluginAliasSetLikeComponentContext,
    RemovePluginAliasSetLikeComponentContext {

    /**
     * Creates a textbox that will be used to edit the {@link PluginInfoSetLike}.
     */
    ValueSpreadsheetTextBoxWrapper<?, AS> textBox();

    /**
     * Adds a {@link SpreadsheetMetadataFetcherWatcher}.<br>
     * Each time a {@link SpreadsheetMetadata} is received, it will refresh all the components in the dialog.
     */
    Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher);

    /**
     * Watcher method called whenever {@link #loadPluginInfoSetLike()} completes.
     */
    Runnable addProviderFetcherWatcher(final Consumer<AS> set);

    /**
     * Loads the {@link PluginInfoSetLike} for the given {@link SpreadsheetId}.
     */
    void loadPluginInfoSetLike();

    /**
     * Parses the text into a {@link PluginAliasSetLike}.
     */
    AS parseAliasSetLike(final String text);

    /**
     * Returns an empty {@link PluginAliasSetLike}.
     */
    AS emptyAliasSetLike();

    /**
     * Getter that fetches the {@link PluginAliasSetLike} from the {@link SpreadsheetMetadata}.
     */
    AS metadataAliasSetLike();

    /**
     * Getter that fetches the {@link PluginAliasSetLike} sourced from the provider.
     */
    AS pluginAliasSetLike();
}
