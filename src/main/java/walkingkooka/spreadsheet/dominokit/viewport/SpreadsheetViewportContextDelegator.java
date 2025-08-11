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

import walkingkooka.tree.text.TextStyle;

public interface SpreadsheetViewportContextDelegator extends SpreadsheetViewportContext {

    @Override
    default TextStyle allCellsStyle() {
        return this.spreadsheetViewportContext()
            .allCellsStyle();
    }

    @Override
    default TextStyle selectedAllCellsStyle() {
        return this.spreadsheetViewportContext()
            .selectedAllCellsStyle();
    }

    @Override
    default TextStyle cellStyle() {
        return this.spreadsheetViewportContext()
            .cellStyle();
    }

    @Override
    default TextStyle selectedCellStyle(final TextStyle cellStyle) {
        return this.spreadsheetViewportContext()
            .selectedCellStyle(cellStyle);
    }

    @Override
    default TextStyle columnStyle() {
        return this.spreadsheetViewportContext()
            .columnStyle();
    }

    @Override
    default TextStyle selectedColumnStyle() {
        return this.spreadsheetViewportContext()
            .selectedColumnStyle();
    }

    @Override
    default TextStyle rowStyle() {
        return this.spreadsheetViewportContext()
            .rowStyle();
    }

    @Override
    default TextStyle selectedRowStyle() {
        return this.spreadsheetViewportContext()
            .selectedRowStyle();
    }

    @Override
    default TextStyle hideZeroStyle(final TextStyle style) {
        return this.spreadsheetViewportContext()
            .hideZeroStyle(style);
    }
    
    SpreadsheetViewportContext spreadsheetViewportContext();
}
