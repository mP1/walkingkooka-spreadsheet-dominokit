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

package walkingkooka.spreadsheet.dominokit.history;

import elemental2.dom.Headers;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.UrlFragment;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.text.cursor.TextCursor;

import java.util.List;
import java.util.Optional;

public final class SpreadsheetLoadHistoryToken extends SpreadsheetIdHistoryToken {

    static SpreadsheetLoadHistoryToken with(final SpreadsheetId id) {
        return new SpreadsheetLoadHistoryToken(
                id
        );
    }

    private SpreadsheetLoadHistoryToken(final SpreadsheetId id) {
        super(id);
    }

    @Override
    UrlFragment spreadsheetIdUrlFragment() {
        return UrlFragment.EMPTY;
    }

    @Override
    HistoryToken parse0(final String component,
                        final TextCursor cursor) {
        return spreadsheetSelect(
                this.id(),
                SpreadsheetName.with(component)
        ).parse(cursor);
    }

    @Override
    public HistoryToken clearAction() {
        return this;
    }

    @Override
    public HistoryToken setFormula() {
        return this; // should not happen
    }

    @Override //
    HistoryToken replaceIdAndName(final SpreadsheetId id,
                                  final SpreadsheetName name) {
        return spreadsheetSelect(
                id,
                name
        );
    }

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        context.addSpreadsheetMetadataWatcherOnce(
                new SpreadsheetMetadataFetcherWatcher() {

                    @Override
                    public void onNoResponse(final AppContext context) {
                        // nop
                    }

                    @Override
                    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                                      final AppContext context) {
                        // nop
                    }

                    @Override
                    public void onSpreadsheetMetadataList(final List<SpreadsheetMetadata> metadatas,
                                                          final AppContext context) {
                        // ignore
                    }

                    @Override
                    public void onBegin(final HttpMethod method,
                                        final Url url,
                                        final Optional<String> body,
                                        final AppContext context) {
                        // nop
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
        context.spreadsheetMetadataFetcher()
                .loadSpreadsheetMetadata(this.id());
    }
}
