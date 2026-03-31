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
import walkingkooka.spreadsheet.dominokit.value.ValueComponentLikeDelegatorTest.TestValueComponentLikeDelegator;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Optional;

public final class ValueComponentLikeDelegatorTest implements ValueComponentLikeTesting<HTMLFieldSetElement, TestValueComponentLikeDelegator> {

    @Override
    public void testTestNaming() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TestValueComponentLikeDelegator createComponent() {
        return new TestValueComponentLikeDelegator();
    }

    // class............................................................................................................

    @Override
    public Class<TestValueComponentLikeDelegator> type() {
        return TestValueComponentLikeDelegator.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    final static class TestValueComponentLikeDelegator implements ValueComponent<HTMLFieldSetElement, String, TestValueComponentLikeDelegator>,
        ValueComponentLikeDelegator<HTMLFieldSetElement, TestValueComponentLikeDelegator> {

        @Override
        public TestValueComponentLikeDelegator setValue(final Optional<String> value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<String> value() {
            throw new UnsupportedOperationException();
        }

        @Override
        public TestValueComponentLikeDelegator clearValue() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Runnable addValueWatcher(final ValueWatcher<String> watcher) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ValueComponentLike<HTMLFieldSetElement, ?> valueComponentLike() {
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
