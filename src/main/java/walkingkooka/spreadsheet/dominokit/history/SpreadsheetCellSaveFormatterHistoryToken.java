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
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellReferenceToSpreadsheetFormatterSelectorMap;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.text.cursor.TextCursor;

import java.util.Map;
import java.util.Optional;

/**
 * This {@link HistoryToken} is used by to paste a {@link SpreadsheetFormatterSelector} for many cells over another range.
 * <pre>
 * /123/SpreadsheetName456/cell/A1/save/formatter/%7B%0A%20%20%20%22A1%22%3A%20%22text%20%40%22%0A%7D
 *
 * /spreadsheet-id/spreadsheet-name/cell/cell or cell-range or label/save/formatter/{@link SpreadsheetFormatterSelector} for each selected cell.
 * </pre>
 */
public final class SpreadsheetCellSaveFormatterHistoryToken extends SpreadsheetCellSaveMapHistoryToken<Optional<SpreadsheetFormatterSelector>> {

    static SpreadsheetCellSaveFormatterHistoryToken with(final SpreadsheetId id,
                                                         final SpreadsheetName name,
                                                         final AnchoredSpreadsheetSelection anchoredSelection,
                                                         final Map<SpreadsheetCellReference, Optional<SpreadsheetFormatterSelector>> value) {
        return new SpreadsheetCellSaveFormatterHistoryToken(
            id,
            name,
            anchoredSelection,
            SpreadsheetCellReferenceToSpreadsheetFormatterSelectorMap.with(value)
        );
    }

    private SpreadsheetCellSaveFormatterHistoryToken(final SpreadsheetId id,
                                                     final SpreadsheetName name,
                                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                                     final SpreadsheetCellReferenceToSpreadsheetFormatterSelectorMap value) {
        super(
            id,
            name,
            anchoredSelection,
            value
        );
    }

    @Override //
    SpreadsheetCellSaveFormatterHistoryToken replace(final SpreadsheetId id,
                                                     final SpreadsheetName name,
                                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                                     final Map<SpreadsheetCellReference, Optional<SpreadsheetFormatterSelector>> value) {
        return new SpreadsheetCellSaveFormatterHistoryToken(
            id,
            name,
            anchoredSelection,
            SpreadsheetCellReferenceToSpreadsheetFormatterSelectorMap.with(value)
        );
    }

    @Override
    SpreadsheetCellReferenceToSpreadsheetFormatterSelectorMap parseSaveValue(final TextCursor cursor) {
        return parseJson(
            cursor,
            SpreadsheetCellReferenceToSpreadsheetFormatterSelectorMap.class
        );
    }

    // HasUrlFragment..................................................................................................

    @Override
    UrlFragment urlFragmentSaveEntity() {
        return FORMATTER;
    }

    // HistoryTokenWatcher..............................................................................................

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.spreadsheetDeltaFetcher()
            .patchCellsFormatter(
                this.id,
                this.anchoredSelection().selection(),
                this.value()
            );
        context.pushHistoryToken(previous);
    }
}
