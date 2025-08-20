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

package walkingkooka.spreadsheet.dominokit.navigate;

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcher;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherDelegator;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.cell.SpreadsheetCellReferenceComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.link.AnchorListComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportHomeNavigationList;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A modal dialog that supports editing a navigation target.
 */
public final class SpreadsheetNavigateDialogComponent implements DialogComponentLifecycle,
    LoadedSpreadsheetMetadataRequired,
    NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher,
    ComponentLifecycleMatcherDelegator,
    SpreadsheetMetadataFetcherWatcher {

    /**
     * Creates a new {@link SpreadsheetNavigateDialogComponent}.
     */
    public static SpreadsheetNavigateDialogComponent with(final SpreadsheetNavigateDialogComponentContext context) {
        return new SpreadsheetNavigateDialogComponent(
            Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetNavigateDialogComponent(final SpreadsheetNavigateDialogComponentContext context) {
        this.context = context;
        context.addHistoryTokenWatcher(this);
        context.addSpreadsheetMetadataFetcherWatcher(this);

        final String idPrefix = this.idPrefix();

        this.home = SpreadsheetCellReferenceComponent.with(
            idPrefix + "home" + SpreadsheetElementIds.TEXT_BOX
        ).addKeyUpListener(
            (e) -> this.refreshLinks()
        ).addChangeListener(
            (oldValue, newValue) -> this.refreshLinks(newValue)
        );

        this.save = HistoryTokenAnchorComponent.empty()
            .setId(
                idPrefix +
                    "save" +
                    SpreadsheetElementIds.LINK
            ).setTextContent("Save");
        this.undo = HistoryTokenAnchorComponent.empty()
            .setId(
                idPrefix +
                    "undo" +
                    SpreadsheetElementIds.LINK
            ).setTextContent("Undo");

        this.close = this.closeAnchor();

        this.dialog = this.dialogCreate();
    }

    // ids..............................................................................................................

    @Override
    public String idPrefix() {
        return ID + "-";
    }

    private final static String ID = "navigate";

    // dialog...........................................................................................................

    private DialogComponent dialogCreate() {
        final SpreadsheetNavigateDialogComponentContext context = this.context;

        return DialogComponent.smallerPrompt(
                ID + SpreadsheetElementIds.DIALOG,
                DialogComponent.INCLUDE_CLOSE,
                context
            ).setTitle(
                context.dialogTitle()
            ).appendChild(this.home)
            .appendChild(
                AnchorListComponent.empty()
                    .appendChild(this.save)
                    .appendChild(this.undo)
                    .appendChild(this.close)
            );
    }

    @Override
    public DialogComponent dialog() {
        return this.dialog;
    }

    private final DialogComponent dialog;

    // ComponentLifecycleMatcherDelegator...............................................................................

    @Override
    public ComponentLifecycleMatcher componentLifecycleMatcher() {
        return this.context;
    }

    // dialog links.....................................................................................................

    private final SpreadsheetCellReferenceComponent home;

    private final HistoryTokenAnchorComponent save;

    private final HistoryTokenAnchorComponent undo;

    /**
     * A CLOSE link which will close the dialog.
     */
    private final HistoryTokenAnchorComponent close;

    // DialogComponentLifecycle.........................................................................................

    @Override
    public void dialogReset() {
        this.undo.clear();
    }

    @Override
    public void openGiveFocus(final RefreshContext context) {
        this.undo.setValue(
            Optional.of(
                context.historyToken()
                    .setNavigation(
                        Optional.of(
                            SpreadsheetViewportHomeNavigationList.with(
                                this.context.spreadsheetMetadata()
                                    .getOrFail(SpreadsheetMetadataPropertyName.VIEWPORT_HOME)
                            )
                        )
                    )
            )
        );
        context.giveFocus(
            this.home::focus
        );
    }

    @Override
    public void refresh(final RefreshContext context) {
        this.context.refreshDialogTitle(this);

        this.refreshLinks();
    }

    private void refreshLinks() {
        this.refreshLinks(
            this.home.value()
        );
    }

    private void refreshLinks(final Optional<SpreadsheetCellReference> cell) {
        final SpreadsheetNavigateDialogComponentContext context = this.context;

        final HistoryToken historyToken = context.historyToken();

        this.save.setValue(
            cell.map(
                c ->
                    historyToken.setNavigation(
                        Optional.of(
                            SpreadsheetViewportHomeNavigationList.with(c)
                        )
                    )
            )
        );

        this.close.setHistoryToken(
            Optional.of(
                historyToken.close()
            )
        );
    }

    // SpreadsheetMetadataFetcherWatcher................................................................................

    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        this.refreshIfOpen(context);
    }

    @Override
    public void onSpreadsheetMetadataSet(final Set<SpreadsheetMetadata> metadatas,
                                         final AppContext context) {
        // ignore
    }

    private final SpreadsheetNavigateDialogComponentContext context;
}
