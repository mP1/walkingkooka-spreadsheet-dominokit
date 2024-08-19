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
import org.dominokit.domino.ui.utils.DominoElement;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.SpreadsheetHotKeys;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.clipboard.SpreadsheetCellClipboardKind;
import walkingkooka.spreadsheet.dominokit.hidezerovalues.HideZeroValues;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.dominokit.meta.SpreadsheetMetadataColorPickerComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.CaseKind;
import walkingkooka.tree.text.BorderStyle;
import walkingkooka.tree.text.BoxEdge;
import walkingkooka.tree.text.FontStyle;
import walkingkooka.tree.text.FontWeight;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.Overflow;
import walkingkooka.tree.text.OverflowWrap;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextDecorationLine;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.tree.text.TextTransform;
import walkingkooka.tree.text.VerticalAlign;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

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
        // show context menu
        final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection = historyToken.anchoredSelection();
        final SpreadsheetSelection selection = anchoredSpreadsheetSelection.selection();

        // CUT
        // COPY
        // PASTE
        // -----
        // STYLE
        // ----
        // FORMAT
        // PARSE
        // -----
        // CLEAR
        // DELETE
        // ------
        // INSERT
        // -------
        // SORT COLUMN
        // SORT ROWS
        // -------
        // FREEZE
        // UNFREEZE
        // -------
        // LABELS

        if (selection.isCellReference() || selection.isCellRangeReference() || selection.isLabelName()) {
            clipboard(
                    historyToken,
                    menu,
                    context
            );

            style(
                    historyToken,
                    menu.subMenu(
                            context.idPrefix() + "style" + SpreadsheetElementIds.SUB_MENU,
                            "Style"
                    ),
                    context
            );

            format(
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
        {
            insertColumns(historyToken, menu, context);
        }
        menu.separator();
        {
            insertRows(historyToken, menu, context);
        }
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
            if (selection.isCellReference() || selection.isCellRangeReference()) {
                label(historyToken, selection, menu, context);
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
                    historyToken::setCellCut
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
                    (k) -> historyToken.setCellCopy(k)
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
                    historyToken::setCellPaste
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

    private static void format(final HistoryToken historyToken,
                               final SpreadsheetContextMenu menu,
                               final SpreadsheetSelectionMenuContext context) {
        final SpreadsheetContextMenu subMenu = menu.subMenu(
                context.idPrefix() + "menu" + SpreadsheetElementIds.SUB_MENU,
                "Formatter"
        );

        SpreadsheetSelectionMenuFormat.build(
                historyToken.cast(SpreadsheetAnchoredSelectionHistoryToken.class),
                subMenu,
                context
        );

        subMenu.disableIfEmpty();
    }

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
                                        ).setSave(
                                                Optional.of(false == hidden)
                                        )
                                )
                        ).checked(hidden)
        );
    }

    // ALIGNMENT
    // VERTICAL ALIGNMENT
    // COLOR
    // BACKGROUND COLOR
    // BOLD
    // ITALICS
    // STRIKE THRU
    // UNDERLINE
    // CASE
    // WRAPPING
    // BORDER
    // CLEAR STYLE
    private static void style(final HistoryToken historyToken,
                              final SpreadsheetContextMenu menu,
                              final SpreadsheetSelectionMenuContext context) {
        alignment(menu, context);
        verticalAlignment(menu, context);
        color(historyToken, menu, context);
        backgroundColor(historyToken, menu, context);
        fontWeight(menu, context);
        fontStyle(menu, context);
        textDecoration(menu, context);
        textCase(historyToken, menu, context);
        textWrapping(menu, context);
        border(menu, context);
        clearStyle(historyToken, menu, context);

        menu.separator();
    }

    private static void color(final HistoryToken historyToken,
                              final SpreadsheetContextMenu menu,
                              final SpreadsheetSelectionMenuContext context) {
        colorItem(
                "color",
                "Color",
                historyToken.setStyle(TextStylePropertyName.COLOR),
                menu,
                context
        );
    }

    private static void backgroundColor(final HistoryToken historyToken,
                                        final SpreadsheetContextMenu menu,
                                        final SpreadsheetSelectionMenuContext context) {
        colorItem(
                "background-color",
                "Background color",
                historyToken.setStyle(TextStylePropertyName.BACKGROUND_COLOR),
                menu,
                context
        );
    }

    private static void colorItem(final String id,
                                  final String text,
                                  final HistoryToken historyToken,
                                  final SpreadsheetContextMenu menu,
                                  final SpreadsheetSelectionMenuContext context) {
        final SpreadsheetContextMenu sub = menu.subMenu(
                context.idPrefix() + id + SpreadsheetElementIds.SUB_MENU,
                text,
                SpreadsheetIcons.palette()
        );

        final SpreadsheetMetadataColorPickerComponent colors = SpreadsheetMetadataColorPickerComponent.with(historyToken);

        colors.refreshAll(
                historyToken,
                context.spreadsheetMetadata()
        );
        sub.item(colors);
    }

    private static void clearStyle(final HistoryToken historyToken,
                                   final SpreadsheetContextMenu menu,
                                   final SpreadsheetSelectionMenuContext context) {
        menu.item(
                historyToken.setStyle(TextStylePropertyName.ALL)
                        .clearSave()
                        .contextMenuItem(
                                context.idPrefix() + "clear-style" + SpreadsheetElementIds.MENU_ITEM,
                                "Clear style"
                        ).icon(
                                Optional.of(
                                        SpreadsheetIcons.clearStyle()
                                )
                        )
        );
    }

    private static void clearDelete(final HistoryToken historyToken,
                                    final SpreadsheetContextMenu menu,
                                    final SpreadsheetSelectionMenuContext context) {
        final SpreadsheetSelection selection = historyToken.anchoredSelectionOrEmpty()
                .orElseThrow(
                        () -> new IllegalStateException("History token missing selection " + historyToken)
                ).selection();

        // only render clear for columns and rows
        if (selection.pick(
                false, // cell
                true, // column
                true // row
        )) {
            menu.item(
                    historyToken.setClear()
                            .contextMenuItem(
                                    context.idPrefix() +
                                            "clear" +
                                            SpreadsheetElementIds.MENU_ITEM,
                                    "Clear"
                            )
            );
        }

        menu.item(
                historyToken.setDelete()
                        .contextMenuItem(
                                context.idPrefix() +
                                        "delete" +
                                        SpreadsheetElementIds.MENU_ITEM,
                                "Delete"
                        ).icon(
                                Optional.of(
                                        selection.<Supplier<Icon<?>>>pick(
                                                SpreadsheetIcons::cellDelete,
                                                SpreadsheetIcons::columnRemove,
                                                SpreadsheetIcons::rowRemove
                                        ).get()
                                )
                        )
        );
    }

    private static void fontWeight(final SpreadsheetContextMenu menu,
                                   final SpreadsheetSelectionMenuContext context) {
        menu.checkedItem(
                context.idPrefix() + "bold" + SpreadsheetElementIds.MENU_ITEM, // id
                "Bold", // text
                Optional.of(
                        SpreadsheetIcons.bold()
                ), // icons
                TextStylePropertyName.FONT_WEIGHT,
                FontWeight.BOLD,
                SpreadsheetHotKeys.BOLD,
                context
        );
    }

    private static void fontStyle(final SpreadsheetContextMenu menu,
                                  final SpreadsheetSelectionMenuContext context) {
        menu.checkedItem(
                context.idPrefix() + "italics" + SpreadsheetElementIds.MENU_ITEM,
                "Italics", // text
                Optional.of(
                        SpreadsheetIcons.italics()
                ), // icons
                TextStylePropertyName.FONT_STYLE,
                FontStyle.ITALIC,
                SpreadsheetHotKeys.ITALICS,
                context
        );
    }

    private static void textDecoration(final SpreadsheetContextMenu menu,
                                       final SpreadsheetSelectionMenuContext context) {
        menu.checkedItem(
                context.idPrefix() + "strike-thru" + SpreadsheetElementIds.MENU_ITEM, // id
                "Strike-thru", // text
                Optional.of(
                        SpreadsheetIcons.strikethrough()
                ), // icons
                TextStylePropertyName.TEXT_DECORATION_LINE,
                TextDecorationLine.LINE_THROUGH,
                SpreadsheetHotKeys.STRIKETHRU,
                context
        ).checkedItem(
                context.idPrefix() + "underline" + SpreadsheetElementIds.MENU_ITEM, // id
                "Underline", // text
                Optional.of(
                        SpreadsheetIcons.underline()
                ), // icons
                TextStylePropertyName.TEXT_DECORATION_LINE,
                TextDecorationLine.UNDERLINE,
                SpreadsheetHotKeys.UNDERLINE,
                context
        );
    }

    private static void textCase(final HistoryToken historyToken,
                                 final SpreadsheetContextMenu menu,
                                 final SpreadsheetSelectionMenuContext context) {
        menu.subMenu(
                context.idPrefix() + "text-case" + SpreadsheetElementIds.SUB_MENU,
                "Text case"
        ).item(
                historyToken.setStyle(TextStylePropertyName.TEXT_TRANSFORM)
                        .setSave("")
                        .contextMenuItem(
                                context.idPrefix() + "normal" + SpreadsheetElementIds.MENU_ITEM,
                                "Normal"
                        ).icon(
                                Optional.of(
                                        SpreadsheetIcons.textCaseUpper()
                                )
                        )
        ).checkedItem(
                context.idPrefix() + "capitalize" + SpreadsheetElementIds.MENU_ITEM, // id
                "Capitalize", // text
                Optional.of(
                        SpreadsheetIcons.textCaseCapitalize()
                ), // icons
                TextStylePropertyName.TEXT_TRANSFORM,
                TextTransform.CAPITALIZE,
                SpreadsheetHotKeys.TEXT_TRANSFORM_CAPITALIZE,
                context
        ).checkedItem(
                context.idPrefix() + "lower" + SpreadsheetElementIds.MENU_ITEM, // id
                "Lower case", // text
                Optional.of(
                        SpreadsheetIcons.textCaseLower()
                ), // icons
                TextStylePropertyName.TEXT_TRANSFORM,
                TextTransform.LOWERCASE,
                SpreadsheetHotKeys.TEXT_TRANSFORM_LOWERCASE,
                context
        ).checkedItem(
                context.idPrefix() + "upper" + SpreadsheetElementIds.MENU_ITEM, // id
                "Upper case", // text
                Optional.of(
                        SpreadsheetIcons.textCaseUpper()
                ), // icons
                TextStylePropertyName.TEXT_TRANSFORM,
                TextTransform.UPPERCASE,
                SpreadsheetHotKeys.TEXT_TRANSFORM_UPPERCASE,
                context
        );
    }

    private static void textWrapping(final SpreadsheetContextMenu menu,
                                     final SpreadsheetSelectionMenuContext context) {
        menu.subMenu(
                context.idPrefix() + "text-wrapping" + SpreadsheetElementIds.SUB_MENU,
                "Wrapping"
        ).checkedItem(
                context.idPrefix() + "clip" + SpreadsheetElementIds.MENU_ITEM, // id
                "Clip", // text
                Optional.of(
                        SpreadsheetIcons.textWrappingClip()
                ), // icons
                TextStylePropertyName.OVERFLOW_WRAP,
                OverflowWrap.NORMAL,
                SpreadsheetHotKeys.TEXT_WRAPPING_NORMAL,
                context
        ).checkedItem(
                context.idPrefix() + "overflow" + SpreadsheetElementIds.MENU_ITEM,
                "Overflow", // text
                Optional.of(
                        SpreadsheetIcons.textWrappingOverflow()
                ), // icons
                TextStylePropertyName.OVERFLOW_X,
                Overflow.VISIBLE,
                SpreadsheetHotKeys.TEXT_WRAPPING_OVERFLOW,
                context
        ).checkedItem(
                context.idPrefix() + "wrap" + SpreadsheetElementIds.MENU_ITEM, // id
                "Wrap", // text
                Optional.of(
                        SpreadsheetIcons.textWrappingWrap()
                ), // icons
                TextStylePropertyName.OVERFLOW_X,
                Overflow.HIDDEN,
                SpreadsheetHotKeys.TEXT_WRAPPING_WRAP,
                context
        );
    }

    // BORDER
    //   TOP
    //   LEFT
    //   RIGHT
    //   BOTTOM
    //     COLOR
    //     STYLE
    //       NONE
    //       THIN
    //     THICKNESS
    //       NONE
    //       1
    //       2

    private static void border(final SpreadsheetContextMenu menu,
                               final SpreadsheetSelectionMenuContext context) {
        final SpreadsheetContextMenu border = menu.subMenu(
                context.idPrefix() + "border" + SpreadsheetElementIds.SUB_MENU,
                "Border"
        );

        final String borderIdPrefix = context.idPrefix() + "border-";

        for (final Entry<BoxEdge, Icon<?>> boxEdgeAndIcon : BORDER_BOX_EDGE_AND_ICONS.entrySet()) {
            final BoxEdge boxEdge = boxEdgeAndIcon.getKey();

            final String enumName = boxEdge.name();
            final String idPrefix = borderIdPrefix + enumName.toLowerCase() + '-';

            borderItem(
                    idPrefix,
                    border.subMenu(
                            idPrefix + SpreadsheetElementIds.SUB_MENU, // id
                            CaseKind.SNAKE.change(
                                    enumName,
                                    CaseKind.TITLE
                            ), // label
                            boxEdgeAndIcon.getValue() // icon
                    ),
                    boxEdge,
                    context
            );
        }
    }

    private final static Map<BoxEdge, Icon<?>> BORDER_BOX_EDGE_AND_ICONS = Maps.of(
            BoxEdge.TOP,
            SpreadsheetIcons.borderTop(),
            BoxEdge.LEFT,
            SpreadsheetIcons.borderLeft(),
            BoxEdge.RIGHT,
            SpreadsheetIcons.borderRight(),
            BoxEdge.BOTTOM,
            SpreadsheetIcons.borderBottom(),
            BoxEdge.ALL,
            SpreadsheetIcons.borderAll()
    );

    // COLOR
    // STYLE
    // THICKNESS
    private static void borderItem(final String idPrefix,
                                   final SpreadsheetContextMenu menu,
                                   final BoxEdge boxEdge,
                                   final SpreadsheetSelectionMenuContext context) {
        final HistoryToken historyToken = context.historyToken();

        colorItem(
                idPrefix + "color",
                "Color",
                historyToken.setStyle(boxEdge.borderColorPropertyName()), // token
                menu,
                context
        );

        borderStyle(
                idPrefix + "style", // without trailing dash
                menu,
                boxEdge,
                context
        );

        borderWidth(
                idPrefix + "width", // without trailing dash
                menu,
                boxEdge,
                context
        );
    }

    private static void borderStyle(final String idPrefix,
                                    final SpreadsheetContextMenu menu,
                                    final BoxEdge boxEdge,
                                    final SpreadsheetSelectionMenuContext context) {
        final SpreadsheetContextMenu styleSubMenu = menu.subMenu(
                idPrefix + SpreadsheetElementIds.SUB_MENU,
                "Style"
        );

        final TextStylePropertyName<BorderStyle> propertyName = boxEdge.borderStylePropertyName();

        for (final BorderStyle borderStyle : BORDER_STYLE) {
            final String name = borderStyle.name();

            styleSubMenu.checkedItem(
                    idPrefix + "-" + CaseKind.SNAKE.change(
                            name,
                            CaseKind.KEBAB
                    ) + SpreadsheetElementIds.MENU_ITEM, // id
                    CaseKind.SNAKE.change(
                            name,
                            CaseKind.TITLE
                    ), // text
                    Optional.empty(), // no icons
                    propertyName, // property name
                    borderStyle, // property value
                    "", // key
                    context
            );
        }

        // clear
        styleSubMenu.item(
                context.historyToken()
                        .setStyle(propertyName)
                        .clearSave()
                        .contextMenuItem(
                                idPrefix + "-clear" + SpreadsheetElementIds.MENU_ITEM, // id
                                "Clear"
                        ).icon(
                                Optional.of(
                                        SpreadsheetIcons.borderStyleClear()
                                )
                        )
        );
    }

    // TODO Add more later BorderStyles
    // https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/2189
    private final static List<BorderStyle> BORDER_STYLE = Lists.of(
            BorderStyle.NONE,
            BorderStyle.DASHED,
            BorderStyle.DOTTED,
            BorderStyle.SOLID
    );

    // none
    // 1
    // 2
    // clear
    private static void borderWidth(final String idPrefix,
                                    final SpreadsheetContextMenu menu,
                                    final BoxEdge boxEdge,
                                    final SpreadsheetSelectionMenuContext context) {
        final SpreadsheetContextMenu borderWidthSubMenu = menu.subMenu(
                idPrefix + SpreadsheetElementIds.SUB_MENU,
                "Width"
        );

        final TextStylePropertyName<Length<?>> propertyName = boxEdge.borderWidthPropertyName();

        for (int i = 0; i < 5; i++) {
            final String label = 0 == i ?
                    "None" :
                    String.valueOf(i);

            borderWidthSubMenu.checkedItem(
                    idPrefix + '-' + i + SpreadsheetElementIds.MENU_ITEM, // id
                    label, // text
                    Optional.empty(), // no icons
                    propertyName, // property name
                    Length.pixel((double) i), // property value
                    "", // key
                    context
            );
        }

        // clear
        borderWidthSubMenu.item(
                context.historyToken()
                        .setStyle(propertyName)
                        .clearSave()
                        .contextMenuItem(
                                idPrefix + "-clear" + SpreadsheetElementIds.MENU_ITEM, // id
                                "Clear"
                        ).icon(
                                Optional.of(
                                        SpreadsheetIcons.borderStyleClear()
                                )
                        )
        );
    }

    // alignment........................................................................................................

    private static void alignment(final SpreadsheetContextMenu menu,
                                  final SpreadsheetSelectionMenuContext context) {
        menu.subMenu(
                context.idPrefix() + "alignment" + SpreadsheetElementIds.SUB_MENU,
                "Alignment"
        ).checkedItem(
                context.idPrefix() + "left" + SpreadsheetElementIds.MENU_ITEM, // id
                "Left", // text
                Optional.of(
                        SpreadsheetIcons.alignLeft()
                ), // icons
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT,
                SpreadsheetHotKeys.ALIGNMENT_LEFT,
                context
        ).checkedItem(
                context.idPrefix() + "center" + SpreadsheetElementIds.MENU_ITEM, // id
                "Center", // text
                Optional.of(
                        SpreadsheetIcons.alignCenter()
                ), // icons
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.CENTER,
                SpreadsheetHotKeys.ALIGNMENT_CENTER,
                context
        ).checkedItem(
                context.idPrefix() + "right" + SpreadsheetElementIds.MENU_ITEM, // id
                "Right", // text
                Optional.of(
                        SpreadsheetIcons.alignRight()
                ), // icons
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.RIGHT,
                SpreadsheetHotKeys.ALIGNMENT_RIGHT,
                context
        ).checkedItem(
                context.idPrefix() + "justify" + SpreadsheetElementIds.MENU_ITEM, // id
                "Justify", // text
                Optional.of(
                        SpreadsheetIcons.alignJustify()
                ), // icons
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.JUSTIFY,
                SpreadsheetHotKeys.ALIGNMENT_JUSTIFY,
                context
        );
    }

    private static void verticalAlignment(final SpreadsheetContextMenu menu,
                                          final SpreadsheetSelectionMenuContext context) {
        menu.subMenu(
                context.idPrefix() + "vertical-alignment" + SpreadsheetElementIds.SUB_MENU,
                "Vertical Alignment"
        ).checkedItem(
                context.idPrefix() + "top" + SpreadsheetElementIds.MENU_ITEM, // id
                "Top", // text
                Optional.of(
                        SpreadsheetIcons.verticalAlignTop()
                ), // icons
                TextStylePropertyName.VERTICAL_ALIGN,
                VerticalAlign.TOP,
                SpreadsheetHotKeys.VERTICAL_ALIGNMENT_TOP,
                context
        ).checkedItem(
                context.idPrefix() + "middle" + SpreadsheetElementIds.MENU_ITEM, // id
                "Middle", // text
                Optional.of(
                        SpreadsheetIcons.verticalAlignMiddle()
                ), // icons
                TextStylePropertyName.VERTICAL_ALIGN,
                VerticalAlign.MIDDLE,
                SpreadsheetHotKeys.VERTICAL_ALIGNMENT_MIDDLE,
                context
        ).checkedItem(
                context.idPrefix() + "bottom" + SpreadsheetElementIds.MENU_ITEM, // id
                "Bottom", // text
                Optional.of(
                        SpreadsheetIcons.verticalAlignBottom()
                ), // icons
                TextStylePropertyName.VERTICAL_ALIGN,
                VerticalAlign.BOTTOM,
                SpreadsheetHotKeys.VERTICAL_ALIGNMENT_BOTTOM,
                context
        );
    }

    private static void insertColumns(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                                      final SpreadsheetContextMenu menu,
                                      final SpreadsheetSelectionMenuContext context) {
        final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection = historyToken.anchoredSelection();
        final SpreadsheetSelection selection = anchoredSpreadsheetSelection.selection();

        if (selection.isColumnReference() | selection.isColumnRangeReference() | selection.isCellReference() || selection.isCellRangeReference()) {
            final HistoryToken columnHistoryToken = historyToken.setAnchoredSelection(
                    Optional.of(
                            selection.toColumnOrColumnRange()
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
                    columnHistoryToken::setInsertBefore
            );

            final String afterIdPrefix = context.idPrefix() + "column-insert-after";

            insertSubMenu(
                    menu.subMenu(
                            afterIdPrefix + SpreadsheetElementIds.SUB_MENU,
                            "Insert after column",
                            SpreadsheetIcons.columnInsertAfter()
                    ),
                    afterIdPrefix,
                    columnHistoryToken::setInsertAfter
            );
        }
    }

    private static void insertRows(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                                   final SpreadsheetContextMenu menu,
                                   final SpreadsheetSelectionMenuContext context) {
        final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection = historyToken.anchoredSelection();
        final SpreadsheetSelection selection = anchoredSpreadsheetSelection.selection();

        if (selection.isRowReference() | selection.isRowRangeReference() | selection.isCellReference() || selection.isCellRangeReference()) {
            final HistoryToken rowHistoryToken = historyToken.setAnchoredSelection(
                    Optional.of(
                            selection.toRowOrRowRange()
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
                    rowHistoryToken::setInsertBefore
            );

            final String afterIdPrefix = context.idPrefix() + "row-insert-after";

            insertSubMenu(
                    menu.subMenu(
                            afterIdPrefix + SpreadsheetElementIds.SUB_MENU,
                            "Insert after row",
                            SpreadsheetIcons.rowInsertAfter()
                    ),
                    afterIdPrefix,
                    rowHistoryToken::setInsertAfter
            );
        }
    }

    // sort.............................................................................................................

    private static void sortColumns(final HistoryToken historyToken,
                                    final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection,
                                    final SpreadsheetContextMenu menu,
                                    final SpreadsheetSelectionMenuContext context) {
        final SpreadsheetSelection selection = anchoredSpreadsheetSelection.selection();

        final boolean columns = selection.isColumnReferenceOrColumnRangeReference();

        if (columns || selection.isCellReferenceOrCellRangeReference()) {
            if (columns || selection.toRowRange().count() > 1) {
                final String idPrefix = context.idPrefix() + "column-sort-";

                SpreadsheetSelectionMenuSort.build(
                        historyToken,
                        selection.toColumnOrColumnRange()
                                .toColumn(),
                        idPrefix, // id prefix
                        SpreadsheetIcons.columnSort(),
                        context.spreadsheetComparatorInfos(), // infos
                        menu
                );
            }
        }
    }

    private static void sortRows(final HistoryToken historyToken,
                                 final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection,
                                 final SpreadsheetContextMenu menu,
                                 final SpreadsheetSelectionMenuContext context) {
        final SpreadsheetSelection selection = anchoredSpreadsheetSelection.selection();

        final boolean rows = selection.isRowReferenceOrRowRangeReference();
        if (rows || selection.isCellReferenceOrCellRangeReference()) {

            if (rows || selection.toColumnOrColumnRange().count() > 1) {
                final String idPrefix = context.idPrefix() + "row-sort-";

                SpreadsheetSelectionMenuSort.build(
                        historyToken,
                        selection.toRowOrRowRange()
                                .toRow(),
                        idPrefix, // id prefix
                        SpreadsheetIcons.columnSort(),
                        context.spreadsheetComparatorInfos(), // infos
                        menu
                );
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

        int i = 0;

        // create items for each label
        for (final SpreadsheetLabelMapping mapping : labelMappings) {
            final SpreadsheetLabelName label = mapping.label();

            sub = sub.item(
                    historyToken.setLabelName(
                            Optional.of(label)
                    ).contextMenuItem(
                            context.idPrefix() + "label-" + i + SpreadsheetElementIds.MENU_ITEM,
                            label + " (" + mapping.target() + ")"
                    )
            );

            i++;
        }

        sub.item(
                historyToken.setLabelName(Optional.empty())
                        .contextMenuItem(
                                context.idPrefix() + "label-create" + SpreadsheetElementIds.MENU_ITEM,
                                "Create..."
                        )
        );
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
    private SpreadsheetSelectionMenu() {
        throw new UnsupportedOperationException();
    }
}
