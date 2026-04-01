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
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentLikeTestingTest.TestFormValueComponentLike;
import walkingkooka.text.printer.IndentingPrinter;

public final class FormValueComponentLikeTestingTest implements FormValueComponentLikeTesting<HTMLFieldSetElement, TestFormValueComponentLike> {

    final static class TestFormValueComponentLike extends FakeFormValueComponentLike<HTMLFieldSetElement, TestFormValueComponentLike> {

        @Override
        public void printTree(final IndentingPrinter printer) {
            printer.println(this.getClass().getSimpleName());
        }
    }
    
    @Override
    public TestFormValueComponentLike createComponent() {
        return new TestFormValueComponentLike();
    }

    @Override
    public Class<TestFormValueComponentLike> type() {
        return TestFormValueComponentLike.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public void testTestNaming() {
        throw new UnsupportedOperationException();
    }
}
