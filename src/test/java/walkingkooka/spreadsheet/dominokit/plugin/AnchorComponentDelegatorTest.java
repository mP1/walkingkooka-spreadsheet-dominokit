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
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponent;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentDelegator;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.plugin.AnchorComponentDelegatorTest.TestAnchorComponentDelegator;

import java.util.Optional;

public final class AnchorComponentDelegatorTest implements ClassTesting<TestAnchorComponentDelegator> {

    @Override
    public void testTestNaming() {
        throw new UnsupportedOperationException();
    }

    // class............................................................................................................

    @Override
    public Class<TestAnchorComponentDelegator> type() {
        return TestAnchorComponentDelegator.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    final static class TestAnchorComponentDelegator implements AnchorComponentDelegator<TestAnchorComponentDelegator, Void> {

        @Override
        public TestAnchorComponentDelegator setValue(final Optional<Void> value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<Void> value() {
            throw new UnsupportedOperationException();
        }

        @Override
        public AnchorComponent<?, ?> anchorComponent() {
            return HistoryTokenAnchorComponent.empty();
        }
    }
}
