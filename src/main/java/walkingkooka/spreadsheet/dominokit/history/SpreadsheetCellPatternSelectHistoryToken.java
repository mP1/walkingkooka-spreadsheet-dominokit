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
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.pattern.PatternEditorWidget;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.text.CaseKind;

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
        if (null == patternEditorWidget) {
            patternEditorWidget = PatternEditorWidget.with(
                    this.patternKind(),
                    this.title(), // title
                    () -> this.loaded(context),
                    (pattern) -> this.save(pattern, context),
                    () -> this.close(context),
                    context
            );

            this.onPatternEditorWidgetHistoryTokenWatcherRemover = context.addHistoryWatcher(
                    this::onPatternEditorWidgetHistoryTokenChange
            );
        }
    }

    private static PatternEditorWidget patternEditorWidget;

    // Edit date/time format
    // Edit text format
    private String title() {
        return "Edit " +
                CaseKind.SNAKE.change(
                        this.patternKind().name(),
                        CaseKind.NORMAL
                ).toLowerCase();
    }

    /**
     * Takes the pattern for the matching {@link walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern}.
     */
    private String loaded(final AppContext context) {
        String loaded = ""; // if cell is absent or missing this property use a default of empty pattern.

        final Optional<SpreadsheetCell> maybeCell = context.viewportCell(
                this.viewportSelection()
                        .selection()
        );
        if (maybeCell.isPresent()) {
            final SpreadsheetCell cell = maybeCell.get();
            final SpreadsheetPatternKind patternKind = this.patternKind();

            final Optional<? extends SpreadsheetPattern> maybePattern = patternKind.isFormatPattern() ?
                    cell.formatPattern() :
                    cell.parsePattern();
            if (maybePattern.isPresent()) {
                final SpreadsheetPattern pattern = maybePattern.get();
                if (patternKind == pattern.kind()) {
                    loaded = pattern.text();
                }
            }
        }

        return loaded;
    }

    /**
     * Save the pattern and push the new {@link HistoryToken}
     */
    private void save(final String pattern,
                      final AppContext context) {
        context.pushHistoryToken(
                this.setSave(pattern)
        );
    }

    // clear the pattern part leaving just the selection history token.
    private void close(final AppContext context) {
        this.pushViewportSelectionHistoryToken(
                context
        );
    }

    private void onPatternEditorWidgetHistoryTokenChange(final HistoryToken previous,
                                                         final AppContext context) {
        if (false == context.historyToken() instanceof SpreadsheetCellPatternHistoryToken) {

            if (null != patternEditorWidget) {
                patternEditorWidget.close();
                patternEditorWidget = null;
            }

            final Runnable remover = this.onPatternEditorWidgetHistoryTokenWatcherRemover;
            if (null == remover) {
                this.onPatternEditorWidgetHistoryTokenWatcherRemover = null;
                remover.run();
            }
        }
    }

    private Runnable onPatternEditorWidgetHistoryTokenWatcherRemover;
}
