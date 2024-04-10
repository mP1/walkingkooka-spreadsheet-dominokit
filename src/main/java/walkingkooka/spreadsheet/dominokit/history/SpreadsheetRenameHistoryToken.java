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

public abstract class SpreadsheetRenameHistoryToken extends SpreadsheetNameHistoryToken {

    SpreadsheetRenameHistoryToken(final SpreadsheetId id,
                                  final SpreadsheetName name) {
        super(
                id,
                name
        );
    }

    @Override final UrlFragment spreadsheetNameUrlFragment() {
        return RENAME.append(
                this.spreadsheetRenameUrlFragment()
        );
    }

    abstract UrlFragment spreadsheetRenameUrlFragment();

    @Override //
    final HistoryToken setClear0() {
        return null;
    }

    @Override //
    final HistoryToken setDelete0() {
        return this;
    }

    @Override //
    final HistoryToken setFormatPattern() {
        return this;
    }

    @Override
    public final HistoryToken setFormula() {
        return this; // should not happen
    }

    @Override //
    final HistoryToken setFreeze0() {
        return this;
    }

    @Override final HistoryToken setInsertAfter0(final OptionalInt count) {
        return this;
    }

    @Override //
    final HistoryToken setInsertBefore0(final OptionalInt count) {
        return this;
    }

    @Override //
    final HistoryToken setMenu1() {
        return this;
    }

    @Override //
    final AnchoredSpreadsheetSelection setMenuSelection(final SpreadsheetSelection selection) {
        return selection.setDefaultAnchor();
    }

    @Override final HistoryToken setParsePattern() {
        return this;
    }

    @Override //
    final HistoryToken replacePatternKind(final Optional<SpreadsheetPatternKind> patternKind) {
        return this;
    }

    @Override //
    final HistoryToken setStyle0(final TextStylePropertyName<?> propertyName) {
        return this;
    }

    @Override //
    final HistoryToken setUnfreeze0() {
        return this;
    }
}
