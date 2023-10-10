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
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;
import walkingkooka.tree.text.TextStylePropertyName;

abstract public class SpreadsheetCellHistoryToken extends SpreadsheetViewportHistoryToken {

    SpreadsheetCellHistoryToken(final SpreadsheetId id,
                                final SpreadsheetName name,
                                final SpreadsheetViewport viewport) {
        super(
                id,
                name,
                viewport
        );

        final SpreadsheetSelection selection = viewport.selection();
        if (false == (selection.isCellReference() || selection.isCellRange() || selection.isLabelName())) {
            throw new IllegalArgumentException("Got " + selection + " expected cell, cell-range or label");
        }
    }

    @Override //
    final UrlFragment viewportUrlFragment() {
        return this.cellUrlFragment();
    }

    abstract UrlFragment cellUrlFragment();

    @Override //
    final HistoryToken setClear0() {
        return cellClear(
                this.id(),
                this.name(),
                this.viewport()
        );
    }

    @Override //
    final HistoryToken setDelete0() {
        return cellDelete(
                this.id(),
                this.name(),
                this.viewport()
        );
    }

    final HistoryToken setFormula0() {
        return formula(
                this.id(),
                this.name(),
                this.viewport()
        );
    }

    @Override //
    final HistoryToken setFreeze0() {
        return cellFreeze(
                this.id(),
                this.name(),
                this.viewport()
        );
    }

    @Override //
    final HistoryToken setMenu1() {
        return cellMenu(
                this.id(),
                this.name(),
                this.viewport()
        );
    }

    @Override //
    final SpreadsheetViewport setMenu2ViewportSelection(final SpreadsheetSelection selection) {
        final SpreadsheetViewport viewport = this.viewport();

        return selection.isCellReference() &&
                viewport
                        .selection()
                        .testCell(selection.toCell()) ?
                viewport :
                selection.setDefaultAnchor();
    }

    @Override //
    final HistoryToken setStyle0(final TextStylePropertyName<?> propertyName) {
        return cellStyle(
                this.id(),
                this.name(),
                this.viewport(),
                propertyName
        );
    }

    @Override //
    final HistoryToken setUnfreeze0() {
        return cellUnfreeze(
                this.id(),
                this.name(),
                this.viewport()
        );
    }
}
