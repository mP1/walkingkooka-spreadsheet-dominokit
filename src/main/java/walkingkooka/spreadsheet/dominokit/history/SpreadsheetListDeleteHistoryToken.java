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
import walkingkooka.spreadsheet.dominokit.fetcher.FetcherRequestBody;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.text.cursor.TextCursor;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Represents the user clicking on a delete link within the list dialog.
 * <pre>
 * /delete/spreadsheet-id
 * </pre>
 */
public final class SpreadsheetListDeleteHistoryToken extends SpreadsheetIdHistoryToken implements HistoryTokenWatcher {

    static SpreadsheetListDeleteHistoryToken with(final SpreadsheetId id) {
        return new SpreadsheetListDeleteHistoryToken(
            id
        );
    }

    private SpreadsheetListDeleteHistoryToken(final SpreadsheetId id) {
        super(
            id
        );
    }

    @Override
    UrlFragment spreadsheetUrlFragment() {
        return DELETE.appendSlashThen(
            this.id.urlFragment()
        );
    }

    @Override
    public HistoryToken clearAction() {
        return HistoryToken.spreadsheetListSelect(HistoryTokenOffsetAndCount.EMPTY);
    }

    @Override //
    HistoryToken replaceIdAndName(final SpreadsheetId id,
                                  final SpreadsheetName name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HistoryToken setSaveValue(final Optional<?> value) {
        Objects.requireNonNull(value, "value");

        return this;
    }

    @Override
    HistoryToken parseNext(final String component,
                           final TextCursor cursor) {
        return this;
    }

    // HistoryTokenWatcher..............................................................................................

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        context.pushHistoryToken(
            previous.clearAction()
        );

        context.addSpreadsheetMetadataFetcherWatcherOnce(
            new SpreadsheetMetadataFetcherWatcher() {
                @Override
                public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata) {
                    // nop
                }

                @Override
                public void onSpreadsheetMetadataSet(final Set<SpreadsheetMetadata> metadatas) {
                    // ignore
                }

                @Override
                public void onBegin(final HttpMethod method,
                                    final Url url,
                                    final Optional<FetcherRequestBody<?>> body) {
                    // nop
                }

                @Override
                public void onFailure(final HttpMethod method,
                                      final AbsoluteOrRelativeUrl url,
                                      final HttpStatus status,
                                      final Headers headers,
                                      final String body) {
                    context.pushHistoryToken(
                        previous.clearAction()
                    );
                }

                @Override
                public void onError(final Object cause) {
                    context.pushHistoryToken(
                        previous.clearAction()
                    );
                }

                @Override
                public void onEmptyResponse() {

                }
            }
        );
        context.spreadsheetMetadataFetcher()
            .deleteSpreadsheetMetadata(this.id);
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitSpreadsheetListDelete(this.id);
    }
}
