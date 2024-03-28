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

package walkingkooka.spreadsheet.dominokit.ui.pattern;

import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

public final class SpreadsheetPatternComponentTableRowProviderContexts implements PublicStaticHelper {

    /**
     * {@see BasicSpreadsheetPatternComponentTableRowProviderContext}
     */
    public static SpreadsheetPatternComponentTableRowProviderContext basic(final SpreadsheetPatternKind kind,
                                                                           final SpreadsheetFormatterContext spreadsheetFormatterContext,
                                                                           final LoggingContext loggingContext) {
        return BasicSpreadsheetPatternComponentTableRowProviderContext.with(
                kind,
                spreadsheetFormatterContext,
                loggingContext
        );
    }

    /**
     * {@see SpreadsheetPatternComponentTableRowProviderContext}
     */
    public static SpreadsheetPatternComponentTableRowProviderContext fake() {
        return new FakeSpreadsheetPatternComponentTableRowProviderContext();
    }

    /**
     * Stop creation
     */
    private SpreadsheetPatternComponentTableRowProviderContexts() {
        throw new UnsupportedOperationException();
    }
}
