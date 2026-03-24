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
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;

import java.util.Collection;
import java.util.Optional;

final class SpreadsheetSelectionMenuValuesDateTimeSymbols extends SpreadsheetSelectionMenuValues<DateTimeSymbols> {

    static SpreadsheetSelectionMenuValuesDateTimeSymbols with(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                                                              final SpreadsheetContextMenu menu,
                                                              final SpreadsheetSelectionMenuContext context) {
        return new SpreadsheetSelectionMenuValuesDateTimeSymbols(
            historyToken,
            menu,
            context
        );
    }

    private SpreadsheetSelectionMenuValuesDateTimeSymbols(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
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
            SpreadsheetIcons.dateTimeSymbolsRemove()
        );
    }

    @Override //
    Collection<DateTimeSymbols> recentValues() {
        return this.context.recentDateTimeSymbols();
    }

    @Override //
    String recentText(final DateTimeSymbols symbols) {
        return symbols.text(); // TODO need a better text such as Locale.AU and 'DIFFERENT_PLUS'
    }

    @Override //
    Class<DateTimeSymbols> type() {
        return DateTimeSymbols.class;
    }
}
