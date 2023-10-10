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

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;

import java.util.Optional;

/**
 * This {@link HistoryToken} is used to represent the selection of the toolbar pattern icon.
 */
public final class SpreadsheetCellPatternToolbarHistoryToken extends SpreadsheetCellPatternHistoryToken {

    static SpreadsheetCellPatternToolbarHistoryToken with(final SpreadsheetId id,
                                                          final SpreadsheetName name,
                                                          final SpreadsheetViewport viewport) {
        return new SpreadsheetCellPatternToolbarHistoryToken(
                id,
                name,
                viewport
        );
    }

    private SpreadsheetCellPatternToolbarHistoryToken(final SpreadsheetId id,
                                                      final SpreadsheetName name,
                                                      final SpreadsheetViewport viewport) {
        super(
                id,
                name,
                viewport,
                Optional.empty()
        );
    }

    @Override
    UrlFragment patternUrlFragment() {
        return UrlFragment.EMPTY;
    }

    @Override
    public HistoryToken setIdAndName(final SpreadsheetId id,
                                     final SpreadsheetName name) {
        return with(
                id,
                name,
                this.viewport()
        );
    }

    @Override
    HistoryToken setPatternKind0(final Optional<SpreadsheetPatternKind> patternKind) {
        return this.patternKind().equals(patternKind) ?
                this :
                this.replacePatternKind(patternKind);
    }

    private HistoryToken replacePatternKind(final Optional<SpreadsheetPatternKind> patternKind) {
        final SpreadsheetId id = this.id();
        final SpreadsheetName name = this.name();
        final SpreadsheetViewport viewport = this.viewport();

        return patternKind.isPresent() ?
                cellPattern(
                        id,
                        name,
                        viewport,
                        patternKind.get()
                ) :
                cell(
                        id,
                        name,
                        viewport
                );
    }

    @Override
    HistoryToken setSave0(final String pattern) {
        final SpreadsheetPatternKind patternKind = this.patternKind()
                .get();


        return cellPatternSave(
                this.id(),
                this.name(),
                this.viewport(),
                patternKind,
                Optional.ofNullable(
                        pattern.isEmpty() ?
                                null :
                                patternKind.parse(pattern)
                )
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // give focus to viewport icon
    }
}
