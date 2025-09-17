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

package walkingkooka.spreadsheet.dominokit.comparator;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorInfoSet;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorProviders;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;

import java.util.Optional;

public final class SpreadsheetComparatorInfoSetComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, SpreadsheetComparatorInfoSet, SpreadsheetComparatorInfoSetComponent> {

    @Test
    public void testParseAndText() {
        final SpreadsheetComparatorInfoSet infos = SpreadsheetComparatorInfoSet.with(
            SpreadsheetComparatorProviders.spreadsheetComparators()
                .spreadsheetComparatorInfos()
        );

        this.checkEquals(
            infos,
            SpreadsheetComparatorInfoSet.parse(infos.text())
        );
    }

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            SpreadsheetComparatorInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        SpreadsheetComparatorProviders.spreadsheetComparators()
                            .spreadsheetComparatorInfos()
                            .text()
                    )
                ),
            "SpreadsheetComparatorInfoSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetComparator/date date,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetComparator/date-time date-time,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetComparator/day-of-month day-of-month,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetComparator/day-of-week day-of-week,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetComparator/hour-of-am-pm hour-of-am-pm,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetComparator/hour-of-day hour-of-day,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetComparator/minute-of-hour minute-of-hour,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetComparator/month-of-year month-of-year,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetComparator/nano-of-second nano-of-second,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetComparator/number number,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetComparator/seconds-of-minute seconds-of-minute,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetComparator/text text,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetComparator/text-case-insensitive text-case-insensitive,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetComparator/time time,https://github.com/mP1/walkingkooka-spreadsheet/SpreadsheetComparator/year year]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalidSpreadsheetComparatorName() {
        this.treePrintAndCheck(
            SpreadsheetComparatorInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "https://example.com hello more more"
                    )
                ),
            "SpreadsheetComparatorInfoSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [https://example.com hello more more]\n" +
                "      Errors\n" +
                "        Invalid character 'm' at 26\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalidSecondAbsoluteUrl() {
        this.treePrintAndCheck(
            SpreadsheetComparatorInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "https://example.com/1 good,bad://example.com/2 bad"
                    )
                ),
            "SpreadsheetComparatorInfoSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [https://example.com/1 good,bad://example.com/2 bad]\n" +
                "      Errors\n" +
                "        unknown protocol: bad\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalidSecondSpreadsheetComparatorName() {
        this.treePrintAndCheck(
            SpreadsheetComparatorInfoSetComponent.empty()
                .setStringValue(
                    Optional.of(
                        "https://example.com/1 good,https://example.com/2 bad!"
                    )
                ),
            "SpreadsheetComparatorInfoSetComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [https://example.com/1 good,https://example.com/2 bad!]\n" +
                "      Errors\n" +
                "        Invalid character '!' at 52\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetComparatorInfoSetComponent createComponent() {
        return SpreadsheetComparatorInfoSetComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetComparatorInfoSetComponent> type() {
        return SpreadsheetComparatorInfoSetComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
