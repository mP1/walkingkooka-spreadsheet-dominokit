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

package walkingkooka.spreadsheet.dominokit.locale;

import org.dominokit.domino.ui.menu.MenuItem;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.http.HttpMethod;
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
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.flex.FlexLayoutComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.link.AnchorListComponent;
import walkingkooka.spreadsheet.dominokit.suggestbox.SuggestBoxComponent;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.server.locale.LocaleHateosResource;
import walkingkooka.spreadsheet.server.locale.LocaleHateosResourceSet;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A model dialog that displays a {@link LocaleComponent} allowing the user to pick a {@link java.util.Locale}.
 */
public final class LocaleDialogComponent implements DialogComponentLifecycle,
    LoadedSpreadsheetMetadataRequired,
    SpreadsheetDeltaFetcherWatcher,
    SpreadsheetMetadataFetcherWatcher,
    ComponentLifecycleMatcherDelegator,
    NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher {

    /**
     * Creates a new {@link LocaleDialogComponent}.
     */
    public static LocaleDialogComponent with(final LocaleDialogComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new LocaleDialogComponent(context);
    }

    private LocaleDialogComponent(final LocaleDialogComponentContext context) {
        this.context = context;

        this.locale = this.locale();

        this.save = this.<Locale>saveValueAnchor(context);

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
        final LocaleDialogComponentContext context = this.context;

        return DialogComponent.smallerPrompt(
            ID + SpreadsheetElementIds.DIALOG,
            DialogComponent.INCLUDE_CLOSE,
            context
        ).appendChild(
            FlexLayoutComponent.row()
                .appendChild(this.locale)
        ).appendChild(
            AnchorListComponent.empty()
                .appendChild(this.save)
                .appendChild(this.clear)
                .appendChild(this.undo)
                .appendChild(this.close)
        );
    }

    private final DialogComponent dialog;

    private final LocaleDialogComponentContext context;

    @Override
    public DialogComponent dialog() {
        return this.dialog;
    }

    // id...............................................................................................................

    @Override
    public String idPrefix() {
        return ID_PREFIX;
    }

    private final static String ID = Locale.class.getSimpleName();

    private final static String ID_PREFIX = ID + "-";

    // localeComponent..................................................................................................

    private LocaleComponent<Locale> locale() {
        return LocaleComponent.empty(
                new LocaleComponentContext<Locale>() {

                    @Override
                    public Optional<LocaleComponentSuggestionsValue<Locale>> toValue(final Locale locale) {
                        return context.localeText(locale)
                            .map(t -> LocaleComponentSuggestionsValue.with(
                                    locale,
                                    t,
                                    locale
                                )
                            );
                    }

                    @Override
                    public void filter(final String startsWith,
                                       final SuggestBoxComponent<LocaleComponentSuggestionsValue<Locale>> suggestBox) {
                        suggestBox.setOptions(
                            LocaleHateosResourceSet.filter(startsWith, LocaleDialogComponent.this.context)
                                .stream()
                                .map(
                                    (LocaleHateosResource lhr) ->
                                        LocaleComponentSuggestionsValue.with(
                                            lhr.locale(),
                                            lhr.text(),
                                            lhr.locale()
                                        )
                                ).sorted()
                                .collect(Collectors.toList())
                        );
                    }

                    @Override
                    public void verifyOption(final LocaleComponentSuggestionsValue<Locale> value,
                                             final SuggestBoxComponent<LocaleComponentSuggestionsValue<Locale>> suggestBox) {
                        LocaleComponentSuggestionsValue<Locale> verified = null;

                        if (null != value) {
                            final Locale locale = value.locale();
                            final String localeText = LocaleDialogComponent.this.context.localeText(locale)
                                .orElse(null);
                            verified = LocaleComponentSuggestionsValue.with(
                                locale,
                                localeText,
                                locale
                            );
                        }

                        if (null != verified) {
                            suggestBox.setVerifiedOption(verified);
                        }
                    }

                    @Override
                    public MenuItem<LocaleComponentSuggestionsValue<Locale>> createMenuItem(final LocaleComponentSuggestionsValue<Locale> value) {
                        return this.historyTokenMenuItem(
                            ID,
                            value,
                            LocaleDialogComponent.this.context
                        );
                    }
                }
            ).optional()
            .addChangeListener(
                (Optional<Locale> oldLocale, Optional<Locale> newLocale) -> this.save.setValue(newLocale)
            );
    }

    private final LocaleComponent<Locale> locale;

    // save.............................................................................................................

    private final HistoryTokenSaveValueAnchorComponent<Locale> save;

    // clear.............................................................................................................

    private void refreshClear() {
        this.clear.clearValue();
    }

    private final HistoryTokenSaveValueAnchorComponent<Locale> clear;

    // undo.............................................................................................................

    private void refreshUndo() {
        this.undo.setValue(
            this.context.undoLocale()
        );
    }

    private final HistoryTokenSaveValueAnchorComponent<Locale> undo;

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
        this.locale.clear();
    }

    /**
     * Give focus to the {@link LocaleComponent}.
     */
    @Override
    public void openGiveFocus(final RefreshContext context) {
        context.giveFocus(
            this.locale::focus
        );
    }

    /**
     * Refreshes all components using the context.
     */
    @Override
    public void refresh(final RefreshContext context) {
        this.context.refreshDialogTitle(this);

        final Optional<Locale> undoLocale = this.context.undoLocale();

        this.locale.setValue(undoLocale);
        this.save.setValue(undoLocale);
        this.refreshClear();
        this.refreshUndo();
        this.refreshClose();
    }

    @Override
    public boolean shouldLogLifecycleChanges() {
        return SPREADSHEET_LOCALE_COMPONENT_LIFECYCLE;
    }

    // SpreadsheetDeltaFetcherWatcher...................................................................................

    @Override
    public void onSpreadsheetDelta(final HttpMethod method,
                                   final AbsoluteOrRelativeUrl url,
                                   final SpreadsheetDelta delta,
                                   final AppContext context) {
        this.refreshIfOpen(context);
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
