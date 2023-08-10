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

import walkingkooka.Context;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatter;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.SpreadsheetText;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

/**
 * A {@link Context} accompanying a {@link SpreadsheetPatternEditorComponentSampleRowProvider}.
 */
public interface SpreadsheetPatternEditorComponentSampleRowProviderContext extends LoggingContext {

    /**
     * Returns the default {@link SpreadsheetFormatter} for the {@link #kind()}.
     */
    SpreadsheetFormatter defaultSpreadsheetFormatter();

    /**
     * Uses the {@link #defaultSpreadsheetFormatter()} to format the {@link Object value}.
     */
    default SpreadsheetText defaultFormat(final Object value) {
        return this.defaultSpreadsheetFormatter()
                .format(
                        value,
                        this.spreadsheetFormatterContext()
                ).orElse(SpreadsheetText.EMPTY);
    }

    /**
     * The {@link SpreadsheetPatternKind} for the parent pattern.
     */
    SpreadsheetPatternKind kind();

    /**
     * Uses the given {@link SpreadsheetFormatter} to format the given {@link Object value}.
     */
    default SpreadsheetText format(final SpreadsheetFormatter formatter,
                                   final Object value) {
        return formatter.formatOrEmptyText(
                value,
                this.spreadsheetFormatterContext()
        );
    }

    /**
     * The {@link SpreadsheetFormatterContext} to be used when formatting a value with this pattern.
     */
    SpreadsheetFormatterContext spreadsheetFormatterContext();
}
