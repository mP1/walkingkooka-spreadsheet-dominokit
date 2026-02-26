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

import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuItem;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;

import java.util.Currency;
import java.util.Optional;

/**
 * Creates a sub menu for each {@link Currency}, clear, edit and recents.
 * <pre>
 * Currency
 *   Clear
 *   ---
 *   Edit
 *   ----
 *   Recent-1 TODO
 *   Recent-2
 * </pre>
 */
final class SpreadsheetSelectionMenuCurrency {

    static void build(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                      final SpreadsheetContextMenu menu,
                      final SpreadsheetSelectionMenuContext context) {
        buildCurrency(
            historyToken.currency(),
            menu,
            context.idPrefix() + "currency-",
            context
        );
    }

    private static void buildCurrency(final HistoryToken historyToken,
                                      final SpreadsheetContextMenu menu,
                                      final String idPrefix,
                                      final SpreadsheetSelectionMenuContext context) {
        buildClear(
            historyToken,
            menu,
            idPrefix
        );

        menu.separator();

        buildEdit(
            historyToken,
            menu,
            idPrefix
        );

        menu.separator();

        buildRecents(
            historyToken,
            menu,
            idPrefix,
            context
        );
    }

    private static void buildClear(final HistoryToken historyToken,
                                   final SpreadsheetContextMenu menu,
                                   final String idPrefix) {
        menu.item(
            SpreadsheetContextMenuItem.with(
                idPrefix + "clear" + SpreadsheetElementIds.MENU_ITEM,
                "Clear..."
            ).icon(
                Optional.of(
                    SpreadsheetIcons.currencyRemove()
                )
            ).historyToken(
                Optional.of(
                    historyToken.currency()
                        .clearSaveValue()
                )
            )
        );
    }

    private static void buildEdit(final HistoryToken historyToken,
                                  final SpreadsheetContextMenu menu,
                                  final String idPrefix) {
        menu.item(
            SpreadsheetContextMenuItem.with(
                idPrefix + "edit" + SpreadsheetElementIds.MENU_ITEM,
                "Edit..."
            ).historyToken(
                Optional.of(
                    historyToken
                )
            )
        );
    }

    private static void buildRecents(final HistoryToken historyToken,
                                     final SpreadsheetContextMenu menu,
                                     final String idPrefix,
                                     final SpreadsheetSelectionMenuContext context) {

        int i = 0;

        for (final Currency currency : context.recentCurrencies()) {
            final String text = context.currencyText(currency)
                .orElse(currency.getCurrencyCode());

            menu.item(
                SpreadsheetContextMenuItem.with(
                    idPrefix + "recent-" + i + SpreadsheetElementIds.MENU_ITEM,
                    text
                ).historyToken(
                    Optional.of(
                        historyToken.setSaveValue(
                            Optional.of(currency)
                        )
                    )
                )
            );

            i++;
        }
    }

    /**
     * Stop creation
     */
    private SpreadsheetSelectionMenuCurrency() {
        throw new UnsupportedOperationException();
    }
}
