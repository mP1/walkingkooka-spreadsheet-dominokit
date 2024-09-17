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
 * Base class that captures a lot of common functionality for a {@link PluginInfoSetDialogComponentContext}.
 */
abstract class PluginInfoSetDialogComponentContextBasic<N extends Name & Comparable<N>, I extends PluginInfoLike<I, N>, S extends PluginInfoSetLike<S, I, N>> implements PluginInfoSetDialogComponentContext<N, I, S>,
        HistoryTokenContextDelegator,
        LoggingContextDelegator {

    /**
     * {@see PluginInfoSetDialogComponentContextBasicConverters}
     */
    static PluginInfoSetDialogComponentContextBasicConverters converters(final AppContext context) {
        return PluginInfoSetDialogComponentContextBasicConverters.with(context);
    }

    /**
     * {@see PluginInfoSetDialogComponentContextBasicSpreadsheetComparators}
     */
    static PluginInfoSetDialogComponentContextBasicSpreadsheetComparators comparators(final AppContext context) {
        return PluginInfoSetDialogComponentContextBasicSpreadsheetComparators.with(context);
    }

    /**
     * {@see PluginInfoSetDialogComponentContextBasicSpreadsheetExporters}
     */
    static PluginInfoSetDialogComponentContextBasicSpreadsheetExporters exporters(final AppContext context) {
        return PluginInfoSetDialogComponentContextBasicSpreadsheetExporters.with(context);
    }

    /**
     * {@see PluginInfoSetDialogComponentContextBasicSpreadsheetExpressionFunctions}
     */
    static PluginInfoSetDialogComponentContextBasicSpreadsheetExpressionFunctions expressionFunctions(final AppContext context) {
        return PluginInfoSetDialogComponentContextBasicSpreadsheetExpressionFunctions.with(context);
    }

    /**
     * {@see PluginInfoSetDialogComponentContextBasicSpreadsheetFormatters}
     */
    static PluginInfoSetDialogComponentContextBasicSpreadsheetFormatters formatters(final AppContext context) {
        return PluginInfoSetDialogComponentContextBasicSpreadsheetFormatters.with(context);
    }

    /**
     * {@see PluginInfoSetDialogComponentContextBasicSpreadsheetImporters}
     */
    static PluginInfoSetDialogComponentContextBasicSpreadsheetImporters importers(final AppContext context) {
        return PluginInfoSetDialogComponentContextBasicSpreadsheetImporters.with(context);
    }

    /**
     * {@see PluginInfoSetDialogComponentContextBasicSpreadsheetParsers}
     */
    static PluginInfoSetDialogComponentContextBasicSpreadsheetParsers parsers(final AppContext context) {
        return PluginInfoSetDialogComponentContextBasicSpreadsheetParsers.with(context);
    }

    PluginInfoSetDialogComponentContextBasic(final AppContext context) {
        this.context = Objects.requireNonNull(context, "context");
    }

    // PluginInfoSetDialogComponentContext..............................................................................

    @Override
    public final String dialogTitle() {
        return CaseKind.KEBAB.change(
                this.metadataPropertyName().text(),
                CaseKind.TITLE
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
    public final void loadProviderInfoSet() {
        this.loadPluginInfoSet0(
                this.historyToken()
                        .cast(SpreadsheetNameHistoryToken.class)
                        .id()
        );
    }

    abstract void loadPluginInfoSet0(final SpreadsheetId id);

    @Override
    public final S parse(final String text) {
        return this.metadataPropertyName()
                .parseUrlFragmentSaveValue(text);
    }

    public abstract S emptyInfoSet();

    @Override
    public final S metadataInfoSet() {
        return this.context.spreadsheetMetadata()
                .getIgnoringDefaults(this.metadataPropertyName())
                .orElse(this.emptyInfoSet());
    }

    abstract SpreadsheetMetadataPropertyName<S> metadataPropertyName();

    @Override
    public final S providerInfoSet() {
        return this.providerInfoSet0(
                this.context.systemSpreadsheetProvider()
        );
    }

    abstract S providerInfoSet0(final SpreadsheetProvider spreadsheetProvider);

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
