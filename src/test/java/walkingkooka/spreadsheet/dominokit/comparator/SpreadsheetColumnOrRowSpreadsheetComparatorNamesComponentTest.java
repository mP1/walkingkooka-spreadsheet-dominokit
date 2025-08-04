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
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNames;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;

import java.util.Optional;

public final class SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, SpreadsheetColumnOrRowSpreadsheetComparatorNames, SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent> {

    @Test
    public void testPrintTree() {
        this.treePrintAndCheck(
            SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent.empty()
                .setValue(
                    Optional.of(
                        SpreadsheetColumnOrRowSpreadsheetComparatorNames.parse("A=text")
                    )
                ),
            "SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [A=text]\n"
        );
    }

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent.empty()
                .setStringValue(
                    Optional.of("A=hello,tree")
                ),
            "SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [A=hello,tree]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalidColumn() {
        this.treePrintAndCheck(
            SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent.empty()
                .setStringValue(
                    Optional.of("!A=hello")
                ),
            "SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [!A=hello]\n" +
                "      Errors\n" +
                "        Invalid character '!' at 0\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalidSpreadsheetComparatorName() {
        this.treePrintAndCheck(
            SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent.empty()
                .setStringValue(
                    Optional.of("A=!hello")
                ),
            "SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [A=!hello]\n" +
                "      Errors\n" +
                "        Invalid character '!' at 2\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent createComponent() {
        return SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent.empty();
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent> type() {
        return SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
