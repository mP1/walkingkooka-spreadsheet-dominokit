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
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

abstract public class SpreadsheetRowHistoryToken extends SpreadsheetViewportHistoryToken {

    SpreadsheetRowHistoryToken(final SpreadsheetId id,
                               final SpreadsheetName name,
                               final SpreadsheetViewport viewport) {
        super(
                id,
                name,
                viewport
        );

        final SpreadsheetSelection selection = viewport.selection();
        if (false == selection.isRowReference() && false == selection.isRowReferenceRange()) {
            throw new IllegalArgumentException("Got " + selection + " expected row or row-range");
        }
    }

    @Override //
    final UrlFragment viewportUrlFragment() {
        return this.rowUrlFragment();
    }

    abstract UrlFragment rowUrlFragment();

    @Override //
    final HistoryToken setClear0() {
        return rowClear(
                this.id(),
                this.name(),
                this.viewport()
        );
    }

    @Override //
    final HistoryToken setDelete0() {
        return rowDelete(
                this.id(),
                this.name(),
                this.viewport()
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
                this.viewport()
        );
    }

    @Override //
    final HistoryToken setMenu1() {
        return rowMenu(
                this.id(),
                this.name(),
                this.viewport()
        );
    }

    @Override //
    final SpreadsheetViewport setMenu2ViewportSelection(final SpreadsheetSelection selection) {
        final SpreadsheetViewport viewport = this.viewport();

        return selection.isRowReference() &&
                viewport
                        .selection()
                        .testRow(selection.toRow()) ?
                viewport :
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
                this.viewport()
        );
    }
}
