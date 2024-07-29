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

package walkingkooka.spreadsheet.dominokit.format;

import org.junit.jupiter.api.Test;
import walkingkooka.Either;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.format.FakeSpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.SpreadsheetText;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

import java.util.Locale;
import java.util.Optional;

public final class SpreadsheetPatternComponentTableRowProviderTextFormatTest extends SpreadsheetPatternComponentTableRowProviderTestCase<SpreadsheetPatternComponentTableRowProviderTextFormat> {

    private final static SpreadsheetPatternComponentTableRowProviderContext CONTEXT = SpreadsheetPatternComponentTableRowProviderContexts.basic(
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
            },
            LOGGING_CONTEXT
    );

    @Test
    public void testValidPatternText() {
        final String text = "Abc123";

        this.applyAndCheck(
                "@@",
                CONTEXT,
                SpreadsheetPatternComponentTableRow.with(
                        "Edit Pattern",
                        Optional.of(
                                SpreadsheetPattern.parseTextFormatPattern("@@")
                        ),
                        Lists.of(
                                SpreadsheetText.EMPTY.setText(text + text)
                                        .toTextNode()
                        )
                ),
                SpreadsheetPatternComponentTableRow.with(
                        "Default text format",
                        Optional.of(
                                SpreadsheetPattern.parseTextFormatPattern("@")
                        ),
                        Lists.of(
                                SpreadsheetText.EMPTY.setText(text)
                                        .toTextNode()
                        )
                )
        );
    }

    @Test
    public void testInvalidPatternText() {
        final String text = "Abc123";

        this.applyAndCheck(
                "\"Unclosed",
                CONTEXT,
                SpreadsheetPatternComponentTableRow.with(
                        "Edit Pattern",
                        Optional.empty(), // no pattern
                        Lists.of(
                                SpreadsheetText.EMPTY.toTextNode()
                        )
                ),
                SpreadsheetPatternComponentTableRow.with(
                        "Default text format",
                        Optional.of(
                                SpreadsheetPattern.parseTextFormatPattern("@")
                        ),
                        Lists.of(
                                SpreadsheetText.EMPTY.setText(text)
                                        .toTextNode()
                        )
                )
        );
    }

    @Override
    SpreadsheetPatternComponentTableRowProviderTextFormat createProvider() {
        return SpreadsheetPatternComponentTableRowProviderTextFormat.INSTANCE;
    }

    @Override
    public Class<SpreadsheetPatternComponentTableRowProviderTextFormat> type() {
        return SpreadsheetPatternComponentTableRowProviderTextFormat.class;
    }
}
