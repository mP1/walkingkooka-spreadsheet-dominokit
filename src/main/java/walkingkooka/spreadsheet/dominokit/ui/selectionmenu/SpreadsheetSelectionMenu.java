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

import org.dominokit.domino.ui.badges.Badge;
import org.dominokit.domino.ui.icons.MdiIcon;
import org.dominokit.domino.ui.utils.DominoElement;
import walkingkooka.spreadsheet.dominokit.history.AnchoredSpreadsheetSelectionHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIds;
import walkingkooka.spreadsheet.dominokit.ui.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.ui.contextmenu.SpreadsheetContextMenuItem;
import walkingkooka.spreadsheet.dominokit.ui.metadatacolorpicker.SpreadsheetMetadataColorPickerComponent;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.FontStyle;
import walkingkooka.tree.text.FontWeight;
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

import static org.dominokit.domino.ui.style.ColorsCss.dui_bg_orange;
import static org.dominokit.domino.ui.style.SpacingCss.dui_rounded_full;

/**
 * Builds a context menu for any given {@link SpreadsheetSelection}.
 */
public class SpreadsheetSelectionMenu {

    /**
     * Renders a drop down menu attaching the context menu to the given {@link DominoElement}.
     * This should make it possible to attach a context menu to the cell in the viewport and the formula component.
     */
    public static void render(final AnchoredSpreadsheetSelectionHistoryToken historyToken,
                              final DominoElement<?> element,
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
        final SpreadsheetContextMenu menu = SpreadsheetContextMenu.empty(
                element,
                context
        );

        // TODO add tick if already selected
        if (selection.isCellReference() || selection.isCellRange() || selection.isLabelName()) {
            renderStyle(
                    historyToken,
                    menu.subMenu(
                            context.idPrefix() + "style-MenuItem",
                            "Style"
                    ),
                    context
            );

            final Locale locale = context.spreadsheetMetadata()
                    .getOrFail(SpreadsheetMetadataPropertyName.LOCALE);

            renderFormat(
                    historyToken,
                    locale,
                    context.recentFormatPatterns()
                            .stream()
                            .map(t -> (SpreadsheetFormatPattern) t.pattern().get()) // cant fail must be SFP
                            .collect(Collectors.toList()),
                    menu,
                    context
            );
            renderParse(
                    historyToken,
                    locale,
                    context.recentParsePatterns()
                            .stream()
                            .map(t -> (SpreadsheetParsePattern) t.pattern().get()) // cant fail must be SPP
                            .collect(Collectors.toList()),
                    menu,
                    context
            );
            menu.separator();
        }
        menu.separator();

        renderClearDelete(
                historyToken,
                menu,
                context
        );
        renderInsertColumns(historyToken, selection, menu, context);
        renderInsertRows(historyToken, selection, menu, context);
        renderFreezeUnfreeze(historyToken, menu, context);

        if (selection.isCellReference() || selection.isCellRange()) {
            menu.separator();
            renderLabel(historyToken, selection, menu, context);
        }

        menu.focus();
    }

    private static void renderParse(final HistoryToken historyToken,
                                    final Locale locale,
                                    final List<SpreadsheetParsePattern> recents,
                                    final SpreadsheetContextMenu menu,
                                    final SpreadsheetSelectionMenuContext context) {
        final String idPrefix = context.idPrefix() + "parse-";

        SpreadsheetSelectionMenuPatternParse.with(
                historyToken,
                locale,
                recents,
                idPrefix
        ).build(
                menu.subMenu(
                        idPrefix + "-MenuItem",
                        "Parse",
                        SpreadsheetIcons.parsePattern()
                )
        );
    }

