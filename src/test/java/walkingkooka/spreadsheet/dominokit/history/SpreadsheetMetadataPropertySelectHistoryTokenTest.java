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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.tree.expression.ExpressionNumberKind;

import java.util.Optional;

public final class SpreadsheetMetadataPropertySelectHistoryTokenTest extends SpreadsheetMetadataPropertyHistoryTokenTestCase<SpreadsheetMetadataPropertySelectHistoryToken<ExpressionNumberKind>, ExpressionNumberKind> {

    // UrlFragment......................................................................................................

    @Test
    public void testUrlFragmentExpressionNumberKind() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/spreadsheet/expressionNumberKind");
    }

    @Test
    public void testUrlFragmentDateFormatter() {
        this.urlFragmentAndCheck(
            SpreadsheetMetadataPropertySelectHistoryToken.with(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.DATE_FORMATTER
            ),
            "/123/SpreadsheetName456/spreadsheet/dateFormatter"
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
            "/123/SpreadsheetName456/spreadsheet/defaultYear"
        );
    }

    // Parse............................................................................................................

    @Test
    public void testParseDateFormatter() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/spreadsheet/dateFormatter",
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
            "/123/SpreadsheetName456/spreadsheet/defaultYear",
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
            "/123/SpreadsheetName456/spreadsheet/spreadsheetId",
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
    // saveValue........................................................................................................

    @Test
    public void testSetSaveValueDateTimeOffset() {
        final SpreadsheetMetadataPropertyName<Long> propertyName = SpreadsheetMetadataPropertyName.DATE_TIME_OFFSET;
        final Long value = Converters.EXCEL_1904_DATE_SYSTEM_OFFSET;

        this.setSaveValueAndCheck(
            SpreadsheetMetadataPropertySelectHistoryToken.with(
                ID,
                NAME,
                propertyName
            ),
            Optional.of(value),
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
    public void testSetSaveValueDateTimeOffsetEmpty() {
        final SpreadsheetMetadataPropertyName<Long> propertyName = SpreadsheetMetadataPropertyName.DATE_TIME_OFFSET;

        this.setSaveValueAndCheck(
            SpreadsheetMetadataPropertySelectHistoryToken.with(
                ID,
                NAME,
                propertyName
            ),
            Optional.empty(),
            HistoryToken.metadataPropertySave(
                ID,
                NAME,
                propertyName,
                Optional.empty()
            )
        );
    }

    @Test
    public void testSetSaveValueExpressionNumberKind() {
        final ExpressionNumberKind kind = ExpressionNumberKind.DOUBLE;

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.of(kind),
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
    public void testSetSaveValueExpressionNumberKindEmpty() {
        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.empty(),
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
