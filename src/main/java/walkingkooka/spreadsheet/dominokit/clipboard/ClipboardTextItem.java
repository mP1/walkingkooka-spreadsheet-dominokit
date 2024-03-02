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

import walkingkooka.net.header.MediaType;
import walkingkooka.text.CharSequences;

import java.util.Objects;

/**
 * Represents text with a {@link MediaType} which can be read or written to the clipboard.
 */
public final class ClipboardTextItem {
    public static ClipboardTextItem with(final MediaType type,
                                         final String text) {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(text, "text");

        return new ClipboardTextItem(
                type,
                text
        );
    }

    private ClipboardTextItem(final MediaType type,
                              final String text) {
        this.type = type;
        this.text = text;
    }

    public MediaType type() {
        return this.type;
    }

    private MediaType type;

    public String text() {
        return this.text;
    }

    private String text;

    // Object...........................................................................................................

    public int hashCode() {
        return Objects.hash(
                this.type,
                this.text
        );
    }

    public boolean equals(final Object other) {
        return this == other || other instanceof ClipboardTextItem && this.equals0((ClipboardTextItem) other);
    }

    private boolean equals0(final ClipboardTextItem other) {
        return this.type.equals(other.type) &&
                this.text.equals(other.text);
    }

    @Override
    public String toString() {
        return this.type + " " + CharSequences.quoteAndEscape(this.text);
    }
}
