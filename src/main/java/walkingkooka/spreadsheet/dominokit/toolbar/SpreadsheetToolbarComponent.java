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
import walkingkooka.spreadsheet.dominokit.AppContext;
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
public final class SpreadsheetToolbarComponent implements HtmlComponentDelegator<HTMLDivElement, SpreadsheetToolbarComponent>,
    SpreadsheetViewportComponentLifecycle,
    LoadedSpreadsheetMetadataRequired,
    NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher,
    SpreadsheetDeltaFetcherWatcher {

    public static SpreadsheetToolbarComponent with(final SpreadsheetToolbarComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetToolbarComponent(context);
    }

    private SpreadsheetToolbarComponent(final SpreadsheetToolbarComponentContext context) {
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
    private FlexLayoutComponent createFlexLayout(final SpreadsheetToolbarComponentContext context) {
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
    private static List<SpreadsheetToolbarComponentItem<?>> components(final SpreadsheetToolbarComponentContext context) {
        return Lists.of(
            // style
            SpreadsheetToolbarComponentItem.bold(context),
            SpreadsheetToolbarComponentItem.italics(context),
            SpreadsheetToolbarComponentItem.strikeThru(context),
            SpreadsheetToolbarComponentItem.underline(context),
            // text
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
            // format/pattern
            SpreadsheetToolbarComponentItem.formatter(context),
            SpreadsheetToolbarComponentItem.dateTimeSymbols(context),
            SpreadsheetToolbarComponentItem.parser(context),
            SpreadsheetToolbarComponentItem.decimalNumberSymbols(context),
            // clear
            SpreadsheetToolbarComponentItem.clearStyle(context),
            // validator
            SpreadsheetToolbarComponentItem.validator(context),
            // metadata properties
            SpreadsheetToolbarComponentItem.hideZeroValues(context),
            SpreadsheetToolbarComponentItem.showFormulaEditor(context),
            SpreadsheetToolbarComponentItem.showHeadings(context),
            SpreadsheetToolbarComponentItem.showFormulas(context),
            SpreadsheetToolbarComponentItem.showGridLines(context),
            SpreadsheetToolbarComponentItem.autoHideScrollbars(context),
            // finding/sorting/highlighting
            SpreadsheetToolbarComponentItem.findCells(context),
            SpreadsheetToolbarComponentItem.highlightCells(context),
            // SORT
            SpreadsheetToolbarComponentItem.sort(context),
            // label
            SpreadsheetToolbarComponentItem.labelCreate(context),
            SpreadsheetToolbarComponentItem.labelList(context),
            // reload
            SpreadsheetToolbarComponentItem.reload(context),
            // plugin
            SpreadsheetToolbarComponentItem.plugin(context),
            // swagger
            SpreadsheetToolbarComponentItem.swagger(context)
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
                                   final SpreadsheetDelta delta,
                                   final AppContext context) {
        this.flexLayout.refreshChildrenIfOpen(context);
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

    private final SpreadsheetToolbarComponentContext context;

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

    public static String autoHideScrollbarsId() {
        return TOOLBAR_ID_PREFIX + "auto-hide-scrollbars";
    }

    public static String dateTimeSymbolsId() {
        return TOOLBAR_ID_PREFIX + "date-time-symbols";
    }

    public static String decimalNumberSymbolsId() {
        return TOOLBAR_ID_PREFIX + "decimal-number-symbols";
    }

    public static String findCellsId() {
        return TOOLBAR_ID_PREFIX + "find-cells";
    }

    public static String findHighlightId() {
        return TOOLBAR_ID_PREFIX + "find-highlight";
    }

    public static String formatterId() {
        return TOOLBAR_ID_PREFIX + "formatter";
    }

    public static String hideZeroValues() {
        return TOOLBAR_ID_PREFIX + "hide-zero-values";
    }

    public static String labelCreateId() {
        return TOOLBAR_ID_PREFIX + "label-create";
    }

    public static String labelListId() {
        return TOOLBAR_ID_PREFIX + "label-list";
    }

    public static String parserId() {
        return TOOLBAR_ID_PREFIX + "parser";
    }

    public static String pluginId() {
        return TOOLBAR_ID_PREFIX + "plugin";
    }

    public static String reloadId() {
        return TOOLBAR_ID_PREFIX + "reload";
    }

    public static String showFormulasId() {
        return TOOLBAR_ID_PREFIX + "show-formulas";
    }

    public static String showFormulaEditorId() {
        return TOOLBAR_ID_PREFIX + "show-formula-editor";
    }

    public static String showGridLinesIds() {
        return TOOLBAR_ID_PREFIX + "show-grid-lines";
    }

    public static String showHeadingsId() {
        return TOOLBAR_ID_PREFIX + "show-headings";
    }

    public static String sortId() {
        return TOOLBAR_ID_PREFIX + "sort";
    }

    public static String swaggerId() {
        return TOOLBAR_ID_PREFIX + "swagger";
    }

    public static String validatorId() {
        return TOOLBAR_ID_PREFIX + "validator";
    }

    final static String TOOLBAR_ID_PREFIX = "toolbar-";
}
