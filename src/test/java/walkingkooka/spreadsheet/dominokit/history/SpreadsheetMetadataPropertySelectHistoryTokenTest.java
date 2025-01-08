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
import walkingkooka.Cast;
import walkingkooka.convert.Converters;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.tree.expression.ExpressionNumberKind;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class SpreadsheetMetadataPropertySelectHistoryTokenTest extends SpreadsheetMetadataPropertyHistoryTokenTestCase<SpreadsheetMetadataPropertySelectHistoryToken<ExpressionNumberKind>, ExpressionNumberKind> {

    // HasSpreadsheetPatternKind........................................................................................

    @Test
    public void testHasSpreadsheetPatternKindColor1() {
        this.hasSpreadsheetPatternKindAndCheck(
            SpreadsheetMetadataPropertySelectHistoryToken.with(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.numberedColor(1)
            )
        );
    }

    @Test
    public void testHasSpreadsheetPatternKindDateFormatter() {
        this.hasSpreadsheetPatternKindAndCheck(
            SpreadsheetMetadataPropertySelectHistoryToken.with(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.DATE_FORMATTER
            ),
            SpreadsheetPatternKind.DATE_FORMAT_PATTERN
        );
    }

    @Test
    public void testHasSpreadsheetPatternKindDateTimeParser() {
        this.hasSpreadsheetPatternKindAndCheck(
            SpreadsheetMetadataPropertySelectHistoryToken.with(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.DATE_TIME_PARSER
            ),
            SpreadsheetPatternKind.DATE_TIME_PARSE_PATTERN
        );
    }

    // UrlFragment......................................................................................................

    @Test
    public void testUrlFragmentExpressionNumberKind() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/spreadsheet/expression-number-kind");
    }

    @Test
    public void testUrlFragmentDateFormatter() {
        this.urlFragmentAndCheck(
            SpreadsheetMetadataPropertySelectHistoryToken.with(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.DATE_FORMATTER
            ),
            "/123/SpreadsheetName456/spreadsheet/date-formatter"
        );
    }

    @Test
    public void testUrlFragmentDefaultYear() {
        this.urlFragmentAndCheck(
            SpreadsheetMetadataPropertySelectHistoryToken.with(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.DEFAULT_YEAR
            ),
            "/123/SpreadsheetName456/spreadsheet/default-year"
        );
    }

    // Parse............................................................................................................

    @Test
    public void testParseDateFormatter() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/spreadsheet/date-formatter",
            SpreadsheetMetadataPropertySelectHistoryToken.with(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.DATE_FORMATTER
            )
        );
    }

    @Test
    public void testParseDefaultYear() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/spreadsheet/default-year",
            SpreadsheetMetadataPropertySelectHistoryToken.with(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.DEFAULT_YEAR
            )
        );
    }

    @Test
    public void testParseSpreadsheetId() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/spreadsheet/spreadsheet-id",
            HistoryToken.metadataPropertySelect(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.SPREADSHEET_ID
            )
        );
    }

    @Test
    public void testParseStyle() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/spreadsheet/style",
            HistoryToken.spreadsheetSelect(
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

    // close............................................................................................................

    @Test
    public void testCloseFormatter() {
        this.closeAndCheck(
            HistoryToken.metadataPropertySelect(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.DATE_FORMATTER
            ),
            HistoryToken.metadataSelect(
                ID,
                NAME
            )
        );
    }

    @Test
    public void testCloseParsePattern() {
        this.closeAndCheck(
            HistoryToken.metadataPropertySelect(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.DATE_PARSER
            ),
            HistoryToken.metadataSelect(
                ID,
                NAME
            )
        );
    }

    // setMetadataPropertyName..........................................................................................

    @Test
    public void testSetMetadataPropertyName() {
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

    // save.............................................................................................................

    @Test
    public void testSaveDateTimeOffset() {
        final SpreadsheetMetadataPropertyName<Long> propertyName = SpreadsheetMetadataPropertyName.DATETIME_OFFSET;
        final Long value = Converters.EXCEL_1904_DATE_SYSTEM_OFFSET;

        this.saveAndCheck(
            SpreadsheetMetadataPropertySelectHistoryToken.with(
                ID,
                NAME,
                propertyName
            ),
            value.toString(),
            HistoryToken.metadataPropertySave(
                ID,
                NAME,
                propertyName,
                Optional.of(
                    value
                )
            )
        );
    }

    @Test
    public void testSaveDateTimeOffsetEmpty() {
        final SpreadsheetMetadataPropertyName<Long> propertyName = SpreadsheetMetadataPropertyName.DATETIME_OFFSET;

        this.saveAndCheck(
            SpreadsheetMetadataPropertySelectHistoryToken.with(
                ID,
                NAME,
                propertyName
            ),
            "",
            HistoryToken.metadataPropertySave(
                ID,
                NAME,
                propertyName,
                Optional.empty()
            )
        );
    }

    @Test
    public void testSaveExpressionNumberKind() {
        final ExpressionNumberKind kind = ExpressionNumberKind.DOUBLE;

        this.saveAndCheck(
            this.createHistoryToken(),
            kind.name(),
            HistoryToken.metadataPropertySave(
                ID,
                NAME,
                EXPRESSION_NUMBER_KIND,
                Optional.of(
                    kind
                )
            )
        );
    }

    @Test
    public void testSaveExpressionNumberKindEmpty() {
        this.saveAndCheck(
            this.createHistoryToken(),
            "",
            HistoryToken.metadataPropertySave(
                ID,
                NAME,
                EXPRESSION_NUMBER_KIND,
                Optional.empty()
            )
        );
    }

    // helper...........................................................................................................

    @Override
    SpreadsheetMetadataPropertySelectHistoryToken<ExpressionNumberKind> createHistoryToken(final SpreadsheetId id,
                                                                                           final SpreadsheetName name) {
        return this.createHistoryToken(
            id,
            name,
            EXPRESSION_NUMBER_KIND
        );
    }

    @Override
    SpreadsheetMetadataPropertySelectHistoryToken<ExpressionNumberKind> createHistoryToken(final SpreadsheetId id,
                                                                                           final SpreadsheetName name,
                                                                                           final SpreadsheetMetadataPropertyName<ExpressionNumberKind> propertyName) {
        return SpreadsheetMetadataPropertySelectHistoryToken.with(
            id,
            name,
            propertyName
        );
    }

    @Override
    public Class<SpreadsheetMetadataPropertySelectHistoryToken<ExpressionNumberKind>> type() {
        return Cast.to(SpreadsheetMetadataPropertySelectHistoryToken.class);
    }
}
