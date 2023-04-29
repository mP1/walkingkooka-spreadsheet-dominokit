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
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.tree.text.TextStylePropertyName;

abstract public class SpreadsheetRowHistoryToken extends SpreadsheetViewportSelectionHistoryToken {

    SpreadsheetRowHistoryToken(final SpreadsheetId id,
                               final SpreadsheetName name,
                               final SpreadsheetViewportSelection viewportSelection) {
        super(
                id,
                name,
                viewportSelection
        );

        final SpreadsheetSelection selection = viewportSelection.selection();
        if (false == selection.isRowReference() && false == selection.isRowReferenceRange()) {
            throw new IllegalArgumentException("Got " + selection + " expected row or row-range");
        }
    }

    @Override //
    final UrlFragment selectionViewportUrlFragment() {
        return this.rowUrlFragment();
    }

    abstract UrlFragment rowUrlFragment();

    @Override //
    final HistoryToken clear() {
        return rowClear(
                this.id(),
                this.name(),
                this.viewportSelection()
        );
    }

    @Override //
    final HistoryToken delete() {
        return rowDelete(
                this.id(),
                this.name(),
                this.viewportSelection()
        );
    }

    @Override
    public final HistoryToken formulaHistoryToken() {
        return this;
    }

    @Override
    public HistoryToken formulaSaveHistoryToken(final String text) {
        throw new UnsupportedOperationException();
    }

    @Override //
    final HistoryToken freeze() {
        return rowFreeze(
                this.id(),
                this.name(),
                this.viewportSelection()
        );
    }

    @Override //
    final HistoryToken menu() {
        return rowMenu(
                this.id(),
                this.name(),
                this.viewportSelection()
        );
    }

    @Override //
    final SpreadsheetViewportSelection menuHistoryTokenSpreadsheetViewportSelection(final SpreadsheetSelection selection) {
        final SpreadsheetViewportSelection viewportSelection = this.viewportSelection();

        return selection.isRowReference() &&
                viewportSelection
                        .selection()
                        .testRow(selection.toRow()) ?
                viewportSelection :
                selection.setDefaultAnchor();
    }

    @Override //
    HistoryToken pattern(final SpreadsheetPatternKind patternKind) {
        return this; // TODO
    }

    @Override //
    HistoryToken save(final String value) {
        return this;
    }

    @Override //
    final HistoryToken style(final TextStylePropertyName<?> propertyName) {
        return this; // row/1/style not currently supported
    }

    @Override //
    final HistoryToken unfreeze() {
        return rowUnfreeze(
                this.id(),
                this.name(),
                this.viewportSelection()
        );
    }

    @Override
    public final HistoryToken viewportSelectionHistoryToken() {
        return row(
                this.id(),
                this.name(),
                this.viewportSelection()
        );
    }
}
