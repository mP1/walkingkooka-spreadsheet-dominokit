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

package walkingkooka.spreadsheet.dominokit.ui.find;

import walkingkooka.spreadsheet.dominokit.history.FakeHistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;

import java.time.LocalDateTime;

public class FakeSpreadsheetFindDialogComponentContext extends FakeHistoryTokenContext implements SpreadsheetFindDialogComponentContext {
    @Override
    public LocalDateTime now() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addSpreadsheetDeltaFetcherWatcherOnce(final SpreadsheetDeltaFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetDeltaFetcher spreadsheetDeltaFetcher() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetMetadata spreadsheetMetadata() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void debug(final Object... values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void error(final Object... values) {
        throw new UnsupportedOperationException();
    }
}
