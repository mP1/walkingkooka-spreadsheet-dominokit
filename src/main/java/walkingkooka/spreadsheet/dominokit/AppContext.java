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
import org.dominokit.domino.ui.dropdown.DropdownAction;
import walkingkooka.Context;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.dom.Anchor;
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
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.text.TextStyle;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public interface AppContext extends HistoryTokenContext, LoggingContext, Context {

    /**
     * Returns the current or active {@link Locale}.
     */
    Locale locale();

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

        final HistoryToken value = historyToken.orElse(null);

        final Anchor anchor = Anchor.empty()
                .setHistoryToken(value)
                .setTextContent(text);

        return DropdownAction.create(
                value,
                anchor.element()
        ).addSelectionHandler(this::pushHistoryToken);
    }
}
