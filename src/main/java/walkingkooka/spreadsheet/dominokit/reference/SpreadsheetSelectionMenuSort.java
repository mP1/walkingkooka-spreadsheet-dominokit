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
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorDirection;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuItem;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReferenceOrRange;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CharSequences;

import java.util.Collection;

/**
 * Builds the context menu for SORT for column, rows and cells. Context menus for column/row should only call this once,
 * a range will call this with the anchor column or row, eg columns=A:C anchor=BOTTOM will call this with column=C.
 * Context menus for cell/cell-range will need to call this twice, once to sort columns and another to sort rows.
 */
final class SpreadsheetSelectionMenuSort {

    static void build(final HistoryToken historyToken,
                      final SpreadsheetColumnOrRowReferenceOrRange columnOrRow,
                      final String idPrefix,
                      final Icon<?> icon,
                      final Collection<SpreadsheetComparatorName> comparatorNames,
                      final SpreadsheetContextMenu menu) {
        // PREFIX-

        comparatorsMenuItem(
            historyToken,
            columnOrRow,
            idPrefix,
            comparatorNames,
            menu.subMenu(
                CharSequences.subSequence(idPrefix, 0, -1) + SpreadsheetElementIds.SUB_MENU, // -1 removes trailing minus because SUB_MENU includes a minus at the start
                "Sort " + columnOrRow.textLabel(),
                icon
            )
        );
    }

    private static void comparatorsMenuItem(final HistoryToken historyToken,
                                            final SpreadsheetColumnOrRowReferenceOrRange columnOrRow,
                                            final String idPrefix,
                                            final Collection<SpreadsheetComparatorName> comparatorNames,
                                            final SpreadsheetContextMenu menu) {

        // for each comparator info build a sub menu.
        for (final SpreadsheetComparatorName name : comparatorNames) {
            final String subMenuId = idPrefix + name.value();

            final SpreadsheetContextMenu subMenu = menu.subMenu(
                subMenuId + SpreadsheetElementIds.SUB_MENU,
                CaseKind.kebabToTitle(
                    name.value()
                )
            );

            final String upDownPrefixId = subMenuId + '-';

            // comparator-name / UP
            subMenu.item(
                upOrDownMenuItem(
                    upDownPrefixId,
                    columnOrRow,
                    name,
                    SpreadsheetComparatorDirection.UP,
                    historyToken
                )
            );

            // comparator-name / DOWN
            subMenu.item(
                upOrDownMenuItem(
                    upDownPrefixId,
                    columnOrRow,
                    name,
                    SpreadsheetComparatorDirection.DOWN,
                    historyToken
                )
            );
        }

        menu.separator();

        menu.item(
            editMenuItem(
                idPrefix,
                columnOrRow,
                historyToken
            )
        );
    }

    // TODO add Icon
    private static SpreadsheetContextMenuItem upOrDownMenuItem(final String idPrefix,
                                                               final SpreadsheetColumnOrRowReferenceOrRange columnOrRow,
                                                               final SpreadsheetComparatorName name,
                                                               final SpreadsheetComparatorDirection direction,
                                                               final HistoryToken historyToken) {
        return historyToken.setSortSave(
            name.setDirection(direction)
                .setColumnOrRow(columnOrRow)
                .list()
        ).contextMenuItem(
            idPrefix + direction + SpreadsheetElementIds.MENU_ITEM,
            CaseKind.SNAKE.change(
                direction.name(),
                CaseKind.TITLE
            )
        );
    }

    private static SpreadsheetContextMenuItem editMenuItem(final String idPrefix,
                                                           final SpreadsheetColumnOrRowReferenceOrRange columnOrRow,
                                                           final HistoryToken historyToken) {
        return historyToken.setSortEdit(columnOrRow + "=")
            .contextMenuItem(
                idPrefix + "edit" + SpreadsheetElementIds.MENU_ITEM,
                "Edit"
            );
    }
}
