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
import walkingkooka.collect.set.Sets;
import walkingkooka.compare.ComparableTesting2;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.ConstantsTesting;
import walkingkooka.reflect.JavaVisibility;

import java.util.Set;

public final class KeyBindingTest implements HashCodeEqualsDefinedTesting2<KeyBinding>,
    ToStringTesting<KeyBinding>,
    ClassTesting<KeyBinding>,
    ComparableTesting2<KeyBinding>,
    ConstantsTesting<KeyBinding> {

    @Test
    public void testShiftDown() {
        this.toStringAndCheck(
            KeyBinding.SHIFT_DOWN,
            "shift DOWN"
        );
    }

    @Test
    public void testShiftUp() {
        this.toStringAndCheck(
            KeyBinding.SHIFT_UP,
            "shift UP"
        );
    }

    @Test
    public void testControlDown() {
        this.toStringAndCheck(
            KeyBinding.CONTROL_DOWN,
            "control DOWN"
        );
    }

    @Test
    public void testControlUp() {
        this.toStringAndCheck(
            KeyBinding.CONTROL_UP,
            "control UP"
        );
    }

    @Test
    public void testAltDown() {
        this.toStringAndCheck(
            KeyBinding.ALT_DOWN,
            "alt DOWN"
        );
    }

    @Test
    public void testAltUp() {
        this.toStringAndCheck(
            KeyBinding.ALT_UP,
            "alt UP"
        );
    }

    @Test
    public void testMetaDown() {
        this.toStringAndCheck(
            KeyBinding.META_DOWN,
            "meta DOWN"
        );
    }

    @Test
    public void testMetaUp() {
        this.toStringAndCheck(
            KeyBinding.META_UP,
            "meta UP"
        );
    }

    // setDown..........................................................................................................

    @Test
    public void testSetDown() {
        this.toStringAndCheck(
            KeyBinding.up("a")
                .setDown(),
            "\"a\" DOWN"
        );
    }

    // setUp............................................................................................................

    @Test
    public void testSetUp() {
        this.toStringAndCheck(
            KeyBinding.down("a")
                .setUp(),
            "\"a\" UP"
        );
    }

    // equals...........................................................................................................

    @Test
    public void testEqualsDifferentKey() {
        this.checkNotEquals(
            KeyBinding.down("a"),
            KeyBinding.down("b")
        );
    }

    @Test
    public void testEqualsDifferentAlt() {
        this.checkNotEquals(
            KeyBinding.down("a"),
            KeyBinding.down("a")
                .setAlt()
        );
    }

    @Test
    public void testEqualsDifferentControl() {
        this.checkNotEquals(
            KeyBinding.down("a"),
            KeyBinding.down("a")
                .setControl()
        );
    }

    @Test
    public void testEqualsDifferentMeta() {
        this.checkNotEquals(
            KeyBinding.down("a"),
            KeyBinding.down("a")
                .setMeta()
        );
    }

    @Test
    public void testEqualsDifferentShift() {
        this.checkNotEquals(
            KeyBinding.down("a"),
            KeyBinding.down("a")
                .setShift()
        );
    }

    @Test
    public void testEqualsDifferentDown() {
        this.checkNotEquals(
            KeyBinding.down("a"),
            KeyBinding.up("a")
        );
    }

    // ToString.........................................................................................................

    @Test
    public void testToStringKeyDown() {
        this.toStringAndCheck(
            KeyBinding.down("a"),
            "\"a\" DOWN"
        );
    }

    @Test
    public void testToStringKeyUp() {
        this.toStringAndCheck(
            KeyBinding.up("a"),
            "\"a\" UP"
        );
    }

    @Test
    public void testToStringKeyShiftDown() {
        this.toStringAndCheck(
            KeyBinding.down("a")
                .setShift(),
            "shift \"a\" DOWN"
        );
    }

    @Test
    public void testToStringKeyAltControlMetaShiftDown() {
        this.toStringAndCheck(
            KeyBinding.down("a")
                .setAlt()
                .setControl()
                .setMeta()
                .setShift(),
            "alt+control+meta+shift \"a\" DOWN"
        );
    }

    // Comparable.......................................................................................................

    @Test
    public void testCompareLess() {
        this.compareToAndCheckLess(
            KeyBinding.down("a"),
            KeyBinding.down("b")
        );
    }

    @Override
    public KeyBinding createComparable() {
        return KeyBinding.down("a");
    }

    // constants........................................................................................................

    @Override
    public Set<KeyBinding> intentionalDuplicateConstants() {
        return Sets.empty();
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
