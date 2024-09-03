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

package walkingkooka.spreadsheet.dominokit.parser;

import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellParserSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellParserSelectHistoryToken;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelector;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link SpreadsheetParserSelectorDialogComponentContext} for editing a cell {@link SpreadsheetParserSelector}.
 */
final class SpreadsheetParserSelectorDialogComponentContextBasicCell extends SpreadsheetParserSelectorDialogComponentContextBasic {

    static SpreadsheetParserSelectorDialogComponentContextBasicCell with(final AppContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetParserSelectorDialogComponentContextBasicCell(context);
    }

    private SpreadsheetParserSelectorDialogComponentContextBasicCell(final AppContext context) {
        super(context);
    }

    @Override
    public String dialogTitle() {
        return "Parser";
    }

    @Override
    public boolean shouldShowTabs() {
        return false;
    }

    @Override
    public String undo() {
        String text = "";

        final Optional<SpreadsheetCell> maybeCell = this.context.spreadsheetViewportCache()
                .cell(
                        this.historyToken()
                                .cast(SpreadsheetAnchoredSelectionHistoryToken.class)
                                .anchoredSelection()
                                .selection()
                );
        if (maybeCell.isPresent()) {
            text = maybeCell.get()
                    .parser()
                    .map(SpreadsheetParserSelector::toString)
                    .orElse("");
        }

        return text;
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
}
