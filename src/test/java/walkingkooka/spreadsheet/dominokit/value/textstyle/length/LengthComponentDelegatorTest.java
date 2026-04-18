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

package walkingkooka.spreadsheet.dominokit.value.textstyle.length;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.dominokit.value.textstyle.length.LengthComponentDelegatorTest.TestLengthComponentDelegator;
import walkingkooka.tree.text.Length;

import java.util.Optional;

public final class LengthComponentDelegatorTest implements FormValueComponentTesting<HTMLFieldSetElement, Length<?>, TestLengthComponentDelegator> {

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            new TestLengthComponentDelegator()
                .setValue(
                    Optional.of(
                        Length.pixel(12.5)
                    )
                ),
            "TestLengthComponentDelegator\n" +
                "  LengthComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [] icon=mdi-close-circle REQUIRED\n" +
                "        Errors\n" +
                "          Empty \"text\"\n"
        );
    }

    @Override
    public TestLengthComponentDelegator createComponent() {
        return new TestLengthComponentDelegator();
    }

    @Override
    public void testTestNaming() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<TestLengthComponentDelegator> type() {
        return TestLengthComponentDelegator.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    final static class TestLengthComponentDelegator implements FormValueComponent<HTMLFieldSetElement, Length<?>, TestLengthComponentDelegator>,
        LengthComponentDelegator<TestLengthComponentDelegator> {

        @Override
        public LengthComponent lengthComponent() {
            return LengthComponent.empty();
        }
    }
}
