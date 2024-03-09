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

import org.dominokit.domino.ui.icons.MdiIcon;
import org.dominokit.domino.ui.utils.DominoElement;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIds;
import walkingkooka.spreadsheet.dominokit.ui.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.ui.contextmenu.SpreadsheetContextMenuItem;
import walkingkooka.spreadsheet.dominokit.ui.hidezerovalues.HideZeroValues;
import walkingkooka.spreadsheet.dominokit.ui.metadatacolorpicker.SpreadsheetMetadataColorPickerComponent;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.FontStyle;
import walkingkooka.tree.text.FontWeight;
import walkingkooka.tree.text.Overflow;
import walkingkooka.tree.text.OverflowWrap;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextDecorationLine;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.tree.text.TextTransform;
import walkingkooka.tree.text.VerticalAlign;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
        final SpreadsheetSelection selection = historyToken.anchoredSelection()
                .selection();

        // STYLE
        // ----
        // FORMAT
        // PARSE
        // -----
        // CLEAR
        // DELETE
        // -------
        // FREEZE
        // UNFREEZE
        // -------
        // LABELS

        // TODO add tick if already selected
        if (selection.isCellReference() || selection.isCellRange() || selection.isLabelName()) {
            style(
                    historyToken,
                    menu.subMenu(
                            context.idPrefix() + "style" + SpreadsheetIds.SUB_MENU,
                            "Style"
                    ),
                    context
            );

            final Locale locale = context.spreadsheetMetadata()
                    .getOrFail(SpreadsheetMetadataPropertyName.LOCALE);

            format(
                    historyToken,
                    locale,
                    context.recentFormatPatterns()
                            .stream()
                            .map(t -> (SpreadsheetFormatPattern) t.pattern().get()) // cant fail must be SFP
                            .collect(Collectors.toList()),
                    menu,
                    context
            );
            parse(
                    historyToken,
                    locale,
                    context.recentParsePatterns()
                            .stream()
                            .map(t -> (SpreadsheetParsePattern) t.pattern().get()) // cant fail must be SPP
                            .collect(Collectors.toList()),
                    menu,
                    context
            );
            hideIfZero(
                    historyToken,
                    menu,
                    context
            );
            menu.separator();
        }
        menu.separator();

        clearDelete(
                historyToken,
                menu,
                context
        );
        insertColumns(historyToken, selection, menu, context);
        insertRows(historyToken, selection, menu, context);
        freezeUnfreeze(historyToken, menu, context);

        if (selection.isCellReference() || selection.isCellRange()) {
            menu.separator();
            label(historyToken, selection, menu, context);
        }
    }

    private static void parse(final HistoryToken historyToken,
                              final Locale locale,
                              final List<SpreadsheetParsePattern> recents,
                              final SpreadsheetContextMenu menu,
                              final SpreadsheetSelectionMenuContext context) {
        final String idPrefix = context.idPrefix() + "parse-";

        SpreadsheetSelectionMenuPatternParse.with(
                historyToken,
                locale,
                recents,
                idPrefix,
                context.selectionSummary()
                        .parsePattern()
        ).build(
                menu.subMenu(
                        idPrefix + SpreadsheetIds.SUB_MENU,
                        "Parse",
                        SpreadsheetIcons.parsePattern()
                )
        );
    }

    private static void format(final HistoryToken historyToken,
                               final Locale locale,
                               final List<SpreadsheetFormatPattern> recents,
                               final SpreadsheetContextMenu menu,
                               final SpreadsheetSelectionMenuContext context) {
        final String idPrefix = context.idPrefix() + "format-";

        SpreadsheetSelectionMenuPatternFormat.with(
                historyToken,
                locale,
                recents,
                idPrefix,
                context.selectionSummary()
                        .formatPattern()
        ).build(
                menu.subMenu(
                        idPrefix + SpreadsheetIds.SUB_MENU,
                        "Format",
                        SpreadsheetIcons.formatPattern()
                )
        );
    }

    private static void hideIfZero(final HistoryToken historyToken,
                                   final SpreadsheetContextMenu menu,
                                   final SpreadsheetSelectionMenuContext context) {
        final boolean hidden = HideZeroValues.isHideZeroValues(context);

        menu.item(
                SpreadsheetContextMenuItem.with(
                                context.idPrefix() + "hideIfZero" + SpreadsheetIds.MENU_ITEM,
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
                context.idPrefix() + id + SpreadsheetIds.SUB_MENU,
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
                                context.idPrefix() + "clear-style" + SpreadsheetIds.MENU_ITEM,
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
        menu.separator();

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
                                            SpreadsheetIds.MENU_ITEM,
                                    "Clear"
                            )
            );
        }

        menu.item(
                historyToken.setDelete()
                        .contextMenuItem(
                                context.idPrefix() +
                                        "delete" +
                                        SpreadsheetIds.MENU_ITEM,
                                "Delete"
                        ).icon(
                                Optional.of(
                                        selection.<Supplier<MdiIcon>>pick(
                                                SpreadsheetIcons::cellDelete,
                                                SpreadsheetIcons::columnRemove,
                                                SpreadsheetIcons::rowRemove
                                        ).get()
                                )
                        )
        ).separator();
    }

    private static void fontWeight(final SpreadsheetContextMenu menu,
                                   final SpreadsheetSelectionMenuContext context) {
        menu.checkedItem(
                context.idPrefix() + "bold" + SpreadsheetIds.MENU_ITEM, // id
                "Bold", // text
                Optional.of(
                        SpreadsheetIcons.bold()
                ), // icons
                TextStylePropertyName.FONT_WEIGHT,
                FontWeight.BOLD,
                context
        );
    }

    private static void fontStyle(final SpreadsheetContextMenu menu,
                                  final SpreadsheetSelectionMenuContext context) {
        menu.checkedItem(
                context.idPrefix() + "italics" + SpreadsheetIds.MENU_ITEM,
                "Italics", // text
                Optional.of(
                        SpreadsheetIcons.italics()
                ), // icons
                TextStylePropertyName.FONT_STYLE,
                FontStyle.ITALIC,
                context
        );
    }

    private static void textDecoration(final SpreadsheetContextMenu menu,
                                       final SpreadsheetSelectionMenuContext context) {
        menu.checkedItem(
                context.idPrefix() + "strike-thru" + SpreadsheetIds.MENU_ITEM, // id
                "Strike-thru", // text
                Optional.of(
                        SpreadsheetIcons.strikethrough()
                ), // icons
                TextStylePropertyName.TEXT_DECORATION_LINE,
                TextDecorationLine.LINE_THROUGH,
                context
        ).checkedItem(
                context.idPrefix() + "underline" + SpreadsheetIds.MENU_ITEM, // id
                "Underline", // text
                Optional.of(
                        SpreadsheetIcons.underline()
                ), // icons
                TextStylePropertyName.TEXT_DECORATION_LINE,
                TextDecorationLine.UNDERLINE,
                context
        );
    }

    private static void textCase(final HistoryToken historyToken,
                                 final SpreadsheetContextMenu menu,
                                 final SpreadsheetSelectionMenuContext context) {
        menu.subMenu(
                context.idPrefix() + "text-case" + SpreadsheetIds.SUB_MENU,
                "Text case"
        ).item(
                historyToken.setStyle(TextStylePropertyName.TEXT_TRANSFORM)
                        .setSave("")
                        .contextMenuItem(
                                context.idPrefix() + "normal" + SpreadsheetIds.MENU_ITEM,
                                "Normal"
                        ).icon(
                                Optional.of(
                                        SpreadsheetIcons.textCaseUpper()
                                )
                        )
        ).checkedItem(
                context.idPrefix() + "capitalize" + SpreadsheetIds.MENU_ITEM, // id
                "Capitalize", // text
                Optional.of(
                        SpreadsheetIcons.textCaseCapitalize()
                ), // icons
                TextStylePropertyName.TEXT_TRANSFORM,
                TextTransform.CAPITALIZE,
                context
        ).checkedItem(
                context.idPrefix() + "lower" + SpreadsheetIds.MENU_ITEM, // id
                "Lower case", // text
                Optional.of(
                        SpreadsheetIcons.textCaseLower()
                ), // icons
                TextStylePropertyName.TEXT_TRANSFORM,
                TextTransform.LOWERCASE,
                context
        ).checkedItem(
                context.idPrefix() + "upper" + SpreadsheetIds.MENU_ITEM, // id
                "Upper case", // text
                Optional.of(
                        SpreadsheetIcons.textCaseUpper()
                ), // icons
                TextStylePropertyName.TEXT_TRANSFORM,
                TextTransform.UPPERCASE,
                context
        );
    }

    private static void textWrapping(final SpreadsheetContextMenu menu,
                                     final SpreadsheetSelectionMenuContext context) {
        menu.subMenu(
                context.idPrefix() + "text-wrapping" + SpreadsheetIds.SUB_MENU,
                "Wrapping"
        ).checkedItem(
                context.idPrefix() + "clip" + SpreadsheetIds.MENU_ITEM, // id
                "Clip", // text
                Optional.of(
                        SpreadsheetIcons.textWrappingClip()
                ), // icons
                TextStylePropertyName.OVERFLOW_WRAP,
                OverflowWrap.NORMAL,
                context
        ).checkedItem(
                context.idPrefix() + "overflow" + SpreadsheetIds.MENU_ITEM,
                "Overflow", // text
                Optional.of(
                        SpreadsheetIcons.textWrappingOverflow()
                ), // icons
                TextStylePropertyName.OVERFLOW_X,
                Overflow.VISIBLE,
                context
        ).checkedItem(
                context.idPrefix() + "wrap" + SpreadsheetIds.MENU_ITEM, // id
                "Wrap", // text
                Optional.of(
                        SpreadsheetIcons.textWrappingWrap()
                ), // icons
                TextStylePropertyName.OVERFLOW_X,
                Overflow.HIDDEN,
                context
        );
    }

    private static void alignment(final SpreadsheetContextMenu menu,
                                  final SpreadsheetSelectionMenuContext context) {
        menu.subMenu(
                context.idPrefix() + "alignment" + SpreadsheetIds.SUB_MENU,
                "Alignment"
        ).checkedItem(
                context.idPrefix() + "left" + SpreadsheetIds.MENU_ITEM, // id
                "Left", // text
                Optional.of(
                        SpreadsheetIcons.alignLeft()
                ), // icons
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT,
                context
        ).checkedItem(
                context.idPrefix() + "center" + SpreadsheetIds.MENU_ITEM, // id
                "Center", // text
                Optional.of(
                        SpreadsheetIcons.alignCenter()
                ), // icons
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.CENTER,
                context
        ).checkedItem(
                context.idPrefix() + "right" + SpreadsheetIds.MENU_ITEM, // id
                "Right", // text
                Optional.of(
                        SpreadsheetIcons.alignRight()
                ), // icons
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.RIGHT,
                context
        ).checkedItem(
                context.idPrefix() + "justify" + SpreadsheetIds.MENU_ITEM, // id
                "Justify", // text
                Optional.of(
                        SpreadsheetIcons.alignJustify()
                ), // icons
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.JUSTIFY,
                context
        );
    }

    private static void verticalAlignment(final SpreadsheetContextMenu menu,
                                          final SpreadsheetSelectionMenuContext context) {
        menu.subMenu(
                context.idPrefix() + "vertical-alignment" + SpreadsheetIds.SUB_MENU,
                "Vertical Alignment"
        ).checkedItem(
                context.idPrefix() + "top" + SpreadsheetIds.MENU_ITEM, // id
                "Top", // text
                Optional.of(
                        SpreadsheetIcons.verticalAlignTop()
                ), // icons
                TextStylePropertyName.VERTICAL_ALIGN,
                VerticalAlign.TOP,
                context
        ).checkedItem(
                context.idPrefix() + "middle" + SpreadsheetIds.MENU_ITEM, // id
                "Middle", // text
                Optional.of(
                        SpreadsheetIcons.verticalAlignMiddle()
                ), // icons
                TextStylePropertyName.VERTICAL_ALIGN,
                VerticalAlign.MIDDLE,
                context
        ).checkedItem(
                context.idPrefix() + "bottom" + SpreadsheetIds.MENU_ITEM, // id
                "Bottom", // text
                Optional.of(
                        SpreadsheetIcons.verticalAlignBottom()
                ), // icons
                TextStylePropertyName.VERTICAL_ALIGN,
                VerticalAlign.BOTTOM,
                context
        );
    }

    private static void insertColumns(final HistoryToken historyToken,
                                      final SpreadsheetSelection selection,
                                      final SpreadsheetContextMenu menu,
                                      final SpreadsheetSelectionMenuContext context) {
        if (selection.isColumnReference() | selection.isColumnReferenceRange() | selection.isCellReference() || selection.isCellRange()) {
            menu.separator();

            final HistoryToken columnHistoryToken = historyToken.setColumn(
                    selection.toColumnOrColumnRange()
            );
            insertSubMenu(
                    menu.subMenu(
                            context.idPrefix() + "insert-before-column" + SpreadsheetIds.SUB_MENU,
                            "Insert before column",
                            SpreadsheetIcons.columnInsertBefore()
                    ),
                    context.idPrefix() + "insert-before-column-",
                    columnHistoryToken::setInsertBefore
            );
            insertSubMenu(
                    menu.subMenu(
                            context.idPrefix() + "insert-after-column" + SpreadsheetIds.SUB_MENU,
                            "Insert after column",
                            SpreadsheetIcons.columnInsertAfter()
                    ),
                    context.idPrefix() + "insert-after-column-",
                    columnHistoryToken::setInsertAfter
            );
            menu.separator();
        }
    }

    private static void insertRows(final HistoryToken historyToken,
                                   final SpreadsheetSelection selection,
                                   final SpreadsheetContextMenu menu,
                                   final SpreadsheetSelectionMenuContext context) {
        if (selection.isRowReference() | selection.isRowReferenceRange() | selection.isCellReference() || selection.isCellRange()) {
            menu.separator();

            final HistoryToken rowHistoryToken = historyToken.setRow(
                    selection.toRowOrRowRange()
            );
            insertSubMenu(
                    menu.subMenu(
                            context.idPrefix() + "insert-row-before" + SpreadsheetIds.SUB_MENU,
                            "Insert before row",
                            SpreadsheetIcons.rowInsertBefore()
                    ),
                    context.idPrefix() + "insert-row-before-",
                    rowHistoryToken::setInsertBefore
            );
            insertSubMenu(
                    menu.subMenu(
                            context.idPrefix() + "insert-row-before" + SpreadsheetIds.SUB_MENU,
                            "Insert after row",
                            SpreadsheetIcons.rowInsertAfter()
                    ),
                    context.idPrefix() + "insert-row-before-",
                    rowHistoryToken::setInsertAfter
            );

            menu.separator();
        }
    }

    private static void freezeUnfreeze(final HistoryToken historyToken,
                                       final SpreadsheetContextMenu menu,
                                       final SpreadsheetSelectionMenuContext context) {
        menu.separator();

        menu.item(
                SpreadsheetContextMenuItem.with(
                        context.idPrefix() + "freeze" + SpreadsheetIds.MENU_ITEM,
                        "Freeze"
                ).historyToken(
                        historyToken.freezeOrEmpty()
                )
        ).item(
                SpreadsheetContextMenuItem.with(
                        context.idPrefix() + "unfreeze" + SpreadsheetIds.MENU_ITEM,
                        "Unfreeze"
                ).historyToken(
                        historyToken.unfreezeOrEmpty()
                )
        );

        menu.separator();
    }

    private static void label(final HistoryToken historyToken,
                              final SpreadsheetSelection selection,
                              final SpreadsheetContextMenu menu,
                              final SpreadsheetSelectionMenuContext context) {
        menu.separator();

        final Set<SpreadsheetLabelMapping> labelMappings = context.labelMappings(selection);

        SpreadsheetContextMenu sub = menu.subMenu(
                context.idPrefix() + "label" + SpreadsheetIds.SUB_MENU,
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
                            context.idPrefix() + "label-" + i + SpreadsheetIds.MENU_ITEM,
                            label + " (" + mapping.target() + ")"
                    )
            );

            i++;
        }

        sub.item(
                historyToken.setLabelName(Optional.empty())
                        .contextMenuItem(
                                context.idPrefix() + "label-create" + SpreadsheetIds.MENU_ITEM,
                                "Create..."
                        )
        );

        menu.separator();
    }

    private static void insertSubMenu(final SpreadsheetContextMenu menu,
                                      final String idPrefix,
                                      final Function<OptionalInt, HistoryToken> setCount) {
        for (int i = 1; i <= 3; i++) {
            menu.item(
                    setCount.apply(
                            OptionalInt.of(i)
                    ).contextMenuItem(
                            idPrefix + "-" + i + SpreadsheetIds.MENU_ITEM,
                            String.valueOf(i)
                    )
            );
        }

        // insert a url which will display a modal to prompt the user for the actual count
        menu.item(
                setCount.apply(
                        OptionalInt.empty()
                ).contextMenuItem(
                        idPrefix + SpreadsheetIds.MENU_ITEM,
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
