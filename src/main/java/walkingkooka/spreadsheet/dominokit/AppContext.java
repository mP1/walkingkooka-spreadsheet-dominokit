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

import org.dominokit.domino.ui.menu.AbstractMenuItem;
import walkingkooka.Context;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.component.Anchor;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetLabelMappingFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetLabelMappingFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.meta.HasSpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.text.TextStyle;

import java.util.Objects;
import java.util.Optional;

public interface AppContext extends CanGiveFocus,
        HasSpreadsheetMetadata,
        HistoryTokenContext,
        LoggingContext,
        Context {

    // delta............................................................................................................

    Runnable addSpreadsheetDeltaWatcher(final SpreadsheetDeltaFetcherWatcher watcher);

    Runnable addSpreadsheetDeltaWatcherOnce(final SpreadsheetDeltaFetcherWatcher watcher);

    SpreadsheetDeltaFetcher spreadsheetDeltaFetcher();

    // labelMapping............................................................................................................

    Runnable addSpreadsheetLabelMappingWatcher(final SpreadsheetLabelMappingFetcherWatcher watcher);

    Runnable addSpreadsheetLabelMappingWatcherOnce(final SpreadsheetLabelMappingFetcherWatcher watcher);

    SpreadsheetLabelMappingFetcher spreadsheetLabelMappingFetcher();

    // metadata..........................................................................................................

    Runnable addSpreadsheetMetadataWatcher(final SpreadsheetMetadataFetcherWatcher watcher);

    Runnable addSpreadsheetMetadataWatcherOnce(final SpreadsheetMetadataFetcherWatcher watcher);

    SpreadsheetMetadataFetcher spreadsheetMetadataFetcher();

    /**
     * Returns the current or last loaded {@link SpreadsheetMetadata}.
     */
    @Override
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
     * Creates a {@link SpreadsheetViewport} with the provided {@link AnchoredSpreadsheetSelection}.
     */
    SpreadsheetViewport viewport(final Optional<AnchoredSpreadsheetSelection> selection);

    /**
     * A cache for the viewport cache.
     */
    SpreadsheetViewportCache viewportCache();

    /**
     * Getter that returns a {@link SpreadsheetCell} if one exists for the {@link SpreadsheetSelection},
     * which may be a {@link walkingkooka.spreadsheet.reference.SpreadsheetLabelName}.
     */
    Optional<SpreadsheetCell> viewportCell(final SpreadsheetSelection selection);

    TextStyle viewportAllStyle(final boolean selected);

    TextStyle viewportCellStyle(final boolean selected);

    TextStyle viewportColumnHeaderStyle(final boolean selected);

    TextStyle viewportRowHeaderStyle(final boolean selected);

    /**
     * Creates an ANCHOR with the given text and if a {@link HistoryToken} is passed, it will be pushed that if this
     * menu item clicked or selected with ENTER.
     */
    default AbstractMenuItem<Void> menuItem(final String text,
                                            final Optional<HistoryToken> historyToken) {
        Objects.requireNonNull(historyToken, "historyToken");

        final Anchor anchor = Anchor.empty()
                .setHistoryToken(historyToken)
                .setTextContent(text);

        final AbstractMenuItem<Void> menu = new AbstractMenuItem<>() {

        };
        menu.appendChild(anchor);

        if (historyToken.isPresent()) {
            menu.addSelectionHandler(
                    (ignored) -> this.pushHistoryToken(
                            historyToken.get()
                    )
            );
            // need to kill margin other menu items with links wont line up with text-only menu items.
            anchor.element()
                    .style
                    .setProperty("margin-left", "0");
        }
        return menu;
    }
}
