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

import org.junit.jupiter.api.Test;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.color.Color;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelector;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetSelectionSummaryTest implements HashCodeEqualsDefinedTesting2<SpreadsheetSelectionSummary>,
    ClassTesting<SpreadsheetSelectionSummary>,
    ToStringTesting<SpreadsheetSelectionSummary> {

    private final static Optional<SpreadsheetFormatterSelector> FORMATTER = Optional.of(
        SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN.spreadsheetFormatterSelector()
    );

    private final static Optional<SpreadsheetParserSelector> PARSER = Optional.of(
        SpreadsheetPattern.parseNumberParsePattern("#.##").spreadsheetParserSelector()
    );

    private final static TextStyle STYLE = TextStyle.EMPTY.set(
        TextStylePropertyName.BACKGROUND_COLOR,
        Color.BLACK
    );

    @Test
    public void testWithNullFormatterFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetSelectionSummary.with(
                null,
                PARSER,
                STYLE
            )
        );
    }

    @Test
    public void testWithNullParserFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetSelectionSummary.with(
                FORMATTER,
                null,
                STYLE
            )
        );
    }

    @Test
    public void testWithNullStyleFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetSelectionSummary.with(
                FORMATTER,
                PARSER,
                null
            )
        );
    }

    // equals...........................................................................................................

    @Test
    public void testEqualsDifferentFormatPattern() {
        this.checkNotEquals(
            SpreadsheetSelectionSummary.with(
                Optional.empty(),
                PARSER,
                STYLE
            )
        );
    }

    @Test
    public void testEqualsDifferentParsePattern() {
        this.checkNotEquals(
            SpreadsheetSelectionSummary.with(
                FORMATTER,
                Optional.empty(),
                STYLE
            )
        );
    }

    @Test
    public void testEqualsDifferentStyle() {
        this.checkNotEquals(
            SpreadsheetSelectionSummary.with(
                FORMATTER,
                PARSER,
                TextStyle.EMPTY
            )
        );
    }

    @Override
    public SpreadsheetSelectionSummary createObject() {
        return SpreadsheetSelectionSummary.with(
            FORMATTER,
            PARSER,
            STYLE
        );
    }

    // ToStringTesting.....................................................................................................

    @Test
    public void testToStringAll() {
        this.toStringAndCheck(
            SpreadsheetSelectionSummary.with(
                FORMATTER,
                PARSER,
                STYLE
            ),
            FORMATTER.get() + " " + PARSER.get() + " " + STYLE
        );
    }

    @Test
    public void testToStringOnlyStyle() {
        this.toStringAndCheck(
            SpreadsheetSelectionSummary.with(
                Optional.empty(),
                Optional.empty(),
                STYLE
            ),
            STYLE.toString()
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetSelectionSummary> type() {
        return SpreadsheetSelectionSummary.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}

