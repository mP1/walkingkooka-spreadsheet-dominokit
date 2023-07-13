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

import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.format.SpreadsheetFormatters;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetTextFormatPattern;

import java.util.List;

/**
 * A {@link SpreadsheetPatternEditorWidgetSampleRowProvider} for {@link SpreadsheetTextFormatPattern}.
 * Currently it provides two samples, one for the current pattern and the second the default text format pattern.
 */
final class SpreadsheetPatternEditorWidgetSampleRowProviderTextFormat extends SpreadsheetPatternEditorWidgetSampleRowProvider {

    /**
     * Singleton
     */
    final static SpreadsheetPatternEditorWidgetSampleRowProviderTextFormat INSTANCE = new SpreadsheetPatternEditorWidgetSampleRowProviderTextFormat();

    private SpreadsheetPatternEditorWidgetSampleRowProviderTextFormat() {
        super();
    }

    @Override
    public List<SpreadsheetPatternEditorWidgetSampleRow> apply(final String patternText,
                                                               final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
        final String value = "Abc123";

        return Lists.of(
                SpreadsheetPatternEditorWidgetSampleRow.with(
                        "Text",
                        patternText,
                        context.defaultFormat(value),
                        context.format(
                                tryParsePatternText(
                                        patternText,
                                        SpreadsheetPattern::parseTextFormatPattern
                                ).map(SpreadsheetTextFormatPattern::formatter)
                                        .orElse(SpreadsheetFormatters.emptyText()),
                                value
                        )
                ),
                SpreadsheetPatternEditorWidgetSampleRow.with(
                        "Default text format",
                        "@",
                        context.defaultFormat(value),
                        context.format(
                                SpreadsheetPattern.parseTextFormatPattern("@")
                                        .formatter(),
                                value
                        )
                )
        );
    }

    @Override
    public String toString() {
        return SpreadsheetPatternKind.TEXT_FORMAT_PATTERN.toString();
    }
}
