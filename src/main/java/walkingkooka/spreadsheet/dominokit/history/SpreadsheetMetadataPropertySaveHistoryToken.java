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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetName;

import java.util.Objects;
import java.util.Optional;

public final class SpreadsheetMetadataPropertySaveHistoryToken<T> extends SpreadsheetMetadataPropertyHistoryToken<T> implements HasValue<Optional<T>> {

    static <T> SpreadsheetMetadataPropertySaveHistoryToken<T> with(final SpreadsheetId spreadsheetId,
                                                                   final SpreadsheetName spreadsheetName,
                                                                   final SpreadsheetMetadataPropertyName<T> propertyName,
                                                                   final Optional<T> propertyValue) {
        return new SpreadsheetMetadataPropertySaveHistoryToken<>(
            spreadsheetId,
            spreadsheetName,
            propertyName,
            propertyValue
        );
    }

    private SpreadsheetMetadataPropertySaveHistoryToken(final SpreadsheetId spreadsheetId,
                                                        final SpreadsheetName spreadsheetName,
                                                        final SpreadsheetMetadataPropertyName<T> propertyName,
                                                        final Optional<T> propertyValue) {
        super(
            spreadsheetId,
            spreadsheetName,
            propertyName
        );

        this.propertyValue = Objects.requireNonNull(propertyValue, "propertyValue");
        propertyValue.ifPresent(propertyName::checkValue);
    }

    @Override
    public Optional<T> value() {
        return this.propertyValue;
    }

    private final Optional<T> propertyValue;

    @Override
    UrlFragment metadataPropertyUrlFragment() {
        return saveUrlFragment(
            this.value()
        );
    }

    @Override
    public HistoryToken clearAction() {
        return HistoryToken.metadataPropertySelect(
            this.spreadsheetId,
            this.spreadsheetName,
            this.propertyName
        );
    }

    // new id/name but still metadata+property+value select
    @Override //
    HistoryToken replaceSpreadsheetIdAndSpreadsheetName(final SpreadsheetId spreadsheetId,
                                                        final SpreadsheetName spreadsheetName) {
        return with(
            spreadsheetId,
            spreadsheetName,
            this.propertyName,
            this.value()
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        final SpreadsheetId id = this.spreadsheetId;
        final SpreadsheetMetadataPropertyName<T> propertyName = this.propertyName;

        context.pushHistoryToken(previous);

        // now perform patch
        context.spreadsheetMetadataFetcher()
            .patchMetadata(
                id,
                propertyName.patch(
                    this.value()
                        .orElse(null)
                )
            );
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitMetadataPropertySave(
            this.spreadsheetId,
            this.spreadsheetName,
            this.propertyName,
            this.propertyValue
        );
    }
}
