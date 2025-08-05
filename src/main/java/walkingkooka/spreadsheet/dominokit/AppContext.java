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

package walkingkooka.spreadsheet.dominokit;

import elemental2.dom.Headers;
import walkingkooka.Context;
import walkingkooka.datetime.HasNow;
import walkingkooka.locale.LocaleContext;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.UrlQueryString;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardContext;
import walkingkooka.spreadsheet.dominokit.fetcher.FetcherRequestBody;
import walkingkooka.spreadsheet.dominokit.fetcher.HasConverterFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.HasDateTimeSymbolsFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.HasDecimalNumberSymbolsFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.HasExpressionFunctionFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.HasFormHandlerFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.HasLocaleFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.HasPluginFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetComparatorFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetExporterFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetFormatterFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetImporterFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetMetadataFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetParserFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.HasValidatorFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.focus.CanGiveFocus;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetNameHistoryToken;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCacheContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.meta.HasSpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.parser.SpreadsheetParserContext;
import walkingkooka.spreadsheet.provider.SpreadsheetProvider;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewport;
import walkingkooka.tree.json.marshall.JsonNodeMarshallUnmarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContextPreProcessor;
import walkingkooka.util.HasLocale;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

/**
 * A {@link Context} at the application level that provides and centralises many global values and components.
 */
public interface AppContext extends CanGiveFocus,
    ClipboardContext,
    SpreadsheetProvider,
    HasDateTimeSymbolsFetcher,
    HasDecimalNumberSymbolsFetcher,
    HasFormHandlerFetcher,
    HasLocale,
    HasNow,
    HasSpreadsheetComparatorFetcher,
    HasConverterFetcher,
    HasPluginFetcher,
    HasLocaleFetcher,
    HasSpreadsheetDeltaFetcher,
    HasSpreadsheetExporterFetcher,
    HasExpressionFunctionFetcher,
    HasSpreadsheetFormatterFetcher,
    HasSpreadsheetImporterFetcher,
    HasSpreadsheetMetadata,
    HasSpreadsheetMetadataFetcher,
    HasSpreadsheetParserFetcher,
    HasValidatorFetcher,
    JsonNodeMarshallUnmarshallContext,
    LocaleContext,
    ProviderContext,
    SpreadsheetFormatterContext,
    SpreadsheetParserContext,
    HistoryContext,
    LoggingContext,
    RefreshContext,
    SpreadsheetViewportCacheContext,
    Context {

    /**
     * Clears any present {@link SpreadsheetMetadata}, ideal after an attempt to load an unknown / invalid {@link SpreadsheetId}.
     */
    void clearSpreadsheetMetadata();

    /**
     * If the metadata.spreadsheetId and current historyToken.spreadsheetId DONT match wait for the metadata to be loaded then fire history token again.
     */
    default boolean isSpreadsheetMetadataLoaded() {
        final boolean loaded;

        final HistoryToken token = this.historyToken();
        if (token instanceof SpreadsheetNameHistoryToken) {
            loaded = token.cast(SpreadsheetNameHistoryToken.class).id()
                .equals(
                    this.spreadsheetMetadata()
                        .id()
                        .orElse(null)
                );
        } else {
            loaded = false;
        }

        return loaded;
    }

    /**
     * Loads the given {@link SpreadsheetId} and will reload the previous {@link HistoryToken} if the load fails eg with a 204.
     * In most cases this will be the most useful method for use cases where a new spreadsheet is being loaded to be displayed
     * for editing/viewing.
     */
    default void loadSpreadsheetMetadataAndPushPreviousIfFails(final SpreadsheetId id,
                                                               final HistoryToken previous) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(previous, "previous");

        this.addSpreadsheetMetadataFetcherWatcherOnce(
            new SpreadsheetMetadataFetcherWatcher() {

                @Override
                public void onEmptyResponse(final AppContext context) {
                    this.reloadPreviousHistoryToken(context);
                }

                @Override
                public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                                  final AppContext context) {
                    // ignore
                }

                @Override
                public void onSpreadsheetMetadataSet(final Set<SpreadsheetMetadata> metadatas,
                                                     final AppContext context) {
                    // ignore
                }

                @Override
                public void onBegin(final HttpMethod method,
                                    final Url url,
                                    final Optional<FetcherRequestBody<?>> body,
                                    final AppContext context) {
                    // ignore
                }

                @Override
                public void onFailure(final HttpMethod method,
                                      final AbsoluteOrRelativeUrl url,
                                      final HttpStatus status,
                                      final Headers headers,
                                      final String body,
                                      final AppContext context) {
                    context.pushHistoryToken(
                        previous.clearAction()
                    );
                }

                @Override
                public void onError(final Object cause,
                                    final AppContext context) {
                    this.reloadPreviousHistoryToken(context);
                }

                // clear the cached SpreadsheetMetadata and then push the previous URL which should load previous spreadsheet etc.
                private void reloadPreviousHistoryToken(final AppContext context) {
                    context.clearSpreadsheetMetadata();
                    context.pushHistoryToken(
                        previous.clearAction()
                    );
                }
            }
        );

        this.spreadsheetMetadataFetcher()
            .getSpreadsheetMetadata(id);
    }

    // viewport.........................................................................................................

    /**
     * Requests a reload of this spreadsheet.
     */
    void reload();

    /**
     * Creates a {@link SpreadsheetViewport} with the provided {@link AnchoredSpreadsheetSelection}.
     */
    SpreadsheetViewport viewport(final Optional<AnchoredSpreadsheetSelection> selection);

    /**
     * A cache for the viewport cache.
     */
    SpreadsheetViewportCache spreadsheetViewportCache();

    // cellFind.........................................................................................................

    /**
     * Returns a {@link UrlQueryString} that includes the viewport/window
     * This is particularly useful for {@link SpreadsheetDeltaFetcher}
     */
    default UrlQueryString viewportAndWindowQueryString() {
        return SpreadsheetDeltaFetcher.viewportAndWindowQueryString(
            this.viewport(SpreadsheetViewport.NO_ANCHORED_SELECTION),
            this.spreadsheetViewportCache()
                .windows()
        );
    }

    // SpreadsheetListDialogComponent...................................................................................

    /**
     * The default row count to use when {@link walkingkooka.spreadsheet.dominokit.history.SpreadsheetListHistoryToken} is
     * missing a count.
     */
    OptionalInt spreadsheetListDialogComponentDefaultCount();

    // system SpreadsheetProvider.......................................................................................

    /**
     * Returns the system {@link SpreadsheetProvider}.<br>
     * This will be used to provide ENABLE | DISABLE links when editing any {@link SpreadsheetMetadata} {@link walkingkooka.plugin.PluginInfoSetLike}.
     */
    SpreadsheetProvider systemSpreadsheetProvider();

    @Override
    default AppContext setPreProcessor(final JsonNodeUnmarshallContextPreProcessor processor) {
        throw new UnsupportedOperationException();
    }
}
