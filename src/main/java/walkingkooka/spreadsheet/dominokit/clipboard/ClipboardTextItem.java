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
import walkingkooka.collect.list.Lists;
import walkingkooka.net.header.MediaType;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Represents text with a {@link MediaType} which can be readClipboardItem or written to the clipboard.
 */
public final class ClipboardTextItem implements TreePrintable {

    /**
     * Extracts part or all of the cell using the {@link SpreadsheetCellClipboardValueKind} to JSON.
     */
    public static ClipboardTextItem prepare(final Iterator<SpreadsheetCell> cells,
                                            final SpreadsheetCellClipboardValueKind kind,
                                            final AppContext context) {
        Objects.requireNonNull(cells, "cells");
        Objects.requireNonNull(kind, "kind");
        Objects.requireNonNull(context, "context");

        final Collection<Object> payload = StreamSupport.stream(
                Spliterators
                        .spliteratorUnknownSize(
                                cells,
                                0 // characteristics
                        ),
                false
        ).map(
                kind::toValue
        ).collect(Collectors.toList());

        final MediaType mediaType = kind.mediaType();
        final String text = context.marshallContext()
                .marshallCollection(payload)
                .toString();
        return ClipboardTextItem.with(
                Lists.of(mediaType),
                text
        );
    }

    public static ClipboardTextItem with(final List<MediaType> types,
                                         final String text) {
        Objects.requireNonNull(types, "types");
        if (types.isEmpty()) {
            throw new IllegalArgumentException("Types must not be empty");
        }
        Objects.requireNonNull(text, "text");

        return new ClipboardTextItem(
                Lists.immutable(types),
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

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println("types");

        printer.indent();
        {
            for (final MediaType type : this.types) {
                printer.println(type.toString());
            }
        }
        printer.outdent();

        printer.println("text");
        printer.indent();
        {
            printer.println(this.text);
        }
        printer.outdent();
    }
}
