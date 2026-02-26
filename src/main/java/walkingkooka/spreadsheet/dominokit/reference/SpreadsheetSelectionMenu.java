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

import org.dominokit.domino.ui.utils.DominoElement;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.clipboard.SpreadsheetCellClipboardKind;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuItem;
import walkingkooka.spreadsheet.dominokit.hidezerovalues.HideZeroValues;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenOffsetAndCount;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.text.CaseKind;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Builds a context menu for any given {@link SpreadsheetSelection}.
 */
public final class SpreadsheetSelectionMenu implements PublicStaticHelper {

    /**
     * Renders a drop down menu attaching the context menu to the given {@link DominoElement}.
     * This should make it possible to attach a context menu to the cell in the viewport and the formula component.
     */
    public static void build(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                             final SpreadsheetContextMenu menu,
                             final SpreadsheetSelectionMenuContext context) {
        build0(
            historyToken.clearAction()
                .cast(SpreadsheetAnchoredSelectionHistoryToken.class),
            menu,
            context
        );
    }

    private static void build0(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                               final SpreadsheetContextMenu menu,
                               final SpreadsheetSelectionMenuContext context) {
        // show context menu
        final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection = historyToken.anchoredSelection();
        final SpreadsheetSelection selection = anchoredSpreadsheetSelection.selection();

        // CUT
        // COPY
        // PASTE
        // -----
        // STYLE
        // ----
        // FORMATTER
        // DATE TIME SYMBOLS
        // DECIMAL NUMBER SYMBOLS
        // LOCALE
        // VALUE TYPE
        // VALUE
        // VALIDATOR
        // HIDE ZERO VALUES
        // -----
        // DELETE
        // ------
        // INSERT XXX
        // -------
        // FREEZE
        // UNFREEZE
        // -------
        // LABELS
        // REFERENCES
        // -------
        // RELOAD

        if (selection.isExternalReference()) {
            clipboard(
                historyToken,
                menu,
                context
            );

            style(
                historyToken,
                menu,
                context
            );

            formatter(
                historyToken,
                menu,
                context
            );

            currency(
                historyToken,
                menu,
                context
            );

            dateTimeSymbols(
                historyToken,
                menu,
                context
            );

            decimalNumberSymbols(
                historyToken,
                menu,
                context
            );

            locale(
                historyToken,
                menu,
                context
            );

            valueTypes(
                historyToken,
                menu,
                context
            );

            value(
                historyToken,
                menu,
                context
            );

            validator(
                historyToken,
                menu,
                context
            );

            hideIfZero(
                historyToken,
                menu,
                context
            );
        }
        menu.separator();

        {
            clearDelete(
                historyToken,
                menu,
                context
            );
        }
        menu.separator();
        insert(historyToken, menu, context);
        menu.separator();
        {
            sortColumns(historyToken, anchoredSpreadsheetSelection, menu, context);
            sortRows(historyToken, anchoredSpreadsheetSelection, menu, context);
        }
        menu.separator();
        {
            freezeUnfreeze(historyToken, menu, context);
        }
        menu.separator();

        {
            if (selection.isExternalReference()) {
                label(
                    historyToken,
                    selection,
                    menu,
                    context
                );

                references(
                    historyToken,
                    selection,
                    menu,
                    context
                );
            }
        }

        menu.separator();
        {
            if (selection.isExternalReference()) {
                reload(
                    historyToken,
                    menu,
                    context
                );
            }
        }
    }

