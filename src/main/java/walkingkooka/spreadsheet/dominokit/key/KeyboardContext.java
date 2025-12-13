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

package walkingkooka.spreadsheet.dominokit.key;

import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.value.SpreadsheetCell;

import java.util.Optional;

/**
 * A {@link walkingkooka.Context} that helps dispatch global keys such as CONTROL-B to bold a cell and more.
 */
public interface KeyboardContext extends HistoryContext,
    LoggingContext {

    /**
     * Returns the {@link SpreadsheetCell} that matches the current {@link HistoryToken}.
     */
    Optional<SpreadsheetCell> historyTokenCell();
}
