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

import walkingkooka.Value;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonPropertyName;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContexts;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContexts;

import java.math.MathContext;
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
public abstract class SpreadsheetCellSaveHistoryToken<V> extends SpreadsheetCellHistoryToken implements Value<Map<SpreadsheetCellReference, V>> {

    SpreadsheetCellSaveHistoryToken(final SpreadsheetId id,
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

    @Override
    public final HistoryToken clearAction() {
        return HistoryToken.cell(
                this.id(),
                this.name(),
                this.anchoredSelection()
        );
    }

    @Override //
    final HistoryToken setDifferentAnchoredSelection(final AnchoredSpreadsheetSelection anchoredSelection) {
        return this.replace(
                this.id(),
                this.name(),
                anchoredSelection,
                this.value
        );
    }

    @Override
    public final HistoryToken setFormula() {
        return setFormula0();
    }

    @Override //
    final HistoryToken setFormatPattern() {
        return this;
    }

    @Override
    public final HistoryToken setIdAndName(final SpreadsheetId id,
                                           final SpreadsheetName name) {
        return this.replace(
                id,
                name,
                this.anchoredSelection(),
                this.value
        );
    }

    @Override //
    final HistoryToken setParsePattern() {
        return this;
    }

    @Override //
    final HistoryToken replacePatternKind(final Optional<SpreadsheetPatternKind> patternKind) {
        return this;
    }

    @Override //
    final HistoryToken setSave0(final String value) {
        return this.replace(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                SpreadsheetCellSaveHistoryToken.<V>parseJson(
                        TextCursors.charSequence(value),
                        this.valueType()
                )
        );
    }

    /**
     * Used to consume the remainder of the {@link TextCursor} text giving some JSON where individual cells are mapped
     * to a value. The type parameter will be used to unmarshall the value into a java object.
     */
    static <VV> Map<SpreadsheetCellReference, VV> parseJson(final TextCursor cursor,
                                                            final Class<VV> valueType) {
        return UNMARSHALL_CONTEXT.unmarshallMap(
                JsonNode.parse(
                        parseAll(cursor)
                ),
                SpreadsheetCellReference.class, // key is always a cell
                valueType
        );
    }

    private final static JsonNodeUnmarshallContext UNMARSHALL_CONTEXT = JsonNodeUnmarshallContexts.basic(
            ExpressionNumberKind.BIG_DECIMAL,
            MathContext.DECIMAL64
    );


    /**
     * Getter that returns the {@link Class} of the {@link Map} value.
     */
    abstract Class<V> valueType();

    /**
     * Factory method used by various would be setters when one or more components have changed and a new instance needs
     * to be created.
     */
    abstract SpreadsheetCellSaveHistoryToken<V> replace(final SpreadsheetId id,
                                                        final SpreadsheetName name,
                                                        final AnchoredSpreadsheetSelection anchoredSelection,
                                                        final Map<SpreadsheetCellReference, V> values);

    // HasUrlFragment...................................................................................................

    @Override//
    final UrlFragment cellUrlFragment() {
        // convert Map to JsonObject, marshall that into a String
        final List<JsonNode> children = Lists.array();
        for (final Entry<SpreadsheetCellReference, V> cellAndValue : this.value().entrySet()) {
            children.add(
                    MARSHALL_CONTEXT.marshall(
                            cellAndValue.getValue()
                    ).setName(
                            JsonPropertyName.with(
                                    cellAndValue.getKey()
                                            .toStringMaybeStar()
                            )
                    )
            );
        }

        return SAVE.append(
                this.cellSaveUrlFragment()
        ).append(
                UrlFragment.SLASH
        ).append(
                UrlFragment.with(
                        JsonNode.object()
                                .setChildren(children)
                                .toString()
                )
        );
    }

    private final static JsonNodeMarshallContext MARSHALL_CONTEXT = JsonNodeMarshallContexts.basic();

    /**
     * This is a single word such as formula/cell etc. The values will be converted into JSON and appended.
     */
    abstract UrlFragment cellSaveUrlFragment();
}
