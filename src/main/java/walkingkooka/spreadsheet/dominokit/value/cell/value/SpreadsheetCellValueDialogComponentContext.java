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

package walkingkooka.spreadsheet.dominokit.value.cell.value;

import walkingkooka.Cast;
import walkingkooka.spreadsheet.dominokit.dialog.DialogAnchorListComponentContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContext;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.validation.ValueType;

import java.util.Optional;

/**
 * The {@link SpreadsheetCellValueDialogComponentContext} for the {@link SpreadsheetCellValueDialogComponent}
 */
public interface SpreadsheetCellValueDialogComponentContext<T> extends DialogComponentContext,
    DialogAnchorListComponentContext<T>,
    HasSpreadsheetDeltaFetcherWatchers {

    /**
     * The top level ID.
     */
    String id();

    /**
     * The {@link ValueType} for this value being edited by this dialog.
     */
    ValueType valueType();

    /**
     * Get the current cell if available, which will be useful to get the value and errors.
     */
    Optional<SpreadsheetCell> cell();

    @Override
    default Optional<T> undo() {
        return this.cell()
            .flatMap((SpreadsheetCell cell) -> Cast.to(
                cell.formula()
                    .value())
            );
    }
}
