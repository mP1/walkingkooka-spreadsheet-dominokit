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

package walkingkooka.spreadsheet.dominokit.currency;

import org.dominokit.domino.ui.menu.MenuItem;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.http.HttpMethod;
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
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.flex.FlexLayoutComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.link.AnchorListComponent;
import walkingkooka.spreadsheet.dominokit.suggestbox.SuggestBoxComponent;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.server.currency.CurrencyHateosResource;
import walkingkooka.spreadsheet.server.currency.CurrencyHateosResourceSet;

import java.util.Currency;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A model dialog that displays a {@link CurrencyComponent} allowing the user to pick a {@link Currency}.
 */
public final class CurrencyDialogComponent implements DialogComponentLifecycle,
    LoadedSpreadsheetMetadataRequired,
    SpreadsheetDeltaFetcherWatcher,
    SpreadsheetMetadataFetcherWatcher,
    ComponentLifecycleMatcherDelegator,
    NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher {

    /**
     * Creates a new {@link CurrencyDialogComponent}.
     */
    public static CurrencyDialogComponent with(final CurrencyDialogComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new CurrencyDialogComponent(context);
    }

    private CurrencyDialogComponent(final CurrencyDialogComponentContext context) {
        this.context = context;

        this.save = this.saveValueAnchor(context);

        // currency after save because currency passes a method reference to #save
        this.currency = this.currency();

        this.clear = this.clearValueAnchor(context);
        this.undo = this.undoAnchor(context);
        this.close = this.closeAnchor();

        this.dialog = this.dialogCreate();

        context.addHistoryTokenWatcher(this);
        context.addSpreadsheetDeltaFetcherWatcher(this);
        context.addSpreadsheetMetadataFetcherWatcher(this);
    }

    // dialog...........................................................................................................

    private DialogComponent dialogCreate() {
        final CurrencyDialogComponentContext context = this.context;

        return DialogComponent.smallerPrompt(
            ID + SpreadsheetElementIds.DIALOG,
            DialogComponent.INCLUDE_CLOSE,
            context
        ).appendChild(
            FlexLayoutComponent.row()
                .appendChild(this.currency)
        ).appendChild(
            AnchorListComponent.empty()
                .appendChild(this.save)
                .appendChild(this.clear)
                .appendChild(this.undo)
                .appendChild(this.close)
        );
    }

    private final DialogComponent dialog;

    private final CurrencyDialogComponentContext context;

    @Override
    public DialogComponent dialog() {
        return this.dialog;
    }

    // id...............................................................................................................

    @Override
    public String idPrefix() {
        return ID_PREFIX;
    }

    private final static String ID = Currency.class.getSimpleName();

    private final static String ID_PREFIX = ID + "-";

    // currencyComponent..................................................................................................

    private CurrencyComponent<Currency> currency() {
        return CurrencyComponent.empty(
                new CurrencyComponentContext<Currency>() {

                    @Override
                    public Optional<CurrencyComponentSuggestionsValue<Currency>> toValue(final Currency currency) {
                        return CurrencyDialogComponent.this.context.currencyText(currency)
                            .map(t -> CurrencyComponentSuggestionsValue.with(
                                    currency,
                                    t,
                                    currency
                                )
                            );
                    }

                    @Override
                    public void filter(final String startsWith,
                                       final SuggestBoxComponent<CurrencyComponentSuggestionsValue<Currency>> suggestBox) {
                        suggestBox.setOptions(
                            CurrencyHateosResourceSet.filter(startsWith, CurrencyDialogComponent.this.context)
                                .stream()
                                .map(
                                    (CurrencyHateosResource lhr) ->
                                        CurrencyComponentSuggestionsValue.with(
                                            lhr.currency(),
                                            lhr.text(),
                                            lhr.currency()
                                        )
                                ).sorted()
                                .collect(Collectors.toList())
                        );
                    }

                    @Override
                    public void verifyOption(final CurrencyComponentSuggestionsValue<Currency> value,
                                             final SuggestBoxComponent<CurrencyComponentSuggestionsValue<Currency>> suggestBox) {
                        CurrencyComponentSuggestionsValue<Currency> verified = null;

                        if (null != value) {
                            final Currency currency = value.currency();
                            final String currencyText = CurrencyDialogComponent.this.context.currencyText(currency)
                                .orElse(null);
                            verified = CurrencyComponentSuggestionsValue.with(
                                currency,
                                currencyText,
                                currency
                            );
                        }

                        if (null != verified) {
                            suggestBox.setVerifiedOption(verified);
                        }
                    }

                    @Override
                    public MenuItem<CurrencyComponentSuggestionsValue<Currency>> createMenuItem(final CurrencyComponentSuggestionsValue<Currency> value) {
                        return this.historyTokenMenuItem(
                            ID,
                            value,
                            CurrencyDialogComponent.this.context
                        );
                    }
                }
            ).optional()
            .addValueWatcher2(
                this.save::setValue
            );
    }

    private final CurrencyComponent<Currency> currency;

    // save.............................................................................................................

    private final HistoryTokenSaveValueAnchorComponent<Currency> save;

    // clear.............................................................................................................

    private void refreshClear() {
        this.clear.clearValue();
    }

    private final HistoryTokenSaveValueAnchorComponent<Currency> clear;

    // undo.............................................................................................................

    private void refreshUndo() {
        this.undo.setValue(
            this.context.undoCurrency()
        );
    }

    private final HistoryTokenSaveValueAnchorComponent<Currency> undo;

    // close............................................................................................................

    private void refreshClose() {
        this.close.setHistoryToken(
            Optional.of(
                this.context.historyToken()
                    .close()
            )
        );
    }

    private final HistoryTokenAnchorComponent close;

    // HistoryTokenAwareComponentLifecycle..............................................................................

    @Override
    public void dialogReset() {
        this.currency.clear();
    }

    /**
     * Give focus to the {@link CurrencyComponent}.
     */
    @Override
    public void openGiveFocus(final RefreshContext context) {
        context.giveFocus(
            this.currency::focus
        );
    }

    /**
     * Refreshes all components using the context.
     */
    @Override
    public void refresh(final RefreshContext context) {
        this.context.refreshDialogTitle(this);

        final Optional<Currency> undoCurrency = this.context.undoCurrency();

        this.currency.setValue(undoCurrency);
        this.save.setValue(undoCurrency);
        this.refreshClear();
        this.refreshUndo();
        this.refreshClose();
    }

    @Override
    public boolean shouldLogLifecycleChanges() {
        return SPREADSHEET_CURRENCY_COMPONENT_LIFECYCLE;
    }

    // SpreadsheetDeltaFetcherWatcher...................................................................................

    @Override
    public void onSpreadsheetDelta(final HttpMethod method,
                                   final AbsoluteOrRelativeUrl url,
                                   final SpreadsheetDelta delta) {
        this.refreshIfOpen(this.context);
    }

    // SpreadsheetMetadataFetcherWatcher................................................................................

    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata) {
        this.refreshIfOpen(this.context);
    }

    @Override
    public void onSpreadsheetMetadataSet(final Set<SpreadsheetMetadata> metadatas) {
        // Ignore many
    }

    // ComponentLifecycleMatcherDelegator...............................................................................

    @Override
    public ComponentLifecycleMatcher componentLifecycleMatcher() {
        return this.context;
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.dialog.toString();
    }
}
