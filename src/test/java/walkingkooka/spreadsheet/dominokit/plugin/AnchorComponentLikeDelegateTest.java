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

package walkingkooka.spreadsheet.dominokit.plugin;

import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentLike;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentLikeDelegate;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.plugin.AnchorComponentLikeDelegateTest.TestAnchorComponentLikeDelegate;
import walkingkooka.text.printer.IndentingPrinter;

public final class AnchorComponentLikeDelegateTest implements ClassTesting<TestAnchorComponentLikeDelegate> {

    @Override
    public void testTestNaming() {
        throw new UnsupportedOperationException();
    }

    // class............................................................................................................

    @Override
    public Class<TestAnchorComponentLikeDelegate> type() {
        return TestAnchorComponentLikeDelegate.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    final static class TestAnchorComponentLikeDelegate implements AnchorComponentLikeDelegate<TestAnchorComponentLikeDelegate> {

        @Override
        public AnchorComponentLike<?> anchorComponentLike() {
            return HistoryTokenAnchorComponent.empty();
        }

        @Override
        public void printTree(final IndentingPrinter printer) {
            this.anchorComponentLike()
                    .printTree(printer);
        }
    }
}
