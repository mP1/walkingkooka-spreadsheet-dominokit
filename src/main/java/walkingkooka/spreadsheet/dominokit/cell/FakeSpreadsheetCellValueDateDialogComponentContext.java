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

import walkingkooka.spreadsheet.dominokit.history.FakeHistoryContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public class FakeSpreadsheetCellValueDateDialogComponentContext extends FakeHistoryContext implements SpreadsheetCellValueDateDialogComponentContext {

    public FakeSpreadsheetCellValueDateDialogComponentContext() {
        super();
    }

    @Override
    public Optional<LocalDate> value() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String prepareSaveValue(final Optional<LocalDate> value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LocalDateTime now() {
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
