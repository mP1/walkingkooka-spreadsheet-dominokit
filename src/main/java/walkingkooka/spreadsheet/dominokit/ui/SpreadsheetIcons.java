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

package walkingkooka.spreadsheet.dominokit.ui;

import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.icons.MdiIcon;
import org.dominokit.domino.ui.icons.UrlIcon;
import org.dominokit.domino.ui.icons.lib.Icons;
import walkingkooka.reflect.PublicStaticHelper;

public final class SpreadsheetIcons implements PublicStaticHelper {

    public static MdiIcon alignLeft() {
        return Icons.format_align_left();
    }

    public static MdiIcon alignCenter() {
        return Icons.format_align_center();
    }

    public static MdiIcon alignRight() {
        return Icons.format_align_right();
    }

    public static MdiIcon alignJustify() {
        return Icons.format_align_justify();
    }

    public static MdiIcon arrowLeft() {
        return Icons.arrow_left();
    }

    public static MdiIcon arrowRight() {
        return Icons.arrow_right();
    }

    public static MdiIcon arrowUp() {
        return Icons.arrow_up();
    }

    public static MdiIcon arrowDown() {
        return Icons.arrow_down();
    }

    public static MdiIcon bold() {
        return Icons.format_bold();
    }

    public static MdiIcon borderAll() {
        return Icons.border_all_variant();
    }

    public static MdiIcon borderBottom() {
        return Icons.border_bottom_variant();
    }

    public static MdiIcon borderColor() {
        return Icons.border_color();
    }

    public static MdiIcon borderLeft() {
        return Icons.border_left_variant();
    }

    public static MdiIcon borderRight() {
        return Icons.border_right_variant();
    }

    public static MdiIcon borderStyle() {
        return Icons.border_style();
    }

    public static MdiIcon borderStyleClear() {
        return Icons.format_clear();
    }

    public static MdiIcon borderTop() {
        return Icons.border_top_variant();
    }

    public static MdiIcon borderWidthClear() {
        return Icons.format_clear();
    }

    public static MdiIcon cellClear() {
        return Icons.close();
    }

    public static MdiIcon cellDelete() {
        return Icons.close();
    }

    public static MdiIcon cellsFind() {
        return Icons.magnify();
    }

    public static MdiIcon cellsLock() {
        return Icons.clock_plus_outline();
    }

    public static MdiIcon cellsUnlock() {
        return Icons.lock_remove_outline();
    }

    public static MdiIcon checked() {
        return Icons.check();
    }

    public static MdiIcon clearStyle() {
        return Icons.format_clear();
    }

    public static MdiIcon close() {
        return Icons.close_circle();
    }

    public static MdiIcon columnClear() {
        return Icons.close();
    }

    public static MdiIcon columnInsertAfter() {
        return Icons.table_column_plus_after();
    }

    public static MdiIcon columnInsertBefore() {
        return Icons.table_column_plus_before();
    }

    public static MdiIcon columnRemove() {
        return Icons.table_column_remove();
    }

    public static MdiIcon columnWidth() {
        return Icons.table_column_width();
    }

    public static MdiIcon commentEdit() {
        return Icons.tooltip_edit_outline();
    }

    public static MdiIcon commentRemove() {
        return Icons.tooltip_remove_outline();
    }

    public static MdiIcon copy() {
        return Icons.content_copy();
    }

    public static MdiIcon cut() {
        return Icons.content_cut();
    }

    public static MdiIcon formatPattern() {
        return Icons.format_text();
    }

    public static MdiIcon formatPatternRemove() {
        return Icons.format_clear();
    }

    public static MdiIcon hideZeroValues() {
        return Icons.star();
    }

    public static MdiIcon highlight() {
        return Icons.spotlight_beam();
    }

    public static MdiIcon italics() {
        return Icons.format_italic();
    }

    public static MdiIcon labelAdd() {
        return Icons.flag_checkered();
    }

    public static MdiIcon palette() {
        return Icons.palette();
    }

    public static MdiIcon parsePattern() {
        return Icons.format_text();
    }

    public static MdiIcon parsePatternRemove() {
        return Icons.format_clear();
    }

    public static MdiIcon paste() {
        return Icons.content_paste();
    }

    public static MdiIcon reload() {
        return Icons.reload();
    }

    public static MdiIcon rowClear() {
        return Icons.close();
    }

    public static MdiIcon rowInsertAfter() {
        return Icons.table_row_plus_after();
    }

    public static MdiIcon rowInsertBefore() {
        return Icons.table_row_plus_before();
    }

    public static MdiIcon rowRemove() {
        return Icons.table_row_remove();
    }

    public static MdiIcon sortAlphaAscending() {
        return Icons.sort_alphabetical_ascending();
    }

    public static MdiIcon sortAlphaDescending() {
        return Icons.sort_alphabetical_descending();
    }

    public static MdiIcon sortDateAscending() {
        return Icons.sort_calendar_ascending();
    }

    public static MdiIcon sortDateDescending() {
        return Icons.sort_calendar_descending();
    }

    // DATE and DATE TIME are the same, need a combo icon DATE + TIME
    public static MdiIcon sortDateTimeAscending() {
        return Icons.sort_calendar_ascending();
    }

    public static MdiIcon sortDateTimeDescending() {
        return Icons.sort_calendar_descending();
    }

    public static MdiIcon sortNumericAscending() {
        return Icons.sort_numeric_ascending();
    }

    public static MdiIcon sortNumericDescending() {
        return Icons.sort_numeric_descending();
    }

    public static MdiIcon sortTimeAscending() {
        return Icons.sort_clock_ascending_outline();
    }

    public static MdiIcon sortTimeDescending() {
        return Icons.sort_clock_descending_outline();
    }

    public static MdiIcon spreadsheetListTableEmpty() {
        return Icons.gauge_empty();
    }

    public static MdiIcon spreadsheetListTableNext() {
        return Icons.arrow_right();
    }

    public static MdiIcon spreadsheetListTablePrevious() {
        return Icons.arrow_left();
    }

    public static MdiIcon spreadsheetListReload() {
        return reload();
    }

    public static MdiIcon strikethrough() {
        return Icons.format_strikethrough();
    }

    // https://iconduck.com/icons/94965/swagger
    public static Icon<?> swagger() {
        return UrlIcon.create(
                "data:image/png;base64,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",
                ""
        );
    }

    public static MdiIcon textBoxClear() {
        return Icons.close_circle();
    }

    public static MdiIcon textCaseCapitalize() {
        return Icons.format_letter_case();
    }

    public static MdiIcon textCaseLower() {
        return Icons.format_letter_case_lower();
    }

    public static MdiIcon textCaseUpper() {
        return Icons.format_letter_case_upper();
    }

    public static MdiIcon textWrappingClip() {
        return Icons.format_text_wrapping_clip();
    }

    public static MdiIcon textWrappingOverflow() {
        return Icons.format_text_wrapping_overflow();
    }

    public static MdiIcon textWrappingWrap() {
        return Icons.format_text_wrapping_wrap();
    }

    public static MdiIcon underline() {
        return Icons.format_underline();
    }

    public static MdiIcon verticalAlignTop() {
        return Icons.format_align_top();
    }

    public static MdiIcon verticalAlignMiddle() {
        return Icons.format_align_middle();
    }

    public static MdiIcon verticalAlignBottom() {
        return Icons.format_align_bottom();
    }

    private SpreadsheetIcons() {
        throw new UnsupportedOperationException();
    }
}
