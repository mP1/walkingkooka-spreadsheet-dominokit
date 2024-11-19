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
import walkingkooka.locale.HasLocale;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.UrlQueryString;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardContext;
import walkingkooka.spreadsheet.dominokit.focus.CanGiveFocus;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetNameHistoryToken;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.net.HasConverterFetcher;
import walkingkooka.spreadsheet.dominokit.net.HasExpressionFunctionFetcher;
import walkingkooka.spreadsheet.dominokit.net.HasSpreadsheetComparatorFetcher;
import walkingkooka.spreadsheet.dominokit.net.HasSpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.net.HasSpreadsheetExporterFetcher;
import walkingkooka.spreadsheet.dominokit.net.HasSpreadsheetFormatterFetcher;
import walkingkooka.spreadsheet.dominokit.net.HasSpreadsheetImporterFetcher;
import walkingkooka.spreadsheet.dominokit.net.HasSpreadsheetMetadataFetcher;
import walkingkooka.spreadsheet.dominokit.net.HasSpreadsheetParserFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.meta.HasSpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.parser.SpreadsheetParserContext;
import walkingkooka.spreadsheet.provider.SpreadsheetProvider;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;
import walkingkooka.tree.json.marshall.JsonNodeMarshallUnmarshallContext;

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
        HasLocale,
        HasNow,
        HasSpreadsheetComparatorFetcher,
        HasConverterFetcher,
        HasSpreadsheetDeltaFetcher,
        HasSpreadsheetExporterFetcher,
        HasExpressionFunctionFetcher,
        HasSpreadsheetFormatterFetcher,
        HasSpreadsheetImporterFetcher,
        HasSpreadsheetMetadata,
        HasSpreadsheetMetadataFetcher,
        HasSpreadsheetParserFetcher,
        JsonNodeMarshallUnmarshallContext,
        ProviderContext,
        SpreadsheetFormatterContext,
        SpreadsheetParserContext,
        HistoryTokenContext,
        LoggingContext,
        Context {

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
     * Loads the given {@link SpreadsheetId} and will reload the previous {@link HistoryToken} if the load fails eg with a 404.
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
                        // nop
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
                                        final Optional<String> body,
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
                        context.pushHistoryToken(
                                previous.clearAction()
                        );
                    }
                }
        );

        this.spreadsheetMetadataFetcher()
                .loadSpreadsheetMetadata(id);
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
}
