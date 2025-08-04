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

public final class RemoveOrReplacePluginSelectorTokenComponentTest implements HtmlComponentTesting<RemoveOrReplacePluginSelectorTokenComponent<
    SpreadsheetFormatterSelectorToken,
    SpreadsheetFormatterSelectorTokenAlternative>,
    HTMLDivElement> {

    @Test
    public void testEmptyWithNullPrefixFails() {
        assertThrows(
            NullPointerException.class,
            () -> RemoveOrReplacePluginSelectorTokenComponent.empty(null)
        );
    }

    @Test
    public void testEmptyWithEmptyPrefixFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> RemoveOrReplacePluginSelectorTokenComponent.empty("")
        );
    }

    // refresh..........................................................................................................

    @Test
    public void testRefreshNoTextComponents() {
        this.refreshAndCheck(
            "/1/Untitled/cell/A1/formatter",
            SpreadsheetFormatterName.DATE_FORMAT_PATTERN,
            Lists.empty(), // textComponents
            "RemoveOrReplacePluginSelectorTokenComponent\n" // expected
        );
    }

    @Test
    public void testRefreshDateFormatTextComponents() {
        this.refreshAndCheck(
            "/1/Untitled/cell/A1/formatter",
            SpreadsheetFormatterName.DATE_FORMAT_PATTERN,
            Lists.of(
                SpreadsheetFormatterSelectorToken.with(
                    "d",
                    "d",
                    Lists.of(
                        SpreadsheetFormatterSelectorTokenAlternative.with(
                            "dd",
                            "dd"
                        ),
                        SpreadsheetFormatterSelectorTokenAlternative.with(
                            "ddd",
                            "ddd"
                        )
                    )
                ),
                SpreadsheetFormatterSelectorToken.with(
                    "m",
                    "m",
                    Lists.of(
                        SpreadsheetFormatterSelectorTokenAlternative.with(
                            "mm",
                            "mm"
                        ),
                        SpreadsheetFormatterSelectorTokenAlternative.with(
                            "mmm",
                            "mmm"
                        )
                    )
                ),
                SpreadsheetFormatterSelectorToken.with(
                    "yy",
                    "yy",
                    Lists.of(
                        SpreadsheetFormatterSelectorTokenAlternative.with(
                            "yyyy",
                            "yyyy"
                        )
                    )
                )
            ), // textComponents
            "RemoveOrReplacePluginSelectorTokenComponent\n" +
                "  SpreadsheetCard\n" +
                "    Card\n" +
                "      Remove / Replace component(s)\n" +
                "        SpreadsheetFlexLayout\n" +
                "          ROW\n" +
                "            \"d\" [#/1/Untitled/cell/A1/formatter/save/date-format-pattern%20myy] id=id123-remove-0-Link\n" +
                "                \"dd\" [/1/Untitled/cell/A1/formatter/save/date-format-pattern%20ddmyy] id=id123-remove-0-Link-replace-0-MenuItem\n" +
                "                \"ddd\" [/1/Untitled/cell/A1/formatter/save/date-format-pattern%20dddmyy] id=id123-remove-0-Link-replace-1-MenuItem\n" +
                "            \"m\" [#/1/Untitled/cell/A1/formatter/save/date-format-pattern%20dyy] id=id123-remove-1-Link\n" +
                "                \"mm\" [/1/Untitled/cell/A1/formatter/save/date-format-pattern%20dmmyy] id=id123-remove-1-Link-replace-0-MenuItem\n" +
                "                \"mmm\" [/1/Untitled/cell/A1/formatter/save/date-format-pattern%20dmmmyy] id=id123-remove-1-Link-replace-1-MenuItem\n" +
                "            \"yy\" [#/1/Untitled/cell/A1/formatter/save/date-format-pattern%20dm] id=id123-remove-2-Link\n" +
                "                \"yyyy\" [/1/Untitled/cell/A1/formatter/save/date-format-pattern%20dmyyyy] id=id123-remove-2-Link-replace-0-MenuItem\n" // expected
        );
    }

    @Test
    public void testRefreshDateTimeFormatTextComponents() {
        this.refreshAndCheck(
            "/1/Untitled/cell/A1/formatter",
            SpreadsheetFormatterName.DATE_TIME_FORMAT_PATTERN,
            Lists.of(
                SpreadsheetFormatterSelectorToken.with(
                    "m",
                    "m",
                    Lists.of(
                        SpreadsheetFormatterSelectorTokenAlternative.with(
                            "mm",
                            "mm"
                        ),
                        SpreadsheetFormatterSelectorTokenAlternative.with(
                            "mmm",
                            "mmm"
                        )
                    )
                ),
                SpreadsheetFormatterSelectorToken.with(
                    "yy",
                    "yy",
                    Lists.of(
                        SpreadsheetFormatterSelectorTokenAlternative.with(
                            "yyyy",
                            "yyyy"
                        )
                    )
                ),
                SpreadsheetFormatterSelectorToken.with(
                    "h",
                    "h",
                    Lists.of(
                        SpreadsheetFormatterSelectorTokenAlternative.with(
                            "hh",
                            "hh"
                        )
                    )
                ),
                SpreadsheetFormatterSelectorToken.with(
                    "m",
                    "m",
                    Lists.of(
                        SpreadsheetFormatterSelectorTokenAlternative.with(
                            "mm",
                            "mm"
                        )
                    )
                )
            ), // textComponents
            "RemoveOrReplacePluginSelectorTokenComponent\n" +
                "  SpreadsheetCard\n" +
                "    Card\n" +
                "      Remove / Replace component(s)\n" +
                "        SpreadsheetFlexLayout\n" +
                "          ROW\n" +
                "            \"m\" [#/1/Untitled/cell/A1/formatter/save/date-time-format-pattern%20yyhm] id=id123-remove-0-Link\n" +
                "                \"mm\" [/1/Untitled/cell/A1/formatter/save/date-time-format-pattern%20mmyyhm] id=id123-remove-0-Link-replace-0-MenuItem\n" +
                "                \"mmm\" [/1/Untitled/cell/A1/formatter/save/date-time-format-pattern%20mmmyyhm] id=id123-remove-0-Link-replace-1-MenuItem\n" +
                "            \"yy\" [#/1/Untitled/cell/A1/formatter/save/date-time-format-pattern%20mhm] id=id123-remove-1-Link\n" +
                "                \"yyyy\" [/1/Untitled/cell/A1/formatter/save/date-time-format-pattern%20myyyyhm] id=id123-remove-1-Link-replace-0-MenuItem\n" +
                "            \"h\" [#/1/Untitled/cell/A1/formatter/save/date-time-format-pattern%20myym] id=id123-remove-2-Link\n" +
                "                \"hh\" [/1/Untitled/cell/A1/formatter/save/date-time-format-pattern%20myyhhm] id=id123-remove-2-Link-replace-0-MenuItem\n" +
                "            \"m\" [#/1/Untitled/cell/A1/formatter/save/date-time-format-pattern%20myyh] id=id123-remove-3-Link\n" +
                "                \"mm\" [/1/Untitled/cell/A1/formatter/save/date-time-format-pattern%20myyhmm] id=id123-remove-3-Link-replace-0-MenuItem\n" // expected
        );
    }


    @Test
    public void testRefreshNumberFormatTextComponents() {
        this.refreshAndCheck(
            "/1/Untitled/cell/A1/formatter",
            SpreadsheetFormatterName.NUMBER_FORMAT_PATTERN,
            Lists.of(
                SpreadsheetFormatterSelectorToken.with(
                    "$",
                    "$",
                    SpreadsheetFormatterSelectorToken.NO_ALTERNATIVES
                ),
                SpreadsheetFormatterSelectorToken.with(
                    "0",
                    "0",
                    SpreadsheetFormatterSelectorToken.NO_ALTERNATIVES
                )
            ), // textComponents
            "RemoveOrReplacePluginSelectorTokenComponent\n" +
                "  SpreadsheetCard\n" +
                "    Card\n" +
                "      Remove / Replace component(s)\n" +
                "        SpreadsheetFlexLayout\n" +
                "          ROW\n" +
                "            \"$\" [#/1/Untitled/cell/A1/formatter/save/number-format-pattern%200] id=id123-remove-0-Link\n" +
                "            \"0\" [#/1/Untitled/cell/A1/formatter/save/number-format-pattern%20$] id=id123-remove-1-Link\n" // expected
        );
    }

    @Test
    public void testRefreshTextFormatTextComponents() {
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
            "RemoveOrReplacePluginSelectorTokenComponent\n" +
                "  SpreadsheetCard\n" +
                "    Card\n" +
                "      Remove / Replace component(s)\n" +
                "        SpreadsheetFlexLayout\n" +
                "          ROW\n" +
                "            \"@\" [#/1/Untitled/cell/A1/formatter/save/] id=id123-remove-0-Link\n"  // expected
        );
    }

    @Test
    public void testRefreshTextFormatTextComponents2() {
        this.refreshAndCheck(
            "/1/Untitled/cell/A1/formatter",
            SpreadsheetFormatterName.TEXT_FORMAT_PATTERN,
            Lists.of(
                SpreadsheetFormatterSelectorToken.with(
                    "@",
                    "@",
                    SpreadsheetFormatterSelectorToken.NO_ALTERNATIVES
                ),
                SpreadsheetFormatterSelectorToken.with(
                    "\"Hello\"",
                    "\"Hello\"",
                    SpreadsheetFormatterSelectorToken.NO_ALTERNATIVES
                )
            ), // textComponents
            "RemoveOrReplacePluginSelectorTokenComponent\n" +
                "  SpreadsheetCard\n" +
                "    Card\n" +
                "      Remove / Replace component(s)\n" +
                "        SpreadsheetFlexLayout\n" +
                "          ROW\n" +
                "            \"@\" [#/1/Untitled/cell/A1/formatter/save/text-format-pattern%20%22Hello%22] id=id123-remove-0-Link\n" +
                "            \"\"Hello\"\" [#/1/Untitled/cell/A1/formatter/save/text-format-pattern%20@] id=id123-remove-1-Link\n" // expected
        );
    }

    @Test
    public void testRefreshTimeFormatTextComponents() {
        this.refreshAndCheck(
            "/1/Untitled/cell/A1/formatter",
            SpreadsheetFormatterName.TIME_FORMAT_PATTERN,
            Lists.of(
                SpreadsheetFormatterSelectorToken.with(
                    "h",
                    "h",
                    Lists.of(
                        SpreadsheetFormatterSelectorTokenAlternative.with(
                            "hh",
                            "hh"
                        )
                    )
                ),
                SpreadsheetFormatterSelectorToken.with(
                    "m",
                    "m",
                    Lists.of(
                        SpreadsheetFormatterSelectorTokenAlternative.with(
                            "mm",
                            "mm"
                        )
                    )
                )
            ), // textComponents
            "RemoveOrReplacePluginSelectorTokenComponent\n" +
                "  SpreadsheetCard\n" +
                "    Card\n" +
                "      Remove / Replace component(s)\n" +
                "        SpreadsheetFlexLayout\n" +
                "          ROW\n" +
                "            \"h\" [#/1/Untitled/cell/A1/formatter/save/time-format-pattern%20m] id=id123-remove-0-Link\n" +
                "                \"hh\" [/1/Untitled/cell/A1/formatter/save/time-format-pattern%20hhm] id=id123-remove-0-Link-replace-0-MenuItem\n" +
                "            \"m\" [#/1/Untitled/cell/A1/formatter/save/time-format-pattern%20h] id=id123-remove-1-Link\n" +
                "                \"mm\" [/1/Untitled/cell/A1/formatter/save/time-format-pattern%20hmm] id=id123-remove-1-Link-replace-0-MenuItem\n" // expected
        );
    }

    private void refreshAndCheck(final String historyToken,
                                 final SpreadsheetFormatterName formatterName,
                                 final List<SpreadsheetFormatterSelectorToken> textComponents,
                                 final String expected) {
        final RemoveOrReplacePluginSelectorTokenComponent<SpreadsheetFormatterSelectorToken, SpreadsheetFormatterSelectorTokenAlternative> component = RemoveOrReplacePluginSelectorTokenComponent.empty("id123-");
        component.refresh(
            textComponents,
            new FakeRemoveOrReplacePluginSelectorTokenContext() {

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
    public Class<RemoveOrReplacePluginSelectorTokenComponent<SpreadsheetFormatterSelectorToken, SpreadsheetFormatterSelectorTokenAlternative>> type() {
        return Cast.to(RemoveOrReplacePluginSelectorTokenComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
