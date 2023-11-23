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

package walkingkooka.spreadsheet.dominokit.component.pattern;

import walkingkooka.spreadsheet.format.pattern.SpreadsheetNumberParsePattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

/**
 * A {@link SpreadsheetPatternEditorComponentSampleRowProvider} for {@link SpreadsheetNumberParsePattern}.
 */
final class SpreadsheetPatternEditorComponentSampleRowProviderNumberParse extends SpreadsheetPatternEditorComponentSampleRowProviderNumber {

    /**
     * Singleton
     */
    final static SpreadsheetPatternEditorComponentSampleRowProviderNumberParse INSTANCE = new SpreadsheetPatternEditorComponentSampleRowProviderNumberParse();

    private SpreadsheetPatternEditorComponentSampleRowProviderNumberParse() {
        super();
    }

    @Override
    SpreadsheetPatternKind kind() {
        return SpreadsheetPatternKind.NUMBER_PARSE_PATTERN;
    }
}
