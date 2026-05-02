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

package walkingkooka.spreadsheet.dominokit.viewport.menu;

import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.color.Color;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.dominokit.value.textstyle.color.ColorPaletteComponent;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.text.CaseKind;
import walkingkooka.tree.text.BorderStyle;
import walkingkooka.tree.text.BoxEdge;
import walkingkooka.tree.text.FontStyle;
import walkingkooka.tree.text.FontWeight;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextDecorationLine;
import walkingkooka.tree.text.TextStyleProperty;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.tree.text.TextTransform;
import walkingkooka.tree.text.TextWrapping;
import walkingkooka.tree.text.VerticalAlign;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

final class SpreadsheetSelectionMenuValuesStyle extends SpreadsheetSelectionMenuValues<TextStyleProperty<?>> {

    static SpreadsheetSelectionMenuValuesStyle with(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                                                    final SpreadsheetContextMenu menu,
                                                    final SpreadsheetSelectionMenuContext context) {
        return new SpreadsheetSelectionMenuValuesStyle(
            historyToken,
            menu,
            context
        );
    }

    private SpreadsheetSelectionMenuValuesStyle(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                                                final SpreadsheetContextMenu menu,
                                                final SpreadsheetSelectionMenuContext context) {
        super(historyToken, menu, context);
    }

    @Override
    void values() {
        this.alignment();
        this.verticalAlignment();
        this.color();
        this.backgroundColor();
        this.fontWeight();
        this.fontStyle();
        this.textDecoration();
        this.textCase();
        this.textWrapping();
        this.border();
    }

    private void alignment() {
        final SpreadsheetSelectionMenuContext context = this.context;
        final String idPrefix = this.idPrefix + "alignment";

        this.menu.subMenu(
            idPrefix + SpreadsheetElementIds.SUB_MENU,
            "Alignment"
        ).checkedItem(
            menuItem(idPrefix, TextAlign.LEFT), // id
            "Left", // text
            Optional.of(
                SpreadsheetIcons.alignLeft()
            ), // icons
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.LEFT,
            context
        ).checkedItem(
            menuItem(idPrefix, TextAlign.CENTER), // id
            "Center", // text
            Optional.of(
                SpreadsheetIcons.alignCenter()
            ), // icons
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.CENTER,
            context
        ).checkedItem(
            menuItem(idPrefix, TextAlign.RIGHT), // id
            "Right", // text
            Optional.of(
                SpreadsheetIcons.alignRight()
            ), // icons
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.RIGHT,
            context
        ).checkedItem(
            menuItem(idPrefix, TextAlign.JUSTIFY), // id
            "Justify", // text
            Optional.of(
                SpreadsheetIcons.alignJustify()
            ), // icons
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.JUSTIFY,
            context
        );
    }

