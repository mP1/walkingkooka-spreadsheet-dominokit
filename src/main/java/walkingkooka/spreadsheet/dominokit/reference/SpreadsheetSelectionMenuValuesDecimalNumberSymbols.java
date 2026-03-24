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

package walkingkooka.spreadsheet.dominokit.reference;

import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.value.SpreadsheetCell;

import java.util.Collection;
import java.util.Optional;

final class SpreadsheetSelectionMenuValuesDecimalNumberSymbols extends SpreadsheetSelectionMenuValues<DecimalNumberSymbols> {

    static SpreadsheetSelectionMenuValuesDecimalNumberSymbols with(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                                                                   final SpreadsheetContextMenu menu,
                                                                   final SpreadsheetSelectionMenuContext context) {
        return new SpreadsheetSelectionMenuValuesDecimalNumberSymbols(
            historyToken,
            menu,
            context
        );
    }

    private SpreadsheetSelectionMenuValuesDecimalNumberSymbols(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                                                               final SpreadsheetContextMenu menu,
                                                               final SpreadsheetSelectionMenuContext context) {
        super(historyToken, menu, context);
    }

    @Override
    void values() {
        // NOP
    }

    @Override
    Optional<Icon<?>> clearIcon() {
        return Optional.of(
            SpreadsheetIcons.decimalNumberSymbolsRemove()
        );
    }

    @Override //
    Collection<DecimalNumberSymbols> recentValues() {
        return this.context.recentDecimalNumberSymbols();
    }

    @Override //
    String recentText(final DecimalNumberSymbols symbols) {
        return symbols.text(); // TODO need a better text
    }

    @Override
    Optional<DecimalNumberSymbols> spreadsheetCellValue(final SpreadsheetCell cell) {
        return cell.decimalNumberSymbols();
    }

    @Override //
    Class<DecimalNumberSymbols> type() {
        return DecimalNumberSymbols.class;
    }
}
