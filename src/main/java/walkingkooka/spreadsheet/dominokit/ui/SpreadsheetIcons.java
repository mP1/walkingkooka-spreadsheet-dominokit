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

import org.dominokit.domino.ui.icons.MdiIcon;
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

    public static MdiIcon cellClear() {
        return Icons.close();
    }

    public static MdiIcon cellDelete() {
        return Icons.close();
    }

    public static MdiIcon cellsFind() {
        return Icons.magnify();
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

    public static MdiIcon copy() {
        return Icons.content_copy();
    }

    public static MdiIcon cut() {
        return Icons.content_cut();
    }

    public static MdiIcon formatPattern() {
        return Icons.format_text();
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

    public static MdiIcon palette() {
        return Icons.palette();
    }

    public static MdiIcon parsePattern() {
        return Icons.format_text();
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

    public static MdiIcon strikethrough() {
        return Icons.format_strikethrough();
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
