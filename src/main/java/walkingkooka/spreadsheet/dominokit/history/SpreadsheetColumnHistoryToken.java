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

abstract public class SpreadsheetColumnHistoryToken extends SpreadsheetViewportSelectionHistoryToken {

    SpreadsheetColumnHistoryToken(final SpreadsheetId id,
                                  final SpreadsheetName name,
                                  final SpreadsheetViewportSelection viewportSelection) {
        super(
                id,
                name,
                viewportSelection
        );

        final SpreadsheetSelection selection = viewportSelection.selection();
        if (false == selection.isColumnReference() && false == selection.isColumnReferenceRange()) {
            throw new IllegalArgumentException("Got " + selection + " expected column or column-range");
        }
    }

    @Override //
    final UrlFragment selectionViewportUrlFragment() {
        return this.columnUrlFragment();
    }

    abstract UrlFragment columnUrlFragment();

    @Override //
    final HistoryToken setClear0() {
        return columnClear(
                this.id(),
                this.name(),
                this.viewportSelection()
        );
    }

    @Override //
    final HistoryToken setDelete0() {
        return columnDelete(
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
    public final HistoryToken formulaSaveHistoryToken(final String text) {
        throw new UnsupportedOperationException();
    }

    @Override //
    final HistoryToken setFreeze0() {
        return columnFreeze(
                this.id(),
                this.name(),
                this.viewportSelection()
        );
    }

    @Override //
    final HistoryToken setMenu1() {
        return columnMenu(
                this.id(),
                this.name(),
                this.viewportSelection()
        );
    }

    @Override //
    final SpreadsheetViewportSelection setMenu2ViewportSelection(final SpreadsheetSelection selection) {
        final SpreadsheetViewportSelection viewportSelection = this.viewportSelection();

        return selection.isColumnReference() &&
                viewportSelection
                        .selection()
                        .testColumn(selection.toColumn()) ?
                viewportSelection :
                selection.setDefaultAnchor();
    }

    @Override //
    HistoryToken setPattern0(final SpreadsheetPatternKind patternKind) {
        return this; // TODO
    }

    @Override //
    HistoryToken save(final String value) {
        return this;
    }

    @Override //
    final HistoryToken setStyle0(final TextStylePropertyName<?> propertyName) {
        return this; // column/A/style not currently supported
    }

    @Override //
    final HistoryToken setUnfreeze0() {
        return columnUnfreeze(
                this.id(),
                this.name(),
                this.viewportSelection()
        );
    }

    @Override
    public final HistoryToken viewportSelectionHistoryToken() {
        return column(
                this.id(),
                this.name(),
                this.viewportSelection()
        );
    }
}
