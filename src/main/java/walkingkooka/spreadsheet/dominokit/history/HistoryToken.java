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
import walkingkooka.Value;
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.net.HasUrlFragment;
import walkingkooka.net.UrlFragment;
import walkingkooka.plugin.PluginName;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetHateosResourceNames;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.SpreadsheetUrlFragments;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetColumnOrRowSpreadsheetComparatorNamesList;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.clipboard.SpreadsheetCellClipboardKind;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuItem;
import walkingkooka.spreadsheet.dominokit.file.BrowserFile;
import walkingkooka.spreadsheet.engine.SpreadsheetCellFindQuery;
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellReferenceToValueMap;
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellSet;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelNameResolver;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoName;
import walkingkooka.spreadsheet.validation.form.SpreadsheetForms;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewport;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportHomeNavigationList;
import walkingkooka.text.CharSequences;
import walkingkooka.text.CharacterConstant;
import walkingkooka.text.HasText;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserContexts;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.cursor.parser.StringParserToken;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContexts;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContexts;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.validation.ValueTypeName;
import walkingkooka.validation.form.Form;
import walkingkooka.validation.form.FormName;
import walkingkooka.validation.provider.ValidatorSelector;

import java.math.MathContext;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Function;

/**
 * A {@link HistoryToken} represents a simple summary of the spreadsheet or an action. Every action performed by the user,
 * by clicking a button or link actually results in that component pushing a {@link HistoryToken} to the application's
 * history. The application has several {@link HistoryTokenWatcher watchers} one which may perform some action, or
 * {@link HistoryToken} themselves can also execute some real action.
 * <br>
 * A sample history token is {@link SpreadsheetCellFormulaSaveHistoryToken}, this includes the spreadsheet-id, cell and formula text.
 * It is thus possible to command the web-app to save a new formula text for any cell in a spreadsheet by updating the URL
 * and hitting enter.
 * <br>
 * The two url hash updates below will replace the formula for the A1 and B2 cells with the expressions given.
 * <pre>
 *     #/1/Untitled/cell/A1/formula/save/=1+2 ENTER
 *     #/1/Untitled/cell/B2/formula/save/=3+A1 ENTER
 * </pre>
 * Another example of using history token is to select and open a context menu for the same or different column.
 * <pre>
 *     #/1/Untitled/column/A/menu
 * </pre>
 * All context menu items action items for column A are themselves links with a history token.
 * <pre>
 *     #/1/Untitled/column/B/clear
 * </pre>
 * This history token will clear column B for spreadsheet with ID=1.
 * <br>
 * A final example is changing the format pattern for a cell to DD/MM/YYYY. This is accomplished by entering the following hash.
 * <pre>
 *     #/1/Untitled/cell/D4/formatter/date/save/date-format DD/MM/YYYY
 * </pre>
 * This architecture makes it possible to program a macro system, manipulating a browser by simply sending new hash tokens.
 */
public abstract class HistoryToken implements HasUrlFragment {

    private final static int MAX_LENGTH = 8192;

    public final static String DISABLE = "disable";

    public final static String ENABLE = "enable";

    final static String CELL_STRING = SpreadsheetHateosResourceNames.CELL_STRING;

    final static UrlFragment CELL = UrlFragment.parse(CELL_STRING);

    final static String CLEAR_STRING = "clear";

    final static UrlFragment CLEAR = UrlFragment.parse(CLEAR_STRING);

    final static String COLUMN_STRING = SpreadsheetHateosResourceNames.COLUMN_STRING;

    final static String COPY_STRING = "copy";

    final static UrlFragment COPY = UrlFragment.parse(COPY_STRING);

    final static String COUNT_STRING = "count";

    final static UrlFragment COUNT = UrlFragment.with(COUNT_STRING);

    final static String CREATE_STRING = "create";

    final static UrlFragment CREATE = UrlFragment.parse(CREATE_STRING);

    final static String CUT_STRING = "cut";

    final static UrlFragment CUT = UrlFragment.parse(CUT_STRING);

    final static String DATE_TIME_SYMBOLS_STRING = "dateTimeSymbols";

    final static UrlFragment DATE_TIME_SYMBOLS = UrlFragment.parse(DATE_TIME_SYMBOLS_STRING);

    final static String DECIMAL_NUMBER_SYMBOLS_STRING = "decimalNumberSymbols";

    final static UrlFragment DECIMAL_NUMBER_SYMBOLS = UrlFragment.parse(DECIMAL_NUMBER_SYMBOLS_STRING);

    final static String DELETE_STRING = "delete";

    final static UrlFragment DELETE = UrlFragment.parse(DELETE_STRING);

    final static String EDIT_STRING = "edit";

    final static UrlFragment EDIT = UrlFragment.parse(EDIT_STRING);

    final static String FILE_STRING = "file";

    final static UrlFragment FILE = UrlFragment.parse(FILE_STRING);

    final static String FIND_STRING = "find";

    final static UrlFragment FIND = UrlFragment.parse(FIND_STRING);

    final static String FORM_STRING = "form";

    final static UrlFragment FORM = UrlFragment.parse(FORM_STRING);

    final static String FORMATTER_STRING = "formatter";

    final static UrlFragment FORMATTER = SpreadsheetUrlFragments.FORMATTER;

    final static String FORMULA_STRING = "formula";

    final static UrlFragment FORMULA = UrlFragment.with(FORMULA_STRING);

    final static String FREEZE_STRING = "freeze";

    final static UrlFragment FREEZE = UrlFragment.parse(FREEZE_STRING);

    final static String INSERT_AFTER_STRING = "insertAfter";

    final static UrlFragment INSERT_AFTER = UrlFragment.parse(INSERT_AFTER_STRING);

    final static String INSERT_BEFORE_STRING = "insertBefore";

    final static UrlFragment INSERT_BEFORE = UrlFragment.parse(INSERT_BEFORE_STRING);

    final static String LABEL_CREATE_STRING = "create-label";

    final static UrlFragment LABEL_CREATE = UrlFragment.parse(LABEL_CREATE_STRING);

    final static String LABEL_STRING = SpreadsheetHateosResourceNames.LABEL_STRING;

    final static UrlFragment LABEL = UrlFragment.parse(LABEL_STRING);

    final static String LABELS_STRING = "labels";

    final static UrlFragment LABELS = UrlFragment.parse(LABELS_STRING);

    final static String LOCALE_STRING = "locale";

    final static UrlFragment LOCALE = UrlFragment.parse(LOCALE_STRING);

    final static String MENU_STRING = "menu";

    final static UrlFragment MENU = UrlFragment.parse(MENU_STRING);

    final static String NAVIGATE_STRING = "navigate";

    final static UrlFragment NAVIGATE = UrlFragment.parse(NAVIGATE_STRING);

    final static String OFFSET_STRING = "offset";

    final static UrlFragment OFFSET = UrlFragment.parse(OFFSET_STRING);

    final static String PARSER_STRING = "parser";

    final static UrlFragment PARSER = SpreadsheetUrlFragments.PARSER;

    final static String PASTE_STRING = "paste";

    final static UrlFragment PASTE = UrlFragment.parse(PASTE_STRING);

    final static String PLUGIN_STRING = "plugin";

    final static UrlFragment PLUGIN = SpreadsheetUrlFragments.PLUGIN;

    final static String PLUGIN_UPLOAD_STRING = "plugin-upload";

    final static UrlFragment PLUGIN_UPLOAD = UrlFragment.parse(PLUGIN_UPLOAD_STRING);

    final static String REFERENCES_STRING = "references";

    final static UrlFragment REFERENCES = UrlFragment.parse(REFERENCES_STRING);

    final static String RELOAD_STRING = "reload";

    final static UrlFragment RELOAD = UrlFragment.parse(RELOAD_STRING);

    final static String RENAME_STRING = "rename";

    final static UrlFragment RENAME = UrlFragment.parse(RENAME_STRING);

    final static String ROW_STRING = SpreadsheetHateosResourceNames.ROW_STRING;

    final static String SELECT_STRING = "";

    final static UrlFragment SELECT = UrlFragment.parse(SELECT_STRING);

    final static String SAVE_STRING = "save";

    final static UrlFragment SAVE = UrlFragment.parse(SAVE_STRING);

    final static String SORT_STRING = "sort";

    final static UrlFragment SORT = UrlFragment.parse(SORT_STRING);

    final static String SPEADSHEET_STRING = SpreadsheetMetadata.HATEOS_RESOURCE_NAME_STRING;

    final static UrlFragment SPREADSHEET = UrlFragment.parse(SPEADSHEET_STRING);

    final static String STYLE_STRING = "style";

    final static UrlFragment STYLE = UrlFragment.parse(STYLE_STRING);

    final static String TOOLBAR_STRING = "toolbar";

    final static UrlFragment TOOLBAR = UrlFragment.parse(TOOLBAR_STRING);

    final static String UNFREEZE_STRING = "unfreeze";

    final static UrlFragment UNFREEZE = UrlFragment.parse(UNFREEZE_STRING);

    final static String VALIDATOR_STRING = "validator";

    final static UrlFragment VALIDATOR = UrlFragment.parse(VALIDATOR_STRING);

    final static String VALUE_STRING = "value";

    final static UrlFragment VALUE = UrlFragment.parse(VALUE_STRING);

    final static String VALUE_TYPE_STRING = "valueType";

    final static UrlFragment VALUE_TYPE = UrlFragment.parse(VALUE_TYPE_STRING);

    final static String WILDCARD_STRING = "*";

    final static UrlFragment WILDCARD = UrlFragment.parse(WILDCARD_STRING);

    final static JsonNodeMarshallContext MARSHALL_CONTEXT = JsonNodeMarshallContexts.basic();

    /**
     * {@see SpreadsheetCellSelectHistoryToken}
     */
    public static SpreadsheetCellSelectHistoryToken cellSelect(final SpreadsheetId id,
                                                               final SpreadsheetName name,
                                                               final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellSelectHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetCellClipboardCopyHistoryToken}
     */
    public static SpreadsheetCellClipboardCopyHistoryToken cellClipboardCopy(final SpreadsheetId id,
                                                                             final SpreadsheetName name,
                                                                             final AnchoredSpreadsheetSelection anchoredSelection,
                                                                             final SpreadsheetCellClipboardKind kind) {
        return SpreadsheetCellClipboardCopyHistoryToken.with(
            id,
            name,
            anchoredSelection,
            kind
        );
    }

    /**
     * {@see SpreadsheetCellClipboardCutHistoryToken}
     */
    public static SpreadsheetCellClipboardCutHistoryToken cellClipboardCut(final SpreadsheetId id,
                                                                           final SpreadsheetName name,
                                                                           final AnchoredSpreadsheetSelection anchoredSelection,
                                                                           final SpreadsheetCellClipboardKind kind) {
        return SpreadsheetCellClipboardCutHistoryToken.with(
            id,
            name,
            anchoredSelection,
            kind
        );
    }

    /**
     * {@see SpreadsheetCellClipboardPasteHistoryToken}
     */
    public static SpreadsheetCellClipboardPasteHistoryToken cellClipboardPaste(final SpreadsheetId id,
                                                                               final SpreadsheetName name,
                                                                               final AnchoredSpreadsheetSelection anchoredSelection,
                                                                               final SpreadsheetCellClipboardKind kind) {
        return SpreadsheetCellClipboardPasteHistoryToken.with(
            id,
            name,
            anchoredSelection,
            kind
        );
    }

    /**
     * {@see SpreadsheetCellDateTimeSymbolsSaveHistoryToken}
     */
    public static SpreadsheetCellDateTimeSymbolsSaveHistoryToken cellDateTimeSymbolsSave(final SpreadsheetId id,
                                                                                         final SpreadsheetName name,
                                                                                         final AnchoredSpreadsheetSelection anchoredSelection,
                                                                                         final Optional<DateTimeSymbols> dateTimeSymbols) {
        return SpreadsheetCellDateTimeSymbolsSaveHistoryToken.with(
            id,
            name,
            anchoredSelection,
            dateTimeSymbols
        );
    }

    /**
     * {@see SpreadsheetCellDateTimeSymbolsSelectHistoryToken}
     */
    public static SpreadsheetCellDateTimeSymbolsSelectHistoryToken cellDateTimeSymbolsSelect(final SpreadsheetId id,
                                                                                             final SpreadsheetName name,
                                                                                             final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellDateTimeSymbolsSelectHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetCellDateTimeSymbolsUnselectHistoryToken}
     */
    public static SpreadsheetCellDateTimeSymbolsUnselectHistoryToken cellDateTimeSymbolsUnselect(final SpreadsheetId id,
                                                                                                 final SpreadsheetName name,
                                                                                                 final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellDateTimeSymbolsUnselectHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetCellDecimalNumberSymbolsSaveHistoryToken}
     */
    public static SpreadsheetCellDecimalNumberSymbolsSaveHistoryToken cellDecimalNumberSymbolsSave(final SpreadsheetId id,
                                                                                                   final SpreadsheetName name,
                                                                                                   final AnchoredSpreadsheetSelection anchoredSelection,
                                                                                                   final Optional<DecimalNumberSymbols> decimalNumberSymbols) {
        return SpreadsheetCellDecimalNumberSymbolsSaveHistoryToken.with(
            id,
            name,
            anchoredSelection,
            decimalNumberSymbols
        );
    }

    /**
     * {@see SpreadsheetCellDecimalNumberSymbolsSelectHistoryToken}
     */
    public static SpreadsheetCellDecimalNumberSymbolsSelectHistoryToken cellDecimalNumberSymbolsSelect(final SpreadsheetId id,
                                                                                                       final SpreadsheetName name,
                                                                                                       final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellDecimalNumberSymbolsSelectHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetCellDecimalNumberSymbolsUnselectHistoryToken}
     */
    public static SpreadsheetCellDecimalNumberSymbolsUnselectHistoryToken cellDecimalNumberSymbolsUnselect(final SpreadsheetId id,
                                                                                                           final SpreadsheetName name,
                                                                                                           final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellDecimalNumberSymbolsUnselectHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetCellDeleteHistoryToken}
     */
    public static SpreadsheetCellDeleteHistoryToken cellDelete(final SpreadsheetId id,
                                                               final SpreadsheetName name,
                                                               final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellDeleteHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetCellFindHistoryToken}
     */
    public static SpreadsheetCellFindHistoryToken cellFind(final SpreadsheetId id,
                                                           final SpreadsheetName name,
                                                           final AnchoredSpreadsheetSelection anchoredSelection,
                                                           final SpreadsheetCellFindQuery query) {
        return SpreadsheetCellFindHistoryToken.with(
            id,
            name,
            anchoredSelection,
            query
        );
    }

    /**
     * {@see SpreadsheetCellFormSaveHistoryToken}
     */
    public static SpreadsheetCellFormSaveHistoryToken cellFormSave(final SpreadsheetId id,
                                                                   final SpreadsheetName name,
                                                                   final AnchoredSpreadsheetSelection anchoredSelection,
                                                                   final FormName formName,
                                                                   final Map<SpreadsheetCellReference, Optional<Object>> value) {
        return SpreadsheetCellFormSaveHistoryToken.with(
            id,
            name,
            anchoredSelection,
            formName,
            value
        );
    }

    /**
     * {@see SpreadsheetCellFormSelectHistoryToken}
     */
    public static SpreadsheetCellFormSelectHistoryToken cellFormSelect(final SpreadsheetId id,
                                                                       final SpreadsheetName name,
                                                                       final AnchoredSpreadsheetSelection anchoredSelection,
                                                                       final FormName formName) {
        return SpreadsheetCellFormSelectHistoryToken.with(
            id,
            name,
            anchoredSelection,
            formName
        );
    }

    /**
     * {@see SpreadsheetCellFormatterSaveHistoryToken}
     */
    public static SpreadsheetCellFormatterSaveHistoryToken cellFormatterSave(final SpreadsheetId id,
                                                                             final SpreadsheetName name,
                                                                             final AnchoredSpreadsheetSelection anchoredSelection,
                                                                             final Optional<SpreadsheetFormatterSelector> selector) {
        return SpreadsheetCellFormatterSaveHistoryToken.with(
            id,
            name,
            anchoredSelection,
            selector
        );
    }

    /**
     * {@see SpreadsheetCellFormatterSelectHistoryToken}
     */
    public static SpreadsheetCellFormatterSelectHistoryToken cellFormatterSelect(final SpreadsheetId id,
                                                                                 final SpreadsheetName name,
                                                                                 final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellFormatterSelectHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetCellFormatterUnselectHistoryToken}
     */
    public static SpreadsheetCellFormatterUnselectHistoryToken cellFormatterUnselect(final SpreadsheetId id,
                                                                                     final SpreadsheetName name,
                                                                                     final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellFormatterUnselectHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetCellFormulaSelectHistoryToken}
     */
    public static SpreadsheetCellFormulaSelectHistoryToken cellFormula(final SpreadsheetId id,
                                                                       final SpreadsheetName name,
                                                                       final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellFormulaSelectHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetCellFormulaMenuHistoryToken}
     */
    public static SpreadsheetCellFormulaMenuHistoryToken cellFormulaMenu(final SpreadsheetId id,
                                                                         final SpreadsheetName name,
                                                                         final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellFormulaMenuHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetCellFormulaSaveHistoryToken}
     */
    public static SpreadsheetCellFormulaSaveHistoryToken cellFormulaSave(final SpreadsheetId id,
                                                                         final SpreadsheetName name,
                                                                         final AnchoredSpreadsheetSelection anchoredSelection,
                                                                         final String formula) {
        return SpreadsheetCellFormulaSaveHistoryToken.with(
            id,
            name,
            anchoredSelection,
            formula
        );
    }

    /**
     * {@see SpreadsheetCellFreezeHistoryToken}
     */
    public static SpreadsheetCellFreezeHistoryToken cellFreeze(final SpreadsheetId id,
                                                               final SpreadsheetName name,
                                                               final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellFreezeHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetCellLabelListHistoryToken}
     */
    public static SpreadsheetCellLabelListHistoryToken cellLabels(final SpreadsheetId id,
                                                                  final SpreadsheetName name,
                                                                  final AnchoredSpreadsheetSelection anchoredSelection,
                                                                  final HistoryTokenOffsetAndCount offsetAndCount) {
        return SpreadsheetCellLabelListHistoryToken.with(
            id,
            name,
            anchoredSelection,
            offsetAndCount
        );
    }

