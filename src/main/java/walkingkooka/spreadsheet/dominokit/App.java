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
import elemental2.dom.Element;
import org.dominokit.domino.ui.layout.Layout;
import org.dominokit.domino.ui.utils.DominoElement;
import org.gwtproject.core.client.Scheduler;
import org.gwtproject.core.client.Scheduler.ScheduledCommand;
import org.jboss.elemento.EventType;
import walkingkooka.collect.set.Sets;
import walkingkooka.color.Color;
import walkingkooka.j2cl.locale.LocaleAware;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetIdHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetSelectionHistoryToken;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataWatcher;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportWidget;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.text.BorderStyle;
import walkingkooka.tree.text.FontFamily;
import walkingkooka.tree.text.FontSize;
import walkingkooka.tree.text.FontStyle;
import walkingkooka.tree.text.FontVariant;
import walkingkooka.tree.text.FontWeight;
import walkingkooka.tree.text.Hyphens;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.tree.text.VerticalAlign;
import walkingkooka.tree.text.WordBreak;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

@LocaleAware
public class App implements EntryPoint, AppContext, HistoryTokenWatcher, SpreadsheetMetadataWatcher, SpreadsheetDeltaWatcher, UncaughtExceptionHandler {


    public App() {
        SpreadsheetDelta.EMPTY.toString(); // force json register.
    }

    // header = metadata toggle | clickable(editable) spreadsheet name
    // right = editable metadata properties
    // content = toolbar
    //   formula,
    //   table holding spreadsheet cells
    private final Layout layout = Layout.create();

    public void onModuleLoad() {
        this.addSpreadsheetDeltaWatcher(this);
        this.addSpreadsheetMetadataWatcher(this);
        GWT.setUncaughtExceptionHandler(this);

        this.setupHistoryListener();

        this.prepareLayout();

        this.setSpreadsheetName("Untitled 123");
        this.showMetadataPanel(false);

        this.registerWindowResizeListener();
        this.fireInitialHistoryToken();
        this.fireInitialWindowSize();
    }

    // layout...........................................................................................................

    private void prepareLayout() {
        this.addHistoryWatcher(this.viewportWidget);

        this.layout.fitHeight();
        this.layout.fitWidth();
        this.layout.setContent(this.viewportWidget.element());

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

        this.viewportWidget.setWidthAndHeight(
                width,
                newHeight
        );
    }

    // delta & metadata change watches..................................................................................

    /**
     * If a viewport selection is present then copy the received selection even if its now gone.
     */
    @Override
    public void onSpreadsheetDelta(final SpreadsheetDelta delta,
                                   final AppContext context) {
        final HistoryToken historyToken = context.historyToken();

        // if a selection is already present copy from the metadata
        if (historyToken instanceof SpreadsheetSelectionHistoryToken) {
            final HistoryToken withViewportSelection = historyToken.setViewportSelection(
                    delta.viewportSelection()
            );

            if (false == historyToken.equals(withViewportSelection)) {
                context.debug("App.onSpreadsheetDelta selection active, updating " + withViewportSelection);
                context.pushHistoryToken(
                        withViewportSelection
                );
            }
        }
    }

    /**
     * Update the spreadsheet-id, spreadsheet-name and viewport selection from the given {@link SpreadsheetMetadata}.
     */
    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        final Optional<SpreadsheetId> id = metadata.id();
        final Optional<SpreadsheetName> name = metadata.name();

