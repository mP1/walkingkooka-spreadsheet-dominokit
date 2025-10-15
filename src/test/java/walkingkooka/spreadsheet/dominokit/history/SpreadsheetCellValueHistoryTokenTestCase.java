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

package walkingkooka.spreadsheet.dominokit.history;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.validation.ValueTypeName;

import java.util.Optional;

public abstract class SpreadsheetCellValueHistoryTokenTestCase<T extends SpreadsheetCellValueHistoryToken> extends SpreadsheetCellHistoryTokenTestCase<T>
    implements SpreadsheetMetadataTesting {

    SpreadsheetCellValueHistoryTokenTestCase() {
        super();
    }

    // navigation.......................................................................................................

    @Test
    public final void testNavigation() {
        this.navigationAndCheck(
            this.createHistoryToken()
        );
    }

    @Test
    public final void testSetValueWithEmpty() {
        this.setValueAndCheck(
            this.createHistoryToken(),
            Optional.empty(), // no ValidationValueType
            HistoryToken.cellValueUnselect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    final void setValueAndCheck(final SpreadsheetCellValueHistoryToken historyToken,
                                final Optional<ValueTypeName> valueType,
                                final HistoryToken expected) {
        this.checkEquals(
            expected,
            historyToken.setValue(valueType)
        );
    }
}