    /**
     * {@see SpreadsheetCellLabelSaveHistoryToken}
     */
    public static SpreadsheetCellLabelSaveHistoryToken cellLabelSave(final SpreadsheetId id,
                                                                     final SpreadsheetName name,
                                                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                                                     final SpreadsheetLabelName label) {
        return SpreadsheetCellLabelSaveHistoryToken.with(
            id,
            name,
            anchoredSelection,
            label
        );
    }

    /**
     * {@see SpreadsheetCellLabelSelectHistoryToken}
     */
    public static SpreadsheetCellLabelSelectHistoryToken cellLabelSelect(final SpreadsheetId id,
                                                                         final SpreadsheetName name,
                                                                         final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellLabelSelectHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetCellLocaleSaveHistoryToken}
     */
    public static SpreadsheetCellLocaleSaveHistoryToken cellLocaleSave(final SpreadsheetId id,
                                                                       final SpreadsheetName name,
                                                                       final AnchoredSpreadsheetSelection anchoredSelection,
                                                                       final Optional<Locale> locale) {
        return SpreadsheetCellLocaleSaveHistoryToken.with(
            id,
            name,
            anchoredSelection,
            locale
        );
    }

    /**
     * {@see SpreadsheetCellLocaleSelectHistoryToken}
     */
    public static SpreadsheetCellLocaleSelectHistoryToken cellLocaleSelect(final SpreadsheetId id,
                                                                           final SpreadsheetName name,
                                                                           final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellLocaleSelectHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetCellLocaleUnselectHistoryToken}
     */
    public static SpreadsheetCellLocaleUnselectHistoryToken cellLocaleUnselect(final SpreadsheetId id,
                                                                               final SpreadsheetName name,
                                                                               final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellLocaleUnselectHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetCellMenuHistoryToken}
     */
    public static SpreadsheetCellMenuHistoryToken cellMenu(final SpreadsheetId id,
                                                           final SpreadsheetName name,
                                                           final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellMenuHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetCellNavigateHistoryToken}
     */
    public static SpreadsheetCellNavigateHistoryToken cellNavigate(final SpreadsheetId id,
                                                                   final SpreadsheetName name,
                                                                   final AnchoredSpreadsheetSelection anchoredSelection,
                                                                   final Optional<SpreadsheetViewportHomeNavigationList> navigation) {
        return SpreadsheetCellNavigateHistoryToken.with(
            id,
            name,
            anchoredSelection,
            navigation
        );
    }

    /**
     * {@see SpreadsheetCellParserSaveHistoryToken}
     */
    public static SpreadsheetCellParserSaveHistoryToken cellParserSave(final SpreadsheetId id,
                                                                       final SpreadsheetName name,
                                                                       final AnchoredSpreadsheetSelection anchoredSelection,
                                                                       final Optional<SpreadsheetParserSelector> selector) {
        return SpreadsheetCellParserSaveHistoryToken.with(
            id,
            name,
            anchoredSelection,
            selector
        );
    }

    /**
     * {@see SpreadsheetCellParserSelectHistoryToken}
     */
    public static SpreadsheetCellParserSelectHistoryToken cellParserSelect(final SpreadsheetId id,
                                                                           final SpreadsheetName name,
                                                                           final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellParserSelectHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetCellParserUnselectHistoryToken}
     */
    public static SpreadsheetCellParserUnselectHistoryToken cellParserUnselect(final SpreadsheetId id,
                                                                               final SpreadsheetName name,
                                                                               final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellParserUnselectHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetCellReferenceListHistoryToken}
     */
    public static SpreadsheetCellReferenceListHistoryToken cellReferences(final SpreadsheetId id,
                                                                          final SpreadsheetName name,
                                                                          final AnchoredSpreadsheetSelection anchoredSelection,
                                                                          final HistoryTokenOffsetAndCount offsetAndCount) {
        return SpreadsheetCellReferenceListHistoryToken.with(
            id,
            name,
            anchoredSelection,
            offsetAndCount
        );
    }

    /**
     * {@see SpreadsheetCellReloadHistoryToken}
     */
    public static SpreadsheetCellReloadHistoryToken cellReload(final SpreadsheetId id,
                                                               final SpreadsheetName name,
                                                               final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellReloadHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetCellSaveCellHistoryToken}
     */
    public static SpreadsheetCellSaveCellHistoryToken cellSaveCell(final SpreadsheetId id,
                                                                   final SpreadsheetName name,
                                                                   final AnchoredSpreadsheetSelection anchoredSelection,
                                                                   final Set<SpreadsheetCell> value) {
        return SpreadsheetCellSaveCellHistoryToken.with(
            id,
            name,
            anchoredSelection,
            value
        );
    }

    /**
     * {@see SpreadsheetCellSaveDateTimeSymbolsHistoryToken}
     */
    public static SpreadsheetCellSaveDateTimeSymbolsHistoryToken cellSaveDateTimeSymbols(final SpreadsheetId id,
                                                                                         final SpreadsheetName name,
                                                                                         final AnchoredSpreadsheetSelection anchoredSelection,
                                                                                         final Map<SpreadsheetCellReference, Optional<DateTimeSymbols>> value) {
        return SpreadsheetCellSaveDateTimeSymbolsHistoryToken.with(
            id,
            name,
            anchoredSelection,
            value
        );
    }

    /**
     * {@see SpreadsheetCellSaveDecimalNumberSymbolsHistoryToken}
     */
    public static SpreadsheetCellSaveDecimalNumberSymbolsHistoryToken cellSaveDecimalNumberSymbols(final SpreadsheetId id,
                                                                                                   final SpreadsheetName name,
                                                                                                   final AnchoredSpreadsheetSelection anchoredSelection,
                                                                                                   final Map<SpreadsheetCellReference, Optional<DecimalNumberSymbols>> value) {
        return SpreadsheetCellSaveDecimalNumberSymbolsHistoryToken.with(
            id,
            name,
            anchoredSelection,
            value
        );
    }

    /**
     * {@see SpreadsheetCellSaveFormatterHistoryToken}
     */
    public static SpreadsheetCellSaveFormatterHistoryToken cellSaveFormatter(final SpreadsheetId id,
                                                                             final SpreadsheetName name,
                                                                             final AnchoredSpreadsheetSelection anchoredSelection,
                                                                             final Map<SpreadsheetCellReference, Optional<SpreadsheetFormatterSelector>> value) {
        return SpreadsheetCellSaveFormatterHistoryToken.with(
            id,
            name,
            anchoredSelection,
            value
        );
    }


    /**
     * {@see SpreadsheetCellSaveFormulaTextHistoryToken}
     */
    public static SpreadsheetCellSaveFormulaTextHistoryToken cellSaveFormulaText(final SpreadsheetId id,
                                                                                 final SpreadsheetName name,
                                                                                 final AnchoredSpreadsheetSelection anchoredSelection,
                                                                                 final Map<SpreadsheetCellReference, String> formulas) {
        return SpreadsheetCellSaveFormulaTextHistoryToken.with(
            id,
            name,
            anchoredSelection,
            formulas
        );
    }

    /**
     * {@see SpreadsheetCellSaveLocaleHistoryToken}
     */
    public static SpreadsheetCellSaveLocaleHistoryToken cellSaveLocale(final SpreadsheetId id,
                                                                       final SpreadsheetName name,
                                                                       final AnchoredSpreadsheetSelection anchoredSelection,
                                                                       final Map<SpreadsheetCellReference, Optional<Locale>> value) {
        return SpreadsheetCellSaveLocaleHistoryToken.with(
            id,
            name,
            anchoredSelection,
            value
        );
    }

    /**
     * {@see SpreadsheetCellSaveParserHistoryToken}
     */
    public static SpreadsheetCellSaveParserHistoryToken cellSaveParser(final SpreadsheetId id,
                                                                       final SpreadsheetName name,
                                                                       final AnchoredSpreadsheetSelection anchoredSelection,
                                                                       final Map<SpreadsheetCellReference, Optional<SpreadsheetParserSelector>> value) {
        return SpreadsheetCellSaveParserHistoryToken.with(
            id,
            name,
            anchoredSelection,
            value
        );
    }

    /**
     * {@see SpreadsheetCellSaveStyleHistoryToken}
     */
    public static SpreadsheetCellSaveStyleHistoryToken cellSaveStyle(final SpreadsheetId id,
                                                                     final SpreadsheetName name,
                                                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                                                     final Map<SpreadsheetCellReference, TextStyle> styles) {
        return SpreadsheetCellSaveStyleHistoryToken.with(
            id,
            name,
            anchoredSelection,
            styles
        );
    }

    /**
     * {@see SpreadsheetCellSaveValidatorHistoryToken}
     */
    public static SpreadsheetCellSaveValidatorHistoryToken cellSaveValidator(final SpreadsheetId id,
                                                                             final SpreadsheetName name,
                                                                             final AnchoredSpreadsheetSelection anchoredSelection,
                                                                             final Map<SpreadsheetCellReference, Optional<ValidatorSelector>> cellToValidator) {
        return SpreadsheetCellSaveValidatorHistoryToken.with(
            id,
            name,
            anchoredSelection,
            cellToValidator
        );
    }

    /**
     * {@see SpreadsheetCellSaveValueTypeHistoryToken}
     */
    public static SpreadsheetCellSaveValueTypeHistoryToken cellSaveValueType(final SpreadsheetId id,
                                                                             final SpreadsheetName name,
                                                                             final AnchoredSpreadsheetSelection anchoredSelection,
                                                                             final Map<SpreadsheetCellReference, Optional<ValueTypeName>> cellToValueType) {
        return SpreadsheetCellSaveValueTypeHistoryToken.with(
            id,
            name,
            anchoredSelection,
            cellToValueType
        );
    }

    // cell sort........................................................................................................

    /**
     * {@see SpreadsheetCellSortEditHistoryToken}
     */
    public static SpreadsheetCellSortEditHistoryToken cellSortEdit(final SpreadsheetId id,
                                                                   final SpreadsheetName name,
                                                                   final AnchoredSpreadsheetSelection anchoredSelection,
                                                                   final String comparatorNames) {
        return SpreadsheetCellSortEditHistoryToken.with(
            id,
            name,
            anchoredSelection,
            comparatorNames
        );
    }

    /**
     * {@see SpreadsheetCellSortSaveHistoryToken}
     */
    public static SpreadsheetCellSortSaveHistoryToken cellSortSave(final SpreadsheetId id,
                                                                   final SpreadsheetName name,
                                                                   final AnchoredSpreadsheetSelection anchoredSelection,
                                                                   final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparatorNames) {
        return SpreadsheetCellSortSaveHistoryToken.with(
            id,
            name,
            anchoredSelection,
            comparatorNames
        );
    }

    // cell style.......................................................................................................

    /**
     * {@see SpreadsheetCellStyleSelectHistoryToken}
     */
    public static <T> SpreadsheetCellStyleSelectHistoryToken<T> cellStyle(final SpreadsheetId id,
                                                                          final SpreadsheetName name,
                                                                          final AnchoredSpreadsheetSelection anchoredSelection,
                                                                          final TextStylePropertyName<T> propertyName) {
        return SpreadsheetCellStyleSelectHistoryToken.with(
            id,
            name,
            anchoredSelection,
            propertyName
        );
    }

    /**
     * {@see SpreadsheetCellStyleSaveHistoryToken}
     */
    public static <T> SpreadsheetCellStyleSaveHistoryToken<T> cellStyleSave(final SpreadsheetId id,
                                                                            final SpreadsheetName name,
                                                                            final AnchoredSpreadsheetSelection anchoredSelection,
                                                                            final TextStylePropertyName<T> propertyName,
                                                                            final Optional<T> propertyValue) {
        return SpreadsheetCellStyleSaveHistoryToken.with(
            id,
            name,
            anchoredSelection,
            propertyName,
            propertyValue
        );
    }

