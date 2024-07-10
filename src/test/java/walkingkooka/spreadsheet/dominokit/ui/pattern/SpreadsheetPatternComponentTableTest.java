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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.Either;
import walkingkooka.datetime.DateTimeContext;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.format.FakeSpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.tree.expression.ExpressionNumberKind;

import java.math.MathContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public final class SpreadsheetPatternComponentTableTest implements ClassTesting<SpreadsheetPatternComponentTable>,
        TreePrintableTesting {

    private final static SpreadsheetFormatterContext DATE_FORMATTER_CONTEXT = new FakeSpreadsheetFormatterContext() {

        @Override
        public <T> Either<T, String> convert(final Object value,
                                             final Class<T> target) {
            return this.successfulConversion(
                    LocalDateTime.of(
                            (LocalDate) value,
                            LocalTime.NOON
                    ),
                    target
            );
        }

        @Override
        public Locale locale() {
            return this.context.locale();
        }

        // cell / DateTimeContext

        @Override
        public List<String> ampms() {
            return this.context.ampms();
        }

        @Override
        public String ampm(int hourOfDay) {
            return this.context.ampm(hourOfDay);
        }

        @Override
        public int defaultYear() {
            return this.context.defaultYear();
        }

        @Override
        public List<String> monthNames() {
            return this.context.monthNames();
        }

        @Override
        public String monthName(int month) {
            return this.context.monthName(month);
        }

        @Override
        public List<String> monthNameAbbreviations() {
            return this.context.monthNameAbbreviations();
        }

        @Override
        public String monthNameAbbreviation(final int month) {
            return this.context.monthNameAbbreviation(month);
        }

        @Override
        public LocalDateTime now() {
            return this.context.now();
        }

        @Override
        public int twoDigitYear() {
            return this.context.twoDigitYear();
        }

        @Override
        public List<String> weekDayNames() {
            return this.context.weekDayNames();
        }

        @Override
        public String weekDayName(final int day) {
            return this.context.weekDayName(day);
        }

        @Override
        public List<String> weekDayNameAbbreviations() {
            return this.context.weekDayNameAbbreviations();
        }

        @Override
        public String weekDayNameAbbreviation(final int day) {
            return this.context.weekDayNameAbbreviation(day);
        }

        private final DateTimeContext context = DateTimeContexts.locale(
                Locale.forLanguageTag("EN-AU"), // cell / locale
                1950, // cell / default year
                50,
                () -> LocalDateTime.of(
                        1999,
                        12,
                        31,
                        12,
                        58,
                        59
                )
        );
    };

    private final static SpreadsheetFormatterContext DATE_TIME_FORMATTER_CONTEXT = new FakeSpreadsheetFormatterContext() {

        @Override
        public <T> Either<T, String> convert(final Object value,
                                             final Class<T> target) {
            return this.successfulConversion(
                    value,
                    target
            );
        }

        @Override
        public Locale locale() {
            return this.context.locale();
        }

        // cell / DateTimeContext

        @Override
        public List<String> ampms() {
            return this.context.ampms();
        }

        @Override
        public String ampm(int hourOfDay) {
            return this.context.ampm(hourOfDay);
        }

        @Override
        public int defaultYear() {
            return this.context.defaultYear();
        }

        @Override
        public List<String> monthNames() {
            return this.context.monthNames();
        }

        @Override
        public String monthName(int month) {
            return this.context.monthName(month);
        }

        @Override
        public List<String> monthNameAbbreviations() {
            return this.context.monthNameAbbreviations();
        }

        @Override
        public String monthNameAbbreviation(final int month) {
            return this.context.monthNameAbbreviation(month);
        }

        @Override
        public LocalDateTime now() {
            return this.context.now();
        }

        @Override
        public int twoDigitYear() {
            return this.context.twoDigitYear();
        }

        @Override
        public List<String> weekDayNames() {
            return this.context.weekDayNames();
        }

        @Override
        public String weekDayName(final int day) {
            return this.context.weekDayName(day);
        }

        @Override
        public List<String> weekDayNameAbbreviations() {
            return this.context.weekDayNameAbbreviations();
        }

        @Override
        public String weekDayNameAbbreviation(final int day) {
            return this.context.weekDayNameAbbreviation(day);
        }

        private final DateTimeContext context = DateTimeContexts.locale(
                Locale.forLanguageTag("EN-AU"), // cell / locale
                1950, // cell / default year
                50,
                () -> LocalDateTime.of(
                        1999,
                        12,
                        31,
                        12,
                        58,
                        59
                )
        );
    };

    private final static SpreadsheetFormatterContext NUMBER_FORMATTER_CONTEXT = new FakeSpreadsheetFormatterContext() {
        @Override
        public boolean canConvert(final Object value,
                                  final Class<?> type) {
            return true;
        }

        @Override
        public <T> Either<T, String> convert(final Object value,
                                             final Class<T> target) {
            return this.successfulConversion(
                    value,
                    target
            );
        }

        @Override
        public int generalFormatNumberDigitCount() {
            return SpreadsheetFormatterContext.DEFAULT_GENERAL_FORMAT_NUMBER_DIGIT_COUNT;
        }

        @Override
        public Locale locale() {
            return this.context.locale();
        }

        @Override
        public ExpressionNumberKind expressionNumberKind() {
            return ExpressionNumberKind.BIG_DECIMAL;
        }

        @Override
        public String currencySymbol() {
            return this.context.currencySymbol();
        }

        @Override
        public char decimalSeparator() {
            return this.context.decimalSeparator();
        }

        @Override
        public String exponentSymbol() {
            return this.context.exponentSymbol();
        }

        @Override
        public char groupSeparator() {
            return this.context.groupSeparator();
        }

        @Override
        public MathContext mathContext() {
            return this.context.mathContext();
        }

        @Override
        public char negativeSign() {
            return this.context.negativeSign();
        }

        @Override
        public char percentageSymbol() {
            return this.context.percentageSymbol();
        }

        @Override
        public char positiveSign() {
            return this.positiveSign();
        }

        private final DecimalNumberContext context = DecimalNumberContexts.american(MathContext.DECIMAL32);
    };

    private final static SpreadsheetFormatterContext TEXT_FORMATTER_CONTEXT = new FakeSpreadsheetFormatterContext() {

        @Override
        public boolean canConvert(final Object value,
                                  final Class<?> type) {
            return true;
        }

        @Override
        public <T> Either<T, String> convert(final Object value,
                                             final Class<T> target) {
            return this.successfulConversion(
                    value,
                    target
            );
        }
    };

    private final static SpreadsheetFormatterContext TIME_FORMATTER_CONTEXT = new FakeSpreadsheetFormatterContext() {

        @Override
        public <T> Either<T, String> convert(final Object value,
                                             final Class<T> target) {
            return this.successfulConversion(
                    LocalDateTime.of(
                            LocalDate.of(2000, 1, 1),
                            LocalTime.class.cast(value)
                    ),
                    target
            );
        }

        @Override
        public Locale locale() {
            return this.context.locale();
        }

        // cell / DateTimeContext

        @Override
        public List<String> ampms() {
            return this.context.ampms();
        }

        @Override
        public String ampm(int hourOfDay) {
            return this.context.ampm(hourOfDay);
        }

        @Override
        public LocalDateTime now() {
            return this.context.now();
        }

        private final DateTimeContext context = DateTimeContexts.locale(
                Locale.forLanguageTag("EN-AU"), // cell / locale
                1950, // cell / default year
                50,
                () -> LocalDateTime.of(
                        1999,
                        12,
                        31,
                        12,
                        58,
                        59
                )
        );
    };

    // date.............................................................................................................

    @Test
    public void testDateFormat() {
        final SpreadsheetPatternComponentTable table = SpreadsheetPatternComponentTable.empty();
        table.refresh(
                "dd/mm/yyyy",
                this.spreadsheetPatternDialogComponentContext(
                        "/1/Spreadsheet123/cell/A1/formatter/date",
                        DATE_FORMATTER_CONTEXT
                )
        );
        this.treePrintAndCheck(
                table,
                "SpreadsheetPatternComponentTable\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      SpreadsheetDataTableComponent\n" +
                        "        ROW(S)\n" +
                        "          ROW 0\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Edit Pattern\"\n" +
                        "            \"dd/mm/yyyy\" [#/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20dd/mm/yyyy] id=pattern-edit pattern-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              31/12/1999\n" +
                        "          ROW 1\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Today Short\"\n" +
                        "            \"d/m/yy\" [#/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20d/m/yy] id=pattern-today short-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              31/12/99\n" +
                        "          ROW 2\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Today Medium\"\n" +
                        "            \"d mmm yyyy\" [#/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20d%20mmm%20yyyy] id=pattern-today medium-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              31 Dec. 1999\n" +
                        "          ROW 3\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Today Long\"\n" +
                        "            \"d mmmm yyyy\" [#/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20d%20mmmm%20yyyy] id=pattern-today long-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              31 December 1999\n" +
                        "          ROW 4\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Today Full\"\n" +
                        "            \"dddd, d mmmm yyyy\" [#/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20dddd,%20d%20mmmm%20yyyy] id=pattern-today full-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              Friday, 31 December 1999\n" +
                        "          ROW 5\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31 December 1999 Short\"\n" +
                        "            \"d/m/yy\" [#/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20d/m/yy] id=pattern-31 december 1999 short-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              31/12/99\n" +
                        "          ROW 6\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31 December 1999 Medium\"\n" +
                        "            \"d mmm yyyy\" [#/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20d%20mmm%20yyyy] id=pattern-31 december 1999 medium-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              31 Dec. 1999\n" +
                        "          ROW 7\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31 December 1999 Long\"\n" +
                        "            \"d mmmm yyyy\" [#/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20d%20mmmm%20yyyy] id=pattern-31 december 1999 long-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              31 December 1999\n" +
                        "          ROW 8\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31 December 1999 Full\"\n" +
                        "            \"dddd, d mmmm yyyy\" [#/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20dddd,%20d%20mmmm%20yyyy] id=pattern-31 december 1999 full-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              Friday, 31 December 1999\n"
        );
    }

    @Test
    public void testDateParse() {
        final SpreadsheetPatternComponentTable table = SpreadsheetPatternComponentTable.empty();
        table.refresh(
                "dd/mm/yyyy",
                this.spreadsheetPatternDialogComponentContext(
                        "/1/Spreadsheet123/cell/A1/parser/date",
                        DATE_FORMATTER_CONTEXT
                )
        );
        this.treePrintAndCheck(
                table,
                "SpreadsheetPatternComponentTable\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      SpreadsheetDataTableComponent\n" +
                        "        ROW(S)\n" +
                        "          ROW 0\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Edit Pattern\"\n" +
                        "            \"dd/mm/yyyy\" [#/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20dd/mm/yyyy] id=pattern-edit pattern-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              31/12/1999\n" +
                        "          ROW 1\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Today Short\"\n" +
                        "            \"d/m/yy\" [#/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20d/m/yy] id=pattern-today short-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              31/12/99\n" +
                        "          ROW 2\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Today Medium\"\n" +
                        "            \"d mmm yyyy\" [#/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20d%20mmm%20yyyy] id=pattern-today medium-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              31 Dec. 1999\n" +
                        "          ROW 3\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Today Long\"\n" +
                        "            \"d mmmm yyyy\" [#/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20d%20mmmm%20yyyy] id=pattern-today long-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              31 December 1999\n" +
                        "          ROW 4\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Today Full\"\n" +
                        "            \"dddd, d mmmm yyyy\" [#/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20dddd,%20d%20mmmm%20yyyy] id=pattern-today full-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              Friday, 31 December 1999\n" +
                        "          ROW 5\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31 December 1999 Short\"\n" +
                        "            \"d/m/yy\" [#/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20d/m/yy] id=pattern-31 december 1999 short-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              31/12/99\n" +
                        "          ROW 6\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31 December 1999 Medium\"\n" +
                        "            \"d mmm yyyy\" [#/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20d%20mmm%20yyyy] id=pattern-31 december 1999 medium-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              31 Dec. 1999\n" +
                        "          ROW 7\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31 December 1999 Long\"\n" +
                        "            \"d mmmm yyyy\" [#/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20d%20mmmm%20yyyy] id=pattern-31 december 1999 long-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              31 December 1999\n" +
                        "          ROW 8\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31 December 1999 Full\"\n" +
                        "            \"dddd, d mmmm yyyy\" [#/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20dddd,%20d%20mmmm%20yyyy] id=pattern-31 december 1999 full-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              Friday, 31 December 1999\n"
        );
    }

    // date-time........................................................................................................

    @Test
    public void testDateTimeFormat() {
        final SpreadsheetPatternComponentTable table = SpreadsheetPatternComponentTable.empty();
        table.refresh(
                "dd/mm/yyyy hh:mm",
                this.spreadsheetPatternDialogComponentContext(
                        "/1/Spreadsheet123/cell/A1/formatter/date-time",
                        DATE_TIME_FORMATTER_CONTEXT
                )
        );
        this.treePrintAndCheck(
                table,
                "SpreadsheetPatternComponentTable\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      SpreadsheetDataTableComponent\n" +
                        "        ROW(S)\n" +
                        "          ROW 0\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Edit Pattern\"\n" +
                        "            \"dd/mm/yyyy hh:mm\" [#/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm] id=pattern-edit pattern-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              31/12/1999 12:58\n" +
                        "          ROW 1\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Today Short\"\n" +
                        "            \"d/m/yy, h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20d/m/yy,%20h:mm%20AM/PM] id=pattern-today short-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              31/12/99, 12:58 PM\n" +
                        "          ROW 2\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Today Medium\"\n" +
                        "            \"d mmm yyyy, h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20d%20mmm%20yyyy,%20h:mm:ss%20AM/PM] id=pattern-today medium-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              31 Dec. 1999, 12:58:59 PM\n" +
                        "          ROW 3\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Today Long\"\n" +
                        "            \"d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-today long-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              31 December 1999 at 12:58:59 PM\n" +
                        "          ROW 4\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Today Full\"\n" +
                        "            \"dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dddd,%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-today full-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              Friday, 31 December 1999 at 12:58:59 PM\n" +
                        "          ROW 5\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31 December 1999 12:58:59 Short\"\n" +
                        "            \"d/m/yy, h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20d/m/yy,%20h:mm%20AM/PM] id=pattern-31 december 1999 12:58:59 short-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              31/12/99, 12:58 PM\n" +
                        "          ROW 6\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31 December 1999 12:58:59 Medium\"\n" +
                        "            \"d mmm yyyy, h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20d%20mmm%20yyyy,%20h:mm:ss%20AM/PM] id=pattern-31 december 1999 12:58:59 medium-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              31 Dec. 1999, 12:58:59 PM\n" +
                        "          ROW 7\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31 December 1999 12:58:59 Long\"\n" +
                        "            \"d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-31 december 1999 12:58:59 long-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              31 December 1999 at 12:58:59 PM\n" +
                        "          ROW 8\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31 December 1999 12:58:59 Full\"\n" +
                        "            \"dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dddd,%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-31 december 1999 12:58:59 full-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              Friday, 31 December 1999 at 12:58:59 PM\n"
        );
    }

    @Test
    public void testDateTimeParse() {
        final SpreadsheetPatternComponentTable table = SpreadsheetPatternComponentTable.empty();
        table.refresh(
                "dd/mm/yyyy hh:mm",
                this.spreadsheetPatternDialogComponentContext(
                        "/1/Spreadsheet123/cell/A1/parser/date-time",
                        DATE_TIME_FORMATTER_CONTEXT
                )
        );
        this.treePrintAndCheck(
                table,
                "SpreadsheetPatternComponentTable\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      SpreadsheetDataTableComponent\n" +
                        "        ROW(S)\n" +
                        "          ROW 0\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Edit Pattern\"\n" +
                        "            \"dd/mm/yyyy hh:mm\" [#/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm] id=pattern-edit pattern-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              31/12/1999 12:58\n" +
                        "          ROW 1\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Today Short\"\n" +
                        "            \"d/m/yy, h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20d/m/yy,%20h:mm%20AM/PM] id=pattern-today short-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              31/12/99, 12:58 PM\n" +
                        "          ROW 2\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Today Medium\"\n" +
                        "            \"d mmm yyyy, h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20d%20mmm%20yyyy,%20h:mm:ss%20AM/PM] id=pattern-today medium-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              31 Dec. 1999, 12:58:59 PM\n" +
                        "          ROW 3\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Today Long\"\n" +
                        "            \"d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-today long-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              31 December 1999 at 12:58:59 PM\n" +
                        "          ROW 4\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Today Full\"\n" +
                        "            \"dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dddd,%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-today full-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              Friday, 31 December 1999 at 12:58:59 PM\n" +
                        "          ROW 5\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31 December 1999 12:58:59 Short\"\n" +
                        "            \"d/m/yy, h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20d/m/yy,%20h:mm%20AM/PM] id=pattern-31 december 1999 12:58:59 short-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              31/12/99, 12:58 PM\n" +
                        "          ROW 6\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31 December 1999 12:58:59 Medium\"\n" +
                        "            \"d mmm yyyy, h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20d%20mmm%20yyyy,%20h:mm:ss%20AM/PM] id=pattern-31 december 1999 12:58:59 medium-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              31 Dec. 1999, 12:58:59 PM\n" +
                        "          ROW 7\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31 December 1999 12:58:59 Long\"\n" +
                        "            \"d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-31 december 1999 12:58:59 long-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              31 December 1999 at 12:58:59 PM\n" +
                        "          ROW 8\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31 December 1999 12:58:59 Full\"\n" +
                        "            \"dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dddd,%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-31 december 1999 12:58:59 full-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              Friday, 31 December 1999 at 12:58:59 PM\n"
        );
    }

    // number...........................................................................................................

    @Test
    public void testNumberFormat() {
        final SpreadsheetPatternComponentTable table = SpreadsheetPatternComponentTable.empty();
        table.refresh(
                "$#.00",
                this.spreadsheetPatternDialogComponentContext(
                        "/1/Spreadsheet123/cell/A1/formatter/number",
                        NUMBER_FORMATTER_CONTEXT
                )
        );
        this.treePrintAndCheck(
                table,
                "SpreadsheetPatternComponentTable\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      SpreadsheetDataTableComponent\n" +
                        "        ROW(S)\n" +
                        "          ROW 0\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Edit Pattern\"\n" +
                        "            \"$#.00\" [#/1/Spreadsheet123/cell/A1/formatter/number/save/number-format-pattern%20$%23.00] id=pattern-edit pattern-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              $1234.56\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              $-9876.54\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              $.00\n" +
                        "          ROW 1\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"General\"\n" +
                        "            \"General\" [#/1/Spreadsheet123/cell/A1/formatter/number/save/number-format-pattern%20General] id=pattern-general-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              1234.56\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              -9876.54\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              0\n" +
                        "          ROW 2\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Number\"\n" +
                        "            \"#,##0.###\" [#/1/Spreadsheet123/cell/A1/formatter/number/save/number-format-pattern%20%23,%23%230.%23%23%23] id=pattern-number-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              1,234.56\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              -9,876.54\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              0.\n" +
                        "          ROW 3\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Integer\"\n" +
                        "            \"#,##0\" [#/1/Spreadsheet123/cell/A1/formatter/number/save/number-format-pattern%20%23,%23%230] id=pattern-integer-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              1,235\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              -9,877\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              0\n" +
                        "          ROW 4\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Percent\"\n" +
                        "            \"#,##0%\" [#/1/Spreadsheet123/cell/A1/formatter/number/save/number-format-pattern%20%23,%23%230%25] id=pattern-percent-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              123,456%\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              -987,654%\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              0%\n" +
                        "          ROW 5\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Currency\"\n" +
                        "            \"$#,##0.00\" [#/1/Spreadsheet123/cell/A1/formatter/number/save/number-format-pattern%20$%23,%23%230.00] id=pattern-currency-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              $1,234.56\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              $-9,876.54\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              $0.00\n"
        );
    }

    @Test
    public void testNumberParse() {
        final SpreadsheetPatternComponentTable table = SpreadsheetPatternComponentTable.empty();
        table.refresh(
                "$#.00",
                this.spreadsheetPatternDialogComponentContext(
                        "/1/Spreadsheet123/cell/A1/parser/number",
                        NUMBER_FORMATTER_CONTEXT
                )
        );
        this.treePrintAndCheck(
                table,
                "SpreadsheetPatternComponentTable\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      SpreadsheetDataTableComponent\n" +
                        "        ROW(S)\n" +
                        "          ROW 0\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Edit Pattern\"\n" +
                        "            \"$#.00\" [#/1/Spreadsheet123/cell/A1/parser/number/save/number-parse-pattern%20$%23.00] id=pattern-edit pattern-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              $1234.56\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              $-9876.54\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              $.00\n" +
                        "          ROW 1\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Number\"\n" +
                        "            \"#,##0.###\" [#/1/Spreadsheet123/cell/A1/parser/number/save/number-parse-pattern%20%23,%23%230.%23%23%23] id=pattern-number-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              1,234.56\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              -9,876.54\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              0.\n" +
                        "          ROW 2\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Integer\"\n" +
                        "            \"#,##0\" [#/1/Spreadsheet123/cell/A1/parser/number/save/number-parse-pattern%20%23,%23%230] id=pattern-integer-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              1,235\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              -9,877\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              0\n" +
                        "          ROW 3\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Percent\"\n" +
                        "            \"#,##0%\" [#/1/Spreadsheet123/cell/A1/parser/number/save/number-parse-pattern%20%23,%23%230%25] id=pattern-percent-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              123,456%\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              -987,654%\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              0%\n" +
                        "          ROW 4\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Currency\"\n" +
                        "            \"$#,##0.00\" [#/1/Spreadsheet123/cell/A1/parser/number/save/number-parse-pattern%20$%23,%23%230.00] id=pattern-currency-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              $1,234.56\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              $-9,876.54\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              $0.00\n"
        );
    }

    // number...........................................................................................................

    @Test
    public void testTextFormat() {
        final SpreadsheetPatternComponentTable table = SpreadsheetPatternComponentTable.empty();
        table.refresh(
                "@",
                this.spreadsheetPatternDialogComponentContext(
                        "/1/Spreadsheet123/cell/A1/formatter/text",
                        TEXT_FORMATTER_CONTEXT
                )
        );
        this.treePrintAndCheck(
                table,
                "SpreadsheetPatternComponentTable\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      SpreadsheetDataTableComponent\n" +
                        "        ROW(S)\n" +
                        "          ROW 0\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Edit Pattern\"\n" +
                        "            \"@\" [#/1/Spreadsheet123/cell/A1/formatter/text/save/text-format-pattern%20@] id=pattern-edit pattern-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              Abc123\n" +
                        "          ROW 1\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Default text format\"\n" +
                        "            \"@\" [#/1/Spreadsheet123/cell/A1/formatter/text/save/text-format-pattern%20@] id=pattern-default text format-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              Abc123\n"
        );
    }

    // time.............................................................................................................

    @Test
    public void testTimeFormat() {
        final SpreadsheetPatternComponentTable table = SpreadsheetPatternComponentTable.empty();
        table.refresh(
                "hh:mm:ss",
                this.spreadsheetPatternDialogComponentContext(
                        "/1/Spreadsheet123/cell/A1/formatter/time",
                        TIME_FORMATTER_CONTEXT
                )
        );
        this.treePrintAndCheck(
                table,
                "SpreadsheetPatternComponentTable\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      SpreadsheetDataTableComponent\n" +
                        "        ROW(S)\n" +
                        "          ROW 0\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Edit Pattern\"\n" +
                        "            \"hh:mm:ss\" [#/1/Spreadsheet123/cell/A1/formatter/time/save/time-format-pattern%20hh:mm:ss] id=pattern-edit pattern-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              12:58:59\n" +
                        "          ROW 1\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Now Short\"\n" +
                        "            \"h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/formatter/time/save/time-format-pattern%20h:mm%20AM/PM] id=pattern-now short-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              12:58 PM\n" +
                        "          ROW 2\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Now Long\"\n" +
                        "            \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/formatter/time/save/time-format-pattern%20h:mm:ss%20AM/PM] id=pattern-now long-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              12:58:59 PM\n" +
                        "          ROW 3\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"12:58:59 AM Short\"\n" +
                        "            \"h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/formatter/time/save/time-format-pattern%20h:mm%20AM/PM] id=pattern-12:58:59 am short-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              12:58 PM\n" +
                        "          ROW 4\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"12:58:59 AM Long\"\n" +
                        "            \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/formatter/time/save/time-format-pattern%20h:mm:ss%20AM/PM] id=pattern-12:58:59 am long-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              12:58:59 PM\n" +
                        "          ROW 5\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"6:01:02 PM Short\"\n" +
                        "            \"h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/formatter/time/save/time-format-pattern%20h:mm%20AM/PM] id=pattern-6:01:02 pm short-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              6:01 PM\n" +
                        "          ROW 6\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"6:01:02 PM Long\"\n" +
                        "            \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/formatter/time/save/time-format-pattern%20h:mm:ss%20AM/PM] id=pattern-6:01:02 pm long-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              6:01:02 PM\n"
        );
    }

    @Test
    public void testTimeParse() {
        final SpreadsheetPatternComponentTable table = SpreadsheetPatternComponentTable.empty();
        table.refresh(
                "hh:mm:ss",
                this.spreadsheetPatternDialogComponentContext(
                        "/1/Spreadsheet123/cell/A1/parser/time",
                        TIME_FORMATTER_CONTEXT
                )
        );
        this.treePrintAndCheck(
                table,
                "SpreadsheetPatternComponentTable\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      SpreadsheetDataTableComponent\n" +
                        "        ROW(S)\n" +
                        "          ROW 0\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Edit Pattern\"\n" +
                        "            \"hh:mm:ss\" [#/1/Spreadsheet123/cell/A1/parser/time/save/time-parse-pattern%20hh:mm:ss] id=pattern-edit pattern-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              12:58:59\n" +
                        "          ROW 1\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Now Short\"\n" +
                        "            \"h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/time/save/time-parse-pattern%20h:mm%20AM/PM] id=pattern-now short-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              12:58 PM\n" +
                        "          ROW 2\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"Now Long\"\n" +
                        "            \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/time/save/time-parse-pattern%20h:mm:ss%20AM/PM] id=pattern-now long-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              12:58:59 PM\n" +
                        "          ROW 3\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"12:58:59 AM Short\"\n" +
                        "            \"h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/time/save/time-parse-pattern%20h:mm%20AM/PM] id=pattern-12:58:59 am short-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              12:58 PM\n" +
                        "          ROW 4\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"12:58:59 AM Long\"\n" +
                        "            \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/time/save/time-parse-pattern%20h:mm:ss%20AM/PM] id=pattern-12:58:59 am long-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              12:58:59 PM\n" +
                        "          ROW 5\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"6:01:02 PM Short\"\n" +
                        "            \"h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/time/save/time-parse-pattern%20h:mm%20AM/PM] id=pattern-6:01:02 pm short-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              6:01 PM\n" +
                        "          ROW 6\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"6:01:02 PM Long\"\n" +
                        "            \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/time/save/time-parse-pattern%20h:mm:ss%20AM/PM] id=pattern-6:01:02 pm long-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              6:01:02 PM\n"
        );
    }

    private SpreadsheetPatternDialogComponentContext spreadsheetPatternDialogComponentContext(final String historyToken,
                                                                                              final SpreadsheetFormatterContext formatterContext) {
        return new FakeSpreadsheetPatternDialogComponentContext() {

            @Override
            public HistoryToken historyToken() {
                return HistoryToken.parseString(historyToken);
            }

            @Override
            public SpreadsheetPatternKind patternKind() {
                return this.historyToken().patternKind().get();
            }

            @Override
            public SpreadsheetFormatterContext spreadsheetFormatterContext() {
                return formatterContext;
            }

            @Override
            public void debug(final Object... values) {
                System.out.println(
                        Arrays.toString(values)
                );
            }
        };
    }

    // ClassVisibility..................................................................................................

    @Override
    public Class<SpreadsheetPatternComponentTable> type() {
        return Cast.to(SpreadsheetPatternComponentTable.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
