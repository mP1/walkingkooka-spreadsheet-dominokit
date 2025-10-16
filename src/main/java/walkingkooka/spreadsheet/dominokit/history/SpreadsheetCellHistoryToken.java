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
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.SpreadsheetUrlFragments;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetColumnOrRowSpreadsheetComparatorNamesList;
import walkingkooka.spreadsheet.dominokit.clipboard.SpreadsheetCellClipboardKind;
import walkingkooka.spreadsheet.engine.SpreadsheetCellFindQuery;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelNameResolvers;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.validation.ValueTypeName;
import walkingkooka.validation.form.FormName;
import walkingkooka.validation.provider.ValidatorSelector;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

abstract public class SpreadsheetCellHistoryToken extends SpreadsheetAnchoredSelectionHistoryToken {

    SpreadsheetCellHistoryToken(final SpreadsheetId id,
                                final SpreadsheetName name,
                                final AnchoredSpreadsheetSelection anchoredSelection) {
        super(
            id,
            name,
            anchoredSelection
        );

        final SpreadsheetSelection selection = anchoredSelection.selection();
        if (false == (selection.isCellOrCellRange() || selection.isLabelName())) {
            throw new IllegalArgumentException("Got " + selection + " expected cell, cell-range or label");
        }
    }

    @Override
    public HistoryToken setSaveValue(final Optional<?> value) {
        Objects.requireNonNull(value, "value");

        HistoryToken historyToken = this;

        final Object valueOrNull = value.orElse(null);

        if (this instanceof SpreadsheetCellSelectHistoryToken || this instanceof SpreadsheetCellSaveHistoryToken) {
            historyToken = this.setSaveValueCell(valueOrNull);
        }

        if (this instanceof SpreadsheetCellDateTimeSymbolsHistoryToken) {
            if (null != valueOrNull && false == valueOrNull instanceof DateTimeSymbols) {
                this.reportInvalidSaveValue(
                    valueOrNull,
                    DateTimeSymbols.class
                );
            }

            historyToken = HistoryToken.cellDateTimeSymbolsSave(
                this.id,
                this.name,
                this.anchoredSelection,
                Cast.to(value)
            );
        }

        if (this instanceof SpreadsheetCellDecimalNumberSymbolsHistoryToken) {
            if (null != valueOrNull && false == valueOrNull instanceof DecimalNumberSymbols) {
                this.reportInvalidSaveValue(
                    valueOrNull,
                    DecimalNumberSymbols.class
                );
            }

            historyToken = HistoryToken.cellDecimalNumberSymbolsSave(
                this.id,
                this.name,
                this.anchoredSelection,
                Cast.to(value)
            );
        }

        if (this instanceof SpreadsheetCellFormatterHistoryToken) {
            if (null != valueOrNull && false == valueOrNull instanceof SpreadsheetFormatterSelector) {
                this.reportInvalidSaveValue(
                    valueOrNull,
                    SpreadsheetFormatterSelector.class
                );
            }

            historyToken = HistoryToken.cellFormatterSave(
                this.id,
                this.name,
                this.anchoredSelection,
                Cast.to(value)
            );
        }

        if (this instanceof SpreadsheetCellFormulaHistoryToken) {
            if (null != valueOrNull && false == valueOrNull instanceof String) {
                this.reportInvalidSaveValue(
                    valueOrNull,
                    String.class
                );
            }

            historyToken = HistoryToken.cellFormulaSave(
                this.id,
                this.name,
                this.anchoredSelection,
                CharSequences.nullToEmpty((String) valueOrNull)
                    .toString()
            );
        }

        if (this instanceof SpreadsheetCellLabelHistoryToken) {
            if (null != valueOrNull && false == valueOrNull instanceof SpreadsheetLabelName) {
                this.reportInvalidSaveValue(
                    valueOrNull,
                    SpreadsheetLabelName.class
                );
            }

            historyToken = HistoryToken.cellLabelSave(
                this.id,
                this.name,
                this.anchoredSelection,
                (SpreadsheetLabelName) valueOrNull
            );
        }

        if (this instanceof SpreadsheetCellLocaleHistoryToken) {
            if (null != valueOrNull && false == valueOrNull instanceof Locale) {
                this.reportInvalidSaveValue(
                    valueOrNull,
                    Locale.class
                );
            }

            historyToken = HistoryToken.cellLocaleSave(
                this.id,
                this.name,
                this.anchoredSelection,
                Cast.to(value)
            );
        }

        if (this instanceof SpreadsheetCellParserHistoryToken) {
            if (null != valueOrNull && false == valueOrNull instanceof SpreadsheetParserSelector) {
                this.reportInvalidSaveValue(
                    valueOrNull,
                    SpreadsheetParserSelector.class
                );
            }

            historyToken = HistoryToken.cellParserSave(
                this.id,
                this.name,
                this.anchoredSelection,
                Cast.to(value)
            );
        }

        if (this instanceof SpreadsheetCellSortHistoryToken) {
            if (null != valueOrNull && false == valueOrNull instanceof SpreadsheetColumnOrRowSpreadsheetComparatorNamesList) {
                this.reportInvalidSaveValue(
                    valueOrNull,
                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.class
                );
            }

            historyToken = HistoryToken.cellSortSave(
                this.id,
                this.name,
                this.anchoredSelection,
                (SpreadsheetColumnOrRowSpreadsheetComparatorNamesList) valueOrNull
            );
        }

        if (this instanceof SpreadsheetCellStyleHistoryToken) {
            historyToken = HistoryToken.cellStyleSave(
                this.id,
                this.name,
                this.anchoredSelection,
                this.cast(SpreadsheetCellStyleHistoryToken.class)
                    .propertyName(),
                value
            );
        }

        if (this instanceof SpreadsheetCellValidatorHistoryToken) {
            if (null != valueOrNull && false == valueOrNull instanceof ValidatorSelector) {
                this.reportInvalidSaveValue(
                    valueOrNull,
                    ValidatorSelector.class
                );
            }

            historyToken = HistoryToken.cellValidatorSave(
                this.id,
                this.name,
                this.anchoredSelection,
                Cast.to(value)
            );
        }

        if (this instanceof SpreadsheetCellValueHistoryToken) {
            if (null != valueOrNull && false == valueOrNull instanceof String) {
                this.reportInvalidSaveValue(
                    valueOrNull,
                    String.class
                );
            }

            historyToken = HistoryToken.cellValueSave(
                this.id,
                this.name,
                this.anchoredSelection,
                this.cast(SpreadsheetCellValueHistoryToken.class)
                    .valueType()
                    .get(),
                CharSequences.nullToEmpty((String) valueOrNull)
                    .toString()
            );
        }

        if (this instanceof SpreadsheetCellValueTypeHistoryToken) {
            if (null != valueOrNull && false == valueOrNull instanceof ValueTypeName) {
                this.reportInvalidSaveValue(
                    valueOrNull,
                    ValueTypeName.class
                );
            }

            historyToken = HistoryToken.cellValueTypeSave(
                this.id,
                this.name,
                this.anchoredSelection,
                Cast.to(value)
            );
        }

        return this.elseIfDifferent(historyToken);
    }

