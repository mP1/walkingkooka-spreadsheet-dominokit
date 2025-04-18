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
import walkingkooka.color.Color;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetMetadataPropertySaveHistoryTokenTest extends SpreadsheetMetadataPropertyHistoryTokenTestCase<SpreadsheetMetadataPropertySaveHistoryToken<ExpressionNumberKind>, ExpressionNumberKind> {

    // with.............................................................................................................

    @Test
    public void testWithNullValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetMetadataPropertySaveHistoryToken.with(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.LOCALE,
                null
            )
        );
    }

    @Test
    public void testWithIncompatibleValueFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> SpreadsheetMetadataPropertySaveHistoryToken.with(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.EXPRESSION_NUMBER_KIND,
                Cast.to(Optional.of("Hello"))
            )
        );
    }

    // HasUrlFragment...................................................................................................

    @Test
    public void testUrlFragmentExpressionNumberKind() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/spreadsheet/expressionNumberKind/save/BIG_DECIMAL");
    }

    @Test
    public void testUrlFragmentDateFormatPatternNullValue() {
        this.urlFragmentAndCheck(
            SpreadsheetMetadataPropertySaveHistoryToken.with(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.DATE_FORMATTER,
                Optional.empty()
            ),
            "/123/SpreadsheetName456/spreadsheet/dateFormatter/save/"
        );
    }

    @Test
    public void testUrlFragmentDateFormatter() {
        this.urlFragmentAndCheck(
            SpreadsheetMetadataPropertySaveHistoryToken.with(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.DATE_FORMATTER,
                Optional.of(
                    SpreadsheetPattern.parseDateFormatPattern("yymmdd")
                        .spreadsheetFormatterSelector()
                )
            ),
            "/123/SpreadsheetName456/spreadsheet/dateFormatter/save/date-format-pattern yymmdd"
        );
    }

    @Test
    public void testUrlFragmentDefaultYear() {
        this.urlFragmentAndCheck(
            SpreadsheetMetadataPropertySaveHistoryToken.with(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.DEFAULT_YEAR,
                Optional.of(
                    99
                )
            ),
            "/123/SpreadsheetName456/spreadsheet/defaultYear/save/99"
        );
    }

    @Test
    public void testParseDateFormatter() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/spreadsheet/dateFormatter/save/date-format-pattern%20yymmdd",
            SpreadsheetMetadataPropertySaveHistoryToken.with(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.DATE_FORMATTER,
                Optional.of(
                    SpreadsheetPattern.parseDateFormatPattern("yymmdd")
                        .spreadsheetFormatterSelector()
                )
            )
        );
    }

    @Test
    public void testParseDefaultYear() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/spreadsheet/defaultYear/save/49",
            SpreadsheetMetadataPropertySaveHistoryToken.with(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.DEFAULT_YEAR,
                Optional.of(
                    49
                )
            )
        );
    }

    // cant save a new id but can select spreadsheet-id.
    @Test
    public void testParseSpreadsheetId() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/spreadsheet/spreadsheetId/save/456",
            HistoryToken.metadataPropertySelect(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.SPREADSHEET_ID
            )
        );
    }

    @Test
    public void testParseStyleSaveWithoutValue() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/spreadsheet/style/color/save/",
            HistoryToken.metadataPropertyStyleSave(
                ID,
                NAME,
                TextStylePropertyName.COLOR,
                Optional.empty()
            )
        );
    }

    @Test
    public void testParseStyleSaveValue() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/spreadsheet/style/color/save/#123456",
            HistoryToken.metadataPropertyStyleSave(
                ID,
                NAME,
                TextStylePropertyName.COLOR,
                Optional.of(
                    Color.parse("#123456")
                )
            )
        );
    }

    // clearAction.....................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
            this.createHistoryToken(),
            HistoryToken.metadataPropertySelect(
                ID,
                NAME,
                EXPRESSION_NUMBER_KIND
            )
        );
    }

    // close............................................................................................................

    @Test
    public void testCloseFormatPattern() {
        this.closeAndCheck(
            HistoryToken.metadataPropertySave(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.DATE_FORMATTER,
                Optional.of(
                    SpreadsheetPattern.parseDateFormatPattern("dd/mm/yyyy")
                        .spreadsheetFormatterSelector()
                )
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
            HistoryToken.metadataPropertySave(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.DATE_PARSER,
                Optional.of(
                    SpreadsheetPattern.parseDateParsePattern("dd/mm/yyyy")
                        .spreadsheetParserSelector()
                )
            ),
            HistoryToken.metadataSelect(
                ID,
                NAME
            )
        );
    }

    // setSaveValue.....................................................................................................

    @Test
    public void testSetSaveValueWithEmpty() {
        final Optional<ExpressionNumberKind> value = Optional.empty();

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            value,
            HistoryToken.metadataPropertySave(
                ID,
                NAME,
                EXPRESSION_NUMBER_KIND,
                value
            )
        );
    }

    @Test
    public void testSetSaveValueWithDifferentNotEmpty() {
        final Optional<ExpressionNumberKind> value = Optional.of(ExpressionNumberKind.DOUBLE);

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            value,
            HistoryToken.metadataPropertySave(
                ID,
                NAME,
                EXPRESSION_NUMBER_KIND,
                value
            )
        );
    }

    // helper...........................................................................................................

    @Override
    SpreadsheetMetadataPropertySaveHistoryToken<ExpressionNumberKind> createHistoryToken(final SpreadsheetId id,
                                                                                         final SpreadsheetName name) {
        return this.createHistoryToken(
            id,
            name,
            EXPRESSION_NUMBER_KIND
        );
    }

    @Override
    SpreadsheetMetadataPropertySaveHistoryToken<ExpressionNumberKind> createHistoryToken(final SpreadsheetId id,
                                                                                         final SpreadsheetName name,
                                                                                         final SpreadsheetMetadataPropertyName<ExpressionNumberKind> propertyName) {
        return SpreadsheetMetadataPropertySaveHistoryToken.with(
            id,
            name,
            propertyName,
            Optional.of(
                ExpressionNumberKind.BIG_DECIMAL
            )
        );
    }

    @Override
    public Class<SpreadsheetMetadataPropertySaveHistoryToken<ExpressionNumberKind>> type() {
        return Cast.to(SpreadsheetMetadataPropertySaveHistoryToken.class);
    }
}
