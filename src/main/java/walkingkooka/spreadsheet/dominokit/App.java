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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import elemental2.dom.DomGlobal;
import elemental2.dom.History;
import jsinterop.base.Js;
import org.dominokit.domino.ui.layout.Layout;
import org.dominokit.domino.ui.utils.DominoElement;
import org.gwtproject.core.client.Scheduler;
import org.gwtproject.core.client.Scheduler.ScheduledCommand;
import org.jboss.elemento.EventType;
import walkingkooka.collect.set.Sets;
import walkingkooka.j2cl.locale.LocaleAware;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@LocaleAware
public class App implements EntryPoint, AppContext, UncaughtExceptionHandler {

    private final SpreadsheetViewportWidget viewportWidget = SpreadsheetViewportWidget.empty();

    // header = metadata toggle | clickable(editable) spreadsheet name
    // right = editable metadata properties
    // content = toolbar
    //   formula,
    //   table holding spreadsheet cells
    private final Layout layout = Layout.create();

    public void onModuleLoad() {
        GWT.setUncaughtExceptionHandler(this);

        SpreadsheetMetadata.EMPTY.toString(); // for registering of JsonContext types etc
        this.setupHistoryListener();

        this.prepareLayout();

        this.setSpreadsheetName("Untitled 123");
        this.showMetadataPanel(false);

        this.registerWindowResizeListener();
        this.fireInitialHashToken();
        this.fireInitialWindowSize();
    }

    // layout...........................................................................................................

    private void prepareLayout() {
        this.layout.fitHeight();
        this.layout.fitWidth();
        this.layout.setContent(this.viewportWidget.tableElement());

        this.layout.show();
    }

    private void registerWindowResizeListener() {
        DomGlobal.window.addEventListener(
                EventType.resize.getName(),
                (e) -> {
                    App.this.onResize(
                            DomGlobal.window.innerWidth,
                            DomGlobal.window.innerHeight
                    );
                }
        );
    }

    /**
     * Fire the window size. This is eventually used to compute the spreadsheet viewport size.
     */
    private void fireInitialWindowSize() {
        this.debug("App.fireInitialWindowSize");

        Scheduler.get()
                .scheduleDeferred(
                        new ScheduledCommand() {
                            @Override
                            public void execute() {
                                App.this.onResize(
                                        DomGlobal.window.innerWidth,
                                        DomGlobal.window.innerHeight
                                );
                            }
                        }
                );
    }

    private void onResize(final int width,
                          final int height) {
        final Layout layout = this.layout;
        final int navigationBarHeight = layout.getNavigationBar().element().offsetHeight;

        final int newHeight = height - navigationBarHeight;
        this.debug("App.onResize: " + width + " x " + height + " navigationBarHeight: " + navigationBarHeight + " newHeight: " + newHeight);

        this.viewportWidget.setHeight(newHeight);
    }

    // delta & metadata change watches..................................................................................

    @Override
    public SpreadsheetDeltaFetcher spreadsheetDeltaFetcher() {
        return this.spreadsheetDeltaFetcher;
    }

    private final SpreadsheetDeltaFetcher spreadsheetDeltaFetcher = SpreadsheetDeltaFetcher.with(
            (d, c) -> this.fireSpreadsheetDelta(d),
            this
    );

    @Override
    public SpreadsheetMetadataFetcher spreadsheetMetadataFetcher() {
        return this.spreadsheetMetadataFetcher;
    }

    private final SpreadsheetMetadataFetcher spreadsheetMetadataFetcher = SpreadsheetMetadataFetcher.with(
            (d, c) -> this.fireSpreadsheetMetadata(d),
            this
    );

    public void fireSpreadsheetDelta(final SpreadsheetDelta delta) {
        for(final SpreadsheetDeltaWatcher watcher : this.deltaWatchers) {
            watcher.onSpreadsheetDelta(
                    delta,
                    this
            );
        }
    }

