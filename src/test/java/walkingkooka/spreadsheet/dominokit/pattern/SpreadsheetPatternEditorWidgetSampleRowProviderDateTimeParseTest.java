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
import walkingkooka.color.Color;
import walkingkooka.datetime.DateTimeContext;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.spreadsheet.format.FakeSpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.SpreadsheetColorName;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public final class SpreadsheetPatternEditorWidgetSampleRowProviderDateTimeParseTest extends SpreadsheetPatternEditorWidgetSampleRowProviderTestCase<SpreadsheetPatternEditorWidgetSampleRowProviderDateTimeParse> {

    private final static LocalDateTime NOW = LocalDateTime.of(
            LocalDate.of(
                    2023,
                    7,
                    12
            ),
            LocalTime.of(
                    12,
                    58,
                    59
            )
    );

    private final static SpreadsheetPatternEditorWidgetSampleRowProviderContext CONTEXT = SpreadsheetPatternEditorWidgetSampleRowProviderContexts.basic(
            SpreadsheetPatternKind.DATE_FORMAT_PATTERN,
            new FakeSpreadsheetFormatterContext() {
                @Override
                public Locale locale() {
                    return Locale.forLanguageTag("EN-AU");
                }

                @Override
                public boolean canConvert(final Object value,
                                          final Class<?> type) {
                    return value instanceof LocalDateTime && LocalDateTime.class == type;
                }

                @Override
                public <T> Either<T, String> convert(final Object value,
                                                     final Class<T> target) {
                    this.canConvertOrFail(value, target);
                    return this.successfulConversion(
                            (LocalDateTime) value,
                            target
                    );
                }

                @Override
                public LocalDateTime now() {
                    return NOW;
                }

                @Override
                public List<String> ampms() {
                    return this.dateTimeContext.ampms();
                }

                @Override
                public String ampm(final int hourOfDay) {
                    return this.dateTimeContext.ampm(hourOfDay);
                }

                @Override
                public int defaultYear() {
                    return this.dateTimeContext.defaultYear();
                }

                @Override
                public List<String> monthNames() {
                    return this.dateTimeContext.monthNames();
                }

                @Override
                public String monthName(int month) {
                    return this.dateTimeContext.monthName(month);
                }

                @Override
                public List<String> monthNameAbbreviations() {
                    return this.dateTimeContext.monthNameAbbreviations();
                }

                @Override
                public String monthNameAbbreviation(int month) {
                    return this.dateTimeContext.monthNameAbbreviation(month);
                }

                @Override
                public int twoDigitYear() {
                    return this.dateTimeContext.twoDigitYear();
                }

                @Override
                public List<String> weekDayNames() {
                    return this.dateTimeContext.weekDayNames();
                }

                @Override
                public String weekDayName(int day) {
                    return this.dateTimeContext.weekDayName(day);
                }

                @Override
                public List<String> weekDayNameAbbreviations() {
                    return this.dateTimeContext.weekDayNameAbbreviations();
                }

                @Override
                public String weekDayNameAbbreviation(int day) {
                    return this.dateTimeContext.weekDayNameAbbreviation(day);
                }

                private final DateTimeContext dateTimeContext = DateTimeContexts.locale(
                        Locale.forLanguageTag("EN-AU"),
                        1950,
                        50,
                        () -> NOW
                );

                @Override
                public Optional<Color> colorName(final SpreadsheetColorName name) {
                    return Optional.of(
                            Color.parse("#00f")
                    );
                }
            }
    );

    @Test
    public void testValidPatternText() {
        final String patternText = "yyyy/mm/dd";

        this.applyAndCheck2(
                patternText,
                CONTEXT,
                "Edit Pattern | yyyy/mm/dd | Wednesday, 12 July 2023 | 2023/07/12\n" +
                        "Today Short | d/m/yy, h:mm AM/PM | Wednesday, 12 July 2023 | 12/7/23, 12:58 PM\n" +
                        "Today Medium | d mmm yyyy, h:mm:ss AM/PM | Wednesday, 12 July 2023 | 12 Jul. 2023, 12:58:59 PM\n" +
                        "Today Long | d mmmm yyyy \\a\\t h:mm:ss AM/PM | Wednesday, 12 July 2023 | 12 July 2023 at 12:58:59 PM\n" +
                        "Today Full | dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM | Wednesday, 12 July 2023 | Wednesday, 12 July 2023 at 12:58:59 PM\n" +
                        "31 December 1999 12:58:59 Short | d/m/yy, h:mm AM/PM | Friday, 31 December 1999 | 31/12/99, 12:58 PM\n" +
                        "31 December 1999 12:58:59 Medium | d mmm yyyy, h:mm:ss AM/PM | Friday, 31 December 1999 | 31 Dec. 1999, 12:58:59 PM\n" +
                        "31 December 1999 12:58:59 Long | d mmmm yyyy \\a\\t h:mm:ss AM/PM | Friday, 31 December 1999 | 31 December 1999 at 12:58:59 PM\n" +
                        "31 December 1999 12:58:59 Full | dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM | Friday, 31 December 1999 | Friday, 31 December 1999 at 12:58:59 PM"
        );
    }

    @Test
    public void testValidPatternTextWithColor() {
        final String patternText = "[RED]yyyy/dd/mmm";

        this.applyAndCheck2(
                patternText,
                CONTEXT,
                "Edit Pattern | | Wednesday, 12 July 2023 |\n" +
                        "Today Short | d/m/yy, h:mm AM/PM | Wednesday, 12 July 2023 | 12/7/23, 12:58 PM\n" +
                        "Today Medium | d mmm yyyy, h:mm:ss AM/PM | Wednesday, 12 July 2023 | 12 Jul. 2023, 12:58:59 PM\n" +
                        "Today Long | d mmmm yyyy \\a\\t h:mm:ss AM/PM | Wednesday, 12 July 2023 | 12 July 2023 at 12:58:59 PM\n" +
                        "Today Full | dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM | Wednesday, 12 July 2023 | Wednesday, 12 July 2023 at 12:58:59 PM\n" +
                        "31 December 1999 12:58:59 Short | d/m/yy, h:mm AM/PM | Friday, 31 December 1999 | 31/12/99, 12:58 PM\n" +
                        "31 December 1999 12:58:59 Medium | d mmm yyyy, h:mm:ss AM/PM | Friday, 31 December 1999 | 31 Dec. 1999, 12:58:59 PM\n" +
                        "31 December 1999 12:58:59 Long | d mmmm yyyy \\a\\t h:mm:ss AM/PM | Friday, 31 December 1999 | 31 December 1999 at 12:58:59 PM\n" +
                        "31 December 1999 12:58:59 Full | dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM | Friday, 31 December 1999 | Friday, 31 December 1999 at 12:58:59 PM"
        );
    }

    @Test
    public void testInvalidPatternText() {
        this.applyAndCheck2(
                "\"Unclosed",
                CONTEXT,
                "Edit Pattern | | Wednesday, 12 July 2023 |\n" +
                        "Today Short | d/m/yy, h:mm AM/PM | Wednesday, 12 July 2023 | 12/7/23, 12:58 PM\n" +
                        "Today Medium | d mmm yyyy, h:mm:ss AM/PM | Wednesday, 12 July 2023 | 12 Jul. 2023, 12:58:59 PM\n" +
                        "Today Long | d mmmm yyyy \\a\\t h:mm:ss AM/PM | Wednesday, 12 July 2023 | 12 July 2023 at 12:58:59 PM\n" +
                        "Today Full | dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM | Wednesday, 12 July 2023 | Wednesday, 12 July 2023 at 12:58:59 PM\n" +
                        "31 December 1999 12:58:59 Short | d/m/yy, h:mm AM/PM | Friday, 31 December 1999 | 31/12/99, 12:58 PM\n" +
                        "31 December 1999 12:58:59 Medium | d mmm yyyy, h:mm:ss AM/PM | Friday, 31 December 1999 | 31 Dec. 1999, 12:58:59 PM\n" +
                        "31 December 1999 12:58:59 Long | d mmmm yyyy \\a\\t h:mm:ss AM/PM | Friday, 31 December 1999 | 31 December 1999 at 12:58:59 PM\n" +
                        "31 December 1999 12:58:59 Full | dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM | Friday, 31 December 1999 | Friday, 31 December 1999 at 12:58:59 PM"
        );
    }

    @Override
    SpreadsheetPatternEditorWidgetSampleRowProviderDateTimeParse createProvider() {
        return SpreadsheetPatternEditorWidgetSampleRowProviderDateTimeParse.INSTANCE;
    }

    @Override
    public Class<SpreadsheetPatternEditorWidgetSampleRowProviderDateTimeParse> type() {
        return SpreadsheetPatternEditorWidgetSampleRowProviderDateTimeParse.class;
    }
}
