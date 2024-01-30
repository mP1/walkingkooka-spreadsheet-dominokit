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
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.ui.Anchor;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetCellFind;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIds;
import walkingkooka.spreadsheet.dominokit.ui.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.format.pattern.HasSpreadsheetPatternKind;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
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
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class HistoryToken implements HasUrlFragment {

    private final static int MAX_LENGTH = 8192;

    public final static String DISABLE = "disable";

    public final static String ENABLE = "enable";

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
     * {@see SpreadsheetCellPatternToolbarFormatHistoryToken}
     */
    public static SpreadsheetCellPatternToolbarHistoryToken cellFormatPatternToolbar(final SpreadsheetId id,
                                                                                     final SpreadsheetName name,
                                                                                     final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellPatternToolbarFormatHistoryToken.with(
                id,
                name,
                anchoredSelection
        );
    }

    /**
     * {@see SpreadsheetCellPatternToolbarParseHistoryToken}
     */
    public static SpreadsheetCellPatternToolbarHistoryToken cellParsePatternToolbar(final SpreadsheetId id,
                                                                                    final SpreadsheetName name,
                                                                                    final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellPatternToolbarParseHistoryToken.with(
                id,
                name,
                anchoredSelection
        );
    }

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
                                                                             final int count) {
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
                                                                               final int count) {
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
                                                                       final int count) {
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
                                                                         final int count) {
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

    public static AnchoredSpreadsheetSelectionHistoryToken selection(final SpreadsheetId id,
                                                                     final SpreadsheetName name,
                                                                     final AnchoredSpreadsheetSelection anchoredSelection) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(anchoredSelection, "anchoredSelection");

        AnchoredSpreadsheetSelectionHistoryToken historyToken;

        final SpreadsheetSelection selection = anchoredSelection.selection();

        for (; ; ) {
            if (selection.isCellReference() || selection.isCellRange() || selection.isLabelName()) {
                historyToken = cell(
                        id,
                        name,
                        anchoredSelection
                );
                break;
            }
            if (selection.isColumnReference() || selection.isColumnReferenceRange()) {
                historyToken = column(
                        id,
                        name,
                        anchoredSelection
                );
                break;
            }
            if (selection.isRowReference() || selection.isRowReferenceRange()) {
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

        HistoryToken token;

        final TextCursor cursor = TextCursors.charSequence(fragment.value());

        try {
            if (false == cursor.isEmpty()) {
                final char c = cursor.at();
                switch (c) {
                    case '/':
                        token = HistoryToken.spreadsheetCreate()
                                .parse(cursor);
                        break;
                    default:
                        token = UnknownHistoryToken.with(fragment);
                }
            } else {
                token = HistoryToken.spreadsheetCreate();
            }
        } catch (final RuntimeException ignore) {
            token = UnknownHistoryToken.with(fragment);
        }

        return token;
    }

    /**
     * Consumes a path ui within the {@link TextCursor}.
     */
    static Optional<String> parseComponent(final TextCursor cursor) {
        return COMPONENT.parse(cursor, CONTEXT)
                .map(p -> p.cast(StringParserToken.class)
                        .value()
                        .substring(1)
                );
    }

    static String parseAll(final TextCursor cursor) {
        final TextCursorSavePoint save = cursor.save();
        cursor.end();
        return save.textBetween()
                .toString()
                .substring(1); // drops assumed leading slash

    }

    /**
     * A {@link Parser} that consumes a path ui within an {@link UrlFragment}.
     */
    private final static Parser<ParserContext> COMPONENT = Parsers.stringInitialAndPartCharPredicate(
            CharPredicates.is('/'),
            CharPredicates.not(
                    CharPredicates.is('/')
            ),
            1,
            MAX_LENGTH
    );

    final static ParserContext CONTEXT = ParserContexts.fake();

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
                            cellFormatPatternToolbar(
                                    id,
                                    name,
                                    anchoredSelection
                            ) :
                            cellParsePatternToolbar(
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
        if (this instanceof SpreadsheetMetadataPropertyHistoryToken) {
            final SpreadsheetMetadataPropertyHistoryToken<?> metadata = (SpreadsheetMetadataPropertyHistoryToken<?>) this;
            closed = metadataSelect(
                    metadata.id(),
                    metadata.name()
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

        if (this instanceof AnchoredSpreadsheetSelectionHistoryToken) {
            final AnchoredSpreadsheetSelectionHistoryToken anchoredSpreadsheetSelectionHistoryToken = this.cast(AnchoredSpreadsheetSelectionHistoryToken.class);

            try {
                token = HistoryToken.selection(
                        anchoredSpreadsheetSelectionHistoryToken.id(),
                        anchoredSpreadsheetSelectionHistoryToken.name(),
                        anchoredSpreadsheetSelectionHistoryToken.anchoredSelection()
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
    public final HistoryToken setInsertAfter(final int count) {
        return this.setIfSpreadsheetNameHistoryToken(
                (nht) -> nht.setInsertAfter0(count)
        );
    }

    /**
     * if possible creates a freeze.
     */
    public final HistoryToken setInsertBefore(final int count) {
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
     * Accepts a id and name, attempting to replace the name if the id is unchanged or when different replaces the
     * entire history token.
     */
    public abstract HistoryToken setIdAndName(final SpreadsheetId id,
                                              final SpreadsheetName name);

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
            if (this instanceof AnchoredSpreadsheetSelectionHistoryToken) {
                result = this.cast(AnchoredSpreadsheetSelectionHistoryToken.class)
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
                    menu = this.cast(AnchoredSpreadsheetSelectionHistoryToken.class)
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

        if (this instanceof SpreadsheetMetadataHistoryToken) {
            token = this.cast(SpreadsheetMetadataHistoryToken.class)
                    .setMetadataPropertyName0(propertyName);
        }

        return token;
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

        return this.setIfSpreadsheetNameHistoryTokenWithValue(
                SpreadsheetNameHistoryToken::setPatternKind0,
                kind
        );
    }

    public final HistoryToken setReload() {
        HistoryToken token = this;

        if (this instanceof SpreadsheetNameHistoryToken) {
            final SpreadsheetNameHistoryToken spreadsheetNameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);
            token = spreadsheetReload(
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
    public final HistoryToken setSave(final Object value) {
        Objects.requireNonNull(value, "value");

        final HistoryToken result;

        if (value instanceof String) {
            result = this.setSave((String) value);
        } else {
            if (value instanceof HasText) {
                result = this.setSave(
                        (HasText) value
                );
            } else {
                if (value instanceof Enum) {
                    result = this.setSave(
                            (Enum<?>) value
                    );
                } else {
                    result = this.setSave(value.toString());
                }
            }
        }

        return result;
    }

    /**
     * if possible creates a save, otherwise returns this.
     */
    public final HistoryToken setSave(final String text) {
        Objects.requireNonNull(text, "text");

        return this.setIfSpreadsheetNameHistoryTokenWithValue(
                SpreadsheetNameHistoryToken::setSave0,
                text
        );
    }


    /**
     * Overload that accepts a value with {@link walkingkooka.text.HasText} such as {@link SpreadsheetPattern}.
     */
    public final HistoryToken setSave(final HasText text) {
        Objects.requireNonNull(text, "text");

        return this.setSave(
                text.text()
        );
    }

    /**
     * Overload that accepts a value with {@link walkingkooka.text.HasText} such as {@link SpreadsheetPattern}.
     */
    public final HistoryToken setSave(final Enum<?> value) {
        Objects.requireNonNull(value, "value");

        return this.setSave(
                value.name()
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
        return name.contains("Menu") || name.contains("Reload") || name.contains("Save");
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
    public final HistoryToken setAnchoredSelection(final Optional<AnchoredSpreadsheetSelection> selection) {
        Objects.requireNonNull(selection, "selection");

        return this.anchoredSelectionOrEmpty().equals(selection) ?
                this :
                this.setDifferentSelection(selection);
    }

    private HistoryToken setDifferentSelection(final Optional<AnchoredSpreadsheetSelection> maybeAnchoredSelection) {
        HistoryToken token = null;

        if (maybeAnchoredSelection.isPresent()) {
            final AnchoredSpreadsheetSelection anchoredSelection = maybeAnchoredSelection.get();

            if (this instanceof AnchoredSpreadsheetSelectionHistoryToken) {
                final AnchoredSpreadsheetSelectionHistoryToken anchored = this.cast(AnchoredSpreadsheetSelectionHistoryToken.class);
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
     * Returns a {@link AnchoredSpreadsheetSelectionHistoryToken} using the id, name and {@link AnchoredSpreadsheetSelection}.
     */
    public final Optional<HistoryToken> anchoredSelectionHistoryTokenOrEmpty() {
        HistoryToken result = null;

        if (this instanceof AnchoredSpreadsheetSelectionHistoryToken) {
            final AnchoredSpreadsheetSelectionHistoryToken AnchoredSpreadsheetSelectionHistoryToken = this.cast(AnchoredSpreadsheetSelectionHistoryToken.class);

            result = HistoryToken.selection(
                    AnchoredSpreadsheetSelectionHistoryToken.id(),
                    AnchoredSpreadsheetSelectionHistoryToken.name(),
                    AnchoredSpreadsheetSelectionHistoryToken.anchoredSelection()
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

        if (this instanceof AnchoredSpreadsheetSelectionHistoryToken) {
            anchoredSelection = this.cast(AnchoredSpreadsheetSelectionHistoryToken.class)
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
     * Creates a link with the given id.
     */
    public final Anchor link(final String id) {
        return Anchor.empty()
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
