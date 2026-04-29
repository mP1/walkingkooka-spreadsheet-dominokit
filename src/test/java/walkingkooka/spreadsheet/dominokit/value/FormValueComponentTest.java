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

import elemental2.dom.HTMLElement;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.util.List;
import java.util.Optional;

public final class FormValueComponentTest implements ClassTesting<FormValueComponent<?, ?, ?>> {

    // addValueWatcherSkipIfErrors......................................................................................

    @Test
    public void testAddValueWatcherSkipIfErrorsWhenErrors() {
        final TestFormValueComponent component = new TestFormValueComponent("Error111");

        component.addValueWatcherSkipIfErrors(
            (Optional<Object> value) -> {
                throw new UnsupportedOperationException();
            }
        );

        component.watchers.onValue(
            Optional.of("FiredValue111")
        );
    }

    @Test
    public void testAddValueWatcherSkipIfErrorsWithoutErrors() {
        this.fired = null;

        final TestFormValueComponent component = new TestFormValueComponent();

        final Optional<Object> fired = Optional.of("FiredValue111");

        component.addValueWatcherSkipIfErrors(
            (Optional<Object> value) -> {
                FormValueComponentTest.this.fired = value;
            }
        );

        component.watchers.onValue(fired);

        this.checkEquals(
            fired,
            this.fired
        );
    }

    class TestFormValueComponent extends FakeFormValueComponent<HTMLElement, Object, TestFormValueComponent> {

        TestFormValueComponent(final String... errors) {
            this.errors = Lists.of(errors);
        }

        @Override
        public Runnable addValueWatcher(final ValueWatcher<Object> watcher) {
            return this.watchers.add(watcher);
        }

        final ValueWatchers<Object> watchers = ValueWatchers.empty();

        @Override
        public List<String> errors() {
            return this.errors;
        }

        private final List<String> errors;
    }

    private Object fired;

    // class............................................................................................................

    @Override
    public Class<FormValueComponent<?, ?, ?>> type() {
        return Cast.to(FormValueComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
