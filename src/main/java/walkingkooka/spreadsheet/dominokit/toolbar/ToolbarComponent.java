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

package walkingkooka.spreadsheet.dominokit.toolbar;

import elemental2.dom.HTMLDivElement;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.flex.FlexLayoutComponent;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportComponentLifecycle;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A toolbar that contains icons that trigger an action.
 */
public final class ToolbarComponent implements HtmlComponentDelegator<HTMLDivElement, ToolbarComponent>,
    SpreadsheetViewportComponentLifecycle,
    LoadedSpreadsheetMetadataRequired,
    NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher,
    SpreadsheetDeltaFetcherWatcher {

    public static ToolbarComponent with(final ToolbarComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new ToolbarComponent(context);
    }

    private ToolbarComponent(final ToolbarComponentContext context) {
        this.flexLayout = this.createFlexLayout(context);

        context.addSpreadsheetDeltaFetcherWatcher(this);

        this.context = context;
    }

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLDivElement, ?> htmlComponent() {
        return this.flexLayout;
    }

    private final FlexLayoutComponent flexLayout;

    /**
     * Creates a {@link FlexLayoutComponent} and populates it with the toolbar icons etc.
     */
    private FlexLayoutComponent createFlexLayout(final ToolbarComponentContext context) {
        return FlexLayoutComponent.row()
            .displayBlock() // without this the toolbar rows have a undesirable line "height" instead of meeting.
            .appendChildren(
                components(context)
            );
    }

    /**
     * Returns all the components that will occupy the toolbar.
     * <br>
     * Basic organisation
     * <ol>
     *     <li>styling</li>
     *     <li>metadata switches (hide zero values)</li>
     *     <li>finding/sorting/highlighting</li>
     *     <li>labels</li>
     *     <li>reload</li>
     *     <li>plugin</li>
     *     <li>swagger (maybe remove later or hide)</li>
     * </ol>
     */
    private static List<ToolbarComponentItem<?>> components(final ToolbarComponentContext context) {
        return Lists.of(
            // style
            ToolbarComponentItem.bold(context),
            ToolbarComponentItem.italics(context),
            ToolbarComponentItem.strikeThru(context),
            ToolbarComponentItem.underline(context),
            // text
            ToolbarComponentItem.textAlignLeft(context),
            ToolbarComponentItem.textAlignCenter(context),
            ToolbarComponentItem.textAlignRight(context),
            ToolbarComponentItem.textAlignJustify(context),
            // vertical
            ToolbarComponentItem.verticalAlignTop(context),
            ToolbarComponentItem.verticalAlignMiddle(context),
            ToolbarComponentItem.verticalAlignBottom(context),
            // case
            ToolbarComponentItem.textCaseCapitalize(context),
            ToolbarComponentItem.textCaseLowercase(context),
            ToolbarComponentItem.textCaseUppercase(context),
            // format/pattern
            ToolbarComponentItem.currency(context),
            ToolbarComponentItem.formatter(context),
            ToolbarComponentItem.dateTimeSymbols(context),
            ToolbarComponentItem.parser(context),
            ToolbarComponentItem.decimalNumberSymbols(context),
            ToolbarComponentItem.locale(context),
            // clear
            ToolbarComponentItem.clearStyle(context),
            // validator
            ToolbarComponentItem.validator(context),
            // metadata properties
            ToolbarComponentItem.hideZeroValues(context),
            ToolbarComponentItem.showFormulaEditor(context),
            ToolbarComponentItem.showHeadings(context),
            ToolbarComponentItem.showFormulas(context),
            ToolbarComponentItem.showGridLines(context),
            ToolbarComponentItem.autoHideScrollbars(context),
            // finding/sorting/highlighting
            ToolbarComponentItem.findCells(context),
            ToolbarComponentItem.highlightCells(context),
            // SORT
            ToolbarComponentItem.sort(context),
            // label
            ToolbarComponentItem.labelCreate(context),
            ToolbarComponentItem.labelList(context),
            // reload
            ToolbarComponentItem.reload(context),
            // plugin
            ToolbarComponentItem.plugin(context),
            // swagger
            ToolbarComponentItem.swagger(context)
        );
    }

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return false; // ask individual components
    }

    // SpreadsheetDeltaFetcherWatcher..........................................................................................

    @Override
    public void onSpreadsheetDelta(final HttpMethod method,
                                   final AbsoluteOrRelativeUrl url,
                                   final SpreadsheetDelta delta) {
        this.flexLayout.refreshChildrenIfOpen(this.context);
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

    private final ToolbarComponentContext context;

    // HistoryTokenAwareComponentLifecycle..............................................................................................

    @Override
    public boolean isOpen() {
        return this.open;
    }

    @Override
    public void open(final RefreshContext context) {
        this.open = true;
    }

    @Override
    public void close(final RefreshContext context) {
        this.open = false;
    }

    private boolean open;

    @Override
    public void refresh(final RefreshContext context) {

    }

    @Override
    public void openGiveFocus(final RefreshContext context) {
        // nop
    }

    @Override
    public boolean shouldLogLifecycleChanges() {
        return SPREADSHEET_TOOLBAR_COMPONENT; // child items return false
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
    

    final static String TOOLBAR_ID_PREFIX = "toolbar-";

    final static String AUTO_HIDE_SCROLL_BARS_ID = TOOLBAR_ID_PREFIX + "autoHideScrollbars";

    final static String CURRENCY_ID = TOOLBAR_ID_PREFIX + "currency";

    final static String DATE_TIME_SYMBOLS_ID = TOOLBAR_ID_PREFIX + "dateTimeSymbols";

    final static String DECIMAL_NUMBER_SYMBOLS_ID = TOOLBAR_ID_PREFIX + "decimalNumberSymbols";

    final static String FIND_CELLS_ID = TOOLBAR_ID_PREFIX + "findCells";

    final static String FIND_HIGHLIGHT_ID = TOOLBAR_ID_PREFIX + "findHighlight";

    final static String FORMATTER_ID = TOOLBAR_ID_PREFIX + "formatter";

    final static String HIDE_ZERO_VALUES_ID = TOOLBAR_ID_PREFIX + "hideZeroValues";

    final static String LABEL_CREATE_ID = TOOLBAR_ID_PREFIX + "labelCreate";

    final static String LABEL_LIST_ID = TOOLBAR_ID_PREFIX + "labelList";

    final static String LOCALE_ID = TOOLBAR_ID_PREFIX + "locale";

    final static String PARSER_ID = TOOLBAR_ID_PREFIX + "parser";

    final static String PLUGIN_ID = TOOLBAR_ID_PREFIX + "plugin";

    final static String RELOAD_ID = TOOLBAR_ID_PREFIX + "reload";

    final static String SHOW_FORMULAS_ID = TOOLBAR_ID_PREFIX + "showFormulas";

    final static String SHOW_FORMULA_EDITOR_ID = TOOLBAR_ID_PREFIX + "showFormulaEditor";

    final static String SHOW_GRID_LINES_ID = TOOLBAR_ID_PREFIX + "showGridLines";

    final static String SHOW_HEADINGS_ID = TOOLBAR_ID_PREFIX + "showHeadings";

    final static String SORT_ID = TOOLBAR_ID_PREFIX + "sort";

    final static String SWAGGER_ID = TOOLBAR_ID_PREFIX + "swagger";

    final static String VALIDATOR_ID = TOOLBAR_ID_PREFIX + "validator";
}
