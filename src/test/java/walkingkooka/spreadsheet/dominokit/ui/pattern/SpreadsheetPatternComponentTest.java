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

import org.junit.Test;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

public final class SpreadsheetPatternComponentTest implements ClassTesting<SpreadsheetPatternComponent> {


    // sheetPatternKindId...............................................................................................

    @Test
    public void testSpreadsheetPatternKindIdDateFormat() {
        this.spreadsheetPatternKindIdAndCheck(
                SpreadsheetPatternKind.DATE_FORMAT_PATTERN,
                "pattern-date-format"
        );
    }

    @Test
    public void testSpreadsheetPatternKindIdDateTimeParse() {
        this.spreadsheetPatternKindIdAndCheck(
                SpreadsheetPatternKind.DATE_TIME_PARSE_PATTERN,
                "pattern-date-time-parse"
        );
    }

    @Test
    public void testSpreadsheetPatternKindIdTextFormat() {
        this.spreadsheetPatternKindIdAndCheck(
                SpreadsheetPatternKind.TEXT_FORMAT_PATTERN,
                "pattern-text-format"
        );
    }

    private void spreadsheetPatternKindIdAndCheck(final SpreadsheetPatternKind kind,
                                                  final String expected) {
        this.checkEquals(
                expected,
                SpreadsheetPatternComponent.spreadsheetPatternKindId(kind),
                () -> "spreadsheetPatternKindIdAndCheck " + kind
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetPatternComponent> type() {
        return SpreadsheetPatternComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
