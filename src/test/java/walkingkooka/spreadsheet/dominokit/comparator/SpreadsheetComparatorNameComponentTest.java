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
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;

import java.util.Optional;

public final class SpreadsheetComparatorNameComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, SpreadsheetComparatorName, SpreadsheetComparatorNameComponent> {

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            SpreadsheetComparatorNameComponent.empty()
                .setStringValue(
                    Optional.of("hello-comparator")
                ),
            "SpreadsheetComparatorNameComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [hello-comparator]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            SpreadsheetComparatorNameComponent.empty()
                .setStringValue(
                    Optional.of("!@#")
                ),
            "SpreadsheetComparatorNameComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [!@#]\n" +
                "      Errors\n" +
                "        Invalid character '!' at 0\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetComparatorNameComponent createComponent() {
        return SpreadsheetComparatorNameComponent.empty();
    }

    // class............................................................................................................


    @Override
    public Class<SpreadsheetComparatorNameComponent> type() {
        return SpreadsheetComparatorNameComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
