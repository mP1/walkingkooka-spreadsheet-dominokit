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

import walkingkooka.text.CharSequences;
import walkingkooka.tree.text.TextNode;

import java.util.Objects;

/**
 * Represents a row of data for the sample table that appears within a {@link SpreadsheetPatternEditorComponent}.
 */
final class SpreadsheetPatternEditorComponentSampleRow {

    /**
     * Factory that creates a new {@link SpreadsheetPatternEditorComponentSampleRow}.
     */
    static SpreadsheetPatternEditorComponentSampleRow with(final String label,
                                                           final String patternText,
                                                           final TextNode defaultFormattedValue,
                                                           final TextNode patternFormattedValue) {
        return new SpreadsheetPatternEditorComponentSampleRow(
                CharSequences.failIfNullOrEmpty(label, "label"),
                Objects.requireNonNull(patternText, "patternText"),
                Objects.requireNonNull(defaultFormattedValue, "defaultFormattedValue"),
                Objects.requireNonNull(patternFormattedValue, "patternFormattedValue")
        );
    }

    private SpreadsheetPatternEditorComponentSampleRow(final String label,
                                                       final String patternText,
                                                       final TextNode defaultFormattedValue,
                                                       final TextNode patternFormattedValue) {
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
    TextNode defaultFormattedValue() {
        return this.defaultFormattedValue;
    }

    private final TextNode defaultFormattedValue;

    /**
     * The value formatted using the {@link #patternText()}.
     */
    TextNode patternFormattedValue() {
        return this.patternFormattedValue;

    }

    private final TextNode patternFormattedValue;

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
        return this == other || other instanceof SpreadsheetPatternEditorComponentSampleRow && this.equals0((SpreadsheetPatternEditorComponentSampleRow) other);
    }

    private boolean equals0(final SpreadsheetPatternEditorComponentSampleRow other) {
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
