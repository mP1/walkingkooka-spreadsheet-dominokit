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

import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.text.TextNode;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a row of data for the sample table that appears within a {@link SpreadsheetPatternComponent}.
 */
final class SpreadsheetPatternComponentTableRow {

    /**
     * Factory that creates a new {@link SpreadsheetPatternComponentTableRow}.
     */
    static SpreadsheetPatternComponentTableRow with(final String label,
                                                    final Optional<SpreadsheetPattern> pattern,
                                                    final List<TextNode> formatted) {
        return new SpreadsheetPatternComponentTableRow(
                CharSequences.failIfNullOrEmpty(label, "label"),
                Objects.requireNonNull(pattern, "pattern"),
                Objects.requireNonNull(formatted, "formatted")
        );
    }

    private SpreadsheetPatternComponentTableRow(final String label,
                                                final Optional<SpreadsheetPattern> pattern,
                                                final List<TextNode> formatted) {
        this.label = label;
        this.pattern = pattern;
        this.formatted = formatted;
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
    Optional<SpreadsheetPattern> pattern() {
        return this.pattern;
    }

    private final Optional<SpreadsheetPattern> pattern;

    /**
     * The value formatted using the {@link #pattern()}.
     */
    List<TextNode> formatted() {
        return this.formatted;
    }

    private final List<TextNode> formatted;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(
                this.label,
                this.pattern,
                this.formatted
        );
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof SpreadsheetPatternComponentTableRow && this.equals0((SpreadsheetPatternComponentTableRow) other);
    }

    private boolean equals0(final SpreadsheetPatternComponentTableRow other) {
        return this.label.equals(other.label) &&
                this.pattern.equals(other.pattern) &&
                this.formatted.equals(other.formatted);
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
            final Optional<SpreadsheetPattern> pattern = this.pattern;
            if (false == pattern.isEmpty()) {
                b.append(
                        pattern.get()
                                .text()
                ).append(' ');
            }
            b.append("| ");
        }

        {
            String separator = "";
            for (final TextNode f : this.formatted) {
                b.append(separator);
                separator = " | ";

                b.append(f.text());
            }
        }

        return b.toString()
                .trim();
    }
}
