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

import walkingkooka.Value;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

import java.util.Optional;

public final class SpreadsheetCellParserSaveHistoryToken extends SpreadsheetCellParserHistoryToken
    implements Value<Optional<SpreadsheetParserSelector>> {

    static SpreadsheetCellParserSaveHistoryToken with(final SpreadsheetId id,
                                                      final SpreadsheetName name,
                                                      final AnchoredSpreadsheetSelection anchoredSelection,
                                                      final Optional<SpreadsheetParserSelector> spreadsheetParserSelector) {
        return new SpreadsheetCellParserSaveHistoryToken(
            id,
            name,
            anchoredSelection,
            spreadsheetParserSelector
        );
    }

    private SpreadsheetCellParserSaveHistoryToken(final SpreadsheetId id,
                                                  final SpreadsheetName name,
                                                  final AnchoredSpreadsheetSelection anchoredSelection,
                                                  final Optional<SpreadsheetParserSelector> spreadsheetParserSelector) {
        super(
            id,
            name,
            anchoredSelection,
            spreadsheetParserSelector
        );
    }

    @Override
    public Optional<SpreadsheetParserSelector> value() {
        return this.spreadsheetParserSelector();
    }

    @Override
    public HistoryToken clearAction() {
        return HistoryToken.cellParserSelect(
            this.id,
            this.name,
            this.anchoredSelection()
        );
    }

    @Override
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetCellParserSaveHistoryToken(
            id,
            name,
            anchoredSelection,
            this.spreadsheetParserSelector
        );
    }

    // cell/A1/parser/save/SpreadsheetParserSelector
    @Override //
    UrlFragment parserUrlFragment() {
        return saveUrlFragment(this.value());
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.pushHistoryToken(previous);

        context.spreadsheetDeltaFetcher()
            .patchParser(
                this.id,
                this.anchoredSelection().selection(),
                this.spreadsheetParserSelector
            );
    }
}
