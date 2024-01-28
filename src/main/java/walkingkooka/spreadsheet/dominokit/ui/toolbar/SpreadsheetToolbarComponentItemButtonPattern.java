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
import elemental2.dom.HTMLElement;
import org.dominokit.domino.ui.icons.MdiIcon;
import walkingkooka.collect.map.Maps;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.ui.NopRefreshComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetCellComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetDominoKitColor;
import walkingkooka.spreadsheet.dominokit.ui.VisibleComponentLifecycle;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.tree.text.TextStyle;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * A button ui that may exist withing a toolbar, which actives the pattern editor by pushing a new {@link HistoryToken}.
 */
abstract class SpreadsheetToolbarComponentItemButtonPattern<T extends SpreadsheetPattern> extends SpreadsheetToolbarComponentItemButton
        implements SpreadsheetCellComponentLifecycle,
        NopRefreshComponentLifecycle,
        VisibleComponentLifecycle<HTMLElement> {

    SpreadsheetToolbarComponentItemButtonPattern(final String id,
                                                 final MdiIcon icon,
                                                 final String tooltip,
                                                 final HistoryTokenContext context) {
        super(
                id,
                icon,
                tooltip,
                context
        );
    }

    /**
     * When clicked perform a save on the {@link HistoryToken} and push that.
     */
    @Override //
    final void onClick(final Event event) {
        final HistoryTokenContext context = this.context;

        context.historyToken()
                .anchoredSelectionHistoryTokenOrEmpty()
                .map(
                        t -> t.setPatternKind(
                                Optional.of(
                                        this.spreadsheetPatternKind()
                                )
                        )
                ).ifPresent(context::pushHistoryToken);
    }

    @Override //
    final void onFocus(final Event event) {
        final HistoryTokenContext context = this.context;

        context.historyToken()
                .anchoredSelectionHistoryTokenOrEmpty()
                .map(HistoryToken::clearPatternKind)
                .ifPresent(context::pushHistoryToken);
    }

    @Override final void onToolbarRefreshBegin() {
        this.patternKindToCount.clear();
    }


    /**
     * Increment the counters for any present {@link SpreadsheetPattern}.
     */
    @Override //
    final void onToolbarRefreshSelectedCell(final SpreadsheetCell cell,
                                            final AppContext context) {
        final Map<SpreadsheetPatternKind, Integer> patternKindToCount = this.patternKindToCount;

        final Optional<T> maybeFormatPattern = this.pattern(cell);
        if (maybeFormatPattern.isPresent()) {
            final SpreadsheetPatternKind formatPatternKind = maybeFormatPattern.get()
                    .kind();
            Integer count = patternKindToCount.get(formatPatternKind);
            if (null == count) {
                count = 0;
            }
            patternKindToCount.put(
                    formatPatternKind,
                    count++
            );
        }
    }

    /**
     * Extracts a pattern from the cell.
     */
    abstract Optional<T> pattern(final SpreadsheetCell cell);


    /**
     * Counts the number of cells with non empty {@link TextStyle}.
     */
    @Override //
    final void onToolbarRefreshEnd(final int cellPresentCount,
                                   final AppContext context) {
        final Map<SpreadsheetPatternKind, Integer> patternKindToCount = this.patternKindToCount;
        final boolean selected = false == patternKindToCount.isEmpty();

        this.setButtonSelected(
                selected,
                SpreadsheetDominoKitColor.TOOLBAR_ICON_SELECTED_BACKGROUND_COLOR
        );

        context.debug(this.getClass().getSimpleName() + ".onToolbarRefreshEnd selected: " + selected + " patternToKindCount: " + patternKindToCount);
    }

    final SpreadsheetPatternKind spreadsheetPatternKind() {
        SpreadsheetPatternKind kind = this.defaultSpreadsheetPatternKind();
        int count = 0;

        for (final Entry<SpreadsheetPatternKind, Integer> kindAndCount : this.patternKindToCount.entrySet()) {
            final Integer possibleCount = kindAndCount.getValue();
            if (possibleCount > count) {
                count = 0;
                kind = kindAndCount.getKey();
            }
        }

        return kind;
    }

    abstract SpreadsheetPatternKind defaultSpreadsheetPatternKind();

    final Map<SpreadsheetPatternKind, Integer> patternKindToCount = Maps.sorted();
}
