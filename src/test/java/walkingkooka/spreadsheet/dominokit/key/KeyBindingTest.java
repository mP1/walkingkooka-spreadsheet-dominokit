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

package walkingkooka.spreadsheet.dominokit.key;

import org.junit.jupiter.api.Test;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.compare.ComparableTesting2;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

public final class KeyBindingTest implements HashCodeEqualsDefinedTesting2<KeyBinding>,
    ToStringTesting<KeyBinding>,
    ClassTesting<KeyBinding>,
    ComparableTesting2<KeyBinding> {

    // equals...........................................................................................................

    @Test
    public void testEqualsDifferentKey() {
        this.checkNotEquals(
            KeyBinding.with("a"),
            KeyBinding.with("b")
        );
    }

    @Test
    public void testEqualsDifferentAlt() {
        this.checkNotEquals(
            KeyBinding.with("a"),
            KeyBinding.with("a")
                .setAlt()
        );
    }

    @Test
    public void testEqualsDifferentControl() {
        this.checkNotEquals(
            KeyBinding.with("a"),
            KeyBinding.with("a")
                .setControl()
        );
    }

    @Test
    public void testEqualsDifferentMeta() {
        this.checkNotEquals(
            KeyBinding.with("a"),
            KeyBinding.with("a")
                .setMeta()
        );
    }

    @Test
    public void testEqualsDifferentShift() {
        this.checkNotEquals(
            KeyBinding.with("a"),
            KeyBinding.with("a")
                .setShift()
        );
    }

    // ToString.........................................................................................................

    @Test
    public void testToStringKey() {
        this.toStringAndCheck(
            KeyBinding.with("a"),
            "\"a\""
        );
    }

    @Test
    public void testToStringKeyShift() {
        this.toStringAndCheck(
            KeyBinding.with("a")
                .setShift(),
            "shift \"a\""
        );
    }

    @Test
    public void testToStringKeyAltControlMetaShift() {
        this.toStringAndCheck(
            KeyBinding.with("a")
                .setAlt()
                .setControl()
                .setMeta()
                .setShift(),
            "alt+control+meta+shift \"a\""
        );
    }

    // Comparable.......................................................................................................

    @Test
    public void testCompareLess() {
        this.compareToAndCheckLess(
            KeyBinding.with("a"),
            KeyBinding.with("b")
        );
    }

    @Override
    public KeyBinding createComparable() {
        return KeyBinding.with("a");
    }

    // class............................................................................................................

    @Override
    public Class<KeyBinding> type() {
        return KeyBinding.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
