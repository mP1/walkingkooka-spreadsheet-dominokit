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

import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

import java.util.Objects;

final class BasicSpreadsheetPatternEditorWidgetSampleRowProviderContext implements SpreadsheetPatternEditorWidgetSampleRowProviderContext {

    static BasicSpreadsheetPatternEditorWidgetSampleRowProviderContext with(final SpreadsheetPatternKind kind,
                                                                            final String patternText,
                                                                            final SpreadsheetFormatterContext spreadsheetFormatterContext) {
        Objects.requireNonNull(kind, "kind");
        Objects.requireNonNull(patternText, "patternText");
        Objects.requireNonNull(spreadsheetFormatterContext, "spreadsheetFormatterContext");

        return new BasicSpreadsheetPatternEditorWidgetSampleRowProviderContext(
                kind,
                patternText,
                spreadsheetFormatterContext
        );
    }

    private BasicSpreadsheetPatternEditorWidgetSampleRowProviderContext(final SpreadsheetPatternKind kind,
                                                                        final String patternText,
                                                                        final SpreadsheetFormatterContext spreadsheetFormatterContext) {
        this.kind = kind;
        this.patternText = patternText;
        this.spreadsheetFormatterContext = spreadsheetFormatterContext;
    }

    @Override
    public SpreadsheetPatternKind kind() {
        return this.kind;
    }

    private final SpreadsheetPatternKind kind;

    @Override
    public String patternText() {
        return this.patternText;
    }

    private final String patternText;

    @Override
    public SpreadsheetFormatterContext spreadsheetFormatterContext() {
        return this.spreadsheetFormatterContext;
    }

    private SpreadsheetFormatterContext spreadsheetFormatterContext;

    @Override
    public String toString() {
        return this.patternText();
    }
}
