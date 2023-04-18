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
import walkingkooka.net.UrlQueryString;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

import java.util.Objects;
import java.util.Optional;

public abstract class SpreadsheetViewportSelectionHistoryToken extends SpreadsheetSelectionHistoryToken {

    SpreadsheetViewportSelectionHistoryToken(final SpreadsheetId id,
                                             final SpreadsheetName name,
                                             final SpreadsheetViewportSelection viewportSelection) {
        super(
                id,
                name
        );
        this.viewportSelection = Objects.requireNonNull(viewportSelection, "viewportSelection");
    }

    public final SpreadsheetViewportSelection viewportSelection() {
        return this.viewportSelection;
    }

    private final SpreadsheetViewportSelection viewportSelection;

    @Override final UrlFragment selectionUrlFragment() {
        return this.viewportSelection.urlFragment()
                .append(this.selectionViewportUrlFragment());
    }

    abstract UrlFragment selectionViewportUrlFragment();

    /**
     * Factory that returns a {@link SpreadsheetViewportSelectionHistoryToken} without any action and just the
     * {@link SpreadsheetViewportSelection}
     *
     * @return
     */
    public abstract HistoryToken viewportSelectionHistoryToken();

    final void deltaClearSelectionAndPushViewportSelectionHistoryToken(final AppContext context) {
        this.deltaClearSelection(context);
        this.pushViewportSelectionHistoryToken(context);
    }

    /**
     * Invokes the server to clear the current selection.
     */
    final void deltaClearSelection(final AppContext context) {
        final SpreadsheetDeltaFetcher fetcher = context.spreadsheetDeltaFetcher();
        final SpreadsheetViewportSelection viewportSelection = this.viewportSelection();

        // clear row
        fetcher.postDelta(
                fetcher.url(
                        this.id(),
                        viewportSelection.selection(),
                        Optional.of(
                                UrlPath.parse("/clear")
                        )
                ).setQuery(
                        SpreadsheetDeltaFetcher.appendViewportSelectionAndWindow(
                                viewportSelection,
                                context.viewportWindow(),
                                UrlQueryString.EMPTY
                        )
                ),
                SpreadsheetDelta.EMPTY
        );

        pushViewportSelectionHistoryToken(context);
    }

    final <T1, T2> void patchMetadataAndPushViewportSelectionHistoryToken(final SpreadsheetMetadataPropertyName<T1> propertyName1,
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

        this.pushViewportSelectionHistoryToken(context);
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

    final <T> void patchMetadataAndPushViewportSelectionHistoryToken(final SpreadsheetMetadataPropertyName<T> propertyName,
                                                                     final T propertyValue,
                                                                     final AppContext context) {
        this.patchMetadata(
                propertyName,
                propertyValue,
                context
        );

        this.pushViewportSelectionHistoryToken(context);
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

    final void pushViewportSelectionHistoryToken(final AppContext context) {
        context.pushHistoryToken(
                this.viewportSelectionHistoryToken()
        );
    }
}
