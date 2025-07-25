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

import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcher;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherDelegator;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.anchor.HistoryTokenSaveValueAnchorComponent;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.flex.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.link.SpreadsheetLinkListComponent;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A model dialog that displays a {@link SpreadsheetLocaleComponent} allowing the user to pick a {@link java.util.Locale}.
 */
public final class SpreadsheetLocaleDialogComponent implements SpreadsheetDialogComponentLifecycle,
    LoadedSpreadsheetMetadataRequired,
    SpreadsheetDeltaFetcherWatcher,
    SpreadsheetMetadataFetcherWatcher,
    ComponentLifecycleMatcherDelegator,
    NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher {

    /**
     * Creates a new {@link SpreadsheetLocaleDialogComponent}.
     */
    public static SpreadsheetLocaleDialogComponent with(final SpreadsheetLocaleDialogComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetLocaleDialogComponent(context);
    }

    private SpreadsheetLocaleDialogComponent(final SpreadsheetLocaleDialogComponentContext context) {
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

    private SpreadsheetDialogComponent dialogCreate() {
        final SpreadsheetLocaleDialogComponentContext context = this.context;

        return SpreadsheetDialogComponent.smallerPrompt(
            ID + SpreadsheetElementIds.DIALOG,
            "Locale",
            SpreadsheetDialogComponent.INCLUDE_CLOSE,
            context
        ).appendChild(
            SpreadsheetFlexLayout.row()
                .appendChild(this.locale)
        ).appendChild(
            SpreadsheetLinkListComponent.empty()
                .appendChild(this.save)
                .appendChild(this.clear)
                .appendChild(this.undo)
                .appendChild(this.close)
        );
    }

    private final SpreadsheetDialogComponent dialog;

    private final SpreadsheetLocaleDialogComponentContext context;

    @Override
    public SpreadsheetDialogComponent dialog() {
        return this.dialog;
    }

    // id...............................................................................................................

    @Override
    public String idPrefix() {
        return ID_PREFIX;
    }

    private final static String ID = "locale";

    private final static String ID_PREFIX = ID + "-";

    // localeComponent..................................................................................................

    private SpreadsheetLocaleComponent locale() {
        return SpreadsheetLocaleComponent.empty(this.context)
            .optional()
            .addChangeListener(
                (Optional<Locale> oldLocale, Optional<Locale> newLocale) -> this.save.setValue(newLocale)
            );
    }

    private final SpreadsheetLocaleComponent locale;

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
     * Give focus to the {@link SpreadsheetLocaleComponent}.
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
        final Optional<Locale> undoLocale = this.context.undoLocale();

        this.dialog.setTitle(
            this.context.dialogTitle()
        );

        this.locale.setValue(undoLocale);
        this.save.setValue(undoLocale);
        this.refreshClear();
        this.refreshUndo();
        this.refreshClose();
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
