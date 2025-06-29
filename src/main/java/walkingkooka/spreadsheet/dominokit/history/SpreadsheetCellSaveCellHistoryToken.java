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
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * This {@link HistoryToken} is used by paste over selected cell saving the given contents.
 * <pre>
 * /123/SpreadsheetName456/cell/A1/save/cell/[{"A1":{"formula":{"text":""}}}]
 *
 * /spreadsheet-id/spreadsheet-name/cell/cell or cell-range or label/save/cell/new-content-in-json-form
 * </pre>
 */
public final class SpreadsheetCellSaveCellHistoryToken extends SpreadsheetCellSaveHistoryToken<Set<SpreadsheetCell>> {

    static SpreadsheetCellSaveCellHistoryToken with(final SpreadsheetId id,
                                                    final SpreadsheetName name,
                                                    final AnchoredSpreadsheetSelection anchoredSelection,
                                                    final Set<SpreadsheetCell> value) {
        return new SpreadsheetCellSaveCellHistoryToken(
            id,
            name,
            anchoredSelection,
            Sets.immutable(value)
        );
    }

    private SpreadsheetCellSaveCellHistoryToken(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection,
                                                final Set<SpreadsheetCell> value) {
        super(
            id,
            name,
            anchoredSelection
        );

        // complain if any of the same formulas are outside the selection range.
        final SpreadsheetSelection selection = anchoredSelection.selection();
        if (false == selection.isLabelName()) {
            final String outside = value.stream()
                .map(SpreadsheetCell::reference)
                .filter(selection.negate())
                .map(SpreadsheetSelection::toString)
                .collect(Collectors.joining(", "));
            if (false == outside.isEmpty()) {
                throw new IllegalArgumentException("Save value includes cells " + outside + " outside " + selection);
            }
        }

        this.value = value;
    }

    @Override
    public Set<SpreadsheetCell> value() {
        return this.value;
    }

    final Set<SpreadsheetCell> value;

    @Override
    SpreadsheetCellSaveCellHistoryToken replace(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection,
                                                final Set<SpreadsheetCell> value) {
        return new SpreadsheetCellSaveCellHistoryToken(
            id,
            name,
            anchoredSelection,
            value
        );
    }

    // HasUrlFragment...................................................................................................


    @Override
    UrlFragment urlFragmentSaveEntity() {
        return CELL;
    }

    @Override
    UrlFragment urlFragmentSaveValue() {
        return UrlFragment.with(
            MARSHALL_CONTEXT.marshallCollection(this.value)
                .toString()
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.spreadsheetDeltaFetcher()
            .postCells(
                this.id(),
                this.anchoredSelection().selection(),
                this.value()
            );
        context.pushHistoryToken(previous);
    }
}
