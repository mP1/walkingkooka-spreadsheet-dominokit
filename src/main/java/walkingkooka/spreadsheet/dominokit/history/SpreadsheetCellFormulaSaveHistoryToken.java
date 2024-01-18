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
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;

import java.util.Objects;
import java.util.Optional;

public final class SpreadsheetCellFormulaSaveHistoryToken extends SpreadsheetCellFormulaHistoryToken {

    static SpreadsheetCellFormulaSaveHistoryToken with(final SpreadsheetId id,
                                                       final SpreadsheetName name,
                                                       final AnchoredSpreadsheetSelection selection,
                                                       final String formula) {
        return new SpreadsheetCellFormulaSaveHistoryToken(
                id,
                name,
                selection,
                formula
        );
    }

    private SpreadsheetCellFormulaSaveHistoryToken(final SpreadsheetId id,
                                                   final SpreadsheetName name,
                                                   final AnchoredSpreadsheetSelection selection,
                                                   final String formula) {
        super(
                id,
                name,
                selection
        );

        this.formula = Objects.requireNonNull(formula, "formula");
    }

    public String formula() {
        return this.formula;
    }

    private final String formula;

    @Override
    UrlFragment formulaUrlFragment() {
        return this.saveUrlFragment(
                this.formula()
        );
    }

    @Override
    public HistoryToken clearAction() {
        return this.setFormula();
    }

    @Override //
    HistoryToken setDifferentSelection(final AnchoredSpreadsheetSelection selection) {
        return selection(
                this.id(),
                this.name(),
                selection
        ).setFormula()
                .setSave(this.formula);
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
                this.selection(),
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

        final AnchoredSpreadsheetSelection selection = this.selection();

        fetcher.patchDelta(
                fetcher.url(
                        this.id(),
                        this.selection()
                                .selection(),
                        Optional.empty() // no extra path
                ).setQuery(
                        SpreadsheetDeltaFetcher.viewportAndWindowQueryString(
                                context.viewport(SpreadsheetViewport.NO_SELECTION),
                                context.viewportCache()
                                        .windows()
                        )
                ),
                SpreadsheetDelta.EMPTY.setCells(
                        Sets.of(
                                selection.selection()
                                        .toCell()
                                        .setFormula(
                                                SpreadsheetFormula.EMPTY.setText(
                                                        this.formula()
                                                )
                                        )
                        )
                )
        );
    }
}
