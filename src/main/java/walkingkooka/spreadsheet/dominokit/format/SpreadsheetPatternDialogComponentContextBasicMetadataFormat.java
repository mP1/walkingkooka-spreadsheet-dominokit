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
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

import java.util.Objects;

/**
 * A {@link SpreadsheetPatternDialogComponentContext} for editing patterns for the {@link walkingkooka.spreadsheet.meta.SpreadsheetMetadata}.
 */
final class SpreadsheetPatternDialogComponentContextBasicMetadataFormat extends SpreadsheetPatternDialogComponentContextBasicMetadata {

    static SpreadsheetPatternDialogComponentContextBasicMetadataFormat with(final AppContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetPatternDialogComponentContextBasicMetadataFormat(context);
    }

    private SpreadsheetPatternDialogComponentContextBasicMetadataFormat(final AppContext context) {
        super(context);
    }

    @Override
    public SpreadsheetPatternKind[] filteredPatternKinds() {
        return SpreadsheetPatternKind.formatValues();
    }

    // ComponentLifecycleMatcher........................................................................................

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token.isMetadataFormatter();
    }
}
