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
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetName;

import java.util.Objects;
import java.util.Optional;

public final class SpreadsheetMetadataPropertySaveHistoryToken<T> extends SpreadsheetMetadataPropertyHistoryToken<T> implements Value<Optional<T>> {

    static <T> SpreadsheetMetadataPropertySaveHistoryToken<T> with(final SpreadsheetId id,
                                                                   final SpreadsheetName name,
                                                                   final SpreadsheetMetadataPropertyName<T> propertyName,
                                                                   final Optional<T> propertyValue) {
        return new SpreadsheetMetadataPropertySaveHistoryToken<>(
            id,
            name,
            propertyName,
            propertyValue
        );
    }

    private SpreadsheetMetadataPropertySaveHistoryToken(final SpreadsheetId id,
                                                        final SpreadsheetName name,
                                                        final SpreadsheetMetadataPropertyName<T> propertyName,
                                                        final Optional<T> propertyValue) {
        super(
            id,
            name,
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
            this.id,
            this.name,
            this.propertyName
        );
    }

    // new id/name but still metadata+property+value select
    @Override //
    HistoryToken replaceIdAndName(final SpreadsheetId id,
                                  final SpreadsheetName name) {
        return with(
            id,
            name,
            this.propertyName,
            this.value()
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        final SpreadsheetId id = this.id;
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
            this.id,
            this.name,
            this.propertyName,
            this.propertyValue
        );
    }
}
