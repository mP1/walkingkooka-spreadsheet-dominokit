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

/**
 * A template source for the output that appears in a sample table.
 */
interface SpreadsheetPatternEditorWidgetSampleRow {

    /**
     * The label or descriptive text that appears in the first column of the sample table.
     */
    String label();

    /**
     * The pattern text.
     */
    String pattern();

    /**
     * The value to be parsed, or the value to be formatted as text.
     */
    String defaultFormattedValue();

    /**
     * The value parsed or formatted using the {@link #pattern()}.
     */
    SpreadsheetText parsedOrFormatted();
}
