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

package walkingkooka.spreadsheet.dominokit.formhandler;

import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcher;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherDelegator;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.dialog.DialogAnchorListComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.validation.form.provider.FormHandlerSelector;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A modal dialog that supports editing a {@link FormHandlerSelector}.
 */
public final class FormHandlerSelectorDialogComponent implements DialogComponentLifecycle,
    LoadedSpreadsheetMetadataRequired,
    NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher,
    SpreadsheetMetadataFetcherWatcher,
    ComponentLifecycleMatcherDelegator {

    /**
     * Creates a new {@link FormHandlerSelectorDialogComponent}.
     */
    public static FormHandlerSelectorDialogComponent with(final FormHandlerSelectorDialogComponentContext context) {
        return new FormHandlerSelectorDialogComponent(
            Objects.requireNonNull(context, "context")
        );
    }

    private FormHandlerSelectorDialogComponent(final FormHandlerSelectorDialogComponentContext context) {
        this.context = context;
        context.addHistoryTokenWatcher(this);

        context.addSpreadsheetMetadataFetcherWatcher(this);

        this.selector = this.selector();

        this.links = this.dialogAnchorListComponent(context)
            .saveAutoDisableWhenMissingValue()
            .undo()
            .clearLink()
            .close();

        this.dialog = this.dialogCreate();
    }

    // ids..............................................................................................................

    @Override
    public String idPrefix() {
        return ID + "-";
    }

    private final static String ID = FormHandlerSelector.class.getSimpleName();

    // dialog...........................................................................................................

    /**
     * Creates the modal dialog, loaded with the {@link FormHandlerSelector} textbox and some links.
     */
    private DialogComponent dialogCreate() {
        final FormHandlerSelectorDialogComponentContext context = this.context;

        DialogComponent dialog = DialogComponent.largeEdit(
            ID + SpreadsheetElementIds.DIALOG,
            DialogComponent.INCLUDE_CLOSE,
            context
        );

        return dialog.appendChild(this.selector)
            .appendChild(this.links);
    }

    @Override
    public DialogComponent dialog() {
        return this.dialog;
    }

    private final DialogComponent dialog;

    private final FormHandlerSelectorDialogComponentContext context;

    // textBox..........................................................................................................

    /**
     * Creates a text box to edit the {@link FormHandlerSelector} and installs a few value change type listeners
     */
    private FormHandlerSelectorComponent selector() {
        return FormHandlerSelectorComponent.empty()
            .setId(ID + SpreadsheetElementIds.TEXT_BOX)
            .addValueWatcher2(
                this::refreshLinks
            );
    }

    /**
     * The {@link FormHandlerSelectorComponent} that holds the {@link FormHandlerSelector} in text form.
     */
    private final FormHandlerSelectorComponent selector;

    // dialog links.....................................................................................................

    void refreshLinks(final Optional<FormHandlerSelector> list) {
        this.selector.validate();

        this.links.setValue(
            this.selector.hasErrors() ?
                Optional.empty() :
                list
        );
    }

    private final DialogAnchorListComponent<FormHandlerSelector> links;

    // SpreadsheetMetadataFetcherWatcher................................................................................
    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata) {
        this.refreshIfOpen(this.context);
    }

    @Override
    public void onSpreadsheetMetadataSet(final Set<SpreadsheetMetadata> metadatas) {
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
            this.selector::focus
        );
    }

    @Override
    public void refresh(final RefreshContext context) {
        final Optional<FormHandlerSelector> undo = this.context.undo();
        this.selector.setValue(undo);
        this.refreshLinks(undo);

        this.context.refreshDialogTitle(this);
    }

    @Override
    public boolean shouldLogLifecycleChanges() {
        return FORM_HANDLER_SELECTOR_DIALOG_COMPONENT;
    }
}
