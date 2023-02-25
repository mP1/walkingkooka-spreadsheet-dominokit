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

public abstract class SpreadsheetHistoryHashTokenTestCase<T extends SpreadsheetHistoryHashToken> extends HistoryHashTokenTestCase<T> {

    final static SpreadsheetId ID = SpreadsheetId.with(0x123);

    final static SpreadsheetName NAME = SpreadsheetName.with("SpreadsheetName456");

    SpreadsheetHistoryHashTokenTestCase() {
        super();
    }

    @Test
    public final void testParse() {
        final T token = this.createHistoryHashToken();

        this.parseAndCheck(
                token.urlFragment(),
                token
        );
    }

    final void parseAndCheck(final String fragment,
                             final SpreadsheetHistoryHashToken token) {
        this.parseAndCheck(
                UrlFragment.parse(fragment),
                token
        );
    }

    final void parseAndCheck(final UrlFragment fragment,
                             final SpreadsheetHistoryHashToken token) {
        this.checkEquals(
                token,
                SpreadsheetHistoryHashToken.parse(fragment)
                        .get()
        );
    }
}
