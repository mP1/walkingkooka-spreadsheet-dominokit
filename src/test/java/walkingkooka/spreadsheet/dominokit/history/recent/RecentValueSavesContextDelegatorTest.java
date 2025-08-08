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

package walkingkooka.spreadsheet.dominokit.history.recent;

import walkingkooka.spreadsheet.dominokit.history.recent.RecentValueSavesContextDelegatorTest.TestRecentValueSavesContextDelegator;

public final class RecentValueSavesContextDelegatorTest implements RecentValueSavesContextTesting<TestRecentValueSavesContextDelegator> {

    @Override
    public TestRecentValueSavesContextDelegator createContext() {
        return new TestRecentValueSavesContextDelegator();
    }

    @Override
    public Class<TestRecentValueSavesContextDelegator> type() {
        return TestRecentValueSavesContextDelegator.class;
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }

    final static class TestRecentValueSavesContextDelegator implements RecentValueSavesContextDelegator {

        @Override
        public RecentValueSavesContext recentValueSavesContext() {
            return RecentValueSavesContexts.fake();
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }
}
