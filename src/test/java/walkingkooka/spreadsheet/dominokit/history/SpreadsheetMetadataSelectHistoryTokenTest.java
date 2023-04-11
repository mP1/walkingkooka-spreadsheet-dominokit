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

public final class SpreadsheetMetadataSelectHistoryTokenTest extends SpreadsheetMetadataHistoryTokenTestCase<SpreadsheetMetadataSelectHistoryToken> {

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/metadata");
    }

    @Test
    public void testParseDateFormatPattern() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/metadata/date-format",
                SpreadsheetMetadataSelectHistoryToken.with(
                        ID,
                        NAME
                )
        );
    }

    @Test
    public void testParseStyle() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/metadata/style",
                HistoryToken.spreadsheetSelect(
                        ID,
                        NAME
                )
        );
    }

    @Override
    SpreadsheetMetadataSelectHistoryToken createHistoryToken() {
        return this.createHistoryToken(
                ID,
                NAME
        );
    }

    @Override
    SpreadsheetMetadataSelectHistoryToken createHistoryToken(final SpreadsheetId id,
                                                             final SpreadsheetName name) {
        return SpreadsheetMetadataSelectHistoryToken.with(
                id,
                name
        );
    }

    @Override
    public Class<SpreadsheetMetadataSelectHistoryToken> type() {
        return SpreadsheetMetadataSelectHistoryToken.class;
    }
}
