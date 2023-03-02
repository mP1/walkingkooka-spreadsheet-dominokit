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

public final class SpreadsheetMetadataPropertySaveHistoryHashTokenTest extends SpreadsheetMetadataPropertyHistoryHashTokenTestCase<SpreadsheetMetadataPropertySaveHistoryHashToken<ExpressionNumberKind>, ExpressionNumberKind> {

    @Test
    public void testUrlFragmentExpressionNumberKind() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/metadata/expression-number-kind/save/BIG_DECIMAL");
    }

    @Test
    public void testUrlFragmentDateFormatPattern() {
        this.urlFragmentAndCheck(
                SpreadsheetMetadataPropertySaveHistoryHashToken.with(
                        ID,
                        NAME,
                        SpreadsheetMetadataPropertyName.DATE_FORMAT_PATTERN,
                        SpreadsheetPattern.parseDateFormatPattern("yymmdd")
                ),
                "/123/SpreadsheetName456/metadata/pattern/date-format/save/yymmdd"
        );
    }

    @Test
    public void testUrlFragmentDefaultYear() {
        this.urlFragmentAndCheck(
                SpreadsheetMetadataPropertySaveHistoryHashToken.with(
                        ID,
                        NAME,
                        SpreadsheetMetadataPropertyName.DEFAULT_YEAR,
                        99
                ),
                "/123/SpreadsheetName456/metadata/default-year/save/99"
        );
    }

    @Test
    public void testParseDateFormatPattern() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/metadata/pattern/date-format/save/yymmdd",
                SpreadsheetMetadataPropertySaveHistoryHashToken.with(
                        ID,
                        NAME,
                        SpreadsheetMetadataPropertyName.DATE_FORMAT_PATTERN,
                        SpreadsheetPattern.parseDateFormatPattern("yymmdd")
                )
        );
    }

    @Test
    public void testParseDefaultYear() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/metadata/default-year/save/49",
                SpreadsheetMetadataPropertySaveHistoryHashToken.with(
                        ID,
                        NAME,
                        SpreadsheetMetadataPropertyName.DEFAULT_YEAR,
                        49
                )
        );
    }

    // cant save a new id but can select spreadsheet-id.
    @Test
    public void testParseSpreadsheetId() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/metadata/spreadsheet-id/save/456",
                SpreadsheetHistoryHashToken.metadataPropertySelect(
                        ID,
                        NAME,
                        SpreadsheetMetadataPropertyName.SPREADSHEET_ID
                )
        );
    }

    @Test
    public void testParseStyle() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/metadata/style/color/save/#123456",
                SpreadsheetHistoryHashToken.metadataPropertyStyleSave(
                        ID,
                        NAME,
                        TextStylePropertyName.COLOR,
                        Color.parse("#123456")
                )
        );
    }

    @Override
    SpreadsheetMetadataPropertySaveHistoryHashToken<ExpressionNumberKind> createHistoryHashToken() {
        return this.createHistoryHashToken(
                ID,
                NAME,
                EXPRESSION_NUMBER_KIND
        );
    }

    @Override
    SpreadsheetMetadataPropertySaveHistoryHashToken<ExpressionNumberKind> createHistoryHashToken(final SpreadsheetId id,
                                                                                                 final SpreadsheetName name,
                                                                                                 final SpreadsheetMetadataPropertyName<ExpressionNumberKind> propertyName) {
        return SpreadsheetMetadataPropertySaveHistoryHashToken.with(
                id,
                name,
                propertyName,
                ExpressionNumberKind.BIG_DECIMAL
        );
    }

    @Override
    public Class<SpreadsheetMetadataPropertySaveHistoryHashToken<ExpressionNumberKind>> type() {
        return Cast.to(SpreadsheetMetadataPropertySaveHistoryHashToken.class);
    }
}
