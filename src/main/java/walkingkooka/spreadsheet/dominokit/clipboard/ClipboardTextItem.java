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
import walkingkooka.collect.set.SortedSets;
import walkingkooka.net.header.MediaType;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetCellRange;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.HasText;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonObject;
import walkingkooka.tree.json.JsonPropertyName;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents text with a {@link MediaType} which can be read or written to the clipboard with {@link MediaType} set
 * to text/plain. Unfortunately all current browsers only support three media types (text/plain, text/html, image/png)
 * and no suffixes therefore it is not possible to set a true mime type, thus the need for an envelope shown below.
 * <br>
 * JSON holding a range of cells
 * <pre>
 * {
 *   "mediaType": "application/json+walkingkooka.spreadsheet.SpreadsheetCell",
 *   "cell-range": "A1:B2",
 *   "value": {
 *     "A1": {
 *       "formula": {
 *         "text": "=1"
 *       },
 *       "style": {
 *         "text-align": "CENTER"
 *       }
 *     },
 *     "B2": {
 *       "formula": {
 *         "text": "=22"
 *       },
 *       "formatter": "text-format- @"
 *     }
 *   }
 * }
 * </pre>
 * JSON Holding formulas
 * <pre>
 * {
 *   "mediaType": "application/json+walkingkooka.spreadsheet.formula.SpreadsheetFormula",
 *   "cell-range": "A1",
 *   "value": {
 *     "A1": ""
 *   }
 * }
 * </pre>
 * JSON holding {@link SpreadsheetFormatterSelector}
 * <pre>
 * {
 *   "mediaType": "application/json+walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector",
 *   "cell-range": "A1:B2",
 *   "value": {
 *     "A1": "date dd/mm/yyyy",
 *     "B2": "number $0.00"
 *   }
 * }
 * </pre>
 * JSON holding {@link SpreadsheetParserSelector}
 * <pre>
 * {
 *   "mediaType": "application/json+walkingkooka.spreadsheet.format.SpreadsheetParserSelector",
 *   "cell-range": "A1:B2",
 *   "value": {
 *     "A1": "date dd/mm/yyyy",
 *     "B2": "number $0.00"
 *   }
 * }
 * </pre>
 * JSON holding a range of cells styles
 * <pre>
 * {
 *   "mediaType": "application/json+walkingkooka.tree.text.TextStyle",
 *   "cell-range": "A1:C3",
 *   "value": {
 *     "A1": {
 *       "color": "#000"
 *     },
 *     "B2": {
 *       "font-weight": "bold",
 *       "text-align": "left"
 *     }
 *   }
 * }
 * </pre>
 * JSON holding formatted values
 * <pre>
 * {
 *   "mediaType": "application/json+walkingkooka.tree.text.TextNode",
 *   "cell-range": "A1:B2",
 *   "value": {
 *     "A1": {
 *       "type": "text",
 *       "value": "111"
 *     },
 *     "B2": {
 *       "type": "text",
 *       "value": "222"
 *     }
 *   }
 * }
 * </pre>
 */
public final class ClipboardTextItem implements HasText,
    TreePrintable {

    /**
     * Extracts part or all of the cell using the {@link SpreadsheetCellClipboardKind} to JSON.
     */
    public static ClipboardTextItem toJson(final SpreadsheetCellRange range,
                                           final SpreadsheetCellClipboardKind kind,
                                           final JsonNodeMarshallContext context) {
        Objects.requireNonNull(range, "range");
        Objects.requireNonNull(kind, "kind");
        Objects.requireNonNull(context, "context");

        final MediaType mediaType = kind.mediaType();
        final SpreadsheetCellRangeReference rangeReference = range.range();

        final List<JsonNode> value = Lists.array();

        for (final SpreadsheetCell cell : range.value()) {
            final SpreadsheetCellReference reference = cell.reference();
            if (rangeReference.testCell(reference)) {

                value.add(
                    kind.marshall(
                        cell,
                        context
                    )
                );
            }
        }

        final JsonObject envelope = JsonNode.object()
            .set(
                MEDIA_TYPE_PROPERTY_NAME,
                mediaType.value()
            ).set(
                CELL_RANGE_PROPERTY_NAME,
                rangeReference.toStringMaybeStar()
            ).set(
                VALUE_PROPERTY_NAME,
                JsonNode.object()
                    .setChildren(value)
            );

        return ClipboardTextItem.with(
            Lists.of(MEDIA_TYPE),
            envelope.toString()
        );
    }

    /**
     * Because of clipboard API limitations text/plain is used even for JSON.
     */
    // @VisibleForTesting
    public final static MediaType MEDIA_TYPE = MediaType.TEXT_PLAIN;

    private final static JsonPropertyName MEDIA_TYPE_PROPERTY_NAME = JsonPropertyName.with("mediaType");

    private final static JsonPropertyName CELL_RANGE_PROPERTY_NAME = JsonPropertyName.with("cell-range");

    private final static JsonPropertyName VALUE_PROPERTY_NAME = JsonPropertyName.with("value");

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

    public ClipboardTextItem setTypes(final List<MediaType> types) {
        Objects.requireNonNull(types, "types");

        final List<MediaType> copy = Lists.immutable(types);
        return this.types.equals(copy) ?
            this :
            new ClipboardTextItem(
                copy,
                this.text
            );
    }

    private final List<MediaType> types;

    @Override
    public String text() {
        return this.text;
    }

    public ClipboardTextItem setText(final String text) {
        Objects.requireNonNull(text, "text");

        return this.text.equals(text) ?
            this :
            new ClipboardTextItem(
                this.types,
                text
            );
    }

    private final String text;

    /**
     * Verifies the type and reads the values from the JSON payload. This is typically an intermediate step
     * before moving relative references in any formulas and then patching the target range.
     */
    public SpreadsheetCellRange toSpreadsheetCellRange(final AppContext context) {
        Objects.requireNonNull(context, "context");

        this.checkMediaType();

        final JsonObject json = this.readJson();

        final SpreadsheetCellClipboardKind kind = SpreadsheetCellClipboardKind.fromMediaType(
            MediaType.parse(
                json.getOrFail(MEDIA_TYPE_PROPERTY_NAME)
                    .stringOrFail()
            )
        );

        final SpreadsheetCellRangeReference range = SpreadsheetSelection.parseCellRange(
            json.getOrFail(CELL_RANGE_PROPERTY_NAME)
                .stringOrFail()
        );

        final Set<SpreadsheetCell> values = SortedSets.tree(SpreadsheetCell.REFERENCE_COMPARATOR);

        for (final JsonNode value : json.getOrFail(VALUE_PROPERTY_NAME)
            .objectOrFail()
            .children()) {
            values.add(
                kind.unmarshall(
                    value,
                    context
                )
            );
        }

        return SpreadsheetCellRange.with(
            range,
            values
        );
    }

    private void checkMediaType() {
        final List<MediaType> types = this.types;
        if (types.stream()
            .noneMatch(MEDIA_TYPE::equals)) {
            throw new IllegalArgumentException(
                "Unsupported clipboard media type " +
                    types.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", ")) +
                    " expected " +
                    MEDIA_TYPE
            );
        }
    }

    private JsonObject readJson() {
        try {
            return JsonNode.parse(this.text())
                .objectOrFail();
        } catch (final RuntimeException cause) {
            throw new IllegalArgumentException("Invalid json: " + cause.getMessage(), cause);
        }
    }

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
