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

package walkingkooka.spreadsheet.dominokit.validator;

import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContextDelegator;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellValidatorSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellValidatorSelectHistoryToken;
import walkingkooka.validation.provider.ValidatorSelector;

import java.util.Objects;
import java.util.Optional;

final class AppContextValidatorSelectorDialogComponentContext implements ValidatorSelectorDialogComponentContext,
    DialogComponentContextDelegator {

    static AppContextValidatorSelectorDialogComponentContext with(final AppContext context) {
        return new AppContextValidatorSelectorDialogComponentContext(
            Objects.requireNonNull(context, "context")
        );
    }

    private AppContextValidatorSelectorDialogComponentContext(final AppContext context) {
        this.context = context;
    }

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetCellValidatorSaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetCellValidatorSelectHistoryToken;
    }

    @Override
    public String dialogTitle() {
        return this.selectionValueDialogTitle(ValidatorSelector.class);
    }

    @Override
    public Optional<ValidatorSelector> undo() {
        Optional<ValidatorSelector> selector = Optional.empty();

        final Optional<SpreadsheetCell> maybeCell = this.context.spreadsheetViewportCache()
            .cell(
                this.historyToken()
                    .selection()
                    .get()
            );
        if (maybeCell.isPresent()) {
            selector = maybeCell.get()
                .validatorSelector();
        }

        return selector;
    }

    // DialogComponentContext...........................................................................................

    @Override
    public DialogComponentContext dialogComponentContext() {
        return DialogComponentContexts.basic(
            this.context,
            this.context
        );
    }

    private final AppContext context;
}
