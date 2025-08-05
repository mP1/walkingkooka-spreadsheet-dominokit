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

import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.viewport.FakeSpreadsheetViewportNavigationContext;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewport;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportWindows;

final class SpreadsheetViewportComponentSpreadsheetViewportComponentTableContextSpreadsheetViewportNavigationContext extends FakeSpreadsheetViewportNavigationContext {

    static SpreadsheetViewportComponentSpreadsheetViewportComponentTableContextSpreadsheetViewportNavigationContext with(final SpreadsheetViewportCache cache) {
        return new SpreadsheetViewportComponentSpreadsheetViewportComponentTableContextSpreadsheetViewportNavigationContext(cache);
    }

    // @VisibleForTesting
    SpreadsheetViewportComponentSpreadsheetViewportComponentTableContextSpreadsheetViewportNavigationContext(final SpreadsheetViewportCache cache) {
        this.cache = cache;
    }

    @Override
    public boolean isColumnHidden(final SpreadsheetColumnReference column) {
        return cache.isColumnHidden(column);
    }

    @Override
    public boolean isRowHidden(final SpreadsheetRowReference row) {
        return this.cache.isRowHidden(row);
    }

    @Override
    public SpreadsheetViewportWindows windows(final SpreadsheetViewport viewport) {
        return this.cache.windows();
    }

    private final SpreadsheetViewportCache cache;

    @Override
    public String toString() {
        return this.cache.toString();
    }
}
