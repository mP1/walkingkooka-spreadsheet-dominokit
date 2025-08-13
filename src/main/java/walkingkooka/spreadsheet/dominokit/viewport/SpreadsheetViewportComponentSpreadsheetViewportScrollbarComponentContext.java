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

import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextDelegator;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;

import java.util.Objects;

final class SpreadsheetViewportComponentSpreadsheetViewportScrollbarComponentContext implements SpreadsheetViewportScrollbarComponentContext,
    HistoryContextDelegator {

    static SpreadsheetViewportComponentSpreadsheetViewportScrollbarComponentContext with(final SpreadsheetViewportComponent component,
                                                                                         final HistoryContext historyContext) {
        return new SpreadsheetViewportComponentSpreadsheetViewportScrollbarComponentContext(
            Objects.requireNonNull(component, "component"),
            Objects.requireNonNull(historyContext, "historyContext")
        );
    }

    private SpreadsheetViewportComponentSpreadsheetViewportScrollbarComponentContext(final SpreadsheetViewportComponent component,
                                                                                     final HistoryContext historyContext) {
        this.component = component;
        this.historyContext = historyContext;
    }

    @Override
    public SpreadsheetViewportCache spreadsheetViewportCache() {
        return this.component.spreadsheetViewportCache();
    }

    @Override
    public SpreadsheetMetadata spreadsheetMetadata() {
        return this.component.spreadsheetMetadata();
    }

    // SpreadsheetViewportScrollbarComponentContext.....................................................................

    @Override
    public int viewportGridWidth() {
        return this.component.viewportGridWidth;
    }

    @Override
    public int viewportGridHeight() {
        return this.component.viewportGridHeight;
    }

    private final SpreadsheetViewportComponent component;

    // HistoryContextDelegator..........................................................................................

    @Override
    public HistoryContext historyContext() {
        return this.historyContext;
    }

    private final HistoryContext historyContext;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.component + " " + this.historyContext;
    }
}
