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

package walkingkooka.spreadsheet.dominokit.net;

import elemental2.dom.Headers;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.UrlPath;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

public final class SpreadsheetLabelMappingFetcher implements Fetcher {

    static {
        SpreadsheetSelection.labelName("Label")
                .mapping(SpreadsheetSelection.A1); // force json unmarshaller register
    }

    public static SpreadsheetLabelMappingFetcher with(final SpreadsheetLabelMappingFetcherWatcher watcher,
                                                      final AppContext context) {
        Objects.requireNonNull(watcher, "watcher");
        Objects.requireNonNull(context, "context");

        return new SpreadsheetLabelMappingFetcher(
                watcher,
                context
        );
    }

    private SpreadsheetLabelMappingFetcher(final SpreadsheetLabelMappingFetcherWatcher watcher,
                                           final AppContext context) {
        this.watcher = watcher;
        this.context = context;
    }

    /**
     * Loads the given {@link SpreadsheetLabelName}.
     */
    public void loadLabelMapping(final SpreadsheetId id,
                                 final SpreadsheetLabelName labelName) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(labelName, "labelName");

        this.get(
                url(
                        id,
                        labelName
                )
        );
    }

    /**
     * Saves the given {@link SpreadsheetLabelMapping}.
     */
    public void saveLabelMapping(final SpreadsheetId id,
                                 final SpreadsheetLabelMapping mapping) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(mapping, "mapping");

        this.post(
                url(
                        id,
                        mapping.label()
                ),
                this.context.marshallContext()
                        .marshall(mapping)
                        .toString()
        );
    }

    /**
     * DELETEs the given {@link SpreadsheetLabelName}
     */
    public void deleteLabelMapping(final SpreadsheetId id,
                                   final SpreadsheetLabelName labelName) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(labelName, "labelName");

        this.delete(
                url(
                        id,
                        labelName
                )
        );
    }

    // GET http://localhost:3000/api/spreadsheet/1/label/Label123
    public static RelativeUrl url(final SpreadsheetId id, 
                                  final SpreadsheetLabelName labelName) {
        return SpreadsheetMetadataFetcher.url(id)
                .appendPath(LABEL)
                .appendPath(
                        UrlPath.parse(
                                labelName.value()
                        )
                );
    }

    private final static UrlPath LABEL = UrlPath.parse("/label");

    @Override
    public void onBegin(final HttpMethod method,
                        final AbsoluteOrRelativeUrl url,
                        final Optional<String> body) {
        this.watcher.onBegin(
                method,
                url,
                body,
                this.context
        );
    }

    @Override
    public void onSuccess(final HttpMethod method,
                          final AbsoluteOrRelativeUrl url,
                          final String contentTypeName,
                          final String body) {
        final SpreadsheetLabelMappingFetcherWatcher watcher = this.watcher;
        final AppContext context = this.context;

        this.logSuccess(
                method,
                url,
                contentTypeName,
                body,
                context
        );

        switch (CharSequences.nullToEmpty(contentTypeName).toString()) {
            case "":
                watcher.onNoResponse(context);
                break;
            case "SpreadsheetLabelMapping":
                watcher.onSpreadsheetLabelMapping(
                        SpreadsheetMetadataFetcher.extractSpreadsheetId(url)
                                .get(),
                        Optional.ofNullable(
                                body.isEmpty() ?
                                        null :
                                        this.parse(
                                                body,
                                                SpreadsheetLabelMapping.class
                                        )
                        ),
                        context
                );
                break;
            default:
                throw new IllegalArgumentException("Unexpected content type " + CharSequences.quoteAndEscape(contentTypeName));
        }
    }

    @Override
    public void onFailure(final HttpMethod method,
                          final AbsoluteOrRelativeUrl url,
                          final HttpStatus status,
                          final Headers headers,
                          final String body) {
        this.watcher.onFailure(
                method,
                url,
                status,
                headers,
                body,
                this.context
        );
    }

    @Override
    public void onError(final Object cause) {
        this.watcher.onError(
                cause,
                this.context
        );
    }

    private final SpreadsheetLabelMappingFetcherWatcher watcher;

    @Override
    public AppContext context() {
        return this.context;
    }

    private final AppContext context;

    @Override
    public String toString() {
        return this.watcher.toString();
    }
}
