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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Optional;

public final class ValueWatchersTest implements ClassTesting<ValueWatchers<String>> {

    private final static String VALUE = "Value123";

    // add..............................................................................................................

    @Test
    public void testAddAndFireOnValue() {
        final ValueWatchers<String> watchers = ValueWatchers.empty();

        final TestValueWatcher watcher = new TestValueWatcher();
        watchers.add(watcher);

        watchers.onValue(
            Optional.of(VALUE)
        );

        this.checkEquals(
            "onValue \"Value123\"",
            watcher.toString()
        );
    }

    @Test
    public void testAddAndFireOnValue2() {
        final ValueWatchers<String> watchers = ValueWatchers.empty();

        final TestValueWatcher watcher = new TestValueWatcher();
        watchers.add(watcher);

        final TestValueWatcher watcher2 = new TestValueWatcher();
        watchers.add(watcher2);

        watchers.onValue(
            Optional.of(VALUE)
        );

        this.checkEquals(
            "onValue \"Value123\"",
            watcher.toString()
        );
        this.checkEquals(
            "onValue \"Value123\"",
            watcher2.toString()
        );
    }

    @Test
    public void testAddAndFireOnError() {
        final ValueWatchers<String> watchers = ValueWatchers.empty();

        final TestValueWatcher watcher = new TestValueWatcher();
        watchers.add(watcher);

        watchers.onErrors(
            Optional.of(
                Lists.of(
                    "Error1",
                    "Error2"
                )
            )
        );

        this.checkEquals(
            "onErrors [\"Error1\", \"Error2\"]",
            watcher.toString()
        );
    }

    private static final class TestValueWatcher implements ValueWatcher<String> {

        private final StringBuilder b = new StringBuilder();

        @Override
        public void onValue(final Optional<String> value) {
            this.b.append("onValue " + CharSequences.quoteIfChars(value));
        }

        @Override
        public void onErrors(final Optional<List<String>> errors) {
            this.b.append("onErrors " + CharSequences.quoteIfChars(errors));
        }

        @Override
        public String toString() {
            return this.b.toString();
        }
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ValueWatchers<String>> type() {
        return Cast.to(ValueWatchers.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
