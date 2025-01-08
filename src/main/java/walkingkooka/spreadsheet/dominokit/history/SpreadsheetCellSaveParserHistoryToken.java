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
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.tree.json.JsonNode;

import java.util.Map;
import java.util.Optional;

/**
 * This {@link HistoryToken} is used by to paste a {@link SpreadsheetParserSelector} for many cells over another range.
 * <pre>
 * {
 *   "A1": "number-parse-pattern 0.00"
 * }
 *
 * /123/SpreadsheetName456/cell/A1/save/parser/%7B%22A1%22%3A%20%22number-parse-pattern%200.00%22%7D}
 *
 * /spreadsheet-id/spreadsheet-name/cell/cell or cell-range or label/save/parser/{@link SpreadsheetParserSelector} patch as json.
 * </pre>
 */
public final class SpreadsheetCellSaveParserHistoryToken extends SpreadsheetCellSaveMapHistoryToken<Optional<SpreadsheetParserSelector>> {

    static SpreadsheetCellSaveParserHistoryToken with(final SpreadsheetId id,
                                                      final SpreadsheetName name,
                                                      final AnchoredSpreadsheetSelection anchoredSelection,
                                                      final Map<SpreadsheetCellReference, Optional<SpreadsheetParserSelector>> value) {
        return new SpreadsheetCellSaveParserHistoryToken(
            id,
            name,
            anchoredSelection,
            Maps.immutable(value)
        );
    }

    private SpreadsheetCellSaveParserHistoryToken(final SpreadsheetId id,
                                                  final SpreadsheetName name,
                                                  final AnchoredSpreadsheetSelection anchoredSelection,
                                                  final Map<SpreadsheetCellReference, Optional<SpreadsheetParserSelector>> value) {
        super(
            id,
            name,
            anchoredSelection,
            value
        );
    }

    @Override
    Map<SpreadsheetCellReference, Optional<SpreadsheetParserSelector>> parseSaveValue(final TextCursor cursor) {
        return parseMapWithOptionalTypedValues(
            cursor
        );
    }

    @Override //
    SpreadsheetCellSaveParserHistoryToken replace(final SpreadsheetId id,
                                                  final SpreadsheetName name,
                                                  final AnchoredSpreadsheetSelection anchoredSelection,
                                                  final Map<SpreadsheetCellReference, Optional<SpreadsheetParserSelector>> value) {
        return new SpreadsheetCellSaveParserHistoryToken(
            id,
            name,
            anchoredSelection,
            value
        );
    }

    // HasUrlFragment..................................................................................................

    @Override
    UrlFragment urlFragmentSaveEntity() {
        return PARSER;
    }

    @Override
    JsonNode saveValueUrlFragmentValueToJson(final Optional<SpreadsheetParserSelector> value) {
        return MARSHALL_CONTEXT.marshall(
            value.orElse(null)
        );
    }

    // HistoryTokenWatcher..............................................................................................

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.spreadsheetDeltaFetcher()
            .patchCellsParser(
                this.id(),
                this.anchoredSelection().selection(),
                this.value()
            );
        context.pushHistoryToken(previous);
    }
}
