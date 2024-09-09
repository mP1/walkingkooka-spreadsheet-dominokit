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

package walkingkooka.spreadsheet.dominokit.pluginfoset;

import walkingkooka.naming.Name;
import walkingkooka.plugin.PluginInfoLike;
import walkingkooka.plugin.PluginInfoSetLike;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.CanGiveFocus;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBoxWrapper;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;

import java.util.function.Consumer;

/**
 * A {@link walkingkooka.Context} tht accompanies a {@link PluginInfoSetDialogComponent} provided various inputs.
 */
public interface PluginInfoSetDialogComponentContext<N extends Name & Comparable<N>, I extends PluginInfoLike<I, N>, S extends PluginInfoSetLike<S, I, N>> extends CanGiveFocus,
        SpreadsheetDialogComponentContext,
        EnablePluginInfoSetComponentContext,
        DisablePluginInfoSetComponentContext {

    /**
     * Logic to provide the dialog title. In some cases the title might not be currently enabled as it is extracted from
     * the {@link walkingkooka.spreadsheet.dominokit.history.HistoryToken} which will change after the dialog is actually created.
     */
    String dialogTitle();

    /**
     * Returns true if the right metadata property is selected.
     */
    boolean isMatch(final HistoryToken token);

    /**
     * Creates a textbox that will be used to edit the {@link PluginInfoSetLike}.
     */
    ValueSpreadsheetTextBoxWrapper<?, S> textBox();

    /**
     * Adds a {@link SpreadsheetMetadataFetcherWatcher}.<br>
     * Each time a {@link SpreadsheetMetadata} is received, it will refresh all the components in the dialog.
     */
    Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher);

    /**
     * Watcher method called whenever {@link #loadProviderInfoSet()} completes.
     */
    Runnable addProviderFetcherWatcher(final Consumer<S> set);

    /**
     * Loads the {@link PluginInfoSetLike} for the given {@link SpreadsheetId}.
     */
    void loadProviderInfoSet();

    /**
     * Parses the text into a {@link PluginInfoSetLike}.
     */
    S parse(final String text);

    /**
     * Returns an empty {@link PluginInfoSetLike}.
     */
    S emptyInfoSet();

    /**
     * The metadata property name which will be used to retrieve the current value of the {@link PluginInfoSetLike}.
     */
    S metadataInfoSet();

    /**
     * Returns a {@link PluginInfoSetLike} sourced from the provider.
     */
    S providerInfoSet();
}
