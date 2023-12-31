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

package walkingkooka.spreadsheet.dominokit.ui.viewport;

import elemental2.dom.HTMLElement;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.ui.Component;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIcons;
import walkingkooka.tree.text.FontStyle;
import walkingkooka.tree.text.FontWeight;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextDecorationLine;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.tree.text.VerticalAlign;

/**
 * A ui such as an icon within a {@link SpreadsheetViewportToolbarComponent}.
 */
abstract class SpreadsheetViewportToolbarComponentItem implements Component<HTMLElement> {

    static SpreadsheetViewportToolbarComponentItem bold(final HistoryTokenContext context) {
        return SpreadsheetViewportToolbarComponentItemButtonTextStyleProperty.with(
                TextStylePropertyName.FONT_WEIGHT,
                FontWeight.BOLD,
                SpreadsheetIcons.bold(),
                "Bold",
                context
        );
    }

    static SpreadsheetViewportToolbarComponentItem clear(final HistoryTokenContext context) {
        return SpreadsheetViewportToolbarComponentItemButtonTextStyleClear.with(
                context
        );
    }

    /**
     * {@link SpreadsheetViewportToolbarComponentItemButtonPatternFormat}
     */
    static SpreadsheetViewportToolbarComponentItem formatPattern(final HistoryTokenContext context) {
        return SpreadsheetViewportToolbarComponentItemButtonPatternFormat.with(context);
    }

    static SpreadsheetViewportToolbarComponentItem italics(final HistoryTokenContext context) {
        return SpreadsheetViewportToolbarComponentItemButtonTextStyleProperty.with(
                TextStylePropertyName.FONT_STYLE,
                FontStyle.ITALIC,
                SpreadsheetIcons.italics(),
                "Italics",
                context
        );
    }

    /**
     * {@link SpreadsheetViewportToolbarComponentItemButtonPatternParse}
     */
    static SpreadsheetViewportToolbarComponentItem parsePattern(final HistoryTokenContext context) {
        return SpreadsheetViewportToolbarComponentItemButtonPatternParse.with(context);
    }

    static SpreadsheetViewportToolbarComponentItem strikeThru(final HistoryTokenContext context) {
        return SpreadsheetViewportToolbarComponentItemButtonTextStyleProperty.with(
                TextStylePropertyName.TEXT_DECORATION_LINE,
                TextDecorationLine.LINE_THROUGH,
                SpreadsheetIcons.strikethrough(),
                "Strike-thru",
                context
        );
    }

    static SpreadsheetViewportToolbarComponentItem textAlignLeft(final HistoryTokenContext context) {
        return SpreadsheetViewportToolbarComponentItemButtonTextStyleProperty.with(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT,
                SpreadsheetIcons.alignLeft(),
                "Left align",
                context
        );
    }

    static SpreadsheetViewportToolbarComponentItem textAlignCenter(final HistoryTokenContext context) {
        return SpreadsheetViewportToolbarComponentItemButtonTextStyleProperty.with(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.CENTER,
                SpreadsheetIcons.alignCenter(),
                "Center align",
                context
        );
    }

    static SpreadsheetViewportToolbarComponentItem textAlignRight(final HistoryTokenContext context) {
        return SpreadsheetViewportToolbarComponentItemButtonTextStyleProperty.with(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.RIGHT,
                SpreadsheetIcons.alignRight(),
                "Right align",
                context
        );
    }

    static SpreadsheetViewportToolbarComponentItem textAlignJustify(final HistoryTokenContext context) {
        return SpreadsheetViewportToolbarComponentItemButtonTextStyleProperty.with(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.JUSTIFY,
                SpreadsheetIcons.alignJustify(),
                "Justify",
                context
        );
    }

    static SpreadsheetViewportToolbarComponentItem underline(final HistoryTokenContext context) {
        return SpreadsheetViewportToolbarComponentItemButtonTextStyleProperty.with(
                TextStylePropertyName.TEXT_DECORATION_LINE,
                TextDecorationLine.UNDERLINE,
                SpreadsheetIcons.underline(),
                "Underline",
                context
        );
    }

    static SpreadsheetViewportToolbarComponentItem verticalAlignTop(final HistoryTokenContext context) {
        return SpreadsheetViewportToolbarComponentItemButtonTextStyleProperty.with(
                TextStylePropertyName.VERTICAL_ALIGN,
                VerticalAlign.TOP,
                SpreadsheetIcons.verticalAlignTop(),
                "Align top",
                context
        );
    }

    static SpreadsheetViewportToolbarComponentItem verticalAlignMiddle(final HistoryTokenContext context) {
        return SpreadsheetViewportToolbarComponentItemButtonTextStyleProperty.with(
                TextStylePropertyName.VERTICAL_ALIGN,
                VerticalAlign.MIDDLE,
                SpreadsheetIcons.verticalAlignMiddle(),
                "Align middle",
                context
        );
    }

    static SpreadsheetViewportToolbarComponentItem verticalAlignBottom(final HistoryTokenContext context) {
        return SpreadsheetViewportToolbarComponentItemButtonTextStyleProperty.with(
                TextStylePropertyName.VERTICAL_ALIGN,
                VerticalAlign.BOTTOM,
                SpreadsheetIcons.verticalAlignBottom(),
                "Align bottom",
                context
        );
    }

    /**
     * The root {@link HTMLElement}
     */
    public abstract HTMLElement element();

    // onToolbarRefresh.................................................................................................

    /**
     * Fired at the beginning of a selection or cell change.
     * This should be used to reset counters etc.
     */
    abstract void onToolbarRefreshBegin();

    /**
     * Fired for each cell within the current selection.
     */
    abstract void onToolbarRefreshSelectedCell(final SpreadsheetCell cell,
                                               final AppContext context);

    /**
     * Fired after receiving all cell {@link walkingkooka.tree.text.TextStyle}.
     */
    abstract void onToolbarRefreshEnd(final int cellPresentCount,
                                      final AppContext context);

}
