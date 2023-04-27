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
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

public final class SpreadsheetCellClearHistoryToken extends SpreadsheetCellHistoryToken {

    static SpreadsheetCellClearHistoryToken with(final SpreadsheetId id,
                                                 final SpreadsheetName name,
                                                 final SpreadsheetViewportSelection viewportSelection) {
        return new SpreadsheetCellClearHistoryToken(
                id,
                name,
                viewportSelection
        );
    }

    private SpreadsheetCellClearHistoryToken(final SpreadsheetId id,
                                             final SpreadsheetName name,
                                             final SpreadsheetViewportSelection viewportSelection) {
        super(
                id,
                name,
                viewportSelection
        );
    }

    @Override
    UrlFragment cellUrlFragment() {
        return CLEAR;
    }

    @Override
    public HistoryToken formulaHistoryToken() {
        return this;
    }

    @Override
    public HistoryToken formulaSaveHistoryToken(final String text) {
        throw new UnsupportedOperationException();
    }

    @Override
    HistoryToken pattern(final SpreadsheetPatternKind patternKind) {
        return this;
    }

    @Override
    HistoryToken save(final String value) {
        return this;
    }

    @Override
    public HistoryToken setIdAndName(final SpreadsheetId id,
                                     final SpreadsheetName name) {
        return with(
                id,
                name,
                this.viewportSelection()
        );
    }

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        // POST cell clear
        this.deltaClearSelectionAndPushViewportSelectionHistoryToken(context);
    }
}
