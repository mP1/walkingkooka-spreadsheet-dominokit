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
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;

import java.util.Objects;
import java.util.Optional;

public abstract class SpreadsheetCellParserHistoryToken extends SpreadsheetCellHistoryToken {
    SpreadsheetCellParserHistoryToken(final SpreadsheetId id,
                                      final SpreadsheetName name,
                                      final AnchoredSpreadsheetSelection anchoredSelection,
                                      final Optional<SpreadsheetPatternKind> spreadsheetPatternKind,
                                      final Optional<SpreadsheetParserSelector> spreadsheetParserSelector) {
        super(id, name, anchoredSelection);
        this.spreadsheetPatternKind = Objects.requireNonNull(spreadsheetPatternKind, "spreadsheetPatternKind");
        if (spreadsheetPatternKind.isPresent()) {
            final SpreadsheetPatternKind kind = spreadsheetPatternKind.get();
            if (kind.isFormatPattern()) {
                throw new IllegalArgumentException("Invalid kind " + kind);
            }
        }
        this.spreadsheetParserSelector = Objects.requireNonNull(spreadsheetParserSelector, "spreadsheetParserSelector");
    }

    final Optional<SpreadsheetPatternKind> spreadsheetPatternKind;

    final Optional<SpreadsheetParserSelector> spreadsheetParserSelector;

    @Override //
    final HistoryToken setFormatter() {
        return HistoryToken.cellFormatterUnselect(
                this.id(),
                this.name(),
                this.anchoredSelection()
        );
    }

    @Override //
    final HistoryToken setParser() {
        return HistoryToken.cellParserUnselect(
                this.id(),
                this.name(),
                this.anchoredSelection()
        );
    }

    @Override //
    final HistoryToken replacePatternKind(final Optional<SpreadsheetPatternKind> patternKind) {
        return patternKind.isPresent() ?
                this.replacePatternKind0(patternKind.get()) :
                this.setParser();
    }

    abstract HistoryToken replacePatternKind0(final SpreadsheetPatternKind patternKind);

    @Override
    public final HistoryToken setFormula() {
        return setFormula0();
    }

    @Override //
    final UrlFragment cellUrlFragment() {
        return this.parserUrlFragment();
    }

    abstract UrlFragment parserUrlFragment();
}
