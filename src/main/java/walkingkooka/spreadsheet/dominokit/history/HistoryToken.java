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

import walkingkooka.net.HasUrlFragment;
import walkingkooka.net.UrlFragment;
import walkingkooka.plugin.PluginName;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetHateosResourceNames;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.SpreadsheetUrlFragments;
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNamesList;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.clipboard.SpreadsheetCellClipboardKind;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.reference.SpreadsheetContextMenuItem;
import walkingkooka.spreadsheet.engine.SpreadsheetCellFindQuery;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.format.pattern.HasSpreadsheetPatternKind;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelNameResolver;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoName;
import walkingkooka.text.CharSequences;
import walkingkooka.text.HasText;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserContexts;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.cursor.parser.StringParserToken;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContexts;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.BiFunction;
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
 * <br>
 * The {@link #onHistoryTokenChange(HistoryToken, AppContext)} is implemented by some actions such as {@link SpreadsheetCellFormulaSaveHistoryToken},
 * which when it appears in the current history token hash will save a new formula text for the cell belonging to the spreadsheet id.
 * This is done by calling a REST end-point on the server using {@link SpreadsheetDeltaFetcher} which uses
 * the browser's fetch object with a JSON payload.
 * <br>
 * Comprehensive testing for parsing and more are available under the corresponding test history package.
 */
public abstract class HistoryToken implements HasUrlFragment,
        HasSpreadsheetPatternKind {

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

    final static String DELETE_STRING = "delete";

    final static UrlFragment DELETE = UrlFragment.parse(DELETE_STRING);

    final static String EDIT_STRING = "edit";

    final static UrlFragment EDIT = UrlFragment.parse(EDIT_STRING);

    final static String FILE_STRING = "file";

    final static UrlFragment FILE = UrlFragment.parse(FILE_STRING);

    final static String FIND_STRING = "find";

    final static UrlFragment FIND = UrlFragment.parse(FIND_STRING);

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

    final static String LABEL_STRING = SpreadsheetHateosResourceNames.LABEL_STRING;

    final static UrlFragment LABEL = UrlFragment.parse(LABEL_STRING);

    final static String MENU_STRING = "menu";

    final static UrlFragment MENU = UrlFragment.parse(MENU_STRING);

    final static String OFFSET_STRING = "offset";

    final static UrlFragment OFFSET = UrlFragment.parse(OFFSET_STRING);

    final static String PARSER_STRING = "parser";

    final static UrlFragment PARSER = SpreadsheetUrlFragments.PARSER;

    final static String PASTE_STRING = "paste";

    final static UrlFragment PASTE = UrlFragment.parse(PASTE_STRING);

    final static String PLUGIN_STRING = "plugin";

    final static UrlFragment PLUGIN = SpreadsheetUrlFragments.PLUGIN;

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

    final static String WILDCARD_STRING = "*";

    final static UrlFragment WILDCARD = UrlFragment.parse(WILDCARD_STRING);

    final static JsonNodeMarshallContext MARSHALL_CONTEXT = JsonNodeMarshallContexts.basic();

    /**
     * {@see SpreadsheetCellSelectHistoryToken}
     */
    public static SpreadsheetCellSelectHistoryToken cell(final SpreadsheetId id,
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
     * {@see SpreadsheetCellSaveFormulaHistoryToken}
     */
    public static SpreadsheetCellSaveFormulaHistoryToken cellSaveFormula(final SpreadsheetId id,
                                                                         final SpreadsheetName name,
                                                                         final AnchoredSpreadsheetSelection anchoredSelection,
                                                                         final Map<SpreadsheetCellReference, String> formulas) {
        return SpreadsheetCellSaveFormulaHistoryToken.with(
                id,
                name,
                anchoredSelection,
                formulas
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

    /**
     * {@see SpreadsheetColumnSelectHistoryToken}
     */
    public static SpreadsheetColumnSelectHistoryToken column(final SpreadsheetId id,
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

    // label............................................................................................................

    /**
     * {@see SpreadsheetLabelMappingSelectHistoryToken}
     */
    public static SpreadsheetLabelMappingSelectHistoryToken labelMapping(final SpreadsheetId id,
                                                                         final SpreadsheetName name,
                                                                         final Optional<SpreadsheetLabelName> label) {
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
    public static PluginListHistoryToken pluginListReload(final OptionalInt offset,
                                                          final OptionalInt count) {
        return PluginListReloadHistoryToken.with(
                offset,
                count
        );
    }

    /**
     * {@see PluginListSelectHistoryToken}
     */
    public static PluginListHistoryToken pluginListSelect(final OptionalInt offset,
                                                          final OptionalInt count) {
        return PluginListSelectHistoryToken.with(
                offset,
                count
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

    // row..............................................................................................................

    /**
     * {@see SpreadsheetRowSelectHistoryToken}
     */
    public static SpreadsheetRowSelectHistoryToken row(final SpreadsheetId id,
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
            if (selection.isCellReference() || selection.isCellRangeReference() || selection.isLabelName()) {
                historyToken = cell(
                        id,
                        name,
                        anchoredSelection
                );
                break;
            }
            if (selection.isColumnReference() || selection.isColumnRangeReference()) {
                historyToken = column(
                        id,
                        name,
                        anchoredSelection
                );
                break;
            }
            if (selection.isRowReference() || selection.isRowRangeReference()) {
                historyToken = row(
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
    public static SpreadsheetListHistoryToken spreadsheetListReload(final OptionalInt offset,
                                                                    final OptionalInt count) {
        return SpreadsheetListReloadHistoryToken.with(
                offset,
                count
        );
    }

    /**
     * {@see SpreadsheetListSelectHistoryToken}
     */
    public static SpreadsheetListHistoryToken spreadsheetListSelect(final OptionalInt offset,
                                                                    final OptionalInt count) {
        return SpreadsheetListSelectHistoryToken.with(
                offset,
                count
        );
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
            OptionalInt.empty(), // from
            OptionalInt.empty() // count
    );

    private final static PluginListSelectHistoryToken PLUGIN_LIST_SELECT_HISTORY_TOKEN = PluginListSelectHistoryToken.with(
            OptionalInt.empty(), // offset
            OptionalInt.empty() // count
    );

    /**
     * Consumes all remaining text into a {@link String}.
     */
    static String parseAll(final TextCursor cursor) {
        final TextCursorSavePoint save = cursor.save();
        cursor.end();

        final String text = save.textBetween()
                .toString();
        return text.isEmpty() ?
                text :
                text.substring(1); // drops assumed leading slash

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
     * A {@link Parser} that consumes a path component within an {@link UrlFragment}.
     */
    private final static Parser<ParserContext> COMPONENT = Parsers.stringInitialAndPartCharPredicate(
            CharPredicates.is('/'),
            CharPredicates.not(
                    CharPredicates.is('/')
            ),
            1,
            MAX_LENGTH
    );

    private final static ParserContext CONTEXT = ParserContexts.fake();

    static String parseComponentOrEmpty(final TextCursor cursor) {
        return parseComponent(cursor)
                .orElse("");
    }

    static OptionalInt parseOptionalInt(final TextCursor cursor) {
        final OptionalInt value;

        final Optional<String> maybeComponent = parseComponent(cursor);
        if (maybeComponent.isPresent()) {
            final String string = maybeComponent.get();
            if (string.isEmpty()) {
                value = OptionalInt.empty();
            } else {
                value = OptionalInt.of(
                        Integer.parseInt(string)
                );
            }
        } else {
            value = OptionalInt.empty();
        }

        return value;
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

    final HistoryToken parseOffsetCountReload(final TextCursor cursor){
        HistoryToken historyToken = this;

        String nextComponent = parseComponentOrEmpty(cursor);

        do {
            switch (nextComponent) {
                case "":
                    break;
                case COUNT_STRING:
                    historyToken = historyToken.setCount(
                            parseCount(cursor)
                    );
                    break;
                case OFFSET_STRING:
                    historyToken = historyToken.setOffset(
                            parseOptionalInt(cursor)
                    );
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
                result = result.parse0(
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

    abstract HistoryToken parse0(final String component,
                                 final TextCursor cursor);

    // token factory methods............................................................................................

    /**
     * Used to close a currently active state, such as a dialog.
     */
    public final HistoryToken close() {
        HistoryToken closed = this;

        if( this instanceof PluginListSelectHistoryToken) {
            closed = this.clearAction();
        }

        if( this instanceof PluginSelectHistoryToken) {
            closed = this.clearAction();
        }

        // must come after PluginSelectHistoryToken
        if( this instanceof PluginFileViewHistoryToken) {
            closed = this.clearAction();
        }

        if (this instanceof SpreadsheetCellFindHistoryToken) {
            closed = this.clearAction();
        }

        if (this instanceof SpreadsheetCellSortHistoryToken || this instanceof SpreadsheetColumnSortHistoryToken || this instanceof SpreadsheetRowSortHistoryToken) {
            closed = this.clearAction();
        }

        if (this instanceof SpreadsheetColumnInsertHistoryToken || this instanceof SpreadsheetRowInsertHistoryToken) {
            closed = this.clearAction();
        }

        if (this instanceof SpreadsheetLabelMappingHistoryToken) {
            final SpreadsheetLabelMappingHistoryToken label = (SpreadsheetLabelMappingHistoryToken) this;
            closed = spreadsheetSelect(
                    label.id(),
                    label.name()
            );
        }
        if (this instanceof SpreadsheetCellFormatterSelectHistoryToken) {
            final SpreadsheetCellFormatterSelectHistoryToken formatter = (SpreadsheetCellFormatterSelectHistoryToken) this;

            closed = cell(
                    formatter.id(),
                    formatter.name(),
                    formatter.anchoredSelection()
            );
        }

        if (this instanceof SpreadsheetCellFormatterSaveHistoryToken) {
            final SpreadsheetCellFormatterHistoryToken formatter = (SpreadsheetCellFormatterHistoryToken) this;

            closed = cellFormatterSelect(
                    formatter.id(),
                    formatter.name(),
                    formatter.anchoredSelection()
            );
        }

        if (this instanceof SpreadsheetCellParserSelectHistoryToken) {
            final SpreadsheetCellParserSelectHistoryToken parser = (SpreadsheetCellParserSelectHistoryToken) this;

            closed = cell(
                    parser.id(),
                    parser.name(),
                    parser.anchoredSelection()
            );
        }

        if (this instanceof SpreadsheetCellParserSaveHistoryToken) {
            final SpreadsheetCellParserHistoryToken parser = (SpreadsheetCellParserHistoryToken) this;

            closed = cellParserSelect(
                    parser.id(),
                    parser.name(),
                    parser.anchoredSelection()
            );
        }

        if (this instanceof SpreadsheetListRenameHistoryToken) {
            closed = spreadsheetListSelect(
                    OptionalInt.empty(), // from
                    OptionalInt.empty() // count
            );
        }

        if (this instanceof SpreadsheetMetadataPropertyHistoryToken) {
            final SpreadsheetMetadataPropertyHistoryToken<?> metadata = (SpreadsheetMetadataPropertyHistoryToken<?>) this;
            closed = metadataSelect(
                    metadata.id(),
                    metadata.name()
            );
        }

        if (this instanceof SpreadsheetRenameHistoryToken) {
            final SpreadsheetRenameHistoryToken rename = this.cast(SpreadsheetRenameHistoryToken.class);
            closed = spreadsheetSelect(
                    rename.id(),
                    rename.name()
            );
        }

        return closed;
    }

    /**
     * Removes an action.
     * <br>
     * cell/menu -> cell
     * cell -> cell
     */
    public abstract HistoryToken clearAction();

    /**
     * If possible creates a {@link SpreadsheetCellClipboardCopyHistoryToken} token.
     */
    public final HistoryToken setCellCopy(final SpreadsheetCellClipboardKind kind) {
        Objects.requireNonNull(kind, "kind");

        final HistoryToken token;

        if (this instanceof SpreadsheetCellHistoryToken) {
            final SpreadsheetCellHistoryToken cell = this.cast(SpreadsheetCellHistoryToken.class);
            token = HistoryToken.cellClipboardCopy(
                    cell.id(),
                    cell.name(),
                    cell.anchoredSelection(),
                    kind
            );
        } else {
            token = this;
        }

        return token;
    }

    /**
     * If possible creates a {@link SpreadsheetCellClipboardCutHistoryToken} token.
     */
    public final HistoryToken setCellCut(final SpreadsheetCellClipboardKind kind) {
        Objects.requireNonNull(kind, "kind");

        final HistoryToken token;

        if (this instanceof SpreadsheetCellHistoryToken) {
            final SpreadsheetCellHistoryToken cell = this.cast(SpreadsheetCellHistoryToken.class);
            token = HistoryToken.cellClipboardCut(
                    cell.id(),
                    cell.name(),
                    cell.anchoredSelection(),
                    kind
            );
        } else {
            token = this;
        }

        return token;
    }

    /**
     * If possible creates a {@link SpreadsheetCellClipboardPasteHistoryToken} token.
     */
    public final HistoryToken setCellPaste(final SpreadsheetCellClipboardKind kind) {
        Objects.requireNonNull(kind, "kind");

        final HistoryToken token;

        if (this instanceof SpreadsheetCellHistoryToken) {
            final SpreadsheetCellHistoryToken cell = this.cast(SpreadsheetCellHistoryToken.class);
            token = HistoryToken.cellClipboardPaste(
                    cell.id(),
                    cell.name(),
                    cell.anchoredSelection(),
                    kind
            );
        } else {
            token = this;
        }

        return token;
    }

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

    public final OptionalInt count() {
        final OptionalInt count;

        if (this instanceof PluginListHistoryToken) {
            count = this.cast(PluginListHistoryToken.class).count;
        } else {
            if (this instanceof SpreadsheetListHistoryToken) {
                count = this.cast(SpreadsheetListHistoryToken.class).count;
            } else {
                if (this instanceof SpreadsheetColumnInsertHistoryToken) {
                    count = this.cast(SpreadsheetColumnInsertHistoryToken.class)
                            .count;
                } else {
                    if (this instanceof SpreadsheetRowInsertHistoryToken) {
                        count = this.cast(SpreadsheetRowInsertHistoryToken.class)
                                .count;
                    } else {
                        count = OptionalInt.empty();
                    }
                }
            }
        }

        return count;
    }

    /**
     * Would be setter that tries to replace the {@link #count()} with new value.
     */
    public final HistoryToken setCount(final OptionalInt count) {
        checkCount(count);

        final HistoryToken with;

        if (this.count().equals(count)) {
            with = this;
        } else {
            if (this instanceof PluginListReloadHistoryToken) {
                final PluginListReloadHistoryToken list = this.cast(PluginListReloadHistoryToken.class);

                with = pluginListReload(
                        list.offset(),
                        count
                );
            } else {
                if (this instanceof PluginListSelectHistoryToken) {
                    final PluginListSelectHistoryToken list = this.cast(PluginListSelectHistoryToken.class);

                    with = pluginListSelect(
                            list.offset(),
                            count
                    );
                } else {
                    if (this instanceof SpreadsheetListReloadHistoryToken) {
                        final SpreadsheetListReloadHistoryToken list = this.cast(SpreadsheetListReloadHistoryToken.class);

                        with = spreadsheetListReload(
                                list.offset(),
                                count
                        );
                    } else {
                        if (this instanceof SpreadsheetListSelectHistoryToken) {
                            final SpreadsheetListSelectHistoryToken list = this.cast(SpreadsheetListSelectHistoryToken.class);

                            with = spreadsheetListSelect(
                                    list.offset(),
                                    count
                            );
                        } else {
                            if (this instanceof SpreadsheetColumnInsertAfterHistoryToken) {
                                final SpreadsheetColumnInsertAfterHistoryToken insert = this.cast(SpreadsheetColumnInsertAfterHistoryToken.class);

                                with = columnInsertAfter(
                                        insert.id(),
                                        insert.name(),
                                        insert.anchoredSelection(),
                                        count
                                );
                            } else {
                                if (this instanceof SpreadsheetColumnInsertBeforeHistoryToken) {
                                    final SpreadsheetColumnInsertBeforeHistoryToken insert = this.cast(SpreadsheetColumnInsertBeforeHistoryToken.class);

                                    with = columnInsertBefore(
                                            insert.id(),
                                            insert.name(),
                                            insert.anchoredSelection(),
                                            count
                                    );
                                } else {
                                    if (this instanceof SpreadsheetRowInsertAfterHistoryToken) {
                                        final SpreadsheetRowInsertAfterHistoryToken insert = this.cast(SpreadsheetRowInsertAfterHistoryToken.class);

                                        with = rowInsertAfter(
                                                insert.id(),
                                                insert.name(),
                                                insert.anchoredSelection(),
                                                count
                                        );
                                    } else {
                                        if (this instanceof SpreadsheetRowInsertBeforeHistoryToken) {
                                            final SpreadsheetRowInsertBeforeHistoryToken insert = this.cast(SpreadsheetRowInsertBeforeHistoryToken.class);

                                            with = rowInsertBefore(
                                                    insert.id(),
                                                    insert.name(),
                                                    insert.anchoredSelection(),
                                                    count
                                            );
                                        } else {
                                            with = this;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return with;
    }

    static OptionalInt parseCount(final TextCursor cursor) {
        return parseOptionalInt(cursor);
    }

    static OptionalInt checkCount(final OptionalInt count) {
        Objects.requireNonNull(count, "count");

        count.ifPresent(value -> {
            if (value <= 0) {
                throw new IllegalArgumentException("Invalid count <= 0 got " + value);
            }
        });

        return count;
    }

    /**
     * if possible creates a delete.
     */
    public final HistoryToken delete() {
        HistoryToken historyToken = this;

        if (this instanceof PluginNameHistoryToken) {
            historyToken = pluginDelete(
                    this.cast(PluginNameHistoryToken.class).name()
            );
        } else {
            if (this instanceof SpreadsheetCellFormatterHistoryToken || this instanceof SpreadsheetCellParserHistoryToken) {
                historyToken = this.clearSave();
            } else {
                if (this instanceof SpreadsheetCellHistoryToken) {
                    final SpreadsheetCellHistoryToken cell = this.cast(SpreadsheetCellHistoryToken.class);

                    historyToken = cellDelete(
                            cell.id(),
                            cell.name(),
                            cell.anchoredSelection()
                    );
                } else {
                    if (this instanceof SpreadsheetColumnHistoryToken) {
                        final SpreadsheetColumnHistoryToken column = this.cast(SpreadsheetColumnHistoryToken.class);

                        historyToken = columnDelete(
                                column.id(),
                                column.name(),
                                column.anchoredSelection()
                        );
                    } else {
                        if (this instanceof SpreadsheetRowHistoryToken) {
                            final SpreadsheetRowHistoryToken row = this.cast(SpreadsheetRowHistoryToken.class);

                            historyToken = rowDelete(
                                    row.id(),
                                    row.name(),
                                    row.anchoredSelection()
                            );
                        } else {
                            if (this instanceof SpreadsheetLabelMappingHistoryToken) {
                                final SpreadsheetLabelMappingHistoryToken labelMapping = this.cast(SpreadsheetLabelMappingHistoryToken.class);
                                final Optional<SpreadsheetLabelName> labelName = labelMapping.labelName();

                                if (labelName.isPresent()) {
                                    historyToken = labelMappingDelete(
                                            labelMapping.id(),
                                            labelMapping.name(),
                                            labelName.get()
                                    );
                                }
                            }
                        }
                    }
                }
            }
        }

        return historyToken;
    }

    /**
     * If possible selects a formatter {@link HistoryToken}.
     */
    public final HistoryToken formatter() {
        HistoryToken historyToken;

        if (this instanceof SpreadsheetCellSelectHistoryToken || this instanceof SpreadsheetCellMenuHistoryToken) {
            final SpreadsheetCellHistoryToken cell = this.cast(SpreadsheetCellHistoryToken.class);

            historyToken = HistoryToken.cellFormatterSelect(
                    cell.id(),
                    cell.name(),
                    cell.anchoredSelection()
            );

        } else {
            historyToken = this;
        }

        return historyToken;
    }

    public final HistoryToken formula() {
        HistoryToken historyToken = this;

        if(this instanceof SpreadsheetCellHistoryToken) {
            final SpreadsheetCellHistoryToken cell = this.cast(SpreadsheetCellHistoryToken.class);

            historyToken = cellFormula(
                    cell.id(),
                    cell.name(),
                    cell.anchoredSelection()
            );
        }

        return historyToken;
    }

    /**
     * if possible creates a freeze.
     */
    public final HistoryToken freeze() {
        HistoryToken historyToken = this;

        if(this instanceof SpreadsheetCellHistoryToken) {
            final SpreadsheetCellHistoryToken cell = this.cast(SpreadsheetCellHistoryToken.class);

            historyToken = cellFreeze(
                    cell.id(),
                    cell.name(),
                    cell.anchoredSelection()
            );
        } else {
            if(this instanceof SpreadsheetColumnHistoryToken) {
                final SpreadsheetColumnHistoryToken column = this.cast(SpreadsheetColumnHistoryToken.class);

                historyToken = columnFreeze(
                        column.id(),
                        column.name(),
                        column.anchoredSelection()
                );
            } else {
                if(this instanceof SpreadsheetRowHistoryToken) {
                    final SpreadsheetRowHistoryToken row = this.cast(SpreadsheetRowHistoryToken.class);

                    historyToken = rowFreeze(
                            row.id(),
                            row.name(),
                            row.anchoredSelection()
                    );
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

    /**
     * if possible creates a insert after.
     */
    public final HistoryToken insertAfter(final OptionalInt count) {
        checkCount(count);

        HistoryToken historyToken = this;

        if(historyToken instanceof SpreadsheetColumnHistoryToken) {
            final SpreadsheetColumnHistoryToken column = historyToken.cast(SpreadsheetColumnHistoryToken.class);

            historyToken = columnInsertAfter(
                    column.id(),
                    column.name(),
                    column.anchoredSelection(),
                    count
            );
        } else {
            if(historyToken instanceof SpreadsheetRowHistoryToken) {
                final SpreadsheetRowHistoryToken row = historyToken.cast(SpreadsheetRowHistoryToken.class);

                historyToken = rowInsertAfter(
                        row.id(),
                        row.name(),
                        row.anchoredSelection(),
                        count
                );
            }
        }

        return historyToken;
    }

    /**
     * if possible creates a freeze.
     */
    public final HistoryToken insertBefore(final OptionalInt count) {
        checkCount(count);

        HistoryToken historyToken = this;

        if(historyToken instanceof SpreadsheetColumnHistoryToken) {
            final SpreadsheetColumnHistoryToken column = historyToken.cast(SpreadsheetColumnHistoryToken.class);

            historyToken = columnInsertBefore(
                    column.id(),
                    column.name(),
                    column.anchoredSelection(),
                    count
            );
        } else {
            if(historyToken instanceof SpreadsheetRowHistoryToken) {
                final SpreadsheetRowHistoryToken row = historyToken.cast(SpreadsheetRowHistoryToken.class);

                historyToken = rowInsertBefore(
                        row.id(),
                        row.name(),
                        row.anchoredSelection(),
                        count
                );
            }
        }

        return historyToken;
    }

    /**
     * Returns true for any metadata {@link SpreadsheetFormatterSelector} {@link HistoryToken}.
     */
    public final boolean isMetadataFormatter() {
        return this.isMetadataFormatterOrParser(SpreadsheetPatternKind::isFormatPattern);
    }

    /**
     * Returns true for any metadata {@link SpreadsheetParserSelector} {@link HistoryToken}.
     */
    public final boolean isMetadataParser() {
        return this.isMetadataFormatterOrParser(SpreadsheetPatternKind::isParsePattern);
    }

    private boolean isMetadataFormatterOrParser(final Function<SpreadsheetPatternKind, Boolean> kind) {
        return this instanceof SpreadsheetMetadataPropertySelectHistoryToken &&
                this.patternKind()
                        .map(kind)
                        .orElse(false);
    }

    /**
     * Sets or replaces the current {@link SpreadsheetLabelName} otherwise returns this.
     */
    public final HistoryToken setLabelName(final Optional<SpreadsheetLabelName> label) {
        Objects.requireNonNull(label, "label");
        HistoryToken token = this;

        if (this instanceof SpreadsheetNameHistoryToken) {
            final SpreadsheetNameHistoryToken spreadsheetNameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);
            token = labelMapping(
                    spreadsheetNameHistoryToken.id(),
                    spreadsheetNameHistoryToken.name(),
                    label
            );
        }

        return token;
    }

    public final HistoryToken menu() {
        HistoryToken historyToken = this;

        if (this instanceof SpreadsheetCellHistoryToken) {
            final SpreadsheetCellHistoryToken cell = this.cast(SpreadsheetCellHistoryToken.class);

            historyToken = cellMenu(
                    cell.id(),
                    cell.name(),
                    cell.anchoredSelection()
            );
        } else {
            if (this instanceof SpreadsheetColumnHistoryToken) {
                final SpreadsheetColumnHistoryToken column = this.cast(SpreadsheetColumnHistoryToken.class);

                historyToken = columnMenu(
                        column.id(),
                        column.name(),
                        column.anchoredSelection()
                );
            } else {
                if (this instanceof SpreadsheetRowHistoryToken) {
                    final SpreadsheetRowHistoryToken row = this.cast(SpreadsheetRowHistoryToken.class);

                    historyToken = rowMenu(
                            row.id(),
                            row.name(),
                            row.anchoredSelection()
                    );
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

        HistoryToken result = this;

        if (selection.isPresent()) {
            result = this.menu0(
                    selection.get(),
                    labelNameResolver
            );
        } else {
            if (this instanceof SpreadsheetAnchoredSelectionHistoryToken) {
                result = this.menu();
            }
        }

        return result;
    }

    private HistoryToken menu0(final SpreadsheetSelection selection,
                               final SpreadsheetLabelNameResolver labelNameResolver) {
        HistoryToken historyToken = null;

        if (this instanceof SpreadsheetNameHistoryToken) {
            SpreadsheetNameHistoryToken name = this.cast(SpreadsheetNameHistoryToken.class);

            final Optional<AnchoredSpreadsheetSelection> maybeAnchored = this.anchoredSelectionOrEmpty();
            if (maybeAnchored.isPresent()) {
                // right mouse happened over already selected selection...
                if (labelNameResolver.resolveIfLabel(maybeAnchored.get().selection())
                        .test(selection)) {
                    historyToken = this.menu();
                }
            }

            // right mouse click happened over a non selected cell/column/row
            if (null == historyToken) {
                historyToken = name.menu(selection);
            }
        }

        if(null == historyToken) {
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

                if (this instanceof SpreadsheetCellHistoryToken && selection.isCellReference()) {
                    anchoredMenuSelection = anchoredSelection.selection()
                            .testCell(selection.toCell()) ?
                            anchoredSelection :
                            selection.setDefaultAnchor();
                } else {
                    if (this instanceof SpreadsheetColumnHistoryToken && selection.isColumnReference()) {
                        anchoredMenuSelection = anchoredSelection.selection()
                                .testColumn(selection.toColumn()) ?
                                anchoredSelection :
                                selection.setDefaultAnchor();
                    } else {
                        if (this instanceof SpreadsheetRowHistoryToken && selection.isRowReference()) {
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

            if (menuSelection.isCellReferenceOrCellRangeReference()) {
                historyToken = cellMenu(
                        id,
                        name,
                        anchoredMenuSelection
                );
            } else {
                if (menuSelection.isColumnReferenceOrColumnRangeReference()) {
                    historyToken = columnMenu(
                            id,
                            name,
                            anchoredMenuSelection
                    );

                } else {
                    if (menuSelection.isRowReferenceOrRowRangeReference()) {
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

    /**
     * Returns if a possible a {@link HistoryToken} which shows the metadata panel
     */
    public final HistoryToken metadataShow() {
        HistoryToken token = this;

        if (this instanceof SpreadsheetNameHistoryToken) {
            final SpreadsheetNameHistoryToken spreadsheetNameHistoryToken = token.cast(SpreadsheetNameHistoryToken.class);
            token = HistoryToken.metadataSelect(
                    spreadsheetNameHistoryToken.id(),
                    spreadsheetNameHistoryToken.name()
            );
        }

        return token;
    }

    /**
     * Returns if a possible a {@link HistoryToken} which hides the metadata panel
     */
    public final HistoryToken metadataHide() {
        HistoryToken token = this;

        if (this instanceof SpreadsheetMetadataHistoryToken) {
            final SpreadsheetMetadataHistoryToken spreadsheetMetadataHistoryToken = token.cast(SpreadsheetMetadataHistoryToken.class);
            token = HistoryToken.spreadsheetSelect(
                    spreadsheetMetadataHistoryToken.id(),
                    spreadsheetMetadataHistoryToken.name()
            );
        }

        return token;
    }

    /**
     * Setter that tries to set a {@link SpreadsheetMetadataPropertyName} to the current {@link HistoryToken}.
     */
    public final HistoryToken setMetadataPropertyName(final SpreadsheetMetadataPropertyName<?> propertyName) {
        Objects.requireNonNull(propertyName, "propertyName");

        HistoryToken token = this;

        if (this instanceof SpreadsheetMetadataPropertyHistoryToken) {
            final SpreadsheetMetadataPropertyHistoryToken<?> metadataHistoryToken = this.cast(SpreadsheetMetadataPropertyHistoryToken.class);
            if (false == propertyName.equals(metadataHistoryToken.propertyName())) {
                token = HistoryToken.metadataPropertySelect(
                        metadataHistoryToken.id(),
                        metadataHistoryToken.name(),
                        propertyName
                );
            }
        } else {

            if (this instanceof SpreadsheetNameHistoryToken) {
                final SpreadsheetNameHistoryToken nameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);

                token = HistoryToken.metadataPropertySelect(
                        nameHistoryToken.id(),
                        nameHistoryToken.name(),
                        propertyName
                );
            }
        }

        return token;
    }

    /**
     * Sets or replaces the current {@link SpreadsheetName}.
     */
    public final HistoryToken setName(final SpreadsheetName name) {
        Objects.requireNonNull(name, "name");

        return this instanceof SpreadsheetNameHistoryToken ?
                this.cast(SpreadsheetNameHistoryToken.class).replaceName(name) :
                this;
    }

    public final OptionalInt offset() {
        final OptionalInt offset;

        if (this instanceof PluginListHistoryToken) {
            offset = this.cast(PluginListHistoryToken.class).offset;
        } else {
            if (this instanceof SpreadsheetListHistoryToken) {
                offset = this.cast(SpreadsheetListHistoryToken.class).offset;
            } else {
                offset = OptionalInt.empty();
            }
        }

        return offset;
    }

    /**
     * Would be setter that tries to replace the {@link #offset()} with new value.
     */
    public final HistoryToken setOffset(final OptionalInt offset) {
        checkOffset(offset);

        final HistoryToken with;

        if (this.offset().equals(offset)) {
            with = this;
        } else {
            if (this instanceof PluginListReloadHistoryToken) {
                with = pluginListReload(
                        offset,
                        this.count()
                );
            } else {
                if (this instanceof PluginListSelectHistoryToken) {
                    with = pluginListSelect(
                            offset,
                            this.count()
                    );
                } else {
                    if (this instanceof SpreadsheetListReloadHistoryToken) {
                        final SpreadsheetListReloadHistoryToken list = this.cast(SpreadsheetListReloadHistoryToken.class);

                        with = spreadsheetListReload(
                                offset,
                                this.count()
                        );
                    } else {
                        if (this instanceof SpreadsheetListSelectHistoryToken) {
                            with = spreadsheetListSelect(
                                    offset,
                                    this.count()
                            );
                        } else {
                            with = this;
                        }
                    }
                }
            }
        }

        return with;
    }

    static OptionalInt checkOffset(final OptionalInt offset) {
        Objects.requireNonNull(offset, "offset");

        offset.ifPresent(value -> {
            if (value < 0) {
                throw new IllegalArgumentException("Invalid offset < 0 got " + value);
            }
        });

        return offset;
    }

    // HasSpreadsheetPatternKind........................................................................................

    @Override
    public final Optional<SpreadsheetPatternKind> patternKind() {
        final Optional<SpreadsheetPatternKind> kind;

        if (this instanceof SpreadsheetMetadataPropertyHistoryToken) {
            kind = this.cast(SpreadsheetMetadataPropertyHistoryToken.class)
                    .patternKind0();
        } else {
            kind = Optional.empty();
        }

        return kind;
    }

    /**
     * Tries to clear any present {@link SpreadsheetPatternKind}.
     */
    public final HistoryToken clearPatternKind() {
        return this.setPatternKind(Optional.empty());
    }

    /**
     * if possible creates a {@link HistoryToken} with the {@link SpreadsheetPatternKind}.
     */
    public final HistoryToken setPatternKind(final Optional<SpreadsheetPatternKind> kind) {
        Objects.requireNonNull(kind, "kind");

        return this.patternKind().equals(kind) ?
                this :
                this.setIfSpreadsheetNameHistoryTokenWithValue(
                        SpreadsheetNameHistoryToken::replacePatternKind,
                        kind
                );
    }

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

    public final HistoryToken reload() {
        HistoryToken token = this;

        if (this instanceof PluginListHistoryToken) {
            final PluginListHistoryToken pluginListHistoryToken = this.cast(PluginListHistoryToken.class);
            token = pluginListReload(
                    pluginListHistoryToken.offset(),
                    pluginListHistoryToken.count()
            );
        } else {
            if (this instanceof SpreadsheetListHistoryToken) {
                final SpreadsheetListHistoryToken spreadsheetListHistoryToken = this.cast(SpreadsheetListHistoryToken.class);
                token = spreadsheetListReload(
                        spreadsheetListHistoryToken.offset(),
                        spreadsheetListHistoryToken.count()
                );

            } else {
                if (this instanceof SpreadsheetNameHistoryToken) {
                    final SpreadsheetNameHistoryToken spreadsheetNameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);
                    token = spreadsheetReload(
                            spreadsheetNameHistoryToken.id(),
                            spreadsheetNameHistoryToken.name()
                    );
                }
            }
        }

        return token;
    }

    public final HistoryToken rename() {
        HistoryToken token = this;

        if (this instanceof SpreadsheetNameHistoryToken) {
            final SpreadsheetNameHistoryToken spreadsheetNameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);
            token = spreadsheetRenameSelect(
                    spreadsheetNameHistoryToken.id(),
                    spreadsheetNameHistoryToken.name()
            );
        }

        return token;
    }

    /**
     * Saves an empty {@link String} which is equivalent to deleting the property.
     */
    public final HistoryToken clearSave() {
        return this.save("");
    }

    /**
     * if possible creates a save, otherwise returns this.
     */
    public final HistoryToken save(final Optional<?> value) {
        Objects.requireNonNull(value, "value");

        String stringValue = "";

        if (value.isPresent()) {
            final Object valueNotNull = value.get();
            if (valueNotNull instanceof HasText) {
                final HasText hasText = (HasText) valueNotNull;
                stringValue = hasText.text();
            } else {
                if (valueNotNull instanceof Enum) {
                    final Enum<?> enumm = (Enum<?>) valueNotNull;
                    stringValue = enumm.name();
                } else {
                    stringValue = valueNotNull.toString();
                }
            }
        }

        return this.save(stringValue);
    }

    /**
     * if possible creates a save, otherwise returns this.
     */
    public final HistoryToken save(final String text) {
        Objects.requireNonNull(text, "text");

        return this.setIfSpreadsheetIdHistoryToken(
                SpreadsheetIdHistoryToken::save0,
                text
        );
    }

    /**
     * if possible creates a sort edit, otherwise returns this.
     */
    public final HistoryToken setSortEdit(final String comparatorNames) {
        Objects.requireNonNull(comparatorNames, "comparatorNames");

        return this.setIfSpreadsheetAnchoredSelectionHistoryTokenWithValue(
                SpreadsheetAnchoredSelectionHistoryToken::setSortEdit0,
                comparatorNames
        );
    }

    /**
     * if possible creates a sort edit, otherwise returns this.
     */
    public final HistoryToken setSortSave(final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparatorNames) {
        Objects.requireNonNull(comparatorNames, "comparatorNames");

        return this.setIfSpreadsheetAnchoredSelectionHistoryTokenWithValue(
                SpreadsheetAnchoredSelectionHistoryToken::setSortSave0,
                comparatorNames
        );
    }

    public final Optional<SpreadsheetFormatterSelector> spreadsheetFormatterSelector() {
        Optional<SpreadsheetFormatterSelector> spreadsheetFormatterSelector = Optional.empty();

        if (this instanceof SpreadsheetCellFormatterHistoryToken) {
            spreadsheetFormatterSelector = this.cast(SpreadsheetCellFormatterHistoryToken.class)
                    .spreadsheetFormatterSelector;
        } else {
            if (this instanceof SpreadsheetMetadataPropertySaveHistoryToken) {
                final SpreadsheetMetadataPropertySaveHistoryToken<SpreadsheetFormatterSelector> metadataSave = this.cast(SpreadsheetMetadataPropertySaveHistoryToken.class);
                final SpreadsheetMetadataPropertyName<?> propertyName = metadataSave.propertyName();
                spreadsheetFormatterSelector = propertyName.isSpreadsheetFormatterSelector() ?
                        metadataSave.propertyValue() :
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
                final SpreadsheetMetadataPropertyName<?> propertyName = metadataSave.propertyName();
                spreadsheetParserSelector = propertyName.isSpreadsheetParserSelector() ?
                        metadataSave.propertyValue() :
                        Optional.empty();
            }
        }

        return spreadsheetParserSelector;
    }

    /**
     * Factory that creates a {@link SpreadsheetNameHistoryToken} with the given {@link TextStylePropertyName} property name.
     */
    public final HistoryToken style(final TextStylePropertyName<?> propertyName) {
        Objects.requireNonNull(propertyName, "propertyName");

        HistoryToken historyToken = this;

        if (this instanceof SpreadsheetSelectHistoryToken || this instanceof SpreadsheetMetadataHistoryToken) {
            final SpreadsheetNameHistoryToken name = this.cast(SpreadsheetNameHistoryToken.class);

            historyToken = metadataPropertyStyle(
                    name.id(),
                    name.name(),
                    propertyName
            );

            if (historyToken.equals(this)) {
                historyToken = this;
            }
        } else {
            if (this instanceof SpreadsheetCellHistoryToken) {
                final SpreadsheetCellHistoryToken cell = this.cast(SpreadsheetCellHistoryToken.class);

                historyToken = cellStyle(
                        cell.id(),
                        cell.name(),
                        cell.anchoredSelection(),
                        propertyName
                );
            }

            if (historyToken.equals(this)) {
                historyToken = this;
            }
        }

        return historyToken;
    }

    public final HistoryToken toolbar() {
        HistoryToken historyToken = this;

        if (this instanceof SpreadsheetCellFormatterSelectHistoryToken) {
            final SpreadsheetCellFormatterSelectHistoryToken formatter = this.cast(SpreadsheetCellFormatterSelectHistoryToken.class);
            historyToken = cellFormatterUnselect(
                    formatter.id(),
                    formatter.name(),
                    formatter.anchoredSelection()
            );
        }

        if (this instanceof SpreadsheetCellParserSelectHistoryToken) {
            final SpreadsheetCellParserSelectHistoryToken parser = this.cast(SpreadsheetCellParserSelectHistoryToken.class);
            historyToken = cellParserUnselect(
                    parser.id(),
                    parser.name(),
                    parser.anchoredSelection()
            );
        }

        return historyToken;
    }

    /**
     * if possible creates a unfreeze.
     */
    public final HistoryToken unfreeze() {
        HistoryToken historyToken = this;

        if(this instanceof SpreadsheetCellHistoryToken) {
            final SpreadsheetCellHistoryToken cell = this.cast(SpreadsheetCellHistoryToken.class);

            historyToken = cellUnfreeze(
                    cell.id(),
                    cell.name(),
                    cell.anchoredSelection()
            );
        } else {
            if(this instanceof SpreadsheetColumnHistoryToken) {
                final SpreadsheetColumnHistoryToken column = this.cast(SpreadsheetColumnHistoryToken.class);

                historyToken = columnUnfreeze(
                        column.id(),
                        column.name(),
                        column.anchoredSelection()
                );
            } else {
                if(this instanceof SpreadsheetRowHistoryToken) {
                    final SpreadsheetRowHistoryToken row = this.cast(SpreadsheetRowHistoryToken.class);

                    historyToken = rowUnfreeze(
                            row.id(),
                            row.name(),
                            row.anchoredSelection()
                    );
                }
            }
        }

        return historyToken;
    }

    /**
     * Tries to create an unfreeze token or {@link Optional#empty()} because the {@link SpreadsheetSelection} is invalid.
     */
    public final Optional<HistoryToken> unfreezeOrEmpty() {
        HistoryToken token;

        try {
            token = this.unfreeze();
        } catch (final RuntimeException ignored) {
            token = null;
        }

        return Optional.ofNullable(token);
    }

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

    private <T> HistoryToken setIfSpreadsheetIdHistoryToken(final BiFunction<SpreadsheetIdHistoryToken, T, HistoryToken> setter,
                                                            final T value) {
        HistoryToken token = this;

        if (this instanceof SpreadsheetIdHistoryToken) {
            token = setter.apply(
                    this.cast(SpreadsheetIdHistoryToken.class),
                    value
            );

            if (token.equals(this)) {
                token = this;
            }
        }

        return token;
    }

    private HistoryToken setIfSpreadsheetNameHistoryToken(final Function<SpreadsheetNameHistoryToken, HistoryToken> setter) {
        HistoryToken token = this;

        if (this instanceof SpreadsheetNameHistoryToken) {
            token = setter.apply(
                    this.cast(SpreadsheetNameHistoryToken.class)
            );

            if (token.equals(this)) {
                token = this;
            }
        }

        return token;
    }

    private <T> HistoryToken setIfSpreadsheetNameHistoryTokenWithValue(final BiFunction<SpreadsheetNameHistoryToken, T, HistoryToken> setter,
                                                                       final T value) {
        HistoryToken token = this;

        if (this instanceof SpreadsheetNameHistoryToken) {
            token = setter.apply(
                    this.cast(SpreadsheetNameHistoryToken.class),
                    value
            );

            if (token.equals(this)) {
                token = this;
            }
        }

        return token;
    }

    private <T> HistoryToken setIfSpreadsheetAnchoredSelectionHistoryTokenWithValue(final BiFunction<SpreadsheetAnchoredSelectionHistoryToken, T, HistoryToken> setter,
                                                                                    final T value) {
        HistoryToken token = this;

        if (this instanceof SpreadsheetAnchoredSelectionHistoryToken) {
            token = setter.apply(
                    this.cast(SpreadsheetAnchoredSelectionHistoryToken.class),
                    value
            );

            if (token.equals(this)) {
                token = this;
            }
        }

        return token;
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
     * Factory that creates a {@link HistoryToken} changing the {@link AnchoredSpreadsheetSelection} ui and clearing any action.
     */
    public final HistoryToken setAnchoredSelection(final Optional<AnchoredSpreadsheetSelection> anchoredSelection) {
        Objects.requireNonNull(anchoredSelection, "anchoredSelection");

        return this.anchoredSelectionOrEmpty().equals(anchoredSelection) ?
                this :
                this.setDifferentAnchoredSelection(anchoredSelection);
    }

    private HistoryToken setDifferentAnchoredSelection(final Optional<AnchoredSpreadsheetSelection> maybeAnchoredSelection) {
        HistoryToken token = this;

        if (maybeAnchoredSelection.isPresent()) {
            final AnchoredSpreadsheetSelection anchoredSelection = maybeAnchoredSelection.get();

            if (this instanceof SpreadsheetNameHistoryToken) {
                final SpreadsheetNameHistoryToken spreadsheetNameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);
                token = HistoryTokenSelectionSpreadsheetSelectionVisitor.selectionToken(
                        spreadsheetNameHistoryToken,
                        anchoredSelection
                );
            }
        } else {
            if (this instanceof SpreadsheetNameHistoryToken) {
                final SpreadsheetNameHistoryToken spreadsheetNameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);
                token = spreadsheetSelect(
                        spreadsheetNameHistoryToken.id(),
                        spreadsheetNameHistoryToken.name()
                );
            }
        }

        return token;
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

    /**
     * Type safe cast to the given {@link Class literal}.
     */
    public final <T extends HistoryToken> T cast(final Class<T> cast) {
        return HistoryTokenCastGwt.cast(
                this,
                cast
        );
    }

    // onHistoryTokenChange.............................................................................................

    /**
     * Fired whenever a new history token change happens.
     */
    abstract public void onHistoryTokenChange(final HistoryToken previous,
                                              final AppContext context);

    // UI...............................................................................................................

    /**
     * Creates a {@link SpreadsheetContextMenuItem} with the given id & text and this {@link HistoryToken}.
     */
    public final SpreadsheetContextMenuItem contextMenuItem(final String id,
                                                            final String text) {
        return SpreadsheetContextMenuItem.with(
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
                                this.save(saveText)
                        )
                );
    }

    // UrlFragment......................................................................................................

    static UrlFragment countAndOffsetUrlFragment(final OptionalInt offset,
                                                 final OptionalInt count,
                                                 final UrlFragment suffix) {
        UrlFragment urlFragment = UrlFragment.EMPTY;

        boolean addStar = true;

        if (offset.isPresent()) {
            urlFragment = urlFragment.appendSlashThen(WILDCARD)
                    .appendSlashThen(OFFSET)
                    .appendSlashThen(
                            UrlFragment.with(
                                    String.valueOf(offset.getAsInt())
                            )
                    );
            addStar = false;
        }

        if (count.isPresent()) {
            if (addStar) {
                urlFragment = urlFragment.appendSlashThen(WILDCARD);
                addStar = false;
            }

            urlFragment = urlFragment.appendSlashThen(COUNT)
                    .appendSlashThen(
                            UrlFragment.with(
                                    String.valueOf(count.getAsInt())
                            )
                    );
        }

        if(false == suffix.isEmpty()) {
            if (addStar) {
                urlFragment = urlFragment.appendSlashThen(WILDCARD);
            }

            urlFragment = urlFragment.appendSlashThen(suffix);
        }

        return urlFragment;
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

    private static UrlFragment saveUrlFragmentValue(final Object value) {
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
                                                String.valueOf(value)
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
                (other != null && this.getClass().equals(other.getClass()) && this.equals0((HistoryToken) other));
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
