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

import walkingkooka.spreadsheet.viewport.SpreadsheetViewportNavigationContextTesting;

public final class SpreadsheetViewportComponentSpreadsheetViewportComponentTableContextSpreadsheetViewportNavigationContextTest implements SpreadsheetViewportNavigationContextTesting<SpreadsheetViewportComponentSpreadsheetViewportComponentTableContextSpreadsheetViewportNavigationContext> {

    @Override
    public void leftPixelsWithNullColumnFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void leftPixelsWithNegativePixelsFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testMoveLeftWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void rightPixelsWithNullColumnFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void rightPixelsWithNegativePixelsFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testMoveRightColumnWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void upPixelsWithNullRowFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void upPixelsWithNegativePixelsFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void moveUpWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void downPixelsWithNullRowFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void downPixelsWithNegativePixelsFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void downRowWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetViewportComponentSpreadsheetViewportComponentTableContextSpreadsheetViewportNavigationContext createContext() {
        return new SpreadsheetViewportComponentSpreadsheetViewportComponentTableContextSpreadsheetViewportNavigationContext(null);
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetViewportComponentSpreadsheetViewportComponentTableContextSpreadsheetViewportNavigationContext> type() {
        return SpreadsheetViewportComponentSpreadsheetViewportComponentTableContextSpreadsheetViewportNavigationContext.class;
    }
}
