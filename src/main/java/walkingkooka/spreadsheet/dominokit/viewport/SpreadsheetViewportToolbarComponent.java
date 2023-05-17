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

    static SpreadsheetViewportToolbarComponent bold(final SpreadsheetViewportToolbarComponentWatcher watcher) {
        return SpreadsheetViewportToolbarComponentButton.with(
                Icons.ALL.format_bold_mdi(),
                SpreadsheetViewportToolbar.id(
                        TextStylePropertyName.FONT_WEIGHT,
                        FontWeight.BOLD
                ),
                watcher
        );
    }

    static SpreadsheetViewportToolbarComponent italics(final SpreadsheetViewportToolbarComponentWatcher watcher) {
        return SpreadsheetViewportToolbarComponentButton.with(
                Icons.ALL.format_italic_mdi(),
                SpreadsheetViewportToolbar.id(
                        TextStylePropertyName.FONT_STYLE,
                        FontStyle.ITALIC
                ),
                watcher
        );
    }

    static SpreadsheetViewportToolbarComponent strikeThru(final SpreadsheetViewportToolbarComponentWatcher watcher) {
        return SpreadsheetViewportToolbarComponentButton.with(
                Icons.ALL.format_strikethrough_mdi(),
                SpreadsheetViewportToolbar.id(
                        TextStylePropertyName.TEXT_DECORATION_LINE,
                        TextDecorationLine.LINE_THROUGH
                ),
                watcher
        );
    }

    static SpreadsheetViewportToolbarComponent textAlignLeft(final SpreadsheetViewportToolbarComponentWatcher watcher) {
        return SpreadsheetViewportToolbarComponentButton.with(
                Icons.ALL.format_align_left_mdi(),
                SpreadsheetViewportToolbar.id(
                        TextStylePropertyName.TEXT_ALIGN,
                        TextAlign.LEFT
                ),
                watcher
        );
    }

    static SpreadsheetViewportToolbarComponent textAlignCenter(final SpreadsheetViewportToolbarComponentWatcher watcher) {
        return SpreadsheetViewportToolbarComponentButton.with(
                Icons.ALL.format_align_center_mdi(),
                SpreadsheetViewportToolbar.id(
                        TextStylePropertyName.TEXT_ALIGN,
                        TextAlign.CENTER
                ),
                watcher
        );
    }

    static SpreadsheetViewportToolbarComponent textAlignRight(final SpreadsheetViewportToolbarComponentWatcher watcher) {
        return SpreadsheetViewportToolbarComponentButton.with(
                Icons.ALL.format_align_right_mdi(),
                SpreadsheetViewportToolbar.id(
                        TextStylePropertyName.TEXT_ALIGN,
                        TextAlign.RIGHT
                ),
                watcher
        );
    }

    static SpreadsheetViewportToolbarComponent textAlignJustify(final SpreadsheetViewportToolbarComponentWatcher watcher) {
        return SpreadsheetViewportToolbarComponentButton.with(
                Icons.ALL.format_align_justify_mdi(),
                SpreadsheetViewportToolbar.id(
                        TextStylePropertyName.TEXT_ALIGN,
                        TextAlign.JUSTIFY
                ),
                watcher
        );
    }

    static SpreadsheetViewportToolbarComponent underline(final SpreadsheetViewportToolbarComponentWatcher watcher) {
        return SpreadsheetViewportToolbarComponentButton.with(
                Icons.ALL.format_underline_mdi(),
                SpreadsheetViewportToolbar.id(
                        TextStylePropertyName.TEXT_DECORATION_LINE,
                        TextDecorationLine.UNDERLINE
                ),
                watcher
        );
    }

    static SpreadsheetViewportToolbarComponent verticalAlignTop(final SpreadsheetViewportToolbarComponentWatcher watcher) {
        return SpreadsheetViewportToolbarComponentButton.with(
                Icons.ALL.format_align_top_mdi(),
                SpreadsheetViewportToolbar.id(
                        TextStylePropertyName.VERTICAL_ALIGN,
                        VerticalAlign.TOP
                ),
                watcher
        );
    }

    static SpreadsheetViewportToolbarComponent verticalAlignMiddle(final SpreadsheetViewportToolbarComponentWatcher watcher) {
        return SpreadsheetViewportToolbarComponentButton.with(
                Icons.ALL.format_align_middle_mdi(),
                SpreadsheetViewportToolbar.id(
                        TextStylePropertyName.VERTICAL_ALIGN,
                        VerticalAlign.MIDDLE
                ),
                watcher
        );
    }

    static SpreadsheetViewportToolbarComponent verticalAlignBottom(final SpreadsheetViewportToolbarComponentWatcher watcher) {
        return SpreadsheetViewportToolbarComponentButton.with(
                Icons.ALL.format_align_bottom_mdi(),
                SpreadsheetViewportToolbar.id(
                        TextStylePropertyName.VERTICAL_ALIGN,
                        VerticalAlign.BOTTOM
                ),
                watcher
        );
    }

    /**
     * The root {@link HTMLElement}
     */
    abstract HTMLElement element();
}
