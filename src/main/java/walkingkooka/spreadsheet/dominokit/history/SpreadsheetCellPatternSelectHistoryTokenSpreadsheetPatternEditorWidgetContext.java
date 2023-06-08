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

import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.pattern.SpreadsheetPatternEditorWidgetContext;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.text.CaseKind;

import java.util.Optional;

final class SpreadsheetCellPatternSelectHistoryTokenSpreadsheetPatternEditorWidgetContext implements SpreadsheetPatternEditorWidgetContext {

    static SpreadsheetCellPatternSelectHistoryTokenSpreadsheetPatternEditorWidgetContext with(final AppContext context) {

        return new SpreadsheetCellPatternSelectHistoryTokenSpreadsheetPatternEditorWidgetContext(context);
    }

    private SpreadsheetCellPatternSelectHistoryTokenSpreadsheetPatternEditorWidgetContext(final AppContext context) {
        this.context = context;
    }

    @Override
    public SpreadsheetPatternKind patternKind() {
        return this.historyToken().patternKind();
    }

    // Edit date/time format
    // Edit text format
    @Override
    public String title() {
        return "Edit " +
                CaseKind.SNAKE.change(
                        this.patternKind().name(),
                        CaseKind.TITLE
                ).replace("Pattern", "pattern");
    }

    /**
     * Takes the pattern for the matching {@link walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern}.
     */
    @Override
    public String loaded() {
        String loaded = ""; // if cell is absent or missing this property use a default of empty pattern.

        final Optional<SpreadsheetCell> maybeCell = this.context.viewportCell(
                this.historyToken()
                        .viewportSelection()
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

    /**
     * Save the pattern and push the new {@link HistoryToken}
     */
    @Override
    public void save(final String pattern) {
        this.context.pushHistoryToken(
                this.historyToken().setSave(pattern)
        );
    }

    /**
     * Pushes a new {@link HistoryToken} which will result in the removal of the pattern from the selection.
     */
    @Override
    public void remove() {
        this.context.pushHistoryToken(
                this.historyToken().setSave("")
        );
    }

    // clear the pattern part leaving just the selection history token.
    @Override
    public void close() {
        this.historyToken().pushViewportSelectionHistoryToken(
                this.context
        );
    }

    private SpreadsheetCellPatternHistoryToken historyToken() {
        return this.context.historyToken()
                .cast(SpreadsheetCellPatternHistoryToken.class);
    }

    @Override
    public void debug(final Object... values) {
        this.context.debug(values);
    }

    @Override
    public void error(final Object... values) {
        this.context.error(values);
    }

    private final AppContext context;
}
