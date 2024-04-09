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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.OptionalInt;

public final class SpreadsheetListDeleteHistoryTokenTest extends SpreadsheetIdHistoryTokenTestCase<SpreadsheetListDeleteHistoryToken> {

    @Test
    public void testParseMissingSpreadsheetId() {
        this.parseAndCheck(
                "/delete",
                HistoryToken.spreadsheetList(
                        OptionalInt.empty(), // from
                        OptionalInt.empty() // count
                )
        );
    }

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck("/delete/123");
    }

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
                this.createHistoryToken(),
                HistoryToken.spreadsheetList(
                        OptionalInt.empty(), // from
                        OptionalInt.empty() // count
                )
        );
    }

    @Test
    public void testSetAnchoredSelection() {
        final SpreadsheetListDeleteHistoryToken token = this.createHistoryToken();

        this.setAnchoredSelectionAndCheck(
                token,
                token
        );
    }

    @Test
    public void testSetMetadataPropertyName() {
        this.setMetadataPropertyNameAndCheck(
                SpreadsheetMetadataPropertyName.LOCALE,
                this.createHistoryToken()
        );
    }

    @Test
    public void testPatternKind() {
        this.patternKindAndCheck(
                this.createHistoryToken()
        );
    }

    @Override
    SpreadsheetListDeleteHistoryToken createHistoryToken() {
        return SpreadsheetListDeleteHistoryToken.with(ID);
    }

    @Override
    public Class<SpreadsheetListDeleteHistoryToken> type() {
        return SpreadsheetListDeleteHistoryToken.class;
    }
}
