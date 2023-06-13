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
import org.dominokit.domino.ui.icons.Icons;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.tree.text.FontStyle;
import walkingkooka.tree.text.FontWeight;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextDecorationLine;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.tree.text.VerticalAlign;

/**
 * A component such as an icon within a {@link SpreadsheetViewportToolbar}.
 */
abstract class SpreadsheetViewportToolbarComponent {

    static SpreadsheetViewportToolbarComponent bold(final HistoryTokenContext context) {
        return SpreadsheetViewportToolbarComponentTextStylePropertyButton.with(
                TextStylePropertyName.FONT_WEIGHT,
                FontWeight.BOLD,
                Icons.ALL.format_bold_mdi(),
                "Bold",
                context
        );
    }

    static SpreadsheetViewportToolbarComponent italics(final HistoryTokenContext context) {
        return SpreadsheetViewportToolbarComponentTextStylePropertyButton.with(
                TextStylePropertyName.FONT_STYLE,
                FontStyle.ITALIC,
                Icons.ALL.format_italic_mdi(),
                "Italics",
                context
        );
    }

    static SpreadsheetViewportToolbarComponent strikeThru(final HistoryTokenContext context) {
        return SpreadsheetViewportToolbarComponentTextStylePropertyButton.with(
                TextStylePropertyName.TEXT_DECORATION_LINE,
                TextDecorationLine.LINE_THROUGH,
                Icons.ALL.format_strikethrough_mdi(),
                "Strike-thru",
                context
        );
    }

    static SpreadsheetViewportToolbarComponent textAlignLeft(final HistoryTokenContext context) {
        return SpreadsheetViewportToolbarComponentTextStylePropertyButton.with(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT,
                Icons.ALL.format_align_left_mdi(),
                "Left align",
                context
        );
    }

    static SpreadsheetViewportToolbarComponent textAlignCenter(final HistoryTokenContext context) {
        return SpreadsheetViewportToolbarComponentTextStylePropertyButton.with(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.CENTER,
                Icons.ALL.format_align_center_mdi(),
                "Center align",
                context
        );
    }

    static SpreadsheetViewportToolbarComponent textAlignRight(final HistoryTokenContext context) {
        return SpreadsheetViewportToolbarComponentTextStylePropertyButton.with(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.RIGHT,
                Icons.ALL.format_align_right_mdi(),
                "Right align",
                context
        );
    }

    static SpreadsheetViewportToolbarComponent textAlignJustify(final HistoryTokenContext context) {
        return SpreadsheetViewportToolbarComponentTextStylePropertyButton.with(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.JUSTIFY,
                Icons.ALL.format_align_justify_mdi(),
                "Justify",
                context
        );
    }

    static SpreadsheetViewportToolbarComponent underline(final HistoryTokenContext context) {
        return SpreadsheetViewportToolbarComponentTextStylePropertyButton.with(
                TextStylePropertyName.TEXT_DECORATION_LINE,
                TextDecorationLine.UNDERLINE,
                Icons.ALL.format_underline_mdi(),
                "Underline",
                context
        );
    }

    static SpreadsheetViewportToolbarComponent verticalAlignTop(final HistoryTokenContext context) {
        return SpreadsheetViewportToolbarComponentTextStylePropertyButton.with(
                TextStylePropertyName.VERTICAL_ALIGN,
                VerticalAlign.TOP,
                Icons.ALL.format_align_top_mdi(),
                "Align top",
                context
        );
    }

    static SpreadsheetViewportToolbarComponent verticalAlignMiddle(final HistoryTokenContext context) {
        return SpreadsheetViewportToolbarComponentTextStylePropertyButton.with(
                TextStylePropertyName.VERTICAL_ALIGN,
                VerticalAlign.MIDDLE,
                Icons.ALL.format_align_middle_mdi(),
                "Align middle",
                context
        );
    }

    static SpreadsheetViewportToolbarComponent verticalAlignBottom(final HistoryTokenContext context) {
        return SpreadsheetViewportToolbarComponentTextStylePropertyButton.with(
                TextStylePropertyName.VERTICAL_ALIGN,
                VerticalAlign.BOTTOM,
                Icons.ALL.format_align_bottom_mdi(),
                "Align bottom",
                context
        );
    }

    /**
     * The root {@link HTMLElement}
     */
    abstract HTMLElement element();

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
