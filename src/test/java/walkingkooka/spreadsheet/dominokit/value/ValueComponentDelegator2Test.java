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

package walkingkooka.spreadsheet.dominokit.value;

import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.text.TextBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentDelegator2Test.TestValueComponentDelegator2;
import walkingkooka.text.printer.IndentingPrinter;

public final class ValueComponentDelegator2Test implements ValueComponentTesting<HTMLFieldSetElement, String, TestValueComponentDelegator2> {

    @Override
    public void testTestNaming() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TestValueComponentDelegator2 createComponent() {
        return new TestValueComponentDelegator2();
    }

    // class............................................................................................................

    @Override
    public Class<TestValueComponentDelegator2> type() {
        return TestValueComponentDelegator2.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    final static class TestValueComponentDelegator2 implements ValueComponentDelegator2<HTMLFieldSetElement, String, TestValueComponentDelegator2> {

        @Override
        public ValueComponent<HTMLFieldSetElement, String, ?> valueComponent() {
            return this.textBox;
        }

        private final TextBoxComponent textBox = TextBoxComponent.empty();

        @Override
        public void printTree(final IndentingPrinter printer) {
            printer.println(this.getClass().getSimpleName());

            printer.indent();
            {
                this.textBox.printTree(printer);
            }
            printer.outdent();
        }
    }
}
