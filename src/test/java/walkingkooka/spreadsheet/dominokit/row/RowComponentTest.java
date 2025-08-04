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
import walkingkooka.spreadsheet.dominokit.text.TextBoxComponent;

public class RowComponentTest implements HtmlComponentTesting<RowComponent, HTMLDivElement> {

    @Test
    public void testTreePrintWhenEmpty() {
        this.treePrintAndCheck(
            RowComponent.columnSpan4(),
            "RowComponent\n"
        );
    }

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
            RowComponent.columnSpan4()
                .appendChild(
                    TextBoxComponent.empty()
                        .setLabel("Hello1")
                ).appendChild(
                    TextBoxComponent.empty()
                        .setLabel("Hello2")
                ),
            "RowComponent\n" +
                "  TextBoxComponent\n" +
                "    Hello1 []\n" +
                "  TextBoxComponent\n" +
                "    Hello2 []\n"
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<RowComponent> type() {
        return RowComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
