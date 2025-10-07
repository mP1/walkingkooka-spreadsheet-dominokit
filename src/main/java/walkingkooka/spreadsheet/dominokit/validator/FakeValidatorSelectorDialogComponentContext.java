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

import walkingkooka.spreadsheet.dominokit.dialog.FakeDialogComponentContext;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.validation.provider.ValidatorSelector;

import java.util.Optional;

public class FakeValidatorSelectorDialogComponentContext extends FakeDialogComponentContext implements ValidatorSelectorDialogComponentContext {

    public FakeValidatorSelectorDialogComponentContext() {
        super();
    }

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String dialogTitle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<ValidatorSelector> undo() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }
}
