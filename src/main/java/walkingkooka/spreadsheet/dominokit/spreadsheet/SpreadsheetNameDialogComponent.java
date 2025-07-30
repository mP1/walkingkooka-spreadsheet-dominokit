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

package walkingkooka.spreadsheet.dominokit.spreadsheet;

import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcher;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherDelegator;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.anchor.HistoryTokenSaveValueAnchorComponent;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.link.SpreadsheetLinkListComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Displays a dialog box allowing the user to edit and save a {@link walkingkooka.spreadsheet.SpreadsheetName}
 * for the selected {@link walkingkooka.spreadsheet.SpreadsheetId}.
 */
public final class SpreadsheetNameDialogComponent implements SpreadsheetDialogComponentLifecycle,
    SpreadsheetMetadataFetcherWatcher,
    NopFetcherWatcher,
    ComponentLifecycleMatcherDelegator {

    public static SpreadsheetNameDialogComponent with(final SpreadsheetNameDialogComponentContext context) {
        return new SpreadsheetNameDialogComponent(
            Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetNameDialogComponent(final SpreadsheetNameDialogComponentContext context) {
        super();

        context.addHistoryTokenWatcher(this);
        this.context = context;

        this.name = this.name();

        this.save = this.<SpreadsheetName>saveValueAnchor(context)
            .autoDisableWhenMissingValue();
        this.undo = this.undoAnchor(context);
        this.close = this.closeAnchor();

        this.dialog = this.dialogCreate();

        if (context.shouldLoadSpreadsheetMetadata()) {
            context.addSpreadsheetMetadataFetcherWatcher(this);
        }
    }

    /**
     * Creates the modal dialog, loaded with the pattern textbox and some links.
     */
    private SpreadsheetDialogComponent dialogCreate() {
        return SpreadsheetDialogComponent.smallerPrompt(
                ID + SpreadsheetElementIds.DIALOG,
                this.context.dialogTitle(),
                SpreadsheetDialogComponent.INCLUDE_CLOSE,
                this.context
            ).appendChild(this.name)
            .appendChild(
                SpreadsheetLinkListComponent.empty()
                    .appendChild(this.save)
                    .appendChild(this.undo)
                    .appendChild(this.close)
            );
    }

    @Override
    public SpreadsheetDialogComponent dialog() {
        return this.dialog;
    }

    private final SpreadsheetDialogComponent dialog;

    // id...............................................................................................................

    @Override
    public String idPrefix() {
        return ID_PREFIX;
    }

    final static String ID = "spreadsheetName";

    final static String ID_PREFIX = ID + '-';

    // name.............................................................................................................

    private SpreadsheetNameComponent name() {
        return SpreadsheetNameComponent.empty()
            .setId(ID +SpreadsheetElementIds.TEXT_BOX)
            .addKeyupListener(
                (e) -> this.setName(this.name.value())
            ).addChangeListener(
                (oldValue, newValue) -> this.setName(newValue)
            );
    }

    private void setName(final Optional<SpreadsheetName> name) {
        this.name.setValue(name);
        this.refreshSave(name);
    }

    /**
     * The {@link SpreadsheetNameComponent} that holds the {@link walkingkooka.spreadsheet.SpreadsheetName}.
     */
    private final SpreadsheetNameComponent name;

    // save.............................................................................................................

    private void refreshSave(final Optional<SpreadsheetName> name) {
        this.save.setValue(name);
    }

    /**
     * A SAVE link which will be updated each time the name box is also updated.
     */
    private final HistoryTokenSaveValueAnchorComponent<SpreadsheetName> save;

    // undo.............................................................................................................

    private void refreshUndo(final Optional<SpreadsheetName> name) {
        this.undo.setValue(name);
    }

    /**
     * A UNDO link which will be updated each time the name is saved.
     */
    private final HistoryTokenSaveValueAnchorComponent<SpreadsheetName> undo;

    // close............................................................................................................

    private void refreshClose() {
        this.close.setHistoryToken(
            Optional.of(
                this.context.historyToken()
                    .close()
            )
        );
    }

    /**
     * A CLOSE link which will close the dialog.
     */
    private final HistoryTokenAnchorComponent close;

    // SpreadsheetDialogComponentLifecycle..............................................................................

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
        final SpreadsheetNameDialogComponentContext dialogContext = this.context;

        final SpreadsheetId id = dialogContext.spreadsheetId();
        this.spreadsheetId = id;
        this.setName(dialogContext.spreadsheetName());

        if (dialogContext.shouldLoadSpreadsheetMetadata()) {
            dialogContext.loadSpreadsheetMetadata(id);
        }
    }

    @Override
    public void refresh(final RefreshContext context) {
        final Optional<SpreadsheetName> name = this.context.spreadsheetName();
        this.refreshSave(name);
        this.refreshUndo(name);

        this.refreshClose();
    }

    private final SpreadsheetNameDialogComponentContext context;

    // SpreadsheetMetadataFetcherWatcher................................................................................

    @Override
    public void onEmptyResponse(final AppContext context) {
        // ignore
    }

    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        if (this.isOpen()) {
            if (Objects.equals(
                metadata.id().orElse(null),
                this.spreadsheetId)) {
                this.refreshIfOpen(context);
            }
        } else {
            this.spreadsheetId = null;
        }
    }

    @Override
    public void onSpreadsheetMetadataSet(final Set<SpreadsheetMetadata> metadatas,
                                         final AppContext context) {
        // ignore
    }

    private SpreadsheetId spreadsheetId;
}
