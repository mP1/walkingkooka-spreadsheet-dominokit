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
import walkingkooka.tree.text.BoxEdge;
import walkingkooka.tree.text.Margin;

import java.util.Optional;

public final class MarginRightComponentTest extends MarginComponentTestCase<MarginRightComponent> {

    final static Margin MARGIN = BoxEdge.RIGHT.parseMargin("1px");

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            MarginRightComponent.empty()
                .clearValue(),
            "MarginRightComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] icons=mdi-close-circle REQUIRED\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            MarginRightComponent.empty()
                .setValue(
                    Optional.of(MARGIN)
                ),
            "MarginRightComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [1px] icons=mdi-close-circle REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValue() {
        final MarginRightComponent component = this.createComponent();

        this.setStringValueAndCheck(
            component,
            MARGIN.text(),
            MARGIN
        );

        this.treePrintAndCheck(
            component,
            "MarginRightComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [1px] icons=mdi-close-circle REQUIRED\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public MarginRightComponent createComponent() {
        return MarginRightComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<MarginRightComponent> type() {
        return MarginRightComponent.class;
    }
}
