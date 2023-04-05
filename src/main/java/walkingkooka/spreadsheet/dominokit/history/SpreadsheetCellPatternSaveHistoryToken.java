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

package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.HasUrlFragment;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

import java.util.Optional;

public final class SpreadsheetCellPatternSaveHistoryToken extends SpreadsheetCellPatternHistoryToken {

    static SpreadsheetCellPatternSaveHistoryToken with(final SpreadsheetId id,
                                                       final SpreadsheetName name,
                                                       final SpreadsheetViewportSelection viewportSelection,
                                                       final SpreadsheetPatternKind patternKind,
                                                       final Optional<SpreadsheetPattern> pattern) {
        return new SpreadsheetCellPatternSaveHistoryToken(
                id,
                name,
                viewportSelection,
                patternKind,
                pattern
        );
    }

    private SpreadsheetCellPatternSaveHistoryToken(final SpreadsheetId id,
                                                   final SpreadsheetName name,
                                                   final SpreadsheetViewportSelection viewportSelection,
                                                   final SpreadsheetPatternKind patternKind,
                                                   final Optional<SpreadsheetPattern> pattern) {
        super(
                id,
                name,
                viewportSelection,
                patternKind
        );

        if (pattern.isPresent()) {
            final SpreadsheetPattern p = pattern.get();
            final SpreadsheetPatternKind kind = p.kind();
            if (patternKind != kind) {
                throw new IllegalArgumentException("Pattern " + p + " is not a " + kind + ".");
            }
        }

        this.pattern = pattern;
    }

    public Optional<SpreadsheetPattern> pattern() {
        return this.pattern;
    }

    private final Optional<SpreadsheetPattern> pattern;

    @Override
    UrlFragment patternUrlFragment() {
        return SAVE.append(
                this.pattern()
                        .map(HasUrlFragment::urlFragment)
                        .orElse(UrlFragment.EMPTY)
        );
    }

    @Override
    SpreadsheetHistoryToken setDifferentIdOrName(final SpreadsheetId id,
                                                 final SpreadsheetName name) {
        return new SpreadsheetCellPatternSaveHistoryToken(
                id,
                name,
                this.viewportSelection(),
                this.patternKind(),
                this.pattern()
        );
    }

    @Override
    SpreadsheetNameHistoryToken pattern(final SpreadsheetPatternKind patternKind) {
        return this;
    }

    @Override
    SpreadsheetNameHistoryToken save(final String value) {
        return this;
    }

    @Override
    void onHashChange0(final HistoryToken previous,
                       final AppContext context) {
        // PATCH metadata with new pattern
    }
}
