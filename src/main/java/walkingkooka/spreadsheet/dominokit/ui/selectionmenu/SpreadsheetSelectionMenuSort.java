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

package walkingkooka.spreadsheet.dominokit.ui.selectionmenu;

import walkingkooka.spreadsheet.compare.SpreadsheetComparatorDirection;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorInfo;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIds;
import walkingkooka.spreadsheet.dominokit.ui.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.ui.contextmenu.SpreadsheetContextMenuItem;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReference;
import walkingkooka.text.CaseKind;

import java.util.Set;

/**
 * Builds the context menu for SORT for column, rows and cells. Context menus for column/row should only call this once,
 * a range will call this with the anchor column or row, eg columns=A:C anchor=BOTTOM will call this with column=C.
 * Context menus for cell/cell-range will need to call this twice, once to sort columns and another to sort rows.
 */
final class SpreadsheetSelectionMenuSort {

    static void build(final HistoryToken historyToken,
                      final SpreadsheetColumnOrRowReference columnOrRow,
                      final String idPrefix,
                      final Set<SpreadsheetComparatorInfo> spreadsheetComparatorInfos,
                      final SpreadsheetContextMenu menu) {
        // PREFIX-column-
        final String subMenuIdPrefix = idPrefix + columnOrRow.textLabel().toLowerCase();

        comparatorsMenuItem(
                historyToken,
                columnOrRow,
                subMenuIdPrefix + '-',
                spreadsheetComparatorInfos,
                menu.subMenu(
                        subMenuIdPrefix + SpreadsheetIds.SUB_MENU,
                        "Sort " + columnOrRow.textLabel()
                )
        );
    }

    static void comparatorsMenuItem(final HistoryToken historyToken,
                                    final SpreadsheetColumnOrRowReference columnOrRow,
                                    final String idPrefix,
                                    final Set<SpreadsheetComparatorInfo> spreadsheetComparatorInfos,
                                    final SpreadsheetContextMenu menu) {

        // for each comparator info build a sub menu.
        for (final SpreadsheetComparatorInfo info : spreadsheetComparatorInfos) {
            final SpreadsheetComparatorName name = info.name();
            final String nameText = name.value();

            final String subMenuId = idPrefix + nameText;

            final SpreadsheetContextMenu subMenu = menu.subMenu(
                    subMenuId + SpreadsheetIds.SUB_MENU,
                    CaseKind.KEBAB.change(
                            nameText,
                            CaseKind.TITLE
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
                                                               final SpreadsheetColumnOrRowReference columnOrRow,
                                                               final SpreadsheetComparatorName name,
                                                               final SpreadsheetComparatorDirection direction,
                                                               final HistoryToken historyToken) {
        return historyToken.setSortSave(
                name.setDirection(direction)
                        .setColumnOrRow(columnOrRow)
                        .list()
        ).contextMenuItem(
                idPrefix + direction + SpreadsheetIds.MENU_ITEM,
                CaseKind.SNAKE.change(
                        direction.name(),
                        CaseKind.TITLE
                )
        );
    }

    private static SpreadsheetContextMenuItem editMenuItem(final String idPrefix,
                                                           final SpreadsheetColumnOrRowReference columnOrRow,
                                                           final HistoryToken historyToken) {
        return historyToken.setSortEdit(columnOrRow + "=")
                .contextMenuItem(
                        idPrefix + "edit" + SpreadsheetIds.MENU_ITEM,
                        "Edit"
                );
    }
}
