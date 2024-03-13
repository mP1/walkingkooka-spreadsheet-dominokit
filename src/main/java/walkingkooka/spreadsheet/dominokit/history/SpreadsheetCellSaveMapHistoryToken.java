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

package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonPropertyName;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Base {@link HistoryToken} for several tokens that support saving or patching individual properties for a range of cells,
 * including formulas, style and more. This will be useful when PASTE functionality is added to the UI, so the user
 * can PASTE formulas, or style over a range of selected cells.
 */
public abstract class SpreadsheetCellSaveMapHistoryToken<V> extends SpreadsheetCellSaveHistoryToken<Map<SpreadsheetCellReference, V>> {

    SpreadsheetCellSaveMapHistoryToken(final SpreadsheetId id,
                                       final SpreadsheetName name,
                                       final AnchoredSpreadsheetSelection anchoredSelection,
                                       final Map<SpreadsheetCellReference, V> values) {
        super(
                id,
                name,
                anchoredSelection
        );

        // complain if any of the same formulas are outside the selection range.
        final SpreadsheetSelection selection = anchoredSelection.selection();
        if (false == selection.isLabelName()) {
            final String outside = values.keySet()
                    .stream()
                    .filter(selection.negate())
                    .map(SpreadsheetSelection::toString)
                    .collect(Collectors.joining(", "));
            if (false == outside.isEmpty()) {
                throw new IllegalArgumentException("Save value includes cells " + outside + " outside " + selection);
            }
        }

        this.value = values;
    }

    @Override
    public final Map<SpreadsheetCellReference, V> value() {
        return this.value;
    }

    final Map<SpreadsheetCellReference, V> value;

    @Override //
    final HistoryToken setSave0(final String value) {
        final TextCursor cursor = TextCursors.charSequence(value);
        final Optional<Class<V>> valueType = this.valueType();

        return this.replace(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                valueType.isEmpty() ?
                        SpreadsheetCellSaveMapHistoryToken.parseMapWithOptionalTypedValues(
                                cursor
                        ) :
                        SpreadsheetCellSaveMapHistoryToken.parseMap(
                                cursor,
                                valueType.get()
                        )
        );
    }

    /**
     * Used to consume the remainder of the {@link TextCursor} text giving some JSON where individual cells are mapped
     * to a value. The type parameter will be used to unmarshall the value into a java object.
     */
    static <VV> Map<SpreadsheetCellReference, VV> parseMap(final TextCursor cursor,
                                                           final Class<VV> valueType) {
        return UNMARSHALL_CONTEXT.unmarshallMap(
                JsonNode.parse(
                        parseAll(cursor)
                ),
                SpreadsheetCellReference.class, // key is always a cell
                valueType
        );
    }

    static <VV> Map<SpreadsheetCellReference, VV> parseMapWithOptionalTypedValues(final TextCursor cursor) {
        final Map<SpreadsheetCellReference, VV> values = Maps.sorted();

        for (final JsonNode keyAndValue : JsonNode.parse(parseAll(cursor))
                .objectOrFail().children()) {
            values.put(
                    SpreadsheetSelection.parseCell(
                            keyAndValue.name()
                                    .value()
                    ),
                    (VV) Optional.ofNullable(
                            UNMARSHALL_CONTEXT.unmarshallWithType(
                                    keyAndValue
                            )
                    )
            );
        }

        return values;
    }

    /**
     * Getter that returns the {@link Class} of the {@link Map} value. If empty the value is polymorphic, and
     * marshalling and unmarshalling must include the type in the final JSON form.
     */
    abstract Optional<Class<V>> valueType();

    /**
     * Factory method used by various would be setters when one or more components have changed and a new instance needs
     * to be created.
     */
    abstract SpreadsheetCellSaveMapHistoryToken<V> replace(final SpreadsheetId id,
                                                           final SpreadsheetName name,
                                                           final AnchoredSpreadsheetSelection anchoredSelection,
                                                           final Map<SpreadsheetCellReference, V> values);

    // HasUrlFragment...................................................................................................

    @Override//
    final UrlFragment saveValueUrlFragment() {
        final Function<V, JsonNode> valueMarshaller = this.valueType()
                .isPresent() ?
                this::marshallValue :
                this::marshallValueWithType;

        final List<JsonNode> children = Lists.array();
        for (final Entry<SpreadsheetCellReference, V> cellAndValue : this.value().entrySet()) {
            children.add(
                    valueMarshaller.apply(
                            cellAndValue.getValue()
                    ).setName(
                            JsonPropertyName.with(
                                    cellAndValue.getKey()
                                            .toStringMaybeStar()
                            )
                    )
            );
        }

        return UrlFragment.with(
                JsonNode.object()
                        .setChildren(children)
                        .toString()
        );
    }
}
