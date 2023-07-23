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
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportWidgetWatcher;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

import java.util.Optional;

public final class SpreadsheetCellSelectHistoryToken extends SpreadsheetCellHistoryToken
        implements SpreadsheetViewportWidgetWatcher {

    static SpreadsheetCellSelectHistoryToken with(final SpreadsheetId id,
                                                  final SpreadsheetName name,
                                                  final SpreadsheetViewportSelection viewportSelection) {
        return new SpreadsheetCellSelectHistoryToken(
                id,
                name,
                viewportSelection
        );
    }

    private SpreadsheetCellSelectHistoryToken(final SpreadsheetId id,
                                              final SpreadsheetName name,
                                              final SpreadsheetViewportSelection viewportSelection) {
        super(
                id,
                name,
                viewportSelection
        );
    }

    @Override
    UrlFragment cellUrlFragment() {
        return SELECT;
    }

    @Override
    public HistoryToken setFormula() {
        return formula(
                this.id(),
                this.name(),
                this.viewportSelection()
        );
    }

    @Override
    public HistoryToken setIdAndName(final SpreadsheetId id,
                                     final SpreadsheetName name) {
        return with(
                id,
                name,
                this.viewportSelection()
        );
    }

    @Override
    HistoryToken setPatternKind0(final Optional<SpreadsheetPatternKind> patternKind) {
        final SpreadsheetId id = this.id();
        final SpreadsheetName name = this.name();
        final SpreadsheetViewportSelection viewportSelection = this.viewportSelection();

        return patternKind.isPresent() ?
                cellPattern(
                        id,
                        name,
                        viewportSelection,
                        patternKind.get()
                ) :
                cellPatternToolbar(
                        id,
                        name,
                        viewportSelection
                );
    }

    @Override
    HistoryToken setSave0(final String value) {
        return this;
    }

    @Override
    public void onHistoryTokenChange0(final HistoryToken previous,
                                      final AppContext context) {
        // nop
    }

    // SpreadsheetViewportWidgetRenderWatcher...........................................................................

    /**
     * Give focus to the selection.
     */
    @Override
    public void onAfterSpreadsheetViewportWidgetRender(final AppContext context) {
        context.setFormula(
                this.viewportSelection()
                        .selection()
        );
        this.giveViewportFocus(context);
    }
}
