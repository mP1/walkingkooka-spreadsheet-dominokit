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

package walkingkooka.spreadsheet.dominokit.cell;

import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.FakeHistoryContext;
import walkingkooka.validation.ValidationValueTypeName;

import java.util.Optional;

public class FakeSpreadsheetCellValueDialogComponentContext<T> extends FakeHistoryContext implements SpreadsheetCellValueDialogComponentContext<T> {

    public FakeSpreadsheetCellValueDialogComponentContext() {
        super();
    }

    @Override
    public String id() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String dialogTitle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isMatch(final ValidationValueTypeName valueType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<T> value() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toHistoryTokenSaveStringValue(final Optional<T> value) {
        throw new UnsupportedOperationException();
    }

    // SpreadsheetDeltaFetcherWatchers..................................................................................

    @Override
    public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addSpreadsheetDeltaFetcherWatcherOnce(final SpreadsheetDeltaFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    // LoggingContext...................................................................................................

    @Override
    public void debug(final Object... values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void info(final Object... values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void warn(final Object... values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void error(final Object... values) {
        throw new UnsupportedOperationException();
    }
}
