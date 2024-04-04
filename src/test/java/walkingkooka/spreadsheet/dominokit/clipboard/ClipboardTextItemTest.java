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
import walkingkooka.collect.set.Sets;
import walkingkooka.net.header.MediaType;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetCellRange;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.HasTextTesting;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContexts;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.text.FontStyle;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ClipboardTextItemTest implements ClassTesting<ClipboardTextItem>,
        HashCodeEqualsDefinedTesting2<ClipboardTextItem>,
        HasTextTesting,
        SpreadsheetMetadataTesting,
        ToStringTesting<ClipboardTextItem>,
        TreePrintableTesting {

    private static final List<MediaType> TYPES = Lists.of(
            MediaType.TEXT_PLAIN
    );
    private static final String TEXT = "Text123";

    @Test
    public void testWithNullMediaTypeFails() {
        assertThrows(
                NullPointerException.class,
                () -> ClipboardTextItem.with(
                        null,
                        TEXT
                )
        );
    }

    @Test
    public void testWithEmptyMediaTypeFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ClipboardTextItem.with(
                        Lists.empty(),
                        TEXT
                )
        );
    }

    @Test
    public void testWithNullTextFails() {
        assertThrows(
                NullPointerException.class,
                () -> ClipboardTextItem.with(
                        TYPES,
                        null
                )
        );
    }

    @Test
    public void testWith() {
        final ClipboardTextItem clipboardTextItem = ClipboardTextItem.with(
                TYPES,
                TEXT
        );
        this.checkTypes(clipboardTextItem);
        this.checkText(clipboardTextItem);
    }

    // setTypes.........................................................................................................

    @Test
    public void testSetTypesWithNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createObject().setTypes(null)
        );
    }

    @Test
    public void testSetTypesWithSame() {
        final ClipboardTextItem clipboard = this.createObject();
        assertSame(
                clipboard,
                clipboard.setTypes(TYPES)
        );
    }

    @Test
    public void testSetTypesWithDifferent() {
        final ClipboardTextItem clipboard = this.createObject();

        final List<MediaType> differentTypes = Lists.of(
                MediaType.ANY_TEXT
        );
        final ClipboardTextItem different = clipboard.setTypes(differentTypes);

        assertNotSame(
                clipboard,
                different
        );

        this.checkTypes(clipboard);
        this.checkText(clipboard);

        this.checkTypes(different, differentTypes);
        this.checkText(different);
    }

    // setTexts.........................................................................................................

    @Test
    public void testSetTextsWithNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createObject().setText(null)
        );
    }

    @Test
    public void testSetTextsWithSame() {
        final ClipboardTextItem clipboard = this.createObject();
        assertSame(
                clipboard,
                clipboard.setText(TEXT)
        );
    }

    @Test
    public void testSetTextWithDifferent() {
        final ClipboardTextItem clipboard = this.createObject();

        final String differentText = "different";
        final ClipboardTextItem different = clipboard.setText(differentText);

        assertNotSame(
                clipboard,
                different
        );

        this.checkTypes(clipboard);
        this.checkText(clipboard);

        this.checkTypes(different);
        this.checkText(different, differentText);
    }

    // toJson.........................................................................................................

    @Test
    public void testToJsonWithNullRangeFails() {
        assertThrows(
                NullPointerException.class,
                () -> ClipboardTextItem.toJson(
                        null, // cell-range
                        Iterators.fake(), // cells,
                        SpreadsheetCellClipboardKind.CELL,
                        JsonNodeMarshallContexts.fake()
                )
        );
    }

    @Test
    public void testToJsonWithNullCellsFails() {
        assertThrows(
                NullPointerException.class,
                () -> ClipboardTextItem.toJson(
                        SpreadsheetSelection.ALL_CELLS,
                        null, // cells,
                        SpreadsheetCellClipboardKind.CELL,
                        JsonNodeMarshallContexts.fake()
                )
        );
    }

    @Test
    public void testToJsonWithNullKindFails() {
        assertThrows(
                NullPointerException.class,
                () -> ClipboardTextItem.toJson(
                        SpreadsheetSelection.ALL_CELLS,
                        Iterators.fake(), // cells,
                        null,
                        JsonNodeMarshallContexts.fake()
                )
        );
    }

    @Test
    public void testToJsonWithNullJsonNodeMarshallContextFails() {
        assertThrows(
                NullPointerException.class,
                () -> ClipboardTextItem.toJson(
                        SpreadsheetSelection.ALL_CELLS,
                        null, // cells,
                        SpreadsheetCellClipboardKind.CELL,
                        null
                )
        );
    }

    @Test
    public void testToJsonCellsCellsOutsideFails() {
        final IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> ClipboardTextItem.toJson(
                        SpreadsheetSelection.parseCellRange("B2:C3"),
                        Iterators.array(
                                SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY),
                                SpreadsheetSelection.parseCell("B2")
                                        .setFormula(SpreadsheetFormula.EMPTY),
                                SpreadsheetSelection.parseCell("C3")
                                        .setFormula(SpreadsheetFormula.EMPTY),
                                SpreadsheetSelection.parseCell("D4")
                                        .setFormula(SpreadsheetFormula.EMPTY)
                        ), // cells,
                        SpreadsheetCellClipboardKind.CELL,
                        JsonNodeMarshallContexts.basic()
                )
        );

        this.checkEquals(
                "Required all cells to be within range B2:C3 but got 2 cells: A1, D4",
                thrown.getMessage(),
                "message"
        );
    }

    @Test
    public void testToJsonCellsWithCellsNone() {
        this.toJsonAndCheck(
                "A1",
                Iterators.empty(),
                SpreadsheetCellClipboardKind.CELL,
                ClipboardTextItem.with(
                        TYPES,
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.spreadsheet.SpreadsheetCell\",\n" +
                                "  \"cell-range\": \"A1\",\n" +
                                "  \"value\": {}\n" +
                                "}"
                )
        );
    }

    @Test
    public void testToJsonCellsWithCellsOne() {
        this.toJsonAndCheck(
                "A1",
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
                SpreadsheetCellClipboardKind.CELL,
                ClipboardTextItem.with(
                        TYPES,
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.spreadsheet.SpreadsheetCell\",\n" +
                                "  \"cell-range\": \"A1\",\n" +
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
    public void testToJsonCellsWithCellsSeveral() {
        this.toJsonAndCheck(
                "A1:B2",
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
                SpreadsheetCellClipboardKind.CELL,
                ClipboardTextItem.with(
                        TYPES,
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.spreadsheet.SpreadsheetCell\",\n" +
                                "  \"cell-range\": \"A1:B2\",\n" +
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
    public void testToJsonCellsFormulaNone() {
        this.toJsonAndCheck(
                "A1",
                Iterators.array(
                ),
                SpreadsheetCellClipboardKind.FORMULA,
                ClipboardTextItem.with(
                        TYPES,
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.spreadsheet.SpreadsheetFormula\",\n" +
                                "  \"cell-range\": \"A1\",\n" +
                                "  \"value\": {}\n" +
                                "}"
                )
        );
    }

    @Test
    public void testToJsonCellsFormulaSeveral() {
        this.toJsonAndCheck(
                "A1:B2",
                Iterators.array(
                        SpreadsheetSelection.A1.setFormula(
                                SpreadsheetFormula.EMPTY.setText("=1")
                        ),
                        SpreadsheetSelection.parseCell("B2")
                                .setFormula(
                                        SpreadsheetFormula.EMPTY.setText("=22")
                                )
                ),
                SpreadsheetCellClipboardKind.FORMULA,
                ClipboardTextItem.with(
                        TYPES,
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.spreadsheet.SpreadsheetFormula\",\n" +
                                "  \"cell-range\": \"A1:B2\",\n" +
                                "  \"value\": {\n" +
                                "    \"A1\": \"=1\",\n" +
                                "    \"B2\": \"=22\"\n" +
                                "  }\n" +
                                "}"
                )
        );
    }

    @Test
    public void testToJsonCellsFormulaEmpty() {
        this.toJsonAndCheck(
                "A1",
                Iterators.array(
                        SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                ),
                SpreadsheetCellClipboardKind.FORMULA,
                ClipboardTextItem.with(
                        TYPES,
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.spreadsheet.SpreadsheetFormula\",\n" +
                                "  \"cell-range\": \"A1\",\n" +
                                "  \"value\": {\n" +
                                "    \"A1\": \"\"\n" +
                                "  }\n" +
                                "}"
                )
        );
    }

    @Test
    public void testToJsonCellsFormatPatternNone() {
        this.toJsonAndCheck(
                "A1",
                Iterators.array(
                ),
                SpreadsheetCellClipboardKind.FORMAT_PATTERN,
                ClipboardTextItem.with(
                        TYPES,
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern\",\n" +
                                "  \"cell-range\": \"A1\",\n" +
                                "  \"value\": {}\n" +
                                "}"
                )
        );
    }

    @Test
    public void testToJsonCellsFormatPatternMissing() {
        this.toJsonAndCheck(
                "A1:B2",
                Iterators.array(
                        SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                ),
                SpreadsheetCellClipboardKind.FORMAT_PATTERN,
                ClipboardTextItem.with(
                        TYPES,
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern\",\n" +
                                "  \"cell-range\": \"A1:B2\",\n" +
                                "  \"value\": {\n" +
                                "    \"A1\": null\n" +
                                "  }\n" +
                                "}"
                )
        );
    }

    @Test
    public void testToJsonCellsFormatPatternSomeMissing() {
        this.toJsonAndCheck(
                "A1:B2",
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
                SpreadsheetCellClipboardKind.FORMAT_PATTERN,
                ClipboardTextItem.with(
                        TYPES,
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern\",\n" +
                                "  \"cell-range\": \"A1:B2\",\n" +
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
    public void testToJsonCellsFormatPattern() {
        this.toJsonAndCheck(
                "A1:B2",
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
                SpreadsheetCellClipboardKind.FORMAT_PATTERN,
                ClipboardTextItem.with(
                        TYPES,
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern\",\n" +
                                "  \"cell-range\": \"A1:B2\",\n" +
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
    public void testToJsonCellsParsePatternNone() {
        this.toJsonAndCheck(
                "A1:B2",
                Iterators.array(
                ),
                SpreadsheetCellClipboardKind.PARSE_PATTERN,
                ClipboardTextItem.with(
                        TYPES,
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern\",\n" +
                                "  \"cell-range\": \"A1:B2\",\n" +
                                "  \"value\": {}\n" +
                                "}"
                )
        );
    }

    @Test
    public void testToJsonCellsParsePatternMissing() {
        this.toJsonAndCheck(
                "A1:B2",
                Iterators.array(
                        SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                ),
                SpreadsheetCellClipboardKind.PARSE_PATTERN,
                ClipboardTextItem.with(
                        TYPES,
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern\",\n" +
                                "  \"cell-range\": \"A1:B2\",\n" +
                                "  \"value\": {\n" +
                                "    \"A1\": null\n" +
                                "  }\n" +
                                "}"
                )
        );
    }

    @Test
    public void testToJsonCellsParsePatternSomeMissing() {
        this.toJsonAndCheck(
                "A1:B2",
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
                SpreadsheetCellClipboardKind.PARSE_PATTERN,
                ClipboardTextItem.with(
                        TYPES,
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern\",\n" +
                                "  \"cell-range\": \"A1:B2\",\n" +
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
    public void testToJsonCellsParsePattern() {
        this.toJsonAndCheck(
                "A1:B2",
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
                SpreadsheetCellClipboardKind.PARSE_PATTERN,
                ClipboardTextItem.with(
                        TYPES,
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern\",\n" +
                                "  \"cell-range\": \"A1:B2\",\n" +
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
    public void testToJsonCellsStyleNone() {
        this.toJsonAndCheck(
                "A1:B2",
                Iterators.array(
                ),
                SpreadsheetCellClipboardKind.STYLE,
                ClipboardTextItem.with(
                        TYPES,
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.tree.text.TextStyle\",\n" +
                                "  \"cell-range\": \"A1:B2\",\n" +
                                "  \"value\": {}\n" +
                                "}"
                )
        );
    }

    @Test
    public void testToJsonCellsStyleEmpty() {
        this.toJsonAndCheck(
                "A1:C3",
                Iterators.array(
                        SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                ),
                SpreadsheetCellClipboardKind.STYLE,
                ClipboardTextItem.with(
                        TYPES,
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.tree.text.TextStyle\",\n" +
                                "  \"cell-range\": \"A1:C3\",\n" +
                                "  \"value\": {\n" +
                                "    \"A1\": {}\n" +
                                "  }\n" +
                                "}"
                )
        );
    }

    @Test
    public void testToJsonCellsStyleSomeEmpty() {
        this.toJsonAndCheck(
                "A1:B2",
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
                SpreadsheetCellClipboardKind.STYLE,
                ClipboardTextItem.with(
                        TYPES,
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.tree.text.TextStyle\",\n" +
                                "  \"cell-range\": \"A1:B2\",\n" +
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
    public void testToJsonCellsStyle() {
        this.toJsonAndCheck(
                "A1:B2",
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
                SpreadsheetCellClipboardKind.STYLE,
                ClipboardTextItem.with(
                        TYPES,
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.tree.text.TextStyle\",\n" +
                                "  \"cell-range\": \"A1:B2\",\n" +
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
    public void testToJsonCellsFormattedNone() {
        this.toJsonAndCheck(
                "*",
                Iterators.array(
                ),
                SpreadsheetCellClipboardKind.FORMATTED_VALUE,
                ClipboardTextItem.with(
                        TYPES,
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.tree.text.TextNode\",\n" +
                                "  \"cell-range\": \"*\",\n" +
                                "  \"value\": {}\n" +
                                "}"
                )
        );
    }

    @Test
    public void testToJsonCellsFormattedSeveral() {
        this.toJsonAndCheck(
                "A1:B2",
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
                SpreadsheetCellClipboardKind.FORMATTED_VALUE,
                ClipboardTextItem.with(
                        TYPES,
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.tree.text.TextNode\",\n" +
                                "  \"cell-range\": \"A1:B2\",\n" +
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
    public void testToJsonCellsFormattedMissing() {
        this.toJsonAndCheck(
                "A1",
                Iterators.array(
                        SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                ),
                SpreadsheetCellClipboardKind.FORMATTED_VALUE,
                ClipboardTextItem.with(
                        TYPES,
                        "{\n" +
                                "  \"mediaType\": \"application/json+walkingkooka.tree.text.TextNode\",\n" +
                                "  \"cell-range\": \"A1\",\n" +
                                "  \"value\": {\n" +
                                "    \"A1\": null\n" +
                                "  }\n" +
                                "}"
                )
        );
    }

    private void toJsonAndCheck(final String range,
                                final Iterator<SpreadsheetCell> cells,
                                final SpreadsheetCellClipboardKind kind,
                                final ClipboardTextItem expected) {
        this.toJsonAndCheck(
                SpreadsheetSelection.parseCellRange(range),
                cells,
                kind,
                JsonNodeMarshallContexts.basic(),
                expected
        );
    }

    private void toJsonAndCheck(final SpreadsheetCellRangeReference range,
                                final Iterator<SpreadsheetCell> cells,
                                final SpreadsheetCellClipboardKind kind,
                                final JsonNodeMarshallContext context,
                                final ClipboardTextItem expected) {
        this.checkEquals(
                expected,
                ClipboardTextItem.toJson(
                        range,
                        cells,
                        kind,
                        context
                )
        );
    }

    // toSpreadsheetCellRange........................................................................................

    @Test
    public void testToSpreadsheetCellRangeNullContextFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createObject().toSpreadsheetCellRange(null)
        );
    }

    @Test
    public void testToSpreadsheetCellRangeInvalidMediaType() {
        final IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> ClipboardTextItem.with(
                                Lists.of(
                                        MediaType.IMAGE_BMP
                                ),
                                "{\n" +
                                        "  \"mediaType\": \"text/invalid123\",\n" +
                                        "  \"cell-range\": \"A1:B2\",\n" +
                                        "  \"value\": true\n" +
                                        "}"
                        )
                        .toSpreadsheetCellRange(
                                new FakeAppContext() {
                                    @Override
                                    public JsonNodeUnmarshallContext unmarshallContext() {
                                        return METADATA_EN_AU.jsonNodeUnmarshallContext();
                                    }
                                })
        );

        this.checkEquals(
                "Unsupported clipboard media type image/bmp expected text/plain",
                thrown.getMessage()
        );
    }

    private final static SpreadsheetCellReference B2 = SpreadsheetSelection.parseCell("b2");

    @Test
    public void testToSpreadsheetCellRange() {
        this.toSpreadsheetCellRangeAndCheck(
                "{\n" +
                        "  \"mediaType\": \"application/json+walkingkooka.spreadsheet.SpreadsheetCell\",\n" +
                        "  \"cell-range\": \"A1:B2\",\n" +
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
                        "}",
                SpreadsheetSelection.parseCellRange("A1:B2")
                        .setValue(
                                Sets.of(
                                        SpreadsheetSelection.A1.setFormula(
                                                SpreadsheetMetadataTesting.parseFormula("=1")
                                        ).setStyle(
                                                TextStyle.EMPTY.set(
                                                        TextStylePropertyName.TEXT_ALIGN,
                                                        TextAlign.CENTER
                                                )
                                        )
                                )
                        )
        );
    }

    @Test
    public void testToSpreadsheetCellRange2() {
        this.toSpreadsheetCellRangeAndCheck(
                "{\n" +
                        "  \"mediaType\": \"application/json+walkingkooka.spreadsheet.SpreadsheetCell\",\n" +
                        "  \"cell-range\": \"A1:B2\",\n" +
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
                        "}",
                SpreadsheetSelection.parseCellRange("A1:B2")
                        .setValue(
                                Sets.of(
                                        SpreadsheetSelection.A1.setFormula(
                                                SpreadsheetMetadataTesting.parseFormula("=1")
                                        ).setStyle(
                                                TextStyle.EMPTY.set(
                                                        TextStylePropertyName.TEXT_ALIGN,
                                                        TextAlign.CENTER
                                                )
                                        ),
                                        B2.setFormula(
                                                SpreadsheetMetadataTesting.parseFormula("=22")
                                        ).setFormatPattern(
                                                Optional.of(
                                                        SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN
                                                )
                                        )
                                )
                        )
        );
    }

    private void toSpreadsheetCellRangeAndCheck(final String text,
                                                final SpreadsheetCellRange expected) {
        this.checkEquals(
                expected,
                ClipboardTextItem.with(
                                Lists.of(ClipboardTextItem.MEDIA_TYPE),
                                text
                        )
                        .toSpreadsheetCellRange(
                                new FakeAppContext() {
                                    @Override
                                    public JsonNodeUnmarshallContext unmarshallContext() {
                                        return METADATA_EN_AU.jsonNodeUnmarshallContext();
                                    }

                                    @Override
                                    public SpreadsheetMetadata spreadsheetMetadata() {
                                        return METADATA_EN_AU;
                                    }
                                })
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
                        TEXT
                )
        );
    }

    @Test
    public void testEqualsDifferentText() {
        this.checkNotEquals(
                ClipboardTextItem.with(
                        TYPES,
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
                        TYPES,
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
                TYPES,
                TEXT
        );
    }

    private void checkTypes(final ClipboardTextItem clipboardTextItem) {
        this.checkTypes(
                clipboardTextItem,
                TYPES
        );
    }

    private void checkTypes(final ClipboardTextItem clipboardTextItem,
                            final List<MediaType> expected) {
        this.checkEquals(
                expected,
                clipboardTextItem.types(),
                "types"
        );
    }

    private void checkText(final ClipboardTextItem clipboardTextItem) {
        this.checkText(
                clipboardTextItem,
                TEXT
        );
    }

    private void checkText(final ClipboardTextItem clipboardTextItem,
                           final String expected) {
        this.checkEquals(
                expected,
                clipboardTextItem.text(),
                "text"
        );
    }
}
