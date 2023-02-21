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

abstract public class SpreadsheetRowHistoryHashToken extends SpreadsheetViewportSelectionHistoryHashToken {

    SpreadsheetRowHistoryHashToken(final SpreadsheetId id,
                                   final SpreadsheetName name,
                                   final SpreadsheetViewportSelection viewportSelection) {
        super(
                id,
                name,
                viewportSelection
        );
    }

    @Override
    final UrlFragment selectionViewportUrlFragment() {
        return this.rowUrlFragment();
    }

    abstract UrlFragment rowUrlFragment();

    @Override
    SpreadsheetViewportSelectionHistoryHashToken clear() {
        return rowClear(
                this.id(),
                this.name(),
                this.viewportSelection()
        );
    }

    @Override
    final SpreadsheetHistoryHashToken style(final TextStylePropertyName<?> propertyName) {
        throw new UnsupportedOperationException();
    }

    @Override
    final <T> SpreadsheetHistoryHashToken styleSave(final TextStylePropertyName<T> propertyName,
                                                    final T propertyValue) {
        throw new UnsupportedOperationException();
    }
}
