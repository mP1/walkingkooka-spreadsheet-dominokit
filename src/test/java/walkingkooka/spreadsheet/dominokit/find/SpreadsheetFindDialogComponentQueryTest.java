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
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.PublicStaticHelperTesting;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.engine.SpreadsheetCellQuery;
import walkingkooka.spreadsheet.expression.function.TextMatch;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.parser.ConditionRightSpreadsheetParserToken;
import walkingkooka.spreadsheet.parser.SpreadsheetParsers;
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
            "", // formatter
            "", // parser
            "", // style
            "", // value
            "", // formattedValue
            ""
        );
    }

    @Test
    public void testQueryWithEmptyAndFormula() {
        this.queryAndCheck(
            "", // query
            "*formula*", // formula
            "", // formatter
            "", // parser
            "", // style
            "", // value
            "", // formattedValue
            "textMatch(\"*formula*\",cellFormula())"
        );
    }

    @Test
    public void testQueryWithEmptyAndFormatter() {
        this.queryAndCheck(
            "", // query
            "", // formula
            "*formatter*", // formatter
            "", // parser
            "", // style
            "", // value
            "", // formattedValue
            "textMatch(\"*formatter*\",cellFormatter())"
        );
    }

    @Test
    public void testQueryWithEmptyAndParser() {
        this.queryAndCheck(
            "", // query
            "", // formula
            "", // formatter
            "*parser*", // parser
            "", // style
            "", // value
            "", // formattedValue
            "textMatch(\"*parser*\",cellParser())"
        );
    }

    @Test
    public void testQueryWithEmptyAndStyle() {
        this.queryAndCheck(
            "", // query
            "", // formula
            "", // formatter
            "", // parser
            "*style*", // style
            "", // value
            "", // formattedValue
            "textMatch(\"*style*\",cellStyle())"
        );
    }

    @Test
    public void testQueryWithEmptyAndValue() {
        this.queryAndCheck(
            "", // query
            "", // formula
            "", // formatter
            "", // parser
            "", // style
            "<111", // value
            "", // formattedValue
            "cellValue()<111"
        );
    }

    @Test
    public void testQueryWithEmptyAndFormattedValue() {
        this.queryAndCheck(
            "", // query
            "", // formula
            "", // formatter
            "", // parser
            "", // style
            "", // value
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
            "", // formatter
            "", // parser
            "", // style
            "", // value
            "", // formattedValue
            "oldQuery()"
        );
    }

    @Test
    public void testQueryWithNotEmptyAndFormula() {
        this.queryAndCheck(
            "oldQuery()", // query
            "*formula*", // formula
            "", // formatter
            "", // parser
            "", // style
            "", // value
            "", // formattedValue
            "OR(oldQuery(),textMatch(\"*formula*\",cellFormula()))"
        );
    }

    @Test
    public void testQueryWithNotEmptyAndFormatter() {
        this.queryAndCheck(
            "oldQuery()", // query
            "", // formula
            "*formatter*", // formatter
            "", // parser
            "", // style
            "", // value
            "", // formattedValue
            "OR(oldQuery(),textMatch(\"*formatter*\",cellFormatter()))"
        );
    }

    @Test
    public void testQueryWithNotEmptyAndParser() {
        this.queryAndCheck(
            "oldQuery()", // query
            "", // formula
            "", // formatter
            "*parser*", // parser
            "", // style
            "", // value
            "", // formattedValue
            "OR(oldQuery(),textMatch(\"*parser*\",cellParser()))"
        );
    }

    @Test
    public void testQueryWithNotEmptyAndStyle() {
        this.queryAndCheck(
            "oldQuery()", // query
            "", // formula
            "", // formatter
            "", // parser
            "*style*", // style
            "", // value
            "", // formattedValue
            "OR(oldQuery(),textMatch(\"*style*\",cellStyle()))"
        );
    }

    @Test
    public void testQueryWithNotEmptyAndValue() {
        this.queryAndCheck(
            "oldQuery()", // query
            "", // formula
            "", // formatter
            "", // parser
            "", // style
            "<111", // value
            "", // formattedValue
            "OR(oldQuery(),cellValue()<111)"
        );
    }

    @Test
    public void testQueryWithNotEmptyAndFormattedValue() {
        this.queryAndCheck(
            "oldQuery()", // query
            "", // formula
            "", // formatter
            "", // parser
            "", // style
            "", // value
            "*formattedValue*", // formattedValue
            "OR(oldQuery(),textMatch(\"*formattedValue*\",cellFormattedValue()))"
        );
    }

    // existing query replaced with cellXXX getters.....................................................................

    @Test
    public void testQueryFormulaAndFormula() {
        this.queryAndCheck(
            "textMatch(\"*old*\",cellFormula())", // query
            "*new*", // formula
            "", // formatter
            "", // parser
            "", // style
            "", // value
            "", // formattedValue
            "textMatch(\"*new*\",cellFormula())"
        );
    }

    @Test
    public void testQueryFormatterAndFormatter() {
        this.queryAndCheck(
            "textMatch(\"*old*\",cellFormatter())", // query
            "", // formula
            "*new*", // formatter
            "", // parser
            "", // style
            "", // value
            "", // formattedValue
            "textMatch(\"*new*\",cellFormatter())"
        );
    }

    @Test
    public void testQueryParserAndParser() {
        this.queryAndCheck(
            "textMatch(\"*old*\",cellParser())", // query
            "", // formula
            "", // formatter
            "*new*", // parser
            "", // style
            "", // value
            "", // formattedValue
            "textMatch(\"*new*\",cellParser())"
        );
    }

    @Test
    public void testQueryStyleAndStyle() {
        this.queryAndCheck(
            "textMatch(\"*old*\",cellStyle())", // query
            "", // formula
            "", // formatter
            "", // parser
            "*new*", // style
            "", // value
            "", // formattedValue
            "textMatch(\"*new*\",cellStyle())"
        );
    }

    @Test
    public void testQueryValueAndValue() {
        this.queryAndCheck(
            "cellValue()<111", // query
            "", // formula
            "", // formatter
            "", // parser
            "", // style
            ">222", // value
            "", // formattedValue
            "cellValue()>222"
        );
    }

    @Test
    public void testQueryValueAndValue2() {
        this.queryAndCheck(
            "cellValue()<111", // query
            "", // formula
            "", // formatter
            "", // parser
            "", // style
            "<333", // value
            "", // formattedValue
            "cellValue()<333"
        );
    }

    @Test
    public void testQueryFormattedValueAndFormattedValue() {
        this.queryAndCheck(
            "textMatch(\"*old*\",cellFormattedValue())", // query
            "", // formula
            "", // formatter
            "", // parser
            "", // style
            "", // value
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
            "", // formatter
            "", // parser
            "", // style
            "", // value
            "", // formattedValue
            "1+textMatch(\"*new*\",cellFormula())"
        );
    }

    @Test
    public void testQueryWithQueryFormatterAndFormatter() {
        this.queryAndCheck(
            "1+textMatch(\"*old*\",cellFormatter())", // query
            "", // formula
            "*new*", // formatter
            "", // parser
            "", // style
            "", // value
            "", // formattedValue
            "1+textMatch(\"*new*\",cellFormatter())"
        );
    }

    @Test
    public void testQueryWithQueryParserAndParser() {
        this.queryAndCheck(
            "1+textMatch(\"*old*\",cellParser())", // query
            "", // formula
            "", // formatter
            "*new*", // parser
            "", // style
            "", // value
            "", // formattedValue
            "1+textMatch(\"*new*\",cellParser())"
        );
    }

    @Test
    public void testQueryWithQueryStyleAndStyle() {
        this.queryAndCheck(
            "1+textMatch(\"*old*\",cellStyle())", // query
            "", // formula
            "", // formatter
            "", // parser
            "*new*", // style
            "", // value
            "", // formattedValue
            "1+textMatch(\"*new*\",cellStyle())"
        );
    }

    @Test
    public void testQueryWithQueryValueAndValue() {
        this.queryAndCheck(
            "cellValue()<111", // query
            "", // formula
            "", // formatter
            "", // parser
            "", // style
            "<333", // value
            "", // formattedValue
            "cellValue()<333"
        );
    }

    @Test
    public void testQueryWithQueryValueAndValue2() {
        this.queryAndCheck(
            "999+(cellValue()<111)", // query
            "", // formula
            "", // formatter
            "", // parser
            "", // style
            "<333", // value
            "", // formattedValue
            "999+(cellValue()<333)"
        );
    }

    @Test
    public void testQueryWithQueryValueAndValue3() {
        this.queryAndCheck(
            "999+(cellValue()<(111+222))", // query
            "", // formula
            "", // formatter
            "", // parser
            "", // style
            "<333", // value
            "", // formattedValue
            "999+(cellValue()<333)"
        );
    }

    @Test
    public void testQueryWithQueryFormattedValueAndFormattedValue() {
        this.queryAndCheck(
            "1+textMatch(\"*old*\",cellFormattedValue())", // query
            "", // formula
            "", // formatter
            "", // parser
            "", // style
            "", // value
            "*new*", // formattedValue
            "1+textMatch(\"*new*\",cellFormattedValue())"
        );
    }

    @Test
    public void testQueryWithQueryFormattedValueAndFormattedValue2() {
        this.queryAndCheck(
            "1+22+textMatch(\"*old*\",cellFormattedValue())", // query
            "", // formula
            "", // formatter
            "", // parser
            "", // style
            "", // value
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
            "*formatter*", // formatter
            "", // parser
            "", // style
            "", // value
            "", // formattedValue
            "OR(textMatch(\"*formula*\",cellFormula()),textMatch(\"*formatter*\",cellFormatter()))"
        );
    }

    @Test
    public void testQueryWithEmptyAndFormulaFormatterParser() {
        this.queryAndCheck(
            "", // query
            "*formula*", // formula
            "*formatter*", // formatter
            "*parser*", // parser
            "", // style
            "", // value
            "", // formattedValue
            "OR(textMatch(\"*formula*\",cellFormula()),OR(textMatch(\"*formatter*\",cellFormatter()),textMatch(\"*parser*\",cellParser())))"
        );
    }

    @Test
    public void testQueryWithEmptyAndParserStyle() {
        this.queryAndCheck(
            "", // query
            "", // formula
            "", // formatter
            "*parser*", // parser
            "*style*", // style
            "", // value
            "", // formattedValue
            "OR(textMatch(\"*parser*\",cellParser()),textMatch(\"*style*\",cellStyle()))"
        );
    }

    @Test
    public void testQueryWithEmptyAndValueFormattedValue() {
        this.queryAndCheck(
            "", // query
            "", // formula
            "", // formatter
            "", // parser
            "", // style
            "<10", // value
            "*formattedValue*", // formattedValue
            "OR(cellValue()<10,textMatch(\"*formattedValue*\",cellFormattedValue()))"
        );
    }

    @Test
    public void testQueryWithEmptyAndAll() {
        this.queryAndCheck(
            "", // query
            "*formula*", // formula
            "*formatted*", // formatter
            "*parser*", // parser
            "*style*", // style
            "<10", // value
            "*formattedValue*", // formattedValue
            "OR(textMatch(\"*formula*\",cellFormula()),OR(textMatch(\"*formatted*\",cellFormatter()),OR(textMatch(\"*parser*\",cellParser()),OR(textMatch(\"*style*\",cellStyle()),OR(cellValue()<10,textMatch(\"*formattedValue*\",cellFormattedValue()))))))"
        );
    }

    // non empty query and two wizard fields............................................................................

    @Test
    public void testQueryWithNotEmptyAndFormulaFormatter() {
        this.queryAndCheck(
            "oldQuery()", // query
            "*formula*", // formula
            "*formatter*", // formatter
            "", // parser
            "", // style
            "", // value
            "", // formattedValue
            "OR(oldQuery(),OR(textMatch(\"*formula*\",cellFormula()),textMatch(\"*formatter*\",cellFormatter())))"
        );
    }

    @Test
    public void testQueryWithNotEmptyAndFormulaFormatterParser() {
        this.queryAndCheck(
            "oldQuery()", // query
            "*formula*", // formula
            "*formatter*", // formatter
            "*parser*", // parser
            "", // style
            "", // value
            "", // formattedValue
            "OR(oldQuery(),OR(textMatch(\"*formula*\",cellFormula()),OR(textMatch(\"*formatter*\",cellFormatter()),textMatch(\"*parser*\",cellParser()))))"
        );
    }

    @Test
    public void testQueryWithNotEmptyAndParserStyle() {
        this.queryAndCheck(
            "oldQuery()", // query
            "", // formula
            "", // formatter
            "*parser*", // parser
            "*style*", // style
            "", // value
            "", // formattedValue
            "OR(oldQuery(),OR(textMatch(\"*parser*\",cellParser()),textMatch(\"*style*\",cellStyle())))"
        );
    }

    @Test
    public void testQueryWithNotEmptyAndValueFormattedValue() {
        this.queryAndCheck(
            "oldQuery()", // query
            "", // formula
            "", // formatter
            "", // parser
            "", // style
            "<10", // value
            "*formattedValue*", // formattedValue
            "OR(oldQuery(),OR(cellValue()<10,textMatch(\"*formattedValue*\",cellFormattedValue())))"
        );
    }

    @Test
    public void testQueryWithNotEmptyAndAll() {
        this.queryAndCheck(
            "oldQuery()", // query
            "*formula*", // formula
            "*formatted*", // formatter
            "*parser*", // parser
            "*style*", // style
            "<10", // value
            "*formattedValue*", // formattedValue
            "OR(oldQuery(),OR(textMatch(\"*formula*\",cellFormula()),OR(textMatch(\"*formatted*\",cellFormatter()),OR(textMatch(\"*parser*\",cellParser()),OR(textMatch(\"*style*\",cellStyle()),OR(cellValue()<10,textMatch(\"*formattedValue*\",cellFormattedValue())))))))"
        );
    }

    private void queryAndCheck(final String query,
                               final String formula,
                               final String formatter,
                               final String parser,
                               final String style,
                               final String value,
                               final String formattedValue,
                               final String expected) {
        this.queryAndCheck(
            Optional.ofNullable(
                CharSequences.isNullOrEmpty(query) ?
                    null :
                    SpreadsheetCellQuery.parse(query)
            ), // query
            textMatch(formula),
            textMatch(formatter),
            textMatch(parser),
            textMatch(style),
            Optional.ofNullable(
                CharSequences.isNullOrEmpty(value) ?
                    null :
                    SpreadsheetParsers.conditionRight(
                        SpreadsheetParsers.expression()
                    ).parseText(
                        value,
                        SPREADSHEET_PARSER_CONTEXT
                    ).cast(ConditionRightSpreadsheetParserToken.class)
            ),
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
                               final Optional<TextMatch> formatter,
                               final Optional<TextMatch> parser,
                               final Optional<TextMatch> style,
                               final Optional<ConditionRightSpreadsheetParserToken> value,
                               final Optional<TextMatch> formattedValue,
                               final String expected) {
        this.checkEquals(
            parseFormula(expected),
            parseFormula(
                SpreadsheetFindDialogComponentQuery.query(
                    query,
                    formula,
                    formatter,
                    parser,
                    style,
                    value,
                    formattedValue
                )
            )
        );
    }

    private Optional<SpreadsheetFormula> parseFormula(final Optional<SpreadsheetFormula> formula) {
        return Optional.ofNullable(
            formula.map(
                    f -> parseFormula0(f.text())
                ).filter(f -> f.isNotEmpty())
                .orElse(null)
        );
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
                SpreadsheetParsers.expression(),
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
