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

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class SpreadsheetIdHistoryTokenTestCase<T extends SpreadsheetHistoryToken> extends SpreadsheetHistoryTokenTestCase<T> {

    SpreadsheetIdHistoryTokenTestCase() {
        super();
    }

    @Test
    public final void testWithNullIdFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createHistoryToken(null)
        );
    }

    final void renameAndCheck(final HistoryToken token,
                              final HistoryToken expected) {
        this.checkEquals(
            expected,
            token.rename(),
            token::toString
        );
    }

    @Override final T createHistoryToken() {
        return this.createHistoryToken(ID);
    }

    abstract T createHistoryToken(final SpreadsheetId id);
}
