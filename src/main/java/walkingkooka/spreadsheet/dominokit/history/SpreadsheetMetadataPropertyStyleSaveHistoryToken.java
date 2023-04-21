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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;

public final class SpreadsheetMetadataPropertyStyleSaveHistoryToken<T> extends SpreadsheetMetadataPropertyStyleHistoryToken<T> {

    static <T> SpreadsheetMetadataPropertyStyleSaveHistoryToken<T> with(final SpreadsheetId id,
                                                                        final SpreadsheetName name,
                                                                        final TextStylePropertyName<T> stylePropertyName,
                                                                        final Optional<T> stylePropertyValue) {
        return new SpreadsheetMetadataPropertyStyleSaveHistoryToken<>(
                id,
                name,
                stylePropertyName,
                stylePropertyValue
        );
    }

    private SpreadsheetMetadataPropertyStyleSaveHistoryToken(final SpreadsheetId id,
                                                             final SpreadsheetName name,
                                                             final TextStylePropertyName<T> stylePropertyName,
                                                             final Optional<T> stylePropertyValue) {
        super(
                id,
                name,
                stylePropertyName
        );

        this.stylePropertyValue = Objects.requireNonNull(stylePropertyValue, "stylePropertyValue");
    }

    public Optional<T> stylePropertyValue() {
        return this.stylePropertyValue;
    }

    private final Optional<T> stylePropertyValue;

    @Override
    UrlFragment styleUrlFragment() {
        return this.saveUrlFragment(
                this.stylePropertyValue()
        );
    }

    @Override
    HistoryToken parse0(final String component,
                        final TextCursor cursor) {
        return this;
    }

    // new id/name but still metadata+style+property+value
    @Override
    public HistoryToken setIdAndName(final SpreadsheetId id,
                                     final SpreadsheetName name) {
        return with(
                id,
                name,
                this.stylePropertyName(),
                this.stylePropertyValue()
        );
    }

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        // PATCH metadata with style property+value
        context.spreadsheetMetadataFetcher()
                .patchMetadata(
                        this.id(),
                        SpreadsheetMetadata.EMPTY.set(
                                SpreadsheetMetadataPropertyName.STYLE,
                                TextStyle.EMPTY.setOrRemove(
                                        this.stylePropertyName(),
                                        this.stylePropertyValue().orElse(null)
                                )
                        )
                );
    }
}
