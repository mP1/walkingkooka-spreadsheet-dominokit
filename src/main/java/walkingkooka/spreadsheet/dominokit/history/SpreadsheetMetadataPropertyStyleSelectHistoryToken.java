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
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.tree.text.TextStylePropertyName;

public final class SpreadsheetMetadataPropertyStyleSelectHistoryToken<T> extends SpreadsheetMetadataPropertyStyleHistoryToken<T> {

    static <T> SpreadsheetMetadataPropertyStyleSelectHistoryToken<T> with(final SpreadsheetId id,
                                                                          final SpreadsheetName name,
                                                                          final TextStylePropertyName<T> stylePropertyName) {
        return new SpreadsheetMetadataPropertyStyleSelectHistoryToken<>(
                id,
                name,
                stylePropertyName
        );
    }

    private SpreadsheetMetadataPropertyStyleSelectHistoryToken(final SpreadsheetId id,
                                                               final SpreadsheetName name,
                                                               final TextStylePropertyName<T> stylePropertyName) {
        super(
                id,
                name,
                stylePropertyName
        );
    }

    @Override
    UrlFragment styleUrlFragment() {
        return SELECT;
    }

    @Override
    HistoryToken parse0(final String component,
                        final TextCursor cursor) {
        HistoryToken result = this;

        switch (component) {
            case "pattern":
                result = this.parsePattern(cursor);
                break;
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
}
