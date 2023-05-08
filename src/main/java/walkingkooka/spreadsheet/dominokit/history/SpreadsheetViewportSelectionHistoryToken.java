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

import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import org.dominokit.domino.ui.dropdown.DropDownMenu;
import org.dominokit.domino.ui.dropdown.DropDownPosition;
import walkingkooka.net.UrlFragment;
import walkingkooka.net.UrlPath;
import walkingkooka.net.UrlQueryString;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
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

    @Override //
    final UrlFragment selectionUrlFragment() {
        return this.viewportSelection.urlFragment()
                .append(this.selectionViewportUrlFragment());
    }

    abstract UrlFragment selectionViewportUrlFragment();

    /**
     * Tries to create a freeze token or {@link Optional#empty()} because the {@link SpreadsheetSelection} is invalid.
     */
    final Optional<HistoryToken> freezeOrEmpty() {
        HistoryToken token;

        try {
            token = this.setFreeze();
        } catch (final RuntimeException ignored) {
            token = null;
        }

        return Optional.ofNullable(token);
    }

    /**
     * Tries to create an unfreeze token or {@link Optional#empty()} because the {@link SpreadsheetSelection} is invalid.
     */
    final Optional<HistoryToken> unfreezeOrEmpty() {
        HistoryToken token;

        try {
            token = this.setUnfreeze();
        } catch (final RuntimeException ignored) {
            token = null;
        }

        return Optional.ofNullable(token);
    }

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

    /**
     * Helper for the select sub-classes which includes handling of labels, resolving them to a cell.
     */
    final void giveViewportFocus(final AppContext context) {
        final SpreadsheetViewportSelection viewportSelection = this.viewportSelection();
        final SpreadsheetSelection selection = viewportSelection.selection();
        final Optional<SpreadsheetSelection> maybeNonLabelSelection = context.nonLabelSelection(selection);

        if (maybeNonLabelSelection.isPresent()) {
            final SpreadsheetSelection nonLabelSelection = maybeNonLabelSelection.get();

            context.giveViewportFocus(
                    nonLabelSelection.focused(
                            selection.isLabelName() ?
                                    nonLabelSelection.defaultAnchor() :
                                    viewportSelection.anchor()
                    )
            );
        }
    }

    /**
     * Renders a drop down setMenu1. This helper is intended only for the setMenu1 sub classes.
     */
    final void renderDropDownMenu(final AppContext context) {
        // show context setMenu1
        final SpreadsheetViewportSelection viewportSelection = this.viewportSelection();
        final Optional<Element> maybeElement = context.findViewportElement(
                viewportSelection.selection().focused(viewportSelection.anchor())
        );

        context.debug(this.getClass().getSimpleName() + ".renderDropDownMenu " + viewportSelection);

        if (maybeElement.isPresent()) {
            final Element element = maybeElement.get();


            // CLEAR
            // DELETE
            // -------
            // FREEZE
            // UNFREEZE
            final DropDownMenu dropDownMenu = DropDownMenu.create((HTMLElement) element)
                    .setPosition(DropDownPosition.BOTTOM)
                    .appendChild(
                            context.dropdownAction(
                                    "Clear",
                                    Optional.of(
                                            this.setClear()
                                    )
                            )
                    ).appendChild(
                            context.dropdownAction(
                                    "Delete",
                                    Optional.of(
                                            this.setDelete()
                                    )
                            )
                    ).separator()
                    .appendChild(
                            context.dropdownAction(
                                    "Freeze",
                                    this.freezeOrEmpty()
                            )
                    ).appendChild(
                            context.dropdownAction(
                                    "Unfreeze",
                                    this.unfreezeOrEmpty()
                            )
                    );

            dropDownMenu.open(false); // focus
        }
    }
}
