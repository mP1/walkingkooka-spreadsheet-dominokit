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

package walkingkooka.spreadsheet.dominokit.padding;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentLikeTesting;
import walkingkooka.tree.text.BoxEdge;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.Padding;
import walkingkooka.tree.text.TextStyle;

import java.util.Optional;

public final class PaddingComponentTest implements ValueTextBoxComponentLikeTesting<PaddingComponent, Padding> {

    private final static Padding PADDING = TextStyle.EMPTY.setPadding(
        Optional.of(
            Length.pixel(1.0)
        )
    ).padding(BoxEdge.ALL);

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            PaddingComponent.empty()
                .clearValue(),
            "PaddingComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] REQUIRED\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            PaddingComponent.empty()
                .setValue(
                    Optional.of(PADDING)
                ),
            "PaddingComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [bottom: 1px; left: 1px; right: 1px; top: 1px;] REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            PaddingComponent.empty()
                .setStringValue(
                    Optional.of(
                        PADDING.text()
                    )
                ),
            "PaddingComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [bottom: 1px; left: 1px; right: 1px; top: 1px;] REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueTop() {
        this.treePrintAndCheck(
            PaddingComponent.empty()
                .setStringValue(
                    Optional.of(
                        PADDING.setEdge(BoxEdge.TOP)
                            .text()
                    )
                ),
            "PaddingComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [top: 1px;] REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            PaddingComponent.empty()
                .setStringValue(
                    Optional.of(
                        "Invalid123!"
                    )
                ),
            "PaddingComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [Invalid123!] REQUIRED\n" +
                "      Errors\n" +
                "        TextStyle: Invalid property \"Invalid123\"\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public PaddingComponent createComponent() {
        return PaddingComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<PaddingComponent> type() {
        return PaddingComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
