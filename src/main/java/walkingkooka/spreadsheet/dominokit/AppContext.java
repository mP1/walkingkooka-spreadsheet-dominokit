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
import walkingkooka.spreadsheet.SpreadsheetViewportWindows;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.text.TextStyle;

import java.util.Objects;
import java.util.Optional;

public interface AppContext extends LoggingContext, Context {

    // delta............................................................................................................

    void addSpreadsheetDeltaWatcher(final SpreadsheetDeltaWatcher watcher);

    SpreadsheetDeltaFetcher spreadsheetDeltaFetcher();

    // metadata..........................................................................................................

    void addSpreadsheetMetadataWatcher(final SpreadsheetMetadataWatcher watcher);

    SpreadsheetMetadataFetcher spreadsheetMetadataFetcher();

    void fireSpreadsheetMetadata(final SpreadsheetMetadata metadata);

    /**
     * Returns the current or last loaded {@link SpreadsheetMetadata}.
     */
    SpreadsheetMetadata spreadsheetMetadata();

    // json............................................................................................................

    /**
     * {@see JsonNodeMarshallContext}
     */
    JsonNodeMarshallContext marshallContext();

    /**
     * {@see JsonNodeUnmarshallContext}
     */
    JsonNodeUnmarshallContext unmarshallContext();

    // history..........................................................................................................

    /**
     * Adds a new {@link HistoryTokenWatcher}, the returned {@link Runnable} may be invoked to remove the watcher.
     */
    Runnable addHistoryWatcher(final HistoryTokenWatcher watcher);

    /**
     * Returns the current history token.
     */
    HistoryToken historyToken();

    /**
     * Pushes the given {@link HistoryToken} to the browser location.hash
     */
    void pushHistoryToken(final HistoryToken token);

    // UI...............................................................................................................

    /**
     * A {@link TextStyle} that should be added to any selected Icon.
     * For now this means a background-color to yellow.
     */
    TextStyle selectedIconStyle();

    // viewport.........................................................................................................

    /**
     * Gives focus to the formula textbox.
     */
    void giveFormulaTextBoxFocus();

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
     * Getter that returns the current {@link SpreadsheetSelection} by checking the {@link #historyToken()}.
     */
    default Optional<SpreadsheetSelection> viewportSelection() {
        return this.historyToken()
                .viewportSelectionOrEmpty()
                .map(SpreadsheetViewportSelection::selection);
    }

    /**
     * Returns a {@link SpreadsheetSelection} resolving labels for the current viewport selection.
     * This basically exists to resolve labels to cells or cell-ranges.
     */
    default Optional<SpreadsheetSelection> viewportNonLabelSelection() {
        return this.viewportSelection()
                .flatMap(this::nonLabelSelection);
    }

    /**
     * Getter that returns the ranges of the viewport window.
     */
    SpreadsheetViewportWindows viewportWindow();

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
            ).addSelectionHandler(this::pushHistoryToken);
        } else {
            dropdownAction = DropdownAction.create(
                    null,
                    text
            );
        }

        return dropdownAction;
    }
}
