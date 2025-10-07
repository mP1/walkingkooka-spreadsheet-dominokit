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

import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetError;
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
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.link.AnchorListComponent;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.validation.provider.ValidatorSelector;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A modal dialog that supports editing a {@link ValidatorSelector}.
 */
public final class ValidatorSelectorDialogComponent implements DialogComponentLifecycle,
    LoadedSpreadsheetMetadataRequired,
    ComponentLifecycleMatcherDelegator,
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

        this.selector = this.selector();

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

    private final static String ID = "selector";

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

        return dialog.appendChild(this.selector)
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

    private final ValidatorSelectorDialogComponentContext context;

    // textBox..........................................................................................................

    /**
     * Creates a text box to edit the {@link ValidatorSelector} and installs a few value change type listeners
     */
    private ValidatorSelectorComponent selector() {
        return ValidatorSelectorComponent.empty()
            .setId(ID + SpreadsheetElementIds.TEXT_BOX)
            .addKeyUpListener(
                (event) -> this.refreshSaveLink(
                    this.selector.value()
                )
            );
    }

    /**
     * The {@link ValidatorSelectorComponent} that holds the {@link ValidatorSelector} in text form.
     */
    private final ValidatorSelectorComponent selector;

    // dialog links.....................................................................................................

    void refreshSaveLink(final Optional<ValidatorSelector> list) {
        this.selector.validate();
        if (this.selector.hasErrors()) {
            this.save.disabled();
        } else {
            this.save.setValue(list);
        }
    }

    /**
     * A SAVE link which will be updated each time the {@link #selector} is also updated.
     */
    private final HistoryTokenSaveValueAnchorComponent<ValidatorSelector> save;

    /**
     * A CLEAR link which will save an empty {@link ValidatorSelector}.
     */
    private final HistoryTokenSaveValueAnchorComponent<ValidatorSelector> clear;

    /**
     * A UNDO link which will be updated each time the {@link ValidatorSelector} is saved.
     */
    private final HistoryTokenSaveValueAnchorComponent<ValidatorSelector> undo;

    /**
     * A CLOSE link which will close the dialog.
     */
    private final HistoryTokenAnchorComponent close;

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
        final Optional<ValidatorSelector> undo = this.context.undo();
        this.selector.setValue(undo);
        this.refreshSaveLink(undo);
        this.undo.setValue(undo);

        this.refreshTitleAndLinks();
    }

    private void refreshTitleAndLinks() {
        final ValidatorSelectorDialogComponentContext context = this.context;
        context.refreshDialogTitle(this);

        final HistoryToken historyToken = context.historyToken();

        this.clear.clearValue();

        this.close.setHistoryToken(
            Optional.of(
                historyToken.close()
            )
        );
    }

    // SpreadsheetDeltaFetcherWatcher...................................................................................

    @Override
    public void onSpreadsheetDelta(final HttpMethod method,
                                   final AbsoluteOrRelativeUrl url,
                                   final SpreadsheetDelta delta,
                                   final AppContext context) {
        if (this.isOpen()) {
            final SpreadsheetSelection selection = context.historyToken()
                .selection()
                .orElse(null);
            if (null != selection) {
                final SpreadsheetCell cell = context.spreadsheetViewportCache()
                    .cell(selection)
                    .orElse(null);
                if (null != cell) {

                    // copy ERRORS from SpreadsheetDelta#cell
                    this.selector.setErrors(
                        cell.formula()
                            .error()
                            .map(SpreadsheetError::message)
                            .stream()
                            .collect(Collectors.toList())
                    );
                }
            }

        }
    }
}
