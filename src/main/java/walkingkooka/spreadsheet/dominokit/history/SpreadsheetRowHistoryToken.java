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

abstract public class SpreadsheetRowHistoryToken extends SpreadsheetViewportSelectionHistoryToken {

    SpreadsheetRowHistoryToken(final SpreadsheetId id,
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
    final SpreadsheetNameHistoryToken clear() {
        return rowClear(
                this.id(),
                this.name(),
                this.viewportSelection()
        );
    }

    @Override
    final SpreadsheetNameHistoryToken delete() {
        return rowDelete(
                this.id(),
                this.name(),
                this.viewportSelection()
        );
    }

    @Override
    final SpreadsheetNameHistoryToken formulaHistoryToken() {
        return this;
    }

    @Override
    final SpreadsheetNameHistoryToken freeze() {
        return rowFreeze(
                this.id(),
                this.name(),
                this.viewportSelection()
        );
    }

    @Override
    final SpreadsheetNameHistoryToken menu() {
        return rowMenu(
                this.id(),
                this.name(),
                this.viewportSelection()
        );
    }

    @Override
    SpreadsheetNameHistoryToken pattern(final SpreadsheetPatternKind patternKind) {
        return this; // TODO
    }

    @Override
    public final SpreadsheetViewportSelectionHistoryToken selection() {
        return row(
                this.id(),
                this.name(),
                this.viewportSelection()
        );
    }

    @Override
    SpreadsheetNameHistoryToken save(final String value) {
        return this;
    }

    @Override
    final SpreadsheetNameHistoryToken style(final TextStylePropertyName<?> propertyName) {
        return this; // row/1/style not currently supported
    }

    @Override
    final SpreadsheetNameHistoryToken unfreeze() {
        return rowUnfreeze(
                this.id(),
                this.name(),
                this.viewportSelection()
        );
    }
}
