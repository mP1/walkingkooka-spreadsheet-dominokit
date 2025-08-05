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

package walkingkooka.spreadsheet.dominokit;

import elemental2.dom.DomGlobal;
import org.dominokit.domino.ui.events.EventType;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.dominokit.history.History;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers;
import walkingkooka.spreadsheet.dominokit.history.Historys;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetIdHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.UnknownHistoryToken;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContextDelegator;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewport;

import java.util.Objects;
import java.util.Optional;

/**
 * Combines the responsibilities and features relating to History events.
 */
final class AppHistoryContextHistoryWatcher implements HistoryContext,
    HistoryTokenWatcher,
    LoggingContextDelegator {

    static AppHistoryContextHistoryWatcher with(final AppContext appContext) {
        return new AppHistoryContextHistoryWatcher(
            Objects.requireNonNull(appContext, "appContext")
        );
    }

    private AppHistoryContextHistoryWatcher(final AppContext appContext) {
        this.appContext = appContext;

        this.history = Historys.elemental(appContext);
        this.previousToken = HistoryToken.unknown(UrlFragment.EMPTY);
        this.historyWatchers = HistoryTokenWatchers.empty();

        this.addHistoryTokenWatcher(this);

        DomGlobal.self.addEventListener(
            EventType.hashchange.getName(),
            event -> this.onHashChange(
                this.historyToken()
            )
        );
    }

    void onHashChange(final HistoryToken token) {
        final HistoryToken previousToken = this.previousToken;
        this.debug(this.getClass().getSimpleName() + ".onHashChange BEGIN from " + previousToken + " to " + token);

        this.previousToken = token;

        if (false == token.equals(previousToken)) {
            if (token instanceof UnknownHistoryToken) {
                this.debug(this.getClass().getSimpleName() + ".onHashChange updated with invalid token " + token + ", will restore previous " + previousToken);
                this.pushHistoryToken(previousToken);

            } else {
                this.historyWatchers.onHistoryTokenChange(
                    previousToken,
                    this.appContext
                );
            }
        }

        this.debug(this.getClass().getSimpleName() + ".onHashChange END from " + previousToken + " to " + token);
    }

    /**
     * Used to track if the history token actually changed. Changes will fire the HistoryToken#onChange method.
     */
    private HistoryToken previousToken;

    /**
     * Pushes the given {@link HistoryToken} to the browser location#hash.
     */
    @Override
    public void pushHistoryToken(final HistoryToken token) {
        Objects.requireNonNull(token, "token");

        HistoryToken push = token;

        // this check is necessary so when historyToken = something save and that gets pushed again we dont want to reply that again.
        final HistoryToken previous = this.firePrevious;
        if (push.shouldIgnore() && push.equals(previous)) {
            push = push.clearAction();
        }
        this.history.pushHistoryToken(push);
    }

    @Override
    public HistoryToken historyToken() {
        return this.history.historyToken();
    }

    private final History history;

    @Override
    public void fireCurrentHistoryToken() {
        final HistoryToken previous = this.historyToken();
        this.firePrevious = previous;

        try {
            this.historyWatchers.onHistoryTokenChange(
                previous,
                this.appContext
            );
        } finally {
            this.firePrevious = null;
        }
    }

    private HistoryToken firePrevious;

    @Override
    public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
        return this.historyWatchers.add(watcher);
    }

    @Override
    public Runnable addHistoryTokenWatcherOnce(final HistoryTokenWatcher watcher) {
        return this.historyWatchers.addOnce(watcher);
    }

    private final HistoryTokenWatchers historyWatchers;

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        // if the selection changed update metadata
        final HistoryToken historyToken = context.historyToken();
        if (false == historyToken.shouldIgnore()) {
            patchMetadataIfSelectionChanged(
                historyToken,
                context
            );
        }

        this.firePrevious = previous;
        try {
            if (historyToken instanceof HistoryTokenWatcher) {
                ((HistoryTokenWatcher) historyToken).onHistoryTokenChange(
                    previous,
                    context
                );
            }
        } finally {
            this.firePrevious = null;
        }
    }

    /**
     * Only PATCH the spreadsheet metadata on the server if the local {@link SpreadsheetMetadata} has a different
     * {@link AnchoredSpreadsheetSelection}. Note we ignore the previous {@link HistoryToken} because that might cause
     * synching issues where
     * <pre>
     * click A1
     *   load viewport selection=A1
     * click B2
     *   load viewport selection=A2
     * load viewport response
     *   update local metadata selection=A1
     *   push history selection=A1
     *   DONT want to PATCH metadata selection=A1 as this will cause load viewport selection=A2 to be ovewritten.
     * </pre>
     */
    private static void patchMetadataIfSelectionChanged(final HistoryToken historyToken,
                                                        final AppContext context) {
        if (historyToken instanceof SpreadsheetIdHistoryToken) {
            // check against local metadata NOT previous history selection, otherwise PATCH will be made
            // when a loadViewport has not yet updated history token with response selection.
            final Optional<AnchoredSpreadsheetSelection> selection = historyToken.anchoredSelectionOrEmpty();

            final Optional<AnchoredSpreadsheetSelection> previousSelection = context.spreadsheetMetadata()
                .get(SpreadsheetMetadataPropertyName.VIEWPORT)
                .flatMap(SpreadsheetViewport::anchoredSelection);
            if (false == selection.equals(previousSelection)) {

                context.debug(AppHistoryContextHistoryWatcher.class.getSimpleName() + ".patchMetadataIfSelectionChanged selection changed from " + previousSelection.orElse(null) + " TO " + selection.orElse(null) + " will update Metadata");

                // initially metadata will be empty because it has not yet loaded, context.viewport below will fail.
                if (context.spreadsheetMetadata()
                    .get(SpreadsheetMetadataPropertyName.VIEWPORT)
                    .isPresent()) {
                    final SpreadsheetIdHistoryToken spreadsheetIdHistoryToken = (SpreadsheetIdHistoryToken) historyToken;
                    context.spreadsheetMetadataFetcher()
                        .patchMetadata(
                            spreadsheetIdHistoryToken.id(),
                            SpreadsheetMetadataPropertyName.VIEWPORT.patch(
                                selection.map(
                                    s -> context.viewport(
                                        Optional.of(s)
                                    )
                                ).orElse(null)
                            )
                        );
                }
            }
        }
    }

    // LoggingContextDelegator..........................................................................................

    @Override
    public LoggingContext loggingContext() {
        return this.appContext;
    }

    private final AppContext appContext;
}