    private static void clipboard(final HistoryToken historyToken,
                                  final SpreadsheetContextMenu menu,
                                  final SpreadsheetSelectionMenuContext context) {
        final String idPrefix = context.idPrefix() + "clipboard-";

        {
            final String itemIdPrefix = idPrefix + "cut";

            clipboardCutCopyPaste(
                menu.subMenu(
                    itemIdPrefix + SpreadsheetElementIds.SUB_MENU,
                    "Cut",
                    SpreadsheetIcons.cut()
                ),
                itemIdPrefix + '-',
                historyToken::cut
            );
        }

        {
            final String itemIdPrefix = idPrefix + "copy";

            clipboardCutCopyPaste(
                menu.subMenu(
                    itemIdPrefix + SpreadsheetElementIds.SUB_MENU,
                    "Copy",
                    SpreadsheetIcons.copy()
                ),
                itemIdPrefix + '-',
                historyToken::copy
            );
        }

        {
            final String itemIdPrefix = idPrefix + "paste";

            clipboardCutCopyPaste(
                menu.subMenu(
                    itemIdPrefix + SpreadsheetElementIds.SUB_MENU,
                    "Paste",
                    SpreadsheetIcons.paste()
                ),
                itemIdPrefix + '-',
                historyToken::paste
            );

            // PASTE items are initially disabled and then async enabled after the clipboard is examined.
        }

        menu.separator();
    }

    private static void clipboardCutCopyPaste(final SpreadsheetContextMenu menu,
                                              final String idPrefix,
                                              final Function<SpreadsheetCellClipboardKind, HistoryToken> historyToken) {
        SpreadsheetContextMenu menu2 = menu;

        // Cut > Cell
        for (final SpreadsheetCellClipboardKind kind : SpreadsheetCellClipboardKind.menuItemValues()) {
            menu2 = menu2.item(
                SpreadsheetContextMenuItem.with(
                    clipboardCutCopyPasteMenuItemId(
                        idPrefix,
                        kind
                    ),
                    clipboardCutCopyPasteMenuItemText(kind)
                ).historyToken(
                    Optional.of(
                        historyToken.apply(kind)
                    )
                )
            );
        }
    }

    private static String clipboardCutCopyPasteMenuItemId(final String idPrefix,
                                                          final SpreadsheetCellClipboardKind kind) {
        return idPrefix +
            CaseKind.SNAKE.change(
                kind.name().toLowerCase(),
                CaseKind.KEBAB
            ) +
            SpreadsheetElementIds.MENU_ITEM;
    }

    // "Cell" | "Format pattern"
    private static String clipboardCutCopyPasteMenuItemText(final SpreadsheetCellClipboardKind kind) {
        return CaseKind.SNAKE.change(
            kind.name().toLowerCase(),
            CaseKind.TITLE
        );
    }

    private static void style(final HistoryToken historyToken,
                              final SpreadsheetContextMenu menu,
                              final SpreadsheetSelectionMenuContext context) {
        final SpreadsheetContextMenu subMenu = menu.subMenu(
            context.idPrefix() + "style" + SpreadsheetElementIds.SUB_MENU,
            "Style"
        );

        SpreadsheetSelectionMenuStyle.build(
            historyToken.cast(SpreadsheetAnchoredSelectionHistoryToken.class),
            subMenu,
            context
        );

        subMenu.disableIfEmpty();
    }

    private static void formatter(final HistoryToken historyToken,
                                  final SpreadsheetContextMenu menu,
                                  final SpreadsheetSelectionMenuContext context) {
        final SpreadsheetContextMenu subMenu = menu.subMenu(
            context.idPrefix() + "formatter" + SpreadsheetElementIds.SUB_MENU,
            "Formatter"
        );

        SpreadsheetSelectionMenuFormatter.build(
            historyToken.cast(SpreadsheetAnchoredSelectionHistoryToken.class),
            subMenu,
            context
        );

        subMenu.disableIfEmpty();
    }

    private static void currency(final HistoryToken historyToken,
                                 final SpreadsheetContextMenu menu,
                                 final SpreadsheetSelectionMenuContext context) {
        final SpreadsheetContextMenu subMenu = menu.subMenu(
            context.idPrefix() + "currency" + SpreadsheetElementIds.SUB_MENU,
            "Currency"
        );

        SpreadsheetSelectionMenuCurrency.build(
            historyToken.cast(SpreadsheetAnchoredSelectionHistoryToken.class),
            subMenu,
            context
        );

        subMenu.disableIfEmpty();
    }

