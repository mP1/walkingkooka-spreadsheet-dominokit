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

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellReferenceToDateTimeSymbolsMap;
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellReferenceToDecimalNumberSymbolsMap;
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellReferenceToFormulaTextMap;
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellReferenceToLocaleMap;
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellReferenceToSpreadsheetFormatterSelectorMap;
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellReferenceToSpreadsheetParserSelectorMap;
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellReferenceToTextStyleMap;
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellReferenceToValidatorSelectorMap;
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellReferenceToValueTypeNameMap;
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellSet;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.text.cursor.TextCursor;

/**
 * This token selects one or more cells for viewing or editing.
 * <pre>
 * /123/SpreadsheetName456/cell/A1/
 * /123/SpreadsheetName456/cell/B2:C3
 * /123/SpreadsheetName456/cell/Label123
 *
 * /spreadsheet-id/spreadsheet-name/cell/cell-or-cell-range-or-label
 * </pre>
 */
public final class SpreadsheetCellSelectHistoryToken extends SpreadsheetCellHistoryToken {

    static SpreadsheetCellSelectHistoryToken with(final SpreadsheetId id,
                                                  final SpreadsheetName name,
                                                  final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetCellSelectHistoryToken(
            id,
            name,
            anchoredSelection
        );
    }

    private SpreadsheetCellSelectHistoryToken(final SpreadsheetId id,
                                              final SpreadsheetName name,
                                              final AnchoredSpreadsheetSelection anchoredSelection) {
        super(
            id,
            name,
            anchoredSelection
        );
    }

    @Override
    UrlFragment cellUrlFragment() {
        return SELECT;
    }

    @Override
    public HistoryToken clearAction() {
        return this.selectionSelect();
    }

    @Override //
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return selection(
            id,
            name,
            anchoredSelection
        );
    }

    @Override
    public void onHistoryTokenChange0(final HistoryToken previous,
                                      final AppContext context) {
        // SpreadsheetViewportComponent will give focus to cell
    }

    /**
     * Handles parsing /cell/save tokens.
     */
    HistoryToken parseCellSave(final TextCursor cursor) {
        HistoryToken result = this;

        final String component = parseComponentOrEmpty(cursor);

        // there will be more such as cell/pattern-format/pattern-parse/style
        switch (component) {
            case CELL_STRING:
                result = cellSaveCell(
                    this.id,
                    this.name,
                    this.anchoredSelection(),
                    parseJson(
                        cursor,
                        SpreadsheetCellSet.class
                    )
                );
                break;
            case DATE_TIME_SYMBOLS_STRING:
                result = cellSaveDateTimeSymbols(
                    this.id,
                    this.name,
                    this.anchoredSelection(),
                    SpreadsheetCellSaveHistoryToken.parseJson(
                        cursor,
                        SpreadsheetCellReferenceToDateTimeSymbolsMap.class
                    )
                );
                break;
            case DECIMAL_NUMBER_SYMBOLS_STRING:
                result = cellSaveDecimalNumberSymbols(
                    this.id,
                    this.name,
                    this.anchoredSelection(),
                    SpreadsheetCellSaveHistoryToken.parseJson(
                        cursor,
                        SpreadsheetCellReferenceToDecimalNumberSymbolsMap.class
                    )
                );
                break;
            case FORMATTER_STRING:
                result = cellSaveFormatter(
                    this.id,
                    this.name,
                    this.anchoredSelection(),
                    SpreadsheetCellSaveHistoryToken.parseJson(
                        cursor,
                        SpreadsheetCellReferenceToSpreadsheetFormatterSelectorMap.class
                    )
                );
                break;
            case FORMULA_STRING:
                result = cellSaveFormulaText(
                    this.id,
                    this.name,
                    this.anchoredSelection(),
                    SpreadsheetCellSaveHistoryToken.parseJson(
                        cursor,
                        SpreadsheetCellReferenceToFormulaTextMap.class
                    )
                );
                break;
            case LOCALE_STRING:
                result = cellSaveLocale(
                    this.id,
                    this.name,
                    this.anchoredSelection(),
                    SpreadsheetCellSaveHistoryToken.parseJson(
                        cursor,
                        SpreadsheetCellReferenceToLocaleMap.class
                    )
                );
                break;
            case PARSER_STRING:
                result = cellSaveParser(
                    this.id,
                    this.name,
                    this.anchoredSelection(),
                    SpreadsheetCellSaveHistoryToken.parseJson(
                        cursor,
                        SpreadsheetCellReferenceToSpreadsheetParserSelectorMap.class
                    )
                );
                break;
            case STYLE_STRING:
                result = cellSaveStyle(
                    this.id,
                    this.name,
                    this.anchoredSelection(),
                    SpreadsheetCellSaveHistoryToken.parseJson(
                        cursor,
                        SpreadsheetCellReferenceToTextStyleMap.class
                    )
                );
                break;
            case VALIDATOR_STRING:
                result = cellSaveValidator(
                    this.id,
                    this.name,
                    this.anchoredSelection(),
                    SpreadsheetCellSaveHistoryToken.parseJson(
                        cursor,
                        SpreadsheetCellReferenceToValidatorSelectorMap.class
                    )
                );
                break;
            case VALUE_TYPE_STRING:
                result = cellSaveValueType(
                    this.id,
                    this.name,
                    this.anchoredSelection(),
                    SpreadsheetCellSaveHistoryToken.parseJson(
                        cursor,
                        SpreadsheetCellReferenceToValueTypeNameMap.class
                    )
                );
                break;
            default:
                cursor.end();
                result = this; // ignore
                break;
        }

        return result;
    }
}
