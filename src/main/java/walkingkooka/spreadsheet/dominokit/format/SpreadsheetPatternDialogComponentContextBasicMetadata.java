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

package walkingkooka.spreadsheet.dominokit.format;

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySaveHistoryToken;

/**
 * A {@link SpreadsheetPatternDialogComponentContext} for editing patterns for the {@link walkingkooka.spreadsheet.meta.SpreadsheetMetadata}.
 */
abstract class SpreadsheetPatternDialogComponentContextBasicMetadata extends SpreadsheetPatternDialogComponentContextBasic {

    SpreadsheetPatternDialogComponentContextBasicMetadata(final AppContext context) {
        super(context);
    }

    // ComponentLifecycleMatcher........................................................................................

    @Override
    public final boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetMetadataPropertySaveHistoryToken;
    }

    // SpreadsheetPatternDialogComponentContext.........................................................................

    /**
     * Retrieves the {@link walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind} from the history token,
     * and then reads the value for its {@link walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName}.
     */
    @Override
    public final String undo() {
        return this.context.spreadsheetMetadata()
                .getIgnoringDefaults(
                        this.historyToken()
                                .patternKind()
                                .get()
                                .spreadsheetMetadataPropertyName()
                ).map(Object::toString)
                .orElse("");
    }
}
