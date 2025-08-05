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

import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.text.cursor.TextCursor;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Base {@link HistoryToken} for several tokens that support saving or patching individual properties for a range of cells,
 * including formulas, style and more. This will be useful when PASTE functionality is added to the UI, so the user
 * can PASTE formulas, or style over a range of selected cells.
 */
public abstract class SpreadsheetCellSaveMapHistoryToken<V> extends SpreadsheetCellSaveHistoryToken<Map<SpreadsheetCellReference, V>> {

    SpreadsheetCellSaveMapHistoryToken(final SpreadsheetId id,
                                       final SpreadsheetName name,
                                       final AnchoredSpreadsheetSelection anchoredSelection,
                                       final Map<SpreadsheetCellReference, V> values) {
        super(
            id,
            name,
            anchoredSelection
        );

        // complain if any of the same formulas are outside the selection range.
        final SpreadsheetSelection selection = anchoredSelection.selection();
        if (false == selection.isLabelName()) {
            final String outside = values.keySet()
                .stream()
                .filter(selection.negate())
                .map(SpreadsheetSelection::toString)
                .collect(Collectors.joining(", "));
            if (false == outside.isEmpty()) {
                throw new IllegalArgumentException("Save value includes cells " + outside + " outside " + selection);
            }
        }

        this.value = values;
    }

    @Override
    public final Map<SpreadsheetCellReference, V> value() {
        return this.value;
    }

    final Map<SpreadsheetCellReference, V> value;

    /**
     * Parses the value which is assumed to hold a {@link Map} in JSON form.
     */
    abstract Map<SpreadsheetCellReference, V> parseSaveValue(final TextCursor cursor);

    /**
     * Factory method used by various would be setters when one or more components have changed and a new instance needs
     * to be created.
     */
    @Override //
    abstract SpreadsheetCellSaveMapHistoryToken<V> replace(final SpreadsheetId id,
                                                           final SpreadsheetName name,
                                                           final AnchoredSpreadsheetSelection anchoredSelection,
                                                           final Map<SpreadsheetCellReference, V> values);
}
