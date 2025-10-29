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

package walkingkooka.spreadsheet.dominokit.convert;

import elemental2.dom.Headers;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.provider.ConverterInfoSet;
import walkingkooka.convert.provider.ConverterSelector;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.convert.provider.MissingConverter;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcher;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherDelegator;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.anchor.HistoryTokenSaveValueAnchorComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.fetcher.ConverterFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.Fetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.FetcherRequestBody;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.link.AnchorListComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A modal dialog that supports editing a {@link ConverterSelector}.
 */
public final class ConverterSelectorDialogComponent implements DialogComponentLifecycle,
    LoadedSpreadsheetMetadataRequired,
    NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher,
    ConverterFetcherWatcher,
    SpreadsheetMetadataFetcherWatcher,
    ComponentLifecycleMatcherDelegator {

    /**
     * Creates a new {@link ConverterSelectorDialogComponent}.
     */
    public static ConverterSelectorDialogComponent with(final ConverterSelectorDialogComponentContext context) {
        return new ConverterSelectorDialogComponent(
            Objects.requireNonNull(context, "context")
        );
    }

    private ConverterSelectorDialogComponent(final ConverterSelectorDialogComponentContext context) {
        this.context = context;
        context.addHistoryTokenWatcher(this);

        context.addSpreadsheetMetadataFetcherWatcher(this);
        context.addConverterFetcherWatcher(this);

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

    private final static String ID = ConverterSelector.class.getSimpleName();

    // dialog...........................................................................................................

    /**
     * Creates the modal dialog, loaded with the {@link ConverterSelector} textbox and some links.
     */
    private DialogComponent dialogCreate() {
        final ConverterSelectorDialogComponentContext context = this.context;

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

    private final ConverterSelectorDialogComponentContext context;

    // textBox..........................................................................................................

    /**
     * Creates a text box to edit the {@link ConverterSelector} and installs a few value change type listeners
     */
    private ConverterSelectorComponent selector() {
        return ConverterSelectorComponent.empty()
            .setId(ID + SpreadsheetElementIds.TEXT_BOX)
            .addKeyUpListener(
                (event) -> this.refreshSaveLink(
                    this.selector.value()
                )
            ).addChangeListener(
                (oldValue, newValue) -> {
                    this.refreshSaveLink(newValue);

                    if (newValue.isPresent()) {
                        this.context.verifySelector(
                            newValue.get()
                                .toString()
                        );
                    }
                }
            );
    }

    /**
     * The {@link ConverterSelectorComponent} that holds the {@link ConverterSelector} in text form.
     */
    private final ConverterSelectorComponent selector;

    // dialog links.....................................................................................................

    void refreshSaveLink(final Optional<ConverterSelector> list) {
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
    private final HistoryTokenSaveValueAnchorComponent<ConverterSelector> save;

    /**
     * A CLEAR link which will save an empty {@link ConverterSelector}.
     */
    private final HistoryTokenSaveValueAnchorComponent<ConverterSelector> clear;

    /**
     * A UNDO link which will be updated each time the {@link ConverterSelector} is saved.
     */
    private final HistoryTokenSaveValueAnchorComponent<ConverterSelector> undo;

    /**
     * A CLOSE link which will close the dialog.
     */
    private final HistoryTokenAnchorComponent close;

    // FetcherWatcher...................................................................................................

    @Override //
    public void onBegin(final HttpMethod method,
                        final Url url,
                        final Optional<FetcherRequestBody<?>> body,
                        final AppContext context) {
        // nop
    }

    @Override //
    public void onFailure(final HttpMethod method,
                          final AbsoluteOrRelativeUrl url,
                          final HttpStatus status,
                          final Headers headers,
                          final String body,
                          final AppContext context) {
        if (this.isOpen()) {
            if (HttpMethod.POST.equals(method) && this.context.isVerifyConverterSelectorUrl(url.path())) {
                this.selector.setErrors(
                    Lists.of(
                        Fetcher.errorMessage(body)
                    )
                );
            } else {
                this.selector.clearErrors();
            }
        }
    }

    @Override//
    public void onError(final Object cause,
                        final AppContext context) {
        // nop
    }

    // ConverterFetcherWatcher..........................................................................................

    @Override
    public void onConverterInfoSet(final ConverterInfoSet infos,
                                   final AppContext context) {
        // ignore
    }

    @Override
    public void onVerify(final SpreadsheetId id,
                         final SpreadsheetMetadataPropertyName<ConverterSelector> metadataPropertyName,
                         final Set<MissingConverter> missingConverters,
                         final AppContext context) {
        this.selector.clearErrors();
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
        final Optional<ConverterSelector> undo = this.context.undo();
        this.selector.setValue(undo);
        this.refreshSaveLink(undo);
        this.undo.setValue(undo);

        this.refreshTitleAndLinks();
    }

    private void refreshTitleAndLinks() {
        final ConverterSelectorDialogComponentContext context = this.context;
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
    public  boolean shouldLogLifecycleChanges() {
        return CONVERTER_SELECTOR_DIALOG_COMPONENT;
    }
}
