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
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellReferenceToValidatorSelectorMap;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.validation.provider.ValidatorSelector;

import java.util.Map;
import java.util.Optional;

/**
 * This {@link HistoryToken} is used by to paste a {@link ValidatorSelector} for many cells over another range.
 * <pre>
 * {
 *   "A1": "number 0.00"
 * }
 *
 * /123/SpreadsheetName456/cell/A1/save/validator/HelloValidator
 *
 * /spreadsheet-id/spreadsheet-name/cell/cell or cell-range or label/save/validator/{@link ValidatorSelector} patch as json.
 * </pre>
 */
public final class SpreadsheetCellSaveValidatorHistoryToken extends SpreadsheetCellSaveMapHistoryToken<Optional<ValidatorSelector>> {

    static SpreadsheetCellSaveValidatorHistoryToken with(final SpreadsheetId id,
                                                         final SpreadsheetName name,
                                                         final AnchoredSpreadsheetSelection anchoredSelection,
                                                         final Map<SpreadsheetCellReference, Optional<ValidatorSelector>> value) {
        return new SpreadsheetCellSaveValidatorHistoryToken(
            id,
            name,
            anchoredSelection,
            SpreadsheetCellReferenceToValidatorSelectorMap.with(value)
        );
    }

    private SpreadsheetCellSaveValidatorHistoryToken(final SpreadsheetId id,
                                                     final SpreadsheetName name,
                                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                                     final SpreadsheetCellReferenceToValidatorSelectorMap value) {
        super(
            id,
            name,
            anchoredSelection,
            value
        );
    }

    @Override
    SpreadsheetCellReferenceToValidatorSelectorMap parseSaveValue(final TextCursor cursor) {
        return parseJson(
            cursor,
            SpreadsheetCellReferenceToValidatorSelectorMap.class
        );
    }

    @Override //
    SpreadsheetCellSaveValidatorHistoryToken replace(final SpreadsheetId id,
                                                     final SpreadsheetName name,
                                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                                     final Map<SpreadsheetCellReference, Optional<ValidatorSelector>> value) {
        return new SpreadsheetCellSaveValidatorHistoryToken(
            id,
            name,
            anchoredSelection,
            SpreadsheetCellReferenceToValidatorSelectorMap.with(value)
        );
    }

    // HasUrlFragment..................................................................................................

    @Override
    UrlFragment urlFragmentSaveEntity() {
        return VALIDATOR;
    }

    // HistoryTokenWatcher..............................................................................................

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.spreadsheetDeltaFetcher()
            .patchCellsValidator(
                this.id,
                this.anchoredSelection().selection(),
                this.value()
            );
        context.pushHistoryToken(previous);
    }
}
