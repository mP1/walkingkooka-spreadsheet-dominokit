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
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

import java.util.Optional;

/**
 * This {@link HistoryToken} selects the toolbar CURRENCY
 */
public final class SpreadsheetCellCurrencyUnselectHistoryToken extends SpreadsheetCellCurrencyHistoryToken {

    static SpreadsheetCellCurrencyUnselectHistoryToken with(final SpreadsheetId id,
                                                            final SpreadsheetName name,
                                                            final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetCellCurrencyUnselectHistoryToken(
            id,
            name,
            anchoredSelection
        );
    }

    private SpreadsheetCellCurrencyUnselectHistoryToken(final SpreadsheetId id,
                                                        final SpreadsheetName name,
                                                        final AnchoredSpreadsheetSelection anchoredSelection) {
        super(
            id,
            name,
            anchoredSelection,
            Optional.empty() // Currency
        );
    }

    @Override
    public HistoryToken clearAction() {
        return this;
    }

    @Override
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetCellCurrencyUnselectHistoryToken(
            id,
            name,
            anchoredSelection
        );
    }

    // cell/A1/currency/toolbar
    @Override
    UrlFragment currencyUrlFragment() {
        return TOOLBAR;
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // TODO give focus to toolbar currency link
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitCellCurrencyUnselect(
            this.id,
            this.name,
            this.anchoredSelection
        );
    }
}
