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

package walkingkooka.spreadsheet.dominokit.clipboard;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.color.Color;
import walkingkooka.net.HasUrlFragmentTesting;
import walkingkooka.net.UrlFragment;
import walkingkooka.net.header.HasMediaTypeTesting;
import walkingkooka.net.header.MediaType;
import walkingkooka.predicate.PredicateTesting;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellClipboardValueKindTest implements ClassTesting<SpreadsheetCellClipboardValueKind>,
        HasMediaTypeTesting,
        HasUrlFragmentTesting,
        ParseStringTesting<SpreadsheetCellClipboardValueKind>,
        PredicateTesting {

    private final static SpreadsheetCell CELL = SpreadsheetSelection.A1.setFormula(
            SpreadsheetFormula.EMPTY.setText("=1+2")
    ).setFormatPattern(
            Optional.of(
                    SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN
            )
    ).setParsePattern(
            Optional.of(
                    SpreadsheetPattern.parseNumberParsePattern("$0.00")
            )
    ).setStyle(
            TextStyle.EMPTY.set(
                    TextStylePropertyName.COLOR,
                    Color.BLACK
            )
    ).setFormattedValue(
            Optional.of(
                    TextNode.text("Formatted-value - Hello123")
            )
    );

    // predicate........................................................................................................

    @Test
    public void testPredicateCell() {
        this.predicateAndCheck(
                SpreadsheetCellClipboardValueKind.CELL,
                SpreadsheetCellClipboardValueKind.values()
        );
    }

    @Test
    public void testPredicateFormula() {
        this.predicateAndCheck(
                SpreadsheetCellClipboardValueKind.FORMULA
        );
    }

    @Test
    public void testPredicateFormatPattern() {
        this.predicateAndCheck(
                SpreadsheetCellClipboardValueKind.FORMAT_PATTERN
        );
    }

    @Test
    public void testPredicateParsePattern() {
        this.predicateAndCheck(
                SpreadsheetCellClipboardValueKind.PARSE_PATTERN
        );
    }

    @Test
    public void testPredicateStyle() {
        this.predicateAndCheck(
                SpreadsheetCellClipboardValueKind.STYLE
        );
    }

    @Test
    public void testPredicateFormatted() {
        this.predicateAndCheck(
                SpreadsheetCellClipboardValueKind.FORMATTED_VALUE
        );
    }

    private void predicateAndCheck(final SpreadsheetCellClipboardValueKind kind) {
        this.predicateAndCheck(
                kind,
                kind
        );
    }

    private void predicateAndCheck(final SpreadsheetCellClipboardValueKind kind,
                                   final SpreadsheetCellClipboardValueKind... trueKinds) {
        this.predicateAndCheck(
                kind,
                Sets.of(trueKinds)
        );
    }

    private void predicateAndCheck(final SpreadsheetCellClipboardValueKind kind,
                                   final Set<SpreadsheetCellClipboardValueKind> trueKinds) {
        final Predicate<SpreadsheetCellClipboardValueKind> predicate = kind.predicate();

        for (final SpreadsheetCellClipboardValueKind possible : SpreadsheetCellClipboardValueKind.values()) {
            this.testAndCheck(
                    predicate,
                    possible,
                    trueKinds.contains(possible)
            );
        }
    }

    // toValue..........................................................................................................

    @Test
    public void testToValueCell() {
        this.toValueAndCheck(
                SpreadsheetCellClipboardValueKind.CELL,
                CELL,
                CELL
        );
    }

    @Test
    public void testToValueFormula() {
        this.toValueAndCheck(
                SpreadsheetCellClipboardValueKind.FORMULA,
                CELL,
                CELL.formula()
        );
    }

    @Test
    public void testToValueFormulaEmptyFormula() {
        final SpreadsheetCell cell = SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY);

        this.toValueAndCheck(
                SpreadsheetCellClipboardValueKind.FORMULA,
                cell,
                cell.formula()
        );
    }

    @Test
    public void testToValueFormatPattern() {
        this.toValueAndCheck(
                SpreadsheetCellClipboardValueKind.FORMAT_PATTERN,
                CELL,
                CELL.formatPattern()
        );
    }

    @Test
    public void testToValueFormatPatternEmpty() {
        final SpreadsheetCell cell = CELL.setFormatPattern(SpreadsheetCell.NO_FORMAT_PATTERN);

        this.toValueAndCheck(
                SpreadsheetCellClipboardValueKind.FORMAT_PATTERN,
                cell,
                cell.formatPattern()
        );
    }

    @Test
    public void testToValueParsePattern() {
        this.toValueAndCheck(
                SpreadsheetCellClipboardValueKind.PARSE_PATTERN,
                CELL,
                CELL.parsePattern()
        );
    }

    @Test
    public void testToValueParsePatternEmpty() {
        final SpreadsheetCell cell = CELL.setParsePattern(SpreadsheetCell.NO_PARSE_PATTERN);

        this.toValueAndCheck(
                SpreadsheetCellClipboardValueKind.PARSE_PATTERN,
                cell,
                cell.parsePattern()
        );
    }

    @Test
    public void testToValueStyle() {
        this.toValueAndCheck(
                SpreadsheetCellClipboardValueKind.STYLE,
                CELL,
                CELL.style()
        );
    }

    @Test
    public void testToValueStyleEmpty() {
        final SpreadsheetCell cell = CELL.setStyle(TextStyle.EMPTY);

        this.toValueAndCheck(
                SpreadsheetCellClipboardValueKind.STYLE,
                cell,
                cell.style()
        );
    }

    @Test
    public void testToValueFormattedValue() {
        this.toValueAndCheck(
                SpreadsheetCellClipboardValueKind.FORMATTED_VALUE,
                CELL,
                CELL.formattedValue()
        );
    }

    @Test
    public void testToValueFormattedValueEmpty() {
        final SpreadsheetCell cell = CELL.setFormattedValue(SpreadsheetCell.NO_FORMATTED_VALUE_CELL);

        this.toValueAndCheck(
                SpreadsheetCellClipboardValueKind.FORMATTED_VALUE,
                cell,
                cell.formattedValue()
        );
    }

    private void toValueAndCheck(final SpreadsheetCellClipboardValueKind kind,
                                 final SpreadsheetCell cell,
                                 final Object expected) {
        final Object value = kind.toValue(cell);

        this.checkEquals(
                expected,
                value,
                () -> kind + " toValue " + cell
        );

        final Object valueOrNull = value instanceof Optional ?
                Optional.class.cast(value).orElse(null) :
                value;
        final Class<?> type = kind.mediaTypeClass();
        this.checkEquals(
                null != valueOrNull,
                type.isInstance(valueOrNull),
                () -> kind + " toValue " + CharSequences.quoteIfChars(value) + " is not instanceof " + type.getName()
        );
    }

    // HasMediaType......................................................................................................

    @Test
    public void testMediaTypeCell() {
        this.mediaTypeAndCheck(
                SpreadsheetCellClipboardValueKind.CELL,
                MediaType.APPLICATION_JSON.setSuffixes(
                        List.of(
                                SpreadsheetCell.class.getName()
                        )
                )
        );
    }

    @Test
    public void testMediaTypeFormula() {
        this.mediaTypeAndCheck(
                SpreadsheetCellClipboardValueKind.FORMULA,
                MediaType.APPLICATION_JSON.setSuffixes(
                        List.of(
                                SpreadsheetFormula.class.getName()
                        )
                )
        );
    }

    // mediaTypeClass...................................................................................................

    @Test
    public void testMediaTypeClassCell() {
        this.mediaTypeClassAndCheck(
                SpreadsheetCellClipboardValueKind.CELL,
                SpreadsheetCell.class
        );
    }

    @Test
    public void testMediaTypeClassFormula() {
        this.mediaTypeClassAndCheck(
                SpreadsheetCellClipboardValueKind.FORMULA,
                SpreadsheetFormula.class
        );
    }

    @Test
    public void testMediaTypeClassStyle() {
        this.mediaTypeClassAndCheck(
                SpreadsheetCellClipboardValueKind.STYLE,
                TextStyle.class
        );
    }

    private void mediaTypeClassAndCheck(final SpreadsheetCellClipboardValueKind kind,
                                        final Class<?> type) {
        this.checkEquals(
                type,
                kind.mediaTypeClass()
        );
    }

    // parse............................................................................................................

    @Test
    public void testParseCell() {
        this.parseStringAndCheck(
                "cell",
                SpreadsheetCellClipboardValueKind.CELL
        );
    }

    @Test
    public void testParseFormula() {
        this.parseStringAndCheck(
                "formula",
                SpreadsheetCellClipboardValueKind.FORMULA
        );
    }

    @Override //
    public SpreadsheetCellClipboardValueKind parseString(final String string) {
        return SpreadsheetCellClipboardValueKind.parseUrlFragment(string);
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
                SpreadsheetCellClipboardValueKind.CELL,
                UrlFragment.with("cell")
        );
    }

    @Test
    public void testUrlFragmentFormula() {
        this.urlFragmentAndCheck(
                SpreadsheetCellClipboardValueKind.FORMULA,
                UrlFragment.with("formula")
        );
    }

    @Test
    public void testUrlFragmentFormatPattern() {
        this.urlFragmentAndCheck(
                SpreadsheetCellClipboardValueKind.FORMAT_PATTERN,
                UrlFragment.with("format-pattern")
        );
    }

    @Test
    public void testUrlFragmentStyle() {
        this.urlFragmentAndCheck(
                SpreadsheetCellClipboardValueKind.STYLE,
                UrlFragment.with("style")
        );
    }

    // fromMediaType....................................................................................................

    @Test
    public void testFromMediaTypeNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetCellClipboardValueKind.fromMediaType(null)
        );
    }

    @Test
    public void testFromMediaTypeUnknownFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> SpreadsheetCellClipboardValueKind.fromMediaType(MediaType.TEXT_PLAIN)
        );
    }

    @Test
    public void testFromMediaTypeCell() {
        this.fromMediaTypeAndCheck(
                SpreadsheetCellClipboardValueKind.CELL.mediaType(),
                SpreadsheetCellClipboardValueKind.CELL
        );
    }

    @Test
    public void testFromMediaTypeAllValues() {
        for (final SpreadsheetCellClipboardValueKind kind : SpreadsheetCellClipboardValueKind.values()) {
            this.fromMediaTypeAndCheck(
                    kind.mediaType(),
                    kind
            );
        }
    }

    private void fromMediaTypeAndCheck(final MediaType mediaType,
                                       final SpreadsheetCellClipboardValueKind expected) {
        this.checkEquals(
                expected,
                SpreadsheetCellClipboardValueKind.fromMediaType(mediaType),
                () -> "fromMediaType " + mediaType
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetCellClipboardValueKind> type() {
        return SpreadsheetCellClipboardValueKind.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
