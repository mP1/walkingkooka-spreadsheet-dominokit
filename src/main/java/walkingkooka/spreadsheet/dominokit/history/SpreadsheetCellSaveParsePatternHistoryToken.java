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
import walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.tree.json.JsonNode;

import java.util.Map;
import java.util.Optional;

/**
 * This {@link HistoryToken} is used by to paste a {@link SpreadsheetParsePattern} for many cells over another range.
 * <pre>
 * /123/SpreadsheetName456/cell/A1/save/parse-pattern/{%20"A1":%20{%20"type":%20"spreadsheet-number-parse-pattern",%20"value":%20"0.00"%20}%20}
 *
 * /spreadsheet-id/spreadsheet-name/cell/cell or cell-range or label/save/parse-pattern/{@link SpreadsheetParsePattern} patch as json.
 * </pre>
 */
public final class SpreadsheetCellSaveParsePatternHistoryToken extends SpreadsheetCellSaveMapHistoryToken<Optional<SpreadsheetParsePattern>> {

    static SpreadsheetCellSaveParsePatternHistoryToken with(final SpreadsheetId id,
                                                            final SpreadsheetName name,
                                                            final AnchoredSpreadsheetSelection anchoredSelection,
                                                            final Map<SpreadsheetCellReference, Optional<SpreadsheetParsePattern>> value) {
        return new SpreadsheetCellSaveParsePatternHistoryToken(
                id,
                name,
                anchoredSelection,
                Maps.immutable(value)
        );
    }

    private SpreadsheetCellSaveParsePatternHistoryToken(final SpreadsheetId id,
                                                        final SpreadsheetName name,
                                                        final AnchoredSpreadsheetSelection anchoredSelection,
                                                        final Map<SpreadsheetCellReference, Optional<SpreadsheetParsePattern>> value) {
        super(
                id,
                name,
                anchoredSelection,
                value
        );
    }

    @Override
    Map<SpreadsheetCellReference, Optional<SpreadsheetParsePattern>> parseSaveValue(final TextCursor cursor) {
        return parseMapWithOptionalTypedValues(
                cursor
        );
    }

    @Override //
    SpreadsheetCellSaveParsePatternHistoryToken replace(final SpreadsheetId id,
                                                        final SpreadsheetName name,
                                                        final AnchoredSpreadsheetSelection anchoredSelection,
                                                        final Map<SpreadsheetCellReference, Optional<SpreadsheetParsePattern>> value) {
        return new SpreadsheetCellSaveParsePatternHistoryToken(
                id,
                name,
                anchoredSelection,
                value
        );
    }

    // HasUrlFragment..................................................................................................

    @Override
    UrlFragment urlFragmentSaveEntity() {
        return PARSE_PATTERN;
    }

    private final static UrlFragment PARSE_PATTERN = UrlFragment.parse("parse-pattern");

    @Override
    JsonNode saveValueUrlFragmentValueToJson(final Optional<SpreadsheetParsePattern> value) {
        return MARSHALL_CONTEXT.marshallWithType(
                value.orElse(null)
        );
    }

    // HistoryTokenWatcher..............................................................................................

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.spreadsheetDeltaFetcher()
                .patchCellsParsePattern(
                        this.id(),
                        this.anchoredSelection().selection(),
                        this.value()
                );
        context.pushHistoryToken(previous);
    }
}
