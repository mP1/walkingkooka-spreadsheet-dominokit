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
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetDateFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetDateParsePattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonPropertyName;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContexts;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellClipboardKindTest implements ClassTesting<SpreadsheetCellClipboardKind>,
        HasMediaTypeTesting,
        HasUrlFragmentTesting,
        ParseStringTesting<SpreadsheetCellClipboardKind>,
        PredicateTesting,
        SpreadsheetMetadataTesting,
        TreePrintableTesting {

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
                SpreadsheetCellClipboardKind.CELL,
                SpreadsheetCellClipboardKind.values()
        );
    }

    @Test
    public void testPredicateFormula() {
        this.predicateAndCheck(
                SpreadsheetCellClipboardKind.FORMULA
        );
    }

    @Test
    public void testPredicateFormatPattern() {
        this.predicateAndCheck(
                SpreadsheetCellClipboardKind.FORMAT_PATTERN
        );
    }

    @Test
    public void testPredicateParsePattern() {
        this.predicateAndCheck(
                SpreadsheetCellClipboardKind.PARSE_PATTERN
        );
    }

    @Test
    public void testPredicateStyle() {
        this.predicateAndCheck(
                SpreadsheetCellClipboardKind.STYLE
        );
    }

    @Test
    public void testPredicateFormatted() {
        this.predicateAndCheck(
                SpreadsheetCellClipboardKind.FORMATTED_VALUE
        );
    }

    private void predicateAndCheck(final SpreadsheetCellClipboardKind kind) {
        this.predicateAndCheck(
                kind,
                kind
        );
    }

    private void predicateAndCheck(final SpreadsheetCellClipboardKind kind,
                                   final SpreadsheetCellClipboardKind... trueKinds) {
        this.predicateAndCheck(
                kind,
                Sets.of(trueKinds)
        );
    }

    private void predicateAndCheck(final SpreadsheetCellClipboardKind kind,
                                   final Set<SpreadsheetCellClipboardKind> trueKinds) {
        final Predicate<SpreadsheetCellClipboardKind> predicate = kind.predicate();

        for (final SpreadsheetCellClipboardKind possible : SpreadsheetCellClipboardKind.values()) {
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
                SpreadsheetCellClipboardKind.CELL,
                CELL,
                CELL
        );
    }

    @Test
    public void testToValueFormula() {
        this.toValueAndCheck(
                SpreadsheetCellClipboardKind.FORMULA,
                CELL,
                CELL.formula()
        );
    }

    @Test
    public void testToValueFormulaEmptyFormula() {
        final SpreadsheetCell cell = SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY);

        this.toValueAndCheck(
                SpreadsheetCellClipboardKind.FORMULA,
                cell,
                cell.formula()
        );
    }

    @Test
    public void testToValueFormatPattern() {
        this.toValueAndCheck(
                SpreadsheetCellClipboardKind.FORMAT_PATTERN,
                CELL,
                CELL.formatPattern()
        );
    }

    @Test
    public void testToValueFormatPatternEmpty() {
        final SpreadsheetCell cell = CELL.setFormatPattern(SpreadsheetCell.NO_FORMAT_PATTERN);

        this.toValueAndCheck(
                SpreadsheetCellClipboardKind.FORMAT_PATTERN,
                cell,
                cell.formatPattern()
        );
    }

    @Test
    public void testToValueParsePattern() {
        this.toValueAndCheck(
                SpreadsheetCellClipboardKind.PARSE_PATTERN,
                CELL,
                CELL.parsePattern()
        );
    }

    @Test
    public void testToValueParsePatternEmpty() {
        final SpreadsheetCell cell = CELL.setParsePattern(SpreadsheetCell.NO_PARSE_PATTERN);

        this.toValueAndCheck(
                SpreadsheetCellClipboardKind.PARSE_PATTERN,
                cell,
                cell.parsePattern()
        );
    }

    @Test
    public void testToValueStyle() {
        this.toValueAndCheck(
                SpreadsheetCellClipboardKind.STYLE,
                CELL,
                CELL.style()
        );
    }

    @Test
    public void testToValueStyleEmpty() {
        final SpreadsheetCell cell = CELL.setStyle(TextStyle.EMPTY);

        this.toValueAndCheck(
                SpreadsheetCellClipboardKind.STYLE,
                cell,
                cell.style()
        );
    }

    @Test
    public void testToValueFormattedValue() {
        this.toValueAndCheck(
                SpreadsheetCellClipboardKind.FORMATTED_VALUE,
                CELL,
                CELL.formattedValue()
        );
    }

    @Test
    public void testToValueFormattedValueEmpty() {
        final SpreadsheetCell cell = CELL.setFormattedValue(SpreadsheetCell.NO_FORMATTED_VALUE_CELL);

        this.toValueAndCheck(
                SpreadsheetCellClipboardKind.FORMATTED_VALUE,
                cell,
                cell.formattedValue()
        );
    }

    private void toValueAndCheck(final SpreadsheetCellClipboardKind kind,
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
                SpreadsheetCellClipboardKind.CELL,
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
                SpreadsheetCellClipboardKind.FORMULA,
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
                SpreadsheetCellClipboardKind.CELL,
                SpreadsheetCell.class
        );
    }

    @Test
    public void testMediaTypeClassFormula() {
        this.mediaTypeClassAndCheck(
                SpreadsheetCellClipboardKind.FORMULA,
                SpreadsheetFormula.class
        );
    }

    @Test
    public void testMediaTypeClassStyle() {
        this.mediaTypeClassAndCheck(
                SpreadsheetCellClipboardKind.STYLE,
                TextStyle.class
        );
    }

    private void mediaTypeClassAndCheck(final SpreadsheetCellClipboardKind kind,
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
                SpreadsheetCellClipboardKind.CELL
        );
    }

    @Test
    public void testParseFormula() {
        this.parseStringAndCheck(
                "formula",
                SpreadsheetCellClipboardKind.FORMULA
        );
    }

    @Override //
    public SpreadsheetCellClipboardKind parseString(final String string) {
        return SpreadsheetCellClipboardKind.parse(string);
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
                SpreadsheetCellClipboardKind.CELL,
                UrlFragment.with("cell")
        );
    }

    @Test
    public void testUrlFragmentFormula() {
        this.urlFragmentAndCheck(
                SpreadsheetCellClipboardKind.FORMULA,
                UrlFragment.with("formula")
        );
    }

    @Test
    public void testUrlFragmentFormatPattern() {
        this.urlFragmentAndCheck(
                SpreadsheetCellClipboardKind.FORMAT_PATTERN,
                UrlFragment.with("format-pattern")
        );
    }

    @Test
    public void testUrlFragmentStyle() {
        this.urlFragmentAndCheck(
                SpreadsheetCellClipboardKind.STYLE,
                UrlFragment.with("style")
        );
    }

    // fromMediaType....................................................................................................

    @Test
    public void testFromMediaTypeNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetCellClipboardKind.fromMediaType(null)
        );
    }

    @Test
    public void testFromMediaTypeUnknownFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> SpreadsheetCellClipboardKind.fromMediaType(MediaType.TEXT_PLAIN)
        );
    }

    @Test
    public void testFromMediaTypeCell() {
        this.fromMediaTypeAndCheck(
                SpreadsheetCellClipboardKind.CELL.mediaType(),
                SpreadsheetCellClipboardKind.CELL
        );
    }

    @Test
    public void testFromMediaTypeAllValues() {
        for (final SpreadsheetCellClipboardKind kind : SpreadsheetCellClipboardKind.values()) {
            this.fromMediaTypeAndCheck(
                    kind.mediaType(),
                    kind
            );
        }
    }

    private void fromMediaTypeAndCheck(final MediaType mediaType,
                                       final SpreadsheetCellClipboardKind expected) {
        this.checkEquals(
                expected,
                SpreadsheetCellClipboardKind.fromMediaType(mediaType),
                () -> "fromMediaType " + mediaType
        );
    }

    // marshall.........................................................................................................

    @Test
    public void testMarshallCell() {
        this.marshallAndCheck(
                SpreadsheetCellClipboardKind.CELL,
                SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY.setText("=1+2")
                ).setFormatPattern(
                        Optional.of(
                                SpreadsheetPattern.parseDateFormatPattern("yyyy/mm/dd")
                        )
                ).setParsePattern(
                        Optional.of(
                                SpreadsheetPattern.parseNumberParsePattern("$0.00")
                        )
                ).setStyle(
                        TextStyle.EMPTY.set(
                                TextStylePropertyName.TEXT_ALIGN,
                                TextAlign.CENTER
                        )
                ),
                "{\n" +
                        "  \"formula\": {\n" +
                        "    \"text\": \"=1+2\"\n" +
                        "  },\n" +
                        "  \"style\": {\n" +
                        "    \"text-align\": \"CENTER\"\n" +
                        "  },\n" +
                        "  \"parse-pattern\": {\n" +
                        "    \"type\": \"spreadsheet-number-parse-pattern\",\n" +
                        "    \"value\": \"$0.00\"\n" +
                        "  },\n" +
                        "  \"format-pattern\": {\n" +
                        "    \"type\": \"spreadsheet-date-format-pattern\",\n" +
                        "    \"value\": \"yyyy/mm/dd\"\n" +
                        "  }\n" +
                        "}"
        );
    }

    @Test
    public void testMarshallFormulaEmpty() {
        final String formula = "";
        this.marshallAndCheck(
                SpreadsheetCellClipboardKind.FORMULA,
                SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY.setText(formula)
                ),
                JsonNode.string(formula)
        );
    }

    @Test
    public void testMarshallFormula() {
        final String formula = "=1+2+3";
        this.marshallAndCheck(
                SpreadsheetCellClipboardKind.FORMULA,
                SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY.setText(formula)
                ),
                JsonNode.string(formula)
        );
    }

    @Test
    public void testMarshallFormatPatternEmpty() {
        this.marshallAndCheck(
                SpreadsheetCellClipboardKind.FORMAT_PATTERN,
                SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY),
                JsonNode.nullNode()
        );
    }

    @Test
    public void testMarshallFormatPattern() {
        this.marshallAndCheck(
                SpreadsheetCellClipboardKind.FORMAT_PATTERN,
                SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                        .setFormatPattern(
                                Optional.of(
                                        SpreadsheetPattern.parseDateFormatPattern("yyyy/mm/dd")
                                )
                        ),
                "{\n" +
                        "  \"type\": \"spreadsheet-date-format-pattern\",\n" +
                        "  \"value\": \"yyyy/mm/dd\"\n" +
                        "}"
        );
    }

    @Test
    public void testMarshallParsePatternEmpty() {
        this.marshallAndCheck(
                SpreadsheetCellClipboardKind.PARSE_PATTERN,
                SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY),
                JsonNode.nullNode()
        );
    }

    @Test
    public void testMarshallParsePattern() {
        this.marshallAndCheck(
                SpreadsheetCellClipboardKind.PARSE_PATTERN,
                SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                        .setParsePattern(
                                Optional.of(
                                        SpreadsheetPattern.parseDateParsePattern("yyyy/mm/dd")
                                )
                        ),
                "{\n" +
                        "  \"type\": \"spreadsheet-date-parse-pattern\",\n" +
                        "  \"value\": \"yyyy/mm/dd\"\n" +
                        "}"
        );
    }

    @Test
    public void testMarshallStyle() {
        this.marshallAndCheck(
                SpreadsheetCellClipboardKind.STYLE,
                SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                        .setStyle(
                                TextStyle.EMPTY.set(
                                        TextStylePropertyName.TEXT_ALIGN,
                                        TextAlign.CENTER
                                )
                        ),
                "{\n" +
                        "  \"text-align\": \"CENTER\"\n" +
                        "}"
        );
    }

    @Test
    public void testMarshallFormattedValueEmpty() {
        this.marshallAndCheck(
                SpreadsheetCellClipboardKind.FORMATTED_VALUE,
                SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY),
                JsonNode.nullNode()
        );
    }

    @Test
    public void testMarshallFormattedValue() {
        this.marshallAndCheck(
                SpreadsheetCellClipboardKind.FORMATTED_VALUE,
                SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                        .setFormattedValue(
                                Optional.of(
                                        TextNode.text("Hello")
                                )
                        ),
                "{\n" +
                        "  \"type\": \"text\",\n" +
                        "  \"value\": \"Hello\"\n" +
                        "}"
        );
    }

    private void marshallAndCheck(final SpreadsheetCellClipboardKind kind,
                                  final SpreadsheetCell cell,
                                  final String expected) {
        this.marshallAndCheck(
                kind,
                cell,
                JsonNode.parse(expected)
        );
    }

    private void marshallAndCheck(final SpreadsheetCellClipboardKind kind,
                                  final SpreadsheetCell cell,
                                  final JsonNode expected) {
        this.checkEquals(
                expected.setName(
                        JsonPropertyName.with("A1")
                ),
                kind.marshall(
                        cell,
                        JsonNodeMarshallContexts.basic()
                ),
                () -> kind + " " + cell
        );
    }

    // unmarshall.......................................................................................................

    private final static JsonPropertyName A1 = JsonPropertyName.with("A1");

    @Test
    public void testUnmarshallCellFormulaEmpty() {
        this.unmarshallAndCheck(
                SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
        );
    }

    @Test
    public void testUnmarshallCellFormula() {
        final String formula = "=1+2";

        this.unmarshallAndCheck(
                SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY.setText(formula)
                ),
                SpreadsheetSelection.A1.setFormula(
                        parseFormula(formula)
                )
        );
    }

    @Test
    public void testUnmarshallCellFormatPattern() {
        final String formula = "=1+2";

        final Optional<SpreadsheetFormatPattern> formatPattern = Optional.of(
                SpreadsheetPattern.parseDateFormatPattern("dd/mm/yyyy")
        );

        this.unmarshallAndCheck(
                SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY.setText(formula)
                ).setFormatPattern(formatPattern),
                SpreadsheetSelection.A1.setFormula(
                        parseFormula(formula)
                ).setFormatPattern(formatPattern)
        );
    }

    @Test
    public void testUnmarshallCellParsePattern() {
        final String formula = "=1+2+333";

        final Optional<SpreadsheetParsePattern> parsePattern = Optional.of(
                SpreadsheetPattern.parseDateParsePattern("dd/mm/yyyy")
        );

        this.unmarshallAndCheck(
                SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY.setText(formula)
                ).setParsePattern(parsePattern),
                SpreadsheetSelection.A1.setFormula(
                                SpreadsheetFormula.EMPTY.setText(formula)
                        ).setParsePattern(parsePattern)
                        .setFormula(
                                parseFormula(formula)
                        )
        );
    }

    @Test
    public void testUnmarshallCellStyle() {
        final String formula = "=1+2";

        final TextStyle style = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.CENTER
        );

        this.unmarshallAndCheck(
                SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY.setText(formula)
                ).setStyle(style),
                SpreadsheetSelection.A1.setFormula(
                        parseFormula(formula)
                ).setStyle(style)
        );
    }

    @Test
    public void testUnmarshallCellFormattedValue() {
        final String formula = "=1+2";
        final Optional<TextNode> formattedValue = Optional.of(
                TextNode.text("3")
        );

        this.unmarshallAndCheck(
                SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY.setText(formula)
                ).setFormattedValue(formattedValue),
                SpreadsheetSelection.A1.setFormula(
                                SpreadsheetFormula.EMPTY.setText(formula)
                        ).setFormattedValue(formattedValue)
                        .setFormula(
                                parseFormula(formula)
                        )
        );
    }

    @Test
    public void testUnmarshallFormulaEmpty() {
        this.unmarshallAndCheck(
                SpreadsheetCellClipboardKind.FORMULA,
                JsonNode.string(""),
                EMPTY_FORMULA
        );
    }

    @Test
    public void testUnmarshallFormula() {
        final String formula = "=1+2";

        this.unmarshallAndCheck(
                SpreadsheetCellClipboardKind.FORMULA,
                JsonNode.string(formula),
                SpreadsheetSelection.A1.setFormula(
                        parseFormula(formula)
                )
        );
    }

    private final static SpreadsheetCell EMPTY_FORMULA = SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY);

    @Test
    public void testUnmarshallFormatPatternEmpty() {
        this.unmarshallAndCheck(
                SpreadsheetCellClipboardKind.FORMAT_PATTERN,
                JsonNode.nullNode(),
                EMPTY_FORMULA
        );
    }

    @Test
    public void testUnmarshallFormatPattern() {
        final SpreadsheetDateFormatPattern pattern = SpreadsheetPattern.parseDateFormatPattern("yyyy/mm/dd");

        this.unmarshallAndCheck(
                SpreadsheetCellClipboardKind.FORMAT_PATTERN,
                APP_CONTEXT.marshallContext()
                        .marshallWithType(pattern),
                EMPTY_FORMULA.setFormatPattern(
                        Optional.of(pattern)
                )
        );
    }

    @Test
    public void testUnmarshallParsePatternEmpty() {
        this.unmarshallAndCheck(
                SpreadsheetCellClipboardKind.PARSE_PATTERN,
                JsonNode.nullNode(),
                EMPTY_FORMULA
        );
    }

    @Test
    public void testUnmarshallParsePattern() {
        final SpreadsheetDateParsePattern pattern = SpreadsheetPattern.parseDateParsePattern("yyyy/mm/dd");

        this.unmarshallAndCheck(
                SpreadsheetCellClipboardKind.PARSE_PATTERN,
                APP_CONTEXT.marshallContext()
                        .marshallWithType(pattern),
                EMPTY_FORMULA.setParsePattern(
                        Optional.of(pattern)
                )
        );
    }

    @Test
    public void testUnmarshallStyleEmpty() {
        this.unmarshallAndCheck(
                SpreadsheetCellClipboardKind.STYLE,
                JsonNode.object(),
                EMPTY_FORMULA
        );
    }

    @Test
    public void testUnmarshallStyleNotEmpty() {
        final TextStyle style = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.CENTER
        );

        this.unmarshallAndCheck(
                SpreadsheetCellClipboardKind.STYLE,
                APP_CONTEXT.marshallContext()
                        .marshall(style),
                EMPTY_FORMULA.setStyle(style)
        );
    }

    @Test
    public void testUnmarshallFormattedValueEmpty() {
        this.unmarshallAndCheck(
                SpreadsheetCellClipboardKind.FORMATTED_VALUE,
                JsonNode.nullNode(),
                EMPTY_FORMULA
        );
    }

    @Test
    public void testUnmarshallFormattedValue() {
        final TextNode value = TextNode.text("Text123");

        this.unmarshallAndCheck(
                SpreadsheetCellClipboardKind.FORMATTED_VALUE,
                APP_CONTEXT.marshallContext()
                        .marshallWithType(value),
                EMPTY_FORMULA.setFormattedValue(
                        Optional.of(value)
                )
        );
    }

    private void unmarshallAndCheck(final SpreadsheetCell cell) {
        this.unmarshallAndCheck(
                cell,
                cell
        );
    }

    private void unmarshallAndCheck(final SpreadsheetCell cell,
                                    final SpreadsheetCell expected) {
        this.unmarshallAndCheck(
                SpreadsheetCellClipboardKind.CELL,
                SpreadsheetCellClipboardKind.CELL.marshall(
                        cell,
                        APP_CONTEXT.marshallContext()
                ),
                expected
        );
    }

    private void unmarshallAndCheck(final SpreadsheetCellClipboardKind kind,
                                    final JsonNode node,
                                    final SpreadsheetCell expected) {
        this.checkEquals(
                expected,
                kind.unmarshall(
                        node.setName(
                                JsonPropertyName.with("A1")
                        ),
                        APP_CONTEXT
                ),
                () -> kind + " " + node
        );
    }

    private static SpreadsheetFormula parseFormula(final String text) {
        return SpreadsheetFormula.parse(
                TextCursors.charSequence(text),
                METADATA_EN_AU
                        .parser(),
                METADATA_EN_AU
                        .parserContext(LocalDateTime::now)
        );
    }

    private final static AppContext APP_CONTEXT = new FakeAppContext() {
        @Override
        public SpreadsheetMetadata spreadsheetMetadata() {
            return METADATA_EN_AU;
        }

        @Override
        public JsonNodeMarshallContext marshallContext() {
            return this.spreadsheetMetadata()
                    .jsonNodeMarshallContext();
        }

        @Override
        public JsonNodeUnmarshallContext unmarshallContext() {
            return this.spreadsheetMetadata()
                    .jsonNodeUnmarshallContext();
        }
    };

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetCellClipboardKind> type() {
        return SpreadsheetCellClipboardKind.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
