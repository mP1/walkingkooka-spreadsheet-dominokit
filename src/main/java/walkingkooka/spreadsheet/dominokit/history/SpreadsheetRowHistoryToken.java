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
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNamesList;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;
import java.util.OptionalInt;

abstract public class SpreadsheetRowHistoryToken extends SpreadsheetAnchoredSelectionHistoryToken {

    SpreadsheetRowHistoryToken(final SpreadsheetId id,
                               final SpreadsheetName name,
                               final AnchoredSpreadsheetSelection anchoredSelection) {
        super(
                id,
                name,
                anchoredSelection
        );

        final SpreadsheetSelection selection = anchoredSelection.selection();
        if (false == selection.isRowReference() && false == selection.isRowRangeReference()) {
            throw new IllegalArgumentException("Got " + selection + " expected row or row-range");
        }
    }

    @Override //
    final UrlFragment anchoredSelectionUrlFragment() {
        return this.rowUrlFragment();
    }

    abstract UrlFragment rowUrlFragment();

    @Override
    public final HistoryToken clearAction() {
        return this.selectionSelect();
    }
    @Override //
    final HistoryToken setClear0() {
        return rowClear(
                this.id(),
                this.name(),
                this.anchoredSelection()
        );
    }

    @Override //
    final HistoryToken setDelete0() {
        return rowDelete(
                this.id(),
                this.name(),
                this.anchoredSelection()
        );
    }

    @Override
    public final HistoryToken setFormatPattern() {
        return this;
    }

    @Override
    public final HistoryToken setFormula() {
        return this;
    }

    @Override //
    final HistoryToken setFreeze0() {
        return rowFreeze(
                this.id(),
                this.name(),
                this.anchoredSelection()
        );
    }

    @Override //
    final HistoryToken setInsertAfter0(final OptionalInt count) {
        return rowInsertAfter(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                count
        );
    }

    @Override //
    final HistoryToken setInsertBefore0(final OptionalInt count) {
        return rowInsertBefore(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                count
        );
    }

    @Override //
    final HistoryToken setMenu1() {
        return rowMenu(
                this.id(),
                this.name(),
                this.anchoredSelection()
        );
    }

    @Override //
    final AnchoredSpreadsheetSelection setMenuSelection(final SpreadsheetSelection selection) {
        final AnchoredSpreadsheetSelection anchored = this.anchoredSelection();

        return selection.isRowReference() &&
                anchored.selection()
                        .testRow(selection.toRow()) ?
                anchored :
                selection.setDefaultAnchor();
    }

    @Override
    public final HistoryToken setParsePattern() {
        return this;
    }

    @Override //
    final HistoryToken replacePatternKind(final Optional<SpreadsheetPatternKind> patternKind) {
        return this;
    }

    @Override //
    final HistoryToken setSave0(final String value) {
        HistoryToken historyToken = this;

        if (historyToken instanceof SpreadsheetRowSortHistoryToken) {
            historyToken = HistoryToken.rowSortSave(
                    this.id(),
                    this.name(),
                    this.anchoredSelection(),
                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse(value)
            );
        }

        return historyToken;
    }

    // sort.............................................................................................................

    @Override //
    final HistoryToken setSortEdit(final String comparators) {
        return HistoryToken.rowSortEdit(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                comparators
        );
    }

    @Override //
    final HistoryToken setSortSave(final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparators) {
        return HistoryToken.rowSortSave(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                comparators
        );
    }

    // style............................................................................................................

    @Override //
    final HistoryToken setStyle0(final TextStylePropertyName<?> propertyName) {
        return this; // row/1/style not currently supported
    }

    @Override //
    final HistoryToken setUnfreeze0() {
        return rowUnfreeze(
                this.id(),
                this.name(),
                this.anchoredSelection()
        );
    }
}
