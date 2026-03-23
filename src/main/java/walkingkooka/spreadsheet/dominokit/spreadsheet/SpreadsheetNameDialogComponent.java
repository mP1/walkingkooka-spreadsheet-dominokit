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

import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcher;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherDelegator;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.dialog.DialogAnchorListComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetName;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Displays a dialog box allowing the user to edit and save a {@link walkingkooka.spreadsheet.meta.SpreadsheetName}
 * for the selected {@link walkingkooka.spreadsheet.meta.SpreadsheetId}.
 */
public final class SpreadsheetNameDialogComponent implements DialogComponentLifecycle,
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

        this.links = this.links();

        this.dialog = this.dialogCreate();

        if (context.shouldLoadSpreadsheetMetadata()) {
            context.addSpreadsheetMetadataFetcherWatcher(this);
        }
    }

    /**
     * Creates the modal dialog, loaded with the pattern textbox and some links.
     */
    private DialogComponent dialogCreate() {
        return DialogComponent.smallerPrompt(
                ID + SpreadsheetElementIds.DIALOG,
                DialogComponent.INCLUDE_CLOSE,
                this.context
            ).appendChild(this.name)
            .appendChild(this.links);
    }

    @Override
    public DialogComponent dialog() {
        return this.dialog;
    }

    private final DialogComponent dialog;

    // id...............................................................................................................

    @Override
    public String idPrefix() {
        return ID_PREFIX;
    }

    final static String ID = SpreadsheetName.class.getSimpleName();

    final static String ID_PREFIX = ID + '-';

    // name.............................................................................................................

    private SpreadsheetNameComponent name() {
        return SpreadsheetNameComponent.empty()
            .setId(ID + SpreadsheetElementIds.TEXT_BOX)
            .addValueWatcher2(
                this::refreshLinks
            );
    }

    /**
     * The {@link SpreadsheetNameComponent} that holds the {@link walkingkooka.spreadsheet.meta.SpreadsheetName}.
     */
    // @VisibleForTesting
    final SpreadsheetNameComponent name;

    // link.............................................................................................................   // links............................................................................................................

    private DialogAnchorListComponent<SpreadsheetName> links() {
        return DialogAnchorListComponent.empty(
                this.idPrefix(),
                context // DialogAnchorListComponentContext
            ).save()
            .undo()
            .close()
            .setComponentWithErrors(this.name);
    }

    private final DialogAnchorListComponent<SpreadsheetName> links;

    // DialogComponentLifecycle.........................................................................................

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

        this.refreshLinks(
            dialogContext.undo()
        );

        if (dialogContext.shouldLoadSpreadsheetMetadata()) {
            dialogContext.loadSpreadsheetMetadata(id);
        }
    }

    @Override
    public void refresh(final RefreshContext context) {
        this.context.refreshDialogTitle(this);

        this.refreshLinks(
            this.context.undo()
        );
    }

    private void refreshLinks(final Optional<SpreadsheetName> name) {
        this.name.setValue(name);
        this.links.setValue(name);
    }

    private final SpreadsheetNameDialogComponentContext context;

    @Override
    public boolean shouldLogLifecycleChanges() {
        return SPREADSHEET_NAME_COMPONENT_LIFECYCLE;
    }

    // SpreadsheetMetadataFetcherWatcher................................................................................

    @Override
    public void onEmptyResponse() {
        // ignore
    }

    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata) {
        if (this.isOpen()) {
            if (Objects.equals(
                metadata.id().orElse(null),
                this.spreadsheetId)) {
                this.refreshIfOpen(this.context);
            }
        } else {
            this.spreadsheetId = null;
        }
    }

    @Override
    public void onSpreadsheetMetadataSet(final Set<SpreadsheetMetadata> metadatas) {
        // ignore
    }

    private SpreadsheetId spreadsheetId;
}
