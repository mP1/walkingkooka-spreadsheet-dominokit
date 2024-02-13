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
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;
import java.util.OptionalInt;

abstract public class SpreadsheetCellHistoryToken extends AnchoredSpreadsheetSelectionHistoryToken {

    SpreadsheetCellHistoryToken(final SpreadsheetId id,
                                final SpreadsheetName name,
                                final AnchoredSpreadsheetSelection anchoredSelection) {
        super(
                id,
                name,
                anchoredSelection
        );

        final SpreadsheetSelection selection = anchoredSelection.selection();
        if (false == (selection.isCellReference() || selection.isCellRange() || selection.isLabelName())) {
            throw new IllegalArgumentException("Got " + selection + " expected cell, cell-range or label");
        }
    }

    @Override //
    final UrlFragment anchoredSelectionUrlFragment() {
        return this.cellUrlFragment();
    }

    abstract UrlFragment cellUrlFragment();

    @Override //
    final HistoryToken setClear0() {
        return this; // clear cell not supported
    }

    @Override //
    final HistoryToken setDelete0() {
        return cellDelete(
                this.id(),
                this.name(),
                this.anchoredSelection()
        );
    }

    final HistoryToken setFormula0() {
        return formula(
                this.id(),
                this.name(),
                this.anchoredSelection()
        );
    }

    @Override //
    final HistoryToken setFreeze0() {
        return cellFreeze(
                this.id(),
                this.name(),
                this.anchoredSelection()
        );
    }

    @Override //
    final HistoryToken setInsertAfter0(final OptionalInt count) {
        return this;
    }

    @Override //
    final HistoryToken setInsertBefore0(final OptionalInt count) {
        return this;
    }

    @Override //
    final HistoryToken setMenu1() {
        return cellMenu(
                this.id(),
                this.name(),
                this.anchoredSelection()
        );
    }

    @Override //
    final AnchoredSpreadsheetSelection setMenuSelection(final SpreadsheetSelection selection) {
        final AnchoredSpreadsheetSelection anchored = this.anchoredSelection();

        return selection.isCellReference() &&
                anchored.selection()
                        .testCell(selection.toCell()) ?
                anchored :
                selection.setDefaultAnchor();
    }

    @Override final HistoryToken replacePatternKind(final Optional<SpreadsheetPatternKind> patternKind) {
        final SpreadsheetId id = this.id();
        final SpreadsheetName name = this.name();
        final AnchoredSpreadsheetSelection anchoredSelection = this.anchoredSelection();

        return patternKind.isPresent() ?
                cellPattern(
                        id,
                        name,
                        anchoredSelection,
                        patternKind.get()
                ) :
                cell(
                        id,
                        name,
                        anchoredSelection
                );
    }

    @Override //
    final HistoryToken setStyle0(final TextStylePropertyName<?> propertyName) {
        return cellStyle(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                propertyName
        );
    }

    @Override //
    final HistoryToken setUnfreeze0() {
        return cellUnfreeze(
                this.id(),
                this.name(),
                this.anchoredSelection()
        );
    }
}
