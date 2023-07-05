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
import walkingkooka.Cast;
import walkingkooka.Either;
import walkingkooka.color.Color;
import walkingkooka.spreadsheet.format.FakeSpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.SpreadsheetColorName;
import walkingkooka.spreadsheet.format.SpreadsheetFormatter;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContexts;
import walkingkooka.spreadsheet.format.SpreadsheetFormatters;
import walkingkooka.spreadsheet.format.SpreadsheetText;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberKind;

import java.math.MathContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetPatternEditorWidgetSampleRowFormatTest extends SpreadsheetPatternEditorWidgetSampleRowTestCase<SpreadsheetPatternEditorWidgetSampleRowFormat<?>> {

    private final static String LABEL = "Label123";

    private final static Color BLUE = Color.parse("#12f");

    // dateFormat.......................................................................................................

    @Test
    public void testDateFormatNullLabelFails() {
        this.dateFormatFails(
                null,
                () -> "",
                LocalDate.now(),
                SpreadsheetFormatters.fake(),
                SpreadsheetFormatterContexts.fake()
        );
    }

    @Test
    public void testDateFormatEmptyLabelFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> SpreadsheetPatternEditorWidgetSampleRowFormat.dateFormat(
                        "",
                        () -> "",
                        LocalDate.now(),
                        SpreadsheetFormatters.fake(),
                        SpreadsheetFormatterContexts.fake()
                )
        );
    }

    @Test
    public void testDateFormatNullPatternFails() {
        this.dateFormatFails(
                LABEL,
                null,
                LocalDate.now(),
                SpreadsheetFormatters.fake(),
                SpreadsheetFormatterContexts.fake()
        );
    }

    @Test
    public void testDateFormatNullValueFails() {
        this.dateFormatFails(
                LABEL,
                () -> "",
                null,
                SpreadsheetFormatters.fake(),
                SpreadsheetFormatterContexts.fake()
        );
    }

    @Test
    public void testDateFormatNullDefaultFormatterFails() {
        this.dateFormatFails(
                LABEL,
                () -> "",
                LocalDate.now(),
                null,
                SpreadsheetFormatterContexts.fake()
        );
    }

    @Test
    public void testDateFormatNullContextFails() {
        this.dateFormatFails(
                LABEL,
                () -> "",
                LocalDate.now(),
                SpreadsheetFormatters.fake(),
                null
        );
    }

    private void dateFormatFails(final String label,
                                 final Supplier<String> pattern,
                                 final LocalDate value,
                                 final SpreadsheetFormatter defaultFormatter,
                                 final SpreadsheetFormatterContext context) {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetPatternEditorWidgetSampleRowFormat.dateFormat(
                        label,
                        pattern,
                        value,
                        defaultFormatter,
                        context
                )
        );
    }

    @Test
    public void testDateFormatEmptyPattern() {
        this.dateFormatAndCheck(
                "",
                LocalDate.of(1999, 12, 31), // date
                "\"Default: \"yyyy/mm/dd", // defaultDateFormatter
                "Default: 1999/12/31", // defaultFormattedText
                "" // patternFormattedText
        );
    }

    @Test
    public void testDateFormatEmptyInvalidPattern() {
        this.dateFormatAndCheck(
                "\"Incomplete",
                LocalDate.of(1999, 12, 31), // date
                "\"Default: \"yyyy/mm/dd", // defaultDateFormatter
                "Default: 1999/12/31", // defaultFormattedText
                "" // patternFormattedText
        );
    }

    @Test
    public void testDateFormat() {
        this.dateFormatAndCheck(
                "dd/mm",
                LocalDate.of(1999, 12, 31), // date
                "\"Default: \"yyyy/mm/dd", // defaultDateFormatter
                "Default: 1999/12/31", // defaultFormattedText
                "31/12" // patternFormattedText
        );
    }

    @Test
    public void testDateFormat2() {
        this.dateFormatAndCheck(
                "\"The date is: \"yyyy/mm/dd",
                LocalDate.of(1999, 12, 31), // date
                "\"Default: \"yyyy/mm/dd", // defaultDateFormatter
                "Default: 1999/12/31", // defaultFormattedText
                "The date is: 1999/12/31" // patternFormattedText
        );
    }

    @Test
    public void testDateFormatWithColor() {
        this.dateFormatAndCheck(
                "[Blue]\"The date is \"yyyy/mm/dd",
                LocalDate.of(1999, 12, 31), // date
                "\"Default: \"yyyy/mm/dd", // defaultDateFormatter
                "Default: 1999/12/31", // defaultFormattedText
                SpreadsheetText.EMPTY.setText("The date is 1999/12/31")
                        .setColor(
                                Optional.of(BLUE)
                        ) // patternFormattedText
        );
    }

    private void dateFormatAndCheck(final String pattern,
                                    final LocalDate text,
                                    final String defaultDateFormatter,
                                    final String defaultFormattedText,
                                    final String patternFormattedText) {
        this.dateFormatAndCheck(
                pattern,
                text,
                defaultDateFormatter,
                defaultFormattedText,
                SpreadsheetText.EMPTY.setText(patternFormattedText)
        );
    }

    private void dateFormatAndCheck(final String pattern,
                                    final LocalDate text,
                                    final String defaultDateFormatter,
                                    final String defaultFormattedText,
                                    final SpreadsheetText patternFormattedText) {
        this.check(
                SpreadsheetPatternEditorWidgetSampleRowFormat.dateFormat(
                        LABEL,
                        () -> pattern,
                        text,
                        SpreadsheetPattern.parseDateFormatPattern(defaultDateFormatter).formatter(),
                        new FakeSpreadsheetFormatterContext() {
                            @Override
                            public boolean canConvert(final Object value,
                                                      final Class<?> type) {
                                return value instanceof LocalDate && LocalDateTime.class == type;
                            }

                            @Override
                            public <T> Either<T, String> convert(final Object value,
                                                                 final Class<T> target) {
                                this.canConvertOrFail(value, target);

                                return this.successfulConversion(
                                        LocalDateTime.of(LocalDate.class.cast(value), LocalTime.MIDNIGHT),
                                        target
                                );
                            }

                            @Override
                            public Optional<Color> colorName(final SpreadsheetColorName name) {
                                checkEquals("Blue", name.value());
                                return Optional.of(BLUE);
                            }
                        }
                ),
                LABEL,
                pattern,
                defaultFormattedText,
                patternFormattedText
        );
    }

    // dateTimeFormat...................................................................................................

    @Test
    public void testDateTimeFormatNullLabelFails() {
        this.dateTimeFormatFails(
                null,
                () -> "",
                LocalDateTime.now(),
                SpreadsheetFormatters.fake(),
                SpreadsheetFormatterContexts.fake()
        );
    }

    @Test
    public void testDateTimeFormatEmptyLabelFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> SpreadsheetPatternEditorWidgetSampleRowFormat.dateTimeFormat(
                        "",
                        () -> "",
                        LocalDateTime.now(),
                        SpreadsheetFormatters.fake(),
                        SpreadsheetFormatterContexts.fake()
                )
        );
    }

    @Test
    public void testDateTimeFormatNullPatternFails() {
        this.dateTimeFormatFails(
                LABEL,
                null,
                LocalDateTime.now(),
                SpreadsheetFormatters.fake(),
                SpreadsheetFormatterContexts.fake()
        );
    }

    @Test
    public void testDateTimeFormatNullValueFails() {
        this.dateTimeFormatFails(
                LABEL,
                () -> "",
                null,
                SpreadsheetFormatters.fake(),
                SpreadsheetFormatterContexts.fake()
        );
    }

    @Test
    public void testDateTimeFormatNullDefaultFormatterFails() {
        this.dateTimeFormatFails(
                LABEL,
                () -> "",
                LocalDateTime.now(),
                null,
                SpreadsheetFormatterContexts.fake()
        );
    }

    @Test
    public void testDateTimeFormatNullContextFails() {
        this.dateTimeFormatFails(
                LABEL,
                () -> "",
                LocalDateTime.now(),
                SpreadsheetFormatters.fake(),
                null
        );
    }

    private void dateTimeFormatFails(final String label,
                                     final Supplier<String> pattern,
                                     final LocalDateTime value,
                                     final SpreadsheetFormatter defaultFormatter,
                                     final SpreadsheetFormatterContext context) {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetPatternEditorWidgetSampleRowFormat.dateTimeFormat(
                        label,
                        pattern,
                        value,
                        defaultFormatter,
                        context
                )
        );
    }

    @Test
    public void testDateTimeFormatEmptyPattern() {
        this.dateTimeFormatAndCheck(
                "",
                LocalDateTime.of(1999, 12, 31, 12, 58, 59), // dateTime
                "\"Default: \"yyyy/mm/dd hh:mm", // defaultDateTimeFormatter
                "Default: 1999/12/31 12:58", // defaultFormattedText
                "" // patternFormattedText
        );
    }

    @Test
    public void testDateTimeFormatEmptyInvalidPattern() {
        this.dateTimeFormatAndCheck(
                "\"Incomplete",
                LocalDateTime.of(1999, 12, 31, 12, 58, 59), // dateTime
                "\"Default: \"yyyy/mm/dd hh:mm", // defaultDateTimeFormatter
                "Default: 1999/12/31 12:58", // defaultFormattedText
                "" // patternFormattedText
        );
    }

    @Test
    public void testDateTimeFormat() {
        this.dateTimeFormatAndCheck(
                "dd/mm",
                LocalDateTime.of(1999, 12, 31, 12, 58, 59), // dateTime
                "\"Default: \"yyyy/mm/dd hh:mm", // defaultDateTimeFormatter
                "Default: 1999/12/31 12:58", // defaultFormattedText
                "31/12" // patternFormattedText
        );
    }

    @Test
    public void testDateTimeFormat2() {
        this.dateTimeFormatAndCheck(
                "\"The dateTime is: \"hh:mm yyyy/mm/dd",
                LocalDateTime.of(1999, 12, 31, 12, 58, 59), // dateTime
                "\"Default: \"yyyy/mm/dd hh:mm", // defaultDateTimeFormatter
                "Default: 1999/12/31 12:58", // defaultFormattedText
                "The dateTime is: 12:58 1999/12/31" // patternFormattedText
        );
    }

    @Test
    public void testDateTimeFormatWithColor() {
        this.dateTimeFormatAndCheck(
                "[Blue]\"The dateTime is \"yyyy/mm/dd",
                LocalDateTime.of(1999, 12, 31, 12, 58, 59), // dateTime
                "\"Default: \"yyyy/mm/dd hh:mm", // defaultDateTimeFormatter
                "Default: 1999/12/31 12:58", // defaultFormattedText
                SpreadsheetText.EMPTY.setText("The dateTime is 1999/12/31")
                        .setColor(
                                Optional.of(BLUE)
                        ) // patternFormattedText
        );
    }

    private void dateTimeFormatAndCheck(final String pattern,
                                        final LocalDateTime text,
                                        final String defaultDateTimeFormatter,
                                        final String defaultFormattedText,
                                        final String patternFormattedText) {
        this.dateTimeFormatAndCheck(
                pattern,
                text,
                defaultDateTimeFormatter,
                defaultFormattedText,
                SpreadsheetText.EMPTY.setText(patternFormattedText)
        );
    }

    private void dateTimeFormatAndCheck(final String pattern,
                                        final LocalDateTime text,
                                        final String defaultDateTimeFormatter,
                                        final String defaultFormattedText,
                                        final SpreadsheetText patternFormattedText) {
        this.check(
                SpreadsheetPatternEditorWidgetSampleRowFormat.dateTimeFormat(
                        LABEL,
                        () -> pattern,
                        text,
                        SpreadsheetPattern.parseDateTimeFormatPattern(defaultDateTimeFormatter).formatter(),
                        new FakeSpreadsheetFormatterContext() {
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
                                        value,
                                        target
                                );
                            }

                            @Override
                            public Optional<Color> colorName(final SpreadsheetColorName name) {
                                checkEquals("Blue", name.value());
                                return Optional.of(BLUE);
                            }
                        }
                ),
                LABEL,
                pattern,
                defaultFormattedText,
                patternFormattedText
        );
    }

    // numberFormat.....................................................................................................

    @Test
    public void testNumberFormatNullLabelFails() {
        this.numberFormatFails(
                null,
                () -> "",
                ExpressionNumberKind.DOUBLE.one(),
                SpreadsheetFormatters.fake(),
                SpreadsheetFormatterContexts.fake()
        );
    }

    @Test
    public void testNumberFormatEmptyLabelFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> SpreadsheetPatternEditorWidgetSampleRowFormat.numberFormat(
                        "",
                        () -> "",
                        ExpressionNumberKind.DOUBLE.one(),
                        SpreadsheetFormatters.fake(),
                        SpreadsheetFormatterContexts.fake()
                )
        );
    }

    @Test
    public void testNumberFormatNullPatternFails() {
        this.numberFormatFails(
                LABEL,
                null,
                ExpressionNumberKind.DOUBLE.one(),
                SpreadsheetFormatters.fake(),
                SpreadsheetFormatterContexts.fake()
        );
    }

    @Test
    public void testNumberFormatNullValueFails() {
        this.numberFormatFails(
                LABEL,
                () -> "",
                null,
                SpreadsheetFormatters.fake(),
                SpreadsheetFormatterContexts.fake()
        );
    }

    @Test
    public void testNumberFormatNullDefaultFormatterFails() {
        this.numberFormatFails(
                LABEL,
                () -> "",
                ExpressionNumberKind.DOUBLE.one(),
                null,
                SpreadsheetFormatterContexts.fake()
        );
    }

    @Test
    public void testNumberFormatNullContextFails() {
        this.numberFormatFails(
                LABEL,
                () -> "",
                ExpressionNumberKind.DOUBLE.one(),
                SpreadsheetFormatters.fake(),
                null
        );
    }

    private void numberFormatFails(final String label,
                                   final Supplier<String> pattern,
                                   final ExpressionNumber value,
                                   final SpreadsheetFormatter defaultFormatter,
                                   final SpreadsheetFormatterContext context) {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetPatternEditorWidgetSampleRowFormat.numberFormat(
                        label,
                        pattern,
                        value,
                        defaultFormatter,
                        context
                )
        );
    }

    @Test
    public void testNumberFormatEmptyPattern() {
        this.numberFormatAndCheck(
                "",
                1.25, // number
                "\"Default: \"0.00", // defaultNumberFormatter
                "Default: 1D25", // defaultFormattedText
                "" // patternFormattedText
        );
    }

    @Test
    public void testNumberFormatEmptyInvalidPattern() {
        this.numberFormatAndCheck(
                "\"Unclosed",
                1.5, // number
                "\"Default: \"0.0", // defaultNumberFormatter
                "Default: 1D5", // defaultFormattedText
                "" // patternFormattedText
        );
    }

    @Test
    public void testNumberFormat() {
        this.numberFormatAndCheck(
                "$0.00",
                1.5, // number
                "\"Default: \"0.0", // defaultNumberFormatter
                "Default: 1D5", // defaultFormattedText
                "AUD1D50" // patternFormattedText
        );
    }

    @Test
    public void testNumberFormat2() {
        this.numberFormatAndCheck(
                "$0.000",
                123, // number
                "\"Default: \"0.0", // defaultNumberFormatter
                "Default: 123D0", // defaultFormattedText
                "AUD123D000" // patternFormattedText
        );
    }

    @Test
    public void testNumberFormatWithColor() {
        this.numberFormatAndCheck(
                "[Blue]0",
                123, // number
                "\"Default: \"0", // defaultNumberFormatter
                "Default: 123", // defaultFormattedText
                SpreadsheetText.EMPTY.setText("123")
                        .setColor(
                                Optional.of(BLUE)
                        ) // patternFormattedText
        );
    }

    private void numberFormatAndCheck(final String pattern,
                                      final double number,
                                      final String defaultNumberFormatter,
                                      final String defaultFormattedText,
                                      final String patternFormattedText) {
        this.numberFormatAndCheck(
                pattern,
                number,
                defaultNumberFormatter,
                defaultFormattedText,
                SpreadsheetText.EMPTY.setText(patternFormattedText)
        );
    }

    private void numberFormatAndCheck(final String pattern,
                                      final double number,
                                      final String defaultNumberFormatter,
                                      final String defaultFormattedText,
                                      final SpreadsheetText patternFormattedText) {
        this.check(
                SpreadsheetPatternEditorWidgetSampleRowFormat.numberFormat(
                        LABEL,
                        () -> pattern,
                        ExpressionNumberKind.BIG_DECIMAL.create(number),
                        SpreadsheetPattern.parseNumberFormatPattern(defaultNumberFormatter).formatter(),
                        new FakeSpreadsheetFormatterContext() {
                            @Override
                            public boolean canConvert(final Object value,
                                                      final Class<?> type) {
                                return value instanceof ExpressionNumber && ExpressionNumber.class == type;
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

                            @Override
                            public Optional<Color> colorName(final SpreadsheetColorName name) {
                                checkEquals("Blue", name.value());
                                return Optional.of(BLUE);
                            }

                            @Override
                            public String currencySymbol() {
                                return "AUD";
                            }

                            @Override
                            public char decimalSeparator() {
                                return 'D';
                            }

                            @Override public String exponentSymbol() {
                                return super.exponentSymbol();
                            }

                            @Override public char groupSeparator() {
                                return super.groupSeparator();
                            }

                            @Override public char negativeSign() {
                                return super.negativeSign();
                            }

                            @Override public char percentageSymbol() {
                                return super.percentageSymbol();
                            }

                            @Override
                            public char positiveSign() {
                                return super.positiveSign();
                            }

                            @Override
                            public MathContext mathContext() {
                                return MathContext.DECIMAL32;
                            }
                        }
                ),
                LABEL,
                pattern,
                defaultFormattedText,
                patternFormattedText
        );
    }

    // textFormat.......................................................................................................

    @Test
    public void testTextFormatNullLabelFails() {
        this.textFormatFails(
                null,
                () -> "",
                "Abc123",
                SpreadsheetFormatters.fake(),
                SpreadsheetFormatterContexts.fake()
        );
    }

    @Test
    public void testTextFormatEmptyLabelFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> SpreadsheetPatternEditorWidgetSampleRowFormat.textFormat(
                        "",
                        () -> "",
                        "Abc123",
                        SpreadsheetFormatters.fake(),
                        SpreadsheetFormatterContexts.fake()
                )
        );
    }

    @Test
    public void testTextFormatNullPatternFails() {
        this.textFormatFails(
                LABEL,
                null,
                "Abc123",
                SpreadsheetFormatters.fake(),
                SpreadsheetFormatterContexts.fake()
        );
    }

    @Test
    public void testTextFormatNullValueFails() {
        this.textFormatFails(
                LABEL,
                () -> "",
                null,
                SpreadsheetFormatters.fake(),
                SpreadsheetFormatterContexts.fake()
        );
    }

    @Test
    public void testTextFormatNullDefaultFormatterFails() {
        this.textFormatFails(
                LABEL,
                () -> "",
                "Abc123",
                null,
                SpreadsheetFormatterContexts.fake()
        );
    }

    @Test
    public void testTextFormatNullContextFails() {
        this.textFormatFails(
                LABEL,
                () -> "",
                "Abc123",
                SpreadsheetFormatters.fake(),
                null
        );
    }

    private void textFormatFails(final String label,
                                 final Supplier<String> pattern,
                                 final String value,
                                 final SpreadsheetFormatter defaultFormatter,
                                 final SpreadsheetFormatterContext context) {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetPatternEditorWidgetSampleRowFormat.textFormat(
                        label,
                        pattern,
                        value,
                        defaultFormatter,
                        context
                )
        );
    }

    @Test
    public void testTextFormatEmptyPattern() {
        this.textFormatAndCheck(
                "",
                "abc123", // text
                "\"Default: \"@", // defaultTextFormatter
                "Default: abc123", // defaultFormattedText
                "" // patternFormattedText
        );
    }

    @Test
    public void testTextFormatEmptyInvalidPattern() {
        this.textFormatAndCheck(
                "\"Unclosed",
                "abc123", // text
                "\"Default: \"@", // defaultTextFormatter
                "Default: abc123", // defaultFormattedText
                "" // patternFormattedText
        );
    }

    @Test
    public void testTextFormat() {
        this.textFormatAndCheck(
                "@",
                "abc123", // text
                "\"Default: \"@", // defaultTextFormatter
                "Default: abc123", // defaultFormattedText
                "abc123" // patternFormattedText
        );
    }

    @Test
    public void testTextFormat2() {
        this.textFormatAndCheck(
                "@@",
                "abc123", // text
                "\"Default: \"@", // defaultTextFormatter
                "Default: abc123", // defaultFormattedText
                "abc123abc123" // patternFormattedText
        );
    }

    @Test
    public void testTextFormatWithColor() {
        this.textFormatAndCheck(
                "[Blue]@@",
                "abc123", // text
                "\"Default: \"@", // defaultTextFormatter
                "Default: abc123", // defaultFormattedText
                SpreadsheetText.EMPTY.setText("abc123abc123")
                        .setColor(
                                Optional.of(BLUE)
                        ) // patternFormattedText
        );
    }

    private void textFormatAndCheck(final String pattern,
                                    final String text,
                                    final String defaultTextFormatter,
                                    final String defaultFormattedText,
                                    final String patternFormattedText) {
        this.textFormatAndCheck(
                pattern,
                text,
                defaultTextFormatter,
                defaultFormattedText,
                SpreadsheetText.EMPTY.setText(patternFormattedText)
        );
    }

    private void textFormatAndCheck(final String pattern,
                                    final String text,
                                    final String defaultTextFormatter,
                                    final String defaultFormattedText,
                                    final SpreadsheetText patternFormattedText) {
        this.check(
                SpreadsheetPatternEditorWidgetSampleRowFormat.textFormat(
                        LABEL,
                        () -> pattern,
                        text,
                        SpreadsheetPattern.parseTextFormatPattern(defaultTextFormatter).formatter(),
                        new FakeSpreadsheetFormatterContext() {
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

                            @Override
                            public Optional<Color> colorName(final SpreadsheetColorName name) {
                                checkEquals("Blue", name.value());
                                return Optional.of(BLUE);
                            }
                        }
                ),
                LABEL,
                pattern,
                defaultFormattedText,
                patternFormattedText
        );
    }

    // timeFormat.......................................................................................................

    @Test
    public void testTimeFormatNullLabelFails() {
        this.timeFormatFails(
                null,
                () -> "",
                LocalTime.now(),
                SpreadsheetFormatters.fake(),
                SpreadsheetFormatterContexts.fake()
        );
    }

    @Test
    public void testTimeFormatEmptyLabelFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> SpreadsheetPatternEditorWidgetSampleRowFormat.timeFormat(
                        "",
                        () -> "",
                        LocalTime.now(),
                        SpreadsheetFormatters.fake(),
                        SpreadsheetFormatterContexts.fake()
                )
        );
    }

    @Test
    public void testTimeFormatNullPatternFails() {
        this.timeFormatFails(
                LABEL,
                null,
                LocalTime.now(),
                SpreadsheetFormatters.fake(),
                SpreadsheetFormatterContexts.fake()
        );
    }

    @Test
    public void testTimeFormatNullValueFails() {
        this.timeFormatFails(
                LABEL,
                () -> "",
                null,
                SpreadsheetFormatters.fake(),
                SpreadsheetFormatterContexts.fake()
        );
    }

    @Test
    public void testTimeFormatNullDefaultFormatterFails() {
        this.timeFormatFails(
                LABEL,
                () -> "",
                LocalTime.now(),
                null,
                SpreadsheetFormatterContexts.fake()
        );
    }

    @Test
    public void testTimeFormatNullContextFails() {
        this.timeFormatFails(
                LABEL,
                () -> "",
                LocalTime.now(),
                SpreadsheetFormatters.fake(),
                null
        );
    }

    private void timeFormatFails(final String label,
                                 final Supplier<String> pattern,
                                 final LocalTime value,
                                 final SpreadsheetFormatter defaultFormatter,
                                 final SpreadsheetFormatterContext context) {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetPatternEditorWidgetSampleRowFormat.timeFormat(
                        label,
                        pattern,
                        value,
                        defaultFormatter,
                        context
                )
        );
    }

    @Test
    public void testTimeFormatEmptyPattern() {
        this.timeFormatAndCheck(
                "",
                LocalTime.NOON, // time
                "\"Default: \"hh:mm:ss", // defaultTimeFormatter
                "Default: 12:00:00", // defaultFormattedText
                "" // patternFormattedText
        );
    }

    @Test
    public void testTimeFormatEmptyInvalidPattern() {
        this.timeFormatAndCheck(
                "\"Unclosed",
                LocalTime.NOON, // time
                "\"Default: \"hh:mm:ss", // defaultTimeFormatter
                "Default: 12:00:00", // defaultFormattedText
                "" // patternFormattedText
        );
    }

    @Test
    public void testTimeFormat() {
        this.timeFormatAndCheck(
                "hh:mm",
                LocalTime.of(12, 58, 59), // time
                "\"Default: \"hh:mm:ss", // defaultTimeFormatter
                "Default: 12:58:59", // defaultFormattedText
                "12:58" // patternFormattedText
        );
    }

    @Test
    public void testTimeFormat2() {
        this.timeFormatAndCheck(
                "\"The time is: \"hh:mm",
                LocalTime.of(12, 58, 59), // time
                "\"Default: \"hh:mm:ss", // defaultTimeFormatter
                "Default: 12:58:59", // defaultFormattedText
                "The time is: 12:58" // patternFormattedText
        );
    }

    @Test
    public void testTimeFormatWithColor() {
        this.timeFormatAndCheck(
                "[Blue]\"The time is \"hh:mm",
                LocalTime.of(12, 58, 59), // time
                "\"Default: \"hh:mm:ss", // defaultTimeFormatter
                "Default: 12:58:59", // defaultFormattedText
                SpreadsheetText.EMPTY.setText("The time is 12:58")
                        .setColor(
                                Optional.of(BLUE)
                        ) // patternFormattedText
        );
    }

    private void timeFormatAndCheck(final String pattern,
                                    final LocalTime text,
                                    final String defaultTimeFormatter,
                                    final String defaultFormattedText,
                                    final String patternFormattedText) {
        this.timeFormatAndCheck(
                pattern,
                text,
                defaultTimeFormatter,
                defaultFormattedText,
                SpreadsheetText.EMPTY.setText(patternFormattedText)
        );
    }

    private void timeFormatAndCheck(final String pattern,
                                    final LocalTime text,
                                    final String defaultTimeFormatter,
                                    final String defaultFormattedText,
                                    final SpreadsheetText patternFormattedText) {
        this.check(
                SpreadsheetPatternEditorWidgetSampleRowFormat.timeFormat(
                        LABEL,
                        () -> pattern,
                        text,
                        SpreadsheetPattern.parseTimeFormatPattern(defaultTimeFormatter).formatter(),
                        new FakeSpreadsheetFormatterContext() {
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
                                        LocalDateTime.of(LocalDate.EPOCH, LocalTime.class.cast(value)),
                                        target
                                );
                            }

                            @Override
                            public Optional<Color> colorName(final SpreadsheetColorName name) {
                                checkEquals("Blue", name.value());
                                return Optional.of(BLUE);
                            }
                        }
                ),
                LABEL,
                pattern,
                defaultFormattedText,
                patternFormattedText
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                SpreadsheetPatternEditorWidgetSampleRowFormat.timeFormat(
                        LABEL,
                        () -> "",
                        LocalTime.now(),
                        SpreadsheetFormatters.fake(),
                        SpreadsheetFormatterContexts.fake()
                ),
                LABEL
        );
    }

    // ClassVisibility..................................................................................................

    @Override
    public Class<SpreadsheetPatternEditorWidgetSampleRowFormat<?>> type() {
        return Cast.to(SpreadsheetPatternEditorWidgetSampleRowFormat.class);
    }
}
