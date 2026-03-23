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

package walkingkooka.spreadsheet.dominokit.validator;

import walkingkooka.collect.list.Lists;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.dialog.DialogAnchorListComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellValidatorSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellValidatorSelectHistoryToken;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.value.SpreadsheetError;
import walkingkooka.validation.provider.ValidatorSelector;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A modal dialog that supports editing a {@link ValidatorSelector}.
 */
public final class ValidatorSelectorDialogComponent implements DialogComponentLifecycle,
    LoadedSpreadsheetMetadataRequired,
    SpreadsheetDeltaFetcherWatcher,
    NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher {

    /**
     * Creates a new {@link ValidatorSelectorDialogComponent}.
     */
    public static ValidatorSelectorDialogComponent with(final ValidatorSelectorDialogComponentContext context) {
        return new ValidatorSelectorDialogComponent(
            Objects.requireNonNull(context, "context")
        );
    }

    private ValidatorSelectorDialogComponent(final ValidatorSelectorDialogComponentContext context) {
        this.context = context;
        context.addHistoryTokenWatcher(this);
        context.addSpreadsheetDeltaFetcherWatcher(this);

        this.validators = this.validators();

        this.selector = this.selector();

        this.links = this.links();
        this.selector.addValueWatcher2(this.links);

        this.dialog = this.dialogCreate();
    }

    // ids..............................................................................................................

    @Override
    public String idPrefix() {
        return ID + "-";
    }

    private final static String ID = ValidatorSelector.class.getSimpleName();

    // dialog...........................................................................................................

    /**
     * Creates the modal dialog, loaded with the {@link ValidatorSelector} textbox and some links.
     */
    private DialogComponent dialogCreate() {
        final ValidatorSelectorDialogComponentContext context = this.context;

        DialogComponent dialog = DialogComponent.largeEdit(
            ID + SpreadsheetElementIds.DIALOG,
            DialogComponent.INCLUDE_CLOSE,
            context
        );

        return dialog.appendChild(this.validators)
            .appendChild(this.selector)
            .appendChild(this.links);
    }

    @Override
    public DialogComponent dialog() {
        return this.dialog;
    }

    private final DialogComponent dialog;

    private final ValidatorSelectorDialogComponentContext context;

    // validators.......................................................................................................

    private ValidatorSelectorNameAnchorListComponent validators() {
        return ValidatorSelectorNameAnchorListComponent.with(
            this.idPrefix(),
            this.context
        );
    }

    private final ValidatorSelectorNameAnchorListComponent validators;

    // textBox..........................................................................................................

    /**
     * Creates a text box to edit the {@link ValidatorSelector} and installs a few value change type listeners
     */
    private ValidatorSelectorComponent selector() {
        return ValidatorSelectorComponent.empty()
            .setId(ID + "-selector" + SpreadsheetElementIds.TEXT_BOX);
    }

    /**
     * The {@link ValidatorSelectorComponent} that holds the {@link ValidatorSelector} in text form.
     */
    // @VisibleForTesting
    final ValidatorSelectorComponent selector;

    /**
     * Copy any error messages for the {@link ValidatorSelector}.
     */
    private void copySelectorErrorMessages() {
        final ValidatorSelectorComponent selector = this.selector;

        if (false == selector.hasErrors()) {
            selector.setErrors(
                this.context.spreadsheetViewportCache()
                    .historyTokenCell()
                    .map(c -> c.formula()
                        .error()
                        .map(SpreadsheetError::message)
                        .stream()
                        .collect(Collectors.toList())
                    ).orElse(Lists.empty())
            );
        }

        if(selector.hasErrors()) {
            this.links.disableSave();
        }
    }

    // dialog links.....................................................................................................

    private DialogAnchorListComponent<ValidatorSelector> links() {
        return DialogAnchorListComponent.empty(
                this.idPrefix(),
                context // DialogAnchorListComponentContext
            ).save()
            .undo()
            .clearLink()
            .close()
            .setComponentWithErrors(this.selector);
    }

    private final DialogAnchorListComponent<ValidatorSelector> links;

    // HistoryTokenAwareComponentLifecycle..............................................................................

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetCellValidatorSaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetCellValidatorSelectHistoryToken;
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
        final Optional<ValidatorSelector> value = this.context.undo();
        this.selector.setValue(value);
        this.validators.setValue(value);

        this.refreshTitleAndLinks();

        this.copySelectorErrorMessages();
    }

    private void refreshTitleAndLinks() {
        final ValidatorSelectorDialogComponentContext context = this.context;
        context.refreshDialogTitle(this);

        this.links.refresh(context);
    }

    @Override
    public boolean shouldLogLifecycleChanges() {
        return VALIDATOR_SELECTOR_DIALOG_COMPONENT;
    }

    // SpreadsheetDeltaFetcherWatcher...................................................................................

    @Override
    public void onSpreadsheetDelta(final HttpMethod method,
                                   final AbsoluteOrRelativeUrl url,
                                   final SpreadsheetDelta delta) {
        this.refreshIfOpen(this.context);
    }
}