    private static void dateTimeSymbols(final HistoryToken historyToken,
                                        final SpreadsheetContextMenu menu,
                                        final SpreadsheetSelectionMenuContext context) {
        menu.item(
            SpreadsheetContextMenuItem.with(
                context.idPrefix() + "dateTimeSymbols" + SpreadsheetElementIds.MENU_ITEM,
                "DateTimeSymbols"
            ).icon(
                Optional.of(
                    SpreadsheetIcons.dateTimeSymbols()
                )
            ).historyToken(
                Optional.of(
                    historyToken.dateTimeSymbols()
                )
            ).checked(
                context.selectionSummary()
                    .flatMap(SpreadsheetCell::dateTimeSymbols)
                    .isPresent()
            )
        );
    }

    private static void decimalNumberSymbols(final HistoryToken historyToken,
                                             final SpreadsheetContextMenu menu,
                                             final SpreadsheetSelectionMenuContext context) {
        menu.item(
            SpreadsheetContextMenuItem.with(
                context.idPrefix() +
                    "decimalNumberSymbols" +
                    SpreadsheetElementIds.MENU_ITEM,
                "DecimalNumberSymbols"
            ).icon(
                Optional.of(
                    SpreadsheetIcons.decimalNumberSymbols()
                )
            ).historyToken(
                Optional.of(
                    historyToken.decimalNumberSymbols()
                )
            ).checked(
                context.selectionSummary()
                    .flatMap(SpreadsheetCell::decimalNumberSymbols)
                    .isPresent()
            )
        );
    }

    // locale...........................................................................................................

    private static void locale(final HistoryToken historyToken,
                               final SpreadsheetContextMenu menu,
                               final SpreadsheetSelectionMenuContext context) {
        final SpreadsheetContextMenu subMenu = menu.subMenu(
            context.idPrefix() + "locale" + SpreadsheetElementIds.SUB_MENU,
            "Locale"
        );

        SpreadsheetSelectionMenuLocale.build(
            historyToken.cast(SpreadsheetAnchoredSelectionHistoryToken.class),
            subMenu,
            context
        );

        subMenu.disableIfEmpty();
    }

    // hideIfZero...........................................................................................................

    private static void hideIfZero(final HistoryToken historyToken,
                                   final SpreadsheetContextMenu menu,
                                   final SpreadsheetSelectionMenuContext context) {
        final boolean hidden = HideZeroValues.isHideZeroValues(context);

        menu.item(
            SpreadsheetContextMenuItem.with(
                    context.idPrefix() + "hideIfZero" + SpreadsheetElementIds.MENU_ITEM,
                    HideZeroValues.label(hidden)
                ).icon(
                    Optional.of(
                        SpreadsheetIcons.hideZeroValues()
                    )
                )
                .historyToken(
                    Optional.of(
                        historyToken.setMetadataPropertyName(
                            SpreadsheetMetadataPropertyName.HIDE_ZERO_VALUES
                        ).setSaveValue(
                            Optional.of(false == hidden)
                        )
                    )
                ).checked(hidden)
        );
    }

    // validator........................................................................................................

    private static void validator(final HistoryToken historyToken,
                                  final SpreadsheetContextMenu menu,
                                  final SpreadsheetSelectionMenuContext context) {
        final SpreadsheetContextMenu subMenu = menu.subMenu(
            context.idPrefix() + "validator" + SpreadsheetElementIds.SUB_MENU,
            "Validator"
        );

        SpreadsheetSelectionMenuValidator.build(
            historyToken.cast(SpreadsheetAnchoredSelectionHistoryToken.class),
            subMenu,
            context
        );

        subMenu.disableIfEmpty();
    }

    // valueTypes.......................................................................................................

    private static void valueTypes(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                                   final SpreadsheetContextMenu menu,
                                   final SpreadsheetSelectionMenuContext context) {
        SpreadsheetSelectionMenuValueType.build(
            historyToken,
            menu.subMenu(
                context.idPrefix() + "valueType" + SpreadsheetElementIds.SUB_MENU,
                "Value type"
            ),
            context
        );
    }

