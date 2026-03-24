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
import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.plugin.PluginSelectorLike;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuItem;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.text.CaseKind;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;
import walkingkooka.validation.provider.ValidatorSelector;

import java.util.Collection;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Template class that provides helpers to create numerous sub-menus and their items.
 */
abstract class SpreadsheetSelectionMenuValues<T> implements TreePrintable {

    static SpreadsheetSelectionMenuValues<Currency> currency(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                                                             final SpreadsheetContextMenu menu,
                                                             final SpreadsheetSelectionMenuContext context) {
        return SpreadsheetSelectionMenuValuesCurrency.with(
            historyToken,
            menu,
            context
        );
    }

    static SpreadsheetSelectionMenuValues<DateTimeSymbols> dateTimeSymbols(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                                                                           final SpreadsheetContextMenu menu,
                                                                           final SpreadsheetSelectionMenuContext context) {
        return SpreadsheetSelectionMenuValuesDateTimeSymbols.with(
            historyToken,
            menu,
            context
        );
    }

    static SpreadsheetSelectionMenuValues<DecimalNumberSymbols> decimalNumberSymbols(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                                                                                     final SpreadsheetContextMenu menu,
                                                                                     final SpreadsheetSelectionMenuContext context) {
        return SpreadsheetSelectionMenuValuesDecimalNumberSymbols.with(
            historyToken,
            menu,
            context
        );
    }

    static SpreadsheetSelectionMenuValues<Locale> locale(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                                                         final SpreadsheetContextMenu menu,
                                                         final SpreadsheetSelectionMenuContext context) {
        return SpreadsheetSelectionMenuValuesLocale.with(
            historyToken,
            menu,
            context
        );
    }

    static SpreadsheetSelectionMenuValues<ValidatorSelector> validator(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                                                                       final SpreadsheetContextMenu menu,
                                                                       final SpreadsheetSelectionMenuContext context) {
        return SpreadsheetSelectionMenuValuesValidator.with(
            historyToken,
            menu,
            context
        );
    }

    SpreadsheetSelectionMenuValues(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                                   final SpreadsheetContextMenu menu,
                                   final SpreadsheetSelectionMenuContext context) {
        super();
        this.historyToken = Objects.requireNonNull(historyToken, "historyToken");

        final String title = this.type().getSimpleName();

        final String idPrefix = context.idPrefix() + title;
        this.idPrefix = idPrefix + "-";

        this.menu = Objects.requireNonNull(menu, "menu")
            .subMenu(
                idPrefix + SpreadsheetElementIds.SUB_MENU,
                selectorTextFix(title)
            );

        this.context = Objects.requireNonNull(context, "context");
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

    final void clear() {
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

    final void edit() {
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

    final void recents() {
        final Collection<T> values = this.recentValues();
        if (false == values.isEmpty()) {

            this.separator();

            int i = 0;

            final HistoryToken historyToken = this.historyToken;
            final SpreadsheetContextMenu menu = this.menu;

            final Predicate<T> checked = this.spreadsheetCellValuePredicate();

            for (final T value : values) {
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
                    ).checked(
                        checked.test(value)
                    )
                );

                i++;
            }
        }
    }

    abstract Collection<T> recentValues();

    /**
     * Provides the text for the recent menu item given a recent {@link Object}.
     */
    abstract String recentText(final T value);

    final Predicate<T> spreadsheetCellValuePredicate() {
        final T value = this.context.selectionSummary()
            .flatMap(this::spreadsheetCellValue)
            .orElse(null);
        return (T other) -> other.equals(value);
    }

    abstract Optional<T> spreadsheetCellValue(final SpreadsheetCell cell);

    final SpreadsheetAnchoredSelectionHistoryToken historyToken;

    final void separator() {
        this.menu.separator();
    }

    final SpreadsheetContextMenu menu;
    final SpreadsheetSelectionMenuContext context;

    final String idPrefix;

    abstract Class<T> type();

    // helper...........................................................................................................

    static String selectorToMenuItemText(final PluginSelectorLike<?> selector) {
        return CaseKind.kebabToTitle(
            selectorTextFix(
                selector.name()
                .value()
            )
        );
    }

    // ValidatorSelector -> Validator
    private static String selectorTextFix(final String text) {
        return text.replace(
            "Selector",
            ""
        );
    }

    // TreePrintable....................................................................................................

    @Override
    public final void printTree(final IndentingPrinter printer) {
        printer.print(this.getClass().getSimpleName());
        printer.indent();
        {
            this.menu.printTree(printer);
        }
        printer.outdent();
    }
}
