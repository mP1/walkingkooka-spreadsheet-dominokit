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
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.tree.text.TextStylePropertyName;

abstract public class SpreadsheetCellHistoryHashToken extends SpreadsheetViewportSelectionHistoryHashToken {

    SpreadsheetCellHistoryHashToken(final SpreadsheetId id,
                                    final SpreadsheetName name,
                                    final SpreadsheetViewportSelection viewportSelection) {
        super(
                id,
                name,
                viewportSelection
        );

        final SpreadsheetSelection selection = viewportSelection.selection();
        if (false == (selection.isCellReference() || selection.isCellRange() || selection.isLabelName())) {
            throw new IllegalArgumentException("Expected cell, cell-range or label but got " + selection);
        }
    }

    @Override
    final UrlFragment selectionViewportUrlFragment() {
        return this.cellUrlFragment();
    }

    abstract UrlFragment cellUrlFragment();

    @Override
    SpreadsheetViewportSelectionHistoryHashToken clear() {
        return cellClear(
                this.id(),
                this.name(),
                this.viewportSelection()
        );
    }

    @Override
    final SpreadsheetViewportSelectionHistoryHashToken style(final TextStylePropertyName<?> propertyName) {
        return cellStyle(
                this.id(),
                this.name(),
                this.viewportSelection(),
                propertyName
        );
    }

    @Override
    final <T> SpreadsheetViewportSelectionHistoryHashToken styleSave(final TextStylePropertyName<T> propertyName,
                                                                     final T propertyValue) {
        return cellStyleSave(
                this.id(),
                this.name(),
                this.viewportSelection(),
                propertyName,
                propertyValue
        );
    }
}
