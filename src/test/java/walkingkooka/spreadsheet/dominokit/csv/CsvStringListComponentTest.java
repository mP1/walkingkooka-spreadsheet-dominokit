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

package walkingkooka.spreadsheet.dominokit.csv;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.CsvStringList;
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;

import java.text.DateFormatSymbols;
import java.util.Locale;
import java.util.Optional;

public final class CsvStringListComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, CsvStringList, CsvStringListComponent> {

    private final CsvStringList MONTH_NAMES_LIST = CsvStringList.EMPTY.setElements(
        DateTimeSymbols.fromDateFormatSymbols(
            new DateFormatSymbols(
                Locale.ENGLISH
            )
        ).monthNames()
    );

    @Test
    public void testParseAndText() {
        this.checkEquals(
            MONTH_NAMES_LIST,
            CsvStringList.parse(
                MONTH_NAMES_LIST.text()
            )
        );
    }

    @Test
    public void testSetStringValueInclusive() {
        this.treePrintAndCheck(
            CsvStringListComponent.empty(2, 12, CsvStringListComponent.INCLUSIVE)
                .setStringValue(
                    Optional.of(
                        MONTH_NAMES_LIST.text()
                    )
                ),
            "CsvStringListComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [January,February,March,April,May,June,July,August,September,October,November,December]\n"
        );
    }

    @Test
    public void testSetStringValueWithAllLocales() {
        for (final Locale locale : Locale.getAvailableLocales()) {
            final CsvStringList monthNames = CsvStringList.EMPTY.setElements(
                DateTimeSymbols.fromDateFormatSymbols(
                    new DateFormatSymbols(locale)
                ).monthNames()
            );

            this.checkEquals(
                monthNames,
                CsvStringListComponent.empty(
                        12,
                        13,
                        CsvStringListComponent.INCLUSIVE
                    ).setStringValue(
                        Optional.of(
                            monthNames.text()
                        )
                    ).value()
                    .orElse(null)
            );
        }
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            CsvStringListComponent.empty(
                2,
                4,
                CsvStringListComponent.INCLUSIVE
            ).setStringValue(
                Optional.of(
                    "Monday,\"Tuesday"
                )
            ),
            "CsvStringListComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [Monday,\"Tuesday]\n" +
                "      Errors\n" +
                "        Missing terminating '\"'\n"
        );
    }

    @Test
    public void testSetStringValueWithLessThan() {
        this.treePrintAndCheck(
            CsvStringListComponent.empty(
                2,
                4,
                CsvStringListComponent.INCLUSIVE
            ).setStringValue(
                Optional.of(
                    "Monday"
                )
            ),
            "CsvStringListComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [Monday]\n" +
                "      Errors\n" +
                "        Require 2 or more\n"
        );
    }

    @Test
    public void testSetStringValueWithLessThanWhenMinEqMax() {
        this.treePrintAndCheck(
            CsvStringListComponent.empty(
                2,
                2,
                CsvStringListComponent.INCLUSIVE
            ).setStringValue(
                Optional.of(
                    "Monday"
                )
            ),
            "CsvStringListComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [Monday]\n" +
                "      Errors\n" +
                "        Require 2\n"
        );
    }

    @Test
    public void testSetStringValueWithMoreThanInclusive() {
        this.treePrintAndCheck(
            CsvStringListComponent.empty(
                2,
                3,
                CsvStringListComponent.INCLUSIVE
            ).setStringValue(
                Optional.of(
                    "Monday, Tuesday, Wednesday, Thursday"
                )
            ),
            "CsvStringListComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [Monday, Tuesday, Wednesday, Thursday]\n" +
                "      Errors\n" +
                "        Require 3 or less\n"
        );
    }

    @Test
    public void testSetStringValueWithMoreThanInclusiveWhenMinEqMax() {
        this.treePrintAndCheck(
            CsvStringListComponent.empty(
                2,
                2,
                CsvStringListComponent.INCLUSIVE
            ).setStringValue(
                Optional.of(
                    "Monday, Tuesday, Wednesday, Thursday"
                )
            ),
            "CsvStringListComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [Monday, Tuesday, Wednesday, Thursday]\n" +
                "      Errors\n" +
                "        Require 2\n"
        );
    }

    @Test
    public void testSetStringValueWithMoreThanExclusive() {
        this.treePrintAndCheck(
            CsvStringListComponent.empty(
                2,
                4,
                CsvStringListComponent.EXCLUSIVE
            ).setStringValue(
                Optional.of(
                    "Monday, Tuesday, Wednesday, Thursday"
                )
            ),
            "CsvStringListComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [Monday, Tuesday, Wednesday, Thursday]\n" +
                "      Errors\n" +
                "        Require less than 4\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public CsvStringListComponent createComponent() {
        return CsvStringListComponent.empty(
            2,
            4,
            CsvStringListComponent.EXCLUSIVE
        );
    }

    // class............................................................................................................

    @Override
    public Class<CsvStringListComponent> type() {
        return CsvStringListComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