    private static void renderFormat(final HistoryToken historyToken,
                                     final Locale locale,
                                     final List<SpreadsheetFormatPattern> recents,
                                     final SpreadsheetContextMenu menu,
                                     final SpreadsheetSelectionMenuContext context) {
        final String idPrefix = context.idPrefix() + "format-";

        SpreadsheetSelectionMenuPatternFormat.with(
                historyToken,
                locale,
                recents,
                idPrefix
        ).build(
                menu.subMenu(
                        idPrefix + "-MenuItem",
                        "Format",
                        SpreadsheetIcons.formatPattern()
                )
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
    // CLEAR STYLE
    private static void renderStyle(final HistoryToken historyToken,
                                    final SpreadsheetContextMenu menu,
                                    final SpreadsheetSelectionMenuContext context) {
        renderAlignment(historyToken, menu, context);
        renderVerticalAlignment(historyToken, menu, context);
        renderColor(historyToken, menu, context);
        renderBackgroundColor(historyToken, menu, context);
        renderFontWeight(historyToken, menu, context);
        renderFontStyle(historyToken, menu, context);
        renderTextDecoration(historyToken, menu, context);
        renderTextCase(historyToken, menu, context);
        renderClearStyle(historyToken, menu, context);

        menu.separator();
    }

    private static void renderColor(final HistoryToken historyToken,
                                    final SpreadsheetContextMenu menu,
                                    final SpreadsheetSelectionMenuContext context) {
        renderColorItem(
                "color",
                "Color",
                historyToken.setStyle(TextStylePropertyName.COLOR),
                menu,
                context
        );
    }

    private static void renderBackgroundColor(final HistoryToken historyToken,
                                              final SpreadsheetContextMenu menu,
                                              final SpreadsheetSelectionMenuContext context) {
        renderColorItem(
                "background-color",
                "Background color",
                historyToken.setStyle(TextStylePropertyName.BACKGROUND_COLOR),
                menu,
                context
        );
    }

    private static void renderColorItem(final String id,
                                        final String text,
                                        final HistoryToken historyToken,
                                        final SpreadsheetContextMenu menu,
                                        final SpreadsheetSelectionMenuContext context) {
        final SpreadsheetContextMenu sub = menu.subMenu(
                context.idPrefix() + id + SpreadsheetIds.MENU_ITEM,
                text,
                SpreadsheetIcons.palette()
        );

        final SpreadsheetMetadataColorPickerComponent colors = SpreadsheetMetadataColorPickerComponent.with(historyToken);
        colors.element().className = "dui dui-menu-item";

        colors.refreshAll(
                historyToken,
                context.spreadsheetMetadata()
        );
        sub.item(colors);
    }

    private static void renderClearStyle(final HistoryToken historyToken,
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

    private static void renderClearDelete(final HistoryToken historyToken,
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

    private static void renderFontWeight(final HistoryToken historyToken,
                                         final SpreadsheetContextMenu menu,
                                         final SpreadsheetSelectionMenuContext context) {
        menu.item(
                historyToken.setStyle(TextStylePropertyName.FONT_WEIGHT)
                        .setSave(FontWeight.BOLD)
                        .contextMenuItem(
                                context.idPrefix() + "bold" + SpreadsheetIds.MENU_ITEM,
                                "Bold"
                        ).icon(
                                Optional.of(
                                        SpreadsheetIcons.bold()
                                )
                        )
        );
    }

    private static void renderFontStyle(final HistoryToken historyToken,
                                        final SpreadsheetContextMenu menu,
                                        final SpreadsheetSelectionMenuContext context) {
        menu.item(
                historyToken.setStyle(TextStylePropertyName.FONT_STYLE)
                        .setSave(FontStyle.ITALIC)
                        .contextMenuItem(
                                context.idPrefix() + "italics" + SpreadsheetIds.MENU_ITEM,
                                "Italics"
                        ).icon(
                                Optional.of(
                                        SpreadsheetIcons.italics()
                                )
                        )
        );
    }

    private static void renderTextDecoration(final HistoryToken historyToken,
                                             final SpreadsheetContextMenu menu,
                                             final SpreadsheetSelectionMenuContext context) {
        menu.item(
                historyToken.setStyle(TextStylePropertyName.TEXT_DECORATION_LINE)
                        .setSave(TextDecorationLine.LINE_THROUGH)
                        .contextMenuItem(
                                context.idPrefix() + "strike-thru" + SpreadsheetIds.MENU_ITEM,
                                "Strike-thru"
                        ).icon(
                                Optional.of(
                                        SpreadsheetIcons.strikethrough()
                                )
                        )
        );

        menu.item(
                historyToken.setStyle(TextStylePropertyName.TEXT_DECORATION_LINE)
                        .setSave(TextDecorationLine.UNDERLINE)
                        .contextMenuItem(
                                context.idPrefix() + "underline" + SpreadsheetIds.MENU_ITEM,
                                "Underline"
                        ).icon(
                                Optional.of(
                                        SpreadsheetIcons.underline()
                                )
                        )
        );
    }

    private static void renderTextCase(final HistoryToken historyToken,
                                       final SpreadsheetContextMenu menu,
                                       final SpreadsheetSelectionMenuContext context) {
        menu.subMenu(
                context.idPrefix() + "text-case",
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
        ).item(
                historyToken.setStyle(TextStylePropertyName.TEXT_TRANSFORM)
                        .setSave(TextTransform.CAPITALIZE)
                        .contextMenuItem(
                                context.idPrefix() + "capitalize" + SpreadsheetIds.MENU_ITEM,
                                "Capitalize"
                        ).icon(
                                Optional.of(
                                        SpreadsheetIcons.textCaseCapitalize()
                                )
                        )
        ).item(
                historyToken.setStyle(TextStylePropertyName.TEXT_TRANSFORM)
                        .setSave(TextTransform.LOWERCASE)
                        .contextMenuItem(
                                context.idPrefix() + "lower" + SpreadsheetIds.MENU_ITEM,
                                "Lower case"
                        ).icon(
                                Optional.of(
                                        SpreadsheetIcons.textCaseLower()
                                )
                        )
        ).item(
                historyToken.setStyle(TextStylePropertyName.TEXT_TRANSFORM)
                        .setSave(TextTransform.UPPERCASE)
                        .contextMenuItem(
                                context.idPrefix() + "upper" + SpreadsheetIds.MENU_ITEM,
                                "Upper case"
                        ).icon(
                                Optional.of(
                                        SpreadsheetIcons.textCaseUpper()
                                )
                        )
        );
    }

    private static void renderAlignment(final HistoryToken historyToken,
                                        final SpreadsheetContextMenu menu,
                                        final SpreadsheetSelectionMenuContext context) {
        menu.subMenu(
                context.idPrefix() + "alignment",
                "Alignment"
        ).item(
                historyToken.setStyle(
                                TextStylePropertyName.TEXT_ALIGN
                        ).setSave(TextAlign.LEFT)
                        .contextMenuItem(
                                context.idPrefix() + "left" + SpreadsheetIds.MENU_ITEM,
                                "Left"
                        ).icon(
                                Optional.of(
                                        SpreadsheetIcons.alignLeft()
                                )
                        )
        ).item(
                historyToken.setStyle(
                                TextStylePropertyName.TEXT_ALIGN
                        ).setSave(TextAlign.CENTER)
                        .contextMenuItem(
                                context.idPrefix() + "center" + SpreadsheetIds.MENU_ITEM,
                                "Center"
                        ).icon(
                                Optional.of(
                                        SpreadsheetIcons.alignCenter()
                                )
                        )
        ).item(
                historyToken.setStyle(
                                TextStylePropertyName.TEXT_ALIGN
                        ).setSave(TextAlign.RIGHT)
                        .contextMenuItem(
                                context.idPrefix() + "right" + SpreadsheetIds.MENU_ITEM,
                                "Right"
                        ).icon(
                                Optional.of(
                                        SpreadsheetIcons.alignRight()
                                )
                        )
        ).item(
                historyToken.setStyle(
                                TextStylePropertyName.TEXT_ALIGN
                        ).setSave(TextAlign.JUSTIFY)
                        .contextMenuItem(
                                context.idPrefix() + "justify" + SpreadsheetIds.MENU_ITEM,
                                "Justify"
                        ).icon(
                                Optional.of(
                                        SpreadsheetIcons.alignJustify()
                                )
                        )
        );
    }

    private static void renderVerticalAlignment(final HistoryToken historyToken,
                                                final SpreadsheetContextMenu menu,
                                                final SpreadsheetSelectionMenuContext context) {
        menu.subMenu(
                context.idPrefix() + "vertical-alignment",
                "Vertical Alignment"
        ).item(
                historyToken.setStyle(
                                TextStylePropertyName.VERTICAL_ALIGN
                        ).setSave(VerticalAlign.TOP)
                        .contextMenuItem(
                                context.idPrefix() + "top" + SpreadsheetIds.MENU_ITEM,
                                "Top"
                        ).icon(
                                Optional.of(
                                        SpreadsheetIcons.verticalAlignTop()
                                )
                        )
        ).item(
                historyToken.setStyle(
                                TextStylePropertyName.VERTICAL_ALIGN
                        ).setSave(VerticalAlign.MIDDLE)
                        .contextMenuItem(
                                context.idPrefix() + "middle" + SpreadsheetIds.MENU_ITEM,
                                "Middle"
                        ).icon(
                                Optional.of(
                                        SpreadsheetIcons.verticalAlignMiddle()
                                )
                        )
        ).item(
                historyToken.setStyle(
                                TextStylePropertyName.VERTICAL_ALIGN
                        ).setSave(VerticalAlign.BOTTOM)
                        .contextMenuItem(
                                context.idPrefix() + "bottom" + SpreadsheetIds.MENU_ITEM,
                                "Bottom"
                        ).icon(
                                Optional.of(
                                        SpreadsheetIcons.verticalAlignBottom()
                                )
                        )
        );
    }

    private static void renderInsertColumns(final HistoryToken historyToken,
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
                            context.idPrefix() + "insert-before-column",
                            "Insert before column",
                            SpreadsheetIcons.columnInsertBefore()
                    ),
                    context.idPrefix() + "insert-before-column-",
                    columnHistoryToken::setInsertBefore
            );
            insertSubMenu(
                    menu.subMenu(
                            context.idPrefix() + "insert-after-column",
                            "Insert after column",
                            SpreadsheetIcons.columnInsertAfter()
                    ),
                    context.idPrefix() + "insert-after-column-",
                    columnHistoryToken::setInsertAfter
            );
            menu.separator();
        }
    }

    private static void renderInsertRows(final HistoryToken historyToken,
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
                            context.idPrefix() + "insert-row-before",
                            "Insert before row",
                            SpreadsheetIcons.rowInsertBefore()
                    ),
                    context.idPrefix() + "insert-row-before-",
                    rowHistoryToken::setInsertBefore
            );
            insertSubMenu(
                    menu.subMenu(
                            context.idPrefix() + "insert-row-before",
                            "Insert after row",
                            SpreadsheetIcons.rowInsertAfter()
                    ),
                    context.idPrefix() + "insert-row-before-",
                    rowHistoryToken::setInsertAfter
            );

            menu.separator();
        }
    }

    private static void renderFreezeUnfreeze(final HistoryToken historyToken,
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

    private static void renderLabel(final HistoryToken historyToken,
                                    final SpreadsheetSelection selection,
                                    final SpreadsheetContextMenu menu,
                                    final SpreadsheetSelectionMenuContext context) {
        menu.separator();

        final Set<SpreadsheetLabelMapping> labelMappings = context.labelMappings(selection);

        SpreadsheetContextMenu sub = menu.subMenu(
                context.idPrefix() + "label" + SpreadsheetIds.MENU_ITEM,
                "Labels",
                Badge.create(
                        String.valueOf(labelMappings.size())
                ).addCss(
                        dui_bg_orange,
                        dui_rounded_full
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
}
