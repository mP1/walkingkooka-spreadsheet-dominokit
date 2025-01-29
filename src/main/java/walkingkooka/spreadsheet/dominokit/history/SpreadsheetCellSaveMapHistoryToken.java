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
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonPropertyName;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
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

    /**
     * Parses the value which is assumed to hold a {@link Map} in JSON form.
     */
    abstract Map<SpreadsheetCellReference, V> parseSaveValue(final TextCursor cursor);

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

    /**
     * Reads the JSON from the {@link TextCursor} as an OBJECT and then unmarshalls that into a {@link Map} with
     * {@link SpreadsheetCellReference} keys and {@link Optional} value of the given value type parameter.
     */
    static <VV> Map<SpreadsheetCellReference, VV> parseMapWithNullableValues(final TextCursor cursor,
                                                                             final Class<VV> valueType) {
        final Map<SpreadsheetCellReference, VV> values = Maps.sorted();

        for (final JsonNode keyAndValue : JsonNode.parse(parseAll(cursor))
            .objectOrFail().children()) {
            values.put(
                SpreadsheetSelection.parseCell(
                    keyAndValue.name()
                        .value()
                ),
                UNMARSHALL_CONTEXT.unmarshall(
                    keyAndValue,
                    valueType
                )
            );
        }

        return values;
    }

    /**
     * Reads the JSON from the {@link TextCursor} as an OBJECT and then unmarshalls that into a {@link Map} with
     * {@link SpreadsheetCellReference} keys and {@link Optional} value of the given value type parameter.
     */
    static <VV> Map<SpreadsheetCellReference, Optional<VV>> parseMapWithOptionalValues(final TextCursor cursor,
                                                                                       final Class<VV> valueType) {
        final Map<SpreadsheetCellReference, Optional<VV>> values = Maps.sorted();

        for (final JsonNode keyAndValue : JsonNode.parse(parseAll(cursor))
            .objectOrFail().children()) {
            values.put(
                SpreadsheetSelection.parseCell(
                    keyAndValue.name()
                        .value()
                ),
                Optional.ofNullable(
                    UNMARSHALL_CONTEXT.unmarshall(
                        keyAndValue,
                        valueType
                    )
                )
            );
        }

        return values;
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
     * Factory method used by various would be setters when one or more components have changed and a new instance needs
     * to be created.
     */
    @Override abstract SpreadsheetCellSaveMapHistoryToken<V> replace(final SpreadsheetId id,
                                                                     final SpreadsheetName name,
                                                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                                                     final Map<SpreadsheetCellReference, V> values);

    // HasUrlFragment...................................................................................................

    @Override//
    final UrlFragment urlFragmentSaveValue() {
        final List<JsonNode> children = Lists.array();
        for (final Entry<SpreadsheetCellReference, V> cellAndValue : this.value().entrySet()) {
            children.add(
                this.saveValueUrlFragmentValueToJson(
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

    abstract JsonNode saveValueUrlFragmentValueToJson(final V value);
}
