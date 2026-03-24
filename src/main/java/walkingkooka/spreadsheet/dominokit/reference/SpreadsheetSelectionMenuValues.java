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
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuItem;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/**
 * Template class that provides helpers to create numerous sub-menus and their items.
 */
abstract class SpreadsheetSelectionMenuValues<T> implements TreePrintable {

    SpreadsheetSelectionMenuValues(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                                   final SpreadsheetContextMenu menu,
                                   final SpreadsheetSelectionMenuContext context) {
        super();
        this.historyToken = Objects.requireNonNull(historyToken, "historyToken");
        this.menu = Objects.requireNonNull(menu, "menu");
        this.context = Objects.requireNonNull(context, "context");

        this.idPrefix = context.idPrefix() + this.type().getSimpleName() + "-";
    }

    final void build() {
        final SpreadsheetContextMenu menu = this.menu;

        this.values();

        menu.separator();

        this.clear();

        this.menu.separator();

        this.edit();

        menu.separator();

        this.recents();
    }

    abstract void values();

    final void clear(//final HistoryToken historyToken,
                     //final SpreadsheetContextMenu menu
    ) {
        this.separator();

        this.menu.item(
            SpreadsheetContextMenuItem.with(
                this.idPrefix + "clear" + SpreadsheetElementIds.MENU_ITEM,
                "Clear..."
            ).icon(
                this.clearIcon()
            ).historyToken(
                Optional.of(
                    historyToken.clearSaveValue()
                )
            )
        );
    }

    abstract Optional<Icon<?>> clearIcon();

    final void edit(//final HistoryToken historyToken,
                    //final SpreadsheetContextMenu menu
    ) {
        this.separator();

        this.menu.item(
            SpreadsheetContextMenuItem.with(
                this.idPrefix + "edit" + SpreadsheetElementIds.MENU_ITEM,
                "Edit..."
            ).historyToken(
                Optional.of(this.historyToken)
            )
        );
    }

//    final void recents(final HistoryToken historyToken,
//                       final SpreadsheetContextMenu menu,
//                       final SpreadsheetSelectionMenuContext context) {
//
//        int i = 0;
//
//        for (final T currency : context.recentCurrencies()) {
//            final String text = context.currencyText(currency)
//                .orElse(currency.getCurrencyCode());
//
//            menu.item(
//                SpreadsheetContextMenuItem.with(
//                    this.idPrefix + "recent-" + i + SpreadsheetElementIds.MENU_ITEM,
//                    text
//                ).historyToken(
//                    Optional.of(
//                        historyToken.setSaveValue(
//                            Optional.of(currency)
//                        )
//                    )
//                )
//            );
//
//            i++;
//        }
//    }

    final void recents(//final HistoryToken historyToken,
                       //final Collection<T> values,
                       //final SpreadsheetContextMenu menu
    ) {
        this.separator();

        int i = 0;

        final HistoryToken historyToken = this.historyToken;

        for (final T value : this.recentValues()) {//context.recentCurrencies()) {
//            final String text = context.currencyText(value)
//                .orElse(value.getCurrencyCode());

            menu.item(
                SpreadsheetContextMenuItem.with(
                    this.idPrefix + "recent-" + i + SpreadsheetElementIds.MENU_ITEM,
                    this.recentText(value)
                ).historyToken(
                    Optional.of(
                        historyToken.setSaveValue(
                            Optional.of(value)
                        )
                    )
                )
            );

            i++;
        }
    }

    abstract Collection<T> recentValues();

    /**
     * Provides the text for the recent menu item given a recent {@link Object}.
     */
    abstract String recentText(final T value);

    final SpreadsheetAnchoredSelectionHistoryToken historyToken;

    final void separator() {
        this.menu.separator();
    }

    final SpreadsheetContextMenu menu;
    final SpreadsheetSelectionMenuContext context;

    final String idPrefix;

    abstract Class<T> type();

    // TreePrintable....................................................................................................

    @Override
    public final void printTree(final IndentingPrinter printer) {
        printer.print(this.type().getSimpleName());
        printer.indent();
        {
            this.menu.printTree(printer);
        }
        printer.outdent();
    }
}
