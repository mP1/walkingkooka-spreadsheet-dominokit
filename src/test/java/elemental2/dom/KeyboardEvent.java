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

package elemental2.dom;

import jsinterop.annotations.JsProperty;
import walkingkooka.ToStringBuilder;
import walkingkooka.ToStringBuilderOption;

public class KeyboardEvent extends UIEvent {
    public static int DOM_KEY_LOCATION_LEFT;
    public static int DOM_KEY_LOCATION_NUMPAD;
    public static int DOM_KEY_LOCATION_RIGHT;
    public static int DOM_KEY_LOCATION_STANDARD;
    public boolean altKey;

    @JsProperty(name = "char")
    public String char_;

    public String code;
    public boolean ctrlKey;
    public String key;
    public String keyIdentifier;
    public String locale;
    public int location;
    public boolean metaKey;
    public boolean repeat;
    public boolean shiftKey;

    public KeyboardEvent(final String type) {
        super(type);
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return ToStringBuilder.empty()
            .separator(" ")
            .value(this.type.toUpperCase())
            .value(this.key)
            .disable(ToStringBuilderOption.QUOTE)
            .label("alt")
            .value(this.altKey)
            .label("control")
            .value(this.ctrlKey)
            .label("meta")
            .value(this.metaKey)
            .label("shift")
            .value(this.shiftKey)
            .build();
    }
}

