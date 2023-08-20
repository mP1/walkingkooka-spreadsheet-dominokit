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

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySelectHistoryToken;
import walkingkooka.spreadsheet.format.pattern.HasSpreadsheetPatternKind;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

import java.util.Objects;

/**
 * A {@link SpreadsheetPatternEditorComponentContext} for editing patterns for the {@link walkingkooka.spreadsheet.meta.SpreadsheetMetadata}.
 */
final class SpreadsheetPatternEditorComponentContextBasicMetadata extends SpreadsheetPatternEditorComponentContextBasic {

    static SpreadsheetPatternEditorComponentContextBasicMetadata with(final AppContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetPatternEditorComponentContextBasicMetadata(context);
    }

    private SpreadsheetPatternEditorComponentContextBasicMetadata(final AppContext context) {
        super(context);
    }

    // ComponentLifecycleMatcher........................................................................................

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetMetadataPropertySaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetMetadataPropertySelectHistoryToken &&
                token.cast(HasSpreadsheetPatternKind.class)
                        .patternKind()
                        .isPresent();
    }

    // SpreadsheetPatternEditorComponentContext.........................................................................

    /**
     * Returns the pattern text for the selected {@link SpreadsheetPatternKind} from the current {@link walkingkooka.spreadsheet.meta.SpreadsheetMetadata}
     */
    @Override
    public String loaded() {
        return this.context.spreadsheetMetadata()
                .getIgnoringDefaults(
                        this.historyToken()
                                .cast(HasSpreadsheetPatternKind.class)
                                .patternKind()
                                .get()
                                .spreadsheetMetadataPropertyName()
                )
                .map(SpreadsheetPattern::text)
                .orElse("");
    }
}
