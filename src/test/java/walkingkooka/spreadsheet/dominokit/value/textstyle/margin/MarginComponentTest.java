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

package walkingkooka.spreadsheet.dominokit.value.textstyle.margin;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentLikeTesting;
import walkingkooka.tree.text.BoxEdge;
import walkingkooka.tree.text.Margin;

import java.util.Optional;

public final class MarginComponentTest implements ValueTextBoxComponentLikeTesting<MarginComponent, Margin> {

    private final static BoxEdge BOX_EDGE = BoxEdge.ALL;
    
    private final static Margin MARGIN = Margin.parse("1px");

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            MarginComponent.empty(BOX_EDGE)
                .clearValue(),
            "MarginComponent\n" +
                "  ALL\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [] icon=mdi-close-circle REQUIRED\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            MarginComponent.empty(BOX_EDGE)
                .setValue(
                    Optional.of(MARGIN)
                ),
            "MarginComponent\n" +
                "  ALL\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [1px] icon=mdi-close-circle REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueTopRightBottomAll() {
        final MarginComponent component = MarginComponent.empty(BOX_EDGE);

        final String text = "1px 2px 3px 4px";

        this.setStringValueAndCheck(
            component,
            text,
            Margin.parse(text)
        );

        this.treePrintAndCheck(
            component,
            "MarginComponent\n" +
                "  ALL\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [1px 2px 3px 4px] icon=mdi-close-circle REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueOneLength() {
        final MarginComponent component = MarginComponent.empty(BOX_EDGE);

        final String text = "1px";

        this.setStringValueAndCheck(
            component,
            text,
            Margin.parse(text)
        );

        this.treePrintAndCheck(
            component,
            "MarginComponent\n" +
                "  ALL\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [1px] icon=mdi-close-circle REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueTop() {
        this.treePrintAndCheck(
            MarginComponent.empty(BOX_EDGE)
                .setStringValue(
                    Optional.of(
                        MARGIN.setEdge(BoxEdge.TOP)
                            .text()
                    )
                ),
            "MarginComponent\n" +
                "  ALL\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [1px] icon=mdi-close-circle REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            MarginComponent.empty(BOX_EDGE)
                .setStringValue(
                    Optional.of(
                        "Invalid123!"
                    )
                ),
            "MarginComponent\n" +
                "  ALL\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [Invalid123!] icon=mdi-close-circle REQUIRED\n" +
                "        Errors\n" +
                "          Invalid number length \"Invalid123!\"\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public MarginComponent createComponent() {
        return MarginComponent.empty(BOX_EDGE);
    }

    // class............................................................................................................

    @Override
    public Class<MarginComponent> type() {
        return MarginComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
