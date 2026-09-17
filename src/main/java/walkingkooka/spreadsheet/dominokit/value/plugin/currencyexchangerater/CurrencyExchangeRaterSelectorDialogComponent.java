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

package walkingkooka.spreadsheet.dominokit.value.plugin.currencyexchangerater;

import walkingkooka.collect.list.Lists;
import walkingkooka.currency.provider.CurrencyExchangeRaterSelector;
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
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellCurrencyExchangeRaterSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellCurrencyExchangeRaterSelectHistoryToken;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.value.SpreadsheetError;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A modal dialog that supports editing a {@link CurrencyExchangeRaterSelector}.
 */
public final class CurrencyExchangeRaterSelectorDialogComponent implements DialogComponentLifecycle,
    LoadedSpreadsheetMetadataRequired,
    SpreadsheetDeltaFetcherWatcher,
    NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher {

    /**
     * Creates a new {@link CurrencyExchangeRaterSelectorDialogComponent}.
     */
    public static CurrencyExchangeRaterSelectorDialogComponent with(final CurrencyExchangeRaterSelectorDialogComponentContext context) {
        return new CurrencyExchangeRaterSelectorDialogComponent(
            Objects.requireNonNull(context, "context")
        );
    }

    private CurrencyExchangeRaterSelectorDialogComponent(final CurrencyExchangeRaterSelectorDialogComponentContext context) {
        this.context = context;
        context.addHistoryWatcher(this);
        context.addSpreadsheetDeltaFetcherWatcher(this);

        this.names = this.names();

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

    private final static String ID = CurrencyExchangeRaterSelector.class.getSimpleName();

    // dialog...........................................................................................................

    /**
     * Creates the modal dialog, loaded with the {@link CurrencyExchangeRaterSelector} textbox and some links.
     */
    private DialogComponent dialogCreate() {
        return DialogComponent.largeEdit(
                ID + SpreadsheetElementIds.DIALOG,
                DialogComponent.INCLUDE_CLOSE,
                this.context
            ).appendChild(this.names)
            .appendChild(this.selector)
            .appendChild(this.links);
    }

    @Override
    public DialogComponent dialog() {
        return this.dialog;
    }

    private final DialogComponent dialog;

    private final CurrencyExchangeRaterSelectorDialogComponentContext context;

    // names............................................................................................................

    private CurrencyExchangeRaterNameAnchorListComponent names() {
        return CurrencyExchangeRaterNameAnchorListComponent.with(
            this.idPrefix(),
            this.context
        );
    }

    // @VisibleForTesting
    final CurrencyExchangeRaterNameAnchorListComponent names;

    // selector.........................................................................................................

    /**
     * Creates a text box to edit the {@link CurrencyExchangeRaterSelector} and installs a few value change type listeners
     */
    private CurrencyExchangeRaterSelectorComponent selector() {
        return CurrencyExchangeRaterSelectorComponent.empty()
            .setId(ID + "-selector" + SpreadsheetElementIds.TEXT_BOX);
    }

    /**
     * The {@link CurrencyExchangeRaterSelectorComponent} that holds the {@link CurrencyExchangeRaterSelector} in text form.
     */
    // @VisibleForTesting
    final CurrencyExchangeRaterSelectorComponent selector;

    // dialog links.....................................................................................................

    private DialogAnchorListComponent<CurrencyExchangeRaterSelector> links() {
        return this.dialogAnchorListComponent(this.context)
            .save()
            .undo()
            .clearLink()
            .close()
            .setComponentWithErrors(this.selector);
    }

    private final DialogAnchorListComponent<CurrencyExchangeRaterSelector> links;

    // HistoryTokenAwareComponentLifecycle..............................................................................

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetCellCurrencyExchangeRaterSaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetCellCurrencyExchangeRaterSelectHistoryToken;
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
    public void refresh(final RefreshContext refreshContext) {
        final CurrencyExchangeRaterSelectorComponent selector = this.selector;
        final CurrencyExchangeRaterSelectorDialogComponentContext context = this.context;

        final Optional<CurrencyExchangeRaterSelector> value = context.undo();
        selector.setValue(value);
        this.names.setValue(
            value.map(CurrencyExchangeRaterSelector::name)
        );

        context.refreshDialogTitle(this);

        this.links.refresh(context);

        // Copy any error messages for the {@link CurrencyExchangeRaterSelector}.
        if (false == selector.hasErrors()) {
            selector.setErrors(
                context.spreadsheetViewportCache()
                    .historyTokenCell()
                    .map(c -> c.formula()
                        .currencyExchangeError()
                        .map(SpreadsheetError::message)
                        .stream()
                        .collect(Collectors.toList())
                    ).orElse(Lists.empty())
            );
        }

        if (selector.hasErrors()) {
            this.links.disableSave();
        }
    }

    @Override
    public boolean shouldLogLifecycleChanges() {
        return CURRENCY_EXCHANGE_RATER_SELECTOR_DIALOG_COMPONENT;
    }

    // SpreadsheetDeltaFetcherWatcher...................................................................................

    @Override
    public void onSpreadsheetDelta(final HttpMethod method,
                                   final AbsoluteOrRelativeUrl url,
                                   final SpreadsheetDelta delta) {
        this.refreshIfOpen(this.context);
    }
}
