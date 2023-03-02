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
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.tree.expression.ExpressionNumberKind;

public final class SpreadsheetMetadataPropertySelectHistoryHashTokenTest extends SpreadsheetMetadataHistoryHashTokenTestCase<SpreadsheetMetadataPropertySelectHistoryHashToken<ExpressionNumberKind>, ExpressionNumberKind> {

    @Test
    public void testUrlFragmentExpressionNumberKind() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/metadata/expression-number-kind");
    }

    @Test
    public void testUrlFragmentDateFormatPattern() {
        this.urlFragmentAndCheck(
                SpreadsheetMetadataPropertySelectHistoryHashToken.with(
                        ID,
                        NAME,
                        SpreadsheetMetadataPropertyName.DATE_FORMAT_PATTERN
                ),
                "/123/SpreadsheetName456/metadata/pattern/date-format"
        );
    }

    @Test
    public void testUrlFragmentDefaultYear() {
        this.urlFragmentAndCheck(
                SpreadsheetMetadataPropertySelectHistoryHashToken.with(
                        ID,
                        NAME,
                        SpreadsheetMetadataPropertyName.DEFAULT_YEAR
                ),
                "/123/SpreadsheetName456/metadata/default-year"
        );
    }

    @Test
    public void testParseDateFormatPattern() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/metadata/pattern/date-format",
                SpreadsheetMetadataPropertySelectHistoryHashToken.with(
                        ID,
                        NAME,
                        SpreadsheetMetadataPropertyName.DATE_FORMAT_PATTERN
                )
        );
    }

    @Test
    public void testParseDefaultYear() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/metadata/default-year",
                SpreadsheetMetadataPropertySelectHistoryHashToken.with(
                        ID,
                        NAME,
                        SpreadsheetMetadataPropertyName.DEFAULT_YEAR
                )
        );
    }

    @Test
    public void testParseSpreadsheetId() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/metadata/spreadsheet-id",
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
                "/123/SpreadsheetName456/metadata/style",
                SpreadsheetHistoryHashToken.spreadsheetSelect(
                        ID,
                        NAME
                )
        );
    }

    @Override
    SpreadsheetMetadataPropertySelectHistoryHashToken<ExpressionNumberKind> createHistoryHashToken() {
        return this.createHistoryHashToken(
                ID,
                NAME,
                EXPRESSION_NUMBER_KIND
        );
    }

    @Override
    SpreadsheetMetadataPropertySelectHistoryHashToken<ExpressionNumberKind> createHistoryHashToken(final SpreadsheetId id,
                                                                                                   final SpreadsheetName name,
                                                                                                   final SpreadsheetMetadataPropertyName<ExpressionNumberKind> propertyName) {
        return SpreadsheetMetadataPropertySelectHistoryHashToken.with(
                id,
                name,
                propertyName
        );
    }

    @Override
    public Class<SpreadsheetMetadataPropertySelectHistoryHashToken<ExpressionNumberKind>> type() {
        return Cast.to(SpreadsheetMetadataPropertySelectHistoryHashToken.class);
    }
}
