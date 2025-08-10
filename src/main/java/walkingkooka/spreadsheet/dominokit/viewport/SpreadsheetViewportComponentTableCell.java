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

package walkingkooka.spreadsheet.dominokit.viewport;

import elemental2.dom.HTMLElement;
import walkingkooka.collect.map.Maps;
import walkingkooka.color.Color;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.SpreadsheetDominoKitColor;
import walkingkooka.spreadsheet.dominokit.dom.HtmlElementComponentDelegator;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.BorderStyle;
import walkingkooka.tree.text.FontSize;
import walkingkooka.tree.text.FontStyle;
import walkingkooka.tree.text.FontVariant;
import walkingkooka.tree.text.FontWeight;
import walkingkooka.tree.text.Hyphens;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.tree.text.VerticalAlign;
import walkingkooka.tree.text.WordBreak;

import java.util.function.Predicate;

/**
 * Base class for all TABLE CELL (TD) components within a {@link SpreadsheetViewportComponentTable}
 */
abstract class SpreadsheetViewportComponentTableCell<E extends HTMLElement, C extends SpreadsheetViewportComponentTableCell<E, C>> implements HtmlComponent<E, C>,
    HtmlElementComponentDelegator<E, C> {

    SpreadsheetViewportComponentTableCell() {
        super();
    }

    final static Color BORDER_COLOR = SpreadsheetDominoKitColor.VIEWPORT_LINES_COLOR;
    final static BorderStyle BORDER_STYLE = BorderStyle.SOLID;
    final static Length<?> BORDER_LENGTH = Length.pixel(1.0);

    /**
     * Creates some CSS with some minimal styling, mostly width/height control.
     * Colors, fonts are other text type styles are not included.
     */
    static String setWidthAndHeight(final TextStyle style,
                                    final Length<?> width,
                                    final Length<?> height) {
        return style.setValues(
            Maps.of(
                TextStylePropertyName.MIN_WIDTH,
                width,
                TextStylePropertyName.WIDTH,
                width,
                TextStylePropertyName.MIN_HEIGHT,
                height,
                TextStylePropertyName.HEIGHT,
                height
            )
        ).text() + "box-sizing: border-box;";
    }

    private final static TextStyle STYLE = TextStyle.EMPTY
        .set(
            TextStylePropertyName.MARGIN,
            Length.none()
        ).setBorder(
            BORDER_COLOR,
            BORDER_STYLE,
            BORDER_LENGTH

        ).set(
            TextStylePropertyName.PADDING,
            Length.none()
        ).set(
            TextStylePropertyName.FONT_SIZE,
            FontSize.with(11)
        ).set(
            TextStylePropertyName.FONT_STYLE,
            FontStyle.NORMAL
        ).set(
            TextStylePropertyName.FONT_WEIGHT,
            FontWeight.NORMAL
        ).set(
            TextStylePropertyName.FONT_VARIANT,
            FontVariant.NORMAL
        ).set(
            TextStylePropertyName.HYPHENS,
            Hyphens.NONE
        ).set(
            TextStylePropertyName.WORD_BREAK,
            WordBreak.NORMAL
        );

    final static TextStyle CELL_STYLE = STYLE
        .set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.LEFT
        ).set(
            TextStylePropertyName.VERTICAL_ALIGN,
            VerticalAlign.TOP
        );

    final static TextStyle HEADER_STYLE = STYLE
        .set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.CENTER
        ).set(
            TextStylePropertyName.VERTICAL_ALIGN,
            VerticalAlign.MIDDLE
        );

    abstract void setIdAndName(final SpreadsheetId id,
                               final SpreadsheetName name);

    abstract void refresh(final Predicate<SpreadsheetSelection> selected,
                          final SpreadsheetViewportComponentTableContext context);

    @Override
    public final boolean isEditing() {
        return false;
    }

    // TreePrintable....................................................................................................

    @Override
    public final void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.htmlElementComponent()
                .printTree(printer);
        }
        printer.outdent();
    }
}
