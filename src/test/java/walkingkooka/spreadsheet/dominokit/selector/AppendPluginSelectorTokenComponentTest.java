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

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterName;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelectorToken;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelectorTokenAlternative;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AppendPluginSelectorTokenComponentTest implements HtmlComponentTesting<AppendPluginSelectorTokenComponent<
    SpreadsheetFormatterSelectorToken,
    SpreadsheetFormatterSelectorTokenAlternative>,
    HTMLDivElement> {

    @Test
    public void testEmptyWithNullPrefixFails() {
        assertThrows(
            NullPointerException.class,
            () -> AppendPluginSelectorTokenComponent.empty(null)
        );
    }

    @Test
    public void testEmptyWithEmptyPrefixFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> AppendPluginSelectorTokenComponent.empty("")
        );
    }

    // refresh..........................................................................................................

    @Test
    public void testRefreshNoTextComponentsNoAlternatives() {
        this.refreshAndCheck(
            "/1/Untitled/cell/A1/formatter",
            SpreadsheetFormatterName.DATE_FORMAT_PATTERN,
            Lists.empty(), // textComponents
            Lists.empty(), // append
            "AppendPluginSelectorTokenComponent\n" // expected
        );
    }

    @Test
    public void testRefreshDateFormatEmptyTextComponents() {
        this.refreshAndCheck(
            "/1/Untitled/cell/A1/formatter",
            SpreadsheetFormatterName.DATE_FORMAT_PATTERN,
            Lists.empty(), // textComponents
            Lists.of(
                SpreadsheetFormatterSelectorTokenAlternative.with(
                    "d",
                    "d"
                ),
                SpreadsheetFormatterSelectorTokenAlternative.with(
                    "m",
                    "m"
                )
            ),
            "AppendPluginSelectorTokenComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      Append component(s)\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"d\" [#/1/Untitled/cell/A1/formatter/save/date-format-pattern%20d] id=id123-append-0-Link\n" +
                "            \"m\" [#/1/Untitled/cell/A1/formatter/save/date-format-pattern%20m] id=id123-append-1-Link\n" // expected
        );
    }

    @Test
    public void testRefreshDateFormatTextComponentsAndAlternatives() {
        this.refreshAndCheck(
            "/1/Untitled/cell/A1/formatter",
            SpreadsheetFormatterName.DATE_FORMAT_PATTERN,
            Lists.of(
                SpreadsheetFormatterSelectorToken.with(
                    "d",
                    "d",
                    SpreadsheetFormatterSelectorToken.NO_ALTERNATIVES
                ),
                SpreadsheetFormatterSelectorToken.with(
                    "m",
                    "m",
                    SpreadsheetFormatterSelectorToken.NO_ALTERNATIVES
                ),
                SpreadsheetFormatterSelectorToken.with(
                    "yy",
                    "yy",
                    SpreadsheetFormatterSelectorToken.NO_ALTERNATIVES
                )
            ), // textComponents
            Lists.of(
                SpreadsheetFormatterSelectorTokenAlternative.with(
                    "d",
                    "d"
                ),
                SpreadsheetFormatterSelectorTokenAlternative.with(
                    "m",
                    "m"
                )
            ),
            "AppendPluginSelectorTokenComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      Append component(s)\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"d\" [#/1/Untitled/cell/A1/formatter/save/date-format-pattern%20dmyyd] id=id123-append-0-Link\n" +
                "            \"m\" [#/1/Untitled/cell/A1/formatter/save/date-format-pattern%20dmyym] id=id123-append-1-Link\n" // expected
        );
    }

    @Test
    public void testRefreshDateTimeFormat() {
        this.refreshAndCheck(
            "/1/Untitled/cell/A1/formatter",
            SpreadsheetFormatterName.DATE_TIME_FORMAT_PATTERN,
            Lists.of(
                SpreadsheetFormatterSelectorToken.with(
                    "d",
                    "d",
                    SpreadsheetFormatterSelectorToken.NO_ALTERNATIVES
                ),
                SpreadsheetFormatterSelectorToken.with(
                    "m",
                    "m",
                    SpreadsheetFormatterSelectorToken.NO_ALTERNATIVES
                ),
                SpreadsheetFormatterSelectorToken.with(
                    "yy",
                    "yy",
                    SpreadsheetFormatterSelectorToken.NO_ALTERNATIVES
                )
            ), // textComponents
            Lists.of(
                SpreadsheetFormatterSelectorTokenAlternative.with(
                    "d",
                    "d"
                ),
                SpreadsheetFormatterSelectorTokenAlternative.with(
                    "m",
                    "m"
                )
            ),
            "AppendPluginSelectorTokenComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      Append component(s)\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"d\" [#/1/Untitled/cell/A1/formatter/save/date-time-format-pattern%20dmyyd] id=id123-append-0-Link\n" +
                "            \"m\" [#/1/Untitled/cell/A1/formatter/save/date-time-format-pattern%20dmyym] id=id123-append-1-Link\n" // expected
        );
    }

    @Test
    public void testRefreshNumberFormat() {
        this.refreshAndCheck(
            "/1/Untitled/cell/A1/formatter",
            SpreadsheetFormatterName.NUMBER_FORMAT_PATTERN,
            Lists.of(
                SpreadsheetFormatterSelectorToken.with(
                    "$",
                    "$",
                    SpreadsheetFormatterSelectorToken.NO_ALTERNATIVES
                )
            ), // textComponents
            Lists.of(
                SpreadsheetFormatterSelectorTokenAlternative.with(
                    "0",
                    "0"
                ),
                SpreadsheetFormatterSelectorTokenAlternative.with(
                    "#",
                    "#"
                )
            ),
            "AppendPluginSelectorTokenComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      Append component(s)\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"0\" [#/1/Untitled/cell/A1/formatter/save/number-format-pattern%20$0] id=id123-append-0-Link\n" +
                "            \"#\" [#/1/Untitled/cell/A1/formatter/save/number-format-pattern%20$%23] id=id123-append-1-Link\n" // expected
        );
    }

    @Test
    public void testRefreshTextFormat() {
        this.refreshAndCheck(
            "/1/Untitled/cell/A1/formatter",
            SpreadsheetFormatterName.TEXT_FORMAT_PATTERN,
            Lists.of(
                SpreadsheetFormatterSelectorToken.with(
                    "@",
                    "@",
                    SpreadsheetFormatterSelectorToken.NO_ALTERNATIVES
                )
            ), // textComponents
            Lists.of(
                SpreadsheetFormatterSelectorTokenAlternative.with(
                    "@",
                    "@"
                ),
                SpreadsheetFormatterSelectorTokenAlternative.with(
                    "_ ",
                    "_ "
                )
            ),
            "AppendPluginSelectorTokenComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      Append component(s)\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"@\" [#/1/Untitled/cell/A1/formatter/save/text-format-pattern%20@@] id=id123-append-0-Link\n" +
                "            \"_ \" [#/1/Untitled/cell/A1/formatter/save/text-format-pattern%20@_%20] id=id123-append-1-Link\n" // expected
        );
    }

    @Test
    public void testRefreshTimeFormat() {
        this.refreshAndCheck(
            "/1/Untitled/cell/A1/formatter",
            SpreadsheetFormatterName.TIME_FORMAT_PATTERN,
            Lists.of(
                SpreadsheetFormatterSelectorToken.with(
                    "h",
                    "h",
                    SpreadsheetFormatterSelectorToken.NO_ALTERNATIVES
                )
            ), // textComponents
            Lists.of(
                SpreadsheetFormatterSelectorTokenAlternative.with(
                    "m",
                    "m"
                )
            ),
            "AppendPluginSelectorTokenComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      Append component(s)\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"m\" [#/1/Untitled/cell/A1/formatter/save/time-format-pattern%20hm] id=id123-append-0-Link\n" // expected
        );
    }

    private void refreshAndCheck(final String historyToken,
                                 final SpreadsheetFormatterName formatterName,
                                 final List<SpreadsheetFormatterSelectorToken> textComponents,
                                 final List<SpreadsheetFormatterSelectorTokenAlternative> append,
                                 final String expected) {
        final AppendPluginSelectorTokenComponent<SpreadsheetFormatterSelectorToken, SpreadsheetFormatterSelectorTokenAlternative> component = AppendPluginSelectorTokenComponent.empty("id123-");
        component.refresh(
            textComponents,
            append,
            new FakeAppendPluginSelectorTokenComponentContext() {

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
    public Class<AppendPluginSelectorTokenComponent<SpreadsheetFormatterSelectorToken, SpreadsheetFormatterSelectorTokenAlternative>> type() {
        return Cast.to(AppendPluginSelectorTokenComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
