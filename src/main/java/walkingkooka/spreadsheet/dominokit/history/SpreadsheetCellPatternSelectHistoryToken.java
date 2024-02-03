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
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;

import java.util.Optional;

public final class SpreadsheetCellPatternSelectHistoryToken extends SpreadsheetCellPatternHistoryToken {

    static SpreadsheetCellPatternSelectHistoryToken with(final SpreadsheetId id,
                                                         final SpreadsheetName name,
                                                         final AnchoredSpreadsheetSelection anchoredSelection,
                                                         final SpreadsheetPatternKind patternKind) {
        return new SpreadsheetCellPatternSelectHistoryToken(
                id,
                name,
                anchoredSelection,
                patternKind
        );
    }

    private SpreadsheetCellPatternSelectHistoryToken(final SpreadsheetId id,
                                                     final SpreadsheetName name,
                                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                                     final SpreadsheetPatternKind patternKind) {
        super(
                id,
                name,
                anchoredSelection,
                Optional.of(patternKind)
        );
    }

    @Override
    UrlFragment cellUrlFragment() {
        return this.patternKind()
                .get()
                .urlFragment();
    }

    @Override
    public HistoryToken clearAction() {
        return this;
    }

    @Override //
    HistoryToken setDifferentAnchoredSelection(final AnchoredSpreadsheetSelection anchoredSelection) {
        return selection(
                this.id(),
                this.name(),
                anchoredSelection
        ).setPatternKind(
                this.patternKind()
        );
    }

    @Override
    public HistoryToken setIdAndName(final SpreadsheetId id,
                                     final SpreadsheetName name) {
        return with(
                id,
                name,
                this.anchoredSelection(),
                this.patternKind()
                        .get()
        );
    }

    @Override
    HistoryToken replacePatternKind(final Optional<SpreadsheetPatternKind> patternKind) {
        return this.patternKind().equals(patternKind) ?
                this :
                this.replacePatternKind0(patternKind);
    }

    private HistoryToken replacePatternKind0(final Optional<SpreadsheetPatternKind> patternKind) {
        final SpreadsheetId id = this.id();
        final SpreadsheetName name = this.name();
        final AnchoredSpreadsheetSelection anchoredSelection = this.anchoredSelection();

        return patternKind.isPresent() ?
                new SpreadsheetCellPatternSelectHistoryToken(
                        id,
                        name,
                        anchoredSelection,
                        patternKind.get()
                ) :
                cell(
                        id,
                        name,
                        anchoredSelection
                );
    }

    @Override
    HistoryToken setSave0(final String pattern) {
        final SpreadsheetPatternKind patternKind = this.patternKind()
                .get();


        return cellPatternSave(
                this.id(),
                this.name(),
                this.anchoredSelection(),
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
        // NOP because SpreadsheetPatternComponent implements ComponentLifecycle
    }
}
