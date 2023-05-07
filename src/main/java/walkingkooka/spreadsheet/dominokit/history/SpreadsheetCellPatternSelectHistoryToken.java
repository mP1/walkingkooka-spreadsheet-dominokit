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
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

import java.util.Optional;

public final class SpreadsheetCellPatternSelectHistoryToken extends SpreadsheetCellPatternHistoryToken {

    static SpreadsheetCellPatternSelectHistoryToken with(final SpreadsheetId id,
                                                         final SpreadsheetName name,
                                                         final SpreadsheetViewportSelection viewportSelection,
                                                         final SpreadsheetPatternKind patternKind) {
        return new SpreadsheetCellPatternSelectHistoryToken(
                id,
                name,
                viewportSelection,
                patternKind
        );
    }

    private SpreadsheetCellPatternSelectHistoryToken(final SpreadsheetId id,
                                                     final SpreadsheetName name,
                                                     final SpreadsheetViewportSelection viewportSelection,
                                                     final SpreadsheetPatternKind patternKind) {
        super(
                id,
                name,
                viewportSelection,
                patternKind
        );
    }

    @Override
    UrlFragment patternUrlFragment() {
        return SELECT;
    }

    @Override
    public HistoryToken setIdAndName(final SpreadsheetId id,
                                     final SpreadsheetName name) {
        return with(
                id,
                name,
                this.viewportSelection(),
                this.patternKind()
        );
    }

    @Override
    HistoryToken setPattern0(final SpreadsheetPatternKind patternKind) {
        return this;
    }

    @Override
    HistoryToken setSave0(final String pattern) {
        final SpreadsheetPatternKind patternKind = this.patternKind();

        return cellPatternSave(
                this.id(),
                this.name(),
                this.viewportSelection(),
                patternKind,
                Optional.ofNullable(
                        pattern.isEmpty() ?
                                null :
                                patternKind.parse(pattern)
                )
        );
    }

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        // show cell pattern edit UI
    }
}
