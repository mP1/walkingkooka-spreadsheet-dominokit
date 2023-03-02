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
import walkingkooka.tree.text.TextStylePropertyName;

public final class SpreadsheetMetadataSelectHistoryHashToken extends SpreadsheetMetadataHistoryHashToken {

    static SpreadsheetMetadataSelectHistoryHashToken with(final SpreadsheetId id,
                                                          final SpreadsheetName name) {
        return new SpreadsheetMetadataSelectHistoryHashToken(
                id,
                name
        );
    }

    private SpreadsheetMetadataSelectHistoryHashToken(final SpreadsheetId id,
                                                     final SpreadsheetName name) {
        super(
                id,
                name
        );
    }

    @Override
    UrlFragment metadataUrlFragment() {
        return SELECT;
    }

    @Override
    SpreadsheetNameHistoryHashToken save(final String value) {
        return this;
    }

    @Override
    SpreadsheetNameHistoryHashToken style(final TextStylePropertyName<?> propertyName) {
        return this;
    }
}
