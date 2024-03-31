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

public final class SpreadsheetCellClipboardValueKindTest implements ClassTesting<SpreadsheetCellClipboardValueKind>,
        HasMediaTypeTesting,
        HasUrlFragmentTesting,
        ParseStringTesting<SpreadsheetCellClipboardValueKind>,
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
        return SpreadsheetCellClipboardValueKind.parse(string);
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

    // marshall.........................................................................................................

    @Test
    public void testMarshallCell() {
        this.marshallAndCheck(
                SpreadsheetCellClipboardValueKind.CELL,
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
                SpreadsheetCellClipboardValueKind.FORMULA,
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
                SpreadsheetCellClipboardValueKind.FORMULA,
                SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY.setText(formula)
                ),
                JsonNode.string(formula)
        );
    }

    @Test
    public void testMarshallFormatPatternEmpty() {
        this.marshallAndCheck(
                SpreadsheetCellClipboardValueKind.FORMAT_PATTERN,
                SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY),
                JsonNode.nullNode()
        );
    }

    @Test
    public void testMarshallFormatPattern() {
        this.marshallAndCheck(
                SpreadsheetCellClipboardValueKind.FORMAT_PATTERN,
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
                SpreadsheetCellClipboardValueKind.PARSE_PATTERN,
                SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY),
                JsonNode.nullNode()
        );
    }

    @Test
    public void testMarshallParsePattern() {
        this.marshallAndCheck(
                SpreadsheetCellClipboardValueKind.PARSE_PATTERN,
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
                SpreadsheetCellClipboardValueKind.STYLE,
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
                SpreadsheetCellClipboardValueKind.FORMATTED_VALUE,
                SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY),
                JsonNode.nullNode()
        );
    }

    @Test
    public void testMarshallFormattedValue() {
        this.marshallAndCheck(
                SpreadsheetCellClipboardValueKind.FORMATTED_VALUE,
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

    private void marshallAndCheck(final SpreadsheetCellClipboardValueKind kind,
                                  final SpreadsheetCell cell,
                                  final String expected) {
        this.marshallAndCheck(
                kind,
                cell,
                JsonNode.parse(expected)
        );
    }

    private void marshallAndCheck(final SpreadsheetCellClipboardValueKind kind,
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
                SpreadsheetCellClipboardValueKind.FORMULA,
                JsonNode.string(""),
                SpreadsheetFormula.EMPTY
        );
    }

    @Test
    public void testUnmarshallFormula() {
        final String formula = "=1+2";

        this.unmarshallAndCheck(
                SpreadsheetCellClipboardValueKind.FORMULA,
                JsonNode.string(formula),
                parseFormula(formula)
        );
    }

    @Test
    public void testUnmarshallFormatPatternEmpty() {
        this.unmarshallAndCheck(
                SpreadsheetCellClipboardValueKind.FORMAT_PATTERN,
                JsonNode.nullNode(),
                Optional.empty()
        );
    }

    @Test
    public void testUnmarshallFormatPattern() {
        final SpreadsheetDateFormatPattern pattern = SpreadsheetPattern.parseDateFormatPattern("yyyy/mm/dd");

        this.unmarshallAndCheck(
                SpreadsheetCellClipboardValueKind.FORMAT_PATTERN,
                APP_CONTEXT.marshallContext()
                        .marshallWithType(pattern),
                Optional.of(pattern)
        );
    }

    @Test
    public void testUnmarshallParsePatternEmpty() {
        this.unmarshallAndCheck(
                SpreadsheetCellClipboardValueKind.PARSE_PATTERN,
                JsonNode.nullNode(),
                Optional.empty()
        );
    }

    @Test
    public void testUnmarshallParsePattern() {
        final SpreadsheetDateParsePattern pattern = SpreadsheetPattern.parseDateParsePattern("yyyy/mm/dd");

        this.unmarshallAndCheck(
                SpreadsheetCellClipboardValueKind.PARSE_PATTERN,
                APP_CONTEXT.marshallContext()
                        .marshallWithType(pattern),
                Optional.of(pattern)
        );
    }

    @Test
    public void testUnmarshallStyleEmpty() {
        this.unmarshallAndCheck(
                SpreadsheetCellClipboardValueKind.STYLE,
                JsonNode.object(),
                TextStyle.EMPTY
        );
    }

    @Test
    public void testUnmarshallStyleNotEmpty() {
        final TextStyle style = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.CENTER
        );

        this.unmarshallAndCheck(
                SpreadsheetCellClipboardValueKind.STYLE,
                APP_CONTEXT.marshallContext()
                        .marshall(style),
                style
        );
    }

    @Test
    public void testUnmarshallFormattedValueEmpty() {
        this.unmarshallAndCheck(
                SpreadsheetCellClipboardValueKind.FORMATTED_VALUE,
                JsonNode.nullNode(),
                Optional.empty()
        );
    }

    @Test
    public void testUnmarshallFormattedValue() {
        final String value = "Text123";

        this.unmarshallAndCheck(
                SpreadsheetCellClipboardValueKind.FORMATTED_VALUE,
                APP_CONTEXT.marshallContext()
                        .marshallWithType(value),
                Optional.of(value)
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
                SpreadsheetCellClipboardValueKind.CELL,
                JsonNode.object()
                        .appendChild(
                                SpreadsheetCellClipboardValueKind.CELL.marshall(
                                        cell,
                                        APP_CONTEXT.marshallContext()
                                )
                        ),
                expected
        );
    }

    private void unmarshallAndCheck(final SpreadsheetCellClipboardValueKind kind,
                                    final JsonNode node,
                                    final Object expected) {
        this.checkEquals(
                expected,
                kind.unmarshall(
                        node,
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
    public Class<SpreadsheetCellClipboardValueKind> type() {
        return SpreadsheetCellClipboardValueKind.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
