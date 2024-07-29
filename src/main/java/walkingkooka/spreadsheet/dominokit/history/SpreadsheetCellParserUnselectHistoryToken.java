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
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;

import java.util.Optional;

/**
 * This {@link HistoryToken} selects the toolbar PARSER
 */
public final class SpreadsheetCellParserUnselectHistoryToken extends SpreadsheetCellParserHistoryToken {

    static SpreadsheetCellParserUnselectHistoryToken with(final SpreadsheetId id,
                                                          final SpreadsheetName name,
                                                          final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetCellParserUnselectHistoryToken(
                id,
                name,
                anchoredSelection
        );
    }

    private SpreadsheetCellParserUnselectHistoryToken(final SpreadsheetId id,
                                                      final SpreadsheetName name,
                                                      final AnchoredSpreadsheetSelection anchoredSelection) {
        super(
                id,
                name,
                anchoredSelection,
                Optional.empty(), // SpreadsheetPatternKind
                Optional.empty() // SpreadsheetParserSelector
        );
    }

    @Override
    public HistoryToken clearAction() {
        return this;
    }

    @Override
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetCellParserUnselectHistoryToken(
                id,
                name,
                anchoredSelection
        );
    }

    @Override//
    HistoryToken replacePatternKind0(final SpreadsheetPatternKind patternKind) {
        return HistoryToken.cellParserSelect(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                patternKind
        );
    }

    @Override
    HistoryToken setSave0(final String value) {
        return this; // cant "save", because of missing SpreadsheetParserSelector
    }

    // cell/A1/parser
    @Override //
    UrlFragment parserUrlFragment() {
        return PARSER;
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // TODO give focus to toolbar parser link
    }
}
