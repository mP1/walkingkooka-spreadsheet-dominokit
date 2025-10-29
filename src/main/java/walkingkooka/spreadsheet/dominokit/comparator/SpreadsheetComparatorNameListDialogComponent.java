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

package walkingkooka.spreadsheet.dominokit.comparator;

import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorNameList;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcher;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherDelegator;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.anchor.HistoryTokenSaveValueAnchorComponent;
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

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A modal dialog that supports editing a {@link SpreadsheetComparatorNameList}.
 */
public final class SpreadsheetComparatorNameListDialogComponent implements DialogComponentLifecycle,
    LoadedSpreadsheetMetadataRequired,
    NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher,
    SpreadsheetMetadataFetcherWatcher,
    ComponentLifecycleMatcherDelegator {

    /**
     * Creates a new {@link SpreadsheetComparatorNameListDialogComponent}.
     */
    public static SpreadsheetComparatorNameListDialogComponent with(final SpreadsheetComparatorNameListDialogComponentContext context) {
        return new SpreadsheetComparatorNameListDialogComponent(
            Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetComparatorNameListDialogComponent(final SpreadsheetComparatorNameListDialogComponentContext context) {
        this.context = context;
        context.addHistoryTokenWatcher(this);

        context.addSpreadsheetMetadataFetcherWatcher(this);

        this.comparatorNameList = this.comparatorNameList();

        this.save = this.saveValueAnchor(context);
        this.clear = this.clearValueAnchor(context);
        this.undo = this.undoAnchor(context);
        this.close = this.closeAnchor();

        this.dialog = this.dialogCreate();
    }

    // ids..............................................................................................................

    @Override
    public String idPrefix() {
        return ID + "-";
    }

    private final static String ID = SpreadsheetComparatorNameList.class.getSimpleName();

    // dialog...........................................................................................................

    /**
     * Creates the modal dialog, loaded with the {@link SpreadsheetComparatorNameList} textbox and some links.
     */
    private DialogComponent dialogCreate() {
        final SpreadsheetComparatorNameListDialogComponentContext context = this.context;

        DialogComponent dialog = DialogComponent.largeEdit(
            ID + SpreadsheetElementIds.DIALOG,
            DialogComponent.INCLUDE_CLOSE,
            context
        );

        return dialog.appendChild(this.comparatorNameList)
            .appendChild(
                AnchorListComponent.empty()
                    .appendChild(this.save)
                    .appendChild(this.clear)
                    .appendChild(this.undo)
                    .appendChild(this.close)
            );
    }

    @Override
    public DialogComponent dialog() {
        return this.dialog;
    }

    private final DialogComponent dialog;

    private final SpreadsheetComparatorNameListDialogComponentContext context;

    // textBox..........................................................................................................

    /**
     * Creates a text box to edit the {@link SpreadsheetComparatorNameList} and installs a few value change type listeners
     */
    private SpreadsheetComparatorNameListComponent comparatorNameList() {
        return SpreadsheetComparatorNameListComponent.empty()
            .setId(ID + SpreadsheetElementIds.TEXT_BOX)
            .addKeyUpListener(
                (event) -> this.refreshSaveLink(
                    this.comparatorNameList.value()
                )
            ).addChangeListener(
                (oldValue, newValue) ->
                    this.refreshSaveLink(newValue)
            );
    }

    /**
     * The {@link SpreadsheetComparatorNameListComponent} that holds the {@link SpreadsheetComparatorNameList} in text form.
     */
    private final SpreadsheetComparatorNameListComponent comparatorNameList;

    // dialog links.....................................................................................................

    void refreshSaveLink(final Optional<SpreadsheetComparatorNameList> list) {
        this.comparatorNameList.validate();
        if (this.comparatorNameList.hasErrors()) {
            this.save.disabled();
        } else {
            this.save.setValue(list);
        }
    }

    /**
     * A SAVE link which will be updated each time the {@link #comparatorNameList} is also updated.
     */
    private final HistoryTokenSaveValueAnchorComponent<SpreadsheetComparatorNameList> save;

    /**
     * A CLEAR link which will save an empty {@link SpreadsheetComparatorNameList}.
     */
    private final HistoryTokenSaveValueAnchorComponent<SpreadsheetComparatorNameList> clear;

    /**
     * A UNDO link which will be updated each time the {@link SpreadsheetComparatorNameList} is saved.
     */
    private final HistoryTokenSaveValueAnchorComponent<SpreadsheetComparatorNameList> undo;

    /**
     * A CLOSE link which will close the dialog.
     */
    private final HistoryTokenAnchorComponent close;

    // SpreadsheetMetadataFetcherWatcher................................................................................
    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        this.refreshIfOpen(context);
    }

    @Override
    public void onSpreadsheetMetadataSet(final Set<SpreadsheetMetadata> metadatas,
                                         final AppContext context) {
        // Ignore many
    }

    // HistoryTokenAwareComponentLifecycle..............................................................................

    @Override
    public ComponentLifecycleMatcher componentLifecycleMatcher() {
        return this.context;
    }

    @Override
    public void dialogReset() {
        // NOP
    }

    @Override
    public void openGiveFocus(final RefreshContext context) {
        context.giveFocus(
            this.comparatorNameList::focus
        );
    }

    /**
     * Refreshes the widget, typically done when some variable within the {@link HistoryToken} changes.
     */
    @Override
    public void refresh(final RefreshContext context) {
        final Optional<SpreadsheetComparatorNameList> undo = this.context.undo();
        this.comparatorNameList.setValue(undo);
        this.refreshSaveLink(undo);
        this.undo.setValue(undo);

        this.refreshTitleAndLinks();
    }

    private void refreshTitleAndLinks() {
        final SpreadsheetComparatorNameListDialogComponentContext context = this.context;
        context.refreshDialogTitle(this);

        final HistoryToken historyToken = context.historyToken();

        this.clear.clearValue();

        this.close.setHistoryToken(
            Optional.of(
                historyToken.close()
            )
        );
    }

    @Override
    public boolean shouldLogLifecycleChanges() {
        return SPREADSHEET_COMPARATOR_NAME_LIST_DIALOG_COMPONENT;
    }
}
