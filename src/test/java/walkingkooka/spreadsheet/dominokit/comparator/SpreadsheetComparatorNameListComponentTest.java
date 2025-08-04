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
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorNameList;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;

import java.util.Optional;

public final class SpreadsheetComparatorNameListComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, SpreadsheetComparatorNameList, SpreadsheetComparatorNameListComponent> {

    @Test
    public void testParseAndText() {
        final SpreadsheetComparatorNameList names = SpreadsheetComparatorNameList.with(
            SpreadsheetComparatorNameList.parse("day-of-month, year")
        );

        this.checkEquals(
            names,
            SpreadsheetComparatorNameList.parse(names.text())
        );
    }

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            SpreadsheetComparatorNameListComponent.empty()
                .setStringValue(
                    Optional.of("day-of-month, month-of-year")
                ),
            "SpreadsheetComparatorNameListComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [day-of-month, month-of-year]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            SpreadsheetComparatorNameListComponent.empty()
                .setStringValue(
                    Optional.of("day-of-month, !@#")
                ),
            "SpreadsheetComparatorNameListComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [day-of-month, !@#]\n" +
                "      Errors\n" +
                "        Invalid character '!' at 14\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetComparatorNameListComponent createComponent() {
        return SpreadsheetComparatorNameListComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetComparatorNameListComponent> type() {
        return SpreadsheetComparatorNameListComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
