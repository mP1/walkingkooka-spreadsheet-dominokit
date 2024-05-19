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

package walkingkooka.spreadsheet.dominokit.ui.toolbar;

import elemental2.dom.HTMLDivElement;
import elemental2.dom.Node;
import org.dominokit.domino.ui.IsElement;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.net.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.NopNoResponseWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetViewportComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.ui.flexlayout.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.ui.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A toolbar that contains icons that trigger an action.
 */
public final class SpreadsheetToolbarComponent implements HtmlElementComponent<HTMLDivElement, SpreadsheetToolbarComponent>,
        SpreadsheetViewportComponentLifecycle,
        LoadedSpreadsheetMetadataRequired,
        NopFetcherWatcher,
        NopNoResponseWatcher,
        SpreadsheetDeltaFetcherWatcher,
        TreePrintable {

    public static SpreadsheetToolbarComponent with(final AppContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetToolbarComponent(context);
    }

    private SpreadsheetToolbarComponent(final AppContext context) {
        this.flexLayout = this.createFlexLayout(context);

        context.addHistoryTokenWatcher(this);
        context.addSpreadsheetDeltaFetcherWatcher(this);

        this.setVisibility(false); // initially hidden.

        this.context = context;
    }

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetToolbarComponent setCssText(final String css) {
        this.flexLayout.setCssText(css);
        return this;
    }

    // isElement........................................................................................................

    @Override
    public HTMLDivElement element() {
        return this.flexLayout.element();
    }

    // node.............................................................................................................

    @Override
    public Node node() {
        return this.element();
    }

    private final SpreadsheetFlexLayout flexLayout;

    /**
     * Creates a {@link SpreadsheetFlexLayout} and populates it with the toolbar icons etc.
     */
    private SpreadsheetFlexLayout createFlexLayout(final AppContext context) {
        return SpreadsheetFlexLayout.row()
                .displayBlock() // without this the toolbar rows have a undesirable line "height" instead of meeting.
                .appendChildren(
                        components(context)
                );
    }

    private static List<SpreadsheetToolbarComponentItem<?>> components(final AppContext context) {
        return Lists.of(
                SpreadsheetToolbarComponentItem.bold(context),
                SpreadsheetToolbarComponentItem.italics(context),
                SpreadsheetToolbarComponentItem.strikeThru(context),
                SpreadsheetToolbarComponentItem.underline(context),
                SpreadsheetToolbarComponentItem.textAlignLeft(context),
                SpreadsheetToolbarComponentItem.textAlignCenter(context),
                SpreadsheetToolbarComponentItem.textAlignRight(context),
                SpreadsheetToolbarComponentItem.textAlignJustify(context),
                // vertical
                SpreadsheetToolbarComponentItem.verticalAlignTop(context),
                SpreadsheetToolbarComponentItem.verticalAlignMiddle(context),
                SpreadsheetToolbarComponentItem.verticalAlignBottom(context),
                // case
                SpreadsheetToolbarComponentItem.textCaseCapitalize(context),
                SpreadsheetToolbarComponentItem.textCaseLowercase(context),
                SpreadsheetToolbarComponentItem.textCaseUppercase(context),
                // pattern
                SpreadsheetToolbarComponentItem.formatPattern(context),
                SpreadsheetToolbarComponentItem.parsePattern(context),
                // clear
                SpreadsheetToolbarComponentItem.clearStyle(context),
                // metadata properties
                SpreadsheetToolbarComponentItem.hideZeroValues(context),
                // FindCells | Highlight
                SpreadsheetToolbarComponentItem.findCells(context),
                SpreadsheetToolbarComponentItem.highlightCells(context),
                // SORT
                SpreadsheetToolbarComponentItem.sort(context),
                // label
                SpreadsheetToolbarComponentItem.labelCreate(context),
                // reload
                SpreadsheetToolbarComponentItem.reload(context),
                // swagger
                SpreadsheetToolbarComponentItem.swagger(context)
        );
    }

    // SpreadsheetDeltaFetcherWatcher..........................................................................................

    @Override
    public void onSpreadsheetDelta(final HttpMethod method,
                                   final AbsoluteOrRelativeUrl url,
                                   final SpreadsheetDelta delta,
                                   final AppContext context) {
        this.refreshIfOpen(context);
    }

    // SpreadsheetViewportComponentLifecycle............................................................................

    @Override
    public SpreadsheetViewportCache spreadsheetViewportCache() {
        return this.context.spreadsheetViewportCache();
    }

    @Override
    public SpreadsheetMetadata spreadsheetMetadata() {
        return this.context.spreadsheetMetadata();
    }

    private final AppContext context;

    // ComponentLifecycle..............................................................................................

    @Override
    public boolean isOpen() {
        return this.open;
    }

    @Override
    public void open(final AppContext context) {
        this.open = true;
        this.setVisibility(true); // show
    }

    @Override
    public void close(final AppContext context) {
        this.open = false;
        this.setVisibility(false); // hidden
    }

    private boolean open;

    @Override
    public void refresh(final AppContext context) {
        for (final IsElement<?> component : this.flexLayout.children()) {
            final SpreadsheetToolbarComponentItem<?> item = (SpreadsheetToolbarComponentItem<?>) component;
            item.refresh(
                    context
            );
        }
    }

    @Override
    public void openGiveFocus(final AppContext context) {
        // nop
    }

    @Override
    public boolean shouldLogLifecycleChanges() {
        return false; // child items return false
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.flexLayout.printTree(printer);
        }
        printer.outdent();
    }

    // element..........................................................................................................

    // viewport-column-A
    public static <T> String id(final TextStylePropertyName<T> propertyName,
                                final Optional<T> value) {
        return TOOLBAR_ID_PREFIX +
                propertyName.constantName().toLowerCase() +
                value.map(
                        v -> '-' + v.toString().toUpperCase()
                ).orElse("");
    }

    public static String findCellsId() {
        return TOOLBAR_ID_PREFIX + "find-cells";
    }

    public static String formatPatternId() {
        return TOOLBAR_ID_PREFIX + "format-pattern";
    }

    public static String hideZeroValues() {
        return TOOLBAR_ID_PREFIX + "hide-zero-values";
    }

    public static String highlightId() {
        return TOOLBAR_ID_PREFIX + "highlight";
    }

    public static String labelCreateId() {
        return TOOLBAR_ID_PREFIX + "label-create";
    }

    public static String parsePatternId() {
        return TOOLBAR_ID_PREFIX + "parse-pattern";
    }

    public static String reloadId() {
        return TOOLBAR_ID_PREFIX + "reload";
    }

    public static String swaggerId() {
        return TOOLBAR_ID_PREFIX + "swagger";
    }

    final static String TOOLBAR_ID_PREFIX = "toolbar-";
}
