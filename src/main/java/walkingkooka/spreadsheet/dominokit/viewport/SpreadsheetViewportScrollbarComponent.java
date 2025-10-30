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

package walkingkooka.spreadsheet.dominokit.viewport;

import elemental2.dom.Event;
import elemental2.dom.EventListener;
import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.Cast;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.dom.HtmlStyledComponent;
import walkingkooka.spreadsheet.dominokit.flex.FlexLayoutComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.slider.SliderComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReferenceKind;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetReferenceKind;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportHomeNavigationList;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportNavigation;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportNavigationList;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Objects;
import java.util.Optional;

/**
 * Both the horizontal and vertical scrollbar for the viewport.
 * Two links one before and one after the slider are included, with the before link containing either LEFT or UP text,
 * and the after link containing RIGHT or DOWN text.
 * <pre>
 * #/1/SpreadsheetName/cell/B2/navigate/A1/up 1000px
 * #/1/SpreadsheetName/cell/B2/navigate/A1/down 1000px
 * </pre>
 * The {@link SliderComponent#setStep(double)} will be set to match the visible column/row count of the viewport.
 */
abstract public class SpreadsheetViewportScrollbarComponent<R extends SpreadsheetColumnOrRowReference>
    implements ValueComponent<HTMLDivElement, R, SpreadsheetViewportScrollbarComponent<R>>,
    SpreadsheetViewportComponentLifecycle,
    HtmlComponentDelegator<HTMLDivElement, SpreadsheetViewportScrollbarComponent<R>>,
    HtmlStyledComponent<SpreadsheetViewportScrollbarComponent<R>> {

    public static SpreadsheetViewportScrollbarComponent<SpreadsheetColumnReference> columns(final SpreadsheetViewportScrollbarComponentContext context) {
        return SpreadsheetViewportScrollbarComponentColumns.with(context);
    }

    public static SpreadsheetViewportScrollbarComponent<SpreadsheetRowReference> rows(final SpreadsheetViewportScrollbarComponentContext context) {
        return SpreadsheetViewportScrollbarComponentRows.with(context);
    }

    SpreadsheetViewportScrollbarComponent(final FlexLayoutComponent layout,
                                          final SpreadsheetViewportScrollbarComponentContext context) {
        super();

        Objects.requireNonNull(context, "context");

        context.addHistoryTokenWatcher(this);
        this.context = context;

        final SpreadsheetColumnOrRowReferenceKind columnOrRow = this.referenceKind();
        final boolean isHorizontal = SpreadsheetColumnOrRowReferenceKind.COLUMN == columnOrRow;

        final String idPrefix = SpreadsheetViewportComponent.ID_PREFIX +
            (isHorizontal ? "horizontal" : "vertical") +
            "-scrollbar";

        this.layout = layout.setId(idPrefix + "-Layout");
        this.before = HistoryTokenAnchorComponent.empty()
            .setId(
                idPrefix +
                    "-" +
                    (isHorizontal ? "left" : "up") +
                    SpreadsheetElementIds.LINK
            );
        this.slider = this.createSlider()
            .setId(idPrefix + "-value" + SpreadsheetElementIds.SLIDER)
            .setFlex("1")
            .setMargin("0");

        if(this instanceof SpreadsheetViewportScrollbarComponentColumns) {
            this.slider.setMarginTop("-7px");
        }

        this.after = HistoryTokenAnchorComponent.empty()
            .setId(
                idPrefix +
                    "-" +
                    (isHorizontal ? "right" : "down") +
                    SpreadsheetElementIds.LINK
            );

        layout.appendChild(this.before)
            .appendChild(this.slider)
            .appendChild(this.after)
            .addMouseEnter(this::onMouseEnter)
            .addMouseOut(this::onMouseOut);
    }

    /**
     * Factory that creates the slider component, without setting of its min and max
     */
    abstract SliderComponent createSlider();

    private void onMouseEnter(final Event event) {
        this.mouseEnter = true;

        if (this.context.autoHideScrollbars()) {
            this.makeOpaque();
        }
    }

    private void onMouseOut(final Event event) {
        this.mouseEnter = false;

        if (this.context.autoHideScrollbars()) {
            this.makeTransparent();
        }
    }

    // SpreadsheetViewportComponent
    void setAutoHideScrollbars(final boolean autoHideScrollbars) {
        if (autoHideScrollbars) {
            if (this.mouseEnter) {
                this.makeOpaque();
            } else {
                this.makeTransparent();
            }
        } else {
            this.makeOpaque();
        }
    }

    private boolean mouseEnter;

    @Override
    public boolean isEditing() {
        return false;
    }

    // HtmlComponentDelegator...........................................................................................

    @Override
    public final HtmlComponent<HTMLDivElement, ?> htmlComponent() {
        return this.layout;
    }

    @Override
    public final SpreadsheetViewportScrollbarComponent<R> setCssProperty(final String name,
                                                                         final String value) {
        this.layout.setCssProperty(name, value);
        return this;
    }

    // ValueComponent...................................................................................................

    @Override
    public final SpreadsheetViewportScrollbarComponent<R> setId(final String id) {
        this.layout.setId(id);
        return this;
    }

    @Override
    public final String id() {
        return this.layout.id();
    }

    @Override
    public final boolean isDisabled() {
        return this.slider.isDisabled();
    }

    @Override
    public final SpreadsheetViewportScrollbarComponent<R> setDisabled(final boolean disabled) {
        this.before.setDisabled(disabled);
        this.slider.setDisabled(disabled);
        this.after.setDisabled(disabled);
        return this;
    }

    @Override
    public SpreadsheetViewportScrollbarComponent<R> setValue(final Optional<R> value) {
        Objects.requireNonNull(value, "value");

        this.slider.setValue(
            value.map(
                this::referenceToDouble
            )
        );
        return this;
    }

    @Override
    public Optional<R> value() {
        return this.slider.value()
            .map(this::doubleToReference);
    }

    @Override
    public final SpreadsheetViewportScrollbarComponent<R> addBlurListener(final EventListener listener) {
       throw new UnsupportedOperationException();
    }

    @Override
    public final SpreadsheetViewportScrollbarComponent<R> addClickListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final SpreadsheetViewportScrollbarComponent<R> addChangeListener(final ChangeListener<Optional<R>> listener) {
        this.slider.addChangeListener(
            (Optional<Double> oldValue, Optional<Double> newValue) -> listener.onValueChanged(
                oldValue.map(this::doubleToReference),
                newValue.map(this::doubleToReference)
            )
        );
        return this;
    }

    @Override
    public final SpreadsheetViewportScrollbarComponent<R> addContextMenuListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final SpreadsheetViewportScrollbarComponent<R> addFocusListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final SpreadsheetViewportScrollbarComponent<R> addKeyDownListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final SpreadsheetViewportScrollbarComponent<R> addKeyUpListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final SpreadsheetViewportScrollbarComponent<R> hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final SpreadsheetViewportScrollbarComponent<R> removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final SpreadsheetViewportScrollbarComponent<R> removePadding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final SpreadsheetViewportScrollbarComponent<R> focus() {
        return this;
    }

    private Double referenceToDouble(final R reference) {
        return Double.valueOf(
            this.referenceKind().value(
                (SpreadsheetSelection) reference
            )
        );
    }

    private R doubleToReference(final Double value) {
        return (R) this.referenceKind()
            .setValue(
                SpreadsheetReferenceKind.RELATIVE,
                value.intValue()
            );
    }

    abstract SpreadsheetColumnOrRowReferenceKind referenceKind();

    // TreePrintable....................................................................................................

    @Override
    public final void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.layout.printTree(printer);
        }
        printer.outdent();
    }

    final FlexLayoutComponent layout;

    final HistoryTokenAnchorComponent before;

    final SliderComponent slider;

    final HistoryTokenAnchorComponent after;

    // SpreadsheetViewportComponentLifecycle............................................................................

    @Override
    public void openGiveFocus(final RefreshContext context) {
        // NOP
    }

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
        this.before.clear();
        this.after.clear();
        this.open = false;
    }

    private boolean open;

    @Override
    public void refresh(final RefreshContext context) {
        final SpreadsheetCellReference home = this.spreadsheetMetadata()
            .getOrFail(SpreadsheetMetadataPropertyName.VIEWPORT_HOME);

        final boolean columns = this.referenceKind() == SpreadsheetColumnOrRowReferenceKind.COLUMN;

        final SpreadsheetViewportScrollbarComponentContext viewportContext = this.context;
        final int width = viewportContext.viewportGridWidth();
        final int height = viewportContext.viewportGridHeight();

        final HistoryToken historyToken = context.historyToken();

        this.before.setHistoryToken(
            Optional.of(
                historyToken.setNavigation(
                    Optional.of(
                        SpreadsheetViewportHomeNavigationList.with(home)
                            .setNavigations(
                                SpreadsheetViewportNavigationList.EMPTY.concat(
                                    columns ?
                                        SpreadsheetViewportNavigation.scrollLeft(width) :
                                        SpreadsheetViewportNavigation.scrollUp(height)
                                )
                            )
                    )
                )
            )
        );

        this.setValue(
            Cast.to(
                Optional.of(
                    columns ?
                        home.column() :
                        home.row()
                )
            )
        );

        this.after.setHistoryToken(
            Optional.of(
                historyToken.setNavigation(
                    Optional.of(
                        SpreadsheetViewportHomeNavigationList.with(home)
                            .setNavigations(
                                SpreadsheetViewportNavigationList.EMPTY.concat(
                                    columns ?
                                        SpreadsheetViewportNavigation.scrollRight(width) :
                                        SpreadsheetViewportNavigation.scrollDown(height)
                                )
                            )
                    )
                )
            )
        );
    }

    @Override
    public SpreadsheetViewportCache spreadsheetViewportCache() {
        return this.context.spreadsheetViewportCache();
    }

    @Override
    public SpreadsheetMetadata spreadsheetMetadata() {
        return this.context.spreadsheetMetadata();
    }

    @Override
    public boolean shouldLogLifecycleChanges() {
        return SPREADSHEET_VIEWPORT_SCROLLBAR_COMPONENT;
    }

    private final SpreadsheetViewportScrollbarComponentContext context;
}
