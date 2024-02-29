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
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.HasUrlFragmentTesting;
import walkingkooka.net.UrlFragment;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellClipboardValueSelectorTest implements ClassTesting<SpreadsheetCellClipboardValueSelector>,
        HasUrlFragmentTesting,
        ParseStringTesting<SpreadsheetCellClipboardValueSelector> {
    // checkValue.......................................................................................................

    @Test
    public void testCheckValueCellNonSetFails() {
        this.checkValueFails(
                SpreadsheetCellClipboardValueSelector.CELL,
                "Not-a-set-of-cells",
                "Expected a Set of cell but got a String"
        );
    }

    @Test
    public void testCheckValueCellSetElementNotCellFails() {
        this.checkValueFails(
                SpreadsheetCellClipboardValueSelector.CELL,
                Sets.of(
                        "Not-a-cells"
                ),
                "Set should only have cells but got String"
        );
    }

    @Test
    public void testCheckValueCellSetOfCells() {
        SpreadsheetCellClipboardValueSelector.CELL.checkValue(
                Sets.of(
                        SpreadsheetSelection.A1.setFormula(
                                SpreadsheetFormula.EMPTY
                        )
                )
        );
    }

    @Test
    public void testCheckValueFormulaNonMapFails() {
        this.checkValueFails(
                SpreadsheetCellClipboardValueSelector.FORMULA,
                123,
                "Expected a Map of cell-references to String but got Integer"
        );
    }

    @Test
    public void testCheckValueFormulaMapValueNotCellReferenceFails() {
        this.checkValueFails(
                SpreadsheetCellClipboardValueSelector.FORMULA,
                Maps.of(
                        SpreadsheetSelection.parseCellRange("A1:B2"),
                        "formula-text-123"
                ),
                "Map should only have keys of type cell but got SpreadsheetCellRange"
        );
    }

    @Test
    public void testCheckValueFormulaMapValueNotStringFails() {
        this.checkValueFails(
                SpreadsheetCellClipboardValueSelector.FORMULA,
                Maps.of(
                        SpreadsheetSelection.A1,
                        123
                ),
                "Map should only have values of type String but got Integer"
        );
    }

    @Test
    public void testCheckValueFormulaMapOfStrings() {
        SpreadsheetCellClipboardValueSelector.FORMULA.checkValue(
                Maps.of(
                        SpreadsheetSelection.A1,
                        "this-is-a-formula"
                )
        );
    }

    @Test
    public void testCheckValueFormatPatternNonMapFails() {
        this.checkValueFails(
                SpreadsheetCellClipboardValueSelector.FORMAT_PATTERN,
                123,
                "Expected a Map of cell-references to SpreadsheetFormatPattern but got Integer"
        );
    }

    @Test
    public void testCheckValueFormatPatternMapValueNotCellReferenceFails() {
        this.checkValueFails(
                SpreadsheetCellClipboardValueSelector.FORMAT_PATTERN,
                Maps.of(
                        SpreadsheetSelection.parseCellRange("A1:B2"),
                        SpreadsheetPattern.parseNumberFormatPattern("$0.00")
                ),
                "Map should only have keys of type cell but got SpreadsheetCellRange"
        );
    }

    @Test
    public void testCheckValueFormatPatternMapValueNotFormatPatternFails() {
        this.checkValueFails(
                SpreadsheetCellClipboardValueSelector.FORMAT_PATTERN,
                Maps.of(
                        SpreadsheetSelection.A1,
                        123
                ),
                "Map should only have values of type SpreadsheetFormatPattern but got Integer"
        );
    }

    @Test
    public void testCheckValueFormatPatternMapCellToFormatPattern() {
        SpreadsheetCellClipboardValueSelector.FORMAT_PATTERN.checkValue(
                Maps.of(
                        SpreadsheetSelection.A1,
                        SpreadsheetPattern.parseNumberFormatPattern("$0.00")
                )
        );
    }

    @Test
    public void testCheckValueParsePatternNonMapFails() {
        this.checkValueFails(
                SpreadsheetCellClipboardValueSelector.PARSE_PATTERN,
                123,
                "Expected a Map of cell-references to SpreadsheetParsePattern but got Integer"
        );
    }

    @Test
    public void testCheckValueParsePatternMapValueNotCellReferenceFails() {
        this.checkValueFails(
                SpreadsheetCellClipboardValueSelector.PARSE_PATTERN,
                Maps.of(
                        SpreadsheetSelection.parseCellRange("A1:B2"),
                        SpreadsheetPattern.parseNumberParsePattern("$0.00")
                ),
                "Map should only have keys of type cell but got SpreadsheetCellRange"
        );
    }

    @Test
    public void testCheckValueParsePatternMapValueNotParsePatternFails() {
        this.checkValueFails(
                SpreadsheetCellClipboardValueSelector.PARSE_PATTERN,
                Maps.of(
                        SpreadsheetSelection.A1,
                        123
                ),
                "Map should only have values of type SpreadsheetParsePattern but got Integer"
        );
    }

    @Test
    public void testCheckValueParsePatternMapCellToParsePattern() {
        SpreadsheetCellClipboardValueSelector.PARSE_PATTERN.checkValue(
                Maps.of(
                        SpreadsheetSelection.A1,
                        SpreadsheetPattern.parseNumberParsePattern("$0.00")
                )
        );
    }


    @Test
    public void testCheckValueStyleNonMapFails() {
        this.checkValueFails(
                SpreadsheetCellClipboardValueSelector.STYLE,
                123,
                "Expected a Map of cell-references to TextStyle but got Integer"
        );
    }

    @Test
    public void testCheckValueStyleMapValueNotCellReferenceFails() {
        this.checkValueFails(
                SpreadsheetCellClipboardValueSelector.STYLE,
                Maps.of(
                        SpreadsheetSelection.parseCellRange("A1:B2"),
                        TextStyle.EMPTY
                ),
                "Map should only have keys of type cell but got SpreadsheetCellRange"
        );
    }

    @Test
    public void testCheckValueStyleMapValueNotTextStyleFails() {
        this.checkValueFails(
                SpreadsheetCellClipboardValueSelector.STYLE,
                Maps.of(
                        SpreadsheetSelection.A1,
                        123
                ),
                "Map should only have values of type TextStyle but got Integer"
        );
    }

    @Test
    public void testCheckValueStyleMapCellToTextStyle() {
        SpreadsheetCellClipboardValueSelector.STYLE.checkValue(
                Maps.of(
                        SpreadsheetSelection.A1,
                        TextStyle.EMPTY.set(
                                TextStylePropertyName.TEXT_ALIGN,
                                TextAlign.CENTER
                        )
                )
        );
    }

    @Test
    public void testCheckValueFormattedNonMapFails() {
        this.checkValueFails(
                SpreadsheetCellClipboardValueSelector.FORMATTED,
                123,
                "Expected a Map of cell-references to String but got Integer"
        );
    }

    @Test
    public void testCheckValueFormattedMapValueNotCellReferenceFails() {
        this.checkValueFails(
                SpreadsheetCellClipboardValueSelector.FORMATTED,
                Maps.of(
                        SpreadsheetSelection.parseCellRange("A1:B2"),
                        "formatted-text-123"
                ),
                "Map should only have keys of type cell but got SpreadsheetCellRange"
        );
    }

    @Test
    public void testCheckValueFormattedMapValueNotStringFails() {
        this.checkValueFails(
                SpreadsheetCellClipboardValueSelector.FORMATTED,
                Maps.of(
                        SpreadsheetSelection.A1,
                        123
                ),
                "Map should only have values of type String but got Integer"
        );
    }

    @Test
    public void testCheckValueFormattedMapOfStrings() {
        SpreadsheetCellClipboardValueSelector.FORMATTED.checkValue(
                Maps.of(
                        SpreadsheetSelection.A1,
                        "this-is-a-formatted"
                )
        );
    }

    private void checkValueFails(final SpreadsheetCellClipboardValueSelector selector,
                                 final Object value,
                                 final String message) {
        final IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> selector.checkValue(value)
        );

        this.checkEquals(
                message,
                thrown.getMessage(),
                "message"
        );
    }

    // parse............................................................................................................

    @Test
    public void testParseCell() {
        this.parseStringAndCheck(
                "cell",
                SpreadsheetCellClipboardValueSelector.CELL
        );
    }

    @Test
    public void testParseFormula() {
        this.parseStringAndCheck(
                "formula",
                SpreadsheetCellClipboardValueSelector.FORMULA
        );
    }

    @Override //
    public SpreadsheetCellClipboardValueSelector parseString(final String string) {
        return SpreadsheetCellClipboardValueSelector.parse(string);
    }

    @Override
    public Class<? extends RuntimeException> parseStringFailedExpected(final Class<? extends RuntimeException> thrown) {
        return thrown;
    }

    @Override
    public RuntimeException parseStringFailedExpected(final RuntimeException thrown) {
        return thrown;
    }

    // HasUrlFragment...................................................................................................

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck(
                SpreadsheetCellClipboardValueSelector.CELL,
                UrlFragment.with("cell")
        );
    }

    @Test
    public void testUrlFragmentFormula() {
        this.urlFragmentAndCheck(
                SpreadsheetCellClipboardValueSelector.FORMULA,
                UrlFragment.with("formula")
        );
    }

    @Test
    public void testUrlFragmentFormatPattern() {
        this.urlFragmentAndCheck(
                SpreadsheetCellClipboardValueSelector.FORMAT_PATTERN,
                UrlFragment.with("format-pattern")
        );
    }

    @Test
    public void testUrlFragmentStyle() {
        this.urlFragmentAndCheck(
                SpreadsheetCellClipboardValueSelector.STYLE,
                UrlFragment.with("style")
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetCellClipboardValueSelector> type() {
        return SpreadsheetCellClipboardValueSelector.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
