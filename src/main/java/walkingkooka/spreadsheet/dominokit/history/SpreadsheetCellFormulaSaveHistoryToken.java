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

import walkingkooka.collect.set.Sets;
import walkingkooka.net.UrlFragment;
import walkingkooka.net.UrlQueryString;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

import java.util.Objects;
import java.util.Optional;

public final class SpreadsheetCellFormulaSaveHistoryToken extends SpreadsheetCellFormulaHistoryToken {

    static SpreadsheetCellFormulaSaveHistoryToken with(final SpreadsheetId id,
                                                       final SpreadsheetName name,
                                                       final SpreadsheetViewportSelection viewportSelection,
                                                       final SpreadsheetFormula formula) {
        return new SpreadsheetCellFormulaSaveHistoryToken(
                id,
                name,
                viewportSelection,
                formula
        );
    }

    private SpreadsheetCellFormulaSaveHistoryToken(final SpreadsheetId id,
                                                   final SpreadsheetName name,
                                                   final SpreadsheetViewportSelection viewportSelection,
                                                   final SpreadsheetFormula formula) {
        super(
                id,
                name,
                viewportSelection
        );

        this.formula = Objects.requireNonNull(formula, "formula");
    }

    public SpreadsheetFormula formula() {
        return this.formula;
    }

    private final SpreadsheetFormula formula;

    @Override
    UrlFragment formulaUrlFragment() {
        return this.saveUrlFragment(
                this.formula()
        );
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
                this.viewportSelection(),
                this.formula()
        );
    }

    @Override
    HistoryToken setSave0(final String value) {
        return this;
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // remove the save
        context.pushHistoryToken(
                this.setFormula()
        );

        // PATCH cell with new formula
        final SpreadsheetDeltaFetcher fetcher = context.spreadsheetDeltaFetcher();

        final SpreadsheetViewportSelection viewportSelection = this.viewportSelection();

        fetcher.patch(
                fetcher.url(
                        this.id(),
                        this.viewportSelection()
                                .selection(),
                        Optional.empty() // no extra path
                ).setQuery(
                        SpreadsheetDeltaFetcher.appendViewportSelectionAndWindow(
                                viewportSelection,
                                context.viewportCache()
                                        .windows(),
                                UrlQueryString.EMPTY
                        )
                ),
                fetcher.toJson(
                        SpreadsheetDelta.EMPTY.setCells(
                                Sets.of(
                                        viewportSelection.selection()
                                                .toCell()
                                                .setFormula(this.formula())
                                )
                        )
                )
        );
    }
}
