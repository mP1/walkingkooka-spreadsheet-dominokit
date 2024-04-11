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
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * A token that represents a reload spreadsheet action.
 */
public final class SpreadsheetReloadHistoryToken extends SpreadsheetNameHistoryToken {

    static SpreadsheetReloadHistoryToken with(final SpreadsheetId id,
                                              final SpreadsheetName name) {
        return new SpreadsheetReloadHistoryToken(
                id,
                name
        );
    }

    private SpreadsheetReloadHistoryToken(final SpreadsheetId id,
                                          final SpreadsheetName name) {
        super(
                id,
                name
        );
    }

    @Override
    HistoryToken parse0(final String component,
                        final TextCursor cursor) {
        return this; // ignores whatever is after /spreadsheet-id/spreadsheet-name/reload
    }

    @Override //
    public HistoryToken clearAction() {
        return spreadsheetSelect(
                this.id(),
                this.name()
        );
    }

    @Override
    public HistoryToken setFormula() {
        return this;
    }

    @Override //
    HistoryToken replaceIdAndName(final SpreadsheetId id,
                                  final SpreadsheetName name) {
        return new SpreadsheetReloadHistoryToken(
                id,
                name
        );
    }

    @Override
    UrlFragment spreadsheetNameUrlFragment() {
        return RELOAD;
    }

    @Override //
    HistoryToken setClear0() {
        return this;
    }

    @Override //
    HistoryToken setDelete0() {
        return this;
    }

    @Override //
    HistoryToken setFormatPattern() {
        return this;
    }

    @Override //
    HistoryToken setFreeze0() {
        return this;
    }

    @Override //
    HistoryToken setInsertAfter0(final OptionalInt count) {
        return this;
    }

    @Override //
    HistoryToken setInsertBefore0(final OptionalInt count) {
        return this;
    }

    @Override //
    HistoryToken setMenu1() {
        return this;
    }

    @Override //
    AnchoredSpreadsheetSelection setMenuSelection(final SpreadsheetSelection selection) {
        return selection.setDefaultAnchor();
    }

    @Override //
    HistoryToken setParsePattern() {
        return this;
    }

    @Override //
    HistoryToken replacePatternKind(final Optional<SpreadsheetPatternKind> patternKind) {
        return this;
    }

    @Override //
    HistoryToken setSave0(final String value) {
        return this;
    }

    @Override
    HistoryToken setStyle0(final TextStylePropertyName<?> propertyName) {
        return this;
    }

    @Override
    HistoryToken setUnfreeze0() {
        return this;
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.pushHistoryToken(
                previous
        );
        context.reload();
    }
}
