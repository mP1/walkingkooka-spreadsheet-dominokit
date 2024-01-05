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

package walkingkooka.spreadsheet.dominokit.ui.toolbar;

import elemental2.dom.HTMLElement;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.ui.Component;
import walkingkooka.spreadsheet.dominokit.ui.ComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIcons;
import walkingkooka.tree.text.FontStyle;
import walkingkooka.tree.text.FontWeight;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextDecorationLine;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.tree.text.VerticalAlign;

/**
 * A ui such as an icon within a {@link SpreadsheetToolbarComponent}.
 */
abstract class SpreadsheetToolbarComponentItem implements Component<HTMLElement>,
        ComponentLifecycle {

    static SpreadsheetToolbarComponentItem bold(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemButtonTextStyleProperty.with(
                TextStylePropertyName.FONT_WEIGHT,
                FontWeight.BOLD,
                SpreadsheetIcons.bold(),
                "Bold",
                context
        );
    }

    static SpreadsheetToolbarComponentItem clear(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemButtonTextStyleClear.with(
                context
        );
    }

    /**
     * {@link SpreadsheetToolbarComponentItemButtonPatternFormat}
     */
    static SpreadsheetToolbarComponentItem formatPattern(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemButtonPatternFormat.with(context);
    }

    static SpreadsheetToolbarComponentItem italics(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemButtonTextStyleProperty.with(
                TextStylePropertyName.FONT_STYLE,
                FontStyle.ITALIC,
                SpreadsheetIcons.italics(),
                "Italics",
                context
        );
    }

    /**
     * {@link SpreadsheetToolbarComponentItemButtonPatternParse}
     */
    static SpreadsheetToolbarComponentItem parsePattern(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemButtonPatternParse.with(context);
    }

    static SpreadsheetToolbarComponentItem strikeThru(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemButtonTextStyleProperty.with(
                TextStylePropertyName.TEXT_DECORATION_LINE,
                TextDecorationLine.LINE_THROUGH,
                SpreadsheetIcons.strikethrough(),
                "Strike-thru",
                context
        );
    }

    static SpreadsheetToolbarComponentItem textAlignLeft(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemButtonTextStyleProperty.with(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT,
                SpreadsheetIcons.alignLeft(),
                "Left align",
                context
        );
    }

    static SpreadsheetToolbarComponentItem textAlignCenter(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemButtonTextStyleProperty.with(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.CENTER,
                SpreadsheetIcons.alignCenter(),
                "Center align",
                context
        );
    }

    static SpreadsheetToolbarComponentItem textAlignRight(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemButtonTextStyleProperty.with(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.RIGHT,
                SpreadsheetIcons.alignRight(),
                "Right align",
                context
        );
    }

    static SpreadsheetToolbarComponentItem textAlignJustify(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemButtonTextStyleProperty.with(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.JUSTIFY,
                SpreadsheetIcons.alignJustify(),
                "Justify",
                context
        );
    }

    static SpreadsheetToolbarComponentItem underline(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemButtonTextStyleProperty.with(
                TextStylePropertyName.TEXT_DECORATION_LINE,
                TextDecorationLine.UNDERLINE,
                SpreadsheetIcons.underline(),
                "Underline",
                context
        );
    }

    static SpreadsheetToolbarComponentItem verticalAlignTop(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemButtonTextStyleProperty.with(
                TextStylePropertyName.VERTICAL_ALIGN,
                VerticalAlign.TOP,
                SpreadsheetIcons.verticalAlignTop(),
                "Align top",
                context
        );
    }

    static SpreadsheetToolbarComponentItem verticalAlignMiddle(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemButtonTextStyleProperty.with(
                TextStylePropertyName.VERTICAL_ALIGN,
                VerticalAlign.MIDDLE,
                SpreadsheetIcons.verticalAlignMiddle(),
                "Align middle",
                context
        );
    }

    static SpreadsheetToolbarComponentItem verticalAlignBottom(final HistoryTokenContext context) {
        return SpreadsheetToolbarComponentItemButtonTextStyleProperty.with(
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
