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

package walkingkooka.spreadsheet.dominokit.row;

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.dominokit.text.SpreadsheetTextBox;

public class SpreadsheetRowComponentTest implements HtmlComponentTesting<SpreadsheetRowComponent, HTMLDivElement> {

    @Test
    public void testTreePrintWhenEmpty() {
        this.treePrintAndCheck(
            SpreadsheetRowComponent.columnSpan4(),
            "SpreadsheetRowComponent\n"
        );
    }

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
            SpreadsheetRowComponent.columnSpan4()
                .appendChild(
                    SpreadsheetTextBox.empty()
                        .setLabel("Hello1")
                ).appendChild(
                    SpreadsheetTextBox.empty()
                        .setLabel("Hello2")
                ),
            "SpreadsheetRowComponent\n" +
                "  SpreadsheetTextBox\n" +
                "    Hello1 []\n" +
                "  SpreadsheetTextBox\n" +
                "    Hello2 []\n"
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetRowComponent> type() {
        return SpreadsheetRowComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
