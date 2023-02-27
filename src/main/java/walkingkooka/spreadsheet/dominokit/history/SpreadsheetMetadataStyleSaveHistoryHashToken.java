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

public final class SpreadsheetMetadataStyleSaveHistoryHashToken<T> extends SpreadsheetMetadataStyleHistoryHashToken<T> {

    static <T> SpreadsheetMetadataStyleSaveHistoryHashToken<T> with(final SpreadsheetId id,
                                                                    final SpreadsheetName name,
                                                                    final TextStylePropertyName<T> stylePropertyName,
                                                                    final T stylePropertyValue) {
        return new SpreadsheetMetadataStyleSaveHistoryHashToken<>(
                id,
                name,
                stylePropertyName,
                stylePropertyValue
        );
    }

    private SpreadsheetMetadataStyleSaveHistoryHashToken(final SpreadsheetId id,
                                                         final SpreadsheetName name,
                                                         final TextStylePropertyName<T> stylePropertyName,
                                                         final T stylePropertyValue) {
        super(
                id,
                name,
                stylePropertyName
        );

        this.stylePropertyValue = stylePropertyValue;
    }

    public T stylePropertyValue() {
        return this.stylePropertyValue;
    }

    private final T stylePropertyValue;

    @Override
    UrlFragment styleUrlFragment() {
        return this.saveUrlFragment(
                this.stylePropertyValue()
        );
    }

    @Override
    HistoryHashToken parse0(final String component,
                            final TextCursor cursor) {
        return this;
    }
}
