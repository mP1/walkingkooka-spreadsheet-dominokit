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
import walkingkooka.spreadsheet.dominokit.pattern.SpreadsheetPatternEditorComponent;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
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
                Optional.of(patternKind)
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
                        .get()
        );
    }

    @Override
    HistoryToken setPatternKind0(final Optional<SpreadsheetPatternKind> patternKind) {
        return this.patternKind().equals(patternKind) ?
                this :
                this.replacePatternKind(patternKind);
    }

    private HistoryToken replacePatternKind(final Optional<SpreadsheetPatternKind> patternKind) {
        return new SpreadsheetCellPatternSelectHistoryToken(
                this.id(),
                this.name(),
                this.viewportSelection(),
                patternKind.get()
        );
    }

    @Override
    HistoryToken setSave0(final String pattern) {
        final SpreadsheetPatternKind patternKind = this.patternKind()
                .get();


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
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        if (null == spreadsheetPatternEditorComponent) {
            spreadsheetPatternEditorComponent = SpreadsheetPatternEditorComponent.with(
                    SpreadsheetCellPatternSelectHistoryTokenSpreadsheetPatternEditorComponentContext.with(context)
            );
            spreadsheetPatternEditorComponent.open();
            onPatternEditorComponentHistoryTokenWatcherRemover = context.addHistoryWatcher(
                    this::onPatternEditorComponentHistoryTokenChange
            );

            onPatternEditorComponentSpreadsheetDeltaWatcherRemover = context.addSpreadsheetDeltaWatcher(
                    this::onPatternEditorComponentSpreadsheetDelta
            );
        } else {
            spreadsheetPatternEditorComponent.refresh();
        }
    }

    private static SpreadsheetPatternEditorComponent spreadsheetPatternEditorComponent;

    private void onPatternEditorComponentHistoryTokenChange(final HistoryToken previous,
                                                            final AppContext context) {
        if (false == context.historyToken() instanceof SpreadsheetCellPatternHistoryToken) {

            if (null != spreadsheetPatternEditorComponent) {
                try {
                    spreadsheetPatternEditorComponent.close();
                    onPatternEditorComponentHistoryTokenWatcherRemover.run();
                    onPatternEditorComponentSpreadsheetDeltaWatcherRemover.run();
                } finally {
                    spreadsheetPatternEditorComponent = null;
                    onPatternEditorComponentHistoryTokenWatcherRemover = null;
                    onPatternEditorComponentSpreadsheetDeltaWatcherRemover = null;
                }
            }
        }
    }

    private static Runnable onPatternEditorComponentHistoryTokenWatcherRemover;

    private void onPatternEditorComponentSpreadsheetDelta(final SpreadsheetDelta delta,
                                                          final AppContext context) {
        if (context.historyToken() instanceof SpreadsheetCellPatternHistoryToken) {
            if (null != spreadsheetPatternEditorComponent) {
                spreadsheetPatternEditorComponent.refresh();
            }
        }
    }

    private static Runnable onPatternEditorComponentSpreadsheetDeltaWatcherRemover;
}
