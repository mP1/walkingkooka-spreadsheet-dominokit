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

import walkingkooka.ToStringTesting;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.format.SpreadsheetText;

public abstract class SpreadsheetPatternEditorWidgetSampleRowTestCase<T extends SpreadsheetPatternEditorWidgetSampleRow> implements ClassTesting<T>,
        ToStringTesting<T> {

    SpreadsheetPatternEditorWidgetSampleRowTestCase() {
        super();
    }

    final void check(final T row,
                     final String label,
                     final String pattern,
                     final String value,
                     final String parsedOrFormatted) {
        this.check(
                row,
                label,
                pattern,
                value,
                SpreadsheetText.EMPTY.setText(parsedOrFormatted)
        );
    }

    final void check(final T row,
                     final String label,
                     final String pattern,
                     final String defaultFormattedValue,
                     final SpreadsheetText parsedOrFormatted) {
        this.checkEquals(
                label,
                row.label(),
                "label"
        );

        this.checkEquals(
                pattern,
                row.pattern(),
                "pattern"
        );

        this.checkEquals(
                defaultFormattedValue,
                row.defaultFormattedValue(),
                "defaultFormattedValue"
        );

        this.checkEquals(
                parsedOrFormatted,
                row.parsedOrFormatted(),
                "spreadsheetText"
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
