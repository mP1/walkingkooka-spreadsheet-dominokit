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
import walkingkooka.collect.map.Maps;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContexts;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContexts;

import java.math.MathContext;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Base {@link HistoryToken} for several tokens that support saving or patching individual properties for a range of cells,
 * including formulas, style and more. This will be useful when PASTE functionality is added to the UI, so the user
 * can PASTE formulas, or style over a range of selected cells.
 */
public abstract class SpreadsheetCellSaveHistoryToken<V> extends SpreadsheetCellHistoryToken implements Value<V> {

    SpreadsheetCellSaveHistoryToken(final SpreadsheetId id,
                                    final SpreadsheetName name,
                                    final AnchoredSpreadsheetSelection anchoredSelection) {
        super(
                id,
                name,
                anchoredSelection
        );
    }

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
                this.value()
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
                this.value()
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

    static Set<SpreadsheetCell> parseCells(final TextCursor cursor) {
        return UNMARSHALL_CONTEXT.unmarshallSet(
                JsonNode.parse(
                        parseAll(cursor)
                ),
                SpreadsheetCell.class
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

    static <VV> Map<SpreadsheetCellReference, VV> parseMapWithTypedValues(final TextCursor cursor) {
        final Map<SpreadsheetCellReference, VV> values = Maps.sorted();

        for (final JsonNode keyAndValue : JsonNode.parse(parseAll(cursor))
                .objectOrFail().children()) {
            values.put(
                    SpreadsheetSelection.parseCell(keyAndValue.name().value()),
                    UNMARSHALL_CONTEXT.unmarshallWithType(
                            keyAndValue
                    )
            );
        }

        return values;
    }

    final static JsonNodeUnmarshallContext UNMARSHALL_CONTEXT = JsonNodeUnmarshallContexts.basic(
            ExpressionNumberKind.BIG_DECIMAL,
            MathContext.DECIMAL64
    );

    /**
     * Factory method used by various would be setters when one or more components have changed and a new instance needs
     * to be created.
     */
    abstract SpreadsheetCellSaveHistoryToken<V> replace(final SpreadsheetId id,
                                                        final SpreadsheetName name,
                                                        final AnchoredSpreadsheetSelection anchoredSelection,
                                                        final V value);

    // HasUrlFragment...................................................................................................

    @Override//
    final UrlFragment cellUrlFragment() {
        return SAVE.append(
                this.saveEntityUrlFragment()
        ).append(
                UrlFragment.SLASH
        ).append(
                this.saveValueUrlFragment()
        );
    }

    /**
     * This is a single word such as formula/cell etc. The values will be converted into JSON and appended.
     */
    abstract UrlFragment saveEntityUrlFragment();

    abstract UrlFragment saveValueUrlFragment();

    /**
     * Some {@lin Map} values are not polymorphic, eg formulas are always {@link String strings}.
     */
    final JsonNode marshallValue(final Object value) {
        return MARSHALL_CONTEXT.marshall(
                value
        );
    }

    /**
     * Some {@link Map} values are polymorphic, eg {@link walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern patterns},
     * and require the type to be recorded along with the marshalled JSON-form.
     */
    final JsonNode marshallValueWithType(final Object value) {
        return MARSHALL_CONTEXT.marshallWithType(
                value
        );
    }

    final static JsonNodeMarshallContext MARSHALL_CONTEXT = JsonNodeMarshallContexts.basic();
}
