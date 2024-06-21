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

    // setMetadataPropertyName..........................................................................................

    @Test
    public void testSetMetadataPropertyName() {
        final SpreadsheetMetadataPropertyName<?> propertyName = SpreadsheetMetadataPropertyName.LOCALE;

        this.setMetadataPropertyNameAndCheck(
                propertyName,
                HistoryToken.metadataPropertySelect(
                        ID,
                        NAME,
                        propertyName
                )
        );
    }

    @Test
    public void testSetMetadataPropertyName2() {
        final SpreadsheetMetadataPropertyName<?> propertyName = SpreadsheetMetadataPropertyName.HIDE_ZERO_VALUES;

        this.setMetadataPropertyNameAndCheck(
                this.createHistoryToken(),
                propertyName,
                HistoryToken.metadataPropertySelect(
                        ID,
                        NAME,
                        propertyName
                )
        );
    }

    // setPatternKind...................................................................................................

    @Test
    public void testSetPatternKindSame() {
        final SpreadsheetMetadataPropertySelectHistoryToken<?> token = SpreadsheetMetadataPropertySelectHistoryToken.with(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.DATE_FORMATTER
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
                        SpreadsheetMetadataPropertyName.TEXT_FORMATTER
                ),
                SpreadsheetPatternKind.DATE_FORMAT_PATTERN,
                SpreadsheetMetadataPropertySelectHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetMetadataPropertyName.DATE_FORMATTER
                )
        );
    }

    @Test
    public void testSetPatternKindDifferent2() {
        this.setPatternKindAndCheck(
                SpreadsheetMetadataPropertySelectHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetMetadataPropertyName.TEXT_FORMATTER
                ),
                SpreadsheetPatternKind.DATE_TIME_PARSE_PATTERN,
                SpreadsheetMetadataPropertySelectHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetMetadataPropertyName.DATE_TIME_PARSER
                )
        );
    }

    @Test
    public void testSetPatternKindEmpty() {
        this.setPatternKindAndCheck(
                SpreadsheetMetadataPropertySelectHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetMetadataPropertyName.TEXT_FORMATTER
                ),
                HistoryToken.metadataSelect(
                        ID,
                        NAME
                )
        );
    }

    // clearAction.....................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck();
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
