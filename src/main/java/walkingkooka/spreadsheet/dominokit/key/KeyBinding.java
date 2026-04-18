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

import elemental2.dom.KeyboardEvent;
import walkingkooka.Cast;
import walkingkooka.ToStringBuilder;
import walkingkooka.ToStringBuilderOption;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Objects;

/**
 * Captures the binding for a single keyboard to action mapping.
 */
public final class KeyBinding implements Comparable<KeyBinding> {

    private final static String NO_LABEL = "";

    private final static List<KeyBinding> CONSTANTS = Lists.array();

    public final static KeyBinding SHIFT_DOWN = registerConstant(
        false, // alt
        false, // control
        false, // meta
        true, // shift,
        true // down NOT up
    );

    public final static KeyBinding SHIFT_UP = registerConstant(
        false, // alt
        false, // control
        false, // meta
        true, // shift,
        false // up NOT down
    );

    public final static KeyBinding CONTROL_DOWN = registerConstant(
        false, // alt
        true, // control
        false, // meta
        false, // shift,
        true // down NOT up
    );

    public final static KeyBinding CONTROL_UP = registerConstant(
        false, // alt
        true, // control
        false, // meta
        false, // shift,
        false // up NOT down
    );

    public final static KeyBinding ALT_DOWN = registerConstant(
        true, // alt
        false, // control
        false, // meta
        false, // shift,
        true // down NOT up
    );

    public final static KeyBinding ALT_UP = registerConstant(
        true, // alt
        false, // control
        false, // meta
        false, // shift,
        false // up NOT down
    );

    public final static KeyBinding META_DOWN = registerConstant(
        false, // alt
        false, // control
        true, // meta
        false, // shift,
        true // down NOT up
    );

    public final static KeyBinding META_UP = registerConstant(
        false, // alt
        false, // control
        true, // meta
        false, // shift,
        false // up NOT down
    );

    private static KeyBinding registerConstant(final boolean alt,
                                               final boolean control,
                                               final boolean meta,
                                               final boolean shift,
                                               final boolean down) {
        final KeyBinding keyBinding = new KeyBinding(
            NO_LABEL,
            "", // no key
            alt,
            control,
            meta,
            shift,
            down
        );
        CONSTANTS.add(keyBinding);
        return keyBinding;
    }

    public static KeyBinding fromKeyEvent(final KeyboardEvent event) {
        Objects.requireNonNull(event, "event");

        final String key = event.key;
        KeyBinding keyBinding = "keydown".equals(event.type) ?
            KeyBinding.down(key) :
            KeyBinding.up(key);

        if (event.altKey) {
            keyBinding = keyBinding.setAlt();
        }
        if (event.ctrlKey) {
            keyBinding = keyBinding.setControl();
        }
        if (event.metaKey) {
            keyBinding = keyBinding.setMeta();
        }
        if (event.shiftKey) {
            keyBinding = keyBinding.setShift();
        }

        if (key.isEmpty()) {
            for (final KeyBinding possible : CONSTANTS) {
                if (possible.equals(keyBinding)) {
                    keyBinding = possible;
                    break;
                }
            }
        }

        return keyBinding;
    }

    public static KeyBinding down(final String key) {
        return new KeyBinding(
            NO_LABEL,
            CharSequences.failIfNullOrEmpty(key, "key"),
            false, // alt
            false, // control
            false, // meta
            false, // shift,
            true // down NOT up
        );
    }

    public static KeyBinding up(final String key) {
        return new KeyBinding(
            NO_LABEL,
            CharSequences.failIfNullOrEmpty(key, "key"),
            false, // alt
            false, // control
            false, // meta
            false, // shift,
            false // up NOT down
        );
    }

    private KeyBinding(final String label,
                       final String key,
                       final boolean alt,
                       final boolean control,
                       final boolean meta,
                       final boolean shift,
                       final boolean down) {
        this.label = label;

        this.key = key;

        this.alt = alt;
        this.control = control;
        this.meta = meta;
        this.shift = shift;

        this.down = down;
    }

    public String label() {
        return this.label;
    }

    public KeyBinding setLabel(final String label) {
        Objects.requireNonNull(label, "label");

        return this.label.equals(label) ?
            this :
            new KeyBinding(
                label,
                this.key,
                this.alt,
                this.control,
                this.meta,
                this.shift,
                this.down
            );
    }

    private final String label;

    public String key() {
        return this.key;
    }

    private final String key;

    public boolean isAlt() {
        return this.alt;
    }

