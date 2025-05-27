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

package walkingkooka.spreadsheet.dominokit.comparator;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.dominokit.AppContexts;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetComparatorNameListDialogComponentContextSortComparatorsTest implements SpreadsheetComparatorNameListDialogComponentContextTesting<SpreadsheetComparatorNameListDialogComponentContextSortComparators> {

    @Test
    public void testWithNullAppContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetComparatorNameListDialogComponentContextSortComparators.with(null)
        );
    }

    // isMatch..........................................................................................................

    @Test
    public void testIsMatchWithSpreadsheetMetadataPropertySelectHistoryTokenSortComparators() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.SORT_COMPARATORS
            ),
            true
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetMetadataPropertySelectHistoryTokenComparators() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.COMPARATORS
            ),
            false
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetMetadataPropertySelectHistoryTokenFunctions() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.FUNCTIONS
            ),
            false
        );
    }

    @Override
    public SpreadsheetComparatorNameListDialogComponentContextSortComparators createContext() {
        return SpreadsheetComparatorNameListDialogComponentContextSortComparators.with(AppContexts.fake());
    }

    @Override
    public String typeNameSuffix() {
        return "Comparators";
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetComparatorNameListDialogComponentContextSortComparators> type() {
        return SpreadsheetComparatorNameListDialogComponentContextSortComparators.class;
    }
}
