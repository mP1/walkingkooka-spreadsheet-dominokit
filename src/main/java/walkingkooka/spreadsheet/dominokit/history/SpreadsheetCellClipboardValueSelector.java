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

import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.HasUrlFragment;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.text.TextStyle;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;


/**
 * Used to mark the tyoe of a clipboard value in a {@link SpreadsheetCellClipboardHistoryToken}.
 */
public enum SpreadsheetCellClipboardValueSelector implements HasUrlFragment {

    /**
     * The clipboard value is a {@link java.util.Set} of {@link walkingkooka.spreadsheet.SpreadsheetCell}.
     */
    CELL("cell") {
        @Override
        Object checkValue(final Object value) {
            if (false == value instanceof Set) {
                throw new IllegalArgumentException("Expected a Set of cell but got a " + value.getClass().getSimpleName());
            }
            final Set<Object> cells = Sets.immutable(
                    Cast.to(value)
            );

            for (final Object cell : cells) {
                if (false == cell instanceof SpreadsheetCell) {
                    throw new IllegalArgumentException("Set should only have cells but got " + cell.getClass().getSimpleName());
                }
            }

            return cells;
        }
    },

    /**
     * The clipboard value is a {@link java.util.Map} of {@link walkingkooka.spreadsheet.reference.SpreadsheetCellReference} to {@link String formula text}.
     */
    FORMULA("formula") {
        @Override
        Object checkValue(final Object value) {
            return this.checkMap(
                    value,
                    (v) -> v instanceof String,
                    String.class
            );
        }
    },

    /**
     * The clipboard value is a {@link java.util.Map} of {@link walkingkooka.spreadsheet.reference.SpreadsheetCellReference} to {@link walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern}.
     */
    FORMAT_PATTERN("format-pattern") {
        @Override
        Object checkValue(final Object value) {
            return this.checkMap(
                    value,
                    (v) -> v instanceof SpreadsheetFormatPattern,
                    SpreadsheetFormatPattern.class
            );
        }
    },

    /**
     * The clipboard value is a {@link java.util.Map} of {@link walkingkooka.spreadsheet.reference.SpreadsheetCellReference} to {@link walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern}.
     */
    PARSE_PATTERN("parse-pattern") {
        @Override
        Object checkValue(final Object value) {
            return this.checkMap(
                    value,
                    (v) -> v instanceof SpreadsheetParsePattern,
                    SpreadsheetParsePattern.class
            );
        }
    },

    /**
     * The clipboard value is a {@link java.util.Map} of {@link walkingkooka.spreadsheet.reference.SpreadsheetCellReference} to {@link walkingkooka.tree.text.TextStyle}.
     */
    STYLE("style") {
        @Override
        Object checkValue(final Object value) {
            return this.checkMap(
                    value,
                    (v) -> v instanceof TextStyle,
                    TextStyle.class
            );
        }
    },

    /**
     * The clipboard value is a {@link java.util.Map} of {@link walkingkooka.spreadsheet.reference.SpreadsheetCellReference} to {@link String formatted text}.
     */
    FORMATTED("formatted") {
        @Override
        Object checkValue(final Object value) {
            return this.checkMap(
                    value,
                    (v) -> v instanceof String,
                    String.class
            );
        }
    };

    SpreadsheetCellClipboardValueSelector(final String urlFragment) {
        this.urlFragment = UrlFragment.parse(urlFragment);
    }

    abstract Object checkValue(final Object value);

    final Object checkMap(final Object map,
                          final Predicate<Object> valueTypeTester,
                          final Class<?> valueType) {
        if (false == map instanceof Map) {
            throw new IllegalArgumentException("Expected a Map of cell-references to " + valueType.getSimpleName() + " but got " + map.getClass().getSimpleName());
        }
        final Map<Object, Object> cellToValue = Maps.immutable(
                Cast.to(map)
        );

        for (final Entry<Object, Object> cellAndValue : cellToValue.entrySet()) {
            final Object cell = cellAndValue.getKey();
            if (false == cell instanceof SpreadsheetCellReference) {
                throw new IllegalArgumentException("Map should only have keys of type cell but got " + cell.getClass().getSimpleName());
            }
            final Object value = cellAndValue.getValue();
            if (false == valueTypeTester.test(value)) {
                throw new IllegalArgumentException("Map should only have values of type " + valueType.getSimpleName() + " but got " + value.getClass().getSimpleName());
            }
        }

        return cellToValue;
    }

    // HasUrlFragment...................................................................................................

    @Override
    public UrlFragment urlFragment() {
        return this.urlFragment;
    }

    private final UrlFragment urlFragment;

    public static SpreadsheetCellClipboardValueSelector parse(final String string) {
        Objects.requireNonNull(string, "string");

        return Arrays.stream(values())
                .filter(e -> e.urlFragment.value().equals(string))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid cell clipboard selector: " + CharSequences.quote(string)));
    }
}
