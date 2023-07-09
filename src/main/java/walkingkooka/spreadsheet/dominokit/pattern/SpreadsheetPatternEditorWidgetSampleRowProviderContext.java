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
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

/**
 * A {@link Context} accompanying a {@link SpreadsheetPatternEditorWidgetSampleRowProvider}.
 */
public interface SpreadsheetPatternEditorWidgetSampleRowProviderContext extends Context {

    /**
     * The {@link SpreadsheetPatternKind} for the parent pattern.
     */
    SpreadsheetPatternKind kind();

    /**
     * The current pattern as text.
     */
    String patternText();

    /**
     * The {@link SpreadsheetFormatterContext} to be used when formatting a value with this pattern.
     */
    SpreadsheetFormatterContext spreadsheetFormatterContext();
}
