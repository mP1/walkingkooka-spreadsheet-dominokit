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

package walkingkooka.spreadsheet.dominokit.viewport;

import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportContextDelegatorTest.TestSpreadsheetViewportContextDelegator;
import walkingkooka.tree.text.TextStyle;

import java.util.Objects;

public final class SpreadsheetViewportContextDelegatorTest implements SpreadsheetViewportContextTesting<TestSpreadsheetViewportContextDelegator> {

    @Override
    public TestSpreadsheetViewportContextDelegator createContext() {
        return new TestSpreadsheetViewportContextDelegator();
    }

    @Override
    public String typeNameSuffix() {
        return "Delegator";
    }

    @Override
    public Class<TestSpreadsheetViewportContextDelegator> type() {
        return TestSpreadsheetViewportContextDelegator.class;
    }

    final static class TestSpreadsheetViewportContextDelegator implements SpreadsheetViewportContextDelegator {

        @Override
        public SpreadsheetViewportContext spreadsheetViewportContext() {
            return new FakeSpreadsheetViewportContext() {
                @Override
                public TextStyle hideZeroStyle(final TextStyle style) {
                    return Objects.requireNonNull(style, "style");
                }
            };
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }
}
