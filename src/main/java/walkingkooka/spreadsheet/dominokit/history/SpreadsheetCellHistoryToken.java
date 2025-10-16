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
import walkingkooka.spreadsheet.SpreadsheetUrlFragments;
import walkingkooka.spreadsheet.dominokit.clipboard.SpreadsheetCellClipboardKind;
import walkingkooka.spreadsheet.engine.SpreadsheetCellFindQuery;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelNameResolvers;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;
import walkingkooka.validation.ValueTypeName;
import walkingkooka.validation.form.FormName;

import java.util.Optional;

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
                token = cell.setCopy(
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
                this.id(),
                this.name(),
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
                token = cell.setCellPaste(
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
