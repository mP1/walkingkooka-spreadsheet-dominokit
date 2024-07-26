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
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;

import java.util.Optional;

public final class SpreadsheetCellFormatterSaveHistoryToken extends SpreadsheetCellFormatterHistoryToken {

    static SpreadsheetCellFormatterSaveHistoryToken with(final SpreadsheetId id,
                                                         final SpreadsheetName name,
                                                         final AnchoredSpreadsheetSelection anchoredSelection,
                                                         final SpreadsheetPatternKind spreadsheetPatternKind,
                                                         final Optional<SpreadsheetFormatterSelector> spreadsheetFormatterSelector) {
        return new SpreadsheetCellFormatterSaveHistoryToken(
                id,
                name,
                anchoredSelection,
                spreadsheetPatternKind,
                spreadsheetFormatterSelector
        );
    }

    private SpreadsheetCellFormatterSaveHistoryToken(final SpreadsheetId id,
                                                     final SpreadsheetName name,
                                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                                     final SpreadsheetPatternKind spreadsheetPatternKind,
                                                     final Optional<SpreadsheetFormatterSelector> spreadsheetFormatterSelector) {
        super(
                id,
                name,
                anchoredSelection,
                Optional.of(spreadsheetPatternKind),
                spreadsheetFormatterSelector
        );
    }

    @Override
    public HistoryToken clearAction() {
        return HistoryToken.cellFormatterSelect(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                this.spreadsheetPatternKind.get()
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
                this.patternKind()
                        .get(),
                this.spreadsheetFormatterSelector
        );
    }

    @Override//
    HistoryToken replacePatternKind0(final SpreadsheetPatternKind patternKind) {
        return new SpreadsheetCellFormatterSaveHistoryToken(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                patternKind,
                this.spreadsheetFormatterSelector
        );
    }

    @Override
    HistoryToken setSave0(final String value) {
        final SpreadsheetPatternKind kind = this.spreadsheetPatternKind.get();

        return new SpreadsheetCellFormatterSaveHistoryToken(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                kind,
                Optional.ofNullable(
                        value.isEmpty() ?
                                null :
                                SpreadsheetFormatterSelector.parse(value)
                )
        );
    }

    // cell/A1/formatter/SpreadsheetPatternKind/save/SpreadsheetFormatterSelector
    @Override
    UrlFragment formatterUrlFragment() {
        return this.spreadsheetPatternKind.get()
                .urlFragment()
                .appendSlashThen(
                        this.saveUrlFragment(this.spreadsheetFormatterSelector)
                );
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
