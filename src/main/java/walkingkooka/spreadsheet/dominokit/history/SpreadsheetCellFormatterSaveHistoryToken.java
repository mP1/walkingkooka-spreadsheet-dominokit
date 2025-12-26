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

import walkingkooka.Value;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

import java.util.Optional;

public final class SpreadsheetCellFormatterSaveHistoryToken extends SpreadsheetCellFormatterHistoryToken implements Value<Optional<SpreadsheetFormatterSelector>> {

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
    public Optional<SpreadsheetFormatterSelector> value() {
        return this.spreadsheetFormatterSelector();
    }

    @Override
    public HistoryToken clearAction() {
        return HistoryToken.cellFormatterSelect(
            this.id,
            this.name,
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

    // cell/A1/formatter/save/SpreadsheetFormatterSelector
    @Override
    UrlFragment formatterUrlFragment() {
        return saveUrlFragment(this.value());
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.pushHistoryToken(previous);

        context.spreadsheetDeltaFetcher()
            .patchFormatter(
                this.id,
                this.anchoredSelection().selection(),
                this.spreadsheetFormatterSelector
            );
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitCellFormatterSave(
            this.id,
            this.name,
            this.anchoredSelection,
            this.spreadsheetFormatterSelector
        );
    }
}
