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

import walkingkooka.Value;
import walkingkooka.net.header.MediaType;
import walkingkooka.text.CharSequences;

import java.util.Objects;

/**
 * A typed clipboard value. When a clipboard copy event happens the clipboard text will be converted into a {@link SpreadsheetCellClipboardValue}.
 * Paste operations such as pasting styles from a clipboard holding cells into a range will use a {@link walkingkooka.convert.Converter}
 * to perform the conversion if possible.
 */
public final class SpreadsheetCellClipboardValue<T> implements Value<T> {

    public static <T> SpreadsheetCellClipboardValue with(final MediaType mediaType,
                                                         final T value) {
        return new SpreadsheetCellClipboardValue<>(
                checkMediaType(mediaType),
                checkValue(value)
        );
    }

    private SpreadsheetCellClipboardValue(final MediaType mediaType,
                                          final T value) {
        this.mediaType = mediaType;
        this.value = value;
    }

    public MediaType mediaType() {
        return this.mediaType;
    }

    public SpreadsheetCellClipboardValue<T> setMediaType(final MediaType mediaType) {
        checkMediaType(mediaType);
        return this.mediaType.equals(mediaType) ?
                this :
                new SpreadsheetCellClipboardValue(mediaType, this.value);
    }

    private final MediaType mediaType;

    private static MediaType checkMediaType(final MediaType mediaType) {
        return Objects.requireNonNull(mediaType, "mediaType");
    }

    // Value............................................................................................................

    @Override
    public T value() {
        return this.value;
    }

    /**
     * Would be setter that returns a {@link SpreadsheetCellClipboardValue} with the given value, creating a new instance if necessary.
     */
    public SpreadsheetCellClipboardValue<T> setValue(final T value) {
        Objects.requireNonNull(value, "value");

        return this.value.equals(value) ?
                this :
                new SpreadsheetCellClipboardValue<>(this.mediaType, value);
    }

    private final T value;

    private static <T> T checkValue(final T value) {
        return Objects.requireNonNull(value, "value");
    }

    // Object............................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(
                this.mediaType,
                this.value
        );
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                (other instanceof SpreadsheetCellClipboardValue && this.equals0((SpreadsheetCellClipboardValue) other));
    }

    private boolean equals0(final SpreadsheetCellClipboardValue other) {
        return this.mediaType.equals(other.mediaType) &&
                this.value.equals(other.value);
    }

    @Override
    public String toString() {
        return this.mediaType + " " + CharSequences.quoteIfChars(this.value);
    }
}
