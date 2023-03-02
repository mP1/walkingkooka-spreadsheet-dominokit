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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.tree.text.TextStylePropertyName;

public final class SpreadsheetMetadataPropertySelectHistoryHashToken<T> extends SpreadsheetMetadataPropertyHistoryHashToken<T> {

    static <T> SpreadsheetMetadataPropertySelectHistoryHashToken<T> with(final SpreadsheetId id,
                                                                         final SpreadsheetName name,
                                                                         final SpreadsheetMetadataPropertyName<T> propertyName) {
        return new SpreadsheetMetadataPropertySelectHistoryHashToken<>(
                id,
                name,
                propertyName
        );
    }

    private SpreadsheetMetadataPropertySelectHistoryHashToken(final SpreadsheetId id,
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
    HistoryHashToken parse0(final String component,
                            final TextCursor cursor) {
        HistoryHashToken result = this;

        switch(component) {
            case "save":
                result = this.parseSave(cursor);
                break;
            default:
                cursor.end();
                result = this; // ignore
                break;
        }

        return result;
    }

    @Override
    SpreadsheetNameHistoryHashToken save(final String value) {
        final SpreadsheetMetadataPropertyName<T> propertyName = this.propertyName();

        return SpreadsheetHistoryHashToken.metadataPropertySave(
            this.id(),
                this.name(),
                propertyName,
                propertyName.parseValue(value)
        );
    }

    @Override
    SpreadsheetNameHistoryHashToken style(final TextStylePropertyName<?> propertyName) {
        return SpreadsheetHistoryHashToken.metadataPropertyStyle(
            this.id(),
                this.name(),
                propertyName
        );
    }
}
