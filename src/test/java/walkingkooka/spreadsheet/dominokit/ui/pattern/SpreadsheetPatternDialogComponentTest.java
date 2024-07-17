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
import walkingkooka.Either;
import walkingkooka.datetime.DateTimeContext;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.ui.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.format.FakeSpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.tree.expression.ExpressionNumberKind;

import java.math.MathContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public final class SpreadsheetPatternDialogComponentTest implements SpreadsheetDialogComponentLifecycleTesting<SpreadsheetPatternDialogComponent> {

    private final static DateTimeContext DATE_TIME_CONTEXT = DateTimeContexts.locale(
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
            return DATE_TIME_CONTEXT.locale();
        }

        // cell / DateTimeContext

        @Override
        public List<String> ampms() {
            return DATE_TIME_CONTEXT.ampms();
        }

        @Override
        public String ampm(int hourOfDay) {
            return DATE_TIME_CONTEXT.ampm(hourOfDay);
        }

        @Override
        public int defaultYear() {
            return DATE_TIME_CONTEXT.defaultYear();
        }

        @Override
        public List<String> monthNames() {
            return DATE_TIME_CONTEXT.monthNames();
        }

        @Override
        public String monthName(int month) {
            return DATE_TIME_CONTEXT.monthName(month);
        }

        @Override
        public List<String> monthNameAbbreviations() {
            return DATE_TIME_CONTEXT.monthNameAbbreviations();
        }

        @Override
        public String monthNameAbbreviation(final int month) {
            return DATE_TIME_CONTEXT.monthNameAbbreviation(month);
        }

        @Override
        public LocalDateTime now() {
            return DATE_TIME_CONTEXT.now();
        }

        @Override
        public int twoDigitYear() {
            return DATE_TIME_CONTEXT.twoDigitYear();
        }

        @Override
        public List<String> weekDayNames() {
            return DATE_TIME_CONTEXT.weekDayNames();
        }

        @Override
        public String weekDayName(final int day) {
            return DATE_TIME_CONTEXT.weekDayName(day);
        }

        @Override
        public List<String> weekDayNameAbbreviations() {
            return DATE_TIME_CONTEXT.weekDayNameAbbreviations();
        }

        @Override
        public String weekDayNameAbbreviation(final int day) {
            return DATE_TIME_CONTEXT.weekDayNameAbbreviation(day);
        }
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
            return DATE_TIME_CONTEXT.locale();
        }

        // cell / DateTimeContext

        @Override
        public List<String> ampms() {
            return DATE_TIME_CONTEXT.ampms();
        }

        @Override
        public String ampm(int hourOfDay) {
            return DATE_TIME_CONTEXT.ampm(hourOfDay);
        }

        @Override
        public int defaultYear() {
            return DATE_TIME_CONTEXT.defaultYear();
        }

        @Override
        public List<String> monthNames() {
            return DATE_TIME_CONTEXT.monthNames();
        }

        @Override
        public String monthName(int month) {
            return DATE_TIME_CONTEXT.monthName(month);
        }

        @Override
        public List<String> monthNameAbbreviations() {
            return DATE_TIME_CONTEXT.monthNameAbbreviations();
        }

        @Override
        public String monthNameAbbreviation(final int month) {
            return DATE_TIME_CONTEXT.monthNameAbbreviation(month);
        }

        @Override
        public LocalDateTime now() {
            return DATE_TIME_CONTEXT.now();
        }

        @Override
        public int twoDigitYear() {
            return DATE_TIME_CONTEXT.twoDigitYear();
        }

        @Override
        public List<String> weekDayNames() {
            return DATE_TIME_CONTEXT.weekDayNames();
        }

        @Override
        public String weekDayName(final int day) {
            return DATE_TIME_CONTEXT.weekDayName(day);
        }

        @Override
        public List<String> weekDayNameAbbreviations() {
            return DATE_TIME_CONTEXT.weekDayNameAbbreviations();
        }

        @Override
        public String weekDayNameAbbreviation(final int day) {
            return DATE_TIME_CONTEXT.weekDayNameAbbreviation(day);
        }
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
            return this.context.positiveSign();
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
            return DATE_TIME_CONTEXT.locale();
        }

        // cell / DateTimeContext

        @Override
        public List<String> ampms() {
            return DATE_TIME_CONTEXT.ampms();
        }

        @Override
        public String ampm(int hourOfDay) {
            return DATE_TIME_CONTEXT.ampm(hourOfDay);
        }

        @Override
        public LocalDateTime now() {
            return DATE_TIME_CONTEXT.now();
        }
    };

    // cell / date......................................................................................................

    @Test
    public void testCellFormatPatternDate() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/cell/A1/formatter/date")
        );

        this.onHistoryTokenChangeAndSetPatternTextAndCheck(
                SpreadsheetPatternDialogComponent.format(
                        this.spreadsheetPatternDialogComponentContext(
                                context,
                                DATE_FORMATTER_CONTEXT
                        )
                ),
                "dd/mm/yyyy",
                context,
                "SpreadsheetPatternDialogComponentFormat\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Date formatter \n" +
                        "    id=pattern includeClose=true CLOSED\n" +
                        "      SpreadsheetPatternComponentTabs\n" +
                        "        SpreadsheetTabsComponent\n" +
                        "          TAB 0\n" +
                        "            \"Date\" DISABLED id=pattern-date-format\n" +
                        "          TAB 1\n" +
                        "            \"Date Time\" DISABLED id=pattern-date-time-format\n" +
                        "          TAB 2\n" +
                        "            \"Number\" DISABLED id=pattern-number-format\n" +
                        "          TAB 3\n" +
                        "            \"Text\" DISABLED id=pattern-text-format\n" +
                        "          TAB 4\n" +
                        "            \"Time\" DISABLED id=pattern-time-format\n" +
                        "      SpreadsheetPatternComponentTable\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              ROW(S)\n" +
                        "                ROW 0\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Edit Pattern\"\n" +
                        "                  \"dd/mm/yyyy\" [#/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20dd/mm/yyyy] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/1999\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Short\"\n" +
                        "                  \"d/m/yy\" [#/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20d/m/yy] id=pattern-today short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Medium\"\n" +
                        "                  \"d mmm yyyy\" [#/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20d%20mmm%20yyyy] id=pattern-today medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Long\"\n" +
                        "                  \"d mmmm yyyy\" [#/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20d%20mmmm%20yyyy] id=pattern-today long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Full\"\n" +
                        "                  \"dddd, d mmmm yyyy\" [#/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20dddd,%20d%20mmmm%20yyyy] id=pattern-today full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999\n" +
                        "                ROW 5\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Short\"\n" +
                        "                  \"d/m/yy\" [#/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20d/m/yy] id=pattern-31 december 1999 short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99\n" +
                        "                ROW 6\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Medium\"\n" +
                        "                  \"d mmm yyyy\" [#/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20d%20mmm%20yyyy] id=pattern-31 december 1999 medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999\n" +
                        "                ROW 7\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Long\"\n" +
                        "                  \"d mmmm yyyy\" [#/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20d%20mmmm%20yyyy] id=pattern-31 december 1999 long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999\n" +
                        "                ROW 8\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Full\"\n" +
                        "                  \"dddd, d mmmm yyyy\" [#/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20dddd,%20d%20mmmm%20yyyy] id=pattern-31 december 1999 full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"dd\" [#/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20/mm/yyyy] id=pattern-remove-0-Link\n" +
                        "                      \"d\" [/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20d/mm/yyyy] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                      \"dd\" [/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20dd/mm/yyyy] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                      \"ddd\" [/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20ddd/mm/yyyy] id=pattern-remove-0-alt-2-MenuItem\n" +
                        "                      \"dddd\" [/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20dddd/mm/yyyy] id=pattern-remove-0-alt-3-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20ddmm/yyyy] id=pattern-remove-1-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20dd//yyyy] id=pattern-remove-2-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20dd/m/yyyy] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20dd/mm/yyyy] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "                      \"mmm\" [/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20dd/mmm/yyyy] id=pattern-remove-2-alt-2-MenuItem\n" +
                        "                      \"mmmm\" [/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20dd/mmmm/yyyy] id=pattern-remove-2-alt-3-MenuItem\n" +
                        "                      \"mmmmm\" [/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20dd/mmmmm/yyyy] id=pattern-remove-2-alt-4-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20dd/mmyyyy] id=pattern-remove-3-Link\n" +
                        "                  \"yyyy\" [#/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20dd/mm/] id=pattern-remove-4-Link\n" +
                        "                      \"yy\" [/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20dd/mm/yy] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                      \"yyyy\" [/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20dd/mm/yyyy] id=pattern-remove-4-alt-1-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [dd/mm/yyyy] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/cell/A1/formatter/date/save/date-format-pattern%20dd/mm/yyyy] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    @Test
    public void testCellParsePatternDate() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/cell/A1/parser/date")
        );

        this.onHistoryTokenChangeAndSetPatternTextAndCheck(
                SpreadsheetPatternDialogComponent.parse(
                        this.spreadsheetPatternDialogComponentContext(
                                context,
                                DATE_FORMATTER_CONTEXT
                        )
                ),
                "dd/mm/yyyy",
                context,
                "SpreadsheetPatternDialogComponentParse\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Date parser \n" +
                        "    id=pattern includeClose=true CLOSED\n" +
                        "      SpreadsheetPatternComponentTabs\n" +
                        "        SpreadsheetTabsComponent\n" +
                        "          TAB 0\n" +
                        "            \"Date\" DISABLED id=pattern-date-parse\n" +
                        "          TAB 1\n" +
                        "            \"Date Time\" DISABLED id=pattern-date-time-parse\n" +
                        "          TAB 2\n" +
                        "            \"Number\" DISABLED id=pattern-number-parse\n" +
                        "          TAB 3\n" +
                        "            \"Time\" DISABLED id=pattern-time-parse\n" +
                        "      SpreadsheetPatternComponentTable\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              ROW(S)\n" +
                        "                ROW 0\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Edit Pattern\"\n" +
                        "                  \"dd/mm/yyyy\" [#/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20dd/mm/yyyy] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/1999\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Short\"\n" +
                        "                  \"d/m/yy\" [#/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20d/m/yy] id=pattern-today short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Medium\"\n" +
                        "                  \"d mmm yyyy\" [#/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20d%20mmm%20yyyy] id=pattern-today medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Long\"\n" +
                        "                  \"d mmmm yyyy\" [#/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20d%20mmmm%20yyyy] id=pattern-today long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Full\"\n" +
                        "                  \"dddd, d mmmm yyyy\" [#/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20dddd,%20d%20mmmm%20yyyy] id=pattern-today full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999\n" +
                        "                ROW 5\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Short\"\n" +
                        "                  \"d/m/yy\" [#/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20d/m/yy] id=pattern-31 december 1999 short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99\n" +
                        "                ROW 6\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Medium\"\n" +
                        "                  \"d mmm yyyy\" [#/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20d%20mmm%20yyyy] id=pattern-31 december 1999 medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999\n" +
                        "                ROW 7\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Long\"\n" +
                        "                  \"d mmmm yyyy\" [#/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20d%20mmmm%20yyyy] id=pattern-31 december 1999 long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999\n" +
                        "                ROW 8\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Full\"\n" +
                        "                  \"dddd, d mmmm yyyy\" [#/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20dddd,%20d%20mmmm%20yyyy] id=pattern-31 december 1999 full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"dd\" [#/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20/mm/yyyy] id=pattern-remove-0-Link\n" +
                        "                      \"d\" [/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20d/mm/yyyy] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                      \"dd\" [/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20dd/mm/yyyy] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                      \"ddd\" [/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20ddd/mm/yyyy] id=pattern-remove-0-alt-2-MenuItem\n" +
                        "                      \"dddd\" [/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20dddd/mm/yyyy] id=pattern-remove-0-alt-3-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20ddmm/yyyy] id=pattern-remove-1-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20dd//yyyy] id=pattern-remove-2-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20dd/m/yyyy] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20dd/mm/yyyy] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "                      \"mmm\" [/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20dd/mmm/yyyy] id=pattern-remove-2-alt-2-MenuItem\n" +
                        "                      \"mmmm\" [/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20dd/mmmm/yyyy] id=pattern-remove-2-alt-3-MenuItem\n" +
                        "                      \"mmmmm\" [/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20dd/mmmmm/yyyy] id=pattern-remove-2-alt-4-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20dd/mmyyyy] id=pattern-remove-3-Link\n" +
                        "                  \"yyyy\" [#/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20dd/mm/] id=pattern-remove-4-Link\n" +
                        "                      \"yy\" [/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20dd/mm/yy] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                      \"yyyy\" [/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20dd/mm/yyyy] id=pattern-remove-4-alt-1-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [dd/mm/yyyy] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/cell/A1/parser/date/save/date-parse-pattern%20dd/mm/yyyy] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    // cell / date-time.................................................................................................

    @Test
    public void testCellFormatPatternDateTime() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/cell/A1/formatter/date-time")
        );

        this.onHistoryTokenChangeAndSetPatternTextAndCheck(
                SpreadsheetPatternDialogComponent.format(
                        this.spreadsheetPatternDialogComponentContext(
                                context,
                                DATE_TIME_FORMATTER_CONTEXT
                        )
                ),
                "dd/mm/yyyy hh:mm:ss",
                context,
                "SpreadsheetPatternDialogComponentFormat\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Date Time formatter \n" +
                        "    id=pattern includeClose=true CLOSED\n" +
                        "      SpreadsheetPatternComponentTabs\n" +
                        "        SpreadsheetTabsComponent\n" +
                        "          TAB 0\n" +
                        "            \"Date\" DISABLED id=pattern-date-format\n" +
                        "          TAB 1\n" +
                        "            \"Date Time\" DISABLED id=pattern-date-time-format\n" +
                        "          TAB 2\n" +
                        "            \"Number\" DISABLED id=pattern-number-format\n" +
                        "          TAB 3\n" +
                        "            \"Text\" DISABLED id=pattern-text-format\n" +
                        "          TAB 4\n" +
                        "            \"Time\" DISABLED id=pattern-time-format\n" +
                        "      SpreadsheetPatternComponentTable\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              ROW(S)\n" +
                        "                ROW 0\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Edit Pattern\"\n" +
                        "                  \"dd/mm/yyyy hh:mm:ss\" [#/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/1999 12:58:59\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Short\"\n" +
                        "                  \"d/m/yy, h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20d/m/yy,%20h:mm%20AM/PM] id=pattern-today short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99, 12:58 PM\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Medium\"\n" +
                        "                  \"d mmm yyyy, h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20d%20mmm%20yyyy,%20h:mm:ss%20AM/PM] id=pattern-today medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999, 12:58:59 PM\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Long\"\n" +
                        "                  \"d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-today long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999 at 12:58:59 PM\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Full\"\n" +
                        "                  \"dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dddd,%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-today full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999 at 12:58:59 PM\n" +
                        "                ROW 5\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Short\"\n" +
                        "                  \"d/m/yy, h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20d/m/yy,%20h:mm%20AM/PM] id=pattern-31 december 1999 12:58:59 short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99, 12:58 PM\n" +
                        "                ROW 6\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Medium\"\n" +
                        "                  \"d mmm yyyy, h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20d%20mmm%20yyyy,%20h:mm:ss%20AM/PM] id=pattern-31 december 1999 12:58:59 medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999, 12:58:59 PM\n" +
                        "                ROW 7\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Long\"\n" +
                        "                  \"d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-31 december 1999 12:58:59 long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999 at 12:58:59 PM\n" +
                        "                ROW 8\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Full\"\n" +
                        "                  \"dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dddd,%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-31 december 1999 12:58:59 full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999 at 12:58:59 PM\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"dd\" [#/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-Link\n" +
                        "                      \"d\" [/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20d/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                      \"dd\" [/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                      \"ddd\" [/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20ddd/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-2-MenuItem\n" +
                        "                      \"dddd\" [/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dddd/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-3-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20ddmm/yyyy%20hh:mm:ss] id=pattern-remove-1-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd//yyyy%20hh:mm:ss] id=pattern-remove-2-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/m/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "                      \"mmm\" [/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mmm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-2-MenuItem\n" +
                        "                      \"mmmm\" [/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mmmm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-3-MenuItem\n" +
                        "                      \"mmmmm\" [/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mmmmm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-4-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mmyyyy%20hh:mm:ss] id=pattern-remove-3-Link\n" +
                        "                  \"yyyy\" [#/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/%20hh:mm:ss] id=pattern-remove-4-Link\n" +
                        "                      \"yy\" [/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yy%20hh:mm:ss] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                      \"yyyy\" [/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-4-alt-1-MenuItem\n" +
                        "                  \" \" [#/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyyhh:mm:ss] id=pattern-remove-5-Link\n" +
                        "                  \"hh\" [#/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20:mm:ss] id=pattern-remove-6-Link\n" +
                        "                      \"h\" [/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20h:mm:ss] id=pattern-remove-6-alt-0-MenuItem\n" +
                        "                      \"hh\" [/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-6-alt-1-MenuItem\n" +
                        "                  \":\" [#/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hhmm:ss] id=pattern-remove-7-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh::ss] id=pattern-remove-8-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:m:ss] id=pattern-remove-8-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-8-alt-1-MenuItem\n" +
                        "                  \":\" [#/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mmss] id=pattern-remove-9-Link\n" +
                        "                  \"ss\" [#/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:] id=pattern-remove-10-Link\n" +
                        "                      \"s\" [/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:s] id=pattern-remove-10-alt-0-MenuItem\n" +
                        "                      \"ss\" [/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-10-alt-1-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [dd/mm/yyyy hh:mm:ss] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    @Test
    public void testCellParsePatternDateTime() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/cell/A1/parser/date-time")
        );

        this.onHistoryTokenChangeAndSetPatternTextAndCheck(
                SpreadsheetPatternDialogComponent.parse(
                        this.spreadsheetPatternDialogComponentContext(
                                context,
                                DATE_TIME_FORMATTER_CONTEXT
                        )
                ),
                "dd/mm/yyyy hh:mm:ss",
                context,
                "SpreadsheetPatternDialogComponentParse\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Date Time parser \n" +
                        "    id=pattern includeClose=true CLOSED\n" +
                        "      SpreadsheetPatternComponentTabs\n" +
                        "        SpreadsheetTabsComponent\n" +
                        "          TAB 0\n" +
                        "            \"Date\" DISABLED id=pattern-date-parse\n" +
                        "          TAB 1\n" +
                        "            \"Date Time\" DISABLED id=pattern-date-time-parse\n" +
                        "          TAB 2\n" +
                        "            \"Number\" DISABLED id=pattern-number-parse\n" +
                        "          TAB 3\n" +
                        "            \"Time\" DISABLED id=pattern-time-parse\n" +
                        "      SpreadsheetPatternComponentTable\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              ROW(S)\n" +
                        "                ROW 0\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Edit Pattern\"\n" +
                        "                  \"dd/mm/yyyy hh:mm:ss\" [#/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/1999 12:58:59\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Short\"\n" +
                        "                  \"d/m/yy, h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20d/m/yy,%20h:mm%20AM/PM] id=pattern-today short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99, 12:58 PM\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Medium\"\n" +
                        "                  \"d mmm yyyy, h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20d%20mmm%20yyyy,%20h:mm:ss%20AM/PM] id=pattern-today medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999, 12:58:59 PM\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Long\"\n" +
                        "                  \"d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-today long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999 at 12:58:59 PM\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Full\"\n" +
                        "                  \"dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dddd,%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-today full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999 at 12:58:59 PM\n" +
                        "                ROW 5\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Short\"\n" +
                        "                  \"d/m/yy, h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20d/m/yy,%20h:mm%20AM/PM] id=pattern-31 december 1999 12:58:59 short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99, 12:58 PM\n" +
                        "                ROW 6\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Medium\"\n" +
                        "                  \"d mmm yyyy, h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20d%20mmm%20yyyy,%20h:mm:ss%20AM/PM] id=pattern-31 december 1999 12:58:59 medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999, 12:58:59 PM\n" +
                        "                ROW 7\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Long\"\n" +
                        "                  \"d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-31 december 1999 12:58:59 long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999 at 12:58:59 PM\n" +
                        "                ROW 8\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Full\"\n" +
                        "                  \"dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dddd,%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-31 december 1999 12:58:59 full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999 at 12:58:59 PM\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"dd\" [#/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-Link\n" +
                        "                      \"d\" [/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20d/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                      \"dd\" [/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                      \"ddd\" [/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20ddd/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-2-MenuItem\n" +
                        "                      \"dddd\" [/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dddd/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-3-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20ddmm/yyyy%20hh:mm:ss] id=pattern-remove-1-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd//yyyy%20hh:mm:ss] id=pattern-remove-2-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/m/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "                      \"mmm\" [/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mmm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-2-MenuItem\n" +
                        "                      \"mmmm\" [/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mmmm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-3-MenuItem\n" +
                        "                      \"mmmmm\" [/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mmmmm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-4-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mmyyyy%20hh:mm:ss] id=pattern-remove-3-Link\n" +
                        "                  \"yyyy\" [#/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/%20hh:mm:ss] id=pattern-remove-4-Link\n" +
                        "                      \"yy\" [/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yy%20hh:mm:ss] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                      \"yyyy\" [/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-4-alt-1-MenuItem\n" +
                        "                  \" \" [#/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyyhh:mm:ss] id=pattern-remove-5-Link\n" +
                        "                  \"hh\" [#/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20:mm:ss] id=pattern-remove-6-Link\n" +
                        "                      \"h\" [/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20h:mm:ss] id=pattern-remove-6-alt-0-MenuItem\n" +
                        "                      \"hh\" [/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-6-alt-1-MenuItem\n" +
                        "                  \":\" [#/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hhmm:ss] id=pattern-remove-7-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh::ss] id=pattern-remove-8-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:m:ss] id=pattern-remove-8-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-8-alt-1-MenuItem\n" +
                        "                  \":\" [#/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mmss] id=pattern-remove-9-Link\n" +
                        "                  \"ss\" [#/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:] id=pattern-remove-10-Link\n" +
                        "                      \"s\" [/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:s] id=pattern-remove-10-alt-0-MenuItem\n" +
                        "                      \"ss\" [/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-10-alt-1-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [dd/mm/yyyy hh:mm:ss] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    // cell / number....................................................................................................

    @Test
    public void testCellFormatPatternNumber() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/cell/A1/formatter/number")
        );

        this.onHistoryTokenChangeAndSetPatternTextAndCheck(
                SpreadsheetPatternDialogComponent.format(
                        this.spreadsheetPatternDialogComponentContext(
                                context,
                                NUMBER_FORMATTER_CONTEXT
                        )
                ),
                "$#0.00",
                context,
                "SpreadsheetPatternDialogComponentFormat\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Number formatter \n" +
                        "    id=pattern includeClose=true CLOSED\n" +
                        "      SpreadsheetPatternComponentTabs\n" +
                        "        SpreadsheetTabsComponent\n" +
                        "          TAB 0\n" +
                        "            \"Date\" DISABLED id=pattern-date-format\n" +
                        "          TAB 1\n" +
                        "            \"Date Time\" DISABLED id=pattern-date-time-format\n" +
                        "          TAB 2\n" +
                        "            \"Number\" DISABLED id=pattern-number-format\n" +
                        "          TAB 3\n" +
                        "            \"Text\" DISABLED id=pattern-text-format\n" +
                        "          TAB 4\n" +
                        "            \"Time\" DISABLED id=pattern-time-format\n" +
                        "      SpreadsheetPatternComponentTable\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              ROW(S)\n" +
                        "                ROW 0\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Edit Pattern\"\n" +
                        "                  \"$#0.00\" [#/1/Spreadsheet123/cell/A1/formatter/number/save/number-format-pattern%20$%230.00] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $1234.56\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $-9876.54\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $0.00\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"General\"\n" +
                        "                  \"General\" [#/1/Spreadsheet123/cell/A1/formatter/number/save/number-format-pattern%20General] id=pattern-general-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    1234.56\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -9876.54\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Number\"\n" +
                        "                  \"#,##0.###\" [#/1/Spreadsheet123/cell/A1/formatter/number/save/number-format-pattern%20%23,%23%230.%23%23%23] id=pattern-number-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    1,234.56\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -9,876.54\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0.\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Integer\"\n" +
                        "                  \"#,##0\" [#/1/Spreadsheet123/cell/A1/formatter/number/save/number-format-pattern%20%23,%23%230] id=pattern-integer-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    1,235\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -9,877\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Percent\"\n" +
                        "                  \"#,##0%\" [#/1/Spreadsheet123/cell/A1/formatter/number/save/number-format-pattern%20%23,%23%230%25] id=pattern-percent-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    123,456%\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -987,654%\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0%\n" +
                        "                ROW 5\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Currency\"\n" +
                        "                  \"$#,##0.00\" [#/1/Spreadsheet123/cell/A1/formatter/number/save/number-format-pattern%20$%23,%23%230.00] id=pattern-currency-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $1,234.56\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $-9,876.54\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $0.00\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"$\" [#/1/Spreadsheet123/cell/A1/formatter/number/save/number-format-pattern%20%230.00] id=pattern-remove-0-Link\n" +
                        "                      \"$\" [/1/Spreadsheet123/cell/A1/formatter/number/save/number-format-pattern%20$%230.00] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                  \"#\" [#/1/Spreadsheet123/cell/A1/formatter/number/save/number-format-pattern%20$0.00] id=pattern-remove-1-Link\n" +
                        "                      \"#\" [/1/Spreadsheet123/cell/A1/formatter/number/save/number-format-pattern%20$%230.00] id=pattern-remove-1-alt-0-MenuItem\n" +
                        "                  \"0\" [#/1/Spreadsheet123/cell/A1/formatter/number/save/number-format-pattern%20$%23.00] id=pattern-remove-2-Link\n" +
                        "                      \"0\" [/1/Spreadsheet123/cell/A1/formatter/number/save/number-format-pattern%20$%230.00] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                  \".\" [#/1/Spreadsheet123/cell/A1/formatter/number/save/number-format-pattern%20$%23000] id=pattern-remove-3-Link\n" +
                        "                      \".\" [/1/Spreadsheet123/cell/A1/formatter/number/save/number-format-pattern%20$%230.00] id=pattern-remove-3-alt-0-MenuItem\n" +
                        "                  \"0\" [#/1/Spreadsheet123/cell/A1/formatter/number/save/number-format-pattern%20$%230.0] id=pattern-remove-4-Link\n" +
                        "                      \"0\" [/1/Spreadsheet123/cell/A1/formatter/number/save/number-format-pattern%20$%230.00] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                  \"0\" [#/1/Spreadsheet123/cell/A1/formatter/number/save/number-format-pattern%20$%230.0] id=pattern-remove-5-Link\n" +
                        "                      \"0\" [/1/Spreadsheet123/cell/A1/formatter/number/save/number-format-pattern%20$%230.00] id=pattern-remove-5-alt-0-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [$#0.00] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/cell/A1/formatter/number/save/number-format-pattern%20$%230.00] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    @Test
    public void testCellParsePatternNumber() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/cell/A1/parser/number")
        );

        this.onHistoryTokenChangeAndSetPatternTextAndCheck(
                SpreadsheetPatternDialogComponent.parse(
                        this.spreadsheetPatternDialogComponentContext(
                                context,
                                NUMBER_FORMATTER_CONTEXT
                        )
                ),
                "$#0.00",
                context,
                "SpreadsheetPatternDialogComponentParse\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Number parser \n" +
                        "    id=pattern includeClose=true CLOSED\n" +
                        "      SpreadsheetPatternComponentTabs\n" +
                        "        SpreadsheetTabsComponent\n" +
                        "          TAB 0\n" +
                        "            \"Date\" DISABLED id=pattern-date-parse\n" +
                        "          TAB 1\n" +
                        "            \"Date Time\" DISABLED id=pattern-date-time-parse\n" +
                        "          TAB 2\n" +
                        "            \"Number\" DISABLED id=pattern-number-parse\n" +
                        "          TAB 3\n" +
                        "            \"Time\" DISABLED id=pattern-time-parse\n" +
                        "      SpreadsheetPatternComponentTable\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              ROW(S)\n" +
                        "                ROW 0\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Edit Pattern\"\n" +
                        "                  \"$#0.00\" [#/1/Spreadsheet123/cell/A1/parser/number/save/number-parse-pattern%20$%230.00] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $1234.56\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $-9876.54\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $0.00\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Number\"\n" +
                        "                  \"#,##0.###\" [#/1/Spreadsheet123/cell/A1/parser/number/save/number-parse-pattern%20%23,%23%230.%23%23%23] id=pattern-number-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    1,234.56\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -9,876.54\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0.\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Integer\"\n" +
                        "                  \"#,##0\" [#/1/Spreadsheet123/cell/A1/parser/number/save/number-parse-pattern%20%23,%23%230] id=pattern-integer-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    1,235\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -9,877\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Percent\"\n" +
                        "                  \"#,##0%\" [#/1/Spreadsheet123/cell/A1/parser/number/save/number-parse-pattern%20%23,%23%230%25] id=pattern-percent-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    123,456%\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -987,654%\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0%\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Currency\"\n" +
                        "                  \"$#,##0.00\" [#/1/Spreadsheet123/cell/A1/parser/number/save/number-parse-pattern%20$%23,%23%230.00] id=pattern-currency-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $1,234.56\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $-9,876.54\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $0.00\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"$\" [#/1/Spreadsheet123/cell/A1/parser/number/save/number-parse-pattern%20%230.00] id=pattern-remove-0-Link\n" +
                        "                      \"$\" [/1/Spreadsheet123/cell/A1/parser/number/save/number-parse-pattern%20$%230.00] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                  \"#\" [#/1/Spreadsheet123/cell/A1/parser/number/save/number-parse-pattern%20$0.00] id=pattern-remove-1-Link\n" +
                        "                      \"#\" [/1/Spreadsheet123/cell/A1/parser/number/save/number-parse-pattern%20$%230.00] id=pattern-remove-1-alt-0-MenuItem\n" +
                        "                  \"0\" [#/1/Spreadsheet123/cell/A1/parser/number/save/number-parse-pattern%20$%23.00] id=pattern-remove-2-Link\n" +
                        "                      \"0\" [/1/Spreadsheet123/cell/A1/parser/number/save/number-parse-pattern%20$%230.00] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                  \".\" [#/1/Spreadsheet123/cell/A1/parser/number/save/number-parse-pattern%20$%23000] id=pattern-remove-3-Link\n" +
                        "                      \".\" [/1/Spreadsheet123/cell/A1/parser/number/save/number-parse-pattern%20$%230.00] id=pattern-remove-3-alt-0-MenuItem\n" +
                        "                  \"0\" [#/1/Spreadsheet123/cell/A1/parser/number/save/number-parse-pattern%20$%230.0] id=pattern-remove-4-Link\n" +
                        "                      \"0\" [/1/Spreadsheet123/cell/A1/parser/number/save/number-parse-pattern%20$%230.00] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                  \"0\" [#/1/Spreadsheet123/cell/A1/parser/number/save/number-parse-pattern%20$%230.0] id=pattern-remove-5-Link\n" +
                        "                      \"0\" [/1/Spreadsheet123/cell/A1/parser/number/save/number-parse-pattern%20$%230.00] id=pattern-remove-5-alt-0-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [$#0.00] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/cell/A1/parser/number/save/number-parse-pattern%20$%230.00] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    // cell / text......................................................................................................

    @Test
    public void testCellFormatPatternText() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/cell/A1/formatter/text")
        );

        this.onHistoryTokenChangeAndSetPatternTextAndCheck(
                SpreadsheetPatternDialogComponent.format(
                        this.spreadsheetPatternDialogComponentContext(
                                context,
                                TEXT_FORMATTER_CONTEXT
                        )
                ),
                "@ \"Hello\"",
                context,
                "SpreadsheetPatternDialogComponentFormat\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Text formatter \n" +
                        "    id=pattern includeClose=true CLOSED\n" +
                        "      SpreadsheetPatternComponentTabs\n" +
                        "        SpreadsheetTabsComponent\n" +
                        "          TAB 0\n" +
                        "            \"Date\" DISABLED id=pattern-date-format\n" +
                        "          TAB 1\n" +
                        "            \"Date Time\" DISABLED id=pattern-date-time-format\n" +
                        "          TAB 2\n" +
                        "            \"Number\" DISABLED id=pattern-number-format\n" +
                        "          TAB 3\n" +
                        "            \"Text\" DISABLED id=pattern-text-format\n" +
                        "          TAB 4\n" +
                        "            \"Time\" DISABLED id=pattern-time-format\n" +
                        "      SpreadsheetPatternComponentTable\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              ROW(S)\n" +
                        "                ROW 0\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Edit Pattern\"\n" +
                        "                  \"@ \"Hello\"\" [#/1/Spreadsheet123/cell/A1/formatter/text/save/text-format-pattern%20@%20%22Hello%22] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Abc123 Hello\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Default text format\"\n" +
                        "                  \"@\" [#/1/Spreadsheet123/cell/A1/formatter/text/save/text-format-pattern%20@] id=pattern-default text format-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Abc123\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"@\" [#/1/Spreadsheet123/cell/A1/formatter/text/save/text-format-pattern%20%20%22Hello%22] id=pattern-remove-0-Link\n" +
                        "                      \"@\" [/1/Spreadsheet123/cell/A1/formatter/text/save/text-format-pattern%20@%20%22Hello%22] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                  \" \" [#/1/Spreadsheet123/cell/A1/formatter/text/save/text-format-pattern%20@%22Hello%22] id=pattern-remove-1-Link\n" +
                        "                  \"\"Hello\"\" [#/1/Spreadsheet123/cell/A1/formatter/text/save/text-format-pattern%20@%20] id=pattern-remove-2-Link\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [@ \"Hello\"] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/cell/A1/formatter/text/save/text-format-pattern%20@%20%22Hello%22] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    // cell / time......................................................................................................

    @Test
    public void testCellFormatPatternTime() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/cell/A1/formatter/time")
        );

        this.onHistoryTokenChangeAndSetPatternTextAndCheck(
                SpreadsheetPatternDialogComponent.format(
                        this.spreadsheetPatternDialogComponentContext(
                                context,
                                TIME_FORMATTER_CONTEXT
                        )
                ),
                "hh:mm",
                context,
                "SpreadsheetPatternDialogComponentFormat\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Time formatter \n" +
                        "    id=pattern includeClose=true CLOSED\n" +
                        "      SpreadsheetPatternComponentTabs\n" +
                        "        SpreadsheetTabsComponent\n" +
                        "          TAB 0\n" +
                        "            \"Date\" DISABLED id=pattern-date-format\n" +
                        "          TAB 1\n" +
                        "            \"Date Time\" DISABLED id=pattern-date-time-format\n" +
                        "          TAB 2\n" +
                        "            \"Number\" DISABLED id=pattern-number-format\n" +
                        "          TAB 3\n" +
                        "            \"Text\" DISABLED id=pattern-text-format\n" +
                        "          TAB 4\n" +
                        "            \"Time\" DISABLED id=pattern-time-format\n" +
                        "      SpreadsheetPatternComponentTable\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              ROW(S)\n" +
                        "                ROW 0\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Edit Pattern\"\n" +
                        "                  \"hh:mm\" [#/1/Spreadsheet123/cell/A1/formatter/time/save/time-format-pattern%20hh:mm] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Now Short\"\n" +
                        "                  \"h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/formatter/time/save/time-format-pattern%20h:mm%20AM/PM] id=pattern-now short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58 PM\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Now Long\"\n" +
                        "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/formatter/time/save/time-format-pattern%20h:mm:ss%20AM/PM] id=pattern-now long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58:59 PM\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"12:58:59 AM Short\"\n" +
                        "                  \"h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/formatter/time/save/time-format-pattern%20h:mm%20AM/PM] id=pattern-12:58:59 am short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58 PM\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"12:58:59 AM Long\"\n" +
                        "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/formatter/time/save/time-format-pattern%20h:mm:ss%20AM/PM] id=pattern-12:58:59 am long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58:59 PM\n" +
                        "                ROW 5\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"6:01:02 PM Short\"\n" +
                        "                  \"h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/formatter/time/save/time-format-pattern%20h:mm%20AM/PM] id=pattern-6:01:02 pm short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    6:01 PM\n" +
                        "                ROW 6\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"6:01:02 PM Long\"\n" +
                        "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/formatter/time/save/time-format-pattern%20h:mm:ss%20AM/PM] id=pattern-6:01:02 pm long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    6:01:02 PM\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"hh\" [#/1/Spreadsheet123/cell/A1/formatter/time/save/time-format-pattern%20:mm] id=pattern-remove-0-Link\n" +
                        "                      \"h\" [/1/Spreadsheet123/cell/A1/formatter/time/save/time-format-pattern%20h:mm] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                      \"hh\" [/1/Spreadsheet123/cell/A1/formatter/time/save/time-format-pattern%20hh:mm] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                  \":\" [#/1/Spreadsheet123/cell/A1/formatter/time/save/time-format-pattern%20hhmm] id=pattern-remove-1-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/cell/A1/formatter/time/save/time-format-pattern%20hh:] id=pattern-remove-2-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/cell/A1/formatter/time/save/time-format-pattern%20hh:m] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/cell/A1/formatter/time/save/time-format-pattern%20hh:mm] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [hh:mm] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/cell/A1/formatter/time/save/time-format-pattern%20hh:mm] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    @Test
    public void testCellParsePatternTime() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/cell/A1/parser/time")
        );

        this.onHistoryTokenChangeAndSetPatternTextAndCheck(
                SpreadsheetPatternDialogComponent.parse(
                        this.spreadsheetPatternDialogComponentContext(
                                context,
                                TIME_FORMATTER_CONTEXT
                        )
                ),
                "hh:mm",
                context,
                "SpreadsheetPatternDialogComponentParse\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Time parser \n" +
                        "    id=pattern includeClose=true CLOSED\n" +
                        "      SpreadsheetPatternComponentTabs\n" +
                        "        SpreadsheetTabsComponent\n" +
                        "          TAB 0\n" +
                        "            \"Date\" DISABLED id=pattern-date-parse\n" +
                        "          TAB 1\n" +
                        "            \"Date Time\" DISABLED id=pattern-date-time-parse\n" +
                        "          TAB 2\n" +
                        "            \"Number\" DISABLED id=pattern-number-parse\n" +
                        "          TAB 3\n" +
                        "            \"Time\" DISABLED id=pattern-time-parse\n" +
                        "      SpreadsheetPatternComponentTable\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              ROW(S)\n" +
                        "                ROW 0\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Edit Pattern\"\n" +
                        "                  \"hh:mm\" [#/1/Spreadsheet123/cell/A1/parser/time/save/time-parse-pattern%20hh:mm] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Now Short\"\n" +
                        "                  \"h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/time/save/time-parse-pattern%20h:mm%20AM/PM] id=pattern-now short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58 PM\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Now Long\"\n" +
                        "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/time/save/time-parse-pattern%20h:mm:ss%20AM/PM] id=pattern-now long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58:59 PM\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"12:58:59 AM Short\"\n" +
                        "                  \"h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/time/save/time-parse-pattern%20h:mm%20AM/PM] id=pattern-12:58:59 am short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58 PM\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"12:58:59 AM Long\"\n" +
                        "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/time/save/time-parse-pattern%20h:mm:ss%20AM/PM] id=pattern-12:58:59 am long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58:59 PM\n" +
                        "                ROW 5\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"6:01:02 PM Short\"\n" +
                        "                  \"h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/time/save/time-parse-pattern%20h:mm%20AM/PM] id=pattern-6:01:02 pm short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    6:01 PM\n" +
                        "                ROW 6\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"6:01:02 PM Long\"\n" +
                        "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/time/save/time-parse-pattern%20h:mm:ss%20AM/PM] id=pattern-6:01:02 pm long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    6:01:02 PM\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"hh\" [#/1/Spreadsheet123/cell/A1/parser/time/save/time-parse-pattern%20:mm] id=pattern-remove-0-Link\n" +
                        "                      \"h\" [/1/Spreadsheet123/cell/A1/parser/time/save/time-parse-pattern%20h:mm] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                      \"hh\" [/1/Spreadsheet123/cell/A1/parser/time/save/time-parse-pattern%20hh:mm] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                  \":\" [#/1/Spreadsheet123/cell/A1/parser/time/save/time-parse-pattern%20hhmm] id=pattern-remove-1-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/cell/A1/parser/time/save/time-parse-pattern%20hh:] id=pattern-remove-2-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/cell/A1/parser/time/save/time-parse-pattern%20hh:m] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/cell/A1/parser/time/save/time-parse-pattern%20hh:mm] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [hh:mm] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/cell/A1/parser/time/save/time-parse-pattern%20hh:mm] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    // metadata / date..................................................................................................

    @Test
    public void testMetadataFormatterDate() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/metadata/date-formatter")
        );

        this.onHistoryTokenChangeAndSetPatternTextAndCheck(
                SpreadsheetPatternDialogComponent.format(
                        this.spreadsheetPatternDialogComponentContext(
                                context,
                                DATE_FORMATTER_CONTEXT
                        )
                ),
                "dd/mm/yyyy",
                context,
                "SpreadsheetPatternDialogComponentFormat\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Date formatter \n" +
                        "    id=pattern includeClose=true CLOSED\n" +
                        "      SpreadsheetPatternComponentTabs\n" +
                        "        SpreadsheetTabsComponent\n" +
                        "          TAB 0\n" +
                        "            \"Date\" DISABLED id=pattern-date-format\n" +
                        "          TAB 1\n" +
                        "            \"Date Time\" DISABLED id=pattern-date-time-format\n" +
                        "          TAB 2\n" +
                        "            \"Number\" DISABLED id=pattern-number-format\n" +
                        "          TAB 3\n" +
                        "            \"Text\" DISABLED id=pattern-text-format\n" +
                        "          TAB 4\n" +
                        "            \"Time\" DISABLED id=pattern-time-format\n" +
                        "      SpreadsheetPatternComponentTable\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              ROW(S)\n" +
                        "                ROW 0\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Edit Pattern\"\n" +
                        "                  \"dd/mm/yyyy\" [#/1/Spreadsheet123/metadata/date-formatter/save/date-format-pattern%20dd/mm/yyyy] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/1999\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Short\"\n" +
                        "                  \"d/m/yy\" [#/1/Spreadsheet123/metadata/date-formatter/save/date-format-pattern%20d/m/yy] id=pattern-today short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Medium\"\n" +
                        "                  \"d mmm yyyy\" [#/1/Spreadsheet123/metadata/date-formatter/save/date-format-pattern%20d%20mmm%20yyyy] id=pattern-today medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Long\"\n" +
                        "                  \"d mmmm yyyy\" [#/1/Spreadsheet123/metadata/date-formatter/save/date-format-pattern%20d%20mmmm%20yyyy] id=pattern-today long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Full\"\n" +
                        "                  \"dddd, d mmmm yyyy\" [#/1/Spreadsheet123/metadata/date-formatter/save/date-format-pattern%20dddd,%20d%20mmmm%20yyyy] id=pattern-today full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999\n" +
                        "                ROW 5\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Short\"\n" +
                        "                  \"d/m/yy\" [#/1/Spreadsheet123/metadata/date-formatter/save/date-format-pattern%20d/m/yy] id=pattern-31 december 1999 short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99\n" +
                        "                ROW 6\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Medium\"\n" +
                        "                  \"d mmm yyyy\" [#/1/Spreadsheet123/metadata/date-formatter/save/date-format-pattern%20d%20mmm%20yyyy] id=pattern-31 december 1999 medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999\n" +
                        "                ROW 7\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Long\"\n" +
                        "                  \"d mmmm yyyy\" [#/1/Spreadsheet123/metadata/date-formatter/save/date-format-pattern%20d%20mmmm%20yyyy] id=pattern-31 december 1999 long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999\n" +
                        "                ROW 8\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Full\"\n" +
                        "                  \"dddd, d mmmm yyyy\" [#/1/Spreadsheet123/metadata/date-formatter/save/date-format-pattern%20dddd,%20d%20mmmm%20yyyy] id=pattern-31 december 1999 full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"dd\" [#/1/Spreadsheet123/metadata/date-formatter/save/date-format-pattern%20/mm/yyyy] id=pattern-remove-0-Link\n" +
                        "                      \"d\" [/1/Spreadsheet123/metadata/date-formatter/save/date-format-pattern%20d/mm/yyyy] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                      \"dd\" [/1/Spreadsheet123/metadata/date-formatter/save/date-format-pattern%20dd/mm/yyyy] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                      \"ddd\" [/1/Spreadsheet123/metadata/date-formatter/save/date-format-pattern%20ddd/mm/yyyy] id=pattern-remove-0-alt-2-MenuItem\n" +
                        "                      \"dddd\" [/1/Spreadsheet123/metadata/date-formatter/save/date-format-pattern%20dddd/mm/yyyy] id=pattern-remove-0-alt-3-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/metadata/date-formatter/save/date-format-pattern%20ddmm/yyyy] id=pattern-remove-1-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/metadata/date-formatter/save/date-format-pattern%20dd//yyyy] id=pattern-remove-2-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/metadata/date-formatter/save/date-format-pattern%20dd/m/yyyy] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/metadata/date-formatter/save/date-format-pattern%20dd/mm/yyyy] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "                      \"mmm\" [/1/Spreadsheet123/metadata/date-formatter/save/date-format-pattern%20dd/mmm/yyyy] id=pattern-remove-2-alt-2-MenuItem\n" +
                        "                      \"mmmm\" [/1/Spreadsheet123/metadata/date-formatter/save/date-format-pattern%20dd/mmmm/yyyy] id=pattern-remove-2-alt-3-MenuItem\n" +
                        "                      \"mmmmm\" [/1/Spreadsheet123/metadata/date-formatter/save/date-format-pattern%20dd/mmmmm/yyyy] id=pattern-remove-2-alt-4-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/metadata/date-formatter/save/date-format-pattern%20dd/mmyyyy] id=pattern-remove-3-Link\n" +
                        "                  \"yyyy\" [#/1/Spreadsheet123/metadata/date-formatter/save/date-format-pattern%20dd/mm/] id=pattern-remove-4-Link\n" +
                        "                      \"yy\" [/1/Spreadsheet123/metadata/date-formatter/save/date-format-pattern%20dd/mm/yy] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                      \"yyyy\" [/1/Spreadsheet123/metadata/date-formatter/save/date-format-pattern%20dd/mm/yyyy] id=pattern-remove-4-alt-1-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [dd/mm/yyyy] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/metadata/date-formatter/save/date-format-pattern%20dd/mm/yyyy] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    @Test
    public void testMetadataParserDate() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/metadata/date-parser")
        );

        this.onHistoryTokenChangeAndSetPatternTextAndCheck(
                SpreadsheetPatternDialogComponent.parse(
                        this.spreadsheetPatternDialogComponentContext(
                                context,
                                DATE_FORMATTER_CONTEXT
                        )
                ),
                "dd/mm/yyyy",
                context,
                "SpreadsheetPatternDialogComponentParse\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Date parser \n" +
                        "    id=pattern includeClose=true CLOSED\n" +
                        "      SpreadsheetPatternComponentTabs\n" +
                        "        SpreadsheetTabsComponent\n" +
                        "          TAB 0\n" +
                        "            \"Date\" DISABLED id=pattern-date-parse\n" +
                        "          TAB 1\n" +
                        "            \"Date Time\" DISABLED id=pattern-date-time-parse\n" +
                        "          TAB 2\n" +
                        "            \"Number\" DISABLED id=pattern-number-parse\n" +
                        "          TAB 3\n" +
                        "            \"Time\" DISABLED id=pattern-time-parse\n" +
                        "      SpreadsheetPatternComponentTable\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              ROW(S)\n" +
                        "                ROW 0\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Edit Pattern\"\n" +
                        "                  \"dd/mm/yyyy\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd/mm/yyyy] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/1999\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Short\"\n" +
                        "                  \"d/m/yy\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20d/m/yy] id=pattern-today short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Medium\"\n" +
                        "                  \"d mmm yyyy\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20d%20mmm%20yyyy] id=pattern-today medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Long\"\n" +
                        "                  \"d mmmm yyyy\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20d%20mmmm%20yyyy] id=pattern-today long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Full\"\n" +
                        "                  \"dddd, d mmmm yyyy\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dddd,%20d%20mmmm%20yyyy] id=pattern-today full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999\n" +
                        "                ROW 5\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Short\"\n" +
                        "                  \"d/m/yy\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20d/m/yy] id=pattern-31 december 1999 short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99\n" +
                        "                ROW 6\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Medium\"\n" +
                        "                  \"d mmm yyyy\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20d%20mmm%20yyyy] id=pattern-31 december 1999 medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999\n" +
                        "                ROW 7\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Long\"\n" +
                        "                  \"d mmmm yyyy\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20d%20mmmm%20yyyy] id=pattern-31 december 1999 long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999\n" +
                        "                ROW 8\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Full\"\n" +
                        "                  \"dddd, d mmmm yyyy\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dddd,%20d%20mmmm%20yyyy] id=pattern-31 december 1999 full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"dd\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20/mm/yyyy] id=pattern-remove-0-Link\n" +
                        "                      \"d\" [/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20d/mm/yyyy] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                      \"dd\" [/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd/mm/yyyy] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                      \"ddd\" [/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20ddd/mm/yyyy] id=pattern-remove-0-alt-2-MenuItem\n" +
                        "                      \"dddd\" [/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dddd/mm/yyyy] id=pattern-remove-0-alt-3-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20ddmm/yyyy] id=pattern-remove-1-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd//yyyy] id=pattern-remove-2-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd/m/yyyy] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd/mm/yyyy] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "                      \"mmm\" [/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd/mmm/yyyy] id=pattern-remove-2-alt-2-MenuItem\n" +
                        "                      \"mmmm\" [/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd/mmmm/yyyy] id=pattern-remove-2-alt-3-MenuItem\n" +
                        "                      \"mmmmm\" [/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd/mmmmm/yyyy] id=pattern-remove-2-alt-4-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd/mmyyyy] id=pattern-remove-3-Link\n" +
                        "                  \"yyyy\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd/mm/] id=pattern-remove-4-Link\n" +
                        "                      \"yy\" [/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd/mm/yy] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                      \"yyyy\" [/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd/mm/yyyy] id=pattern-remove-4-alt-1-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [dd/mm/yyyy] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd/mm/yyyy] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    // metadata / date-time.............................................................................................

    @Test
    public void testMetadataFormatterDateTime() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/metadata/date-time-formatter")
        );

        this.onHistoryTokenChangeAndSetPatternTextAndCheck(
                SpreadsheetPatternDialogComponent.format(
                        this.spreadsheetPatternDialogComponentContext(
                                context,
                                DATE_TIME_FORMATTER_CONTEXT
                        )
                ),
                "dd/mm/yyyy hh:mm:ss",
                context,
                "SpreadsheetPatternDialogComponentFormat\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Date Time formatter \n" +
                        "    id=pattern includeClose=true CLOSED\n" +
                        "      SpreadsheetPatternComponentTabs\n" +
                        "        SpreadsheetTabsComponent\n" +
                        "          TAB 0\n" +
                        "            \"Date\" DISABLED id=pattern-date-format\n" +
                        "          TAB 1\n" +
                        "            \"Date Time\" DISABLED id=pattern-date-time-format\n" +
                        "          TAB 2\n" +
                        "            \"Number\" DISABLED id=pattern-number-format\n" +
                        "          TAB 3\n" +
                        "            \"Text\" DISABLED id=pattern-text-format\n" +
                        "          TAB 4\n" +
                        "            \"Time\" DISABLED id=pattern-time-format\n" +
                        "      SpreadsheetPatternComponentTable\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              ROW(S)\n" +
                        "                ROW 0\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Edit Pattern\"\n" +
                        "                  \"dd/mm/yyyy hh:mm:ss\" [#/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/1999 12:58:59\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Short\"\n" +
                        "                  \"d/m/yy, h:mm AM/PM\" [#/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20d/m/yy,%20h:mm%20AM/PM] id=pattern-today short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99, 12:58 PM\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Medium\"\n" +
                        "                  \"d mmm yyyy, h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20d%20mmm%20yyyy,%20h:mm:ss%20AM/PM] id=pattern-today medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999, 12:58:59 PM\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Long\"\n" +
                        "                  \"d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-today long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999 at 12:58:59 PM\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Full\"\n" +
                        "                  \"dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20dddd,%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-today full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999 at 12:58:59 PM\n" +
                        "                ROW 5\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Short\"\n" +
                        "                  \"d/m/yy, h:mm AM/PM\" [#/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20d/m/yy,%20h:mm%20AM/PM] id=pattern-31 december 1999 12:58:59 short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99, 12:58 PM\n" +
                        "                ROW 6\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Medium\"\n" +
                        "                  \"d mmm yyyy, h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20d%20mmm%20yyyy,%20h:mm:ss%20AM/PM] id=pattern-31 december 1999 12:58:59 medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999, 12:58:59 PM\n" +
                        "                ROW 7\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Long\"\n" +
                        "                  \"d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-31 december 1999 12:58:59 long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999 at 12:58:59 PM\n" +
                        "                ROW 8\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Full\"\n" +
                        "                  \"dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20dddd,%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-31 december 1999 12:58:59 full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999 at 12:58:59 PM\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"dd\" [#/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-Link\n" +
                        "                      \"d\" [/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20d/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                      \"dd\" [/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                      \"ddd\" [/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20ddd/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-2-MenuItem\n" +
                        "                      \"dddd\" [/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20dddd/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-3-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20ddmm/yyyy%20hh:mm:ss] id=pattern-remove-1-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20dd//yyyy%20hh:mm:ss] id=pattern-remove-2-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20dd/m/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "                      \"mmm\" [/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mmm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-2-MenuItem\n" +
                        "                      \"mmmm\" [/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mmmm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-3-MenuItem\n" +
                        "                      \"mmmmm\" [/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mmmmm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-4-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mmyyyy%20hh:mm:ss] id=pattern-remove-3-Link\n" +
                        "                  \"yyyy\" [#/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/%20hh:mm:ss] id=pattern-remove-4-Link\n" +
                        "                      \"yy\" [/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yy%20hh:mm:ss] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                      \"yyyy\" [/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-4-alt-1-MenuItem\n" +
                        "                  \" \" [#/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyyhh:mm:ss] id=pattern-remove-5-Link\n" +
                        "                  \"hh\" [#/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20:mm:ss] id=pattern-remove-6-Link\n" +
                        "                      \"h\" [/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20h:mm:ss] id=pattern-remove-6-alt-0-MenuItem\n" +
                        "                      \"hh\" [/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-6-alt-1-MenuItem\n" +
                        "                  \":\" [#/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hhmm:ss] id=pattern-remove-7-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh::ss] id=pattern-remove-8-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:m:ss] id=pattern-remove-8-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-8-alt-1-MenuItem\n" +
                        "                  \":\" [#/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mmss] id=pattern-remove-9-Link\n" +
                        "                  \"ss\" [#/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:] id=pattern-remove-10-Link\n" +
                        "                      \"s\" [/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:s] id=pattern-remove-10-alt-0-MenuItem\n" +
                        "                      \"ss\" [/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-10-alt-1-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [dd/mm/yyyy hh:mm:ss] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    @Test
    public void testMetadataParserDateTime() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/metadata/date-time-parser")
        );

        this.onHistoryTokenChangeAndSetPatternTextAndCheck(
                SpreadsheetPatternDialogComponent.parse(
                        this.spreadsheetPatternDialogComponentContext(
                                context,
                                DATE_TIME_FORMATTER_CONTEXT
                        )
                ),
                "dd/mm/yyyy hh:mm:ss",
                context,
                "SpreadsheetPatternDialogComponentParse\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Date Time parser \n" +
                        "    id=pattern includeClose=true CLOSED\n" +
                        "      SpreadsheetPatternComponentTabs\n" +
                        "        SpreadsheetTabsComponent\n" +
                        "          TAB 0\n" +
                        "            \"Date\" DISABLED id=pattern-date-parse\n" +
                        "          TAB 1\n" +
                        "            \"Date Time\" DISABLED id=pattern-date-time-parse\n" +
                        "          TAB 2\n" +
                        "            \"Number\" DISABLED id=pattern-number-parse\n" +
                        "          TAB 3\n" +
                        "            \"Time\" DISABLED id=pattern-time-parse\n" +
                        "      SpreadsheetPatternComponentTable\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              ROW(S)\n" +
                        "                ROW 0\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Edit Pattern\"\n" +
                        "                  \"dd/mm/yyyy hh:mm:ss\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/1999 12:58:59\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Short\"\n" +
                        "                  \"d/m/yy, h:mm AM/PM\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20d/m/yy,%20h:mm%20AM/PM] id=pattern-today short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99, 12:58 PM\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Medium\"\n" +
                        "                  \"d mmm yyyy, h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20d%20mmm%20yyyy,%20h:mm:ss%20AM/PM] id=pattern-today medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999, 12:58:59 PM\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Long\"\n" +
                        "                  \"d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-today long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999 at 12:58:59 PM\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Full\"\n" +
                        "                  \"dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dddd,%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-today full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999 at 12:58:59 PM\n" +
                        "                ROW 5\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Short\"\n" +
                        "                  \"d/m/yy, h:mm AM/PM\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20d/m/yy,%20h:mm%20AM/PM] id=pattern-31 december 1999 12:58:59 short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99, 12:58 PM\n" +
                        "                ROW 6\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Medium\"\n" +
                        "                  \"d mmm yyyy, h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20d%20mmm%20yyyy,%20h:mm:ss%20AM/PM] id=pattern-31 december 1999 12:58:59 medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999, 12:58:59 PM\n" +
                        "                ROW 7\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Long\"\n" +
                        "                  \"d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-31 december 1999 12:58:59 long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999 at 12:58:59 PM\n" +
                        "                ROW 8\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Full\"\n" +
                        "                  \"dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dddd,%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-31 december 1999 12:58:59 full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999 at 12:58:59 PM\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"dd\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-Link\n" +
                        "                      \"d\" [/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20d/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                      \"dd\" [/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                      \"ddd\" [/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20ddd/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-2-MenuItem\n" +
                        "                      \"dddd\" [/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dddd/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-3-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20ddmm/yyyy%20hh:mm:ss] id=pattern-remove-1-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd//yyyy%20hh:mm:ss] id=pattern-remove-2-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/m/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "                      \"mmm\" [/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mmm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-2-MenuItem\n" +
                        "                      \"mmmm\" [/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mmmm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-3-MenuItem\n" +
                        "                      \"mmmmm\" [/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mmmmm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-4-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mmyyyy%20hh:mm:ss] id=pattern-remove-3-Link\n" +
                        "                  \"yyyy\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/%20hh:mm:ss] id=pattern-remove-4-Link\n" +
                        "                      \"yy\" [/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yy%20hh:mm:ss] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                      \"yyyy\" [/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-4-alt-1-MenuItem\n" +
                        "                  \" \" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyyhh:mm:ss] id=pattern-remove-5-Link\n" +
                        "                  \"hh\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20:mm:ss] id=pattern-remove-6-Link\n" +
                        "                      \"h\" [/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20h:mm:ss] id=pattern-remove-6-alt-0-MenuItem\n" +
                        "                      \"hh\" [/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-6-alt-1-MenuItem\n" +
                        "                  \":\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hhmm:ss] id=pattern-remove-7-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh::ss] id=pattern-remove-8-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:m:ss] id=pattern-remove-8-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-8-alt-1-MenuItem\n" +
                        "                  \":\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mmss] id=pattern-remove-9-Link\n" +
                        "                  \"ss\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:] id=pattern-remove-10-Link\n" +
                        "                      \"s\" [/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:s] id=pattern-remove-10-alt-0-MenuItem\n" +
                        "                      \"ss\" [/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-10-alt-1-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [dd/mm/yyyy hh:mm:ss] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    // metadata / number................................................................................................

    @Test
    public void testMetadataFormatterNumber() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/metadata/number-formatter")
        );

        this.onHistoryTokenChangeAndSetPatternTextAndCheck(
                SpreadsheetPatternDialogComponent.format(
                        this.spreadsheetPatternDialogComponentContext(
                                context,
                                NUMBER_FORMATTER_CONTEXT
                        )
                ),
                "$#0.00",
                context,
                "SpreadsheetPatternDialogComponentFormat\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Number formatter \n" +
                        "    id=pattern includeClose=true CLOSED\n" +
                        "      SpreadsheetPatternComponentTabs\n" +
                        "        SpreadsheetTabsComponent\n" +
                        "          TAB 0\n" +
                        "            \"Date\" DISABLED id=pattern-date-format\n" +
                        "          TAB 1\n" +
                        "            \"Date Time\" DISABLED id=pattern-date-time-format\n" +
                        "          TAB 2\n" +
                        "            \"Number\" DISABLED id=pattern-number-format\n" +
                        "          TAB 3\n" +
                        "            \"Text\" DISABLED id=pattern-text-format\n" +
                        "          TAB 4\n" +
                        "            \"Time\" DISABLED id=pattern-time-format\n" +
                        "      SpreadsheetPatternComponentTable\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              ROW(S)\n" +
                        "                ROW 0\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Edit Pattern\"\n" +
                        "                  \"$#0.00\" [#/1/Spreadsheet123/metadata/number-formatter/save/number-format-pattern%20$%230.00] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $1234.56\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $-9876.54\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $0.00\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"General\"\n" +
                        "                  \"General\" [#/1/Spreadsheet123/metadata/number-formatter/save/number-format-pattern%20General] id=pattern-general-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    1234.56\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -9876.54\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Number\"\n" +
                        "                  \"#,##0.###\" [#/1/Spreadsheet123/metadata/number-formatter/save/number-format-pattern%20%23,%23%230.%23%23%23] id=pattern-number-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    1,234.56\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -9,876.54\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0.\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Integer\"\n" +
                        "                  \"#,##0\" [#/1/Spreadsheet123/metadata/number-formatter/save/number-format-pattern%20%23,%23%230] id=pattern-integer-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    1,235\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -9,877\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Percent\"\n" +
                        "                  \"#,##0%\" [#/1/Spreadsheet123/metadata/number-formatter/save/number-format-pattern%20%23,%23%230%25] id=pattern-percent-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    123,456%\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -987,654%\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0%\n" +
                        "                ROW 5\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Currency\"\n" +
                        "                  \"$#,##0.00\" [#/1/Spreadsheet123/metadata/number-formatter/save/number-format-pattern%20$%23,%23%230.00] id=pattern-currency-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $1,234.56\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $-9,876.54\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $0.00\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"$\" [#/1/Spreadsheet123/metadata/number-formatter/save/number-format-pattern%20%230.00] id=pattern-remove-0-Link\n" +
                        "                      \"$\" [/1/Spreadsheet123/metadata/number-formatter/save/number-format-pattern%20$%230.00] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                  \"#\" [#/1/Spreadsheet123/metadata/number-formatter/save/number-format-pattern%20$0.00] id=pattern-remove-1-Link\n" +
                        "                      \"#\" [/1/Spreadsheet123/metadata/number-formatter/save/number-format-pattern%20$%230.00] id=pattern-remove-1-alt-0-MenuItem\n" +
                        "                  \"0\" [#/1/Spreadsheet123/metadata/number-formatter/save/number-format-pattern%20$%23.00] id=pattern-remove-2-Link\n" +
                        "                      \"0\" [/1/Spreadsheet123/metadata/number-formatter/save/number-format-pattern%20$%230.00] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                  \".\" [#/1/Spreadsheet123/metadata/number-formatter/save/number-format-pattern%20$%23000] id=pattern-remove-3-Link\n" +
                        "                      \".\" [/1/Spreadsheet123/metadata/number-formatter/save/number-format-pattern%20$%230.00] id=pattern-remove-3-alt-0-MenuItem\n" +
                        "                  \"0\" [#/1/Spreadsheet123/metadata/number-formatter/save/number-format-pattern%20$%230.0] id=pattern-remove-4-Link\n" +
                        "                      \"0\" [/1/Spreadsheet123/metadata/number-formatter/save/number-format-pattern%20$%230.00] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                  \"0\" [#/1/Spreadsheet123/metadata/number-formatter/save/number-format-pattern%20$%230.0] id=pattern-remove-5-Link\n" +
                        "                      \"0\" [/1/Spreadsheet123/metadata/number-formatter/save/number-format-pattern%20$%230.00] id=pattern-remove-5-alt-0-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [$#0.00] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/metadata/number-formatter/save/number-format-pattern%20$%230.00] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    @Test
    public void testMetadataParserNumber() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/metadata/number-parser")
        );

        this.onHistoryTokenChangeAndSetPatternTextAndCheck(
                SpreadsheetPatternDialogComponent.parse(
                        this.spreadsheetPatternDialogComponentContext(
                                context,
                                NUMBER_FORMATTER_CONTEXT
                        )
                ),
                "$#0.00",
                context,
                "SpreadsheetPatternDialogComponentParse\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Number parser \n" +
                        "    id=pattern includeClose=true CLOSED\n" +
                        "      SpreadsheetPatternComponentTabs\n" +
                        "        SpreadsheetTabsComponent\n" +
                        "          TAB 0\n" +
                        "            \"Date\" DISABLED id=pattern-date-parse\n" +
                        "          TAB 1\n" +
                        "            \"Date Time\" DISABLED id=pattern-date-time-parse\n" +
                        "          TAB 2\n" +
                        "            \"Number\" DISABLED id=pattern-number-parse\n" +
                        "          TAB 3\n" +
                        "            \"Time\" DISABLED id=pattern-time-parse\n" +
                        "      SpreadsheetPatternComponentTable\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              ROW(S)\n" +
                        "                ROW 0\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Edit Pattern\"\n" +
                        "                  \"$#0.00\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$%230.00] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $1234.56\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $-9876.54\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $0.00\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Number\"\n" +
                        "                  \"#,##0.###\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20%23,%23%230.%23%23%23] id=pattern-number-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    1,234.56\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -9,876.54\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0.\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Integer\"\n" +
                        "                  \"#,##0\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20%23,%23%230] id=pattern-integer-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    1,235\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -9,877\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Percent\"\n" +
                        "                  \"#,##0%\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20%23,%23%230%25] id=pattern-percent-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    123,456%\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -987,654%\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0%\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Currency\"\n" +
                        "                  \"$#,##0.00\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$%23,%23%230.00] id=pattern-currency-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $1,234.56\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $-9,876.54\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $0.00\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"$\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20%230.00] id=pattern-remove-0-Link\n" +
                        "                      \"$\" [/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$%230.00] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                  \"#\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$0.00] id=pattern-remove-1-Link\n" +
                        "                      \"#\" [/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$%230.00] id=pattern-remove-1-alt-0-MenuItem\n" +
                        "                  \"0\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$%23.00] id=pattern-remove-2-Link\n" +
                        "                      \"0\" [/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$%230.00] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                  \".\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$%23000] id=pattern-remove-3-Link\n" +
                        "                      \".\" [/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$%230.00] id=pattern-remove-3-alt-0-MenuItem\n" +
                        "                  \"0\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$%230.0] id=pattern-remove-4-Link\n" +
                        "                      \"0\" [/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$%230.00] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                  \"0\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$%230.0] id=pattern-remove-5-Link\n" +
                        "                      \"0\" [/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$%230.00] id=pattern-remove-5-alt-0-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [$#0.00] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$%230.00] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    // metadata / text..................................................................................................

    @Test
    public void testMetadataFormatterText() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/metadata/text-formatter")
        );

        this.onHistoryTokenChangeAndSetPatternTextAndCheck(
                SpreadsheetPatternDialogComponent.format(
                        this.spreadsheetPatternDialogComponentContext(
                                context,
                                TEXT_FORMATTER_CONTEXT
                        )
                ),
                "@ \"Hello\"",
                context,
                "SpreadsheetPatternDialogComponentFormat\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Text formatter \n" +
                        "    id=pattern includeClose=true CLOSED\n" +
                        "      SpreadsheetPatternComponentTabs\n" +
                        "        SpreadsheetTabsComponent\n" +
                        "          TAB 0\n" +
                        "            \"Date\" DISABLED id=pattern-date-format\n" +
                        "          TAB 1\n" +
                        "            \"Date Time\" DISABLED id=pattern-date-time-format\n" +
                        "          TAB 2\n" +
                        "            \"Number\" DISABLED id=pattern-number-format\n" +
                        "          TAB 3\n" +
                        "            \"Text\" DISABLED id=pattern-text-format\n" +
                        "          TAB 4\n" +
                        "            \"Time\" DISABLED id=pattern-time-format\n" +
                        "      SpreadsheetPatternComponentTable\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              ROW(S)\n" +
                        "                ROW 0\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Edit Pattern\"\n" +
                        "                  \"@ \"Hello\"\" [#/1/Spreadsheet123/metadata/text-formatter/save/text-format-pattern%20@%20%22Hello%22] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Abc123 Hello\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Default text format\"\n" +
                        "                  \"@\" [#/1/Spreadsheet123/metadata/text-formatter/save/text-format-pattern%20@] id=pattern-default text format-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Abc123\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"@\" [#/1/Spreadsheet123/metadata/text-formatter/save/text-format-pattern%20%20%22Hello%22] id=pattern-remove-0-Link\n" +
                        "                      \"@\" [/1/Spreadsheet123/metadata/text-formatter/save/text-format-pattern%20@%20%22Hello%22] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                  \" \" [#/1/Spreadsheet123/metadata/text-formatter/save/text-format-pattern%20@%22Hello%22] id=pattern-remove-1-Link\n" +
                        "                  \"\"Hello\"\" [#/1/Spreadsheet123/metadata/text-formatter/save/text-format-pattern%20@%20] id=pattern-remove-2-Link\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [@ \"Hello\"] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/metadata/text-formatter/save/text-format-pattern%20@%20%22Hello%22] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    // metadata / time..................................................................................................

    @Test
    public void testMetadataFormatterTime() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/metadata/time-formatter")
        );

        this.onHistoryTokenChangeAndSetPatternTextAndCheck(
                SpreadsheetPatternDialogComponent.format(
                        this.spreadsheetPatternDialogComponentContext(
                                context,
                                TIME_FORMATTER_CONTEXT
                        )
                ),
                "hh:mm",
                context,
                "SpreadsheetPatternDialogComponentFormat\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Time formatter \n" +
                        "    id=pattern includeClose=true CLOSED\n" +
                        "      SpreadsheetPatternComponentTabs\n" +
                        "        SpreadsheetTabsComponent\n" +
                        "          TAB 0\n" +
                        "            \"Date\" DISABLED id=pattern-date-format\n" +
                        "          TAB 1\n" +
                        "            \"Date Time\" DISABLED id=pattern-date-time-format\n" +
                        "          TAB 2\n" +
                        "            \"Number\" DISABLED id=pattern-number-format\n" +
                        "          TAB 3\n" +
                        "            \"Text\" DISABLED id=pattern-text-format\n" +
                        "          TAB 4\n" +
                        "            \"Time\" DISABLED id=pattern-time-format\n" +
                        "      SpreadsheetPatternComponentTable\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              ROW(S)\n" +
                        "                ROW 0\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Edit Pattern\"\n" +
                        "                  \"hh:mm\" [#/1/Spreadsheet123/metadata/time-formatter/save/time-format-pattern%20hh:mm] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Now Short\"\n" +
                        "                  \"h:mm AM/PM\" [#/1/Spreadsheet123/metadata/time-formatter/save/time-format-pattern%20h:mm%20AM/PM] id=pattern-now short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58 PM\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Now Long\"\n" +
                        "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/time-formatter/save/time-format-pattern%20h:mm:ss%20AM/PM] id=pattern-now long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58:59 PM\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"12:58:59 AM Short\"\n" +
                        "                  \"h:mm AM/PM\" [#/1/Spreadsheet123/metadata/time-formatter/save/time-format-pattern%20h:mm%20AM/PM] id=pattern-12:58:59 am short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58 PM\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"12:58:59 AM Long\"\n" +
                        "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/time-formatter/save/time-format-pattern%20h:mm:ss%20AM/PM] id=pattern-12:58:59 am long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58:59 PM\n" +
                        "                ROW 5\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"6:01:02 PM Short\"\n" +
                        "                  \"h:mm AM/PM\" [#/1/Spreadsheet123/metadata/time-formatter/save/time-format-pattern%20h:mm%20AM/PM] id=pattern-6:01:02 pm short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    6:01 PM\n" +
                        "                ROW 6\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"6:01:02 PM Long\"\n" +
                        "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/time-formatter/save/time-format-pattern%20h:mm:ss%20AM/PM] id=pattern-6:01:02 pm long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    6:01:02 PM\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"hh\" [#/1/Spreadsheet123/metadata/time-formatter/save/time-format-pattern%20:mm] id=pattern-remove-0-Link\n" +
                        "                      \"h\" [/1/Spreadsheet123/metadata/time-formatter/save/time-format-pattern%20h:mm] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                      \"hh\" [/1/Spreadsheet123/metadata/time-formatter/save/time-format-pattern%20hh:mm] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                  \":\" [#/1/Spreadsheet123/metadata/time-formatter/save/time-format-pattern%20hhmm] id=pattern-remove-1-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/metadata/time-formatter/save/time-format-pattern%20hh:] id=pattern-remove-2-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/metadata/time-formatter/save/time-format-pattern%20hh:m] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/metadata/time-formatter/save/time-format-pattern%20hh:mm] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [hh:mm] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/metadata/time-formatter/save/time-format-pattern%20hh:mm] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    @Test
    public void testMetadataParserTime() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/metadata/time-parser")
        );

        this.onHistoryTokenChangeAndSetPatternTextAndCheck(
                SpreadsheetPatternDialogComponent.parse(
                        this.spreadsheetPatternDialogComponentContext(
                                context,
                                TIME_FORMATTER_CONTEXT
                        )
                ),
                "hh:mm",
                context,
                "SpreadsheetPatternDialogComponentParse\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Time parser \n" +
                        "    id=pattern includeClose=true CLOSED\n" +
                        "      SpreadsheetPatternComponentTabs\n" +
                        "        SpreadsheetTabsComponent\n" +
                        "          TAB 0\n" +
                        "            \"Date\" DISABLED id=pattern-date-parse\n" +
                        "          TAB 1\n" +
                        "            \"Date Time\" DISABLED id=pattern-date-time-parse\n" +
                        "          TAB 2\n" +
                        "            \"Number\" DISABLED id=pattern-number-parse\n" +
                        "          TAB 3\n" +
                        "            \"Time\" DISABLED id=pattern-time-parse\n" +
                        "      SpreadsheetPatternComponentTable\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              ROW(S)\n" +
                        "                ROW 0\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Edit Pattern\"\n" +
                        "                  \"hh:mm\" [#/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20hh:mm] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Now Short\"\n" +
                        "                  \"h:mm AM/PM\" [#/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20h:mm%20AM/PM] id=pattern-now short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58 PM\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Now Long\"\n" +
                        "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20h:mm:ss%20AM/PM] id=pattern-now long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58:59 PM\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"12:58:59 AM Short\"\n" +
                        "                  \"h:mm AM/PM\" [#/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20h:mm%20AM/PM] id=pattern-12:58:59 am short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58 PM\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"12:58:59 AM Long\"\n" +
                        "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20h:mm:ss%20AM/PM] id=pattern-12:58:59 am long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58:59 PM\n" +
                        "                ROW 5\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"6:01:02 PM Short\"\n" +
                        "                  \"h:mm AM/PM\" [#/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20h:mm%20AM/PM] id=pattern-6:01:02 pm short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    6:01 PM\n" +
                        "                ROW 6\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"6:01:02 PM Long\"\n" +
                        "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20h:mm:ss%20AM/PM] id=pattern-6:01:02 pm long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    6:01:02 PM\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"hh\" [#/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20:mm] id=pattern-remove-0-Link\n" +
                        "                      \"h\" [/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20h:mm] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                      \"hh\" [/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20hh:mm] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                  \":\" [#/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20hhmm] id=pattern-remove-1-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20hh:] id=pattern-remove-2-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20hh:m] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20hh:mm] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [hh:mm] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20hh:mm] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }
    private void onHistoryTokenChangeAndSetPatternTextAndCheck(final SpreadsheetPatternDialogComponent dialog,
                                                               final String patternText,
                                                               final AppContext context,
                                                               final String expected) {
        this.checkEquals(
                false,
                dialog.isMatch(NOT_MATCHED),
                () -> "should not be matched " + NOT_MATCHED
        );

        dialog.onHistoryTokenChange(
                NOT_MATCHED,
                context
        );

        dialog.setPatternText(patternText);

        this.treePrintAndCheck(
                dialog,
                expected
        );
    }

    private AppContext appContext(final HistoryToken historyToken) {
        return new FakeAppContext() {

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return null;
            }

            @Override
            public HistoryToken historyToken() {
                return historyToken;
            }

            @Override
            public SpreadsheetViewportCache spreadsheetViewportCache() {
                return SpreadsheetViewportCache.empty(this);
            }

            @Override
            public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
                return null;
            }

            @Override
            public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
                return null;
            }
        };
    }

    private SpreadsheetPatternDialogComponentContext spreadsheetPatternDialogComponentContext(final AppContext context,
                                                                                              final SpreadsheetFormatterContext formatterContext) {
        return new FakeSpreadsheetPatternDialogComponentContext() {
            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return null;
            }

            @Override
            public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
                return null;
            }

            @Override
            public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
                return null;
            }

            @Override
            public boolean isMatch(final HistoryToken token) {
                return context.historyToken()
                        .equals(token);
            }

            @Override
            public boolean shouldIgnore(final HistoryToken token) {
                return true;
            }

            @Override
            public HistoryToken historyToken() {
                return context.historyToken();
            }

            @Override
            public SpreadsheetPatternKind patternKind() {
                return context.historyToken()
                        .patternKind()
                        .get();
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

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetPatternDialogComponent> type() {
        return SpreadsheetPatternDialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