    // value............................................................................................................

    private static void value(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                              final SpreadsheetContextMenu menu,
                              final SpreadsheetSelectionMenuContext context) {
        SpreadsheetSelectionMenuValue.build(
            historyToken,
            menu.subMenu(
                context.idPrefix() + "value" + SpreadsheetElementIds.SUB_MENU,
                "Value"
            ),
            context
        );
    }

    // delete...........................................................................................................

    private static void clearDelete(final HistoryToken historyToken,
                                    final SpreadsheetContextMenu menu,
                                    final SpreadsheetSelectionMenuContext context) {
        final SpreadsheetSelection selection = historyToken.anchoredSelectionOrEmpty()
            .orElseThrow(
                () -> new IllegalStateException("History token missing selection " + historyToken)
            ).selection();

        // only render clear for columns and rows
        if (selection.isColumnOrColumnRange() || selection.isRowOrRowRange()) {
            menu.item(
                historyToken.clear()
                    .contextMenuItem(
                        context.idPrefix() +
                            "clear" +
                            SpreadsheetElementIds.MENU_ITEM,
                        "Clear"
                    )
            );
        }

        menu.item(
            historyToken.delete()
                .contextMenuItem(
                    context.idPrefix() +
                        "delete" +
                        SpreadsheetElementIds.MENU_ITEM,
                    "Delete"
                ).icon(
                    Optional.of(
                        selection instanceof SpreadsheetExpressionReference ?
                            SpreadsheetIcons.cellDelete() :
                            selection.isColumnOrColumnRange() ?
                                SpreadsheetIcons.columnRemove() :
                                SpreadsheetIcons.rowRemove()
                    )
                )
        );
    }

    // insert...........................................................................................................

    private static void insert(final HistoryToken historyToken,
                               final SpreadsheetContextMenu menu,
                               final SpreadsheetSelectionMenuContext context) {
        final SpreadsheetContextMenu subMenu = menu.subMenu(
            context.idPrefix() + "insert" + SpreadsheetElementIds.SUB_MENU,
            "Insert"
        );

        SpreadsheetSelectionMenuInsert.build(
            historyToken.cast(SpreadsheetAnchoredSelectionHistoryToken.class),
            subMenu,
            context
        );

        subMenu.disableIfEmpty();
    }

    // sort.............................................................................................................

    private static void sortColumns(final HistoryToken historyToken,
                                    final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection,
                                    final SpreadsheetContextMenu menu,
                                    final SpreadsheetSelectionMenuContext context) {
        final SpreadsheetSelection selection = anchoredSpreadsheetSelection.selection();

        final SpreadsheetSelection selectionNotLabel = context.resolveIfLabel(selection)
            .orElse(null);

        if (null != selectionNotLabel) {
            final boolean columns = selection.isColumnOrColumnRange();

            if (columns || selection.isExternalReference()) {
                if (columns || selectionNotLabel.toRowRange().count() > 1) {
                    final String idPrefix = context.idPrefix() + "column-sort-";

                    SpreadsheetSelectionMenuSort.build(
                        historyToken,
                        selectionNotLabel.toColumnOrColumnRange()
                            .toColumn(),
                        idPrefix, // id prefix
                        SpreadsheetIcons.columnSort(),
                        context.sortComparatorNames(),
                        menu
                    );
                }
            }
        }
    }

    private static void sortRows(final HistoryToken historyToken,
                                 final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection,
                                 final SpreadsheetContextMenu menu,
                                 final SpreadsheetSelectionMenuContext context) {
        final SpreadsheetSelection selection = anchoredSpreadsheetSelection.selection();

        final SpreadsheetSelection selectionNotLabel = context.resolveIfLabel(selection)
            .orElse(null);

        if (null != selectionNotLabel) {
            final boolean rows = selection.isRowOrRowRange();
            if (rows || selection.isExternalReference()) {

                if (rows || selectionNotLabel.toColumnOrColumnRange().count() > 1) {
                    final String idPrefix = context.idPrefix() + "row-sort-";

                    SpreadsheetSelectionMenuSort.build(
                        historyToken,
                        selectionNotLabel.toRowOrRowRange()
                            .toRow(),
                        idPrefix, // id prefix
                        SpreadsheetIcons.columnSort(),
                        context.sortComparatorNames(),
                        menu
                    );
                }
            }
        }
    }

