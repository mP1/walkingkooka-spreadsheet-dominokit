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

import walkingkooka.Value;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;

public final class SpreadsheetMetadataPropertyStyleSaveHistoryToken<T> extends SpreadsheetMetadataPropertyStyleHistoryToken<T>
implements Value<Optional<T>>  {

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

        stylePropertyValue.ifPresent(stylePropertyName::checkValue);
    }

    @Override
    public Optional<T> value() {
        return this.stylePropertyValue;
    }

    private final Optional<T> stylePropertyValue;

    @Override
    UrlFragment styleUrlFragment() {
        return saveUrlFragment(
            this.value()
        );
    }

    @Override
    public HistoryToken clearAction() {
        return HistoryToken.metadataPropertyStyle(
            this.id(),
            this.name(),
            this.stylePropertyName()
        );
    }

    // new id/name but still metadata+style+property+value
    @Override //
    HistoryToken replaceIdAndName(final SpreadsheetId id,
                                  final SpreadsheetName name) {
        return with(
            id,
            name,
            this.stylePropertyName(),
            this.value()
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.pushHistoryToken(previous);

        // PATCH metadata with style property+value
        context.spreadsheetMetadataFetcher()
            .patchMetadata(
                this.id(),
                SpreadsheetMetadata.EMPTY.set(
                    SpreadsheetMetadataPropertyName.STYLE,
                    TextStyle.EMPTY.setOrRemove(
                        this.stylePropertyName(),
                        this.value()
                            .orElse(null)
                    )
                )
            );
    }
}
