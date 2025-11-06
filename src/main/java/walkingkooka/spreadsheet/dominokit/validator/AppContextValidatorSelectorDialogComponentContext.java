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
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
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
    public String dialogTitle() {
        return this.selectionValueDialogTitle(ValidatorSelector.class);
    }

    @Override
    public Optional<ValidatorSelector> undo() {
        return this.context.spreadsheetViewportCache()
            .historyTokenCell()
            .flatMap(SpreadsheetCell::validator);
    }

    @Override
    public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.context.addSpreadsheetDeltaFetcherWatcher(watcher);
    }

    @Override
    public SpreadsheetViewportCache spreadsheetViewportCache() {
        return this.context.spreadsheetViewportCache();
    }

    // ValidatorSelectorNameAnchorListComponentContext..................................................................

    @Override
    public SpreadsheetMetadata spreadsheetMetadata() {
        return this.context.spreadsheetMetadata();
    }

    // DialogComponentContext...........................................................................................

    @Override
    public DialogComponentContext dialogComponentContext() {
        return DialogComponentContexts.basic(this.context);
    }

    private final AppContext context;
}
