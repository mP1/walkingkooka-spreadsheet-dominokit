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
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;

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

    // setPatternKind...................................................................................................

    @Test
    public void testSetPatternKindSame() {
        final SpreadsheetMetadataPropertySelectHistoryToken<?> token = SpreadsheetMetadataPropertySelectHistoryToken.with(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.DATE_FORMAT_PATTERN
        );
        assertSame(
                token,
                token.setPatternKind(
                        Optional.of(
                                SpreadsheetPatternKind.DATE_FORMAT_PATTERN
                        )
                )
        );
    }

    @Test
    public void testSetPatternKindDifferent() {
        this.setPatternKindAndCheck(
                SpreadsheetMetadataPropertySelectHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetMetadataPropertyName.TEXT_FORMAT_PATTERN
                ),
                SpreadsheetPatternKind.DATE_FORMAT_PATTERN,
                SpreadsheetMetadataPropertySelectHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetMetadataPropertyName.DATE_FORMAT_PATTERN
                )
        );
    }

    @Test
    public void testSetPatternKindDifferent2() {
        this.setPatternKindAndCheck(
                SpreadsheetMetadataPropertySelectHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetMetadataPropertyName.TEXT_FORMAT_PATTERN
                ),
                SpreadsheetPatternKind.DATE_TIME_PARSE_PATTERN,
                SpreadsheetMetadataPropertySelectHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetMetadataPropertyName.DATETIME_PARSE_PATTERN
                )
        );
    }

    @Test
    public void testSetPatternKindEmpty() {
        this.setPatternKindAndCheck(
                SpreadsheetMetadataPropertySelectHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetMetadataPropertyName.TEXT_FORMAT_PATTERN
                ),
                HistoryToken.metadataSelect(
                        ID,
                        NAME
                )
        );
    }

    // helpers..........................................................................................................

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
