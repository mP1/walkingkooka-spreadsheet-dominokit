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
import walkingkooka.validation.ValidationValueTypeName;

import java.util.Optional;

public abstract class SpreadsheetCellValueTypeHistoryTokenTestCase<T extends SpreadsheetCellValueTypeHistoryToken> extends SpreadsheetCellHistoryTokenTestCase<T>
    implements SpreadsheetMetadataTesting {

    SpreadsheetCellValueTypeHistoryTokenTestCase() {
        super();
    }

    // close............................................................................................................

    @Test
    public final void testClose() {
        this.closeAndCheck(
            this.createHistoryToken(),
            HistoryToken.cellSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    // setSaveValue.....................................................................................................

    @Test
    public final void testSetSaveValueWithInvalidOptionalValueFails() {
        this.setSaveValueFails(
            Optional.of(this)
        );
    }

    @Test
    public final void testSetSaveValueWithEmpty() {
        final Optional<ValidationValueTypeName> value = Optional.empty();

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            value,
            HistoryToken.cellValueTypeSave(
                ID,
                NAME,
                SELECTION,
                value
            )
        );
    }

    @Test
    public final void testSetSaveValueWithNonEmpty() {
        final Optional<ValidationValueTypeName> value = Optional.of(
            ValidationValueTypeName.with("hello-type-name")
        );

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            value,
            HistoryToken.cellValueTypeSave(
                ID,
                NAME,
                SELECTION,
                value
            )
        );
    }
}
