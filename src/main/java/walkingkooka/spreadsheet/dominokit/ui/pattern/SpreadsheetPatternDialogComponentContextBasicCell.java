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
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;

import java.util.Optional;

/**
 * A {@link SpreadsheetPatternDialogComponentContext} for editing patterns for a cell.
 */
abstract class SpreadsheetPatternDialogComponentContextBasicCell extends SpreadsheetPatternDialogComponentContextBasic {

    SpreadsheetPatternDialogComponentContextBasicCell(final AppContext context) {
        super(context);
    }

    @Override
    public final String undo() {
        String text = "";

        final Optional<SpreadsheetCell> maybeCell = this.context.spreadsheetViewportCache()
                .cell(
                        this.historyToken()
                                .cast(SpreadsheetAnchoredSelectionHistoryToken.class)
                                .anchoredSelection()
                                .selection()
                );
        if (maybeCell.isPresent()) {
            text = this.undoFormatterOrParser(maybeCell.get())
                    .map(Object::toString)
                    .orElse("");
        }

        return text;
    }

    abstract Optional<? extends PluginSelectorLike<?>> undoFormatterOrParser(final SpreadsheetCell cell);
}
