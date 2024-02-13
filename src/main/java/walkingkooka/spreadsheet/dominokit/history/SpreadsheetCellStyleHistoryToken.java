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
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;

abstract public class SpreadsheetCellStyleHistoryToken<T> extends SpreadsheetCellHistoryToken {

    SpreadsheetCellStyleHistoryToken(final SpreadsheetId id,
                                     final SpreadsheetName name,
                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                     final TextStylePropertyName<T> propertyName) {
        super(
                id,
                name,
                anchoredSelection
        );

        this.propertyName = Objects.requireNonNull(propertyName, "propertyName");
    }

    public final TextStylePropertyName<T> propertyName() {
        return this.propertyName;
    }

    private final TextStylePropertyName<T> propertyName;

    @Override
    UrlFragment cellUrlFragment() {
        return STYLE.append(this.propertyName().urlFragment())
                .append(this.styleUrlFragment());
    }

    abstract UrlFragment styleUrlFragment();

    @Override
    public final HistoryToken clearAction() {
        return this.setStyle(this.propertyName());
    }

    @Override
    public final HistoryToken setFormatPattern() {
        return this;
    }

    @Override
    public final HistoryToken setFormula() {
        return this.setFormula0();
    }

    @Override
    public final HistoryToken setParsePattern() {
        return this;
    }
}
