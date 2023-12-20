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
import walkingkooka.spreadsheet.dominokit.AppContext;

public final class SpreadsheetPatternComponentContexts implements PublicStaticHelper {

    /**
     * {@see SpreadsheetPatternComponentContextBasicCellFormat}
     */
    public static SpreadsheetPatternComponentContext cellFormat(final AppContext context) {
        return SpreadsheetPatternComponentContextBasicCellFormat.with(context);
    }

    /**
     * {@see SpreadsheetPatternComponentContextBasicCellParse}
     */
    public static SpreadsheetPatternComponentContext cellParse(final AppContext context) {
        return SpreadsheetPatternComponentContextBasicCellParse.with(context);
    }

    /**
     * {@see SpreadsheetPatternComponentContextBasicMetadataFormat}
     */
    public static SpreadsheetPatternComponentContext metadataFormat(final AppContext context) {
        return SpreadsheetPatternComponentContextBasicMetadataFormat.with(context);
    }

    /**
     * {@see SpreadsheetPatternComponentContextBasicMetadataParse}
     */
    public static SpreadsheetPatternComponentContext metadataParse(final AppContext context) {
        return SpreadsheetPatternComponentContextBasicMetadataParse.with(context);
    }

    /**
     * Stop creation
     */
    private SpreadsheetPatternComponentContexts() {
        throw new UnsupportedOperationException();
    }
}
