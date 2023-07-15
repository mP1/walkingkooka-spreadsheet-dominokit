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

import org.junit.jupiter.api.Test;
import walkingkooka.Either;
import walkingkooka.spreadsheet.format.FakeSpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.SpreadsheetText;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

import java.util.Locale;

public final class SpreadsheetPatternEditorWidgetSampleRowProviderTextFormatTest extends SpreadsheetPatternEditorWidgetSampleRowProviderTestCase<SpreadsheetPatternEditorWidgetSampleRowProviderTextFormat> {

    private final static SpreadsheetPatternEditorWidgetSampleRowProviderContext CONTEXT = SpreadsheetPatternEditorWidgetSampleRowProviderContexts.basic(
            SpreadsheetPatternKind.TEXT_FORMAT_PATTERN,
            new FakeSpreadsheetFormatterContext() {
                @Override
                public Locale locale() {
                    return Locale.forLanguageTag("EN-AU");
                }

                @Override
                public boolean canConvert(final Object value,
                                          final Class<?> type) {
                    return value instanceof String && String.class == type;
                }

                @Override
                public <T> Either<T, String> convert(final Object value,
                                                     final Class<T> target) {
                    this.canConvertOrFail(value, target);
                    return this.successfulConversion(
                            value,
                            target
                    );
                }
            }
    );

    @Test
    public void testValidPatternText() {
        final String text = "Abc123";

        this.applyAndCheck(
                "@@",
                CONTEXT,
                SpreadsheetPatternEditorWidgetSampleRow.with(
                        "Edit Pattern",
                        "@@",
                        SpreadsheetText.EMPTY.setText(text),
                        SpreadsheetText.EMPTY.setText(text + text)
                ),
                SpreadsheetPatternEditorWidgetSampleRow.with(
                        "Default text format",
                        "@",
                        SpreadsheetText.EMPTY.setText(text),
                        SpreadsheetText.EMPTY.setText(text)
                )
        );
    }

    @Test
    public void testInvalidPatternText() {
        final String text = "Abc123";

        this.applyAndCheck(
                "\"Unclosed",
                CONTEXT,
                SpreadsheetPatternEditorWidgetSampleRow.with(
                        "Edit Pattern",
                        "\"Unclosed",
                        SpreadsheetText.EMPTY.setText(text),
                        SpreadsheetText.EMPTY
                ),
                SpreadsheetPatternEditorWidgetSampleRow.with(
                        "Default text format",
                        "@",
                        SpreadsheetText.EMPTY.setText(text),
                        SpreadsheetText.EMPTY.setText(text)
                )
        );
    }

    @Override
    SpreadsheetPatternEditorWidgetSampleRowProviderTextFormat createProvider() {
        return SpreadsheetPatternEditorWidgetSampleRowProviderTextFormat.INSTANCE;
    }

    @Override
    public Class<SpreadsheetPatternEditorWidgetSampleRowProviderTextFormat> type() {
        return SpreadsheetPatternEditorWidgetSampleRowProviderTextFormat.class;
    }
}
