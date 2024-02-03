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

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySaveHistoryToken;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

/**
 * A {@link SpreadsheetPatternComponentContext} for editing patterns for the {@link walkingkooka.spreadsheet.meta.SpreadsheetMetadata}.
 */
abstract class SpreadsheetPatternComponentContextBasicMetadata extends SpreadsheetPatternComponentContextBasic {

    SpreadsheetPatternComponentContextBasicMetadata(final AppContext context) {
        super(context);
    }

    // ComponentLifecycleMatcher........................................................................................

    @Override
    public final boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetMetadataPropertySaveHistoryToken;
    }

    // SpreadsheetPatternComponentContext.........................................................................

    /**
     * Returns the pattern text for the selected {@link SpreadsheetPatternKind} from the current {@link walkingkooka.spreadsheet.meta.SpreadsheetMetadata}
     */
    @Override
    public final String loaded() {
        return this.context.spreadsheetMetadata()
                .getIgnoringDefaults(
                        this.historyToken()
                                .patternKind()
                                .get()
                                .spreadsheetMetadataPropertyName()
                )
                .map(SpreadsheetPattern::text)
                .orElse("");
    }
}
