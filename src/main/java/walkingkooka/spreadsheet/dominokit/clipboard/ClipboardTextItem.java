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

package walkingkooka.spreadsheet.dominokit.clipboard;

import walkingkooka.ToStringBuilder;
import walkingkooka.net.header.MediaType;

import java.util.List;
import java.util.Objects;

/**
 * Represents text with a {@link MediaType} which can be readClipboardItem or written to the clipboard.
 */
public final class ClipboardTextItem {
    public static ClipboardTextItem with(final List<MediaType> types,
                                         final String text) {
        Objects.requireNonNull(types, "types");
        if (types.isEmpty()) {
            throw new IllegalArgumentException("Types must not be empty");
        }
        Objects.requireNonNull(text, "text");

        return new ClipboardTextItem(
                types,
                text
        );
    }

    private ClipboardTextItem(final List<MediaType> types,
                              final String text) {
        this.types = types;
        this.text = text;
    }

    public List<MediaType> types() {
        return this.types;
    }

    private final List<MediaType> types;

    public String text() {
        return this.text;
    }

    private final String text;

    // Object...........................................................................................................

    public int hashCode() {
        return Objects.hash(
                this.types,
                this.text
        );
    }

    public boolean equals(final Object other) {
        return this == other || other instanceof ClipboardTextItem && this.equals0((ClipboardTextItem) other);
    }

    private boolean equals0(final ClipboardTextItem other) {
        return this.types.equals(other.types) &&
                this.text.equals(other.text);
    }

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .value(this.types)
                .value(this.text)
                .build();
    }
}