    /**
     * {@see SpreadsheetCellUnfreezeHistoryToken}
     */
    public static SpreadsheetCellUnfreezeHistoryToken cellUnfreeze(final SpreadsheetId id,
                                                                   final SpreadsheetName name,
                                                                   final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellUnfreezeHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    // cellValue........................................................................................................

    /**
     * {@see SpreadsheetCellValueSaveHistoryToken}
     */
    public static SpreadsheetCellValueSaveHistoryToken cellValueSave(final SpreadsheetId id,
                                                                     final SpreadsheetName name,
                                                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                                                     final ValueTypeName valueType,
                                                                     final String value) {
        return SpreadsheetCellValueSaveHistoryToken.with(
            id,
            name,
            anchoredSelection,
            valueType,
            value
        );
    }

    /**
     * {@see SpreadsheetCellValueSelectHistoryToken}
     */
    public static SpreadsheetCellValueSelectHistoryToken cellValueSelect(final SpreadsheetId id,
                                                                         final SpreadsheetName name,
                                                                         final AnchoredSpreadsheetSelection anchoredSelection,
                                                                         final ValueTypeName valueType) {
        return SpreadsheetCellValueSelectHistoryToken.with(
            id,
            name,
            anchoredSelection,
            valueType
        );
    }

    /**
     * {@see SpreadsheetCellValueUnselectHistoryToken}
     */
    public static SpreadsheetCellValueUnselectHistoryToken cellValueUnselect(final SpreadsheetId id,
                                                                             final SpreadsheetName name,
                                                                             final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellValueUnselectHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    // cellValueType....................................................................................................

    /**
     * {@see SpreadsheetCellValueTypeSaveHistoryToken}
     */
    public static SpreadsheetCellValueTypeSaveHistoryToken cellValueTypeSave(final SpreadsheetId id,
                                                                             final SpreadsheetName name,
                                                                             final AnchoredSpreadsheetSelection anchoredSelection,
                                                                             final Optional<ValueTypeName> valueType) {
        return SpreadsheetCellValueTypeSaveHistoryToken.with(
            id,
            name,
            anchoredSelection,
            valueType
        );
    }

    /**
     * {@see SpreadsheetCellValueTypeSelectHistoryToken}
     */
    public static SpreadsheetCellValueTypeSelectHistoryToken cellValueTypeSelect(final SpreadsheetId id,
                                                                                 final SpreadsheetName name,
                                                                                 final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellValueTypeSelectHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetCellValueTypeUnselectHistoryToken}
     */
    public static SpreadsheetCellValueTypeUnselectHistoryToken cellValueTypeUnselect(final SpreadsheetId id,
                                                                                     final SpreadsheetName name,
                                                                                     final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellValueTypeUnselectHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    // cell ValidatorSelector...........................................................................................

    /**
     * {@see SpreadsheetCellValidatorSaveHistoryToken}
     */
    public static SpreadsheetCellValidatorSaveHistoryToken cellValidatorSave(final SpreadsheetId id,
                                                                             final SpreadsheetName name,
                                                                             final AnchoredSpreadsheetSelection anchoredSelection,
                                                                             final Optional<ValidatorSelector> validatorSelector) {
        return SpreadsheetCellValidatorSaveHistoryToken.with(
            id,
            name,
            anchoredSelection,
            validatorSelector
        );
    }

    /**
     * {@see SpreadsheetCellValidatorSelectHistoryToken}
     */
    public static SpreadsheetCellValidatorSelectHistoryToken cellValidatorSelect(final SpreadsheetId id,
                                                                                 final SpreadsheetName name,
                                                                                 final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellValidatorSelectHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetCellValidatorUnselectHistoryToken}
     */
    public static SpreadsheetCellValidatorUnselectHistoryToken cellValidatorUnselect(final SpreadsheetId id,
                                                                                     final SpreadsheetName name,
                                                                                     final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellValidatorUnselectHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    // column...........................................................................................................

    /**
     * {@see SpreadsheetColumnSelectHistoryToken}
     */
    public static SpreadsheetColumnSelectHistoryToken columnSelect(final SpreadsheetId id,
                                                                   final SpreadsheetName name,
                                                                   final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetColumnSelectHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetColumnClearHistoryToken}
     */
    public static SpreadsheetColumnClearHistoryToken columnClear(final SpreadsheetId id,
                                                                 final SpreadsheetName name,
                                                                 final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetColumnClearHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetColumnDeleteHistoryToken}
     */
    public static SpreadsheetColumnDeleteHistoryToken columnDelete(final SpreadsheetId id,
                                                                   final SpreadsheetName name,
                                                                   final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetColumnDeleteHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetColumnFreezeHistoryToken}
     */
    public static SpreadsheetColumnFreezeHistoryToken columnFreeze(final SpreadsheetId id,
                                                                   final SpreadsheetName name,
                                                                   final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetColumnFreezeHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetColumnInsertAfterHistoryToken}
     */
    public static SpreadsheetColumnInsertAfterHistoryToken columnInsertAfter(final SpreadsheetId id,
                                                                             final SpreadsheetName name,
                                                                             final AnchoredSpreadsheetSelection anchoredSelection,
                                                                             final OptionalInt count) {
        return SpreadsheetColumnInsertAfterHistoryToken.with(
            id,
            name,
            anchoredSelection,
            count
        );
    }

    /**
     * {@see SpreadsheetColumnInsertBeforeHistoryToken}
     */
    public static SpreadsheetColumnInsertBeforeHistoryToken columnInsertBefore(final SpreadsheetId id,
                                                                               final SpreadsheetName name,
                                                                               final AnchoredSpreadsheetSelection anchoredSelection,
                                                                               final OptionalInt count) {
        return SpreadsheetColumnInsertBeforeHistoryToken.with(
            id,
            name,
            anchoredSelection,
            count
        );
    }

    /**
     * {@see SpreadsheetColumnMenuHistoryToken}
     */
    public static SpreadsheetColumnMenuHistoryToken columnMenu(final SpreadsheetId id,
                                                               final SpreadsheetName name,
                                                               final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetColumnMenuHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetColumnNavigateHistoryToken}
     */
    public static SpreadsheetColumnNavigateHistoryToken columnNavigate(final SpreadsheetId id,
                                                                       final SpreadsheetName name,
                                                                       final AnchoredSpreadsheetSelection anchoredSelection,
                                                                       final Optional<SpreadsheetViewportHomeNavigationList> navigation) {
        return SpreadsheetColumnNavigateHistoryToken.with(
            id,
            name,
            anchoredSelection,
            navigation
        );
    }

    // column sort........................................................................................................

    /**
     * {@see SpreadsheetColumnSortEditHistoryToken}
     */
    public static SpreadsheetColumnSortEditHistoryToken columnSortEdit(final SpreadsheetId id,
                                                                       final SpreadsheetName name,
                                                                       final AnchoredSpreadsheetSelection anchoredSelection,
                                                                       final String comparatorNames) {
        return SpreadsheetColumnSortEditHistoryToken.with(
            id,
            name,
            anchoredSelection,
            comparatorNames
        );
    }

    /**
     * {@see SpreadsheetColumnSortSaveHistoryToken}
     */
    public static SpreadsheetColumnSortSaveHistoryToken columnSortSave(final SpreadsheetId id,
                                                                       final SpreadsheetName name,
                                                                       final AnchoredSpreadsheetSelection anchoredSelection,
                                                                       final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparatorNames) {
        return SpreadsheetColumnSortSaveHistoryToken.with(
            id,
            name,
            anchoredSelection,
            comparatorNames
        );
    }

    // unfreeze.........................................................................................................

    /**
     * {@see SpreadsheetColumnUnfreezeHistoryToken}
     */
    public static SpreadsheetColumnUnfreezeHistoryToken columnUnfreeze(final SpreadsheetId id,
                                                                       final SpreadsheetName name,
                                                                       final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetColumnUnfreezeHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    // form.............................................................................................................

    /**
     * {@see SpreadsheetFormDeleteHistoryToken}
     */
    public static SpreadsheetFormDeleteHistoryToken formDelete(final SpreadsheetId id,
                                                               final SpreadsheetName name,
                                                               final FormName formName) {
        return SpreadsheetFormDeleteHistoryToken.with(
            id,
            name,
            formName
        );
    }

    /**
     * {@see SpreadsheetFormListHistoryToken}
     */
    public static SpreadsheetFormListHistoryToken formList(final SpreadsheetId id,
                                                           final SpreadsheetName name,
                                                           final HistoryTokenOffsetAndCount offsetAndCount) {
        return SpreadsheetFormListHistoryToken.with(
            id,
            name,
            offsetAndCount
        );
    }

    /**
     * {@see SpreadsheetFormSelectHistoryToken}
     */
    public static SpreadsheetFormSelectHistoryToken formSelect(final SpreadsheetId id,
                                                               final SpreadsheetName name,
                                                               final FormName formName) {
        return SpreadsheetFormSelectHistoryToken.with(
            id,
            name,
            formName
        );
    }

    /**
     * {@see SpreadsheetFormSaveHistoryToken}
     */
    public static SpreadsheetFormSaveHistoryToken formSave(final SpreadsheetId id,
                                                           final SpreadsheetName name,
                                                           final Form<SpreadsheetExpressionReference> form) {
        return SpreadsheetFormSaveHistoryToken.with(
            id,
            name,
            form
        );
    }

    // label............................................................................................................

    /**
     * {@see SpreadsheetLabelMappingCreateHistoryToken}
     */
    public static SpreadsheetLabelMappingCreateHistoryToken labelMappingCreate(final SpreadsheetId id,
                                                                               final SpreadsheetName name) {
        return SpreadsheetLabelMappingCreateHistoryToken.with(
            id,
            name
        );
    }

    /**
     * {@see SpreadsheetLabelMappingSelectHistoryToken}
     */
    public static SpreadsheetLabelMappingSelectHistoryToken labelMappingSelect(final SpreadsheetId id,
                                                                               final SpreadsheetName name,
                                                                               final SpreadsheetLabelName label) {
        return SpreadsheetLabelMappingSelectHistoryToken.with(
            id,
            name,
            label
        );
    }

    /**
     * {@see SpreadsheetLabelMappingDeleteHistoryToken}
     */
    public static SpreadsheetLabelMappingDeleteHistoryToken labelMappingDelete(final SpreadsheetId id,
                                                                               final SpreadsheetName name,
                                                                               final SpreadsheetLabelName labelName) {
        return SpreadsheetLabelMappingDeleteHistoryToken.with(
            id,
            name,
            labelName
        );
    }

    /**
     * {@see SpreadsheetLabelMappingListHistoryToken}
     */
    public static SpreadsheetLabelMappingListHistoryToken labelMappingList(final SpreadsheetId id,
                                                                           final SpreadsheetName name,
                                                                           final HistoryTokenOffsetAndCount offsetAndCount) {
        return SpreadsheetLabelMappingListHistoryToken.with(
            id,
            name,
            offsetAndCount
        );
    }

    /**
     * {@see SpreadsheetLabelMappingSaveHistoryToken}
     */
    public static SpreadsheetLabelMappingSaveHistoryToken labelMappingSave(final SpreadsheetId id,
                                                                           final SpreadsheetName name,
                                                                           final SpreadsheetLabelMapping mapping) {
        return SpreadsheetLabelMappingSaveHistoryToken.with(
            id,
            name,
            mapping
        );
    }

    // metadata.........................................................................................................

    /**
     * {@see SpreadsheetMetadataPropertySelectHistoryToken}
     */
    public static <T> SpreadsheetMetadataPropertySelectHistoryToken<T> metadataPropertySelect(final SpreadsheetId id,
                                                                                              final SpreadsheetName name,
                                                                                              final SpreadsheetMetadataPropertyName<T> propertyName) {
        return SpreadsheetMetadataPropertySelectHistoryToken.with(
            id,
            name,
            propertyName
        );
    }

    /**
     * {@see SpreadsheetMetadataPropertySaveHistoryToken}
     */
    public static <T> SpreadsheetMetadataPropertySaveHistoryToken<T> metadataPropertySave(final SpreadsheetId id,
                                                                                          final SpreadsheetName name,
                                                                                          final SpreadsheetMetadataPropertyName<T> propertyName,
                                                                                          final Optional<T> propertyValue) {
        return SpreadsheetMetadataPropertySaveHistoryToken.with(
            id,
            name,
            propertyName,
            propertyValue
        );
    }

    /**
     * {@see SpreadsheetMetadataPropertyStyleSelectHistoryToken}
     */
    public static <T> SpreadsheetMetadataPropertyStyleSelectHistoryToken<T> metadataPropertyStyle(final SpreadsheetId id,
                                                                                                  final SpreadsheetName name,
                                                                                                  final TextStylePropertyName<T> stylePropertyName) {
        return SpreadsheetMetadataPropertyStyleSelectHistoryToken.with(
            id,
            name,
            stylePropertyName
        );
    }

    /**
     * {@see SpreadsheetMetadataPropertyStyleSaveHistoryToken}
     */
    public static <T> SpreadsheetMetadataPropertyStyleSaveHistoryToken<T> metadataPropertyStyleSave(final SpreadsheetId id,
                                                                                                    final SpreadsheetName name,
                                                                                                    final TextStylePropertyName<T> stylePropertyName,
                                                                                                    final Optional<T> stylePropertyValue) {
        return SpreadsheetMetadataPropertyStyleSaveHistoryToken.with(
            id,
            name,
            stylePropertyName,
            stylePropertyValue
        );
    }

    /**
     * {@see SpreadsheetMetadataSelectHistoryToken}
     */
    public static SpreadsheetMetadataSelectHistoryToken metadataSelect(final SpreadsheetId id,
                                                                       final SpreadsheetName name) {
        return SpreadsheetMetadataSelectHistoryToken.with(
            id,
            name
        );
    }

    // navigate.........................................................................................................

    /**
     * {@see SpreadsheetNavigateHistoryToken}
     */
    public static SpreadsheetNavigateHistoryToken navigate(final SpreadsheetId id,
                                                           final SpreadsheetName name,
                                                           final Optional<SpreadsheetViewportHomeNavigationList> navigation) {
        return SpreadsheetNavigateHistoryToken.with(
            id,
            name,
            navigation
        );
    }

    // plugin...........................................................................................................

    /**
     * {@see PluginDeleteHistoryToken}
     */
    public static PluginDeleteHistoryToken pluginDelete(final PluginName name) {
        return PluginDeleteHistoryToken.with(
            name
        );
    }

    /**
     * {@see PluginFileViewHistoryToken}
     */
    public static PluginFileViewHistoryToken pluginFileView(final PluginName name,
                                                            final Optional<JarEntryInfoName> file) {
        return PluginFileViewHistoryToken.with(
            name,
            file
        );
    }

    /**
     * {@see PluginListReloadHistoryToken}
     */
    public static PluginListReloadHistoryToken pluginListReload(final HistoryTokenOffsetAndCount offsetAndCount) {
        return PluginListReloadHistoryToken.with(offsetAndCount);
    }

    /**
     * {@see PluginListSelectHistoryToken}
     */
    public static PluginListSelectHistoryToken pluginListSelect(final HistoryTokenOffsetAndCount offsetAndCount) {
        return PluginListSelectHistoryToken.with(offsetAndCount);
    }

    /**
     * {@see PluginSaveHistoryToken}
     */
    public static PluginSaveHistoryToken pluginSave(final PluginName name,
                                                    final String save) {
        return PluginSaveHistoryToken.with(
            name,
            save
        );
    }

    /**
     * {@see PluginSelectHistoryToken}
     */
    public static PluginSelectHistoryToken pluginSelect(final PluginName name) {
        return PluginSelectHistoryToken.with(
            name
        );
    }

    /**
     * {@see PluginUploadSaveHistoryToken}
     */
    public static PluginUploadSaveHistoryToken pluginUploadSave(final BrowserFile file) {
        return PluginUploadSaveHistoryToken.with(file);
    }

    /**
     * {@see PluginUploadSelectHistoryToken}
     */
    public static PluginUploadSelectHistoryToken pluginUploadSelect() {
        return PluginUploadSelectHistoryToken.INSTANCE;
    }

    // row..............................................................................................................

    /**
     * {@see SpreadsheetRowSelectHistoryToken}
     */
    public static SpreadsheetRowSelectHistoryToken rowSelect(final SpreadsheetId id,
                                                             final SpreadsheetName name,
                                                             final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetRowSelectHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetRowClearHistoryToken}
     */
    public static SpreadsheetRowClearHistoryToken rowClear(final SpreadsheetId id,
                                                           final SpreadsheetName name,
                                                           final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetRowClearHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetRowDeleteHistoryToken}
     */
    public static SpreadsheetRowDeleteHistoryToken rowDelete(final SpreadsheetId id,
                                                             final SpreadsheetName name,
                                                             final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetRowDeleteHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetRowFreezeHistoryToken}
     */
    public static SpreadsheetRowFreezeHistoryToken rowFreeze(final SpreadsheetId id,
                                                             final SpreadsheetName name,
                                                             final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetRowFreezeHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetRowInsertAfterHistoryToken}
     */
    public static SpreadsheetRowInsertAfterHistoryToken rowInsertAfter(final SpreadsheetId id,
                                                                       final SpreadsheetName name,
                                                                       final AnchoredSpreadsheetSelection anchoredSelection,
                                                                       final OptionalInt count) {
        return SpreadsheetRowInsertAfterHistoryToken.with(
            id,
            name,
            anchoredSelection,
            count
        );
    }

    /**
     * {@see SpreadsheetRowInsertBeforeHistoryToken}
     */
    public static SpreadsheetRowInsertBeforeHistoryToken rowInsertBefore(final SpreadsheetId id,
                                                                         final SpreadsheetName name,
                                                                         final AnchoredSpreadsheetSelection anchoredSelection,
                                                                         final OptionalInt count) {
        return SpreadsheetRowInsertBeforeHistoryToken.with(
            id,
            name,
            anchoredSelection,
            count
        );
    }

    /**
     * {@see SpreadsheetRowMenuHistoryToken}
     */
    public static SpreadsheetRowMenuHistoryToken rowMenu(final SpreadsheetId id,
                                                         final SpreadsheetName name,
                                                         final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetRowMenuHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetRowNavigateHistoryToken}
     */
    public static SpreadsheetRowNavigateHistoryToken rowNavigate(final SpreadsheetId id,
                                                                 final SpreadsheetName name,
                                                                 final AnchoredSpreadsheetSelection anchoredSelection,
                                                                 final Optional<SpreadsheetViewportHomeNavigationList> navigation) {
        return SpreadsheetRowNavigateHistoryToken.with(
            id,
            name,
            anchoredSelection,
            navigation
        );
    }

    // row... sort......................................................................................................

    /**
     * {@see SpreadsheetRowSortEditHistoryToken}
     */
    public static SpreadsheetRowSortEditHistoryToken rowSortEdit(final SpreadsheetId id,
                                                                 final SpreadsheetName name,
                                                                 final AnchoredSpreadsheetSelection anchoredSelection,
                                                                 final String comparatorNames) {
        return SpreadsheetRowSortEditHistoryToken.with(
            id,
            name,
            anchoredSelection,
            comparatorNames
        );
    }

    /**
     * {@see SpreadsheetRowSortSaveHistoryToken}
     */
    public static SpreadsheetRowSortSaveHistoryToken rowSortSave(final SpreadsheetId id,
                                                                 final SpreadsheetName name,
                                                                 final AnchoredSpreadsheetSelection anchoredSelection,
                                                                 final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparatorNames) {
        return SpreadsheetRowSortSaveHistoryToken.with(
            id,
            name,
            anchoredSelection,
            comparatorNames
        );
    }

    // row unfreeze.....................................................................................................

    /**
     * {@see SpreadsheetRowUnfreezeHistoryToken}
     */
    public static SpreadsheetRowUnfreezeHistoryToken rowUnfreeze(final SpreadsheetId id,
                                                                 final SpreadsheetName name,
                                                                 final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetRowUnfreezeHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    // selection........................................................................................................

    public static SpreadsheetAnchoredSelectionHistoryToken selection(final SpreadsheetId id,
                                                                     final SpreadsheetName name,
                                                                     final AnchoredSpreadsheetSelection anchoredSelection) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(anchoredSelection, "anchoredSelection");

        SpreadsheetAnchoredSelectionHistoryToken historyToken;

        final SpreadsheetSelection selection = anchoredSelection.selection();

        for (; ; ) {
            if (selection.isCellOrCellRange() || selection.isLabelName()) {
                historyToken = cellSelect(
                    id,
                    name,
                    anchoredSelection
                );
                break;
            }
            if (selection.isColumnOrColumnRange()) {
                historyToken = columnSelect(
                    id,
                    name,
                    anchoredSelection
                );
                break;
            }
            if (selection.isRowOrRowRange()) {
                historyToken = rowSelect(
                    id,
                    name,
                    anchoredSelection
                );
                break;
            }

            throw new UnsupportedOperationException("Unexpected selection type " + selection);
        }

        return historyToken;
    }

    // spreadsheet......................................................................................................

    /**
     * {@see SpreadsheetCreateHistoryToken}
     */
    public static SpreadsheetCreateHistoryToken spreadsheetCreate() {
        return SpreadsheetCreateHistoryToken.with();
    }

    /**
     * {@see SpreadsheetListReloadHistoryToken}
     */
    public static SpreadsheetListHistoryToken spreadsheetListReload(final HistoryTokenOffsetAndCount offsetAndCount) {
        return SpreadsheetListReloadHistoryToken.with(offsetAndCount);
    }

    /**
     * {@see SpreadsheetListSelectHistoryToken}
     */
    public static SpreadsheetListHistoryToken spreadsheetListSelect(final HistoryTokenOffsetAndCount offsetAndCount) {
        return SpreadsheetListSelectHistoryToken.with(offsetAndCount);
    }

    /**
     * {@see SpreadsheetListDeleteHistoryToken}
     */
    public static SpreadsheetListDeleteHistoryToken spreadsheetListDelete(final SpreadsheetId id) {
        return SpreadsheetListDeleteHistoryToken.with(id);
    }

    /**
     * {@see SpreadsheetListRenameSelectHistoryToken}
     */
    public static SpreadsheetListRenameSelectHistoryToken spreadsheetListRenameSelect(final SpreadsheetId id) {
        return SpreadsheetListRenameSelectHistoryToken.with(id);
    }

    /**
     * {@see SpreadsheetListRenameSaveHistoryToken}
     */
    public static SpreadsheetListRenameSaveHistoryToken spreadsheetListRenameSave(final SpreadsheetId id,
                                                                                  final SpreadsheetName value) {
        return SpreadsheetListRenameSaveHistoryToken.with(
            id,
            value
        );
    }

    /**
     * {@see SpreadsheetLoadHistoryToken}
     */
    public static SpreadsheetLoadHistoryToken spreadsheetLoad(final SpreadsheetId id) {
        return SpreadsheetLoadHistoryToken.with(
            id
        );
    }

    /**
     * {@see SpreadsheetReloadHistoryToken}
     */
    public static SpreadsheetReloadHistoryToken spreadsheetReload(final SpreadsheetId id,
                                                                  final SpreadsheetName name) {
        return SpreadsheetReloadHistoryToken.with(
            id,
            name
        );
    }

    /**
     * {@see SpreadsheetRenameSelectHistoryToken}
     */
    public static SpreadsheetRenameSelectHistoryToken spreadsheetRenameSelect(final SpreadsheetId id,
                                                                              final SpreadsheetName name) {
        return SpreadsheetRenameSelectHistoryToken.with(
            id,
            name
        );
    }

    /**
     * {@see SpreadsheetRenameSaveHistoryToken}
     */
    public static SpreadsheetRenameSaveHistoryToken spreadsheetRenameSave(final SpreadsheetId id,
                                                                          final SpreadsheetName name,
                                                                          final SpreadsheetName value) {
        return SpreadsheetRenameSaveHistoryToken.with(
            id,
            name,
            value
        );
    }

    /**
     * {@see SpreadsheetSelectHistoryToken}
     */
    public static SpreadsheetSelectHistoryToken spreadsheetSelect(final SpreadsheetId id,
                                                                  final SpreadsheetName name) {
        return SpreadsheetSelectHistoryToken.with(
            id,
            name
        );
    }

    /**
     * {@see UnknownHistoryToken}
     */
    public static UnknownHistoryToken unknown(final UrlFragment fragment) {
        return UnknownHistoryToken.with(fragment);
    }

    // parse............................................................................................................

    final HistoryToken parseSave(final TextCursor cursor) {
        HistoryToken historyToken;

        if (this instanceof SpreadsheetCellSelectHistoryToken) {
            historyToken = this.cast(SpreadsheetCellSelectHistoryToken.class)
                .parseCellSave(cursor);
        } else {
            historyToken = this.setSaveStringValue(
                parseUntilEmpty(
                    cursor,
                    true // skipLeadingSlash
                )
            );
        }

        return historyToken;
    }

    /**
     * Parses the given {@link String} if that matching fails a {@link SpreadsheetListSelectHistoryToken} is returned.
     */
    public static HistoryToken parseString(final String fragment) {
        return parse(
            UrlFragment.parse(fragment)
        );
    }

    /**
     * Parses the given {@link UrlFragment} if that matching fails a {@link SpreadsheetListSelectHistoryToken} is returned.
     */
    public static HistoryToken parse(final UrlFragment fragment) {
        Objects.requireNonNull(fragment, "fragment");

        HistoryToken token = null;

        final TextCursor cursor = TextCursors.charSequence(fragment.value());
        if (cursor.isEmpty()) {
            token = SPREADSHEET_LIST_SELECT_HISTORY_TOKEN;
        } else {
            try {
                final String component = parseComponentOrEmpty(cursor);
                switch (component) {
                    case SELECT_STRING:
                        token = SPREADSHEET_LIST_SELECT_HISTORY_TOKEN;
                        break;
                    case WILDCARD_STRING:
                        token = SPREADSHEET_LIST_SELECT_HISTORY_TOKEN;
                        token = token.parseOffsetCountReload(cursor);
                        break;
                    case CREATE_STRING:
                        token = HistoryToken.spreadsheetCreate()
                            .parse(cursor);
                        break;
                    case DELETE_STRING:
                        token = parseDelete(cursor);
                        break;
                    case PLUGIN_STRING:
                        token = PLUGIN_LIST_SELECT_HISTORY_TOKEN;
                        token = token.parse(cursor);
                        break;
                    case PLUGIN_UPLOAD_STRING:
                        token = HistoryToken.pluginUploadSelect();
                        token = token.parse(cursor);
                        break;
                    case RENAME_STRING:
                        token = SPREADSHEET_LIST_SELECT_HISTORY_TOKEN;
                        token = parseRename(cursor);
                        break;
                    default:
                        token = SPREADSHEET_LIST_SELECT_HISTORY_TOKEN;
                        token = spreadsheetLoad(
                            SpreadsheetId.parse(component)
                        ).parse(cursor);
                }
            } catch (final RuntimeException ignore) {
                // nop
            }
        }

        if (null == token) {
            token = UnknownHistoryToken.with(fragment);
        }

        return token;
    }

    private final static SpreadsheetListHistoryToken SPREADSHEET_LIST_SELECT_HISTORY_TOKEN = HistoryToken.spreadsheetListSelect(
        HistoryTokenOffsetAndCount.EMPTY
    );

    private final static PluginListSelectHistoryToken PLUGIN_LIST_SELECT_HISTORY_TOKEN = PluginListSelectHistoryToken.with(
        HistoryTokenOffsetAndCount.EMPTY
    );

    /**
     * Consumes all remaining text into a {@link String}.
     */
    static String parseUntilEmpty(final TextCursor cursor,
                                  final boolean skipLeadingSlash) {
        final TextCursorSavePoint save = cursor.save();
        cursor.end();

        final String text = save.textBetween()
            .toString();

        return skipLeadingSlash && text.startsWith("/") ?
            text.substring(1) :
            text;
    }

    /**
     * Consumes a path component within a {@link TextCursor}.
     */
    static Optional<String> parseComponent(final TextCursor cursor) {
        return COMPONENT.parse(cursor, CONTEXT)
            .map(p -> p.cast(StringParserToken.class)
                .value()
                .substring(1)
            );
    }

    /**
     * The separator character used between components or complex path of a {@link HistoryToken}.
     */
    public final static CharacterConstant SEPARATOR = CharacterConstant.with('/');

    /**
     * A {@link Parser} that consumes a path component within an {@link UrlFragment}.
     */
    private final static Parser<ParserContext> COMPONENT = Parsers.initialAndPartCharPredicateString(
        CharPredicates.is(SEPARATOR.character()),
        CharPredicates.not(
            CharPredicates.is(SEPARATOR.character())
        ),
        1,
        MAX_LENGTH
    );

    private final static ParserContext CONTEXT = ParserContexts.fake();

    static String parseComponentOrEmpty(final TextCursor cursor) {
        return parseComponent(cursor)
            .orElse("");
    }

    static <T> T parseJson(final TextCursor cursor,
                           final Class<T> type) {
        return parseJson(
            JsonNode.parse(
                parseUntilEmpty(
                    cursor,
                    true // skipLeadingSlash
                )
            ),
            type
        );
    }

    static <T> T parseJson(final JsonNode json,
                           final Class<T> type) {
        return UNMARSHALL_CONTEXT.unmarshall(
            json,
            type
        );
    }

    final static JsonNodeUnmarshallContext UNMARSHALL_CONTEXT = JsonNodeUnmarshallContexts.basic(
        ExpressionNumberKind.BIG_DECIMAL,
        MathContext.DECIMAL64
    );

    static OptionalInt parseOptionalInt(final TextCursor cursor) {
        return parseComponent(cursor)
            .map(
                (final String token) -> token.isEmpty() ?
                    OptionalInt.empty() :
                    OptionalInt.of(
                        Integer.parseInt(token)
                    )
            ).orElse(OptionalInt.empty());
    }

    // ctor.............................................................................................................

    /**
     * Package private to limit sub-classing
     */
    HistoryToken() {
        super();
    }

    // parse............................................................................................................

    final HistoryToken parse(final TextCursor cursor) {
        HistoryToken result = this;

        try {
            final Optional<String> component = parseComponent(cursor);
            if (component.isPresent()) {
                result = result.parseNext(
                    component.get(),
                    cursor
                );

                result = result.parse(cursor);
            }
        } catch (final RuntimeException ignore) {
            result = this;
            cursor.end();
        }

        return result;
    }

    abstract HistoryToken parseNext(final String component,
                                    final TextCursor cursor);

    // ACTION...........................................................................................................

    /**
     * Removes an action.
     * <br>
     * cell/menu -> cell
     * cell -> cell
     */
    public abstract HistoryToken clearAction();

    // ANCHORED SELECTION...............................................................................................

    /**
     * Factory that creates a {@link HistoryToken} changing the {@link AnchoredSpreadsheetSelection} ui and clearing any action.
     */
    public final HistoryToken setAnchoredSelection(final Optional<AnchoredSpreadsheetSelection> anchoredSelection) {
        Objects.requireNonNull(anchoredSelection, "anchoredSelection");

        HistoryToken historyToken = null;

        if(false == this.anchoredSelectionOrEmpty().equals(anchoredSelection)) {
            if (this instanceof SpreadsheetNameHistoryToken) {
                if (anchoredSelection.isPresent()) {
                    final SpreadsheetNameHistoryToken spreadsheetNameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);
                    historyToken = HistoryTokenSelectionSpreadsheetSelectionVisitor.selectionToken(
                        spreadsheetNameHistoryToken,
                        anchoredSelection.get()
                    );
                } else {
                    final SpreadsheetNameHistoryToken spreadsheetNameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);
                    historyToken = spreadsheetSelect(
                        spreadsheetNameHistoryToken.id(),
                        spreadsheetNameHistoryToken.name()
                    );
                }
            }
        }

        return this.elseIfDifferent(historyToken);
    }

    /**
     * Returns a {@link SpreadsheetAnchoredSelectionHistoryToken} using the id, name and {@link AnchoredSpreadsheetSelection}.
     */
    public final Optional<HistoryToken> anchoredSelectionHistoryTokenOrEmpty() {
        HistoryToken result = null;

        if (this instanceof SpreadsheetAnchoredSelectionHistoryToken) {
            final SpreadsheetAnchoredSelectionHistoryToken SpreadsheetAnchoredSelectionHistoryToken = this.cast(SpreadsheetAnchoredSelectionHistoryToken.class);

            result = HistoryToken.selection(
                SpreadsheetAnchoredSelectionHistoryToken.id(),
                SpreadsheetAnchoredSelectionHistoryToken.name(),
                SpreadsheetAnchoredSelectionHistoryToken.anchoredSelection()
            );

            if (this.equals(result)) {
                result = this;
            }
        }

        return Optional.ofNullable(result);
    }

    /**
     * Maybe used to get the {@link SpreadsheetViewport} from any {@link HistoryToken}
     */
    public final Optional<AnchoredSpreadsheetSelection> anchoredSelectionOrEmpty() {
        AnchoredSpreadsheetSelection anchoredSelection = null;

        if (this instanceof SpreadsheetAnchoredSelectionHistoryToken) {
            anchoredSelection = this.cast(SpreadsheetAnchoredSelectionHistoryToken.class)
                .anchoredSelection();
        }

        return Optional.ofNullable(anchoredSelection);
    }

    // COPY.............................................................................................................

    /**
     * If possible creates a {@link SpreadsheetCellClipboardCopyHistoryToken} token.
     */
    public final HistoryToken copy(final SpreadsheetCellClipboardKind kind) {
        Objects.requireNonNull(kind, "kind");

        HistoryToken token = this;

        if (this instanceof SpreadsheetCellHistoryToken) {
            final SpreadsheetCellHistoryToken cell = this.cast(SpreadsheetCellHistoryToken.class);
            token = HistoryToken.cellClipboardCopy(
                cell.id(),
                cell.name(),
                cell.anchoredSelection(),
                kind
            );
        }

        return token;
    }

    // CUT..............................................................................................................

    /**
     * If possible creates a {@link SpreadsheetCellClipboardCutHistoryToken} token.
     */
    public final HistoryToken cut(final SpreadsheetCellClipboardKind kind) {
        Objects.requireNonNull(kind, "kind");

        HistoryToken token = this;

        if (this instanceof SpreadsheetCellHistoryToken) {
            final SpreadsheetCellHistoryToken cell = this.cast(SpreadsheetCellHistoryToken.class);
            token = HistoryToken.cellClipboardCut(
                cell.id(),
                cell.name(),
                cell.anchoredSelection(),
                kind
            );
        }

        return token;
    }
    
    // clear............................................................................................................

    /**
     * if possible creates a clear.
     */
    public final HistoryToken clear() {
        HistoryToken historyToken = this;

        if (this instanceof SpreadsheetColumnHistoryToken) {
            final SpreadsheetColumnHistoryToken column = this.cast(SpreadsheetColumnHistoryToken.class);
            historyToken = columnClear(
                column.id(),
                column.name(),
                column.anchoredSelection()
            );
        } else {
            if (this instanceof SpreadsheetRowHistoryToken) {
                final SpreadsheetRowHistoryToken row = this.cast(SpreadsheetRowHistoryToken.class);
                historyToken = rowClear(
                    row.id(),
                    row.name(),
                    row.anchoredSelection()
                );
            }
        }

        return historyToken;
    }

    // CLOSE............................................................................................................

    /**
     * Used to close a currently active state, such as a dialog.
     */
    public final HistoryToken close() {
        HistoryToken closed = this;

        if (this instanceof PluginHistoryToken) {
            if (this instanceof PluginListSelectHistoryToken) {
                closed = this.clearAction();
            }

            if (this instanceof PluginSelectHistoryToken) {
                closed = this.clearAction();
            }

            // must come after PluginSelectHistoryToken
            if (this instanceof PluginFileViewHistoryToken) {
                closed = this.clearAction();
            }

            if (this instanceof PluginUploadSelectHistoryToken) {
                closed = this.clearAction();
            }

            if (this instanceof PluginUploadSaveHistoryToken) {
                closed = this.clearAction();
            }
        } else {
            if (this instanceof SpreadsheetNameHistoryToken) {
                final SpreadsheetNameHistoryToken spreadsheetNameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);
                final SpreadsheetId id = spreadsheetNameHistoryToken.id();
                final SpreadsheetName name = spreadsheetNameHistoryToken.name();

                if (this instanceof SpreadsheetColumnInsertHistoryToken || this instanceof SpreadsheetRowInsertHistoryToken) {
                    closed = this.clearAction();
                }

                if (this instanceof SpreadsheetFormHistoryToken) {
                    closed = spreadsheetSelect(
                        id,
                        name
                    );
                }

                if (this instanceof SpreadsheetLabelMappingHistoryToken) {
                    closed = spreadsheetSelect(
                        id,
                        name
                    );
                }

                if (this instanceof SpreadsheetNavigateHistoryToken) {
                    closed = spreadsheetSelect(
                        id,
                        name
                    );
                }

                if (this instanceof SpreadsheetAnchoredSelectionHistoryToken) {
                    if (this instanceof SpreadsheetCellDateTimeSymbolsHistoryToken ||
                        this instanceof SpreadsheetCellDecimalNumberSymbolsHistoryToken ||
                        this instanceof SpreadsheetCellFindHistoryToken ||
                        this instanceof SpreadsheetCellFormHistoryToken ||
                        this instanceof SpreadsheetCellFormatterHistoryToken ||
                        this instanceof SpreadsheetCellLabelHistoryToken ||
                        this instanceof SpreadsheetCellLabelListHistoryToken ||
                        this instanceof SpreadsheetCellLocaleHistoryToken ||
                        this instanceof SpreadsheetCellNavigateHistoryToken ||
                        this instanceof SpreadsheetColumnNavigateHistoryToken ||
                        this instanceof SpreadsheetRowNavigateHistoryToken ||
                        this instanceof SpreadsheetCellParserHistoryToken ||
                        this instanceof SpreadsheetCellReferenceListHistoryToken ||
                        this instanceof SpreadsheetCellSortHistoryToken ||
                        this instanceof SpreadsheetColumnSortHistoryToken ||
                        this instanceof SpreadsheetRowSortHistoryToken ||
                        this instanceof SpreadsheetCellValidatorHistoryToken ||
                        this instanceof SpreadsheetCellValueHistoryToken ||
                        this instanceof SpreadsheetCellValueTypeHistoryToken) {
                        closed = this.cast(SpreadsheetAnchoredSelectionHistoryToken.class)
                            .selectionSelect();
                    }
                } else {
                    if (this instanceof SpreadsheetMetadataPropertyHistoryToken) {
                        closed = metadataSelect(
                            id,
                            name
                        );
                    }

                    if (this instanceof SpreadsheetRenameHistoryToken) {
                        closed = spreadsheetSelect(
                            id,
                            name
                        );
                    }
                }

            } else {
                if (this instanceof SpreadsheetListRenameHistoryToken) {
                    closed = spreadsheetListSelect(HistoryTokenOffsetAndCount.EMPTY);
                }
            }
        }

        return closed;
    }

    // COUNT............................................................................................................

    /**
     * Returns the count property or {@link OptionalInt#empty()} if no count is present
     */
    public final OptionalInt count() {
        final OptionalInt count;

        if (this instanceof SpreadsheetColumnInsertHistoryToken) {
            count = this.cast(SpreadsheetColumnInsertHistoryToken.class)
                .count;
        } else {
            if (this instanceof SpreadsheetRowInsertHistoryToken) {
                count = this.cast(SpreadsheetRowInsertHistoryToken.class)
                    .count;
            } else {
                count = this.offsetAndCount()
                    .count();
            }
        }

        return count;
    }

    /**
     * Would be setter that tries to replace the {@link #count()} with new value.
     */
    public final HistoryToken setCount(final OptionalInt count) {
        Objects.requireNonNull(count, "count");

        HistoryToken historyToken = this;

        if (this.count().equals(count)) {
            historyToken = this;
        } else {
            if (this instanceof PluginHistoryToken) {
                if (this instanceof PluginListReloadHistoryToken) {
                    historyToken = pluginListReload(
                        this.offsetAndCount()
                            .setCount(count)
                    );
                }
                if (this instanceof PluginListSelectHistoryToken) {
                    historyToken = pluginListSelect(
                        this.offsetAndCount()
                            .setCount(count)
                    );
                }
            }

            if (this instanceof SpreadsheetListHistoryToken) {
                if (this instanceof SpreadsheetListReloadHistoryToken) {
                    historyToken = spreadsheetListReload(
                        this.offsetAndCount()
                            .setCount(count)
                    );
                }
                if (this instanceof SpreadsheetListSelectHistoryToken) {
                    historyToken = spreadsheetListSelect(
                        this.offsetAndCount()
                            .setCount(count)
                    );
                }
            }

            if (this instanceof SpreadsheetNameHistoryToken) {
                final SpreadsheetNameHistoryToken spreadsheetNameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);
                final SpreadsheetId id = spreadsheetNameHistoryToken.id();
                final SpreadsheetName name = spreadsheetNameHistoryToken.name();

                if (this instanceof SpreadsheetFormListHistoryToken) {
                    final HistoryTokenOffsetAndCount offsetAndCount = this.offsetAndCount();

                    historyToken = formList(
                        id,
                        name,
                        offsetAndCount.setCount(count)
                    );
                }

                if (this instanceof SpreadsheetAnchoredSelectionHistoryToken) {
                    final AnchoredSpreadsheetSelection anchored = this.cast(SpreadsheetAnchoredSelectionHistoryToken.class)
                        .anchoredSelection();

                    if (this instanceof SpreadsheetCellLabelListHistoryToken) {
                        historyToken = cellLabels(
                            id,
                            name,
                            anchored,
                            this.offsetAndCount()
                                .setCount(count)
                        );
                    }

                    if (this instanceof SpreadsheetCellReferenceListHistoryToken) {
                        historyToken = cellReferences(
                            id,
                            name,
                            anchored,
                            this.offsetAndCount()
                                .setCount(count)
                        );
                    }

                    if (this instanceof SpreadsheetColumnInsertAfterHistoryToken) {
                        historyToken = columnInsertAfter(
                            id,
                            name,
                            anchored,
                            count
                        );
                    }

                    if (this instanceof SpreadsheetColumnInsertBeforeHistoryToken) {
                        historyToken = columnInsertBefore(
                            id,
                            name,
                            anchored,
                            count
                        );
                    }

                    if (this instanceof SpreadsheetRowInsertAfterHistoryToken) {
                        historyToken = rowInsertAfter(
                            id,
                            name,
                            anchored,
                            count
                        );
                    }

                    if (this instanceof SpreadsheetRowInsertBeforeHistoryToken) {
                        historyToken = rowInsertBefore(
                            id,
                            name,
                            anchored,
                            count
                        );
                    }
                }

                if (this instanceof SpreadsheetLabelMappingHistoryToken) {
                    if (this instanceof SpreadsheetLabelMappingListHistoryToken) {
                        historyToken = labelMappingList(
                            id,
                            name,
                            this.offsetAndCount()
                                .setCount(count)
                        );
                    }
                }
            }
        }

        return historyToken;
    }

    static OptionalInt parseCount(final TextCursor cursor) {
        return parseOptionalInt(cursor);
    }

    static OptionalInt checkCount(final OptionalInt count) {
        Objects.requireNonNull(count, "count");

        count.ifPresent(value -> {
            if (value <= 0) {
                throw new IllegalArgumentException("Invalid count " + value + " < 0");
            }
        });

        return count;
    }

    // DELETE...........................................................................................................

    /**
     * if possible creates a delete {@link HistoryToken}.
     */
    public final HistoryToken delete() {
        HistoryToken historyToken = this;

        if (this instanceof PluginNameHistoryToken) {
            historyToken = pluginDelete(
                this.cast(PluginNameHistoryToken.class).name()
            );
        } else {
            if (this instanceof SpreadsheetNameHistoryToken) {
                final SpreadsheetNameHistoryToken spreadsheetNameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);
                final SpreadsheetId id = spreadsheetNameHistoryToken.id();
                final SpreadsheetName name = spreadsheetNameHistoryToken.name();

                if (this instanceof SpreadsheetFormHistoryToken) {
                    if (this instanceof SpreadsheetFormSelectHistoryToken) {
                        historyToken = formDelete(
                            id,
                            name,
                            this.cast(SpreadsheetFormSelectHistoryToken.class)
                                .formName
                        );
                    }
                }
                if (this instanceof SpreadsheetSelectionHistoryToken) {

                    if (this instanceof SpreadsheetAnchoredSelectionHistoryToken) {
                        if (this instanceof SpreadsheetCellDateTimeSymbolsHistoryToken ||
                            this instanceof SpreadsheetCellDecimalNumberSymbolsHistoryToken ||
                            this instanceof SpreadsheetCellFormHistoryToken ||
                            this instanceof SpreadsheetCellFormatterHistoryToken ||
                            this instanceof SpreadsheetCellLocaleHistoryToken ||
                            this instanceof SpreadsheetCellParserHistoryToken ||
                            this instanceof SpreadsheetCellValidatorHistoryToken ||
                            this instanceof SpreadsheetCellValueHistoryToken ||
                            this instanceof SpreadsheetCellValueTypeHistoryToken) {
                            historyToken = this.clearAction();
                        } else {
                            final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection = this.cast(SpreadsheetAnchoredSelectionHistoryToken.class)
                                .anchoredSelection();

                            if (this instanceof SpreadsheetCellHistoryToken && false == this instanceof SpreadsheetCellLabelHistoryToken) {
                                historyToken = cellDelete(
                                    id,
                                    name,
                                    anchoredSpreadsheetSelection
                                );
                            } else {
                                if (this instanceof SpreadsheetColumnHistoryToken) {
                                    historyToken = columnDelete(
                                        id,
                                        name,
                                        anchoredSpreadsheetSelection
                                    );
                                } else {
                                    if (this instanceof SpreadsheetRowHistoryToken) {
                                        historyToken = rowDelete(
                                            id,
                                            name,
                                            anchoredSpreadsheetSelection
                                        );
                                    }
                                }
                            }
                        }
                    } else {
                        if (this instanceof SpreadsheetLabelMappingHistoryToken) {
                            final Optional<SpreadsheetLabelName> labelName = this.cast(SpreadsheetLabelMappingHistoryToken.class)
                                .labelName();

                            if (labelName.isPresent()) {
                                historyToken = labelMappingDelete(
                                    id,
                                    name,
                                    labelName.get()
                                );
                            }
                        }
                    }
                }
            }
        }

        return historyToken;
    }

