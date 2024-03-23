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
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonObject;
import walkingkooka.tree.json.JsonPropertyName;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Represents text with a {@link MediaType} which can be read or written to the clipboard with {@link MediaType} set
 * to text/plain. Unfortunately all current browsers only support three media types (text/plain, text/html, image/png)
 * and no suffixes therefore it is not possible to set a true mime type, thus the need for an envelope shown below.
 * <br>
 * JSON holding a range of cells
 * <pre>
 * {
 *     "mediaType": "application/json+walkingkooka.spreadsheet.cell"
 *     "value": [
 *         "A1": {
 *             "formula": ...
 *         },
 *         "B2": {
 *             "formula": ...
 *         }
 *     ]
 * }
 * </pre>
 * JSON holding a range of cells styles
 * <pre>
 * {
 *     "mediaType": "application/json+walkingkooka.spreadsheet.cell"
 *     "value": [
 *         "A1": {
 *             "color": "#000"
 *         },
 *         "B2": {
 *             "font-weight": "bold"
 *         }
 *     ]
 * }
 * </pre>
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

        final MediaType mediaType = kind.mediaType();
        final JsonNodeMarshallContext marshallContext = context.marshallContext();

        final List<JsonNode> value = Lists.array();

        while (cells.hasNext()) {
            final SpreadsheetCell cell = cells.next();
            final JsonNode json = kind.marshall(
                    cell,
                    marshallContext
            );
            value.add(json);
        }

        final JsonObject envelope = JsonNode.object()
                .set(
                        MEDIA_TYPE,
                        JsonNode.string(
                                        mediaType.value()
                        )
                ).set(
                        VALUE,
                        JsonNode.object()
                                .setChildren(value)
                );

        return ClipboardTextItem.with(
                Lists.of(MediaType.TEXT_PLAIN),
                envelope.toString()
        );
    }

    private final static JsonPropertyName MEDIA_TYPE = JsonPropertyName.with("mediaType");
    private final static JsonPropertyName VALUE = JsonPropertyName.with("value");

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
