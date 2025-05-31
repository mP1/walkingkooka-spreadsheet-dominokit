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

import walkingkooka.Value;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.validation.provider.ValidatorSelector;

import java.util.Optional;

public final class SpreadsheetCellValidatorSaveHistoryToken extends SpreadsheetCellValidatorHistoryToken implements Value<Optional<ValidatorSelector>> {

    static SpreadsheetCellValidatorSaveHistoryToken with(final SpreadsheetId id,
                                                         final SpreadsheetName name,
                                                         final AnchoredSpreadsheetSelection anchoredSelection,
                                                         final Optional<ValidatorSelector> validator) {
        return new SpreadsheetCellValidatorSaveHistoryToken(
            id,
            name,
            anchoredSelection,
            validator
        );
    }

    private SpreadsheetCellValidatorSaveHistoryToken(final SpreadsheetId id,
                                                     final SpreadsheetName name,
                                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                                     final Optional<ValidatorSelector> validator) {
        super(
            id,
            name,
            anchoredSelection,
            validator
        );
    }

    @Override
    public Optional<ValidatorSelector> value() {
        return this.validator;
    }

    @Override
    public HistoryToken clearAction() {
        return HistoryToken.cellValidatorSelect(
            this.id(),
            this.name(),
            this.anchoredSelection()
        );
    }

    @Override
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetCellValidatorSaveHistoryToken(
            id,
            name,
            anchoredSelection,
            this.validator
        );
    }

    // cell/A1/validator/save/ValidatorSelector
    @Override
    UrlFragment validatorUrlFragment() {
        return saveUrlFragment(this.value());
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.pushHistoryToken(previous);

        context.spreadsheetDeltaFetcher()
            .patchValidator(
                this.id(),
                this.anchoredSelection().selection(),
                this.validator
            );
    }
}
