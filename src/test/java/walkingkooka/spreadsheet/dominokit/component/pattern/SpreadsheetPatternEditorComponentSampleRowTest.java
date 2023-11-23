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

package walkingkooka.spreadsheet.dominokit.component.pattern;

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

public final class SpreadsheetPatternEditorComponentSampleRowTest implements ClassTesting<SpreadsheetPatternEditorComponentSampleRow>,
        HashCodeEqualsDefinedTesting2<SpreadsheetPatternEditorComponentSampleRow>,
        ToStringTesting<SpreadsheetPatternEditorComponentSampleRow> {

    private final static String LABEL = "Label123";

    private final static Optional<SpreadsheetPattern> PATTERN = Optional.of(
            SpreadsheetPattern.parseTextFormatPattern("@")
    );

    private final static TextNode DEFAULT_FORMATTED_VALUE = SpreadsheetText.with("default formatted value")
            .toTextNode();

    private final static TextNode PATTERN_FORMATTED_VALUE = SpreadsheetText.with("pattern formatted value")
            .toTextNode();

    // dateFormat.......................................................................................................

    @Test
    public void testWithNullLabelFails() {
        this.withFails(
                null,
                PATTERN,
                DEFAULT_FORMATTED_VALUE,
                PATTERN_FORMATTED_VALUE
        );
    }

    @Test
    public void testWithEmptyLabelFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> SpreadsheetPatternEditorComponentSampleRow.with(
                        "",
                        PATTERN,
                        DEFAULT_FORMATTED_VALUE,
                        PATTERN_FORMATTED_VALUE
                )
        );
    }

    @Test
    public void testWithNullPatternFails() {
        this.withFails(
                LABEL,
                null,
                DEFAULT_FORMATTED_VALUE,
                PATTERN_FORMATTED_VALUE
        );
    }

    @Test
    public void testWithNullDefaultFormattedValueFails() {
        this.withFails(
                LABEL,
                PATTERN,
                null,
                PATTERN_FORMATTED_VALUE
        );
    }

    @Test
    public void testWithNullPatternFormattedValueFails() {
        this.withFails(
                LABEL,
                PATTERN,
                DEFAULT_FORMATTED_VALUE,
                null
        );
    }

    private void withFails(final String label,
                           final Optional<SpreadsheetPattern> pattern,
                           final TextNode defaultFormattedValue,
                           final TextNode patternFormattedValue) {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetPatternEditorComponentSampleRow.with(
                        label,
                        pattern,
                        defaultFormattedValue,
                        patternFormattedValue
                )
        );
    }
    // equals............................................................................................................

    @Test
    public void testEqualsDifferentLabel() {
        this.checkNotEquals(
                SpreadsheetPatternEditorComponentSampleRow.with(
                        "different",
                        PATTERN,
                        DEFAULT_FORMATTED_VALUE,
                        PATTERN_FORMATTED_VALUE
                )
        );
    }

    @Test
    public void testEqualsDifferentPattern() {
        this.checkNotEquals(
                SpreadsheetPatternEditorComponentSampleRow.with(
                        LABEL,
                        Optional.of(
                                SpreadsheetPattern.parseTextFormatPattern("\"different\"")
                        ),
                        DEFAULT_FORMATTED_VALUE,
                        PATTERN_FORMATTED_VALUE
                )
        );
    }

    @Test
    public void testEqualsDifferentDefaultFormattedValue() {
        this.checkNotEquals(
                SpreadsheetPatternEditorComponentSampleRow.with(
                        LABEL,
                        PATTERN,
                        SpreadsheetText.with("different")
                                .toTextNode(),
                        PATTERN_FORMATTED_VALUE
                )
        );
    }

    @Test
    public void testEqualsDifferentPatternFormattedValue() {
        this.checkNotEquals(
                SpreadsheetPatternEditorComponentSampleRow.with(
                        LABEL,
                        PATTERN,
                        DEFAULT_FORMATTED_VALUE,
                        SpreadsheetText.with("different").toTextNode()
                )
        );
    }

    @Override
    public SpreadsheetPatternEditorComponentSampleRow createObject() {
        return SpreadsheetPatternEditorComponentSampleRow.with(
                LABEL,
                PATTERN,
                DEFAULT_FORMATTED_VALUE,
                PATTERN_FORMATTED_VALUE
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                SpreadsheetPatternEditorComponentSampleRow.with(
                        LABEL,
                        PATTERN,
                        DEFAULT_FORMATTED_VALUE,
                        PATTERN_FORMATTED_VALUE
                ),
                "Label123 | @ | default formatted value | pattern formatted value"
        );
    }

    @Test
    public void testToStringEmptyPattern() {
        this.toStringAndCheck(
                SpreadsheetPatternEditorComponentSampleRow.with(
                        LABEL,
                        Optional.empty(),
                        DEFAULT_FORMATTED_VALUE,
                        PATTERN_FORMATTED_VALUE
                ),
                "Label123 | | default formatted value | pattern formatted value"
        );
    }

    @Test
    public void testToStringAllEmpty() {
        this.toStringAndCheck(
                SpreadsheetPatternEditorComponentSampleRow.with(
                        LABEL,
                        Optional.empty(),
                        SpreadsheetText.EMPTY
                                .toTextNode(),
                        SpreadsheetText.EMPTY.toTextNode()
                ),
                "Label123 | | |"
        );
    }

    // ClassVisibility..................................................................................................

    @Override
    public Class<SpreadsheetPatternEditorComponentSampleRow> type() {
        return SpreadsheetPatternEditorComponentSampleRow.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