    public void fireSpreadsheetMetadata(final SpreadsheetMetadata metadata) {
        final Optional<HistoryToken> maybeToken = this.historyToken();
        if(maybeToken.isPresent()) {
            final HistoryToken token = maybeToken.get();
            if(token instanceof SpreadsheetMetadataWatcher) {
                final SpreadsheetMetadataWatcher watcher = (SpreadsheetMetadataWatcher)token;
                watcher.onSpreadsheetMetadata(
                        metadata,
                        this
                );
            }
        }

        for(final SpreadsheetMetadataWatcher watcher : this.metadataWatchers) {
            watcher.onSpreadsheetMetadata(
                    metadata,
                    this
            );
        }
    }

    @Override
    public void addSpreadsheetDeltaWatcher(final SpreadsheetDeltaWatcher watcher) {
        Objects.requireNonNull(watcher, "watcher");
        this.deltaWatchers.add(watcher);
    }

    /**
     * A collection of listeners for {@link SpreadsheetDeltaWatcher}
     */
    final Set<SpreadsheetDeltaWatcher> deltaWatchers = Sets.ordered();

    @Override
    public void addSpreadsheetMetadataWatcher(final SpreadsheetMetadataWatcher watcher) {
        Objects.requireNonNull(watcher, "watcher");
        this.metadataWatchers.add(watcher);
    }
    
    /**
     * A collection of listeners for {@link SpreadsheetMetadataWatcher}
     */
    final Set<SpreadsheetMetadataWatcher> metadataWatchers = Sets.ordered();

    // misc..........................................................................................................

    public void setSpreadsheetName(final String name) {
        this.layout.setTitle(name);
    }

    private void showMetadataPanel(final boolean show) {
        final DominoElement<?> right = this.layout.getRightPanel();
        if (show) {
            right.show();
        } else {
            right.hide();
        }
    }

    // history..........................................................................................................

    private void setupHistoryListener() {
        DomGlobal.self.addEventListener(
                "hashchange",
                event -> this.onHashChange(
                        this.historyToken()
                )
        );
    }

    private void fireInitialHashToken() {
        final Optional<HistoryToken> token = this.historyToken();
        this.debug("App.fireInitialHashToken " + token.orElse(null));
        this.onHashChange(token);
    }

    @Override
    public Optional<HistoryToken> historyToken() {
        // remove the leading hash if necessary.
        String hash = DomGlobal.location.hash;
        if (hash.startsWith("#")) {
            hash = hash.substring(1);
        }

        return HistoryToken.parse(UrlFragment.parse(hash));
    }

    private void onHashChange(final Optional<HistoryToken> token) {
        try {
            final Optional<HistoryToken> previousToken = this.previousToken;
            debug("App.onHashChange from " + previousToken.orElse(null) + " to " + token.orElse(null));

            if (false == previousToken.equals(token)) {
                this.previousToken = token;

                if (token.isPresent()) {
                    this.pushAndFire(token.get());
                } else {
                    DomGlobal.location.hash = "";
                }
            }
        } catch (final Exception e) {
            error(e.getMessage());
        }
    }

    private void pushAndFire(final HistoryToken token) {
        this.pushHistoryToken(token);

        this.debug(token + " onHashChange");
        token.onHashChange(this);
    }

    /**
     * Pushes the given {@link HistoryToken} to the browser location#hash.
     */
    @Override
    public void pushHistoryToken(final HistoryToken token) {
        this.debug("pushHistoryToken " + token);
        DomGlobal.location.hash = "#" + token.urlFragment();
    }

    /**
     * Maybe later switch to history#pushState
     */
    private final History history = Js.cast(DomGlobal.self.history);

    /**
     * Used to track if the history token actually changed. Changes will fire the HistoryToken#onChange method.
     */
    private Optional<HistoryToken> previousToken = Optional.empty();

    // logging..........................................................................................................

    @Override
    public void onUncaughtException(final Throwable caught) {
        this.error(caught);
    }

    public void debug(final Object message) {
        DomGlobal.console.debug(message);
    }

    public void error(final Object message) {
        DomGlobal.console.error(message);
    }
}
