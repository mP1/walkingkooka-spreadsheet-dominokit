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

import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContext;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchers;
import walkingkooka.validation.ValidationValueTypeName;

import java.time.LocalDate;
import java.util.Optional;

/**
 * The {@link SpreadsheetCellValueDialogComponentContext} for the {@link SpreadsheetCellValueDialogComponent}
 */
public interface SpreadsheetCellValueDialogComponentContext<T> extends SpreadsheetDialogComponentContext,
    HasSpreadsheetDeltaFetcherWatchers {

    /**
     * The top level ID.
     */
    String id();

    /**
     * The dialog title.
     */
    String dialogTitle();

    /**
     * The {@link ValidationValueTypeName} for this value being edited by this dialog.
     */
    ValidationValueTypeName valueType();

    /**
     * Gets the current {@link LocalDate} value from the currently selected cell.
     */
    Optional<T> value();

    /**
     * Serializes the given value into a {@link String} which can be passed to {@link walkingkooka.spreadsheet.dominokit.history.HistoryToken#setSaveStringValue(String).
     */
    String toHistoryTokenSaveStringValue(final Optional<T> value);
}
