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

package walkingkooka.spreadsheet.dominokit.grid;

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.dominokit.value.text.TextBoxComponent;

import java.util.Optional;

public final class ThreeColumnComponentTest implements HtmlComponentTesting<ThreeColumnComponent, HTMLDivElement>,
    ToStringTesting<ThreeColumnComponent> {

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
            ThreeColumnComponent.empty()
                .appendChild(
                    TextBoxComponent.empty()
                        .setValue(
                            Optional.of("Value111")
                        )
                ).appendChild(
                    TextBoxComponent.empty()
                        .setValue(
                            Optional.of("Value222")
                        )
                ),
            "ThreeColumnComponent\n" +
                "  DIV\n" +
                "    style=\"display: grid; gap: 5px; grid-template-columns: 33% 33% 33%;\"\n" +
                "      TextBoxComponent\n" +
                "        [Value111] REQUIRED\n" +
                "      TextBoxComponent\n" +
                "        [Value222] REQUIRED\n"
        );
    }

    @Test
    public void testTreePrintIncludesId() {
        this.treePrintAndCheck(
            ThreeColumnComponent.empty()
                .setId("Id123")
                .appendChild(
                    TextBoxComponent.empty()
                        .setValue(
                            Optional.of("Value111")
                        )
                ).appendChild(
                    TextBoxComponent.empty()
                        .setValue(
                            Optional.of("Value222")
                        )
                ),
            "ThreeColumnComponent\n" +
                "  DIV\n" +
                "    id=\"Id123\" style=\"display: grid; gap: 5px; grid-template-columns: 33% 33% 33%;\"\n" +
                "      TextBoxComponent\n" +
                "        [Value111] REQUIRED\n" +
                "      TextBoxComponent\n" +
                "        [Value222] REQUIRED\n"
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            ThreeColumnComponent.empty()
                .setId("Id123")
                .appendChild(
                    TextBoxComponent.empty()
                        .setValue(
                            Optional.of("Value111")
                        )
                ).appendChild(
                    TextBoxComponent.empty()
                        .setValue(
                            Optional.of("Value222")
                        )
                ),
            "ThreeColumnComponent DIV [[Value111] REQUIRED, [Value222] REQUIRED]"
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ThreeColumnComponent> type() {
        return ThreeColumnComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