    public KeyBinding setAlt() {
        return this.alt ?
            this :
            new KeyBinding(
                this.label,
                this.key,
                true, // alt
                this.control,
                this.meta,
                this.shift,
                this.down
            );
    }

    private final boolean alt;

    public boolean isControl() {
        return this.control;
    }

    public KeyBinding setControl() {
        return this.control ?
            this :
            new KeyBinding(
                this.label,
                this.key,
                this.alt,
                true, // control
                this.meta,
                this.shift,
                this.down
            );
    }

    private final boolean control;


    public boolean isMeta() {
        return this.meta;
    }

    public KeyBinding setMeta() {
        return this.meta ?
            this :
            new KeyBinding(
                this.label,
                this.key,
                this.alt,
                this.control,
                true, // meta
                this.shift,
                this.down
            );
    }

    private final boolean meta;

    public boolean isShift() {
        return this.shift;
    }

    public KeyBinding setShift() {
        return this.shift ?
            this :
            new KeyBinding(
                this.label,
                this.key,
                this.alt,
                this.control,
                this.meta,
                true, // shift
                this.down
            );
    }


    private final boolean shift;

    public boolean isDown() {
        return this.down;
    }

    public KeyBinding setDown() {
        return this.down == true ?
            this :
            new KeyBinding(
                this.label,
                this.key,
                this.alt,
                this.control,
                this.meta,
                this.shift,
                true // down
            );
    }

    private final boolean down;

    public boolean isUp() {
        return false == this.isDown();
    }

    public KeyBinding setUp() {
        return this.down == false ?
            this :
            new KeyBinding(
                this.label,
                this.key,
                this.alt,
                this.control,
                this.meta,
                this.shift,
                false // down
            );
    }

    /**
     * Tests if this {@link KeyBinding} is actually for a ALT, CONTROL, META or SHIFT key events themselves and returns
     * false for any other key.
     */
    public boolean isModifier() {
        final boolean isModifier;

        switch (this.key) {
            case "Alt":
            case "Control":
            case "Meta":
            case "Shift":
                isModifier = true;
                break;
            default:
                isModifier = false;
                break;
        }

        return isModifier;
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(
            this.label,
            this.key,
            this.alt,
            this.control,
            this.meta,
            this.shift,
            this.down
        );
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof KeyBinding &&
                this.equals0(Cast.to(other));
    }

    private boolean equals0(final KeyBinding other) {
        return this.label.equals(other.label) &&
            this.key.equals(other.key) &&
            this.down == other.down &&
            this.equalModifiers(other);
    }

    /**
     * Returns true if the modifiers of this and the given {@link KeyBinding} match ignoring the key and down state.
     */
    public boolean equalModifiers(final KeyBinding other) {
        Objects.requireNonNull(other, "other");

        return this.alt == other.alt &&
            this.control == other.control &&
            this.meta == other.meta &&
            this.shift == other.shift;
    }

    /**
     * <pre>
     * "a" DOWN
     * Label123 "a" DOWN
     * Label123 control+meta+shift "a" DOWN
     * </pre>
     */
    @Override
    public String toString() {
        if (null == this.toString) {
            this.toString = ToStringBuilder.empty()
                .disable(ToStringBuilderOption.QUOTE)
                .value(this.label)
                .separator(" ")
                .value(this.toStringModifiers())
                .enable(ToStringBuilderOption.QUOTE)
                .value(this.key)
                .disable(ToStringBuilderOption.QUOTE)
                .value(this.down ? "DOWN" : "UP")
                .build();
        }
        return this.toString;
    }

    private String toString;

    /**
     * <pre>
     * alt
     * control
     * meta
     * shift
     * alt+control+meta+shift
     * </pre>
     */
    public String toStringModifiers() {
        return ToStringBuilder.empty()
            .separator("+")
            .disable(ToStringBuilderOption.QUOTE)
            .value(this.alt ? "alt" : "")
            .value(this.control ? "control" : "")
            .value(this.meta ? "meta" : "")
            .value(this.shift ? "shift" : "")
            .build();
    }

    // Comparable.......................................................................................................

    /**
     * Compares both {@link KeyBinding} ignoring any difference in the {@link #label()}, with only the key and modifiers
     * being significant.
     */
    @Override
    public int compareTo(final KeyBinding other) {
        return this.compareToString()
            .compareTo(
                other.compareToString()
            );
    }

    private String compareToString() {
        if (null == this.compareToString) {
            this.compareToString = this.setLabel("")
                .toString();
        }
        return this.compareToString;
    }

    private String compareToString;
}
