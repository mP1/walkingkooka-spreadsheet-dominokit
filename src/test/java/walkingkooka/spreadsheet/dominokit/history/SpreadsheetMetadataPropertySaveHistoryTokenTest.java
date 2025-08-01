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
import walkingkooka.CanBeEmpty;
import walkingkooka.Cast;
import walkingkooka.color.Color;
import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.plugin.PluginNameSet;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.SpreadsheetViewportRectangle;
import walkingkooka.spreadsheet.engine.SpreadsheetCellQuery;
import walkingkooka.spreadsheet.export.SpreadsheetExporterAliasSet;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionFunctions;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.importer.SpreadsheetImporterAliasSet;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.validation.form.provider.FormHandlerAliasSet;

import java.text.DecimalFormatSymbols;
import java.util.Collection;
import java.util.Locale;
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
    public void testUrlFragmentDecimalNumberSymbols() {
        this.urlFragmentAndCheck(
            SpreadsheetMetadataPropertySaveHistoryToken.with(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.DECIMAL_NUMBER_SYMBOLS,
                Optional.of(
                    DecimalNumberSymbols.fromDecimalFormatSymbols(
                        '+',
                        new DecimalFormatSymbols(Locale.US)
                    )
                )
            ),
            "/123/SpreadsheetName456/spreadsheet/decimalNumberSymbols/save/-,+,0,$,.,E,%22,%22,%E2%88%9E,.,NaN,%25,%E2%80%B0"
        );
    }

    @Test
    public void testUrlFragmentFrozenColumns() {
        this.urlFragmentAndCheck(
            SpreadsheetMetadataPropertySaveHistoryToken.with(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.FROZEN_COLUMNS,
                Optional.of(
                    SpreadsheetSelection.parseColumnRange("A:B")
                )
            ),
            "/123/SpreadsheetName456/spreadsheet/frozenColumns/save/A:B"
        );
    }

    @Test
    public void testUrlFragmentFrozenRows() {
        this.urlFragmentAndCheck(
            SpreadsheetMetadataPropertySaveHistoryToken.with(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.FROZEN_ROWS,
                Optional.of(
                    SpreadsheetSelection.parseRowRange("1:2")
                )
            ),
            "/123/SpreadsheetName456/spreadsheet/frozenRows/save/1:2"
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
    public void testParseDecimalNumberSymbols() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/spreadsheet/decimalNumberSymbols/save/-,+,0,$,.,E,%22,%22,%E2%88%9E,.,NaN,%25,%E2%80%B0",
            SpreadsheetMetadataPropertySaveHistoryToken.with(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.DECIMAL_NUMBER_SYMBOLS,
                Optional.of(
                    DecimalNumberSymbols.fromDecimalFormatSymbols(
                        '+',
                        new DecimalFormatSymbols(Locale.US)
                    )
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

    @Test
    public void testParseFrozenColumns() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/spreadsheet/frozenColumns/save/A:B",
            SpreadsheetMetadataPropertySaveHistoryToken.with(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.FROZEN_COLUMNS,
                Optional.of(
                    SpreadsheetSelection.parseColumnRange("A:B")
                )
            )
        );
    }

    @Test
    public void testParseFrozenRows() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/spreadsheet/frozenRows/save/1:2",
            SpreadsheetMetadataPropertySaveHistoryToken.with(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.FROZEN_ROWS,
                Optional.of(
                    SpreadsheetSelection.parseRowRange("1:2")
                )
            )
        );
    }

    @Test
    public void testParseLocale() {
        final Locale locale = Locale.forLanguageTag("en-AU");

        this.parseAndCheck(
            "/123/SpreadsheetName456/spreadsheet/locale/save/" + HistoryToken.saveUrlFragmentValue(locale),
            SpreadsheetMetadataPropertySaveHistoryToken.with(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.LOCALE,
                Optional.of(
                    locale
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

    @Test
    public void testParseUntilEmptySpreadsheetMetadataProperties() {
        final SpreadsheetMetadata metadata = SpreadsheetMetadataTesting.METADATA_EN_AU.set(
            SpreadsheetMetadataPropertyName.CLIPBOARD_EXPORTER,
            SpreadsheetExporterAliasSet.parse("clipboard-exporter1")
        ).set(
            SpreadsheetMetadataPropertyName.CLIPBOARD_IMPORTER,
            SpreadsheetImporterAliasSet.parse("clipboard-importer1")
        ).set(
            SpreadsheetMetadataPropertyName.FIND_FUNCTIONS,
            SpreadsheetExpressionFunctions.parseAliasSet("find-function1")
        ).set(
            SpreadsheetMetadataPropertyName.FIND_HIGHLIGHTING,
            true
        ).set(
            SpreadsheetMetadataPropertyName.FIND_QUERY,
            SpreadsheetCellQuery.parse("findQuery2()")
        ).set(
            SpreadsheetMetadataPropertyName.FORM_HANDLERS,
            FormHandlerAliasSet.parse("form-handler3")
        ).set(
            SpreadsheetMetadataPropertyName.FORMULA_FUNCTIONS,
            SpreadsheetExpressionFunctions.parseAliasSet("formulaFunction1")
        ).set(
            SpreadsheetMetadataPropertyName.FROZEN_COLUMNS,
            SpreadsheetSelection.parseColumnRange("A:B")
        ).set(
            SpreadsheetMetadataPropertyName.FROZEN_ROWS,
            SpreadsheetSelection.parseRowRange("1:2")
        ).set(
            SpreadsheetMetadataPropertyName.FUNCTIONS,
            SpreadsheetExpressionFunctions.parseAliasSet("functions4")
        ).set(
            SpreadsheetMetadataPropertyName.HIDE_ZERO_VALUES,
            false
        ).set(
            SpreadsheetMetadataPropertyName.PLUGINS,
            PluginNameSet.parse("plugins5")
        ).set(
            SpreadsheetMetadataPropertyName.VIEWPORT,
            SpreadsheetViewport.with(SpreadsheetViewportRectangle.parse("A1:100:200"))
        );

        // verify that SpreadsheetMetadata has all properties
        for (final SpreadsheetMetadataPropertyName<?> propertyName : SpreadsheetMetadataPropertyName.ALL) {
            if (SpreadsheetMetadataPropertyName.SPREADSHEET_ID == propertyName) {
                continue;
            }
            if (SpreadsheetMetadataPropertyName.SPREADSHEET_NAME == propertyName) {
                continue;
            }
            if (SpreadsheetMetadataPropertyName.AUDIT_INFO == propertyName) {
                continue;
            }
            if (SpreadsheetMetadataPropertyName.STYLE == propertyName) {
                continue;
            }

            final Object value = metadata.getOrFail(propertyName);
            if (value instanceof Collection) {
                final Collection<?> collection = (Collection<?>) value;
                if (collection.isEmpty()) {
                    continue;
                }
                this.checkEquals(
                    false,
                    collection.isEmpty(),
                    propertyName::toString
                );
            }
            if (value instanceof CanBeEmpty) {
                final CanBeEmpty canBeEmpty = (CanBeEmpty) value;
                this.checkEquals(
                    false,
                    canBeEmpty.isEmpty(),
                    propertyName::toString
                );
            }

            System.out.println(propertyName);
            System.out.println("  " + CharSequences.quoteIfChars(value));
            System.out.println("  " + "/123/SpreadsheetName456/spreadsheet/" + propertyName.value() + "/save/" + HistoryToken.saveUrlFragmentValue(value));

            this.parseAndCheck(
                "/123/SpreadsheetName456/spreadsheet/" + propertyName.value() + "/save/" + HistoryToken.saveUrlFragmentValue(value),
                SpreadsheetMetadataPropertySaveHistoryToken.with(
                    ID,
                    NAME,
                    propertyName,
                    Optional.of(
                        Cast.to(value)
                    )
                )
            );
        }
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
