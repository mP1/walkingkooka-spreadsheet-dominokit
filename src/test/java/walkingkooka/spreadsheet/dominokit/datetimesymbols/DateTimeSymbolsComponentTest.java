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

package walkingkooka.spreadsheet.dominokit.datetimesymbols;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;

import java.text.DateFormatSymbols;
import java.util.Locale;
import java.util.Optional;

public final class DateTimeSymbolsComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, DateTimeSymbols, DateTimeSymbolsComponent> {

    @Test
    public void testParseAndText() {
        final DateTimeSymbols symbols = DateTimeSymbols.fromDateFormatSymbols(
            new DateFormatSymbols(Locale.ENGLISH)
        );

        this.checkEquals(
            symbols,
            DateTimeSymbols.parse(symbols.text())
        );
    }

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            DateTimeSymbolsComponent.empty()
                .setStringValue(
                    Optional.of(
                        DateTimeSymbols.fromDateFormatSymbols(
                            new DateFormatSymbols(Locale.ENGLISH)
                        ).text()
                    )
                ),
            "DateTimeSymbolsComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [\"AM,PM\",\"January,February,March,April,May,June,July,August,September,October,November,December\",\"Jan,Feb,Mar,Apr,May,Jun,Jul,Aug,Sep,Oct,Nov,Dec\",\"Sunday,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday\",\"Sun,Mon,Tue,Wed,Thu,Fri,Sat\"]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            DateTimeSymbolsComponent.empty()
                .setStringValue(
                    Optional.of(
                        "hello, !"
                    )
                ),
            "DateTimeSymbolsComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [hello, !]\n" +
                "      Errors\n" +
                "        Expected 5 tokens but got 2\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public DateTimeSymbolsComponent createComponent() {
        return DateTimeSymbolsComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<DateTimeSymbolsComponent> type() {
        return DateTimeSymbolsComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
