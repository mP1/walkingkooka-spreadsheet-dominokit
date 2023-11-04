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

abstract public class SpreadsheetRowHistoryToken extends AnchoredSpreadsheetSelectionHistoryToken {

    SpreadsheetRowHistoryToken(final SpreadsheetId id,
                               final SpreadsheetName name,
                               final AnchoredSpreadsheetSelection selection) {
        super(
                id,
                name,
                selection
        );

        final SpreadsheetSelection spreadsheetSelection = selection.selection();
        if (false == spreadsheetSelection.isRowReference() && false == spreadsheetSelection.isRowReferenceRange()) {
            throw new IllegalArgumentException("Got " + spreadsheetSelection + " expected row or row-range");
        }
    }

    @Override //
    final UrlFragment anchoredSelectionUrlFragment() {
        return this.rowUrlFragment();
    }

    abstract UrlFragment rowUrlFragment();

    @Override //
    final HistoryToken setClear0() {
        return rowClear(
                this.id(),
                this.name(),
                this.selection()
        );
    }

    @Override //
    final HistoryToken setDelete0() {
        return rowDelete(
                this.id(),
                this.name(),
                this.selection()
        );
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
                this.selection()
        );
    }

    @Override //
    final HistoryToken setInsertAfter0(final int count) {
        return rowInsertAfter(
                this.id(),
                this.name(),
                this.selection(),
                count
        );
    }

    @Override //
    final HistoryToken setInsertBefore0(final int count) {
        return rowInsertBefore(
                this.id(),
                this.name(),
                this.selection(),
                count
        );
    }

    @Override //
    final HistoryToken setMenu1() {
        return rowMenu(
                this.id(),
                this.name(),
                this.selection()
        );
    }

    @Override //
    final AnchoredSpreadsheetSelection setMenuSelection(final SpreadsheetSelection selection) {
        final AnchoredSpreadsheetSelection anchored = this.selection();

        return selection.isRowReference() &&
                anchored.selection()
                        .testRow(selection.toRow()) ?
                anchored :
                selection.setDefaultAnchor();
    }

    @Override //
    HistoryToken setPatternKind0(final Optional<SpreadsheetPatternKind> patternKind) {
        return this; // TODO
    }

    @Override //
    HistoryToken setSave0(final String value) {
        return this;
    }

    @Override //
    final HistoryToken setStyle0(final TextStylePropertyName<?> propertyName) {
        return this; // row/1/style not currently supported
    }

    @Override //
    final HistoryToken setUnfreeze0() {
        return rowUnfreeze(
                this.id(),
                this.name(),
                this.selection()
        );
    }
}
