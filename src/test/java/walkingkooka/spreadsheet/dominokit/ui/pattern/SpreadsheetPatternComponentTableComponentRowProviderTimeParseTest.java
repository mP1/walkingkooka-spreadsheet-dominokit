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

package walkingkooka.spreadsheet.dominokit.ui.pattern;

import org.junit.Test;
import walkingkooka.Either;
import walkingkooka.datetime.DateTimeContext;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.spreadsheet.format.FakeSpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

public final class SpreadsheetPatternComponentTableComponentRowProviderTimeParseTest extends SpreadsheetPatternComponentTableComponentRowProviderTestCase<SpreadsheetPatternComponentTableComponentRowProviderTimeParse> {

    private final static LocalDateTime NOW = LocalDateTime.of(
            LocalDate.of(
                    2023,
                    7,
                    12
            ),
            LocalTime.of(
                    3,
                    33,
                    59
            )
    );

    private final static SpreadsheetPatternComponentTableComponentRowProviderContext CONTEXT = SpreadsheetPatternComponentTableComponentRowProviderContexts.basic(
            SpreadsheetPatternKind.TIME_FORMAT_PATTERN,
            new FakeSpreadsheetFormatterContext() {
                @Override
                public Locale locale() {
                    return Locale.forLanguageTag("EN-AU");
                }

                @Override
                public boolean canConvert(final Object value,
                                          final Class<?> type) {
                    return value instanceof LocalTime && LocalDateTime.class == type;
                }

                @Override
                public <T> Either<T, String> convert(final Object value,
                                                     final Class<T> target) {
                    this.canConvertOrFail(value, target);
                    return this.successfulConversion(
                            LocalDateTime.of(
                                    LocalDate.EPOCH,
                                    LocalTime.class.cast(value)
                            ),
                            target
                    );
                }

                @Override
                public LocalDateTime now() {
                    return NOW;
                }

                @Override
                public int defaultYear() {
                    return this.dateTimeContext.defaultYear();
                }

                @Override
                public List<String> ampms() {
                    return this.dateTimeContext.ampms();
                }

                @Override public String ampm(final int hourOfDay) {
                    return this.dateTimeContext.ampm(hourOfDay);
                }

                private final DateTimeContext dateTimeContext = DateTimeContexts.locale(
                        Locale.forLanguageTag("EN-AU"),
                        1950,
                        50,
                        () -> NOW
                );
            },
            LOGGING_CONTEXT
    );

    @Test
    public void testValidPatternText() {
        final String patternText = "hh/mm/ss";

        this.applyAndCheck2(
                patternText,
                CONTEXT,
                "Edit Pattern | hh/mm/ss | 03/33/59\n" +
                        "Now Short | h:mm AM/PM | 3:33 AM\n" +
                        "Now Long | h:mm:ss AM/PM | 3:33:59 AM\n" +
                        "12:58:59 AM Short | h:mm AM/PM | 12:58 PM\n" +
                        "12:58:59 AM Long | h:mm:ss AM/PM | 12:58:59 PM\n" +
                        "6:01:02 PM Short | h:mm AM/PM | 6:01 PM\n" +
                        "6:01:02 PM Long | h:mm:ss AM/PM | 6:01:02 PM"
        );
    }

    @Test
    public void testValidPatternText2() {
        final String patternText = "h/m/s";

        this.applyAndCheck2(
                patternText,
                CONTEXT,
                "Edit Pattern | h/m/s | 3/33/59\n" +
                        "Now Short | h:mm AM/PM | 3:33 AM\n" +
                        "Now Long | h:mm:ss AM/PM | 3:33:59 AM\n" +
                        "12:58:59 AM Short | h:mm AM/PM | 12:58 PM\n" +
                        "12:58:59 AM Long | h:mm:ss AM/PM | 12:58:59 PM\n" +
                        "6:01:02 PM Short | h:mm AM/PM | 6:01 PM\n" +
                        "6:01:02 PM Long | h:mm:ss AM/PM | 6:01:02 PM"
        );
    }

    @Test
    public void testPatternWithColor() {
        final String patternText = "[RED]h/m/s";

        this.applyAndCheck2(
                patternText,
                CONTEXT,
                "Edit Pattern | |\n" +
                        "Now Short | h:mm AM/PM | 3:33 AM\n" +
                        "Now Long | h:mm:ss AM/PM | 3:33:59 AM\n" +
                        "12:58:59 AM Short | h:mm AM/PM | 12:58 PM\n" +
                        "12:58:59 AM Long | h:mm:ss AM/PM | 12:58:59 PM\n" +
                        "6:01:02 PM Short | h:mm AM/PM | 6:01 PM\n" +
                        "6:01:02 PM Long | h:mm:ss AM/PM | 6:01:02 PM"
        );
    }

    @Test
    public void testInvalidPatternText() {
        this.applyAndCheck2(
                "\"Unclosed",
                CONTEXT,
                "Edit Pattern | |\n" +
                        "Now Short | h:mm AM/PM | 3:33 AM\n" +
                        "Now Long | h:mm:ss AM/PM | 3:33:59 AM\n" +
                        "12:58:59 AM Short | h:mm AM/PM | 12:58 PM\n" +
                        "12:58:59 AM Long | h:mm:ss AM/PM | 12:58:59 PM\n" +
                        "6:01:02 PM Short | h:mm AM/PM | 6:01 PM\n" +
                        "6:01:02 PM Long | h:mm:ss AM/PM | 6:01:02 PM"
        );
    }

    @Override
    SpreadsheetPatternComponentTableComponentRowProviderTimeParse createProvider() {
        return SpreadsheetPatternComponentTableComponentRowProviderTimeParse.INSTANCE;
    }

    @Override
    public Class<SpreadsheetPatternComponentTableComponentRowProviderTimeParse> type() {
        return SpreadsheetPatternComponentTableComponentRowProviderTimeParse.class;
    }
}
