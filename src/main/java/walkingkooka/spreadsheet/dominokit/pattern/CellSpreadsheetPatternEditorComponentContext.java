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

package walkingkooka.spreadsheet.dominokit.pattern;

import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellPatternSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellPatternSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataWatcher;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.pattern.HasSpreadsheetPatternKind;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link SpreadsheetPatternEditorComponentContext} for editing patterns for a cell.
 */
final class CellSpreadsheetPatternEditorComponentContext implements SpreadsheetPatternEditorComponentContext {

    static CellSpreadsheetPatternEditorComponentContext with(final AppContext context) {
        Objects.requireNonNull(context, "context");

        return new CellSpreadsheetPatternEditorComponentContext(context);
    }

    private CellSpreadsheetPatternEditorComponentContext(final AppContext context) {
        this.context = context;
    }

    // ComponentLifecycleMatcher........................................................................................

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetCellPatternSaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetCellPatternSelectHistoryToken;
    }

    // SpreadsheetPatternEditorComponentContext.........................................................................

    @Override
    public SpreadsheetPatternKind patternKind() {
        return this.historyToken()
                .cast(HasSpreadsheetPatternKind.class)
                .patternKind()
                .get();
    }

    /**
     * Switches the editor to the given {@link SpreadsheetPatternKind}.
     */
    public void setPatternKind(final SpreadsheetPatternKind patternKind) {
        final AppContext context = this.context;

        context.debug("CellSpreadsheetPatternEditorComponentContext.setPatternKind " + patternKind);
        context.pushHistoryToken(
                this.historyToken()
                        .setPatternKind(
                                Optional.of(patternKind)
                        )
        );
    }

    /**
     * Returns the pattern text for the current {@link #patternKind()}.
     */
    @Override
    public String loaded() {
        String loaded = ""; // if cell is absent or missing this property use a default of empty pattern.

        final Optional<SpreadsheetCell> maybeCell = this.context.viewportCell(
                this.historyToken()
                        .cast(SpreadsheetCellPatternSelectHistoryToken.class)
                        .viewportSelection()
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
    @Override
    public void save(final String pattern) {
        this.pushHistoryToken(
                this.historyToken()
                        .setSave(pattern)
        );
    }

    /**
     * Pushes a new {@link HistoryToken} which will result in the removal of the pattern from the selection.
     */
    @Override
    public void remove() {
        this.save("");
    }

    // clear the pattern part leaving just the selection history token.
    @Override
    public void close() {
        this.pushHistoryToken(
                this.historyToken()
                        .close()
        );
    }

    @Override
    public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
        return this.context.addHistoryTokenWatcher(watcher);
    }

    @Override
    public HistoryToken historyToken() {
        return this.context.historyToken();
    }

    @Override
    public void pushHistoryToken(final HistoryToken token) {
        this.context.pushHistoryToken(token);
    }

    @Override
    public void fireCurrentHistoryToken() {
        this.context.fireCurrentHistoryToken();
    }

    @Override
    public SpreadsheetFormatterContext spreadsheetFormatterContext() {
        return this.context.spreadsheetMetadata()
                .formatterContext(
                        LocalDateTime::now,
                        (s) -> {
                            throw new UnsupportedOperationException();
                        }
                );
    }

    @Override
    public void giveFocus(final Runnable focus) {
        this.context.giveFocus(focus);
    }

    @Override
    public Runnable addSpreadsheetDeltaWatcher(final SpreadsheetDeltaWatcher watcher) {
        return this.context.addSpreadsheetDeltaWatcher(watcher);
    }

    @Override
    public Runnable addSpreadsheetMetadataWatcher(final SpreadsheetMetadataWatcher watcher) {
        return this.context.addSpreadsheetMetadataWatcher(watcher);
    }

    @Override
    public void debug(final Object... values) {
        this.context.debug(values);
    }

    @Override
    public void error(final Object... values) {
        this.context.error(values);
    }

    private final AppContext context;
}