    private void backgroundColor() {
        this.colorItem(
            this.idPrefix,
            TextStylePropertyName.BACKGROUND_COLOR,
            this.menu
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

    private void border() {
        final String idPrefix = this.idPrefix + TextStylePropertyName.BORDER;

        final SpreadsheetContextMenu border = this.menu.subMenu(
            idPrefix + SpreadsheetElementIds.SUB_MENU,
            "Border"
        );

        for (final Entry<BoxEdge, Icon<?>> boxEdgeAndIcon : BORDER_BOX_EDGE_AND_ICONS.entrySet()) {
            final BoxEdge boxEdge = boxEdgeAndIcon.getKey();

            final String enumName = boxEdge.name();

            this.borderItem(
                idPrefix + CaseKind.SNAKE.change(
                    enumName,
                    CaseKind.TITLE
                ),
                border.subMenu(
                    idPrefix + SpreadsheetElementIds.SUB_MENU, // id
                    CaseKind.SNAKE.change(
                        enumName,
                        CaseKind.TITLE
                    ), // label
                    boxEdgeAndIcon.getValue() // icon
                ),
                boxEdge
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
    private void borderItem(final String idPrefix,
                            final SpreadsheetContextMenu menu,
                            final BoxEdge boxEdge) {
        this.colorItem(
            this.idPrefix, // HACK because colorItem uses the TextStylePropertyName which would cause duplication of property name
            boxEdge.borderColorPropertyName(),
            menu
        );

        this.borderStyle(
            idPrefix + "Style", // without trailing dash
            menu,
            boxEdge
        );

        this.borderWidth(
            idPrefix + "Width", // without trailing dash
            menu,
            boxEdge
        );
    }

    private void borderStyle(final String idPrefix,
                             final SpreadsheetContextMenu menu,
                             final BoxEdge boxEdge) {
        final SpreadsheetContextMenu styleSubMenu = menu.subMenu(
            idPrefix + SpreadsheetElementIds.SUB_MENU,
            "Style"
        );

        final TextStylePropertyName<BorderStyle> propertyName = boxEdge.borderStylePropertyName();
        final SpreadsheetSelectionMenuContext context = this.context;

        for (final BorderStyle borderStyle : BORDER_STYLE) {
            styleSubMenu.checkedItem(
                menuItem(idPrefix, borderStyle), // id
                CaseKind.SNAKE.change(
                    borderStyle.name(),
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
                .setStyleProperty(
                    propertyName.clearValue()
                ).contextMenuItem(
                    idPrefix + "-clear" + SpreadsheetElementIds.MENU_ITEM, // id
                    "Clear"
                ).icon(
                    Optional.of(
                        SpreadsheetIcons.borderStyleClear()
                    )
                )
        );
    }

    private final static List<BorderStyle> BORDER_STYLE = Lists.of(
        BorderStyle.NONE,
        BorderStyle.HIDDEN,
        BorderStyle.DOTTED,
        BorderStyle.DASHED,
        BorderStyle.SOLID,
        BorderStyle.DOUBLE,
        BorderStyle.GROOVE,
        BorderStyle.RIDGE,
        BorderStyle.INSET,
        BorderStyle.OUTSET
    );

    // none
    // 1
    // 2
    // clear
    private void borderWidth(final String idPrefix,
                             final SpreadsheetContextMenu menu,
                             final BoxEdge boxEdge) {
        final SpreadsheetSelectionMenuContext context = this.context;

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
                .setStyleProperty(
                    propertyName.clearValue()
                ).contextMenuItem(
                    idPrefix + "-clear" + SpreadsheetElementIds.MENU_ITEM, // id
                    "Clear"
                ).icon(
                    Optional.of(
                        SpreadsheetIcons.borderStyleClear()
                    )
                )
        );
    }

    private void color() {
        this.colorItem(
            this.idPrefix,
            TextStylePropertyName.COLOR,
            this.menu
        );
    }

    private void colorItem(final String idPrefix,
                           final TextStylePropertyName<Color> color,
                           final SpreadsheetContextMenu menu) {
        final SpreadsheetSelectionMenuContext context = this.context;
        final String colorValue = color.value();
        final String id = idPrefix + CaseKind.KEBAB.change(
            colorValue,
            CaseKind.CAMEL
        );

        final SpreadsheetContextMenu sub = menu.subMenu(
            id + SpreadsheetElementIds.SUB_MENU,
            CaseKind.kebabToTitle(colorValue),
            SpreadsheetIcons.palette()
        );

        sub.item(
            ColorPaletteComponent.with(
                id + "-",
                ColorPaletteComponent.historyTokenPreparer(color),
                context
            ).setValue(
                context.selectionSummary()
                    .flatMap(
                        (SpreadsheetCell cell) -> cell.style()
                            .get(color)
                    )
            )
        );
    }

    private void fontStyle() {
        final SpreadsheetSelectionMenuContext context = this.context;

        this.menu.checkedItem(
            menuItem(idPrefix, FontStyle.ITALIC), // id
            "Italics", // text
            Optional.of(
                SpreadsheetIcons.italics()
            ), // icons
            TextStylePropertyName.FONT_STYLE,
            FontStyle.ITALIC,
            context
        );
    }

    private void fontWeight() {
        final SpreadsheetSelectionMenuContext context = this.context;

        this.menu.checkedItem(
            menuItem(idPrefix, FontWeight.BOLD), // id
            "Bold", // text
            Optional.of(
                SpreadsheetIcons.bold()
            ), // icons
            TextStylePropertyName.FONT_WEIGHT,
            FontWeight.BOLD,
            context
        );
    }

    private void textCase() {
        final SpreadsheetSelectionMenuContext context = this.context;
        final String idPrefix = this.idPrefix + "textCase";

        this.menu.subMenu(
            idPrefix + SpreadsheetElementIds.SUB_MENU,
            "Text case"
        ).item(
            this.historyToken.setStyleProperty(
                TextStylePropertyName.TEXT_TRANSFORM.clearValue()
            ).contextMenuItem(
                menuItem(idPrefix, TextTransform.NONE), // id
                "None"
            ).icon(
                Optional.of(
                    SpreadsheetIcons.textCaseUpper()
                )
            )
        ).checkedItem(
            menuItem(idPrefix, TextTransform.CAPITALIZE), // id
            "Capitalize", // text
            Optional.of(
                SpreadsheetIcons.textCaseCapitalize()
            ), // icons
            TextStylePropertyName.TEXT_TRANSFORM,
            TextTransform.CAPITALIZE,
            context
        ).checkedItem(
            menuItem(idPrefix, TextTransform.LOWERCASE), // id
            "Lower case", // text
            Optional.of(
                SpreadsheetIcons.textCaseLower()
            ), // icons
            TextStylePropertyName.TEXT_TRANSFORM,
            TextTransform.LOWERCASE,
            context
        ).checkedItem(
            menuItem(idPrefix, TextTransform.UPPERCASE), // id
            "Upper case", // text
            Optional.of(
                SpreadsheetIcons.textCaseUpper()
            ), // icons
            TextStylePropertyName.TEXT_TRANSFORM,
            TextTransform.UPPERCASE,
            context
        );
    }

    private void textDecoration() {
        final SpreadsheetSelectionMenuContext context = this.context;
        final String idPrefix = context.idPrefix();

        this.menu.checkedItem(
            menuItem(idPrefix, TextDecorationLine.LINE_THROUGH), // id
            "Strike-thru", // text
            Optional.of(
                SpreadsheetIcons.strikethrough()
            ), // icons
            TextStylePropertyName.TEXT_DECORATION_LINE,
            TextDecorationLine.LINE_THROUGH,
            context
        ).checkedItem(
            menuItem(idPrefix, TextDecorationLine.UNDERLINE), // id
            "Underline", // text
            Optional.of(
                SpreadsheetIcons.underline()
            ), // icons
            TextStylePropertyName.TEXT_DECORATION_LINE,
            TextDecorationLine.UNDERLINE,
            context
        );
    }

    private void textWrapping() {
        final SpreadsheetSelectionMenuContext context = this.context;
        final String idPrefix = this.idPrefix + "textWrapping";

        this.menu.subMenu(
            idPrefix + SpreadsheetElementIds.SUB_MENU,
            "Wrapping"
        ).checkedItem(
            menuItem(idPrefix, TextWrapping.CLIP), // id
            "Clip", // text
            Optional.of(
                SpreadsheetIcons.textWrappingClip()
            ), // icons
            TextStylePropertyName.TEXT_WRAPPING,
            TextWrapping.CLIP,
            context
        ).checkedItem(
            menuItem(idPrefix, TextWrapping.OVERFLOW), // id
            "Overflow", // text
            Optional.of(
                SpreadsheetIcons.textWrappingOverflow()
            ), // icons
            TextStylePropertyName.TEXT_WRAPPING,
            TextWrapping.OVERFLOW,
            context
        ).checkedItem(
            menuItem(idPrefix, TextWrapping.WRAP), // id
            "Wrap", // text
            Optional.of(
                SpreadsheetIcons.textWrappingWrap()
            ), // icons
            TextStylePropertyName.TEXT_WRAPPING,
            TextWrapping.WRAP,
            context
        );
    }

    private void verticalAlignment() {
        final SpreadsheetSelectionMenuContext context = this.context;
        final String idPrefix = this.idPrefix + "verticalAlignment";

        this.menu.subMenu(
            idPrefix + SpreadsheetElementIds.SUB_MENU,
            "Vertical Alignment"
        ).checkedItem(
            menuItem(idPrefix,  VerticalAlign.TOP),
            "Top", // text
            Optional.of(
                SpreadsheetIcons.verticalAlignTop()
            ), // icons
            TextStylePropertyName.VERTICAL_ALIGN,
            VerticalAlign.TOP,
            context
        ).checkedItem(
            menuItem(idPrefix,  VerticalAlign.MIDDLE),
            "Middle", // text
            Optional.of(
                SpreadsheetIcons.verticalAlignMiddle()
            ), // icons
            TextStylePropertyName.VERTICAL_ALIGN,
            VerticalAlign.MIDDLE,
            context
        ).checkedItem(
            menuItem(idPrefix,  VerticalAlign.BOTTOM),
            "Bottom", // text
            Optional.of(
                SpreadsheetIcons.verticalAlignBottom()
            ), // icons
            TextStylePropertyName.VERTICAL_ALIGN,
            VerticalAlign.BOTTOM,
            context
        );
    }


    @Override
    Optional<Icon<?>> clearIcon() {
        return Optional.of(
            SpreadsheetIcons.clearStyle()
        );
    }

    @Override //
    Collection<TextStyleProperty<?>> recentValues() {
        return this.context.recentTextStyleProperties();
    }

    @Override //
    String recentText(final TextStyleProperty<?> style) {
        return CaseKind.kebabToTitle(
            style.name()
                .value()
        );
    }

    @Override
    Optional<TextStyleProperty<?>> spreadsheetCellValue(final SpreadsheetCell cell) {
        return Optional.empty();
    }

    @Override //
    Class<TextStyleProperty<?>> type() {
        return Cast.to(TextStyleProperty.class);
    }

    private static String menuItem(final String idPrefix,
                                   final Enum<?> value) {
        return menuItem(
            idPrefix,
            CaseKind.SNAKE.change(
                value.name(),
                CaseKind.CAMEL
            )
        );
    }

    private static String menuItem(final String idPrefix,
                                   final Object value) {
        return (
            idPrefix.endsWith("-") ?
                idPrefix :
                idPrefix.concat("-")
        ) +
            value +
            SpreadsheetElementIds.MENU_ITEM;
    }
}