    // freeze/unfreeze..................................................................................................

    private static void freezeUnfreeze(final HistoryToken historyToken,
                                       final SpreadsheetContextMenu menu,
                                       final SpreadsheetSelectionMenuContext context) {
        menu.item(
            SpreadsheetContextMenuItem.with(
                context.idPrefix() + "freeze" + SpreadsheetElementIds.MENU_ITEM,
                "Freeze"
            ).historyToken(
                historyToken.freezeOrEmpty()
            )
        ).item(
            SpreadsheetContextMenuItem.with(
                context.idPrefix() + "unfreeze" + SpreadsheetElementIds.MENU_ITEM,
                "Unfreeze"
            ).historyToken(
                historyToken.unfreezeOrEmpty()
            )
        );
    }

    // labels...........................................................................................................

    private static void label(final HistoryToken historyToken,
                              final SpreadsheetSelection selection,
                              final SpreadsheetContextMenu menu,
                              final SpreadsheetSelectionMenuContext context) {
        final Set<SpreadsheetLabelMapping> labelMappings = context.labelMappings(selection);

        SpreadsheetContextMenu sub = menu.subMenu(
            context.idPrefix() + "label" + SpreadsheetElementIds.SUB_MENU,
            "Labels",
            String.valueOf(
                labelMappings.size()
            )
        );

        // create should be first because there could be lost of labels which would push Crete to the bottom.
        sub.item(
            historyToken.setLabelName(Optional.empty())
                .contextMenuItem(
                    context.idPrefix() + "label-create" + SpreadsheetElementIds.MENU_ITEM,
                    "Create..."
                )
        );

        sub.item(
            historyToken.setLabels(HistoryTokenOffsetAndCount.EMPTY)
                .contextMenuItem(
                    context.idPrefix() + "labels-list" + SpreadsheetElementIds.MENU_ITEM,
                    "List..."
                )
        );

        int i = 0;

        // create items for each label
        for (final SpreadsheetLabelMapping mapping : labelMappings) {
            final SpreadsheetLabelName label = mapping.label();

            sub = sub.item(
                historyToken.setLabelName(
                    Optional.of(label)
                ).contextMenuItem(
                    context.idPrefix() + "label-" + i + SpreadsheetElementIds.MENU_ITEM,
                    label + " (" + mapping.reference() + ")"
                )
            );

            i++;
        }
    }

    private static void references(final HistoryToken historyToken,
                                   final SpreadsheetSelection selection,
                                   final SpreadsheetContextMenu menu,
                                   final SpreadsheetSelectionMenuContext context) {
        final Set<SpreadsheetExpressionReference> references = context.references(selection);

        menu.item(
            SpreadsheetContextMenuItem.with(
                context.idPrefix() + "references" + SpreadsheetElementIds.MENU_ITEM,
                "References"
            ).badge(
                Optional.of(
                    String.valueOf(references.size())
                )
            ).historyToken(
                Optional.of(
                    historyToken.references(HistoryTokenOffsetAndCount.EMPTY)
                )
            )
        );
    }

    private static void reload(final HistoryToken historyToken,
                               final SpreadsheetContextMenu menu,
                               final SpreadsheetSelectionMenuContext context) {
        menu.item(
            SpreadsheetContextMenuItem.with(
                context.idPrefix() + "reload" + SpreadsheetElementIds.MENU_ITEM,
                "Reload"
            ).historyToken(
                Optional.of(
                    historyToken.reload()
                )
            )
        );
    }

    /**
     * Stop creation
     */
    private SpreadsheetSelectionMenu() {
        throw new UnsupportedOperationException();
    }
}
