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

import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A provider that uses the user's {@link java.util.Locale} and active {@link walkingkooka.spreadsheet.meta.SpreadsheetMetadata} to prepare rows
 * for the sample.
 */
abstract class SpreadsheetPatternEditorWidgetSampleRowProvider implements BiFunction<String, SpreadsheetPatternEditorWidgetSampleRowProviderContext, List<SpreadsheetPatternEditorWidgetSampleRow>> {

    /**
     * {@see SpreadsheetPatternEditorWidgetSampleRowProviderDateFormat}
     */
    static SpreadsheetPatternEditorWidgetSampleRowProvider dateFormat() {
        return SpreadsheetPatternEditorWidgetSampleRowProviderDateFormat.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternEditorWidgetSampleRowProviderEmpty}
     */
    static SpreadsheetPatternEditorWidgetSampleRowProvider dateParse() {
        return SpreadsheetPatternEditorWidgetSampleRowProviderEmpty.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternEditorWidgetSampleRowProviderDateTimeFormat}
     */
    static SpreadsheetPatternEditorWidgetSampleRowProvider dateTimeFormat() {
        return SpreadsheetPatternEditorWidgetSampleRowProviderDateTimeFormat.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternEditorWidgetSampleRowProviderEmpty}
     */
    static SpreadsheetPatternEditorWidgetSampleRowProvider dateTimeParse() {
        return SpreadsheetPatternEditorWidgetSampleRowProviderEmpty.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternEditorWidgetSampleRowProviderNumberFormat}
     */
    static SpreadsheetPatternEditorWidgetSampleRowProvider numberFormat() {
        return SpreadsheetPatternEditorWidgetSampleRowProviderNumberFormat.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternEditorWidgetSampleRowProviderEmpty}
     */
    static SpreadsheetPatternEditorWidgetSampleRowProvider numberParse() {
        return SpreadsheetPatternEditorWidgetSampleRowProviderEmpty.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternEditorWidgetSampleRowProviderTextFormat}
     */
    static SpreadsheetPatternEditorWidgetSampleRowProviderTextFormat textFormat() {
        return SpreadsheetPatternEditorWidgetSampleRowProviderTextFormat.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternEditorWidgetSampleRowProviderTimeFormat}
     */
    static SpreadsheetPatternEditorWidgetSampleRowProvider timeFormat() {
        return SpreadsheetPatternEditorWidgetSampleRowProviderTimeFormat.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternEditorWidgetSampleRowProviderEmpty}
     */
    static SpreadsheetPatternEditorWidgetSampleRowProvider timeParse() {
        return SpreadsheetPatternEditorWidgetSampleRowProviderEmpty.INSTANCE;
    }

    /**
     * Helper that provides a {@link SpreadsheetPattern} using the given {@link String patternText}.
     */
    static <T extends SpreadsheetPattern> Optional<T> tryParsePatternText(final String patternText,
                                                                          final Function<String, T> parser) {
        Objects.requireNonNull(patternText, "patternText");

        T spreadsheetFormatPattern = null;

        try {
            spreadsheetFormatPattern = parser.apply(patternText);
        } catch (final Exception fail) {
            // ignore
        }

        return Optional.ofNullable(spreadsheetFormatPattern);
    }

    /**
     * Package private to limit sub classing.
     */
    SpreadsheetPatternEditorWidgetSampleRowProvider() {
        super();
    }
}
