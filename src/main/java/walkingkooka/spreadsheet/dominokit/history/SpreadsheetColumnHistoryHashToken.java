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

abstract public class SpreadsheetColumnHistoryHashToken extends SpreadsheetViewportSelectionHistoryHashToken {

    SpreadsheetColumnHistoryHashToken(final SpreadsheetId id,
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
        return this.columnUrlFragment();
    }

    abstract UrlFragment columnUrlFragment();

    @Override
    final SpreadsheetSelectionHistoryHashToken clear() {
        return columnClear(
                this.id(),
                this.name(),
                this.viewportSelection()
        );
    }

    @Override
    final SpreadsheetSelectionHistoryHashToken delete() {
        return columnDelete(
                this.id(),
                this.name(),
                this.viewportSelection()
        );
    }

    @Override
    final SpreadsheetSelectionHistoryHashToken formula() {
        return this;
    }

    @Override
    final SpreadsheetSelectionHistoryHashToken freeze() {
        return columnFreeze(
                this.id(),
                this.name(),
                this.viewportSelection()
        );
    }

    @Override
    final SpreadsheetSelectionHistoryHashToken menu() {
        return columnMenu(
                this.id(),
                this.name(),
                this.viewportSelection()
        );
    }

    @Override
    SpreadsheetSelectionHistoryHashToken save(final String value) {
        return this;
    }

    @Override
    final SpreadsheetSelectionHistoryHashToken style(final TextStylePropertyName<?> propertyName) {
        return this; // column/A/style not currently supported
    }

    @Override
    final SpreadsheetSelectionHistoryHashToken unfreeze() {
        return columnUnfreeze(
                this.id(),
                this.name(),
                this.viewportSelection()
        );
    }
}
