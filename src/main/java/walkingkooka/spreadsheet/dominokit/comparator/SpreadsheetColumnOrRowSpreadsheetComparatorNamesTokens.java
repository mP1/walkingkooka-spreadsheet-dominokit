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

package walkingkooka.spreadsheet.dominokit.comparator;

import walkingkooka.ToStringBuilder;
import walkingkooka.ToStringBuilderOption;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetColumnOrRowSpreadsheetComparatorNames;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReferenceOrRange;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.HasText;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Assists decomposing text that potentially holds a {@link SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens}.
 */
final class SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens implements HasText {

    static SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens parse(final String text) {
        Objects.requireNonNull(text, "text");

        final int MODE_COLUMN_OR_ROW = 0;
        final int MODE_NAME = MODE_COLUMN_OR_ROW + 1;
        final int MODE_TEXT = MODE_NAME + 1;

        int mode = MODE_COLUMN_OR_ROW;

        final int length = text.length();

        int tokenStart = 0;
        SpreadsheetColumnOrRowReferenceOrRange columnOrRow = null;
        final List<SpreadsheetComparatorName> comparatorNames = Lists.array();

        int i = 0;

        while (i < length) {
            final char c = text.charAt(i);

            switch (mode) {
                case MODE_COLUMN_OR_ROW:
                    if (SpreadsheetColumnOrRowSpreadsheetComparatorNames.COLUMN_ROW_AND_COMPARATOR_NAME_SEPARATOR.character() == c) {
                        try {
                            columnOrRow = SpreadsheetSelection.parseColumnOrRow(
                                text.substring(
                                    tokenStart,
                                    i // without COLUMN_ROW_AND_COMPARATOR_NAME_SEPARATOR
                                )
                            );

                            tokenStart = i + 1;
                            mode = MODE_NAME;
                        } catch (final RuntimeException cause) {
                            mode = MODE_TEXT;
                            break;
                        }
                    }
                    break;
                case MODE_NAME:
                    if (SpreadsheetColumnOrRowSpreadsheetComparatorNames.COMPARATOR_NAME_SEPARATOR.character() == c) {
                        try {
                            comparatorNames.add(
                                SpreadsheetComparatorName.with(
                                    text.substring(
                                        tokenStart,
                                        i // without COLUMN_ROW_AND_COMPARATOR_NAME_SEPARATOR
                                    )
                                )
                            );

                            tokenStart = i + 1; // after COMPARATOR_NAME_SEPARATOR
                        } catch (final RuntimeException cause) {
                            mode = MODE_TEXT;
                            break;
                        }
                    }
                    break;
                default:
                case MODE_TEXT:

                    break;
            }

            i++;
        }

        switch (mode) {
            case MODE_COLUMN_OR_ROW:
                try {
                    columnOrRow = SpreadsheetSelection.parseColumnOrRow(
                        text.substring(
                            tokenStart,
                            i
                        )
                    );
                    tokenStart = i;
                } catch (final RuntimeException cause) {
                    mode = MODE_TEXT;
                }
                break;
            case MODE_NAME:
                try {
                    comparatorNames.add(
                        SpreadsheetComparatorName.with(
                            text.substring(
                                tokenStart,
                                i
                            )
                        )
                    );
                    tokenStart = i;
                } catch (final RuntimeException cause) {
                    mode = MODE_TEXT;
                }
                break;
            default:
                break;
        }

        return with(
            Optional.ofNullable(columnOrRow),
            comparatorNames,
            MODE_TEXT == mode ?
                text.substring(
                    tokenStart
                ) :
                ""
        );
    }

    static SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens with(final Optional<SpreadsheetColumnOrRowReferenceOrRange> columnOrRow,
                                                                       final List<SpreadsheetComparatorName> comparatorNames,
                                                                       final String text) {
        Objects.requireNonNull(columnOrRow, "columnOrRow");
        Objects.requireNonNull(comparatorNames, "comparatorNames");
        if (columnOrRow.isEmpty()) {
            if (false == comparatorNames.isEmpty()) {
                throw new IllegalArgumentException("Comparator names should be empty when columnOrRow is empty.");
            }
        }
        Objects.requireNonNull(text, "text");

        return new SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens(
            columnOrRow,
            comparatorNames,
            text
        );
    }

    private SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens(final Optional<SpreadsheetColumnOrRowReferenceOrRange> columnOrRow,
                                                                   final List<SpreadsheetComparatorName> comparatorNames,
                                                                   final String text) {
        super();

        this.columnOrRow = columnOrRow;
        this.comparatorNames = Lists.immutable(comparatorNames);
        this.text = text;
    }

    final static Optional<SpreadsheetColumnOrRowReferenceOrRange> EMPTY_COLUMN_OR_ROW = Optional.empty();

    Optional<SpreadsheetColumnOrRowReferenceOrRange> columnOrRow() {
        return this.columnOrRow;
    }

    private final Optional<SpreadsheetColumnOrRowReferenceOrRange> columnOrRow;

    final static List<SpreadsheetComparatorName> EMPTY_COMPARATOR_NAMES = Lists.empty();

    List<SpreadsheetComparatorName> comparatorNames() {
        return this.comparatorNames;
    }

    private final List<SpreadsheetComparatorName> comparatorNames;

    @Override
    public String text() {
        return this.text;
    }

    SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens setText(final String text) {
        return this.text.equals(text) ?
            this :
            new SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens(
                this.columnOrRow,
                this.comparatorNames,
                Objects.requireNonNull(text, "text")
            );
    }

    private final String text;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(
            this.columnOrRow,
            this.comparatorNames,
            this.text
        );
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens && this.equals0((SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens) other);
    }

    private boolean equals0(final SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens other) {
        return this.columnOrRow.equals(other.columnOrRow) &&
            this.comparatorNames.equals(other.comparatorNames) &&
            this.text.equals(other.text);
    }

    /**
     * Note it is possible that the {@link #toString()} representation may be ambiguous because {@link #text()},
     * could hold one or more valid {@link SpreadsheetComparatorName}.
     */
    @Override
    public String toString() {
        return ToStringBuilder.empty()
            .value(this.columnOrRow)
            .separator(SpreadsheetColumnOrRowSpreadsheetComparatorNames.COLUMN_ROW_AND_COMPARATOR_NAME_SEPARATOR.string())
            .valueSeparator(SpreadsheetColumnOrRowSpreadsheetComparatorNames.COMPARATOR_NAME_SEPARATOR.string())
            .value(this.comparatorNames)
            .separator(
                this.comparatorNames.isEmpty() ?
                    SpreadsheetColumnOrRowSpreadsheetComparatorNames.COLUMN_ROW_AND_COMPARATOR_NAME_SEPARATOR.string() :
                    ""
            ).disable(ToStringBuilderOption.QUOTE)
            .value(this.text)
            .build();
    }
}
