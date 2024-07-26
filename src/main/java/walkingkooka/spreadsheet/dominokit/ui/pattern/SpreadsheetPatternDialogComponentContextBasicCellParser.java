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

import walkingkooka.plugin.PluginSelectorLike;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellParserSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellParserSelectHistoryToken;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link SpreadsheetPatternDialogComponentContext} for editing patterns for a cell parse pattern.
 */
final class SpreadsheetPatternDialogComponentContextBasicCellParser extends SpreadsheetPatternDialogComponentContextBasicCell {

    static SpreadsheetPatternDialogComponentContextBasicCellParser with(final AppContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetPatternDialogComponentContextBasicCellParser(context);
    }

    private SpreadsheetPatternDialogComponentContextBasicCellParser(final AppContext context) {
        super(context);
    }

    @Override
    public SpreadsheetPatternKind[] filteredPatternKinds() {
        return SpreadsheetPatternKind.parseValues();
    }

    // ComponentLifecycleMatcher........................................................................................

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetCellParserSaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetCellParserSelectHistoryToken;
    }

    @Override
    Optional<? extends PluginSelectorLike<?>> undoFormatterOrParser(final SpreadsheetCell cell) {
        return cell.parser();
    }
}
