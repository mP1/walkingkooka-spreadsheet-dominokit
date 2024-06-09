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
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.tree.json.JsonNode;

import java.util.Map;
import java.util.Optional;

/**
 * This {@link HistoryToken} is used by to paste a {@link SpreadsheetFormatterSelector} for many cells over another range.
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
                Maps.immutable(value)
        );
    }

    private SpreadsheetCellSaveFormatterHistoryToken(final SpreadsheetId id,
                                                     final SpreadsheetName name,
                                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                                     final Map<SpreadsheetCellReference, Optional<SpreadsheetFormatterSelector>> value) {
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
                value
        );
    }

    @Override
    Map<SpreadsheetCellReference, Optional<SpreadsheetFormatterSelector>> parseSaveValue(final TextCursor cursor) {
        return parseMapWithOptionalValues(
                cursor,
                SpreadsheetFormatterSelector.class
        );
    }

    // HasUrlFragment..................................................................................................

    @Override
    UrlFragment urlFragmentSaveEntity() {
        return FORMATTER;
    }

    @Override
    JsonNode saveValueUrlFragmentValueToJson(final Optional<SpreadsheetFormatterSelector> value) {
        return MARSHALL_CONTEXT.marshall(
                value.orElse(null)
        );
    }

    // HistoryTokenWatcher..............................................................................................

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.spreadsheetDeltaFetcher()
                .patchCellsFormatter(
                        this.id(),
                        this.anchoredSelection().selection(),
                        this.value()
                );
        context.pushHistoryToken(previous);
    }
}
