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

package walkingkooka.spreadsheet.dominokit.value.textstyle.border;

import org.junit.jupiter.api.Test;

import java.util.Optional;

public final class BorderTopWidthComponentTest extends BorderWidthSharedComponentTestCase<BorderTopWidthComponent> {

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "BorderTopWidthComponent\n" +
                "  LengthComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [] icons=mdi-close-circle id=TestIdPrefix123-borderTopWidth-TextBox REQUIRED\n" +
                "        Errors\n" +
                "          Empty \"text\"\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(LENGTH)
                ),
            "BorderTopWidthComponent\n" +
                "  LengthComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [1px] icons=mdi-close-circle id=TestIdPrefix123-borderTopWidth-TextBox REQUIRED\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public BorderTopWidthComponent createComponent() {
        return BorderTopWidthComponent.empty(
            "TestIdPrefix123-"
        );
    }

    // class............................................................................................................

    @Override
    public Class<BorderTopWidthComponent> type() {
        return BorderTopWidthComponent.class;
    }
}
