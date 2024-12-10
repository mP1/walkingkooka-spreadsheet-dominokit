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

package walkingkooka.spreadsheet.dominokit.spreadsheet;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.dominokit.AppContexts;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicSpreadsheetNameDialogComponentContextSpreadsheetListRenameTest implements SpreadsheetNameDialogComponentContextTesting<BasicSpreadsheetNameDialogComponentContextSpreadsheetListRename> {

    @Test
    public void testWithNullAppContextFails() {
        assertThrows(
                NullPointerException.class,
                () -> BasicSpreadsheetNameDialogComponentContextSpreadsheetListRename.with(null)
        );
    }

    @Override
    public BasicSpreadsheetNameDialogComponentContextSpreadsheetListRename createContext() {
        return BasicSpreadsheetNameDialogComponentContextSpreadsheetListRename.with(AppContexts.fake());
    }

    @Override
    public String typeNameSuffix() {
        return "SpreadsheetListRename";
    }

    // class............................................................................................................

    @Override
    public Class<BasicSpreadsheetNameDialogComponentContextSpreadsheetListRename> type() {
        return BasicSpreadsheetNameDialogComponentContextSpreadsheetListRename.class;
    }
}