        if (id.isPresent() && name.isPresent()) {
            final HistoryToken historyToken = context.historyToken();
            final HistoryToken idNameViewportSelectionHistoryToken = historyToken
                    .setIdAndName(
                            id.get(),
                            name.get()
                    ).setViewportSelection(
                            metadata.get(SpreadsheetMetadataPropertyName.SELECTION)
                    );

            if (false == historyToken.equals(idNameViewportSelectionHistoryToken)) {
                context.debug("App.onSpreadsheetMetadata from " + historyToken + " to different id/name/viewportSelection " + idNameViewportSelectionHistoryToken);
                context.pushHistoryToken(idNameViewportSelectionHistoryToken);
            }
        }
    }

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
        for (final SpreadsheetDeltaWatcher watcher : this.deltaWatchers) {
            fireSpreadsheetDelta(delta, watcher);
        }
    }

    private void fireSpreadsheetDelta(final SpreadsheetDelta delta,
                                      final SpreadsheetDeltaWatcher watcher) {
        this.callAndCatch(
                delta,
                watcher::onSpreadsheetDelta
        );
    }

    public void fireSpreadsheetMetadata(final SpreadsheetMetadata metadata) {
        this.spreadsheetMetadata = metadata;

        final HistoryToken token = this.historyToken();
        if (token instanceof SpreadsheetMetadataWatcher) {
            this.fireSpreadsheetMetadata(
                    metadata,
                    (SpreadsheetMetadataWatcher) token
            );
        }

        for (final SpreadsheetMetadataWatcher watcher : this.metadataWatchers) {
            this.fireSpreadsheetMetadata(
                    metadata,
                    watcher
            );
        }
    }

    private void fireSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                         final SpreadsheetMetadataWatcher watcher) {
        this.callAndCatch(
                metadata,
                watcher::onSpreadsheetMetadata
        );
    }

    private <T> void callAndCatch(final T value,
                                  final BiConsumer<T, AppContext> fire) {
        try {
            fire.accept(
                    value,
                    this
            );
        } catch (final RuntimeException cause) {
            this.error(cause);
        }
    }

    /**
     * Returns the current or last loaded {@link SpreadsheetMetadata}
     */
    @Override
    public SpreadsheetMetadata spreadsheetMetadata() {
        return this.spreadsheetMetadata;
    }

    private SpreadsheetMetadata spreadsheetMetadata = SpreadsheetMetadata.EMPTY;

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

    // history eventListener............................................................................................

    private void fireInitialHistoryToken() {
        final HistoryToken token = this.historyToken();
        this.debug("App.fireInitialHistoryToken " + token);
        this.onHistoryTokenChange(token);
    }

    private void setupHistoryListener() {
        this.addHistoryWatcher(this);

        DomGlobal.self.addEventListener(
                EventType.hashchange.getName(),
                event -> this.onHistoryTokenChange(
                        this.historyToken()
                )
        );
    }

    private void onHistoryTokenChange(final HistoryToken token) {
        try {
            final HistoryToken previousToken = this.previousToken;
            debug("App.onHistoryTokenChange from " + previousToken + " to " + token);

            if (false == token.equals(previousToken)) {
                this.fireOnHistoryTokenChange(previousToken);
                this.pushHistoryToken(token);
            }

        } catch (final Exception e) {
            error(e.getMessage());
        }
    }

    private void fireOnHistoryTokenChange(final HistoryToken previous) {
        this.debug("App.fireOnHistoryTokenChange from " + previous + " to " + this.historyToken());

        final String hash = DomGlobal.location.hash;

        for (final HistoryTokenWatcher watcher : this.historyWatchers) {
            final String hash2 = DomGlobal.location.hash;
            if (false == hash.equals(hash2)) {
                this.debug("App.fireOnHistoryTokenChange aborted " + hash + " to " + hash2);
                break;
            }

            this.fireHistoryWatcher(
                    previous,
                    watcher
            );
        }

        this.previousToken = this.historyToken();
    }

    private void fireHistoryWatcher(final HistoryToken token,
                                    final HistoryTokenWatcher watcher) {
        this.callAndCatch(
                token,
                watcher::onHistoryTokenChange
        );
    }

    // AppContext history...............................................................................................

    /**
     * Pushes the given {@link HistoryToken} to the browser location#hash.
     */
    @Override
    public void pushHistoryToken(final HistoryToken token) {
        Scheduler.get()
                .scheduleDeferred(
                        () -> {
                            final String newHash = "#" + token.urlFragment();
                            final String current = DomGlobal.location.hash;
                            if (false == current.equals(newHash)) {
                                this.debug("App.pushHistoryToken from " + CharSequences.quoteAndEscape(current) + " to " + newHash + " " + token);

                                DomGlobal.location.hash = newHash;
                            }
                        }
                );
    }

    @Override
    public HistoryToken historyToken() {
        // remove the leading hash if necessary.
        String hash = DomGlobal.location.hash;
        if (false == hash.equals(this.locationHash)) {
            if (hash.startsWith("#")) {
                hash = hash.substring(1);
            }

            this.locationHash = hash;
            this.historyToken = HistoryToken.parse(UrlFragment.parse(hash));
        }

        return this.historyToken;
    }

    /**
     * The original window.location.hash used to produce the {@link #historyToken}.
     */
    private String locationHash;

    private HistoryToken historyToken;

    // HistoryWatcher...................................................................................................

    @Override
    public void addHistoryWatcher(final HistoryTokenWatcher watcher) {
        Objects.requireNonNull(watcher, "watcher");

        this.historyWatchers.add(watcher);
    }

    private final Set<HistoryTokenWatcher> historyWatchers = Sets.ordered();

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {

        // if the viewport selection changed update metadata
        final HistoryToken historyToken = context.historyToken();
        if (historyToken instanceof SpreadsheetIdHistoryToken) {
            final Optional<SpreadsheetViewportSelection> viewportSelection = historyToken.viewportSelectionOrEmpty();
            final Optional<SpreadsheetViewportSelection> previousViewportSelection = previous.viewportSelectionOrEmpty();
            if (false == viewportSelection.equals(previousViewportSelection)) {

                context.debug("App.onHistoryTokenChange viewportSelection changed from " + previousViewportSelection.orElse(null) + " TO " + viewportSelection.orElse(null) + " will update Metadata");

                final SpreadsheetIdHistoryToken spreadsheetIdHistoryToken = (SpreadsheetIdHistoryToken) historyToken;
                context.spreadsheetMetadataFetcher()
                        .patchMetadata(
                                spreadsheetIdHistoryToken.id(),
                                SpreadsheetMetadataPropertyName.SELECTION.patch(
                                        viewportSelection.orElse(null)
                                )
                        );
            }
        }

        historyToken.onHistoryTokenChange(
                previous,
                context
        );
    }

    /**
     * Used to track if the history token actually changed. Changes will fire the HistoryToken#onChange method.
     */
    private HistoryToken previousToken = HistoryToken.unknown(UrlFragment.EMPTY);

    // Viewport.........................................................................................................

    /**
     * Init here to avoid race conditions with other fields like {@link #metadataWatchers}.
     */
    private final SpreadsheetViewportWidget viewportWidget = SpreadsheetViewportWidget.empty(this);

    @Override
    public void giveFcrmulaTextBoxFocus() {
        this.debug("App.giveFcrmulaTextBoxFocus");

        this.giveFocus(
                this.viewportWidget::giveFcrmulaTextBoxFocus
        );
    }

    @Override
    public Set<SpreadsheetCellRange> viewportWindow() {
        return this.viewportWidget.window();
    }

    @Override
    public TextStyle viewportAll(final boolean selected) {
        return this.viewportColumnRowHeader(selected);
    }

    @Override
    public TextStyle viewportCell(final boolean selected) {
        return CELL_STYLE.set(
                TextStylePropertyName.BACKGROUND_COLOR,
                selected ? CELL_SELECTED : CELL_UNSELECTED
        );
    }

    private final static Color BORDER_COLOR = Color.BLACK;
    private final static BorderStyle BORDER_STYLE = BorderStyle.SOLID;
    private final static Length<?> BORDER_LENGTH = Length.pixel(1.0);

    /**
     * THe default style for the TD holding the formatted cell.
     */
    private final static TextStyle CELL_STYLE = TextStyle.EMPTY
            .setMargin(
                    Length.none()
            ).setBorder(
                    BORDER_COLOR,
                    BORDER_STYLE,
                    BORDER_LENGTH

            ).setPadding(
                    Length.none()
            ).set(
                    TextStylePropertyName.TEXT_ALIGN,
                    TextAlign.LEFT
            ).set(
                    TextStylePropertyName.VERTICAL_ALIGN,
                    VerticalAlign.TOP
            ).set(
                    TextStylePropertyName.FONT_FAMILY,
                    FontFamily.with("MS Sans Serif")
            ).set(
                    TextStylePropertyName.FONT_SIZE,
                    FontSize.with(11)
            ).set(
                    TextStylePropertyName.FONT_STYLE,
                    FontStyle.NORMAL
            ).set(
                    TextStylePropertyName.FONT_WEIGHT,
                    FontWeight.NORMAL
            ).set(
                    TextStylePropertyName.FONT_VARIANT,
                    FontVariant.NORMAL
            ).set(
                    TextStylePropertyName.HYPHENS,
                    Hyphens.NONE
            ).set(
                    TextStylePropertyName.WORD_BREAK,
                    WordBreak.NORMAL
            ); // overflow-wrap ?

    // TODO get these from theme
    private final static Color CELL_SELECTED = Color.parse("#ccc");
    private final static Color CELL_UNSELECTED = Color.parse("#fff");

    @Override
    public TextStyle viewportColumnHeader(final boolean selected) {
        return this.viewportColumnRowHeader(selected);
    }

    @Override
    public TextStyle viewportRowHeader(final boolean selected) {
        return this.viewportColumnRowHeader(selected);
    }

    private TextStyle viewportColumnRowHeader(final boolean selected) {
        return COLUMN_ROW_STYLE.set(
                TextStylePropertyName.BACKGROUND_COLOR,
                selected ? COLUMN_ROW_SELECTED : COLUMN_ROW_UNSELECTED
        );
    }

    private final static TextStyle COLUMN_ROW_STYLE = TextStyle.EMPTY
            .setMargin(
                    Length.none()
            ).setBorder(
                    BORDER_COLOR,
                    BORDER_STYLE,
                    BORDER_LENGTH

            ).setPadding(
                    Length.none()
            ).set(
                    TextStylePropertyName.TEXT_ALIGN,
                    TextAlign.CENTER
            ).set(
                    TextStylePropertyName.VERTICAL_ALIGN,
                    VerticalAlign.MIDDLE
            ).set(
                    TextStylePropertyName.FONT_WEIGHT,
                    FontWeight.NORMAL
            );

    // TODO get these from theme
    private final static Color COLUMN_ROW_SELECTED = Color.parse("#555");
    private final static Color COLUMN_ROW_UNSELECTED = Color.parse("#aaa");

    @Override
    public void giveViewportFocus(final SpreadsheetSelection selection) {
        this.debug("App.giveViewportFocus " + selection);

        this.giveFocus(() -> this.giveViewportFocus0(selection));
    }

    private void giveViewportFocus0(final SpreadsheetSelection selection) {
        final Optional<Element> maybeElement = this.findViewportElement(selection);
        if (maybeElement.isPresent()) {
            Element element = maybeElement.get();

            boolean give = true;

            final Element active = DomGlobal.document.activeElement;
            if (null != active) {
                // verify active element belongs to the same selection. if it does it must have focus so no need to focus again
                give = false == Doms.isOrHasChild(
                        element,
                        active
                );
            }

            if (give) {
                // for column/row the anchor and not the TH/TD should receive focus.
                if (selection.isColumnReference() || selection.isRowReference()) {
                    element = element.firstElementChild;
                }

                this.debug("App.giveViewportFocus0 " + selection + " focus element " + element);
                element.focus();
            }
        } else {
            this.debug("App.giveViewportFocus0 " + selection + " element not found!");
        }
    }

    @Override
    public Optional<Element> findViewportElement(final SpreadsheetSelection selection) {
        Objects.requireNonNull(selection, "selection");

        return Optional.ofNullable(
                DomGlobal.document
                        .getElementById(
                                SpreadsheetViewportWidget.id(selection)
                        )
        );
    }

    // focus............................................................................................................

    /**
     * Schedules giving focus to the {@link Element} if it exists. If multiple attempts are made to give focus in a short
     * period of time an {@link IllegalStateException} will be thrown.
     */
    private void giveFocus(final Runnable giveFocus) {
        this.debug("App.giveFocus " + giveFocus);

        final Runnable existingGiveFocus = this.giveFocus;
        if (null != existingGiveFocus) {
            this.giveFocus = null;
            throw new IllegalStateException("Second attempt to give focus " + existingGiveFocus + " AND " + giveFocus);
        }
        this.giveFocus = giveFocus;

        Scheduler.get()
                .scheduleDeferred(this::giveFocus0);
    }

    /**
     * If {@link Runnable #giveFocus} is available run it.
     */
    private void giveFocus0() {
        final Runnable giveFocus = this.giveFocus;
        this.giveFocus = null;

        if (null != giveFocus) {
            giveFocus.run();
        }
    }

    /**
     * A {@link Runnable} which will give focus to some element. This is used to track and prevent multiple give focus attempts
     */
    private Runnable giveFocus;

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
