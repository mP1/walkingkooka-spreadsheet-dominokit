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
import walkingkooka.net.UrlQueryString;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;

final public class SpreadsheetCellStyleSaveHistoryToken<T> extends SpreadsheetCellStyleHistoryToken<T> {

    static <T> SpreadsheetCellStyleSaveHistoryToken<T> with(final SpreadsheetId id,
                                                            final SpreadsheetName name,
                                                            final SpreadsheetViewport viewport,
                                                            final TextStylePropertyName<T> propertyName,
                                                            final Optional<T> propertyValue) {
        return new SpreadsheetCellStyleSaveHistoryToken<>(
                id,
                name,
                viewport,
                propertyName,
                propertyValue
        );
    }

    private SpreadsheetCellStyleSaveHistoryToken(final SpreadsheetId id,
                                                 final SpreadsheetName name,
                                                 final SpreadsheetViewport viewport,
                                                 final TextStylePropertyName<T> propertyName,
                                                 final Optional<T> propertyValue) {
        super(
                id,
                name,
                viewport,
                propertyName
        );
        this.propertyValue = Objects.requireNonNull(propertyValue, "propertyValue");
    }

    public Optional<T> propertyValue() {
        return this.propertyValue;
    }

    private final Optional<T> propertyValue;

    @Override
    UrlFragment styleUrlFragment() {
        return this.saveUrlFragment(this.propertyValue());
    }

    @Override
    public HistoryToken setIdAndName(final SpreadsheetId id,
                                     final SpreadsheetName name) {
        return with(
                id,
                name,
                this.viewport(),
                this.propertyName(),
                this.propertyValue()
        );
    }

    @Override
    HistoryToken setSave0(final String value) {
        return this;
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        final TextStylePropertyName<T> propertyName = this.propertyName();

        // clear the save from the history token.
        context.pushHistoryToken(
                this.setStyle(propertyName)
        );

        final SpreadsheetDeltaFetcher fetcher = context.spreadsheetDeltaFetcher();

        final SpreadsheetViewport viewport = this.viewport();

        fetcher.patch(
                fetcher.url(
                        this.id(),
                        viewport.selection(),
                        Optional.empty() // no extra path
                ).setQuery(
                        SpreadsheetDeltaFetcher.appendViewportAndWindow(
                                viewport,
                                context.viewportCache()
                                        .windows(),
                                UrlQueryString.EMPTY
                        )
                ),
                SpreadsheetDelta.stylePatch(
                        propertyName.patch(
                                this.propertyValue().orElse(null)
                        )
                ).toString()
        );
    }
}