    private static HistoryToken parseDelete(final TextCursor cursor) {
        HistoryToken historyToken = SPREADSHEET_LIST_SELECT_HISTORY_TOKEN;

        final Optional<String> maybeComponent = parseComponent(cursor);
        if (maybeComponent.isPresent()) {
            historyToken = HistoryToken.spreadsheetListDelete(
                SpreadsheetId.parse(maybeComponent.get())
            );
        }

        return historyToken;
    }

    // DateTimeSymbols..................................................................................................

    /**
     * If possible selects a {@link DateTimeSymbols} {@link HistoryToken}.
     */
    public final HistoryToken dateTimeSymbols() {
        HistoryToken historyToken = this;

        if (this instanceof SpreadsheetCellSelectHistoryToken || this instanceof SpreadsheetCellMenuHistoryToken) {
            final SpreadsheetCellHistoryToken cell = this.cast(SpreadsheetCellHistoryToken.class);

            historyToken = HistoryToken.cellDateTimeSymbolsSelect(
                cell.id(),
                cell.name(),
                cell.anchoredSelection()
            );
        }

        return historyToken;
    }

    // decimalNumberSymbols.............................................................................................

    /**
     * If possible selects a {@link DecimalNumberSymbols} {@link HistoryToken}.
     */
    public final HistoryToken decimalNumberSymbols() {
        HistoryToken historyToken = this;

        if (this instanceof SpreadsheetCellSelectHistoryToken || this instanceof SpreadsheetCellMenuHistoryToken) {
            final SpreadsheetCellHistoryToken cell = this.cast(SpreadsheetCellHistoryToken.class);

            historyToken = HistoryToken.cellDecimalNumberSymbolsSelect(
                cell.id(),
                cell.name(),
                cell.anchoredSelection()
            );
        }

        return historyToken;
    }

