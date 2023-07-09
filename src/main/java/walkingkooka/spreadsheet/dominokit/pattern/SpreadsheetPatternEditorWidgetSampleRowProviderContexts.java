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

import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

public final class SpreadsheetPatternEditorWidgetSampleRowProviderContexts implements PublicStaticHelper {

    /**
     * {@see SpreadsheetPatternEditorWidgetSampleRowProviderContext}
     */
    public static SpreadsheetPatternEditorWidgetSampleRowProviderContext fake() {
        return new FakeSpreadsheetPatternEditorWidgetSampleRowProviderContext();
    }

    /**
     * {@see BasicSpreadsheetPatternEditorWidgetSampleRowProviderContext}
     */
    public static SpreadsheetPatternEditorWidgetSampleRowProviderContext with(final SpreadsheetPatternKind kind,
                                                                              final String patternText,
                                                                              final SpreadsheetFormatterContext spreadsheetFormatterContext) {
        return BasicSpreadsheetPatternEditorWidgetSampleRowProviderContext.with(
                kind,
                patternText,
                spreadsheetFormatterContext
        );
    }

    /**
     * Stop creation
     */
    private SpreadsheetPatternEditorWidgetSampleRowProviderContexts() {
        throw new UnsupportedOperationException();
    }
}
