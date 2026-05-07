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

import walkingkooka.HasValue;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;

public final class SpreadsheetMetadataPropertyStyleSaveHistoryToken<T> extends SpreadsheetMetadataPropertyStyleHistoryToken<T>
    implements HasValue<Optional<T>> {

    static <T> SpreadsheetMetadataPropertyStyleSaveHistoryToken<T> with(final SpreadsheetId spreadsheetId,
                                                                        final SpreadsheetName spreadsheetName,
                                                                        final TextStylePropertyName<T> stylePropertyName,
                                                                        final Optional<T> stylePropertyValue) {
        return new SpreadsheetMetadataPropertyStyleSaveHistoryToken<>(
            spreadsheetId,
            spreadsheetName,
            stylePropertyName,
            stylePropertyValue
        );
    }

    private SpreadsheetMetadataPropertyStyleSaveHistoryToken(final SpreadsheetId spreadsheetId,
                                                             final SpreadsheetName spreadsheetName,
                                                             final TextStylePropertyName<T> stylePropertyName,
                                                             final Optional<T> stylePropertyValue) {
        super(
            spreadsheetId,
            spreadsheetName,
            Optional.of(stylePropertyName)
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
            this.spreadsheetId,
            this.spreadsheetName,
            this.stylePropertyName
        );
    }

    // new id/name but still metadata+style+property+value
    @Override //
    HistoryToken replaceSpreadsheetIdAndSpreadsheetName(final SpreadsheetId spreadsheetId,
                                                        final SpreadsheetName spreadsheetName) {
        return with(
            spreadsheetId,
            spreadsheetName,
            this.stylePropertyName.get(),
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
                this.spreadsheetId,
                SpreadsheetMetadata.EMPTY.set(
                    SpreadsheetMetadataPropertyName.STYLE,
                    TextStyle.EMPTY.setOrRemove(
                        this.stylePropertyName.get(),
                        this.value()
                            .orElse(null)
                    )
                )
            );
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitMetadataStyleSave(
            this.spreadsheetId,
            this.spreadsheetName,
            this.stylePropertyName.get(),
            this.stylePropertyValue
        );
    }
}
