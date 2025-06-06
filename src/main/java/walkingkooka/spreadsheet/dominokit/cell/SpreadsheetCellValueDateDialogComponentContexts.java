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

package walkingkooka.spreadsheet.dominokit.cell;

import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;

public final class SpreadsheetCellValueDateDialogComponentContexts implements PublicStaticHelper {

    /**
     * {@see BasicSpreadsheetCellValueDateDialogComponentContext}
     */
    public static SpreadsheetCellValueDateDialogComponentContext basic(final SpreadsheetViewportCache viewportCache,
                                                                       final JsonNodeMarshallContext marshallContext,
                                                                       final HistoryContext historyContext,
                                                                       final LoggingContext loggingContext) {
        return BasicSpreadsheetCellValueDateDialogComponentContext.with(
            viewportCache,
            marshallContext,
            historyContext,
            loggingContext
        );
    }

    /**
     * {@see FakeSpreadsheetCellValueDateDialogComponentContext}
     */
    public static SpreadsheetCellValueDateDialogComponentContext fake() {
        return new FakeSpreadsheetCellValueDateDialogComponentContext();
    }

    /**
     * Stop creation
     */
    private SpreadsheetCellValueDateDialogComponentContexts() {
        throw new UnsupportedOperationException();
    }
}
