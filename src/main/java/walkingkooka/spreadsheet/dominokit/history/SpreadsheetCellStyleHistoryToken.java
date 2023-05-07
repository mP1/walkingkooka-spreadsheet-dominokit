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
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;

abstract public class SpreadsheetCellStyleHistoryToken<T> extends SpreadsheetCellHistoryToken {

    SpreadsheetCellStyleHistoryToken(final SpreadsheetId id,
                                     final SpreadsheetName name,
                                     final SpreadsheetViewportSelection viewportSelection,
                                     final TextStylePropertyName<T> propertyName) {
        super(
                id,
                name,
                viewportSelection
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
    public final HistoryToken setFormula() {
        return this;
    }

    @Override
    public final HistoryToken formulaSaveHistoryToken(final String text) {
        throw new UnsupportedOperationException();
    }

    @Override //
    final HistoryToken setPattern0(final SpreadsheetPatternKind patternKind) {
        return this;
    }
}
