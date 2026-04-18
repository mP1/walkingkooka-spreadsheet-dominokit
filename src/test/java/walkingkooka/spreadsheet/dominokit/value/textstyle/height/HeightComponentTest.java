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

package walkingkooka.spreadsheet.dominokit.value.textstyle.height;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.textstyle.length.TextStyleLengthPropertyComponentLikeTesting;
import walkingkooka.tree.text.Length;

import java.util.Optional;

public final class HeightComponentTest implements TextStyleLengthPropertyComponentLikeTesting<HeightComponent> {

    private final static String ID_PREFIX = "TestIdPrefix123-";

    private final static Length<?> LENGTH = Length.parse("1px");

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "HeightComponent\n" +
                "  LengthComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        Height [] icon=mdi-close-circle id=TestIdPrefix123-height-TextBox REQUIRED\n" +
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
            "HeightComponent\n" +
                "  LengthComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        Height [1px] icon=mdi-close-circle id=TestIdPrefix123-height-TextBox REQUIRED\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public HeightComponent createComponent() {
        return HeightComponent.empty(ID_PREFIX);
    }

    // class............................................................................................................

    @Override
    public Class<HeightComponent> type() {
        return HeightComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
