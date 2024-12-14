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

package walkingkooka.spreadsheet.dominokit.format;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.dominokit.AppContexts;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AppContextSpreadsheetFormatterSelectorDialogComponentContextCellTest implements SpreadsheetFormatterSelectorDialogComponentContextTesting<AppContextSpreadsheetFormatterSelectorDialogComponentContextCell> {

    @Test
    public void testWithNullAppContextFails() {
        assertThrows(
                NullPointerException.class,
                () -> AppContextSpreadsheetFormatterSelectorDialogComponentContextCell.with(null)
        );
    }

    @Override
    public AppContextSpreadsheetFormatterSelectorDialogComponentContextCell createContext() {
        return AppContextSpreadsheetFormatterSelectorDialogComponentContextCell.with(AppContexts.fake());
    }

    @Override
    public String typeNameSuffix() {
        return "Cell";
    }

    // class............................................................................................................

    @Override
    public Class<AppContextSpreadsheetFormatterSelectorDialogComponentContextCell> type() {
        return AppContextSpreadsheetFormatterSelectorDialogComponentContextCell.class;
    }
}