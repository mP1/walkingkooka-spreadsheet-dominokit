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
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;

public abstract class SpreadsheetNameHistoryTokenTestCase<T extends SpreadsheetNameHistoryToken> extends SpreadsheetIdHistoryTokenTestCase<T> {

    SpreadsheetNameHistoryTokenTestCase() {
        super();
    }

    @Test
    public void testSetIdAndNameDifferentId() {
        final T token = this.createHistoryToken();

        final SpreadsheetId differentId = SpreadsheetId.with(999);

        final SpreadsheetNameHistoryToken different = (SpreadsheetNameHistoryToken) token.setIdAndName(
                differentId,
                NAME
        );

        this.checkEquals(
                differentId,
                different.id(),
                "id"
        );

        this.checkEquals(
                NAME,
                different.name(),
                "name"
        );

        this.setIdAndNameAndCheck(
                different,
                ID,
                NAME,
                token
        );
    }

    @Test
    public void testSetIdAndNameDifferentName() {
        final T token = this.createHistoryToken();

        final SpreadsheetName differentName = SpreadsheetName.with("different");

        final SpreadsheetNameHistoryToken different = (SpreadsheetNameHistoryToken) token.setIdAndName(
                ID,
                differentName
        );

        this.checkEquals(
                ID,
                different.id(),
                "id"
        );

        this.checkEquals(
                differentName,
                different.name(),
                "name"
        );

        this.setIdAndNameAndCheck(
                different,
                ID,
                NAME,
                token
        );
    }
}
