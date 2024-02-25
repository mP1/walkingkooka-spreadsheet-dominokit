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

package walkingkooka.spreadsheet.dominokit.ui.viewport;

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.ui.selectionmenu.SpreadsheetSelectionMenuContext;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.List;
import java.util.Set;

/**
 * A {@link SpreadsheetSelectionMenuContext} used by a {@link SpreadsheetViewportComponent}.
 */
final class SpreadsheetViewportComponentSpreadsheetSelectionMenuContext implements SpreadsheetSelectionMenuContext {

    static SpreadsheetViewportComponentSpreadsheetSelectionMenuContext with(final List<HistoryToken> recentFormatPatterns,
                                                                            final List<HistoryToken> recentParsePatterns,
                                                                            final AppContext context) {
        return new SpreadsheetViewportComponentSpreadsheetSelectionMenuContext(
                recentFormatPatterns,
                recentParsePatterns,
                context
        );
    }

    private SpreadsheetViewportComponentSpreadsheetSelectionMenuContext(final List<HistoryToken> recentFormatPatterns,
                                                                        final List<HistoryToken> recentParsePatterns,
                                                                        final AppContext context) {
        this.recentFormatPatterns = recentFormatPatterns;
        this.recentParsePatterns = recentParsePatterns;
        this.context = context;
    }

    @Override
    public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
        return this.context.addHistoryTokenWatcher(watcher);
    }

    @Override
    public Runnable addHistoryTokenWatcherOnce(final HistoryTokenWatcher watcher) {
        return this.context.addHistoryTokenWatcherOnce(watcher);
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
    public Set<SpreadsheetLabelMapping> labelMappings(final SpreadsheetSelection selection) {
        return this.context.viewportCache()
                .labelMappings(selection);
    }

    @Override
    public List<HistoryToken> recentFormatPatterns() {
        return this.recentFormatPatterns;
    }

    private final List<HistoryToken> recentFormatPatterns;

    @Override
    public List<HistoryToken> recentParsePatterns() {
        return this.recentParsePatterns;
    }

    private final List<HistoryToken> recentParsePatterns;

    @Override
    public String idPrefix() {
        return ID_PREFIX;
    }

    final static String ID_PREFIX = SpreadsheetViewportComponent.ID_PREFIX + "context-menu-";

    @Override
    public SpreadsheetMetadata spreadsheetMetadata() {
        return this.context.spreadsheetMetadata();
    }

    @Override
    public SpreadsheetSelectionSummary selectionSummary() {
        return this.context.viewportCache()
                .selectionSummary();
    }

    private final AppContext context;

    public String toString() {
        return this.context.toString();
    }
}
