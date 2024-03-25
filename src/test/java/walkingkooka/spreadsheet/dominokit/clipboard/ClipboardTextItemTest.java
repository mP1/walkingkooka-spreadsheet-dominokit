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
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.iterator.Iterators;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.header.MediaType;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.AppContexts;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContexts;
import walkingkooka.tree.text.FontStyle;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Iterator;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ClipboardTextItemTest implements ClassTesting<ClipboardTextItem>,
        HashCodeEqualsDefinedTesting2<ClipboardTextItem>,
        ToStringTesting<ClipboardTextItem>,
        TreePrintableTesting {

    @Test
    public void testWithNullMediaTypeFails() {
        assertThrows(
                NullPointerException.class,
                () -> ClipboardTextItem.with(
                        null,
                        "abc123"
                )
        );
    }

    @Test
    public void testWithEmptyMediaTypeFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ClipboardTextItem.with(
                        Lists.empty(),
                        "abc123"
                )
        );
    }

    @Test
    public void testWithNullTextFails() {
        assertThrows(
                NullPointerException.class,
                () -> ClipboardTextItem.with(
                        Lists.of(
                                MediaType.TEXT_PLAIN
                        ),
                        null
                )
        );
    }

    // prepare.........................................................................................................

    @Test
    public void testPrepareWIthNullCellsFails() {
        assertThrows(
                NullPointerException.class,
                () -> ClipboardTextItem.prepare(
                        null, // cells,
                        SpreadsheetCellClipboardValueKind.CELL,
                        AppContexts.fake()
                )
        );
    }

    @Test
    public void testPrepareWIthNullKindFails() {
        assertThrows(
                NullPointerException.class,
                () -> ClipboardTextItem.prepare(
                        Iterators.fake(), // cells,
                        null,
                        AppContexts.fake()
                )
        );
    }

    @Test
    public void testPrepareWIthNullContextFails() {
        assertThrows(
                NullPointerException.class,
                () -> ClipboardTextItem.prepare(
                        null, // cells,
                        SpreadsheetCellClipboardValueKind.CELL,
                        null
                )
        );
    }

    @Test
    public void testPrepareCellsWithCellsNone() {
        this.prepareAndCheck(
                Iterators.empty(),
                SpreadsheetCellClipboardValueKind.CELL,
                ClipboardTextItem.with(
                        Lists.of(
                                MediaType.TEXT_PLAIN
                        ),
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.spreadsheet.SpreadsheetCell\",\n" +
                                "  \"value\": {}\n" +
                                "}"
                )
        );
    }

    @Test
    public void testPrepareCellsWithCellsOne() {
        this.prepareAndCheck(
                Iterators.array(
                        SpreadsheetSelection.A1.setFormula(
                                SpreadsheetFormula.EMPTY.setText("=1")
                        ).setStyle(
                                TextStyle.EMPTY.set(
                                        TextStylePropertyName.TEXT_ALIGN,
                                        TextAlign.CENTER
                                )
                        )
                ),
                SpreadsheetCellClipboardValueKind.CELL,
                ClipboardTextItem.with(
                        Lists.of(
                                MediaType.TEXT_PLAIN
                        ),
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.spreadsheet.SpreadsheetCell\",\n" +
                                "  \"value\": {\n" +
                                "    \"A1\": {\n" +
                                "      \"formula\": {\n" +
                                "        \"text\": \"=1\"\n" +
                                "      },\n" +
                                "      \"style\": {\n" +
                                "        \"text-align\": \"CENTER\"\n" +
                                "      }\n" +
                                "    }\n" +
                                "  }\n" +
                                "}"
                )
        );
    }

    @Test
    public void testPrepareCellsWithCellsSeveral() {
        this.prepareAndCheck(
                Iterators.array(
                        SpreadsheetSelection.A1.setFormula(
                                SpreadsheetFormula.EMPTY.setText("=1")
                        ).setStyle(
                                TextStyle.EMPTY.set(
                                        TextStylePropertyName.TEXT_ALIGN,
                                        TextAlign.CENTER
                                )
                        ),
                        SpreadsheetSelection.parseCell("B2")
                                .setFormula(
                                        SpreadsheetFormula.EMPTY.setText("=22")
                                ).setFormatPattern(
                                        Optional.of(
                                                SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN
                                        )
                                )
                ),
                SpreadsheetCellClipboardValueKind.CELL,
                ClipboardTextItem.with(
                        Lists.of(
                                MediaType.TEXT_PLAIN
                        ),
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.spreadsheet.SpreadsheetCell\",\n" +
                                "  \"value\": {\n" +
                                "    \"A1\": {\n" +
                                "      \"formula\": {\n" +
                                "        \"text\": \"=1\"\n" +
                                "      },\n" +
                                "      \"style\": {\n" +
                                "        \"text-align\": \"CENTER\"\n" +
                                "      }\n" +
                                "    },\n" +
                                "    \"B2\": {\n" +
                                "      \"formula\": {\n" +
                                "        \"text\": \"=22\"\n" +
                                "      },\n" +
                                "      \"format-pattern\": {\n" +
                                "        \"type\": \"spreadsheet-text-format-pattern\",\n" +
                                "        \"value\": \"@\"\n" +
                                "      }\n" +
                                "    }\n" +
                                "  }\n" +
                                "}"
                )
        );
    }

    @Test
    public void testPrepareCellsFormulaNone() {
        this.prepareAndCheck(
                Iterators.array(
                ),
                SpreadsheetCellClipboardValueKind.FORMULA,
                ClipboardTextItem.with(
                        Lists.of(
                                MediaType.TEXT_PLAIN
                        ),
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.spreadsheet.SpreadsheetFormula\",\n" +
                                "  \"value\": {}\n" +
                                "}"
                )
        );
    }

    @Test
    public void testPrepareCellsFormulaSeveral() {
        this.prepareAndCheck(
                Iterators.array(
                        SpreadsheetSelection.A1.setFormula(
                                SpreadsheetFormula.EMPTY.setText("=1")
                        ),
                        SpreadsheetSelection.parseCell("B2")
                                .setFormula(
                                        SpreadsheetFormula.EMPTY.setText("=22")
                                )
                ),
                SpreadsheetCellClipboardValueKind.FORMULA,
                ClipboardTextItem.with(
                        Lists.of(
                                MediaType.TEXT_PLAIN
                        ),
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.spreadsheet.SpreadsheetFormula\",\n" +
                                "  \"value\": {\n" +
                                "    \"A1\": \"=1\",\n" +
                                "    \"B2\": \"=22\"\n" +
                                "  }\n" +
                                "}"
                )
        );
    }

    @Test
    public void testPrepareCellsFormulaEmpty() {
        this.prepareAndCheck(
                Iterators.array(
                        SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                ),
                SpreadsheetCellClipboardValueKind.FORMULA,
                ClipboardTextItem.with(
                        Lists.of(
                                MediaType.TEXT_PLAIN
                        ),
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.spreadsheet.SpreadsheetFormula\",\n" +
                                "  \"value\": {\n" +
                                "    \"A1\": \"\"\n" +
                                "  }\n" +
                                "}"
                )
        );
    }

    @Test
    public void testPrepareCellsFormatPatternNone() {
        this.prepareAndCheck(
                Iterators.array(
                ),
                SpreadsheetCellClipboardValueKind.FORMAT_PATTERN,
                ClipboardTextItem.with(
                        Lists.of(
                                MediaType.TEXT_PLAIN
                        ),
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern\",\n" +
                                "  \"value\": {}\n" +
                                "}"
                )
        );
    }

    @Test
    public void testPrepareCellsFormatPatternMissing() {
        this.prepareAndCheck(
                Iterators.array(
                        SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                ),
                SpreadsheetCellClipboardValueKind.FORMAT_PATTERN,
                ClipboardTextItem.with(
                        Lists.of(
                                MediaType.TEXT_PLAIN
                        ),
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern\",\n" +
                                "  \"value\": {\n" +
                                "    \"A1\": null\n" +
                                "  }\n" +
                                "}"
                )
        );
    }

    @Test
    public void testPrepareCellsFormatPatternSomeMissing() {
        this.prepareAndCheck(
                Iterators.array(
                        SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY),
                        SpreadsheetSelection.parseCell("B2")
                                .setFormula(SpreadsheetFormula.EMPTY)
                                .setFormatPattern(
                                        Optional.of(
                                                SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN
                                        )
                                )
                ),
                SpreadsheetCellClipboardValueKind.FORMAT_PATTERN,
                ClipboardTextItem.with(
                        Lists.of(
                                MediaType.TEXT_PLAIN
                        ),
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern\",\n" +
                                "  \"value\": {\n" +
                                "    \"A1\": null,\n" +
                                "    \"B2\": {\n" +
                                "      \"type\": \"spreadsheet-text-format-pattern\",\n" +
                                "      \"value\": \"@\"\n" +
                                "    }\n" +
                                "  }\n" +
                                "}"
                )
        );
    }

    @Test
    public void testPrepareCellsFormatPattern() {
        this.prepareAndCheck(
                Iterators.array(
                        SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                                .setFormatPattern(
                                        Optional.of(SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN)
                                ),
                        SpreadsheetSelection.parseCell("B2")
                                .setFormula(SpreadsheetFormula.EMPTY)
                                .setFormatPattern(
                                        Optional.of(
                                                SpreadsheetPattern.parseNumberFormatPattern("$0.00")
                                        )
                                )
                ),
                SpreadsheetCellClipboardValueKind.FORMAT_PATTERN,
                ClipboardTextItem.with(
                        Lists.of(
                                MediaType.TEXT_PLAIN
                        ),
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern\",\n" +
                                "  \"value\": {\n" +
                                "    \"A1\": {\n" +
                                "      \"type\": \"spreadsheet-text-format-pattern\",\n" +
                                "      \"value\": \"@\"\n" +
                                "    },\n" +
                                "    \"B2\": {\n" +
                                "      \"type\": \"spreadsheet-number-format-pattern\",\n" +
                                "      \"value\": \"$0.00\"\n" +
                                "    }\n" +
                                "  }\n" +
                                "}"
                )
        );
    }

    @Test
    public void testPrepareCellsParsePatternNone() {
        this.prepareAndCheck(
                Iterators.array(
                ),
                SpreadsheetCellClipboardValueKind.PARSE_PATTERN,
                ClipboardTextItem.with(
                        Lists.of(
                                MediaType.TEXT_PLAIN
                        ),
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern\",\n" +
                                "  \"value\": {}\n" +
                                "}"
                )
        );
    }

    @Test
    public void testPrepareCellsParsePatternMissing() {
        this.prepareAndCheck(
                Iterators.array(
                        SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                ),
                SpreadsheetCellClipboardValueKind.PARSE_PATTERN,
                ClipboardTextItem.with(
                        Lists.of(
                                MediaType.TEXT_PLAIN
                        ),
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern\",\n" +
                                "  \"value\": {\n" +
                                "    \"A1\": null\n" +
                                "  }\n" +
                                "}"
                )
        );
    }

    @Test
    public void testPrepareCellsParsePatternSomeMissing() {
        this.prepareAndCheck(
                Iterators.array(
                        SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY),
                        SpreadsheetSelection.parseCell("B2")
                                .setFormula(SpreadsheetFormula.EMPTY)
                                .setParsePattern(
                                        Optional.of(
                                                SpreadsheetPattern.parseNumberParsePattern("$0.00")
                                        )
                                )
                ),
                SpreadsheetCellClipboardValueKind.PARSE_PATTERN,
                ClipboardTextItem.with(
                        Lists.of(
                                MediaType.TEXT_PLAIN
                        ),
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern\",\n" +
                                "  \"value\": {\n" +
                                "    \"A1\": null,\n" +
                                "    \"B2\": {\n" +
                                "      \"type\": \"spreadsheet-number-parse-pattern\",\n" +
                                "      \"value\": \"$0.00\"\n" +
                                "    }\n" +
                                "  }\n" +
                                "}"
                )
        );
    }

    @Test
    public void testPrepareCellsParsePattern() {
        this.prepareAndCheck(
                Iterators.array(
                        SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                                .setParsePattern(
                                        Optional.of(
                                                SpreadsheetPattern.parseDateParsePattern("dd/mm/yyyy")
                                        )
                                ),
                        SpreadsheetSelection.parseCell("B2")
                                .setFormula(SpreadsheetFormula.EMPTY)
                                .setParsePattern(
                                        Optional.of(
                                                SpreadsheetPattern.parseNumberParsePattern("$0.00")
                                        )
                                )
                ),
                SpreadsheetCellClipboardValueKind.PARSE_PATTERN,
                ClipboardTextItem.with(
                        Lists.of(
                                MediaType.TEXT_PLAIN
                        ),
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern\",\n" +
                                "  \"value\": {\n" +
                                "    \"A1\": {\n" +
                                "      \"type\": \"spreadsheet-date-parse-pattern\",\n" +
                                "      \"value\": \"dd/mm/yyyy\"\n" +
                                "    },\n" +
                                "    \"B2\": {\n" +
                                "      \"type\": \"spreadsheet-number-parse-pattern\",\n" +
                                "      \"value\": \"$0.00\"\n" +
                                "    }\n" +
                                "  }\n" +
                                "}"
                )
        );
    }

    @Test
    public void testPrepareCellsStyleNone() {
        this.prepareAndCheck(
                Iterators.array(
                ),
                SpreadsheetCellClipboardValueKind.STYLE,
                ClipboardTextItem.with(
                        Lists.of(
                                MediaType.TEXT_PLAIN
                        ),
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.tree.text.TextStyle\",\n" +
                                "  \"value\": {}\n" +
                                "}"
                )
        );
    }

    @Test
    public void testPrepareCellsStyleEmpty() {
        this.prepareAndCheck(
                Iterators.array(
                        SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                ),
                SpreadsheetCellClipboardValueKind.STYLE,
                ClipboardTextItem.with(
                        Lists.of(
                                MediaType.TEXT_PLAIN
                        ),
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.tree.text.TextStyle\",\n" +
                                "  \"value\": {\n" +
                                "    \"A1\": {}\n" +
                                "  }\n" +
                                "}"
                )
        );
    }

    @Test
    public void testPrepareCellsStyleSomeEmpty() {
        this.prepareAndCheck(
                Iterators.array(
                        SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY),
                        SpreadsheetSelection.parseCell("B2")
                                .setFormula(SpreadsheetFormula.EMPTY)
                                .setStyle(
                                        TextStyle.EMPTY.set(
                                                TextStylePropertyName.TEXT_ALIGN,
                                                TextAlign.CENTER
                                        )
                                )
                ),
                SpreadsheetCellClipboardValueKind.STYLE,
                ClipboardTextItem.with(
                        Lists.of(
                                MediaType.TEXT_PLAIN
                        ),
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.tree.text.TextStyle\",\n" +
                                "  \"value\": {\n" +
                                "    \"A1\": {},\n" +
                                "    \"B2\": {\n" +
                                "      \"text-align\": \"CENTER\"\n" +
                                "    }\n" +
                                "  }\n" +
                                "}"
                )
        );
    }

    @Test
    public void testPrepareCellsStyle() {
        this.prepareAndCheck(
                Iterators.array(
                        SpreadsheetSelection.A1
                                .setFormula(SpreadsheetFormula.EMPTY)
                                .setStyle(
                                        TextStyle.EMPTY.set(
                                                TextStylePropertyName.FONT_STYLE,
                                                FontStyle.ITALIC
                                        )
                                ),
                        SpreadsheetSelection.parseCell("B2")
                                .setFormula(SpreadsheetFormula.EMPTY)
                                .setStyle(
                                        TextStyle.EMPTY.set(
                                                TextStylePropertyName.TEXT_ALIGN,
                                                TextAlign.CENTER
                                        )
                                )
                ),
                SpreadsheetCellClipboardValueKind.STYLE,
                ClipboardTextItem.with(
                        Lists.of(
                                MediaType.TEXT_PLAIN
                        ),
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.tree.text.TextStyle\",\n" +
                                "  \"value\": {\n" +
                                "    \"A1\": {\n" +
                                "      \"font-style\": \"ITALIC\"\n" +
                                "    },\n" +
                                "    \"B2\": {\n" +
                                "      \"text-align\": \"CENTER\"\n" +
                                "    }\n" +
                                "  }\n" +
                                "}"
                )
        );
    }

    @Test
    public void testPrepareCellsFormattedNone() {
        this.prepareAndCheck(
                Iterators.array(
                ),
                SpreadsheetCellClipboardValueKind.FORMATTED_VALUE,
                ClipboardTextItem.with(
                        Lists.of(
                                MediaType.TEXT_PLAIN
                        ),
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.tree.text.TextNode\",\n" +
                                "  \"value\": {}\n" +
                                "}"
                )
        );
    }

    @Test
    public void testPrepareCellsFormattedSeveral() {
        this.prepareAndCheck(
                Iterators.array(
                        SpreadsheetSelection.A1.setFormula(
                                SpreadsheetFormula.EMPTY.setText("=1")
                        ).setFormattedValue(
                                Optional.of(
                                        TextNode.text("111")
                                )
                        ),
                        SpreadsheetSelection.parseCell("B2")
                                .setFormula(
                                        SpreadsheetFormula.EMPTY.setText("=2")
                                ).setFormattedValue(
                                        Optional.of(
                                                TextNode.text("222")
                                        )
                                )
                ),
                SpreadsheetCellClipboardValueKind.FORMATTED_VALUE,
                ClipboardTextItem.with(
                        Lists.of(
                                MediaType.TEXT_PLAIN
                        ),
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.tree.text.TextNode\",\n" +
                                "  \"value\": {\n" +
                                "    \"A1\": {\n" +
                                "      \"type\": \"text\",\n" +
                                "      \"value\": \"111\"\n" +
                                "    },\n" +
                                "    \"B2\": {\n" +
                                "      \"type\": \"text\",\n" +
                                "      \"value\": \"222\"\n" +
                                "    }\n" +
                                "  }\n" +
                                "}"
                )
        );
    }

    @Test
    public void testPrepareCellsFormattedMissing() {
        this.prepareAndCheck(
                Iterators.array(
                        SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                ),
                SpreadsheetCellClipboardValueKind.FORMATTED_VALUE,
                ClipboardTextItem.with(
                        Lists.of(
                                MediaType.TEXT_PLAIN
                        ),
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.tree.text.TextNode\",\n" +
                                "  \"value\": {\n" +
                                "    \"A1\": null\n" +
                                "  }\n" +
                                "}"
                )
        );
    }

    private void prepareAndCheck(final Iterator<SpreadsheetCell> cells,
                                 final SpreadsheetCellClipboardValueKind kind,
                                 final ClipboardTextItem expected) {
        this.prepareAndCheck(
                cells,
                kind,
                new FakeAppContext() {
                    @Override
                    public JsonNodeMarshallContext marshallContext() {
                        return JsonNodeMarshallContexts.basic();
                    }
                },
                expected
        );
    }

    private void prepareAndCheck(final Iterator<SpreadsheetCell> cells,
                                 final SpreadsheetCellClipboardValueKind kind,
                                 final AppContext context,
                                 final ClipboardTextItem expected) {
        this.checkEquals(
                expected,
                ClipboardTextItem.prepare(
                        cells,
                        kind,
                        context
                )
        );
    }

    // equals...........................................................................................................

    @Test
    public void testEqualsDifferentTypes() {
        this.checkNotEquals(
                ClipboardTextItem.with(
                        Lists.of(
                                MediaType.parse("text/different")
                        ),
                        "Text123"
                )
        );
    }

    @Test
    public void testEqualsDifferentText() {
        this.checkNotEquals(
                ClipboardTextItem.with(
                        Lists.of(
                                MediaType.TEXT_PLAIN
                        ),
                        "different"
                )
        );
    }

    @Test
    public void testToString() {
        final MediaType type = MediaType.TEXT_PLAIN;
        final String text = "abc123";

        this.toStringAndCheck(
                ClipboardTextItem.with(
                        Lists.of(type),
                        text
                ),
                type + " \"" + text + "\""
        );
    }

    // TreePrinterTesting...............................................................................................

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
                ClipboardTextItem.with(
                        Lists.of(
                                MediaType.TEXT_PLAIN
                        ),
                        "123"
                ),
                "types\n" +
                        "  text/plain\n" +
                        "text\n" +
                        "  123\n"
        );
    }

    @Test
    public void testTreePrintSeveralMediaTypes() {
        this.treePrintAndCheck(
                ClipboardTextItem.with(
                        Lists.of(
                                MediaType.TEXT_PLAIN,
                                MediaType.APPLICATION_JSON
                        ),
                        "123"
                ),
                "types\n" +
                        "  text/plain\n" +
                        "  application/json\n" +
                        "text\n" +
                        "  123\n"
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ClipboardTextItem> type() {
        return ClipboardTextItem.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // HashCodeEqualsDefinedTesting2....................................................................................

    @Override
    public ClipboardTextItem createObject() {
        return ClipboardTextItem.with(
                Lists.of(
                        MediaType.TEXT_PLAIN
                ),
                "Text123"
        );
    }
}
