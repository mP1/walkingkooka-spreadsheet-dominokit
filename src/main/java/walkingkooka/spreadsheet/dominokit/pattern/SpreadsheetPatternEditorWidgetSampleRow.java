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

import walkingkooka.spreadsheet.format.SpreadsheetText;
import walkingkooka.text.CharSequences;

import java.util.Objects;

/**
 * Represents a row of data for the sample table that appears within a {@link SpreadsheetPatternEditorWidget}.
 */
final class SpreadsheetPatternEditorWidgetSampleRow {

    /**
     * Factory that creates a new {@link SpreadsheetPatternEditorWidgetSampleRow}.
     */
    static SpreadsheetPatternEditorWidgetSampleRow with(final String label,
                                                        final String patternText,
                                                        final SpreadsheetText defaultFormattedValue,
                                                        final SpreadsheetText patternFormattedValue) {
        return new SpreadsheetPatternEditorWidgetSampleRow(
                CharSequences.failIfNullOrEmpty(label, "label"),
                Objects.requireNonNull(patternText, "patternText"),
                Objects.requireNonNull(defaultFormattedValue, "defaultFormattedValue"),
                Objects.requireNonNull(patternFormattedValue, "patternFormattedValue")
        );
    }

    private SpreadsheetPatternEditorWidgetSampleRow(final String label,
                                                    final String patternText,
                                                    final SpreadsheetText defaultFormattedValue,
                                                    final SpreadsheetText patternFormattedValue) {
        this.label = label;
        this.patternText = patternText;
        this.defaultFormattedValue = defaultFormattedValue;
        this.patternFormattedValue = patternFormattedValue;
    }

    /**
     * The label that appears in the first column.
     */
    String label() {
        return this.label;
    }

    private final String label;

    /**
     * The pattern text that appears in the 2nd column.
     */
    String patternText() {
        return this.patternText;
    }

    private final String patternText;

    /**
     * The value default formatted.
     */
    SpreadsheetText defaultFormattedValue() {
        return this.defaultFormattedValue;
    }

    private final SpreadsheetText defaultFormattedValue;

    /**
     * The value formatted using the {@link #patternText()}.
     */
    SpreadsheetText patternFormattedValue() {
        return this.patternFormattedValue;

    }

    private final SpreadsheetText patternFormattedValue;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(
                this.label,
                this.patternText,
                this.defaultFormattedValue,
                this.patternFormattedValue
        );
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof SpreadsheetPatternEditorWidgetSampleRow && this.equals0((SpreadsheetPatternEditorWidgetSampleRow) other);
    }

    private boolean equals0(final SpreadsheetPatternEditorWidgetSampleRow other) {
        return this.label.equals(other.label) &&
                this.patternText.equals(other.patternText) &&
                this.defaultFormattedValue.equals(other.defaultFormattedValue) &&
                this.patternFormattedValue.equals(other.patternFormattedValue);
    }

    /**
     * Produces a row with each of the properties separated by the PIPE SYMBOL.
     * <pre>
     * Label123 | dd/yyyy/mm | 31/12/1999 | 31/1999/12
     * </pre>
     */
    @Override
    public String toString() {
        final StringBuilder b = new StringBuilder();

        {
            final String label = this.label;
            if (false == label.isEmpty()) {
                b.append(label);
            }
            b.append(" | ");
        }

        {
            final String patternText = this.patternText;
            if (false == patternText.isEmpty()) {
                b.append(patternText)
                        .append(' ');
            }
            b.append("| ");
        }

        {
            final String defaultFormattedValue = this.defaultFormattedValue.text();
            if (false == defaultFormattedValue.isEmpty()) {
                b.append(defaultFormattedValue)
                        .append(' ');
            }
            b.append("| ");
        }

        {
            final String patternFormattedValue = this.patternFormattedValue.text();
            if (false == patternFormattedValue.isEmpty()) {
                b.append(patternFormattedValue);
            }
        }

        return b.toString()
                .trim();
    }
}
