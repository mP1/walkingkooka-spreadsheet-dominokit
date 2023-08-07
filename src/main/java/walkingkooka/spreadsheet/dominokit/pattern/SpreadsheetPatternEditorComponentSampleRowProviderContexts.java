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

public final class SpreadsheetPatternEditorComponentSampleRowProviderContexts implements PublicStaticHelper {

    /**
     * {@see BasicSpreadsheetPatternEditorComponentSampleRowProviderContext}
     */
    public static SpreadsheetPatternEditorComponentSampleRowProviderContext basic(final SpreadsheetPatternKind kind,
                                                                                  final SpreadsheetFormatterContext spreadsheetFormatterContext) {
        return BasicSpreadsheetPatternEditorComponentSampleRowProviderContext.with(
                kind,
                spreadsheetFormatterContext
        );
    }

    /**
     * {@see SpreadsheetPatternEditorComponentSampleRowProviderContext}
     */
    public static SpreadsheetPatternEditorComponentSampleRowProviderContext fake() {
        return new FakeSpreadsheetPatternEditorComponentSampleRowProviderContext();
    }

    /**
     * Stop creation
     */
    private SpreadsheetPatternEditorComponentSampleRowProviderContexts() {
        throw new UnsupportedOperationException();
    }
}
