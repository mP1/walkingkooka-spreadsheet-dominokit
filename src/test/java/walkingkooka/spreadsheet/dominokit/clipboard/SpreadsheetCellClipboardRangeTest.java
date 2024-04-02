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
import walkingkooka.Cast;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.collect.map.Maps;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellClipboardRangeTest implements ClassTesting<SpreadsheetCellClipboardRange<Object>>,
        HashCodeEqualsDefinedTesting2<SpreadsheetCellClipboardRange<Object>>,
        TreePrintableTesting {

    private final static SpreadsheetCellRange RANGE = SpreadsheetSelection.parseCellRange("A1:b2");

    private final static SpreadsheetCellReference B2 = SpreadsheetSelection.parseCell("B2");

    private final static Map<SpreadsheetCellReference, Object> VALUE = Maps.of(
            SpreadsheetSelection.A1,
            SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY.setText("=1")),
            B2,
            B2.setFormula(SpreadsheetFormula.EMPTY.setText("=22"))
                    .setFormatPattern(
                            Optional.of(
                                    SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN
                            )
                    ).setParsePattern(
                            Optional.of(
                                    SpreadsheetPattern.parseNumberParsePattern("#.##")
                            )
                    ).setStyle(
                            TextStyle.EMPTY.set(
                                    TextStylePropertyName.TEXT_ALIGN,
                                    TextAlign.LEFT
                            )
                    )
    );

    @Test
    public void testWithNullRangeFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetCellClipboardRange.with(
                        null,
                        VALUE
                )
        );
    }

    @Test
    public void testWithNullValueFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetCellClipboardRange.with(
                        RANGE,
                        null
                )
        );
    }

    @Test
    public void testWithValueCellOutOfBoundsFails() {
        final SpreadsheetCellReference c3 = SpreadsheetSelection.parseCell("$C$3");
        final SpreadsheetCellReference d4 = SpreadsheetSelection.parseCell("D4");

        final IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> SpreadsheetCellClipboardRange.with(
                        RANGE,
                        Maps.of(
                                SpreadsheetSelection.A1,
                                SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY),
                                c3,
                                c3.setFormula(SpreadsheetFormula.EMPTY),
                                d4,
                                d4.setFormula(SpreadsheetFormula.EMPTY)
                        )
                )
        );

        this.checkEquals(
                "Found 2 cells out of range A1:B2 got $C$3, D4",
                thrown.getMessage(),
                "message"
        );
    }

    @Test
    public void testWith() {
        final SpreadsheetCellClipboardRange<?> spreadsheetCellClipboardRange = SpreadsheetCellClipboardRange.with(
                RANGE,
                VALUE
        );
        this.checkRange(spreadsheetCellClipboardRange);
        this.checkValue(spreadsheetCellClipboardRange);
    }


    // setRange.........................................................................................................

    @Test
    public void testSetRangeNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createObject().setRange(null)
        );
    }

    @Test
    public void testSetRangeSame() {
        final SpreadsheetCellClipboardRange<?> spreadsheetCellClipboardRange = this.createObject();

        assertSame(
                spreadsheetCellClipboardRange,
                spreadsheetCellClipboardRange.setRange(RANGE)
        );
    }

    @Test
    public void testSetRangeOutOfBoundsFails() {
        final IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> this.createObject()
                        .setRange(SpreadsheetSelection.parseCellRange("A1"))
        );

        this.checkEquals(
                "Found 1 cells out of range A1 got B2",
                thrown.getMessage()
        );
    }

    @Test
    public void testSetRangeDifferentLarger() {
        final SpreadsheetCellClipboardRange<?> spreadsheetCellClipboardRange = this.createObject();
        final SpreadsheetCellRange differentRange = SpreadsheetSelection.parseCellRange("A1:C3");

        final SpreadsheetCellClipboardRange<?> different = spreadsheetCellClipboardRange.setRange(differentRange);

        assertNotSame(
                spreadsheetCellClipboardRange,
                different
        );

        this.checkRange(different, differentRange);
        this.checkValue(different);

        this.checkRange(spreadsheetCellClipboardRange);
        this.checkValue(spreadsheetCellClipboardRange);
    }

    @Test
    public void testSetRangeDifferentSmaller() {
        final SpreadsheetCellClipboardRange<?> spreadsheetCellClipboardRange = this.createObject();
        final SpreadsheetCellRange differentRange = SpreadsheetSelection.parseCellRange("A1:C3");

        final SpreadsheetCellClipboardRange<?> different = spreadsheetCellClipboardRange.setRange(differentRange)
                .setRange(RANGE);

        assertNotSame(
                spreadsheetCellClipboardRange,
                different
        );

        this.checkRange(different);
        this.checkValue(different);

        this.checkRange(spreadsheetCellClipboardRange);
        this.checkValue(spreadsheetCellClipboardRange);
    }

    // setValue.........................................................................................................

    @Test
    public void testSetValueNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createObject()
                        .setValue(null)
        );
    }

    @Test
    public void testSetValueSame() {
        final SpreadsheetCellClipboardRange<Object> spreadsheetCellClipboardRange = this.createObject();

        assertSame(
                spreadsheetCellClipboardRange,
                spreadsheetCellClipboardRange.setValue(VALUE)
        );
    }

    @Test
    public void testSetValueOutOfBoundsFails() {
        final SpreadsheetCellReference c3 = SpreadsheetSelection.parseCell("C3");

        final IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> this.createObject()
                        .setValue(
                                Maps.of(
                                        SpreadsheetSelection.A1,
                                        SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY),
                                        c3,
                                        c3.setFormula(SpreadsheetFormula.EMPTY.setText("=1"))
                                )
                        )
        );

        this.checkEquals(
                "Found 1 cells out of range A1:B2 got C3",
                thrown.getMessage()
        );
    }

    @Test
    public void testSetValueDifferent() {
        final SpreadsheetCellClipboardRange<Object> spreadsheetCellClipboardRange = this.createObject();
        final Map<SpreadsheetCellReference, Object> differentValue = Maps.of(
                SpreadsheetSelection.A1,
                SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY.setText("'different")
                )
        );

        final SpreadsheetCellClipboardRange<?> different = spreadsheetCellClipboardRange.setValue(differentValue);

        assertNotSame(
                spreadsheetCellClipboardRange,
                different
        );

        this.checkRange(different);
        this.checkValue(different, differentValue);

        this.checkRange(spreadsheetCellClipboardRange);
        this.checkValue(spreadsheetCellClipboardRange);
    }

    // helpers..........................................................................................................

    private void checkRange(final SpreadsheetCellClipboardRange<?> spreadsheetCellClipboardRange) {
        this.checkRange(
                spreadsheetCellClipboardRange,
                RANGE
        );
    }

    private void checkRange(final SpreadsheetCellClipboardRange<?> spreadsheetCellClipboardRange,
                            final SpreadsheetCellRange range) {
        this.checkEquals(
                range,
                spreadsheetCellClipboardRange.range()
        );
    }

    private void checkValue(final SpreadsheetCellClipboardRange<?> spreadsheetCellClipboardRange) {
        this.checkValue(
                spreadsheetCellClipboardRange,
                VALUE
        );
    }

    private void checkValue(final SpreadsheetCellClipboardRange<?> spreadsheetCellClipboardRange,
                            final Map<SpreadsheetCellReference, Object> value) {
        this.checkEquals(
                value,
                spreadsheetCellClipboardRange.value()
        );
    }

    // TreePrintable....................................................................................................

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
                this.createObject(),
                "A1:B2\n" +
                        "  A1\n" +
                        "    Cell A1\n" +
                        "      Formula\n" +
                        "        text: \"=1\"\n" +
                        "  B2\n" +
                        "    Cell B2\n" +
                        "      Formula\n" +
                        "        text: \"=22\"\n" +
                        "      formatPattern:\n" +
                        "        text-format-pattern\n" +
                        "          \"@\"\n" +
                        "      parsePattern:\n" +
                        "        number-parse-pattern\n" +
                        "          \"#.##\"\n" +
                        "      TextStyle\n" +
                        "        text-align=LEFT (walkingkooka.tree.text.TextAlign)\n"
        );
    }

    // ClassTesting....................................................................................................

    @Override
    public Class<SpreadsheetCellClipboardRange<Object>> type() {
        return Cast.to(SpreadsheetCellClipboardRange.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // HashCodeEqualsDefinedTesting2...................................................................................

    @Override
    public SpreadsheetCellClipboardRange<Object> createObject() {
        return SpreadsheetCellClipboardRange.with(
                RANGE,
                VALUE
        );
    }
}