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

package walkingkooka.spreadsheet.dominokit;

import elemental2.dom.Element;
import elemental2.dom.HTMLAnchorElement;
import org.dominokit.domino.ui.dropdown.DropdownAction;
import org.jboss.elemento.Elements;
import walkingkooka.Context;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataWatcher;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.text.TextStyle;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public interface AppContext extends Context {

    void addHistoryWatcher(final HistoryTokenWatcher watcher);

    void addSpreadsheetDeltaWatcher(final SpreadsheetDeltaWatcher watcher);

    SpreadsheetDeltaFetcher spreadsheetDeltaFetcher();

    void fireSpreadsheetDelta(final SpreadsheetDelta delta);

    void addSpreadsheetMetadataWatcher(final SpreadsheetMetadataWatcher watcher);

    SpreadsheetMetadataFetcher spreadsheetMetadataFetcher();

    void fireSpreadsheetMetadata(final SpreadsheetMetadata metadata);

    /**
     * Returns the current or last loaded {@link SpreadsheetMetadata}.
     */
    SpreadsheetMetadata spreadsheetMetadata();

    /**
     * Returns the current history token.
     */
    HistoryToken historyToken();

    /**
     * Pushes the given {@link HistoryToken} to the browser location.hash
     */
    void pushHistoryToken(final HistoryToken token);

    /**
     * Gives focus to the formula textbox.
     */
    void giveFcrmulaTextBoxFocus();

    /**
     * Updates the formula text and more to the {@link SpreadsheetSelection}.
     * The {@link SpreadsheetSelection} should be either a {@link walkingkooka.spreadsheet.reference.SpreadsheetCellReference} or {@link walkingkooka.spreadsheet.reference.SpreadsheetLabelName}.
     */
    void setFormula(final SpreadsheetSelection selection);

    /**
     * Getter that returns a {@link SpreadsheetCell} if one exists for the {@link SpreadsheetSelection},
     * which may be a {@link walkingkooka.spreadsheet.reference.SpreadsheetLabelName}.
     */
    Optional<SpreadsheetCell> viewportCell(final SpreadsheetSelection selection);

    /**
     * Getter that returns the ranges of the viewport window.
     */
    Set<SpreadsheetCellRange> viewportWindow();

    TextStyle viewportAllStyle(final boolean selected);

    TextStyle viewportCellStyle(final boolean selected);

    TextStyle viewportColumnHeaderStyle(final boolean selected);

    TextStyle viewportRowHeaderStyle(final boolean selected);

    /**
     * If the {@link SpreadsheetSelection} is a {@link walkingkooka.spreadsheet.reference.SpreadsheetLabelName}
     * return a {link SpreadsheetCellReference} otherwise return the original {@link SpreadsheetSelection}.
     */
    Optional<SpreadsheetSelection> nonLabelSelection(final SpreadsheetSelection selection);

    /**
     * If the {@link SpreadsheetSelection} is present, the element will be given focus.
     */
    void giveViewportFocus(final SpreadsheetSelection selection);

    /**
     * Finds an existing {@link Element} for the given {@link SpreadsheetSelection}.
     */
    Optional<Element> findViewportElement(final SpreadsheetSelection selection);

    /**
     * Creates a {@link DropdownAction} which may be added to a drop-down menu.
     * <br>
     * If a {@link HistoryToken} is present a clickable link with its HREF with the {@link HistoryToken#urlFragment()}
     * will be created.
     * When clicked it will push the given {@link HistoryToken} to the history.
     */
    default DropdownAction<HistoryToken> dropdownAction(final String text,
                                                        final Optional<HistoryToken> historyToken) {
        CharSequences.failIfNullOrEmpty(text, "text");
        Objects.requireNonNull(historyToken, "historyToken");

        final DropdownAction<HistoryToken> dropdownAction;
        if (historyToken.isPresent()) {
            final HistoryToken value = historyToken.get();

            final HTMLAnchorElement link = Elements.a()
                    .attr("href", "#" + value.urlFragment())
                    .textContent(text)
                    .element();

            dropdownAction = DropdownAction.create(
                    value,
                    link
            ).addSelectionHandler(v -> this.pushHistoryToken(v));
        } else {
            dropdownAction = DropdownAction.create(
                    null,
                    text
            );
        }

        return dropdownAction;
    }

    void debug(final Object... values);

    void error(final Object... values);
}
