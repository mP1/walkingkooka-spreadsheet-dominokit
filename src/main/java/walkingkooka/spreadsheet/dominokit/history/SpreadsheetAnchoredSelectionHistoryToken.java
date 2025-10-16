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

import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetColumnOrRowSpreadsheetComparatorNamesList;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.text.cursor.TextCursor;

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

    final AnchoredSpreadsheetSelection anchoredSelection;

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

    // parse............................................................................................................

    final HistoryToken parseSort(final TextCursor cursor) {
        final HistoryToken historyToken;

        final String component = parseComponentOrEmpty(cursor);
        switch (component) {
            case EDIT_STRING:
                historyToken = this.setSortEdit(
                    parseComponentOrEmpty(cursor)
                );
                break;
            case SAVE_STRING:
                historyToken = this.setSortSave(
                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse(
                        parseComponentOrEmpty(cursor)
                    )
                );
                break;
            default:
                historyToken = this;
                break;
        }

        return historyToken;
    }

    // historyToken helpers.............................................................................................

    final HistoryToken selectionSelect() {
        final HistoryToken selection = HistoryToken.selection(
            this.id,
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
            .postClear(
                this.id,
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
                this.id,
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
                this.id,
                propertyName,
                propertyValue
            );
    }

    final void pushSelectionHistoryToken(final AppContext context) {
        this.anchoredSelectionHistoryTokenOrEmpty()
            .ifPresent(context::pushHistoryToken);
    }
}