    // FORMATTER........................................................................................................

    /**
     * If possible selects a formatter {@link HistoryToken}.
     */
    public final HistoryToken formatter() {
        HistoryToken historyToken = this;

        if (this instanceof SpreadsheetCellSelectHistoryToken || this instanceof SpreadsheetCellMenuHistoryToken) {
            final SpreadsheetCellHistoryToken cell = this.cast(SpreadsheetCellHistoryToken.class);

            historyToken = HistoryToken.cellFormatterSelect(
                cell.id(),
                cell.name(),
                cell.anchoredSelection()
            );
        }

        return historyToken;
    }

    // FORMULA..........................................................................................................

    public final HistoryToken formula() {
        HistoryToken historyToken = this;

        if (this instanceof SpreadsheetCellHistoryToken) {
            final SpreadsheetCellHistoryToken cell = this.cast(SpreadsheetCellHistoryToken.class);

            historyToken = cellFormula(
                cell.id(),
                cell.name(),
                cell.anchoredSelection()
            );
        }

        return historyToken;
    }

    // FREEZE...........................................................................................................

    /**
     * if possible creates a freeze.
     */
    public final HistoryToken freeze() {
        HistoryToken historyToken = this;

        if (this instanceof SpreadsheetAnchoredSelectionHistoryToken) {
            final SpreadsheetAnchoredSelectionHistoryToken anchored = this.cast(SpreadsheetAnchoredSelectionHistoryToken.class);
            final SpreadsheetId id = anchored.id();
            final SpreadsheetName name = anchored.name();
            final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection = anchored.anchoredSelection();

            if (this instanceof SpreadsheetCellHistoryToken) {
                historyToken = cellFreeze(
                    id,
                    name,
                    anchoredSpreadsheetSelection
                );
            } else {
                if (this instanceof SpreadsheetColumnHistoryToken) {
                    historyToken = columnFreeze(
                        id,
                        name,
                        anchoredSpreadsheetSelection
                    );
                } else {
                    if (this instanceof SpreadsheetRowHistoryToken) {
                        historyToken = rowFreeze(
                            id,
                            name,
                            anchoredSpreadsheetSelection
                        );
                    }
                }
            }
        }

        return historyToken;
    }

    /**
     * Tries to create a freeze token or {@link Optional#empty()} because the {@link SpreadsheetSelection} is invalid.
     */
    public final Optional<HistoryToken> freezeOrEmpty() {
        HistoryToken token;

        try {
            token = this.freeze();
        } catch (final RuntimeException ignored) {
            token = null;
        }

        return Optional.ofNullable(token);
    }

    // ID...............................................................................................................

    /**
     * Would be setter, returning a {@link HistoryToken} with the given {@link SpreadsheetId} and {@link SpreadsheetName},
     * creating a new instance if necessary.
     */
    public final HistoryToken setIdAndName(final SpreadsheetId id,
                                           final SpreadsheetName name) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");

        HistoryToken token = null;

