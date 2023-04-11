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
    public void testUrlFragmentExpressionNumberKind() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/metadata/expression-number-kind/save/BIG_DECIMAL");
    }

    @Test
    public void testUrlFragmentDateFormatPatternNullValue() {
        this.urlFragmentAndCheck(
                SpreadsheetMetadataPropertySaveHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetMetadataPropertyName.DATE_FORMAT_PATTERN,
                        Optional.empty()
                ),
                "/123/SpreadsheetName456/metadata/pattern/date-format/save/"
        );
    }

    @Test
    public void testUrlFragmentDateFormatPattern() {
        this.urlFragmentAndCheck(
                SpreadsheetMetadataPropertySaveHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetMetadataPropertyName.DATE_FORMAT_PATTERN,
                        Optional.of(
                                SpreadsheetPattern.parseDateFormatPattern("yymmdd")
                        )
                ),
                "/123/SpreadsheetName456/metadata/pattern/date-format/save/yymmdd"
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
                "/123/SpreadsheetName456/metadata/default-year/save/99"
        );
    }

    @Test
    public void testParseDateFormatPattern() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/metadata/pattern/date-format/save/yymmdd",
                SpreadsheetMetadataPropertySaveHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetMetadataPropertyName.DATE_FORMAT_PATTERN,
                        Optional.of(
                                SpreadsheetPattern.parseDateFormatPattern("yymmdd")
                        )
                )
        );
    }

    @Test
    public void testParseDefaultYear() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/metadata/default-year/save/49",
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
                "/123/SpreadsheetName456/metadata/spreadsheet-id/save/456",
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
                "/123/SpreadsheetName456/metadata/style/color/save/",
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
                "/123/SpreadsheetName456/metadata/style/color/save/#123456",
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

    @Override
    SpreadsheetMetadataPropertySaveHistoryToken<ExpressionNumberKind> createHistoryToken() {
        return this.createHistoryToken(
                ID,
                NAME,
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
