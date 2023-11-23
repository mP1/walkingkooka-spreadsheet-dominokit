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

import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellPatternSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellPatternSelectHistoryToken;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

import java.util.Optional;

/**
 * A {@link SpreadsheetPatternEditorComponentContext} for editing patterns for a cell.
 */
abstract class SpreadsheetPatternEditorComponentContextBasicCell extends SpreadsheetPatternEditorComponentContextBasic {

    SpreadsheetPatternEditorComponentContextBasicCell(final AppContext context) {
        super(context);
    }

    // ComponentLifecycleMatcher........................................................................................

    @Override
    public final boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetCellPatternSaveHistoryToken;
    }

    // SpreadsheetPatternEditorComponentContext.........................................................................

    /**
     * Returns the pattern text for the current {@link #patternKind()}.
     */
    @Override
    public final String loaded() {
        String loaded = ""; // if cell is absent or missing this property use a default of empty pattern.

        final Optional<SpreadsheetCell> maybeCell = this.context.viewportCell(
                this.historyToken()
                        .cast(SpreadsheetCellPatternSelectHistoryToken.class)
                        .selection()
                        .selection()
        );
        if (maybeCell.isPresent()) {
            final SpreadsheetCell cell = maybeCell.get();
            final SpreadsheetPatternKind patternKind = this.patternKind();

            final Optional<? extends SpreadsheetPattern> maybePattern = patternKind.isFormatPattern() ?
                    cell.formatPattern() :
                    cell.parsePattern();
            if (maybePattern.isPresent()) {
                final SpreadsheetPattern pattern = maybePattern.get();
                if (patternKind == pattern.kind()) {
                    loaded = pattern.text();
                }
            }
        }

        return loaded;
    }
}
