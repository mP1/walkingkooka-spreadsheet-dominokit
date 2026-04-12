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

package walkingkooka.spreadsheet.dominokit.value.textstyle;

import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyHistoryTokenAnchorComponentContextDelegatorTest.TestTextStylePropertyHistoryTokenAnchorComponentContextDelegator;

public final class TextStylePropertyHistoryTokenAnchorComponentContextDelegatorTest implements TextStylePropertyHistoryTokenAnchorComponentContextTesting<TestTextStylePropertyHistoryTokenAnchorComponentContextDelegator> {

    @Override
    public TestTextStylePropertyHistoryTokenAnchorComponentContextDelegator createContext() {
        return new TestTextStylePropertyHistoryTokenAnchorComponentContextDelegator();
    }

    @Override
    public Class<TestTextStylePropertyHistoryTokenAnchorComponentContextDelegator> type() {
        return TestTextStylePropertyHistoryTokenAnchorComponentContextDelegator.class;
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testAddHistoryTokenWatcherWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testAddHistoryTokenWatcherOnceWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testPushHistoryTokenWithNullFails() {
        throw new UnsupportedOperationException();
    }

    final static class TestTextStylePropertyHistoryTokenAnchorComponentContextDelegator implements TextStylePropertyHistoryTokenAnchorComponentContextDelegator {

        @Override
        public TextStylePropertyHistoryTokenAnchorComponentContext textStylePropertyHistoryTokenAnchorComponentContext() {
            return new FakeTextStylePropertyHistoryTokenAnchorComponentContext();
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }
}
