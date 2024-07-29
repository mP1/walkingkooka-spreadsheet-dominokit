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
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;

import java.util.Optional;

public final class SpreadsheetCellParserSaveHistoryToken extends SpreadsheetCellParserHistoryToken {

    static SpreadsheetCellParserSaveHistoryToken with(final SpreadsheetId id,
                                                      final SpreadsheetName name,
                                                      final AnchoredSpreadsheetSelection anchoredSelection,
                                                      final SpreadsheetPatternKind spreadsheetPatternKind,
                                                      final Optional<SpreadsheetParserSelector> spreadsheetParserSelector) {
        return new SpreadsheetCellParserSaveHistoryToken(
                id,
                name,
                anchoredSelection,
                spreadsheetPatternKind,
                spreadsheetParserSelector
        );
    }

    private SpreadsheetCellParserSaveHistoryToken(final SpreadsheetId id,
                                                  final SpreadsheetName name,
                                                  final AnchoredSpreadsheetSelection anchoredSelection,
                                                  final SpreadsheetPatternKind spreadsheetPatternKind,
                                                  final Optional<SpreadsheetParserSelector> spreadsheetParserSelector) {
        super(
                id,
                name,
                anchoredSelection,
                Optional.of(spreadsheetPatternKind),
                spreadsheetParserSelector
        );
    }

    @Override
    public HistoryToken clearAction() {
        return HistoryToken.cellParserSelect(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                this.spreadsheetPatternKind.get()
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
                this.spreadsheetPatternKind.get(),
                this.spreadsheetParserSelector
        );
    }

    @Override//
    HistoryToken replacePatternKind0(final SpreadsheetPatternKind patternKind) {
        return new SpreadsheetCellParserSaveHistoryToken(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                patternKind,
                this.spreadsheetParserSelector
        );
    }

    @Override
    HistoryToken setSave0(final String value) {
        final SpreadsheetPatternKind kind = this.spreadsheetPatternKind.get();

        return new SpreadsheetCellParserSaveHistoryToken(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                kind,
                Optional.ofNullable(
                        value.isEmpty() ?
                                null :
                                SpreadsheetParserSelector.parse(value)
                )
        );
    }

    // cell/A1/parser/SpreadsheetPatternKind/save/SpreadsheetParserSelector
    @Override //
    UrlFragment parserUrlFragment() {
        return this.spreadsheetPatternKind.get()
                .urlFragment()
                .appendSlashThen(
                        this.saveUrlFragment(this.spreadsheetParserSelector)
                );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.pushHistoryToken(previous);

        context.spreadsheetDeltaFetcher()
                .saveParser(
                        this.id(),
                        this.anchoredSelection().selection(),
                        this.spreadsheetParserSelector
                );
    }
}
