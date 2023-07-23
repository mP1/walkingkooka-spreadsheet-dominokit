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
import org.dominokit.domino.ui.icons.Icons;
import org.jboss.elemento.EventType;
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
final class SpreadsheetViewportToolbarComponentButtonPattern extends SpreadsheetViewportToolbarComponentButton {

    static SpreadsheetViewportToolbarComponentButtonPattern with(final HistoryTokenContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetViewportToolbarComponentButtonPattern(
                context
        );
    }

    private SpreadsheetViewportToolbarComponentButtonPattern(final HistoryTokenContext context) {
        super(
                SpreadsheetViewportToolbar.pattern(),
                Icons.ALL.format_text_mdi(),
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
                .viewportSelectionHistoryTokenOrEmpty()
                .map(
                        t -> t.setPatternKind(this.spreadsheetPatternKind())
                                .setSave("")
                ).ifPresent(context::pushHistoryToken);
    }

    /**
     * Temporary fix for https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/722.
     * Previously giving focus to the pattern icon would immediately open the {@link walkingkooka.spreadsheet.dominokit.pattern.SpreadsheetPatternEditorWidget}.
     * This behaviour meant clicking the CLOSE button would give focus to the PATTERN ICON which would immediately re-open
     * the {@link walkingkooka.spreadsheet.dominokit.pattern.SpreadsheetPatternEditorWidget}.
     * This FIX also means tabbing to the PATTERN ICON results in it losing focus, this will be fixed by
     * https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/759.
     */
    private void onFocus(final Event event) {
        final HistoryTokenContext context = this.context;

        context.historyToken()
                .viewportSelectionHistoryTokenOrEmpty()
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
        final boolean selected = patternKindToCount.size() > 0;

        this.setButtonSelected(
                selected,
                context
        );

        context.debug("SpreadsheetViewportToolbarComponentButtonPattern.onToolbarRefreshEnd selected: " + selected + " patternToKindCount: " + patternKindToCount);
    }

    private void setButtonSelected(final boolean selected,
                                   final AppContext context) {
        TextStyle style = BUTTON_STYLE;
        if (selected) {
            style = style.merge(
                    context.selectedIconStyle()
            );
        }

        this.button.style(
                style.css()
        );
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
