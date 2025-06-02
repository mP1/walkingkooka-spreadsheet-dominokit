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
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.validation.ValidationValueTypeName;

import java.util.Objects;
import java.util.Optional;

/**
 * Selects a picker for the user to enter a value for a {@link walkingkooka.spreadsheet.SpreadsheetCell}.
 * If the optional {@link walkingkooka.validation.ValidationValueTypeName} is present that will be used to select
 * a picker, otherwise it will default to {@link SpreadsheetFormula#valueType()}.
 * <pre>
 * /1/SpreadsheetName222/cell/A1/value/{@link ValidationValueTypeName}
 * </pre>
 */
public final class SpreadsheetCellValueHistoryToken extends SpreadsheetCellHistoryToken {

    static SpreadsheetCellValueHistoryToken with(final SpreadsheetId id,
                                                 final SpreadsheetName name,
                                                 final AnchoredSpreadsheetSelection anchoredSelection,
                                                 final Optional<ValidationValueTypeName> valueType) {
        return new SpreadsheetCellValueHistoryToken(
            id,
            name,
            anchoredSelection,
            valueType
        );
    }

    private SpreadsheetCellValueHistoryToken(final SpreadsheetId id,
                                             final SpreadsheetName name,
                                             final AnchoredSpreadsheetSelection anchoredSelection,
                                             final Optional<ValidationValueTypeName> valueType) {
        super(
            id,
            name,
            anchoredSelection
        );
        this.valueType = Objects.requireNonNull(
            valueType,
            "valueType"
        );
    }

    final Optional<ValidationValueTypeName> valueType;

    // /cell/A1/value
    // /cell/A1/value/date
    @Override
    UrlFragment cellUrlFragment() {
        final Optional<ValidationValueTypeName> valueType = this.valueType;
        return VALUE.appendSlashThen(
            valueType.map(HasUrlFragment::urlFragment)
                .orElse(UrlFragment.EMPTY)
        );
    }

    @Override
    public HistoryToken clearAction() {
        return this.selectionSelect();
    }

    @Override //
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetCellValueHistoryToken(
            id,
            name,
            anchoredSelection,
            this.valueType
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // references dialog will open
    }
}
