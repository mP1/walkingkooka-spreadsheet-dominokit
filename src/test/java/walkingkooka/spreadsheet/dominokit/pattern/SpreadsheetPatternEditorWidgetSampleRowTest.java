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
import walkingkooka.ToStringTesting;
import walkingkooka.color.Color;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.format.FakeSpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.SpreadsheetColorName;
import walkingkooka.spreadsheet.format.SpreadsheetFormatter;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContexts;
import walkingkooka.spreadsheet.format.SpreadsheetFormatters;
import walkingkooka.spreadsheet.format.SpreadsheetText;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern;
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

public final class SpreadsheetPatternEditorWidgetSampleRowTest implements ClassTesting<SpreadsheetPatternEditorWidgetSampleRow>,
        ToStringTesting<SpreadsheetPatternEditorWidgetSampleRow> {

    private final static String LABEL = "Label123";

    private final static Supplier<Optional<String>> PATTERN_TEXT_SUPPLIER = () -> Optional.of("");

    private final static Supplier<Optional<? extends SpreadsheetFormatPattern>> FORMAT_PATTERN_SUPPLIER = Optional::empty;

    private final static Color BLUE = Color.parse("#12f");

    // dateFormat.......................................................................................................

    @Test
    public void testWithNullLabelFails() {
        this.withFails(
                null,
                PATTERN_TEXT_SUPPLIER,
                LocalDate.now(),
                SpreadsheetFormatters.fake(),
                FORMAT_PATTERN_SUPPLIER,
                SpreadsheetFormatterContexts.fake()
        );
    }

    @Test
    public void testWithEmptyLabelFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> SpreadsheetPatternEditorWidgetSampleRow.with(
                        "",
                        PATTERN_TEXT_SUPPLIER,
                        LocalDate.now(),
                        SpreadsheetFormatters.fake(),
                        FORMAT_PATTERN_SUPPLIER,
                        SpreadsheetFormatterContexts.fake()
                )
        );
    }

    @Test
    public void testWithNullPatternTextFails() {
        this.withFails(
                LABEL,
                null,
                LocalDate.now(),
                SpreadsheetFormatters.fake(),
                FORMAT_PATTERN_SUPPLIER,
                SpreadsheetFormatterContexts.fake()
        );
    }

    @Test
    public void testWithNullValueFails() {
        this.withFails(
                LABEL,
                PATTERN_TEXT_SUPPLIER,
                null,
                SpreadsheetFormatters.fake(),
                FORMAT_PATTERN_SUPPLIER,
                SpreadsheetFormatterContexts.fake()
        );
    }

    @Test
    public void testWithNullDefaultFormatterFails() {
        this.withFails(
                LABEL,
                PATTERN_TEXT_SUPPLIER,
                LocalDate.now(),
                null,
                FORMAT_PATTERN_SUPPLIER,
                SpreadsheetFormatterContexts.fake()
        );
    }

    @Test
    public void testWithNullContextFails() {
        this.withFails(
                LABEL,
                PATTERN_TEXT_SUPPLIER,
                LocalDate.now(),
                SpreadsheetFormatters.fake(),
                FORMAT_PATTERN_SUPPLIER,
                null
        );
    }

    private void withFails(final String label,
                           final Supplier<Optional<String>> patternText,
                           final Object value,
                           final SpreadsheetFormatter defaultFormatter,
                           final Supplier<Optional<? extends SpreadsheetFormatPattern>> formatPattern,
                           final SpreadsheetFormatterContext context) {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetPatternEditorWidgetSampleRow.with(
                        label,
                        patternText,
                        value,
                        defaultFormatter,
                        formatPattern,
                        context
                )
        );
    }

    // dateFormat......................................................................................................

    @Test
    public void testDateFormatEmptyPattern() {
        this.dateFormatAndCheck(
                "",
                LocalDate.of(1999, 12, 31), // date
                "\"Default: \"yyyy/mm/dd", // defaultDateFormatter
                "Default: 1999/12/31", // defaultFormattedText
                "" // patternFormattedValue
        );
    }

    @Test
    public void testDateFormatEmptyInvalidPattern() {
        this.dateFormatAndCheck(
                "\"Incomplete",
                LocalDate.of(1999, 12, 31), // date
                "\"Default: \"yyyy/mm/dd", // defaultDateFormatter
                "Default: 1999/12/31", // defaultFormattedText
                "" // patternFormattedValue
        );
    }

    @Test
    public void testDateFormat() {
        this.dateFormatAndCheck(
                "dd/mm",
                LocalDate.of(1999, 12, 31), // date
                "\"Default: \"yyyy/mm/dd", // defaultDateFormatter
                "Default: 1999/12/31", // defaultFormattedText
                "31/12" // patternFormattedValue
        );
    }

    @Test
    public void testDateFormat2() {
        this.dateFormatAndCheck(
                "\"The date is: \"yyyy/mm/dd",
                LocalDate.of(1999, 12, 31), // date
                "\"Default: \"yyyy/mm/dd", // defaultDateFormatter
                "Default: 1999/12/31", // defaultFormattedText
                "The date is: 1999/12/31" // patternFormattedValue
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
                        ) // patternFormattedValue
        );
    }

    private void dateFormatAndCheck(final String patternText,
                                    final LocalDate text,
                                    final String defaultDateFormatter,
                                    final String defaultFormattedText,
                                    final String patternFormatted) {
        this.dateFormatAndCheck(
                patternText,
                text,
                defaultDateFormatter,
                defaultFormattedText,
                SpreadsheetText.EMPTY.setText(patternFormatted)
        );
    }

    private void dateFormatAndCheck(final String patternText,
                                    final LocalDate value,
                                    final String defaultDateFormatter,
                                    final String defaultFormattedText,
                                    final SpreadsheetText patternFormatted) {
        final Supplier<Optional<String>> patternTextSupplier = () -> Optional.of(patternText);

        this.check(
                SpreadsheetPatternEditorWidgetSampleRow.with(
                        LABEL,
                        patternTextSupplier,
                        value,
                        SpreadsheetPattern.parseDateFormatPattern(defaultDateFormatter)
                                .formatter(),
                        SpreadsheetPatternEditorWidgetSampleRow.formatPatternSupplier(
                                patternTextSupplier,
                                SpreadsheetPattern::parseDateFormatPattern
                        ),
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
                patternText,
                defaultFormattedText,
                patternFormatted
        );
    }

    // dateTimeFormat...................................................................................................

    @Test
    public void testDateTimeFormatEmptyPattern() {
        this.dateTimeFormatAndCheck(
                "",
                LocalDateTime.of(1999, 12, 31, 12, 58, 59), // dateTime
                "\"Default: \"yyyy/mm/dd hh:mm", // defaultDateTimeFormatter
                "Default: 1999/12/31 12:58", // defaultFormattedText
                "" // patternFormattedValue
        );
    }

    @Test
    public void testDateTimeFormatEmptyInvalidPattern() {
        this.dateTimeFormatAndCheck(
                "\"Incomplete",
                LocalDateTime.of(1999, 12, 31, 12, 58, 59), // dateTime
                "\"Default: \"yyyy/mm/dd hh:mm", // defaultDateTimeFormatter
                "Default: 1999/12/31 12:58", // defaultFormattedText
                "" // patternFormattedValue
        );
    }

    @Test
    public void testDateTimeFormat() {
        this.dateTimeFormatAndCheck(
                "dd/mm",
                LocalDateTime.of(1999, 12, 31, 12, 58, 59), // dateTime
                "\"Default: \"yyyy/mm/dd hh:mm", // defaultDateTimeFormatter
                "Default: 1999/12/31 12:58", // defaultFormattedText
                "31/12" // patternFormattedValue
        );
    }

    @Test
    public void testDateTimeFormat2() {
        this.dateTimeFormatAndCheck(
                "\"The dateTime is: \"hh:mm yyyy/mm/dd",
                LocalDateTime.of(1999, 12, 31, 12, 58, 59), // dateTime
                "\"Default: \"yyyy/mm/dd hh:mm", // defaultDateTimeFormatter
                "Default: 1999/12/31 12:58", // defaultFormattedText
                "The dateTime is: 12:58 1999/12/31" // patternFormattedValue
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
                        ) // patternFormattedValue
        );
    }

    private void dateTimeFormatAndCheck(final String patternText,
                                        final LocalDateTime value,
                                        final String defaultDateTimeFormatter,
                                        final String defaultFormattedText,
                                        final String patternFormatted) {
        this.dateTimeFormatAndCheck(
                patternText,
                value,
                defaultDateTimeFormatter,
                defaultFormattedText,
                SpreadsheetText.EMPTY.setText(patternFormatted)
        );
    }

    private void dateTimeFormatAndCheck(final String patternText,
                                        final LocalDateTime value,
                                        final String defaultDateTimeFormatter,
                                        final String defaultFormattedText,
                                        final SpreadsheetText patternFormatted) {
        final Supplier<Optional<String>> patternTextSupplier = () -> Optional.of(patternText);

        this.check(
                SpreadsheetPatternEditorWidgetSampleRow.with(
                        LABEL,
                        patternTextSupplier,
                        value,
                        SpreadsheetPattern.parseDateTimeFormatPattern(defaultDateTimeFormatter)
                                .formatter(),
                        SpreadsheetPatternEditorWidgetSampleRow.formatPatternSupplier(
                                patternTextSupplier,
                                SpreadsheetPattern::parseDateTimeFormatPattern
                        ),
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
                patternText,
                defaultFormattedText,
                patternFormatted
        );
    }

    // numberFormat.....................................................................................................

    @Test
    public void testNumberFormatEmptyPattern() {
        this.numberFormatAndCheck(
                "",
                1.25, // number
                "\"Default: \"0.00", // defaultNumberFormatter
                "Default: 1D25", // defaultFormattedText
                "" // patternFormattedValue
        );
    }

    @Test
    public void testNumberFormatEmptyInvalidPattern() {
        this.numberFormatAndCheck(
                "\"Unclosed",
                1.5, // number
                "\"Default: \"0.0", // defaultNumberFormatter
                "Default: 1D5", // defaultFormattedText
                "" // patternFormattedValue
        );
    }

    @Test
    public void testNumberFormat() {
        this.numberFormatAndCheck(
                "$0.00",
                1.5, // number
                "\"Default: \"0.0", // defaultNumberFormatter
                "Default: 1D5", // defaultFormattedText
                "AUD1D50" // patternFormattedValue
        );
    }

    @Test
    public void testNumberFormat2() {
        this.numberFormatAndCheck(
                "$0.000",
                123, // number
                "\"Default: \"0.0", // defaultNumberFormatter
                "Default: 123D0", // defaultFormattedText
                "AUD123D000" // patternFormattedValue
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
                        ) // patternFormattedValue
        );
    }

    private void numberFormatAndCheck(final String patternText,
                                      final double number,
                                      final String defaultNumberFormatter,
                                      final String defaultFormattedText,
                                      final String patternFormatted) {
        this.numberFormatAndCheck(
                patternText,
                number,
                defaultNumberFormatter,
                defaultFormattedText,
                SpreadsheetText.EMPTY.setText(patternFormatted)
        );
    }

    private void numberFormatAndCheck(final String patternText,
                                      final double number,
                                      final String defaultNumberFormatter,
                                      final String defaultFormattedText,
                                      final SpreadsheetText patternFormatted) {
        final Supplier<Optional<String>> patternTextSupplier = () -> Optional.of(patternText);

        this.check(
                SpreadsheetPatternEditorWidgetSampleRow.with(
                        LABEL,
                        patternTextSupplier,
                        ExpressionNumberKind.BIG_DECIMAL.create(number),
                        SpreadsheetPattern.parseNumberFormatPattern(defaultNumberFormatter)
                                .formatter(),
                        SpreadsheetPatternEditorWidgetSampleRow.formatPatternSupplier(
                                patternTextSupplier,
                                SpreadsheetPattern::parseNumberFormatPattern
                        ),
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
                patternText,
                defaultFormattedText,
                patternFormatted
        );
    }

    // textFormat.......................................................................................................

    @Test
    public void testTextFormatEmptyPattern() {
        this.textFormatAndCheck(
                "",
                "abc123", // text
                "\"Default: \"@", // defaultTextFormatter
                "Default: abc123", // defaultFormattedText
                "" // patternFormattedValue
        );
    }

    @Test
    public void testTextFormatEmptyInvalidPattern() {
        this.textFormatAndCheck(
                "\"Unclosed",
                "abc123", // text
                "\"Default: \"@", // defaultTextFormatter
                "Default: abc123", // defaultFormattedText
                "" // patternFormattedValue
        );
    }

    @Test
    public void testTextFormat() {
        this.textFormatAndCheck(
                "@",
                "abc123", // text
                "\"Default: \"@", // defaultTextFormatter
                "Default: abc123", // defaultFormattedText
                "abc123" // patternFormattedValue
        );
    }

    @Test
    public void testTextFormat2() {
        this.textFormatAndCheck(
                "@@",
                "abc123", // text
                "\"Default: \"@", // defaultTextFormatter
                "Default: abc123", // defaultFormattedText
                "abc123abc123" // patternFormattedValue
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
                        ) // patternFormattedValue
        );
    }

    private void textFormatAndCheck(final String patternText,
                                    final String value,
                                    final String defaultTextFormatter,
                                    final String defaultFormattedText,
                                    final String patternFormatted) {
        this.textFormatAndCheck(
                patternText,
                value,
                defaultTextFormatter,
                defaultFormattedText,
                SpreadsheetText.EMPTY.setText(patternFormatted)
        );
    }

    private void textFormatAndCheck(final String patternText,
                                    final String text,
                                    final String defaultTextFormatter,
                                    final String defaultFormattedText,
                                    final SpreadsheetText patternFormatted) {
        final Supplier<Optional<String>> patternTextSupplier = () -> Optional.of(patternText);

        this.check(
                SpreadsheetPatternEditorWidgetSampleRow.with(
                        LABEL,
                        patternTextSupplier,
                        text,
                        SpreadsheetPattern.parseTextFormatPattern(defaultTextFormatter)
                                .formatter(),
                        SpreadsheetPatternEditorWidgetSampleRow.formatPatternSupplier(
                                patternTextSupplier,
                                SpreadsheetPattern::parseTextFormatPattern
                        ),
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
                patternText,
                defaultFormattedText,
                patternFormatted
        );
    }

    // timeFormat.......................................................................................................

    @Test
    public void testTimeFormatEmptyPattern() {
        this.timeFormatAndCheck(
                "",
                LocalTime.NOON, // time
                "\"Default: \"hh:mm:ss", // defaultTimeFormatter
                "Default: 12:00:00", // defaultFormattedText
                "" // patternFormattedValue
        );
    }

    @Test
    public void testTimeFormatEmptyInvalidPattern() {
        this.timeFormatAndCheck(
                "\"Unclosed",
                LocalTime.NOON, // time
                "\"Default: \"hh:mm:ss", // defaultTimeFormatter
                "Default: 12:00:00", // defaultFormattedText
                "" // patternFormattedValue
        );
    }

    @Test
    public void testTimeFormat() {
        this.timeFormatAndCheck(
                "hh:mm",
                LocalTime.of(12, 58, 59), // time
                "\"Default: \"hh:mm:ss", // defaultTimeFormatter
                "Default: 12:58:59", // defaultFormattedText
                "12:58" // patternFormattedValue
        );
    }

    @Test
    public void testTimeFormat2() {
        this.timeFormatAndCheck(
                "\"The time is: \"hh:mm",
                LocalTime.of(12, 58, 59), // time
                "\"Default: \"hh:mm:ss", // defaultTimeFormatter
                "Default: 12:58:59", // defaultFormattedText
                "The time is: 12:58" // patternFormattedValue
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
                        ) // patternFormattedValue
        );
    }

    private void timeFormatAndCheck(final String patternText,
                                    final LocalTime value,
                                    final String defaultTimeFormatter,
                                    final String defaultFormattedText,
                                    final String patternFormatted) {
        this.timeFormatAndCheck(
                patternText,
                value,
                defaultTimeFormatter,
                defaultFormattedText,
                SpreadsheetText.EMPTY.setText(patternFormatted)
        );
    }

    private void timeFormatAndCheck(final String patternText,
                                    final LocalTime value,
                                    final String defaultTimeFormatter,
                                    final String defaultFormattedText,
                                    final SpreadsheetText patternFormatted) {
        final Supplier<Optional<String>> patternTextSupplier = () -> Optional.of(patternText);

        this.check(
                SpreadsheetPatternEditorWidgetSampleRow.with(
                        LABEL,
                        patternTextSupplier,
                        value,
                        SpreadsheetPattern.parseTimeFormatPattern(defaultTimeFormatter)
                                .formatter(),
                        SpreadsheetPatternEditorWidgetSampleRow.formatPatternSupplier(
                                patternTextSupplier,
                                SpreadsheetPattern::parseTimeFormatPattern
                        ),
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
                patternText,
                defaultFormattedText,
                patternFormatted
        );
    }

    private void check(final SpreadsheetPatternEditorWidgetSampleRow row,
                       final String label,
                       final String patternText,
                       final String value,
                       final String parsedOrFormatted) {
        this.check(
                row,
                label,
                patternText,
                value,
                SpreadsheetText.EMPTY.setText(parsedOrFormatted)
        );
    }

    private void check(final SpreadsheetPatternEditorWidgetSampleRow row,
                       final String label,
                       final String patternText,
                       final String defaultFormattedValue,
                       final SpreadsheetText patternFormattedValue) {
        this.checkEquals(
                label,
                row.label(),
                "label"
        );

        this.checkEquals(
                patternText,
                row.patternText(),
                "patternText"
        );

        this.checkEquals(
                defaultFormattedValue,
                row.defaultFormattedValue(),
                "defaultFormattedValue"
        );

        this.checkEquals(
                patternFormattedValue,
                row.patternFormattedValue(),
                "patternFormattedValue"
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                SpreadsheetPatternEditorWidgetSampleRow.with(
                        LABEL,
                        PATTERN_TEXT_SUPPLIER,
                        LocalTime.now(),
                        SpreadsheetFormatters.fake(),
                        FORMAT_PATTERN_SUPPLIER,
                        SpreadsheetFormatterContexts.fake()
                ),
                LABEL
        );
    }

    // ClassVisibility..................................................................................................

    @Override
    public Class<SpreadsheetPatternEditorWidgetSampleRow> type() {
        return SpreadsheetPatternEditorWidgetSampleRow.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
