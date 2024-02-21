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
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursors;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * This {@link HistoryToken} is used by to paste a cell over a {@link walkingkooka.spreadsheet.reference.SpreadsheetCellRange}.
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
                    .map(c -> c.reference())
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

    @Override //
    final HistoryToken setSave0(final String value) {
        final TextCursor cursor = TextCursors.charSequence(value);

        return this.replace(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                parseCells(
                        cursor
                )
        );
    }

    // HasUrlFragment...................................................................................................


    @Override
    UrlFragment saveEntityUrlFragment() {
        return CELL;
    }

    private final static UrlFragment CELL = UrlFragment.parse("cell");

    @Override
    UrlFragment saveValueUrlFragment() {
        return UrlFragment.with(
                MARSHALL_CONTEXT.marshallCollection(this.value)
                        .toString()
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // TODO PATCH cell text
    }
}
