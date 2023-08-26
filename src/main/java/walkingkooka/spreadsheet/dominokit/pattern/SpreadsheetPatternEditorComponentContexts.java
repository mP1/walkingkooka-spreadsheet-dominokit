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

package walkingkooka.spreadsheet.dominokit.pattern;

import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySelectHistoryToken;
import walkingkooka.spreadsheet.format.pattern.HasSpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

public final class SpreadsheetPatternEditorComponentContexts implements PublicStaticHelper {

    /**
     * {@see SpreadsheetPatternEditorComponentContextBasicCell}
     */
    public static SpreadsheetPatternEditorComponentContext cell(final AppContext context) {
        return SpreadsheetPatternEditorComponentContextBasicCell.with(context);
    }

    /**
     * {@see SpreadsheetPatternEditorComponentContextBasicMetadata}
     */
    public static SpreadsheetPatternEditorComponentContext metadata(final AppContext context) {
        return SpreadsheetPatternEditorComponentContextBasicMetadata.with(context);
    }

    /**
     * Helper that returns true for a {@link HistoryToken} for a {@link SpreadsheetMetadataPropertyName#isPattern()}.
     */
    public static boolean isMetadata(final HistoryToken token) {
        return token instanceof SpreadsheetMetadataPropertySelectHistoryToken &&
                token.cast(HasSpreadsheetPatternKind.class)
                        .patternKind()
                        .isPresent();
    }

    /**
     * Stop creation
     */
    private SpreadsheetPatternEditorComponentContexts() {
        throw new UnsupportedOperationException();
    }
}