        if (this instanceof SpreadsheetNameHistoryToken) {
            final SpreadsheetNameHistoryToken spreadsheetNameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);
            if (id.equals(spreadsheetNameHistoryToken.id()) && name.equals(spreadsheetNameHistoryToken.name())) {
                token = this;
            }
        }

        if (null == token) {
            token = this.replaceIdAndName(
                id,
                name
            );
        }

        return token;
    }

    /**
     * Accepts a id and name, attempting to replace the name if the id is unchanged or when different replaces the
     * entire history token.
     */
    abstract HistoryToken replaceIdAndName(final SpreadsheetId id,
                                           final SpreadsheetName name);

    // INSERT...........................................................................................................

    /**
     * if possible creates an insert after {@link HistoryToken}.
     */
    public final HistoryToken insertAfter(final OptionalInt count) {
        checkCount(count);

        HistoryToken historyToken = this;

        if (historyToken instanceof SpreadsheetAnchoredSelectionHistoryToken) {
            final SpreadsheetAnchoredSelectionHistoryToken spreadsheetAnchoredSelectionHistoryToken = this.cast(SpreadsheetAnchoredSelectionHistoryToken.class);
            final SpreadsheetId id = spreadsheetAnchoredSelectionHistoryToken.id();
            final SpreadsheetName name = spreadsheetAnchoredSelectionHistoryToken.name();
            final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection = spreadsheetAnchoredSelectionHistoryToken.anchoredSelection();

            if (historyToken instanceof SpreadsheetColumnHistoryToken) {
                historyToken = columnInsertAfter(
                    id,
                    name,
                    anchoredSpreadsheetSelection,
                    count
                );
            } else {
                if (historyToken instanceof SpreadsheetRowHistoryToken) {
                    historyToken = rowInsertAfter(
                        id,
                        name,
                        anchoredSpreadsheetSelection,
                        count
                    );
                }
            }

        }

        return historyToken;
    }

    /**
     * if possible creates a insert before {@link HistoryToken}.
     */
    public final HistoryToken insertBefore(final OptionalInt count) {
        checkCount(count);

        HistoryToken historyToken = this;

        if (historyToken instanceof SpreadsheetAnchoredSelectionHistoryToken) {
            final SpreadsheetAnchoredSelectionHistoryToken spreadsheetAnchoredSelectionHistoryToken = this.cast(SpreadsheetAnchoredSelectionHistoryToken.class);
            final SpreadsheetId id = spreadsheetAnchoredSelectionHistoryToken.id();
            final SpreadsheetName name = spreadsheetAnchoredSelectionHistoryToken.name();
            final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection = spreadsheetAnchoredSelectionHistoryToken.anchoredSelection();

            if (historyToken instanceof SpreadsheetColumnHistoryToken) {
                historyToken = columnInsertBefore(
                    id,
                    name,
                    anchoredSpreadsheetSelection,
                    count
                );
            } else {
                if (historyToken instanceof SpreadsheetRowHistoryToken) {
                    historyToken = rowInsertBefore(
                        id,
                        name,
                        anchoredSpreadsheetSelection,
                        count
                    );
                }
            }

        }

        return historyToken;
    }

    // LABEL............................................................................................................

    public final HistoryToken createLabel() {
        HistoryToken historyToken = this;

        if (this instanceof SpreadsheetNameHistoryToken) {
            final SpreadsheetNameHistoryToken spreadsheetNameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);
            final SpreadsheetId id = spreadsheetNameHistoryToken.id();
            final SpreadsheetName name = spreadsheetNameHistoryToken.name();

            if (this instanceof SpreadsheetCellHistoryToken) {
                if (false == this instanceof SpreadsheetCellLabelSelectHistoryToken) {
                    historyToken = cellLabelSelect(
                        id,
                        name,
                        this.cast(SpreadsheetCellHistoryToken.class)
                            .anchoredSelection()
                    );
                }
            } else {
                historyToken = labelMappingCreate(
                    id,
                    name
                );
            }
        }

        return historyToken;
    }

    // LABEL MAPPING....................................................................................................

    public final HistoryToken labelMapping() {
        HistoryToken historyToken = this;

        if (this instanceof SpreadsheetCellHistoryToken) {
            final SpreadsheetCellHistoryToken cell = this.cast(SpreadsheetCellHistoryToken.class);

            historyToken = cellLabelSelect(
                cell.id(),
                cell.name(),
                cell.anchoredSelection()
            );
        }

        return historyToken;
    }

    /**
     * Getter that returns a {@link SpreadsheetLabelMapping#reference()} if this token has one.
     */
    public final Optional<SpreadsheetExpressionReference> labelMappingReference() {
        SpreadsheetExpressionReference target = null;

        if (this instanceof SpreadsheetCellLabelHistoryToken) {
            target = this.cast(SpreadsheetCellLabelHistoryToken.class)
                .anchoredSelection()
                .selection()
                .toExpressionReference();
        } else {
            if (this instanceof SpreadsheetLabelMappingSaveHistoryToken) {
                target = this.cast(SpreadsheetLabelMappingSaveHistoryToken.class)
                    .mapping
                    .reference();
            }
        }

        return Optional.ofNullable(target);
    }

    /**
     * Would be setter that only sets/replaces/adds the given {@link SpreadsheetExpressionReference} reference if the
     * history token holds a label mapping.
     */
    public final HistoryToken setLabelMappingReference(final Optional<SpreadsheetExpressionReference> labelMappingReference) {
        Objects.requireNonNull(labelMappingReference, "labelMappingReference");

        HistoryToken afterSet = null;

        if (this instanceof SpreadsheetCellLabelHistoryToken) {
            if (labelMappingReference.isPresent()) {
                final SpreadsheetExpressionReference reference = labelMappingReference.get();

                final SpreadsheetCellLabelHistoryToken cellLabelHistoryToken = this.cast(SpreadsheetCellLabelHistoryToken.class);
                final SpreadsheetId id = cellLabelHistoryToken.id();
                final SpreadsheetName name = cellLabelHistoryToken.name();

                final AnchoredSpreadsheetSelection anchoredSelection = cellLabelHistoryToken.anchoredSelection();
                if (false == reference.equals(anchoredSelection.selection())) {
                    if (this instanceof SpreadsheetCellLabelSelectHistoryToken) {
                        afterSet = HistoryToken.cellLabelSelect(
                            id,
                            name,
                            reference.setDefaultAnchor()
                        );
                    } else {
                        if (this instanceof SpreadsheetCellLabelSaveHistoryToken) {
                            afterSet = HistoryToken.cellLabelSave(
                                id,
                                name,
                                reference.setDefaultAnchor(),
                                this.cast(SpreadsheetCellLabelSaveHistoryToken.class).labelName
                            );
                        }
                    }
                }
            }
        } else {
            if (this instanceof SpreadsheetLabelMappingHistoryToken) {
                afterSet = this.setSaveValue(labelMappingReference);
            }
        }

        return this.elseIfDifferent(afterSet);
    }

    // LABEL NAME.......................................................................................................

    /**
     * Getter that returns any {@link SpreadsheetLabelName} value from the given {@link HistoryToken}.
     */
    public final Optional<SpreadsheetLabelName> labelName() {
        SpreadsheetLabelName labelName = null;

        if (this instanceof SpreadsheetCellLabelSaveHistoryToken) {
            labelName = this.cast(SpreadsheetCellLabelSaveHistoryToken.class).labelName;
        } else {
            if (this instanceof SpreadsheetLabelMappingDeleteHistoryToken) {
                labelName = this.cast(SpreadsheetLabelMappingDeleteHistoryToken.class).labelName;
            } else {
                if (this instanceof SpreadsheetLabelMappingSaveHistoryToken) {
                    labelName = this.cast(SpreadsheetLabelMappingSaveHistoryToken.class).value();
                } else {
                    if (this instanceof SpreadsheetLabelMappingSelectHistoryToken) {
                        labelName = this.cast(SpreadsheetLabelMappingSelectHistoryToken.class).labelName;
                    }
                }
            }
        }

        return Optional.ofNullable(labelName);
    }

    /**
     * Sets or replaces the current {@link SpreadsheetLabelName} otherwise returns this.
     */
    public final HistoryToken setLabelName(final Optional<SpreadsheetLabelName> label) {
        Objects.requireNonNull(label, "label");

        HistoryToken historyToken = this;

        if (this instanceof SpreadsheetNameHistoryToken) {
            final SpreadsheetNameHistoryToken spreadsheetNameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);
            final SpreadsheetId id = spreadsheetNameHistoryToken.id();
            final SpreadsheetName name = spreadsheetNameHistoryToken.name();

            if (this instanceof SpreadsheetCellHistoryToken) {
                final AnchoredSpreadsheetSelection selection = historyToken.cast(SpreadsheetCellHistoryToken.class)
                    .anchoredSelection();

                if (this instanceof SpreadsheetCellLabelHistoryToken) {
                    final SpreadsheetCellLabelHistoryToken cellLabelHistoryToken = this.cast(SpreadsheetCellLabelHistoryToken.class);

                    // if equal return this,
                    // present -> cellLabelSave
                    // missing -> cellLabelSelect
                    historyToken = cellLabelHistoryToken.labelName()
                        .equals(label) ?
                        this :
                        label.isPresent() ?
                            HistoryToken.cellLabelSave(
                                id,
                                name,
                                selection,
                                label.get()
                            ) :
                            HistoryToken.cellLabelSelect(
                                id,
                                name,
                                selection
                            );
                } else {
                    historyToken = label.isPresent() ?
                        HistoryToken.labelMappingSelect(
                            id,
                            name,
                            label.get()
                        ) :
                        HistoryToken.cellLabelSelect(
                            id,
                            name,
                            selection
                        );
                }

            } else {
                historyToken = label.isPresent() ?
                    HistoryToken.labelMappingSelect(
                        id,
                        name,
                        label.get()
                    ) :
                    HistoryToken.labelMappingCreate(
                        id,
                        name
                    );
            }
        }

        return historyToken;
    }

    // LABELS...........................................................................................................

    public final HistoryToken setLabels(final HistoryTokenOffsetAndCount offsetAndCount) {
        Objects.requireNonNull(offsetAndCount, "offsetAndCount");

        HistoryToken historyToken = null;

        if (this instanceof SpreadsheetNameHistoryToken) {
            final SpreadsheetNameHistoryToken spreadsheetNameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);
            final SpreadsheetId id = spreadsheetNameHistoryToken.id();
            final SpreadsheetName name = spreadsheetNameHistoryToken.name();

            if (this instanceof SpreadsheetCellHistoryToken) {
                historyToken = cellLabels(
                    id,
                    name,
                    this.cast(SpreadsheetCellHistoryToken.class)
                        .anchoredSelection(),
                    offsetAndCount
                );
            } else {
                historyToken = labelMappingList(
                    id,
                    name,
                    offsetAndCount
                );
            }
        }

        return this.elseIfDifferent(historyToken);
    }

    // LIST.............................................................................................................

    public final HistoryToken list(final HistoryTokenOffsetAndCount offsetAndCount) {
        Objects.requireNonNull(offsetAndCount, "offsetAndCount");

        HistoryToken historyToken;

        if (this instanceof PluginHistoryToken) {
            historyToken = pluginListSelect(offsetAndCount);
        } else {
            if (this instanceof SpreadsheetNameHistoryToken) {
                final SpreadsheetNameHistoryToken spreadsheetNameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);
                final SpreadsheetId id = spreadsheetNameHistoryToken.id();
                final SpreadsheetName name = spreadsheetNameHistoryToken.name();

                if (this instanceof SpreadsheetLabelMappingHistoryToken) {
                    historyToken = labelMappingList(
                        id,
                        name,
                        offsetAndCount
                    );
                } else {
                    historyToken = spreadsheetListSelect(
                        offsetAndCount
                    );
                }

            } else {
                if (this instanceof SpreadsheetListHistoryToken) {
                    historyToken = this.setOffset(offsetAndCount.offset)
                        .setCount(offsetAndCount.count);
                } else {
                    historyToken = spreadsheetListSelect(
                        offsetAndCount
                    );
                }
            }
        }

        return this.elseIfDifferent(historyToken);
    }

    // locale...........................................................................................................

    /**
     * If possible selects a {@link Locale} {@link HistoryToken}.
     */
    public final HistoryToken locale() {
        HistoryToken historyToken = this;

        if (this instanceof SpreadsheetCellSelectHistoryToken || this instanceof SpreadsheetCellMenuHistoryToken) {
            final SpreadsheetCellHistoryToken cell = this.cast(SpreadsheetCellHistoryToken.class);

            historyToken = HistoryToken.cellLocaleSelect(
                cell.id(),
                cell.name(),
                cell.anchoredSelection()
            );
        }

        return historyToken;
    }

    // MENU.............................................................................................................

    public final HistoryToken menu() {
        HistoryToken historyToken = this;

        if (this instanceof SpreadsheetAnchoredSelectionHistoryToken) {
            final SpreadsheetAnchoredSelectionHistoryToken spreadsheetAnchoredSelectionHistoryToken = this.cast(SpreadsheetAnchoredSelectionHistoryToken.class);

            final SpreadsheetId id = spreadsheetAnchoredSelectionHistoryToken.id();
            final SpreadsheetName name = spreadsheetAnchoredSelectionHistoryToken.name();
            final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection = spreadsheetAnchoredSelectionHistoryToken.anchoredSelection();

            if (this instanceof SpreadsheetCellHistoryToken) {
                if (this instanceof SpreadsheetCellFormulaHistoryToken) {
                    historyToken = cellFormulaMenu(
                        id,
                        name,
                        anchoredSpreadsheetSelection
                    );
                } else {
                    historyToken = cellMenu(
                        id,
                        name,
                        anchoredSpreadsheetSelection
                    );
                }
            } else {
                if (this instanceof SpreadsheetColumnHistoryToken) {
                    historyToken = columnMenu(
                        id,
                        name,
                        anchoredSpreadsheetSelection
                    );
                } else {
                    if (this instanceof SpreadsheetRowHistoryToken) {
                        historyToken = rowMenu(
                            id,
                            name,
                            anchoredSpreadsheetSelection
                        );
                    }
                }
            }
        }

        return historyToken;
    }

    /**
     * Creates a {@link HistoryToken} with the given {@link SpreadsheetSelection}.
     * If the given selection is outside the selection for this {@link HistoryToken}, then replace the selection otherwise
     * use the original selection in the new menu history token.
     */
    public final HistoryToken menu(final Optional<SpreadsheetSelection> selection,
                                   final SpreadsheetLabelNameResolver labelNameResolver) {
        Objects.requireNonNull(selection, "selection");
        Objects.requireNonNull(labelNameResolver, "labelNameResolver");

        HistoryToken historyToken = this;

        if (selection.isPresent()) {
            historyToken = this.menu0(
                selection.get(),
                labelNameResolver
            );
        } else {
            if (this instanceof SpreadsheetAnchoredSelectionHistoryToken) {
                historyToken = this.menu();
            }
        }

        return historyToken;
    }

    private HistoryToken menu0(final SpreadsheetSelection selection,
                               final SpreadsheetLabelNameResolver labelNameResolver) {
        HistoryToken historyToken = null;

        if (this instanceof SpreadsheetNameHistoryToken) {
            SpreadsheetNameHistoryToken name = this.cast(SpreadsheetNameHistoryToken.class);

            final Optional<AnchoredSpreadsheetSelection> maybeAnchored = this.anchoredSelectionOrEmpty();
            if (maybeAnchored.isPresent()) {
                // right mouse happened over already selected selection...
                if (labelNameResolver.resolveIfLabelOrFail(maybeAnchored.get().selection())
                    .test(selection)) {
                    historyToken = this.menu();
                }
            }

            // right mouse click happened over a non selected cell/column/row
            if (null == historyToken) {
                historyToken = name.menu(selection);
            }
        }

        if (null == historyToken) {
            historyToken = this;
        }

        return historyToken;
    }

    /**
     * Helper that takes a selection (assuming a context menu/right click) on a {@link HistoryToken}, that may
     * already have a selection and then returns a {@link HistoryToken} with the right new selection.
     */
    // @VisibleForTesting
    final HistoryToken menu(final SpreadsheetSelection selection) {
        Objects.requireNonNull(selection, "selection");
        if (false == selection.isScalar()) {
            throw new IllegalArgumentException("Got " + selection + ", expected cell, column or row");
        }

        final AnchoredSpreadsheetSelection anchoredMenuSelection;
        HistoryToken historyToken = this;

        if (this instanceof SpreadsheetNameHistoryToken) {
            if (this instanceof SpreadsheetAnchoredSelectionHistoryToken) {
                final AnchoredSpreadsheetSelection anchoredSelection = this.cast(SpreadsheetAnchoredSelectionHistoryToken.class)
                    .anchoredSelection();

                if (this instanceof SpreadsheetCellHistoryToken && selection.isCell()) {
                    anchoredMenuSelection = anchoredSelection.selection()
                        .testCell(selection.toCell()) ?
                        anchoredSelection :
                        selection.setDefaultAnchor();
                } else {
                    if (this instanceof SpreadsheetColumnHistoryToken && selection.isColumn()) {
                        anchoredMenuSelection = anchoredSelection.selection()
                            .testColumn(selection.toColumn()) ?
                            anchoredSelection :
                            selection.setDefaultAnchor();
                    } else {
                        if (this instanceof SpreadsheetRowHistoryToken && selection.isRow()) {
                            anchoredMenuSelection = anchoredSelection.selection()
                                .testRow(selection.toRow()) ?
                                anchoredSelection :
                                selection.setDefaultAnchor();
                        } else {
                            anchoredMenuSelection = selection.setDefaultAnchor();
                        }
                    }
                }

            } else {
                anchoredMenuSelection = selection.setDefaultAnchor();
            }

            final SpreadsheetNameHistoryToken nameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);
            final SpreadsheetId id = nameHistoryToken.id();
            final SpreadsheetName name = nameHistoryToken.name();

            final SpreadsheetSelection menuSelection = anchoredMenuSelection.selection();

            if (menuSelection.isCellOrCellRange()) {
                historyToken = cellMenu(
                    id,
                    name,
                    anchoredMenuSelection
                );
            } else {
                if (menuSelection.isColumnOrColumnRange()) {
                    historyToken = columnMenu(
                        id,
                        name,
                        anchoredMenuSelection
                    );

                } else {
                    if (menuSelection.isRowOrRowRange()) {
                        historyToken = rowMenu(
                            id,
                            name,
                            anchoredMenuSelection
                        );
                    }
                }
            }
        }

        return historyToken;
    }

    // METADATA.........................................................................................................

    /**
     * Returns if a possible a {@link HistoryToken} which shows the metadata panel
     */
    public final HistoryToken metadataShow() {
        HistoryToken historyToken = this;

        if (this instanceof SpreadsheetNameHistoryToken) {
            final SpreadsheetNameHistoryToken spreadsheetNameHistoryToken = historyToken.cast(SpreadsheetNameHistoryToken.class);
            historyToken = HistoryToken.metadataSelect(
                spreadsheetNameHistoryToken.id(),
                spreadsheetNameHistoryToken.name()
            );
        }

        return historyToken;
    }

    /**
     * Returns if a possible a {@link HistoryToken} which hides the metadata panel
     */
    public final HistoryToken metadataHide() {
        HistoryToken historyToken = this;

        if (this instanceof SpreadsheetMetadataHistoryToken) {
            final SpreadsheetMetadataHistoryToken spreadsheetMetadataHistoryToken = historyToken.cast(SpreadsheetMetadataHistoryToken.class);
            historyToken = HistoryToken.spreadsheetSelect(
                spreadsheetMetadataHistoryToken.id(),
                spreadsheetMetadataHistoryToken.name()
            );
        }

        return historyToken;
    }

    public final Optional<SpreadsheetMetadataPropertyName<?>> metadataPropertyName() {
        SpreadsheetMetadataPropertyName<?> propertyName = null;

        if(this instanceof SpreadsheetMetadataPropertyHistoryToken) {
            propertyName = this.cast(SpreadsheetMetadataPropertyHistoryToken.class)
                .propertyName;
        }

        return Optional.ofNullable(propertyName);
    }

    /**
     * Setter that tries to set a {@link SpreadsheetMetadataPropertyName} to the current {@link HistoryToken}.
     */
    public final HistoryToken setMetadataPropertyName(final SpreadsheetMetadataPropertyName<?> propertyName) {
        Objects.requireNonNull(propertyName, "propertyName");

        HistoryToken historyToken = this;

        if (this instanceof SpreadsheetMetadataPropertyHistoryToken) {
            final SpreadsheetMetadataPropertyHistoryToken<?> metadataHistoryToken = this.cast(SpreadsheetMetadataPropertyHistoryToken.class);

            historyToken = HistoryToken.metadataPropertySelect(
                    metadataHistoryToken.id(),
                    metadataHistoryToken.name(),
                    propertyName
                );
        } else {

            if (this instanceof SpreadsheetNameHistoryToken) {
                final SpreadsheetNameHistoryToken nameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);

                historyToken = HistoryToken.metadataPropertySelect(
                    nameHistoryToken.id(),
                    nameHistoryToken.name(),
                    propertyName
                );
            }
        }

        return historyToken;
    }

    // name.............................................................................................................

    /**
     * Sets or replaces the current {@link SpreadsheetName}.
     */
    public final HistoryToken setName(final SpreadsheetName name) {
        Objects.requireNonNull(name, "name");

        return this instanceof SpreadsheetNameHistoryToken ?
            this.cast(SpreadsheetNameHistoryToken.class).replaceName(name) :
            this;
    }

    // navigation.......................................................................................................

    /**
     * Getter that returns any present {@link SpreadsheetViewportHomeNavigationList}.
     */
    public final Optional<SpreadsheetViewportHomeNavigationList> navigation() {
        Optional<SpreadsheetViewportHomeNavigationList> navigation;

        if (this instanceof SpreadsheetNavigateHistoryToken) {
            navigation = this.cast(SpreadsheetNavigateHistoryToken.class)
                .navigation;
        } else {
            if (this instanceof SpreadsheetCellNavigateHistoryToken) {
                navigation = this.cast(SpreadsheetCellNavigateHistoryToken.class)
                    .navigation;
            } else {
                if (this instanceof SpreadsheetColumnNavigateHistoryToken) {
                    navigation = this.cast(SpreadsheetColumnNavigateHistoryToken.class)
                        .navigation;
                } else {
                    if (this instanceof SpreadsheetRowNavigateHistoryToken) {
                        navigation = this.cast(SpreadsheetRowNavigateHistoryToken.class)
                            .navigation;
                    } else {
                        navigation = Optional.empty();
                    }
                }
            }
        }

        return navigation;
    }

    public final HistoryToken setNavigation(final Optional<SpreadsheetViewportHomeNavigationList> navigation) {
        Objects.requireNonNull(navigation, "navigation");

        HistoryToken historyToken = this;

        if (this instanceof SpreadsheetNameHistoryToken) {
            final SpreadsheetNameHistoryToken spreadsheetNameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);
            final SpreadsheetId id = spreadsheetNameHistoryToken.id();
            final SpreadsheetName name = spreadsheetNameHistoryToken.name();

            if (this instanceof SpreadsheetAnchoredSelectionHistoryToken) {
                final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection = this.cast(SpreadsheetAnchoredSelectionHistoryToken.class)
                    .anchoredSelection();

                if (this instanceof SpreadsheetCellHistoryToken) {
                    historyToken = HistoryToken.cellNavigate(
                        id,
                        name,
                        anchoredSpreadsheetSelection,
                        navigation
                    );
                }
                if (this instanceof SpreadsheetColumnHistoryToken) {
                    historyToken = HistoryToken.columnNavigate(
                        id,
                        name,
                        anchoredSpreadsheetSelection,
                        navigation
                    );
                }
                if (this instanceof SpreadsheetRowHistoryToken) {
                    historyToken = HistoryToken.rowNavigate(
                        id,
                        name,
                        anchoredSpreadsheetSelection,
                        navigation
                    );
                }
            } else {
                historyToken = HistoryToken.navigate(
                    id,
                    name,
                    navigation
                );
            }
        }

        return historyToken;
    }

    // OFFSET...........................................................................................................

    /**
     * Getter that returns the offset or {@link OptionalInt} if absent.
     */
    public final OptionalInt offset() {
        return this.offsetAndCount()
            .offset();
    }

    /**
     * Would be setter that tries to replace the {@link #offset()} with new value.
     */
    public final HistoryToken setOffset(final OptionalInt offset) {
        Objects.requireNonNull(offset, "offset");

        HistoryToken historyToken = this;

        final HistoryTokenOffsetAndCount offsetAndCount = this.offsetAndCount();
        if (false == offsetAndCount.offset()
            .equals(offset)) {

            if (this instanceof PluginHistoryToken) {
                if (this instanceof PluginListReloadHistoryToken) {
                    historyToken = pluginListReload(
                        offsetAndCount.setOffset(offset)
                    );
                }
                if (this instanceof PluginListSelectHistoryToken) {
                    historyToken = pluginListSelect(
                        offsetAndCount.setOffset(offset)
                    );
                }
            }

            if (this instanceof SpreadsheetListHistoryToken) {
                if (this instanceof SpreadsheetListReloadHistoryToken) {
                    historyToken = spreadsheetListReload(
                        offsetAndCount.setOffset(offset)
                    );
                }
                if (this instanceof SpreadsheetListSelectHistoryToken) {
                    historyToken = spreadsheetListSelect(
                        offsetAndCount.setOffset(offset)
                    );
                }
            }

            if (this instanceof SpreadsheetNameHistoryToken) {
                final SpreadsheetNameHistoryToken spreadsheetNameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);

                final SpreadsheetId id = spreadsheetNameHistoryToken.id();
                final SpreadsheetName name = spreadsheetNameHistoryToken.name();

                if (this instanceof SpreadsheetFormListHistoryToken) {
                    historyToken = formList(
                        id,
                        name,
                        offsetAndCount.setOffset(offset)
                    );
                }

                if (this instanceof SpreadsheetAnchoredSelectionHistoryToken) {
                    final AnchoredSpreadsheetSelection anchored = this.cast(SpreadsheetAnchoredSelectionHistoryToken.class)
                        .anchoredSelection();

                    if (this instanceof SpreadsheetCellLabelListHistoryToken) {
                        historyToken = cellLabels(
                            id,
                            name,
                            anchored,
                            offsetAndCount.setOffset(offset)
                        );
                    }
                    if (this instanceof SpreadsheetCellReferenceListHistoryToken) {
                        historyToken = cellReferences(
                            id,
                            name,
                            anchored,
                            offsetAndCount.setOffset(offset)
                        );
                    }
                }

                if (this instanceof SpreadsheetLabelMappingHistoryToken) {
                    if (this instanceof SpreadsheetLabelMappingListHistoryToken) {
                        historyToken = labelMappingList(
                            id,
                            name,
                            offsetAndCount.setOffset(offset)
                        );
                    }
                }
            }
        }

        return historyToken;
    }

    // OFFSET AND COUNT.................................................................................................

    public final HistoryTokenOffsetAndCount offsetAndCount() {
        final HistoryTokenOffsetAndCount offsetAndCount;

        if (this instanceof PluginListHistoryToken) {
            offsetAndCount = this.cast(PluginListHistoryToken.class).offsetAndCount;
        } else {
            if (this instanceof SpreadsheetFormListHistoryToken) {
                offsetAndCount = this.cast(SpreadsheetFormListHistoryToken.class)
                    .offsetAndCount;
            } else {
                if (this instanceof SpreadsheetListHistoryToken) {
                    offsetAndCount = this.cast(SpreadsheetListHistoryToken.class)
                        .offsetAndCount;
                } else {
                    if (this instanceof SpreadsheetCellLabelListHistoryToken) {
                        offsetAndCount = this.cast(SpreadsheetCellLabelListHistoryToken.class)
                            .offsetAndCount;
                    } else {
                        if (this instanceof SpreadsheetCellReferenceListHistoryToken) {
                            offsetAndCount = this.cast(SpreadsheetCellReferenceListHistoryToken.class)
                                .offsetAndCount;
                        } else {
                            if (this instanceof SpreadsheetLabelMappingListHistoryToken) {
                                offsetAndCount = this.cast(SpreadsheetLabelMappingListHistoryToken.class)
                                    .offsetAndCount;
                            } else {
                                offsetAndCount = HistoryTokenOffsetAndCount.EMPTY;
                            }
                        }
                    }
                }
            }
        }

        return offsetAndCount;
    }

    final HistoryToken parseOffsetAndCount(final TextCursor text) {
        final HistoryTokenOffsetAndCount offsetAndCount = HistoryTokenOffsetAndCount.parse(text);

        return this.setOffset(
            offsetAndCount.offset
        ).setCount(offsetAndCount.count);
    }

    static UrlFragment countAndOffsetUrlFragment(final HistoryTokenOffsetAndCount offsetAndCount,
                                                 final UrlFragment suffix) {
        UrlFragment urlFragment = UrlFragment.EMPTY;

        boolean addStar = true;

        if (offsetAndCount.isNotEmpty()) {
            urlFragment = urlFragment.appendSlashThen(WILDCARD)
                .append(
                    offsetAndCount.urlFragment()
                );
            addStar = false;
        }

        if (false == suffix.isEmpty()) {
            if (addStar) {
                urlFragment = urlFragment.appendSlashThen(WILDCARD);
            }

            urlFragment = urlFragment.appendSlashThen(suffix);
        }

        return urlFragment;
    }

    // PARSER...........................................................................................................

    /**
     * If possible selects a formatter {@link HistoryToken}.
     */
    public final HistoryToken parser() {
        HistoryToken historyToken;

        if (this instanceof SpreadsheetCellSelectHistoryToken) {
            final SpreadsheetCellSelectHistoryToken cell = this.cast(SpreadsheetCellSelectHistoryToken.class);

            historyToken = HistoryToken.cellParserSelect(
                cell.id(),
                cell.name(),
                cell.anchoredSelection()
            );

        } else {
            historyToken = this;
        }

        return historyToken;
    }

    /**
     * If possible creates a {@link SpreadsheetCellClipboardPasteHistoryToken} token.
     */
    public final HistoryToken paste(final SpreadsheetCellClipboardKind kind) {
        Objects.requireNonNull(kind, "kind");

        HistoryToken token = this;

        if (this instanceof SpreadsheetCellHistoryToken) {
            final SpreadsheetCellHistoryToken cell = this.cast(SpreadsheetCellHistoryToken.class);
            token = HistoryToken.cellClipboardPaste(
                cell.id(),
                cell.name(),
                cell.anchoredSelection(),
                kind
            );
        }

        return token;
    }
    
    // pluginName.......................................................................................................

    /**
     * Returns a {@link PluginName} if one is available.
     */
    public Optional<PluginName> pluginName() {
        PluginName pluginName = null;

        if (this instanceof PluginNameHistoryToken) {
            pluginName = this.cast(PluginNameHistoryToken.class)
                .name();
        }

        return Optional.ofNullable(pluginName);
    }

    // QUERY............................................................................................................

    /**
     * Creates a {@link SpreadsheetCellFindHistoryToken} with the given parameters.
     */
    public final HistoryToken setQuery(final SpreadsheetCellFindQuery query) {
        HistoryToken historyToken = this;

        if (this instanceof SpreadsheetCellHistoryToken) {
            if (this instanceof SpreadsheetCellFindHistoryToken) {
                final SpreadsheetCellFindHistoryToken findHistoryToken = (SpreadsheetCellFindHistoryToken) this;
                historyToken = findHistoryToken.setQuery0(query);
            } else {
                final SpreadsheetCellHistoryToken cell = (SpreadsheetCellHistoryToken) this;
                historyToken = cellFind(
                    cell.id(),
                    cell.name(),
                    cell.anchoredSelection(),
                    query
                );
            }

        }

        return historyToken;
    }

    // REFERENCES.......................................................................................................

    final HistoryToken parseReferences(final TextCursor cursor) {
        HistoryTokenOffsetAndCount offsetAndCount;

        try {
            offsetAndCount = HistoryTokenOffsetAndCount.parse(cursor);
        } catch (final IllegalArgumentException cause) {
            offsetAndCount = HistoryTokenOffsetAndCount.EMPTY;
        }

        return this.references(offsetAndCount);
    }

    public final HistoryToken references(final HistoryTokenOffsetAndCount offsetAndCount) {
        Objects.requireNonNull(offsetAndCount, "offsetAndCount");

        HistoryToken historyToken = this;

        if (this instanceof SpreadsheetNameHistoryToken) {
            final SpreadsheetNameHistoryToken nameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);

            final SpreadsheetId id = nameHistoryToken.id();
            final SpreadsheetName name = nameHistoryToken.name();

            if (this instanceof SpreadsheetCellHistoryToken) {
                final SpreadsheetCellHistoryToken cell = this.cast(SpreadsheetCellHistoryToken.class);

                historyToken = cellReferences(
                    id,
                    name,
                    cell.anchoredSelection(),
                    offsetAndCount
                );
            }
        }

        return historyToken;
    }

    // RELOAD...........................................................................................................

    public final HistoryToken reload() {
        HistoryToken historyToken = this;

        if (this instanceof PluginListHistoryToken) {
            historyToken = pluginListReload(
                this.cast(PluginListHistoryToken.class).offsetAndCount
            );
        } else {
            if (this instanceof SpreadsheetListHistoryToken) {
                historyToken = spreadsheetListReload(
                    this.cast(SpreadsheetListHistoryToken.class).offsetAndCount
                );

            } else {
                if (this instanceof SpreadsheetNameHistoryToken) {
                    final SpreadsheetNameHistoryToken spreadsheetNameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);
                    final SpreadsheetId id = spreadsheetNameHistoryToken.id();
                    final SpreadsheetName name = spreadsheetNameHistoryToken.name();

                    if (this instanceof SpreadsheetCellHistoryToken) {
                        historyToken = cellReload(
                            id,
                            name,
                            this.cast(SpreadsheetCellHistoryToken.class)
                                .anchoredSelection()
                        );
                    } else {
                        historyToken = spreadsheetReload(
                            id,
                            name
                        );
                    }

                }
            }
        }

        return historyToken;
    }

    final HistoryToken parseOffsetCountReload(final TextCursor cursor) {
        HistoryToken historyToken = this.parseOffsetAndCount(cursor);

        String nextComponent = parseComponentOrEmpty(cursor);

        do {
            switch (nextComponent) {
                case "":
                    break;
                case RELOAD_STRING:
                    historyToken = historyToken.reload();
                    break;
                default:
                    cursor.end();
                    break;
            }
            nextComponent = parseComponentOrEmpty(cursor);
        } while (false == nextComponent.isEmpty());

        return historyToken;
    }

    // RENAME...........................................................................................................

    public final HistoryToken rename() {
        HistoryToken historyToken = this;

        if (false == this instanceof SpreadsheetListRenameHistoryToken && false == this instanceof SpreadsheetRenameHistoryToken) {
            if (this instanceof SpreadsheetIdHistoryToken) {
                final SpreadsheetId id = this.cast(SpreadsheetIdHistoryToken.class)
                    .id();

                if (this instanceof SpreadsheetNameHistoryToken) {
                    historyToken = spreadsheetRenameSelect(
                        id,
                        this.cast(SpreadsheetNameHistoryToken.class)
                            .name()
                    );
                } else {
                    historyToken = spreadsheetListRenameSelect(id);
                }
            }
        }

        return historyToken;
    }

    private static HistoryToken parseRename(final TextCursor cursor) {
        HistoryToken historyToken = SPREADSHEET_LIST_SELECT_HISTORY_TOKEN;

        final Optional<String> maybeComponent = parseComponent(cursor);
        if (maybeComponent.isPresent()) {
            historyToken = HistoryToken.spreadsheetListRenameSelect(
                SpreadsheetId.parse(maybeComponent.get())
            ).parse(cursor);
        }

        return historyToken;
    }

    // SAVE VALUE.......................................................................................................

    /**
     * If this is a {@link HistoryToken} with a save value that will be returned.
     */
    public final <T> Optional<T> saveValue() {
        T value = null;

        if (this instanceof Value) {
            final Value<T> hasValue = Cast.to(this);
            value = hasValue.value();
        }

        return Optional.ofNullable(value);
    }

    /**
     * Saves an empty {@link String} which is equivalent to deleting the property.
     */
    public final HistoryToken clearSaveValue() {
        return this.setSaveStringValue("");
    }

    abstract public HistoryToken setSaveValue(final Optional<?> value);

    final void reportInvalidSaveValue(final Object value,
                                        final Class<?> expected) {
        // Invalid value: got String expected Integer

        throw new IllegalArgumentException(
            "Invalid value: got " +
                value.getClass().getSimpleName() +
                " expected " +
                expected.getSimpleName()
        );
    }

    /**
     * if possible creates a save, otherwise returns this.
     */
    public final HistoryToken setSaveStringValue(final String value) {
        Objects.requireNonNull(value, "value");

        HistoryToken saved = this;

        if (this instanceof PluginNameHistoryToken) {
            final PluginNameHistoryToken pluginNameHistoryToken = (PluginNameHistoryToken) this;

            saved = HistoryToken.pluginSave(
                pluginNameHistoryToken.name,
                value
            );
        } else {
            if (this instanceof SpreadsheetIdHistoryToken) {
                final SpreadsheetIdHistoryToken spreadsheetId = this.cast(SpreadsheetIdHistoryToken.class);
                final SpreadsheetId id = spreadsheetId.id();

                if (this instanceof SpreadsheetNameHistoryToken) {
                    final SpreadsheetName name = this.cast(SpreadsheetNameHistoryToken.class).name();

                    if (this instanceof SpreadsheetFormHistoryToken) {
                        saved = HistoryToken.formSave(
                            id,
                            name,
                            HistoryToken.parseJson(
                                JsonNode.parse(value),
                                SpreadsheetForms.FORM_CLASS
                            )
                        );
                    }

                    if (this instanceof SpreadsheetMetadataPropertyHistoryToken) {
                        if (this instanceof SpreadsheetMetadataPropertySelectHistoryToken) {
                            final SpreadsheetMetadataPropertySelectHistoryToken<?> spreadsheetMetadataPropertySelectHistoryToken = this.cast(SpreadsheetMetadataPropertySelectHistoryToken.class);

                            // raw type here simplest option
                            final SpreadsheetMetadataPropertyName<?> propertyName = spreadsheetMetadataPropertySelectHistoryToken.propertyName;

                            saved = HistoryToken.metadataPropertySave(
                                id,
                                name,
                                propertyName,
                                Cast.to(
                                    parseOptional(
                                        value,
                                        propertyName::parseUrlFragmentSaveValue
                                    )
                                )
                            );
                        }

                        if (this instanceof SpreadsheetMetadataPropertyStyleHistoryToken) {
                            final SpreadsheetMetadataPropertyStyleHistoryToken<?> spreadsheetMetadataPropertyStyleHistoryToken = this.cast(SpreadsheetMetadataPropertyStyleHistoryToken.class);

                            final TextStylePropertyName<?> propertyName = spreadsheetMetadataPropertyStyleHistoryToken.stylePropertyName();

                            return HistoryToken.metadataPropertyStyleSave(
                                id,
                                name,
                                propertyName,
                                Cast.to(
                                    parseOptional(
                                        value,
                                        propertyName::parseValue
                                    )
                                )
                            );
                        }
                    }

                    if (this instanceof SpreadsheetSelectionHistoryToken) {
                        if (this instanceof SpreadsheetAnchoredSelectionHistoryToken) {
                            final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection = this.cast(SpreadsheetAnchoredSelectionHistoryToken.class)
                                .anchoredSelection();

                            if (this instanceof SpreadsheetCellHistoryToken) {
                                if (this instanceof SpreadsheetCellDateTimeSymbolsHistoryToken && false == this instanceof SpreadsheetCellDateTimeSymbolsUnselectHistoryToken) {
                                    saved = HistoryToken.cellDateTimeSymbolsSave(
                                        id,
                                        name,
                                        anchoredSpreadsheetSelection,
                                        parseOptional(
                                            value,
                                            DateTimeSymbols::parse
                                        )
                                    );
                                }

                                if (this instanceof SpreadsheetCellDecimalNumberSymbolsHistoryToken && false == this instanceof SpreadsheetCellDecimalNumberSymbolsUnselectHistoryToken) {
                                    saved = HistoryToken.cellDecimalNumberSymbolsSave(
                                        id,
                                        name,
                                        anchoredSpreadsheetSelection,
                                        parseOptional(
                                            value,
                                            DecimalNumberSymbols::parse
                                        )
                                    );
                                }

                                if (this instanceof SpreadsheetCellFormatterHistoryToken && false == this instanceof SpreadsheetCellFormatterUnselectHistoryToken) {
                                    saved = HistoryToken.cellFormatterSave(
                                        id,
                                        name,
                                        anchoredSpreadsheetSelection,
                                        parseOptional(
                                            value,
                                            SpreadsheetFormatterSelector::parse
                                        )
                                    );
                                }

                                if (this instanceof SpreadsheetCellFindHistoryToken) {
                                    saved = HistoryToken.cellFind(
                                        id,
                                        name,
                                        anchoredSpreadsheetSelection,
                                        SpreadsheetCellFindQuery.parse(value)
                                    );
                                }

                                if (this instanceof SpreadsheetCellFormHistoryToken) {
                                    saved = HistoryToken.cellFormSave(
                                        id,
                                        name,
                                        anchoredSpreadsheetSelection,
                                        this.cast(SpreadsheetCellFormHistoryToken.class)
                                            .formName(),
                                        parseJson(
                                            value.isEmpty() ?
                                                JsonNode.object() :
                                                JsonNode.parse(value),
                                            SpreadsheetCellReferenceToValueMap.class
                                        )
                                    );
                                }

                                if (this instanceof SpreadsheetCellFormulaHistoryToken) {
                                    saved = HistoryToken.cellFormulaSave(
                                        id,
                                        name,
                                        anchoredSpreadsheetSelection,
                                        value
                                    );
                                }

                                if (this instanceof SpreadsheetCellLabelHistoryToken) {
                                    saved = value.isEmpty() ?
                                        this.clearAction() :
                                        this.setLabelName(
                                            Optional.of(
                                                SpreadsheetSelection.labelName(value)
                                            )
                                        );
                                }

                                if (this instanceof SpreadsheetCellLocaleHistoryToken) {
                                    saved = HistoryToken.cellLocaleSave(
                                        id,
                                        name,
                                        anchoredSpreadsheetSelection,
                                        Optional.of(
                                            Locale.forLanguageTag(value)
                                        )
                                    );
                                }

                                if (this instanceof SpreadsheetCellParserHistoryToken && false == this instanceof SpreadsheetCellParserUnselectHistoryToken) {
                                    saved = HistoryToken.cellParserSave(
                                        id,
                                        name,
                                        anchoredSpreadsheetSelection,
                                        parseOptional(
                                            value,
                                            SpreadsheetParserSelector::parse
                                        )
                                    );
                                }

                                if (this instanceof SpreadsheetCellSaveCellHistoryToken) {
                                    final SpreadsheetCellSaveCellHistoryToken spreadsheetCellSaveCellHistoryToken = this.cast(SpreadsheetCellSaveCellHistoryToken.class);
                                    saved = spreadsheetCellSaveCellHistoryToken.replace(
                                        id,
                                        name,
                                        anchoredSpreadsheetSelection,
                                        parseJson(
                                            TextCursors.charSequence(value),
                                            SpreadsheetCellSet.class
                                        )
                                    );
                                }

                                if (this instanceof SpreadsheetCellSaveMapHistoryToken) {
                                    final SpreadsheetCellSaveMapHistoryToken<Map<?, ?>> spreadsheetCellSaveMapHistoryToken = this.cast(SpreadsheetCellSaveMapHistoryToken.class);
                                    saved = spreadsheetCellSaveMapHistoryToken.replace(
                                        id,
                                        name,
                                        anchoredSpreadsheetSelection,
                                        spreadsheetCellSaveMapHistoryToken.parseSaveValue(
                                            TextCursors.charSequence(value)
                                        )
                                    );
                                }

                                if (this instanceof SpreadsheetCellSortHistoryToken) {
                                    saved = HistoryToken.cellSortSave(
                                        id,
                                        name,
                                        anchoredSpreadsheetSelection,
                                        SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse(value)
                                    );
                                }

                                if (this instanceof SpreadsheetCellStyleSelectHistoryToken) {
                                    final SpreadsheetCellStyleSelectHistoryToken<?> spreadsheetCellStyleSelectHistoryToken = this.cast(SpreadsheetCellStyleSelectHistoryToken.class);
                                    final TextStylePropertyName<?> propertyName = spreadsheetCellStyleSelectHistoryToken.propertyName();

                                    saved = cellStyleSave(
                                        id,
                                        name,
                                        anchoredSpreadsheetSelection,
                                        propertyName,
                                        Cast.to(
                                            parseOptional(
                                                value,
                                                propertyName::parseValue
                                            )
                                        )
                                    );
                                }

                                if (this instanceof SpreadsheetCellValidatorHistoryToken && false == this instanceof SpreadsheetCellValidatorUnselectHistoryToken) {
                                    saved = HistoryToken.cellValidatorSave(
                                        id,
                                        name,
                                        anchoredSpreadsheetSelection,
                                        parseOptional(
                                            value,
                                            ValidatorSelector::parse
                                        )
                                    );
                                }

                                if (this instanceof SpreadsheetCellValueSaveHistoryToken || this instanceof SpreadsheetCellValueSelectHistoryToken) {
                                    saved = HistoryToken.cellValueSave(
                                        id,
                                        name,
                                        anchoredSpreadsheetSelection,
                                        this.valueType()
                                            .get(),
                                        value
                                    );
                                }

                                if (this instanceof SpreadsheetCellValueTypeHistoryToken && false == this instanceof SpreadsheetCellValueTypeUnselectHistoryToken) {
                                    saved = HistoryToken.cellValueTypeSave(
                                        id,
                                        name,
                                        anchoredSpreadsheetSelection,
                                        parseOptional(
                                            value,
                                            ValueTypeName::with
                                        )
                                    );
                                }
                            } else {
                                if (this instanceof SpreadsheetColumnSortHistoryToken) {
                                    saved = HistoryToken.columnSortSave(
                                        id,
                                        name,
                                        anchoredSpreadsheetSelection,
                                        SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse(value)
                                    );
                                }

                                if (this instanceof SpreadsheetRowSortHistoryToken) {
                                    saved = HistoryToken.rowSortSave(
                                        id,
                                        name,
                                        anchoredSpreadsheetSelection,
                                        SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse(value)
                                    );
                                }
                            }
                        }
                        if (this instanceof SpreadsheetLabelMappingSelectHistoryToken) {
                            saved = labelMappingSave(
                                id,
                                name,
                                this.cast(SpreadsheetLabelMappingSelectHistoryToken.class)
                                    .labelName
                                    .setLabelMappingReference(
                                        SpreadsheetSelection.parseExpressionReference(value)
                                    )
                            );
                        }
                    } else {
                        if (this instanceof SpreadsheetRenameHistoryToken) {
                            if (value.isEmpty()) {
                                saved = HistoryToken.spreadsheetRenameSelect(
                                    id,
                                    name
                                );
                            } else {
                                saved = HistoryToken.spreadsheetRenameSave(
                                    id,
                                    name,
                                    SpreadsheetName.with(value)
                                );
                            }
                        }
                    }
                } else {
                    if (this instanceof SpreadsheetListRenameHistoryToken) {
                        if (value.isEmpty()) {
                            saved = HistoryToken.spreadsheetListRenameSelect(
                                id
                            );
                        } else {
                            saved = HistoryToken.spreadsheetListRenameSave(
                                id,
                                SpreadsheetName.with(value)
                            );
                        }
                    }
                }
            }
        }

        return this.elseIfDifferent(saved);
    }

    private static <T> Optional<T> parseOptional(final String text,
                                                 final Function<String, T> notEmptyParser) {
        return Optional.ofNullable(
            text.isEmpty() ?
                null :
                notEmptyParser.apply(text)
        );
    }

    /**
     * Returns true if this {@link HistoryToken} contains a save value.
     */
    public final boolean isSave() {
        return this.getClass()
            .getSimpleName()
            .contains("Save");
    }

    // SELECTION........................................................................................................

    public final Optional<SpreadsheetSelection> selection() {
        return this.anchoredSelectionOrEmpty()
            .map(AnchoredSpreadsheetSelection::selection);
    }

    /**
     * Returns an instance with the selection cleared or removed if necessary
     */
    public final HistoryToken clearSelection() {
        return this.setAnchoredSelection(
            Optional.empty()
        );
    }

    /**
     * Returns a {@link HistoryToken} with the given {@link SpreadsheetSelection}. if the selection is different
     * the action will be cleared.
     */
    public final HistoryToken setSelection(final Optional<? extends SpreadsheetSelection> selection) {
        Objects.requireNonNull(selection, "selection");

        HistoryToken historyToken = this;

        final Optional<AnchoredSpreadsheetSelection> maybeAnchoredSpreadsheetSelection = this.anchoredSelectionOrEmpty();

        if (false == maybeAnchoredSpreadsheetSelection.map(AnchoredSpreadsheetSelection::selection).equals(selection)) {

            if (this instanceof SpreadsheetNameHistoryToken) {
                final SpreadsheetNameHistoryToken spreadsheetNameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);

                final SpreadsheetId id = spreadsheetNameHistoryToken.id();
                final SpreadsheetName name = spreadsheetNameHistoryToken.name();

                if (selection.isPresent()) {
                    final SpreadsheetSelection spreadsheetSelection = selection.get();

                    AnchoredSpreadsheetSelection anchoredSpreadsheetSelection = maybeAnchoredSpreadsheetSelection.orElse(null);
                    if (null != anchoredSpreadsheetSelection) {
                        anchoredSpreadsheetSelection = anchoredSpreadsheetSelection.setSelection(spreadsheetSelection);
                    } else {
                        anchoredSpreadsheetSelection = selection.get()
                            .setDefaultAnchor();
                    }

                    historyToken = this.setAnchoredSelection(
                        Optional.of(anchoredSpreadsheetSelection.setSelection(spreadsheetSelection))
                    );
                } else {
                    historyToken = spreadsheetSelect(
                        id,
                        name
                    );
                }
            }
        }

        return historyToken;
    }

    // SORT.............................................................................................................

    /**
     * if possible creates a sort edit, otherwise returns this.
     */
    public final HistoryToken setSortEdit(final String comparatorNames) {
        Objects.requireNonNull(comparatorNames, "comparatorNames");

        HistoryToken historyToken = null;

        if (this instanceof SpreadsheetAnchoredSelectionHistoryToken) {
            final SpreadsheetAnchoredSelectionHistoryToken anchoredSelectionHistoryToken = this.cast(SpreadsheetAnchoredSelectionHistoryToken.class);

            final SpreadsheetId id = anchoredSelectionHistoryToken.id();
            final SpreadsheetName name = anchoredSelectionHistoryToken.name();
            final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection = anchoredSelectionHistoryToken.anchoredSelection();

            if (this instanceof SpreadsheetCellHistoryToken) {
                historyToken = HistoryToken.cellSortEdit(
                    id,
                    name,
                    anchoredSpreadsheetSelection,
                    comparatorNames
                );
            }

            if (this instanceof SpreadsheetColumnHistoryToken) {
                historyToken = HistoryToken.columnSortEdit(
                    id,
                    name,
                    anchoredSpreadsheetSelection,
                    comparatorNames
                );
            }

            if (this instanceof SpreadsheetRowHistoryToken) {
                historyToken = HistoryToken.rowSortEdit(
                    id,
                    name,
                    anchoredSpreadsheetSelection,
                    comparatorNames
                );
            }
        }

        return this.elseIfDifferent(historyToken);
    }

    /**
     * if possible creates a sort save, otherwise returns this.
     */
    public final HistoryToken setSortSave(final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparatorNames) {
        Objects.requireNonNull(comparatorNames, "comparatorNames");

        HistoryToken historyToken = null;

        if (this instanceof SpreadsheetAnchoredSelectionHistoryToken) {
            final SpreadsheetAnchoredSelectionHistoryToken anchoredSelectionHistoryToken = this.cast(SpreadsheetAnchoredSelectionHistoryToken.class);

            final SpreadsheetId id = anchoredSelectionHistoryToken.id();
            final SpreadsheetName name = anchoredSelectionHistoryToken.name();
            final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection = anchoredSelectionHistoryToken.anchoredSelection();

            if (this instanceof SpreadsheetCellHistoryToken) {
                historyToken = HistoryToken.cellSortSave(
                    id,
                    name,
                    anchoredSpreadsheetSelection,
                    comparatorNames
                );
            }

            if (this instanceof SpreadsheetColumnHistoryToken) {
                historyToken = HistoryToken.columnSortSave(
                    id,
                    name,
                    anchoredSpreadsheetSelection,
                    comparatorNames
                );
            }

            if (this instanceof SpreadsheetRowHistoryToken) {
                historyToken = HistoryToken.rowSortSave(
                    id,
                    name,
                    anchoredSpreadsheetSelection,
                    comparatorNames
                );
            }
        }

        return this.elseIfDifferent(historyToken);
    }

    public final Optional<SpreadsheetFormatterSelector> spreadsheetFormatterSelector() {
        Optional<SpreadsheetFormatterSelector> spreadsheetFormatterSelector = Optional.empty();

        if (this instanceof SpreadsheetCellFormatterHistoryToken) {
            spreadsheetFormatterSelector = this.cast(SpreadsheetCellFormatterHistoryToken.class)
                .spreadsheetFormatterSelector;
        } else {
            if (this instanceof SpreadsheetMetadataPropertySaveHistoryToken) {
                final SpreadsheetMetadataPropertySaveHistoryToken<SpreadsheetFormatterSelector> metadataSave = this.cast(SpreadsheetMetadataPropertySaveHistoryToken.class);
                final SpreadsheetMetadataPropertyName<?> propertyName = metadataSave.propertyName;
                spreadsheetFormatterSelector = propertyName.isSpreadsheetFormatterSelector() ?
                    metadataSave.value() :
                    Optional.empty();
            }
        }

        return spreadsheetFormatterSelector;
    }

    public final Optional<SpreadsheetParserSelector> spreadsheetParserSelector() {
        Optional<SpreadsheetParserSelector> spreadsheetParserSelector = Optional.empty();

        if (this instanceof SpreadsheetCellParserHistoryToken) {
            spreadsheetParserSelector = this.cast(SpreadsheetCellParserHistoryToken.class)
                .spreadsheetParserSelector;
        } else {
            if (this instanceof SpreadsheetMetadataPropertySaveHistoryToken) {
                final SpreadsheetMetadataPropertySaveHistoryToken<SpreadsheetParserSelector> metadataSave = this.cast(SpreadsheetMetadataPropertySaveHistoryToken.class);
                final SpreadsheetMetadataPropertyName<?> propertyName = metadataSave.propertyName;
                spreadsheetParserSelector = propertyName.isSpreadsheetParserSelector() ?
                    metadataSave.value() :
                    Optional.empty();
            }
        }

        return spreadsheetParserSelector;
    }

    // STYLE............................................................................................................

    /**
     * Factory that creates a {@link SpreadsheetNameHistoryToken} with the given {@link TextStylePropertyName} property name.
     */
    public final HistoryToken setStylePropertyName(final TextStylePropertyName<?> propertyName) {
        Objects.requireNonNull(propertyName, "propertyName");

        HistoryToken historyToken = null;

        if (this instanceof SpreadsheetNameHistoryToken) {
            final SpreadsheetNameHistoryToken spreadsheetNameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);
            final SpreadsheetId id = spreadsheetNameHistoryToken.id();
            final SpreadsheetName name = spreadsheetNameHistoryToken.name();

            if (this instanceof SpreadsheetSelectHistoryToken || this instanceof SpreadsheetMetadataHistoryToken) {
                historyToken = metadataPropertyStyle(
                    id,
                    name,
                    propertyName
                );
            } else {
                if (this instanceof SpreadsheetCellHistoryToken) {
                    historyToken = cellStyle(
                        id,
                        name,
                        this.cast(SpreadsheetCellHistoryToken.class)
                            .anchoredSelection(),
                        propertyName
                    );
                }
            }
        }

        return this.elseIfDifferent(historyToken);
    }

    // TOOLBAR..........................................................................................................

    public final HistoryToken toolbar() {
        HistoryToken historyToken = this;

        if (this instanceof SpreadsheetAnchoredSelectionHistoryToken) {
            final SpreadsheetAnchoredSelectionHistoryToken spreadsheetAnchoredSelectionHistoryToken = this.cast(SpreadsheetAnchoredSelectionHistoryToken.class);

            final SpreadsheetId id = spreadsheetAnchoredSelectionHistoryToken.id();
            final SpreadsheetName name = spreadsheetAnchoredSelectionHistoryToken.name();
            final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection = spreadsheetAnchoredSelectionHistoryToken.anchoredSelection();

            if (this instanceof SpreadsheetCellDateTimeSymbolsSelectHistoryToken) {
                historyToken = cellDateTimeSymbolsUnselect(
                    id,
                    name,
                    anchoredSpreadsheetSelection
                );
            }

            if (this instanceof SpreadsheetCellDecimalNumberSymbolsSelectHistoryToken) {
                historyToken = cellDecimalNumberSymbolsUnselect(
                    id,
                    name,
                    anchoredSpreadsheetSelection
                );
            }

            if (this instanceof SpreadsheetCellFormatterSelectHistoryToken) {
                historyToken = cellFormatterUnselect(
                    id,
                    name,
                    anchoredSpreadsheetSelection
                );
            }

            if (this instanceof SpreadsheetCellLocaleSelectHistoryToken) {
                historyToken = cellLocaleUnselect(
                    id,
                    name,
                    anchoredSpreadsheetSelection
                );
            }

            if (this instanceof SpreadsheetCellParserSelectHistoryToken) {
                historyToken = cellParserUnselect(
                    id,
                    name,
                    anchoredSpreadsheetSelection
                );
            }

            if (this instanceof SpreadsheetCellValidatorSelectHistoryToken) {
                historyToken = cellValidatorUnselect(
                    id,
                    name,
                    anchoredSpreadsheetSelection
                );
            }

            if (this instanceof SpreadsheetCellValueTypeSelectHistoryToken) {
                historyToken = cellValueTypeUnselect(
                    id,
                    name,
                    anchoredSpreadsheetSelection
                );
            }
        }

        return historyToken;
    }

    // UNFREEZE.........................................................................................................

    /**
     * if possible creates a unfreeze.
     */
    public final HistoryToken unfreeze() {
        HistoryToken historyToken = this;

        if (this instanceof SpreadsheetAnchoredSelectionHistoryToken) {
            final SpreadsheetAnchoredSelectionHistoryToken anchored = this.cast(SpreadsheetAnchoredSelectionHistoryToken.class);
            final SpreadsheetId id = anchored.id();
            final SpreadsheetName name = anchored.name();
            final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection = anchored.anchoredSelection();

            if (this instanceof SpreadsheetCellHistoryToken) {
                historyToken = cellUnfreeze(
                    id,
                    name,
                    anchoredSpreadsheetSelection
                );
            } else {
                if (this instanceof SpreadsheetColumnHistoryToken) {
                    historyToken = columnUnfreeze(
                        id,
                        name,
                        anchoredSpreadsheetSelection
                    );
                } else {
                    if (this instanceof SpreadsheetRowHistoryToken) {
                        historyToken = rowUnfreeze(
                            id,
                            name,
                            anchoredSpreadsheetSelection
                        );
                    }
                }
            }
        }

        return historyToken;
    }

    /**
     * Tries to create an unfreeze token or {@link Optional#empty()} because the {@link SpreadsheetSelection} is invalid.
     */
    public final Optional<HistoryToken> unfreezeOrEmpty() {
        HistoryToken historyToken;

        try {
            historyToken = this.unfreeze();
        } catch (final RuntimeException ignored) {
            historyToken = null;
        }

        return Optional.ofNullable(historyToken);
    }

    // validator........................................................................................................

    /**
     * If possible selects a {@link ValidatorSelector} {@link HistoryToken}.
     */
    public final HistoryToken validator() {
        HistoryToken historyToken = this;

        if (this instanceof SpreadsheetCellSelectHistoryToken || this instanceof SpreadsheetCellMenuHistoryToken) {
            final SpreadsheetCellHistoryToken cell = this.cast(SpreadsheetCellHistoryToken.class);

            historyToken = HistoryToken.cellValidatorSelect(
                cell.id(),
                cell.name(),
                cell.anchoredSelection()
            );
        }

        return historyToken;
    }

    // value............................................................................................................

    public final HistoryToken setValue(final Optional<ValueTypeName> valueType) {
        HistoryToken historyToken = this;

        if (this instanceof SpreadsheetCellHistoryToken) {
            final SpreadsheetCellHistoryToken cell = this.cast(SpreadsheetCellHistoryToken.class);

            final SpreadsheetId id = cell.id();
            final SpreadsheetName name = cell.name();
            final AnchoredSpreadsheetSelection anchoredSelection = cell.anchoredSelection();

            if (this instanceof SpreadsheetCellValueHistoryToken) {
                if (false == this.valueType().equals(valueType)) {
                    if (valueType.isPresent()) {
                        historyToken = HistoryToken.cellValueSelect(
                            id,
                            name,
                            anchoredSelection,
                            valueType.get()
                        );
                    } else {
                        historyToken = HistoryToken.cellValueUnselect(
                            id,
                            name,
                            anchoredSelection
                        );
                    }
                }
            } else {
                if (valueType.isPresent()) {
                    historyToken = HistoryToken.cellValueSelect(
                        id,
                        name,
                        anchoredSelection,
                        valueType.get()
                    );
                } else {
                    historyToken = HistoryToken.cellValueUnselect(
                        id,
                        name,
                        anchoredSelection
                    );
                }
            }
        }

        return this.elseIfDifferent(historyToken);
    }

    // valueType........................................................................................................

    public final Optional<ValueTypeName> valueType() {
        return this instanceof SpreadsheetCellValueHistoryToken ?
            this.cast(SpreadsheetCellValueHistoryToken.class).valueType :
            Optional.empty();
    }

    /**
     * If possible selects a {@link ValueTypeName} {@link HistoryToken}.
     */
    public final HistoryToken setValueType() {
        HistoryToken historyToken = this;

        if (this instanceof SpreadsheetCellHistoryToken) {
            final SpreadsheetCellHistoryToken cell = this.cast(SpreadsheetCellHistoryToken.class);

            historyToken = HistoryToken.cellValueTypeSelect(
                cell.id(),
                cell.name(),
                cell.anchoredSelection()
            );
        }

        return historyToken;
    }

    final HistoryToken elseIfDifferent(final HistoryToken historyToken) {
        return null == historyToken || this.equals(historyToken) ?
            this :
            historyToken;
    }

    // shouldIgnore.....................................................................................................

    /**
     * Temporary {@link HistoryToken} which perform an immediate action and probably return the previous {@link HistoryToken}
     * should probably be ignored by {@link HistoryTokenWatcher} that are monitoring if a {@link SpreadsheetSelection} changes
     * or present a stateful UI like a dialog box.
     */
    public final boolean shouldIgnore() {
        final String name = this.getClass().getSimpleName();
        return name.contains("InsertAfter") ||
            name.contains("Menu") ||
            name.contains("Reload") ||
            name.contains("Save");
    }

    // HELPERS..........................................................................................................

    /**
     * Type safe cast to the given {@link Class literal}.
     */
    public final <T extends HistoryToken> T cast(final Class<T> cast) {
        return HistoryTokenCastGwt.cast(
            this,
            cast
        );
    }

    // UI...............................................................................................................

    /**
     * Creates a {@link SpreadsheetContextMenuItem} with the given id & text and this {@link HistoryToken}.
     */
    public final <T> SpreadsheetContextMenuItem contextMenuItem(final String id,
                                                                final String text) {
        return SpreadsheetContextMenuItem.<T>with(
            id,
            text
        ).historyToken(
            Optional.of(this)
        );
    }

    /**
     * Creates a link with the given id.
     */
    public final HistoryTokenAnchorComponent link(final String id) {
        return HistoryTokenAnchorComponent.empty()
            .setId(id + SpreadsheetElementIds.LINK)
            .setHistoryToken(
                Optional.of(this)
            );
    }

    /**
     * Creates a link with the given ID, text and save value.
     */
    public final HistoryTokenAnchorComponent saveLink(final String id,
                                                      final String text,
                                                      final String saveText) {
        return this.link(id)
            .setTextContent(text)
            .setHistoryToken(
                Optional.of(
                    this.setSaveStringValue(saveText)
                )
            );
    }

    /**
     * Creates a {@link UrlFragment} with a save returning a {@link UrlFragment} with its equivalent value.
     */
    static UrlFragment saveUrlFragment(final Object value) {
        // always want slash after SAVE
        return SAVE.append(UrlFragment.SLASH)
            .append(
                saveUrlFragmentValue(value)
            );
    }

    // @VisibleForTesting
    static UrlFragment saveUrlFragmentValue(final Object value) {
        if (value instanceof Form) {
            throw new IllegalArgumentException("Invalid saveUrlFragmentValue form " + value);
        }

        return null == value ?
            UrlFragment.EMPTY :
            value instanceof UrlFragment ?
                (UrlFragment) value :
                value instanceof HasUrlFragment ?
                    ((HasUrlFragment) value)
                        .urlFragment() :
                    value instanceof Optional ?
                        saveUrlFragmentValueOptional((Optional<?>) value) :
                        UrlFragment.with(
                            String.valueOf(
                                value instanceof Locale ?
                                    // toLanguageTag = EN-AU while Locale#toString EN_AU
                                    ((Locale) value).toLanguageTag() :
                                    value instanceof HasText ?
                                        ((HasText) value).text() :
                                        value
                            )
                        );
    }

    private static UrlFragment saveUrlFragmentValueOptional(final Optional<?> value) {
        return saveUrlFragmentValue(
            value.orElse(null)
        );
    }

    // Object...........................................................................................................

    @Override
    public final int hashCode() {
        return this.urlFragment().hashCode();
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
            (other != null && this.getClass().equals(other.getClass()) &&
                this.equals0((HistoryToken) other));
    }

    private boolean equals0(final HistoryToken other) {
        return this.urlFragment().equals(other.urlFragment());
    }

    @Override
    public final String toString() {
        return this.getClass().getSimpleName() +
            ' ' +
            CharSequences.quoteAndEscape(
                this.urlFragment()
                    .toString()
            );
    }
}
