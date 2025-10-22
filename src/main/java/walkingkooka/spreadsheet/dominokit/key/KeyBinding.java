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

import walkingkooka.Cast;
import walkingkooka.ToStringBuilder;
import walkingkooka.ToStringBuilderOption;
import walkingkooka.text.CharSequences;

import java.util.Objects;

/**
 * Captures the binding for a single keyboard to action mapping.
 */
public final class KeyBinding implements Comparable<KeyBinding> {

    public static KeyBinding with(final String key) {
        return new KeyBinding(
            CharSequences.failIfNullOrEmpty(key, "key"),
            false, // alt
            false, // control
            false, // meta
            false // shift
        );
    }

    private KeyBinding(final String key,
                       final boolean alt,
                       final boolean control,
                       final boolean meta,
                       final boolean shift) {
        this.key = key;

        this.alt = alt;
        this.control = control;
        this.meta = meta;
        this.shift = shift;
    }
    
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
                this.key,
                true,
                this.control,
                this.meta,
                this.shift
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
                this.key,
                this.alt,
                true,
                this.meta,
                this.shift
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
                this.key,
                this.alt,
                this.control,
                true,
                this.shift
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
                this.key,
                this.alt,
                this.control,
                this.meta,
                true
            );
    }


    private final boolean shift;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(
            this.key,
            this.alt,
            this.control,
            this.meta,
            this.shift
        );
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof KeyBinding &&
                this.equals0(Cast.to(other));
    }

    private boolean equals0(final KeyBinding other) {
        return this.key.equals(other.key) &&
            this.alt == other.alt &&
            this.control == other.control &&
            this.meta == other.meta &&
            this.shift == other.shift;
    }

    @Override
    public String toString() {
        if(null == this.toString) {
            this.toString = ToStringBuilder.empty()
                .separator("+")
                .disable(ToStringBuilderOption.QUOTE)
                .value(this.alt ? "alt" : "")
                .value(this.control ? "control" : "")
                .value(this.meta ? "meta" : "")
                .value(this.shift ? "shift" : "")
                .separator(" ")
                .enable(ToStringBuilderOption.QUOTE)
                .value(this.key)
                .build();
        }
        return this.toString;
    }

    private String toString;

    // Comparable.......................................................................................................

    @Override
    public int compareTo(final KeyBinding other) {
        return this.toString().compareTo(other.toString());
    }
}
