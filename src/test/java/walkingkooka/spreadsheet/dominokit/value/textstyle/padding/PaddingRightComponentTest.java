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

package walkingkooka.spreadsheet.dominokit.value.textstyle.padding;

import org.junit.jupiter.api.Test;

import java.util.Optional;

public final class PaddingRightComponentTest extends PaddingComponentTestCase<PaddingRightComponent> {

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "PaddingRightComponent\n" +
                "  LengthComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [] icons=mdi-close-circle id=TestIdPrefix123-paddingRight-TextBox REQUIRED\n" +
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
            "PaddingRightComponent\n" +
                "  LengthComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [12.5px] icons=mdi-close-circle id=TestIdPrefix123-paddingRight-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValue() {
        final PaddingRightComponent component = this.createComponent();

        this.setStringValueAndCheck(
            component,
            LENGTH.text(),
            LENGTH
        );

        this.treePrintAndCheck(
            component,
            "PaddingRightComponent\n" +
                "  LengthComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [12.5px] icons=mdi-close-circle id=TestIdPrefix123-paddingRight-TextBox REQUIRED\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public PaddingRightComponent createComponent(final String idPrefix) {
        return PaddingRightComponent.with(idPrefix);
    }

    // class............................................................................................................

    @Override
    public Class<PaddingRightComponent> type() {
        return PaddingRightComponent.class;
    }
}
