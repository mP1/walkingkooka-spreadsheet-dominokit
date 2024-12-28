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
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;

import java.util.Optional;

public final class SpreadsheetCellFormatterSaveHistoryToken extends SpreadsheetCellFormatterHistoryToken {

    static SpreadsheetCellFormatterSaveHistoryToken with(final SpreadsheetId id,
                                                         final SpreadsheetName name,
                                                         final AnchoredSpreadsheetSelection anchoredSelection,
                                                         final Optional<SpreadsheetFormatterSelector> spreadsheetFormatterSelector) {
        return new SpreadsheetCellFormatterSaveHistoryToken(
                id,
                name,
                anchoredSelection,
                spreadsheetFormatterSelector
        );
    }

    private SpreadsheetCellFormatterSaveHistoryToken(final SpreadsheetId id,
                                                     final SpreadsheetName name,
                                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                                     final Optional<SpreadsheetFormatterSelector> spreadsheetFormatterSelector) {
        super(
                id,
                name,
                anchoredSelection,
                spreadsheetFormatterSelector
        );
    }

    @Override
    public HistoryToken clearAction() {
        return HistoryToken.cellFormatterSelect(
                this.id(),
                this.name(),
                this.anchoredSelection()
        );
    }

    @Override
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetCellFormatterSaveHistoryToken(
                id,
                name,
                anchoredSelection,
                this.spreadsheetFormatterSelector
        );
    }

    @Override
    HistoryToken save0(final String value) {
        return new SpreadsheetCellFormatterSaveHistoryToken(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                Optional.ofNullable(
                        value.isEmpty() ?
                                null :
                                SpreadsheetFormatterSelector.parse(value)
                )
        );
    }

    // cell/A1/formatter/save/SpreadsheetFormatterSelector
    @Override
    UrlFragment formatterUrlFragment() {
        return saveUrlFragment(this.spreadsheetFormatterSelector);
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.pushHistoryToken(previous);

        context.spreadsheetDeltaFetcher()
                .saveFormatter(
                        this.id(),
                        this.anchoredSelection().selection(),
                        this.spreadsheetFormatterSelector
                );
    }
}
