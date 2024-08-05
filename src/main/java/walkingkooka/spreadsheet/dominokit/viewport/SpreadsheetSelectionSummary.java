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

package walkingkooka.spreadsheet.dominokit.viewport;

import walkingkooka.ToStringBuilder;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelector;
import walkingkooka.tree.text.TextStyle;

import java.util.Objects;
import java.util.Optional;


/**
 * This contains a summary of what should be shown by UI tools such as menus and toolbars regarding the following properties
 * for the current {@link walkingkooka.spreadsheet.reference.SpreadsheetSelection}.
 * It should be recomputed each time the selection changes or when a new {@link walkingkooka.spreadsheet.engine.SpreadsheetDelta} delivers new cells.
 */
public final class SpreadsheetSelectionSummary {

    /**
     * An empty {@link SpreadsheetSelectionSummary} which may be useful when a {@link SpreadsheetViewportCache} has no cells.
     */
    public final static SpreadsheetSelectionSummary EMPTY = new SpreadsheetSelectionSummary(
            Optional.empty(),
            Optional.empty(),
            TextStyle.EMPTY
    );

    public static SpreadsheetSelectionSummary with(final Optional<SpreadsheetFormatterSelector> formatter,
                                                   final Optional<SpreadsheetParserSelector> parser,
                                                   final TextStyle style) {
        return new SpreadsheetSelectionSummary(
                Objects.requireNonNull(formatter, "formatter"),
                Objects.requireNonNull(parser, "parser"),
                Objects.requireNonNull(style, "style")
        );
    }

    private SpreadsheetSelectionSummary(final Optional<SpreadsheetFormatterSelector> formatter,
                                        final Optional<SpreadsheetParserSelector> parser,
                                       final TextStyle style) {
        this.formatter = formatter;
        this.parser = parser;
        this.style = style;
    }

    private final Optional<SpreadsheetFormatterSelector> formatter;

    public Optional<SpreadsheetFormatterSelector> formatter() {
        return this.formatter;
    }

    private final Optional<SpreadsheetParserSelector> parser;

    public Optional<SpreadsheetParserSelector> parser() {
        return this.parser;
    }

    private final TextStyle style;

    public TextStyle style() {
        return this.style;
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(
                this.formatter,
                this.parser,
                this.style
        );
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof SpreadsheetSelectionSummary && this.equals0((SpreadsheetSelectionSummary) other);
    }

    private boolean equals0(final SpreadsheetSelectionSummary other) {
        return this.formatter.equals(other.formatter) &&
                this.parser.equals(other.parser) &&
                this.style.equals(other.style);
    }

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .value(this.formatter)
                .value(this.parser)
                .value(this.style)
                .build();
    }
}
