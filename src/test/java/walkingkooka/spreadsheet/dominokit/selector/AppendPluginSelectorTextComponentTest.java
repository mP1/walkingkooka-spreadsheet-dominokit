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

package walkingkooka.spreadsheet.dominokit.selector;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterName;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelectorTextComponent;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelectorTextComponentAlternative;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AppendPluginSelectorTextComponentTest implements ClassTesting<AppendPluginSelectorTextComponent<SpreadsheetFormatterSelectorTextComponent, SpreadsheetFormatterSelectorTextComponentAlternative>>,
        TreePrintableTesting {

    @Test
    public void testEmptyWithNullPrefixFails() {
        assertThrows(
                NullPointerException.class,
                () -> AppendPluginSelectorTextComponent.empty(null)
        );
    }

    @Test
    public void testEmptyWithEmptyPrefixFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> AppendPluginSelectorTextComponent.empty("")
        );
    }

    // refresh..........................................................................................................

    @Test
    public void testRefreshNoTextComponentsNoAlternatives() {
        this.refreshAndCheck(
                "/1/Untitled/cell/A1/formatter/date",
                SpreadsheetFormatterName.DATE_FORMAT_PATTERN,
                Lists.empty(), // textComponents
                Lists.empty(), // append
                "AppendPluginSelectorTextComponent\n" // expected
        );
    }

    @Test
    public void testRefreshDateFormatEmptyTextComponents() {
        this.refreshAndCheck(
                "/1/Untitled/cell/A1/formatter/date",
                SpreadsheetFormatterName.DATE_FORMAT_PATTERN,
                Lists.empty(), // textComponents
                Lists.of(
                        SpreadsheetFormatterSelectorTextComponentAlternative.with(
                                "d",
                                "d"
                        ),
                        SpreadsheetFormatterSelectorTextComponentAlternative.with(
                                "m",
                                "m"
                        )
                ),
                "AppendPluginSelectorTextComponent\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Append component(s)\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"d\" [#/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20d] id=id123-append-0-Link\n" +
                        "            \"m\" [#/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20m] id=id123-append-1-Link\n" // expected
        );
    }

    @Test
    public void testRefreshDateFormatTextComponentsAndAlternatives() {
        this.refreshAndCheck(
                "/1/Untitled/cell/A1/formatter/date",
                SpreadsheetFormatterName.DATE_FORMAT_PATTERN,
                Lists.of(
                        SpreadsheetFormatterSelectorTextComponent.with(
                                "d",
                                "d",
                                SpreadsheetFormatterSelectorTextComponent.NO_ALTERNATIVES
                        ),
                        SpreadsheetFormatterSelectorTextComponent.with(
                                "m",
                                "m",
                                SpreadsheetFormatterSelectorTextComponent.NO_ALTERNATIVES
                        ),
                        SpreadsheetFormatterSelectorTextComponent.with(
                                "yy",
                                "yy",
                                SpreadsheetFormatterSelectorTextComponent.NO_ALTERNATIVES
                        )
                ), // textComponents
                Lists.of(
                        SpreadsheetFormatterSelectorTextComponentAlternative.with(
                                "d",
                                "d"
                        ),
                        SpreadsheetFormatterSelectorTextComponentAlternative.with(
                                "m",
                                "m"
                        )
                ),
                "AppendPluginSelectorTextComponent\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Append component(s)\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"d\" [#/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20dmyyd] id=id123-append-0-Link\n" +
                        "            \"m\" [#/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20dmyym] id=id123-append-1-Link\n" // expected
        );
    }

    @Test
    public void testRefreshDateTimeFormat() {
        this.refreshAndCheck(
                "/1/Untitled/cell/A1/formatter/date-time",
                SpreadsheetFormatterName.DATE_TIME_FORMAT_PATTERN,
                Lists.of(
                        SpreadsheetFormatterSelectorTextComponent.with(
                                "d",
                                "d",
                                SpreadsheetFormatterSelectorTextComponent.NO_ALTERNATIVES
                        ),
                        SpreadsheetFormatterSelectorTextComponent.with(
                                "m",
                                "m",
                                SpreadsheetFormatterSelectorTextComponent.NO_ALTERNATIVES
                        ),
                        SpreadsheetFormatterSelectorTextComponent.with(
                                "yy",
                                "yy",
                                SpreadsheetFormatterSelectorTextComponent.NO_ALTERNATIVES
                        )
                ), // textComponents
                Lists.of(
                        SpreadsheetFormatterSelectorTextComponentAlternative.with(
                                "d",
                                "d"
                        ),
                        SpreadsheetFormatterSelectorTextComponentAlternative.with(
                                "m",
                                "m"
                        )
                ),
                "AppendPluginSelectorTextComponent\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Append component(s)\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"d\" [#/1/Untitled/cell/A1/formatter/date-time/save/date-time-format-pattern%20dmyyd] id=id123-append-0-Link\n" +
                        "            \"m\" [#/1/Untitled/cell/A1/formatter/date-time/save/date-time-format-pattern%20dmyym] id=id123-append-1-Link\n" // expected
        );
    }

    @Test
    public void testRefreshNumberFormat() {
        this.refreshAndCheck(
                "/1/Untitled/cell/A1/formatter/number",
                SpreadsheetFormatterName.NUMBER_FORMAT_PATTERN,
                Lists.of(
                        SpreadsheetFormatterSelectorTextComponent.with(
                                "$",
                                "$",
                                SpreadsheetFormatterSelectorTextComponent.NO_ALTERNATIVES
                        )
                ), // textComponents
                Lists.of(
                        SpreadsheetFormatterSelectorTextComponentAlternative.with(
                                "0",
                                "0"
                        ),
                        SpreadsheetFormatterSelectorTextComponentAlternative.with(
                                "#",
                                "#"
                        )
                ),
                "AppendPluginSelectorTextComponent\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Append component(s)\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"0\" [#/1/Untitled/cell/A1/formatter/number/save/number-format-pattern%20$0] id=id123-append-0-Link\n" +
                        "            \"#\" [#/1/Untitled/cell/A1/formatter/number/save/number-format-pattern%20$%23] id=id123-append-1-Link\n" // expected
        );
    }

    @Test
    public void testRefreshTextFormat() {
        this.refreshAndCheck(
                "/1/Untitled/cell/A1/formatter/text",
                SpreadsheetFormatterName.TEXT_FORMAT_PATTERN,
                Lists.of(
                        SpreadsheetFormatterSelectorTextComponent.with(
                                "@",
                                "@",
                                SpreadsheetFormatterSelectorTextComponent.NO_ALTERNATIVES
                        )
                ), // textComponents
                Lists.of(
                        SpreadsheetFormatterSelectorTextComponentAlternative.with(
                                "@",
                                "@"
                        ),
                        SpreadsheetFormatterSelectorTextComponentAlternative.with(
                                "_ ",
                                "_ "
                        )
                ),
                "AppendPluginSelectorTextComponent\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Append component(s)\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"@\" [#/1/Untitled/cell/A1/formatter/text/save/text-format-pattern%20@@] id=id123-append-0-Link\n" +
                        "            \"_ \" [#/1/Untitled/cell/A1/formatter/text/save/text-format-pattern%20@_%20] id=id123-append-1-Link\n" // expected
        );
    }

    @Test
    public void testRefreshTimeFormat() {
        this.refreshAndCheck(
                "/1/Untitled/cell/A1/formatter/time",
                SpreadsheetFormatterName.TIME_FORMAT_PATTERN,
                Lists.of(
                        SpreadsheetFormatterSelectorTextComponent.with(
                                "h",
                                "h",
                                SpreadsheetFormatterSelectorTextComponent.NO_ALTERNATIVES
                        )
                ), // textComponents
                Lists.of(
                        SpreadsheetFormatterSelectorTextComponentAlternative.with(
                                "m",
                                "m"
                        )
                ),
                "AppendPluginSelectorTextComponent\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Append component(s)\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"m\" [#/1/Untitled/cell/A1/formatter/time/save/time-format-pattern%20hm] id=id123-append-0-Link\n" // expected
        );
    }

    private void refreshAndCheck(final String historyToken,
                                 final SpreadsheetFormatterName formatterName,
                                 final List<SpreadsheetFormatterSelectorTextComponent> textComponents,
                                 final List<SpreadsheetFormatterSelectorTextComponentAlternative> append,
                                 final String expected) {
        final AppendPluginSelectorTextComponent<SpreadsheetFormatterSelectorTextComponent, SpreadsheetFormatterSelectorTextComponentAlternative> component = AppendPluginSelectorTextComponent.empty("id123-");
        component.refresh(
                textComponents,
                append,
                new FakeAppendPluginSelectorTextComponentContext() {

                    @Override
                    public String saveText(final String text) {
                        return text.isEmpty() ?
                                "" :
                                formatterName + " " + text;
                    }

                    @Override
                    public HistoryToken historyToken() {
                        return HistoryToken.parseString(historyToken);
                    }
                }
        );

        this.treePrintAndCheck(
                component,
                expected
        );
    }

    // Class............................................................................................................

    @Override
    public Class<AppendPluginSelectorTextComponent<SpreadsheetFormatterSelectorTextComponent, SpreadsheetFormatterSelectorTextComponentAlternative>> type() {
        return Cast.to(AppendPluginSelectorTextComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
