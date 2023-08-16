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
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.dom.Anchor;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;
import walkingkooka.text.CharSequences;
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

    /**
     * {@see SpreadsheetCellSelectHistoryToken}
     */
    public static SpreadsheetCellSelectHistoryToken cell(final SpreadsheetId id,
                                                         final SpreadsheetName name,
                                                         final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellSelectHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetCellFormulaSelectHistoryToken}
     */
    public static SpreadsheetCellFormulaSelectHistoryToken formula(final SpreadsheetId id,
                                                                   final SpreadsheetName name,
                                                                   final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellFormulaSelectHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetCellFormulaSaveHistoryToken}
     */
    public static SpreadsheetCellFormulaSaveHistoryToken formulaSave(final SpreadsheetId id,
                                                                     final SpreadsheetName name,
                                                                     final SpreadsheetViewportSelection viewportSelection,
                                                                     final SpreadsheetFormula formula) {
        return SpreadsheetCellFormulaSaveHistoryToken.with(
                id,
                name,
                viewportSelection,
                formula
        );
    }

    /**
     * {@see SpreadsheetCellClearHistoryToken}
     */
    public static SpreadsheetCellClearHistoryToken cellClear(final SpreadsheetId id,
                                                             final SpreadsheetName name,
                                                             final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellClearHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetCellDeleteHistoryToken}
     */
    public static SpreadsheetCellDeleteHistoryToken cellDelete(final SpreadsheetId id,
                                                               final SpreadsheetName name,
                                                               final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellDeleteHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetCellFreezeHistoryToken}
     */
    public static SpreadsheetCellFreezeHistoryToken cellFreeze(final SpreadsheetId id,
                                                               final SpreadsheetName name,
                                                               final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellFreezeHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetCellMenuHistoryToken}
     */
    public static SpreadsheetCellMenuHistoryToken cellMenu(final SpreadsheetId id,
                                                           final SpreadsheetName name,
                                                           final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellMenuHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetCellPatternSelectHistoryToken}
     */
    public static SpreadsheetCellPatternSelectHistoryToken cellPattern(final SpreadsheetId id,
                                                                       final SpreadsheetName name,
                                                                       final SpreadsheetViewportSelection viewportSelection,
                                                                       final SpreadsheetPatternKind patternKind) {
        return SpreadsheetCellPatternSelectHistoryToken.with(
                id,
                name,
                viewportSelection,
                patternKind
        );
    }

    /**
     * {@see SpreadsheetCellPatternSaveHistoryToken}
     */
    public static SpreadsheetCellPatternSaveHistoryToken cellPatternSave(final SpreadsheetId id,
                                                                         final SpreadsheetName name,
                                                                         final SpreadsheetViewportSelection viewportSelection,
                                                                         final SpreadsheetPatternKind patternKind,
                                                                         final Optional<SpreadsheetPattern> pattern) {
        return SpreadsheetCellPatternSaveHistoryToken.with(
                id,
                name,
                viewportSelection,
                patternKind,
                pattern
        );
    }

    /**
     * {@see SpreadsheetCellPatternToolbarHistoryToken}
     */
    public static SpreadsheetCellPatternToolbarHistoryToken cellPatternToolbar(final SpreadsheetId id,
                                                                               final SpreadsheetName name,
                                                                               final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellPatternToolbarHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetCellStyleSelectHistoryToken}
     */
    public static <T> SpreadsheetCellStyleSelectHistoryToken<T> cellStyle(final SpreadsheetId id,
                                                                          final SpreadsheetName name,
                                                                          final SpreadsheetViewportSelection viewportSelection,
                                                                          final TextStylePropertyName<T> propertyName) {
        return SpreadsheetCellStyleSelectHistoryToken.with(
                id,
                name,
                viewportSelection,
                propertyName
        );
    }

    /**
     * {@see SpreadsheetCellStyleSaveHistoryToken}
     */
    public static <T> SpreadsheetCellStyleSaveHistoryToken<T> cellStyleSave(final SpreadsheetId id,
                                                                            final SpreadsheetName name,
                                                                            final SpreadsheetViewportSelection viewportSelection,
                                                                            final TextStylePropertyName<T> propertyName,
                                                                            final Optional<T> propertyValue) {
        return SpreadsheetCellStyleSaveHistoryToken.with(
                id,
                name,
                viewportSelection,
                propertyName,
                propertyValue
        );
    }

    /**
     * {@see SpreadsheetCellUnfreezeHistoryToken}
     */
    public static SpreadsheetCellUnfreezeHistoryToken cellUnfreeze(final SpreadsheetId id,
                                                                   final SpreadsheetName name,
                                                                   final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellUnfreezeHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetColumnSelectHistoryToken}
     */
    public static SpreadsheetColumnSelectHistoryToken column(final SpreadsheetId id,
                                                             final SpreadsheetName name,
                                                             final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnSelectHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetColumnClearHistoryToken}
     */
    public static SpreadsheetColumnClearHistoryToken columnClear(final SpreadsheetId id,
                                                                 final SpreadsheetName name,
                                                                 final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnClearHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetColumnDeleteHistoryToken}
     */
    public static SpreadsheetColumnDeleteHistoryToken columnDelete(final SpreadsheetId id,
                                                                   final SpreadsheetName name,
                                                                   final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnDeleteHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetColumnFreezeHistoryToken}
     */
    public static SpreadsheetColumnFreezeHistoryToken columnFreeze(final SpreadsheetId id,
                                                                   final SpreadsheetName name,
                                                                   final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnFreezeHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetColumnMenuHistoryToken}
     */
    public static SpreadsheetColumnMenuHistoryToken columnMenu(final SpreadsheetId id,
                                                               final SpreadsheetName name,
                                                               final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnMenuHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetColumnUnfreezeHistoryToken}
     */
    public static SpreadsheetColumnUnfreezeHistoryToken columnUnfreeze(final SpreadsheetId id,
                                                                       final SpreadsheetName name,
                                                                       final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnUnfreezeHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetLabelMappingSelectHistoryToken}
     */
    public static SpreadsheetLabelMappingSelectHistoryToken labelMapping(final SpreadsheetId id,
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
                                                       final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetRowSelectHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetRowClearHistoryToken}
     */
    public static SpreadsheetRowClearHistoryToken rowClear(final SpreadsheetId id,
                                                           final SpreadsheetName name,
                                                           final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetRowClearHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetRowDeleteHistoryToken}
     */
    public static SpreadsheetRowDeleteHistoryToken rowDelete(final SpreadsheetId id,
                                                             final SpreadsheetName name,
                                                             final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetRowDeleteHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetRowFreezeHistoryToken}
     */
    public static SpreadsheetRowFreezeHistoryToken rowFreeze(final SpreadsheetId id,
                                                             final SpreadsheetName name,
                                                             final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetRowFreezeHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetRowMenuHistoryToken}
     */
    public static SpreadsheetRowMenuHistoryToken rowMenu(final SpreadsheetId id,
                                                         final SpreadsheetName name,
                                                         final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetRowMenuHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    /**
     * {@see SpreadsheetRowUnfreezeHistoryToken}
     */
    public static SpreadsheetRowUnfreezeHistoryToken rowUnfreeze(final SpreadsheetId id,
                                                                 final SpreadsheetName name,
                                                                 final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetRowUnfreezeHistoryToken.with(
                id,
                name,
                viewportSelection
        );
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

    public static SpreadsheetViewportSelectionHistoryToken viewportSelection(final SpreadsheetId id,
                                                                             final SpreadsheetName name,
                                                                             final SpreadsheetViewportSelection viewportSelection) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(viewportSelection, "viewportSelection");

        SpreadsheetViewportSelectionHistoryToken historyToken;

        final SpreadsheetSelection selection = viewportSelection.selection();

        for (; ; ) {
            if (selection.isCellReference() || selection.isCellRange() || selection.isLabelName()) {
                historyToken = cell(
                        id,
                        name,
                        viewportSelection
                );
                break;
            }
            if (selection.isColumnReference() || selection.isColumnReferenceRange()) {
                historyToken = column(
                        id,
                        name,
                        viewportSelection
                );
                break;
            }
            if (selection.isRowReference() || selection.isRowReferenceRange()) {
                historyToken = row(
                        id,
                        name,
                        viewportSelection
                );
                break;
            }

            throw new UnsupportedOperationException("Unexpected selection type " + selection);
        }

        return historyToken;
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
     * Consumes a path component within the {@link TextCursor}.
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

        if (this instanceof SpreadsheetLabelMappingHistoryToken) {
            final SpreadsheetLabelMappingHistoryToken label = (SpreadsheetLabelMappingHistoryToken) this;
            closed = spreadsheetSelect(
                    label.id(),
                    label.name()
            );
        }
        if (this instanceof SpreadsheetCellPatternSelectHistoryToken) {
            final SpreadsheetCellPatternSelectHistoryToken patternSelect = (SpreadsheetCellPatternSelectHistoryToken) this;
            closed = cellPatternToolbar(
                    patternSelect.id(),
                    patternSelect.name(),
                    patternSelect.viewportSelection()
            );
        }

        return closed;
    }

    /**
     * Sets or replaces the current {@link SpreadsheetViewportSelectionAnchor} otherwise returns this.
     */
    public final HistoryToken setAnchor(final SpreadsheetViewportSelectionAnchor anchor) {
        Objects.requireNonNull(anchor, "anchor");
        HistoryToken token = this;

        if (this instanceof SpreadsheetViewportSelectionHistoryToken) {
            final SpreadsheetViewportSelectionHistoryToken spreadsheetViewportSelectionHistoryToken = this.cast(SpreadsheetViewportSelectionHistoryToken.class);

            try {
                token = HistoryToken.viewportSelection(
                        spreadsheetViewportSelectionHistoryToken.id(),
                        spreadsheetViewportSelectionHistoryToken.name(),
                        spreadsheetViewportSelectionHistoryToken.viewportSelection()
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
     * Sets or replaces the current {@link SpreadsheetViewportSelectionAnchor} otherwise returns this.
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
     * if possible creates a clear.
     */
    public final HistoryToken setClear() {
        return this.setIfSpreadsheetNameHistoryToken(
                SpreadsheetNameHistoryToken::setClear0
        );
    }

    /**
     * Sets or replaces the current {@link SpreadsheetViewportSelectionAnchor} otherwise returns this.
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
     * Accepts a id and name, attempting to replace the name if the id is unchanged or when different replaces the
     * entire history token.
     */
    public abstract HistoryToken setIdAndName(final SpreadsheetId id,
                                              final SpreadsheetName name);

    /**
     * Sets or replaces the current {@link SpreadsheetLabelName} otherwise returns this.
     */
    public final HistoryToken setLabelName(final SpreadsheetLabelName label) {
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
            if (this instanceof SpreadsheetViewportSelectionHistoryToken) {
                result = this.cast(SpreadsheetViewportSelectionHistoryToken.class)
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

            final Optional<SpreadsheetViewportSelection> maybeViewportSelection = this.viewportSelectionOrEmpty();
            if (maybeViewportSelection.isPresent()) {
                final SpreadsheetViewportSelection viewportSelection = maybeViewportSelection.get();

                // right mouse happened over already selected selection...
                if (viewportSelection.selection().test(selection)) {
                    menu = this.cast(SpreadsheetViewportSelectionHistoryToken.class)
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
     * if possible creates a {@link HistoryToken} with the {@link SpreadsheetPatternKind}.
     */
    public final HistoryToken setPatternKind(final Optional<SpreadsheetPatternKind> kind) {
        return this.setIfSpreadsheetNameHistoryToken0(
                SpreadsheetNameHistoryToken::setPatternKind0,
                kind
        );
    }

    /**
     * Sets or replaces the current {@link SpreadsheetViewportSelectionAnchor} otherwise returns this.
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
     * if possible creates a save.
     */
    public final HistoryToken setSave(final String text) {
        return this.setIfSpreadsheetNameHistoryToken0(
                SpreadsheetNameHistoryToken::setSave0,
                text
        );
    }

    /**
     * Factory that creates a {@link SpreadsheetNameHistoryToken} with the given {@link TextStylePropertyName} property name.
     */
    public final HistoryToken setStyle(final TextStylePropertyName<?> propertyName) {
        return this.setIfSpreadsheetNameHistoryToken0(
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

    private HistoryToken setIfSpreadsheetNameHistoryToken(final Function<SpreadsheetNameHistoryToken, HistoryToken> setter) {
        HistoryToken token = this;

        if (this instanceof SpreadsheetNameHistoryToken) {
            token = setter.apply(
                    this.cast(SpreadsheetNameHistoryToken.class)
            );
        }

        return token;
    }

    private <T> HistoryToken setIfSpreadsheetNameHistoryToken0(final BiFunction<SpreadsheetNameHistoryToken, T, HistoryToken> setter,
                                                               final T value) {
        HistoryToken token = this;

        if (this instanceof SpreadsheetNameHistoryToken) {
            token = setter.apply(
                    this.cast(SpreadsheetNameHistoryToken.class),
                    value
            );
        }

        return token;
    }

    /**
     * Factory that creates a {@link HistoryToken} changing the {@link SpreadsheetViewportSelection} component and clearing any action.
     */
    public final HistoryToken setViewportSelection(final Optional<SpreadsheetViewportSelection> viewportSelection) {
        Objects.requireNonNull(viewportSelection, "viewportSelection");

        return this.viewportSelectionOrEmpty().equals(viewportSelection) ?
                this :
                this.setDifferentViewportSelection(viewportSelection);
    }

    private HistoryToken setDifferentViewportSelection(final Optional<SpreadsheetViewportSelection> viewportSelection) {
        HistoryToken token = this;

        if (this instanceof SpreadsheetNameHistoryToken) {
            final SpreadsheetNameHistoryToken spreadsheetNameHistoryToken = this.cast(SpreadsheetNameHistoryToken.class);

            token = viewportSelection.isPresent() ?
                    HistoryTokenSelectionSpreadsheetSelectionVisitor.selectionToken(
                            spreadsheetNameHistoryToken,
                            viewportSelection.get()
                    ) :
                    spreadsheetSelect(
                            spreadsheetNameHistoryToken.id(),
                            spreadsheetNameHistoryToken.name()
                    );
        }

        return token;
    }

    /**
     * Returns a {@link SpreadsheetViewportSelectionHistoryToken} using the id, name and {@link SpreadsheetViewportSelection}.
     */
    public final Optional<HistoryToken> viewportSelectionHistoryTokenOrEmpty() {
        HistoryToken result = null;

        if (this instanceof SpreadsheetViewportSelectionHistoryToken) {
            final SpreadsheetViewportSelectionHistoryToken spreadsheetViewportSelectionHistoryToken = this.cast(SpreadsheetViewportSelectionHistoryToken.class);

            result = HistoryToken.viewportSelection(
                    spreadsheetViewportSelectionHistoryToken.id(),
                    spreadsheetViewportSelectionHistoryToken.name(),
                    spreadsheetViewportSelectionHistoryToken.viewportSelection()
            );

            if (this.equals(result)) {
                result = this;
            }
        }

        return Optional.ofNullable(result);
    }

    /**
     * Maybe used to get the {@link SpreadsheetViewportSelection} from any {@link HistoryToken}
     */
    public final Optional<SpreadsheetViewportSelection> viewportSelectionOrEmpty() {
        SpreadsheetViewportSelection viewportSelection = null;

        if (this instanceof SpreadsheetViewportSelectionHistoryToken) {
            viewportSelection = this.cast(SpreadsheetViewportSelectionHistoryToken.class)
                    .viewportSelection();
        }

        return Optional.ofNullable(viewportSelection);
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
     * Creates a link with the given text and id.
     */
    public final Anchor link(final String id) {
        return Anchor.empty()
                .setId(id + "-link")
                .setHistoryToken(this);
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
