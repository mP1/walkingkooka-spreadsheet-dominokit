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

package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.net.UrlPath;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;

import java.util.Objects;
import java.util.Optional;

public abstract class AnchoredSpreadsheetSelectionHistoryToken extends SpreadsheetSelectionHistoryToken {

    AnchoredSpreadsheetSelectionHistoryToken(final SpreadsheetId id,
                                             final SpreadsheetName name,
                                             final AnchoredSpreadsheetSelection selection) {
        super(
                id,
                name
        );
        this.selection = Objects.requireNonNull(selection, "selection");
    }

    public final AnchoredSpreadsheetSelection selection() {
        return this.selection;
    }

    /**
     * Factory method where sub-classes create a new instance with the given {@link AnchoredSpreadsheetSelection}.
     */
    abstract HistoryToken setDifferentSelection(final AnchoredSpreadsheetSelection selection);

    private final AnchoredSpreadsheetSelection selection;

    @Override //
    final UrlFragment selectionUrlFragment() {
        return this.selection.urlFragment()
                .append(this.anchoredSelectionUrlFragment());
    }

    abstract UrlFragment anchoredSelectionUrlFragment();

    final HistoryToken selectionSelect() {
        final HistoryToken selection = HistoryToken.selection(
                this.id(),
                this.name(),
                this.selection()
        );
        return this.equals(selection) ?
                this :
                selection;
    }

    final void deltaClearSelectionAndPushViewportHistoryToken(final AppContext context) {
        this.deltaClearSelection(context);
        this.pushSelectionHistoryToken(context);
    }

    /**
     * Invokes the server to clear the current selection.
     */
    final void deltaClearSelection(final AppContext context) {
        final SpreadsheetDeltaFetcher fetcher = context.spreadsheetDeltaFetcher();
        final AnchoredSpreadsheetSelection selection = this.selection();

        // clear row
        fetcher.postDelta(
                fetcher.url(
                        this.id(),
                        selection.selection(),
                        Optional.of(
                                UrlPath.parse("/clear")
                        )
                ).setQuery(
                        SpreadsheetDeltaFetcher.viewportAndWindowQueryString(
                                context.viewport(SpreadsheetViewport.NO_SELECTION),
                                context.viewportCache()
                                        .windows()
                        )
                ),
                SpreadsheetDelta.EMPTY
        );

        pushSelectionHistoryToken(context);
    }

    final <T1, T2> void patchMetadataAndPushSelectionHistoryToken(final SpreadsheetMetadataPropertyName<T1> propertyName1,
                                                                  final T1 propertyValue1,
                                                                  final SpreadsheetMetadataPropertyName<T2> propertyName2,
                                                                  final T2 propertyValue2,
                                                                  final AppContext context) {
        this.patchMetadata(
                propertyName1,
                propertyValue1,
                propertyName2,
                propertyValue2,
                context
        );

        this.pushSelectionHistoryToken(context);
    }

    final <T1, T2> void patchMetadata(final SpreadsheetMetadataPropertyName<T1> propertyName1,
                                      final T1 propertyValue1,
                                      final SpreadsheetMetadataPropertyName<T2> propertyName2,
                                      final T2 propertyValue2,
                                      final AppContext context) {
        context.spreadsheetMetadataFetcher()
                .patchMetadata(
                        this.id(),
                        SpreadsheetMetadata.EMPTY
                                .set(
                                        propertyName1,
                                        propertyValue1
                                ).set(
                                        propertyName2,
                                        propertyValue2
                                )
                );
    }

    final <T> void patchMetadataAndPushSelectionHistoryToken(final SpreadsheetMetadataPropertyName<T> propertyName,
                                                             final T propertyValue,
                                                             final AppContext context) {
        this.patchMetadata(
                propertyName,
                propertyValue,
                context
        );

        this.pushSelectionHistoryToken(context);
    }

    final <T> void patchMetadata(final SpreadsheetMetadataPropertyName<T> propertyName,
                                 final T propertyValue,
                                 final AppContext context) {
        // POST metadata with frozen row=row range = null
        context.spreadsheetMetadataFetcher()
                .patchMetadata(
                        this.id(),
                        propertyName,
                        propertyValue
                );
    }

    final void pushSelectionHistoryToken(final AppContext context) {
        this.selectionHistoryTokenOrEmpty()
                .ifPresent(context::pushHistoryToken);
    }
}
