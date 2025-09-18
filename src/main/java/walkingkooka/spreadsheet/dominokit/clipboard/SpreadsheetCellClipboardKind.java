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
import walkingkooka.collect.map.Maps;
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.net.HasUrlFragment;
import walkingkooka.net.UrlFragment;
import walkingkooka.net.header.HasMediaType;
import walkingkooka.net.header.MediaType;
import walkingkooka.predicate.Predicates;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetCellRange;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellClipboardHistoryToken;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportComponent;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonPropertyName;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.validation.ValidationValueTypeName;
import walkingkooka.validation.provider.ValidatorSelector;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * Used to mark the type of the clipboard value in a {@link SpreadsheetCellClipboardHistoryToken}.
 * For the moment this only supports possible cell values from the {@link SpreadsheetViewportComponent},
 * which means the clipboard can only holds cells or components of a cell such as the formula text.
 */
public enum SpreadsheetCellClipboardKind implements HasMediaType,
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

        @Override //
        SpreadsheetCell unmarshall(final JsonNode node,
                                   final AppContext context) {
            SpreadsheetCell cell = context.unmarshall(
                JsonNode.object()
                    .appendChild(node),
                SpreadsheetCell.class
            );
            final SpreadsheetFormula formula = cell.formula();

            // if theres no token try and parse the formula text. This will be necessary because PASTE will
            // need to update relative references.
            if (false == formula.token().isPresent()) {
                try {
                    final SpreadsheetMetadata metadata = context.spreadsheetMetadata();

                    cell = cell.setFormula(
                        SpreadsheetFormula.parse(
                            TextCursors.charSequence(
                                formula.text()
                            ),
                            metadata.spreadsheetParser(
                                context, // SpreadsheetParserProvider
                                context
                            ),
                            metadata.spreadsheetParserContext(
                                Optional.of(cell),
                                context,
                                context
                            )
                        )
                    );
                } catch (final RuntimeException ignore) {

                }
            }
            return cell;
        }

        @Override
        public void saveOrUpdateCells(final SpreadsheetDeltaFetcher fetcher,
                                      final SpreadsheetId id,
                                      final SpreadsheetCellRange range) {
            fetcher.postCells(
                id,
                range.range(),
                range.value()
            );
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

        @Override //
        SpreadsheetCell unmarshall(final JsonNode node,
                                   final AppContext context) {
            final SpreadsheetMetadata metadata = context.spreadsheetMetadata();

            return SpreadsheetSelection.parseCell(
                node.name()
                    .value()
            ).setFormula(
                SpreadsheetFormula.parse(
                    TextCursors.charSequence(
                        node.stringOrFail()
                    ),
                    metadata.spreadsheetParser(
                        context, // SpreadsheetParserProvider
                        context // ProviderContext
                    ),
                    metadata.spreadsheetParserContext(
                        SpreadsheetMetadata.NO_CELL, // TODO seems wrong to pass NO_CELL
                        context,
                        context
                    )// parser context
                )
            );
        }

        @Override
        public void saveOrUpdateCells(final SpreadsheetDeltaFetcher fetcher,
                                      final SpreadsheetId id,
                                      final SpreadsheetCellRange range) {
            fetcher.patchCellsFormulaText(
                id,
                range.range(),
                toMap(
                    range,
                    (SpreadsheetCell cell) -> cell.formula().text()
                )
            );
        }
    },

    /**
     * The clipboard value is cells to {@link walkingkooka.datetime.DateTimeSymbols}.
     */
    DATE_TIME_SYMBOLS(
        DateTimeSymbols.class,
        SpreadsheetCell::dateTimeSymbols,
        "dateTimeSymbols"
    ) {
        @Override
        JsonNode marshall(final SpreadsheetCell cell,
                          final JsonNodeMarshallContext context) {
            return marshallCellToOptionalValue(
                cell,
                cell.dateTimeSymbols(),
                context
            );
        }

        @Override //
        SpreadsheetCell unmarshall(final JsonNode node,
                                   final AppContext context) {
            return SpreadsheetSelection.parseCell(
                node.name()
                    .value()
            ).setFormula(
                SpreadsheetFormula.EMPTY
            ).setDateTimeSymbols(
                context.unmarshallOptional(
                    node,
                    DateTimeSymbols.class
                )
            );
        }

        @Override
        public void saveOrUpdateCells(final SpreadsheetDeltaFetcher fetcher,
                                      final SpreadsheetId id,
                                      final SpreadsheetCellRange range) {
            fetcher.patchCellsDateTimeSymbols(
                id,
                range.range(),
                toMap(
                    range,
                    SpreadsheetCell::dateTimeSymbols
                )
            );
        }
    },

    /**
     * The clipboard value is cells to {@link DecimalNumberSymbols}.
     */
    DECIMAL_NUMBER_SYMBOLS(
        DecimalNumberSymbols.class,
        SpreadsheetCell::decimalNumberSymbols,
        "decimalNumberSymbols"
    ) {
        @Override
        JsonNode marshall(final SpreadsheetCell cell,
                          final JsonNodeMarshallContext context) {
            return marshallCellToOptionalValue(
                cell,
                cell.decimalNumberSymbols(),
                context
            );
        }

        @Override //
        SpreadsheetCell unmarshall(final JsonNode node,
                                   final AppContext context) {
            return SpreadsheetSelection.parseCell(
                node.name()
                    .value()
            ).setFormula(
                SpreadsheetFormula.EMPTY
            ).setDecimalNumberSymbols(
                context.unmarshallOptional(
                    node,
                    DecimalNumberSymbols.class
                )
            );
        }

        @Override
        public void saveOrUpdateCells(final SpreadsheetDeltaFetcher fetcher,
                                      final SpreadsheetId id,
                                      final SpreadsheetCellRange range) {
            fetcher.patchCellsDecimalNumberSymbols(
                id,
                range.range(),
                toMap(
                    range,
                    SpreadsheetCell::decimalNumberSymbols
                )
            );
        }
    },

    /**
     * The clipboard value is cells to {@link Locale}.
     */
    LOCALE(
        Locale.class,
        SpreadsheetCell::locale,
        "locale"
    ) {
        @Override
        JsonNode marshall(final SpreadsheetCell cell,
                          final JsonNodeMarshallContext context) {
            return marshallCellToOptionalValue(
                cell,
                cell.locale(),
                context
            );
        }

        @Override //
        SpreadsheetCell unmarshall(final JsonNode node,
                                   final AppContext context) {
            return SpreadsheetSelection.parseCell(
                node.name()
                    .value()
            ).setFormula(
                SpreadsheetFormula.EMPTY
            ).setLocale(
                context.unmarshallOptional(
                    node,
                    Locale.class
                )
            );
        }

        @Override
        public void saveOrUpdateCells(final SpreadsheetDeltaFetcher fetcher,
                                      final SpreadsheetId id,
                                      final SpreadsheetCellRange range) {
            fetcher.patchCellsLocale(
                id,
                range.range(),
                toMap(
                    range,
                    SpreadsheetCell::locale
                )
            );
        }
    },

    /**
     * The clipboard value is cells to {@link SpreadsheetFormatterSelector}.
     */
    FORMATTER(
        SpreadsheetFormatterSelector.class,
        SpreadsheetCell::formatter,
        "formatter"
    ) {
        @Override
        JsonNode marshall(final SpreadsheetCell cell,
                          final JsonNodeMarshallContext context) {
            return marshallCellToOptionalValue(
                cell,
                cell.formatter(),
                context
            );
        }

        @Override //
        SpreadsheetCell unmarshall(final JsonNode node,
                                   final AppContext context) {
            return SpreadsheetSelection.parseCell(
                node.name()
                    .value()
            ).setFormula(
                SpreadsheetFormula.EMPTY
            ).setFormatter(
                context.unmarshallOptional(
                    node,
                    SpreadsheetFormatterSelector.class
                )
            );
        }

        @Override
        public void saveOrUpdateCells(final SpreadsheetDeltaFetcher fetcher,
                                      final SpreadsheetId id,
                                      final SpreadsheetCellRange range) {
            fetcher.patchCellsFormatter(
                id,
                range.range(),
                toMap(
                    range,
                    SpreadsheetCell::formatter
                )
            );
        }
    },

    /**
     * The clipboard value is a cells to {@link SpreadsheetParserSelector}.
     */
    PARSER(
        SpreadsheetParserSelector.class,
        SpreadsheetCell::parser,
        "parser"
    ) {
        @Override
        JsonNode marshall(final SpreadsheetCell cell,
                          final JsonNodeMarshallContext context) {
            return marshallCellToOptionalValue(
                cell,
                cell.parser(),
                context
            );
        }

        @Override //
        SpreadsheetCell unmarshall(final JsonNode node,
                                   final AppContext context) {
            return SpreadsheetSelection.parseCell(
                node.name()
                    .value()
            ).setFormula(
                SpreadsheetFormula.EMPTY
            ).setParser(
                context.unmarshallOptional(
                    node,
                    SpreadsheetParserSelector.class
                )
            );
        }

        @Override
        public void saveOrUpdateCells(final SpreadsheetDeltaFetcher fetcher,
                                      final SpreadsheetId id,
                                      final SpreadsheetCellRange range) {
            fetcher.patchCellsParser(
                id,
                range.range(),
                toMap(
                    range,
                    SpreadsheetCell::parser
                )
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

        @Override //
        SpreadsheetCell unmarshall(final JsonNode node,
                                   final AppContext context) {
            return SpreadsheetSelection.parseCell(
                node.name()
                    .value()
            ).setFormula(
                SpreadsheetFormula.EMPTY
            ).setStyle(
                context.unmarshall(
                    node,
                    TextStyle.class
                )
            );
        }

        @Override
        public void saveOrUpdateCells(final SpreadsheetDeltaFetcher fetcher,
                                      final SpreadsheetId id,
                                      final SpreadsheetCellRange range) {
            fetcher.patchCellsStyle(
                id,
                range.range(),
                toMap(
                    range,
                    SpreadsheetCell::style
                )
            );
        }
    },

    /**
     * The clipboard value is a formatted text.
     */
    FORMATTED_VALUE(
        TextNode.class,
        SpreadsheetCell::formattedValue,
        "formatted-value"
    ) {
        @Override
        JsonNode marshall(final SpreadsheetCell cell,
                          final JsonNodeMarshallContext context) {
            return marshallCellToOptionalTypeValue(
                cell,
                cell.formattedValue(),
                context
            );
        }

        @Override //
        SpreadsheetCell unmarshall(final JsonNode node,
                                   final AppContext context) {
            return SpreadsheetSelection.parseCell(
                node.name()
                    .value()
            ).setFormula(
                SpreadsheetFormula.EMPTY
            ).setFormattedValue(
                context.<TextNode>unmarshallOptionalWithType(node)
            );
        }

        @Override
        public void saveOrUpdateCells(final SpreadsheetDeltaFetcher fetcher,
                                      final SpreadsheetId id,
                                      final SpreadsheetCellRange range) {
            throw new UnsupportedOperationException("Pasting formattedValue not supported");
        }
    },

    /**
     * The value entered or the result of the expression.
     */
    VALUE(
        Object.class,
        (SpreadsheetCell cell) -> cell.formula()
            .value(),
        "value"
    ) {
        @Override
        JsonNode marshall(final SpreadsheetCell cell,
                          final JsonNodeMarshallContext context) {
            return marshallCellToOptionalTypeValue(
                cell,
                cell.formula()
                    .value(),
                context
            );
        }

        @Override //
        SpreadsheetCell unmarshall(final JsonNode node,
                                   final AppContext context) {
            return SpreadsheetSelection.parseCell(
                node.name()
                    .value()
            ).setFormula(
                SpreadsheetFormula.EMPTY.setValue(
                    context.unmarshallOptionalWithType(node)
                )
            );
        }

        @Override
        public void saveOrUpdateCells(final SpreadsheetDeltaFetcher fetcher,
                                      final SpreadsheetId id,
                                      final SpreadsheetCellRange range) {
            fetcher.patchCellsValue(
                id,
                range.range(),
                toMap(
                    range,
                    (SpreadsheetCell cell) -> cell.formula()
                        .value()
                )
            );
        }
    },

    /**
     * The clipboard value is a formatted text.
     */
    VALUE_TYPE(
        ValidationValueTypeName.class,
        (SpreadsheetCell cell) -> cell.formula()
            .valueType(),
        "value-type"
    ) {
        @Override
        JsonNode marshall(final SpreadsheetCell cell,
                          final JsonNodeMarshallContext context) {
            return SpreadsheetCellClipboardKind.marshallCellToOptionalValue(
                cell,
                cell.formula()
                    .valueType(),
                context
            );
        }

        @Override //
        SpreadsheetCell unmarshall(final JsonNode node,
                                   final AppContext context) {
            return SpreadsheetSelection.parseCell(
                node.name()
                    .value()
            ).setFormula(
                SpreadsheetFormula.EMPTY.setValueType(
                    context.unmarshallOptional(
                        node,
                        ValidationValueTypeName.class
                    )
                )
            );
        }

        @Override
        public void saveOrUpdateCells(final SpreadsheetDeltaFetcher fetcher,
                                      final SpreadsheetId id,
                                      final SpreadsheetCellRange range) {
            fetcher.patchCellsValueType(
                id,
                range.range(),
                toMap(
                    range,
                    (SpreadsheetCell cell) -> cell.formula()
                        .valueType()
                )
            );
        }
    },

    /**
     * The clipboard value is a cells to {@link ValidatorSelector}.
     */
    VALIDATOR(
        ValidatorSelector.class,
        SpreadsheetCell::validator,
        "validator"
    ) {
        @Override
        JsonNode marshall(final SpreadsheetCell cell,
                          final JsonNodeMarshallContext context) {
            return marshallCellToOptionalValue(
                cell,
                cell.validator(),
                context
            );
        }

        @Override //
        SpreadsheetCell unmarshall(final JsonNode node,
                                   final AppContext context) {
            return SpreadsheetSelection.parseCell(
                node.name()
                    .value()
            ).setFormula(
                SpreadsheetFormula.EMPTY
            ).setValidator(
                context.unmarshallOptional(
                    node,
                    ValidatorSelector.class
                )
            );
        }

        @Override
        public void saveOrUpdateCells(final SpreadsheetDeltaFetcher fetcher,
                                      final SpreadsheetId id,
                                      final SpreadsheetCellRange range) {
            fetcher.patchCellsValidator(
                id,
                range.range(),
                toMap(
                    range,
                    SpreadsheetCell::validator
                )
            );
        }
    };

    private static JsonNode marshallCellToOptionalValue(final SpreadsheetCell cell,
                                                        final Optional<?> value,
                                                        final JsonNodeMarshallContext context) {
        return value.map(context::marshall)
            .orElse(JsonNode.nullNode())
            .setName(propertyName(cell));
    }

    private static JsonNode marshallCellToOptionalTypeValue(final SpreadsheetCell cell,
                                                            final Optional<?> value,
                                                            final JsonNodeMarshallContext context) {
        return value.map(context::marshallWithType)
            .orElse(JsonNode.nullNode())
            .setName(propertyName(cell));
    }

    private static JsonPropertyName propertyName(final SpreadsheetCell cell) {
        return JsonPropertyName.with(
            cell.reference().toString()
        );
    }

    SpreadsheetCellClipboardKind(final Class<?> type,
                                 final Function<SpreadsheetCell, Object> valueExtractor,
                                 final String urlFragment) {
        this.mediaType = MediaType.APPLICATION_JSON.setSuffix(
            Optional.of(
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
     * Extracts the cell or cell property selected by this {@link SpreadsheetCellClipboardKind}. Some values will be wrapped inside an {@link java.util.Optional}.
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
     * Internal method used because each of the different types clipboard values are marshalled differently.
     */
    abstract SpreadsheetCell unmarshall(final JsonNode node,
                                        final AppContext context);

    /**
     * All {@link SpreadsheetCellClipboardKind} except for {@link #CELL} only match themselves while {@link #CELL} matches all enum values.
     * If the clipboard value is a {@link SpreadsheetCell} all PASTE menu items will be enabled, while other value types will only enable themselves,
     * eg if clipboard value is a {@link TextStyle} only PASTE STYLE will be enabled.
     */
    public Predicate<SpreadsheetCellClipboardKind> predicate() {
        return predicate;
    }

    private final Predicate<SpreadsheetCellClipboardKind> predicate;

    /**
     * Performs a PATCH using the given {@link SpreadsheetCellRange range}.
     */
    public abstract void saveOrUpdateCells(final SpreadsheetDeltaFetcher fetcher,
                                           final SpreadsheetId id,
                                           final SpreadsheetCellRange range);

    static <T> Map<SpreadsheetCellReference, T> toMap(final SpreadsheetCellRange range,
                                                      final Function<SpreadsheetCell, T> valueExtractor) {
        final Map<SpreadsheetCellReference, T> map = Maps.sorted();
        for (final SpreadsheetCell cell : range.value()) {
            map.put(
                cell.reference(),
                valueExtractor.apply(cell)
            );
        }

        return map;
    }

    // HasMediaType.....................................................................................................

    @Override
    public MediaType mediaType() {
        return this.mediaType;
    }

    private final MediaType mediaType;

    /**
     * Getter that returns the class type for this {@link MediaType}. This can be used to unmarshall values from a clipboard,
     * when in JSON form.
     */
    public Class<?> mediaTypeClass() {
        return this.mediaTypeClass;
    }

    private final Class<?> mediaTypeClass;

    // HasUrlFragment...................................................................................................

    @Override
    public UrlFragment urlFragment() {
        return this.urlFragment;
    }

    private final transient UrlFragment urlFragment;

    /**
     * Parses the given {@link UrlFragment} component attempting to match the equivalent {@link SpreadsheetCellClipboardKind}.
     * This is used to turn a history token fragment into a {@link SpreadsheetCellClipboardHistoryToken} such as CUT | COPY | PASTE.
     */
    public static SpreadsheetCellClipboardKind parse(final String string) {
        Objects.requireNonNull(string, "string");

        return Arrays.stream(values())
            .filter(e -> e.urlFragment.value().equals(string))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid SpreadsheetCellClipboardKind: " + CharSequences.quote(string)));
    }

    /**
     * Scans the current {@link MediaType} for the equivalent {@link SpreadsheetCellClipboardKind}. This is used
     * to determine the type of a json payload written to the clipboard.
     */
    public static SpreadsheetCellClipboardKind fromMediaType(final MediaType mediaType) {
        Objects.requireNonNull(mediaType, "mediaType");

        return Arrays.stream(values())
            .filter(e -> e.mediaType.equals(mediaType))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unknown MediaType: " + mediaType));
    }

    /**
     * Because {@link SpreadsheetCellClipboardKind#values} ordering is not guaranteed this getter is provided.
     */
    public static List<SpreadsheetCellClipboardKind> menuItemValues() {
        return VALUES;
    }

    private final static List<SpreadsheetCellClipboardKind> VALUES = Lists.of(
        CELL,
        FORMULA,
        DATE_TIME_SYMBOLS,
        DECIMAL_NUMBER_SYMBOLS,
        LOCALE,
        FORMATTER,
        PARSER,
        STYLE,
        FORMATTED_VALUE,
        VALUE,
        VALUE_TYPE,
        VALIDATOR
    );

    static {
        TextNode.text("Hello");
    }
}
