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

import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.UrlPath;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.server.label.SpreadsheetLabelHateosResourceMappings;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

public final class SpreadsheetLabelFetcher extends Fetcher<SpreadsheetLabelFetcherWatcher> {

    static {
        SpreadsheetSelection.labelName("Label")
                .mapping(SpreadsheetSelection.A1); // force json unmarshaller register
    }

    public static SpreadsheetLabelFetcher with(final SpreadsheetLabelFetcherWatcher watcher,
                                               final AppContext context) {
        Objects.requireNonNull(watcher, "watcher");
        Objects.requireNonNull(context, "context");

        return new SpreadsheetLabelFetcher(
                watcher,
                context
        );
    }

    private SpreadsheetLabelFetcher(final SpreadsheetLabelFetcherWatcher watcher,
                                    final AppContext context) {
        super(
                watcher,
                context
        );
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
                this.context.marshall(mapping)
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
                .appendPathName(SpreadsheetLabelHateosResourceMappings.LABEL.toUrlPathName())
                .appendPath(
                        UrlPath.parse(
                                labelName.value()
                        )
                );
    }

    @Override
    public void onSuccess(final HttpMethod method,
                          final AbsoluteOrRelativeUrl url,
                          final String contentTypeName,
                          final String body) {
        final SpreadsheetLabelFetcherWatcher watcher = this.watcher;
        final AppContext context = this.context;

        switch (CharSequences.nullToEmpty(contentTypeName).toString()) {
            case "":
                watcher.onEmptyResponse(context);
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
}
