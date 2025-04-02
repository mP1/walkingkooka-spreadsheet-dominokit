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

import walkingkooka.Cast;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueHistoryTokenAnchorComponentDelegatorTest.TestValueHistoryTokenAnchorComponent;

import java.util.Optional;

public final class ValueHistoryTokenAnchorComponentDelegatorTest implements ClassTesting<ValueHistoryTokenAnchorComponentDelegator<TestValueHistoryTokenAnchorComponent, Void>> {

    class TestValueHistoryTokenAnchorComponent implements ValueHistoryTokenAnchorComponentDelegator<TestValueHistoryTokenAnchorComponent, Void> {
        @Override
        public ValueHistoryTokenAnchorComponent<Void> valueHistoryTokenAnchorComponent() {
            return ValueHistoryTokenAnchorComponent.with(
                HistoryTokenAnchorComponent.empty(),
                this::getter,
                this::setter
            );
        }

        private Optional<Void> getter(final HistoryTokenAnchorComponent anchor) {
            return Optional.empty();
        }

        private void setter(final Optional<Void> value,
                            final HistoryTokenAnchorComponent anchor) {
            // nop
        }
    }

    // class............................................................................................................

    @Override
    public Class<ValueHistoryTokenAnchorComponentDelegator<TestValueHistoryTokenAnchorComponent, Void>> type() {
        return Cast.to(ValueHistoryTokenAnchorComponentDelegator.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
