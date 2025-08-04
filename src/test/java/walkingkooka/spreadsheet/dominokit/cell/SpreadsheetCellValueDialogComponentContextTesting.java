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

import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContextTesting;

import java.util.Optional;

public interface SpreadsheetCellValueDialogComponentContextTesting<V, C extends SpreadsheetCellValueDialogComponentContext<V>> extends DialogComponentContextTesting<C> {

    default void toHistoryTokenSaveStringValueAndCheck(final Optional<V> value,
                                                       final String expected) {
        this.checkEquals(
            expected,
            this.createContext()
                .toHistoryTokenSaveStringValue(value)
        );
    }

    @Override
    default String typeNameSuffix() {
        return SpreadsheetCellValueDialogComponentContext.class.getSimpleName();
    }
}
