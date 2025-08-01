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

package walkingkooka.spreadsheet.dominokit.find;

import org.junit.jupiter.api.Test;
import walkingkooka.CanBeEmpty;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.PublicStaticHelperTesting;
import walkingkooka.spreadsheet.engine.SpreadsheetCellQuery;
import walkingkooka.spreadsheet.expression.function.TextMatch;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.formula.SpreadsheetFormulaParsers;
import walkingkooka.spreadsheet.formula.parser.ConditionRightSpreadsheetFormulaParserToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.printer.TreePrintableTesting;

import java.lang.reflect.Method;
import java.util.Optional;

public final class SpreadsheetFindDialogComponentQueryTest implements PublicStaticHelperTesting<SpreadsheetFindDialogComponentQuery>,
    TreePrintableTesting,
    SpreadsheetMetadataTesting {

    // without query but cellXXX getters................................................................................

    @Test
    public void testQueryWithEmpty() {
        this.queryAndCheck(
            "", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "", // style
            "", // value
            "", // validator
            "", // formattedValue
            ""
        );
    }

    @Test
    public void testQueryWithEmptyAndFormula() {
        this.queryAndCheck(
            "", // query
            "*formula*", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "", // style
            "", // value
            "", // validator
            "", // formattedValue
            "textMatch(\"*formula*\",cellFormula())"
        );
    }

    @Test
    public void testQueryWithEmptyAndFormatter() {
        this.queryAndCheck(
            "", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "*formatter*", // formatter
            "", // parser
            "", // style
            "", // value
            "", // validator
            "", // formattedValue
            "textMatch(\"*formatter*\",cellFormatter())"
        );
    }

    @Test
    public void testQueryWithEmptyAndParser() {
        this.queryAndCheck(
            "", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "*parser*", // parser
            "", // style
            "", // value
            "", // validator
            "", // formattedValue
            "textMatch(\"*parser*\",cellParser())"
        );
    }

    @Test
    public void testQueryWithEmptyAndStyle() {
        this.queryAndCheck(
            "", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "*style*", // style
            "", // value
            "", // validator
            "", // formattedValue
            "textMatch(\"*style*\",cellStyle())"
        );
    }

    @Test
    public void testQueryWithEmptyAndValue() {
        this.queryAndCheck(
            "", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "", // style
            "<111", // value
            "", // validator
            "", // formattedValue
            "cellValue()<111"
        );
    }

    @Test
    public void testQueryWithEmptyAndFormattedValue() {
        this.queryAndCheck(
            "", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "", // style
            "", // value
            "", // validator
            "*formattedValue*", // formattedValue
            "textMatch(\"*formattedValue*\",cellFormattedValue())"
        );
    }

    // existing query but no cellXXX getters............................................................................

    @Test
    public void testQueryWithNotEmpty() {
        this.queryAndCheck(
            "oldQuery()", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "", // style
            "", // value
            "", // validator
            "", // formattedValue
            "oldQuery()"
        );
    }

    @Test
    public void testQueryWithNotEmptyAndFormula() {
        this.queryAndCheck(
            "oldQuery()", // query
            "*formula*", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "", // style
            "", // value
            "", // validator
            "", // formattedValue
            "or(oldQuery(),textMatch(\"*formula*\",cellFormula()))"
        );
    }

    @Test
    public void testQueryWithNotEmptyAndDateTimeSymbols() {
        this.queryAndCheck(
            "oldQuery()", // query
            "", // formula
            "*dateTimeSymbols*", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "", // style
            "", // value
            "", // validator
            "", // formattedValue
            "or(oldQuery(),textMatch(\"*dateTimeSymbols*\",cellDateTimeSymbols()))"
        );
    }

    @Test
    public void testQueryWithNotEmptyAnDecimalNumberSymbols() {
        this.queryAndCheck(
            "oldQuery()", // query
            "", // formula
            "", // dateTimeSymbols
            "*decimalNumberSymbols*", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "", // style
            "", // value
            "", // validator
            "", // formattedValue
            "or(oldQuery(),textMatch(\"*decimalNumberSymbols*\",cellDecimalNumberSymbols()))"
        );
    }

    @Test
    public void testQueryWithNotEmptyAndFormatter() {
        this.queryAndCheck(
            "oldQuery()", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "*formatter*", // formatter
            "", // parser
            "", // style
            "", // value
            "", // validator
            "", // formattedValue
            "or(oldQuery(),textMatch(\"*formatter*\",cellFormatter()))"
        );
    }

    @Test
    public void testQueryWithNotEmptyAndParser() {
        this.queryAndCheck(
            "oldQuery()", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "*parser*", // parser
            "", // style
            "", // value
            "", // validator
            "", // formattedValue
            "or(oldQuery(),textMatch(\"*parser*\",cellParser()))"
        );
    }

    @Test
    public void testQueryWithNotEmptyAndStyle() {
        this.queryAndCheck(
            "oldQuery()", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "*style*", // style
            "", // value
            "", // validator
            "", // formattedValue
            "or(oldQuery(),textMatch(\"*style*\",cellStyle()))"
        );
    }

    @Test
    public void testQueryWithNotEmptyAndValue() {
        this.queryAndCheck(
            "oldQuery()", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "", // style
            "<111", // value
            "", // validator
            "", // formattedValue
            "or(oldQuery(),cellValue()<111)"
        );
    }

    @Test
    public void testQueryWithNotEmptyAndFormattedValue() {
        this.queryAndCheck(
            "oldQuery()", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "", // style
            "", // value
            "", // validator
            "*formattedValue*", // formattedValue
            "or(oldQuery(),textMatch(\"*formattedValue*\",cellFormattedValue()))"
        );
    }

    // existing query replaced with cellXXX getters.....................................................................

    @Test
    public void testQueryFormulaAndFormula() {
        this.queryAndCheck(
            "textMatch(\"*old*\",cellFormula())", // query
            "*new*", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "", // style
            "", // value
            "", // validator
            "", // formattedValue
            "textMatch(\"*new*\",cellFormula())"
        );
    }

    @Test
    public void testQueryFormatterAndFormatter() {
        this.queryAndCheck(
            "textMatch(\"*old*\",cellFormatter())", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "*new*", // formatter
            "", // parser
            "", // style
            "", // value
            "", // validator
            "", // formattedValue
            "textMatch(\"*new*\",cellFormatter())"
        );
    }

    @Test
    public void testQueryParserAndParser() {
        this.queryAndCheck(
            "textMatch(\"*old*\",cellParser())", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "*new*", // parser
            "", // style
            "", // value
            "", // validator
            "", // formattedValue
            "textMatch(\"*new*\",cellParser())"
        );
    }

    @Test
    public void testQueryStyleAndStyle() {
        this.queryAndCheck(
            "textMatch(\"*old*\",cellStyle())", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "*new*", // style
            "", // value
            "", // validator
            "", // formattedValue
            "textMatch(\"*new*\",cellStyle())"
        );
    }

    @Test
    public void testQueryValueAndValue() {
        this.queryAndCheck(
            "cellValue()<111", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "", // style
            ">222", // value
            "", // validator
            "", // formattedValue
            "cellValue()>222"
        );
    }

    @Test
    public void testQueryValueAndValue2() {
        this.queryAndCheck(
            "cellValue()<111", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "", // style
            "<333", // value
            "", // validator
            "", // formattedValue
            "cellValue()<333"
        );
    }

    @Test
    public void testQueryFormattedValueAndFormattedValue() {
        this.queryAndCheck(
            "textMatch(\"*old*\",cellFormattedValue())", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "", // style
            "", // value
            "", // validator
            "*new*", // formattedValue
            "textMatch(\"*new*\",cellFormattedValue())"
        );
    }

    // existing query partly replaced with cellXXX getters..............................................................

    @Test
    public void testQueryWithQueryFormulaAndFormula() {
        this.queryAndCheck(
            "1+textMatch(\"*old*\",cellFormula())", // query
            "*new*", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "", // style
            "", // value
            "", // validator
            "", // formattedValue
            "1+textMatch(\"*new*\",cellFormula())"
        );
    }

    @Test
    public void testQueryWithQueryFormatterAndFormatter() {
        this.queryAndCheck(
            "1+textMatch(\"*old*\",cellFormatter())", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "*new*", // formatter
            "", // parser
            "", // style
            "", // value
            "", // validator
            "", // formattedValue
            "1+textMatch(\"*new*\",cellFormatter())"
        );
    }

    @Test
    public void testQueryWithQueryParserAndParser() {
        this.queryAndCheck(
            "1+textMatch(\"*old*\",cellParser())", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "*new*", // parser
            "", // style
            "", // value
            "", // validator
            "", // formattedValue
            "1+textMatch(\"*new*\",cellParser())"
        );
    }

    @Test
    public void testQueryWithQueryStyleAndStyle() {
        this.queryAndCheck(
            "1+textMatch(\"*old*\",cellStyle())", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "*new*", // style
            "", // value
            "", // validator
            "", // formattedValue
            "1+textMatch(\"*new*\",cellStyle())"
        );
    }

    @Test
    public void testQueryWithQueryValueAndValue() {
        this.queryAndCheck(
            "cellValue()<111", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "", // style
            "<333", // value
            "", // validator
            "", // formattedValue
            "cellValue()<333"
        );
    }

    @Test
    public void testQueryWithQueryValueAndValue2() {
        this.queryAndCheck(
            "999+(cellValue()<111)", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "", // style
            "<333", // value
            "", // validator
            "", // formattedValue
            "999+(cellValue()<333)"
        );
    }

    @Test
    public void testQueryWithQueryValueAndValue3() {
        this.queryAndCheck(
            "999+(cellValue()<(111+222))", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "", // style
            "<333", // value
            "", // validator
            "", // formattedValue
            "999+(cellValue()<333)"
        );
    }

    @Test
    public void testQueryWithQueryFormattedValueAndFormattedValue() {
        this.queryAndCheck(
            "1+textMatch(\"*old*\",cellFormattedValue())", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "", // style
            "", // value
            "", // validator
            "*new*", // formattedValue
            "1+textMatch(\"*new*\",cellFormattedValue())"
        );
    }

    @Test
    public void testQueryWithQueryFormattedValueAndFormattedValue2() {
        this.queryAndCheck(
            "1+22+textMatch(\"*old*\",cellFormattedValue())", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "", // style
            "", // value
            "", // validator
            "*new*", // formattedValue
            "1+22+textMatch(\"*new*\",cellFormattedValue())"
        );
    }

    // empty query and two wizard fields................................................................................

    @Test
    public void testQueryWithEmptyAndFormulaFormatter() {
        this.queryAndCheck(
            "", // query
            "*formula*", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "*formatter*", // formatter
            "", // parser
            "", // style
            "", // value
            "", // validator
            "", // formattedValue
            "or(textMatch(\"*formula*\",cellFormula()),textMatch(\"*formatter*\",cellFormatter()))"
        );
    }

    @Test
    public void testQueryWithEmptyAndFormulaFormatterParser() {
        this.queryAndCheck(
            "", // query
            "*formula*", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "*formatter*", // formatter
            "*parser*", // parser
            "", // style
            "", // value
            "", // validator
            "", // formattedValue
            "or(textMatch(\"*formula*\",cellFormula()),or(textMatch(\"*formatter*\",cellFormatter()),textMatch(\"*parser*\",cellParser())))"
        );
    }

    @Test
    public void testQueryWithEmptyAndParserStyle() {
        this.queryAndCheck(
            "", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "*parser*", // parser
            "*style*", // style
            "", // value
            "", // validator
            "", // formattedValue
            "or(textMatch(\"*parser*\",cellParser()),textMatch(\"*style*\",cellStyle()))"
        );
    }

    @Test
    public void testQueryWithEmptyAndValueFormattedValue() {
        this.queryAndCheck(
            "", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "", // style
            "<10", // value
            "", // validator
            "*formattedValue*", // formattedValue
            "or(cellValue()<10,textMatch(\"*formattedValue*\",cellFormattedValue()))"
        );
    }

    @Test
    public void testQueryWithEmptyAndAll() {
        this.queryAndCheck(
            "", // query
            "*formula*", // formula
            "*dateTimeSymbols*", // dateTimeSymbols
            "", // decimalNumberSymbols
            "*formatted*", // formatter
            "*parser*", // parser
            "*style*", // style
            "<10", // value
            "*validator*", // validator
            "*formattedValue*", // formattedValue
            "or(textMatch(\"*formula*\",cellFormula()),or(textMatch(\"*dateTimeSymbols*\",cellDateTimeSymbols()),or(textMatch(\"*formatted*\",cellFormatter()),or(textMatch(\"*parser*\",cellParser()),or(textMatch(\"*style*\",cellStyle()),or(cellValue()<10,or(textMatch(\"*validator*\",cellValidator()),textMatch(\"*formattedValue*\",cellFormattedValue()))))))))"
        );
    }

    // non empty query and two wizard fields............................................................................

    @Test
    public void testQueryWithNotEmptyAndFormulaAndDateTimeSymbols() {
        this.queryAndCheck(
            "oldQuery()", // query
            "*formula*", // formula
            "MONDAY", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "", // style
            "", // value
            "", // validator
            "", // formattedValue
            "or(oldQuery(),or(textMatch(\"*formula*\",cellFormula()),textMatch(\"MONDAY\",cellDateTimeSymbols())))"
        );
    }

    @Test
    public void testQueryWithNotEmptyAndFormulaFormatter() {
        this.queryAndCheck(
            "oldQuery()", // query
            "*formula*", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "*formatter*", // formatter
            "", // parser
            "", // style
            "", // value
            "", // validator
            "", // formattedValue
            "or(oldQuery(),or(textMatch(\"*formula*\",cellFormula()),textMatch(\"*formatter*\",cellFormatter())))"
        );
    }

    @Test
    public void testQueryWithNotEmptyAndFormulaFormatterParser() {
        this.queryAndCheck(
            "oldQuery()", // query
            "*formula*", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "*formatter*", // formatter
            "*parser*", // parser
            "", // style
            "", // value
            "", // validator
            "", // formattedValue
            "or(oldQuery(),or(textMatch(\"*formula*\",cellFormula()),or(textMatch(\"*formatter*\",cellFormatter()),textMatch(\"*parser*\",cellParser()))))"
        );
    }

    @Test
    public void testQueryWithNotEmptyAndParserStyle() {
        this.queryAndCheck(
            "oldQuery()", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "*parser*", // parser
            "*style*", // style
            "", // value
            "", // validator
            "", // formattedValue
            "or(oldQuery(),or(textMatch(\"*parser*\",cellParser()),textMatch(\"*style*\",cellStyle())))"
        );
    }

    @Test
    public void testQueryWithNotEmptyAndValidator() {
        this.queryAndCheck(
            "oldQuery()", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "", // style
            "", // value
            "*validator123*", // validator
            "", // formattedValue
            "or(oldQuery(),textMatch(\"*validator123*\",cellValidator()))"
        );
    }

    @Test
    public void testQueryWithNotEmptyAndValueFormattedValue() {
        this.queryAndCheck(
            "oldQuery()", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "", // style
            "<10", // value
            "", // validator
            "*formattedValue*", // formattedValue
            "or(oldQuery(),or(cellValue()<10,textMatch(\"*formattedValue*\",cellFormattedValue())))"
        );
    }

    @Test
    public void testQueryWithNotEmptyAndAll() {
        this.queryAndCheck(
            "oldQuery()", // query
            "*formula*", // formula
            "*dateTimeSymbols*", // dateTimeSymbols
            "decimalNumberSymbols", // decimalNumberSymbols
            "*formatted*", // formatter
            "*parser*", // parser
            "*style*", // style
            "<10", // value
            "*validator*", // validator
            "*formattedValue*", // formattedValue
            "or(oldQuery(),or(textMatch(\"*formula*\",cellFormula()),or(textMatch(\"*dateTimeSymbols*\",cellDateTimeSymbols()),or(textMatch(\"decimalNumberSymbols\",cellDecimalNumberSymbols()),or(textMatch(\"*formatted*\",cellFormatter()),or(textMatch(\"*parser*\",cellParser()),or(textMatch(\"*style*\",cellStyle()),or(cellValue()<10,or(textMatch(\"*validator*\",cellValidator()),textMatch(\"*formattedValue*\",cellFormattedValue()))))))))))"
        );
    }

    // components removed...............................................................................................

    @Test
    public void testQueryWithFormulaRemoved() {
        this.queryAndCheck(
            "textMatch(\"*formula*\",cellFormula())", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "", // style
            "", // value
            "", // validator
            "", // formattedValue
            ""
        );
    }

    @Test
    public void testQueryWithFormulaDateTimeSymbolsRemoved() {
        this.queryAndCheck(
            "or(textMatch(\"*formula*\",cellFormula()),textMatch(\"*dateTimeSymbols*\",cellDateTimeSymbols()))", // query
            "*formula*", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "", // style
            "", // value
            "", // validator
            "", // formattedValue
            "textMatch(\"*formula*\",cellFormula())"
        );
    }

    @Test
    public void testQueryWithFormulaDecimalNumberSymbolsRemoved() {
        this.queryAndCheck(
            "or(textMatch(\"*formula*\",cellFormula()),textMatch(\"*decimalNumberSymbols*\",cellDecimalNumberSymbols()))", // query
            "*formula*", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "", // style
            "", // value
            "", // validator
            "", // formattedValue
            "textMatch(\"*formula*\",cellFormula())"
        );
    }

    @Test
    public void testQueryWithFormatterRemoved() {
        this.queryAndCheck(
            "textMatch(\"*formatter*\",cellFormatter())", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "", // style
            "", // value
            "", // validator
            "", // formattedValue
            ""
        );
    }

    @Test
    public void testQueryWithFormulaFormatterRemoved() {
        this.queryAndCheck(
            "or(textMatch(\"*formula*\",cellFormula()),textMatch(\"*formatter*\",cellFormatter()))", // query
            "*formula*", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "", // parser
            "", // style
            "", // value
            "", // validator
            "", // formattedValue
            "textMatch(\"*formula*\",cellFormula())" // query
        );
    }

    @Test
    public void testQueryWithFormulaRemovedFormatter() {
        this.queryAndCheck(
            "or(textMatch(\"*formula*\",cellFormula()),textMatch(\"*formatter*\",cellFormatter()))", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "*formatter*", // formatter
            "", // parser
            "", // style
            "", // value
            "", // validator
            "", // formattedValue
            "textMatch(\"*formatter*\",cellFormatter())" // query
        );
    }

    @Test
    public void testQueryWithFormulaFormatterRemovedParser() {
        this.queryAndCheck(
            "or(textMatch(\"*formula*\",cellFormula()),or(textMatch(\"*formatter*\",cellFormatter()),textMatch(\"*parser*\",cellParser())))", // query
            "*formula*", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "", // formatter
            "*parser*", // parser
            "", // style
            "", // value
            "", // validator
            "", // formattedValue
            "or(textMatch(\"*formula*\",cellFormula()),textMatch(\"*parser*\",cellParser()))" // query
        );
    }

    @Test
    public void testQueryWithFormulaRemovedFormatterParser() {
        this.queryAndCheck(
            "or(or(textMatch(\"*formula*\",cellFormula()),textMatch(\"*formatter*\",cellFormatter())),textMatch(\"*parser*\",cellParser()))", // query
            "", // formula
            "", // dateTimeSymbols
            "", // decimalNumberSymbols
            "*formatter*", // formatter
            "*parser*", // parser
            "", // style
            "", // value
            "", // validator
            "", // formattedValue
            "or(textMatch(\"*formatter*\",cellFormatter()),textMatch(\"*parser*\",cellParser()))" // query
        );
    }

    private void queryAndCheck(final String query,
                               final String formula,
                               final String dateTimeSymbols,
                               final String decimalNumberSymbols,
                               final String formatter,
                               final String parser,
                               final String style,
                               final String value,
                               final String validator,
                               final String formattedValue,
                               final String expected) {
        this.queryAndCheck(
            Optional.ofNullable(
                CharSequences.isNullOrEmpty(query) ?
                    null :
                    SpreadsheetCellQuery.parse(query)
            ), // query
            textMatch(formula),
            textMatch(dateTimeSymbols),
            textMatch(decimalNumberSymbols),
            textMatch(formatter),
            textMatch(parser),
            textMatch(style),
            Optional.ofNullable(
                CharSequences.isNullOrEmpty(value) ?
                    null :
                    SpreadsheetFormulaParsers.conditionRight(
                        SpreadsheetFormulaParsers.expression()
                    ).parseText(
                        value,
                        SPREADSHEET_PARSER_CONTEXT
                    ).cast(ConditionRightSpreadsheetFormulaParserToken.class)
            ),
            textMatch(validator),
            textMatch(formattedValue),
            expected
        );
    }

    private static Optional<TextMatch> textMatch(final String textMatch) {
        return Optional.ofNullable(
            CharSequences.isNullOrEmpty(textMatch) ?
                null :
                TextMatch.parse(textMatch)
        );
    }

    private void queryAndCheck(final Optional<SpreadsheetCellQuery> query,
                               final Optional<TextMatch> formula,
                               final Optional<TextMatch> dateTimeSymbols,
                               final Optional<TextMatch> decimalNumberSymbols,
                               final Optional<TextMatch> formatter,
                               final Optional<TextMatch> parser,
                               final Optional<TextMatch> style,
                               final Optional<ConditionRightSpreadsheetFormulaParserToken> value,
                               final Optional<TextMatch> validator,
                               final Optional<TextMatch> formattedValue,
                               final String expected) {
        this.checkEquals(
            parseFormula(expected),
            parseFormula(
                SpreadsheetFindDialogComponentQuery.query(
                    query,
                    formula,
                    dateTimeSymbols,
                    decimalNumberSymbols,
                    formatter,
                    parser,
                    style,
                    value,
                    validator,
                    formattedValue
                )
            )
        );
    }

    private Optional<SpreadsheetFormula> parseFormula(final Optional<SpreadsheetFormula> formula) {
        return formula.map(
            f -> parseFormula0(f.text())
        ).filter(CanBeEmpty::isNotEmpty);
    }

    private Optional<SpreadsheetFormula> parseFormula(final String text) {
        return parseFormula(
            Optional.of(
                parseFormula0(text)
            )
        );
    }

    private SpreadsheetFormula parseFormula0(final String text) {
        return CharSequences.isNullOrEmpty(text) ?
            SpreadsheetFormula.EMPTY :
            SpreadsheetFormula.parse(
                TextCursors.charSequence(text),
                SpreadsheetFormulaParsers.expression(),
                SPREADSHEET_PARSER_CONTEXT
            );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetFindDialogComponentQuery> type() {
        return SpreadsheetFindDialogComponentQuery.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public boolean canHavePublicTypes(Method method) {
        return false;
    }
}
