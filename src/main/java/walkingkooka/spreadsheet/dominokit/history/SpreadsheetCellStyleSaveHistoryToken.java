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
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;

final public class SpreadsheetCellStyleSaveHistoryToken<T> extends SpreadsheetCellStyleHistoryToken<T> {

    static <T> SpreadsheetCellStyleSaveHistoryToken<T> with(final SpreadsheetId id,
                                                            final SpreadsheetName name,
                                                            final SpreadsheetViewportSelection viewportSelection,
                                                            final TextStylePropertyName<T> propertyName,
                                                            final Optional<T> propertyValue) {
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
                                                 final Optional<T> propertyValue) {
        super(
                id,
                name,
                viewportSelection,
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
                this.viewportSelection(),
                this.propertyName(),
                this.propertyValue()
        );
    }

    @Override
    HistoryToken setSave0(final String value) {
        return this;
    }

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        final TextStylePropertyName<T> propertyName = this.propertyName();

        // clear the save from the history token.
        context.pushHistoryToken(
                this.setStyle(propertyName)
        );

        final SpreadsheetDeltaFetcher fetcher = context.spreadsheetDeltaFetcher();

        final SpreadsheetViewportSelection viewportSelection = this.viewportSelection();

        fetcher.patch(
                fetcher.url(
                        this.id(),
                        viewportSelection.selection(),
                        Optional.empty() // no extra path
                ).setQuery(
                        SpreadsheetDeltaFetcher.appendViewportSelectionAndWindow(
                                viewportSelection,
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
