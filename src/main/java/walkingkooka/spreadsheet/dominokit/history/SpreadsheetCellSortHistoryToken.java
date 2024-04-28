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
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNamesList;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;

import java.util.Optional;

/**
 * Base class for both cell sort history tokens.
 */
public abstract class SpreadsheetCellSortHistoryToken extends SpreadsheetCellHistoryToken {
    SpreadsheetCellSortHistoryToken(final SpreadsheetId id,
                                    final SpreadsheetName name,
                                    final AnchoredSpreadsheetSelection anchoredSelection) {
        super(
                id,
                name,
                anchoredSelection
        );
    }

    @Override //
    final UrlFragment cellUrlFragment() {
        return SORT.append(this.sortUrlFragment());
    }

    abstract UrlFragment sortUrlFragment();

    @Override
    public HistoryToken clearAction() {
        return HistoryToken.cell(
                this.id(),
                this.name(),
                this.anchoredSelection()
        );
    }

    @Override
    public final HistoryToken setFormula() {
        return HistoryToken.formula(
                this.id(),
                this.name(),
                this.anchoredSelection()
        );
    }

    @Override //
    final HistoryToken setFormatPattern() {
        return this;
    }

    @Override //
    final HistoryToken setParsePattern() {
        return this;
    }

    @Override
    HistoryToken replacePatternKind(final Optional<SpreadsheetPatternKind> patternKind) {
        return this;
    }

    @Override final HistoryToken setSave0(final String value) {
        return HistoryToken.cellSortSave(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse(value)
        );
    }
}
