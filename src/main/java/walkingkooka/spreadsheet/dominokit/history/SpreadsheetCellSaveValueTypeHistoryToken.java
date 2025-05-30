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
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.validation.ValidationValueTypeName;

import java.util.Map;
import java.util.Optional;

/**
 * This {@link HistoryToken} is used by to paste a {@link ValidationValueTypeName} for many cells over another range.
 * <pre>
 * /123/SpreadsheetName456/cell/A1/save/valueType/XYZ
 *
 * /spreadsheet-id/spreadsheet-name/cell/cell or cell-range or label/save/valueType/{@link ValidationValueTypeName} for each selected cell.
 * </pre>
 */
public final class SpreadsheetCellSaveValueTypeHistoryToken extends SpreadsheetCellSaveMapHistoryToken<Optional<ValidationValueTypeName>> {

    static SpreadsheetCellSaveValueTypeHistoryToken with(final SpreadsheetId id,
                                                         final SpreadsheetName name,
                                                         final AnchoredSpreadsheetSelection anchoredSelection,
                                                         final Map<SpreadsheetCellReference, Optional<ValidationValueTypeName>> value) {
        return new SpreadsheetCellSaveValueTypeHistoryToken(
            id,
            name,
            anchoredSelection,
            Maps.immutable(value)
        );
    }

    private SpreadsheetCellSaveValueTypeHistoryToken(final SpreadsheetId id,
                                                     final SpreadsheetName name,
                                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                                     final Map<SpreadsheetCellReference, Optional<ValidationValueTypeName>> value) {
        super(
            id,
            name,
            anchoredSelection,
            value
        );
    }

    @Override //
    SpreadsheetCellSaveValueTypeHistoryToken replace(final SpreadsheetId id,
                                                     final SpreadsheetName name,
                                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                                     final Map<SpreadsheetCellReference, Optional<ValidationValueTypeName>> value) {
        return new SpreadsheetCellSaveValueTypeHistoryToken(
            id,
            name,
            anchoredSelection,
            value
        );
    }

    @Override
    Map<SpreadsheetCellReference, Optional<ValidationValueTypeName>> parseSaveValue(final TextCursor cursor) {
        return parseMapWithOptionalValues(
            cursor,
            ValidationValueTypeName.class
        );
    }

    // HasUrlFragment..................................................................................................

    @Override
    UrlFragment urlFragmentSaveEntity() {
        return VALUE_TYPE;
    }

    @Override
    JsonNode saveValueUrlFragmentValueToJson(final Optional<ValidationValueTypeName> value) {
        return MARSHALL_CONTEXT.marshallOptional(value);
    }

    // HistoryTokenWatcher..............................................................................................

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.spreadsheetDeltaFetcher()
            .patchCellsValueType(
                this.id(),
                this.anchoredSelection().selection(),
                this.value()
            );
        context.pushHistoryToken(previous);
    }
}
