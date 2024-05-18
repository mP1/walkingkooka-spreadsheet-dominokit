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

    private final static SpreadsheetFormatterContext DATE_FORMATTER_CONTEXT = new FakeSpreadsheetFormatterContext() {

        @Override
        public <T> T convertOrFail(final Object value,
                                   final Class<T> target) {
            return (T) LocalDateTime.of(
                    (LocalDate) value,
                    LocalTime.NOON
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
        public <T> T convertOrFail(final Object value,
                                   final Class<T> target) {
            return (T) target.cast(value);
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
        public <T> T convertOrFail(final Object value,
                                   final Class<T> target) {
            return (T) target.cast(value);
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
        public <T> T convertOrFail(final Object value,
                                   final Class<T> target) {
            return (T) target.cast(value);
        }
    };

    private final static SpreadsheetFormatterContext TIME_FORMATTER_CONTEXT = new FakeSpreadsheetFormatterContext() {

        @Override
        public <T> T convertOrFail(final Object value,
                                   final Class<T> target) {
            return (T)
                    LocalDateTime.of(
                            LocalDate.of(2000, 1, 1),
                            LocalTime.class.cast(value)
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

    // cell / date......................................................................................................

    @Test
    public void testCellFormatPatternDate() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/cell/A1/format-pattern/date")
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
                        "    Pattern\n" +
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
                        "                  \"dd/mm/yyyy\" [#/1/Spreadsheet123/cell/A1/format-pattern/date/save/dd/mm/yyyy] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/1999\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Short\"\n" +
                        "                  \"d/m/yy\" [#/1/Spreadsheet123/cell/A1/format-pattern/date/save/d/m/yy] id=pattern-today short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Medium\"\n" +
                        "                  \"d mmm yyyy\" [#/1/Spreadsheet123/cell/A1/format-pattern/date/save/d%20mmm%20yyyy] id=pattern-today medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Long\"\n" +
                        "                  \"d mmmm yyyy\" [#/1/Spreadsheet123/cell/A1/format-pattern/date/save/d%20mmmm%20yyyy] id=pattern-today long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Full\"\n" +
                        "                  \"dddd, d mmmm yyyy\" [#/1/Spreadsheet123/cell/A1/format-pattern/date/save/dddd,%20d%20mmmm%20yyyy] id=pattern-today full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999\n" +
                        "                ROW 5\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Short\"\n" +
                        "                  \"d/m/yy\" [#/1/Spreadsheet123/cell/A1/format-pattern/date/save/d/m/yy] id=pattern-31 december 1999 short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99\n" +
                        "                ROW 6\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Medium\"\n" +
                        "                  \"d mmm yyyy\" [#/1/Spreadsheet123/cell/A1/format-pattern/date/save/d%20mmm%20yyyy] id=pattern-31 december 1999 medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999\n" +
                        "                ROW 7\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Long\"\n" +
                        "                  \"d mmmm yyyy\" [#/1/Spreadsheet123/cell/A1/format-pattern/date/save/d%20mmmm%20yyyy] id=pattern-31 december 1999 long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999\n" +
                        "                ROW 8\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Full\"\n" +
                        "                  \"dddd, d mmmm yyyy\" [#/1/Spreadsheet123/cell/A1/format-pattern/date/save/dddd,%20d%20mmmm%20yyyy] id=pattern-31 december 1999 full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"dd\" [#/1/Spreadsheet123/cell/A1/format-pattern/date/save//mm/yyyy] id=pattern-remove-0-Link\n" +
                        "                      \"d\" [/1/Spreadsheet123/cell/A1/format-pattern/date/save/d/mm/yyyy] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                      \"dd\" [/1/Spreadsheet123/cell/A1/format-pattern/date/save/dd/mm/yyyy] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                      \"ddd\" [/1/Spreadsheet123/cell/A1/format-pattern/date/save/ddd/mm/yyyy] id=pattern-remove-0-alt-2-MenuItem\n" +
                        "                      \"dddd\" [/1/Spreadsheet123/cell/A1/format-pattern/date/save/dddd/mm/yyyy] id=pattern-remove-0-alt-3-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/cell/A1/format-pattern/date/save/ddmm/yyyy] id=pattern-remove-1-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/cell/A1/format-pattern/date/save/dd//yyyy] id=pattern-remove-2-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/cell/A1/format-pattern/date/save/dd/m/yyyy] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/cell/A1/format-pattern/date/save/dd/mm/yyyy] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "                      \"mmm\" [/1/Spreadsheet123/cell/A1/format-pattern/date/save/dd/mmm/yyyy] id=pattern-remove-2-alt-2-MenuItem\n" +
                        "                      \"mmmm\" [/1/Spreadsheet123/cell/A1/format-pattern/date/save/dd/mmmm/yyyy] id=pattern-remove-2-alt-3-MenuItem\n" +
                        "                      \"mmmmm\" [/1/Spreadsheet123/cell/A1/format-pattern/date/save/dd/mmmmm/yyyy] id=pattern-remove-2-alt-4-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/cell/A1/format-pattern/date/save/dd/mmyyyy] id=pattern-remove-3-Link\n" +
                        "                  \"yyyy\" [#/1/Spreadsheet123/cell/A1/format-pattern/date/save/dd/mm/] id=pattern-remove-4-Link\n" +
                        "                      \"yy\" [/1/Spreadsheet123/cell/A1/format-pattern/date/save/dd/mm/yy] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                      \"yyyy\" [/1/Spreadsheet123/cell/A1/format-pattern/date/save/dd/mm/yyyy] id=pattern-remove-4-alt-1-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [dd/mm/yyyy] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/cell/A1/format-pattern/date/save/dd/mm/yyyy] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    @Test
    public void testCellParsePatternDate() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/cell/A1/parse-pattern/date")
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
                        "    Pattern\n" +
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
                        "                  \"dd/mm/yyyy\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date/save/dd/mm/yyyy] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/1999\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Short\"\n" +
                        "                  \"d/m/yy\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date/save/d/m/yy] id=pattern-today short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Medium\"\n" +
                        "                  \"d mmm yyyy\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date/save/d%20mmm%20yyyy] id=pattern-today medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Long\"\n" +
                        "                  \"d mmmm yyyy\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date/save/d%20mmmm%20yyyy] id=pattern-today long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Full\"\n" +
                        "                  \"dddd, d mmmm yyyy\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date/save/dddd,%20d%20mmmm%20yyyy] id=pattern-today full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999\n" +
                        "                ROW 5\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Short\"\n" +
                        "                  \"d/m/yy\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date/save/d/m/yy] id=pattern-31 december 1999 short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99\n" +
                        "                ROW 6\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Medium\"\n" +
                        "                  \"d mmm yyyy\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date/save/d%20mmm%20yyyy] id=pattern-31 december 1999 medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999\n" +
                        "                ROW 7\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Long\"\n" +
                        "                  \"d mmmm yyyy\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date/save/d%20mmmm%20yyyy] id=pattern-31 december 1999 long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999\n" +
                        "                ROW 8\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Full\"\n" +
                        "                  \"dddd, d mmmm yyyy\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date/save/dddd,%20d%20mmmm%20yyyy] id=pattern-31 december 1999 full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"dd\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date/save//mm/yyyy] id=pattern-remove-0-Link\n" +
                        "                      \"d\" [/1/Spreadsheet123/cell/A1/parse-pattern/date/save/d/mm/yyyy] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                      \"dd\" [/1/Spreadsheet123/cell/A1/parse-pattern/date/save/dd/mm/yyyy] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                      \"ddd\" [/1/Spreadsheet123/cell/A1/parse-pattern/date/save/ddd/mm/yyyy] id=pattern-remove-0-alt-2-MenuItem\n" +
                        "                      \"dddd\" [/1/Spreadsheet123/cell/A1/parse-pattern/date/save/dddd/mm/yyyy] id=pattern-remove-0-alt-3-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date/save/ddmm/yyyy] id=pattern-remove-1-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date/save/dd//yyyy] id=pattern-remove-2-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/cell/A1/parse-pattern/date/save/dd/m/yyyy] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/cell/A1/parse-pattern/date/save/dd/mm/yyyy] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "                      \"mmm\" [/1/Spreadsheet123/cell/A1/parse-pattern/date/save/dd/mmm/yyyy] id=pattern-remove-2-alt-2-MenuItem\n" +
                        "                      \"mmmm\" [/1/Spreadsheet123/cell/A1/parse-pattern/date/save/dd/mmmm/yyyy] id=pattern-remove-2-alt-3-MenuItem\n" +
                        "                      \"mmmmm\" [/1/Spreadsheet123/cell/A1/parse-pattern/date/save/dd/mmmmm/yyyy] id=pattern-remove-2-alt-4-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date/save/dd/mmyyyy] id=pattern-remove-3-Link\n" +
                        "                  \"yyyy\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date/save/dd/mm/] id=pattern-remove-4-Link\n" +
                        "                      \"yy\" [/1/Spreadsheet123/cell/A1/parse-pattern/date/save/dd/mm/yy] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                      \"yyyy\" [/1/Spreadsheet123/cell/A1/parse-pattern/date/save/dd/mm/yyyy] id=pattern-remove-4-alt-1-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [dd/mm/yyyy] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date/save/dd/mm/yyyy] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    // cell / date-time.................................................................................................

    @Test
    public void testCellFormatPatternDateTime() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/cell/A1/format-pattern/date-time")
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
                        "    Pattern\n" +
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
                        "                  \"dd/mm/yyyy hh:mm:ss\" [#/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/1999 12:58:59\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Short\"\n" +
                        "                  \"d/m/yy, h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/d/m/yy,%20h:mm%20AM/PM] id=pattern-today short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99, 12:58 PM\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Medium\"\n" +
                        "                  \"d mmm yyyy, h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/d%20mmm%20yyyy,%20h:mm:ss%20AM/PM] id=pattern-today medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999, 12:58:59 PM\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Long\"\n" +
                        "                  \"d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-today long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999 at 12:58:59 PM\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Full\"\n" +
                        "                  \"dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/dddd,%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-today full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999 at 12:58:59 PM\n" +
                        "                ROW 5\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Short\"\n" +
                        "                  \"d/m/yy, h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/d/m/yy,%20h:mm%20AM/PM] id=pattern-31 december 1999 12:58:59 short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99, 12:58 PM\n" +
                        "                ROW 6\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Medium\"\n" +
                        "                  \"d mmm yyyy, h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/d%20mmm%20yyyy,%20h:mm:ss%20AM/PM] id=pattern-31 december 1999 12:58:59 medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999, 12:58:59 PM\n" +
                        "                ROW 7\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Long\"\n" +
                        "                  \"d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-31 december 1999 12:58:59 long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999 at 12:58:59 PM\n" +
                        "                ROW 8\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Full\"\n" +
                        "                  \"dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/dddd,%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-31 december 1999 12:58:59 full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999 at 12:58:59 PM\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"dd\" [#/1/Spreadsheet123/cell/A1/format-pattern/date-time/save//mm/yyyy%20hh:mm:ss] id=pattern-remove-0-Link\n" +
                        "                      \"d\" [/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/d/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                      \"dd\" [/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                      \"ddd\" [/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/ddd/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-2-MenuItem\n" +
                        "                      \"dddd\" [/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/dddd/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-3-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/ddmm/yyyy%20hh:mm:ss] id=pattern-remove-1-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/dd//yyyy%20hh:mm:ss] id=pattern-remove-2-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/dd/m/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "                      \"mmm\" [/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/dd/mmm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-2-MenuItem\n" +
                        "                      \"mmmm\" [/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/dd/mmmm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-3-MenuItem\n" +
                        "                      \"mmmmm\" [/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/dd/mmmmm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-4-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/dd/mmyyyy%20hh:mm:ss] id=pattern-remove-3-Link\n" +
                        "                  \"yyyy\" [#/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/dd/mm/%20hh:mm:ss] id=pattern-remove-4-Link\n" +
                        "                      \"yy\" [/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/dd/mm/yy%20hh:mm:ss] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                      \"yyyy\" [/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-4-alt-1-MenuItem\n" +
                        "                  \" \" [#/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/dd/mm/yyyyhh:mm:ss] id=pattern-remove-5-Link\n" +
                        "                  \"hh\" [#/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/dd/mm/yyyy%20:mm:ss] id=pattern-remove-6-Link\n" +
                        "                      \"h\" [/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/dd/mm/yyyy%20h:mm:ss] id=pattern-remove-6-alt-0-MenuItem\n" +
                        "                      \"hh\" [/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-6-alt-1-MenuItem\n" +
                        "                  \":\" [#/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/dd/mm/yyyy%20hhmm:ss] id=pattern-remove-7-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/dd/mm/yyyy%20hh::ss] id=pattern-remove-8-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/dd/mm/yyyy%20hh:m:ss] id=pattern-remove-8-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-8-alt-1-MenuItem\n" +
                        "                  \":\" [#/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/dd/mm/yyyy%20hh:mmss] id=pattern-remove-9-Link\n" +
                        "                  \"ss\" [#/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/dd/mm/yyyy%20hh:mm:] id=pattern-remove-10-Link\n" +
                        "                      \"s\" [/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/dd/mm/yyyy%20hh:mm:s] id=pattern-remove-10-alt-0-MenuItem\n" +
                        "                      \"ss\" [/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-10-alt-1-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [dd/mm/yyyy hh:mm:ss] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/cell/A1/format-pattern/date-time/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    @Test
    public void testCellParsePatternDateTime() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/cell/A1/parse-pattern/date-time")
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
                        "    Pattern\n" +
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
                        "                  \"dd/mm/yyyy hh:mm:ss\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/1999 12:58:59\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Short\"\n" +
                        "                  \"d/m/yy, h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/d/m/yy,%20h:mm%20AM/PM] id=pattern-today short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99, 12:58 PM\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Medium\"\n" +
                        "                  \"d mmm yyyy, h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/d%20mmm%20yyyy,%20h:mm:ss%20AM/PM] id=pattern-today medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999, 12:58:59 PM\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Long\"\n" +
                        "                  \"d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-today long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999 at 12:58:59 PM\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Full\"\n" +
                        "                  \"dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/dddd,%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-today full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999 at 12:58:59 PM\n" +
                        "                ROW 5\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Short\"\n" +
                        "                  \"d/m/yy, h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/d/m/yy,%20h:mm%20AM/PM] id=pattern-31 december 1999 12:58:59 short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99, 12:58 PM\n" +
                        "                ROW 6\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Medium\"\n" +
                        "                  \"d mmm yyyy, h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/d%20mmm%20yyyy,%20h:mm:ss%20AM/PM] id=pattern-31 december 1999 12:58:59 medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999, 12:58:59 PM\n" +
                        "                ROW 7\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Long\"\n" +
                        "                  \"d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-31 december 1999 12:58:59 long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999 at 12:58:59 PM\n" +
                        "                ROW 8\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Full\"\n" +
                        "                  \"dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/dddd,%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-31 december 1999 12:58:59 full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999 at 12:58:59 PM\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"dd\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save//mm/yyyy%20hh:mm:ss] id=pattern-remove-0-Link\n" +
                        "                      \"d\" [/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/d/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                      \"dd\" [/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                      \"ddd\" [/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/ddd/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-2-MenuItem\n" +
                        "                      \"dddd\" [/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/dddd/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-3-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/ddmm/yyyy%20hh:mm:ss] id=pattern-remove-1-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/dd//yyyy%20hh:mm:ss] id=pattern-remove-2-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/dd/m/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "                      \"mmm\" [/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/dd/mmm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-2-MenuItem\n" +
                        "                      \"mmmm\" [/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/dd/mmmm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-3-MenuItem\n" +
                        "                      \"mmmmm\" [/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/dd/mmmmm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-4-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/dd/mmyyyy%20hh:mm:ss] id=pattern-remove-3-Link\n" +
                        "                  \"yyyy\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/dd/mm/%20hh:mm:ss] id=pattern-remove-4-Link\n" +
                        "                      \"yy\" [/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/dd/mm/yy%20hh:mm:ss] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                      \"yyyy\" [/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-4-alt-1-MenuItem\n" +
                        "                  \" \" [#/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/dd/mm/yyyyhh:mm:ss] id=pattern-remove-5-Link\n" +
                        "                  \"hh\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy%20:mm:ss] id=pattern-remove-6-Link\n" +
                        "                      \"h\" [/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy%20h:mm:ss] id=pattern-remove-6-alt-0-MenuItem\n" +
                        "                      \"hh\" [/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-6-alt-1-MenuItem\n" +
                        "                  \":\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy%20hhmm:ss] id=pattern-remove-7-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy%20hh::ss] id=pattern-remove-8-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy%20hh:m:ss] id=pattern-remove-8-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-8-alt-1-MenuItem\n" +
                        "                  \":\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy%20hh:mmss] id=pattern-remove-9-Link\n" +
                        "                  \"ss\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy%20hh:mm:] id=pattern-remove-10-Link\n" +
                        "                      \"s\" [/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy%20hh:mm:s] id=pattern-remove-10-alt-0-MenuItem\n" +
                        "                      \"ss\" [/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-10-alt-1-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [dd/mm/yyyy hh:mm:ss] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    // cell / number....................................................................................................

    @Test
    public void testCellFormatPatternNumber() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/cell/A1/format-pattern/number")
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
                        "    Pattern\n" +
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
                        "                  \"$#0.00\" [#/1/Spreadsheet123/cell/A1/format-pattern/number/save/$%230.00] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $1234.56\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $-9876.54\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $0.00\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"General\"\n" +
                        "                  \"General\" [#/1/Spreadsheet123/cell/A1/format-pattern/number/save/General] id=pattern-general-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    1234.56\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -9876.54\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Number\"\n" +
                        "                  \"#,##0.###\" [#/1/Spreadsheet123/cell/A1/format-pattern/number/save/%23,%23%230.%23%23%23] id=pattern-number-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    1,234.56\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -9,876.54\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0.\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Integer\"\n" +
                        "                  \"#,##0\" [#/1/Spreadsheet123/cell/A1/format-pattern/number/save/%23,%23%230] id=pattern-integer-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    1,235\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -9,877\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Percent\"\n" +
                        "                  \"#,##0%\" [#/1/Spreadsheet123/cell/A1/format-pattern/number/save/%23,%23%230%25] id=pattern-percent-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    123,456%\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -987,654%\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0%\n" +
                        "                ROW 5\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Currency\"\n" +
                        "                  \"$#,##0.00\" [#/1/Spreadsheet123/cell/A1/format-pattern/number/save/$%23,%23%230.00] id=pattern-currency-Link\n" +
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
                        "                  \"$\" [#/1/Spreadsheet123/cell/A1/format-pattern/number/save/%230.00] id=pattern-remove-0-Link\n" +
                        "                      \"$\" [/1/Spreadsheet123/cell/A1/format-pattern/number/save/$%230.00] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                  \"#\" [#/1/Spreadsheet123/cell/A1/format-pattern/number/save/$0.00] id=pattern-remove-1-Link\n" +
                        "                      \"#\" [/1/Spreadsheet123/cell/A1/format-pattern/number/save/$%230.00] id=pattern-remove-1-alt-0-MenuItem\n" +
                        "                  \"0\" [#/1/Spreadsheet123/cell/A1/format-pattern/number/save/$%23.00] id=pattern-remove-2-Link\n" +
                        "                      \"0\" [/1/Spreadsheet123/cell/A1/format-pattern/number/save/$%230.00] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                  \".\" [#/1/Spreadsheet123/cell/A1/format-pattern/number/save/$%23000] id=pattern-remove-3-Link\n" +
                        "                      \".\" [/1/Spreadsheet123/cell/A1/format-pattern/number/save/$%230.00] id=pattern-remove-3-alt-0-MenuItem\n" +
                        "                  \"0\" [#/1/Spreadsheet123/cell/A1/format-pattern/number/save/$%230.0] id=pattern-remove-4-Link\n" +
                        "                      \"0\" [/1/Spreadsheet123/cell/A1/format-pattern/number/save/$%230.00] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                  \"0\" [#/1/Spreadsheet123/cell/A1/format-pattern/number/save/$%230.0] id=pattern-remove-5-Link\n" +
                        "                      \"0\" [/1/Spreadsheet123/cell/A1/format-pattern/number/save/$%230.00] id=pattern-remove-5-alt-0-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [$#0.00] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/cell/A1/format-pattern/number/save/$%230.00] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    @Test
    public void testCellParsePatternNumber() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/cell/A1/parse-pattern/number")
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
                        "    Pattern\n" +
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
                        "                  \"$#0.00\" [#/1/Spreadsheet123/cell/A1/parse-pattern/number/save/$%230.00] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $1234.56\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $-9876.54\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $0.00\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Number\"\n" +
                        "                  \"#,##0.###\" [#/1/Spreadsheet123/cell/A1/parse-pattern/number/save/%23,%23%230.%23%23%23] id=pattern-number-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    1,234.56\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -9,876.54\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0.\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Integer\"\n" +
                        "                  \"#,##0\" [#/1/Spreadsheet123/cell/A1/parse-pattern/number/save/%23,%23%230] id=pattern-integer-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    1,235\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -9,877\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Percent\"\n" +
                        "                  \"#,##0%\" [#/1/Spreadsheet123/cell/A1/parse-pattern/number/save/%23,%23%230%25] id=pattern-percent-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    123,456%\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -987,654%\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0%\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Currency\"\n" +
                        "                  \"$#,##0.00\" [#/1/Spreadsheet123/cell/A1/parse-pattern/number/save/$%23,%23%230.00] id=pattern-currency-Link\n" +
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
                        "                  \"$\" [#/1/Spreadsheet123/cell/A1/parse-pattern/number/save/%230.00] id=pattern-remove-0-Link\n" +
                        "                      \"$\" [/1/Spreadsheet123/cell/A1/parse-pattern/number/save/$%230.00] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                  \"#\" [#/1/Spreadsheet123/cell/A1/parse-pattern/number/save/$0.00] id=pattern-remove-1-Link\n" +
                        "                      \"#\" [/1/Spreadsheet123/cell/A1/parse-pattern/number/save/$%230.00] id=pattern-remove-1-alt-0-MenuItem\n" +
                        "                  \"0\" [#/1/Spreadsheet123/cell/A1/parse-pattern/number/save/$%23.00] id=pattern-remove-2-Link\n" +
                        "                      \"0\" [/1/Spreadsheet123/cell/A1/parse-pattern/number/save/$%230.00] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                  \".\" [#/1/Spreadsheet123/cell/A1/parse-pattern/number/save/$%23000] id=pattern-remove-3-Link\n" +
                        "                      \".\" [/1/Spreadsheet123/cell/A1/parse-pattern/number/save/$%230.00] id=pattern-remove-3-alt-0-MenuItem\n" +
                        "                  \"0\" [#/1/Spreadsheet123/cell/A1/parse-pattern/number/save/$%230.0] id=pattern-remove-4-Link\n" +
                        "                      \"0\" [/1/Spreadsheet123/cell/A1/parse-pattern/number/save/$%230.00] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                  \"0\" [#/1/Spreadsheet123/cell/A1/parse-pattern/number/save/$%230.0] id=pattern-remove-5-Link\n" +
                        "                      \"0\" [/1/Spreadsheet123/cell/A1/parse-pattern/number/save/$%230.00] id=pattern-remove-5-alt-0-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [$#0.00] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/cell/A1/parse-pattern/number/save/$%230.00] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    // cell / text......................................................................................................

    @Test
    public void testCellFormatPatternText() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/cell/A1/format-pattern/text")
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
                        "    Pattern\n" +
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
                        "                  \"@ \"Hello\"\" [#/1/Spreadsheet123/cell/A1/format-pattern/text/save/@%20%22Hello%22] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Abc123 Hello\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Default text format\"\n" +
                        "                  \"@\" [#/1/Spreadsheet123/cell/A1/format-pattern/text/save/@] id=pattern-default text format-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Abc123\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"@\" [#/1/Spreadsheet123/cell/A1/format-pattern/text/save/%20%22Hello%22] id=pattern-remove-0-Link\n" +
                        "                      \"@\" [/1/Spreadsheet123/cell/A1/format-pattern/text/save/@%20%22Hello%22] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                  \" \" [#/1/Spreadsheet123/cell/A1/format-pattern/text/save/@%22Hello%22] id=pattern-remove-1-Link\n" +
                        "                  \"\"Hello\"\" [#/1/Spreadsheet123/cell/A1/format-pattern/text/save/@%20] id=pattern-remove-2-Link\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [@ \"Hello\"] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/cell/A1/format-pattern/text/save/@%20%22Hello%22] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    // cell / time......................................................................................................

    @Test
    public void testCellFormatPatternTime() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/cell/A1/format-pattern/time")
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
                        "    Pattern\n" +
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
                        "                  \"hh:mm\" [#/1/Spreadsheet123/cell/A1/format-pattern/time/save/hh:mm] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Now Short\"\n" +
                        "                  \"h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/format-pattern/time/save/h:mm%20AM/PM] id=pattern-now short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58 PM\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Now Long\"\n" +
                        "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/format-pattern/time/save/h:mm:ss%20AM/PM] id=pattern-now long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58:59 PM\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"12:58:59 AM Short\"\n" +
                        "                  \"h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/format-pattern/time/save/h:mm%20AM/PM] id=pattern-12:58:59 am short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58 PM\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"12:58:59 AM Long\"\n" +
                        "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/format-pattern/time/save/h:mm:ss%20AM/PM] id=pattern-12:58:59 am long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58:59 PM\n" +
                        "                ROW 5\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"6:01:02 PM Short\"\n" +
                        "                  \"h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/format-pattern/time/save/h:mm%20AM/PM] id=pattern-6:01:02 pm short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    6:01 PM\n" +
                        "                ROW 6\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"6:01:02 PM Long\"\n" +
                        "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/format-pattern/time/save/h:mm:ss%20AM/PM] id=pattern-6:01:02 pm long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    6:01:02 PM\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"hh\" [#/1/Spreadsheet123/cell/A1/format-pattern/time/save/:mm] id=pattern-remove-0-Link\n" +
                        "                      \"h\" [/1/Spreadsheet123/cell/A1/format-pattern/time/save/h:mm] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                      \"hh\" [/1/Spreadsheet123/cell/A1/format-pattern/time/save/hh:mm] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                  \":\" [#/1/Spreadsheet123/cell/A1/format-pattern/time/save/hhmm] id=pattern-remove-1-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/cell/A1/format-pattern/time/save/hh:] id=pattern-remove-2-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/cell/A1/format-pattern/time/save/hh:m] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/cell/A1/format-pattern/time/save/hh:mm] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [hh:mm] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/cell/A1/format-pattern/time/save/hh:mm] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    @Test
    public void testCellParsePatternTime() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/cell/A1/parse-pattern/time")
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
                        "    Pattern\n" +
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
                        "                  \"hh:mm\" [#/1/Spreadsheet123/cell/A1/parse-pattern/time/save/hh:mm] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Now Short\"\n" +
                        "                  \"h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/parse-pattern/time/save/h:mm%20AM/PM] id=pattern-now short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58 PM\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Now Long\"\n" +
                        "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parse-pattern/time/save/h:mm:ss%20AM/PM] id=pattern-now long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58:59 PM\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"12:58:59 AM Short\"\n" +
                        "                  \"h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/parse-pattern/time/save/h:mm%20AM/PM] id=pattern-12:58:59 am short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58 PM\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"12:58:59 AM Long\"\n" +
                        "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parse-pattern/time/save/h:mm:ss%20AM/PM] id=pattern-12:58:59 am long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58:59 PM\n" +
                        "                ROW 5\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"6:01:02 PM Short\"\n" +
                        "                  \"h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/parse-pattern/time/save/h:mm%20AM/PM] id=pattern-6:01:02 pm short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    6:01 PM\n" +
                        "                ROW 6\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"6:01:02 PM Long\"\n" +
                        "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parse-pattern/time/save/h:mm:ss%20AM/PM] id=pattern-6:01:02 pm long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    6:01:02 PM\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"hh\" [#/1/Spreadsheet123/cell/A1/parse-pattern/time/save/:mm] id=pattern-remove-0-Link\n" +
                        "                      \"h\" [/1/Spreadsheet123/cell/A1/parse-pattern/time/save/h:mm] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                      \"hh\" [/1/Spreadsheet123/cell/A1/parse-pattern/time/save/hh:mm] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                  \":\" [#/1/Spreadsheet123/cell/A1/parse-pattern/time/save/hhmm] id=pattern-remove-1-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/cell/A1/parse-pattern/time/save/hh:] id=pattern-remove-2-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/cell/A1/parse-pattern/time/save/hh:m] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/cell/A1/parse-pattern/time/save/hh:mm] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [hh:mm] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/cell/A1/parse-pattern/time/save/hh:mm] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    // metadata / date..................................................................................................

    @Test
    public void testMetadataFormatPatternDate() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/metadata/date-format-pattern")
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
                        "    Pattern\n" +
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
                        "                  \"dd/mm/yyyy\" [#/1/Spreadsheet123/metadata/date-format-pattern/save/dd/mm/yyyy] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/1999\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Short\"\n" +
                        "                  \"d/m/yy\" [#/1/Spreadsheet123/metadata/date-format-pattern/save/d/m/yy] id=pattern-today short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Medium\"\n" +
                        "                  \"d mmm yyyy\" [#/1/Spreadsheet123/metadata/date-format-pattern/save/d%20mmm%20yyyy] id=pattern-today medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Long\"\n" +
                        "                  \"d mmmm yyyy\" [#/1/Spreadsheet123/metadata/date-format-pattern/save/d%20mmmm%20yyyy] id=pattern-today long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Full\"\n" +
                        "                  \"dddd, d mmmm yyyy\" [#/1/Spreadsheet123/metadata/date-format-pattern/save/dddd,%20d%20mmmm%20yyyy] id=pattern-today full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999\n" +
                        "                ROW 5\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Short\"\n" +
                        "                  \"d/m/yy\" [#/1/Spreadsheet123/metadata/date-format-pattern/save/d/m/yy] id=pattern-31 december 1999 short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99\n" +
                        "                ROW 6\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Medium\"\n" +
                        "                  \"d mmm yyyy\" [#/1/Spreadsheet123/metadata/date-format-pattern/save/d%20mmm%20yyyy] id=pattern-31 december 1999 medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999\n" +
                        "                ROW 7\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Long\"\n" +
                        "                  \"d mmmm yyyy\" [#/1/Spreadsheet123/metadata/date-format-pattern/save/d%20mmmm%20yyyy] id=pattern-31 december 1999 long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999\n" +
                        "                ROW 8\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Full\"\n" +
                        "                  \"dddd, d mmmm yyyy\" [#/1/Spreadsheet123/metadata/date-format-pattern/save/dddd,%20d%20mmmm%20yyyy] id=pattern-31 december 1999 full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"dd\" [#/1/Spreadsheet123/metadata/date-format-pattern/save//mm/yyyy] id=pattern-remove-0-Link\n" +
                        "                      \"d\" [/1/Spreadsheet123/metadata/date-format-pattern/save/d/mm/yyyy] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                      \"dd\" [/1/Spreadsheet123/metadata/date-format-pattern/save/dd/mm/yyyy] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                      \"ddd\" [/1/Spreadsheet123/metadata/date-format-pattern/save/ddd/mm/yyyy] id=pattern-remove-0-alt-2-MenuItem\n" +
                        "                      \"dddd\" [/1/Spreadsheet123/metadata/date-format-pattern/save/dddd/mm/yyyy] id=pattern-remove-0-alt-3-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/metadata/date-format-pattern/save/ddmm/yyyy] id=pattern-remove-1-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/metadata/date-format-pattern/save/dd//yyyy] id=pattern-remove-2-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/metadata/date-format-pattern/save/dd/m/yyyy] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/metadata/date-format-pattern/save/dd/mm/yyyy] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "                      \"mmm\" [/1/Spreadsheet123/metadata/date-format-pattern/save/dd/mmm/yyyy] id=pattern-remove-2-alt-2-MenuItem\n" +
                        "                      \"mmmm\" [/1/Spreadsheet123/metadata/date-format-pattern/save/dd/mmmm/yyyy] id=pattern-remove-2-alt-3-MenuItem\n" +
                        "                      \"mmmmm\" [/1/Spreadsheet123/metadata/date-format-pattern/save/dd/mmmmm/yyyy] id=pattern-remove-2-alt-4-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/metadata/date-format-pattern/save/dd/mmyyyy] id=pattern-remove-3-Link\n" +
                        "                  \"yyyy\" [#/1/Spreadsheet123/metadata/date-format-pattern/save/dd/mm/] id=pattern-remove-4-Link\n" +
                        "                      \"yy\" [/1/Spreadsheet123/metadata/date-format-pattern/save/dd/mm/yy] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                      \"yyyy\" [/1/Spreadsheet123/metadata/date-format-pattern/save/dd/mm/yyyy] id=pattern-remove-4-alt-1-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [dd/mm/yyyy] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/metadata/date-format-pattern/save/dd/mm/yyyy] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    @Test
    public void testMetadataParsePatternDate() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/metadata/date-parse-pattern")
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
                        "    Pattern\n" +
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
                        "                  \"dd/mm/yyyy\" [#/1/Spreadsheet123/metadata/date-parse-pattern/save/dd/mm/yyyy] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/1999\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Short\"\n" +
                        "                  \"d/m/yy\" [#/1/Spreadsheet123/metadata/date-parse-pattern/save/d/m/yy] id=pattern-today short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Medium\"\n" +
                        "                  \"d mmm yyyy\" [#/1/Spreadsheet123/metadata/date-parse-pattern/save/d%20mmm%20yyyy] id=pattern-today medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Long\"\n" +
                        "                  \"d mmmm yyyy\" [#/1/Spreadsheet123/metadata/date-parse-pattern/save/d%20mmmm%20yyyy] id=pattern-today long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Full\"\n" +
                        "                  \"dddd, d mmmm yyyy\" [#/1/Spreadsheet123/metadata/date-parse-pattern/save/dddd,%20d%20mmmm%20yyyy] id=pattern-today full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999\n" +
                        "                ROW 5\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Short\"\n" +
                        "                  \"d/m/yy\" [#/1/Spreadsheet123/metadata/date-parse-pattern/save/d/m/yy] id=pattern-31 december 1999 short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99\n" +
                        "                ROW 6\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Medium\"\n" +
                        "                  \"d mmm yyyy\" [#/1/Spreadsheet123/metadata/date-parse-pattern/save/d%20mmm%20yyyy] id=pattern-31 december 1999 medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999\n" +
                        "                ROW 7\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Long\"\n" +
                        "                  \"d mmmm yyyy\" [#/1/Spreadsheet123/metadata/date-parse-pattern/save/d%20mmmm%20yyyy] id=pattern-31 december 1999 long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999\n" +
                        "                ROW 8\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 Full\"\n" +
                        "                  \"dddd, d mmmm yyyy\" [#/1/Spreadsheet123/metadata/date-parse-pattern/save/dddd,%20d%20mmmm%20yyyy] id=pattern-31 december 1999 full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"dd\" [#/1/Spreadsheet123/metadata/date-parse-pattern/save//mm/yyyy] id=pattern-remove-0-Link\n" +
                        "                      \"d\" [/1/Spreadsheet123/metadata/date-parse-pattern/save/d/mm/yyyy] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                      \"dd\" [/1/Spreadsheet123/metadata/date-parse-pattern/save/dd/mm/yyyy] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                      \"ddd\" [/1/Spreadsheet123/metadata/date-parse-pattern/save/ddd/mm/yyyy] id=pattern-remove-0-alt-2-MenuItem\n" +
                        "                      \"dddd\" [/1/Spreadsheet123/metadata/date-parse-pattern/save/dddd/mm/yyyy] id=pattern-remove-0-alt-3-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/metadata/date-parse-pattern/save/ddmm/yyyy] id=pattern-remove-1-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/metadata/date-parse-pattern/save/dd//yyyy] id=pattern-remove-2-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/metadata/date-parse-pattern/save/dd/m/yyyy] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/metadata/date-parse-pattern/save/dd/mm/yyyy] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "                      \"mmm\" [/1/Spreadsheet123/metadata/date-parse-pattern/save/dd/mmm/yyyy] id=pattern-remove-2-alt-2-MenuItem\n" +
                        "                      \"mmmm\" [/1/Spreadsheet123/metadata/date-parse-pattern/save/dd/mmmm/yyyy] id=pattern-remove-2-alt-3-MenuItem\n" +
                        "                      \"mmmmm\" [/1/Spreadsheet123/metadata/date-parse-pattern/save/dd/mmmmm/yyyy] id=pattern-remove-2-alt-4-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/metadata/date-parse-pattern/save/dd/mmyyyy] id=pattern-remove-3-Link\n" +
                        "                  \"yyyy\" [#/1/Spreadsheet123/metadata/date-parse-pattern/save/dd/mm/] id=pattern-remove-4-Link\n" +
                        "                      \"yy\" [/1/Spreadsheet123/metadata/date-parse-pattern/save/dd/mm/yy] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                      \"yyyy\" [/1/Spreadsheet123/metadata/date-parse-pattern/save/dd/mm/yyyy] id=pattern-remove-4-alt-1-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [dd/mm/yyyy] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/metadata/date-parse-pattern/save/dd/mm/yyyy] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    // metadata / date-time.............................................................................................

    @Test
    public void testMetadataFormatPatternDateTime() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/metadata/date-time-format-pattern")
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
                        "    Pattern\n" +
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
                        "                  \"dd/mm/yyyy hh:mm:ss\" [#/1/Spreadsheet123/metadata/date-time-format-pattern/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/1999 12:58:59\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Short\"\n" +
                        "                  \"d/m/yy, h:mm AM/PM\" [#/1/Spreadsheet123/metadata/date-time-format-pattern/save/d/m/yy,%20h:mm%20AM/PM] id=pattern-today short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99, 12:58 PM\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Medium\"\n" +
                        "                  \"d mmm yyyy, h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/date-time-format-pattern/save/d%20mmm%20yyyy,%20h:mm:ss%20AM/PM] id=pattern-today medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999, 12:58:59 PM\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Long\"\n" +
                        "                  \"d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/date-time-format-pattern/save/d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-today long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999 at 12:58:59 PM\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Full\"\n" +
                        "                  \"dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/date-time-format-pattern/save/dddd,%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-today full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999 at 12:58:59 PM\n" +
                        "                ROW 5\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Short\"\n" +
                        "                  \"d/m/yy, h:mm AM/PM\" [#/1/Spreadsheet123/metadata/date-time-format-pattern/save/d/m/yy,%20h:mm%20AM/PM] id=pattern-31 december 1999 12:58:59 short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99, 12:58 PM\n" +
                        "                ROW 6\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Medium\"\n" +
                        "                  \"d mmm yyyy, h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/date-time-format-pattern/save/d%20mmm%20yyyy,%20h:mm:ss%20AM/PM] id=pattern-31 december 1999 12:58:59 medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999, 12:58:59 PM\n" +
                        "                ROW 7\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Long\"\n" +
                        "                  \"d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/date-time-format-pattern/save/d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-31 december 1999 12:58:59 long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999 at 12:58:59 PM\n" +
                        "                ROW 8\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Full\"\n" +
                        "                  \"dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/date-time-format-pattern/save/dddd,%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-31 december 1999 12:58:59 full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999 at 12:58:59 PM\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"dd\" [#/1/Spreadsheet123/metadata/date-time-format-pattern/save//mm/yyyy%20hh:mm:ss] id=pattern-remove-0-Link\n" +
                        "                      \"d\" [/1/Spreadsheet123/metadata/date-time-format-pattern/save/d/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                      \"dd\" [/1/Spreadsheet123/metadata/date-time-format-pattern/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                      \"ddd\" [/1/Spreadsheet123/metadata/date-time-format-pattern/save/ddd/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-2-MenuItem\n" +
                        "                      \"dddd\" [/1/Spreadsheet123/metadata/date-time-format-pattern/save/dddd/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-3-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/metadata/date-time-format-pattern/save/ddmm/yyyy%20hh:mm:ss] id=pattern-remove-1-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/metadata/date-time-format-pattern/save/dd//yyyy%20hh:mm:ss] id=pattern-remove-2-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/metadata/date-time-format-pattern/save/dd/m/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/metadata/date-time-format-pattern/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "                      \"mmm\" [/1/Spreadsheet123/metadata/date-time-format-pattern/save/dd/mmm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-2-MenuItem\n" +
                        "                      \"mmmm\" [/1/Spreadsheet123/metadata/date-time-format-pattern/save/dd/mmmm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-3-MenuItem\n" +
                        "                      \"mmmmm\" [/1/Spreadsheet123/metadata/date-time-format-pattern/save/dd/mmmmm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-4-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/metadata/date-time-format-pattern/save/dd/mmyyyy%20hh:mm:ss] id=pattern-remove-3-Link\n" +
                        "                  \"yyyy\" [#/1/Spreadsheet123/metadata/date-time-format-pattern/save/dd/mm/%20hh:mm:ss] id=pattern-remove-4-Link\n" +
                        "                      \"yy\" [/1/Spreadsheet123/metadata/date-time-format-pattern/save/dd/mm/yy%20hh:mm:ss] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                      \"yyyy\" [/1/Spreadsheet123/metadata/date-time-format-pattern/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-4-alt-1-MenuItem\n" +
                        "                  \" \" [#/1/Spreadsheet123/metadata/date-time-format-pattern/save/dd/mm/yyyyhh:mm:ss] id=pattern-remove-5-Link\n" +
                        "                  \"hh\" [#/1/Spreadsheet123/metadata/date-time-format-pattern/save/dd/mm/yyyy%20:mm:ss] id=pattern-remove-6-Link\n" +
                        "                      \"h\" [/1/Spreadsheet123/metadata/date-time-format-pattern/save/dd/mm/yyyy%20h:mm:ss] id=pattern-remove-6-alt-0-MenuItem\n" +
                        "                      \"hh\" [/1/Spreadsheet123/metadata/date-time-format-pattern/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-6-alt-1-MenuItem\n" +
                        "                  \":\" [#/1/Spreadsheet123/metadata/date-time-format-pattern/save/dd/mm/yyyy%20hhmm:ss] id=pattern-remove-7-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/metadata/date-time-format-pattern/save/dd/mm/yyyy%20hh::ss] id=pattern-remove-8-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/metadata/date-time-format-pattern/save/dd/mm/yyyy%20hh:m:ss] id=pattern-remove-8-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/metadata/date-time-format-pattern/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-8-alt-1-MenuItem\n" +
                        "                  \":\" [#/1/Spreadsheet123/metadata/date-time-format-pattern/save/dd/mm/yyyy%20hh:mmss] id=pattern-remove-9-Link\n" +
                        "                  \"ss\" [#/1/Spreadsheet123/metadata/date-time-format-pattern/save/dd/mm/yyyy%20hh:mm:] id=pattern-remove-10-Link\n" +
                        "                      \"s\" [/1/Spreadsheet123/metadata/date-time-format-pattern/save/dd/mm/yyyy%20hh:mm:s] id=pattern-remove-10-alt-0-MenuItem\n" +
                        "                      \"ss\" [/1/Spreadsheet123/metadata/date-time-format-pattern/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-10-alt-1-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [dd/mm/yyyy hh:mm:ss] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/metadata/date-time-format-pattern/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    @Test
    public void testMetadataParsePatternDateTime() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/metadata/date-time-parse-pattern")
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
                        "    Pattern\n" +
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
                        "                  \"dd/mm/yyyy hh:mm:ss\" [#/1/Spreadsheet123/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/1999 12:58:59\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Short\"\n" +
                        "                  \"d/m/yy, h:mm AM/PM\" [#/1/Spreadsheet123/metadata/date-time-parse-pattern/save/d/m/yy,%20h:mm%20AM/PM] id=pattern-today short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99, 12:58 PM\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Medium\"\n" +
                        "                  \"d mmm yyyy, h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/date-time-parse-pattern/save/d%20mmm%20yyyy,%20h:mm:ss%20AM/PM] id=pattern-today medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999, 12:58:59 PM\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Long\"\n" +
                        "                  \"d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/date-time-parse-pattern/save/d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-today long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999 at 12:58:59 PM\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Today Full\"\n" +
                        "                  \"dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/date-time-parse-pattern/save/dddd,%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-today full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999 at 12:58:59 PM\n" +
                        "                ROW 5\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Short\"\n" +
                        "                  \"d/m/yy, h:mm AM/PM\" [#/1/Spreadsheet123/metadata/date-time-parse-pattern/save/d/m/yy,%20h:mm%20AM/PM] id=pattern-31 december 1999 12:58:59 short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99, 12:58 PM\n" +
                        "                ROW 6\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Medium\"\n" +
                        "                  \"d mmm yyyy, h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/date-time-parse-pattern/save/d%20mmm%20yyyy,%20h:mm:ss%20AM/PM] id=pattern-31 december 1999 12:58:59 medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999, 12:58:59 PM\n" +
                        "                ROW 7\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Long\"\n" +
                        "                  \"d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/date-time-parse-pattern/save/d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-31 december 1999 12:58:59 long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999 at 12:58:59 PM\n" +
                        "                ROW 8\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31 December 1999 12:58:59 Full\"\n" +
                        "                  \"dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/date-time-parse-pattern/save/dddd,%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=pattern-31 december 1999 12:58:59 full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999 at 12:58:59 PM\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"dd\" [#/1/Spreadsheet123/metadata/date-time-parse-pattern/save//mm/yyyy%20hh:mm:ss] id=pattern-remove-0-Link\n" +
                        "                      \"d\" [/1/Spreadsheet123/metadata/date-time-parse-pattern/save/d/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                      \"dd\" [/1/Spreadsheet123/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                      \"ddd\" [/1/Spreadsheet123/metadata/date-time-parse-pattern/save/ddd/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-2-MenuItem\n" +
                        "                      \"dddd\" [/1/Spreadsheet123/metadata/date-time-parse-pattern/save/dddd/mm/yyyy%20hh:mm:ss] id=pattern-remove-0-alt-3-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/metadata/date-time-parse-pattern/save/ddmm/yyyy%20hh:mm:ss] id=pattern-remove-1-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/metadata/date-time-parse-pattern/save/dd//yyyy%20hh:mm:ss] id=pattern-remove-2-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/metadata/date-time-parse-pattern/save/dd/m/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "                      \"mmm\" [/1/Spreadsheet123/metadata/date-time-parse-pattern/save/dd/mmm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-2-MenuItem\n" +
                        "                      \"mmmm\" [/1/Spreadsheet123/metadata/date-time-parse-pattern/save/dd/mmmm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-3-MenuItem\n" +
                        "                      \"mmmmm\" [/1/Spreadsheet123/metadata/date-time-parse-pattern/save/dd/mmmmm/yyyy%20hh:mm:ss] id=pattern-remove-2-alt-4-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/metadata/date-time-parse-pattern/save/dd/mmyyyy%20hh:mm:ss] id=pattern-remove-3-Link\n" +
                        "                  \"yyyy\" [#/1/Spreadsheet123/metadata/date-time-parse-pattern/save/dd/mm/%20hh:mm:ss] id=pattern-remove-4-Link\n" +
                        "                      \"yy\" [/1/Spreadsheet123/metadata/date-time-parse-pattern/save/dd/mm/yy%20hh:mm:ss] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                      \"yyyy\" [/1/Spreadsheet123/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-4-alt-1-MenuItem\n" +
                        "                  \" \" [#/1/Spreadsheet123/metadata/date-time-parse-pattern/save/dd/mm/yyyyhh:mm:ss] id=pattern-remove-5-Link\n" +
                        "                  \"hh\" [#/1/Spreadsheet123/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20:mm:ss] id=pattern-remove-6-Link\n" +
                        "                      \"h\" [/1/Spreadsheet123/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20h:mm:ss] id=pattern-remove-6-alt-0-MenuItem\n" +
                        "                      \"hh\" [/1/Spreadsheet123/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-6-alt-1-MenuItem\n" +
                        "                  \":\" [#/1/Spreadsheet123/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hhmm:ss] id=pattern-remove-7-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hh::ss] id=pattern-remove-8-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hh:m:ss] id=pattern-remove-8-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-8-alt-1-MenuItem\n" +
                        "                  \":\" [#/1/Spreadsheet123/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hh:mmss] id=pattern-remove-9-Link\n" +
                        "                  \"ss\" [#/1/Spreadsheet123/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hh:mm:] id=pattern-remove-10-Link\n" +
                        "                      \"s\" [/1/Spreadsheet123/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hh:mm:s] id=pattern-remove-10-alt-0-MenuItem\n" +
                        "                      \"ss\" [/1/Spreadsheet123/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-remove-10-alt-1-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [dd/mm/yyyy hh:mm:ss] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hh:mm:ss] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    // metadata / number................................................................................................

    @Test
    public void testMetadataFormatPatternNumber() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/metadata/number-format-pattern")
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
                        "    Pattern\n" +
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
                        "                  \"$#0.00\" [#/1/Spreadsheet123/metadata/number-format-pattern/save/$%230.00] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $1234.56\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $-9876.54\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $0.00\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"General\"\n" +
                        "                  \"General\" [#/1/Spreadsheet123/metadata/number-format-pattern/save/General] id=pattern-general-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    1234.56\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -9876.54\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Number\"\n" +
                        "                  \"#,##0.###\" [#/1/Spreadsheet123/metadata/number-format-pattern/save/%23,%23%230.%23%23%23] id=pattern-number-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    1,234.56\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -9,876.54\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0.\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Integer\"\n" +
                        "                  \"#,##0\" [#/1/Spreadsheet123/metadata/number-format-pattern/save/%23,%23%230] id=pattern-integer-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    1,235\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -9,877\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Percent\"\n" +
                        "                  \"#,##0%\" [#/1/Spreadsheet123/metadata/number-format-pattern/save/%23,%23%230%25] id=pattern-percent-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    123,456%\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -987,654%\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0%\n" +
                        "                ROW 5\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Currency\"\n" +
                        "                  \"$#,##0.00\" [#/1/Spreadsheet123/metadata/number-format-pattern/save/$%23,%23%230.00] id=pattern-currency-Link\n" +
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
                        "                  \"$\" [#/1/Spreadsheet123/metadata/number-format-pattern/save/%230.00] id=pattern-remove-0-Link\n" +
                        "                      \"$\" [/1/Spreadsheet123/metadata/number-format-pattern/save/$%230.00] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                  \"#\" [#/1/Spreadsheet123/metadata/number-format-pattern/save/$0.00] id=pattern-remove-1-Link\n" +
                        "                      \"#\" [/1/Spreadsheet123/metadata/number-format-pattern/save/$%230.00] id=pattern-remove-1-alt-0-MenuItem\n" +
                        "                  \"0\" [#/1/Spreadsheet123/metadata/number-format-pattern/save/$%23.00] id=pattern-remove-2-Link\n" +
                        "                      \"0\" [/1/Spreadsheet123/metadata/number-format-pattern/save/$%230.00] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                  \".\" [#/1/Spreadsheet123/metadata/number-format-pattern/save/$%23000] id=pattern-remove-3-Link\n" +
                        "                      \".\" [/1/Spreadsheet123/metadata/number-format-pattern/save/$%230.00] id=pattern-remove-3-alt-0-MenuItem\n" +
                        "                  \"0\" [#/1/Spreadsheet123/metadata/number-format-pattern/save/$%230.0] id=pattern-remove-4-Link\n" +
                        "                      \"0\" [/1/Spreadsheet123/metadata/number-format-pattern/save/$%230.00] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                  \"0\" [#/1/Spreadsheet123/metadata/number-format-pattern/save/$%230.0] id=pattern-remove-5-Link\n" +
                        "                      \"0\" [/1/Spreadsheet123/metadata/number-format-pattern/save/$%230.00] id=pattern-remove-5-alt-0-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [$#0.00] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/metadata/number-format-pattern/save/$%230.00] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    @Test
    public void testMetadataParsePatternNumber() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/metadata/number-parse-pattern")
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
                        "    Pattern\n" +
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
                        "                  \"$#0.00\" [#/1/Spreadsheet123/metadata/number-parse-pattern/save/$%230.00] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $1234.56\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $-9876.54\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $0.00\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Number\"\n" +
                        "                  \"#,##0.###\" [#/1/Spreadsheet123/metadata/number-parse-pattern/save/%23,%23%230.%23%23%23] id=pattern-number-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    1,234.56\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -9,876.54\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0.\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Integer\"\n" +
                        "                  \"#,##0\" [#/1/Spreadsheet123/metadata/number-parse-pattern/save/%23,%23%230] id=pattern-integer-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    1,235\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -9,877\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Percent\"\n" +
                        "                  \"#,##0%\" [#/1/Spreadsheet123/metadata/number-parse-pattern/save/%23,%23%230%25] id=pattern-percent-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    123,456%\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -987,654%\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0%\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Currency\"\n" +
                        "                  \"$#,##0.00\" [#/1/Spreadsheet123/metadata/number-parse-pattern/save/$%23,%23%230.00] id=pattern-currency-Link\n" +
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
                        "                  \"$\" [#/1/Spreadsheet123/metadata/number-parse-pattern/save/%230.00] id=pattern-remove-0-Link\n" +
                        "                      \"$\" [/1/Spreadsheet123/metadata/number-parse-pattern/save/$%230.00] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                  \"#\" [#/1/Spreadsheet123/metadata/number-parse-pattern/save/$0.00] id=pattern-remove-1-Link\n" +
                        "                      \"#\" [/1/Spreadsheet123/metadata/number-parse-pattern/save/$%230.00] id=pattern-remove-1-alt-0-MenuItem\n" +
                        "                  \"0\" [#/1/Spreadsheet123/metadata/number-parse-pattern/save/$%23.00] id=pattern-remove-2-Link\n" +
                        "                      \"0\" [/1/Spreadsheet123/metadata/number-parse-pattern/save/$%230.00] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                  \".\" [#/1/Spreadsheet123/metadata/number-parse-pattern/save/$%23000] id=pattern-remove-3-Link\n" +
                        "                      \".\" [/1/Spreadsheet123/metadata/number-parse-pattern/save/$%230.00] id=pattern-remove-3-alt-0-MenuItem\n" +
                        "                  \"0\" [#/1/Spreadsheet123/metadata/number-parse-pattern/save/$%230.0] id=pattern-remove-4-Link\n" +
                        "                      \"0\" [/1/Spreadsheet123/metadata/number-parse-pattern/save/$%230.00] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                  \"0\" [#/1/Spreadsheet123/metadata/number-parse-pattern/save/$%230.0] id=pattern-remove-5-Link\n" +
                        "                      \"0\" [/1/Spreadsheet123/metadata/number-parse-pattern/save/$%230.00] id=pattern-remove-5-alt-0-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [$#0.00] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/metadata/number-parse-pattern/save/$%230.00] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    // metadata / text..................................................................................................

    @Test
    public void testMetadataFormatPatternText() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/metadata/text-format-pattern")
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
                        "    Pattern\n" +
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
                        "                  \"@ \"Hello\"\" [#/1/Spreadsheet123/metadata/text-format-pattern/save/@%20%22Hello%22] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Abc123 Hello\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Default text format\"\n" +
                        "                  \"@\" [#/1/Spreadsheet123/metadata/text-format-pattern/save/@] id=pattern-default text format-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Abc123\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"@\" [#/1/Spreadsheet123/metadata/text-format-pattern/save/%20%22Hello%22] id=pattern-remove-0-Link\n" +
                        "                      \"@\" [/1/Spreadsheet123/metadata/text-format-pattern/save/@%20%22Hello%22] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                  \" \" [#/1/Spreadsheet123/metadata/text-format-pattern/save/@%22Hello%22] id=pattern-remove-1-Link\n" +
                        "                  \"\"Hello\"\" [#/1/Spreadsheet123/metadata/text-format-pattern/save/@%20] id=pattern-remove-2-Link\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [@ \"Hello\"] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/metadata/text-format-pattern/save/@%20%22Hello%22] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    // metadata / time..................................................................................................

    @Test
    public void testMetadataFormatPatternTime() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/metadata/time-format-pattern")
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
                        "    Pattern\n" +
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
                        "                  \"hh:mm\" [#/1/Spreadsheet123/metadata/time-format-pattern/save/hh:mm] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Now Short\"\n" +
                        "                  \"h:mm AM/PM\" [#/1/Spreadsheet123/metadata/time-format-pattern/save/h:mm%20AM/PM] id=pattern-now short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58 PM\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Now Long\"\n" +
                        "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/time-format-pattern/save/h:mm:ss%20AM/PM] id=pattern-now long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58:59 PM\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"12:58:59 AM Short\"\n" +
                        "                  \"h:mm AM/PM\" [#/1/Spreadsheet123/metadata/time-format-pattern/save/h:mm%20AM/PM] id=pattern-12:58:59 am short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58 PM\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"12:58:59 AM Long\"\n" +
                        "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/time-format-pattern/save/h:mm:ss%20AM/PM] id=pattern-12:58:59 am long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58:59 PM\n" +
                        "                ROW 5\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"6:01:02 PM Short\"\n" +
                        "                  \"h:mm AM/PM\" [#/1/Spreadsheet123/metadata/time-format-pattern/save/h:mm%20AM/PM] id=pattern-6:01:02 pm short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    6:01 PM\n" +
                        "                ROW 6\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"6:01:02 PM Long\"\n" +
                        "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/time-format-pattern/save/h:mm:ss%20AM/PM] id=pattern-6:01:02 pm long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    6:01:02 PM\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"hh\" [#/1/Spreadsheet123/metadata/time-format-pattern/save/:mm] id=pattern-remove-0-Link\n" +
                        "                      \"h\" [/1/Spreadsheet123/metadata/time-format-pattern/save/h:mm] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                      \"hh\" [/1/Spreadsheet123/metadata/time-format-pattern/save/hh:mm] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                  \":\" [#/1/Spreadsheet123/metadata/time-format-pattern/save/hhmm] id=pattern-remove-1-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/metadata/time-format-pattern/save/hh:] id=pattern-remove-2-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/metadata/time-format-pattern/save/hh:m] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/metadata/time-format-pattern/save/hh:mm] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [hh:mm] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/metadata/time-format-pattern/save/hh:mm] id=pattern-save-Link\n" +
                        "          \"Undo\" DISABLED id=pattern-undo-Link\n" +
                        "          \"Clear\" DISABLED id=pattern-clear-Link\n" +
                        "          \"Close\" DISABLED id=pattern-close-Link\n"
        );
    }

    @Test
    public void testMetadataParsePatternTime() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/metadata/time-parse-pattern")
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
                        "    Pattern\n" +
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
                        "                  \"hh:mm\" [#/1/Spreadsheet123/metadata/time-parse-pattern/save/hh:mm] id=pattern-edit pattern-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Now Short\"\n" +
                        "                  \"h:mm AM/PM\" [#/1/Spreadsheet123/metadata/time-parse-pattern/save/h:mm%20AM/PM] id=pattern-now short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58 PM\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"Now Long\"\n" +
                        "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/time-parse-pattern/save/h:mm:ss%20AM/PM] id=pattern-now long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58:59 PM\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"12:58:59 AM Short\"\n" +
                        "                  \"h:mm AM/PM\" [#/1/Spreadsheet123/metadata/time-parse-pattern/save/h:mm%20AM/PM] id=pattern-12:58:59 am short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58 PM\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"12:58:59 AM Long\"\n" +
                        "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/time-parse-pattern/save/h:mm:ss%20AM/PM] id=pattern-12:58:59 am long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58:59 PM\n" +
                        "                ROW 5\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"6:01:02 PM Short\"\n" +
                        "                  \"h:mm AM/PM\" [#/1/Spreadsheet123/metadata/time-parse-pattern/save/h:mm%20AM/PM] id=pattern-6:01:02 pm short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    6:01 PM\n" +
                        "                ROW 6\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"6:01:02 PM Long\"\n" +
                        "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/time-parse-pattern/save/h:mm:ss%20AM/PM] id=pattern-6:01:02 pm long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    6:01:02 PM\n" +
                        "      SpreadsheetPatternComponentElementRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove individual component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"hh\" [#/1/Spreadsheet123/metadata/time-parse-pattern/save/:mm] id=pattern-remove-0-Link\n" +
                        "                      \"h\" [/1/Spreadsheet123/metadata/time-parse-pattern/save/h:mm] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                      \"hh\" [/1/Spreadsheet123/metadata/time-parse-pattern/save/hh:mm] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                  \":\" [#/1/Spreadsheet123/metadata/time-parse-pattern/save/hhmm] id=pattern-remove-1-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/metadata/time-parse-pattern/save/hh:] id=pattern-remove-2-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/metadata/time-parse-pattern/save/hh:m] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                      \"mm\" [/1/Spreadsheet123/metadata/time-parse-pattern/save/hh:mm] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [hh:mm] id=pattern-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/metadata/time-parse-pattern/save/hh:mm] id=pattern-save-Link\n" +
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
