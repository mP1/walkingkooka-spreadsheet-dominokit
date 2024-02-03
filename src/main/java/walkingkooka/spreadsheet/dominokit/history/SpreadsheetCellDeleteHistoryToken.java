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

public final class SpreadsheetCellDeleteHistoryToken extends SpreadsheetCellHistoryToken {

    static SpreadsheetCellDeleteHistoryToken with(final SpreadsheetId id,
                                                  final SpreadsheetName name,
                                                  final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetCellDeleteHistoryToken(
                id,
                name,
                anchoredSelection
        );
    }

    private SpreadsheetCellDeleteHistoryToken(final SpreadsheetId id,
                                              final SpreadsheetName name,
                                              final AnchoredSpreadsheetSelection anchoredSelection) {
        super(
                id,
                name,
                anchoredSelection
        );
    }

    @Override
    UrlFragment cellUrlFragment() {
        return DELETE;
    }

    @Override
    public HistoryToken clearAction() {
        return this.selectionSelect();
    }

    @Override //
    HistoryToken setDifferentAnchoredSelection(final AnchoredSpreadsheetSelection anchoredSelection) {
        return selection(
                this.id(),
                this.name(),
                anchoredSelection
        ).setDelete();
    }

    @Override
    public HistoryToken setFormatPattern() {
        return this;
    }

    @Override
    public HistoryToken setFormula() {
        return setFormula0();
    }

    @Override
    public HistoryToken setIdAndName(final SpreadsheetId id,
                                     final SpreadsheetName name) {
        return with(
                id,
                name,
                this.anchoredSelection()
        );
    }

    @Override
    public HistoryToken setParsePattern() {
        return this;
    }

    @Override
    HistoryToken replacePatternKind(final Optional<SpreadsheetPatternKind> patternKind) {
        return this;
    }

    @Override
    HistoryToken setSave0(final String value) {
        return this;
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.spreadsheetDeltaFetcher()
                .deleteDelta(
                        this.id(),
                        this.anchoredSelection().selection()
                );
    }
}
