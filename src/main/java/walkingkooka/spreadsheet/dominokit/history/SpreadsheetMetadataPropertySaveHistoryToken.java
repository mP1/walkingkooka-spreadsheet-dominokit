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
import walkingkooka.tree.text.TextStylePropertyName;

public final class SpreadsheetMetadataPropertySaveHistoryToken<T> extends SpreadsheetMetadataPropertyHistoryToken<T> {

    static <T> SpreadsheetMetadataPropertySaveHistoryToken<T> with(final SpreadsheetId id,
                                                                   final SpreadsheetName name,
                                                                   final SpreadsheetMetadataPropertyName<T> propertyName,
                                                                   final T propertyValue) {
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
                                                        final T propertyValue) {
        super(
                id,
                name,
                propertyName
        );

        this.propertyValue = propertyValue;
    }

    public T propertyValue() {
        return this.propertyValue;
    }

    private final T propertyValue;

    @Override
    UrlFragment metadataPropertyUrlFragment() {
        return this.saveUrlFragment(
                this.propertyValue()
        );
    }

    @Override
    HistoryToken parse0(final String component,
                        final TextCursor cursor) {
        HistoryToken result = this;

        switch (component) {
            case "save":
                result = this.parseSave(cursor);
                break;
            case "style":
                result = this.parseStyle(cursor);
                break;
            default:
                cursor.end();
                break;
        }

        return result;
    }

    @Override
    SpreadsheetHistoryToken setDifferentIdOrName(final SpreadsheetId id,
                                                 final SpreadsheetName name) {
        return new SpreadsheetMetadataPropertySaveHistoryToken<>(
                id,
                name,
                this.propertyName(),
                this.propertyValue()
        );
    }

    @Override
    SpreadsheetNameHistoryToken save(final String value) {
        return this;
    }

    @Override
    SpreadsheetNameHistoryToken style(final TextStylePropertyName<?> propertyName) {
        return this;
    }

    @Override
    void onHashChange0(final HistoryToken previous,
                       final AppContext context) {
        context.spreadsheetMetadataFetcher()
                .patchMetadata(
                        this.id(),
                        SpreadsheetMetadata.EMPTY.set(
                                this.propertyName(),
                                this.propertyValue()
                        )
                );
    }
}
