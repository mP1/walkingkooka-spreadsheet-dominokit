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
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Function;

/**
 * Creates a sub menu for the current {@link walkingkooka.spreadsheet.reference.SpreadsheetSelection}, holding two or more
 * insert menus.
 * </pre>
 */
final class SpreadsheetSelectionMenuInsert {

    static void build(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                      final SpreadsheetContextMenu menu,
                      final SpreadsheetSelectionMenuContext context) {
        buildInsert(
            historyToken,
            menu,
            context
        );
    }

    private static void buildInsert(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                                    final SpreadsheetContextMenu menu,
                                    final SpreadsheetSelectionMenuContext context) {
        {
            insertColumns(historyToken, menu, context);
        }
        menu.separator();
        {
            insertRows(historyToken, menu, context);
        }
    }

    private static void insertColumns(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                                      final SpreadsheetContextMenu menu,
                                      final SpreadsheetSelectionMenuContext context) {
        final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection = historyToken.anchoredSelection();
        final SpreadsheetSelection selection = anchoredSpreadsheetSelection.selection();

        final SpreadsheetSelection selectionNotLabel = context.resolveIfLabel(selection)
            .orElse(null);

        if (null != selectionNotLabel && (selection.isColumnOrColumnRange() | selection.isExternalReference())) {
            final HistoryToken columnHistoryToken = historyToken.setAnchoredSelection(
                Optional.of(
                    selectionNotLabel.toColumnOrColumnRange()
                        .setAnchor(
                            anchoredSpreadsheetSelection.anchor()
                                .toColumnOrColumnRangeAnchor()
                        )
                )
            );

            final String beforeIdPrefix = context.idPrefix() + "column-insert-before";

            insertSubMenu(
                menu.subMenu(
                    beforeIdPrefix + SpreadsheetElementIds.SUB_MENU,
                    "Insert before column",
                    SpreadsheetIcons.columnInsertBefore()
                ),
                beforeIdPrefix,
                columnHistoryToken::insertBefore
            );

            final String afterIdPrefix = context.idPrefix() + "column-insert-after";

            insertSubMenu(
                menu.subMenu(
                    afterIdPrefix + SpreadsheetElementIds.SUB_MENU,
                    "Insert after column",
                    SpreadsheetIcons.columnInsertAfter()
                ),
                afterIdPrefix,
                columnHistoryToken::insertAfter
            );
        }
    }

    private static void insertRows(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                                   final SpreadsheetContextMenu menu,
                                   final SpreadsheetSelectionMenuContext context) {
        final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection = historyToken.anchoredSelection();
        final SpreadsheetSelection selection = anchoredSpreadsheetSelection.selection();

        final SpreadsheetSelection selectionNotLabel = context.resolveIfLabel(selection)
            .orElse(null);

        if (null != selectionNotLabel && (selection.isRowOrRowRange() | selection.isExternalReference())) {
            final HistoryToken rowHistoryToken = historyToken.setAnchoredSelection(
                Optional.of(
                    selectionNotLabel.toRowOrRowRange()
                        .setAnchor(
                            anchoredSpreadsheetSelection.anchor()
                                .toRowOrRowRangeAnchor()
                        )
                )
            );

            final String beforeIdPrefix = context.idPrefix() + "row-insert-before";

            insertSubMenu(
                menu.subMenu(
                    beforeIdPrefix + SpreadsheetElementIds.SUB_MENU,
                    "Insert before row",
                    SpreadsheetIcons.rowInsertBefore()
                ),
                beforeIdPrefix,
                rowHistoryToken::insertBefore
            );

            final String afterIdPrefix = context.idPrefix() + "row-insert-after";

            insertSubMenu(
                menu.subMenu(
                    afterIdPrefix + SpreadsheetElementIds.SUB_MENU,
                    "Insert after row",
                    SpreadsheetIcons.rowInsertAfter()
                ),
                afterIdPrefix,
                rowHistoryToken::insertAfter
            );
        }
    }

    private static void insertSubMenu(final SpreadsheetContextMenu menu,
                                      final String idPrefix,
                                      final Function<OptionalInt, HistoryToken> setCount) {
        for (int i = 1; i <= 3; i++) {
            menu.item(
                setCount.apply(
                    OptionalInt.of(i)
                ).contextMenuItem(
                    idPrefix + '-' + i + SpreadsheetElementIds.MENU_ITEM,
                    String.valueOf(i)
                )
            );
        }

        // insert a url which will display a modal to prompt the user for the actual count
        menu.item(
            setCount.apply(
                OptionalInt.empty()
            ).contextMenuItem(
                idPrefix + "-prompt" + SpreadsheetElementIds.MENU_ITEM,
                "..."
            )
        );
    }

    /**
     * Stop creation
     */
    private SpreadsheetSelectionMenuInsert() {
        throw new UnsupportedOperationException();
    }
}
