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
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.tree.text.TextStylePropertyName;

final public class SpreadsheetCellStyleSelectHistoryToken<T> extends SpreadsheetCellStyleHistoryToken<T> {

    static <T> SpreadsheetCellStyleSelectHistoryToken<T> with(final SpreadsheetId id,
                                                              final SpreadsheetName name,
                                                              final SpreadsheetViewportSelection viewportSelection,
                                                              final TextStylePropertyName<T> propertyName) {
        return new SpreadsheetCellStyleSelectHistoryToken(
                id,
                name,
                viewportSelection,
                propertyName
        );
    }

    private SpreadsheetCellStyleSelectHistoryToken(final SpreadsheetId id,
                                                   final SpreadsheetName name,
                                                   final SpreadsheetViewportSelection viewportSelection,
                                                   final TextStylePropertyName<T> propertyName) {
        super(
                id,
                name,
                viewportSelection,
                propertyName
        );
    }

    @Override
    UrlFragment styleUrlFragment() {
        return SELECT;
    }

    @Override
    SpreadsheetNameHistoryToken save(final String value) {
        final TextStylePropertyName<T> propertyName = this.propertyName();

        return cellStyleSave(
                this.id(),
                this.name(),
                this.viewportSelection(),
                propertyName,
                propertyName.parseValue(value)
        );
    }
}
