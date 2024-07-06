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

import walkingkooka.color.Color;
import walkingkooka.color.WebColorName;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.spreadsheet.dominokit.ui.find.SpreadsheetCellFind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

/**
 * A collection of {@link Color} constants used in the app.
 */
public final class SpreadsheetDominoKitColor implements PublicStaticHelper {

    /**
     * This {@link Color} is mixed with cells that are selected for highlighting because of a {@link SpreadsheetCellFind} query.
     */
    public final static Color HIGHLIGHT_COLOR = WebColorName.YELLOW.color();

    /**
     * This color will be mixed with the cell background-color when {@link SpreadsheetMetadataPropertyName#HIDE_ZERO_VALUES} is true and the value is a zero number.
     */
    public final static Color HIDE_ZERO_VALUES_COLOR = WebColorName.LIGHTGREEN.color();

    /**
     * The default selected background color for toolbar button icons.
     */
    public final static Color TOOLBAR_ICON_SELECTED_BACKGROUND_COLOR = Color.parse("#ffff00");

    /**
     * The default selected background color for toolbar button icons.
     */
    public final static Color TOOLBAR_ICON_SELECTED_COLOR = Color.parse("#0000ff");

    /**
     * The background color for selected column/row headers.
     */
    public final static Color VIEWPORT_CELL_SELECTED_BACKGROUND_COLOR = Color.parse("#888");

    /**
     * The background color for unselected column/row headers.
     */
    public final static Color VIEWPORT_HEADER_UNSELECTED_BACKGROUND_COLOR = Color.parse("#aaa");

    /**
     * The background color for selected column/row headers.
     */
    public final static Color VIEWPORT_HEADER_SELECTED_BACKGROUND_COLOR = VIEWPORT_CELL_SELECTED_BACKGROUND_COLOR;

    private SpreadsheetDominoKitColor() {
        throw new UnsupportedOperationException();
    }
}
