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
import walkingkooka.net.HasUrlFragment;
import walkingkooka.net.UrlFragment;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNames;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.clipboard.SpreadsheetCellClipboardKind;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetCellFind;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIds;
import walkingkooka.spreadsheet.dominokit.ui.contextmenu.SpreadsheetContextMenuItem;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.ui.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.format.pattern.HasSpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.HasSpreadsheetPatternKind;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportAnchor;
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

import java.util.List;
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
 *     #/1/Untitled/cell/D4/format-pattern/date/save/DD/MM/YYYY
 * </pre>
 * This architecture makes it possible to program a macro system, manipulating a browser by simply sending new hash tokens.
 * <br>
 * The {@link #onHistoryTokenChange(HistoryToken, AppContext)} is implemented by some actions such as {@link SpreadsheetCellFormulaSaveHistoryToken},
 * which when it appears in the current history token hash will save a new formula text for the cell belonging to the spreadsheet id.
 * This is done by calling a REST end-point on the server using {@link walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher} which uses
 * the browser's fetch object with a JSON payload.
 * <br>
 * Comprehensive testing for parsing and more are available under the corresponding test history package.
 */
public abstract class HistoryToken implements HasUrlFragment,
        HasSpreadsheetPattern,
        HasSpreadsheetPatternKind {

    private final static int MAX_LENGTH = 8192;

    public final static String DISABLE = "disable";

    public final static String ENABLE = "enable";

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
     * {@see SpreadsheetCellFormulaSelectHistoryToken}
     */
    public static SpreadsheetCellFormulaSelectHistoryToken formula(final SpreadsheetId id,
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
    public static SpreadsheetCellFormulaSaveHistoryToken formulaSave(final SpreadsheetId id,
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
                                                           final SpreadsheetCellFind find) {
        return SpreadsheetCellFindHistoryToken.with(
                id,
                name,
                anchoredSelection,
                find
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
     * {@see SpreadsheetCellHighlightSaveHistoryToken}
     */
    public static SpreadsheetCellHighlightSaveHistoryToken cellHighlightSave(final SpreadsheetId id,
                                                                             final SpreadsheetName name,
                                                                             final AnchoredSpreadsheetSelection anchoredSelection,
                                                                             final boolean value) {
        return SpreadsheetCellHighlightSaveHistoryToken.with(
                id,
                name,
                anchoredSelection,
                value
        );
    }

    /**
     * {@see SpreadsheetCellHighlightSelectHistoryToken}
     */
    public static SpreadsheetCellHighlightSelectHistoryToken cellHighlightSelect(final SpreadsheetId id,
                                                                                 final SpreadsheetName name,
                                                                                 final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellHighlightSelectHistoryToken.with(
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
     * {@see SpreadsheetCellPatternSelectHistoryToken}
     */
    public static SpreadsheetCellPatternSelectHistoryToken cellPattern(final SpreadsheetId id,
                                                                       final SpreadsheetName name,
                                                                       final AnchoredSpreadsheetSelection anchoredSelection,
                                                                       final SpreadsheetPatternKind patternKind) {
        return SpreadsheetCellPatternSelectHistoryToken.with(
                id,
                name,
                anchoredSelection,
                patternKind
        );
    }

    /**
     * {@see SpreadsheetCellPatternSaveHistoryToken}
     */
    public static SpreadsheetCellPatternSaveHistoryToken cellPatternSave(final SpreadsheetId id,
                                                                         final SpreadsheetName name,
                                                                         final AnchoredSpreadsheetSelection anchoredSelection,
                                                                         final SpreadsheetPatternKind patternKind,
                                                                         final Optional<SpreadsheetPattern> pattern) {
        return SpreadsheetCellPatternSaveHistoryToken.with(
                id,
                name,
                anchoredSelection,
                patternKind,
                pattern
        );
    }

    /**
     * {@see SpreadsheetCellPatternFormatHistoryToken}
     */
    public static SpreadsheetCellPatternFormatOrParseHistoryToken cellFormatPattern(final SpreadsheetId id,
                                                                                    final SpreadsheetName name,
                                                                                    final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellPatternFormatHistoryToken.with(
                id,
                name,
                anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetCellPatternParseHistoryToken}
     */
    public static SpreadsheetCellPatternFormatOrParseHistoryToken cellParsePattern(final SpreadsheetId id,
                                                                                   final SpreadsheetName name,
                                                                                   final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellPatternParseHistoryToken.with(
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
     * {@see SpreadsheetCellSaveFormatPatternHistoryToken}
     */
    public static SpreadsheetCellSaveFormatPatternHistoryToken cellSaveFormatPattern(final SpreadsheetId id,
                                                                                     final SpreadsheetName name,
                                                                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                                                                     final Map<SpreadsheetCellReference, Optional<SpreadsheetFormatPattern>> value) {
        return SpreadsheetCellSaveFormatPatternHistoryToken.with(
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
     * {@see SpreadsheetCellSaveParsePatternHistoryToken}
     */
    public static SpreadsheetCellSaveParsePatternHistoryToken cellSaveParsePattern(final SpreadsheetId id,
                                                                                   final SpreadsheetName name,
                                                                                   final AnchoredSpreadsheetSelection anchoredSelection,
                                                                                   final Map<SpreadsheetCellReference, Optional<SpreadsheetParsePattern>> value) {
        return SpreadsheetCellSaveParsePatternHistoryToken.with(
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
                                                                   final List<SpreadsheetColumnOrRowSpreadsheetComparatorNames> comparatorNames) {
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

    /**
     * {@see SpreadsheetCreateHistoryToken}
     */
    public static SpreadsheetCreateHistoryToken spreadsheetCreate() {
        return SpreadsheetCreateHistoryToken.with();
    }

    /**
     * {@see SpreadsheetListReloadHistoryToken}
     */
    public static SpreadsheetListHistoryToken spreadsheetListReload(final OptionalInt from,
                                                                    final OptionalInt count) {
        return SpreadsheetListReloadHistoryToken.with(
                from,
                count
        );
    }
    
    /**
     * {@see SpreadsheetListSelectHistoryToken}
     */
    public static SpreadsheetListHistoryToken spreadsheetListSelect(final OptionalInt from,
                                                                    final OptionalInt count) {
        return SpreadsheetListSelectHistoryToken.with(
                from,
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

    /**
     * Parses the given {@link UrlFragment} if matching fails a {@link UnknownHistoryToken} is returned.
     */
    public static HistoryToken parse(final UrlFragment fragment) {
        Objects.requireNonNull(fragment, "fragment");

        HistoryToken token = null;

        final TextCursor cursor = TextCursors.charSequence(fragment.value());
        if (cursor.isEmpty()) {
            token = SPREADSHEET_LIST_SELECT_HISTORY_TOKEN;
        } else {
            try {
                final Optional<String> maybeComponent = parseComponent(cursor);
                if (maybeComponent.isPresent()) {
                    final String component = maybeComponent.get();
                    switch (component) {
                        case "":
                            token = SPREADSHEET_LIST_SELECT_HISTORY_TOKEN;
                            break;
                        case "count":
                            token = SPREADSHEET_LIST_SELECT_HISTORY_TOKEN;
                            token = token.setCount(
                                    parseCount(cursor)
                            ).parse(cursor);
                            break;
                        case "create":
                            token = HistoryToken.spreadsheetCreate()
                                    .parse(cursor);
                            break;
                        case "delete":
                            token = parseDelete(cursor);
                            break;
                        case "from":
                            token = SPREADSHEET_LIST_SELECT_HISTORY_TOKEN;
                            token = token.cast(SpreadsheetListHistoryToken.class)
                                    .setFrom(
                                            parseOptionalInt(cursor)
                                    ).parse(cursor);
                            break;
                        case "reload":
                            token = SPREADSHEET_LIST_RELOAD_HISTORY_TOKEN;
                            token = token.parse(cursor);
                            break;
                        case "rename":
                            token = SPREADSHEET_LIST_SELECT_HISTORY_TOKEN;
                            token = parseRename(cursor);
                            break;
                        default:
                            token = spreadsheetLoad(
                                    SpreadsheetId.parse(component)
                            ).parse(cursor);
                    }
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

    private final static SpreadsheetListHistoryToken SPREADSHEET_LIST_RELOAD_HISTORY_TOKEN = HistoryToken.spreadsheetListReload(
            OptionalInt.empty(), // from
            OptionalInt.empty() // count
    );

    private final static SpreadsheetListHistoryToken SPREADSHEET_LIST_SELECT_HISTORY_TOKEN = HistoryToken.spreadsheetListSelect(
            OptionalInt.empty(), // from
            OptionalInt.empty() // count
    );

    /**
     * Consumes all remaining text into a {@link String}.
     */
    static String parseAll(final TextCursor cursor) {
        final TextCursorSavePoint save = cursor.save();
        cursor.end();
        return save.textBetween()
                .toString()
                .substring(1); // drops assumed leading slash

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

        if (this instanceof SpreadsheetCellFindHistoryToken) {
            final SpreadsheetCellFindHistoryToken find = (SpreadsheetCellFindHistoryToken) this;
            closed = find.clearAction();
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
        if (this instanceof SpreadsheetCellPatternHistoryToken) {
            final SpreadsheetCellPatternHistoryToken cellPattern = (SpreadsheetCellPatternHistoryToken) this;

            final SpreadsheetId id = cellPattern.id();
            final SpreadsheetName name = cellPattern.name();
            final AnchoredSpreadsheetSelection anchoredSelection = cellPattern.anchoredSelection();
            final Optional<SpreadsheetPatternKind> patternKind = cellPattern.patternKind();

            closed = patternKind.isPresent() ?
                    patternKind.get()
                            .isFormatPattern() ?
                            cellFormatPattern(
                                    id,
                                    name,
                                    anchoredSelection
                            ) :
                            cellParsePattern(
                                    id,
                                    name,
                                    anchoredSelection
                            ) :
                    selection(
                            id,
                            name,
                            anchoredSelection
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
     * Sets or replaces the current {@link SpreadsheetViewportAnchor} otherwise returns this.
     */
    public final HistoryToken setAnchor(final SpreadsheetViewportAnchor anchor) {
        Objects.requireNonNull(anchor, "anchor");
        HistoryToken token = this;

        if (this instanceof SpreadsheetAnchoredSelectionHistoryToken) {
            final SpreadsheetAnchoredSelectionHistoryToken spreadsheetAnchoredSelectionHistoryToken = this.cast(SpreadsheetAnchoredSelectionHistoryToken.class);

            try {
                token = HistoryToken.selection(
                        spreadsheetAnchoredSelectionHistoryToken.id(),
                        spreadsheetAnchoredSelectionHistoryToken.name(),
                        spreadsheetAnchoredSelectionHistoryToken.anchoredSelection()
                                .selection()
                                .setAnchor(anchor)
                );
            } catch (final IllegalArgumentException ignore) {
                // nop
            }
        }

        return token;
    }

    /**
     * Sets or replaces the current {@link SpreadsheetViewportAnchor} otherwise returns this.
     */
    public final HistoryToken setCell(final SpreadsheetSelection selection) {
        Objects.requireNonNull(selection, "selection");
        HistoryToken token = this;

        if (this instanceof SpreadsheetNameHistoryToken) {
            final SpreadsheetNameHistoryToken spreadsheetNameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);
            token = cell(
                    spreadsheetNameHistoryToken.id(),
                    spreadsheetNameHistoryToken.name(),
                    selection.setDefaultAnchor()
            );
        }

        return token;
    }


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
     * Returns true for any cell format pattern {@link HistoryToken}.
     */
    public final boolean isCellFormatPattern() {
        return this.isCellPattern(SpreadsheetPatternKind::isFormatPattern);
    }

    /**
     * Returns true for any cell parse pattern {@link HistoryToken}.
     */
    public final boolean isCellParsePattern() {
        return this.isCellPattern(SpreadsheetPatternKind::isParsePattern);
    }

    private boolean isCellPattern(final Function<SpreadsheetPatternKind, Boolean> kind) {
        return this instanceof SpreadsheetCellPatternHistoryToken &&
                this.cast(HasSpreadsheetPatternKind.class)
                        .patternKind()
                        .map(kind)
                        .orElse(false);
    }

    /**
     * if possible creates a clear.
     */
    public final HistoryToken setClear() {
        return this.setIfSpreadsheetNameHistoryToken(
                SpreadsheetNameHistoryToken::setClear0
        );
    }

    /**
     * Sets or replaces the current {@link SpreadsheetViewportAnchor} otherwise returns this.
     */
    public final HistoryToken setColumn(final SpreadsheetSelection selection) {
        Objects.requireNonNull(selection, "selection");
        HistoryToken token = this;

        if (this instanceof SpreadsheetNameHistoryToken) {
            final SpreadsheetNameHistoryToken spreadsheetNameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);
            token = column(
                    spreadsheetNameHistoryToken.id(),
                    spreadsheetNameHistoryToken.name(),
                    selection.setDefaultAnchor()
            );
        }

        return token;
    }

    public final OptionalInt count() {
        final OptionalInt count;

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
            if (this instanceof SpreadsheetListReloadHistoryToken) {
                final SpreadsheetListReloadHistoryToken list = this.cast(SpreadsheetListReloadHistoryToken.class);

                with = spreadsheetListReload(
                        list.from(),
                        count
                );
            } else {
                if (this instanceof SpreadsheetListSelectHistoryToken) {
                    final SpreadsheetListSelectHistoryToken list = this.cast(SpreadsheetListSelectHistoryToken.class);

                    with = spreadsheetListSelect(
                            list.from(),
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

        return with;
    }

    static OptionalInt parseCount(final TextCursor cursor) {
        return parseOptionalInt(cursor);
    }

    final static OptionalInt checkCount(final OptionalInt count) {
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
    public final HistoryToken setDelete() {
        return this.setIfSpreadsheetNameHistoryToken(
                SpreadsheetNameHistoryToken::setDelete0
        );
    }

    /**
     * Creates a {@link SpreadsheetCellFindHistoryToken} with the given parameters.
     */
    public final HistoryToken setFind(final SpreadsheetCellFind find) {
        HistoryToken historyToken = this;

        if (this instanceof SpreadsheetCellHistoryToken) {
            if (this instanceof SpreadsheetCellFindHistoryToken) {
                final SpreadsheetCellFindHistoryToken findHistoryToken = (SpreadsheetCellFindHistoryToken) this;
                historyToken = findHistoryToken.setFind0(find);
            } else {
                final SpreadsheetCellHistoryToken cell = (SpreadsheetCellHistoryToken) this;
                historyToken = cellFind(
                        cell.id(),
                        cell.name(),
                        cell.anchoredSelection(),
                        find
                );
            }

        }

        return historyToken;
    }

    /**
     * Creates a formula where possible otherwise returns this.
     */
    public abstract HistoryToken setFormula();

    /**
     * if possible creates a freeze.
     */
    public final HistoryToken setFreeze() {
        return this.setIfSpreadsheetNameHistoryToken(
                SpreadsheetNameHistoryToken::setFreeze0
        );
    }

    /**
     * Tries to create a freeze token or {@link Optional#empty()} because the {@link SpreadsheetSelection} is invalid.
     */
    public final Optional<HistoryToken> freezeOrEmpty() {
        HistoryToken token;

        try {
            token = this.setFreeze();
        } catch (final RuntimeException ignored) {
            token = null;
        }

        return Optional.ofNullable(token);
    }

    /**
     * Creates a {@link SpreadsheetCellHighlightHistoryToken}.
     */
    public final HistoryToken setHighlight() {
        HistoryToken historyToken = this;

        if (this instanceof SpreadsheetCellHistoryToken) {
            if (false == this instanceof SpreadsheetCellHighlightHistoryToken) {
                final SpreadsheetCellHistoryToken cellHistoryToken = (SpreadsheetCellHistoryToken) this;
                historyToken = cellHighlightSelect(
                        cellHistoryToken.id(),
                        cellHistoryToken.name(),
                        cellHistoryToken.anchoredSelection()
                );
            }
        }

        return historyToken;
    }

    /**
     * if possible creates a insert after.
     */
    public final HistoryToken setInsertAfter(final OptionalInt count) {
        return this.setIfSpreadsheetNameHistoryToken(
                (nht) -> nht.setInsertAfter0(count)
        );
    }

    /**
     * if possible creates a freeze.
     */
    public final HistoryToken setInsertBefore(final OptionalInt count) {
        return this.setIfSpreadsheetNameHistoryToken(
                (nht) -> nht.setInsertBefore0(count)
        );
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
     * Returns true for any metadata format pattern {@link HistoryToken}.
     */
    public final boolean isMetadataFormatPattern() {
        return this.isMetadataPattern(SpreadsheetPatternKind::isFormatPattern);
    }

    /**
     * Returns true for any metadata parse pattern {@link HistoryToken}.
     */
    public final boolean isMetadataParsePattern() {
        return this.isMetadataPattern(SpreadsheetPatternKind::isParsePattern);
    }

    private boolean isMetadataPattern(final Function<SpreadsheetPatternKind, Boolean> kind) {
        return this instanceof SpreadsheetMetadataPropertySelectHistoryToken &&
                this.cast(HasSpreadsheetPatternKind.class)
                        .patternKind()
                        .map(kind)
                        .orElse(false);
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
     * Sets or replaces the current {@link SpreadsheetName}.
     */
    public final HistoryToken setName(final SpreadsheetName name) {
        Objects.requireNonNull(name, "name");

        return this instanceof SpreadsheetNameHistoryToken ?
                this.cast(SpreadsheetNameHistoryToken.class).replaceName(name) :
                this;
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

    /**
     * Creates a {@link HistoryToken} with the given {@link SpreadsheetSelection}.
     */
    public final HistoryToken setMenu(final Optional<SpreadsheetSelection> selection) {
        Objects.requireNonNull(selection, "selection");

        HistoryToken result = this;

        if (selection.isPresent()) {
            result = this.setMenu0(selection.get());
        } else {
            if (this instanceof SpreadsheetAnchoredSelectionHistoryToken) {
                result = this.cast(SpreadsheetAnchoredSelectionHistoryToken.class)
                        .setMenu1();
            }
        }

        return result;
    }

    private HistoryToken setMenu0(final SpreadsheetSelection selection) {
        Objects.requireNonNull(selection, "selection");

        HistoryToken menu = null;

        if (this instanceof SpreadsheetNameHistoryToken) {
            SpreadsheetNameHistoryToken spreadsheetNameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);

            final Optional<AnchoredSpreadsheetSelection> maybeAnchored = this.anchoredSelectionOrEmpty();
            if (maybeAnchored.isPresent()) {
                final AnchoredSpreadsheetSelection anchored = maybeAnchored.get();

                // right mouse happened over already selected selection...
                if (anchored.selection().test(selection)) {
                    menu = this.cast(SpreadsheetAnchoredSelectionHistoryToken.class)
                            .setMenu1();
                }
            }

            // right mouse click happened over a non selected cell/column/row
            if (null == menu) {
                menu = spreadsheetNameHistoryToken.setMenu2(selection);
            }
        } else {
            menu = this; // id missing just return this and ignore context setMenu1.
        }

        return menu;
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

    @Override
    public final Optional<SpreadsheetPattern> pattern() {
        final Optional<SpreadsheetPattern> pattern;

        if (this instanceof SpreadsheetCellPatternSaveHistoryToken) {
            pattern = this.cast(SpreadsheetCellPatternSaveHistoryToken.class)
                    .pattern0();
        } else {
            if (this instanceof SpreadsheetMetadataPropertySaveHistoryToken) {
                pattern = this.cast(SpreadsheetMetadataPropertySaveHistoryToken.class)
                        .pattern0();
            } else {
                pattern = Optional.empty();
            }
        }

        return pattern;
    }

    /**
     * if possible creates a {@link HistoryToken} with the {@link SpreadsheetPattern}.
     */
    public final HistoryToken setPattern(final SpreadsheetPattern pattern) {
        Objects.requireNonNull(pattern, "pattern");

        return this.setIfSpreadsheetNameHistoryToken(
                (n) -> n.setPatternKind(
                        Optional.of(pattern.kind())
                ).setSave(
                        pattern.text()
                )
        );
    }

    // HasSpreadsheetPatternKind........................................................................................

    @Override
    public final Optional<SpreadsheetPatternKind> patternKind() {
        final Optional<SpreadsheetPatternKind> kind;

        if (this instanceof SpreadsheetCellPatternHistoryToken) {
            kind = this.cast(SpreadsheetCellPatternHistoryToken.class)
                    .patternKind0();
        } else {
            if (this instanceof SpreadsheetMetadataPropertyHistoryToken) {
                kind = this.cast(SpreadsheetMetadataPropertyHistoryToken.class)
                        .patternKind0();
            } else {
                kind = Optional.empty();
            }
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

    public final HistoryToken setReload() {
        HistoryToken token = this;

        if (this instanceof SpreadsheetListHistoryToken) {
            final SpreadsheetListHistoryToken spreadsheetListHistoryToken = this.cast(SpreadsheetListHistoryToken.class);
            token = spreadsheetListReload(
                    spreadsheetListHistoryToken.from(),
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

        return token;
    }

    public final HistoryToken setRename() {
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
     * Sets or replaces the current {@link SpreadsheetViewportAnchor} otherwise returns this.
     */
    public final HistoryToken setRow(final SpreadsheetSelection selection) {
        Objects.requireNonNull(selection, "selection");
        HistoryToken token = this;

        if (this instanceof SpreadsheetNameHistoryToken) {
            final SpreadsheetNameHistoryToken spreadsheetNameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);
            token = row(
                    spreadsheetNameHistoryToken.id(),
                    spreadsheetNameHistoryToken.name(),
                    selection.setDefaultAnchor()
            );
        }

        return token;
    }

    /**
     * Saves an empty {@link String} which is equivalent to deleting the property.
     */
    public final HistoryToken clearSave() {
        return this.setSave("");
    }

    /**
     * if possible creates a save, otherwise returns this.
     */
    public final HistoryToken setSave(final Optional<?> value) {
        Objects.requireNonNull(value, "value");

        String stringValue = "";

        if (value.isPresent()) {
            final Object valueNotNull = value.get();
            if (valueNotNull instanceof String) {
                stringValue = (String) valueNotNull;
            } else {
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
        }

        return this.setSave(stringValue);
    }

    /**
     * if possible creates a save, otherwise returns this.
     */
    public final HistoryToken setSave(final String text) {
        Objects.requireNonNull(text, "text");

        return this.setIfSpreadsheetIdHistoryToken(
                SpreadsheetIdHistoryToken::setSave0,
                text
        );
    }

    /**
     * Factory that creates a {@link SpreadsheetNameHistoryToken} with the given {@link TextStylePropertyName} property name.
     */
    public final HistoryToken setStyle(final TextStylePropertyName<?> propertyName) {
        return this.setIfSpreadsheetNameHistoryTokenWithValue(
                SpreadsheetNameHistoryToken::setStyle0,
                propertyName
        );
    }

    /**
     * if possible creates a unfreeze.
     */
    public final HistoryToken setUnfreeze() {
        return this.setIfSpreadsheetNameHistoryToken(
                SpreadsheetNameHistoryToken::setUnfreeze0
        );
    }

    /**
     * Tries to create an unfreeze token or {@link Optional#empty()} because the {@link SpreadsheetSelection} is invalid.
     */
    public final Optional<HistoryToken> unfreezeOrEmpty() {
        HistoryToken token;

        try {
            token = this.setUnfreeze();
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

            if (this instanceof SpreadsheetAnchoredSelectionHistoryToken) {
                final SpreadsheetAnchoredSelectionHistoryToken anchored = this.cast(SpreadsheetAnchoredSelectionHistoryToken.class);
                token = anchored.setDifferentAnchoredSelection(anchoredSelection);
            } else {
                if (this instanceof SpreadsheetNameHistoryToken) {
                    final SpreadsheetNameHistoryToken spreadsheetNameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);
                    token = HistoryTokenSelectionSpreadsheetSelectionVisitor.selectionToken(
                            spreadsheetNameHistoryToken,
                            anchoredSelection
                    );
                }
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
     * Returns any non label {@link SpreadsheetSelection} from this {@link HistoryToken}.
     */
    public final Optional<SpreadsheetSelection> nonLabelSelection(final SpreadsheetViewportCache viewportCache) {
        Objects.requireNonNull(viewportCache, "viewportCache");

        return this.anchoredSelectionOrEmpty()
                .flatMap((a) -> viewportCache.nonLabelSelection(a.selection()));
    }

    /**
     * Type safe cast to the given {@link Class literal}.
     */
    public <T> T cast(final Class<T> cast) {
        return Cast.to(this);
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
                .setId(id + SpreadsheetIds.LINK)
                .setHistoryToken(
                        Optional.of(this)
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
