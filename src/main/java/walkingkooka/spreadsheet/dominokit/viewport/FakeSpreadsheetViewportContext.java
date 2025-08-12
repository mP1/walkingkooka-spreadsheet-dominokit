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

public class FakeSpreadsheetViewportContext implements SpreadsheetViewportContext {
    
    public FakeSpreadsheetViewportContext() {
        super();
    }

    @Override
    public TextStyle allCellsStyle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStyle selectedAllCellsStyle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStyle cellStyle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStyle selectedCellStyle(final TextStyle style) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStyle columnStyle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStyle selectedColumnStyle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStyle rowStyle() {
        throw new UnsupportedOperationException();
    }

    @Override 
    public TextStyle selectedRowStyle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStyle hideZeroStyle(final TextStyle style) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStyle showFormulasStyle(final TextStyle style) {
        throw new UnsupportedOperationException();
    }
}
