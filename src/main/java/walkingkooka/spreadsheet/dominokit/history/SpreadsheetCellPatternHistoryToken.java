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
import walkingkooka.spreadsheet.format.pattern.HasSpreadsheetPatternKind;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;

import java.util.Objects;
import java.util.Optional;

abstract public class SpreadsheetCellPatternHistoryToken extends SpreadsheetCellHistoryToken
        implements HasSpreadsheetPatternKind {

    SpreadsheetCellPatternHistoryToken(final SpreadsheetId id,
                                       final SpreadsheetName name,
                                       final SpreadsheetViewport viewport,
                                       final Optional<SpreadsheetPatternKind> patternKind) {
        super(
                id,
                name,
                viewport
        );

        this.patternKind = Objects.requireNonNull(patternKind, "patternKind");
    }

    @Override
    public final Optional<SpreadsheetPatternKind> patternKind() {
        return this.patternKind;
    }

    private final Optional<SpreadsheetPatternKind> patternKind;

    @Override
    UrlFragment cellUrlFragment() {
        return this.patternKind()
                .map(k -> k.urlFragment().append(this.patternUrlFragment()))
                .orElse(SpreadsheetUrlFragments.PATTERN);
    }

    /**
     * Sub-classes append the action and its related parameters.
     */
    abstract UrlFragment patternUrlFragment();

    @Override
    public final HistoryToken setFormula() {
        return setFormula0();
    }
}
