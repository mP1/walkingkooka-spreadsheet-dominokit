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

package walkingkooka.spreadsheet.dominokit.ui.pattern;

import walkingkooka.Cast;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellPatternSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellPatternSelectHistoryToken;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.format.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

import java.util.Optional;

/**
 * A {@link SpreadsheetPatternDialogComponentContext} for editing patterns for a cell.
 */
abstract class SpreadsheetPatternDialogComponentContextBasicCell extends SpreadsheetPatternDialogComponentContextBasic {

    SpreadsheetPatternDialogComponentContextBasicCell(final AppContext context) {
        super(context);
    }

    // ComponentLifecycleMatcher........................................................................................

    @Override
    public final boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetCellPatternSaveHistoryToken;
    }

    // SpreadsheetPatternDialogComponentContext.........................................................................

    /**
     * Returns the {@link SpreadsheetPattern} for the cell.
     */
    @Override
    public Optional<SpreadsheetPattern> undo() {
        Optional<SpreadsheetPattern> pattern = Optional.empty();

        final AppContext context = this.context;

        final Optional<SpreadsheetCell> maybeCell = context.spreadsheetViewportCache()
                .cell(
                this.historyToken()
                        .cast(SpreadsheetCellPatternSelectHistoryToken.class)
                        .anchoredSelection()
                        .selection()
        );
        if (maybeCell.isPresent()) {
            final SpreadsheetCell cell = maybeCell.get();
            final SpreadsheetPatternKind patternKind = this.patternKind();

            pattern = Cast.to(
                    patternKind.isFormatPattern() ?
                            cell.formatter()
                                    .flatMap(SpreadsheetFormatterSelector::spreadsheetFormatPattern) :
                            cell.parser()
                                    .flatMap(SpreadsheetParserSelector::spreadsheetParsePattern)
            );
        }

        return pattern;
    }
}
