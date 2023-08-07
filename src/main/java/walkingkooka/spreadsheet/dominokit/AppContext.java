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
import org.dominokit.domino.ui.menu.AbstractMenuItem;
import walkingkooka.Context;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.dom.Anchor;
import walkingkooka.spreadsheet.dominokit.dom.CanGiveFocus;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataWatcher;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.text.TextStyle;

import java.util.Objects;
import java.util.Optional;

public interface AppContext extends CanGiveFocus,
        HistoryTokenContext,
        LoggingContext,
        Context {

    // delta............................................................................................................

    Runnable addSpreadsheetDeltaWatcher(final SpreadsheetDeltaWatcher watcher);

    SpreadsheetDeltaFetcher spreadsheetDeltaFetcher();

    // metadata..........................................................................................................

    Runnable addSpreadsheetMetadataWatcher(final SpreadsheetMetadataWatcher watcher);

    SpreadsheetMetadataFetcher spreadsheetMetadataFetcher();

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
     * A cache for the viewport cache.
     */
    SpreadsheetViewportCache viewportCache();

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
    Optional<SpreadsheetSelection> viewportNonLabelSelection();

    TextStyle viewportAllStyle(final boolean selected);

    TextStyle viewportCellStyle(final boolean selected);

    TextStyle viewportColumnHeaderStyle(final boolean selected);

    TextStyle viewportRowHeaderStyle(final boolean selected);

    /**
     * If the {@link SpreadsheetSelection} is present, the element will be given focus.
     */
    void giveViewportFocus(final SpreadsheetSelection selection);

    /**
     * Finds an existing {@link Element} for the given {@link SpreadsheetSelection}.
     */
    Optional<Element> findViewportElement(final SpreadsheetSelection selection);

    /**
     * Creates an ANCHOR with the given text and if a {@link HistoryToken} is passed, it will be pushed that if this
     * menu item clicked or selected with ENTER.
     */
    default AbstractMenuItem<Void> menuItem(final String text,
                                            final Optional<HistoryToken> historyToken) {
        Objects.requireNonNull(historyToken, "historyToken");

        final HistoryToken value = historyToken.orElse(null);

        final Anchor anchor = Anchor.empty()
                .setHistoryToken(value)
                .setTextContent(text);

        final AbstractMenuItem<Void> menu = new AbstractMenuItem<>() {

        };
        menu.appendChild(anchor);
        menu.addSelectionHandler((ignored) -> this.pushHistoryToken(value));
        return menu;
    }
}
