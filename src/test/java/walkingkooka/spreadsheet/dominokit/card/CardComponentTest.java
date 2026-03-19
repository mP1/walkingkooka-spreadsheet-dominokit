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

package walkingkooka.spreadsheet.dominokit.card;

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.dominokit.text.TextBoxComponent;

import java.util.Optional;

public final class CardComponentTest implements HtmlComponentTesting<CardComponent, HTMLDivElement> {

    @Test
    public void testEmpty() {
        this.treePrintAndCheck(
            CardComponent.empty(),
            ""
        );
    }

    @Test
    public void testAppendChild() {
        this.treePrintAndCheck(
            CardComponent.empty()
                .appendChild(
                    TextBoxComponent.empty()
                        .setValue(
                            Optional.of("Value123")
                        )
                ),
            "CardComponent\n" +
                "  Card\n" +
                "    TextBoxComponent\n" +
                "      [Value123] REQUIRED\n"
        );
    }

    @Test
    public void testSetTitleAppendChild() {
        this.treePrintAndCheck(
            CardComponent.empty()
                .setTitle("CardTitle123")
                .appendChild(
                    TextBoxComponent.empty()
                        .setValue(
                            Optional.of("Value123")
                        )
                ),
            "CardComponent\n" +
                "  Card\n" +
                "    CardTitle123\n" +
                "      TextBoxComponent\n" +
                "        [Value123] REQUIRED\n"
        );
    }

    @Test
    public void testAppendChildRemoveChild() {
        this.treePrintAndCheck(
            CardComponent.empty()
                .appendChild(
                    TextBoxComponent.empty()
                        .setValue(
                            Optional.of("Value123")
                        )
                ).removeChild(0),
            ""
        );
    }

    @Test
    public void testAppendChildRemoveChildAppendChild() {
        this.treePrintAndCheck(
            CardComponent.empty()
                .appendChild(
                    TextBoxComponent.empty()
                        .setValue(
                            Optional.of("Value111")
                        )
                ).removeChild(0)
                .appendChild(
                    TextBoxComponent.empty()
                        .setValue(
                            Optional.of("Value222")
                        )
                ),
            "CardComponent\n" +
                "  Card\n" +
                "    TextBoxComponent\n" +
                "      [Value222] REQUIRED\n"
        );
    }

    @Test
    public void testAppendChildRemoveAllChildrenAppendChild() {
        this.treePrintAndCheck(
            CardComponent.empty()
                .appendChild(
                    TextBoxComponent.empty()
                        .setValue(
                            Optional.of("Value111")
                        )
                ).removeAllChildren()
                .appendChild(
                    TextBoxComponent.empty()
                        .setValue(
                            Optional.of("Value222")
                        )
                ),
            "CardComponent\n" +
                "  Card\n" +
                "    TextBoxComponent\n" +
                "      [Value222] REQUIRED\n"
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<CardComponent> type() {
        return CardComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