    final HistoryToken setSaveValueCell(final Object valueOrNull) {
        HistoryToken historyToken = this;

        if (valueOrNull instanceof Set) {
            historyToken = HistoryToken.cellSaveCell(
                this.id,
                this.name,
                this.anchoredSelection,
                Cast.to(valueOrNull) // Set<SpreadsheetCell>
            );
        } else {
            if (valueOrNull instanceof Map) {
                final Map<?, ?> map = Cast.to(valueOrNull);
                if (false == map.isEmpty()) {
                    final int MODE_DATE_TIME_SYMBOLS = 1;
                    final int MODE_DECIMAL_NUMBER_SYMBOLS = 2;
                    final int MODE_FORMATTER = 4;
                    final int MODE_FORMULA = 8;
                    final int MODE_LOCALE = 16;
                    final int MODE_PARSER = 32;
                    final int MODE_STYLE = 64;
                    final int MODE_VALIDATOR = 128;
                    final int MODE_VALUE_TYPE = 256;

                    int mode = MODE_DATE_TIME_SYMBOLS | MODE_DECIMAL_NUMBER_SYMBOLS | MODE_FORMATTER | MODE_FORMULA | MODE_LOCALE | MODE_PARSER | MODE_STYLE | MODE_VALIDATOR | MODE_VALUE_TYPE;

                    for (final Object mapValue : map.values()) {
                        // ignore nulls
                        if (mapValue instanceof Optional) {
                            final Optional<?> mapValueOptional = (Optional<?>) mapValue;
                            if (mapValueOptional.isPresent()) {
                                Object mapValueOptionalValue = mapValueOptional.get();
                                if (mapValueOptionalValue instanceof DateTimeSymbols) {
                                    mode = MODE_DATE_TIME_SYMBOLS & mode;
                                } else {
                                    if (mapValueOptionalValue instanceof DecimalNumberSymbols) {
                                        mode = MODE_DECIMAL_NUMBER_SYMBOLS & mode;
                                    } else {
                                        if (mapValueOptionalValue instanceof SpreadsheetFormatterSelector) {
                                            mode = MODE_FORMATTER & mode;
                                        } else {
                                            if (mapValueOptionalValue instanceof Locale) {
                                                mode = MODE_LOCALE & mode;
                                            } else {
                                                if (mapValueOptionalValue instanceof SpreadsheetParserSelector) {
                                                    mode = MODE_PARSER & mode;
                                                } else {
                                                    if (mapValueOptionalValue instanceof ValidatorSelector) {
                                                        mode = MODE_VALIDATOR & mode;
                                                    } else {
                                                        if (mapValueOptionalValue instanceof ValueTypeName) {
                                                            mode = MODE_VALUE_TYPE & mode;
                                                        } else {
                                                            mode = 0;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if (mapValue instanceof String) {
                                mode = MODE_FORMULA & mode;
                            } else {
                                if (mapValue instanceof TextStyle) {
                                    mode = MODE_STYLE & mode;
                                } else {
                                    mode = 0;
                                    break;
                                }
                            }
                        }

                        if (0 == mode) {
                            break;
                        }
                    }
                    switch (mode) {
                        case MODE_DATE_TIME_SYMBOLS:
                            historyToken = HistoryToken.cellSaveDateTimeSymbols(
                                this.id,
                                this.name,
                                this.anchoredSelection,
                                Cast.to(valueOrNull)
                            );
                            break;
                        case MODE_DECIMAL_NUMBER_SYMBOLS:
                            historyToken = HistoryToken.cellSaveDecimalNumberSymbols(
                                this.id,
                                this.name,
                                this.anchoredSelection,
                                Cast.to(valueOrNull)
                            );
                            break;
                        case MODE_FORMATTER:
                            historyToken = HistoryToken.cellSaveFormatter(
                                this.id,
                                this.name,
                                this.anchoredSelection,
                                Cast.to(valueOrNull)
                            );
                            break;
                        case MODE_FORMULA:
                            historyToken = HistoryToken.cellSaveFormulaText(
                                this.id,
                                this.name,
                                this.anchoredSelection,
                                Cast.to(valueOrNull)
                            );
                            break;
                        case MODE_LOCALE:
                            historyToken = HistoryToken.cellSaveLocale(
                                this.id,
                                this.name,
                                this.anchoredSelection,
                                Cast.to(valueOrNull)
                            );
                            break;
                        case MODE_PARSER:
                            historyToken = HistoryToken.cellSaveParser(
                                this.id,
                                this.name,
                                this.anchoredSelection,
                                Cast.to(valueOrNull)
                            );
                            break;
                        case MODE_STYLE:
                            historyToken = HistoryToken.cellSaveStyle(
                                this.id,
                                this.name,
                                this.anchoredSelection,
                                Cast.to(valueOrNull)
                            );
                            break;
                        case MODE_VALIDATOR:
                            historyToken = HistoryToken.cellSaveValidator(
                                this.id,
                                this.name,
                                this.anchoredSelection,
                                Cast.to(valueOrNull)
                            );
                            break;
                        case MODE_VALUE_TYPE:
                            historyToken = HistoryToken.cellSaveValueType(
                                this.id,
                                this.name,
                                this.anchoredSelection,
                                Cast.to(valueOrNull)
                            );
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid value: got " + valueOrNull.getClass().getSimpleName());
                    }
                }
            } else {
                if (null == valueOrNull) {
                    historyToken = this.clearAction();
                } else {
                    throw new IllegalArgumentException("Invalid value: got " + valueOrNull.getClass().getSimpleName());
                }
            }
        }
        return historyToken;
    }

    // HasUrlFragment...................................................................................................

    @Override //
    final UrlFragment selectionUrlFragment() {
        return SpreadsheetUrlFragments.CELL.append(
            this.anchoredSelection.urlFragment()
        ).appendSlashThen(this.cellUrlFragment());
    }

    abstract UrlFragment cellUrlFragment();

    // parse............................................................................................................

    @Override //
    final HistoryToken parseNext(final String component,
                                 final TextCursor cursor) {
        final HistoryToken result;

        switch (component) {
            case COPY_STRING:
                result = this.parseCopy(cursor);
                break;
            case CUT_STRING:
                result = this.parseCut(cursor);
                break;
            case DATE_TIME_SYMBOLS_STRING:
                result = this.dateTimeSymbols();
                break;
            case DECIMAL_NUMBER_SYMBOLS_STRING:
                result = this.decimalNumberSymbols();
                break;
            case DELETE_STRING:
                result = this.delete();
                break;
            case FIND_STRING:
                result = this.parseFind(cursor);
                break;
            case FORM_STRING:
                result = this.parseForm(cursor);
                break;
            case FREEZE_STRING:
                result = this.freeze();
                break;
            case FORMATTER_STRING:
                result = this.formatter();
                break;
            case FORMULA_STRING:
                result = this.formula();
                break;
            case LABEL_STRING:
                result = this.labelMapping();
                break;
            case LABELS_STRING:
                result = this.parseLabels(cursor);
                break;
            case LOCALE_STRING:
                result = this.locale();
                break;
            case MENU_STRING:
                result = this.menu(
                    Optional.empty(), // no selection
                    SpreadsheetLabelNameResolvers.fake()
                );
                break;
            case NAVIGATE_STRING:
                result = this.parseNavigate(cursor);
                break;
            case PARSER_STRING:
                result = this.parser();
                break;
            case PASTE_STRING:
                result = this.parsePaste(cursor);
                break;
            case REFERENCES_STRING:
                result = this.parseReferences(cursor);
                break;
            case RELOAD_STRING:
                result = this.reload();
                break;
            case SAVE_STRING:
                result = this.parseSave(cursor);
                break;
            case SORT_STRING:
                result = this.parseSort(cursor);
                break;
            case STYLE_STRING:
                result = this.parseStyle(cursor);
                break;
            case TOOLBAR_STRING:
                result = this.toolbar();
                break;
            case UNFREEZE_STRING:
                result = this.unfreeze();
                break;
            case VALIDATOR_STRING:
                result = this.validator();
                break;
            case VALUE_STRING:
                result = this.parseValue(cursor);
                break;
            case VALUE_TYPE_STRING:
                result = this.setValueType();
                break;
            default:
                cursor.end();
                result = this; // ignore
                break;
        }

        return result;
    }

    private HistoryToken parseCopy(final TextCursor cursor) {
        HistoryToken token = this;

        if (this instanceof SpreadsheetCellSelectHistoryToken) {
            final SpreadsheetCellSelectHistoryToken cell = this.cast(SpreadsheetCellSelectHistoryToken.class);

            String component = parseComponentOrNull(cursor);
            if (null != component) {
                token = cell.copy(
                    SpreadsheetCellClipboardKind.parse(component)
                );
            }
        }

        return token;
    }

    private HistoryToken parseCut(final TextCursor cursor) {
        HistoryToken token = this;

        if (this instanceof SpreadsheetCellSelectHistoryToken) {
            final SpreadsheetCellSelectHistoryToken cell = this.cast(SpreadsheetCellSelectHistoryToken.class);

            String component = parseComponentOrNull(cursor);
            if (null != component) {
                token = cell.cut(
                    SpreadsheetCellClipboardKind.parse(component)
                );
            }
        }

        return token;
    }

    private HistoryToken parseFind(final TextCursor cursor) {
        final TextCursorSavePoint save = cursor.save();
        cursor.end();

        final String queryText = save.textBetween()
            .toString();

        return this.setQuery(
            SpreadsheetCellFindQuery.parse(queryText)
        );
    }

    private HistoryToken parseForm(final TextCursor cursor) {
        final String formName = parseComponentOrNull(cursor);

        return null != formName ?
            HistoryToken.cellFormSelect(
                this.id,
                this.name,
                this.anchoredSelection,
                FormName.with(formName)
            ).parse(cursor) :
            this;
    }

    private HistoryToken parseLabels(final TextCursor cursor) {
        HistoryTokenOffsetAndCount offsetAndCount;

        try {
            offsetAndCount = HistoryTokenOffsetAndCount.parse(cursor);
        } catch (final IllegalArgumentException cause) {
            offsetAndCount = HistoryTokenOffsetAndCount.EMPTY;
        }

        return this.setLabels(offsetAndCount);
    }

    private HistoryToken parsePaste(final TextCursor cursor) {
        HistoryToken token = this;

        if (this instanceof SpreadsheetCellSelectHistoryToken) {
            final SpreadsheetCellSelectHistoryToken cell = this.cast(SpreadsheetCellSelectHistoryToken.class);

            String component = parseComponentOrNull(cursor);
            if (null != component) {
                token = cell.paste(
                    SpreadsheetCellClipboardKind.parse(component)
                );
            }
        }

        return token;
    }

    private HistoryToken parseValue(final TextCursor cursor) {
        final String valueTypeString = parseComponentOrNull(cursor);

        return this.setValue(
            Optional.ofNullable(
                null == valueTypeString ?
                    null :
                    ValueTypeName.with(valueTypeString)
            )
        );
    }

    private static String parseComponentOrNull(final TextCursor cursor) {
        return parseComponent(cursor)
            .orElse(null);
    }
}
