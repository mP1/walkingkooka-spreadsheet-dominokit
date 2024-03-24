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

import walkingkooka.collect.list.Lists;
import walkingkooka.net.HasUrlFragment;
import walkingkooka.net.UrlFragment;
import walkingkooka.net.header.HasMediaType;
import walkingkooka.net.header.MediaType;
import walkingkooka.predicate.Predicates;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellClipboardHistoryToken;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonPropertyName;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * Used to mark the type of the clipboard value in a {@link SpreadsheetCellClipboardHistoryToken}.
 * For the moment this only supports possible cell values from the {@link walkingkooka.spreadsheet.dominokit.ui.viewport.SpreadsheetViewportComponent},
 * which means the clipboard can only holds cells or components of a cell such as the formula text.
 */
public enum SpreadsheetCellClipboardValueKind implements HasMediaType,
        HasUrlFragment {

    /**
     * The clipboard value is {@link SpreadsheetCell}.
     */
    CELL(
            SpreadsheetCell.class,
            (c) -> c, // returns the entire cell
            "cell"
    ) {
        @Override
        JsonNode marshall(final SpreadsheetCell cell,
                          final JsonNodeMarshallContext context) {
            return context.marshall(cell)
                    .children()
                    .get(0);
        }
    },

    /**
     * The clipboard value is cells to {@link String formula text}.
     */
    FORMULA(
            SpreadsheetFormula.class,
            SpreadsheetCell::formula,
            "formula"
    ) {
        @Override
        JsonNode marshall(final SpreadsheetCell cell,
                          final JsonNodeMarshallContext context) {
            return JsonNode.string(
                    cell.formula()
                            .text()
            ).setName(
                    propertyName(cell)
            );
        }
    },

    /**
     * The clipboard value is cells to {@link SpreadsheetFormatPattern}.
     */
    FORMAT_PATTERN(
            SpreadsheetFormatPattern.class,
            SpreadsheetCell::formatPattern,
            "format-pattern"
    ) {
        @Override
        JsonNode marshall(final SpreadsheetCell cell,
                          final JsonNodeMarshallContext context) {
            return marshallCellToOptionalValue(
                    cell,
                    cell.formatPattern(),
                    context
            );
        }
    },

    /**
     * The clipboard value is a cells to {@link SpreadsheetParsePattern}.
     */
    PARSE_PATTERN(
            SpreadsheetParsePattern.class,
            SpreadsheetCell::parsePattern,
            "parse-pattern"
    ) {
        @Override
        JsonNode marshall(final SpreadsheetCell cell,
                          final JsonNodeMarshallContext context) {
            return marshallCellToOptionalValue(
                    cell,
                    cell.parsePattern(),
                    context
            );
        }
    },

    /**
     * The clipboard value is cells to {@link TextStyle}.
     */
    STYLE(
            TextStyle.class,
            SpreadsheetCell::style,
            "style"
    ) {
        @Override
        JsonNode marshall(final SpreadsheetCell cell,
                          final JsonNodeMarshallContext context) {
            return context.marshall(cell.style())
                    .setName(propertyName(cell));
        }
    },

    /**
     * The clipboard value is a formatted text.
     */
    FORMATTED(
            TextNode.class,
            SpreadsheetCell::formatted,
            "formatted"
    ) {
        @Override
        JsonNode marshall(final SpreadsheetCell cell,
                          final JsonNodeMarshallContext context) {
            return marshallCellToOptionalValue(
                    cell,
                    cell.formatted(),
                    context
            );
        }
    };

    private static JsonNode marshallCellToOptionalValue(final SpreadsheetCell cell,
                                                        final Optional<?> value,
                                                        final JsonNodeMarshallContext context) {
        return value.map(p -> context.marshallWithType(p))
                .orElse(JsonNode.nullNode())
                .setName(propertyName(cell));
    }

    private static JsonPropertyName propertyName(final SpreadsheetCell cell) {
        return JsonPropertyName.with(
                cell.reference().toString()
        );
    }

    SpreadsheetCellClipboardValueKind(final Class<?> type,
                                      final Function<SpreadsheetCell, Object> valueExtractor,
                                      final String urlFragment) {
        this.mediaType = MediaType.APPLICATION_JSON.setSuffixes(
                Lists.of(
                        type.getName()
                )
        );
        this.mediaTypeClass = type;

        this.valueExtractor = valueExtractor;

        this.urlFragment = UrlFragment.parse(urlFragment);

        this.predicate = this.name().equals("CELL") ?
                Predicates.always() :
                Predicate.isEqual(this);
    }

    /**
     * Extracts the cell or cell property selected by this {@link SpreadsheetCellClipboardValueKind}. Some values will be wrapped inside an {@link java.util.Optional}.
     * This will be used by the {@link walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellClipboardCopyHistoryToken} and {@link walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellClipboardCutHistoryToken}
     * to convert cells to the required value before they are serialized to the clipboard as text.
     */
    public Object toValue(final SpreadsheetCell cell) {
        Objects.requireNonNull(cell, "cell");

        return this.valueExtractor.apply(cell);
    }

    private final Function<SpreadsheetCell, Object> valueExtractor;

    /**
     * Marshalls a single cell or cell property ready for inclusion in the array of selected cells.
     * Note {@link Optional#empty()} will have a null value, this means pasting a null value for a cell should clear that property.
     */
    abstract JsonNode marshall(final SpreadsheetCell cell,
                               final JsonNodeMarshallContext context);

    /**
     * All {@Link SpreadsheetCellClipboardValueKind} except for {@link #CELL} only match themselves while {@link #CELL} matches all enum values.
     * If the clipboard value is a {@link SpreadsheetCell} all PASTE menu items will be enabled, while other value types will only enable themselves,
     * eg if clipboard value is a {@link TextStyle} only PASTE STYLE will be enabled.
     */
    public Predicate<SpreadsheetCellClipboardValueKind> predicate() {
        return predicate;
    }

    private final Predicate<SpreadsheetCellClipboardValueKind> predicate;

    // HasMediaType.....................................................................................................

    @Override
    public MediaType mediaType() {
        return this.mediaType;
    }

    private final MediaType mediaType;

    /**
     * Getter that returns the class type for this {@link MediaType}.
     */
    public Class<?> mediaTypeClass() {
        return this.mediaTypeClass;
    }

    private Class<?> mediaTypeClass;

    // HasUrlFragment...................................................................................................

    @Override
    public UrlFragment urlFragment() {
        return this.urlFragment;
    }

    private final transient UrlFragment urlFragment;

    /**
     * Parses the given {@link UrlFragment} component attempting to match the equivalent {@link SpreadsheetCellClipboardValueKind}.
     * This is used to turn a history token fragment into a {@link SpreadsheetCellClipboardHistoryToken} such as CUT | COPY | PASTE.
     */
    public static SpreadsheetCellClipboardValueKind parseUrlFragment(final String string) {
        Objects.requireNonNull(string, "string");

        return Arrays.stream(values())
                .filter(e -> e.urlFragment.value().equals(string))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid cell clipboard selector: " + CharSequences.quote(string)));
    }
}
