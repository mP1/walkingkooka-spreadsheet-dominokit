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

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class KeyBindingTest implements HashCodeEqualsDefinedTesting2<KeyBinding>,
    ToStringTesting<KeyBinding>,
    ClassTesting<KeyBinding>,
    ComparableTesting2<KeyBinding>,
    ConstantsTesting<KeyBinding> {

    @Test
    public void testSetLabelWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> KeyBinding.ALT_UP.setLabel(null)
        );
    }

    @Test
    public void testSetLabelWithSame() {
        final KeyBinding keyBinding = KeyBinding.ALT_UP;
        assertSame(
            keyBinding,
            keyBinding.setLabel(keyBinding.label())
        );
    }

    @Test
    public void testSetLabelWithDifferent() {
        final KeyBinding keyBinding = KeyBinding.ALT_UP;

        final String label = "Label123";
        final KeyBinding different = keyBinding.setLabel(label);
        assertNotSame(
            keyBinding,
            different
        );

        this.toStringAndCheck(
            different,
            "Label123 alt UP"
        );
    }

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

    // isModifier......................................................................................................

    @Test
    public void testIsModifierWithAlt() {
        this.isModifierAndCheck(
            KeyBinding.down("Alt")
                .setAlt(),
            true
        );
    }

    @Test
    public void testIsModifierWithControl() {
        this.isModifierAndCheck(
            KeyBinding.down("Control")
                .setControl(),
            true
        );
    }

    @Test
    public void testIsModifierWithMeta() {
        this.isModifierAndCheck(
            KeyBinding.down("Meta")
                .setMeta(),
            true
        );
    }

    @Test
    public void testIsModifierWithShift() {
        this.isModifierAndCheck(
            KeyBinding.down("Shift")
                .setShift(),
            true
        );
    }

    @Test
    public void testIsModifierWithShiftAndControl() {
        this.isModifierAndCheck(
            KeyBinding.down("Shift")
                .setShift()
                .setControl(),
            true
        );
    }

    @Test
    public void testIsModifierWithLetter() {
        this.isModifierAndCheck(
            KeyBinding.down("A"),
            false
        );
    }

    @Test
    public void testIsModifierWithLetterAndShiftModifier() {
        this.isModifierAndCheck(
            KeyBinding.down("A")
                .setShift(),
            false
        );
    }

    private void isModifierAndCheck(final KeyBinding keyBinding,
                                    final boolean expected) {
        this.checkEquals(
            keyBinding.isModifier(),
            expected,
            keyBinding::toString
        );
    }

    // equalModifiers...................................................................................................

    @Test
    public void testEqualModifiersWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createObject()
                .equalModifiers(null)
        );
    }

    @Test
    public void testEqualModifiersDifferentKey() {
        this.equalModifiersAndCheck(
            KeyBinding.down("A"),
            KeyBinding.down("B"),
            true
        );
    }

    @Test
    public void testEqualModifiersDifferentDown() {
        this.equalModifiersAndCheck(
            KeyBinding.down("A"),
            KeyBinding.up("A"),
            true
        );
    }

    @Test
    public void testEqualModifiersDifferentAlt() {
        this.equalModifiersAndCheck(
            KeyBinding.down("A"),
            KeyBinding.down("A")
                .setAlt(),
            false
        );
    }

    @Test
    public void testEqualModifiersDifferentControl() {
        this.equalModifiersAndCheck(
            KeyBinding.down("A"),
            KeyBinding.down("A")
                .setControl(),
            false
        );
    }

    @Test
    public void testEqualModifiersDifferentMeta() {
        this.equalModifiersAndCheck(
            KeyBinding.down("A"),
            KeyBinding.down("A")
                .setMeta(),
            false
        );
    }

    @Test
    public void testEqualModifiersDifferentShift() {
        this.equalModifiersAndCheck(
            KeyBinding.down("A"),
            KeyBinding.down("A")
                .setShift(),
            false
        );
    }

    @Test
    public void testEqualModifiersAlt() {
        this.equalModifiersAndCheck(
            KeyBinding.down("A")
                .setAlt(),
            KeyBinding.down("B")
                .setAlt(),
            true
        );
    }

    @Test
    public void testEqualModifiersControl() {
        this.equalModifiersAndCheck(
            KeyBinding.down("A")
                .setControl(),
            KeyBinding.down("B")
                .setControl(),
            true
        );
    }

    @Test
    public void testEqualModifiersMeta() {
        this.equalModifiersAndCheck(
            KeyBinding.down("A")
                .setMeta(),
            KeyBinding.down("B")
                .setMeta(),
            true
        );
    }

    @Test
    public void testEqualModifiersShift() {
        this.equalModifiersAndCheck(
            KeyBinding.down("A")
                .setShift(),
            KeyBinding.down("B")
                .setShift(),
            true
        );
    }

    private void equalModifiersAndCheck(final KeyBinding keyBinding,
                                        final KeyBinding other,
                                        final boolean expected) {
        this.checkEquals(
            expected,
            keyBinding.equalModifiers(other),
            () -> keyBinding + " equalModifiers " + other
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

    // Comparable.......................................................................................................

    @Test
    public void testComparableEqualsIgnoresLabel() {
        final KeyBinding keyBinding = KeyBinding.down("A");

        this.compareToAndCheckEquals(
            keyBinding.setLabel("Label123"),
            keyBinding
        );
    }

    @Test
    public void testComparableNotEqualsIgnoresLabel() {
        final KeyBinding keyBinding = KeyBinding.down("A");

        this.compareToAndCheckNotEquals(
            keyBinding.setLabel("Label123"),
            keyBinding.setShift()
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

    @Test
    public void testToStringLabelKey() {
        this.toStringAndCheck(
            KeyBinding.down("a")
                .setLabel("Label123"),
            "Label123 \"a\" DOWN"
        );
    }

    @Test
    public void testToStringLabelKeyAlt() {
        this.toStringAndCheck(
            KeyBinding.down("a")
                .setAlt()
                .setLabel("Label123"),
            "Label123 alt \"a\" DOWN"
        );
    }

    @Test
    public void testToStringLabelKeyShift() {
        this.toStringAndCheck(
            KeyBinding.down("a")
                .setShift()
                .setLabel("Label123"),
            "Label123 shift \"a\" DOWN"
        );
    }

    @Test
    public void testToStringLabelKeyControlMetaShift() {
        this.toStringAndCheck(
            KeyBinding.down("a")
                .setControl()
                .setMeta()
                .setShift()
                .setLabel("Label123"),
            "Label123 control+meta+shift \"a\" DOWN"
        );
    }

    // toStringModifiers................................................................................................

    @Test
    public void testToStringModifiers() {
        this.toStringModifiersAndCheck(
            KeyBinding.up("A"),
            ""
        );
    }

    @Test
    public void testToStringModifiersAlt() {
        this.toStringModifiersAndCheck(
            KeyBinding.up("A")
                .setAlt(),
            "alt"
        );
    }

    @Test
    public void testToStringModifiersControl() {
        this.toStringModifiersAndCheck(
            KeyBinding.up("A")
                .setControl(),
            "control"
        );
    }

    @Test
    public void testToStringModifiersMeta() {
        this.toStringModifiersAndCheck(
            KeyBinding.up("A")
                .setMeta(),
            "meta"
        );
    }

    @Test
    public void testToStringModifiersShift() {
        this.toStringModifiersAndCheck(
            KeyBinding.up("A")
                .setShift(),
            "shift"
        );
    }

    @Test
    public void testToStringModifiersAltControlMetaShift() {
        this.toStringModifiersAndCheck(
            KeyBinding.up("A")
                .setAlt()
                .setControl()
                .setMeta()
                .setShift(),
            "alt+control+meta+shift"
        );
    }

    private void toStringModifiersAndCheck(final KeyBinding keyBinding,
                                           final String expected) {
        this.checkEquals(
            expected,
            keyBinding.toStringModifiers(),
            keyBinding::toString
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
