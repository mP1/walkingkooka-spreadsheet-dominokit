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

import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.tree.text.TextStyle;

import java.util.function.Function;

/**
 * A collection of {@link SpreadsheetViewportContext}
 */
public final class SpreadsheetViewportContexts implements PublicStaticHelper {

    /**
     * {@see BasicSpreadsheetViewportContext}
     */
    public static SpreadsheetViewportContext basic(final TextStyle allCellsStyle,
                                                   final TextStyle selectedAllCellsStyle,
                                                   final TextStyle cellStyle,
                                                   final Function<TextStyle,TextStyle> selectedCellStyle,
                                                   final TextStyle columnStyle,
                                                   final TextStyle selectedColumnStyle,
                                                   final TextStyle rowStyle,
                                                   final TextStyle selectedRowStyle,
                                                   final Function<TextStyle, TextStyle> hideZeroStyle) {
        return BasicSpreadsheetViewportContext.with(
            allCellsStyle,
            selectedAllCellsStyle,
            cellStyle,
            selectedCellStyle,
            columnStyle,
            selectedColumnStyle,
            rowStyle,
            selectedRowStyle,
            hideZeroStyle
        );
    }

    /**
     * {@see FakeSpreadsheetViewportContext}
     */
    public static SpreadsheetViewportContext fake() {
        return new FakeSpreadsheetViewportContext();
    }

    /**
     * Stop creation
     */
    private SpreadsheetViewportContexts() {
        throw new UnsupportedOperationException();
    }
}
