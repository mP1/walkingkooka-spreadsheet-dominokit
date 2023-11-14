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
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;

/**
 * This {@link HistoryToken} is used to represent the selection of the toolbar format-pattern icon.
 */
public final class SpreadsheetCellPatternToolbarParseHistoryToken extends SpreadsheetCellPatternToolbarHistoryToken {

    static SpreadsheetCellPatternToolbarParseHistoryToken with(final SpreadsheetId id,
                                                               final SpreadsheetName name,
                                                               final AnchoredSpreadsheetSelection selection) {
        return new SpreadsheetCellPatternToolbarParseHistoryToken(
                id,
                name,
                selection
        );
    }

    private SpreadsheetCellPatternToolbarParseHistoryToken(final SpreadsheetId id,
                                                           final SpreadsheetName name,
                                                           final AnchoredSpreadsheetSelection selection) {
        super(
                id,
                name,
                selection
        );
    }

    @Override
    public HistoryToken setIdAndName(final SpreadsheetId id,
                                     final SpreadsheetName name) {
        return with(
                id,
                name,
                this.selection()
        );
    }

    @Override
    UrlFragment cellUrlFragment() {
        return SpreadsheetUrlFragments.PARSE_PATTERN;
    }

    @Override
    boolean isCompatible(final SpreadsheetPatternKind kind) {
        return kind.isParsePattern();
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // give focus to viewport icon
    }
}
