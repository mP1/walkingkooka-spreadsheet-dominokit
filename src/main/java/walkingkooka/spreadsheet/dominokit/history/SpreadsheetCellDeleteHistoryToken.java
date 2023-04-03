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

public final class SpreadsheetCellDeleteHistoryToken extends SpreadsheetCellHistoryToken {

    static SpreadsheetCellDeleteHistoryToken with(final SpreadsheetId id,
                                                  final SpreadsheetName name,
                                                  final SpreadsheetViewportSelection viewportSelection) {
        return new SpreadsheetCellDeleteHistoryToken(
                id,
                name,
                viewportSelection
        );
    }

    private SpreadsheetCellDeleteHistoryToken(final SpreadsheetId id,
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
        return DELETE;
    }

    @Override
    SpreadsheetHistoryToken setDifferentIdOrName(final SpreadsheetId id,
                                                 final SpreadsheetName name) {
        return new SpreadsheetCellDeleteHistoryToken(
                id,
                name,
                this.viewportSelection()
        );
    }

    @Override
    SpreadsheetNameHistoryToken formulaHistoryToken() {
        return this;
    }

    @Override
    SpreadsheetNameHistoryToken pattern(final SpreadsheetPatternKind patternKind) {
        return this;
    }

    @Override
    SpreadsheetNameHistoryToken save(final String value) {
        return this;
    }

    @Override
    void onHashChange0(final HistoryToken previous,
                       final AppContext context) {
        context.spreadsheetDeltaFetcher()
                .deleteDelta(
                        this.id(),
                        this.viewportSelection()
                );
    }
}
