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

package walkingkooka.spreadsheet.dominokit.ui.pattern;

import org.junit.jupiter.api.Test;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.format.SpreadsheetText;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.tree.text.TextNode;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetPatternComponentTableComponentRowTest implements ClassTesting<SpreadsheetPatternComponentTableComponentRow>,
        HashCodeEqualsDefinedTesting2<SpreadsheetPatternComponentTableComponentRow>,
        ToStringTesting<SpreadsheetPatternComponentTableComponentRow> {

    private final static String LABEL = "Label123";

    private final static Optional<SpreadsheetPattern> PATTERN = Optional.of(
            SpreadsheetPattern.parseTextFormatPattern("@")
    );

    private final static TextNode PATTERN_FORMATTED_VALUE = SpreadsheetText.with("pattern formatted value")
            .toTextNode();

    // dateFormat.......................................................................................................

    @Test
    public void testWithNullLabelFails() {
        this.withFails(
                null,
                PATTERN,
                PATTERN_FORMATTED_VALUE
        );
    }

    @Test
    public void testWithEmptyLabelFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> SpreadsheetPatternComponentTableComponentRow.with(
                        "",
                        PATTERN,
                        PATTERN_FORMATTED_VALUE
                )
        );
    }

    @Test
    public void testWithNullPatternFails() {
        this.withFails(
                LABEL,
                null,
                PATTERN_FORMATTED_VALUE
        );
    }

    @Test
    public void testWithNullPatternFormattedValueFails() {
        this.withFails(
                LABEL,
                PATTERN,
                null
        );
    }

    private void withFails(final String label,
                           final Optional<SpreadsheetPattern> pattern,
                           final TextNode patternFormattedValue) {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetPatternComponentTableComponentRow.with(
                        label,
                        pattern,
                        patternFormattedValue
                )
        );
    }
    // equals............................................................................................................

    @Test
    public void testEqualsDifferentLabel() {
        this.checkNotEquals(
                SpreadsheetPatternComponentTableComponentRow.with(
                        "different",
                        PATTERN,
                        PATTERN_FORMATTED_VALUE
                )
        );
    }

    @Test
    public void testEqualsDifferentPattern() {
        this.checkNotEquals(
                SpreadsheetPatternComponentTableComponentRow.with(
                        LABEL,
                        Optional.of(
                                SpreadsheetPattern.parseTextFormatPattern("\"different\"")
                        ),
                        PATTERN_FORMATTED_VALUE
                )
        );
    }

    @Test
    public void testEqualsDifferentPatternFormattedValue() {
        this.checkNotEquals(
                SpreadsheetPatternComponentTableComponentRow.with(
                        LABEL,
                        PATTERN,
                        SpreadsheetText.with("different").toTextNode()
                )
        );
    }

    @Override
    public SpreadsheetPatternComponentTableComponentRow createObject() {
        return SpreadsheetPatternComponentTableComponentRow.with(
                LABEL,
                PATTERN,
                PATTERN_FORMATTED_VALUE
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                SpreadsheetPatternComponentTableComponentRow.with(
                        LABEL,
                        PATTERN,
                        PATTERN_FORMATTED_VALUE
                ),
                "Label123 | @ | pattern formatted value"
        );
    }

    @Test
    public void testToStringEmptyPattern() {
        this.toStringAndCheck(
                SpreadsheetPatternComponentTableComponentRow.with(
                        LABEL,
                        Optional.empty(),
                        PATTERN_FORMATTED_VALUE
                ),
                "Label123 | | pattern formatted value"
        );
    }

    @Test
    public void testToStringAllEmpty() {
        this.toStringAndCheck(
                SpreadsheetPatternComponentTableComponentRow.with(
                        LABEL,
                        Optional.empty(),
                        SpreadsheetText.EMPTY.toTextNode()
                ),
                "Label123 | |"
        );
    }

    // ClassVisibility..................................................................................................

    @Override
    public Class<SpreadsheetPatternComponentTableComponentRow> type() {
        return SpreadsheetPatternComponentTableComponentRow.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
