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
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;

public abstract class SpreadsheetMetadataPropertyStyleHistoryToken<T> extends SpreadsheetMetadataPropertyHistoryToken<TextStyle> {

    SpreadsheetMetadataPropertyStyleHistoryToken(final SpreadsheetId id,
                                                 final SpreadsheetName name,
                                                 final TextStylePropertyName<T> stylePropertyName) {
        super(
                id,
                name,
                SpreadsheetMetadataPropertyName.STYLE
        );

        this.stylePropertyName = Objects.requireNonNull(stylePropertyName, "stylePropertyName");
    }

    public final TextStylePropertyName<T> stylePropertyName() {
        return this.stylePropertyName;
    }

    private final TextStylePropertyName<T> stylePropertyName;

    @Override
    final UrlFragment metadataPropertyUrlFragment() {
        return UrlFragment.SLASH.append(
                this.stylePropertyName()
                                .urlFragment()
                ).append(this.styleUrlFragment());
    }

    abstract UrlFragment styleUrlFragment();


    @Override
    HistoryToken parse0(final String component,
                        final TextCursor cursor) {
        throw new UnsupportedOperationException();
    }

    @Override
    SpreadsheetNameHistoryToken save(final String value) {
        final TextStylePropertyName<T> propertyName = this.stylePropertyName();

        return SpreadsheetHistoryToken.metadataPropertyStyleSave(
                this.id(),
                this.name(),
                propertyName,
                propertyName.parseValue(value)
        );
    }

    @Override
    SpreadsheetNameHistoryToken style(final TextStylePropertyName<?> propertyName) {
        return this;
    }
}
