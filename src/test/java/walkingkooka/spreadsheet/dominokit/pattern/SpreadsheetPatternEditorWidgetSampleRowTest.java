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

package walkingkooka.spreadsheet.dominokit.pattern;

import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.format.SpreadsheetText;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetPatternEditorWidgetSampleRowTest implements ClassTesting<SpreadsheetPatternEditorWidgetSampleRow>,
        ToStringTesting<SpreadsheetPatternEditorWidgetSampleRow> {

    private final static String LABEL = "Label123";

    private final static String PATTERN_TEXT = "@";

    private final static SpreadsheetText DEFAULT_FORMATTED_VALUE = SpreadsheetText.with("default formatted value");

    private final static SpreadsheetText PATTERN_FORMATTED_VALUE = SpreadsheetText.with("pattern formatted value");

    // dateFormat.......................................................................................................

    @Test
    public void testWithNullLabelFails() {
        this.withFails(
                null,
                PATTERN_TEXT,
                DEFAULT_FORMATTED_VALUE,
                PATTERN_FORMATTED_VALUE
        );
    }

    @Test
    public void testWithEmptyLabelFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> SpreadsheetPatternEditorWidgetSampleRow.with(
                        "",
                        PATTERN_TEXT,
                        DEFAULT_FORMATTED_VALUE,
                        PATTERN_FORMATTED_VALUE
                )
        );
    }

    @Test
    public void testWithNullPatternTextFails() {
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
                PATTERN_TEXT,
                null,
                PATTERN_FORMATTED_VALUE
        );
    }

    @Test
    public void testWithNullPatternFormattedValueFails() {
        this.withFails(
                LABEL,
                PATTERN_TEXT,
                DEFAULT_FORMATTED_VALUE,
                null
        );
    }

    private void withFails(final String label,
                           final String patternText,
                           final SpreadsheetText defaultFormattedValue,
                           final SpreadsheetText patternFormattedValue) {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetPatternEditorWidgetSampleRow.with(
                        label,
                        patternText,
                        defaultFormattedValue,
                        patternFormattedValue
                )
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                SpreadsheetPatternEditorWidgetSampleRow.with(
                        LABEL,
                        PATTERN_TEXT,
                        DEFAULT_FORMATTED_VALUE,
                        PATTERN_FORMATTED_VALUE
                ),
                "\"" + LABEL + "\" | \"" + PATTERN_TEXT + "\" | " + DEFAULT_FORMATTED_VALUE + " | " + PATTERN_FORMATTED_VALUE
        );
    }

    // ClassVisibility..................................................................................................

    @Override
    public Class<SpreadsheetPatternEditorWidgetSampleRow> type() {
        return SpreadsheetPatternEditorWidgetSampleRow.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
