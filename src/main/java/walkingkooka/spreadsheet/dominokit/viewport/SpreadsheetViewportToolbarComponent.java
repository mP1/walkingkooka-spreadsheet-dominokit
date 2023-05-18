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
import walkingkooka.spreadsheet.dominokit.AppContext;
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

    static SpreadsheetViewportToolbarComponent bold(final AppContext context) {
        return SpreadsheetViewportToolbarComponentButton.with(
                TextStylePropertyName.FONT_WEIGHT,
                FontWeight.BOLD,
                Icons.ALL.format_bold_mdi(),
                context
        );
    }

    static SpreadsheetViewportToolbarComponent italics(final AppContext context) {
        return SpreadsheetViewportToolbarComponentButton.with(
                TextStylePropertyName.FONT_STYLE,
                FontStyle.ITALIC,
                Icons.ALL.format_italic_mdi(),
                context
        );
    }

    static SpreadsheetViewportToolbarComponent strikeThru(final AppContext context) {
        return SpreadsheetViewportToolbarComponentButton.with(
                TextStylePropertyName.TEXT_DECORATION_LINE,
                TextDecorationLine.LINE_THROUGH,
                Icons.ALL.format_strikethrough_mdi(),
                context
        );
    }

    static SpreadsheetViewportToolbarComponent textAlignLeft(final AppContext context) {
        return SpreadsheetViewportToolbarComponentButton.with(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT,
                Icons.ALL.format_align_left_mdi(),
                context
        );
    }

    static SpreadsheetViewportToolbarComponent textAlignCenter(final AppContext context) {
        return SpreadsheetViewportToolbarComponentButton.with(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.CENTER,
                Icons.ALL.format_align_center_mdi(),
                context
        );
    }

    static SpreadsheetViewportToolbarComponent textAlignRight(final AppContext context) {
        return SpreadsheetViewportToolbarComponentButton.with(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.RIGHT,
                Icons.ALL.format_align_right_mdi(),
                context
        );
    }

    static SpreadsheetViewportToolbarComponent textAlignJustify(final AppContext context) {
        return SpreadsheetViewportToolbarComponentButton.with(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.JUSTIFY,
                Icons.ALL.format_align_justify_mdi(),
                context
        );
    }

    static SpreadsheetViewportToolbarComponent underline(final AppContext context) {
        return SpreadsheetViewportToolbarComponentButton.with(
                TextStylePropertyName.TEXT_DECORATION_LINE,
                TextDecorationLine.UNDERLINE,
                Icons.ALL.format_underline_mdi(),
                context
        );
    }

    static SpreadsheetViewportToolbarComponent verticalAlignTop(final AppContext context) {
        return SpreadsheetViewportToolbarComponentButton.with(
                TextStylePropertyName.VERTICAL_ALIGN,
                VerticalAlign.TOP,
                Icons.ALL.format_align_top_mdi(),
                context
        );
    }

    static SpreadsheetViewportToolbarComponent verticalAlignMiddle(final AppContext context) {
        return SpreadsheetViewportToolbarComponentButton.with(
                TextStylePropertyName.VERTICAL_ALIGN,
                VerticalAlign.MIDDLE,
                Icons.ALL.format_align_middle_mdi(),
                context
        );
    }

    static SpreadsheetViewportToolbarComponent verticalAlignBottom(final AppContext context) {
        return SpreadsheetViewportToolbarComponentButton.with(
                TextStylePropertyName.VERTICAL_ALIGN,
                VerticalAlign.BOTTOM,
                Icons.ALL.format_align_bottom_mdi(),
                context
        );
    }

    /**
     * The root {@link HTMLElement}
     */
    abstract HTMLElement element();
}
