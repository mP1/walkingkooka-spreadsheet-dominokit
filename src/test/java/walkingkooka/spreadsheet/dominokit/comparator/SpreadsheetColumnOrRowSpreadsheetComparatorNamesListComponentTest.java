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
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNamesList;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;

import java.util.Optional;

public class SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, SpreadsheetColumnOrRowSpreadsheetComparatorNamesList, SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent> {

    @Test
    public void testStringStringMissingEqualsValidationFailure() {
        this.treePrintAndCheck(
            SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent.empty()
                .setStringValue(
                    Optional.of("A")
                ),
            "SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [A]\n" +
                "      Errors\n" +
                "        Missing '='\n"
        );
    }

    @Test
    public void testSetStringMissingComparatorNamesValidationFailure() {
        this.treePrintAndCheck(
            SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent.empty()
                .setStringValue(
                    Optional.of("A=")
                ),
            "SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [A=]\n" +
                "      Errors\n" +
                "        Missing comparator name\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent.empty()
                .setValue(
                    Optional.of(
                        SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse("A=text;B=text-case-insensitive")
                    )
                ),
            "SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [A=text;B=text-case-insensitive]\n"
        );
    }

    @Test
    public void testSetStringValueInvalidSecondColumn() {
        this.treePrintAndCheck(
            SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent.empty()
                .setStringValue(
                    Optional.of("A=GOOD;!B=BAD")
                ),
            "SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [A=GOOD;!B=BAD]\n" +
                "      Errors\n" +
                "        Invalid character '!' at 7\n"
        );
    }

    @Test
    public void testSetStringValueInvalidSecondSpreadsheetComparatorName() {
        this.treePrintAndCheck(
            SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent.empty()
                .setStringValue(
                    Optional.of("A=GOOD;B=BAD!")
                ),
            "SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [A=GOOD;B=BAD!]\n" +
                "      Errors\n" +
                "        Invalid character '!' at 12\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent createComponent() {
        return SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent.empty();
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent> type() {
        return SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
