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

import walkingkooka.Cast;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.tree.text.TextStylePropertyName;

final public class SpreadsheetCellStyleSelectHistoryHashToken extends SpreadsheetCellStyleHistoryHashToken {

    static SpreadsheetCellStyleSelectHistoryHashToken with(final SpreadsheetId id,
                                                           final SpreadsheetName name,
                                                           final SpreadsheetViewportSelection viewportSelection,
                                                           final TextStylePropertyName<?> propertyName) {
        return new SpreadsheetCellStyleSelectHistoryHashToken(
                id,
                name,
                viewportSelection,
                propertyName
        );
    }

    private SpreadsheetCellStyleSelectHistoryHashToken(final SpreadsheetId id,
                                                       final SpreadsheetName name,
                                                       final SpreadsheetViewportSelection viewportSelection,
                                                       final TextStylePropertyName<?> propertyName) {
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
    SpreadsheetNameHistoryHashToken save(final String value) {
        final TextStylePropertyName<?> propertyName = this.propertyName();

        return cellStyleSave(
                this.id(),
                this.name(),
                this.viewportSelection(),
                propertyName,
                Cast.to(
                        propertyName.parseValue(value)
                )
        );
    }
}
