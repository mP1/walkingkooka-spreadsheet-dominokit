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

package walkingkooka.spreadsheet.dominokit.viewport;

import elemental2.dom.Event;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.icons.lib.Icons;
import walkingkooka.collect.map.Maps;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.tree.text.TextStyle;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

/**
 * A button component that may exist withing a toolbar, which actives the pattern editor.
 */
final class SpreadsheetViewportToolbarComponentItemButtonPattern extends SpreadsheetViewportToolbarComponentItemButton {

    static SpreadsheetViewportToolbarComponentItemButtonPattern with(final HistoryTokenContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetViewportToolbarComponentItemButtonPattern(
                context
        );
    }

    private SpreadsheetViewportToolbarComponentItemButtonPattern(final HistoryTokenContext context) {
        super(
                SpreadsheetViewportToolbarComponent.pattern(),
                Icons.format_text(),
                "Pattern edit",
                context
        );
        this.button.addEventListener(
                EventType.click,
                this::onClick
        ).addEventListener(
                EventType.focus,
                this::onFocus
        );

        this.element().tabIndex = 0;
    }

    /**
     * When clicked perform a save on the {@link walkingkooka.spreadsheet.dominokit.history.HistoryToken} and push that.
     */
    private void onClick(final Event event) {
        final HistoryTokenContext context = this.context;

        context.historyToken()
                .selectionHistoryTokenOrEmpty()
                .map(
                        t -> t.setPatternKind(
                                Optional.of(
                                        this.spreadsheetPatternKind()
                                )
                        ).setSave("")
                ).ifPresent(context::pushHistoryToken);
    }

    private void onFocus(final Event event) {
        final HistoryTokenContext context = this.context;

        context.historyToken()
                .selectionHistoryTokenOrEmpty()
                .map(t -> t.setPatternKind(Optional.empty()))
                .ifPresent(context::pushHistoryToken);
    }

    @Override
    void onToolbarRefreshBegin() {
        this.patternKindToCount.clear();
    }

    /**
     * Increment the counters for any present {@link SpreadsheetFormatPattern} and {@link SpreadsheetParsePattern}
     */
    @Override
    void onToolbarRefreshSelectedCell(final SpreadsheetCell cell,
                                      final AppContext context) {
        final Map<SpreadsheetPatternKind, Integer> patternKindToCount = this.patternKindToCount;

        final Optional<SpreadsheetFormatPattern> maybeFormatPattern = cell.formatPattern();
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

        final Optional<SpreadsheetParsePattern> maybeParsePattern = cell.parsePattern();
        if (maybeParsePattern.isPresent()) {
            final SpreadsheetPatternKind parsePatternKind = maybeParsePattern.get()
                    .kind();
            Integer count = patternKindToCount.get(parsePatternKind);
            if (null == count) {
                count = 0;
            }
            patternKindToCount.put(
                    parsePatternKind,
                    count++
            );
        }
    }

    /**
     * Counts the number of cells with non empty {@link TextStyle}.
     */
    @Override
    void onToolbarRefreshEnd(final int cellPresentCount,
                             final AppContext context) {
        final Map<SpreadsheetPatternKind, Integer> patternKindToCount = this.patternKindToCount;
        final boolean selected = false == patternKindToCount.isEmpty();

        this.setButtonSelected(
                selected,
                context
        );

        context.debug("SpreadsheetViewportToolbarComponentItemButtonPattern.onToolbarRefreshEnd selected: " + selected + " patternToKindCount: " + patternKindToCount);
    }

    private SpreadsheetPatternKind spreadsheetPatternKind() {
        SpreadsheetPatternKind kind = SpreadsheetPatternKind.TEXT_FORMAT_PATTERN;
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

    private final Map<SpreadsheetPatternKind, Integer> patternKindToCount = Maps.sorted();
}
