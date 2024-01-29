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

import walkingkooka.Context;
import walkingkooka.datetime.HasNow;
import walkingkooka.net.UrlQueryString;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.net.HasSpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.net.HasSpreadsheetLabelMappingFetcher;
import walkingkooka.spreadsheet.dominokit.net.HasSpreadsheetMetadataFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.ui.CanGiveFocus;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetCellFind;
import walkingkooka.spreadsheet.dominokit.ui.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.meta.HasSpreadsheetMetadata;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Optional;

public interface AppContext extends CanGiveFocus,
        HasNow,
        HasSpreadsheetDeltaFetcher,
        HasSpreadsheetLabelMappingFetcher,
        HasSpreadsheetMetadata,
        HasSpreadsheetMetadataFetcher,
        HistoryTokenContext,
        LoggingContext,
        Context {

    // json............................................................................................................

    /**
     * {@see JsonNodeMarshallContext}
     */
    JsonNodeMarshallContext marshallContext();

    /**
     * {@see JsonNodeUnmarshallContext}
     */
    JsonNodeUnmarshallContext unmarshallContext();

    // viewport.........................................................................................................

    /**
     * Requests a reload of this spreadsheet.
     */
    void reload();

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

    /**
     * Returns true if viewport {@link SpreadsheetCellFind} highlighting is enabled.
     */
    boolean isViewportHighlightEnabled();

    /**
     * Setter that updates the viewportHighlightEnabled flag.
     */
    void setViewportHighlightEnabled(boolean viewportHighlightEnabled);

    // cellFind.........................................................................................................

    /**
     * Returns the last {@link SpreadsheetCellFind} which will initially be {@link SpreadsheetCellFind#empty()}.
     */
    SpreadsheetCellFind lastCellFind();

    void setLastCellFind(final SpreadsheetCellFind lastCellFind);

    /**
     * Returns a {@link UrlQueryString} which will not be empty if {@link #isViewportHighlightEnabled()} is true
     * and {@link SpreadsheetCellFind} is not empty.
     */
    default UrlQueryString lastCellFindQueryString() {
        return this.isViewportHighlightEnabled() ?
                SpreadsheetDeltaFetcher.cellFindQueryString(
                        this.lastCellFind()
                ) :
                UrlQueryString.EMPTY;
    }

    /**
     * Returns a {@link UrlQueryString} that includes the viewport/window and lastCellFind.
     * This is particularly useful for {@link SpreadsheetDeltaFetcher}
     */
    default UrlQueryString lastCellFindAndViewportAndWindowQueryString() {
        return SpreadsheetDeltaFetcher.viewportAndWindowQueryString(
                this.viewport(SpreadsheetViewport.NO_ANCHORED_SELECTION),
                this.viewportCache()
                        .windows()
        ).addParameters(
                this.lastCellFindQueryString()
        );
    }
}
