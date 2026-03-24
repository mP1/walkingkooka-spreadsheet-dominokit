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
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.value.SpreadsheetCell;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

final class SpreadsheetSelectionMenuValuesLocale extends SpreadsheetSelectionMenuValues<Locale> {

    static SpreadsheetSelectionMenuValuesLocale with(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                                                     final SpreadsheetContextMenu menu,
                                                     final SpreadsheetSelectionMenuContext context) {
        return new SpreadsheetSelectionMenuValuesLocale(
            historyToken,
            menu,
            context
        );
    }

    private SpreadsheetSelectionMenuValuesLocale(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
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
            SpreadsheetIcons.localeRemove()
        );
    }

    @Override //
    Collection<Locale> recentValues() {
        return this.context.recentLocales();
    }

    @Override //
    String recentText(final Locale locale) {
        return context.localeText(locale)
            .orElse(locale.toLanguageTag());
    }

    @Override
    Optional<Locale> spreadsheetCellValue(final SpreadsheetCell cell) {
        return cell.locale();
    }

    @Override //
    Class<Locale> type() {
        return Locale.class;
    }
}
