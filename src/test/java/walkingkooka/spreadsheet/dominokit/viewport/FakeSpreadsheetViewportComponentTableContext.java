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

import walkingkooka.spreadsheet.dominokit.history.FakeHistoryContext;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.tree.text.TextStyle;

public class FakeSpreadsheetViewportComponentTableContext extends FakeHistoryContext
    implements SpreadsheetViewportComponentTableContext {

    public FakeSpreadsheetViewportComponentTableContext() {
        super();
    }

    @Override
    public boolean hideZeroValues() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStyle defaultCellStyle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean mustRefresh() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isShiftKeyDown() {
        throw new UnsupportedOperationException();
    }

    @Override
    public AnchoredSpreadsheetSelection extendColumn(final SpreadsheetColumnReference column) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AnchoredSpreadsheetSelection extendRow(final SpreadsheetRowReference row) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void debug(Object... values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void info(Object... values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void warn(Object... values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void error(final Object... values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetViewportCache spreadsheetViewportCache() {
        throw new UnsupportedOperationException();
    }
}
