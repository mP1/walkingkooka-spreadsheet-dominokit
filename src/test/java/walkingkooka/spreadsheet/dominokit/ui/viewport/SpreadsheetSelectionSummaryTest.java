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

package walkingkooka.spreadsheet.dominokit.ui.viewport;

import org.junit.Test;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.color.Color;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetSelectionSummaryTest implements HashCodeEqualsDefinedTesting2<SpreadsheetSelectionSummary>,
        ClassTesting<SpreadsheetSelectionSummary>,
        ToStringTesting<SpreadsheetSelectionSummary> {

    private final static Optional<SpreadsheetFormatPattern> FORMAT = Optional.of(
            SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN
    );

    private final static Optional<SpreadsheetParsePattern> PARSE = Optional.of(
            SpreadsheetPattern.parseNumberParsePattern("#.##")
    );

    private final static TextStyle STYLE = TextStyle.EMPTY.set(
            TextStylePropertyName.BACKGROUND_COLOR,
            Color.BLACK
    );

    @Test
    public void testWithNullFormatPatternFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetSelectionSummary.with(
                        null,
                        PARSE,
                        STYLE
                )
        );
    }

    @Test
    public void testWithNullParsePatternFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetSelectionSummary.with(
                        FORMAT,
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
                        FORMAT,
                        PARSE,
                        null
                )
        );
    }

    // equals...........................................................................................................

    @Test
    public void testDifferentFormatPattern() {
        this.checkNotEquals(
                SpreadsheetSelectionSummary.with(
                        Optional.empty(),
                        PARSE,
                        STYLE
                )
        );
    }

    @Test
    public void testDifferentParsePattern() {
        this.checkNotEquals(
                SpreadsheetSelectionSummary.with(
                        FORMAT,
                        Optional.empty(),
                        STYLE
                )
        );
    }

    @Test
    public void testDifferentStyle() {
        this.checkNotEquals(
                SpreadsheetSelectionSummary.with(
                        FORMAT,
                        PARSE,
                        TextStyle.EMPTY
                )
        );
    }

    @Override
    public SpreadsheetSelectionSummary createObject() {
        return SpreadsheetSelectionSummary.with(
                FORMAT,
                PARSE,
                STYLE
        );
    }

    // ToStringTesting.....................................................................................................

    @Test
    public void testToStringAll() {
        this.toStringAndCheck(
                SpreadsheetSelectionSummary.with(
                        FORMAT,
                        PARSE,
                        STYLE
                ),
                FORMAT.get() + " " + PARSE.get() + " " + STYLE
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

