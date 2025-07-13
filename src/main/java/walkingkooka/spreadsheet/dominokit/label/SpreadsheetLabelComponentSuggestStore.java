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

package walkingkooka.spreadsheet.dominokit.label;

import elemental2.dom.Headers;
import org.dominokit.domino.ui.elements.SpanElement;
import org.dominokit.domino.ui.forms.suggest.SuggestOption;
import org.dominokit.domino.ui.forms.suggest.SuggestionsStore;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.fetcher.FetcherRequestBody;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * An empty {@link SuggestionsStore}, eventually this will call a server API to search for matching {@link walkingkooka.spreadsheet.reference.SpreadsheetLabelName}
 */
final class SpreadsheetLabelComponentSuggestStore implements SuggestionsStore<SpreadsheetLabelName, SpanElement, SuggestOption<SpreadsheetLabelName>> {

    static SpreadsheetLabelComponentSuggestStore with(final SpreadsheetLabelComponentContext context) {
        return new SpreadsheetLabelComponentSuggestStore(context);
    }

    private SpreadsheetLabelComponentSuggestStore(final SpreadsheetLabelComponentContext context) {
        super();
        this.context = context;
    }

    @Override
    public void find(final SpreadsheetLabelName searchValue,
                     final Consumer<SuggestOption<SpreadsheetLabelName>> handler) {
        handler.accept(
            SuggestOption.create(searchValue)
        );
    }

    @Override
    public void filter(final String filter,
                       final SuggestionsHandler<SpreadsheetLabelName, SpanElement, SuggestOption<SpreadsheetLabelName>> handler) {
        this.context.addSpreadsheetDeltaFetcherWatcherOnce(
            new SpreadsheetDeltaFetcherWatcher() {

                @Override
                public void onSpreadsheetDelta(final HttpMethod method,
                                               final AbsoluteOrRelativeUrl url,
                                               final SpreadsheetDelta delta,
                                               final AppContext context) {
                    if(SpreadsheetDeltaFetcher.isGetLabelMappingsFindByName(method, url.path())) {
                        handler.onSuggestionsReady(
                            delta.labels()
                                .stream()
                                .map((SpreadsheetLabelMapping m) -> SuggestOption.create(m.label()))
                                .collect(Collectors.toList())
                        );
                    }
                }

                @Override
                public void onBegin(final HttpMethod method,
                                    final Url url,
                                    final Optional<FetcherRequestBody<?>> body,
                                    final AppContext context) {
                    // NOP
                }

                @Override
                public void onFailure(final HttpMethod method,
                                      final AbsoluteOrRelativeUrl url,
                                      final HttpStatus status,
                                      final Headers headers,
                                      final String body,
                                      final AppContext context) {
                    // NOP
                }

                @Override
                public void onError(final Object cause,
                                    final AppContext context) {
                    // NOP
                }

                @Override
                public void onEmptyResponse(final AppContext context) {
                    // NOP
                }
            }
        );

        handler.onSuggestionsReady(
            Lists.of(
                SuggestOption.create(
                    SpreadsheetSelection.labelName(filter)
                )
            )
        );
    }

    private final SpreadsheetLabelComponentContext context;

    @Override
    public String toString() {
        return this.context.toString();
    }
}
