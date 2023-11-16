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

import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.icons.lib.Icons;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;

/**
 * A button component that may exist withing a toolbar, which updates the a {@link TextStylePropertyName} with a fixed
 * {@link Object value} when selected(clicked).
 */
final class SpreadsheetViewportToolbarComponentItemButtonTextStyleClear extends SpreadsheetViewportToolbarComponentItemButton {

    static SpreadsheetViewportToolbarComponentItemButtonTextStyleClear with(final HistoryTokenContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetViewportToolbarComponentItemButtonTextStyleClear(
                context
        );
    }

    private SpreadsheetViewportToolbarComponentItemButtonTextStyleClear(final HistoryTokenContext context) {
        super(
                SpreadsheetViewportToolbarComponent.id(
                        PROPERTY,
                        Optional.empty()
                ),
                Icons.format_clear(),
                "Clear styling",
                context
        );
        this.button.addEventListener(
                EventType.click,
                (event) -> this.onClick()
        ).addEventListener(
                EventType.focus,
                (event) -> this.onFocus()
        );

        this.element().tabIndex = 0;
    }

    /**
     * When clicked perform a save on the {@link walkingkooka.spreadsheet.dominokit.history.HistoryToken} and push that.
     */
    private void onClick() {
        final HistoryTokenContext context = this.context;

        context.historyToken()
                .selectionHistoryTokenOrEmpty()
                .map(
                        t -> t.setStyle(PROPERTY)
                                .clearSave()
                ).ifPresent(context::pushHistoryToken);
    }

    /**
     * Upon focus the history token is set {@link walkingkooka.spreadsheet.reference.SpreadsheetSelection} and the {@link TextStylePropertyName}.
     */
    private void onFocus() {
        final HistoryTokenContext context = this.context;

        context.historyToken()
                .selectionHistoryTokenOrEmpty()
                .map(
                        t -> t.setStyle(PROPERTY)
                ).ifPresent(context::pushHistoryToken);
    }

    @Override
    void onToolbarRefreshBegin() {
        this.cellsWithNonEmptyStyleCounter = 0;
    }

    /**
     * Counts the number of cells in the selection with non empty {@link TextStyle}
     */
    private int cellsWithNonEmptyStyleCounter;

    /**
     * Increment the {@link #cellsWithNonEmptyStyleCounter} if the given cell has non-empty {@link TextStyle}.
     */
    @Override
    void onToolbarRefreshSelectedCell(final SpreadsheetCell cell,
                                      final AppContext context) {
        final Optional<TextNode> maybeFormatted = cell.formatted();
        if (maybeFormatted.isPresent()) {
            if (false == cell.style().isEmpty()) {
                this.cellsWithNonEmptyStyleCounter++;
            }
        }
    }

    /**
     * Counts the number of cells with non empty {@link TextStyle}.
     */
    @Override
    void onToolbarRefreshEnd(final int cellPresentCount,
                             final AppContext context) {
        final int setCellCounter = this.cellsWithNonEmptyStyleCounter;
        final boolean selected = setCellCounter == cellPresentCount;

        this.setButtonSelected(
                selected,
                context
        );

        context.debug("SpreadsheetViewportToolbarComponentItemButtonTextStyleClear.onToolbarRefreshEnd " + PROPERTY + " " + setCellCounter + "/" + cellPresentCount + " selected: " + selected);
    }

    private static final TextStylePropertyName<Void> PROPERTY = TextStylePropertyName.ALL;
}
