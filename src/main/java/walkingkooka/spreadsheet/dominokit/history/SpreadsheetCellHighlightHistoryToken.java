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
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;

import java.util.Optional;

public abstract class SpreadsheetCellHighlightHistoryToken extends SpreadsheetCellHistoryToken {

    final static String DISABLE = "disable";

    final static String ENABLE = "enable";

    SpreadsheetCellHighlightHistoryToken(final SpreadsheetId id,
                                         final SpreadsheetName name,
                                         final AnchoredSpreadsheetSelection selection) {
        super(id, name, selection);
    }

    @Override //
    final UrlFragment cellUrlFragment() {
        return HIGHLIGHT.append(this.highlightUrlFragment());
    }

    private final static UrlFragment HIGHLIGHT = UrlFragment.SLASH
            .append(UrlFragment.with("highlight"));

    abstract UrlFragment highlightUrlFragment();

    @Override
    public final HistoryToken setFormula() {
        return setFormula0();
    }

    @Override //
    final HistoryToken setFormatPattern() {
        return this;
    }

    @Override //
    final HistoryToken setParsePattern() {
        return this;
    }

    @Override //
    final HistoryToken setPatternKind0(final Optional<SpreadsheetPatternKind> patternKind) {
        return this;
    }
}
