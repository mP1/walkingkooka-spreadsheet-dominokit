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

import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;

import java.util.Optional;

public abstract class SpreadsheetCellPatternToolbarHistoryToken extends SpreadsheetCellPatternHistoryToken {

    SpreadsheetCellPatternToolbarHistoryToken(final SpreadsheetId id,
                                              final SpreadsheetName name,
                                              final AnchoredSpreadsheetSelection anchoredSelection) {
        super(
                id,
                name,
                anchoredSelection,
                Optional.empty()
        );
    }

    @Override
    public final HistoryToken clearAction() {
        return this;
    }

    abstract boolean isCompatible(final SpreadsheetPatternKind kind);

    // ignore save value and return this.
    @Override //
    final HistoryToken setSave0(final String value) {
        return this;
    }
}
