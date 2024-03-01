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
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class SpreadsheetMetadataPropertySelectHistoryToken<T> extends SpreadsheetMetadataPropertyHistoryToken<T> {

    static <T> SpreadsheetMetadataPropertySelectHistoryToken<T> with(final SpreadsheetId id,
                                                                     final SpreadsheetName name,
                                                                     final SpreadsheetMetadataPropertyName<T> propertyName) {
        return new SpreadsheetMetadataPropertySelectHistoryToken<>(
                id,
                name,
                propertyName
        );
    }

    private SpreadsheetMetadataPropertySelectHistoryToken(final SpreadsheetId id,
                                                          final SpreadsheetName name,
                                                          final SpreadsheetMetadataPropertyName<T> propertyName) {
        super(
                id,
                name,
                propertyName
        );
    }

    @Override
    UrlFragment metadataPropertyUrlFragment() {
        return SELECT;
    }

    @Override
    public HistoryToken clearAction() {
        return this;
    }

    // new id/name but still metadata+property select
    @Override //
    HistoryToken replaceIdAndName(final SpreadsheetId id,
                                  final SpreadsheetName name) {
        return with(
                id,
                name,
                this.propertyName()
        );
    }

    HistoryToken replacePatternKind(final Optional<SpreadsheetPatternKind> patternKind) {
        final SpreadsheetId id = this.id();
        final SpreadsheetName name = this.name();

        return patternKind.isPresent() ?
                new SpreadsheetMetadataPropertySelectHistoryToken<>(
                        id,
                        name,
                        patternKind.get()
                                .spreadsheetMetadataPropertyName()
                ) :
                HistoryToken.metadataSelect(
                        id,
                        name
                );
    }

    @Override
    HistoryToken setSave0(final String value) {
        final SpreadsheetMetadataPropertyName<T> propertyName = this.propertyName();

        return HistoryToken.metadataPropertySave(
                this.id(),
                this.name(),
                propertyName,
                Optional.ofNullable(
                        value.isEmpty() ?
                                null :
                                propertyName.parseValue(value)
                )
        );
    }

    @Override
    HistoryToken setStyle0(final TextStylePropertyName<?> propertyName) {
        return HistoryToken.metadataPropertyStyle(
                this.id(),
                this.name(),
                propertyName
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // show metadata edit UI
    }
}
