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
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

import java.util.Objects;

abstract public class SpreadsheetCellPatternHistoryToken extends SpreadsheetCellHistoryToken {

    SpreadsheetCellPatternHistoryToken(final SpreadsheetId id,
                                       final SpreadsheetName name,
                                       final SpreadsheetViewportSelection viewportSelection,
                                        final SpreadsheetPatternKind patternKind) {
        super(
                id,
                name,
                viewportSelection
        );

        this.patternKind = Objects.requireNonNull(patternKind, "patternKind");
    }

    public final SpreadsheetPatternKind patternKind() {
        return this.patternKind;
    }

    private final SpreadsheetPatternKind patternKind;

    @Override
    UrlFragment cellUrlFragment() {
        return this.patternKind()
                .urlFragment()
                .append(this.patternUrlFragment());
    }

    /**
     * Sub-classes append the action and its related parameters.
     */
    abstract UrlFragment patternUrlFragment();

    @Override final HistoryToken formulaHistoryToken() {
        return this;
    }
}
