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
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class SpreadsheetHistoryTokenTestCase<T extends SpreadsheetHistoryToken> extends HistoryTokenTestCase<T> {

    final static SpreadsheetId ID = SpreadsheetId.with(0x123);

    final static SpreadsheetName NAME = SpreadsheetName.with("SpreadsheetName456");

    SpreadsheetHistoryTokenTestCase() {
        super();
    }

    @Test
    public final void testParse() {
        final T token = this.createHistoryToken();

        this.parseAndCheck(
                token.urlFragment(),
                token
        );
    }

    final void parseAndCheck(final String fragment,
                             final SpreadsheetHistoryToken token) {
        this.parseAndCheck(
                UrlFragment.parse(fragment),
                token
        );
    }

    final void parseAndCheck(final UrlFragment fragment,
                             final SpreadsheetHistoryToken token) {
        this.checkEquals(
                token,
                SpreadsheetHistoryToken.parse(fragment)
                        .get()
        );
    }

    // setIdAndName.....................................................................................................

    @Test
    public final void testSetIdAndNameNullIdFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createHistoryToken().setIdAndName(
                        null,
                        NAME
                )
        );
    }

    @Test
    public final void testSetIdAndNameNullNameFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createHistoryToken().setIdAndName(
                        ID,
                        null
                )
        );
    }

    final void setIdAndNameAndCheck(final SpreadsheetId id,
                                    final SpreadsheetName name,
                                    final SpreadsheetHistoryToken expected) {
        this.setIdAndNameAndCheck(
                this.createHistoryToken(),
                id,
                name,
                expected
        );
    }

    final void setIdAndNameAndCheck(final SpreadsheetHistoryToken token,
                                    final SpreadsheetId id,
                                    final SpreadsheetName name,
                                    final SpreadsheetHistoryToken expected) {
        this.checkEquals(
                expected,
                token.setIdAndName(
                        id,
                        name
                ),
                () -> token + " setIdAndName " + id + ", " + name
        );
    }
}
