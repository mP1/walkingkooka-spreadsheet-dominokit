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
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNamesList;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;

import java.util.Objects;

public abstract class SpreadsheetAnchoredSelectionHistoryToken extends SpreadsheetSelectionHistoryToken {

    SpreadsheetAnchoredSelectionHistoryToken(final SpreadsheetId id,
                                             final SpreadsheetName name,
                                             final AnchoredSpreadsheetSelection anchoredSelection) {
        super(
                id,
                name
        );
        this.anchoredSelection = Objects.requireNonNull(anchoredSelection, "anchoredSelection");
    }

    public final AnchoredSpreadsheetSelection anchoredSelection() {
        return this.anchoredSelection;
    }

    /**
     * Factory method where sub-classes create a new instance with the given {@link AnchoredSpreadsheetSelection}.
     */
    final HistoryToken setDifferentAnchoredSelection(final AnchoredSpreadsheetSelection anchoredSelection) {
        return this.replaceIdNameAnchoredSelection(
                this.id(),
                this.name(),
                anchoredSelection
        );
    }

    private final AnchoredSpreadsheetSelection anchoredSelection;

    @Override //
    final HistoryToken replaceIdAndName(final SpreadsheetId id,
                                        final SpreadsheetName name) {
        return this.replaceIdNameAnchoredSelection(
                id,
                name,
                this.anchoredSelection
        );
    }

    abstract HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                         final SpreadsheetName name,
                                                         final AnchoredSpreadsheetSelection anchoredSelection);

    @Override //
    final UrlFragment selectionUrlFragment() {
        return this.anchoredSelection.urlFragment()
                .append(this.anchoredSelectionUrlFragment());
    }

    abstract UrlFragment anchoredSelectionUrlFragment();

    // sort.............................................................................................................

    abstract HistoryToken setSortEdit0(final String comparators);

    abstract HistoryToken setSortSave(final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparators);

    // historyToken helpers.............................................................................................

    final HistoryToken selectionSelect() {
        final HistoryToken selection = HistoryToken.selection(
                this.id(),
                this.name(),
                this.anchoredSelection()
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
        context.spreadsheetDeltaFetcher()
                .clear(
                        this.id(),
                        this.anchoredSelection().selection()
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
        this.anchoredSelectionHistoryTokenOrEmpty()
                .ifPresent(context::pushHistoryToken);
    }
}
