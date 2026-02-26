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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.color.Color;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.color.ColorComponent;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
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
import walkingkooka.tree.text.TextStyleProperty;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.tree.text.TextTransform;
import walkingkooka.tree.text.VerticalAlign;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeSet;

/**
 * Creates a menu containing the style along with items for all individual {@link walkingkooka.tree.text.TextStylePropertyName}.
 */
final class SpreadsheetSelectionMenuStyle {

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
    static void build(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                      final SpreadsheetContextMenu menu,
                      final SpreadsheetSelectionMenuContext context) {
        style(
            historyToken,
            menu,
            context
        );
    }

    private static void style(final HistoryToken historyToken,
                              final SpreadsheetContextMenu menu,
                              final SpreadsheetSelectionMenuContext context) {
        alignment(menu, context);
        verticalAlignment(menu, context);
        color(menu, context);
        backgroundColor(menu, context);
        fontWeight(menu, context);
        fontStyle(menu, context);
        textDecoration(menu, context);
        textCase(historyToken, menu, context);
        textWrapping(menu, context);
        border(menu, context);

        menu.separator();

        clearStyle(historyToken, menu, context);

        menu.separator();

        recentStyle(historyToken, menu, context);
    }

    private static void color(final SpreadsheetContextMenu menu,
                              final SpreadsheetSelectionMenuContext context) {
        colorItem(
            TextStylePropertyName.COLOR,
            menu,
            context
        );
    }

    private static void backgroundColor(final SpreadsheetContextMenu menu,
                                        final SpreadsheetSelectionMenuContext context) {
        colorItem(
            TextStylePropertyName.BACKGROUND_COLOR,
            menu,
            context
        );
    }

    private static void colorItem(final TextStylePropertyName<Color> color,
                                  final SpreadsheetContextMenu menu,
                                  final SpreadsheetSelectionMenuContext context) {
        final String id = context.idPrefix() + color.value();

        final SpreadsheetContextMenu sub = menu.subMenu(
            id + SpreadsheetElementIds.SUB_MENU,
            CaseKind.kebabToTitle(
                color.value()
            ),
            SpreadsheetIcons.palette()
        );

        sub.item(
            ColorComponent.with(
                id + "-",
                (h) -> Optional.of(
                    h.setStylePropertyName(color)
                ),
                context
            )
        );
    }

    private static void clearStyle(final HistoryToken historyToken,
                                   final SpreadsheetContextMenu menu,
                                   final SpreadsheetSelectionMenuContext context) {
        menu.item(
            historyToken.setStylePropertyName(TextStylePropertyName.WILDCARD)
                .clearSaveValue()
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

    private static void recentStyle(final HistoryToken historyToken,
                                    final SpreadsheetContextMenu menu,
                                    final SpreadsheetSelectionMenuContext context) {
        int i = 0;
        final String idPrefix = context.idPrefix() + "recent-style";

        // sort recent before creating menu items...

        for (final TextStyleProperty<?> style : new TreeSet<>(context.recentTextStyleProperties())) {
            final TextStylePropertyName<?> name = style.name();
            final Optional<?> value = style.value();

            final String label = CaseKind.kebabToTitle(
                name.value()
            );

            menu.item(
                historyToken.setStylePropertyName(name)
                    .setSaveValue(value)
                    .contextMenuItem(
                        idPrefix + "-" + i + SpreadsheetElementIds.MENU_ITEM,
                        value.isPresent() ?
                            "Set " + label + " " + value.get() :
                            "Clear " + label
                    )
            );
        }
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
            context
        ).checkedItem(
            context.idPrefix() + "underline" + SpreadsheetElementIds.MENU_ITEM, // id
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
            context.idPrefix() + "text-case" + SpreadsheetElementIds.SUB_MENU,
            "Text case"
        ).item(
            historyToken.setStylePropertyName(TextStylePropertyName.TEXT_TRANSFORM)
                .clearSaveValue()
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
            context
        ).checkedItem(
            context.idPrefix() + "lower" + SpreadsheetElementIds.MENU_ITEM, // id
            "Lower case", // text
            Optional.of(
                SpreadsheetIcons.textCaseLower()
            ), // icons
            TextStylePropertyName.TEXT_TRANSFORM,
            TextTransform.LOWERCASE,
            context
        ).checkedItem(
            context.idPrefix() + "upper" + SpreadsheetElementIds.MENU_ITEM, // id
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
            context
        ).checkedItem(
            context.idPrefix() + "overflow" + SpreadsheetElementIds.MENU_ITEM,
            "Overflow", // text
            Optional.of(
                SpreadsheetIcons.textWrappingOverflow()
            ), // icons
            TextStylePropertyName.OVERFLOW_X,
            Overflow.VISIBLE,
            context
        ).checkedItem(
            context.idPrefix() + "wrap" + SpreadsheetElementIds.MENU_ITEM, // id
            "Wrap", // text
            Optional.of(
                SpreadsheetIcons.textWrappingWrap()
            ), // icons
            TextStylePropertyName.OVERFLOW_X,
            Overflow.HIDDEN,
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

        final String borderIdPrefix = context.idPrefix() + "border";

        for (final Entry<BoxEdge, Icon<?>> boxEdgeAndIcon : BORDER_BOX_EDGE_AND_ICONS.entrySet()) {
            final BoxEdge boxEdge = boxEdgeAndIcon.getKey();

            final String enumName = boxEdge.name();
            final String idPrefix = borderIdPrefix + '-' + enumName.toLowerCase();

            borderItem(
                idPrefix + '-',
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
            boxEdge.borderColorPropertyName(),
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
                context
            );
        }

        // clear
        styleSubMenu.item(
            context.historyToken()
                .setStylePropertyName(propertyName)
                .clearSaveValue()
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
                context
            );
        }

        // clear
        borderWidthSubMenu.item(
            context.historyToken()
                .setStylePropertyName(propertyName)
                .clearSaveValue()
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
            context
        ).checkedItem(
            context.idPrefix() + "center" + SpreadsheetElementIds.MENU_ITEM, // id
            "Center", // text
            Optional.of(
                SpreadsheetIcons.alignCenter()
            ), // icons
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.CENTER,
            context
        ).checkedItem(
            context.idPrefix() + "right" + SpreadsheetElementIds.MENU_ITEM, // id
            "Right", // text
            Optional.of(
                SpreadsheetIcons.alignRight()
            ), // icons
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.RIGHT,
            context
        ).checkedItem(
            context.idPrefix() + "justify" + SpreadsheetElementIds.MENU_ITEM, // id
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
            context
        ).checkedItem(
            context.idPrefix() + "middle" + SpreadsheetElementIds.MENU_ITEM, // id
            "Middle", // text
            Optional.of(
                SpreadsheetIcons.verticalAlignMiddle()
            ), // icons
            TextStylePropertyName.VERTICAL_ALIGN,
            VerticalAlign.MIDDLE,
            context
        ).checkedItem(
            context.idPrefix() + "bottom" + SpreadsheetElementIds.MENU_ITEM, // id
            "Bottom", // text
            Optional.of(
                SpreadsheetIcons.verticalAlignBottom()
            ), // icons
            TextStylePropertyName.VERTICAL_ALIGN,
            VerticalAlign.BOTTOM,
            context
        );
    }

    /**
     * Stop creation
     */
    private SpreadsheetSelectionMenuStyle() {
        throw new UnsupportedOperationException();
    }
}
