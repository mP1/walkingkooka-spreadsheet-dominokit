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

package walkingkooka.spreadsheet.dominokit.ui.toolbar;

import elemental2.dom.Event;
import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.ui.NopComponentLifecycleOpenGiveFocus;
import walkingkooka.spreadsheet.dominokit.ui.NopComponentLifecycleRefresh;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetCellComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetSelectionSummary;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

import java.util.Optional;

/**
 * A link ui that may exist withing a toolbar, which actives the pattern editor by pushing a new {@link HistoryToken}.
 */
abstract class SpreadsheetToolbarComponentItemAnchorPattern<P extends SpreadsheetPattern, C extends SpreadsheetToolbarComponentItemAnchorPattern<P, C>>
        extends SpreadsheetToolbarComponentItemAnchor<C>
        implements SpreadsheetCellComponentLifecycle,
        NopComponentLifecycleRefresh,
        NopComponentLifecycleOpenGiveFocus {

    SpreadsheetToolbarComponentItemAnchorPattern(final String id,
                                                 final Optional<Icon<?>> icon,
                                                 final String text,
                                                 final String tooltip,
                                                 final HistoryTokenContext context) {
        super(
                id,
                icon,
                text,
                tooltip,
                context
        );
    }

    // SpreadsheetToolbarComponentItemLink............................................................................

    @Override //
    final void onFocus(final Event event) {
        final HistoryTokenContext context = this.context;

        context.historyToken()
                .anchoredSelectionHistoryTokenOrEmpty()
                .map(
                        t -> t.setPatternKind(
                                this.patternKind
                        ).clearPatternKind()
                )
                .ifPresent(context::pushHistoryToken);
    }

    // ComponentLifecycle...............................................................................................

    @Override
    public final void refresh(final AppContext context) {
        final Optional<SpreadsheetPatternKind> patternKind = this.pattern(
                context.spreadsheetViewportCache()
                        .selectionSummary()
        ).map(SpreadsheetPattern::patternKind);

        this.patternKind = patternKind.isPresent() ?
                patternKind :
                Optional.of(
                        this.defaultSpreadsheetPatternKind()
                );

        this.anchor.setHistoryToken(
                context.historyToken()
                        .anchoredSelectionHistoryTokenOrEmpty()
                        .map(
                                t -> t.setPatternKind(
                                        this.patternKind
                                )
                        )
        ).setChecked(patternKind.isPresent());
    }

    abstract Optional<P> pattern(final SpreadsheetSelectionSummary summary);

    /**
     * This is updated by {@link #refresh(AppContext)} and will contain the best {@link SpreadsheetPatternKind} to open
     * when this link is clicked.
     */
    private Optional<SpreadsheetPatternKind> patternKind = Optional.empty();

    abstract SpreadsheetPatternKind defaultSpreadsheetPatternKind();
}
