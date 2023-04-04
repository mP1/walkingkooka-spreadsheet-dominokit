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

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonPropertyName;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

final public class SpreadsheetCellStyleSaveHistoryToken<T> extends SpreadsheetCellStyleHistoryToken<T> {

    static <T> SpreadsheetCellStyleSaveHistoryToken<T> with(final SpreadsheetId id,
                                                            final SpreadsheetName name,
                                                            final SpreadsheetViewportSelection viewportSelection,
                                                            final TextStylePropertyName<T> propertyName,
                                                            final T propertyValue) {
        return new SpreadsheetCellStyleSaveHistoryToken<>(
                id,
                name,
                viewportSelection,
                propertyName,
                propertyValue
        );
    }

    private SpreadsheetCellStyleSaveHistoryToken(final SpreadsheetId id,
                                                 final SpreadsheetName name,
                                                 final SpreadsheetViewportSelection viewportSelection,
                                                 final TextStylePropertyName<T> propertyName,
                                                 final T propertyValue) {
        super(
                id,
                name,
                viewportSelection,
                propertyName
        );
        this.propertyValue = propertyValue;
    }

    public T propertyValue() {
        return this.propertyValue;
    }

    private final T propertyValue;

    @Override
    UrlFragment styleUrlFragment() {
        return this.saveUrlFragment(this.propertyValue());
    }

    @Override
    SpreadsheetHistoryToken setDifferentIdOrName(final SpreadsheetId id,
                                                 final SpreadsheetName name) {
        return new SpreadsheetCellStyleSaveHistoryToken(
                id,
                name,
                this.viewportSelection(),
                this.propertyName(),
                this.propertyValue()
        );
    }

    @Override
    SpreadsheetNameHistoryToken save(final String value) {
        return this;
    }

    @Override
    void onHashChange0(final HistoryToken previous,
                       final AppContext context) {
        final TextStylePropertyName<T> propertyName = this.propertyName();

        // clear the save from the history token.
        context.pushHistoryToken(
                this.style(propertyName)
        );

        // PATCH cell with style property
        //
        // {
        //   "style": {
        //     "text-align":"CENTER"
        //   }
        // }
        final SpreadsheetDeltaFetcher fetcher = context.spreadsheetDeltaFetcher();

        fetcher.patch(
                fetcher.url(
                        this.id(),
                        this.viewportSelection()
                                .selection(),
                        Optional.empty() // no extra path
                ),
                JsonNode.object()
                        .set(
                                JsonPropertyName.with(
                                        SpreadsheetMetadataPropertyName.STYLE.value()
                                ),
                                JsonNode.parse(
                                        fetcher.toJson(
                                                TextStyle.EMPTY.set(
                                                        propertyName,
                                                        this.propertyValue()
                                                )
                                        )
                                )
                        ).toString()
        );
    }
}
