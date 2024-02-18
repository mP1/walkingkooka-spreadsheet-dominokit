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

import walkingkooka.collect.map.Maps;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.cursor.TextCursors;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This {@link HistoryToken} is used by to paste a formula over a {@link walkingkooka.spreadsheet.reference.SpreadsheetCellRange}.
 */
public final class SpreadsheetCellSaveFormulaHistoryToken extends SpreadsheetCellSaveHistoryToken {

    static SpreadsheetCellSaveFormulaHistoryToken with(final SpreadsheetId id,
                                                       final SpreadsheetName name,
                                                       final AnchoredSpreadsheetSelection anchoredSelection,
                                                       final Map<SpreadsheetCellReference, String> formulas) {
        return new SpreadsheetCellSaveFormulaHistoryToken(
                id,
                name,
                anchoredSelection,
                Maps.immutable(formulas)
        );
    }

    private SpreadsheetCellSaveFormulaHistoryToken(final SpreadsheetId id,
                                                   final SpreadsheetName name,
                                                   final AnchoredSpreadsheetSelection anchoredSelection,
                                                   final Map<SpreadsheetCellReference, String> formulas) {
        super(id, name, anchoredSelection);
        this.formulas = formulas;

        // complain if any of the same formulas are outside the selection range.
        final SpreadsheetSelection selection = anchoredSelection.selection();
        if (false == selection.isLabelName()) {
            final String outside = formulas.keySet()
                    .stream()
                    .filter(selection.negate())
                    .map(SpreadsheetSelection::toString)
                    .collect(Collectors.joining(", "));
            if (false == outside.isEmpty()) {
                throw new IllegalArgumentException("Save formulas includes cells " + outside + " outside " + selection);
            }
        }
    }

    public Map<SpreadsheetCellReference, String> formulas() {
        return this.formulas;
    }

    private final Map<SpreadsheetCellReference, String> formulas;

    @Override
    HistoryToken setDifferentAnchoredSelection(final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetCellSaveFormulaHistoryToken(
                this.id(),
                this.name(),
                anchoredSelection,
                this.formulas
        );
    }

    @Override
    public HistoryToken setIdAndName(final SpreadsheetId id,
                                     final SpreadsheetName name) {
        return new SpreadsheetCellSaveFormulaHistoryToken(
                id,
                name,
                this.anchoredSelection(),
                this.formulas
        );
    }

    @Override
    UrlFragment cellSaveUrlFragment() {
        return FORMULA.append(
                UrlFragment.with(
                        this.formulas.entrySet()
                                .stream()
                                .map(SpreadsheetCellSaveFormulaHistoryToken::cellToFormula)
                                .collect(
                                        Collectors.joining(
                                                UrlFragment.SLASH.value()
                                        )
                                )
                )
        );
    }

    private final static UrlFragment FORMULA = UrlFragment.parse("formula/");

    // /A1/=1+2
    // /B2/=3
    private static String cellToFormula(final Entry<SpreadsheetCellReference, String> cellAndFormula) {
        return cellAndFormula.getKey()
                .toStringMaybeStar() +
                '/' +
                cellAndFormula.getValue();
    }

    @Override
    HistoryToken setSave0(final String value) {
        return new SpreadsheetCellSaveFormulaHistoryToken(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                parseMap(
                        TextCursors.charSequence(value),
                        Function.identity() // note the formula text is not validated for correctness.
                )
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // TODO PATCH cell formula text
    }
}
